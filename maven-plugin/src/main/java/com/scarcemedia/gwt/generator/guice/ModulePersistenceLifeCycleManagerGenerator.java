/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.generator.guice;

import com.scarcemedia.gwt.generator.AbstractGenerator;
import com.scarcemedia.gwt.generator.Settings;
import com.scarcemedia.gwt.generator.definition.Definition;
import com.scarcemedia.gwt.generator.definition.Model;
import com.scarcemedia.gwt.generator.definition.PackageDefinition;
import com.scarcemedia.gwt.generator.util.AnnotationHelper;
import com.scarcemedia.gwt.generator.util.ImportHelper;
import com.scarcemedia.gwt.generator.util.NameHelper;
import japa.parser.ASTHelper;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.ThisExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.VoidType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jeremy
 */
public class ModulePersistenceLifeCycleManagerGenerator extends AbstractGenerator {

  public ModulePersistenceLifeCycleManagerGenerator(Settings settings, Definition definition, PackageDefinition packageDefinition, Model model) {
    super(settings, definition, packageDefinition, model);
  }
  ClassOrInterfaceDeclaration typeDeclare;

  FieldDeclaration fieldPersistService;
  FieldDeclaration fieldUnitOfWork;
  FieldAccessExpr fieldUnitOfWorkRef;
  FieldAccessExpr fieldPersistServiceRef;
  final String persistService = "persistService";
  final String unitofwork = "unitOfWork";
  String persistLifecycleManagerName;

  ClassOrInterfaceType persistServiceType = new ClassOrInterfaceType("PersistService");
  ClassOrInterfaceType unitOfWorkType = new ClassOrInterfaceType("UnitOfWork");

  @Override
  protected void onGenerate() {
    persistLifecycleManagerName = NameHelper.getPersistLifecycleManagerName(packageDefinition);

    typeDeclare = new ClassOrInterfaceDeclaration();

    typeDeclare.setImplements(new ArrayList<ClassOrInterfaceType>());
    typeDeclare.getImplements().add(new ClassOrInterfaceType("PersistenceLifeCycleManager"));
    ASTHelper.addTypeDeclaration(compileUnit, typeDeclare);
    typeDeclare.setModifiers(ModifierSet.PUBLIC);
    typeDeclare.setName(persistLifecycleManagerName);
    typeDeclare.setMembers(new ArrayList<BodyDeclaration>());

    ImportHelper.addImport(compileUnit, new NameExpr("com.google.inject.persist.PersistService"));
    ImportHelper.addImport(compileUnit, new NameExpr("com.google.inject.persist.UnitOfWork"));

    fieldPersistService = new FieldDeclaration();
    fieldPersistService.setModifiers(ModifierSet.FINAL);
    fieldPersistService.setVariables(new ArrayList<VariableDeclarator>());
    fieldPersistService.setType(persistServiceType);
    fieldPersistService.getVariables().add(new VariableDeclarator(new VariableDeclaratorId(persistService)));
    ASTHelper.addMember(typeDeclare, fieldPersistService);

    fieldUnitOfWork = new FieldDeclaration();
    fieldUnitOfWork.setModifiers(ModifierSet.FINAL);
    fieldUnitOfWork.setVariables(new ArrayList<VariableDeclarator>());
    fieldUnitOfWork.setType(unitOfWorkType);
    fieldUnitOfWork.getVariables().add(new VariableDeclarator(new VariableDeclaratorId(unitofwork)));
    ASTHelper.addMember(typeDeclare, fieldUnitOfWork);

    fieldUnitOfWorkRef = new FieldAccessExpr(new ThisExpr(), unitofwork);
    fieldPersistServiceRef = new FieldAccessExpr(new ThisExpr(), persistService);

    addConstructor();

//    typeDeclare.setInterface(true);
    Map<String, String> methods = new HashMap<String, String>();
    methods.put("startService", "start");
    methods.put("stopService", "stop");

    for (Map.Entry<String, String> kvp : methods.entrySet()) {
      MethodDeclaration methodStub = addMethodStub(kvp.getKey());
      MethodCallExpr callMethod = new MethodCallExpr(fieldPersistServiceRef, kvp.getValue());
      ASTHelper.addStmt(methodStub.getBody(), callMethod);
    }

    methods = new HashMap<String, String>();
    methods.put("beginUnitOfWork", "begin");
    methods.put("endUnitOfWork", "end");

    for (Map.Entry<String, String> kvp : methods.entrySet()) {
      MethodDeclaration methodStub = addMethodStub(kvp.getKey());
      MethodCallExpr callMethod = new MethodCallExpr(fieldUnitOfWorkRef, kvp.getValue());
      ASTHelper.addStmt(methodStub.getBody(), callMethod);
    }
  }

  MethodDeclaration addMethodStub(String methodName) {
    MethodDeclaration methodDeclare = new MethodDeclaration();
    methodDeclare.setModifiers(ModifierSet.PUBLIC);
    methodDeclare.setBody(new BlockStmt(new ArrayList<Statement>()));
    AnnotationHelper.addOverrideAnnotation(methodDeclare, compileUnit);
    methodDeclare.setName(methodName);
    methodDeclare.setType(new VoidType());

    ASTHelper.addMember(typeDeclare, methodDeclare);
    return methodDeclare;
  }

  @Override
  protected String getPackageName() {
    return settings.getGuicePersistPackage();
  }

  private void addConstructor() {
    ConstructorDeclaration constructor = new ConstructorDeclaration(ModifierSet.PUBLIC, persistLifecycleManagerName);
    constructor.setBlock(new BlockStmt(new ArrayList<Statement>()));
    AnnotationHelper.addInjectAnnotation(constructor, compileUnit);
    ASTHelper.addMember(typeDeclare, constructor);
    constructor.setParameters(new ArrayList<Parameter>());
    constructor.getParameters().add(new Parameter(unitOfWorkType, new VariableDeclaratorId(unitofwork)));
    constructor.getParameters().add(new Parameter(persistServiceType, new VariableDeclaratorId(persistService)));

    ASTHelper.addStmt(constructor.getBlock(), new AssignExpr(fieldUnitOfWorkRef, new NameExpr(unitofwork), AssignExpr.Operator.assign));
    ASTHelper.addStmt(constructor.getBlock(), new AssignExpr(fieldPersistServiceRef, new NameExpr(persistService), AssignExpr.Operator.assign));

  }
}

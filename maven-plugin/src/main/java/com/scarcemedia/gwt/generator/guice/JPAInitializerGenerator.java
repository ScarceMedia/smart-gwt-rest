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
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.type.ClassOrInterfaceType;
import java.util.ArrayList;

/**
 *
 * @author jeremy
 */
public class JPAInitializerGenerator extends AbstractGenerator {

  public JPAInitializerGenerator(Settings settings, Definition definition, PackageDefinition packageDefinition, Model model) {
    super(settings, definition, packageDefinition, model);
  }
  ClassOrInterfaceDeclaration typeDeclare;

  NameExpr moduleName;

  @Override
  protected void onGenerate() {
    String persistModuleName = NameHelper.getJPAInitializerName(packageDefinition);
    typeDeclare = new ClassOrInterfaceDeclaration();
    ASTHelper.addTypeDeclaration(compileUnit, typeDeclare);
    typeDeclare.setModifiers(ModifierSet.PUBLIC);
    typeDeclare.setName(persistModuleName);
    typeDeclare.setMembers(new ArrayList<BodyDeclaration>());
    AnnotationHelper.addSingletonAnnotation(typeDeclare, compileUnit);
    
    ImportHelper.addImport(compileUnit, new NameExpr("com.google.inject.persist.PersistService"));
    
    ConstructorDeclaration constructor = new ConstructorDeclaration(ModifierSet.PUBLIC, persistModuleName);
    constructor.setParameters(new ArrayList<Parameter>());
    constructor.setBlock(new BlockStmt(new ArrayList<Statement>()));
    AnnotationHelper.addInjectAnnotation(constructor, compileUnit);
    ASTHelper.addMember(typeDeclare, constructor);

    NameExpr persistService = new NameExpr("persistService");
    
    Parameter persistServiceParameter = new Parameter(new ClassOrInterfaceType("PersistService"), new VariableDeclaratorId(persistService.getName()));
    constructor.getParameters().add(persistServiceParameter);
    MethodCallExpr callFoo = new MethodCallExpr(persistService, "start");
    ASTHelper.addStmt(constructor.getBlock(), callFoo);
  }

  @Override
  protected String getPackageName() {
    return settings.getGuicePersistPackage();
  }
}

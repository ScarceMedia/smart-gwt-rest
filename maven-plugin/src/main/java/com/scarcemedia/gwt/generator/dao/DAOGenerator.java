/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.generator.dao;

import com.scarcemedia.gwt.generator.Settings;
import com.scarcemedia.gwt.generator.definition.Definition;
import com.scarcemedia.gwt.generator.definition.Field;
import com.scarcemedia.gwt.generator.definition.Model;
import com.scarcemedia.gwt.generator.definition.PackageDefinition;
import com.scarcemedia.gwt.generator.model.ModelGenerator;
import com.scarcemedia.gwt.generator.util.AnnotationHelper;
import com.scarcemedia.gwt.generator.util.ImportHelper;
import com.scarcemedia.gwt.generator.util.NameHelper;
import japa.parser.ASTHelper;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.BinaryExpr;
import japa.parser.ast.expr.ClassExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.NullLiteralExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.ReturnStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.stmt.ThrowStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.Type;
import japa.parser.ast.type.VoidType;
import java.util.ArrayList;

/**
 *
 * @author jeremy
 */
public class DAOGenerator extends ModelGenerator {

  final boolean generateInterface;

  public DAOGenerator(Settings settings, Definition definition, PackageDefinition packageDefinition, Model model, boolean generateInterface) {
    super(settings, definition, packageDefinition, model);
    this.generateInterface = generateInterface;
  }
  ClassOrInterfaceDeclaration typeDeclare;
  ReferenceType modelType;
  Parameter itemParameter;
  NameExpr itemExpr;
  NameExpr entityManagerProviderExpr;
  NameExpr mapperExpr;

  @Override
  protected void onGenerate() {
    String interfaceName = NameHelper.getDAOInterfaceName(model);
    String implName = NameHelper.getDAOImplName(model);
    itemExpr = new NameExpr("item");
    entityManagerProviderExpr = new NameExpr("entityManagerProvider");
    mapperExpr = new NameExpr("mapper");

    typeDeclare = new ClassOrInterfaceDeclaration(ModifierSet.PUBLIC, generateInterface, generateInterface ? interfaceName : implName);
    compileUnit.getTypes().add(typeDeclare);

    String modelName = NameHelper.getModelName(model);
    NameExpr qualifiedModel = new QualifiedNameExpr(new NameExpr(packageDefinition.getName()), modelName);
    ImportHelper.addImport(compileUnit, qualifiedModel);

    modelType = ASTHelper.createReferenceType(modelName, 0);
    itemParameter = new Parameter(modelType, new VariableDeclaratorId(itemExpr.getName()));


    if (!generateInterface) {
      ClassOrInterfaceType providerType = new ClassOrInterfaceType("Provider");
      providerType.setTypeArgs(new ArrayList<Type>());
      providerType.getTypeArgs().add(new ClassOrInterfaceType("EntityManager"));
      FieldDeclaration providerField = new FieldDeclaration();
      providerField.setType(providerType);
      providerField.setVariables(new ArrayList<VariableDeclarator>());
      providerField.getVariables().add(new VariableDeclarator(new VariableDeclaratorId(entityManagerProviderExpr.getName())));
      AnnotationHelper.addInjectAnnotation(providerField, compileUnit);
      ASTHelper.addMember(typeDeclare, providerField);

      FieldDeclaration mapperField = new FieldDeclaration();
      mapperField.setType(ASTHelper.createReferenceType("Mapper", 0));
      mapperField.setVariables(new ArrayList<VariableDeclarator>());
      mapperField.getVariables().add(new VariableDeclarator(new VariableDeclaratorId(mapperExpr.getName())));
      AnnotationHelper.addInjectAnnotation(mapperField, compileUnit);
      ASTHelper.addMember(typeDeclare, mapperField);
    }


    addMethod();
    updateMethod();
    removeMethod();

    if (generateInterface) {
      return;
    }
    NameExpr preconditionsImport = new NameExpr("com.google.common.base.Preconditions");
    ImportHelper.addImport(compileUnit, preconditionsImport);
    ImportHelper.addImport(compileUnit, new NameExpr("javax.persistence.EntityManager"));
    ImportHelper.addImport(compileUnit, new NameExpr("com.google.inject.Provider"));
    ImportHelper.addImport(compileUnit, new NameExpr("org.dozer.Mapper"));

    typeDeclare.setImplements(new ArrayList<ClassOrInterfaceType>());
    typeDeclare.getImplements().add(new ClassOrInterfaceType(interfaceName));
  }

  NameExpr addEntityManager(MethodDeclaration method) {
    NameExpr varName = new NameExpr("em");
    VariableDeclarationExpr expr = new VariableDeclarationExpr();
    expr.setType(new ClassOrInterfaceType("EntityManager"));
    expr.setVars(new ArrayList<VariableDeclarator>());
    expr.getVars().add(new VariableDeclarator(new VariableDeclaratorId(varName.getName()), new MethodCallExpr(entityManagerProviderExpr, "get")));
    ASTHelper.addStmt(method.getBody(), expr);
    return varName;
  }

  NameExpr findExisting(MethodDeclaration method, NameExpr varEm, Field primaryKey) {
    String primaryKeyMethod = NameHelper.getGetterMethodName(primaryKey);
    MethodCallExpr callPrimaryKey = new MethodCallExpr(itemExpr, primaryKeyMethod);
    return findExisting(method, varEm, callPrimaryKey);
  }

  NameExpr findExisting(MethodDeclaration method, NameExpr varEm, Expression primaryKeyExpression) {
    NameExpr varName = new NameExpr("existing");
    ;

    MethodCallExpr callFind = new MethodCallExpr(varEm, "find");
    ASTHelper.addArgument(callFind, new ClassExpr(modelType));
    ASTHelper.addArgument(callFind, primaryKeyExpression);

    VariableDeclarationExpr expr = new VariableDeclarationExpr();
    expr.setType(modelType);
    expr.setVars(new ArrayList<VariableDeclarator>());
    expr.getVars().add(new VariableDeclarator(new VariableDeclaratorId(varName.getName()), callFind));
    ASTHelper.addStmt(method.getBody(), expr);

    return varName;
  }

  static void addItemNullCheck(MethodDeclaration method, NameExpr nameExpr) {
    MethodCallExpr callcheckArgument = new MethodCallExpr(new NameExpr("Preconditions"), "checkArgument");
    ASTHelper.addArgument(callcheckArgument, new BinaryExpr(new NullLiteralExpr(), nameExpr, BinaryExpr.Operator.notEquals));
    ASTHelper.addArgument(callcheckArgument, new StringLiteralExpr(nameExpr.getName() + " cannot be null."));

    if (null == method.getBody()) {
      method.setBody(new BlockStmt());
    }

    if (null == method.getBody().getStmts()) {
      method.getBody().setStmts(new ArrayList<Statement>());
    }

    ASTHelper.addStmt(method.getBody(), callcheckArgument);

  }

  void addMethod() {
    MethodDeclaration addMethod = new MethodDeclaration();
    ASTHelper.addMember(typeDeclare, addMethod);
    addMethod.setName("add");
    addMethod.setType(modelType);
    addMethod.setParameters(new ArrayList<Parameter>());
    addMethod.getParameters().add(itemParameter);

    if (generateInterface) {
      return;
    }

    addMethod.setModifiers(ModifierSet.PUBLIC);
    addMethod.setBody(new BlockStmt());
    addItemNullCheck(addMethod, itemExpr);
    AnnotationHelper.addOverrideAnnotation(addMethod, compileUnit);
    AnnotationHelper.addTransactionalAnnotation(addMethod, compileUnit);
    NameExpr varEm = addEntityManager(addMethod);

    MethodCallExpr callPersist = new MethodCallExpr(varEm, "persist");
    ASTHelper.addArgument(callPersist, itemExpr);
    ASTHelper.addStmt(addMethod.getBody(), callPersist);
    ASTHelper.addStmt(addMethod.getBody(), new MethodCallExpr(varEm, "flush"));
    ASTHelper.addStmt(addMethod.getBody(), new ReturnStmt(itemExpr));

  }

  void updateMethod() {
    MethodDeclaration updateMethod = new MethodDeclaration();
    ASTHelper.addMember(typeDeclare, updateMethod);
    updateMethod.setName("update");
    updateMethod.setType(modelType);
    updateMethod.setParameters(new ArrayList<Parameter>());

    Field primaryKey = model.getPrimaryKey();

    updateMethod.getParameters().add(itemParameter);

    if (generateInterface) {
      return;
    }

    updateMethod.setModifiers(ModifierSet.PUBLIC);
    updateMethod.setBody(new BlockStmt());
    addItemNullCheck(updateMethod, itemExpr);
    AnnotationHelper.addOverrideAnnotation(updateMethod, compileUnit);
    AnnotationHelper.addTransactionalAnnotation(updateMethod, compileUnit);
    NameExpr varEm = addEntityManager(updateMethod);

    if (null == primaryKey) {
      addUnsupportedOperationException(updateMethod.getBody(), "Update is not supported on tables without a primary key.");
      return;
    }

    NameExpr varExisting = findExisting(updateMethod, varEm, primaryKey);

    MethodCallExpr mapMethodCall = new MethodCallExpr(mapperExpr, "map");
    ASTHelper.addArgument(mapMethodCall, itemExpr);
    ASTHelper.addArgument(mapMethodCall, varExisting);
    ASTHelper.addStmt(updateMethod.getBody(), mapMethodCall);

    MethodCallExpr callPersist = new MethodCallExpr(varEm, "persist");
    ASTHelper.addArgument(callPersist, itemExpr);
    ASTHelper.addStmt(updateMethod.getBody(), callPersist);

    ASTHelper.addStmt(updateMethod.getBody(), new ReturnStmt(itemExpr));
  }

  void addUnsupportedOperationException(BlockStmt statements, String message) {
    ObjectCreationExpr createNotsupported = new ObjectCreationExpr();
    createNotsupported.setType(new ClassOrInterfaceType("UnsupportedOperationException"));

    createNotsupported.setArgs(new ArrayList<Expression>());
    createNotsupported.getArgs().add(new StringLiteralExpr(message));

    ASTHelper.addStmt(statements, new ThrowStmt(createNotsupported));
  }

  void removeMethod() {
    MethodDeclaration removeMethod = new MethodDeclaration();
    ASTHelper.addMember(typeDeclare, removeMethod);
    removeMethod.setName("remove");
    removeMethod.setType(new VoidType());
    removeMethod.setParameters(new ArrayList<Parameter>());

    Field primaryKey = model.getPrimaryKey();

    NameExpr varPrimaryKey;
    Parameter parameter;


    if (null != primaryKey) {
      String argumentName = NameHelper.getVariableName(primaryKey);
      varPrimaryKey = new NameExpr(argumentName);
      parameter = new Parameter(getFieldType(primaryKey), new VariableDeclaratorId(argumentName));
    } else {
      varPrimaryKey = itemExpr;
      parameter = itemParameter;
    }


    removeMethod.getParameters().add(parameter);

    if (generateInterface) {
      return;
    }

    removeMethod.setModifiers(ModifierSet.PUBLIC);
    removeMethod.setBody(new BlockStmt());

    if (null == primaryKey) {
      addUnsupportedOperationException(removeMethod.getBody(), "Remove is not supported on tables without a primary key.");
      return;
    }

    addItemNullCheck(removeMethod, varPrimaryKey);
    AnnotationHelper.addOverrideAnnotation(removeMethod, compileUnit);
    AnnotationHelper.addTransactionalAnnotation(removeMethod, compileUnit);
    NameExpr varEm = addEntityManager(removeMethod);

    NameExpr varExisting = findExisting(removeMethod, varEm, varPrimaryKey);


    MethodCallExpr callRemove = new MethodCallExpr(varEm, "remove");
    ASTHelper.addArgument(callRemove, varExisting);
    ASTHelper.addStmt(removeMethod.getBody(), callRemove);
    ASTHelper.addStmt(removeMethod.getBody(), new MethodCallExpr(varEm, "flush"));
  }

  @Override
  protected String getPackageName() {
    return settings.getDAOPackage();
  }
}

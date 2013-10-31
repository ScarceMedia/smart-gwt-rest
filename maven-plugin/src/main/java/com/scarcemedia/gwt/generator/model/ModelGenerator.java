/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.generator.model;

import com.scarcemedia.gwt.generator.AbstractGenerator;
import com.scarcemedia.gwt.generator.Settings;
import com.scarcemedia.gwt.generator.definition.Definition;
import com.scarcemedia.gwt.generator.definition.Field;
import com.scarcemedia.gwt.generator.definition.FieldType;
import com.scarcemedia.gwt.generator.definition.Model;
import com.scarcemedia.gwt.generator.definition.PackageDefinition;
import com.scarcemedia.gwt.generator.definition.UniqueIndex;
import com.scarcemedia.gwt.generator.util.AnnotationHelper;
import com.scarcemedia.gwt.generator.util.ImportHelper;
import com.scarcemedia.gwt.generator.util.NameHelper;
import japa.parser.ASTHelper;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.ArrayInitializerExpr;
import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.BooleanLiteralExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.NormalAnnotationExpr;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.expr.SingleMemberAnnotationExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.expr.ThisExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.ReturnStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.VoidType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jeremy
 */
public class ModelGenerator extends AbstractGenerator {

  public ModelGenerator(Settings settings, Definition definition, PackageDefinition packageDefinition, Model model) {
    super(settings, definition, packageDefinition, model);
  }

  ClassOrInterfaceDeclaration typeDeclare;

  void addUniqueIndexes() {
    for (UniqueIndex index : model.getUniqueIndexes()) {
      ArrayInitializerExpr membersArray = new ArrayInitializerExpr();
      membersArray.setValues(new ArrayList<Expression>());

      for (String fieldName : index.getFields()) {
        Field field = model.getField(fieldName);

        if (null == field) {
          continue;
        }

        QualifiedNameExpr constantFieldReference = new QualifiedNameExpr(sharedDataClassExpr, NameHelper.getFieldNameConstant(field));
        membersArray.getValues().add(constantFieldReference);
      }
      
      NormalAnnotationExpr indexAnnotation = new NormalAnnotationExpr();
      indexAnnotation.setName(new NameExpr("Index"));
      indexAnnotation.setPairs(new ArrayList<MemberValuePair>());
      indexAnnotation.getPairs().add(new MemberValuePair("unique", new StringLiteralExpr("true")));
      indexAnnotation.getPairs().add(new MemberValuePair("members", membersArray));
      indexes.add(indexAnnotation);

    }
  }
//  
//import ;
//import ;
//  @Indices({
//  @Index(members = {AccountConstants.ACCOUNT_ID_NAME}, unique = "true")
//})

  List<Expression> indexes = new ArrayList<Expression>();

  void addIndexes() {
    addUniqueIndexes();

    if (indexes.isEmpty()) {
      return;
    }

    ImportHelper.addImport(compileUnit, new NameExpr("javax.jdo.annotations.Index"));
    ImportHelper.addImport(compileUnit, new NameExpr("javax.jdo.annotations.Indices"));

    ArrayInitializerExpr indexArray = new ArrayInitializerExpr();
    indexArray.setValues(indexes);

    SingleMemberAnnotationExpr exprTarget = new SingleMemberAnnotationExpr(new NameExpr("Indices"), indexArray);
    AnnotationHelper.addAnnotation(typeDeclare, exprTarget);
  }

  NameExpr sharedDataClassExpr;

  @Override
  protected void onGenerate() {
    typeDeclare = new ClassOrInterfaceDeclaration(ModifierSet.PUBLIC, false, NameHelper.getModelName(model));
    AnnotationHelper.addXmlTypeAnnotation(typeDeclare, compileUnit);
    AnnotationHelper.addEntityAnnotation(typeDeclare, compileUnit);
    AnnotationHelper.addPersistenceUnitAnnotation(typeDeclare, compileUnit, packageDefinition.getPersistenceUnit());
    AnnotationHelper.addTableAnnotation(typeDeclare, compileUnit, model.getTable());
    ImportHelper.addImport(compileUnit, new NameExpr(settings.getSharedPackage() + "." + NameHelper.getSharedDataClassName(model)));

    sharedDataClassExpr = new NameExpr(NameHelper.getSharedDataClassName(model));

    List<BodyDeclaration> methods = new ArrayList<BodyDeclaration>();

    for (Field field : model.getFields()) {
      String fieldName = NameHelper.getVariableName(field);
      ClassOrInterfaceType fieldType = getFieldType(field);
      FieldAccessExpr fieldAccessExpr = new FieldAccessExpr(new ThisExpr(), fieldName);

      QualifiedNameExpr constantFieldReference = new QualifiedNameExpr(sharedDataClassExpr, NameHelper.getFieldNameConstant(field));

      FieldDeclaration fieldDeclare = new FieldDeclaration(ModifierSet.PRIVATE, fieldType, new VariableDeclarator(new VariableDeclaratorId(fieldName)));
      ASTHelper.addMember(typeDeclare, fieldDeclare);

      if (field.getXmlIgnore()) {
        AnnotationHelper.addXmlTransientAnnotation(fieldDeclare, compileUnit);
      } else {
        AnnotationHelper.addXmlElementAnnotation(fieldDeclare, compileUnit, constantFieldReference);
      }
      AnnotationHelper.addColumnAnnotation(fieldDeclare, compileUnit, constantFieldReference, field.getNullable());

      if (field.getPrimaryKey()) {
        AnnotationHelper.addIdAnnotation(fieldDeclare, compileUnit);
        AnnotationHelper.addGeneratedValueAnnotation(fieldDeclare, compileUnit);
      }

      if (field.getType() == FieldType.DateTime) {
        AnnotationHelper.addTemporalTimestampAnnotation(fieldDeclare, compileUnit);
      }

      MethodDeclaration getMethod = new MethodDeclaration(ModifierSet.PUBLIC, fieldType, NameHelper.getGetterMethodName(field));
      methods.add(getMethod);
      getMethod.setBody(new BlockStmt());
      ASTHelper.addStmt(getMethod.getBody(), new ReturnStmt(fieldAccessExpr));
      AnnotationHelper.addXmlTransientAnnotation(getMethod, compileUnit);

      MethodDeclaration setMethod = new MethodDeclaration(ModifierSet.PUBLIC, new VoidType(), NameHelper.getSetterMethodName(field));
      methods.add(setMethod);
      setMethod.setBody(new BlockStmt());
      setMethod.setParameters(new ArrayList<Parameter>());
      setMethod.getParameters().add(new Parameter(fieldType, new VariableDeclaratorId("value")));
      ASTHelper.addStmt(setMethod.getBody(), new AssignExpr(fieldAccessExpr, new NameExpr("value"), AssignExpr.Operator.assign));
    }

    typeDeclare.getMembers().addAll(methods);

    addIndexes();

    compileUnit.getTypes().add(typeDeclare);

  }

  @Override
  protected String getPackageName() {
    return packageDefinition.getName();
  }
}

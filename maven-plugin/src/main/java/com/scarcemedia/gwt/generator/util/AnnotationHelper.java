/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.generator.util;

import com.google.common.base.Preconditions;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.BooleanLiteralExpr;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.NormalAnnotationExpr;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.expr.SingleMemberAnnotationExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import java.util.ArrayList;

/**
 *
 * @author jeremy
 */
//@XmlType
public class AnnotationHelper {

  /*
   import com.google.inject.Inject;
   import com.google.inject.Singleton;
   */
  private static final NameExpr injectNameExpr = new NameExpr("com.google.inject.Inject");
  private static final NameExpr singletonNameExpr = new NameExpr("com.google.inject.Singleton");
  private static final NameExpr xmlTypeNameExpr = new NameExpr("javax.xml.bind.annotation.XmlType");
  private static final NameExpr xmlElementNameExpr = new NameExpr("javax.xml.bind.annotation.XmlElement");
  private static final NameExpr xmlTransientNameExpr = new NameExpr("javax.xml.bind.annotation.XmlTransient");
  private static final NameExpr entityNameExpr = new NameExpr("javax.persistence.Entity");
  private static final NameExpr persistenceUnitNameExpr = new NameExpr("javax.persistence.PersistenceUnit");
  private static final NameExpr tableNameExpr = new NameExpr("javax.persistence.Table");
  private static final NameExpr columnNameExpr = new NameExpr("javax.persistence.Column");
  private static final NameExpr idNameExpr = new NameExpr("javax.persistence.Id");
  private static final NameExpr generatedValueNameExpr = new NameExpr("javax.persistence.GeneratedValue");
  private static final NameExpr temporalNameExpr = new NameExpr("javax.persistence.Temporal");
  private static final NameExpr temporalTypeNameExpr = new NameExpr("javax.persistence.TemporalType");
  private static final NameExpr transactionalNameExpr = new NameExpr("com.google.inject.persist.Transactional");

  public static void addAnnotation(Parameter parameter, AnnotationExpr annotation) {
    Preconditions.checkArgument(null != parameter, "parameter cannot be null.");

    if (null == parameter.getAnnotations()) {
      parameter.setAnnotations(new ArrayList<AnnotationExpr>());
    }

    parameter.getAnnotations().add(annotation);
  }

  public static void addAnnotation(BodyDeclaration bodyDeclaration, AnnotationExpr annotation) {
    Preconditions.checkArgument(null != bodyDeclaration, "bodyDeclaration cannot be null.");

    if (null == bodyDeclaration.getAnnotations()) {
      bodyDeclaration.setAnnotations(new ArrayList<AnnotationExpr>());
    }

    bodyDeclaration.getAnnotations().add(annotation);
  }

  public static AnnotationExpr addInjectAnnotation(BodyDeclaration bodyDeclaration, CompilationUnit compileUnit) {
    AnnotationExpr annotation = new NormalAnnotationExpr();
    annotation.setName(new NameExpr("Inject"));
    addAnnotation(bodyDeclaration, annotation);
    ImportHelper.addImport(compileUnit, injectNameExpr);
    return annotation;
  }

  public static AnnotationExpr addSingletonAnnotation(TypeDeclaration typeDeclare, CompilationUnit compileUnit) {
    AnnotationExpr annotation = new NormalAnnotationExpr();
    annotation.setName(new NameExpr("Singleton"));
    addAnnotation(typeDeclare, annotation);
    ImportHelper.addImport(compileUnit, singletonNameExpr);
    return annotation;
  }

  public static AnnotationExpr addXmlTypeAnnotation(TypeDeclaration typeDeclare, CompilationUnit compileUnit) {
    AnnotationExpr annotation = new NormalAnnotationExpr();
    annotation.setName(new NameExpr("XmlType"));
    addAnnotation(typeDeclare, annotation);
    ImportHelper.addImport(compileUnit, xmlTypeNameExpr);
    return annotation;
  }

  public static AnnotationExpr addXmlTransientAnnotation(BodyDeclaration typeDeclare, CompilationUnit compileUnit) {
    AnnotationExpr annotation = new NormalAnnotationExpr();
    annotation.setName(new NameExpr("XmlTransient"));
    addAnnotation(typeDeclare, annotation);
    ImportHelper.addImport(compileUnit, xmlTransientNameExpr);
    return annotation;
  }

  public static AnnotationExpr addEntityAnnotation(TypeDeclaration typeDeclare, CompilationUnit compileUnit) {
    NormalAnnotationExpr annotation = new NormalAnnotationExpr();
    annotation.setName(new NameExpr("Entity"));
    addAnnotation(typeDeclare, annotation);
    ImportHelper.addImport(compileUnit, entityNameExpr);
    return annotation;
  }

  public static AnnotationExpr addPersistenceUnitAnnotation(TypeDeclaration typeDeclare, CompilationUnit compileUnit, String persistenceUnit) {
    NormalAnnotationExpr annotation = new NormalAnnotationExpr();
    annotation.setName(new NameExpr("PersistenceUnit"));
    annotation.setPairs(new ArrayList<MemberValuePair>());
    annotation.getPairs().add(new MemberValuePair("name", new StringLiteralExpr(persistenceUnit)));
    addAnnotation(typeDeclare, annotation);
    ImportHelper.addImport(compileUnit, persistenceUnitNameExpr);
    return annotation;
  }

  public static NormalAnnotationExpr addTableAnnotation(TypeDeclaration typeDeclare, CompilationUnit compileUnit, String tableName) {
    NormalAnnotationExpr annotation = new NormalAnnotationExpr();
    annotation.setName(new NameExpr("Table"));
    annotation.setPairs(new ArrayList<MemberValuePair>());
    annotation.getPairs().add(new MemberValuePair("name", new StringLiteralExpr(tableName)));
    addAnnotation(typeDeclare, annotation);
    ImportHelper.addImport(compileUnit, tableNameExpr);
    return annotation;
  }
  //javax.persistence.PersistenceUnit

  public static NormalAnnotationExpr addXmlElementAnnotation(FieldDeclaration fieldDeclare, CompilationUnit compileUnit, NameExpr name) {
    NormalAnnotationExpr annotation = new NormalAnnotationExpr();
    annotation.setName(new NameExpr("XmlElement"));
    annotation.setPairs(new ArrayList<MemberValuePair>());
    annotation.getPairs().add(new MemberValuePair("name", name));
    addAnnotation(fieldDeclare, annotation);
    ImportHelper.addImport(compileUnit, xmlElementNameExpr);
    return annotation;
  }

  public static NormalAnnotationExpr addColumnAnnotation(FieldDeclaration fieldDeclare, CompilationUnit compileUnit, NameExpr name, boolean nullable) {
    NormalAnnotationExpr annotation = new NormalAnnotationExpr();
    annotation.setName(new NameExpr("Column"));
    annotation.setPairs(new ArrayList<MemberValuePair>());
    annotation.getPairs().add(new MemberValuePair("nullable", new BooleanLiteralExpr(nullable)));
    annotation.getPairs().add(new MemberValuePair("name", name));
    addAnnotation(fieldDeclare, annotation);
    ImportHelper.addImport(compileUnit, columnNameExpr);
    return annotation;
  }

  public static AnnotationExpr addIdAnnotation(FieldDeclaration typeDeclare, CompilationUnit compileUnit) {
    NormalAnnotationExpr annotation = new NormalAnnotationExpr();
    annotation.setName(new NameExpr("Id"));
    addAnnotation(typeDeclare, annotation);
    ImportHelper.addImport(compileUnit, idNameExpr);
    return annotation;
  }

  public static AnnotationExpr addGeneratedValueAnnotation(FieldDeclaration typeDeclare, CompilationUnit compileUnit) {
    NormalAnnotationExpr annotation = new NormalAnnotationExpr();
    annotation.setName(new NameExpr("GeneratedValue"));
    addAnnotation(typeDeclare, annotation);
    ImportHelper.addImport(compileUnit, generatedValueNameExpr);
    return annotation;
  }

  public static AnnotationExpr addTemporalTimestampAnnotation(FieldDeclaration typeDeclare, CompilationUnit compileUnit) {
    return addTemporalAnnotation(typeDeclare, compileUnit, "TIMESTAMP");
  }

  public static AnnotationExpr addTemporalAnnotation(FieldDeclaration typeDeclare, CompilationUnit compileUnit, String temporalType) {
    SingleMemberAnnotationExpr annotation = new SingleMemberAnnotationExpr();
    annotation.setName(new NameExpr("Temporal"));
    annotation.setMemberValue(new QualifiedNameExpr(new NameExpr("TemporalType"), temporalType));
    addAnnotation(typeDeclare, annotation);
    ImportHelper.addImport(compileUnit, temporalNameExpr);
    ImportHelper.addImport(compileUnit, temporalTypeNameExpr);
    return annotation;
  }

  public static AnnotationExpr addTransactionalAnnotation(BodyDeclaration typeDeclare, CompilationUnit compileUnit) {
    NormalAnnotationExpr annotation = new NormalAnnotationExpr();
    annotation.setName(new NameExpr("Transactional"));
    addAnnotation(typeDeclare, annotation);
    ImportHelper.addImport(compileUnit, transactionalNameExpr);
    return annotation;
  }

  public static AnnotationExpr addOverrideAnnotation(BodyDeclaration typeDeclare, CompilationUnit compileUnit) {
    NormalAnnotationExpr annotation = new NormalAnnotationExpr();
    annotation.setName(new NameExpr("Override"));
    addAnnotation(typeDeclare, annotation);
    return annotation;
  }
}

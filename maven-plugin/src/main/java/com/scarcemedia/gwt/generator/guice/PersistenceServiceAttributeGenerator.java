/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.generator.guice;

import com.scarcemedia.gwt.generator.AbstractModelGenerator;
import com.scarcemedia.gwt.generator.Settings;
import com.scarcemedia.gwt.generator.definition.Definition;
import com.scarcemedia.gwt.generator.definition.Model;
import com.scarcemedia.gwt.generator.definition.PackageDefinition;
import com.scarcemedia.gwt.generator.util.AnnotationHelper;
import com.scarcemedia.gwt.generator.util.ImportHelper;
import com.scarcemedia.gwt.generator.util.NameHelper;
import japa.parser.ASTHelper;
import japa.parser.ast.body.AnnotationDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.ArrayInitializerExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.NormalAnnotationExpr;
import japa.parser.ast.expr.SingleMemberAnnotationExpr;
import java.util.ArrayList;

/**
 *
 * @author jeremy
 */
public class PersistenceServiceAttributeGenerator extends AbstractModelGenerator {

  public PersistenceServiceAttributeGenerator(Settings settings, Definition definition, PackageDefinition packageDefinition, Model model) {
    super(settings, definition, packageDefinition, model);
  }
  AnnotationDeclaration typeDeclare;

  @Override
  protected void onGenerate() {
    String persistServiceAttribute = NameHelper.getPersistServiceAttributeName(packageDefinition);

    ImportHelper.addImport(compileUnit, new NameExpr("com.google.inject.BindingAnnotation"));
    ImportHelper.addImport(compileUnit, new NameExpr("java.lang.annotation.Retention"));
    ImportHelper.addImport(compileUnit, new NameExpr("java.lang.annotation.Target"));

    ImportHelper.addStaticImport(compileUnit, "java.lang.annotation.ElementType.FIELD",
            "java.lang.annotation.ElementType.METHOD",
            "java.lang.annotation.ElementType.PARAMETER",
            "java.lang.annotation.RetentionPolicy.RUNTIME");

    typeDeclare = new japa.parser.ast.body.AnnotationDeclaration();
    AnnotationHelper.addAnnotation(typeDeclare, new NormalAnnotationExpr(new NameExpr("BindingAnnotation"), new ArrayList<MemberValuePair>()));

    AnnotationExpr exprRetention = new SingleMemberAnnotationExpr(new NameExpr("Retention"), new NameExpr("RUNTIME"));
    AnnotationHelper.addAnnotation(typeDeclare, exprRetention);

//    japa.parser.ast.expr.ArrayInitializerExpr

    ArrayInitializerExpr targets = new ArrayInitializerExpr();
    targets.setValues(new ArrayList<Expression>());
    targets.getValues().add(new NameExpr("METHOD"));
    targets.getValues().add(new NameExpr("PARAMETER"));
    targets.getValues().add(new NameExpr("FIELD"));

    SingleMemberAnnotationExpr exprTarget = new SingleMemberAnnotationExpr(new NameExpr("Target"), targets);
    AnnotationHelper.addAnnotation(typeDeclare, exprTarget);


    ASTHelper.addTypeDeclaration(compileUnit, typeDeclare);
    typeDeclare.setModifiers(ModifierSet.PUBLIC);
    typeDeclare.setName(persistServiceAttribute);
    typeDeclare.setMembers(new ArrayList<BodyDeclaration>());
  }

  @Override
  protected String getPackageName() {
    return settings.getGuicePersistPackage();
  }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.generator.shared;

import com.scarcemedia.gwt.generator.AbstractModelGenerator;
import com.scarcemedia.gwt.generator.Settings;
import com.scarcemedia.gwt.generator.definition.Definition;
import com.scarcemedia.gwt.generator.definition.Field;
import com.scarcemedia.gwt.generator.definition.Model;
import com.scarcemedia.gwt.generator.definition.PackageDefinition;
import com.scarcemedia.gwt.generator.util.NameHelper;
import japa.parser.ASTHelper;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.IntegerLiteralExpr;
import japa.parser.ast.expr.StringLiteralExpr;

/**
 *
 * @author jeremy
 */
public class ModelConstantsGenerator extends AbstractModelGenerator {

  public ModelConstantsGenerator(Settings settings, Definition definition, PackageDefinition packageDefinition, Model model) {
    super(settings, definition, packageDefinition, model);
  }
  ClassOrInterfaceDeclaration typeDeclare;

  @Override
  protected void onGenerate() {
    typeDeclare = new ClassOrInterfaceDeclaration(ModifierSet.addModifier(ModifierSet.PUBLIC, ModifierSet.FINAL), false, NameHelper.getSharedDataClassName(model));

    int modifiers = ModifierSet.addModifier(ModifierSet.FINAL, ModifierSet.PUBLIC);
    modifiers = ModifierSet.addModifier(modifiers, ModifierSet.STATIC);

    for (Field field : model.getFields()) {
      String fieldName = NameHelper.getFieldNameConstant(field);
      FieldDeclaration fieldNameDeclare = new FieldDeclaration(modifiers, ASTHelper.createReferenceType("String", 0), new VariableDeclarator(new VariableDeclaratorId(fieldName), new StringLiteralExpr(field.getName())));
      ASTHelper.addMember(typeDeclare, fieldNameDeclare);

      if (field.hasLength()) {
        String fieldLength = NameHelper.getFieldLengthConstant(field);
        FieldDeclaration fieldLengthDeclare = new FieldDeclaration(modifiers, ASTHelper.createReferenceType("int", 0), new VariableDeclarator(new VariableDeclaratorId(fieldLength), new IntegerLiteralExpr(field.getLength().toString())));
        ASTHelper.addMember(typeDeclare, fieldLengthDeclare);
      }
    }
    
    compileUnit.getTypes().add(typeDeclare);
  }

  @Override
  protected String getPackageName() {
    return settings.getSharedPackage();
  }
}

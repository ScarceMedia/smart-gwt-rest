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
import japa.parser.ASTHelper;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.type.VoidType;
import java.util.ArrayList;

/**
 *
 * @author jeremy
 */
public class PersistenceLifeCycleManagerGenerator extends AbstractModelGenerator {

  public PersistenceLifeCycleManagerGenerator(Settings settings, Definition definition, PackageDefinition packageDefinition, Model model) {
    super(settings, definition, packageDefinition, model);
  }
  ClassOrInterfaceDeclaration typeDeclare;

  @Override
  protected void onGenerate() {
    String persistModuleName = "PersistenceLifeCycleManager";
    
    typeDeclare = new ClassOrInterfaceDeclaration();
    typeDeclare.setInterface(true);
    ASTHelper.addTypeDeclaration(compileUnit, typeDeclare);
    typeDeclare.setModifiers(ModifierSet.PUBLIC);
    typeDeclare.setName(persistModuleName);
    typeDeclare.setMembers(new ArrayList<BodyDeclaration>());

    String[] methodNames = new String[]{
      "startService",
      "stopService",
      "beginUnitOfWork",
      "endUnitOfWork",      
    };
    
    for(String methodName:methodNames){
      MethodDeclaration methodDeclare = new MethodDeclaration();
      methodDeclare.setName(methodName);
      methodDeclare.setType(new VoidType());
      ASTHelper.addMember(typeDeclare, methodDeclare);
    }
  }

  @Override
  protected String getPackageName() {
    return settings.getGuicePersistPackage();
  }
}

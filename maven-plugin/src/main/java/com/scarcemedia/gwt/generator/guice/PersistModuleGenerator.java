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
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.expr.ClassExpr;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.SuperExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.VoidType;
import java.util.ArrayList;

/**
 *
 * @author jeremy
 */
public class PersistModuleGenerator extends AbstractGenerator {
  
  public PersistModuleGenerator(Settings settings, Definition definition, PackageDefinition packageDefinition, Model model) {
    super(settings, definition, packageDefinition, model);
  }
  ClassOrInterfaceDeclaration typeDeclare;
  
  @Override
  protected void onGenerate() {
    String persistModuleName = NameHelper.getPersistModuleName(packageDefinition);
    typeDeclare = new ClassOrInterfaceDeclaration();
    ASTHelper.addTypeDeclaration(compileUnit, typeDeclare);
    typeDeclare.setModifiers(ModifierSet.PUBLIC);
    typeDeclare.setName(persistModuleName);
    typeDeclare.setExtends(new ArrayList<ClassOrInterfaceType>());
    typeDeclare.setMembers(new ArrayList<BodyDeclaration>());
    typeDeclare.getExtends().add(new ClassOrInterfaceType("PrivateModule"));
    
    ImportHelper.addImport(compileUnit, new NameExpr("com.google.inject.PrivateModule"));
    
    MethodDeclaration configureMethod = new MethodDeclaration();
    ASTHelper.addMember(typeDeclare, configureMethod);
    AnnotationHelper.addOverrideAnnotation(configureMethod, compileUnit);
    configureMethod.setType(new VoidType());
    configureMethod.setModifiers(ModifierSet.PUBLIC);
    configureMethod.setName("configure");
    configureMethod.setBody(new BlockStmt());

    /*
     * 
     *     bind(PersistenceLifeCycleManager.class).annotatedWith(PuppetPersistService.class).to(PuppetPersistenceLifeCycleManager.class);
     expose(PersistenceLifeCycleManager.class).annotatedWith(PuppetPersistService.class);
     */
    
    
    MethodCallExpr callBind = bind(new ClassOrInterfaceType("PersistenceLifeCycleManager"), new ClassOrInterfaceType(NameHelper.getPersistLifecycleManagerName(packageDefinition)), new ClassOrInterfaceType(NameHelper.getPersistServiceName(packageDefinition)));
    ASTHelper.addStmt(configureMethod.getBody(), callBind);
    
    
    
    
    for (Model m : packageDefinition.getModels()) {
      addDAOInterfaceImport(m);
      addDAOImplImport(m);
      
      ClassOrInterfaceType daoInterface = new ClassOrInterfaceType(NameHelper.getDAOInterfaceName(m));
      ClassOrInterfaceType daoImpl = new ClassOrInterfaceType(NameHelper.getDAOImplName(m));
      
      MethodCallExpr callTo = bind(daoInterface, daoImpl);
      ASTHelper.addStmt(configureMethod.getBody(), callTo);
      
      
      
      MethodCallExpr callExpose = new MethodCallExpr(new SuperExpr(), "expose");
      ASTHelper.addArgument(callExpose, new ClassExpr(daoInterface));
      ASTHelper.addStmt(configureMethod.getBody(), callExpose);
    }
  }
  
  MethodCallExpr bind(ClassOrInterfaceType from, ClassOrInterfaceType to) {
    MethodCallExpr callBind = new MethodCallExpr(new SuperExpr(), "bind");
    ASTHelper.addArgument(callBind, new ClassExpr(from));
    
    MethodCallExpr callTo = new MethodCallExpr(callBind, "to");
    ASTHelper.addArgument(callTo, new ClassExpr(to));
    
    return callTo;
  }
  
  MethodCallExpr bind(ClassOrInterfaceType from, ClassOrInterfaceType to, ClassOrInterfaceType annotatedWith) {
    MethodCallExpr callBind = new MethodCallExpr(new SuperExpr(), "bind");
    ASTHelper.addArgument(callBind, new ClassExpr(from));
    
    MethodCallExpr callannotatedWith = new MethodCallExpr(callBind, "annotatedWith");
    ASTHelper.addArgument(callannotatedWith, new ClassExpr(annotatedWith));
    
    MethodCallExpr callTo = new MethodCallExpr(callannotatedWith, "to");
    ASTHelper.addArgument(callTo, new ClassExpr(to));
    
    return callTo;
  }
  
  @Override
  protected String getPackageName() {
    return settings.getGuicePersistPackage();
  }
}

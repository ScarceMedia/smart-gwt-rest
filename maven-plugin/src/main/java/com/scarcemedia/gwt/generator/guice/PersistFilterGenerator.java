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
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.NormalAnnotationExpr;
import japa.parser.ast.expr.ThisExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.stmt.TryStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.VoidType;
import java.util.ArrayList;

/**
 *
 * @author jeremy
 */
public class PersistFilterGenerator extends AbstractGenerator {

  public PersistFilterGenerator(Settings settings, Definition definition, PackageDefinition packageDefinition, Model model) {
    super(settings, definition, packageDefinition, model);
  }
  ClassOrInterfaceDeclaration typeDeclare;

  void addImports(String... imports) {
    for (String s : imports) {
      ImportHelper.addImport(compileUnit, new NameExpr(s));
    }
  }
  FieldAccessExpr managerExpr;

  @Override
  protected void onGenerate() {
    managerExpr = new FieldAccessExpr(new ThisExpr(), "manager");

    String persistFilter = NameHelper.getPersistFilterName(packageDefinition);
    String persistFilterAttribute = NameHelper.getPersistServiceAttributeName(packageDefinition);

    typeDeclare = new ClassOrInterfaceDeclaration();
    AnnotationHelper.addSingletonAnnotation(typeDeclare, compileUnit);
    ASTHelper.addTypeDeclaration(compileUnit, typeDeclare);
    typeDeclare.setModifiers(ModifierSet.PUBLIC);
    typeDeclare.setName(persistFilter);
    typeDeclare.setMembers(new ArrayList<BodyDeclaration>());
    typeDeclare.setImplements(new ArrayList<ClassOrInterfaceType>());
    typeDeclare.getImplements().add(new ClassOrInterfaceType("Filter"));

    addImports("java.io.IOException",
            "javax.servlet.Filter",
            "javax.servlet.FilterChain",
            "javax.servlet.FilterConfig",
            "javax.servlet.ServletException",
            "javax.servlet.ServletRequest",
            "javax.servlet.ServletResponse");

    ConstructorDeclaration constructor = new ConstructorDeclaration();
    constructor.setModifiers(ModifierSet.PUBLIC);
    constructor.setName(persistFilter);
    constructor.setBlock(new BlockStmt());
    constructor.getBlock().setStmts(new ArrayList<Statement>());
    constructor.setParameters(new ArrayList<Parameter>());
    Parameter parameter = new Parameter();
    parameter.setType(new ClassOrInterfaceType("PersistenceLifeCycleManager"));
    parameter.setId(new VariableDeclaratorId(managerExpr.getField()));
    AnnotationHelper.addAnnotation(parameter, new NormalAnnotationExpr(new NameExpr(persistFilterAttribute), new ArrayList<MemberValuePair>()));

//    AnnotationHelper.q    

    constructor.getParameters().add(parameter);



//    
    FieldDeclaration fieldManager = new FieldDeclaration(ModifierSet.FINAL, new ClassOrInterfaceType("PersistenceLifeCycleManager"), new ArrayList<VariableDeclarator>());
    fieldManager.getVariables().add(new VariableDeclarator(new VariableDeclaratorId(managerExpr.getField())));
    ASTHelper.addMember(typeDeclare, fieldManager);
    ASTHelper.addStmt(constructor.getBlock(), new AssignExpr(managerExpr, new NameExpr("manager"), AssignExpr.Operator.assign));

    AnnotationHelper.addInjectAnnotation(constructor, compileUnit);
    ASTHelper.addMember(typeDeclare, constructor);

    addInitMethod();
    addDestroyMethod();
    adddoFilterMethod();

  }

  void addInitMethod() {
    MethodDeclaration methodInit = new MethodDeclaration(ModifierSet.PUBLIC, new VoidType(), "init");
    ASTHelper.addMember(typeDeclare, methodInit);
    AnnotationHelper.addOverrideAnnotation(methodInit, compileUnit);
    methodInit.setBody(new BlockStmt());
    methodInit.getBody().setStmts(new ArrayList<Statement>());
    methodInit.setThrows(new ArrayList<NameExpr>());
    methodInit.getThrows().add(new NameExpr("ServletException"));
    Parameter parameter = new Parameter();
    parameter.setType(new ClassOrInterfaceType("FilterConfig"));
    parameter.setId(new VariableDeclaratorId("filterConfig"));
    ASTHelper.addParameter(methodInit, parameter);

    MethodCallExpr callStartService = new MethodCallExpr(managerExpr, "startService");
    ASTHelper.addStmt(methodInit.getBody(), callStartService);


  }

  void addDestroyMethod() {
    MethodDeclaration methodDestroy = new MethodDeclaration(ModifierSet.PUBLIC, new VoidType(), "destroy");
    ASTHelper.addMember(typeDeclare, methodDestroy);
    AnnotationHelper.addOverrideAnnotation(methodDestroy, compileUnit);
    methodDestroy.setBody(new BlockStmt());
    methodDestroy.getBody().setStmts(new ArrayList<Statement>());

    MethodCallExpr callStartService = new MethodCallExpr(managerExpr, "stopService");
    ASTHelper.addStmt(methodDestroy.getBody(), callStartService);


  }

  void adddoFilterMethod() {
    MethodDeclaration methodDoFilter = new MethodDeclaration(ModifierSet.PUBLIC, new VoidType(), "doFilter");
    ASTHelper.addMember(typeDeclare, methodDoFilter);
    AnnotationHelper.addOverrideAnnotation(methodDoFilter, compileUnit);
    methodDoFilter.setBody(new BlockStmt());
    methodDoFilter.getBody().setStmts(new ArrayList<Statement>());
    methodDoFilter.setThrows(new ArrayList<NameExpr>());
    methodDoFilter.getThrows().add(new NameExpr("IOException"));
    methodDoFilter.getThrows().add(new NameExpr("ServletException"));

    Parameter pServletRequest = new Parameter();
    pServletRequest.setModifiers(ModifierSet.FINAL);
    pServletRequest.setType(new ClassOrInterfaceType("ServletRequest"));
    pServletRequest.setId(new VariableDeclaratorId("servletRequest"));
    ASTHelper.addParameter(methodDoFilter, pServletRequest);

    Parameter pServletResponse = new Parameter();
    pServletResponse.setModifiers(ModifierSet.FINAL);
    pServletResponse.setType(new ClassOrInterfaceType("ServletResponse"));
    pServletResponse.setId(new VariableDeclaratorId("servletResponse"));
    ASTHelper.addParameter(methodDoFilter, pServletResponse);

    Parameter pFilterChain = new Parameter();
    pFilterChain.setModifiers(ModifierSet.FINAL);
    pFilterChain.setType(new ClassOrInterfaceType("FilterChain"));
    pFilterChain.setId(new VariableDeclaratorId("filterChain"));
    ASTHelper.addParameter(methodDoFilter, pFilterChain);

    MethodCallExpr callBeginUnitOfWork = new MethodCallExpr(managerExpr, "beginUnitOfWork");
    ASTHelper.addStmt(methodDoFilter.getBody(), callBeginUnitOfWork);

    TryStmt tryDoFilter = new TryStmt();
    ASTHelper.addStmt(methodDoFilter.getBody(), tryDoFilter);
    tryDoFilter.setTryBlock(new BlockStmt(new ArrayList<Statement>()));
    tryDoFilter.setFinallyBlock(new BlockStmt(new ArrayList<Statement>()));

    MethodCallExpr callDoFilter = new MethodCallExpr(new NameExpr("filterChain"), "doFilter");
    ASTHelper.addArgument(callDoFilter, new NameExpr("servletRequest"));
    ASTHelper.addArgument(callDoFilter, new NameExpr("servletResponse"));
    ASTHelper.addStmt(tryDoFilter.getTryBlock(), callDoFilter);




    MethodCallExpr callEndUnitOfWork = new MethodCallExpr(managerExpr, "endUnitOfWork");
    ASTHelper.addStmt(tryDoFilter.getFinallyBlock(), callEndUnitOfWork);

    MethodCallExpr callStartService = new MethodCallExpr(managerExpr, "startService");
    ASTHelper.addStmt(methodDoFilter.getBody(), callStartService);


  }

//    public void doFilter(final ServletRequest servletRequest,
//          final ServletResponse servletResponse, final FilterChain filterChain)
//          throws IOException, ServletException {
//
//    this.manager.beginUnitOfWork();
//    try {
//      filterChain.doFilter(servletRequest, servletResponse);
//    } finally {
//      this.manager.endUnitOfWork();
//    }
//  }
  @Override
  protected String getPackageName() {
    return settings.getGuicePersistPackage();
  }
}

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
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.expr.ThisExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.CatchClause;
import japa.parser.ast.stmt.IfStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.stmt.ThrowStmt;
import japa.parser.ast.stmt.TryStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.VoidType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jeremy
 */
public class PersistModuleGenerator extends AbstractGenerator {

  public PersistModuleGenerator(Settings settings, Definition definition, PackageDefinition packageDefinition, Model model) {
    super(settings, definition, packageDefinition, model);
  }
  ClassOrInterfaceDeclaration typeDeclare;

  NameExpr moduleName;

  protected NameExpr addFinalString(BlockStmt stmt, String variableName, String value) {
    return addFinalString(stmt, variableName, new StringLiteralExpr(value));
  }

  protected NameExpr addFinalString(BlockStmt stmt, String variableName, Expression value) {
    NameExpr variableNameExpr = new NameExpr(variableName);

    VariableDeclarationExpr varConnectionString = new VariableDeclarationExpr(new ClassOrInterfaceType("String"), new ArrayList<VariableDeclarator>());
    varConnectionString.setModifiers(ModifierSet.FINAL);
    varConnectionString.getVars().add(new VariableDeclarator(new VariableDeclaratorId(variableName), value));
    ASTHelper.addStmt(stmt, varConnectionString);

    return variableNameExpr;
  }

  protected MethodCallExpr getResourceAsStream(String key) {
    return getResourceAsStream(new StringLiteralExpr(key));
  }

  protected MethodCallExpr getResourceAsStream(Expression key) {

    MethodCallExpr callGetClass = new MethodCallExpr(new ThisExpr(), "getClass");
    MethodCallExpr callGetClassLoader = new MethodCallExpr(callGetClass, "getClassLoader");
    MethodCallExpr callgetResourceAsStream = new MethodCallExpr(callGetClassLoader, "getResourceAsStream");
    ASTHelper.addArgument(callgetResourceAsStream, key);

    return callgetResourceAsStream;
  }

  protected NameExpr declareVariable(BlockStmt stmt, String type, String variableName, Expression initExpression) {
    NameExpr variableNameExpr = new NameExpr(variableName);

    VariableDeclarationExpr varConnectionString = new VariableDeclarationExpr(new ClassOrInterfaceType(type), new ArrayList<VariableDeclarator>());
    VariableDeclarator variableDeclarator = new VariableDeclarator(new VariableDeclaratorId(variableName));
    if (null != initExpression) {
      variableDeclarator.setInit(initExpression);
    }

    varConnectionString.getVars().add(variableDeclarator);
    ASTHelper.addStmt(stmt, varConnectionString);

    return variableNameExpr;
  }

  protected IfStmt ifNull(BlockStmt stmt, Expression expression) {
    IfStmt ifStmt = new IfStmt();
    ifStmt.setThenStmt(new BlockStmt(new ArrayList<Statement>()));
    BinaryExpr check = new BinaryExpr(new NullLiteralExpr(), expression, BinaryExpr.Operator.equals);
    ifStmt.setCondition(check);
    ASTHelper.addStmt(stmt, ifStmt);
    return ifStmt;
  }

  protected IfStmt ifNotNull(BlockStmt stmt, Expression expression) {
    IfStmt ifStmt = new IfStmt();
    ifStmt.setThenStmt(new BlockStmt(new ArrayList<Statement>()));
    BinaryExpr check = new BinaryExpr(new NullLiteralExpr(), expression, BinaryExpr.Operator.notEquals);
    ifStmt.setCondition(check);
    ASTHelper.addStmt(stmt, ifStmt);
    return ifStmt;
  }

  String modulePropertiesFile;

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
    ImportHelper.addImport(compileUnit, new NameExpr("com.google.inject.persist.jpa.JpaPersistModule"));
    ImportHelper.addImport(compileUnit, new NameExpr("java.util.Properties"));
    ImportHelper.addImport(compileUnit, new NameExpr("java.io.InputStream"));
    ImportHelper.addImport(compileUnit, new NameExpr("java.io.IOException"));
    ImportHelper.addImport(compileUnit, new NameExpr("org.dozer.Mapper"));
    ImportHelper.addImport(compileUnit, new NameExpr("org.dozer.DozerBeanMapper"));

    MethodDeclaration configureMethod = new MethodDeclaration();
    ASTHelper.addMember(typeDeclare, configureMethod);
    AnnotationHelper.addOverrideAnnotation(configureMethod, compileUnit);
    configureMethod.setType(new VoidType());
    configureMethod.setModifiers(ModifierSet.PUBLIC);
    configureMethod.setName("configure");
    configureMethod.setBody(new BlockStmt());

    moduleName = addFinalString(configureMethod.getBody(), "moduleName", packageDefinition.getPersistenceUnit());
    modulePropertiesFile = String.format("%s.db.properties", packageDefinition.getPersistenceUnit().toLowerCase());

    properties = declareVariable(configureMethod.getBody(), "Properties", "properties", new ObjectCreationExpr(null, new ClassOrInterfaceType("Properties"), new ArrayList<Expression>()));
    NameExpr varInputStream = declareVariable(configureMethod.getBody(),
            "InputStream",
            "inputStream",
            getResourceAsStream(modulePropertiesFile)
    );

    IfStmt ifInputStream = ifNotNull(configureMethod.getBody(), varInputStream);
    ifInputStream.setElseStmt(new BlockStmt(new ArrayList<Statement>()));

    /*
     * 
     *     bind(PersistenceLifeCycleManager.class).annotatedWith(PuppetPersistService.class).to(PuppetPersistenceLifeCycleManager.class);
     expose(PersistenceLifeCycleManager.class).annotatedWith(PuppetPersistService.class);
     */
    configurePersistModuleForAws((BlockStmt) ifInputStream.getElseStmt());
    configurePersistModuleForClassPathPropertiesFile((BlockStmt) ifInputStream.getThenStmt(), varInputStream);

    ObjectCreationExpr newJpaPersistModule = new ObjectCreationExpr(null, new ClassOrInterfaceType("JpaPersistModule"), new ArrayList<Expression>());
    newJpaPersistModule.getArgs().add(moduleName);
    NameExpr persistModule = declareVariable(configureMethod.getBody(), "JpaPersistModule", "persistModule", newJpaPersistModule);
    MethodCallExpr callProperties = new MethodCallExpr(persistModule, "properties");
    ASTHelper.addArgument(callProperties, properties);

    Map<String, String> s = new HashMap<String, String>();
    s.put("datanucleus.autoCreateSchema", "true");
    s.put("datanucleus.autoCreateTables", "true");
    s.put("datanucleus.validateTables", "true");
    s.put("datanucleus.validateConstraints", "true");
    s.put("datanucleus.identifier.case", "PreserveCase");

    for (Map.Entry<String, String> kvp : s.entrySet()) {
      addKeyValue(configureMethod.getBody(), properties, kvp.getKey(), kvp.getValue());
    }

    MethodCallExpr callInstall = new MethodCallExpr(null, "install");
    ASTHelper.addArgument(callInstall, callProperties);
    ASTHelper.addStmt(configureMethod.getBody(), callInstall);

    ASTHelper.addStmt(configureMethod.getBody(),
            bind(new ClassOrInterfaceType("PersistenceLifeCycleManager"),
                    new ClassOrInterfaceType(NameHelper.getPersistLifecycleManagerName(packageDefinition)),
                    new ClassOrInterfaceType(NameHelper.getPersistServiceName(packageDefinition))
            )
    );

    MethodCallExpr eager = new MethodCallExpr(bind(new ClassOrInterfaceType("Mapper"), new ClassOrInterfaceType("DozerBeanMapper")), "asEagerSingleton");
    ASTHelper.addStmt(configureMethod.getBody(), eager);

    String jpaInitializerName = NameHelper.getJPAInitializerName(packageDefinition);

    MethodCallExpr callBindJpaInitializer = new MethodCallExpr(null, "bind");
    callBindJpaInitializer.setArgs(new ArrayList<Expression>());
    callBindJpaInitializer.getArgs().add(new ClassExpr(new ClassOrInterfaceType(jpaInitializerName)));

    ASTHelper.addStmt(configureMethod.getBody(), new MethodCallExpr(callBindJpaInitializer, "asEagerSingleton"));

    List<MethodCallExpr> exposeCalls = new ArrayList<MethodCallExpr>();

    for (Model m : packageDefinition.getModels()) {
      addDAOInterfaceImport(m);
      addDAOImplImport(m);

      ClassOrInterfaceType daoInterface = new ClassOrInterfaceType(NameHelper.getDAOInterfaceName(m));
      ClassOrInterfaceType daoImpl = new ClassOrInterfaceType(NameHelper.getDAOImplName(m));

      MethodCallExpr callTo = bind(daoInterface, daoImpl);
      ASTHelper.addStmt(configureMethod.getBody(), callTo);

      MethodCallExpr callExpose = new MethodCallExpr(null, "expose");
      ASTHelper.addArgument(callExpose, new ClassExpr(daoInterface));
      exposeCalls.add(callExpose);

    }

    for (MethodCallExpr callExpose : exposeCalls) {
      ASTHelper.addStmt(configureMethod.getBody(), callExpose);
    }

  }
  NameExpr properties;

  void configurePersistModuleForClassPathPropertiesFile(BlockStmt stmt, Expression varInputStream) {
    TryStmt tryLoad = new TryStmt();
    ASTHelper.addStmt(stmt, tryLoad);
    tryLoad.setTryBlock(new BlockStmt(new ArrayList<Statement>()));
//    tryLoad.setFinallyBlock(new BlockStmt(new ArrayList<Statement>()));
    tryLoad.setCatchs(new ArrayList<CatchClause>());

    MethodCallExpr callLoad = new MethodCallExpr(properties, "load");
    ASTHelper.addArgument(callLoad, varInputStream);
    ASTHelper.addStmt(tryLoad.getTryBlock(), callLoad);
//    ASTHelper.addStmt(tryLoad.getFinallyBlock(), new MethodCallExpr(varInputStream, "close"));

    Parameter parmIoException = new Parameter(new ClassOrInterfaceType("IOException"), new VariableDeclaratorId("ex"));

    CatchClause ioExceptionClause = new CatchClause(parmIoException, new BlockStmt(new ArrayList<Statement>()));
    tryLoad.getCatchs().add(ioExceptionClause);

    ObjectCreationExpr newIllegalStateException = new ObjectCreationExpr(null, new ClassOrInterfaceType("IllegalStateException"), new ArrayList<Expression>());
    newIllegalStateException.getArgs().add(new StringLiteralExpr("Exception while loading " + modulePropertiesFile));
    newIllegalStateException.getArgs().add(new NameExpr("ex"));

    ThrowStmt throwIllegalArgument = new ThrowStmt(newIllegalStateException);
    ASTHelper.addStmt(ioExceptionClause.getCatchBlock(), throwIllegalArgument);

  }

  void configurePersistModuleForAws(BlockStmt stmt) {

    MethodCallExpr callGetPropertyConnStr = new MethodCallExpr(new NameExpr("System"), "getProperty");
    ASTHelper.addArgument(callGetPropertyConnStr, new StringLiteralExpr("JDBC_CONNECTION_STRING"));

    NameExpr varConnectionString = new NameExpr("connectionString");
    VariableDeclarationExpr varDecConnectionString = new VariableDeclarationExpr(new ClassOrInterfaceType("String"), new ArrayList<VariableDeclarator>());
    varDecConnectionString.setModifiers(ModifierSet.FINAL);
    varDecConnectionString.getVars().add(new VariableDeclarator(new VariableDeclaratorId(varConnectionString.getName()), callGetPropertyConnStr));
    ASTHelper.addStmt(stmt, varDecConnectionString);

    IfStmt ifConnectionString = ifNotNull(stmt, varConnectionString);
    ifConnectionString.setThenStmt(new BlockStmt(new ArrayList<Statement>()));
    addKeyValue((BlockStmt)ifConnectionString.getThenStmt(), properties, "javax.persistence.jdbc.url", varConnectionString);
    
    MethodCallExpr callGetPropertyDriver = new MethodCallExpr(new NameExpr("System"), "getProperty");
    ASTHelper.addArgument(callGetPropertyDriver, new StringLiteralExpr("JDBC_DRIVER"));
    
    
    
//    ASTHelper.addArgument(callGetPropertyDriver, new StringLiteralExpr("com.mysql.jdbc.Driver"));

    NameExpr driver = new NameExpr("driver");
    VariableDeclarationExpr varDriver = new VariableDeclarationExpr(new ClassOrInterfaceType("String"), new ArrayList<VariableDeclarator>());
    varDriver.setModifiers(ModifierSet.FINAL);
    varDriver.getVars().add(new VariableDeclarator(new VariableDeclaratorId(driver.getName()), callGetPropertyDriver));
    ASTHelper.addStmt(stmt, varDriver);
    
    
    IfStmt ifDriver = ifNotNull(stmt, varConnectionString);
    ifDriver.setThenStmt(new BlockStmt(new ArrayList<Statement>()));
    addKeyValue((BlockStmt)ifDriver.getThenStmt(), properties, "javax.persistence.jdbc.driver", driver);
    

//    VariableDeclarationExpr varPersistModule = new VariableDeclarationExpr(new ClassOrInterfaceType("JpaPersistModule"), new ArrayList<VariableDeclarator>());
//    ObjectCreationExpr newJpaPersistModule = new ObjectCreationExpr(null, new ClassOrInterfaceType("JpaPersistModule"), new ArrayList<Expression>());
//    newJpaPersistModule.getArgs().add(new StringLiteralExpr(packageDefinition.getPersistenceUnit()));
//    varPersistModule.getVars().add(new VariableDeclarator(new VariableDeclaratorId("persistModule"), newJpaPersistModule));
//    ASTHelper.addStmt(stmt, varPersistModule);
//    NameExpr persistModule = new NameExpr("persistModule");
    

//    super.packageDefinition.getPersistenceUnit()
  }

  void addKeyValue(BlockStmt stmt, NameExpr properties, String key, String value) {
    addKeyValue(stmt, properties, key, new StringLiteralExpr(value));
  }

  void addKeyValue(BlockStmt stmt, NameExpr properties, String key, Expression value) {
    MethodCallExpr callPut = new MethodCallExpr(properties, "put");
    ASTHelper.addArgument(callPut, new StringLiteralExpr(key));
    ASTHelper.addArgument(callPut, value);
    ASTHelper.addStmt(stmt, callPut);
  }

  MethodCallExpr bind(ClassOrInterfaceType from, ClassOrInterfaceType to) {
    MethodCallExpr callBind = new MethodCallExpr(null, "bind");
    ASTHelper.addArgument(callBind, new ClassExpr(from));

    MethodCallExpr callTo = new MethodCallExpr(callBind, "to");
    ASTHelper.addArgument(callTo, new ClassExpr(to));

    return callTo;
  }

  MethodCallExpr bind(ClassOrInterfaceType from, ClassOrInterfaceType to, ClassOrInterfaceType annotatedWith) {
    MethodCallExpr callBind = new MethodCallExpr(null, "bind");
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

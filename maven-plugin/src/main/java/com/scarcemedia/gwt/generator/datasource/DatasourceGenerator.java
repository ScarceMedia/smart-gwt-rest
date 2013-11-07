/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.generator.datasource;

import com.google.common.base.Preconditions;
import com.scarcemedia.gwt.generator.AbstractGenerator;
import com.scarcemedia.gwt.generator.Settings;
import com.scarcemedia.gwt.generator.definition.Definition;
import com.scarcemedia.gwt.generator.definition.Field;
import com.scarcemedia.gwt.generator.definition.Model;
import com.scarcemedia.gwt.generator.definition.PackageDefinition;
import com.scarcemedia.gwt.generator.util.AnnotationHelper;
import com.scarcemedia.gwt.generator.util.CodeHelpers;
import com.scarcemedia.gwt.generator.util.ImportHelper;
import com.scarcemedia.gwt.generator.util.NameHelper;
import japa.parser.ASTHelper;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.ArrayAccessExpr;
import japa.parser.ast.expr.ArrayCreationExpr;
import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.IntegerLiteralExpr;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.expr.SuperExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.type.ClassOrInterfaceType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jeremy
 */
public class DatasourceGenerator extends AbstractGenerator {

  public DatasourceGenerator(Settings settings, Definition definition, PackageDefinition packageDefinition, Model model) {
    super(settings, definition, packageDefinition, model);
  }
  ClassOrInterfaceDeclaration typeDeclare;
  ConstructorDeclaration constructor;
  Map<String, NameExpr> referencedModels;

  @Override
  public void onGenerate() {
    referencedModels = new HashMap<String, NameExpr>();
    String dataSourceName = NameHelper.getDataSourceName(model);

    typeDeclare = new ClassOrInterfaceDeclaration();
    typeDeclare.setModifiers(ModifierSet.PUBLIC);
    typeDeclare.setName(dataSourceName);
    typeDeclare.setExtends(new ArrayList<ClassOrInterfaceType>());
    typeDeclare.setMembers(new ArrayList<BodyDeclaration>());
    typeDeclare.getExtends().add(new ClassOrInterfaceType("RestDataSource"));

    ImportHelper.addImport(compileUnit, new NameExpr("com.smartgwt.client.data.RestDataSource"));
    addSharedConstantsImport(model);


    constructor = new ConstructorDeclaration(ModifierSet.PUBLIC, dataSourceName);
    constructor.setBlock(new BlockStmt());
    constructor.getBlock().setStmts(new ArrayList<Statement>());
    constructor.setParameters(new ArrayList<Parameter>());
    AnnotationHelper.addSingletonAnnotation(typeDeclare, compileUnit);
    AnnotationHelper.addInjectAnnotation(constructor, compileUnit);

    for (Field field : model.getFields()) {
      processField(field);
    }

    ASTHelper.addMember(typeDeclare, constructor);
    ASTHelper.addTypeDeclaration(compileUnit, typeDeclare);
  }

  ClassOrInterfaceType getDatasourceFieldType(Field field) {
    ClassOrInterfaceType result = null;
    switch (field.getType()) {
      case Boolean:
        result = new ClassOrInterfaceType("DataSourceBooleanField");
        break;
      case Date:
        result = new ClassOrInterfaceType("DataSourceDateField");
        break;
      case DateTime:
        result = new ClassOrInterfaceType("DataSourceDateTimeField");
        break;
      case Double:
        result = new ClassOrInterfaceType("DataSourceFloatField");
        break;
      case Integer:
        result = new ClassOrInterfaceType("DataSourceIntegerField");
        break;
      case String:
        result = new ClassOrInterfaceType("DataSourceTextField");
        break;
      default:
        throw new UnsupportedOperationException(String.format("%s is not a supported field type.", field.getType()));
//        throw new 
    }
    return result;
  }

  void addFieldImport(ClassOrInterfaceType type) {
    String importName = String.format("com.smartgwt.client.data.fields.%s", type.getName());
    ImportHelper.addImport(compileUnit, new NameExpr(importName));
  }

  private void addLookup(Field field, NameExpr variableNameExpr) {
    Model referencedModel = getModel(field.getModelLookup().getModelName());
    Preconditions.checkArgument(null != referencedModel, "Referenced Model %s was not found.", field.getModelLookup().getModelName());
    Field referencedValueField = referencedModel.getField(field.getModelLookup().getValueField());
    Preconditions.checkArgument(null != referencedValueField, "Referenced Field %s.%s was not found", field.getModelLookup().getModelName(), field.getModelLookup().getValueField());

    String editorVariableName = String.format("%sEditor", variableNameExpr.getName());
    NameExpr editorVariableExpr = new NameExpr(editorVariableName);


    String referencedModelDatasource = NameHelper.getDataSourceName(model);

    NameExpr referencedModelDatasourceExpr = referencedModels.get(referencedModelDatasource);

    if (null == referencedModelDatasourceExpr) {
      String variableName = referencedModelDatasource.toLowerCase();
      referencedModelDatasourceExpr = new NameExpr(variableName);
      ClassOrInterfaceType datasourceType = new ClassOrInterfaceType(referencedModelDatasource);
      Parameter parameter = new Parameter(datasourceType, new VariableDeclaratorId(variableName));
      constructor.getParameters().add(parameter);
      referencedModels.put(referencedModelDatasource, referencedModelDatasourceExpr);
    }

    ClassOrInterfaceType editorType;

    if (field.getModelLookup().getDisplayFields().size() == 1) {
      ImportHelper.addImport(compileUnit, new NameExpr("com.smartgwt.client.widgets.form.fields.ComboBoxItem"));
      editorType = new ClassOrInterfaceType("ComboBoxItem");
    } else {
      ImportHelper.addImport(compileUnit, new NameExpr("com.smartgwt.client.widgets.form.fields.SelectItem"));
      editorType = new ClassOrInterfaceType("SelectItem");
    }

    VariableDeclarationExpr fieldEditorDeclare = new VariableDeclarationExpr();
    fieldEditorDeclare.setType(editorType);
    fieldEditorDeclare.setVars(new ArrayList<VariableDeclarator>());

    ObjectCreationExpr createFieldExpression = new ObjectCreationExpr();
    createFieldExpression.setArgs(new ArrayList<Expression>());
    createFieldExpression.setType(editorType);

    fieldEditorDeclare.getVars().add(new VariableDeclarator(new VariableDeclaratorId(editorVariableName), createFieldExpression));
    constructor.getBlock().getStmts().add(new ExpressionStmt(fieldEditorDeclare));

    CodeHelpers.addMethodCall(constructor.getBlock(), editorVariableExpr, "setOptionDataSource", referencedModelDatasourceExpr);
    CodeHelpers.addMethodCall(constructor.getBlock(), editorVariableExpr, "setValueField", getSharedConstantFieldName(referencedModel, referencedValueField));

    if (field.getModelLookup().getDisplayFields().size() == 1) {
      String displayField = field.getModelLookup().getDisplayFields().get(0);
      Field referencedDisplayField = model.getField(displayField);
      Preconditions.checkArgument(null != referencedDisplayField, "Referenced Field %s.%s was not found", field.getModelLookup().getModelName(), displayField);
      CodeHelpers.addMethodCall(constructor.getBlock(), editorVariableExpr, "setDisplayField", getSharedConstantFieldName(referencedModel, referencedDisplayField));
    } else {
      ClassOrInterfaceType listGridFieldType = new ClassOrInterfaceType("ListGridField");
      ImportHelper.addImport(compileUnit, new NameExpr("com.smartgwt.client.widgets.grid.ListGridField"));
      String editorFieldsVariableName = String.format("%sEditorFields", variableNameExpr.getName());
      VariableDeclarationExpr fieldEditorFieldsDeclare = new VariableDeclarationExpr();
      NameExpr fieldEditorFieldsExpr = new NameExpr(editorFieldsVariableName);
      fieldEditorFieldsDeclare.setType(ASTHelper.createReferenceType("ListGridField", 1));
//      fieldEditorFieldsDeclare
      fieldEditorFieldsDeclare.setVars(new ArrayList<VariableDeclarator>());
      ArrayCreationExpr arrayCreate = new ArrayCreationExpr();
      arrayCreate.setType(listGridFieldType);
      arrayCreate.setArrayCount(0);
      arrayCreate.setDimensions(new ArrayList<Expression>());
      arrayCreate.getDimensions().add(new IntegerLiteralExpr(String.format("%s", field.getModelLookup().getDisplayFields().size())));
      fieldEditorFieldsDeclare.getVars().add(new VariableDeclarator(new VariableDeclaratorId(editorFieldsVariableName), arrayCreate));
      constructor.getBlock().getStmts().add(new ExpressionStmt(fieldEditorFieldsDeclare));

//      itemtypeIdFieldEditorFields[0] = new ListGridField(this, this);

      Integer index = 0;
      for (String displayField : field.getModelLookup().getDisplayFields()) {
        Field referencedDisplayField = referencedModel.getField(displayField);
        Preconditions.checkArgument(null != referencedDisplayField, "Referenced Field %s.%s was not found", field.getModelLookup().getModelName(), displayField);

        ObjectCreationExpr createReferencedField = new ObjectCreationExpr();
        createReferencedField.setArgs(new ArrayList<Expression>());
        createReferencedField.setType(listGridFieldType);
        createReferencedField.getArgs().add(getSharedConstantFieldName(referencedModel, referencedDisplayField));
        if (null != referencedDisplayField.getDisplayText() && !referencedDisplayField.getDisplayText().isEmpty()) {
          createReferencedField.getArgs().add(new StringLiteralExpr(referencedDisplayField.getDisplayText()));
        }
        
        ArrayAccessExpr arrayAccess = new ArrayAccessExpr(fieldEditorFieldsExpr, new IntegerLiteralExpr(index.toString()));
        AssignExpr assignArray = new AssignExpr(arrayAccess, createReferencedField, AssignExpr.Operator.assign);
        constructor.getBlock().getStmts().add(new ExpressionStmt(assignArray));
        index++;
      }

      CodeHelpers.addMethodCall(constructor.getBlock(), editorVariableExpr, "setPickListFields", fieldEditorFieldsExpr);
    }

    CodeHelpers.addMethodCall(constructor.getBlock(), editorVariableExpr, "setAutoFetchData", true);
    CodeHelpers.addMethodCall(constructor.getBlock(), variableNameExpr, "setEditorProperties", editorVariableExpr);
  }

  private void processField(Field field) {
    ClassOrInterfaceType fieldType = getDatasourceFieldType(field);
    addFieldImport(fieldType);

    String sharedDataClassName = NameHelper.getSharedDataClassName(model);
    String sharedDataFieldName = NameHelper.getFieldNameConstant(field);

    String variableName = NameHelper.getDatasourceFieldVariableName(field);
    NameExpr variableNameExpr = new NameExpr(variableName);

    VariableDeclarationExpr fieldDeclare = new VariableDeclarationExpr();
    fieldDeclare.setType(fieldType);
    fieldDeclare.setVars(new ArrayList<VariableDeclarator>());

    ObjectCreationExpr createFieldExpression = new ObjectCreationExpr();
    createFieldExpression.setArgs(new ArrayList<Expression>());
    createFieldExpression.getArgs().add(new QualifiedNameExpr(new NameExpr(sharedDataClassName), sharedDataFieldName));
    createFieldExpression.setType(fieldType);

    fieldDeclare.getVars().add(new VariableDeclarator(new VariableDeclaratorId(variableName), createFieldExpression));
    constructor.getBlock().getStmts().add(new ExpressionStmt(fieldDeclare));

    if (field.getPrimaryKey()) {
      CodeHelpers.addMethodCall(constructor.getBlock(), variableNameExpr, "setPrimaryKey", field.getPrimaryKey());
      CodeHelpers.addMethodCall(constructor.getBlock(), variableNameExpr, "setDetail", field.getPrimaryKey());
      CodeHelpers.addMethodCall(constructor.getBlock(), variableNameExpr, "setCanEdit", false);
    }
    
    if(null!=field.getRequired() && field.getRequired()){
      CodeHelpers.addMethodCall(constructor.getBlock(), variableNameExpr, "setRequired", true);
    }
    
    if (field.hasLength()) {
      String sharedDataFieldLength = NameHelper.getFieldLengthConstant(field);
      CodeHelpers.addMethodCall(constructor.getBlock(), variableNameExpr, "setLength", new QualifiedNameExpr(new NameExpr(sharedDataClassName), sharedDataFieldLength));
    }

    if (null != field.getDisplayText() && !field.getDisplayText().isEmpty()) {
      CodeHelpers.addMethodCall(constructor.getBlock(), variableNameExpr, "setTitle", field.getDisplayText());
    }

    if (null != field.getModelLookup()) {
      addLookup(field, variableNameExpr);
    }

    MethodCallExpr calladdField = new MethodCallExpr(new SuperExpr(), "addField");
    ASTHelper.addArgument(calladdField, variableNameExpr);
    constructor.getBlock().getStmts().add(new ExpressionStmt(calladdField));
  }

  @Override
  protected String getPackageName() {
    return settings.getDatasourcePackage();
  }
}

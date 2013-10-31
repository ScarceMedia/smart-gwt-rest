/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.generator;

import com.google.common.base.Preconditions;
import com.scarcemedia.gwt.generator.definition.Definition;
import com.scarcemedia.gwt.generator.definition.Field;
import com.scarcemedia.gwt.generator.definition.FieldType;
import static com.scarcemedia.gwt.generator.definition.FieldType.Boolean;
import static com.scarcemedia.gwt.generator.definition.FieldType.Date;
import static com.scarcemedia.gwt.generator.definition.FieldType.DateTime;
import static com.scarcemedia.gwt.generator.definition.FieldType.Double;
import static com.scarcemedia.gwt.generator.definition.FieldType.Integer;
import com.scarcemedia.gwt.generator.definition.Model;
import com.scarcemedia.gwt.generator.definition.PackageDefinition;
import com.scarcemedia.gwt.generator.util.ImportHelper;
import com.scarcemedia.gwt.generator.util.NameHelper;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.type.ClassOrInterfaceType;
import java.util.ArrayList;

/**
 *
 * @author jeremy
 */
public abstract class AbstractGenerator {

  protected CompilationUnit compileUnit;
  protected final PackageDefinition packageDefinition;
  protected final Model model;
  protected final Settings settings;
  protected final Definition definition;

  public AbstractGenerator(Settings settings, Definition definition, PackageDefinition packageDefinition, Model model) {
    Preconditions.checkArgument(null != settings, "settings cannot be null");
    Preconditions.checkArgument(null != definition, "definition cannot be null");
    Preconditions.checkArgument(null != packageDefinition, "packageDefinition cannot be null.");
    Preconditions.checkArgument(null != model, "model cannot be null");

    this.settings = settings;
    this.definition = definition;
    this.packageDefinition = packageDefinition;
    this.model = model;
  }

  protected abstract void onGenerate();

  protected abstract String getPackageName();

  protected ClassOrInterfaceType getFieldType(Field field) {
    ClassOrInterfaceType result = null;
    switch (field.getType()) {
      case Boolean:
        result = new ClassOrInterfaceType("Boolean");
        break;
      case Date:
      case DateTime:
        result = new ClassOrInterfaceType("java.util.Date");
        break;
      case Double:
        result = new ClassOrInterfaceType("Double");
        break;
      case Integer:
        result = new ClassOrInterfaceType("Integer");
        break;
      case String:
        result = new ClassOrInterfaceType("String");
        break;
      default:
        throw new UnsupportedOperationException(String.format("%s is not a supported field type.", field.getType()));
//        throw new 
    }
    return result;
  }

  protected Model getModel(String name) {
    Preconditions.checkArgument(null != name && !name.isEmpty(), "name cannot be null");
    for (PackageDefinition pd : definition.getPackages()) {
      for (Model m : pd.getModels()) {
        if (m.getName().equals(name)) {
          return m;
        }
      }
    }

    return null;
  }

  protected void addDAOInterfaceImport(Model m) {
    String sharedConstantsImport = String.format("%s.%s",
            settings.getDAOPackage(),
            NameHelper.getDAOInterfaceName(m));
    ImportHelper.addImport(compileUnit, new NameExpr(sharedConstantsImport));
  }

  protected void addDAOImplImport(Model m) {
    String sharedConstantsImport = String.format("%s.%s",
            settings.getDAOPackage(),
            NameHelper.getDAOImplName(m));
    ImportHelper.addImport(compileUnit, new NameExpr(sharedConstantsImport));
  }

  protected void addSharedConstantsImport(Model m) {
    String sharedConstantsImport = String.format("%s.%s",
            settings.getSharedPackage(),
            NameHelper.getSharedDataClassName(m));
    ImportHelper.addImport(compileUnit, new NameExpr(sharedConstantsImport));
  }

  protected NameExpr getSharedConstantFieldName(Model m, Field field) {
    addSharedConstantsImport(m);
    String sharedConstantsClass = NameHelper.getSharedDataClassName(m);
    QualifiedNameExpr qualifiedName = new QualifiedNameExpr(new NameExpr(sharedConstantsClass), NameHelper.getFieldNameConstant(field));
    return qualifiedName;
  }

  protected NameExpr getSharedConstantFieldLength(Model m, Field field) {
    addSharedConstantsImport(m);
    String sharedConstantsClass = NameHelper.getSharedDataClassName(m);
    QualifiedNameExpr qualifiedName = new QualifiedNameExpr(new NameExpr(sharedConstantsClass), NameHelper.getFieldLengthConstant(field));
    return qualifiedName;
  }

  public CompilationUnit generate() {
    compileUnit = new CompilationUnit();
    PackageDeclaration packageDeclaration = new PackageDeclaration();
    packageDeclaration.setName(new NameExpr(getPackageName()));
    compileUnit.setPackage(packageDeclaration);
    compileUnit.setTypes(new ArrayList<TypeDeclaration>());
    onGenerate();

    return compileUnit;
  }
}

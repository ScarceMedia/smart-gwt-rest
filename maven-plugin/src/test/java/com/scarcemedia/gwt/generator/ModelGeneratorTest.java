/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.generator;

import com.scarcemedia.gwt.generator.definition.Field;
import com.scarcemedia.gwt.generator.definition.FieldType;
import com.scarcemedia.gwt.generator.definition.Model;
import com.scarcemedia.gwt.generator.definition.PackageDefinition;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.expr.NameExpr;
import org.junit.Assert;
import org.junit.Before;

/**
 *
 * @author jeremy
 */
public abstract class ModelGeneratorTest<T extends AbstractModelGenerator> {
  protected T generator;
  protected PackageDefinition pkg;
  protected Model model;
//  protected Settings settings = new Settings(null)
  @Before
  public void setup() {
//    generator = getGenerator();
//    generator.setSharedNamespace("com.example.shared");
//    generator.setDatasourceNamespace("com.example.client.datasource");
//    pkg = new PackageDefinition();
//    pkg.setName("com.example.client.datasource");
//    pkg.setPersistenceUnit("main");
//    generator.setPackageDefinition(pkg);
//
//    model = new Model();
//    model.setName("Item");
//    model.setTable("item");
//    generator.setModel(model);
//    
//    Field itemID = new Field();
//    itemID.setName("item_id");
//    itemID.setPrimaryKey(true);
//    itemID.setType(FieldType.Integer);
//    itemID.setRequired(true);
//    model.getFields().add(itemID);
  }

  protected abstract T getGenerator();
  
  protected void assertCompileUnit(CompilationUnit compileUnit, String packageName) {
    Assert.assertNotNull("compileUnit should not be null.", compileUnit);
    Assert.assertNotNull("compileUnit.getPackage() should not be null.", compileUnit.getPackage());
    Assert.assertEquals("compileUnit.getPackage().getName() does not match.", new NameExpr(packageName), compileUnit.getPackage().getName());
  }
}

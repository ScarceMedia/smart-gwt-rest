/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.generator.datasource;

import com.scarcemedia.gwt.generator.AbstractGeneratorTest;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.TypeDeclaration;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author jeremy
 */
//public class DatasourceGeneratorTest extends ModelGeneratorTest<DatasourceGenerator> {
//
//  @Override
//  protected DatasourceGenerator getGenerator() {
//    return new DatasourceGenerator();
//  }
//
//  @Test
//  public void getDatasourceName(){
//    Assert.assertEquals("ItemDatasource", generator.getDatasourceName());
//  }
//  
//  @Test
//  public void generate() {
//    CompilationUnit result = generator.generate();
//    assertCompileUnit(result, pkg.getName());
//
//    Assert.assertNotNull("types should not be null", result.getTypes());
//    Assert.assertFalse("types should not be empty", result.getTypes().isEmpty());
//
//    TypeDeclaration typeDeclaration = result.getTypes().get(0);
//    Assert.assertEquals("Class name does not match.", generator.getDatasourceName(), typeDeclaration.getName());
//
//    System.out.println(result);
//  }
//}

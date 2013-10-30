/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.generator.shared;

import com.scarcemedia.gwt.generator.ModelGeneratorTest;
import com.scarcemedia.gwt.generator.util.NameHelper;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.TypeDeclaration;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author jeremy
 */
//public class ModelConstantsGeneratorTest extends ModelGeneratorTest<ModelConstantsGenerator> {
//
//  @Override
//  protected ModelConstantsGenerator getGenerator() {
//    return new ModelConstantsGenerator();
//  }
//  
//  @Test
//  public void test(){
//    CompilationUnit result = generator.generate();
//    assertCompileUnit(result, generator.getSharedNamespace());
//
//    Assert.assertNotNull("types should not be null", result.getTypes());
//    Assert.assertFalse("types should not be empty", result.getTypes().isEmpty());
//
//    TypeDeclaration typeDeclaration = result.getTypes().get(0);
//    Assert.assertEquals("Class name does not match.", NameHelper.getSharedDataClassName(model), typeDeclaration.getName());
//
//    System.out.println(result);
//    
//    
//  }
//  
//  
//}

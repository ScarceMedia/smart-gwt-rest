/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.generator;

import com.scarcemedia.gwt.generator.definition.Definition;
import com.scarcemedia.gwt.generator.definition.DefinitionTest;
import com.scarcemedia.gwt.generator.definition.Field;
import com.scarcemedia.gwt.generator.definition.Model;
import com.scarcemedia.gwt.generator.definition.PackageDefinition;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.expr.NameExpr;
import org.junit.Assert;
import org.junit.Before;

/**
 *
 * @author jeremy
 * @param <T>
 */
public abstract class AbstractGeneratorTest<T extends AbstractGenerator> {

  protected T generator;
  protected PackageDefinition pkg;
  protected Model model;
  protected Definition definition;
//  protected Settings settings = new Settings(null)

  @Before
  public void setup() {
    definition = DefinitionTest.getTestDefinition();
    pkg = definition.getPackages().get(0);
    model = pkg.getModels().get(0);

    generator = createGenerator(new TestSettings(), definition, pkg, model);
  }

  protected abstract T createGenerator(Settings settings, Definition definition, PackageDefinition packageDefinition, Model model);

  protected void assertCompileUnit(CompilationUnit compileUnit, String packageName) {
    Assert.assertNotNull("compileUnit should not be null.", compileUnit);
    Assert.assertNotNull("compileUnit.getPackage() should not be null.", compileUnit.getPackage());
    Assert.assertEquals("compileUnit.getPackage().getName() does not match.", new NameExpr(packageName), compileUnit.getPackage().getName());
  }

  protected CompilationUnit generate() {
    CompilationUnit compileUnit = generator.generate();
    System.out.print(compileUnit);
    return compileUnit;
  }
}

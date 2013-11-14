/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.generator.guice;

import com.scarcemedia.gwt.generator.AbstractGeneratorTest;
import com.scarcemedia.gwt.generator.Settings;
import com.scarcemedia.gwt.generator.definition.Definition;
import com.scarcemedia.gwt.generator.definition.Model;
import com.scarcemedia.gwt.generator.definition.PackageDefinition;
import japa.parser.ast.CompilationUnit;
import org.junit.Test;

/**
 *
 * @author jeremy
 */
public class JPAInitializerGeneratorTest extends AbstractGeneratorTest<JPAInitializerGenerator> {

  @Override
  protected JPAInitializerGenerator createGenerator(Settings settings, Definition definition, PackageDefinition packageDefinition, Model model) {
    return new JPAInitializerGenerator(settings, definition, packageDefinition, model);
  }

  @Test
  public void test() {
    CompilationUnit compileUnit = generate();
  }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.generator.mojo;

import com.scarcemedia.gwt.generator.AbstractGenerator;
import com.scarcemedia.gwt.generator.AbstractGeneratorMojo;
import com.scarcemedia.gwt.generator.Settings;
import com.scarcemedia.gwt.generator.definition.Definition;
import com.scarcemedia.gwt.generator.definition.Model;
import com.scarcemedia.gwt.generator.definition.PackageDefinition;
import com.scarcemedia.gwt.generator.shared.ModelConstantsGenerator;

/**
 * @phase generate-sources
 * @goal sharedconstants
 * @author jeremy
 */
public class SharedConstantsMojo extends AbstractGeneratorMojo {

  @Override
  protected AbstractGenerator[] createModelGenerators(Settings settings, Definition definition, PackageDefinition packageDefinition, Model model) {
    return new AbstractGenerator[]{new ModelConstantsGenerator(settings, definition, packageDefinition, model)};
  }
}

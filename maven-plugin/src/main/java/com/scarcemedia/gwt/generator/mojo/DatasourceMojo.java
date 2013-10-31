/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.generator.mojo;

import com.scarcemedia.gwt.generator.AbstractGenerator;
import com.scarcemedia.gwt.generator.AbstractGeneratorMojo;
import com.scarcemedia.gwt.generator.Settings;
import com.scarcemedia.gwt.generator.datasource.DatasourceGenerator;
import com.scarcemedia.gwt.generator.definition.Definition;
import com.scarcemedia.gwt.generator.definition.Model;
import com.scarcemedia.gwt.generator.definition.PackageDefinition;

/**
 * @phase generate-sources
 * @author jeremy
 * @goal datasource
 */
public class DatasourceMojo extends AbstractGeneratorMojo {

  @Override
  protected AbstractGenerator[] createModelGenerators(Settings settings, Definition definition, PackageDefinition packageDefinition, Model model) {
    return new AbstractGenerator[]{ new DatasourceGenerator(settings, definition, packageDefinition, model)};
  }
}

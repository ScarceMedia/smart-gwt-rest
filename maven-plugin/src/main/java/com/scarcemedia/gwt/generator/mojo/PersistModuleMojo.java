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
import com.scarcemedia.gwt.generator.guice.ModulePersistenceLifeCycleManagerGenerator;
import com.scarcemedia.gwt.generator.guice.PersistFilterGenerator;
import com.scarcemedia.gwt.generator.guice.PersistModuleGenerator;
import com.scarcemedia.gwt.generator.guice.PersistenceLifeCycleManagerGenerator;
import com.scarcemedia.gwt.generator.guice.PersistenceServiceAttributeGenerator;

/**
 * @phase generate-sources
 * @author jeremy
 * @goal persist
 */
public class PersistModuleMojo extends AbstractGeneratorMojo {

  @Override
  protected AbstractGenerator[] createModelGenerators(Settings settings, Definition definition, PackageDefinition packageDefinition, Model model) {
    return new AbstractGenerator[]{
      new PersistModuleGenerator(settings, definition, packageDefinition, model),
      new PersistenceLifeCycleManagerGenerator(settings, definition, packageDefinition, model),
      new PersistFilterGenerator(settings, definition, packageDefinition, model),
      new PersistenceServiceAttributeGenerator(settings, definition, packageDefinition, model),
      new ModulePersistenceLifeCycleManagerGenerator(settings, definition, packageDefinition, model)
    };
  }
}

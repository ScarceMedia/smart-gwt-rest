/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.generator.mojo;

import com.scarcemedia.gwt.generator.AbstractModelGenerator;
import com.scarcemedia.gwt.generator.AbstractModelGeneratorMojo;
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
public class PersistModuleMojo extends AbstractModelGeneratorMojo {

  @Override
  protected AbstractModelGenerator[] createModelGenerators(Settings settings, Definition definition, PackageDefinition packageDefinition, Model model) {
    return new AbstractModelGenerator[]{
      new PersistModuleGenerator(settings, definition, packageDefinition, model),
      new PersistenceLifeCycleManagerGenerator(settings, definition, packageDefinition, model),
      new PersistFilterGenerator(settings, definition, packageDefinition, model),
      new PersistenceServiceAttributeGenerator(settings, definition, packageDefinition, model),
      new ModulePersistenceLifeCycleManagerGenerator(settings, definition, packageDefinition, model)
    };
  }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.generator;

import com.google.common.base.Preconditions;
import com.scarcemedia.gwt.generator.definition.Definition;
import com.scarcemedia.gwt.generator.definition.Model;
import com.scarcemedia.gwt.generator.definition.PackageDefinition;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.TypeDeclaration;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 *
 * @author jeremy
 */
public abstract class AbstractGeneratorMojo extends AbstractMojo {

  /**
   * Folder where generated-source will be created (automatically added to
   * compile classpath).
   *
   * @parameter
   * default-value="${project.build.directory}/generated-sources/scarcemedia-rest"
   * @required
   */
  protected File generateDirectory;
  /**
   * The maven project descriptor
   *
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
  protected MavenProject project;
  /**
   * @parameter @required
   */
  String sharedPackage;
  /**
   * @parameter @required
   */
  String daoPackage;
  /**
   * @parameter @required
   */
  String datasourcePackage;
  /**
   * @parameter @required
   */
  String resourceClass;
  /**
   * @parameter @required
   */
  String guicePersistPackage;
  /**
   * Folder where generated-source will be created (automatically added to
   * compile classpath).
   *
   * @parameter default-value="${basedir}/src/main/definition/definition.xml"
   * @required
   */
  protected File definitionFile;

  public File getGenerateDirectory() {
    return generateDirectory;
  }

  public Settings getSettings() {
    return new MavenSettings(this);
  }

  protected void write(CompilationUnit compileUnit) {
    Preconditions.checkArgument(null != compileUnit, "compileUnit cannot be null.");
    Preconditions.checkArgument(null != compileUnit.getTypes() && !compileUnit.getTypes().isEmpty(), "compileUnit.getTypes() cannot be null or empty.");

    TypeDeclaration firstType = compileUnit.getTypes().get(0);

    String fileName = String.format("%s.java", firstType.getName());

    String packageName = compileUnit.getPackage().getName().toString();
    getLog().debug("packageName=" + packageName);
    String[] packageParts = packageName.split("\\.");
    getLog().debug("packageParts.length=" + packageParts.length);
    File directory = null;

    for (String packagePart : packageParts) {
      directory = new File(directory == null ? generateDirectory : directory, packagePart);
    }

    if (!directory.isDirectory()) {
      directory.mkdirs();
    }

    File filePath = new File(directory, fileName);
    try {
      getLog().info("Writing " + filePath.getAbsolutePath());
      PrintStream iostr = new PrintStream(filePath);
      iostr.print(compileUnit);
      iostr.close();
    } catch (FileNotFoundException ex) {
    }
  }

  public void execute() throws MojoExecutionException, MojoFailureException {
    Definition definition = load();

    for (PackageDefinition packageDefinition : definition.getPackages()) {
      for (Model model : packageDefinition.getModels()) {
        AbstractGenerator[] generators = createModelGenerators(getSettings(), definition, packageDefinition, model);

        for (AbstractGenerator generator : generators) {
          CompilationUnit compileUnit = generator.generate();
          write(compileUnit);
        }
      }
    }
  }

  protected Definition load() throws MojoExecutionException, MojoFailureException {
    if (null == definitionFile) {
      throw new MojoFailureException("packageDefinitionFile cannot be null.");
    }

    if (!definitionFile.exists()) {
      throw new MojoFailureException(definitionFile.getAbsoluteFile() + " does not exist.");
    }

    try {
      return Definition.load(definitionFile);
    } catch (IOException ex) {
      throw new MojoFailureException("Exception while loading " + definitionFile.getAbsolutePath(), ex);
    }

  }

  protected abstract AbstractGenerator[] createModelGenerators(Settings settings, Definition definition, PackageDefinition packageDefinition, Model model);
}

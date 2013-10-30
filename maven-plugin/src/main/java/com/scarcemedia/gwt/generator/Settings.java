/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.generator;

import com.google.common.base.Preconditions;

/**
 *
 * @author jeremy
 */
public class Settings {

  String daoNamespace;
  AbstractModelGeneratorMojo mojo;

  public Settings(AbstractModelGeneratorMojo mojo) {
    this.mojo = mojo;
  }

  public String getSharedPackage() {
    Preconditions.checkArgument(null != mojo.sharedPackage && !mojo.sharedPackage.isEmpty(), "sharedPackage cannot be null.");
    return mojo.sharedPackage;
  }

  public String getDAOPackage() {
    Preconditions.checkArgument(null != mojo.daoPackage && !mojo.daoPackage.isEmpty(), "daoPackage cannot be null.");
    return mojo.daoPackage;
  }

  public String getDatasourcePackage() {
    Preconditions.checkArgument(null != mojo.datasourcePackage && !mojo.datasourcePackage.isEmpty(), "datasourcePackage cannot be null.");
    return mojo.datasourcePackage;
  }

  public String getGuicePersistPackage() {
    Preconditions.checkArgument(null != mojo.guicePersistPackage && !mojo.guicePersistPackage.isEmpty(), "guicePersistPackage cannot be null.");
    return mojo.guicePersistPackage;
  }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.scarcemedia.gwt.generator;

/**
 *
 * @author jeremy
 */
public class TestSettings implements Settings {

  @Override
  public String getSharedPackage() {
    return "com.example.foo.shared";
  }

  @Override
  public String getDAOPackage() {
    return "com.example.foo.server.dao";
  }

  @Override
  public String getDatasourcePackage() {
    return "com.example.foo.client.datasource";
  }

  @Override
  public String getGuicePersistPackage() {
    return "com.example.foo.client.guice";
  }
  
}

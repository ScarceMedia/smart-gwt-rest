/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.guice;

/**
 *
 * @author jeremy
 */
public interface PersistenceLifeCycleManager {

  void startService();

  void stopService();

  void beginUnitOfWork();

  void endUnitOfWork();
}

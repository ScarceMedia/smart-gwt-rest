/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.guice;

import com.google.inject.persist.PersistService;
import com.google.inject.persist.UnitOfWork;

/**
 *
 * @author jeremy
 */
public abstract class AbstractPersistenceLifeCycleManager implements PersistenceLifeCycleManager {

  final UnitOfWork unitOfWork;
  final PersistService persistService;

  public AbstractPersistenceLifeCycleManager(UnitOfWork unitOfWork,
          PersistService persistService) {
    this.unitOfWork = unitOfWork;
    this.persistService = persistService;
  }

  @Override
  public void startService() {
    this.persistService.start();
  }

  @Override
  public void stopService() {
    this.persistService.stop();
  }

  @Override
  public void beginUnitOfWork() {
    this.unitOfWork.begin();
  }

  @Override
  public void endUnitOfWork() {
    this.unitOfWork.end();
  }
}

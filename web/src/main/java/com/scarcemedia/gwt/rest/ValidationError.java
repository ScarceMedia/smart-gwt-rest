/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.rest;

import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author jeremy
 */
public class ValidationError {

  @XmlElement(name = "errorMessage")
  String errorMessage;

  public ValidationError() {
  }

  public ValidationError(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}

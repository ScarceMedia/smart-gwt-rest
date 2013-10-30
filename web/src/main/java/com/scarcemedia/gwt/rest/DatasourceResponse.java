/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jeremy
 */
@XmlRootElement(name = "response")
public abstract class DatasourceResponse<T> {

  public static int STATUS_FAILURE = -1;
  public static int STATUS_LOGIN_INCORRECT = -5;
  public static int STATUS_LOGIN_REQUIRED = -7;
  public static int STATUS_LOGIN_SUCCESS = -8;
  public static int STATUS_MAX_LOGIN_ATTEMPTS_EXCEEDED = -6;
  public static int STATUS_SERVER_TIMEOUT = -100;
  public static int STATUS_SUCCESS = 0;
  public static int STATUS_TRANSPORT_ERROR = -90;
  public static int STATUS_VALIDATION_ERROR = -4;
  @XmlElement
  int status;
  @XmlElement
  int startRow;
  @XmlElement
  int endRow;
  @XmlElement
  int totalRows;
  @XmlElement(name = "errors")
  Map<String, List<ValidationError>> errors;
  @XmlElementWrapper(name = "data")
  @XmlElement(name = "record")
  List<T> data;

  public int getStartRow() {
    return startRow;
  }

  public void setStartRow(int startRow) {
    this.startRow = startRow;
  }

  public int getEndRow() {
    return endRow;
  }

  public void setEndRow(int endRow) {
    this.endRow = endRow;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int getStatus() {
    return status;
  }

  public void setTotalRows(int totalRows) {
    this.totalRows = totalRows;
  }

  public int getTotalRows() {
    return totalRows;
  }

  public void addError(String field, String errorMessage) {
    this.status = STATUS_VALIDATION_ERROR;

    if (null == errors) {
      errors = new HashMap<String, List<ValidationError>>();
    }

    List<ValidationError> fieldErrors = errors.get(field);

    if (null == fieldErrors) {
      fieldErrors = new ArrayList<ValidationError>();
    }

    ValidationError error = new ValidationError(errorMessage);
    fieldErrors.add(error);
    errors.put(field, fieldErrors);
  }

  public boolean hasErrors() {
    return this.status == STATUS_VALIDATION_ERROR;
  }

  public List<T> getMessages() {
    return data;
  }

  public void setMessages(List<T> value) {
    this.data = value;
  }

  public void addMessage(T item) {
    if (data == null) {
      data = new ArrayList<T>();
    }
    this.data.add(item);
  }
}

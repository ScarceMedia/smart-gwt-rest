/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.rest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jeremy
 */
@XmlRootElement(name = "request")
public abstract class DatasourceRequest<T> {

  @XmlElement
  String dataSource;
  @XmlElement
  OperationType operationType;
  @XmlElement
  int startRow;
  @XmlElement
  int endRow;
  @XmlElement
  String componentId;

  public OperationType getOperationType() {
    return operationType;
  }

  public void setOperationType(OperationType operationType) {
    this.operationType = operationType;
  }

  public int getStartRow() {
    return startRow;
  }

  public void setStartRow(int startRow) {
    this.startRow = startRow;
  }

  public int getEndRow() {
    return endRow;
  }

  /**
   *
   * @param endRow
   */
  public void setEndRow(int endRow) {
    this.endRow = endRow;
  }

  /**
   * Id of the component that cause the request to be issued.
   *
   * @return
   */
  public void setComponentId(String componentId) {
    this.componentId = componentId;
  }

  /**
   * Id of the component that cause the request to be issued.
   *
   * @return
   */
  public String getComponentId() {
    return componentId;
  }

  /**
   * Id of the datasource that issued the request.
   *
   * @return
   */
  public void setDataSource(String dataSource) {
    this.dataSource = dataSource;
  }

  /**
   * Id of the datasource that issued the request.
   *
   * @return
   */
  public String getDataSource() {
    return dataSource;
  }
  
  @XmlElement(name = "data")
  T data;
  @XmlElement(name = "oldValues")
  T oldValues;

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public T getOldValues() {
    return oldValues;
  }

  public void setOldValues(T oldValues) {
    this.oldValues = oldValues;
  }
}

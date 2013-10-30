/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.rest;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

/**
 *
 * @author jeremy
 */
@XmlEnum(String.class)
public enum OperationType {

  @XmlEnumValue("add")
  add,
  @XmlEnumValue("fetch")
  fetch,
  @XmlEnumValue("update")
  update,
  @XmlEnumValue("remove")
  remove
}

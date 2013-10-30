/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.generator.definition;

import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author jeremy
 */
@XmlType
public class ModelLookup {

  @XmlAttribute(name = "modelName")
  String modelName;
  @XmlAttribute(name = "valueField")
  String valueField;
  @XmlElement(name = "displayField")
  List<String> displayFields;

  @XmlTransient
  public List<String> getDisplayFields() {
    return displayFields;
  }

  public void setDisplayFields(List<String> displayFields) {
    this.displayFields = displayFields;
  }

  @XmlTransient
  public String getModelName() {
    return modelName;
  }

  public void setModelName(String modelName) {
    this.modelName = modelName;
  }

  @XmlTransient
  public String getValueField() {
    return valueField;
  }

  public void setValueField(String valueField) {
    this.valueField = valueField;
  }
}

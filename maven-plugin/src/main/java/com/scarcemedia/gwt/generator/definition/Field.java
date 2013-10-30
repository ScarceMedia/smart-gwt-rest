/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.generator.definition;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jeremy
 */
public class Field {

  @XmlAttribute(name = "name")
  String name;
  @XmlAttribute(name = "type")
  FieldType type = FieldType.String;
  @XmlAttribute(name = "primaryKey")
  boolean primaryKey;
  @XmlAttribute(name = "required")
  Boolean required;
  @XmlAttribute(name = "nullable")
  boolean nullable;
  @XmlAttribute(name = "xmlignore")
  boolean xmlignore;
  @XmlAttribute(name = "length")
  Integer length;
  @XmlElement(name="displayText")
  String displayText;
  @XmlElement(name="modelLookup")
  ModelLookup modelLookup;
  
  @XmlTransient
  public String getName() {
    return name;
  }

  public void setName(String value) {
    this.name = value;
  }

  @XmlTransient
  public FieldType getType() {
    return type;
  }

  public void setType(FieldType value) {
    this.type = value;
  }

  @XmlTransient
  public boolean getPrimaryKey() {
    return primaryKey;
  }

  public void setPrimaryKey(boolean primaryKey) {
    this.primaryKey = primaryKey;
  }

  @XmlTransient
  public Boolean getRequired() {
    return required;
  }

  public void setRequired(Boolean required) {
    this.required = required;
  }

  @XmlTransient
  public boolean getNullable() {
    return nullable;
  }

  public void setNullable(boolean value) {
    this.nullable = value;
  }

  @XmlTransient
  public boolean getXmlIgnore() {
    return xmlignore;
  }

  public void setXmlIgnore(boolean value) {
    this.xmlignore = value;
  }

  @XmlTransient
  public Integer getLength() {
    return length;
  }

  public void setLength(Integer length) {
    this.length = length;
  }

  @XmlTransient
  public String getDisplayText() {
    return displayText;
  }

  public void setDisplayText(String displayText) {
    this.displayText = displayText;
  }

  @XmlTransient
  public ModelLookup getModelLookup() {
    return modelLookup;
  }

  public void setModelLookup(ModelLookup modelLookup) {
    this.modelLookup = modelLookup;
  }
  
  
  

  /**
   * Method is used to determine if a field type has length.
   *
   * @param field
   * @return
   */
//  @XmlTransient
  public boolean hasLength() {
    if (type == FieldType.String) {
      if (length != null) {
        return true;
      }
    }

    return false;
  }
}

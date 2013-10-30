/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.generator.definition;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jeremy
 */
@XmlRootElement(name = "package")
public class PackageDefinition {

  public static PackageDefinition load(File packageDefinitionFile) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @XmlAttribute(name = "name")
  String name;
  @XmlAttribute(name = "persistenceUnit")
  String persistenceUnit;
//  @XmlElementWrapper(name = "models")
  @XmlElement(name = "model")
  List<Model> models = new ArrayList<Model>();

  @XmlTransient
  public String getName() {
    return name;
  }

  @XmlTransient
  public String getPersistenceUnit() {
    return persistenceUnit;
  }

  public void setName(String value) {
    this.name = value;
  }

  public void setPersistenceUnit(String value) {
    this.persistenceUnit = value;
  }

  @XmlTransient
  public List<Model> getModels() {
    return models;
  }

  public void setModels(List<Model> value) {
    this.models = value;
  }
}

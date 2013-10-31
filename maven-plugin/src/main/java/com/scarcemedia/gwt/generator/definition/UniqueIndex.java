/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.generator.definition;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jeremy
 */
@XmlRootElement(name = "uniqueIndex")
public class UniqueIndex {

  @XmlElement(name = "field")
  List<String> fields = new ArrayList<String>();

  @XmlTransient
  public List<String> getFields() {
    return fields;
  }

  public void setFields(List<String> fields) {
    this.fields = fields;
  }
   
}

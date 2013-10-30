/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.generator.definition;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jeremy
 */
@XmlRootElement(name = "model")
public class Model {

  @XmlAttribute(name = "name")
  String name;
  @XmlAttribute(name = "table")
  String table;
  @XmlElement(name = "field")
  List<Field> fields = new ArrayList<Field>();

  @XmlTransient
  public String getName() {
    return name;
  }

  public void setName(String value) {
    this.name = value;
  }

  @XmlTransient
  public String getTable() {
    return table;
  }

  public void setTable(String value) {
    this.table = value;
  }

  @XmlTransient
  public List<Field> getFields() {
    return fields;
  }

  public void setFields(List<Field> value) {
    this.fields = value;
  }

  @XmlTransient
  public Field getPrimaryKey() {
    for (Field field : this.fields) {
      if (field.getPrimaryKey()) {
        return field;
      }
    }

    return null;
  }


  public Field getField(String name) {
    Preconditions.checkArgument(null!=name&&!name.isEmpty(), "name cannot be null.");
    for (Field field : fields) {
      if (name.equals(field.getName())) {
        return field;
      }
    }
    return null;

  }
}

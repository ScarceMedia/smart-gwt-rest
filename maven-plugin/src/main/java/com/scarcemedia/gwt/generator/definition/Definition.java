/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.generator.definition;

import com.google.common.base.Preconditions;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jeremy
 */
@XmlRootElement(name = "definition")
public class Definition {

  @XmlElement(name = "package")
  List<PackageDefinition> packages = new ArrayList<PackageDefinition>();

  @XmlTransient
  public List<PackageDefinition> getPackages() {
    return packages;
  }

  public void setPackages(List<PackageDefinition> value) {
    this.packages = value;
  }

  public static void save(File outputPath, Definition definitionFile) throws IOException {
    Preconditions.checkArgument(null != outputPath, "outputPath cannot be null.");
    Preconditions.checkArgument(null != definitionFile, "definitionFile cannot be null.");

    try {
      JAXBContext context = JAXBContext.newInstance(Definition.class);
      Marshaller marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//      marshaller.setProperty(null, context);
      marshaller.marshal(definitionFile, outputPath);
    } catch (Exception ex) {
      throw new IOException("Exception thrown while saving " + outputPath, ex);
    }

  }

  public static Definition load(File file) throws IOException {
    Preconditions.checkArgument(null != file, "File cannot be null.");

    try {
      JAXBContext context = JAXBContext.newInstance(Definition.class);
      Unmarshaller unmarshaller = context.createUnmarshaller();
      return (Definition) unmarshaller.unmarshal(file);
    } catch (Exception ex) {
      throw new IOException("Exception thrown while loading " + file, ex);
    }

  }
}

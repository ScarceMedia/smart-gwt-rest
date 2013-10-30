/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.rest.provider;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.Provider;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.SerializationConfig;

/**
 *
 * @author jeremy
 */
@Provider
@Consumes(value = {"application/json", "text/json"})
@Produces(value = {"application/json", "text/json"})
public class SmartJacksonJaxbJsonProvider extends JacksonJaxbJsonProvider {
  public SmartJacksonJaxbJsonProvider(){
    this(false);
  }
  public SmartJacksonJaxbJsonProvider(boolean indent_output) {
    super();
    configure(SerializationConfig.Feature.USE_ANNOTATIONS, true);
    configure(DeserializationConfig.Feature.USE_ANNOTATIONS, true);
    configure(SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING, false);
    configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, true);
    configure(SerializationConfig.Feature.INDENT_OUTPUT, indent_output);
    configure(SerializationConfig.Feature.AUTO_DETECT_GETTERS, false);
    configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
    configure(DeserializationConfig.Feature.READ_ENUMS_USING_TO_STRING, false);
    configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }
}

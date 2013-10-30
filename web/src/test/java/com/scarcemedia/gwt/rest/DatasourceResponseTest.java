/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.rest;

import com.scarcemedia.gwt.rest.provider.SmartJacksonJaxbJsonProvider;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.ws.rs.core.MediaType;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author jeremy
 */
public class DatasourceResponseTest {

  ObjectMapper mapper;
  MockDatasourceResponse response;

  @Before
  public void setup() {
    SmartJacksonJaxbJsonProvider jsonProvider = new SmartJacksonJaxbJsonProvider(true);
    mapper = jsonProvider.locateMapper(MockDatasourceResponse.class, MediaType.APPLICATION_JSON_TYPE);
  }

  @Before
  public void setupResponse() {
    response = new MockDatasourceResponse();
    response.setStatus(MockDatasourceResponse.STATUS_SUCCESS);
    response.setStartRow(0);
    response.setEndRow(1);
    response.setTotalRows(1);
    
    MockItem item = new MockItem();
    item.setItemID("asdfasa");
    response.addMessage(item);
  }

  @Test
  public void serialize() throws IOException {
    
    ByteArrayOutputStream outstr = new ByteArrayOutputStream();
    mapper.writeValue(System.out, response);
    mapper.writeValue(outstr, response);
    
    MockDatasourceResponse actual = mapper.readValue(outstr.toByteArray(), 0, outstr.size(), MockDatasourceResponse.class);
    Assert.assertNotNull("actual should not be null", actual);
  }
}

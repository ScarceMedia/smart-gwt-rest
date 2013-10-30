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
public class DatasourceRequestTest {

  ObjectMapper mapper;
  MockDatasourceRequest request;

  @Before
  public void setup() {
    SmartJacksonJaxbJsonProvider jsonProvider = new SmartJacksonJaxbJsonProvider(true);
    mapper = jsonProvider.locateMapper(MockDatasourceRequest.class, MediaType.APPLICATION_JSON_TYPE);
  }

  @Before
  public void setupRequest() {
    request = new MockDatasourceRequest();
    request.setComponentId("listgrid1");
    request.setOperationType(OperationType.update);
    request.setDataSource("mockDatasource");
    request.setData(new MockItem());
    request.setOldValues(new MockItem());

    request.getData().setItemID("adfoasdfas");
    request.getOldValues().setItemID("adfoasdfas");
  }

  @Test
  public void serialize() throws IOException {

    ByteArrayOutputStream outstr = new ByteArrayOutputStream();
    mapper.writeValue(outstr, request);
    mapper.writeValue(System.out, request);

    MockDatasourceRequest actual = mapper.readValue(outstr.toByteArray(), 0, outstr.size(), MockDatasourceRequest.class);
    Assert.assertNotNull("actual should not be null", actual);
  }
}

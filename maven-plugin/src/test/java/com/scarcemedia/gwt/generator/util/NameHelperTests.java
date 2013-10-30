/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.generator.util;

import com.scarcemedia.gwt.generator.util.NameHelper;
import com.scarcemedia.gwt.generator.definition.Model;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author jeremy
 */
public class NameHelperTests {

  @Test(expected = IllegalArgumentException.class)
  public void getDatasourceName_null() {
    NameHelper.getDataSourceName(null);
  }

  @Test
  public void getDatasourceName() {
    Model model = new Model();
    model.setName("Item");
    
    final String expected = "ItemDatasource";
    final String actual = NameHelper.getDataSourceName(model);
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void getVariableName(){
    final String input = "item_id";
    final String expected = "itemId";
    
    String actual = NameHelper.getVariableName(input);
    Assert.assertEquals(expected, actual);
  }
  
  
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.generator.util;

import com.google.common.base.Preconditions;
import com.scarcemedia.gwt.generator.definition.Field;
import com.scarcemedia.gwt.generator.definition.Model;
import com.scarcemedia.gwt.generator.definition.PackageDefinition;
import java.util.regex.Pattern;

/**
 *
 * @author jeremy
 */
public class NameHelper {

  static Pattern pattern = Pattern.compile("[^a-z0-9]");

  public static String getMethodName(Field field) {
    return getMethodName(field.getName());
  }

  public static String getMethodName(String name) {
    Preconditions.checkArgument(null != name && !name.isEmpty(), "name cannot be null");
    String[] parts = pattern.split(name);

    StringBuilder builder = new StringBuilder();

    for (String part : parts) {
      builder.append(part.substring(0, 1).toUpperCase());
      builder.append(part.substring(1));
    }

    return builder.toString();
  }

  public static String getVariableName(Field field) {
    Preconditions.checkArgument(null != field, "field cannot be null");
    return getVariableName(field.getName());
  }

  public static String getDatasourceFieldVariableName(Field field) {
    Preconditions.checkArgument(null != field, "field cannot be null");
    String variableName = getVariableName(field.getName());
    return String.format("%sField", variableName);
  }

  public static String getVariableName(String name) {
    Preconditions.checkArgument(null != name && !name.isEmpty(), "name cannot be null");
    String[] parts = pattern.split(name);

    StringBuilder builder = new StringBuilder();

    int index = 0;
    for (String part : parts) {
      if (index == 0) {
        builder.append(part.substring(0, 1).toLowerCase());
      } else {
        builder.append(part.substring(0, 1).toUpperCase());
      }
      builder.append(part.substring(1));

      index++;
    }

    return builder.toString();
  }

  public static String getFieldNameConstant(Field field) {
    Preconditions.checkArgument(null != field, "field cannot be null");
    String value = field.getName().replace("[^a-z0-9]", "").toUpperCase();
    return String.format("%s_NAME", value);
  }

  public static String getFieldLengthConstant(Field field) {
    Preconditions.checkArgument(null != field, "field cannot be null");
    String value = field.getName().replace("[^a-z0-9]", "").toUpperCase();
    return String.format("%s_LENGTH", value);
  }

  public static String getDataSourceName(Model model) {
    Preconditions.checkArgument(null != model, "model cannot be null");
    return String.format("%sDataSource", model.getName());
  }

  public static String getSharedDataClassName(Model model) {
    Preconditions.checkArgument(null != model, "model cannot be null");
    return String.format("%sConstants", model.getName());
  }

  public static String getModelName(Model model) {
    Preconditions.checkArgument(null != model, "model cannot be null");
    return model.getName();
  }

  public static String getGetterMethodName(Field field) {
    return String.format("get%s", getMethodName(field));
  }

  public static String getSetterMethodName(Field field) {
    return String.format("set%s", getMethodName(field));
  }

  public static String getDAOInterfaceName(Model model) {
    Preconditions.checkArgument(null != model, "model cannot be null");
    return String.format("%sDAO", model.getName());
  }

  public static String getDAOImplName(Model model) {
    Preconditions.checkArgument(null != model, "model cannot be null");
    return String.format("%sDAOImpl", model.getName());
  }

  public static String getPersistModuleName(PackageDefinition packageDefinition) {
    Preconditions.checkArgument(null != packageDefinition, "packageDefinition cannot be null");
    return String.format("%sPersistModule", packageDefinition.getPersistenceUnit());
  }

  public static String getPersistFilterName(PackageDefinition packageDefinition) {
    Preconditions.checkArgument(null != packageDefinition, "packageDefinition cannot be null");
    return String.format("%sPersistFilter", packageDefinition.getPersistenceUnit());
  }

  public static String getPersistServiceName(PackageDefinition packageDefinition) {
    Preconditions.checkArgument(null != packageDefinition, "packageDefinition cannot be null");
    return String.format("%sPersistService", packageDefinition.getPersistenceUnit());
  }

  public static String getPersistServiceAttributeName(PackageDefinition packageDefinition) {
    Preconditions.checkArgument(null != packageDefinition, "packageDefinition cannot be null");
    return String.format("%sPersistService", packageDefinition.getPersistenceUnit());
  }

  public static String getPersistLifecycleManagerName(PackageDefinition packageDefinition) {
    Preconditions.checkArgument(null != packageDefinition, "packageDefinition cannot be null");
    return String.format("%sPersistenceLifeCycleManager", packageDefinition.getPersistenceUnit());
  }

}

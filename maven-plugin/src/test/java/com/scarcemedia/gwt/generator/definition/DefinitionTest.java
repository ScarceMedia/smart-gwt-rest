/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.generator.definition;

import java.io.IOException;
import java.util.ArrayList;
import org.junit.Test;

/**
 *
 * @author jeremy
 */
public class DefinitionTest {

  public static Definition getTestDefinition() {
    Definition definition = new Definition();

    PackageDefinition fooPackage = new PackageDefinition();
    fooPackage.setName("com.example.foo");
    fooPackage.setPersistenceUnit("foo");
    definition.getPackages().add(fooPackage);

    Model model = new Model();
    model.setName("Item");
    model.setTable("items");
    fooPackage.getModels().add(model);

    Field itemIDField = new Field();
    model.getFields().add(itemIDField);
    itemIDField.setName("item_id");
    itemIDField.setType(FieldType.Integer);
    itemIDField.setPrimaryKey(true);

    Field nameField = new Field();
    model.getFields().add(nameField);
    nameField.setName("name");
    nameField.setType(FieldType.String);

    Field itemTypeIDField = new Field();
    model.getFields().add(itemTypeIDField);
    itemTypeIDField.setName("itemtype_id");
    itemTypeIDField.setType(FieldType.Integer);
    itemTypeIDField.setPrimaryKey(false);

    ModelLookup lookup = new ModelLookup();
    lookup.setModelName("ItemType");
    lookup.setValueField("itemtype_id");
    lookup.setDisplayFields(new ArrayList<String>());
    lookup.getDisplayFields().add("name");
    itemTypeIDField.setModelLookup(lookup);

    UniqueIndex nameUnique = new UniqueIndex();
    nameUnique.getFields().add("name");
    model.getUniqueIndexes().add(nameUnique);
    return definition;
  }

  @Test
  public void test() throws IOException {
    Definition definition = getTestDefinition();
    Definition.save(System.out, definition);
  }
}

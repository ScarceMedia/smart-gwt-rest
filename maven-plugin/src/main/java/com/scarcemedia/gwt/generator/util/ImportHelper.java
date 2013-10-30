/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.generator.util;

import com.google.common.base.Preconditions;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.expr.NameExpr;
import java.util.ArrayList;

/**
 *
 * @author jeremy
 */
public class ImportHelper {

  static void addImport(CompilationUnit compileUnit, ImportDeclaration importDeclare) {
    Preconditions.checkArgument(null != compileUnit, "compileUnit cannot be null");

    if (null == compileUnit.getImports()) {
      compileUnit.setImports(new ArrayList<ImportDeclaration>());
    }
    for (ImportDeclaration importDeclaration : compileUnit.getImports()) {
      if (importDeclaration.getName().getName().equals(importDeclare.getName().getName())) {
        return;
      }
    }

    compileUnit.getImports().add(importDeclare);
  }

  public static void addImport(CompilationUnit compileUnit, NameExpr nameExpr) {
    Preconditions.checkArgument(null != nameExpr, "nameExpr cannot be null.");
    ImportDeclaration importDeclare = new ImportDeclaration(nameExpr, false, false);
    addImport(compileUnit, importDeclare);
  }

  public static void addStaticImport(CompilationUnit compileUnit, String... imports) {
    for (String s : imports) {
      ImportDeclaration importDeclare = new ImportDeclaration(new NameExpr(s), true, false);
      addImport(compileUnit, importDeclare);
    }
  }
}

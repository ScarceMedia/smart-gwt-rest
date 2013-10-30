/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.gwt.generator.util;

import japa.parser.ASTHelper;
import japa.parser.ast.expr.BooleanLiteralExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.IntegerLiteralExpr;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.Statement;
import java.util.ArrayList;

/**
 *
 * @author jeremy
 */
public class CodeHelpers {

  public static void addMethodCall(BlockStmt blockStmt, Expression scope, String methodName, Expression value) {
    MethodCallExpr method = new MethodCallExpr(scope, methodName);
    ASTHelper.addArgument(method, value);
    if (null == blockStmt.getStmts()) {
      blockStmt.setStmts(new ArrayList<Statement>());
    }
    blockStmt.getStmts().add(new ExpressionStmt(method));
  }

  public static void addMethodCall(BlockStmt blockStmt, Expression scope, String methodName, boolean value) {
    addMethodCall(blockStmt, scope, methodName, new BooleanLiteralExpr(value));
  }

  public static void addMethodCall(BlockStmt blockStmt, Expression scope, String methodName, String value) {
    addMethodCall(blockStmt, scope, methodName, new StringLiteralExpr(value));
  }

  public static void addMethodCall(BlockStmt blockStmt, Expression scope, String methodName, Integer value) {
    addMethodCall(blockStmt, scope, methodName, new IntegerLiteralExpr(value.toString()));
  }
}

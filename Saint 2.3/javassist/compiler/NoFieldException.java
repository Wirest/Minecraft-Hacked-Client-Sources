package javassist.compiler;

import javassist.compiler.ast.ASTree;

public class NoFieldException extends CompileError {
   private String fieldName;
   private ASTree expr;

   public NoFieldException(String name, ASTree e) {
      super("no such field: " + name);
      this.fieldName = name;
      this.expr = e;
   }

   public String getField() {
      return this.fieldName;
   }

   public ASTree getExpr() {
      return this.expr;
   }
}

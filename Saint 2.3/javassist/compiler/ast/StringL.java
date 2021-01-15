package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class StringL extends ASTree {
   protected String text;

   public StringL(String t) {
      this.text = t;
   }

   public String get() {
      return this.text;
   }

   public String toString() {
      return "\"" + this.text + "\"";
   }

   public void accept(Visitor v) throws CompileError {
      v.atStringL(this);
   }
}

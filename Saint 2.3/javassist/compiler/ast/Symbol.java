package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class Symbol extends ASTree {
   protected String identifier;

   public Symbol(String sym) {
      this.identifier = sym;
   }

   public String get() {
      return this.identifier;
   }

   public String toString() {
      return this.identifier;
   }

   public void accept(Visitor v) throws CompileError {
      v.atSymbol(this);
   }
}

package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class Keyword extends ASTree {
   protected int tokenId;

   public Keyword(int token) {
      this.tokenId = token;
   }

   public int get() {
      return this.tokenId;
   }

   public String toString() {
      return "id:" + this.tokenId;
   }

   public void accept(Visitor v) throws CompileError {
      v.atKeyword(this);
   }
}

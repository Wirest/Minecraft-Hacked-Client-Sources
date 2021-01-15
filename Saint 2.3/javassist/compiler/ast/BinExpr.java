package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class BinExpr extends Expr {
   private BinExpr(int op, ASTree _head, ASTList _tail) {
      super(op, _head, _tail);
   }

   public static BinExpr makeBin(int op, ASTree oprand1, ASTree oprand2) {
      return new BinExpr(op, oprand1, new ASTList(oprand2));
   }

   public void accept(Visitor v) throws CompileError {
      v.atBinExpr(this);
   }
}

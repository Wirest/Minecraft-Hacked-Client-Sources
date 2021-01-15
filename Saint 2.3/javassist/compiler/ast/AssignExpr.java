package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class AssignExpr extends Expr {
   private AssignExpr(int op, ASTree _head, ASTList _tail) {
      super(op, _head, _tail);
   }

   public static AssignExpr makeAssign(int op, ASTree oprand1, ASTree oprand2) {
      return new AssignExpr(op, oprand1, new ASTList(oprand2));
   }

   public void accept(Visitor v) throws CompileError {
      v.atAssignExpr(this);
   }
}

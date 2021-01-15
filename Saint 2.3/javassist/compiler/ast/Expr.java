package javassist.compiler.ast;

import javassist.compiler.CompileError;
import javassist.compiler.TokenId;

public class Expr extends ASTList implements TokenId {
   protected int operatorId;

   Expr(int op, ASTree _head, ASTList _tail) {
      super(_head, _tail);
      this.operatorId = op;
   }

   Expr(int op, ASTree _head) {
      super(_head);
      this.operatorId = op;
   }

   public static Expr make(int op, ASTree oprand1, ASTree oprand2) {
      return new Expr(op, oprand1, new ASTList(oprand2));
   }

   public static Expr make(int op, ASTree oprand1) {
      return new Expr(op, oprand1);
   }

   public int getOperator() {
      return this.operatorId;
   }

   public void setOperator(int op) {
      this.operatorId = op;
   }

   public ASTree oprand1() {
      return this.getLeft();
   }

   public void setOprand1(ASTree expr) {
      this.setLeft(expr);
   }

   public ASTree oprand2() {
      return this.getRight().getLeft();
   }

   public void setOprand2(ASTree expr) {
      this.getRight().setLeft(expr);
   }

   public void accept(Visitor v) throws CompileError {
      v.atExpr(this);
   }

   public String getName() {
      int id = this.operatorId;
      if (id < 128) {
         return String.valueOf((char)id);
      } else if (350 <= id && id <= 371) {
         return opNames[id - 350];
      } else {
         return id == 323 ? "instanceof" : String.valueOf(id);
      }
   }

   protected String getTag() {
      return "op:" + this.getName();
   }
}

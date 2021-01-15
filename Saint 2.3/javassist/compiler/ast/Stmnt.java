package javassist.compiler.ast;

import javassist.compiler.CompileError;
import javassist.compiler.TokenId;

public class Stmnt extends ASTList implements TokenId {
   protected int operatorId;

   public Stmnt(int op, ASTree _head, ASTList _tail) {
      super(_head, _tail);
      this.operatorId = op;
   }

   public Stmnt(int op, ASTree _head) {
      super(_head);
      this.operatorId = op;
   }

   public Stmnt(int op) {
      this(op, (ASTree)null);
   }

   public static Stmnt make(int op, ASTree oprand1, ASTree oprand2) {
      return new Stmnt(op, oprand1, new ASTList(oprand2));
   }

   public static Stmnt make(int op, ASTree op1, ASTree op2, ASTree op3) {
      return new Stmnt(op, op1, new ASTList(op2, new ASTList(op3)));
   }

   public void accept(Visitor v) throws CompileError {
      v.atStmnt(this);
   }

   public int getOperator() {
      return this.operatorId;
   }

   protected String getTag() {
      return this.operatorId < 128 ? "stmnt:" + (char)this.operatorId : "stmnt:" + this.operatorId;
   }
}

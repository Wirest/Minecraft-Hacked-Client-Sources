package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class CondExpr extends ASTList {
   public CondExpr(ASTree cond, ASTree thenp, ASTree elsep) {
      super(cond, new ASTList(thenp, new ASTList(elsep)));
   }

   public ASTree condExpr() {
      return this.head();
   }

   public void setCond(ASTree t) {
      this.setHead(t);
   }

   public ASTree thenExpr() {
      return this.tail().head();
   }

   public void setThen(ASTree t) {
      this.tail().setHead(t);
   }

   public ASTree elseExpr() {
      return this.tail().tail().head();
   }

   public void setElse(ASTree t) {
      this.tail().tail().setHead(t);
   }

   public String getTag() {
      return "?:";
   }

   public void accept(Visitor v) throws CompileError {
      v.atCondExpr(this);
   }
}

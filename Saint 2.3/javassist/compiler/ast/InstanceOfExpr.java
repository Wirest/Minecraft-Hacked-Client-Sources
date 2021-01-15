package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class InstanceOfExpr extends CastExpr {
   public InstanceOfExpr(ASTList className, int dim, ASTree expr) {
      super(className, dim, expr);
   }

   public InstanceOfExpr(int type, int dim, ASTree expr) {
      super(type, dim, expr);
   }

   public String getTag() {
      return "instanceof:" + this.castType + ":" + this.arrayDim;
   }

   public void accept(Visitor v) throws CompileError {
      v.atInstanceOfExpr(this);
   }
}

package javassist.compiler.ast;

import javassist.compiler.CompileError;
import javassist.compiler.TokenId;

public class CastExpr extends ASTList implements TokenId {
   protected int castType;
   protected int arrayDim;

   public CastExpr(ASTList className, int dim, ASTree expr) {
      super(className, new ASTList(expr));
      this.castType = 307;
      this.arrayDim = dim;
   }

   public CastExpr(int type, int dim, ASTree expr) {
      super((ASTree)null, new ASTList(expr));
      this.castType = type;
      this.arrayDim = dim;
   }

   public int getType() {
      return this.castType;
   }

   public int getArrayDim() {
      return this.arrayDim;
   }

   public ASTList getClassName() {
      return (ASTList)this.getLeft();
   }

   public ASTree getOprand() {
      return this.getRight().getLeft();
   }

   public void setOprand(ASTree t) {
      this.getRight().setLeft(t);
   }

   public String getTag() {
      return "cast:" + this.castType + ":" + this.arrayDim;
   }

   public void accept(Visitor v) throws CompileError {
      v.atCastExpr(this);
   }
}

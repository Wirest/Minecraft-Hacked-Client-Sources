package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class DoubleConst extends ASTree {
   protected double value;
   protected int type;

   public DoubleConst(double v, int tokenId) {
      this.value = v;
      this.type = tokenId;
   }

   public double get() {
      return this.value;
   }

   public void set(double v) {
      this.value = v;
   }

   public int getType() {
      return this.type;
   }

   public String toString() {
      return Double.toString(this.value);
   }

   public void accept(Visitor v) throws CompileError {
      v.atDoubleConst(this);
   }

   public ASTree compute(int op, ASTree right) {
      if (right instanceof IntConst) {
         return this.compute0(op, (IntConst)right);
      } else {
         return right instanceof DoubleConst ? this.compute0(op, (DoubleConst)right) : null;
      }
   }

   private DoubleConst compute0(int op, DoubleConst right) {
      short newType;
      if (this.type != 405 && right.type != 405) {
         newType = 404;
      } else {
         newType = 405;
      }

      return compute(op, this.value, right.value, newType);
   }

   private DoubleConst compute0(int op, IntConst right) {
      return compute(op, this.value, (double)right.value, this.type);
   }

   private static DoubleConst compute(int op, double value1, double value2, int newType) {
      double newValue;
      switch(op) {
      case 37:
         newValue = value1 % value2;
         break;
      case 38:
      case 39:
      case 40:
      case 41:
      case 44:
      case 46:
      default:
         return null;
      case 42:
         newValue = value1 * value2;
         break;
      case 43:
         newValue = value1 + value2;
         break;
      case 45:
         newValue = value1 - value2;
         break;
      case 47:
         newValue = value1 / value2;
      }

      return new DoubleConst(newValue, newType);
   }
}

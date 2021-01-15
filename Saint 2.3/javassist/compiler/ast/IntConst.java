package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class IntConst extends ASTree {
   protected long value;
   protected int type;

   public IntConst(long v, int tokenId) {
      this.value = v;
      this.type = tokenId;
   }

   public long get() {
      return this.value;
   }

   public void set(long v) {
      this.value = v;
   }

   public int getType() {
      return this.type;
   }

   public String toString() {
      return Long.toString(this.value);
   }

   public void accept(Visitor v) throws CompileError {
      v.atIntConst(this);
   }

   public ASTree compute(int op, ASTree right) {
      if (right instanceof IntConst) {
         return this.compute0(op, (IntConst)right);
      } else {
         return right instanceof DoubleConst ? this.compute0(op, (DoubleConst)right) : null;
      }
   }

   private IntConst compute0(int op, IntConst right) {
      int type1 = this.type;
      int type2 = right.type;
      int newType;
      if (type1 != 403 && type2 != 403) {
         if (type1 == 401 && type2 == 401) {
            newType = 401;
         } else {
            newType = 402;
         }
      } else {
         newType = 403;
      }

      long value1 = this.value;
      long value2 = right.value;
      long newValue;
      switch(op) {
      case 37:
         newValue = value1 % value2;
         break;
      case 38:
         newValue = value1 & value2;
         break;
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
         break;
      case 94:
         newValue = value1 ^ value2;
         break;
      case 124:
         newValue = value1 | value2;
         break;
      case 364:
         newValue = this.value << (int)value2;
         newType = type1;
         break;
      case 366:
         newValue = this.value >> (int)value2;
         newType = type1;
         break;
      case 370:
         newValue = this.value >>> (int)value2;
         newType = type1;
         break;
      default:
         return null;
      }

      return new IntConst(newValue, newType);
   }

   private DoubleConst compute0(int op, DoubleConst right) {
      double value1 = (double)this.value;
      double value2 = right.value;
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

      return new DoubleConst(newValue, right.type);
   }
}

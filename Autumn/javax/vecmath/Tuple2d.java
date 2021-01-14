package javax.vecmath;

import java.io.Serializable;

public abstract class Tuple2d implements Serializable, Cloneable {
   static final long serialVersionUID = 6205762482756093838L;
   public double x;
   public double y;

   public Tuple2d(double var1, double var3) {
      this.x = var1;
      this.y = var3;
   }

   public Tuple2d(double[] var1) {
      this.x = var1[0];
      this.y = var1[1];
   }

   public Tuple2d(Tuple2d var1) {
      this.x = var1.x;
      this.y = var1.y;
   }

   public Tuple2d(Tuple2f var1) {
      this.x = (double)var1.x;
      this.y = (double)var1.y;
   }

   public Tuple2d() {
      this.x = 0.0D;
      this.y = 0.0D;
   }

   public final void set(double var1, double var3) {
      this.x = var1;
      this.y = var3;
   }

   public final void set(double[] var1) {
      this.x = var1[0];
      this.y = var1[1];
   }

   public final void set(Tuple2d var1) {
      this.x = var1.x;
      this.y = var1.y;
   }

   public final void set(Tuple2f var1) {
      this.x = (double)var1.x;
      this.y = (double)var1.y;
   }

   public final void get(double[] var1) {
      var1[0] = this.x;
      var1[1] = this.y;
   }

   public final void add(Tuple2d var1, Tuple2d var2) {
      this.x = var1.x + var2.x;
      this.y = var1.y + var2.y;
   }

   public final void add(Tuple2d var1) {
      this.x += var1.x;
      this.y += var1.y;
   }

   public final void sub(Tuple2d var1, Tuple2d var2) {
      this.x = var1.x - var2.x;
      this.y = var1.y - var2.y;
   }

   public final void sub(Tuple2d var1) {
      this.x -= var1.x;
      this.y -= var1.y;
   }

   public final void negate(Tuple2d var1) {
      this.x = -var1.x;
      this.y = -var1.y;
   }

   public final void negate() {
      this.x = -this.x;
      this.y = -this.y;
   }

   public final void scale(double var1, Tuple2d var3) {
      this.x = var1 * var3.x;
      this.y = var1 * var3.y;
   }

   public final void scale(double var1) {
      this.x *= var1;
      this.y *= var1;
   }

   public final void scaleAdd(double var1, Tuple2d var3, Tuple2d var4) {
      this.x = var1 * var3.x + var4.x;
      this.y = var1 * var3.y + var4.y;
   }

   public final void scaleAdd(double var1, Tuple2d var3) {
      this.x = var1 * this.x + var3.x;
      this.y = var1 * this.y + var3.y;
   }

   public int hashCode() {
      long var1 = 1L;
      var1 = 31L * var1 + VecMathUtil.doubleToLongBits(this.x);
      var1 = 31L * var1 + VecMathUtil.doubleToLongBits(this.y);
      return (int)(var1 ^ var1 >> 32);
   }

   public boolean equals(Tuple2d var1) {
      try {
         return this.x == var1.x && this.y == var1.y;
      } catch (NullPointerException var3) {
         return false;
      }
   }

   public boolean equals(Object var1) {
      try {
         Tuple2d var2 = (Tuple2d)var1;
         return this.x == var2.x && this.y == var2.y;
      } catch (NullPointerException var3) {
         return false;
      } catch (ClassCastException var4) {
         return false;
      }
   }

   public boolean epsilonEquals(Tuple2d var1, double var2) {
      double var4 = this.x - var1.x;
      if ((var4 < 0.0D ? -var4 : var4) > var2) {
         return false;
      } else {
         var4 = this.y - var1.y;
         return (var4 < 0.0D ? -var4 : var4) <= var2;
      }
   }

   public String toString() {
      return "(" + this.x + ", " + this.y + ")";
   }

   public final void clamp(double var1, double var3, Tuple2d var5) {
      if (var5.x > var3) {
         this.x = var3;
      } else if (var5.x < var1) {
         this.x = var1;
      } else {
         this.x = var5.x;
      }

      if (var5.y > var3) {
         this.y = var3;
      } else if (var5.y < var1) {
         this.y = var1;
      } else {
         this.y = var5.y;
      }

   }

   public final void clampMin(double var1, Tuple2d var3) {
      if (var3.x < var1) {
         this.x = var1;
      } else {
         this.x = var3.x;
      }

      if (var3.y < var1) {
         this.y = var1;
      } else {
         this.y = var3.y;
      }

   }

   public final void clampMax(double var1, Tuple2d var3) {
      if (var3.x > var1) {
         this.x = var1;
      } else {
         this.x = var3.x;
      }

      if (var3.y > var1) {
         this.y = var1;
      } else {
         this.y = var3.y;
      }

   }

   public final void absolute(Tuple2d var1) {
      this.x = Math.abs(var1.x);
      this.y = Math.abs(var1.y);
   }

   public final void clamp(double var1, double var3) {
      if (this.x > var3) {
         this.x = var3;
      } else if (this.x < var1) {
         this.x = var1;
      }

      if (this.y > var3) {
         this.y = var3;
      } else if (this.y < var1) {
         this.y = var1;
      }

   }

   public final void clampMin(double var1) {
      if (this.x < var1) {
         this.x = var1;
      }

      if (this.y < var1) {
         this.y = var1;
      }

   }

   public final void clampMax(double var1) {
      if (this.x > var1) {
         this.x = var1;
      }

      if (this.y > var1) {
         this.y = var1;
      }

   }

   public final void absolute() {
      this.x = Math.abs(this.x);
      this.y = Math.abs(this.y);
   }

   public final void interpolate(Tuple2d var1, Tuple2d var2, double var3) {
      this.x = (1.0D - var3) * var1.x + var3 * var2.x;
      this.y = (1.0D - var3) * var1.y + var3 * var2.y;
   }

   public final void interpolate(Tuple2d var1, double var2) {
      this.x = (1.0D - var2) * this.x + var2 * var1.x;
      this.y = (1.0D - var2) * this.y + var2 * var1.y;
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new InternalError();
      }
   }
}

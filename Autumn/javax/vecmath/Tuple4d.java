package javax.vecmath;

import java.io.Serializable;

public abstract class Tuple4d implements Serializable, Cloneable {
   static final long serialVersionUID = -4748953690425311052L;
   public double x;
   public double y;
   public double z;
   public double w;

   public Tuple4d(double var1, double var3, double var5, double var7) {
      this.x = var1;
      this.y = var3;
      this.z = var5;
      this.w = var7;
   }

   public Tuple4d(double[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
      this.w = var1[3];
   }

   public Tuple4d(Tuple4d var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      this.w = var1.w;
   }

   public Tuple4d(Tuple4f var1) {
      this.x = (double)var1.x;
      this.y = (double)var1.y;
      this.z = (double)var1.z;
      this.w = (double)var1.w;
   }

   public Tuple4d() {
      this.x = 0.0D;
      this.y = 0.0D;
      this.z = 0.0D;
      this.w = 0.0D;
   }

   public final void set(double var1, double var3, double var5, double var7) {
      this.x = var1;
      this.y = var3;
      this.z = var5;
      this.w = var7;
   }

   public final void set(double[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
      this.w = var1[3];
   }

   public final void set(Tuple4d var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      this.w = var1.w;
   }

   public final void set(Tuple4f var1) {
      this.x = (double)var1.x;
      this.y = (double)var1.y;
      this.z = (double)var1.z;
      this.w = (double)var1.w;
   }

   public final void get(double[] var1) {
      var1[0] = this.x;
      var1[1] = this.y;
      var1[2] = this.z;
      var1[3] = this.w;
   }

   public final void get(Tuple4d var1) {
      var1.x = this.x;
      var1.y = this.y;
      var1.z = this.z;
      var1.w = this.w;
   }

   public final void add(Tuple4d var1, Tuple4d var2) {
      this.x = var1.x + var2.x;
      this.y = var1.y + var2.y;
      this.z = var1.z + var2.z;
      this.w = var1.w + var2.w;
   }

   public final void add(Tuple4d var1) {
      this.x += var1.x;
      this.y += var1.y;
      this.z += var1.z;
      this.w += var1.w;
   }

   public final void sub(Tuple4d var1, Tuple4d var2) {
      this.x = var1.x - var2.x;
      this.y = var1.y - var2.y;
      this.z = var1.z - var2.z;
      this.w = var1.w - var2.w;
   }

   public final void sub(Tuple4d var1) {
      this.x -= var1.x;
      this.y -= var1.y;
      this.z -= var1.z;
      this.w -= var1.w;
   }

   public final void negate(Tuple4d var1) {
      this.x = -var1.x;
      this.y = -var1.y;
      this.z = -var1.z;
      this.w = -var1.w;
   }

   public final void negate() {
      this.x = -this.x;
      this.y = -this.y;
      this.z = -this.z;
      this.w = -this.w;
   }

   public final void scale(double var1, Tuple4d var3) {
      this.x = var1 * var3.x;
      this.y = var1 * var3.y;
      this.z = var1 * var3.z;
      this.w = var1 * var3.w;
   }

   public final void scale(double var1) {
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
      this.w *= var1;
   }

   public final void scaleAdd(double var1, Tuple4d var3, Tuple4d var4) {
      this.x = var1 * var3.x + var4.x;
      this.y = var1 * var3.y + var4.y;
      this.z = var1 * var3.z + var4.z;
      this.w = var1 * var3.w + var4.w;
   }

   /** @deprecated */
   public final void scaleAdd(float var1, Tuple4d var2) {
      this.scaleAdd((double)var1, var2);
   }

   public final void scaleAdd(double var1, Tuple4d var3) {
      this.x = var1 * this.x + var3.x;
      this.y = var1 * this.y + var3.y;
      this.z = var1 * this.z + var3.z;
      this.w = var1 * this.w + var3.w;
   }

   public String toString() {
      return "(" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + ")";
   }

   public boolean equals(Tuple4d var1) {
      try {
         return this.x == var1.x && this.y == var1.y && this.z == var1.z && this.w == var1.w;
      } catch (NullPointerException var3) {
         return false;
      }
   }

   public boolean equals(Object var1) {
      try {
         Tuple4d var2 = (Tuple4d)var1;
         return this.x == var2.x && this.y == var2.y && this.z == var2.z && this.w == var2.w;
      } catch (NullPointerException var3) {
         return false;
      } catch (ClassCastException var4) {
         return false;
      }
   }

   public boolean epsilonEquals(Tuple4d var1, double var2) {
      double var4 = this.x - var1.x;
      if ((var4 < 0.0D ? -var4 : var4) > var2) {
         return false;
      } else {
         var4 = this.y - var1.y;
         if ((var4 < 0.0D ? -var4 : var4) > var2) {
            return false;
         } else {
            var4 = this.z - var1.z;
            if ((var4 < 0.0D ? -var4 : var4) > var2) {
               return false;
            } else {
               var4 = this.w - var1.w;
               return (var4 < 0.0D ? -var4 : var4) <= var2;
            }
         }
      }
   }

   public int hashCode() {
      long var1 = 1L;
      var1 = 31L * var1 + VecMathUtil.doubleToLongBits(this.x);
      var1 = 31L * var1 + VecMathUtil.doubleToLongBits(this.y);
      var1 = 31L * var1 + VecMathUtil.doubleToLongBits(this.z);
      var1 = 31L * var1 + VecMathUtil.doubleToLongBits(this.w);
      return (int)(var1 ^ var1 >> 32);
   }

   /** @deprecated */
   public final void clamp(float var1, float var2, Tuple4d var3) {
      this.clamp((double)var1, (double)var2, var3);
   }

   public final void clamp(double var1, double var3, Tuple4d var5) {
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

      if (var5.z > var3) {
         this.z = var3;
      } else if (var5.z < var1) {
         this.z = var1;
      } else {
         this.z = var5.z;
      }

      if (var5.w > var3) {
         this.w = var3;
      } else if (var5.w < var1) {
         this.w = var1;
      } else {
         this.w = var5.w;
      }

   }

   /** @deprecated */
   public final void clampMin(float var1, Tuple4d var2) {
      this.clampMin((double)var1, var2);
   }

   public final void clampMin(double var1, Tuple4d var3) {
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

      if (var3.z < var1) {
         this.z = var1;
      } else {
         this.z = var3.z;
      }

      if (var3.w < var1) {
         this.w = var1;
      } else {
         this.w = var3.w;
      }

   }

   /** @deprecated */
   public final void clampMax(float var1, Tuple4d var2) {
      this.clampMax((double)var1, var2);
   }

   public final void clampMax(double var1, Tuple4d var3) {
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

      if (var3.z > var1) {
         this.z = var1;
      } else {
         this.z = var3.z;
      }

      if (var3.w > var1) {
         this.w = var1;
      } else {
         this.w = var3.z;
      }

   }

   public final void absolute(Tuple4d var1) {
      this.x = Math.abs(var1.x);
      this.y = Math.abs(var1.y);
      this.z = Math.abs(var1.z);
      this.w = Math.abs(var1.w);
   }

   /** @deprecated */
   public final void clamp(float var1, float var2) {
      this.clamp((double)var1, (double)var2);
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

      if (this.z > var3) {
         this.z = var3;
      } else if (this.z < var1) {
         this.z = var1;
      }

      if (this.w > var3) {
         this.w = var3;
      } else if (this.w < var1) {
         this.w = var1;
      }

   }

   /** @deprecated */
   public final void clampMin(float var1) {
      this.clampMin((double)var1);
   }

   public final void clampMin(double var1) {
      if (this.x < var1) {
         this.x = var1;
      }

      if (this.y < var1) {
         this.y = var1;
      }

      if (this.z < var1) {
         this.z = var1;
      }

      if (this.w < var1) {
         this.w = var1;
      }

   }

   /** @deprecated */
   public final void clampMax(float var1) {
      this.clampMax((double)var1);
   }

   public final void clampMax(double var1) {
      if (this.x > var1) {
         this.x = var1;
      }

      if (this.y > var1) {
         this.y = var1;
      }

      if (this.z > var1) {
         this.z = var1;
      }

      if (this.w > var1) {
         this.w = var1;
      }

   }

   public final void absolute() {
      this.x = Math.abs(this.x);
      this.y = Math.abs(this.y);
      this.z = Math.abs(this.z);
      this.w = Math.abs(this.w);
   }

   /** @deprecated */
   public void interpolate(Tuple4d var1, Tuple4d var2, float var3) {
      this.interpolate(var1, var2, (double)var3);
   }

   public void interpolate(Tuple4d var1, Tuple4d var2, double var3) {
      this.x = (1.0D - var3) * var1.x + var3 * var2.x;
      this.y = (1.0D - var3) * var1.y + var3 * var2.y;
      this.z = (1.0D - var3) * var1.z + var3 * var2.z;
      this.w = (1.0D - var3) * var1.w + var3 * var2.w;
   }

   /** @deprecated */
   public void interpolate(Tuple4d var1, float var2) {
      this.interpolate(var1, (double)var2);
   }

   public void interpolate(Tuple4d var1, double var2) {
      this.x = (1.0D - var2) * this.x + var2 * var1.x;
      this.y = (1.0D - var2) * this.y + var2 * var1.y;
      this.z = (1.0D - var2) * this.z + var2 * var1.z;
      this.w = (1.0D - var2) * this.w + var2 * var1.w;
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new InternalError();
      }
   }
}

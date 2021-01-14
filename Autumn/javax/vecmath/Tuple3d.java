package javax.vecmath;

import java.io.Serializable;

public abstract class Tuple3d implements Serializable, Cloneable {
   static final long serialVersionUID = 5542096614926168415L;
   public double x;
   public double y;
   public double z;

   public Tuple3d(double var1, double var3, double var5) {
      this.x = var1;
      this.y = var3;
      this.z = var5;
   }

   public Tuple3d(double[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
   }

   public Tuple3d(Tuple3d var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
   }

   public Tuple3d(Tuple3f var1) {
      this.x = (double)var1.x;
      this.y = (double)var1.y;
      this.z = (double)var1.z;
   }

   public Tuple3d() {
      this.x = 0.0D;
      this.y = 0.0D;
      this.z = 0.0D;
   }

   public final void set(double var1, double var3, double var5) {
      this.x = var1;
      this.y = var3;
      this.z = var5;
   }

   public final void set(double[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
   }

   public final void set(Tuple3d var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
   }

   public final void set(Tuple3f var1) {
      this.x = (double)var1.x;
      this.y = (double)var1.y;
      this.z = (double)var1.z;
   }

   public final void get(double[] var1) {
      var1[0] = this.x;
      var1[1] = this.y;
      var1[2] = this.z;
   }

   public final void get(Tuple3d var1) {
      var1.x = this.x;
      var1.y = this.y;
      var1.z = this.z;
   }

   public final void add(Tuple3d var1, Tuple3d var2) {
      this.x = var1.x + var2.x;
      this.y = var1.y + var2.y;
      this.z = var1.z + var2.z;
   }

   public final void add(Tuple3d var1) {
      this.x += var1.x;
      this.y += var1.y;
      this.z += var1.z;
   }

   public final void sub(Tuple3d var1, Tuple3d var2) {
      this.x = var1.x - var2.x;
      this.y = var1.y - var2.y;
      this.z = var1.z - var2.z;
   }

   public final void sub(Tuple3d var1) {
      this.x -= var1.x;
      this.y -= var1.y;
      this.z -= var1.z;
   }

   public final void negate(Tuple3d var1) {
      this.x = -var1.x;
      this.y = -var1.y;
      this.z = -var1.z;
   }

   public final void negate() {
      this.x = -this.x;
      this.y = -this.y;
      this.z = -this.z;
   }

   public final void scale(double var1, Tuple3d var3) {
      this.x = var1 * var3.x;
      this.y = var1 * var3.y;
      this.z = var1 * var3.z;
   }

   public final void scale(double var1) {
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
   }

   public final void scaleAdd(double var1, Tuple3d var3, Tuple3d var4) {
      this.x = var1 * var3.x + var4.x;
      this.y = var1 * var3.y + var4.y;
      this.z = var1 * var3.z + var4.z;
   }

   /** @deprecated */
   public final void scaleAdd(double var1, Tuple3f var3) {
      this.scaleAdd(var1, (Tuple3d)(new Point3d(var3)));
   }

   public final void scaleAdd(double var1, Tuple3d var3) {
      this.x = var1 * this.x + var3.x;
      this.y = var1 * this.y + var3.y;
      this.z = var1 * this.z + var3.z;
   }

   public String toString() {
      return "(" + this.x + ", " + this.y + ", " + this.z + ")";
   }

   public int hashCode() {
      long var1 = 1L;
      var1 = 31L * var1 + VecMathUtil.doubleToLongBits(this.x);
      var1 = 31L * var1 + VecMathUtil.doubleToLongBits(this.y);
      var1 = 31L * var1 + VecMathUtil.doubleToLongBits(this.z);
      return (int)(var1 ^ var1 >> 32);
   }

   public boolean equals(Tuple3d var1) {
      try {
         return this.x == var1.x && this.y == var1.y && this.z == var1.z;
      } catch (NullPointerException var3) {
         return false;
      }
   }

   public boolean equals(Object var1) {
      try {
         Tuple3d var2 = (Tuple3d)var1;
         return this.x == var2.x && this.y == var2.y && this.z == var2.z;
      } catch (ClassCastException var3) {
         return false;
      } catch (NullPointerException var4) {
         return false;
      }
   }

   public boolean epsilonEquals(Tuple3d var1, double var2) {
      double var4 = this.x - var1.x;
      if ((var4 < 0.0D ? -var4 : var4) > var2) {
         return false;
      } else {
         var4 = this.y - var1.y;
         if ((var4 < 0.0D ? -var4 : var4) > var2) {
            return false;
         } else {
            var4 = this.z - var1.z;
            return (var4 < 0.0D ? -var4 : var4) <= var2;
         }
      }
   }

   /** @deprecated */
   public final void clamp(float var1, float var2, Tuple3d var3) {
      this.clamp((double)var1, (double)var2, var3);
   }

   public final void clamp(double var1, double var3, Tuple3d var5) {
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

   }

   /** @deprecated */
   public final void clampMin(float var1, Tuple3d var2) {
      this.clampMin((double)var1, var2);
   }

   public final void clampMin(double var1, Tuple3d var3) {
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

   }

   /** @deprecated */
   public final void clampMax(float var1, Tuple3d var2) {
      this.clampMax((double)var1, var2);
   }

   public final void clampMax(double var1, Tuple3d var3) {
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

   }

   public final void absolute(Tuple3d var1) {
      this.x = Math.abs(var1.x);
      this.y = Math.abs(var1.y);
      this.z = Math.abs(var1.z);
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

   }

   public final void absolute() {
      this.x = Math.abs(this.x);
      this.y = Math.abs(this.y);
      this.z = Math.abs(this.z);
   }

   /** @deprecated */
   public final void interpolate(Tuple3d var1, Tuple3d var2, float var3) {
      this.interpolate(var1, var2, (double)var3);
   }

   public final void interpolate(Tuple3d var1, Tuple3d var2, double var3) {
      this.x = (1.0D - var3) * var1.x + var3 * var2.x;
      this.y = (1.0D - var3) * var1.y + var3 * var2.y;
      this.z = (1.0D - var3) * var1.z + var3 * var2.z;
   }

   /** @deprecated */
   public final void interpolate(Tuple3d var1, float var2) {
      this.interpolate(var1, (double)var2);
   }

   public final void interpolate(Tuple3d var1, double var2) {
      this.x = (1.0D - var2) * this.x + var2 * var1.x;
      this.y = (1.0D - var2) * this.y + var2 * var1.y;
      this.z = (1.0D - var2) * this.z + var2 * var1.z;
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new InternalError();
      }
   }
}

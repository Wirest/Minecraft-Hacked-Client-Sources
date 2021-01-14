package javax.vecmath;

import java.io.Serializable;

public class AxisAngle4d implements Serializable, Cloneable {
   static final long serialVersionUID = 3644296204459140589L;
   public double x;
   public double y;
   public double z;
   public double angle;
   static final double EPS = 1.0E-6D;

   public AxisAngle4d(double var1, double var3, double var5, double var7) {
      this.x = var1;
      this.y = var3;
      this.z = var5;
      this.angle = var7;
   }

   public AxisAngle4d(double[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
      this.angle = var1[3];
   }

   public AxisAngle4d(AxisAngle4d var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      this.angle = var1.angle;
   }

   public AxisAngle4d(AxisAngle4f var1) {
      this.x = (double)var1.x;
      this.y = (double)var1.y;
      this.z = (double)var1.z;
      this.angle = (double)var1.angle;
   }

   public AxisAngle4d(Vector3d var1, double var2) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      this.angle = var2;
   }

   public AxisAngle4d() {
      this.x = 0.0D;
      this.y = 0.0D;
      this.z = 1.0D;
      this.angle = 0.0D;
   }

   public final void set(double var1, double var3, double var5, double var7) {
      this.x = var1;
      this.y = var3;
      this.z = var5;
      this.angle = var7;
   }

   public final void set(double[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
      this.angle = var1[3];
   }

   public final void set(AxisAngle4d var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      this.angle = var1.angle;
   }

   public final void set(AxisAngle4f var1) {
      this.x = (double)var1.x;
      this.y = (double)var1.y;
      this.z = (double)var1.z;
      this.angle = (double)var1.angle;
   }

   public final void set(Vector3d var1, double var2) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      this.angle = var2;
   }

   public final void get(double[] var1) {
      var1[0] = this.x;
      var1[1] = this.y;
      var1[2] = this.z;
      var1[3] = this.angle;
   }

   public final void set(Matrix4f var1) {
      Matrix3d var2 = new Matrix3d();
      var1.get(var2);
      this.x = (double)((float)(var2.m21 - var2.m12));
      this.y = (double)((float)(var2.m02 - var2.m20));
      this.z = (double)((float)(var2.m10 - var2.m01));
      double var3 = this.x * this.x + this.y * this.y + this.z * this.z;
      if (var3 > 1.0E-6D) {
         var3 = Math.sqrt(var3);
         double var5 = 0.5D * var3;
         double var7 = 0.5D * (var2.m00 + var2.m11 + var2.m22 - 1.0D);
         this.angle = (double)((float)Math.atan2(var5, var7));
         double var9 = 1.0D / var3;
         this.x *= var9;
         this.y *= var9;
         this.z *= var9;
      } else {
         this.x = 0.0D;
         this.y = 1.0D;
         this.z = 0.0D;
         this.angle = 0.0D;
      }

   }

   public final void set(Matrix4d var1) {
      Matrix3d var2 = new Matrix3d();
      var1.get(var2);
      this.x = (double)((float)(var2.m21 - var2.m12));
      this.y = (double)((float)(var2.m02 - var2.m20));
      this.z = (double)((float)(var2.m10 - var2.m01));
      double var3 = this.x * this.x + this.y * this.y + this.z * this.z;
      if (var3 > 1.0E-6D) {
         var3 = Math.sqrt(var3);
         double var5 = 0.5D * var3;
         double var7 = 0.5D * (var2.m00 + var2.m11 + var2.m22 - 1.0D);
         this.angle = (double)((float)Math.atan2(var5, var7));
         double var9 = 1.0D / var3;
         this.x *= var9;
         this.y *= var9;
         this.z *= var9;
      } else {
         this.x = 0.0D;
         this.y = 1.0D;
         this.z = 0.0D;
         this.angle = 0.0D;
      }

   }

   public final void set(Matrix3f var1) {
      this.x = (double)(var1.m21 - var1.m12);
      this.y = (double)(var1.m02 - var1.m20);
      this.z = (double)(var1.m10 - var1.m01);
      double var2 = this.x * this.x + this.y * this.y + this.z * this.z;
      if (var2 > 1.0E-6D) {
         var2 = Math.sqrt(var2);
         double var4 = 0.5D * var2;
         double var6 = 0.5D * ((double)(var1.m00 + var1.m11 + var1.m22) - 1.0D);
         this.angle = (double)((float)Math.atan2(var4, var6));
         double var8 = 1.0D / var2;
         this.x *= var8;
         this.y *= var8;
         this.z *= var8;
      } else {
         this.x = 0.0D;
         this.y = 1.0D;
         this.z = 0.0D;
         this.angle = 0.0D;
      }

   }

   public final void set(Matrix3d var1) {
      this.x = (double)((float)(var1.m21 - var1.m12));
      this.y = (double)((float)(var1.m02 - var1.m20));
      this.z = (double)((float)(var1.m10 - var1.m01));
      double var2 = this.x * this.x + this.y * this.y + this.z * this.z;
      if (var2 > 1.0E-6D) {
         var2 = Math.sqrt(var2);
         double var4 = 0.5D * var2;
         double var6 = 0.5D * (var1.m00 + var1.m11 + var1.m22 - 1.0D);
         this.angle = (double)((float)Math.atan2(var4, var6));
         double var8 = 1.0D / var2;
         this.x *= var8;
         this.y *= var8;
         this.z *= var8;
      } else {
         this.x = 0.0D;
         this.y = 1.0D;
         this.z = 0.0D;
         this.angle = 0.0D;
      }

   }

   public final void set(Quat4f var1) {
      double var2 = (double)(var1.x * var1.x + var1.y * var1.y + var1.z * var1.z);
      if (var2 > 1.0E-6D) {
         var2 = Math.sqrt(var2);
         double var4 = 1.0D / var2;
         this.x = (double)var1.x * var4;
         this.y = (double)var1.y * var4;
         this.z = (double)var1.z * var4;
         this.angle = 2.0D * Math.atan2(var2, (double)var1.w);
      } else {
         this.x = 0.0D;
         this.y = 1.0D;
         this.z = 0.0D;
         this.angle = 0.0D;
      }

   }

   public final void set(Quat4d var1) {
      double var2 = var1.x * var1.x + var1.y * var1.y + var1.z * var1.z;
      if (var2 > 1.0E-6D) {
         var2 = Math.sqrt(var2);
         double var4 = 1.0D / var2;
         this.x = var1.x * var4;
         this.y = var1.y * var4;
         this.z = var1.z * var4;
         this.angle = 2.0D * Math.atan2(var2, var1.w);
      } else {
         this.x = 0.0D;
         this.y = 1.0D;
         this.z = 0.0D;
         this.angle = 0.0D;
      }

   }

   public String toString() {
      return "(" + this.x + ", " + this.y + ", " + this.z + ", " + this.angle + ")";
   }

   public boolean equals(AxisAngle4d var1) {
      try {
         return this.x == var1.x && this.y == var1.y && this.z == var1.z && this.angle == var1.angle;
      } catch (NullPointerException var3) {
         return false;
      }
   }

   public boolean equals(Object var1) {
      try {
         AxisAngle4d var2 = (AxisAngle4d)var1;
         return this.x == var2.x && this.y == var2.y && this.z == var2.z && this.angle == var2.angle;
      } catch (NullPointerException var3) {
         return false;
      } catch (ClassCastException var4) {
         return false;
      }
   }

   public boolean epsilonEquals(AxisAngle4d var1, double var2) {
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
               var4 = this.angle - var1.angle;
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
      var1 = 31L * var1 + VecMathUtil.doubleToLongBits(this.angle);
      return (int)(var1 ^ var1 >> 32);
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new InternalError();
      }
   }
}

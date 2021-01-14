package javax.vecmath;

import java.io.Serializable;

public class Quat4d extends Tuple4d implements Serializable {
   static final long serialVersionUID = 7577479888820201099L;
   static final double EPS = 1.0E-6D;
   static final double EPS2 = 1.0E-30D;
   static final double PIO2 = 1.57079632679D;

   public Quat4d(double var1, double var3, double var5, double var7) {
      double var9 = 1.0D / Math.sqrt(var1 * var1 + var3 * var3 + var5 * var5 + var7 * var7);
      this.x = var1 * var9;
      this.y = var3 * var9;
      this.z = var5 * var9;
      this.w = var7 * var9;
   }

   public Quat4d(double[] var1) {
      double var2 = 1.0D / Math.sqrt(var1[0] * var1[0] + var1[1] * var1[1] + var1[2] * var1[2] + var1[3] * var1[3]);
      this.x = var1[0] * var2;
      this.y = var1[1] * var2;
      this.z = var1[2] * var2;
      this.w = var1[3] * var2;
   }

   public Quat4d(Quat4d var1) {
      super((Tuple4d)var1);
   }

   public Quat4d(Quat4f var1) {
      super((Tuple4f)var1);
   }

   public Quat4d(Tuple4f var1) {
      double var2 = 1.0D / Math.sqrt((double)(var1.x * var1.x + var1.y * var1.y + var1.z * var1.z + var1.w * var1.w));
      this.x = (double)var1.x * var2;
      this.y = (double)var1.y * var2;
      this.z = (double)var1.z * var2;
      this.w = (double)var1.w * var2;
   }

   public Quat4d(Tuple4d var1) {
      double var2 = 1.0D / Math.sqrt(var1.x * var1.x + var1.y * var1.y + var1.z * var1.z + var1.w * var1.w);
      this.x = var1.x * var2;
      this.y = var1.y * var2;
      this.z = var1.z * var2;
      this.w = var1.w * var2;
   }

   public Quat4d() {
   }

   public final void conjugate(Quat4d var1) {
      this.x = -var1.x;
      this.y = -var1.y;
      this.z = -var1.z;
      this.w = var1.w;
   }

   public final void conjugate() {
      this.x = -this.x;
      this.y = -this.y;
      this.z = -this.z;
   }

   public final void mul(Quat4d var1, Quat4d var2) {
      if (this != var1 && this != var2) {
         this.w = var1.w * var2.w - var1.x * var2.x - var1.y * var2.y - var1.z * var2.z;
         this.x = var1.w * var2.x + var2.w * var1.x + var1.y * var2.z - var1.z * var2.y;
         this.y = var1.w * var2.y + var2.w * var1.y - var1.x * var2.z + var1.z * var2.x;
         this.z = var1.w * var2.z + var2.w * var1.z + var1.x * var2.y - var1.y * var2.x;
      } else {
         double var7 = var1.w * var2.w - var1.x * var2.x - var1.y * var2.y - var1.z * var2.z;
         double var3 = var1.w * var2.x + var2.w * var1.x + var1.y * var2.z - var1.z * var2.y;
         double var5 = var1.w * var2.y + var2.w * var1.y - var1.x * var2.z + var1.z * var2.x;
         this.z = var1.w * var2.z + var2.w * var1.z + var1.x * var2.y - var1.y * var2.x;
         this.w = var7;
         this.x = var3;
         this.y = var5;
      }

   }

   public final void mul(Quat4d var1) {
      double var6 = this.w * var1.w - this.x * var1.x - this.y * var1.y - this.z * var1.z;
      double var2 = this.w * var1.x + var1.w * this.x + this.y * var1.z - this.z * var1.y;
      double var4 = this.w * var1.y + var1.w * this.y - this.x * var1.z + this.z * var1.x;
      this.z = this.w * var1.z + var1.w * this.z + this.x * var1.y - this.y * var1.x;
      this.w = var6;
      this.x = var2;
      this.y = var4;
   }

   public final void mulInverse(Quat4d var1, Quat4d var2) {
      Quat4d var3 = new Quat4d(var2);
      var3.inverse();
      this.mul(var1, var3);
   }

   public final void mulInverse(Quat4d var1) {
      Quat4d var2 = new Quat4d(var1);
      var2.inverse();
      this.mul(var2);
   }

   public final void inverse(Quat4d var1) {
      double var2 = 1.0D / (var1.w * var1.w + var1.x * var1.x + var1.y * var1.y + var1.z * var1.z);
      this.w = var2 * var1.w;
      this.x = -var2 * var1.x;
      this.y = -var2 * var1.y;
      this.z = -var2 * var1.z;
   }

   public final void inverse() {
      double var1 = 1.0D / (this.w * this.w + this.x * this.x + this.y * this.y + this.z * this.z);
      this.w *= var1;
      this.x *= -var1;
      this.y *= -var1;
      this.z *= -var1;
   }

   public final void normalize(Quat4d var1) {
      double var2 = var1.x * var1.x + var1.y * var1.y + var1.z * var1.z + var1.w * var1.w;
      if (var2 > 0.0D) {
         var2 = 1.0D / Math.sqrt(var2);
         this.x = var2 * var1.x;
         this.y = var2 * var1.y;
         this.z = var2 * var1.z;
         this.w = var2 * var1.w;
      } else {
         this.x = 0.0D;
         this.y = 0.0D;
         this.z = 0.0D;
         this.w = 0.0D;
      }

   }

   public final void normalize() {
      double var1 = this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
      if (var1 > 0.0D) {
         var1 = 1.0D / Math.sqrt(var1);
         this.x *= var1;
         this.y *= var1;
         this.z *= var1;
         this.w *= var1;
      } else {
         this.x = 0.0D;
         this.y = 0.0D;
         this.z = 0.0D;
         this.w = 0.0D;
      }

   }

   public final void set(Matrix4f var1) {
      double var2 = 0.25D * (double)(var1.m00 + var1.m11 + var1.m22 + var1.m33);
      if (var2 >= 0.0D) {
         if (var2 >= 1.0E-30D) {
            this.w = Math.sqrt(var2);
            var2 = 0.25D / this.w;
            this.x = (double)(var1.m21 - var1.m12) * var2;
            this.y = (double)(var1.m02 - var1.m20) * var2;
            this.z = (double)(var1.m10 - var1.m01) * var2;
         } else {
            this.w = 0.0D;
            var2 = -0.5D * (double)(var1.m11 + var1.m22);
            if (var2 >= 0.0D) {
               if (var2 >= 1.0E-30D) {
                  this.x = Math.sqrt(var2);
                  var2 = 1.0D / (2.0D * this.x);
                  this.y = (double)var1.m10 * var2;
                  this.z = (double)var1.m20 * var2;
               } else {
                  this.x = 0.0D;
                  var2 = 0.5D * (1.0D - (double)var1.m22);
                  if (var2 >= 1.0E-30D) {
                     this.y = Math.sqrt(var2);
                     this.z = (double)var1.m21 / (2.0D * this.y);
                  } else {
                     this.y = 0.0D;
                     this.z = 1.0D;
                  }
               }
            } else {
               this.x = 0.0D;
               this.y = 0.0D;
               this.z = 1.0D;
            }
         }
      } else {
         this.w = 0.0D;
         this.x = 0.0D;
         this.y = 0.0D;
         this.z = 1.0D;
      }
   }

   public final void set(Matrix4d var1) {
      double var2 = 0.25D * (var1.m00 + var1.m11 + var1.m22 + var1.m33);
      if (var2 >= 0.0D) {
         if (var2 >= 1.0E-30D) {
            this.w = Math.sqrt(var2);
            var2 = 0.25D / this.w;
            this.x = (var1.m21 - var1.m12) * var2;
            this.y = (var1.m02 - var1.m20) * var2;
            this.z = (var1.m10 - var1.m01) * var2;
         } else {
            this.w = 0.0D;
            var2 = -0.5D * (var1.m11 + var1.m22);
            if (var2 >= 0.0D) {
               if (var2 >= 1.0E-30D) {
                  this.x = Math.sqrt(var2);
                  var2 = 0.5D / this.x;
                  this.y = var1.m10 * var2;
                  this.z = var1.m20 * var2;
               } else {
                  this.x = 0.0D;
                  var2 = 0.5D * (1.0D - var1.m22);
                  if (var2 >= 1.0E-30D) {
                     this.y = Math.sqrt(var2);
                     this.z = var1.m21 / (2.0D * this.y);
                  } else {
                     this.y = 0.0D;
                     this.z = 1.0D;
                  }
               }
            } else {
               this.x = 0.0D;
               this.y = 0.0D;
               this.z = 1.0D;
            }
         }
      } else {
         this.w = 0.0D;
         this.x = 0.0D;
         this.y = 0.0D;
         this.z = 1.0D;
      }
   }

   public final void set(Matrix3f var1) {
      double var2 = 0.25D * ((double)(var1.m00 + var1.m11 + var1.m22) + 1.0D);
      if (var2 >= 0.0D) {
         if (var2 >= 1.0E-30D) {
            this.w = Math.sqrt(var2);
            var2 = 0.25D / this.w;
            this.x = (double)(var1.m21 - var1.m12) * var2;
            this.y = (double)(var1.m02 - var1.m20) * var2;
            this.z = (double)(var1.m10 - var1.m01) * var2;
         } else {
            this.w = 0.0D;
            var2 = -0.5D * (double)(var1.m11 + var1.m22);
            if (var2 >= 0.0D) {
               if (var2 >= 1.0E-30D) {
                  this.x = Math.sqrt(var2);
                  var2 = 0.5D / this.x;
                  this.y = (double)var1.m10 * var2;
                  this.z = (double)var1.m20 * var2;
               } else {
                  this.x = 0.0D;
                  var2 = 0.5D * (1.0D - (double)var1.m22);
                  if (var2 >= 1.0E-30D) {
                     this.y = Math.sqrt(var2);
                     this.z = (double)var1.m21 / (2.0D * this.y);
                  }

                  this.y = 0.0D;
                  this.z = 1.0D;
               }
            } else {
               this.x = 0.0D;
               this.y = 0.0D;
               this.z = 1.0D;
            }
         }
      } else {
         this.w = 0.0D;
         this.x = 0.0D;
         this.y = 0.0D;
         this.z = 1.0D;
      }
   }

   public final void set(Matrix3d var1) {
      double var2 = 0.25D * (var1.m00 + var1.m11 + var1.m22 + 1.0D);
      if (var2 >= 0.0D) {
         if (var2 >= 1.0E-30D) {
            this.w = Math.sqrt(var2);
            var2 = 0.25D / this.w;
            this.x = (var1.m21 - var1.m12) * var2;
            this.y = (var1.m02 - var1.m20) * var2;
            this.z = (var1.m10 - var1.m01) * var2;
         } else {
            this.w = 0.0D;
            var2 = -0.5D * (var1.m11 + var1.m22);
            if (var2 >= 0.0D) {
               if (var2 >= 1.0E-30D) {
                  this.x = Math.sqrt(var2);
                  var2 = 0.5D / this.x;
                  this.y = var1.m10 * var2;
                  this.z = var1.m20 * var2;
               } else {
                  this.x = 0.0D;
                  var2 = 0.5D * (1.0D - var1.m22);
                  if (var2 >= 1.0E-30D) {
                     this.y = Math.sqrt(var2);
                     this.z = var1.m21 / (2.0D * this.y);
                  } else {
                     this.y = 0.0D;
                     this.z = 1.0D;
                  }
               }
            } else {
               this.x = 0.0D;
               this.y = 0.0D;
               this.z = 1.0D;
            }
         }
      } else {
         this.w = 0.0D;
         this.x = 0.0D;
         this.y = 0.0D;
         this.z = 1.0D;
      }
   }

   public final void set(AxisAngle4f var1) {
      double var4 = Math.sqrt((double)(var1.x * var1.x + var1.y * var1.y + var1.z * var1.z));
      if (var4 < 1.0E-6D) {
         this.w = 0.0D;
         this.x = 0.0D;
         this.y = 0.0D;
         this.z = 0.0D;
      } else {
         double var2 = Math.sin((double)var1.angle / 2.0D);
         var4 = 1.0D / var4;
         this.w = Math.cos((double)var1.angle / 2.0D);
         this.x = (double)var1.x * var4 * var2;
         this.y = (double)var1.y * var4 * var2;
         this.z = (double)var1.z * var4 * var2;
      }

   }

   public final void set(AxisAngle4d var1) {
      double var4 = Math.sqrt(var1.x * var1.x + var1.y * var1.y + var1.z * var1.z);
      if (var4 < 1.0E-6D) {
         this.w = 0.0D;
         this.x = 0.0D;
         this.y = 0.0D;
         this.z = 0.0D;
      } else {
         var4 = 1.0D / var4;
         double var2 = Math.sin(var1.angle / 2.0D);
         this.w = Math.cos(var1.angle / 2.0D);
         this.x = var1.x * var4 * var2;
         this.y = var1.y * var4 * var2;
         this.z = var1.z * var4 * var2;
      }

   }

   public final void interpolate(Quat4d var1, double var2) {
      double var4 = this.x * var1.x + this.y * var1.y + this.z * var1.z + this.w * var1.w;
      if (var4 < 0.0D) {
         var1.x = -var1.x;
         var1.y = -var1.y;
         var1.z = -var1.z;
         var1.w = -var1.w;
         var4 = -var4;
      }

      double var6;
      double var8;
      if (1.0D - var4 > 1.0E-6D) {
         double var10 = Math.acos(var4);
         double var12 = Math.sin(var10);
         var6 = Math.sin((1.0D - var2) * var10) / var12;
         var8 = Math.sin(var2 * var10) / var12;
      } else {
         var6 = 1.0D - var2;
         var8 = var2;
      }

      this.w = var6 * this.w + var8 * var1.w;
      this.x = var6 * this.x + var8 * var1.x;
      this.y = var6 * this.y + var8 * var1.y;
      this.z = var6 * this.z + var8 * var1.z;
   }

   public final void interpolate(Quat4d var1, Quat4d var2, double var3) {
      double var5 = var2.x * var1.x + var2.y * var1.y + var2.z * var1.z + var2.w * var1.w;
      if (var5 < 0.0D) {
         var1.x = -var1.x;
         var1.y = -var1.y;
         var1.z = -var1.z;
         var1.w = -var1.w;
         var5 = -var5;
      }

      double var7;
      double var9;
      if (1.0D - var5 > 1.0E-6D) {
         double var11 = Math.acos(var5);
         double var13 = Math.sin(var11);
         var7 = Math.sin((1.0D - var3) * var11) / var13;
         var9 = Math.sin(var3 * var11) / var13;
      } else {
         var7 = 1.0D - var3;
         var9 = var3;
      }

      this.w = var7 * var1.w + var9 * var2.w;
      this.x = var7 * var1.x + var9 * var2.x;
      this.y = var7 * var1.y + var9 * var2.y;
      this.z = var7 * var1.z + var9 * var2.z;
   }
}

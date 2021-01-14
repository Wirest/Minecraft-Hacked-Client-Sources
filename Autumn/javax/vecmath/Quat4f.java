package javax.vecmath;

import java.io.Serializable;

public class Quat4f extends Tuple4f implements Serializable {
   static final long serialVersionUID = 2675933778405442383L;
   static final double EPS = 1.0E-6D;
   static final double EPS2 = 1.0E-30D;
   static final double PIO2 = 1.57079632679D;

   public Quat4f(float var1, float var2, float var3, float var4) {
      float var5 = (float)(1.0D / Math.sqrt((double)(var1 * var1 + var2 * var2 + var3 * var3 + var4 * var4)));
      this.x = var1 * var5;
      this.y = var2 * var5;
      this.z = var3 * var5;
      this.w = var4 * var5;
   }

   public Quat4f(float[] var1) {
      float var2 = (float)(1.0D / Math.sqrt((double)(var1[0] * var1[0] + var1[1] * var1[1] + var1[2] * var1[2] + var1[3] * var1[3])));
      this.x = var1[0] * var2;
      this.y = var1[1] * var2;
      this.z = var1[2] * var2;
      this.w = var1[3] * var2;
   }

   public Quat4f(Quat4f var1) {
      super((Tuple4f)var1);
   }

   public Quat4f(Quat4d var1) {
      super((Tuple4d)var1);
   }

   public Quat4f(Tuple4f var1) {
      float var2 = (float)(1.0D / Math.sqrt((double)(var1.x * var1.x + var1.y * var1.y + var1.z * var1.z + var1.w * var1.w)));
      this.x = var1.x * var2;
      this.y = var1.y * var2;
      this.z = var1.z * var2;
      this.w = var1.w * var2;
   }

   public Quat4f(Tuple4d var1) {
      double var2 = 1.0D / Math.sqrt(var1.x * var1.x + var1.y * var1.y + var1.z * var1.z + var1.w * var1.w);
      this.x = (float)(var1.x * var2);
      this.y = (float)(var1.y * var2);
      this.z = (float)(var1.z * var2);
      this.w = (float)(var1.w * var2);
   }

   public Quat4f() {
   }

   public final void conjugate(Quat4f var1) {
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

   public final void mul(Quat4f var1, Quat4f var2) {
      if (this != var1 && this != var2) {
         this.w = var1.w * var2.w - var1.x * var2.x - var1.y * var2.y - var1.z * var2.z;
         this.x = var1.w * var2.x + var2.w * var1.x + var1.y * var2.z - var1.z * var2.y;
         this.y = var1.w * var2.y + var2.w * var1.y - var1.x * var2.z + var1.z * var2.x;
         this.z = var1.w * var2.z + var2.w * var1.z + var1.x * var2.y - var1.y * var2.x;
      } else {
         float var5 = var1.w * var2.w - var1.x * var2.x - var1.y * var2.y - var1.z * var2.z;
         float var3 = var1.w * var2.x + var2.w * var1.x + var1.y * var2.z - var1.z * var2.y;
         float var4 = var1.w * var2.y + var2.w * var1.y - var1.x * var2.z + var1.z * var2.x;
         this.z = var1.w * var2.z + var2.w * var1.z + var1.x * var2.y - var1.y * var2.x;
         this.w = var5;
         this.x = var3;
         this.y = var4;
      }

   }

   public final void mul(Quat4f var1) {
      float var4 = this.w * var1.w - this.x * var1.x - this.y * var1.y - this.z * var1.z;
      float var2 = this.w * var1.x + var1.w * this.x + this.y * var1.z - this.z * var1.y;
      float var3 = this.w * var1.y + var1.w * this.y - this.x * var1.z + this.z * var1.x;
      this.z = this.w * var1.z + var1.w * this.z + this.x * var1.y - this.y * var1.x;
      this.w = var4;
      this.x = var2;
      this.y = var3;
   }

   public final void mulInverse(Quat4f var1, Quat4f var2) {
      Quat4f var3 = new Quat4f(var2);
      var3.inverse();
      this.mul(var1, var3);
   }

   public final void mulInverse(Quat4f var1) {
      Quat4f var2 = new Quat4f(var1);
      var2.inverse();
      this.mul(var2);
   }

   public final void inverse(Quat4f var1) {
      float var2 = 1.0F / (var1.w * var1.w + var1.x * var1.x + var1.y * var1.y + var1.z * var1.z);
      this.w = var2 * var1.w;
      this.x = -var2 * var1.x;
      this.y = -var2 * var1.y;
      this.z = -var2 * var1.z;
   }

   public final void inverse() {
      float var1 = 1.0F / (this.w * this.w + this.x * this.x + this.y * this.y + this.z * this.z);
      this.w *= var1;
      this.x *= -var1;
      this.y *= -var1;
      this.z *= -var1;
   }

   public final void normalize(Quat4f var1) {
      float var2 = var1.x * var1.x + var1.y * var1.y + var1.z * var1.z + var1.w * var1.w;
      if (var2 > 0.0F) {
         var2 = 1.0F / (float)Math.sqrt((double)var2);
         this.x = var2 * var1.x;
         this.y = var2 * var1.y;
         this.z = var2 * var1.z;
         this.w = var2 * var1.w;
      } else {
         this.x = 0.0F;
         this.y = 0.0F;
         this.z = 0.0F;
         this.w = 0.0F;
      }

   }

   public final void normalize() {
      float var1 = this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
      if (var1 > 0.0F) {
         var1 = 1.0F / (float)Math.sqrt((double)var1);
         this.x *= var1;
         this.y *= var1;
         this.z *= var1;
         this.w *= var1;
      } else {
         this.x = 0.0F;
         this.y = 0.0F;
         this.z = 0.0F;
         this.w = 0.0F;
      }

   }

   public final void set(Matrix4f var1) {
      float var2 = 0.25F * (var1.m00 + var1.m11 + var1.m22 + var1.m33);
      if (var2 >= 0.0F) {
         if ((double)var2 >= 1.0E-30D) {
            this.w = (float)Math.sqrt((double)var2);
            var2 = 0.25F / this.w;
            this.x = (var1.m21 - var1.m12) * var2;
            this.y = (var1.m02 - var1.m20) * var2;
            this.z = (var1.m10 - var1.m01) * var2;
         } else {
            this.w = 0.0F;
            var2 = -0.5F * (var1.m11 + var1.m22);
            if (var2 >= 0.0F) {
               if ((double)var2 >= 1.0E-30D) {
                  this.x = (float)Math.sqrt((double)var2);
                  var2 = 1.0F / (2.0F * this.x);
                  this.y = var1.m10 * var2;
                  this.z = var1.m20 * var2;
               } else {
                  this.x = 0.0F;
                  var2 = 0.5F * (1.0F - var1.m22);
                  if ((double)var2 >= 1.0E-30D) {
                     this.y = (float)Math.sqrt((double)var2);
                     this.z = var1.m21 / (2.0F * this.y);
                  } else {
                     this.y = 0.0F;
                     this.z = 1.0F;
                  }
               }
            } else {
               this.x = 0.0F;
               this.y = 0.0F;
               this.z = 1.0F;
            }
         }
      } else {
         this.w = 0.0F;
         this.x = 0.0F;
         this.y = 0.0F;
         this.z = 1.0F;
      }
   }

   public final void set(Matrix4d var1) {
      double var2 = 0.25D * (var1.m00 + var1.m11 + var1.m22 + var1.m33);
      if (var2 >= 0.0D) {
         if (var2 >= 1.0E-30D) {
            this.w = (float)Math.sqrt(var2);
            var2 = 0.25D / (double)this.w;
            this.x = (float)((var1.m21 - var1.m12) * var2);
            this.y = (float)((var1.m02 - var1.m20) * var2);
            this.z = (float)((var1.m10 - var1.m01) * var2);
         } else {
            this.w = 0.0F;
            var2 = -0.5D * (var1.m11 + var1.m22);
            if (var2 >= 0.0D) {
               if (var2 >= 1.0E-30D) {
                  this.x = (float)Math.sqrt(var2);
                  var2 = 0.5D / (double)this.x;
                  this.y = (float)(var1.m10 * var2);
                  this.z = (float)(var1.m20 * var2);
               } else {
                  this.x = 0.0F;
                  var2 = 0.5D * (1.0D - var1.m22);
                  if (var2 >= 1.0E-30D) {
                     this.y = (float)Math.sqrt(var2);
                     this.z = (float)(var1.m21 / (2.0D * (double)this.y));
                  } else {
                     this.y = 0.0F;
                     this.z = 1.0F;
                  }
               }
            } else {
               this.x = 0.0F;
               this.y = 0.0F;
               this.z = 1.0F;
            }
         }
      } else {
         this.w = 0.0F;
         this.x = 0.0F;
         this.y = 0.0F;
         this.z = 1.0F;
      }
   }

   public final void set(Matrix3f var1) {
      float var2 = 0.25F * (var1.m00 + var1.m11 + var1.m22 + 1.0F);
      if (var2 >= 0.0F) {
         if ((double)var2 >= 1.0E-30D) {
            this.w = (float)Math.sqrt((double)var2);
            var2 = 0.25F / this.w;
            this.x = (var1.m21 - var1.m12) * var2;
            this.y = (var1.m02 - var1.m20) * var2;
            this.z = (var1.m10 - var1.m01) * var2;
         } else {
            this.w = 0.0F;
            var2 = -0.5F * (var1.m11 + var1.m22);
            if (var2 >= 0.0F) {
               if ((double)var2 >= 1.0E-30D) {
                  this.x = (float)Math.sqrt((double)var2);
                  var2 = 0.5F / this.x;
                  this.y = var1.m10 * var2;
                  this.z = var1.m20 * var2;
               } else {
                  this.x = 0.0F;
                  var2 = 0.5F * (1.0F - var1.m22);
                  if ((double)var2 >= 1.0E-30D) {
                     this.y = (float)Math.sqrt((double)var2);
                     this.z = var1.m21 / (2.0F * this.y);
                  } else {
                     this.y = 0.0F;
                     this.z = 1.0F;
                  }
               }
            } else {
               this.x = 0.0F;
               this.y = 0.0F;
               this.z = 1.0F;
            }
         }
      } else {
         this.w = 0.0F;
         this.x = 0.0F;
         this.y = 0.0F;
         this.z = 1.0F;
      }
   }

   public final void set(Matrix3d var1) {
      double var2 = 0.25D * (var1.m00 + var1.m11 + var1.m22 + 1.0D);
      if (var2 >= 0.0D) {
         if (var2 >= 1.0E-30D) {
            this.w = (float)Math.sqrt(var2);
            var2 = 0.25D / (double)this.w;
            this.x = (float)((var1.m21 - var1.m12) * var2);
            this.y = (float)((var1.m02 - var1.m20) * var2);
            this.z = (float)((var1.m10 - var1.m01) * var2);
         } else {
            this.w = 0.0F;
            var2 = -0.5D * (var1.m11 + var1.m22);
            if (var2 >= 0.0D) {
               if (var2 >= 1.0E-30D) {
                  this.x = (float)Math.sqrt(var2);
                  var2 = 0.5D / (double)this.x;
                  this.y = (float)(var1.m10 * var2);
                  this.z = (float)(var1.m20 * var2);
               } else {
                  this.x = 0.0F;
                  var2 = 0.5D * (1.0D - var1.m22);
                  if (var2 >= 1.0E-30D) {
                     this.y = (float)Math.sqrt(var2);
                     this.z = (float)(var1.m21 / (2.0D * (double)this.y));
                  } else {
                     this.y = 0.0F;
                     this.z = 1.0F;
                  }
               }
            } else {
               this.x = 0.0F;
               this.y = 0.0F;
               this.z = 1.0F;
            }
         }
      } else {
         this.w = 0.0F;
         this.x = 0.0F;
         this.y = 0.0F;
         this.z = 1.0F;
      }
   }

   public final void set(AxisAngle4f var1) {
      float var3 = (float)Math.sqrt((double)(var1.x * var1.x + var1.y * var1.y + var1.z * var1.z));
      if ((double)var3 < 1.0E-6D) {
         this.w = 0.0F;
         this.x = 0.0F;
         this.y = 0.0F;
         this.z = 0.0F;
      } else {
         var3 = 1.0F / var3;
         float var2 = (float)Math.sin((double)var1.angle / 2.0D);
         this.w = (float)Math.cos((double)var1.angle / 2.0D);
         this.x = var1.x * var3 * var2;
         this.y = var1.y * var3 * var2;
         this.z = var1.z * var3 * var2;
      }

   }

   public final void set(AxisAngle4d var1) {
      float var3 = (float)(1.0D / Math.sqrt(var1.x * var1.x + var1.y * var1.y + var1.z * var1.z));
      if ((double)var3 < 1.0E-6D) {
         this.w = 0.0F;
         this.x = 0.0F;
         this.y = 0.0F;
         this.z = 0.0F;
      } else {
         var3 = 1.0F / var3;
         float var2 = (float)Math.sin(var1.angle / 2.0D);
         this.w = (float)Math.cos(var1.angle / 2.0D);
         this.x = (float)var1.x * var3 * var2;
         this.y = (float)var1.y * var3 * var2;
         this.z = (float)var1.z * var3 * var2;
      }

   }

   public final void interpolate(Quat4f var1, float var2) {
      double var3 = (double)(this.x * var1.x + this.y * var1.y + this.z * var1.z + this.w * var1.w);
      if (var3 < 0.0D) {
         var1.x = -var1.x;
         var1.y = -var1.y;
         var1.z = -var1.z;
         var1.w = -var1.w;
         var3 = -var3;
      }

      double var5;
      double var7;
      if (1.0D - var3 > 1.0E-6D) {
         double var9 = Math.acos(var3);
         double var11 = Math.sin(var9);
         var5 = Math.sin((1.0D - (double)var2) * var9) / var11;
         var7 = Math.sin((double)var2 * var9) / var11;
      } else {
         var5 = 1.0D - (double)var2;
         var7 = (double)var2;
      }

      this.w = (float)(var5 * (double)this.w + var7 * (double)var1.w);
      this.x = (float)(var5 * (double)this.x + var7 * (double)var1.x);
      this.y = (float)(var5 * (double)this.y + var7 * (double)var1.y);
      this.z = (float)(var5 * (double)this.z + var7 * (double)var1.z);
   }

   public final void interpolate(Quat4f var1, Quat4f var2, float var3) {
      double var4 = (double)(var2.x * var1.x + var2.y * var1.y + var2.z * var1.z + var2.w * var1.w);
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
         var6 = Math.sin((1.0D - (double)var3) * var10) / var12;
         var8 = Math.sin((double)var3 * var10) / var12;
      } else {
         var6 = 1.0D - (double)var3;
         var8 = (double)var3;
      }

      this.w = (float)(var6 * (double)var1.w + var8 * (double)var2.w);
      this.x = (float)(var6 * (double)var1.x + var8 * (double)var2.x);
      this.y = (float)(var6 * (double)var1.y + var8 * (double)var2.y);
      this.z = (float)(var6 * (double)var1.z + var8 * (double)var2.z);
   }
}

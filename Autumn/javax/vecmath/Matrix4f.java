package javax.vecmath;

import java.io.Serializable;

public class Matrix4f implements Serializable, Cloneable {
   static final long serialVersionUID = -8405036035410109353L;
   public float m00;
   public float m01;
   public float m02;
   public float m03;
   public float m10;
   public float m11;
   public float m12;
   public float m13;
   public float m20;
   public float m21;
   public float m22;
   public float m23;
   public float m30;
   public float m31;
   public float m32;
   public float m33;
   private static final double EPS = 1.0E-8D;

   public Matrix4f(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16) {
      this.m00 = var1;
      this.m01 = var2;
      this.m02 = var3;
      this.m03 = var4;
      this.m10 = var5;
      this.m11 = var6;
      this.m12 = var7;
      this.m13 = var8;
      this.m20 = var9;
      this.m21 = var10;
      this.m22 = var11;
      this.m23 = var12;
      this.m30 = var13;
      this.m31 = var14;
      this.m32 = var15;
      this.m33 = var16;
   }

   public Matrix4f(float[] var1) {
      this.m00 = var1[0];
      this.m01 = var1[1];
      this.m02 = var1[2];
      this.m03 = var1[3];
      this.m10 = var1[4];
      this.m11 = var1[5];
      this.m12 = var1[6];
      this.m13 = var1[7];
      this.m20 = var1[8];
      this.m21 = var1[9];
      this.m22 = var1[10];
      this.m23 = var1[11];
      this.m30 = var1[12];
      this.m31 = var1[13];
      this.m32 = var1[14];
      this.m33 = var1[15];
   }

   public Matrix4f(Quat4f var1, Vector3f var2, float var3) {
      this.m00 = (float)((double)var3 * (1.0D - 2.0D * (double)var1.y * (double)var1.y - 2.0D * (double)var1.z * (double)var1.z));
      this.m10 = (float)((double)var3 * 2.0D * (double)(var1.x * var1.y + var1.w * var1.z));
      this.m20 = (float)((double)var3 * 2.0D * (double)(var1.x * var1.z - var1.w * var1.y));
      this.m01 = (float)((double)var3 * 2.0D * (double)(var1.x * var1.y - var1.w * var1.z));
      this.m11 = (float)((double)var3 * (1.0D - 2.0D * (double)var1.x * (double)var1.x - 2.0D * (double)var1.z * (double)var1.z));
      this.m21 = (float)((double)var3 * 2.0D * (double)(var1.y * var1.z + var1.w * var1.x));
      this.m02 = (float)((double)var3 * 2.0D * (double)(var1.x * var1.z + var1.w * var1.y));
      this.m12 = (float)((double)var3 * 2.0D * (double)(var1.y * var1.z - var1.w * var1.x));
      this.m22 = (float)((double)var3 * (1.0D - 2.0D * (double)var1.x * (double)var1.x - 2.0D * (double)var1.y * (double)var1.y));
      this.m03 = var2.x;
      this.m13 = var2.y;
      this.m23 = var2.z;
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.m33 = 1.0F;
   }

   public Matrix4f(Matrix4d var1) {
      this.m00 = (float)var1.m00;
      this.m01 = (float)var1.m01;
      this.m02 = (float)var1.m02;
      this.m03 = (float)var1.m03;
      this.m10 = (float)var1.m10;
      this.m11 = (float)var1.m11;
      this.m12 = (float)var1.m12;
      this.m13 = (float)var1.m13;
      this.m20 = (float)var1.m20;
      this.m21 = (float)var1.m21;
      this.m22 = (float)var1.m22;
      this.m23 = (float)var1.m23;
      this.m30 = (float)var1.m30;
      this.m31 = (float)var1.m31;
      this.m32 = (float)var1.m32;
      this.m33 = (float)var1.m33;
   }

   public Matrix4f(Matrix4f var1) {
      this.m00 = var1.m00;
      this.m01 = var1.m01;
      this.m02 = var1.m02;
      this.m03 = var1.m03;
      this.m10 = var1.m10;
      this.m11 = var1.m11;
      this.m12 = var1.m12;
      this.m13 = var1.m13;
      this.m20 = var1.m20;
      this.m21 = var1.m21;
      this.m22 = var1.m22;
      this.m23 = var1.m23;
      this.m30 = var1.m30;
      this.m31 = var1.m31;
      this.m32 = var1.m32;
      this.m33 = var1.m33;
   }

   public Matrix4f(Matrix3f var1, Vector3f var2, float var3) {
      this.m00 = var1.m00 * var3;
      this.m01 = var1.m01 * var3;
      this.m02 = var1.m02 * var3;
      this.m03 = var2.x;
      this.m10 = var1.m10 * var3;
      this.m11 = var1.m11 * var3;
      this.m12 = var1.m12 * var3;
      this.m13 = var2.y;
      this.m20 = var1.m20 * var3;
      this.m21 = var1.m21 * var3;
      this.m22 = var1.m22 * var3;
      this.m23 = var2.z;
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.m33 = 1.0F;
   }

   public Matrix4f() {
      this.m00 = 0.0F;
      this.m01 = 0.0F;
      this.m02 = 0.0F;
      this.m03 = 0.0F;
      this.m10 = 0.0F;
      this.m11 = 0.0F;
      this.m12 = 0.0F;
      this.m13 = 0.0F;
      this.m20 = 0.0F;
      this.m21 = 0.0F;
      this.m22 = 0.0F;
      this.m23 = 0.0F;
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.m33 = 0.0F;
   }

   public String toString() {
      return this.m00 + ", " + this.m01 + ", " + this.m02 + ", " + this.m03 + "\n" + this.m10 + ", " + this.m11 + ", " + this.m12 + ", " + this.m13 + "\n" + this.m20 + ", " + this.m21 + ", " + this.m22 + ", " + this.m23 + "\n" + this.m30 + ", " + this.m31 + ", " + this.m32 + ", " + this.m33 + "\n";
   }

   public final void setIdentity() {
      this.m00 = 1.0F;
      this.m01 = 0.0F;
      this.m02 = 0.0F;
      this.m03 = 0.0F;
      this.m10 = 0.0F;
      this.m11 = 1.0F;
      this.m12 = 0.0F;
      this.m13 = 0.0F;
      this.m20 = 0.0F;
      this.m21 = 0.0F;
      this.m22 = 1.0F;
      this.m23 = 0.0F;
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.m33 = 1.0F;
   }

   public final void setElement(int var1, int var2, float var3) {
      switch(var1) {
      case 0:
         switch(var2) {
         case 0:
            this.m00 = var3;
            return;
         case 1:
            this.m01 = var3;
            return;
         case 2:
            this.m02 = var3;
            return;
         case 3:
            this.m03 = var3;
            return;
         default:
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f0"));
         }
      case 1:
         switch(var2) {
         case 0:
            this.m10 = var3;
            return;
         case 1:
            this.m11 = var3;
            return;
         case 2:
            this.m12 = var3;
            return;
         case 3:
            this.m13 = var3;
            return;
         default:
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f0"));
         }
      case 2:
         switch(var2) {
         case 0:
            this.m20 = var3;
            return;
         case 1:
            this.m21 = var3;
            return;
         case 2:
            this.m22 = var3;
            return;
         case 3:
            this.m23 = var3;
            return;
         default:
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f0"));
         }
      case 3:
         switch(var2) {
         case 0:
            this.m30 = var3;
            return;
         case 1:
            this.m31 = var3;
            return;
         case 2:
            this.m32 = var3;
            return;
         case 3:
            this.m33 = var3;
            return;
         default:
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f0"));
         }
      default:
         throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f0"));
      }
   }

   public final float getElement(int var1, int var2) {
      switch(var1) {
      case 0:
         switch(var2) {
         case 0:
            return this.m00;
         case 1:
            return this.m01;
         case 2:
            return this.m02;
         case 3:
            return this.m03;
         default:
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f1"));
         }
      case 1:
         switch(var2) {
         case 0:
            return this.m10;
         case 1:
            return this.m11;
         case 2:
            return this.m12;
         case 3:
            return this.m13;
         default:
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f1"));
         }
      case 2:
         switch(var2) {
         case 0:
            return this.m20;
         case 1:
            return this.m21;
         case 2:
            return this.m22;
         case 3:
            return this.m23;
         default:
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f1"));
         }
      case 3:
         switch(var2) {
         case 0:
            return this.m30;
         case 1:
            return this.m31;
         case 2:
            return this.m32;
         case 3:
            return this.m33;
         }
      }

      throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f1"));
   }

   public final void getRow(int var1, Vector4f var2) {
      if (var1 == 0) {
         var2.x = this.m00;
         var2.y = this.m01;
         var2.z = this.m02;
         var2.w = this.m03;
      } else if (var1 == 1) {
         var2.x = this.m10;
         var2.y = this.m11;
         var2.z = this.m12;
         var2.w = this.m13;
      } else if (var1 == 2) {
         var2.x = this.m20;
         var2.y = this.m21;
         var2.z = this.m22;
         var2.w = this.m23;
      } else {
         if (var1 != 3) {
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f2"));
         }

         var2.x = this.m30;
         var2.y = this.m31;
         var2.z = this.m32;
         var2.w = this.m33;
      }

   }

   public final void getRow(int var1, float[] var2) {
      if (var1 == 0) {
         var2[0] = this.m00;
         var2[1] = this.m01;
         var2[2] = this.m02;
         var2[3] = this.m03;
      } else if (var1 == 1) {
         var2[0] = this.m10;
         var2[1] = this.m11;
         var2[2] = this.m12;
         var2[3] = this.m13;
      } else if (var1 == 2) {
         var2[0] = this.m20;
         var2[1] = this.m21;
         var2[2] = this.m22;
         var2[3] = this.m23;
      } else {
         if (var1 != 3) {
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f2"));
         }

         var2[0] = this.m30;
         var2[1] = this.m31;
         var2[2] = this.m32;
         var2[3] = this.m33;
      }

   }

   public final void getColumn(int var1, Vector4f var2) {
      if (var1 == 0) {
         var2.x = this.m00;
         var2.y = this.m10;
         var2.z = this.m20;
         var2.w = this.m30;
      } else if (var1 == 1) {
         var2.x = this.m01;
         var2.y = this.m11;
         var2.z = this.m21;
         var2.w = this.m31;
      } else if (var1 == 2) {
         var2.x = this.m02;
         var2.y = this.m12;
         var2.z = this.m22;
         var2.w = this.m32;
      } else {
         if (var1 != 3) {
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f4"));
         }

         var2.x = this.m03;
         var2.y = this.m13;
         var2.z = this.m23;
         var2.w = this.m33;
      }

   }

   public final void getColumn(int var1, float[] var2) {
      if (var1 == 0) {
         var2[0] = this.m00;
         var2[1] = this.m10;
         var2[2] = this.m20;
         var2[3] = this.m30;
      } else if (var1 == 1) {
         var2[0] = this.m01;
         var2[1] = this.m11;
         var2[2] = this.m21;
         var2[3] = this.m31;
      } else if (var1 == 2) {
         var2[0] = this.m02;
         var2[1] = this.m12;
         var2[2] = this.m22;
         var2[3] = this.m32;
      } else {
         if (var1 != 3) {
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f4"));
         }

         var2[0] = this.m03;
         var2[1] = this.m13;
         var2[2] = this.m23;
         var2[3] = this.m33;
      }

   }

   public final void setScale(float var1) {
      double[] var2 = new double[9];
      double[] var3 = new double[3];
      this.getScaleRotate(var3, var2);
      this.m00 = (float)(var2[0] * (double)var1);
      this.m01 = (float)(var2[1] * (double)var1);
      this.m02 = (float)(var2[2] * (double)var1);
      this.m10 = (float)(var2[3] * (double)var1);
      this.m11 = (float)(var2[4] * (double)var1);
      this.m12 = (float)(var2[5] * (double)var1);
      this.m20 = (float)(var2[6] * (double)var1);
      this.m21 = (float)(var2[7] * (double)var1);
      this.m22 = (float)(var2[8] * (double)var1);
   }

   public final void get(Matrix3d var1) {
      double[] var2 = new double[9];
      double[] var3 = new double[3];
      this.getScaleRotate(var3, var2);
      var1.m00 = var2[0];
      var1.m01 = var2[1];
      var1.m02 = var2[2];
      var1.m10 = var2[3];
      var1.m11 = var2[4];
      var1.m12 = var2[5];
      var1.m20 = var2[6];
      var1.m21 = var2[7];
      var1.m22 = var2[8];
   }

   public final void get(Matrix3f var1) {
      double[] var2 = new double[9];
      double[] var3 = new double[3];
      this.getScaleRotate(var3, var2);
      var1.m00 = (float)var2[0];
      var1.m01 = (float)var2[1];
      var1.m02 = (float)var2[2];
      var1.m10 = (float)var2[3];
      var1.m11 = (float)var2[4];
      var1.m12 = (float)var2[5];
      var1.m20 = (float)var2[6];
      var1.m21 = (float)var2[7];
      var1.m22 = (float)var2[8];
   }

   public final float get(Matrix3f var1, Vector3f var2) {
      double[] var3 = new double[9];
      double[] var4 = new double[3];
      this.getScaleRotate(var4, var3);
      var1.m00 = (float)var3[0];
      var1.m01 = (float)var3[1];
      var1.m02 = (float)var3[2];
      var1.m10 = (float)var3[3];
      var1.m11 = (float)var3[4];
      var1.m12 = (float)var3[5];
      var1.m20 = (float)var3[6];
      var1.m21 = (float)var3[7];
      var1.m22 = (float)var3[8];
      var2.x = this.m03;
      var2.y = this.m13;
      var2.z = this.m23;
      return (float)Matrix3d.max3(var4);
   }

   public final void get(Quat4f var1) {
      double[] var2 = new double[9];
      double[] var3 = new double[3];
      this.getScaleRotate(var3, var2);
      double var4 = 0.25D * (1.0D + var2[0] + var2[4] + var2[8]);
      if ((var4 < 0.0D ? -var4 : var4) >= 1.0E-30D) {
         var1.w = (float)Math.sqrt(var4);
         var4 = 0.25D / (double)var1.w;
         var1.x = (float)((var2[7] - var2[5]) * var4);
         var1.y = (float)((var2[2] - var2[6]) * var4);
         var1.z = (float)((var2[3] - var2[1]) * var4);
      } else {
         var1.w = 0.0F;
         var4 = -0.5D * (var2[4] + var2[8]);
         if ((var4 < 0.0D ? -var4 : var4) >= 1.0E-30D) {
            var1.x = (float)Math.sqrt(var4);
            var4 = 0.5D / (double)var1.x;
            var1.y = (float)(var2[3] * var4);
            var1.z = (float)(var2[6] * var4);
         } else {
            var1.x = 0.0F;
            var4 = 0.5D * (1.0D - var2[8]);
            if ((var4 < 0.0D ? -var4 : var4) >= 1.0E-30D) {
               var1.y = (float)Math.sqrt(var4);
               var1.z = (float)(var2[7] / (2.0D * (double)var1.y));
            } else {
               var1.y = 0.0F;
               var1.z = 1.0F;
            }
         }
      }
   }

   public final void get(Vector3f var1) {
      var1.x = this.m03;
      var1.y = this.m13;
      var1.z = this.m23;
   }

   public final void getRotationScale(Matrix3f var1) {
      var1.m00 = this.m00;
      var1.m01 = this.m01;
      var1.m02 = this.m02;
      var1.m10 = this.m10;
      var1.m11 = this.m11;
      var1.m12 = this.m12;
      var1.m20 = this.m20;
      var1.m21 = this.m21;
      var1.m22 = this.m22;
   }

   public final float getScale() {
      double[] var1 = new double[9];
      double[] var2 = new double[3];
      this.getScaleRotate(var2, var1);
      return (float)Matrix3d.max3(var2);
   }

   public final void setRotationScale(Matrix3f var1) {
      this.m00 = var1.m00;
      this.m01 = var1.m01;
      this.m02 = var1.m02;
      this.m10 = var1.m10;
      this.m11 = var1.m11;
      this.m12 = var1.m12;
      this.m20 = var1.m20;
      this.m21 = var1.m21;
      this.m22 = var1.m22;
   }

   public final void setRow(int var1, float var2, float var3, float var4, float var5) {
      switch(var1) {
      case 0:
         this.m00 = var2;
         this.m01 = var3;
         this.m02 = var4;
         this.m03 = var5;
         break;
      case 1:
         this.m10 = var2;
         this.m11 = var3;
         this.m12 = var4;
         this.m13 = var5;
         break;
      case 2:
         this.m20 = var2;
         this.m21 = var3;
         this.m22 = var4;
         this.m23 = var5;
         break;
      case 3:
         this.m30 = var2;
         this.m31 = var3;
         this.m32 = var4;
         this.m33 = var5;
         break;
      default:
         throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f6"));
      }

   }

   public final void setRow(int var1, Vector4f var2) {
      switch(var1) {
      case 0:
         this.m00 = var2.x;
         this.m01 = var2.y;
         this.m02 = var2.z;
         this.m03 = var2.w;
         break;
      case 1:
         this.m10 = var2.x;
         this.m11 = var2.y;
         this.m12 = var2.z;
         this.m13 = var2.w;
         break;
      case 2:
         this.m20 = var2.x;
         this.m21 = var2.y;
         this.m22 = var2.z;
         this.m23 = var2.w;
         break;
      case 3:
         this.m30 = var2.x;
         this.m31 = var2.y;
         this.m32 = var2.z;
         this.m33 = var2.w;
         break;
      default:
         throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f6"));
      }

   }

   public final void setRow(int var1, float[] var2) {
      switch(var1) {
      case 0:
         this.m00 = var2[0];
         this.m01 = var2[1];
         this.m02 = var2[2];
         this.m03 = var2[3];
         break;
      case 1:
         this.m10 = var2[0];
         this.m11 = var2[1];
         this.m12 = var2[2];
         this.m13 = var2[3];
         break;
      case 2:
         this.m20 = var2[0];
         this.m21 = var2[1];
         this.m22 = var2[2];
         this.m23 = var2[3];
         break;
      case 3:
         this.m30 = var2[0];
         this.m31 = var2[1];
         this.m32 = var2[2];
         this.m33 = var2[3];
         break;
      default:
         throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f6"));
      }

   }

   public final void setColumn(int var1, float var2, float var3, float var4, float var5) {
      switch(var1) {
      case 0:
         this.m00 = var2;
         this.m10 = var3;
         this.m20 = var4;
         this.m30 = var5;
         break;
      case 1:
         this.m01 = var2;
         this.m11 = var3;
         this.m21 = var4;
         this.m31 = var5;
         break;
      case 2:
         this.m02 = var2;
         this.m12 = var3;
         this.m22 = var4;
         this.m32 = var5;
         break;
      case 3:
         this.m03 = var2;
         this.m13 = var3;
         this.m23 = var4;
         this.m33 = var5;
         break;
      default:
         throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f9"));
      }

   }

   public final void setColumn(int var1, Vector4f var2) {
      switch(var1) {
      case 0:
         this.m00 = var2.x;
         this.m10 = var2.y;
         this.m20 = var2.z;
         this.m30 = var2.w;
         break;
      case 1:
         this.m01 = var2.x;
         this.m11 = var2.y;
         this.m21 = var2.z;
         this.m31 = var2.w;
         break;
      case 2:
         this.m02 = var2.x;
         this.m12 = var2.y;
         this.m22 = var2.z;
         this.m32 = var2.w;
         break;
      case 3:
         this.m03 = var2.x;
         this.m13 = var2.y;
         this.m23 = var2.z;
         this.m33 = var2.w;
         break;
      default:
         throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f9"));
      }

   }

   public final void setColumn(int var1, float[] var2) {
      switch(var1) {
      case 0:
         this.m00 = var2[0];
         this.m10 = var2[1];
         this.m20 = var2[2];
         this.m30 = var2[3];
         break;
      case 1:
         this.m01 = var2[0];
         this.m11 = var2[1];
         this.m21 = var2[2];
         this.m31 = var2[3];
         break;
      case 2:
         this.m02 = var2[0];
         this.m12 = var2[1];
         this.m22 = var2[2];
         this.m32 = var2[3];
         break;
      case 3:
         this.m03 = var2[0];
         this.m13 = var2[1];
         this.m23 = var2[2];
         this.m33 = var2[3];
         break;
      default:
         throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f9"));
      }

   }

   public final void add(float var1) {
      this.m00 += var1;
      this.m01 += var1;
      this.m02 += var1;
      this.m03 += var1;
      this.m10 += var1;
      this.m11 += var1;
      this.m12 += var1;
      this.m13 += var1;
      this.m20 += var1;
      this.m21 += var1;
      this.m22 += var1;
      this.m23 += var1;
      this.m30 += var1;
      this.m31 += var1;
      this.m32 += var1;
      this.m33 += var1;
   }

   public final void add(float var1, Matrix4f var2) {
      this.m00 = var2.m00 + var1;
      this.m01 = var2.m01 + var1;
      this.m02 = var2.m02 + var1;
      this.m03 = var2.m03 + var1;
      this.m10 = var2.m10 + var1;
      this.m11 = var2.m11 + var1;
      this.m12 = var2.m12 + var1;
      this.m13 = var2.m13 + var1;
      this.m20 = var2.m20 + var1;
      this.m21 = var2.m21 + var1;
      this.m22 = var2.m22 + var1;
      this.m23 = var2.m23 + var1;
      this.m30 = var2.m30 + var1;
      this.m31 = var2.m31 + var1;
      this.m32 = var2.m32 + var1;
      this.m33 = var2.m33 + var1;
   }

   public final void add(Matrix4f var1, Matrix4f var2) {
      this.m00 = var1.m00 + var2.m00;
      this.m01 = var1.m01 + var2.m01;
      this.m02 = var1.m02 + var2.m02;
      this.m03 = var1.m03 + var2.m03;
      this.m10 = var1.m10 + var2.m10;
      this.m11 = var1.m11 + var2.m11;
      this.m12 = var1.m12 + var2.m12;
      this.m13 = var1.m13 + var2.m13;
      this.m20 = var1.m20 + var2.m20;
      this.m21 = var1.m21 + var2.m21;
      this.m22 = var1.m22 + var2.m22;
      this.m23 = var1.m23 + var2.m23;
      this.m30 = var1.m30 + var2.m30;
      this.m31 = var1.m31 + var2.m31;
      this.m32 = var1.m32 + var2.m32;
      this.m33 = var1.m33 + var2.m33;
   }

   public final void add(Matrix4f var1) {
      this.m00 += var1.m00;
      this.m01 += var1.m01;
      this.m02 += var1.m02;
      this.m03 += var1.m03;
      this.m10 += var1.m10;
      this.m11 += var1.m11;
      this.m12 += var1.m12;
      this.m13 += var1.m13;
      this.m20 += var1.m20;
      this.m21 += var1.m21;
      this.m22 += var1.m22;
      this.m23 += var1.m23;
      this.m30 += var1.m30;
      this.m31 += var1.m31;
      this.m32 += var1.m32;
      this.m33 += var1.m33;
   }

   public final void sub(Matrix4f var1, Matrix4f var2) {
      this.m00 = var1.m00 - var2.m00;
      this.m01 = var1.m01 - var2.m01;
      this.m02 = var1.m02 - var2.m02;
      this.m03 = var1.m03 - var2.m03;
      this.m10 = var1.m10 - var2.m10;
      this.m11 = var1.m11 - var2.m11;
      this.m12 = var1.m12 - var2.m12;
      this.m13 = var1.m13 - var2.m13;
      this.m20 = var1.m20 - var2.m20;
      this.m21 = var1.m21 - var2.m21;
      this.m22 = var1.m22 - var2.m22;
      this.m23 = var1.m23 - var2.m23;
      this.m30 = var1.m30 - var2.m30;
      this.m31 = var1.m31 - var2.m31;
      this.m32 = var1.m32 - var2.m32;
      this.m33 = var1.m33 - var2.m33;
   }

   public final void sub(Matrix4f var1) {
      this.m00 -= var1.m00;
      this.m01 -= var1.m01;
      this.m02 -= var1.m02;
      this.m03 -= var1.m03;
      this.m10 -= var1.m10;
      this.m11 -= var1.m11;
      this.m12 -= var1.m12;
      this.m13 -= var1.m13;
      this.m20 -= var1.m20;
      this.m21 -= var1.m21;
      this.m22 -= var1.m22;
      this.m23 -= var1.m23;
      this.m30 -= var1.m30;
      this.m31 -= var1.m31;
      this.m32 -= var1.m32;
      this.m33 -= var1.m33;
   }

   public final void transpose() {
      float var1 = this.m10;
      this.m10 = this.m01;
      this.m01 = var1;
      var1 = this.m20;
      this.m20 = this.m02;
      this.m02 = var1;
      var1 = this.m30;
      this.m30 = this.m03;
      this.m03 = var1;
      var1 = this.m21;
      this.m21 = this.m12;
      this.m12 = var1;
      var1 = this.m31;
      this.m31 = this.m13;
      this.m13 = var1;
      var1 = this.m32;
      this.m32 = this.m23;
      this.m23 = var1;
   }

   public final void transpose(Matrix4f var1) {
      if (this != var1) {
         this.m00 = var1.m00;
         this.m01 = var1.m10;
         this.m02 = var1.m20;
         this.m03 = var1.m30;
         this.m10 = var1.m01;
         this.m11 = var1.m11;
         this.m12 = var1.m21;
         this.m13 = var1.m31;
         this.m20 = var1.m02;
         this.m21 = var1.m12;
         this.m22 = var1.m22;
         this.m23 = var1.m32;
         this.m30 = var1.m03;
         this.m31 = var1.m13;
         this.m32 = var1.m23;
         this.m33 = var1.m33;
      } else {
         this.transpose();
      }

   }

   public final void set(Quat4f var1) {
      this.m00 = 1.0F - 2.0F * var1.y * var1.y - 2.0F * var1.z * var1.z;
      this.m10 = 2.0F * (var1.x * var1.y + var1.w * var1.z);
      this.m20 = 2.0F * (var1.x * var1.z - var1.w * var1.y);
      this.m01 = 2.0F * (var1.x * var1.y - var1.w * var1.z);
      this.m11 = 1.0F - 2.0F * var1.x * var1.x - 2.0F * var1.z * var1.z;
      this.m21 = 2.0F * (var1.y * var1.z + var1.w * var1.x);
      this.m02 = 2.0F * (var1.x * var1.z + var1.w * var1.y);
      this.m12 = 2.0F * (var1.y * var1.z - var1.w * var1.x);
      this.m22 = 1.0F - 2.0F * var1.x * var1.x - 2.0F * var1.y * var1.y;
      this.m03 = 0.0F;
      this.m13 = 0.0F;
      this.m23 = 0.0F;
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.m33 = 1.0F;
   }

   public final void set(AxisAngle4f var1) {
      float var2 = (float)Math.sqrt((double)(var1.x * var1.x + var1.y * var1.y + var1.z * var1.z));
      if ((double)var2 < 1.0E-8D) {
         this.m00 = 1.0F;
         this.m01 = 0.0F;
         this.m02 = 0.0F;
         this.m10 = 0.0F;
         this.m11 = 1.0F;
         this.m12 = 0.0F;
         this.m20 = 0.0F;
         this.m21 = 0.0F;
         this.m22 = 1.0F;
      } else {
         var2 = 1.0F / var2;
         float var3 = var1.x * var2;
         float var4 = var1.y * var2;
         float var5 = var1.z * var2;
         float var6 = (float)Math.sin((double)var1.angle);
         float var7 = (float)Math.cos((double)var1.angle);
         float var8 = 1.0F - var7;
         float var9 = var3 * var5;
         float var10 = var3 * var4;
         float var11 = var4 * var5;
         this.m00 = var8 * var3 * var3 + var7;
         this.m01 = var8 * var10 - var6 * var5;
         this.m02 = var8 * var9 + var6 * var4;
         this.m10 = var8 * var10 + var6 * var5;
         this.m11 = var8 * var4 * var4 + var7;
         this.m12 = var8 * var11 - var6 * var3;
         this.m20 = var8 * var9 - var6 * var4;
         this.m21 = var8 * var11 + var6 * var3;
         this.m22 = var8 * var5 * var5 + var7;
      }

      this.m03 = 0.0F;
      this.m13 = 0.0F;
      this.m23 = 0.0F;
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.m33 = 1.0F;
   }

   public final void set(Quat4d var1) {
      this.m00 = (float)(1.0D - 2.0D * var1.y * var1.y - 2.0D * var1.z * var1.z);
      this.m10 = (float)(2.0D * (var1.x * var1.y + var1.w * var1.z));
      this.m20 = (float)(2.0D * (var1.x * var1.z - var1.w * var1.y));
      this.m01 = (float)(2.0D * (var1.x * var1.y - var1.w * var1.z));
      this.m11 = (float)(1.0D - 2.0D * var1.x * var1.x - 2.0D * var1.z * var1.z);
      this.m21 = (float)(2.0D * (var1.y * var1.z + var1.w * var1.x));
      this.m02 = (float)(2.0D * (var1.x * var1.z + var1.w * var1.y));
      this.m12 = (float)(2.0D * (var1.y * var1.z - var1.w * var1.x));
      this.m22 = (float)(1.0D - 2.0D * var1.x * var1.x - 2.0D * var1.y * var1.y);
      this.m03 = 0.0F;
      this.m13 = 0.0F;
      this.m23 = 0.0F;
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.m33 = 1.0F;
   }

   public final void set(AxisAngle4d var1) {
      double var2 = Math.sqrt(var1.x * var1.x + var1.y * var1.y + var1.z * var1.z);
      if (var2 < 1.0E-8D) {
         this.m00 = 1.0F;
         this.m01 = 0.0F;
         this.m02 = 0.0F;
         this.m10 = 0.0F;
         this.m11 = 1.0F;
         this.m12 = 0.0F;
         this.m20 = 0.0F;
         this.m21 = 0.0F;
         this.m22 = 1.0F;
      } else {
         var2 = 1.0D / var2;
         double var4 = var1.x * var2;
         double var6 = var1.y * var2;
         double var8 = var1.z * var2;
         float var10 = (float)Math.sin(var1.angle);
         float var11 = (float)Math.cos(var1.angle);
         float var12 = 1.0F - var11;
         float var13 = (float)(var4 * var8);
         float var14 = (float)(var4 * var6);
         float var15 = (float)(var6 * var8);
         this.m00 = var12 * (float)(var4 * var4) + var11;
         this.m01 = var12 * var14 - var10 * (float)var8;
         this.m02 = var12 * var13 + var10 * (float)var6;
         this.m10 = var12 * var14 + var10 * (float)var8;
         this.m11 = var12 * (float)(var6 * var6) + var11;
         this.m12 = var12 * var15 - var10 * (float)var4;
         this.m20 = var12 * var13 - var10 * (float)var6;
         this.m21 = var12 * var15 + var10 * (float)var4;
         this.m22 = var12 * (float)(var8 * var8) + var11;
      }

      this.m03 = 0.0F;
      this.m13 = 0.0F;
      this.m23 = 0.0F;
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.m33 = 1.0F;
   }

   public final void set(Quat4d var1, Vector3d var2, double var3) {
      this.m00 = (float)(var3 * (1.0D - 2.0D * var1.y * var1.y - 2.0D * var1.z * var1.z));
      this.m10 = (float)(var3 * 2.0D * (var1.x * var1.y + var1.w * var1.z));
      this.m20 = (float)(var3 * 2.0D * (var1.x * var1.z - var1.w * var1.y));
      this.m01 = (float)(var3 * 2.0D * (var1.x * var1.y - var1.w * var1.z));
      this.m11 = (float)(var3 * (1.0D - 2.0D * var1.x * var1.x - 2.0D * var1.z * var1.z));
      this.m21 = (float)(var3 * 2.0D * (var1.y * var1.z + var1.w * var1.x));
      this.m02 = (float)(var3 * 2.0D * (var1.x * var1.z + var1.w * var1.y));
      this.m12 = (float)(var3 * 2.0D * (var1.y * var1.z - var1.w * var1.x));
      this.m22 = (float)(var3 * (1.0D - 2.0D * var1.x * var1.x - 2.0D * var1.y * var1.y));
      this.m03 = (float)var2.x;
      this.m13 = (float)var2.y;
      this.m23 = (float)var2.z;
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.m33 = 1.0F;
   }

   public final void set(Quat4f var1, Vector3f var2, float var3) {
      this.m00 = var3 * (1.0F - 2.0F * var1.y * var1.y - 2.0F * var1.z * var1.z);
      this.m10 = var3 * 2.0F * (var1.x * var1.y + var1.w * var1.z);
      this.m20 = var3 * 2.0F * (var1.x * var1.z - var1.w * var1.y);
      this.m01 = var3 * 2.0F * (var1.x * var1.y - var1.w * var1.z);
      this.m11 = var3 * (1.0F - 2.0F * var1.x * var1.x - 2.0F * var1.z * var1.z);
      this.m21 = var3 * 2.0F * (var1.y * var1.z + var1.w * var1.x);
      this.m02 = var3 * 2.0F * (var1.x * var1.z + var1.w * var1.y);
      this.m12 = var3 * 2.0F * (var1.y * var1.z - var1.w * var1.x);
      this.m22 = var3 * (1.0F - 2.0F * var1.x * var1.x - 2.0F * var1.y * var1.y);
      this.m03 = var2.x;
      this.m13 = var2.y;
      this.m23 = var2.z;
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.m33 = 1.0F;
   }

   public final void set(Matrix4d var1) {
      this.m00 = (float)var1.m00;
      this.m01 = (float)var1.m01;
      this.m02 = (float)var1.m02;
      this.m03 = (float)var1.m03;
      this.m10 = (float)var1.m10;
      this.m11 = (float)var1.m11;
      this.m12 = (float)var1.m12;
      this.m13 = (float)var1.m13;
      this.m20 = (float)var1.m20;
      this.m21 = (float)var1.m21;
      this.m22 = (float)var1.m22;
      this.m23 = (float)var1.m23;
      this.m30 = (float)var1.m30;
      this.m31 = (float)var1.m31;
      this.m32 = (float)var1.m32;
      this.m33 = (float)var1.m33;
   }

   public final void set(Matrix4f var1) {
      this.m00 = var1.m00;
      this.m01 = var1.m01;
      this.m02 = var1.m02;
      this.m03 = var1.m03;
      this.m10 = var1.m10;
      this.m11 = var1.m11;
      this.m12 = var1.m12;
      this.m13 = var1.m13;
      this.m20 = var1.m20;
      this.m21 = var1.m21;
      this.m22 = var1.m22;
      this.m23 = var1.m23;
      this.m30 = var1.m30;
      this.m31 = var1.m31;
      this.m32 = var1.m32;
      this.m33 = var1.m33;
   }

   public final void invert(Matrix4f var1) {
      this.invertGeneral(var1);
   }

   public final void invert() {
      this.invertGeneral(this);
   }

   final void invertGeneral(Matrix4f var1) {
      double[] var2 = new double[16];
      double[] var3 = new double[16];
      int[] var4 = new int[4];
      var2[0] = (double)var1.m00;
      var2[1] = (double)var1.m01;
      var2[2] = (double)var1.m02;
      var2[3] = (double)var1.m03;
      var2[4] = (double)var1.m10;
      var2[5] = (double)var1.m11;
      var2[6] = (double)var1.m12;
      var2[7] = (double)var1.m13;
      var2[8] = (double)var1.m20;
      var2[9] = (double)var1.m21;
      var2[10] = (double)var1.m22;
      var2[11] = (double)var1.m23;
      var2[12] = (double)var1.m30;
      var2[13] = (double)var1.m31;
      var2[14] = (double)var1.m32;
      var2[15] = (double)var1.m33;
      if (!luDecomposition(var2, var4)) {
         throw new SingularMatrixException(VecMathI18N.getString("Matrix4f12"));
      } else {
         for(int var5 = 0; var5 < 16; ++var5) {
            var3[var5] = 0.0D;
         }

         var3[0] = 1.0D;
         var3[5] = 1.0D;
         var3[10] = 1.0D;
         var3[15] = 1.0D;
         luBacksubstitution(var2, var4, var3);
         this.m00 = (float)var3[0];
         this.m01 = (float)var3[1];
         this.m02 = (float)var3[2];
         this.m03 = (float)var3[3];
         this.m10 = (float)var3[4];
         this.m11 = (float)var3[5];
         this.m12 = (float)var3[6];
         this.m13 = (float)var3[7];
         this.m20 = (float)var3[8];
         this.m21 = (float)var3[9];
         this.m22 = (float)var3[10];
         this.m23 = (float)var3[11];
         this.m30 = (float)var3[12];
         this.m31 = (float)var3[13];
         this.m32 = (float)var3[14];
         this.m33 = (float)var3[15];
      }
   }

   static boolean luDecomposition(double[] var0, int[] var1) {
      double[] var2 = new double[4];
      int var5 = 0;
      int var6 = 0;

      int var3;
      double var7;
      for(var3 = 4; var3-- != 0; var2[var6++] = 1.0D / var7) {
         var7 = 0.0D;
         int var4 = 4;

         while(var4-- != 0) {
            double var9 = var0[var5++];
            var9 = Math.abs(var9);
            if (var9 > var7) {
               var7 = var9;
            }
         }

         if (var7 == 0.0D) {
            return false;
         }
      }

      byte var17 = 0;

      for(var3 = 0; var3 < 4; ++var3) {
         int var8;
         int var10;
         double var11;
         int var18;
         int var19;
         for(var5 = 0; var5 < var3; ++var5) {
            var8 = var17 + 4 * var5 + var3;
            var11 = var0[var8];
            var18 = var5;
            var19 = var17 + 4 * var5;

            for(var10 = var17 + var3; var18-- != 0; var10 += 4) {
               var11 -= var0[var19] * var0[var10];
               ++var19;
            }

            var0[var8] = var11;
         }

         double var13 = 0.0D;
         var6 = -1;

         double var15;
         for(var5 = var3; var5 < 4; ++var5) {
            var8 = var17 + 4 * var5 + var3;
            var11 = var0[var8];
            var18 = var3;
            var19 = var17 + 4 * var5;

            for(var10 = var17 + var3; var18-- != 0; var10 += 4) {
               var11 -= var0[var19] * var0[var10];
               ++var19;
            }

            var0[var8] = var11;
            if ((var15 = var2[var5] * Math.abs(var11)) >= var13) {
               var13 = var15;
               var6 = var5;
            }
         }

         if (var6 < 0) {
            throw new RuntimeException(VecMathI18N.getString("Matrix4f13"));
         }

         if (var3 != var6) {
            var18 = 4;
            var19 = var17 + 4 * var6;

            for(var10 = var17 + 4 * var3; var18-- != 0; var0[var10++] = var15) {
               var15 = var0[var19];
               var0[var19++] = var0[var10];
            }

            var2[var6] = var2[var3];
         }

         var1[var3] = var6;
         if (var0[var17 + 4 * var3 + var3] == 0.0D) {
            return false;
         }

         if (var3 != 3) {
            var15 = 1.0D / var0[var17 + 4 * var3 + var3];
            var8 = var17 + 4 * (var3 + 1) + var3;

            for(var5 = 3 - var3; var5-- != 0; var8 += 4) {
               var0[var8] *= var15;
            }
         }
      }

      return true;
   }

   static void luBacksubstitution(double[] var0, int[] var1, double[] var2) {
      byte var8 = 0;

      for(int var7 = 0; var7 < 4; ++var7) {
         int var9 = var7;
         int var4 = -1;

         int var10;
         for(int var3 = 0; var3 < 4; ++var3) {
            int var5 = var1[var8 + var3];
            double var11 = var2[var9 + 4 * var5];
            var2[var9 + 4 * var5] = var2[var9 + 4 * var3];
            if (var4 >= 0) {
               var10 = var3 * 4;

               for(int var6 = var4; var6 <= var3 - 1; ++var6) {
                  var11 -= var0[var10 + var6] * var2[var9 + 4 * var6];
               }
            } else if (var11 != 0.0D) {
               var4 = var3;
            }

            var2[var9 + 4 * var3] = var11;
         }

         byte var13 = 12;
         var2[var9 + 12] /= var0[var13 + 3];
         var10 = var13 - 4;
         var2[var9 + 8] = (var2[var9 + 8] - var0[var10 + 3] * var2[var9 + 12]) / var0[var10 + 2];
         var10 -= 4;
         var2[var9 + 4] = (var2[var9 + 4] - var0[var10 + 2] * var2[var9 + 8] - var0[var10 + 3] * var2[var9 + 12]) / var0[var10 + 1];
         var10 -= 4;
         var2[var9 + 0] = (var2[var9 + 0] - var0[var10 + 1] * var2[var9 + 4] - var0[var10 + 2] * var2[var9 + 8] - var0[var10 + 3] * var2[var9 + 12]) / var0[var10 + 0];
      }

   }

   public final float determinant() {
      float var1 = this.m00 * (this.m11 * this.m22 * this.m33 + this.m12 * this.m23 * this.m31 + this.m13 * this.m21 * this.m32 - this.m13 * this.m22 * this.m31 - this.m11 * this.m23 * this.m32 - this.m12 * this.m21 * this.m33);
      var1 -= this.m01 * (this.m10 * this.m22 * this.m33 + this.m12 * this.m23 * this.m30 + this.m13 * this.m20 * this.m32 - this.m13 * this.m22 * this.m30 - this.m10 * this.m23 * this.m32 - this.m12 * this.m20 * this.m33);
      var1 += this.m02 * (this.m10 * this.m21 * this.m33 + this.m11 * this.m23 * this.m30 + this.m13 * this.m20 * this.m31 - this.m13 * this.m21 * this.m30 - this.m10 * this.m23 * this.m31 - this.m11 * this.m20 * this.m33);
      var1 -= this.m03 * (this.m10 * this.m21 * this.m32 + this.m11 * this.m22 * this.m30 + this.m12 * this.m20 * this.m31 - this.m12 * this.m21 * this.m30 - this.m10 * this.m22 * this.m31 - this.m11 * this.m20 * this.m32);
      return var1;
   }

   public final void set(Matrix3f var1) {
      this.m00 = var1.m00;
      this.m01 = var1.m01;
      this.m02 = var1.m02;
      this.m03 = 0.0F;
      this.m10 = var1.m10;
      this.m11 = var1.m11;
      this.m12 = var1.m12;
      this.m13 = 0.0F;
      this.m20 = var1.m20;
      this.m21 = var1.m21;
      this.m22 = var1.m22;
      this.m23 = 0.0F;
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.m33 = 1.0F;
   }

   public final void set(Matrix3d var1) {
      this.m00 = (float)var1.m00;
      this.m01 = (float)var1.m01;
      this.m02 = (float)var1.m02;
      this.m03 = 0.0F;
      this.m10 = (float)var1.m10;
      this.m11 = (float)var1.m11;
      this.m12 = (float)var1.m12;
      this.m13 = 0.0F;
      this.m20 = (float)var1.m20;
      this.m21 = (float)var1.m21;
      this.m22 = (float)var1.m22;
      this.m23 = 0.0F;
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.m33 = 1.0F;
   }

   public final void set(float var1) {
      this.m00 = var1;
      this.m01 = 0.0F;
      this.m02 = 0.0F;
      this.m03 = 0.0F;
      this.m10 = 0.0F;
      this.m11 = var1;
      this.m12 = 0.0F;
      this.m13 = 0.0F;
      this.m20 = 0.0F;
      this.m21 = 0.0F;
      this.m22 = var1;
      this.m23 = 0.0F;
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.m33 = 1.0F;
   }

   public final void set(float[] var1) {
      this.m00 = var1[0];
      this.m01 = var1[1];
      this.m02 = var1[2];
      this.m03 = var1[3];
      this.m10 = var1[4];
      this.m11 = var1[5];
      this.m12 = var1[6];
      this.m13 = var1[7];
      this.m20 = var1[8];
      this.m21 = var1[9];
      this.m22 = var1[10];
      this.m23 = var1[11];
      this.m30 = var1[12];
      this.m31 = var1[13];
      this.m32 = var1[14];
      this.m33 = var1[15];
   }

   public final void set(Vector3f var1) {
      this.m00 = 1.0F;
      this.m01 = 0.0F;
      this.m02 = 0.0F;
      this.m03 = var1.x;
      this.m10 = 0.0F;
      this.m11 = 1.0F;
      this.m12 = 0.0F;
      this.m13 = var1.y;
      this.m20 = 0.0F;
      this.m21 = 0.0F;
      this.m22 = 1.0F;
      this.m23 = var1.z;
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.m33 = 1.0F;
   }

   public final void set(float var1, Vector3f var2) {
      this.m00 = var1;
      this.m01 = 0.0F;
      this.m02 = 0.0F;
      this.m03 = var2.x;
      this.m10 = 0.0F;
      this.m11 = var1;
      this.m12 = 0.0F;
      this.m13 = var2.y;
      this.m20 = 0.0F;
      this.m21 = 0.0F;
      this.m22 = var1;
      this.m23 = var2.z;
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.m33 = 1.0F;
   }

   public final void set(Vector3f var1, float var2) {
      this.m00 = var2;
      this.m01 = 0.0F;
      this.m02 = 0.0F;
      this.m03 = var2 * var1.x;
      this.m10 = 0.0F;
      this.m11 = var2;
      this.m12 = 0.0F;
      this.m13 = var2 * var1.y;
      this.m20 = 0.0F;
      this.m21 = 0.0F;
      this.m22 = var2;
      this.m23 = var2 * var1.z;
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.m33 = 1.0F;
   }

   public final void set(Matrix3f var1, Vector3f var2, float var3) {
      this.m00 = var1.m00 * var3;
      this.m01 = var1.m01 * var3;
      this.m02 = var1.m02 * var3;
      this.m03 = var2.x;
      this.m10 = var1.m10 * var3;
      this.m11 = var1.m11 * var3;
      this.m12 = var1.m12 * var3;
      this.m13 = var2.y;
      this.m20 = var1.m20 * var3;
      this.m21 = var1.m21 * var3;
      this.m22 = var1.m22 * var3;
      this.m23 = var2.z;
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.m33 = 1.0F;
   }

   public final void set(Matrix3d var1, Vector3d var2, double var3) {
      this.m00 = (float)(var1.m00 * var3);
      this.m01 = (float)(var1.m01 * var3);
      this.m02 = (float)(var1.m02 * var3);
      this.m03 = (float)var2.x;
      this.m10 = (float)(var1.m10 * var3);
      this.m11 = (float)(var1.m11 * var3);
      this.m12 = (float)(var1.m12 * var3);
      this.m13 = (float)var2.y;
      this.m20 = (float)(var1.m20 * var3);
      this.m21 = (float)(var1.m21 * var3);
      this.m22 = (float)(var1.m22 * var3);
      this.m23 = (float)var2.z;
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.m33 = 1.0F;
   }

   public final void setTranslation(Vector3f var1) {
      this.m03 = var1.x;
      this.m13 = var1.y;
      this.m23 = var1.z;
   }

   public final void rotX(float var1) {
      float var2 = (float)Math.sin((double)var1);
      float var3 = (float)Math.cos((double)var1);
      this.m00 = 1.0F;
      this.m01 = 0.0F;
      this.m02 = 0.0F;
      this.m03 = 0.0F;
      this.m10 = 0.0F;
      this.m11 = var3;
      this.m12 = -var2;
      this.m13 = 0.0F;
      this.m20 = 0.0F;
      this.m21 = var2;
      this.m22 = var3;
      this.m23 = 0.0F;
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.m33 = 1.0F;
   }

   public final void rotY(float var1) {
      float var2 = (float)Math.sin((double)var1);
      float var3 = (float)Math.cos((double)var1);
      this.m00 = var3;
      this.m01 = 0.0F;
      this.m02 = var2;
      this.m03 = 0.0F;
      this.m10 = 0.0F;
      this.m11 = 1.0F;
      this.m12 = 0.0F;
      this.m13 = 0.0F;
      this.m20 = -var2;
      this.m21 = 0.0F;
      this.m22 = var3;
      this.m23 = 0.0F;
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.m33 = 1.0F;
   }

   public final void rotZ(float var1) {
      float var2 = (float)Math.sin((double)var1);
      float var3 = (float)Math.cos((double)var1);
      this.m00 = var3;
      this.m01 = -var2;
      this.m02 = 0.0F;
      this.m03 = 0.0F;
      this.m10 = var2;
      this.m11 = var3;
      this.m12 = 0.0F;
      this.m13 = 0.0F;
      this.m20 = 0.0F;
      this.m21 = 0.0F;
      this.m22 = 1.0F;
      this.m23 = 0.0F;
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.m33 = 1.0F;
   }

   public final void mul(float var1) {
      this.m00 *= var1;
      this.m01 *= var1;
      this.m02 *= var1;
      this.m03 *= var1;
      this.m10 *= var1;
      this.m11 *= var1;
      this.m12 *= var1;
      this.m13 *= var1;
      this.m20 *= var1;
      this.m21 *= var1;
      this.m22 *= var1;
      this.m23 *= var1;
      this.m30 *= var1;
      this.m31 *= var1;
      this.m32 *= var1;
      this.m33 *= var1;
   }

   public final void mul(float var1, Matrix4f var2) {
      this.m00 = var2.m00 * var1;
      this.m01 = var2.m01 * var1;
      this.m02 = var2.m02 * var1;
      this.m03 = var2.m03 * var1;
      this.m10 = var2.m10 * var1;
      this.m11 = var2.m11 * var1;
      this.m12 = var2.m12 * var1;
      this.m13 = var2.m13 * var1;
      this.m20 = var2.m20 * var1;
      this.m21 = var2.m21 * var1;
      this.m22 = var2.m22 * var1;
      this.m23 = var2.m23 * var1;
      this.m30 = var2.m30 * var1;
      this.m31 = var2.m31 * var1;
      this.m32 = var2.m32 * var1;
      this.m33 = var2.m33 * var1;
   }

   public final void mul(Matrix4f var1) {
      float var2 = this.m00 * var1.m00 + this.m01 * var1.m10 + this.m02 * var1.m20 + this.m03 * var1.m30;
      float var3 = this.m00 * var1.m01 + this.m01 * var1.m11 + this.m02 * var1.m21 + this.m03 * var1.m31;
      float var4 = this.m00 * var1.m02 + this.m01 * var1.m12 + this.m02 * var1.m22 + this.m03 * var1.m32;
      float var5 = this.m00 * var1.m03 + this.m01 * var1.m13 + this.m02 * var1.m23 + this.m03 * var1.m33;
      float var6 = this.m10 * var1.m00 + this.m11 * var1.m10 + this.m12 * var1.m20 + this.m13 * var1.m30;
      float var7 = this.m10 * var1.m01 + this.m11 * var1.m11 + this.m12 * var1.m21 + this.m13 * var1.m31;
      float var8 = this.m10 * var1.m02 + this.m11 * var1.m12 + this.m12 * var1.m22 + this.m13 * var1.m32;
      float var9 = this.m10 * var1.m03 + this.m11 * var1.m13 + this.m12 * var1.m23 + this.m13 * var1.m33;
      float var10 = this.m20 * var1.m00 + this.m21 * var1.m10 + this.m22 * var1.m20 + this.m23 * var1.m30;
      float var11 = this.m20 * var1.m01 + this.m21 * var1.m11 + this.m22 * var1.m21 + this.m23 * var1.m31;
      float var12 = this.m20 * var1.m02 + this.m21 * var1.m12 + this.m22 * var1.m22 + this.m23 * var1.m32;
      float var13 = this.m20 * var1.m03 + this.m21 * var1.m13 + this.m22 * var1.m23 + this.m23 * var1.m33;
      float var14 = this.m30 * var1.m00 + this.m31 * var1.m10 + this.m32 * var1.m20 + this.m33 * var1.m30;
      float var15 = this.m30 * var1.m01 + this.m31 * var1.m11 + this.m32 * var1.m21 + this.m33 * var1.m31;
      float var16 = this.m30 * var1.m02 + this.m31 * var1.m12 + this.m32 * var1.m22 + this.m33 * var1.m32;
      float var17 = this.m30 * var1.m03 + this.m31 * var1.m13 + this.m32 * var1.m23 + this.m33 * var1.m33;
      this.m00 = var2;
      this.m01 = var3;
      this.m02 = var4;
      this.m03 = var5;
      this.m10 = var6;
      this.m11 = var7;
      this.m12 = var8;
      this.m13 = var9;
      this.m20 = var10;
      this.m21 = var11;
      this.m22 = var12;
      this.m23 = var13;
      this.m30 = var14;
      this.m31 = var15;
      this.m32 = var16;
      this.m33 = var17;
   }

   public final void mul(Matrix4f var1, Matrix4f var2) {
      if (this != var1 && this != var2) {
         this.m00 = var1.m00 * var2.m00 + var1.m01 * var2.m10 + var1.m02 * var2.m20 + var1.m03 * var2.m30;
         this.m01 = var1.m00 * var2.m01 + var1.m01 * var2.m11 + var1.m02 * var2.m21 + var1.m03 * var2.m31;
         this.m02 = var1.m00 * var2.m02 + var1.m01 * var2.m12 + var1.m02 * var2.m22 + var1.m03 * var2.m32;
         this.m03 = var1.m00 * var2.m03 + var1.m01 * var2.m13 + var1.m02 * var2.m23 + var1.m03 * var2.m33;
         this.m10 = var1.m10 * var2.m00 + var1.m11 * var2.m10 + var1.m12 * var2.m20 + var1.m13 * var2.m30;
         this.m11 = var1.m10 * var2.m01 + var1.m11 * var2.m11 + var1.m12 * var2.m21 + var1.m13 * var2.m31;
         this.m12 = var1.m10 * var2.m02 + var1.m11 * var2.m12 + var1.m12 * var2.m22 + var1.m13 * var2.m32;
         this.m13 = var1.m10 * var2.m03 + var1.m11 * var2.m13 + var1.m12 * var2.m23 + var1.m13 * var2.m33;
         this.m20 = var1.m20 * var2.m00 + var1.m21 * var2.m10 + var1.m22 * var2.m20 + var1.m23 * var2.m30;
         this.m21 = var1.m20 * var2.m01 + var1.m21 * var2.m11 + var1.m22 * var2.m21 + var1.m23 * var2.m31;
         this.m22 = var1.m20 * var2.m02 + var1.m21 * var2.m12 + var1.m22 * var2.m22 + var1.m23 * var2.m32;
         this.m23 = var1.m20 * var2.m03 + var1.m21 * var2.m13 + var1.m22 * var2.m23 + var1.m23 * var2.m33;
         this.m30 = var1.m30 * var2.m00 + var1.m31 * var2.m10 + var1.m32 * var2.m20 + var1.m33 * var2.m30;
         this.m31 = var1.m30 * var2.m01 + var1.m31 * var2.m11 + var1.m32 * var2.m21 + var1.m33 * var2.m31;
         this.m32 = var1.m30 * var2.m02 + var1.m31 * var2.m12 + var1.m32 * var2.m22 + var1.m33 * var2.m32;
         this.m33 = var1.m30 * var2.m03 + var1.m31 * var2.m13 + var1.m32 * var2.m23 + var1.m33 * var2.m33;
      } else {
         float var3 = var1.m00 * var2.m00 + var1.m01 * var2.m10 + var1.m02 * var2.m20 + var1.m03 * var2.m30;
         float var4 = var1.m00 * var2.m01 + var1.m01 * var2.m11 + var1.m02 * var2.m21 + var1.m03 * var2.m31;
         float var5 = var1.m00 * var2.m02 + var1.m01 * var2.m12 + var1.m02 * var2.m22 + var1.m03 * var2.m32;
         float var6 = var1.m00 * var2.m03 + var1.m01 * var2.m13 + var1.m02 * var2.m23 + var1.m03 * var2.m33;
         float var7 = var1.m10 * var2.m00 + var1.m11 * var2.m10 + var1.m12 * var2.m20 + var1.m13 * var2.m30;
         float var8 = var1.m10 * var2.m01 + var1.m11 * var2.m11 + var1.m12 * var2.m21 + var1.m13 * var2.m31;
         float var9 = var1.m10 * var2.m02 + var1.m11 * var2.m12 + var1.m12 * var2.m22 + var1.m13 * var2.m32;
         float var10 = var1.m10 * var2.m03 + var1.m11 * var2.m13 + var1.m12 * var2.m23 + var1.m13 * var2.m33;
         float var11 = var1.m20 * var2.m00 + var1.m21 * var2.m10 + var1.m22 * var2.m20 + var1.m23 * var2.m30;
         float var12 = var1.m20 * var2.m01 + var1.m21 * var2.m11 + var1.m22 * var2.m21 + var1.m23 * var2.m31;
         float var13 = var1.m20 * var2.m02 + var1.m21 * var2.m12 + var1.m22 * var2.m22 + var1.m23 * var2.m32;
         float var14 = var1.m20 * var2.m03 + var1.m21 * var2.m13 + var1.m22 * var2.m23 + var1.m23 * var2.m33;
         float var15 = var1.m30 * var2.m00 + var1.m31 * var2.m10 + var1.m32 * var2.m20 + var1.m33 * var2.m30;
         float var16 = var1.m30 * var2.m01 + var1.m31 * var2.m11 + var1.m32 * var2.m21 + var1.m33 * var2.m31;
         float var17 = var1.m30 * var2.m02 + var1.m31 * var2.m12 + var1.m32 * var2.m22 + var1.m33 * var2.m32;
         float var18 = var1.m30 * var2.m03 + var1.m31 * var2.m13 + var1.m32 * var2.m23 + var1.m33 * var2.m33;
         this.m00 = var3;
         this.m01 = var4;
         this.m02 = var5;
         this.m03 = var6;
         this.m10 = var7;
         this.m11 = var8;
         this.m12 = var9;
         this.m13 = var10;
         this.m20 = var11;
         this.m21 = var12;
         this.m22 = var13;
         this.m23 = var14;
         this.m30 = var15;
         this.m31 = var16;
         this.m32 = var17;
         this.m33 = var18;
      }

   }

   public final void mulTransposeBoth(Matrix4f var1, Matrix4f var2) {
      if (this != var1 && this != var2) {
         this.m00 = var1.m00 * var2.m00 + var1.m10 * var2.m01 + var1.m20 * var2.m02 + var1.m30 * var2.m03;
         this.m01 = var1.m00 * var2.m10 + var1.m10 * var2.m11 + var1.m20 * var2.m12 + var1.m30 * var2.m13;
         this.m02 = var1.m00 * var2.m20 + var1.m10 * var2.m21 + var1.m20 * var2.m22 + var1.m30 * var2.m23;
         this.m03 = var1.m00 * var2.m30 + var1.m10 * var2.m31 + var1.m20 * var2.m32 + var1.m30 * var2.m33;
         this.m10 = var1.m01 * var2.m00 + var1.m11 * var2.m01 + var1.m21 * var2.m02 + var1.m31 * var2.m03;
         this.m11 = var1.m01 * var2.m10 + var1.m11 * var2.m11 + var1.m21 * var2.m12 + var1.m31 * var2.m13;
         this.m12 = var1.m01 * var2.m20 + var1.m11 * var2.m21 + var1.m21 * var2.m22 + var1.m31 * var2.m23;
         this.m13 = var1.m01 * var2.m30 + var1.m11 * var2.m31 + var1.m21 * var2.m32 + var1.m31 * var2.m33;
         this.m20 = var1.m02 * var2.m00 + var1.m12 * var2.m01 + var1.m22 * var2.m02 + var1.m32 * var2.m03;
         this.m21 = var1.m02 * var2.m10 + var1.m12 * var2.m11 + var1.m22 * var2.m12 + var1.m32 * var2.m13;
         this.m22 = var1.m02 * var2.m20 + var1.m12 * var2.m21 + var1.m22 * var2.m22 + var1.m32 * var2.m23;
         this.m23 = var1.m02 * var2.m30 + var1.m12 * var2.m31 + var1.m22 * var2.m32 + var1.m32 * var2.m33;
         this.m30 = var1.m03 * var2.m00 + var1.m13 * var2.m01 + var1.m23 * var2.m02 + var1.m33 * var2.m03;
         this.m31 = var1.m03 * var2.m10 + var1.m13 * var2.m11 + var1.m23 * var2.m12 + var1.m33 * var2.m13;
         this.m32 = var1.m03 * var2.m20 + var1.m13 * var2.m21 + var1.m23 * var2.m22 + var1.m33 * var2.m23;
         this.m33 = var1.m03 * var2.m30 + var1.m13 * var2.m31 + var1.m23 * var2.m32 + var1.m33 * var2.m33;
      } else {
         float var3 = var1.m00 * var2.m00 + var1.m10 * var2.m01 + var1.m20 * var2.m02 + var1.m30 * var2.m03;
         float var4 = var1.m00 * var2.m10 + var1.m10 * var2.m11 + var1.m20 * var2.m12 + var1.m30 * var2.m13;
         float var5 = var1.m00 * var2.m20 + var1.m10 * var2.m21 + var1.m20 * var2.m22 + var1.m30 * var2.m23;
         float var6 = var1.m00 * var2.m30 + var1.m10 * var2.m31 + var1.m20 * var2.m32 + var1.m30 * var2.m33;
         float var7 = var1.m01 * var2.m00 + var1.m11 * var2.m01 + var1.m21 * var2.m02 + var1.m31 * var2.m03;
         float var8 = var1.m01 * var2.m10 + var1.m11 * var2.m11 + var1.m21 * var2.m12 + var1.m31 * var2.m13;
         float var9 = var1.m01 * var2.m20 + var1.m11 * var2.m21 + var1.m21 * var2.m22 + var1.m31 * var2.m23;
         float var10 = var1.m01 * var2.m30 + var1.m11 * var2.m31 + var1.m21 * var2.m32 + var1.m31 * var2.m33;
         float var11 = var1.m02 * var2.m00 + var1.m12 * var2.m01 + var1.m22 * var2.m02 + var1.m32 * var2.m03;
         float var12 = var1.m02 * var2.m10 + var1.m12 * var2.m11 + var1.m22 * var2.m12 + var1.m32 * var2.m13;
         float var13 = var1.m02 * var2.m20 + var1.m12 * var2.m21 + var1.m22 * var2.m22 + var1.m32 * var2.m23;
         float var14 = var1.m02 * var2.m30 + var1.m12 * var2.m31 + var1.m22 * var2.m32 + var1.m32 * var2.m33;
         float var15 = var1.m03 * var2.m00 + var1.m13 * var2.m01 + var1.m23 * var2.m02 + var1.m33 * var2.m03;
         float var16 = var1.m03 * var2.m10 + var1.m13 * var2.m11 + var1.m23 * var2.m12 + var1.m33 * var2.m13;
         float var17 = var1.m03 * var2.m20 + var1.m13 * var2.m21 + var1.m23 * var2.m22 + var1.m33 * var2.m23;
         float var18 = var1.m03 * var2.m30 + var1.m13 * var2.m31 + var1.m23 * var2.m32 + var1.m33 * var2.m33;
         this.m00 = var3;
         this.m01 = var4;
         this.m02 = var5;
         this.m03 = var6;
         this.m10 = var7;
         this.m11 = var8;
         this.m12 = var9;
         this.m13 = var10;
         this.m20 = var11;
         this.m21 = var12;
         this.m22 = var13;
         this.m23 = var14;
         this.m30 = var15;
         this.m31 = var16;
         this.m32 = var17;
         this.m33 = var18;
      }

   }

   public final void mulTransposeRight(Matrix4f var1, Matrix4f var2) {
      if (this != var1 && this != var2) {
         this.m00 = var1.m00 * var2.m00 + var1.m01 * var2.m01 + var1.m02 * var2.m02 + var1.m03 * var2.m03;
         this.m01 = var1.m00 * var2.m10 + var1.m01 * var2.m11 + var1.m02 * var2.m12 + var1.m03 * var2.m13;
         this.m02 = var1.m00 * var2.m20 + var1.m01 * var2.m21 + var1.m02 * var2.m22 + var1.m03 * var2.m23;
         this.m03 = var1.m00 * var2.m30 + var1.m01 * var2.m31 + var1.m02 * var2.m32 + var1.m03 * var2.m33;
         this.m10 = var1.m10 * var2.m00 + var1.m11 * var2.m01 + var1.m12 * var2.m02 + var1.m13 * var2.m03;
         this.m11 = var1.m10 * var2.m10 + var1.m11 * var2.m11 + var1.m12 * var2.m12 + var1.m13 * var2.m13;
         this.m12 = var1.m10 * var2.m20 + var1.m11 * var2.m21 + var1.m12 * var2.m22 + var1.m13 * var2.m23;
         this.m13 = var1.m10 * var2.m30 + var1.m11 * var2.m31 + var1.m12 * var2.m32 + var1.m13 * var2.m33;
         this.m20 = var1.m20 * var2.m00 + var1.m21 * var2.m01 + var1.m22 * var2.m02 + var1.m23 * var2.m03;
         this.m21 = var1.m20 * var2.m10 + var1.m21 * var2.m11 + var1.m22 * var2.m12 + var1.m23 * var2.m13;
         this.m22 = var1.m20 * var2.m20 + var1.m21 * var2.m21 + var1.m22 * var2.m22 + var1.m23 * var2.m23;
         this.m23 = var1.m20 * var2.m30 + var1.m21 * var2.m31 + var1.m22 * var2.m32 + var1.m23 * var2.m33;
         this.m30 = var1.m30 * var2.m00 + var1.m31 * var2.m01 + var1.m32 * var2.m02 + var1.m33 * var2.m03;
         this.m31 = var1.m30 * var2.m10 + var1.m31 * var2.m11 + var1.m32 * var2.m12 + var1.m33 * var2.m13;
         this.m32 = var1.m30 * var2.m20 + var1.m31 * var2.m21 + var1.m32 * var2.m22 + var1.m33 * var2.m23;
         this.m33 = var1.m30 * var2.m30 + var1.m31 * var2.m31 + var1.m32 * var2.m32 + var1.m33 * var2.m33;
      } else {
         float var3 = var1.m00 * var2.m00 + var1.m01 * var2.m01 + var1.m02 * var2.m02 + var1.m03 * var2.m03;
         float var4 = var1.m00 * var2.m10 + var1.m01 * var2.m11 + var1.m02 * var2.m12 + var1.m03 * var2.m13;
         float var5 = var1.m00 * var2.m20 + var1.m01 * var2.m21 + var1.m02 * var2.m22 + var1.m03 * var2.m23;
         float var6 = var1.m00 * var2.m30 + var1.m01 * var2.m31 + var1.m02 * var2.m32 + var1.m03 * var2.m33;
         float var7 = var1.m10 * var2.m00 + var1.m11 * var2.m01 + var1.m12 * var2.m02 + var1.m13 * var2.m03;
         float var8 = var1.m10 * var2.m10 + var1.m11 * var2.m11 + var1.m12 * var2.m12 + var1.m13 * var2.m13;
         float var9 = var1.m10 * var2.m20 + var1.m11 * var2.m21 + var1.m12 * var2.m22 + var1.m13 * var2.m23;
         float var10 = var1.m10 * var2.m30 + var1.m11 * var2.m31 + var1.m12 * var2.m32 + var1.m13 * var2.m33;
         float var11 = var1.m20 * var2.m00 + var1.m21 * var2.m01 + var1.m22 * var2.m02 + var1.m23 * var2.m03;
         float var12 = var1.m20 * var2.m10 + var1.m21 * var2.m11 + var1.m22 * var2.m12 + var1.m23 * var2.m13;
         float var13 = var1.m20 * var2.m20 + var1.m21 * var2.m21 + var1.m22 * var2.m22 + var1.m23 * var2.m23;
         float var14 = var1.m20 * var2.m30 + var1.m21 * var2.m31 + var1.m22 * var2.m32 + var1.m23 * var2.m33;
         float var15 = var1.m30 * var2.m00 + var1.m31 * var2.m01 + var1.m32 * var2.m02 + var1.m33 * var2.m03;
         float var16 = var1.m30 * var2.m10 + var1.m31 * var2.m11 + var1.m32 * var2.m12 + var1.m33 * var2.m13;
         float var17 = var1.m30 * var2.m20 + var1.m31 * var2.m21 + var1.m32 * var2.m22 + var1.m33 * var2.m23;
         float var18 = var1.m30 * var2.m30 + var1.m31 * var2.m31 + var1.m32 * var2.m32 + var1.m33 * var2.m33;
         this.m00 = var3;
         this.m01 = var4;
         this.m02 = var5;
         this.m03 = var6;
         this.m10 = var7;
         this.m11 = var8;
         this.m12 = var9;
         this.m13 = var10;
         this.m20 = var11;
         this.m21 = var12;
         this.m22 = var13;
         this.m23 = var14;
         this.m30 = var15;
         this.m31 = var16;
         this.m32 = var17;
         this.m33 = var18;
      }

   }

   public final void mulTransposeLeft(Matrix4f var1, Matrix4f var2) {
      if (this != var1 && this != var2) {
         this.m00 = var1.m00 * var2.m00 + var1.m10 * var2.m10 + var1.m20 * var2.m20 + var1.m30 * var2.m30;
         this.m01 = var1.m00 * var2.m01 + var1.m10 * var2.m11 + var1.m20 * var2.m21 + var1.m30 * var2.m31;
         this.m02 = var1.m00 * var2.m02 + var1.m10 * var2.m12 + var1.m20 * var2.m22 + var1.m30 * var2.m32;
         this.m03 = var1.m00 * var2.m03 + var1.m10 * var2.m13 + var1.m20 * var2.m23 + var1.m30 * var2.m33;
         this.m10 = var1.m01 * var2.m00 + var1.m11 * var2.m10 + var1.m21 * var2.m20 + var1.m31 * var2.m30;
         this.m11 = var1.m01 * var2.m01 + var1.m11 * var2.m11 + var1.m21 * var2.m21 + var1.m31 * var2.m31;
         this.m12 = var1.m01 * var2.m02 + var1.m11 * var2.m12 + var1.m21 * var2.m22 + var1.m31 * var2.m32;
         this.m13 = var1.m01 * var2.m03 + var1.m11 * var2.m13 + var1.m21 * var2.m23 + var1.m31 * var2.m33;
         this.m20 = var1.m02 * var2.m00 + var1.m12 * var2.m10 + var1.m22 * var2.m20 + var1.m32 * var2.m30;
         this.m21 = var1.m02 * var2.m01 + var1.m12 * var2.m11 + var1.m22 * var2.m21 + var1.m32 * var2.m31;
         this.m22 = var1.m02 * var2.m02 + var1.m12 * var2.m12 + var1.m22 * var2.m22 + var1.m32 * var2.m32;
         this.m23 = var1.m02 * var2.m03 + var1.m12 * var2.m13 + var1.m22 * var2.m23 + var1.m32 * var2.m33;
         this.m30 = var1.m03 * var2.m00 + var1.m13 * var2.m10 + var1.m23 * var2.m20 + var1.m33 * var2.m30;
         this.m31 = var1.m03 * var2.m01 + var1.m13 * var2.m11 + var1.m23 * var2.m21 + var1.m33 * var2.m31;
         this.m32 = var1.m03 * var2.m02 + var1.m13 * var2.m12 + var1.m23 * var2.m22 + var1.m33 * var2.m32;
         this.m33 = var1.m03 * var2.m03 + var1.m13 * var2.m13 + var1.m23 * var2.m23 + var1.m33 * var2.m33;
      } else {
         float var3 = var1.m00 * var2.m00 + var1.m10 * var2.m10 + var1.m20 * var2.m20 + var1.m30 * var2.m30;
         float var4 = var1.m00 * var2.m01 + var1.m10 * var2.m11 + var1.m20 * var2.m21 + var1.m30 * var2.m31;
         float var5 = var1.m00 * var2.m02 + var1.m10 * var2.m12 + var1.m20 * var2.m22 + var1.m30 * var2.m32;
         float var6 = var1.m00 * var2.m03 + var1.m10 * var2.m13 + var1.m20 * var2.m23 + var1.m30 * var2.m33;
         float var7 = var1.m01 * var2.m00 + var1.m11 * var2.m10 + var1.m21 * var2.m20 + var1.m31 * var2.m30;
         float var8 = var1.m01 * var2.m01 + var1.m11 * var2.m11 + var1.m21 * var2.m21 + var1.m31 * var2.m31;
         float var9 = var1.m01 * var2.m02 + var1.m11 * var2.m12 + var1.m21 * var2.m22 + var1.m31 * var2.m32;
         float var10 = var1.m01 * var2.m03 + var1.m11 * var2.m13 + var1.m21 * var2.m23 + var1.m31 * var2.m33;
         float var11 = var1.m02 * var2.m00 + var1.m12 * var2.m10 + var1.m22 * var2.m20 + var1.m32 * var2.m30;
         float var12 = var1.m02 * var2.m01 + var1.m12 * var2.m11 + var1.m22 * var2.m21 + var1.m32 * var2.m31;
         float var13 = var1.m02 * var2.m02 + var1.m12 * var2.m12 + var1.m22 * var2.m22 + var1.m32 * var2.m32;
         float var14 = var1.m02 * var2.m03 + var1.m12 * var2.m13 + var1.m22 * var2.m23 + var1.m32 * var2.m33;
         float var15 = var1.m03 * var2.m00 + var1.m13 * var2.m10 + var1.m23 * var2.m20 + var1.m33 * var2.m30;
         float var16 = var1.m03 * var2.m01 + var1.m13 * var2.m11 + var1.m23 * var2.m21 + var1.m33 * var2.m31;
         float var17 = var1.m03 * var2.m02 + var1.m13 * var2.m12 + var1.m23 * var2.m22 + var1.m33 * var2.m32;
         float var18 = var1.m03 * var2.m03 + var1.m13 * var2.m13 + var1.m23 * var2.m23 + var1.m33 * var2.m33;
         this.m00 = var3;
         this.m01 = var4;
         this.m02 = var5;
         this.m03 = var6;
         this.m10 = var7;
         this.m11 = var8;
         this.m12 = var9;
         this.m13 = var10;
         this.m20 = var11;
         this.m21 = var12;
         this.m22 = var13;
         this.m23 = var14;
         this.m30 = var15;
         this.m31 = var16;
         this.m32 = var17;
         this.m33 = var18;
      }

   }

   public boolean equals(Matrix4f var1) {
      try {
         return this.m00 == var1.m00 && this.m01 == var1.m01 && this.m02 == var1.m02 && this.m03 == var1.m03 && this.m10 == var1.m10 && this.m11 == var1.m11 && this.m12 == var1.m12 && this.m13 == var1.m13 && this.m20 == var1.m20 && this.m21 == var1.m21 && this.m22 == var1.m22 && this.m23 == var1.m23 && this.m30 == var1.m30 && this.m31 == var1.m31 && this.m32 == var1.m32 && this.m33 == var1.m33;
      } catch (NullPointerException var3) {
         return false;
      }
   }

   public boolean equals(Object var1) {
      try {
         Matrix4f var2 = (Matrix4f)var1;
         return this.m00 == var2.m00 && this.m01 == var2.m01 && this.m02 == var2.m02 && this.m03 == var2.m03 && this.m10 == var2.m10 && this.m11 == var2.m11 && this.m12 == var2.m12 && this.m13 == var2.m13 && this.m20 == var2.m20 && this.m21 == var2.m21 && this.m22 == var2.m22 && this.m23 == var2.m23 && this.m30 == var2.m30 && this.m31 == var2.m31 && this.m32 == var2.m32 && this.m33 == var2.m33;
      } catch (ClassCastException var3) {
         return false;
      } catch (NullPointerException var4) {
         return false;
      }
   }

   public boolean epsilonEquals(Matrix4f var1, float var2) {
      boolean var3 = true;
      if (Math.abs(this.m00 - var1.m00) > var2) {
         var3 = false;
      }

      if (Math.abs(this.m01 - var1.m01) > var2) {
         var3 = false;
      }

      if (Math.abs(this.m02 - var1.m02) > var2) {
         var3 = false;
      }

      if (Math.abs(this.m03 - var1.m03) > var2) {
         var3 = false;
      }

      if (Math.abs(this.m10 - var1.m10) > var2) {
         var3 = false;
      }

      if (Math.abs(this.m11 - var1.m11) > var2) {
         var3 = false;
      }

      if (Math.abs(this.m12 - var1.m12) > var2) {
         var3 = false;
      }

      if (Math.abs(this.m13 - var1.m13) > var2) {
         var3 = false;
      }

      if (Math.abs(this.m20 - var1.m20) > var2) {
         var3 = false;
      }

      if (Math.abs(this.m21 - var1.m21) > var2) {
         var3 = false;
      }

      if (Math.abs(this.m22 - var1.m22) > var2) {
         var3 = false;
      }

      if (Math.abs(this.m23 - var1.m23) > var2) {
         var3 = false;
      }

      if (Math.abs(this.m30 - var1.m30) > var2) {
         var3 = false;
      }

      if (Math.abs(this.m31 - var1.m31) > var2) {
         var3 = false;
      }

      if (Math.abs(this.m32 - var1.m32) > var2) {
         var3 = false;
      }

      if (Math.abs(this.m33 - var1.m33) > var2) {
         var3 = false;
      }

      return var3;
   }

   public int hashCode() {
      long var1 = 1L;
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.m00);
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.m01);
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.m02);
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.m03);
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.m10);
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.m11);
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.m12);
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.m13);
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.m20);
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.m21);
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.m22);
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.m23);
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.m30);
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.m31);
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.m32);
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.m33);
      return (int)(var1 ^ var1 >> 32);
   }

   public final void transform(Tuple4f var1, Tuple4f var2) {
      float var3 = this.m00 * var1.x + this.m01 * var1.y + this.m02 * var1.z + this.m03 * var1.w;
      float var4 = this.m10 * var1.x + this.m11 * var1.y + this.m12 * var1.z + this.m13 * var1.w;
      float var5 = this.m20 * var1.x + this.m21 * var1.y + this.m22 * var1.z + this.m23 * var1.w;
      var2.w = this.m30 * var1.x + this.m31 * var1.y + this.m32 * var1.z + this.m33 * var1.w;
      var2.x = var3;
      var2.y = var4;
      var2.z = var5;
   }

   public final void transform(Tuple4f var1) {
      float var2 = this.m00 * var1.x + this.m01 * var1.y + this.m02 * var1.z + this.m03 * var1.w;
      float var3 = this.m10 * var1.x + this.m11 * var1.y + this.m12 * var1.z + this.m13 * var1.w;
      float var4 = this.m20 * var1.x + this.m21 * var1.y + this.m22 * var1.z + this.m23 * var1.w;
      var1.w = this.m30 * var1.x + this.m31 * var1.y + this.m32 * var1.z + this.m33 * var1.w;
      var1.x = var2;
      var1.y = var3;
      var1.z = var4;
   }

   public final void transform(Point3f var1, Point3f var2) {
      float var3 = this.m00 * var1.x + this.m01 * var1.y + this.m02 * var1.z + this.m03;
      float var4 = this.m10 * var1.x + this.m11 * var1.y + this.m12 * var1.z + this.m13;
      var2.z = this.m20 * var1.x + this.m21 * var1.y + this.m22 * var1.z + this.m23;
      var2.x = var3;
      var2.y = var4;
   }

   public final void transform(Point3f var1) {
      float var2 = this.m00 * var1.x + this.m01 * var1.y + this.m02 * var1.z + this.m03;
      float var3 = this.m10 * var1.x + this.m11 * var1.y + this.m12 * var1.z + this.m13;
      var1.z = this.m20 * var1.x + this.m21 * var1.y + this.m22 * var1.z + this.m23;
      var1.x = var2;
      var1.y = var3;
   }

   public final void transform(Vector3f var1, Vector3f var2) {
      float var3 = this.m00 * var1.x + this.m01 * var1.y + this.m02 * var1.z;
      float var4 = this.m10 * var1.x + this.m11 * var1.y + this.m12 * var1.z;
      var2.z = this.m20 * var1.x + this.m21 * var1.y + this.m22 * var1.z;
      var2.x = var3;
      var2.y = var4;
   }

   public final void transform(Vector3f var1) {
      float var2 = this.m00 * var1.x + this.m01 * var1.y + this.m02 * var1.z;
      float var3 = this.m10 * var1.x + this.m11 * var1.y + this.m12 * var1.z;
      var1.z = this.m20 * var1.x + this.m21 * var1.y + this.m22 * var1.z;
      var1.x = var2;
      var1.y = var3;
   }

   public final void setRotation(Matrix3d var1) {
      double[] var2 = new double[9];
      double[] var3 = new double[3];
      this.getScaleRotate(var3, var2);
      this.m00 = (float)(var1.m00 * var3[0]);
      this.m01 = (float)(var1.m01 * var3[1]);
      this.m02 = (float)(var1.m02 * var3[2]);
      this.m10 = (float)(var1.m10 * var3[0]);
      this.m11 = (float)(var1.m11 * var3[1]);
      this.m12 = (float)(var1.m12 * var3[2]);
      this.m20 = (float)(var1.m20 * var3[0]);
      this.m21 = (float)(var1.m21 * var3[1]);
      this.m22 = (float)(var1.m22 * var3[2]);
   }

   public final void setRotation(Matrix3f var1) {
      double[] var2 = new double[9];
      double[] var3 = new double[3];
      this.getScaleRotate(var3, var2);
      this.m00 = (float)((double)var1.m00 * var3[0]);
      this.m01 = (float)((double)var1.m01 * var3[1]);
      this.m02 = (float)((double)var1.m02 * var3[2]);
      this.m10 = (float)((double)var1.m10 * var3[0]);
      this.m11 = (float)((double)var1.m11 * var3[1]);
      this.m12 = (float)((double)var1.m12 * var3[2]);
      this.m20 = (float)((double)var1.m20 * var3[0]);
      this.m21 = (float)((double)var1.m21 * var3[1]);
      this.m22 = (float)((double)var1.m22 * var3[2]);
   }

   public final void setRotation(Quat4f var1) {
      double[] var2 = new double[9];
      double[] var3 = new double[3];
      this.getScaleRotate(var3, var2);
      this.m00 = (float)((double)(1.0F - 2.0F * var1.y * var1.y - 2.0F * var1.z * var1.z) * var3[0]);
      this.m10 = (float)((double)(2.0F * (var1.x * var1.y + var1.w * var1.z)) * var3[0]);
      this.m20 = (float)((double)(2.0F * (var1.x * var1.z - var1.w * var1.y)) * var3[0]);
      this.m01 = (float)((double)(2.0F * (var1.x * var1.y - var1.w * var1.z)) * var3[1]);
      this.m11 = (float)((double)(1.0F - 2.0F * var1.x * var1.x - 2.0F * var1.z * var1.z) * var3[1]);
      this.m21 = (float)((double)(2.0F * (var1.y * var1.z + var1.w * var1.x)) * var3[1]);
      this.m02 = (float)((double)(2.0F * (var1.x * var1.z + var1.w * var1.y)) * var3[2]);
      this.m12 = (float)((double)(2.0F * (var1.y * var1.z - var1.w * var1.x)) * var3[2]);
      this.m22 = (float)((double)(1.0F - 2.0F * var1.x * var1.x - 2.0F * var1.y * var1.y) * var3[2]);
   }

   public final void setRotation(Quat4d var1) {
      double[] var2 = new double[9];
      double[] var3 = new double[3];
      this.getScaleRotate(var3, var2);
      this.m00 = (float)((1.0D - 2.0D * var1.y * var1.y - 2.0D * var1.z * var1.z) * var3[0]);
      this.m10 = (float)(2.0D * (var1.x * var1.y + var1.w * var1.z) * var3[0]);
      this.m20 = (float)(2.0D * (var1.x * var1.z - var1.w * var1.y) * var3[0]);
      this.m01 = (float)(2.0D * (var1.x * var1.y - var1.w * var1.z) * var3[1]);
      this.m11 = (float)((1.0D - 2.0D * var1.x * var1.x - 2.0D * var1.z * var1.z) * var3[1]);
      this.m21 = (float)(2.0D * (var1.y * var1.z + var1.w * var1.x) * var3[1]);
      this.m02 = (float)(2.0D * (var1.x * var1.z + var1.w * var1.y) * var3[2]);
      this.m12 = (float)(2.0D * (var1.y * var1.z - var1.w * var1.x) * var3[2]);
      this.m22 = (float)((1.0D - 2.0D * var1.x * var1.x - 2.0D * var1.y * var1.y) * var3[2]);
   }

   public final void setRotation(AxisAngle4f var1) {
      double[] var2 = new double[9];
      double[] var3 = new double[3];
      this.getScaleRotate(var3, var2);
      double var4 = Math.sqrt((double)(var1.x * var1.x + var1.y * var1.y + var1.z * var1.z));
      if (var4 < 1.0E-8D) {
         this.m00 = 1.0F;
         this.m01 = 0.0F;
         this.m02 = 0.0F;
         this.m10 = 0.0F;
         this.m11 = 1.0F;
         this.m12 = 0.0F;
         this.m20 = 0.0F;
         this.m21 = 0.0F;
         this.m22 = 1.0F;
      } else {
         var4 = 1.0D / var4;
         double var6 = (double)var1.x * var4;
         double var8 = (double)var1.y * var4;
         double var10 = (double)var1.z * var4;
         double var12 = Math.sin((double)var1.angle);
         double var14 = Math.cos((double)var1.angle);
         double var16 = 1.0D - var14;
         double var18 = (double)(var1.x * var1.z);
         double var20 = (double)(var1.x * var1.y);
         double var22 = (double)(var1.y * var1.z);
         this.m00 = (float)((var16 * var6 * var6 + var14) * var3[0]);
         this.m01 = (float)((var16 * var20 - var12 * var10) * var3[1]);
         this.m02 = (float)((var16 * var18 + var12 * var8) * var3[2]);
         this.m10 = (float)((var16 * var20 + var12 * var10) * var3[0]);
         this.m11 = (float)((var16 * var8 * var8 + var14) * var3[1]);
         this.m12 = (float)((var16 * var22 - var12 * var6) * var3[2]);
         this.m20 = (float)((var16 * var18 - var12 * var8) * var3[0]);
         this.m21 = (float)((var16 * var22 + var12 * var6) * var3[1]);
         this.m22 = (float)((var16 * var10 * var10 + var14) * var3[2]);
      }

   }

   public final void setZero() {
      this.m00 = 0.0F;
      this.m01 = 0.0F;
      this.m02 = 0.0F;
      this.m03 = 0.0F;
      this.m10 = 0.0F;
      this.m11 = 0.0F;
      this.m12 = 0.0F;
      this.m13 = 0.0F;
      this.m20 = 0.0F;
      this.m21 = 0.0F;
      this.m22 = 0.0F;
      this.m23 = 0.0F;
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.m33 = 0.0F;
   }

   public final void negate() {
      this.m00 = -this.m00;
      this.m01 = -this.m01;
      this.m02 = -this.m02;
      this.m03 = -this.m03;
      this.m10 = -this.m10;
      this.m11 = -this.m11;
      this.m12 = -this.m12;
      this.m13 = -this.m13;
      this.m20 = -this.m20;
      this.m21 = -this.m21;
      this.m22 = -this.m22;
      this.m23 = -this.m23;
      this.m30 = -this.m30;
      this.m31 = -this.m31;
      this.m32 = -this.m32;
      this.m33 = -this.m33;
   }

   public final void negate(Matrix4f var1) {
      this.m00 = -var1.m00;
      this.m01 = -var1.m01;
      this.m02 = -var1.m02;
      this.m03 = -var1.m03;
      this.m10 = -var1.m10;
      this.m11 = -var1.m11;
      this.m12 = -var1.m12;
      this.m13 = -var1.m13;
      this.m20 = -var1.m20;
      this.m21 = -var1.m21;
      this.m22 = -var1.m22;
      this.m23 = -var1.m23;
      this.m30 = -var1.m30;
      this.m31 = -var1.m31;
      this.m32 = -var1.m32;
      this.m33 = -var1.m33;
   }

   private final void getScaleRotate(double[] var1, double[] var2) {
      double[] var3 = new double[]{(double)this.m00, (double)this.m01, (double)this.m02, (double)this.m10, (double)this.m11, (double)this.m12, (double)this.m20, (double)this.m21, (double)this.m22};
      Matrix3d.compute_svd(var3, var1, var2);
   }

   public Object clone() {
      Matrix4f var1 = null;

      try {
         var1 = (Matrix4f)super.clone();
         return var1;
      } catch (CloneNotSupportedException var3) {
         throw new InternalError();
      }
   }
}

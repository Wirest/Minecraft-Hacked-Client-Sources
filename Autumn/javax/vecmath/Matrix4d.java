package javax.vecmath;

import java.io.Serializable;

public class Matrix4d implements Serializable, Cloneable {
   static final long serialVersionUID = 8223903484171633710L;
   public double m00;
   public double m01;
   public double m02;
   public double m03;
   public double m10;
   public double m11;
   public double m12;
   public double m13;
   public double m20;
   public double m21;
   public double m22;
   public double m23;
   public double m30;
   public double m31;
   public double m32;
   public double m33;
   private static final double EPS = 1.0E-10D;

   public Matrix4d(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23, double var25, double var27, double var29, double var31) {
      this.m00 = var1;
      this.m01 = var3;
      this.m02 = var5;
      this.m03 = var7;
      this.m10 = var9;
      this.m11 = var11;
      this.m12 = var13;
      this.m13 = var15;
      this.m20 = var17;
      this.m21 = var19;
      this.m22 = var21;
      this.m23 = var23;
      this.m30 = var25;
      this.m31 = var27;
      this.m32 = var29;
      this.m33 = var31;
   }

   public Matrix4d(double[] var1) {
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

   public Matrix4d(Quat4d var1, Vector3d var2, double var3) {
      this.m00 = var3 * (1.0D - 2.0D * var1.y * var1.y - 2.0D * var1.z * var1.z);
      this.m10 = var3 * 2.0D * (var1.x * var1.y + var1.w * var1.z);
      this.m20 = var3 * 2.0D * (var1.x * var1.z - var1.w * var1.y);
      this.m01 = var3 * 2.0D * (var1.x * var1.y - var1.w * var1.z);
      this.m11 = var3 * (1.0D - 2.0D * var1.x * var1.x - 2.0D * var1.z * var1.z);
      this.m21 = var3 * 2.0D * (var1.y * var1.z + var1.w * var1.x);
      this.m02 = var3 * 2.0D * (var1.x * var1.z + var1.w * var1.y);
      this.m12 = var3 * 2.0D * (var1.y * var1.z - var1.w * var1.x);
      this.m22 = var3 * (1.0D - 2.0D * var1.x * var1.x - 2.0D * var1.y * var1.y);
      this.m03 = var2.x;
      this.m13 = var2.y;
      this.m23 = var2.z;
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.m33 = 1.0D;
   }

   public Matrix4d(Quat4f var1, Vector3d var2, double var3) {
      this.m00 = var3 * (1.0D - 2.0D * (double)var1.y * (double)var1.y - 2.0D * (double)var1.z * (double)var1.z);
      this.m10 = var3 * 2.0D * (double)(var1.x * var1.y + var1.w * var1.z);
      this.m20 = var3 * 2.0D * (double)(var1.x * var1.z - var1.w * var1.y);
      this.m01 = var3 * 2.0D * (double)(var1.x * var1.y - var1.w * var1.z);
      this.m11 = var3 * (1.0D - 2.0D * (double)var1.x * (double)var1.x - 2.0D * (double)var1.z * (double)var1.z);
      this.m21 = var3 * 2.0D * (double)(var1.y * var1.z + var1.w * var1.x);
      this.m02 = var3 * 2.0D * (double)(var1.x * var1.z + var1.w * var1.y);
      this.m12 = var3 * 2.0D * (double)(var1.y * var1.z - var1.w * var1.x);
      this.m22 = var3 * (1.0D - 2.0D * (double)var1.x * (double)var1.x - 2.0D * (double)var1.y * (double)var1.y);
      this.m03 = var2.x;
      this.m13 = var2.y;
      this.m23 = var2.z;
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.m33 = 1.0D;
   }

   public Matrix4d(Matrix4d var1) {
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

   public Matrix4d(Matrix4f var1) {
      this.m00 = (double)var1.m00;
      this.m01 = (double)var1.m01;
      this.m02 = (double)var1.m02;
      this.m03 = (double)var1.m03;
      this.m10 = (double)var1.m10;
      this.m11 = (double)var1.m11;
      this.m12 = (double)var1.m12;
      this.m13 = (double)var1.m13;
      this.m20 = (double)var1.m20;
      this.m21 = (double)var1.m21;
      this.m22 = (double)var1.m22;
      this.m23 = (double)var1.m23;
      this.m30 = (double)var1.m30;
      this.m31 = (double)var1.m31;
      this.m32 = (double)var1.m32;
      this.m33 = (double)var1.m33;
   }

   public Matrix4d(Matrix3f var1, Vector3d var2, double var3) {
      this.m00 = (double)var1.m00 * var3;
      this.m01 = (double)var1.m01 * var3;
      this.m02 = (double)var1.m02 * var3;
      this.m03 = var2.x;
      this.m10 = (double)var1.m10 * var3;
      this.m11 = (double)var1.m11 * var3;
      this.m12 = (double)var1.m12 * var3;
      this.m13 = var2.y;
      this.m20 = (double)var1.m20 * var3;
      this.m21 = (double)var1.m21 * var3;
      this.m22 = (double)var1.m22 * var3;
      this.m23 = var2.z;
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.m33 = 1.0D;
   }

   public Matrix4d(Matrix3d var1, Vector3d var2, double var3) {
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
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.m33 = 1.0D;
   }

   public Matrix4d() {
      this.m00 = 0.0D;
      this.m01 = 0.0D;
      this.m02 = 0.0D;
      this.m03 = 0.0D;
      this.m10 = 0.0D;
      this.m11 = 0.0D;
      this.m12 = 0.0D;
      this.m13 = 0.0D;
      this.m20 = 0.0D;
      this.m21 = 0.0D;
      this.m22 = 0.0D;
      this.m23 = 0.0D;
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.m33 = 0.0D;
   }

   public String toString() {
      return this.m00 + ", " + this.m01 + ", " + this.m02 + ", " + this.m03 + "\n" + this.m10 + ", " + this.m11 + ", " + this.m12 + ", " + this.m13 + "\n" + this.m20 + ", " + this.m21 + ", " + this.m22 + ", " + this.m23 + "\n" + this.m30 + ", " + this.m31 + ", " + this.m32 + ", " + this.m33 + "\n";
   }

   public final void setIdentity() {
      this.m00 = 1.0D;
      this.m01 = 0.0D;
      this.m02 = 0.0D;
      this.m03 = 0.0D;
      this.m10 = 0.0D;
      this.m11 = 1.0D;
      this.m12 = 0.0D;
      this.m13 = 0.0D;
      this.m20 = 0.0D;
      this.m21 = 0.0D;
      this.m22 = 1.0D;
      this.m23 = 0.0D;
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.m33 = 1.0D;
   }

   public final void setElement(int var1, int var2, double var3) {
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
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d0"));
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
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d0"));
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
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d0"));
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
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d0"));
         }
      default:
         throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d0"));
      }
   }

   public final double getElement(int var1, int var2) {
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
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d1"));
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
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d1"));
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
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d1"));
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

      throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d1"));
   }

   public final void getRow(int var1, Vector4d var2) {
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
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d2"));
         }

         var2.x = this.m30;
         var2.y = this.m31;
         var2.z = this.m32;
         var2.w = this.m33;
      }

   }

   public final void getRow(int var1, double[] var2) {
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
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d2"));
         }

         var2[0] = this.m30;
         var2[1] = this.m31;
         var2[2] = this.m32;
         var2[3] = this.m33;
      }

   }

   public final void getColumn(int var1, Vector4d var2) {
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
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d3"));
         }

         var2.x = this.m03;
         var2.y = this.m13;
         var2.z = this.m23;
         var2.w = this.m33;
      }

   }

   public final void getColumn(int var1, double[] var2) {
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
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d3"));
         }

         var2[0] = this.m03;
         var2[1] = this.m13;
         var2[2] = this.m23;
         var2[3] = this.m33;
      }

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

   public final double get(Matrix3d var1, Vector3d var2) {
      double[] var3 = new double[9];
      double[] var4 = new double[3];
      this.getScaleRotate(var4, var3);
      var1.m00 = var3[0];
      var1.m01 = var3[1];
      var1.m02 = var3[2];
      var1.m10 = var3[3];
      var1.m11 = var3[4];
      var1.m12 = var3[5];
      var1.m20 = var3[6];
      var1.m21 = var3[7];
      var1.m22 = var3[8];
      var2.x = this.m03;
      var2.y = this.m13;
      var2.z = this.m23;
      return Matrix3d.max3(var4);
   }

   public final double get(Matrix3f var1, Vector3d var2) {
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
      return Matrix3d.max3(var4);
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

   public final void get(Quat4d var1) {
      double[] var2 = new double[9];
      double[] var3 = new double[3];
      this.getScaleRotate(var3, var2);
      double var4 = 0.25D * (1.0D + var2[0] + var2[4] + var2[8]);
      if ((var4 < 0.0D ? -var4 : var4) >= 1.0E-30D) {
         var1.w = Math.sqrt(var4);
         var4 = 0.25D / var1.w;
         var1.x = (var2[7] - var2[5]) * var4;
         var1.y = (var2[2] - var2[6]) * var4;
         var1.z = (var2[3] - var2[1]) * var4;
      } else {
         var1.w = 0.0D;
         var4 = -0.5D * (var2[4] + var2[8]);
         if ((var4 < 0.0D ? -var4 : var4) >= 1.0E-30D) {
            var1.x = Math.sqrt(var4);
            var4 = 0.5D / var1.x;
            var1.y = var2[3] * var4;
            var1.z = var2[6] * var4;
         } else {
            var1.x = 0.0D;
            var4 = 0.5D * (1.0D - var2[8]);
            if ((var4 < 0.0D ? -var4 : var4) >= 1.0E-30D) {
               var1.y = Math.sqrt(var4);
               var1.z = var2[7] / (2.0D * var1.y);
            } else {
               var1.y = 0.0D;
               var1.z = 1.0D;
            }
         }
      }
   }

   public final void get(Vector3d var1) {
      var1.x = this.m03;
      var1.y = this.m13;
      var1.z = this.m23;
   }

   public final void getRotationScale(Matrix3f var1) {
      var1.m00 = (float)this.m00;
      var1.m01 = (float)this.m01;
      var1.m02 = (float)this.m02;
      var1.m10 = (float)this.m10;
      var1.m11 = (float)this.m11;
      var1.m12 = (float)this.m12;
      var1.m20 = (float)this.m20;
      var1.m21 = (float)this.m21;
      var1.m22 = (float)this.m22;
   }

   public final void getRotationScale(Matrix3d var1) {
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

   public final double getScale() {
      double[] var1 = new double[9];
      double[] var2 = new double[3];
      this.getScaleRotate(var2, var1);
      return Matrix3d.max3(var2);
   }

   public final void setRotationScale(Matrix3d var1) {
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

   public final void setRotationScale(Matrix3f var1) {
      this.m00 = (double)var1.m00;
      this.m01 = (double)var1.m01;
      this.m02 = (double)var1.m02;
      this.m10 = (double)var1.m10;
      this.m11 = (double)var1.m11;
      this.m12 = (double)var1.m12;
      this.m20 = (double)var1.m20;
      this.m21 = (double)var1.m21;
      this.m22 = (double)var1.m22;
   }

   public final void setScale(double var1) {
      double[] var3 = new double[9];
      double[] var4 = new double[3];
      this.getScaleRotate(var4, var3);
      this.m00 = var3[0] * var1;
      this.m01 = var3[1] * var1;
      this.m02 = var3[2] * var1;
      this.m10 = var3[3] * var1;
      this.m11 = var3[4] * var1;
      this.m12 = var3[5] * var1;
      this.m20 = var3[6] * var1;
      this.m21 = var3[7] * var1;
      this.m22 = var3[8] * var1;
   }

   public final void setRow(int var1, double var2, double var4, double var6, double var8) {
      switch(var1) {
      case 0:
         this.m00 = var2;
         this.m01 = var4;
         this.m02 = var6;
         this.m03 = var8;
         break;
      case 1:
         this.m10 = var2;
         this.m11 = var4;
         this.m12 = var6;
         this.m13 = var8;
         break;
      case 2:
         this.m20 = var2;
         this.m21 = var4;
         this.m22 = var6;
         this.m23 = var8;
         break;
      case 3:
         this.m30 = var2;
         this.m31 = var4;
         this.m32 = var6;
         this.m33 = var8;
         break;
      default:
         throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d4"));
      }

   }

   public final void setRow(int var1, Vector4d var2) {
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
         throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d4"));
      }

   }

   public final void setRow(int var1, double[] var2) {
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
         throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d4"));
      }

   }

   public final void setColumn(int var1, double var2, double var4, double var6, double var8) {
      switch(var1) {
      case 0:
         this.m00 = var2;
         this.m10 = var4;
         this.m20 = var6;
         this.m30 = var8;
         break;
      case 1:
         this.m01 = var2;
         this.m11 = var4;
         this.m21 = var6;
         this.m31 = var8;
         break;
      case 2:
         this.m02 = var2;
         this.m12 = var4;
         this.m22 = var6;
         this.m32 = var8;
         break;
      case 3:
         this.m03 = var2;
         this.m13 = var4;
         this.m23 = var6;
         this.m33 = var8;
         break;
      default:
         throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d7"));
      }

   }

   public final void setColumn(int var1, Vector4d var2) {
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
         throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d7"));
      }

   }

   public final void setColumn(int var1, double[] var2) {
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
         throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d7"));
      }

   }

   public final void add(double var1) {
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

   public final void add(double var1, Matrix4d var3) {
      this.m00 = var3.m00 + var1;
      this.m01 = var3.m01 + var1;
      this.m02 = var3.m02 + var1;
      this.m03 = var3.m03 + var1;
      this.m10 = var3.m10 + var1;
      this.m11 = var3.m11 + var1;
      this.m12 = var3.m12 + var1;
      this.m13 = var3.m13 + var1;
      this.m20 = var3.m20 + var1;
      this.m21 = var3.m21 + var1;
      this.m22 = var3.m22 + var1;
      this.m23 = var3.m23 + var1;
      this.m30 = var3.m30 + var1;
      this.m31 = var3.m31 + var1;
      this.m32 = var3.m32 + var1;
      this.m33 = var3.m33 + var1;
   }

   public final void add(Matrix4d var1, Matrix4d var2) {
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

   public final void add(Matrix4d var1) {
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

   public final void sub(Matrix4d var1, Matrix4d var2) {
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

   public final void sub(Matrix4d var1) {
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
      double var1 = this.m10;
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

   public final void transpose(Matrix4d var1) {
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

   public final void set(double[] var1) {
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

   public final void set(Matrix3f var1) {
      this.m00 = (double)var1.m00;
      this.m01 = (double)var1.m01;
      this.m02 = (double)var1.m02;
      this.m03 = 0.0D;
      this.m10 = (double)var1.m10;
      this.m11 = (double)var1.m11;
      this.m12 = (double)var1.m12;
      this.m13 = 0.0D;
      this.m20 = (double)var1.m20;
      this.m21 = (double)var1.m21;
      this.m22 = (double)var1.m22;
      this.m23 = 0.0D;
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.m33 = 1.0D;
   }

   public final void set(Matrix3d var1) {
      this.m00 = var1.m00;
      this.m01 = var1.m01;
      this.m02 = var1.m02;
      this.m03 = 0.0D;
      this.m10 = var1.m10;
      this.m11 = var1.m11;
      this.m12 = var1.m12;
      this.m13 = 0.0D;
      this.m20 = var1.m20;
      this.m21 = var1.m21;
      this.m22 = var1.m22;
      this.m23 = 0.0D;
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.m33 = 1.0D;
   }

   public final void set(Quat4d var1) {
      this.m00 = 1.0D - 2.0D * var1.y * var1.y - 2.0D * var1.z * var1.z;
      this.m10 = 2.0D * (var1.x * var1.y + var1.w * var1.z);
      this.m20 = 2.0D * (var1.x * var1.z - var1.w * var1.y);
      this.m01 = 2.0D * (var1.x * var1.y - var1.w * var1.z);
      this.m11 = 1.0D - 2.0D * var1.x * var1.x - 2.0D * var1.z * var1.z;
      this.m21 = 2.0D * (var1.y * var1.z + var1.w * var1.x);
      this.m02 = 2.0D * (var1.x * var1.z + var1.w * var1.y);
      this.m12 = 2.0D * (var1.y * var1.z - var1.w * var1.x);
      this.m22 = 1.0D - 2.0D * var1.x * var1.x - 2.0D * var1.y * var1.y;
      this.m03 = 0.0D;
      this.m13 = 0.0D;
      this.m23 = 0.0D;
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.m33 = 1.0D;
   }

   public final void set(AxisAngle4d var1) {
      double var2 = Math.sqrt(var1.x * var1.x + var1.y * var1.y + var1.z * var1.z);
      if (var2 < 1.0E-10D) {
         this.m00 = 1.0D;
         this.m01 = 0.0D;
         this.m02 = 0.0D;
         this.m10 = 0.0D;
         this.m11 = 1.0D;
         this.m12 = 0.0D;
         this.m20 = 0.0D;
         this.m21 = 0.0D;
         this.m22 = 1.0D;
      } else {
         var2 = 1.0D / var2;
         double var4 = var1.x * var2;
         double var6 = var1.y * var2;
         double var8 = var1.z * var2;
         double var10 = Math.sin(var1.angle);
         double var12 = Math.cos(var1.angle);
         double var14 = 1.0D - var12;
         double var16 = var4 * var8;
         double var18 = var4 * var6;
         double var20 = var6 * var8;
         this.m00 = var14 * var4 * var4 + var12;
         this.m01 = var14 * var18 - var10 * var8;
         this.m02 = var14 * var16 + var10 * var6;
         this.m10 = var14 * var18 + var10 * var8;
         this.m11 = var14 * var6 * var6 + var12;
         this.m12 = var14 * var20 - var10 * var4;
         this.m20 = var14 * var16 - var10 * var6;
         this.m21 = var14 * var20 + var10 * var4;
         this.m22 = var14 * var8 * var8 + var12;
      }

      this.m03 = 0.0D;
      this.m13 = 0.0D;
      this.m23 = 0.0D;
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.m33 = 1.0D;
   }

   public final void set(Quat4f var1) {
      this.m00 = 1.0D - 2.0D * (double)var1.y * (double)var1.y - 2.0D * (double)var1.z * (double)var1.z;
      this.m10 = 2.0D * (double)(var1.x * var1.y + var1.w * var1.z);
      this.m20 = 2.0D * (double)(var1.x * var1.z - var1.w * var1.y);
      this.m01 = 2.0D * (double)(var1.x * var1.y - var1.w * var1.z);
      this.m11 = 1.0D - 2.0D * (double)var1.x * (double)var1.x - 2.0D * (double)var1.z * (double)var1.z;
      this.m21 = 2.0D * (double)(var1.y * var1.z + var1.w * var1.x);
      this.m02 = 2.0D * (double)(var1.x * var1.z + var1.w * var1.y);
      this.m12 = 2.0D * (double)(var1.y * var1.z - var1.w * var1.x);
      this.m22 = 1.0D - 2.0D * (double)var1.x * (double)var1.x - 2.0D * (double)var1.y * (double)var1.y;
      this.m03 = 0.0D;
      this.m13 = 0.0D;
      this.m23 = 0.0D;
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.m33 = 1.0D;
   }

   public final void set(AxisAngle4f var1) {
      double var2 = Math.sqrt((double)(var1.x * var1.x + var1.y * var1.y + var1.z * var1.z));
      if (var2 < 1.0E-10D) {
         this.m00 = 1.0D;
         this.m01 = 0.0D;
         this.m02 = 0.0D;
         this.m10 = 0.0D;
         this.m11 = 1.0D;
         this.m12 = 0.0D;
         this.m20 = 0.0D;
         this.m21 = 0.0D;
         this.m22 = 1.0D;
      } else {
         var2 = 1.0D / var2;
         double var4 = (double)var1.x * var2;
         double var6 = (double)var1.y * var2;
         double var8 = (double)var1.z * var2;
         double var10 = Math.sin((double)var1.angle);
         double var12 = Math.cos((double)var1.angle);
         double var14 = 1.0D - var12;
         double var16 = var4 * var8;
         double var18 = var4 * var6;
         double var20 = var6 * var8;
         this.m00 = var14 * var4 * var4 + var12;
         this.m01 = var14 * var18 - var10 * var8;
         this.m02 = var14 * var16 + var10 * var6;
         this.m10 = var14 * var18 + var10 * var8;
         this.m11 = var14 * var6 * var6 + var12;
         this.m12 = var14 * var20 - var10 * var4;
         this.m20 = var14 * var16 - var10 * var6;
         this.m21 = var14 * var20 + var10 * var4;
         this.m22 = var14 * var8 * var8 + var12;
      }

      this.m03 = 0.0D;
      this.m13 = 0.0D;
      this.m23 = 0.0D;
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.m33 = 1.0D;
   }

   public final void set(Quat4d var1, Vector3d var2, double var3) {
      this.m00 = var3 * (1.0D - 2.0D * var1.y * var1.y - 2.0D * var1.z * var1.z);
      this.m10 = var3 * 2.0D * (var1.x * var1.y + var1.w * var1.z);
      this.m20 = var3 * 2.0D * (var1.x * var1.z - var1.w * var1.y);
      this.m01 = var3 * 2.0D * (var1.x * var1.y - var1.w * var1.z);
      this.m11 = var3 * (1.0D - 2.0D * var1.x * var1.x - 2.0D * var1.z * var1.z);
      this.m21 = var3 * 2.0D * (var1.y * var1.z + var1.w * var1.x);
      this.m02 = var3 * 2.0D * (var1.x * var1.z + var1.w * var1.y);
      this.m12 = var3 * 2.0D * (var1.y * var1.z - var1.w * var1.x);
      this.m22 = var3 * (1.0D - 2.0D * var1.x * var1.x - 2.0D * var1.y * var1.y);
      this.m03 = var2.x;
      this.m13 = var2.y;
      this.m23 = var2.z;
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.m33 = 1.0D;
   }

   public final void set(Quat4f var1, Vector3d var2, double var3) {
      this.m00 = var3 * (1.0D - 2.0D * (double)var1.y * (double)var1.y - 2.0D * (double)var1.z * (double)var1.z);
      this.m10 = var3 * 2.0D * (double)(var1.x * var1.y + var1.w * var1.z);
      this.m20 = var3 * 2.0D * (double)(var1.x * var1.z - var1.w * var1.y);
      this.m01 = var3 * 2.0D * (double)(var1.x * var1.y - var1.w * var1.z);
      this.m11 = var3 * (1.0D - 2.0D * (double)var1.x * (double)var1.x - 2.0D * (double)var1.z * (double)var1.z);
      this.m21 = var3 * 2.0D * (double)(var1.y * var1.z + var1.w * var1.x);
      this.m02 = var3 * 2.0D * (double)(var1.x * var1.z + var1.w * var1.y);
      this.m12 = var3 * 2.0D * (double)(var1.y * var1.z - var1.w * var1.x);
      this.m22 = var3 * (1.0D - 2.0D * (double)var1.x * (double)var1.x - 2.0D * (double)var1.y * (double)var1.y);
      this.m03 = var2.x;
      this.m13 = var2.y;
      this.m23 = var2.z;
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.m33 = 1.0D;
   }

   public final void set(Quat4f var1, Vector3f var2, float var3) {
      this.m00 = (double)var3 * (1.0D - 2.0D * (double)var1.y * (double)var1.y - 2.0D * (double)var1.z * (double)var1.z);
      this.m10 = (double)var3 * 2.0D * (double)(var1.x * var1.y + var1.w * var1.z);
      this.m20 = (double)var3 * 2.0D * (double)(var1.x * var1.z - var1.w * var1.y);
      this.m01 = (double)var3 * 2.0D * (double)(var1.x * var1.y - var1.w * var1.z);
      this.m11 = (double)var3 * (1.0D - 2.0D * (double)var1.x * (double)var1.x - 2.0D * (double)var1.z * (double)var1.z);
      this.m21 = (double)var3 * 2.0D * (double)(var1.y * var1.z + var1.w * var1.x);
      this.m02 = (double)var3 * 2.0D * (double)(var1.x * var1.z + var1.w * var1.y);
      this.m12 = (double)var3 * 2.0D * (double)(var1.y * var1.z - var1.w * var1.x);
      this.m22 = (double)var3 * (1.0D - 2.0D * (double)var1.x * (double)var1.x - 2.0D * (double)var1.y * (double)var1.y);
      this.m03 = (double)var2.x;
      this.m13 = (double)var2.y;
      this.m23 = (double)var2.z;
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.m33 = 1.0D;
   }

   public final void set(Matrix4f var1) {
      this.m00 = (double)var1.m00;
      this.m01 = (double)var1.m01;
      this.m02 = (double)var1.m02;
      this.m03 = (double)var1.m03;
      this.m10 = (double)var1.m10;
      this.m11 = (double)var1.m11;
      this.m12 = (double)var1.m12;
      this.m13 = (double)var1.m13;
      this.m20 = (double)var1.m20;
      this.m21 = (double)var1.m21;
      this.m22 = (double)var1.m22;
      this.m23 = (double)var1.m23;
      this.m30 = (double)var1.m30;
      this.m31 = (double)var1.m31;
      this.m32 = (double)var1.m32;
      this.m33 = (double)var1.m33;
   }

   public final void set(Matrix4d var1) {
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

   public final void invert(Matrix4d var1) {
      this.invertGeneral(var1);
   }

   public final void invert() {
      this.invertGeneral(this);
   }

   final void invertGeneral(Matrix4d var1) {
      double[] var2 = new double[16];
      int[] var3 = new int[4];
      double[] var7 = new double[]{var1.m00, var1.m01, var1.m02, var1.m03, var1.m10, var1.m11, var1.m12, var1.m13, var1.m20, var1.m21, var1.m22, var1.m23, var1.m30, var1.m31, var1.m32, var1.m33};
      if (!luDecomposition(var7, var3)) {
         throw new SingularMatrixException(VecMathI18N.getString("Matrix4d10"));
      } else {
         for(int var4 = 0; var4 < 16; ++var4) {
            var2[var4] = 0.0D;
         }

         var2[0] = 1.0D;
         var2[5] = 1.0D;
         var2[10] = 1.0D;
         var2[15] = 1.0D;
         luBacksubstitution(var7, var3, var2);
         this.m00 = var2[0];
         this.m01 = var2[1];
         this.m02 = var2[2];
         this.m03 = var2[3];
         this.m10 = var2[4];
         this.m11 = var2[5];
         this.m12 = var2[6];
         this.m13 = var2[7];
         this.m20 = var2[8];
         this.m21 = var2[9];
         this.m22 = var2[10];
         this.m23 = var2[11];
         this.m30 = var2[12];
         this.m31 = var2[13];
         this.m32 = var2[14];
         this.m33 = var2[15];
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
            throw new RuntimeException(VecMathI18N.getString("Matrix4d11"));
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

   public final double determinant() {
      double var1 = this.m00 * (this.m11 * this.m22 * this.m33 + this.m12 * this.m23 * this.m31 + this.m13 * this.m21 * this.m32 - this.m13 * this.m22 * this.m31 - this.m11 * this.m23 * this.m32 - this.m12 * this.m21 * this.m33);
      var1 -= this.m01 * (this.m10 * this.m22 * this.m33 + this.m12 * this.m23 * this.m30 + this.m13 * this.m20 * this.m32 - this.m13 * this.m22 * this.m30 - this.m10 * this.m23 * this.m32 - this.m12 * this.m20 * this.m33);
      var1 += this.m02 * (this.m10 * this.m21 * this.m33 + this.m11 * this.m23 * this.m30 + this.m13 * this.m20 * this.m31 - this.m13 * this.m21 * this.m30 - this.m10 * this.m23 * this.m31 - this.m11 * this.m20 * this.m33);
      var1 -= this.m03 * (this.m10 * this.m21 * this.m32 + this.m11 * this.m22 * this.m30 + this.m12 * this.m20 * this.m31 - this.m12 * this.m21 * this.m30 - this.m10 * this.m22 * this.m31 - this.m11 * this.m20 * this.m32);
      return var1;
   }

   public final void set(double var1) {
      this.m00 = var1;
      this.m01 = 0.0D;
      this.m02 = 0.0D;
      this.m03 = 0.0D;
      this.m10 = 0.0D;
      this.m11 = var1;
      this.m12 = 0.0D;
      this.m13 = 0.0D;
      this.m20 = 0.0D;
      this.m21 = 0.0D;
      this.m22 = var1;
      this.m23 = 0.0D;
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.m33 = 1.0D;
   }

   public final void set(Vector3d var1) {
      this.m00 = 1.0D;
      this.m01 = 0.0D;
      this.m02 = 0.0D;
      this.m03 = var1.x;
      this.m10 = 0.0D;
      this.m11 = 1.0D;
      this.m12 = 0.0D;
      this.m13 = var1.y;
      this.m20 = 0.0D;
      this.m21 = 0.0D;
      this.m22 = 1.0D;
      this.m23 = var1.z;
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.m33 = 1.0D;
   }

   public final void set(double var1, Vector3d var3) {
      this.m00 = var1;
      this.m01 = 0.0D;
      this.m02 = 0.0D;
      this.m03 = var3.x;
      this.m10 = 0.0D;
      this.m11 = var1;
      this.m12 = 0.0D;
      this.m13 = var3.y;
      this.m20 = 0.0D;
      this.m21 = 0.0D;
      this.m22 = var1;
      this.m23 = var3.z;
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.m33 = 1.0D;
   }

   public final void set(Vector3d var1, double var2) {
      this.m00 = var2;
      this.m01 = 0.0D;
      this.m02 = 0.0D;
      this.m03 = var2 * var1.x;
      this.m10 = 0.0D;
      this.m11 = var2;
      this.m12 = 0.0D;
      this.m13 = var2 * var1.y;
      this.m20 = 0.0D;
      this.m21 = 0.0D;
      this.m22 = var2;
      this.m23 = var2 * var1.z;
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.m33 = 1.0D;
   }

   public final void set(Matrix3f var1, Vector3f var2, float var3) {
      this.m00 = (double)(var1.m00 * var3);
      this.m01 = (double)(var1.m01 * var3);
      this.m02 = (double)(var1.m02 * var3);
      this.m03 = (double)var2.x;
      this.m10 = (double)(var1.m10 * var3);
      this.m11 = (double)(var1.m11 * var3);
      this.m12 = (double)(var1.m12 * var3);
      this.m13 = (double)var2.y;
      this.m20 = (double)(var1.m20 * var3);
      this.m21 = (double)(var1.m21 * var3);
      this.m22 = (double)(var1.m22 * var3);
      this.m23 = (double)var2.z;
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.m33 = 1.0D;
   }

   public final void set(Matrix3d var1, Vector3d var2, double var3) {
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
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.m33 = 1.0D;
   }

   public final void setTranslation(Vector3d var1) {
      this.m03 = var1.x;
      this.m13 = var1.y;
      this.m23 = var1.z;
   }

   public final void rotX(double var1) {
      double var3 = Math.sin(var1);
      double var5 = Math.cos(var1);
      this.m00 = 1.0D;
      this.m01 = 0.0D;
      this.m02 = 0.0D;
      this.m03 = 0.0D;
      this.m10 = 0.0D;
      this.m11 = var5;
      this.m12 = -var3;
      this.m13 = 0.0D;
      this.m20 = 0.0D;
      this.m21 = var3;
      this.m22 = var5;
      this.m23 = 0.0D;
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.m33 = 1.0D;
   }

   public final void rotY(double var1) {
      double var3 = Math.sin(var1);
      double var5 = Math.cos(var1);
      this.m00 = var5;
      this.m01 = 0.0D;
      this.m02 = var3;
      this.m03 = 0.0D;
      this.m10 = 0.0D;
      this.m11 = 1.0D;
      this.m12 = 0.0D;
      this.m13 = 0.0D;
      this.m20 = -var3;
      this.m21 = 0.0D;
      this.m22 = var5;
      this.m23 = 0.0D;
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.m33 = 1.0D;
   }

   public final void rotZ(double var1) {
      double var3 = Math.sin(var1);
      double var5 = Math.cos(var1);
      this.m00 = var5;
      this.m01 = -var3;
      this.m02 = 0.0D;
      this.m03 = 0.0D;
      this.m10 = var3;
      this.m11 = var5;
      this.m12 = 0.0D;
      this.m13 = 0.0D;
      this.m20 = 0.0D;
      this.m21 = 0.0D;
      this.m22 = 1.0D;
      this.m23 = 0.0D;
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.m33 = 1.0D;
   }

   public final void mul(double var1) {
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

   public final void mul(double var1, Matrix4d var3) {
      this.m00 = var3.m00 * var1;
      this.m01 = var3.m01 * var1;
      this.m02 = var3.m02 * var1;
      this.m03 = var3.m03 * var1;
      this.m10 = var3.m10 * var1;
      this.m11 = var3.m11 * var1;
      this.m12 = var3.m12 * var1;
      this.m13 = var3.m13 * var1;
      this.m20 = var3.m20 * var1;
      this.m21 = var3.m21 * var1;
      this.m22 = var3.m22 * var1;
      this.m23 = var3.m23 * var1;
      this.m30 = var3.m30 * var1;
      this.m31 = var3.m31 * var1;
      this.m32 = var3.m32 * var1;
      this.m33 = var3.m33 * var1;
   }

   public final void mul(Matrix4d var1) {
      double var2 = this.m00 * var1.m00 + this.m01 * var1.m10 + this.m02 * var1.m20 + this.m03 * var1.m30;
      double var4 = this.m00 * var1.m01 + this.m01 * var1.m11 + this.m02 * var1.m21 + this.m03 * var1.m31;
      double var6 = this.m00 * var1.m02 + this.m01 * var1.m12 + this.m02 * var1.m22 + this.m03 * var1.m32;
      double var8 = this.m00 * var1.m03 + this.m01 * var1.m13 + this.m02 * var1.m23 + this.m03 * var1.m33;
      double var10 = this.m10 * var1.m00 + this.m11 * var1.m10 + this.m12 * var1.m20 + this.m13 * var1.m30;
      double var12 = this.m10 * var1.m01 + this.m11 * var1.m11 + this.m12 * var1.m21 + this.m13 * var1.m31;
      double var14 = this.m10 * var1.m02 + this.m11 * var1.m12 + this.m12 * var1.m22 + this.m13 * var1.m32;
      double var16 = this.m10 * var1.m03 + this.m11 * var1.m13 + this.m12 * var1.m23 + this.m13 * var1.m33;
      double var18 = this.m20 * var1.m00 + this.m21 * var1.m10 + this.m22 * var1.m20 + this.m23 * var1.m30;
      double var20 = this.m20 * var1.m01 + this.m21 * var1.m11 + this.m22 * var1.m21 + this.m23 * var1.m31;
      double var22 = this.m20 * var1.m02 + this.m21 * var1.m12 + this.m22 * var1.m22 + this.m23 * var1.m32;
      double var24 = this.m20 * var1.m03 + this.m21 * var1.m13 + this.m22 * var1.m23 + this.m23 * var1.m33;
      double var26 = this.m30 * var1.m00 + this.m31 * var1.m10 + this.m32 * var1.m20 + this.m33 * var1.m30;
      double var28 = this.m30 * var1.m01 + this.m31 * var1.m11 + this.m32 * var1.m21 + this.m33 * var1.m31;
      double var30 = this.m30 * var1.m02 + this.m31 * var1.m12 + this.m32 * var1.m22 + this.m33 * var1.m32;
      double var32 = this.m30 * var1.m03 + this.m31 * var1.m13 + this.m32 * var1.m23 + this.m33 * var1.m33;
      this.m00 = var2;
      this.m01 = var4;
      this.m02 = var6;
      this.m03 = var8;
      this.m10 = var10;
      this.m11 = var12;
      this.m12 = var14;
      this.m13 = var16;
      this.m20 = var18;
      this.m21 = var20;
      this.m22 = var22;
      this.m23 = var24;
      this.m30 = var26;
      this.m31 = var28;
      this.m32 = var30;
      this.m33 = var32;
   }

   public final void mul(Matrix4d var1, Matrix4d var2) {
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
         double var3 = var1.m00 * var2.m00 + var1.m01 * var2.m10 + var1.m02 * var2.m20 + var1.m03 * var2.m30;
         double var5 = var1.m00 * var2.m01 + var1.m01 * var2.m11 + var1.m02 * var2.m21 + var1.m03 * var2.m31;
         double var7 = var1.m00 * var2.m02 + var1.m01 * var2.m12 + var1.m02 * var2.m22 + var1.m03 * var2.m32;
         double var9 = var1.m00 * var2.m03 + var1.m01 * var2.m13 + var1.m02 * var2.m23 + var1.m03 * var2.m33;
         double var11 = var1.m10 * var2.m00 + var1.m11 * var2.m10 + var1.m12 * var2.m20 + var1.m13 * var2.m30;
         double var13 = var1.m10 * var2.m01 + var1.m11 * var2.m11 + var1.m12 * var2.m21 + var1.m13 * var2.m31;
         double var15 = var1.m10 * var2.m02 + var1.m11 * var2.m12 + var1.m12 * var2.m22 + var1.m13 * var2.m32;
         double var17 = var1.m10 * var2.m03 + var1.m11 * var2.m13 + var1.m12 * var2.m23 + var1.m13 * var2.m33;
         double var19 = var1.m20 * var2.m00 + var1.m21 * var2.m10 + var1.m22 * var2.m20 + var1.m23 * var2.m30;
         double var21 = var1.m20 * var2.m01 + var1.m21 * var2.m11 + var1.m22 * var2.m21 + var1.m23 * var2.m31;
         double var23 = var1.m20 * var2.m02 + var1.m21 * var2.m12 + var1.m22 * var2.m22 + var1.m23 * var2.m32;
         double var25 = var1.m20 * var2.m03 + var1.m21 * var2.m13 + var1.m22 * var2.m23 + var1.m23 * var2.m33;
         double var27 = var1.m30 * var2.m00 + var1.m31 * var2.m10 + var1.m32 * var2.m20 + var1.m33 * var2.m30;
         double var29 = var1.m30 * var2.m01 + var1.m31 * var2.m11 + var1.m32 * var2.m21 + var1.m33 * var2.m31;
         double var31 = var1.m30 * var2.m02 + var1.m31 * var2.m12 + var1.m32 * var2.m22 + var1.m33 * var2.m32;
         double var33 = var1.m30 * var2.m03 + var1.m31 * var2.m13 + var1.m32 * var2.m23 + var1.m33 * var2.m33;
         this.m00 = var3;
         this.m01 = var5;
         this.m02 = var7;
         this.m03 = var9;
         this.m10 = var11;
         this.m11 = var13;
         this.m12 = var15;
         this.m13 = var17;
         this.m20 = var19;
         this.m21 = var21;
         this.m22 = var23;
         this.m23 = var25;
         this.m30 = var27;
         this.m31 = var29;
         this.m32 = var31;
         this.m33 = var33;
      }

   }

   public final void mulTransposeBoth(Matrix4d var1, Matrix4d var2) {
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
         double var3 = var1.m00 * var2.m00 + var1.m10 * var2.m01 + var1.m20 * var2.m02 + var1.m30 * var2.m03;
         double var5 = var1.m00 * var2.m10 + var1.m10 * var2.m11 + var1.m20 * var2.m12 + var1.m30 * var2.m13;
         double var7 = var1.m00 * var2.m20 + var1.m10 * var2.m21 + var1.m20 * var2.m22 + var1.m30 * var2.m23;
         double var9 = var1.m00 * var2.m30 + var1.m10 * var2.m31 + var1.m20 * var2.m32 + var1.m30 * var2.m33;
         double var11 = var1.m01 * var2.m00 + var1.m11 * var2.m01 + var1.m21 * var2.m02 + var1.m31 * var2.m03;
         double var13 = var1.m01 * var2.m10 + var1.m11 * var2.m11 + var1.m21 * var2.m12 + var1.m31 * var2.m13;
         double var15 = var1.m01 * var2.m20 + var1.m11 * var2.m21 + var1.m21 * var2.m22 + var1.m31 * var2.m23;
         double var17 = var1.m01 * var2.m30 + var1.m11 * var2.m31 + var1.m21 * var2.m32 + var1.m31 * var2.m33;
         double var19 = var1.m02 * var2.m00 + var1.m12 * var2.m01 + var1.m22 * var2.m02 + var1.m32 * var2.m03;
         double var21 = var1.m02 * var2.m10 + var1.m12 * var2.m11 + var1.m22 * var2.m12 + var1.m32 * var2.m13;
         double var23 = var1.m02 * var2.m20 + var1.m12 * var2.m21 + var1.m22 * var2.m22 + var1.m32 * var2.m23;
         double var25 = var1.m02 * var2.m30 + var1.m12 * var2.m31 + var1.m22 * var2.m32 + var1.m32 * var2.m33;
         double var27 = var1.m03 * var2.m00 + var1.m13 * var2.m01 + var1.m23 * var2.m02 + var1.m33 * var2.m03;
         double var29 = var1.m03 * var2.m10 + var1.m13 * var2.m11 + var1.m23 * var2.m12 + var1.m33 * var2.m13;
         double var31 = var1.m03 * var2.m20 + var1.m13 * var2.m21 + var1.m23 * var2.m22 + var1.m33 * var2.m23;
         double var33 = var1.m03 * var2.m30 + var1.m13 * var2.m31 + var1.m23 * var2.m32 + var1.m33 * var2.m33;
         this.m00 = var3;
         this.m01 = var5;
         this.m02 = var7;
         this.m03 = var9;
         this.m10 = var11;
         this.m11 = var13;
         this.m12 = var15;
         this.m13 = var17;
         this.m20 = var19;
         this.m21 = var21;
         this.m22 = var23;
         this.m23 = var25;
         this.m30 = var27;
         this.m31 = var29;
         this.m32 = var31;
         this.m33 = var33;
      }

   }

   public final void mulTransposeRight(Matrix4d var1, Matrix4d var2) {
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
         double var3 = var1.m00 * var2.m00 + var1.m01 * var2.m01 + var1.m02 * var2.m02 + var1.m03 * var2.m03;
         double var5 = var1.m00 * var2.m10 + var1.m01 * var2.m11 + var1.m02 * var2.m12 + var1.m03 * var2.m13;
         double var7 = var1.m00 * var2.m20 + var1.m01 * var2.m21 + var1.m02 * var2.m22 + var1.m03 * var2.m23;
         double var9 = var1.m00 * var2.m30 + var1.m01 * var2.m31 + var1.m02 * var2.m32 + var1.m03 * var2.m33;
         double var11 = var1.m10 * var2.m00 + var1.m11 * var2.m01 + var1.m12 * var2.m02 + var1.m13 * var2.m03;
         double var13 = var1.m10 * var2.m10 + var1.m11 * var2.m11 + var1.m12 * var2.m12 + var1.m13 * var2.m13;
         double var15 = var1.m10 * var2.m20 + var1.m11 * var2.m21 + var1.m12 * var2.m22 + var1.m13 * var2.m23;
         double var17 = var1.m10 * var2.m30 + var1.m11 * var2.m31 + var1.m12 * var2.m32 + var1.m13 * var2.m33;
         double var19 = var1.m20 * var2.m00 + var1.m21 * var2.m01 + var1.m22 * var2.m02 + var1.m23 * var2.m03;
         double var21 = var1.m20 * var2.m10 + var1.m21 * var2.m11 + var1.m22 * var2.m12 + var1.m23 * var2.m13;
         double var23 = var1.m20 * var2.m20 + var1.m21 * var2.m21 + var1.m22 * var2.m22 + var1.m23 * var2.m23;
         double var25 = var1.m20 * var2.m30 + var1.m21 * var2.m31 + var1.m22 * var2.m32 + var1.m23 * var2.m33;
         double var27 = var1.m30 * var2.m00 + var1.m31 * var2.m01 + var1.m32 * var2.m02 + var1.m33 * var2.m03;
         double var29 = var1.m30 * var2.m10 + var1.m31 * var2.m11 + var1.m32 * var2.m12 + var1.m33 * var2.m13;
         double var31 = var1.m30 * var2.m20 + var1.m31 * var2.m21 + var1.m32 * var2.m22 + var1.m33 * var2.m23;
         double var33 = var1.m30 * var2.m30 + var1.m31 * var2.m31 + var1.m32 * var2.m32 + var1.m33 * var2.m33;
         this.m00 = var3;
         this.m01 = var5;
         this.m02 = var7;
         this.m03 = var9;
         this.m10 = var11;
         this.m11 = var13;
         this.m12 = var15;
         this.m13 = var17;
         this.m20 = var19;
         this.m21 = var21;
         this.m22 = var23;
         this.m23 = var25;
         this.m30 = var27;
         this.m31 = var29;
         this.m32 = var31;
         this.m33 = var33;
      }

   }

   public final void mulTransposeLeft(Matrix4d var1, Matrix4d var2) {
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
         double var3 = var1.m00 * var2.m00 + var1.m10 * var2.m10 + var1.m20 * var2.m20 + var1.m30 * var2.m30;
         double var5 = var1.m00 * var2.m01 + var1.m10 * var2.m11 + var1.m20 * var2.m21 + var1.m30 * var2.m31;
         double var7 = var1.m00 * var2.m02 + var1.m10 * var2.m12 + var1.m20 * var2.m22 + var1.m30 * var2.m32;
         double var9 = var1.m00 * var2.m03 + var1.m10 * var2.m13 + var1.m20 * var2.m23 + var1.m30 * var2.m33;
         double var11 = var1.m01 * var2.m00 + var1.m11 * var2.m10 + var1.m21 * var2.m20 + var1.m31 * var2.m30;
         double var13 = var1.m01 * var2.m01 + var1.m11 * var2.m11 + var1.m21 * var2.m21 + var1.m31 * var2.m31;
         double var15 = var1.m01 * var2.m02 + var1.m11 * var2.m12 + var1.m21 * var2.m22 + var1.m31 * var2.m32;
         double var17 = var1.m01 * var2.m03 + var1.m11 * var2.m13 + var1.m21 * var2.m23 + var1.m31 * var2.m33;
         double var19 = var1.m02 * var2.m00 + var1.m12 * var2.m10 + var1.m22 * var2.m20 + var1.m32 * var2.m30;
         double var21 = var1.m02 * var2.m01 + var1.m12 * var2.m11 + var1.m22 * var2.m21 + var1.m32 * var2.m31;
         double var23 = var1.m02 * var2.m02 + var1.m12 * var2.m12 + var1.m22 * var2.m22 + var1.m32 * var2.m32;
         double var25 = var1.m02 * var2.m03 + var1.m12 * var2.m13 + var1.m22 * var2.m23 + var1.m32 * var2.m33;
         double var27 = var1.m03 * var2.m00 + var1.m13 * var2.m10 + var1.m23 * var2.m20 + var1.m33 * var2.m30;
         double var29 = var1.m03 * var2.m01 + var1.m13 * var2.m11 + var1.m23 * var2.m21 + var1.m33 * var2.m31;
         double var31 = var1.m03 * var2.m02 + var1.m13 * var2.m12 + var1.m23 * var2.m22 + var1.m33 * var2.m32;
         double var33 = var1.m03 * var2.m03 + var1.m13 * var2.m13 + var1.m23 * var2.m23 + var1.m33 * var2.m33;
         this.m00 = var3;
         this.m01 = var5;
         this.m02 = var7;
         this.m03 = var9;
         this.m10 = var11;
         this.m11 = var13;
         this.m12 = var15;
         this.m13 = var17;
         this.m20 = var19;
         this.m21 = var21;
         this.m22 = var23;
         this.m23 = var25;
         this.m30 = var27;
         this.m31 = var29;
         this.m32 = var31;
         this.m33 = var33;
      }

   }

   public boolean equals(Matrix4d var1) {
      try {
         return this.m00 == var1.m00 && this.m01 == var1.m01 && this.m02 == var1.m02 && this.m03 == var1.m03 && this.m10 == var1.m10 && this.m11 == var1.m11 && this.m12 == var1.m12 && this.m13 == var1.m13 && this.m20 == var1.m20 && this.m21 == var1.m21 && this.m22 == var1.m22 && this.m23 == var1.m23 && this.m30 == var1.m30 && this.m31 == var1.m31 && this.m32 == var1.m32 && this.m33 == var1.m33;
      } catch (NullPointerException var3) {
         return false;
      }
   }

   public boolean equals(Object var1) {
      try {
         Matrix4d var2 = (Matrix4d)var1;
         return this.m00 == var2.m00 && this.m01 == var2.m01 && this.m02 == var2.m02 && this.m03 == var2.m03 && this.m10 == var2.m10 && this.m11 == var2.m11 && this.m12 == var2.m12 && this.m13 == var2.m13 && this.m20 == var2.m20 && this.m21 == var2.m21 && this.m22 == var2.m22 && this.m23 == var2.m23 && this.m30 == var2.m30 && this.m31 == var2.m31 && this.m32 == var2.m32 && this.m33 == var2.m33;
      } catch (ClassCastException var3) {
         return false;
      } catch (NullPointerException var4) {
         return false;
      }
   }

   /** @deprecated */
   public boolean epsilonEquals(Matrix4d var1, float var2) {
      return this.epsilonEquals(var1, (double)var2);
   }

   public boolean epsilonEquals(Matrix4d var1, double var2) {
      double var4 = this.m00 - var1.m00;
      if ((var4 < 0.0D ? -var4 : var4) > var2) {
         return false;
      } else {
         var4 = this.m01 - var1.m01;
         if ((var4 < 0.0D ? -var4 : var4) > var2) {
            return false;
         } else {
            var4 = this.m02 - var1.m02;
            if ((var4 < 0.0D ? -var4 : var4) > var2) {
               return false;
            } else {
               var4 = this.m03 - var1.m03;
               if ((var4 < 0.0D ? -var4 : var4) > var2) {
                  return false;
               } else {
                  var4 = this.m10 - var1.m10;
                  if ((var4 < 0.0D ? -var4 : var4) > var2) {
                     return false;
                  } else {
                     var4 = this.m11 - var1.m11;
                     if ((var4 < 0.0D ? -var4 : var4) > var2) {
                        return false;
                     } else {
                        var4 = this.m12 - var1.m12;
                        if ((var4 < 0.0D ? -var4 : var4) > var2) {
                           return false;
                        } else {
                           var4 = this.m13 - var1.m13;
                           if ((var4 < 0.0D ? -var4 : var4) > var2) {
                              return false;
                           } else {
                              var4 = this.m20 - var1.m20;
                              if ((var4 < 0.0D ? -var4 : var4) > var2) {
                                 return false;
                              } else {
                                 var4 = this.m21 - var1.m21;
                                 if ((var4 < 0.0D ? -var4 : var4) > var2) {
                                    return false;
                                 } else {
                                    var4 = this.m22 - var1.m22;
                                    if ((var4 < 0.0D ? -var4 : var4) > var2) {
                                       return false;
                                    } else {
                                       var4 = this.m23 - var1.m23;
                                       if ((var4 < 0.0D ? -var4 : var4) > var2) {
                                          return false;
                                       } else {
                                          var4 = this.m30 - var1.m30;
                                          if ((var4 < 0.0D ? -var4 : var4) > var2) {
                                             return false;
                                          } else {
                                             var4 = this.m31 - var1.m31;
                                             if ((var4 < 0.0D ? -var4 : var4) > var2) {
                                                return false;
                                             } else {
                                                var4 = this.m32 - var1.m32;
                                                if ((var4 < 0.0D ? -var4 : var4) > var2) {
                                                   return false;
                                                } else {
                                                   var4 = this.m33 - var1.m33;
                                                   return (var4 < 0.0D ? -var4 : var4) <= var2;
                                                }
                                             }
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public int hashCode() {
      long var1 = 1L;
      var1 = 31L * var1 + VecMathUtil.doubleToLongBits(this.m00);
      var1 = 31L * var1 + VecMathUtil.doubleToLongBits(this.m01);
      var1 = 31L * var1 + VecMathUtil.doubleToLongBits(this.m02);
      var1 = 31L * var1 + VecMathUtil.doubleToLongBits(this.m03);
      var1 = 31L * var1 + VecMathUtil.doubleToLongBits(this.m10);
      var1 = 31L * var1 + VecMathUtil.doubleToLongBits(this.m11);
      var1 = 31L * var1 + VecMathUtil.doubleToLongBits(this.m12);
      var1 = 31L * var1 + VecMathUtil.doubleToLongBits(this.m13);
      var1 = 31L * var1 + VecMathUtil.doubleToLongBits(this.m20);
      var1 = 31L * var1 + VecMathUtil.doubleToLongBits(this.m21);
      var1 = 31L * var1 + VecMathUtil.doubleToLongBits(this.m22);
      var1 = 31L * var1 + VecMathUtil.doubleToLongBits(this.m23);
      var1 = 31L * var1 + VecMathUtil.doubleToLongBits(this.m30);
      var1 = 31L * var1 + VecMathUtil.doubleToLongBits(this.m31);
      var1 = 31L * var1 + VecMathUtil.doubleToLongBits(this.m32);
      var1 = 31L * var1 + VecMathUtil.doubleToLongBits(this.m33);
      return (int)(var1 ^ var1 >> 32);
   }

   public final void transform(Tuple4d var1, Tuple4d var2) {
      double var3 = this.m00 * var1.x + this.m01 * var1.y + this.m02 * var1.z + this.m03 * var1.w;
      double var5 = this.m10 * var1.x + this.m11 * var1.y + this.m12 * var1.z + this.m13 * var1.w;
      double var7 = this.m20 * var1.x + this.m21 * var1.y + this.m22 * var1.z + this.m23 * var1.w;
      var2.w = this.m30 * var1.x + this.m31 * var1.y + this.m32 * var1.z + this.m33 * var1.w;
      var2.x = var3;
      var2.y = var5;
      var2.z = var7;
   }

   public final void transform(Tuple4d var1) {
      double var2 = this.m00 * var1.x + this.m01 * var1.y + this.m02 * var1.z + this.m03 * var1.w;
      double var4 = this.m10 * var1.x + this.m11 * var1.y + this.m12 * var1.z + this.m13 * var1.w;
      double var6 = this.m20 * var1.x + this.m21 * var1.y + this.m22 * var1.z + this.m23 * var1.w;
      var1.w = this.m30 * var1.x + this.m31 * var1.y + this.m32 * var1.z + this.m33 * var1.w;
      var1.x = var2;
      var1.y = var4;
      var1.z = var6;
   }

   public final void transform(Tuple4f var1, Tuple4f var2) {
      float var3 = (float)(this.m00 * (double)var1.x + this.m01 * (double)var1.y + this.m02 * (double)var1.z + this.m03 * (double)var1.w);
      float var4 = (float)(this.m10 * (double)var1.x + this.m11 * (double)var1.y + this.m12 * (double)var1.z + this.m13 * (double)var1.w);
      float var5 = (float)(this.m20 * (double)var1.x + this.m21 * (double)var1.y + this.m22 * (double)var1.z + this.m23 * (double)var1.w);
      var2.w = (float)(this.m30 * (double)var1.x + this.m31 * (double)var1.y + this.m32 * (double)var1.z + this.m33 * (double)var1.w);
      var2.x = var3;
      var2.y = var4;
      var2.z = var5;
   }

   public final void transform(Tuple4f var1) {
      float var2 = (float)(this.m00 * (double)var1.x + this.m01 * (double)var1.y + this.m02 * (double)var1.z + this.m03 * (double)var1.w);
      float var3 = (float)(this.m10 * (double)var1.x + this.m11 * (double)var1.y + this.m12 * (double)var1.z + this.m13 * (double)var1.w);
      float var4 = (float)(this.m20 * (double)var1.x + this.m21 * (double)var1.y + this.m22 * (double)var1.z + this.m23 * (double)var1.w);
      var1.w = (float)(this.m30 * (double)var1.x + this.m31 * (double)var1.y + this.m32 * (double)var1.z + this.m33 * (double)var1.w);
      var1.x = var2;
      var1.y = var3;
      var1.z = var4;
   }

   public final void transform(Point3d var1, Point3d var2) {
      double var3 = this.m00 * var1.x + this.m01 * var1.y + this.m02 * var1.z + this.m03;
      double var5 = this.m10 * var1.x + this.m11 * var1.y + this.m12 * var1.z + this.m13;
      var2.z = this.m20 * var1.x + this.m21 * var1.y + this.m22 * var1.z + this.m23;
      var2.x = var3;
      var2.y = var5;
   }

   public final void transform(Point3d var1) {
      double var2 = this.m00 * var1.x + this.m01 * var1.y + this.m02 * var1.z + this.m03;
      double var4 = this.m10 * var1.x + this.m11 * var1.y + this.m12 * var1.z + this.m13;
      var1.z = this.m20 * var1.x + this.m21 * var1.y + this.m22 * var1.z + this.m23;
      var1.x = var2;
      var1.y = var4;
   }

   public final void transform(Point3f var1, Point3f var2) {
      float var3 = (float)(this.m00 * (double)var1.x + this.m01 * (double)var1.y + this.m02 * (double)var1.z + this.m03);
      float var4 = (float)(this.m10 * (double)var1.x + this.m11 * (double)var1.y + this.m12 * (double)var1.z + this.m13);
      var2.z = (float)(this.m20 * (double)var1.x + this.m21 * (double)var1.y + this.m22 * (double)var1.z + this.m23);
      var2.x = var3;
      var2.y = var4;
   }

   public final void transform(Point3f var1) {
      float var2 = (float)(this.m00 * (double)var1.x + this.m01 * (double)var1.y + this.m02 * (double)var1.z + this.m03);
      float var3 = (float)(this.m10 * (double)var1.x + this.m11 * (double)var1.y + this.m12 * (double)var1.z + this.m13);
      var1.z = (float)(this.m20 * (double)var1.x + this.m21 * (double)var1.y + this.m22 * (double)var1.z + this.m23);
      var1.x = var2;
      var1.y = var3;
   }

   public final void transform(Vector3d var1, Vector3d var2) {
      double var3 = this.m00 * var1.x + this.m01 * var1.y + this.m02 * var1.z;
      double var5 = this.m10 * var1.x + this.m11 * var1.y + this.m12 * var1.z;
      var2.z = this.m20 * var1.x + this.m21 * var1.y + this.m22 * var1.z;
      var2.x = var3;
      var2.y = var5;
   }

   public final void transform(Vector3d var1) {
      double var2 = this.m00 * var1.x + this.m01 * var1.y + this.m02 * var1.z;
      double var4 = this.m10 * var1.x + this.m11 * var1.y + this.m12 * var1.z;
      var1.z = this.m20 * var1.x + this.m21 * var1.y + this.m22 * var1.z;
      var1.x = var2;
      var1.y = var4;
   }

   public final void transform(Vector3f var1, Vector3f var2) {
      float var3 = (float)(this.m00 * (double)var1.x + this.m01 * (double)var1.y + this.m02 * (double)var1.z);
      float var4 = (float)(this.m10 * (double)var1.x + this.m11 * (double)var1.y + this.m12 * (double)var1.z);
      var2.z = (float)(this.m20 * (double)var1.x + this.m21 * (double)var1.y + this.m22 * (double)var1.z);
      var2.x = var3;
      var2.y = var4;
   }

   public final void transform(Vector3f var1) {
      float var2 = (float)(this.m00 * (double)var1.x + this.m01 * (double)var1.y + this.m02 * (double)var1.z);
      float var3 = (float)(this.m10 * (double)var1.x + this.m11 * (double)var1.y + this.m12 * (double)var1.z);
      var1.z = (float)(this.m20 * (double)var1.x + this.m21 * (double)var1.y + this.m22 * (double)var1.z);
      var1.x = var2;
      var1.y = var3;
   }

   public final void setRotation(Matrix3d var1) {
      double[] var2 = new double[9];
      double[] var3 = new double[3];
      this.getScaleRotate(var3, var2);
      this.m00 = var1.m00 * var3[0];
      this.m01 = var1.m01 * var3[1];
      this.m02 = var1.m02 * var3[2];
      this.m10 = var1.m10 * var3[0];
      this.m11 = var1.m11 * var3[1];
      this.m12 = var1.m12 * var3[2];
      this.m20 = var1.m20 * var3[0];
      this.m21 = var1.m21 * var3[1];
      this.m22 = var1.m22 * var3[2];
   }

   public final void setRotation(Matrix3f var1) {
      double[] var2 = new double[9];
      double[] var3 = new double[3];
      this.getScaleRotate(var3, var2);
      this.m00 = (double)var1.m00 * var3[0];
      this.m01 = (double)var1.m01 * var3[1];
      this.m02 = (double)var1.m02 * var3[2];
      this.m10 = (double)var1.m10 * var3[0];
      this.m11 = (double)var1.m11 * var3[1];
      this.m12 = (double)var1.m12 * var3[2];
      this.m20 = (double)var1.m20 * var3[0];
      this.m21 = (double)var1.m21 * var3[1];
      this.m22 = (double)var1.m22 * var3[2];
   }

   public final void setRotation(Quat4f var1) {
      double[] var2 = new double[9];
      double[] var3 = new double[3];
      this.getScaleRotate(var3, var2);
      this.m00 = (1.0D - (double)(2.0F * var1.y * var1.y) - (double)(2.0F * var1.z * var1.z)) * var3[0];
      this.m10 = 2.0D * (double)(var1.x * var1.y + var1.w * var1.z) * var3[0];
      this.m20 = 2.0D * (double)(var1.x * var1.z - var1.w * var1.y) * var3[0];
      this.m01 = 2.0D * (double)(var1.x * var1.y - var1.w * var1.z) * var3[1];
      this.m11 = (1.0D - (double)(2.0F * var1.x * var1.x) - (double)(2.0F * var1.z * var1.z)) * var3[1];
      this.m21 = 2.0D * (double)(var1.y * var1.z + var1.w * var1.x) * var3[1];
      this.m02 = 2.0D * (double)(var1.x * var1.z + var1.w * var1.y) * var3[2];
      this.m12 = 2.0D * (double)(var1.y * var1.z - var1.w * var1.x) * var3[2];
      this.m22 = (1.0D - (double)(2.0F * var1.x * var1.x) - (double)(2.0F * var1.y * var1.y)) * var3[2];
   }

   public final void setRotation(Quat4d var1) {
      double[] var2 = new double[9];
      double[] var3 = new double[3];
      this.getScaleRotate(var3, var2);
      this.m00 = (1.0D - 2.0D * var1.y * var1.y - 2.0D * var1.z * var1.z) * var3[0];
      this.m10 = 2.0D * (var1.x * var1.y + var1.w * var1.z) * var3[0];
      this.m20 = 2.0D * (var1.x * var1.z - var1.w * var1.y) * var3[0];
      this.m01 = 2.0D * (var1.x * var1.y - var1.w * var1.z) * var3[1];
      this.m11 = (1.0D - 2.0D * var1.x * var1.x - 2.0D * var1.z * var1.z) * var3[1];
      this.m21 = 2.0D * (var1.y * var1.z + var1.w * var1.x) * var3[1];
      this.m02 = 2.0D * (var1.x * var1.z + var1.w * var1.y) * var3[2];
      this.m12 = 2.0D * (var1.y * var1.z - var1.w * var1.x) * var3[2];
      this.m22 = (1.0D - 2.0D * var1.x * var1.x - 2.0D * var1.y * var1.y) * var3[2];
   }

   public final void setRotation(AxisAngle4d var1) {
      double[] var2 = new double[9];
      double[] var3 = new double[3];
      this.getScaleRotate(var3, var2);
      double var4 = 1.0D / Math.sqrt(var1.x * var1.x + var1.y * var1.y + var1.z * var1.z);
      double var6 = var1.x * var4;
      double var8 = var1.y * var4;
      double var10 = var1.z * var4;
      double var12 = Math.sin(var1.angle);
      double var14 = Math.cos(var1.angle);
      double var16 = 1.0D - var14;
      double var18 = var1.x * var1.z;
      double var20 = var1.x * var1.y;
      double var22 = var1.y * var1.z;
      this.m00 = (var16 * var6 * var6 + var14) * var3[0];
      this.m01 = (var16 * var20 - var12 * var10) * var3[1];
      this.m02 = (var16 * var18 + var12 * var8) * var3[2];
      this.m10 = (var16 * var20 + var12 * var10) * var3[0];
      this.m11 = (var16 * var8 * var8 + var14) * var3[1];
      this.m12 = (var16 * var22 - var12 * var6) * var3[2];
      this.m20 = (var16 * var18 - var12 * var8) * var3[0];
      this.m21 = (var16 * var22 + var12 * var6) * var3[1];
      this.m22 = (var16 * var10 * var10 + var14) * var3[2];
   }

   public final void setZero() {
      this.m00 = 0.0D;
      this.m01 = 0.0D;
      this.m02 = 0.0D;
      this.m03 = 0.0D;
      this.m10 = 0.0D;
      this.m11 = 0.0D;
      this.m12 = 0.0D;
      this.m13 = 0.0D;
      this.m20 = 0.0D;
      this.m21 = 0.0D;
      this.m22 = 0.0D;
      this.m23 = 0.0D;
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.m33 = 0.0D;
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

   public final void negate(Matrix4d var1) {
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
      double[] var3 = new double[]{this.m00, this.m01, this.m02, this.m10, this.m11, this.m12, this.m20, this.m21, this.m22};
      Matrix3d.compute_svd(var3, var1, var2);
   }

   public Object clone() {
      Matrix4d var1 = null;

      try {
         var1 = (Matrix4d)super.clone();
         return var1;
      } catch (CloneNotSupportedException var3) {
         throw new InternalError();
      }
   }
}

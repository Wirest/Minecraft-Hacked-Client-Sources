package javax.vecmath;

import java.io.Serializable;

public class Matrix3f implements Serializable, Cloneable {
   static final long serialVersionUID = 329697160112089834L;
   public float m00;
   public float m01;
   public float m02;
   public float m10;
   public float m11;
   public float m12;
   public float m20;
   public float m21;
   public float m22;
   private static final double EPS = 1.0E-8D;

   public Matrix3f(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      this.m00 = var1;
      this.m01 = var2;
      this.m02 = var3;
      this.m10 = var4;
      this.m11 = var5;
      this.m12 = var6;
      this.m20 = var7;
      this.m21 = var8;
      this.m22 = var9;
   }

   public Matrix3f(float[] var1) {
      this.m00 = var1[0];
      this.m01 = var1[1];
      this.m02 = var1[2];
      this.m10 = var1[3];
      this.m11 = var1[4];
      this.m12 = var1[5];
      this.m20 = var1[6];
      this.m21 = var1[7];
      this.m22 = var1[8];
   }

   public Matrix3f(Matrix3d var1) {
      this.m00 = (float)var1.m00;
      this.m01 = (float)var1.m01;
      this.m02 = (float)var1.m02;
      this.m10 = (float)var1.m10;
      this.m11 = (float)var1.m11;
      this.m12 = (float)var1.m12;
      this.m20 = (float)var1.m20;
      this.m21 = (float)var1.m21;
      this.m22 = (float)var1.m22;
   }

   public Matrix3f(Matrix3f var1) {
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

   public Matrix3f() {
      this.m00 = 0.0F;
      this.m01 = 0.0F;
      this.m02 = 0.0F;
      this.m10 = 0.0F;
      this.m11 = 0.0F;
      this.m12 = 0.0F;
      this.m20 = 0.0F;
      this.m21 = 0.0F;
      this.m22 = 0.0F;
   }

   public String toString() {
      return this.m00 + ", " + this.m01 + ", " + this.m02 + "\n" + this.m10 + ", " + this.m11 + ", " + this.m12 + "\n" + this.m20 + ", " + this.m21 + ", " + this.m22 + "\n";
   }

   public final void setIdentity() {
      this.m00 = 1.0F;
      this.m01 = 0.0F;
      this.m02 = 0.0F;
      this.m10 = 0.0F;
      this.m11 = 1.0F;
      this.m12 = 0.0F;
      this.m20 = 0.0F;
      this.m21 = 0.0F;
      this.m22 = 1.0F;
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
         default:
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f0"));
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
         default:
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f0"));
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
         default:
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f0"));
         }
      default:
         throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f0"));
      }
   }

   public final void getRow(int var1, Vector3f var2) {
      if (var1 == 0) {
         var2.x = this.m00;
         var2.y = this.m01;
         var2.z = this.m02;
      } else if (var1 == 1) {
         var2.x = this.m10;
         var2.y = this.m11;
         var2.z = this.m12;
      } else {
         if (var1 != 2) {
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f1"));
         }

         var2.x = this.m20;
         var2.y = this.m21;
         var2.z = this.m22;
      }

   }

   public final void getRow(int var1, float[] var2) {
      if (var1 == 0) {
         var2[0] = this.m00;
         var2[1] = this.m01;
         var2[2] = this.m02;
      } else if (var1 == 1) {
         var2[0] = this.m10;
         var2[1] = this.m11;
         var2[2] = this.m12;
      } else {
         if (var1 != 2) {
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f1"));
         }

         var2[0] = this.m20;
         var2[1] = this.m21;
         var2[2] = this.m22;
      }

   }

   public final void getColumn(int var1, Vector3f var2) {
      if (var1 == 0) {
         var2.x = this.m00;
         var2.y = this.m10;
         var2.z = this.m20;
      } else if (var1 == 1) {
         var2.x = this.m01;
         var2.y = this.m11;
         var2.z = this.m21;
      } else {
         if (var1 != 2) {
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f3"));
         }

         var2.x = this.m02;
         var2.y = this.m12;
         var2.z = this.m22;
      }

   }

   public final void getColumn(int var1, float[] var2) {
      if (var1 == 0) {
         var2[0] = this.m00;
         var2[1] = this.m10;
         var2[2] = this.m20;
      } else if (var1 == 1) {
         var2[0] = this.m01;
         var2[1] = this.m11;
         var2[2] = this.m21;
      } else {
         if (var1 != 2) {
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f3"));
         }

         var2[0] = this.m02;
         var2[1] = this.m12;
         var2[2] = this.m22;
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
         default:
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f5"));
         }
      case 1:
         switch(var2) {
         case 0:
            return this.m10;
         case 1:
            return this.m11;
         case 2:
            return this.m12;
         default:
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f5"));
         }
      case 2:
         switch(var2) {
         case 0:
            return this.m20;
         case 1:
            return this.m21;
         case 2:
            return this.m22;
         }
      }

      throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f5"));
   }

   public final void setRow(int var1, float var2, float var3, float var4) {
      switch(var1) {
      case 0:
         this.m00 = var2;
         this.m01 = var3;
         this.m02 = var4;
         break;
      case 1:
         this.m10 = var2;
         this.m11 = var3;
         this.m12 = var4;
         break;
      case 2:
         this.m20 = var2;
         this.m21 = var3;
         this.m22 = var4;
         break;
      default:
         throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f6"));
      }

   }

   public final void setRow(int var1, Vector3f var2) {
      switch(var1) {
      case 0:
         this.m00 = var2.x;
         this.m01 = var2.y;
         this.m02 = var2.z;
         break;
      case 1:
         this.m10 = var2.x;
         this.m11 = var2.y;
         this.m12 = var2.z;
         break;
      case 2:
         this.m20 = var2.x;
         this.m21 = var2.y;
         this.m22 = var2.z;
         break;
      default:
         throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f6"));
      }

   }

   public final void setRow(int var1, float[] var2) {
      switch(var1) {
      case 0:
         this.m00 = var2[0];
         this.m01 = var2[1];
         this.m02 = var2[2];
         break;
      case 1:
         this.m10 = var2[0];
         this.m11 = var2[1];
         this.m12 = var2[2];
         break;
      case 2:
         this.m20 = var2[0];
         this.m21 = var2[1];
         this.m22 = var2[2];
         break;
      default:
         throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f6"));
      }

   }

   public final void setColumn(int var1, float var2, float var3, float var4) {
      switch(var1) {
      case 0:
         this.m00 = var2;
         this.m10 = var3;
         this.m20 = var4;
         break;
      case 1:
         this.m01 = var2;
         this.m11 = var3;
         this.m21 = var4;
         break;
      case 2:
         this.m02 = var2;
         this.m12 = var3;
         this.m22 = var4;
         break;
      default:
         throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f9"));
      }

   }

   public final void setColumn(int var1, Vector3f var2) {
      switch(var1) {
      case 0:
         this.m00 = var2.x;
         this.m10 = var2.y;
         this.m20 = var2.z;
         break;
      case 1:
         this.m01 = var2.x;
         this.m11 = var2.y;
         this.m21 = var2.z;
         break;
      case 2:
         this.m02 = var2.x;
         this.m12 = var2.y;
         this.m22 = var2.z;
         break;
      default:
         throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f9"));
      }

   }

   public final void setColumn(int var1, float[] var2) {
      switch(var1) {
      case 0:
         this.m00 = var2[0];
         this.m10 = var2[1];
         this.m20 = var2[2];
         break;
      case 1:
         this.m01 = var2[0];
         this.m11 = var2[1];
         this.m21 = var2[2];
         break;
      case 2:
         this.m02 = var2[0];
         this.m12 = var2[1];
         this.m22 = var2[2];
         break;
      default:
         throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f9"));
      }

   }

   public final float getScale() {
      double[] var1 = new double[9];
      double[] var2 = new double[3];
      this.getScaleRotate(var2, var1);
      return (float)Matrix3d.max3(var2);
   }

   public final void add(float var1) {
      this.m00 += var1;
      this.m01 += var1;
      this.m02 += var1;
      this.m10 += var1;
      this.m11 += var1;
      this.m12 += var1;
      this.m20 += var1;
      this.m21 += var1;
      this.m22 += var1;
   }

   public final void add(float var1, Matrix3f var2) {
      this.m00 = var2.m00 + var1;
      this.m01 = var2.m01 + var1;
      this.m02 = var2.m02 + var1;
      this.m10 = var2.m10 + var1;
      this.m11 = var2.m11 + var1;
      this.m12 = var2.m12 + var1;
      this.m20 = var2.m20 + var1;
      this.m21 = var2.m21 + var1;
      this.m22 = var2.m22 + var1;
   }

   public final void add(Matrix3f var1, Matrix3f var2) {
      this.m00 = var1.m00 + var2.m00;
      this.m01 = var1.m01 + var2.m01;
      this.m02 = var1.m02 + var2.m02;
      this.m10 = var1.m10 + var2.m10;
      this.m11 = var1.m11 + var2.m11;
      this.m12 = var1.m12 + var2.m12;
      this.m20 = var1.m20 + var2.m20;
      this.m21 = var1.m21 + var2.m21;
      this.m22 = var1.m22 + var2.m22;
   }

   public final void add(Matrix3f var1) {
      this.m00 += var1.m00;
      this.m01 += var1.m01;
      this.m02 += var1.m02;
      this.m10 += var1.m10;
      this.m11 += var1.m11;
      this.m12 += var1.m12;
      this.m20 += var1.m20;
      this.m21 += var1.m21;
      this.m22 += var1.m22;
   }

   public final void sub(Matrix3f var1, Matrix3f var2) {
      this.m00 = var1.m00 - var2.m00;
      this.m01 = var1.m01 - var2.m01;
      this.m02 = var1.m02 - var2.m02;
      this.m10 = var1.m10 - var2.m10;
      this.m11 = var1.m11 - var2.m11;
      this.m12 = var1.m12 - var2.m12;
      this.m20 = var1.m20 - var2.m20;
      this.m21 = var1.m21 - var2.m21;
      this.m22 = var1.m22 - var2.m22;
   }

   public final void sub(Matrix3f var1) {
      this.m00 -= var1.m00;
      this.m01 -= var1.m01;
      this.m02 -= var1.m02;
      this.m10 -= var1.m10;
      this.m11 -= var1.m11;
      this.m12 -= var1.m12;
      this.m20 -= var1.m20;
      this.m21 -= var1.m21;
      this.m22 -= var1.m22;
   }

   public final void transpose() {
      float var1 = this.m10;
      this.m10 = this.m01;
      this.m01 = var1;
      var1 = this.m20;
      this.m20 = this.m02;
      this.m02 = var1;
      var1 = this.m21;
      this.m21 = this.m12;
      this.m12 = var1;
   }

   public final void transpose(Matrix3f var1) {
      if (this != var1) {
         this.m00 = var1.m00;
         this.m01 = var1.m10;
         this.m02 = var1.m20;
         this.m10 = var1.m01;
         this.m11 = var1.m11;
         this.m12 = var1.m21;
         this.m20 = var1.m02;
         this.m21 = var1.m12;
         this.m22 = var1.m22;
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
         double var10 = Math.sin(var1.angle);
         double var12 = Math.cos(var1.angle);
         double var14 = 1.0D - var12;
         double var16 = var4 * var8;
         double var18 = var4 * var6;
         double var20 = var6 * var8;
         this.m00 = (float)(var14 * var4 * var4 + var12);
         this.m01 = (float)(var14 * var18 - var10 * var8);
         this.m02 = (float)(var14 * var16 + var10 * var6);
         this.m10 = (float)(var14 * var18 + var10 * var8);
         this.m11 = (float)(var14 * var6 * var6 + var12);
         this.m12 = (float)(var14 * var20 - var10 * var4);
         this.m20 = (float)(var14 * var16 - var10 * var6);
         this.m21 = (float)(var14 * var20 + var10 * var4);
         this.m22 = (float)(var14 * var8 * var8 + var12);
      }

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
   }

   public final void set(float[] var1) {
      this.m00 = var1[0];
      this.m01 = var1[1];
      this.m02 = var1[2];
      this.m10 = var1[3];
      this.m11 = var1[4];
      this.m12 = var1[5];
      this.m20 = var1[6];
      this.m21 = var1[7];
      this.m22 = var1[8];
   }

   public final void set(Matrix3f var1) {
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

   public final void set(Matrix3d var1) {
      this.m00 = (float)var1.m00;
      this.m01 = (float)var1.m01;
      this.m02 = (float)var1.m02;
      this.m10 = (float)var1.m10;
      this.m11 = (float)var1.m11;
      this.m12 = (float)var1.m12;
      this.m20 = (float)var1.m20;
      this.m21 = (float)var1.m21;
      this.m22 = (float)var1.m22;
   }

   public final void invert(Matrix3f var1) {
      this.invertGeneral(var1);
   }

   public final void invert() {
      this.invertGeneral(this);
   }

   private final void invertGeneral(Matrix3f var1) {
      double[] var2 = new double[9];
      double[] var3 = new double[9];
      int[] var4 = new int[3];
      var2[0] = (double)var1.m00;
      var2[1] = (double)var1.m01;
      var2[2] = (double)var1.m02;
      var2[3] = (double)var1.m10;
      var2[4] = (double)var1.m11;
      var2[5] = (double)var1.m12;
      var2[6] = (double)var1.m20;
      var2[7] = (double)var1.m21;
      var2[8] = (double)var1.m22;
      if (!luDecomposition(var2, var4)) {
         throw new SingularMatrixException(VecMathI18N.getString("Matrix3f12"));
      } else {
         for(int var5 = 0; var5 < 9; ++var5) {
            var3[var5] = 0.0D;
         }

         var3[0] = 1.0D;
         var3[4] = 1.0D;
         var3[8] = 1.0D;
         luBacksubstitution(var2, var4, var3);
         this.m00 = (float)var3[0];
         this.m01 = (float)var3[1];
         this.m02 = (float)var3[2];
         this.m10 = (float)var3[3];
         this.m11 = (float)var3[4];
         this.m12 = (float)var3[5];
         this.m20 = (float)var3[6];
         this.m21 = (float)var3[7];
         this.m22 = (float)var3[8];
      }
   }

   static boolean luDecomposition(double[] var0, int[] var1) {
      double[] var2 = new double[3];
      int var5 = 0;
      int var6 = 0;

      int var3;
      double var7;
      for(var3 = 3; var3-- != 0; var2[var6++] = 1.0D / var7) {
         var7 = 0.0D;
         int var4 = 3;

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

      for(var3 = 0; var3 < 3; ++var3) {
         int var8;
         int var10;
         double var11;
         int var18;
         int var19;
         for(var5 = 0; var5 < var3; ++var5) {
            var8 = var17 + 3 * var5 + var3;
            var11 = var0[var8];
            var18 = var5;
            var19 = var17 + 3 * var5;

            for(var10 = var17 + var3; var18-- != 0; var10 += 3) {
               var11 -= var0[var19] * var0[var10];
               ++var19;
            }

            var0[var8] = var11;
         }

         double var13 = 0.0D;
         var6 = -1;

         double var15;
         for(var5 = var3; var5 < 3; ++var5) {
            var8 = var17 + 3 * var5 + var3;
            var11 = var0[var8];
            var18 = var3;
            var19 = var17 + 3 * var5;

            for(var10 = var17 + var3; var18-- != 0; var10 += 3) {
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
            throw new RuntimeException(VecMathI18N.getString("Matrix3f13"));
         }

         if (var3 != var6) {
            var18 = 3;
            var19 = var17 + 3 * var6;

            for(var10 = var17 + 3 * var3; var18-- != 0; var0[var10++] = var15) {
               var15 = var0[var19];
               var0[var19++] = var0[var10];
            }

            var2[var6] = var2[var3];
         }

         var1[var3] = var6;
         if (var0[var17 + 3 * var3 + var3] == 0.0D) {
            return false;
         }

         if (var3 != 2) {
            var15 = 1.0D / var0[var17 + 3 * var3 + var3];
            var8 = var17 + 3 * (var3 + 1) + var3;

            for(var5 = 2 - var3; var5-- != 0; var8 += 3) {
               var0[var8] *= var15;
            }
         }
      }

      return true;
   }

   static void luBacksubstitution(double[] var0, int[] var1, double[] var2) {
      byte var8 = 0;

      for(int var7 = 0; var7 < 3; ++var7) {
         int var9 = var7;
         int var4 = -1;

         int var10;
         for(int var3 = 0; var3 < 3; ++var3) {
            int var5 = var1[var8 + var3];
            double var11 = var2[var9 + 3 * var5];
            var2[var9 + 3 * var5] = var2[var9 + 3 * var3];
            if (var4 >= 0) {
               var10 = var3 * 3;

               for(int var6 = var4; var6 <= var3 - 1; ++var6) {
                  var11 -= var0[var10 + var6] * var2[var9 + 3 * var6];
               }
            } else if (var11 != 0.0D) {
               var4 = var3;
            }

            var2[var9 + 3 * var3] = var11;
         }

         byte var13 = 6;
         var2[var9 + 6] /= var0[var13 + 2];
         var10 = var13 - 3;
         var2[var9 + 3] = (var2[var9 + 3] - var0[var10 + 2] * var2[var9 + 6]) / var0[var10 + 1];
         var10 -= 3;
         var2[var9 + 0] = (var2[var9 + 0] - var0[var10 + 1] * var2[var9 + 3] - var0[var10 + 2] * var2[var9 + 6]) / var0[var10 + 0];
      }

   }

   public final float determinant() {
      float var1 = this.m00 * (this.m11 * this.m22 - this.m12 * this.m21) + this.m01 * (this.m12 * this.m20 - this.m10 * this.m22) + this.m02 * (this.m10 * this.m21 - this.m11 * this.m20);
      return var1;
   }

   public final void set(float var1) {
      this.m00 = var1;
      this.m01 = 0.0F;
      this.m02 = 0.0F;
      this.m10 = 0.0F;
      this.m11 = var1;
      this.m12 = 0.0F;
      this.m20 = 0.0F;
      this.m21 = 0.0F;
      this.m22 = var1;
   }

   public final void rotX(float var1) {
      float var2 = (float)Math.sin((double)var1);
      float var3 = (float)Math.cos((double)var1);
      this.m00 = 1.0F;
      this.m01 = 0.0F;
      this.m02 = 0.0F;
      this.m10 = 0.0F;
      this.m11 = var3;
      this.m12 = -var2;
      this.m20 = 0.0F;
      this.m21 = var2;
      this.m22 = var3;
   }

   public final void rotY(float var1) {
      float var2 = (float)Math.sin((double)var1);
      float var3 = (float)Math.cos((double)var1);
      this.m00 = var3;
      this.m01 = 0.0F;
      this.m02 = var2;
      this.m10 = 0.0F;
      this.m11 = 1.0F;
      this.m12 = 0.0F;
      this.m20 = -var2;
      this.m21 = 0.0F;
      this.m22 = var3;
   }

   public final void rotZ(float var1) {
      float var2 = (float)Math.sin((double)var1);
      float var3 = (float)Math.cos((double)var1);
      this.m00 = var3;
      this.m01 = -var2;
      this.m02 = 0.0F;
      this.m10 = var2;
      this.m11 = var3;
      this.m12 = 0.0F;
      this.m20 = 0.0F;
      this.m21 = 0.0F;
      this.m22 = 1.0F;
   }

   public final void mul(float var1) {
      this.m00 *= var1;
      this.m01 *= var1;
      this.m02 *= var1;
      this.m10 *= var1;
      this.m11 *= var1;
      this.m12 *= var1;
      this.m20 *= var1;
      this.m21 *= var1;
      this.m22 *= var1;
   }

   public final void mul(float var1, Matrix3f var2) {
      this.m00 = var1 * var2.m00;
      this.m01 = var1 * var2.m01;
      this.m02 = var1 * var2.m02;
      this.m10 = var1 * var2.m10;
      this.m11 = var1 * var2.m11;
      this.m12 = var1 * var2.m12;
      this.m20 = var1 * var2.m20;
      this.m21 = var1 * var2.m21;
      this.m22 = var1 * var2.m22;
   }

   public final void mul(Matrix3f var1) {
      float var2 = this.m00 * var1.m00 + this.m01 * var1.m10 + this.m02 * var1.m20;
      float var3 = this.m00 * var1.m01 + this.m01 * var1.m11 + this.m02 * var1.m21;
      float var4 = this.m00 * var1.m02 + this.m01 * var1.m12 + this.m02 * var1.m22;
      float var5 = this.m10 * var1.m00 + this.m11 * var1.m10 + this.m12 * var1.m20;
      float var6 = this.m10 * var1.m01 + this.m11 * var1.m11 + this.m12 * var1.m21;
      float var7 = this.m10 * var1.m02 + this.m11 * var1.m12 + this.m12 * var1.m22;
      float var8 = this.m20 * var1.m00 + this.m21 * var1.m10 + this.m22 * var1.m20;
      float var9 = this.m20 * var1.m01 + this.m21 * var1.m11 + this.m22 * var1.m21;
      float var10 = this.m20 * var1.m02 + this.m21 * var1.m12 + this.m22 * var1.m22;
      this.m00 = var2;
      this.m01 = var3;
      this.m02 = var4;
      this.m10 = var5;
      this.m11 = var6;
      this.m12 = var7;
      this.m20 = var8;
      this.m21 = var9;
      this.m22 = var10;
   }

   public final void mul(Matrix3f var1, Matrix3f var2) {
      if (this != var1 && this != var2) {
         this.m00 = var1.m00 * var2.m00 + var1.m01 * var2.m10 + var1.m02 * var2.m20;
         this.m01 = var1.m00 * var2.m01 + var1.m01 * var2.m11 + var1.m02 * var2.m21;
         this.m02 = var1.m00 * var2.m02 + var1.m01 * var2.m12 + var1.m02 * var2.m22;
         this.m10 = var1.m10 * var2.m00 + var1.m11 * var2.m10 + var1.m12 * var2.m20;
         this.m11 = var1.m10 * var2.m01 + var1.m11 * var2.m11 + var1.m12 * var2.m21;
         this.m12 = var1.m10 * var2.m02 + var1.m11 * var2.m12 + var1.m12 * var2.m22;
         this.m20 = var1.m20 * var2.m00 + var1.m21 * var2.m10 + var1.m22 * var2.m20;
         this.m21 = var1.m20 * var2.m01 + var1.m21 * var2.m11 + var1.m22 * var2.m21;
         this.m22 = var1.m20 * var2.m02 + var1.m21 * var2.m12 + var1.m22 * var2.m22;
      } else {
         float var3 = var1.m00 * var2.m00 + var1.m01 * var2.m10 + var1.m02 * var2.m20;
         float var4 = var1.m00 * var2.m01 + var1.m01 * var2.m11 + var1.m02 * var2.m21;
         float var5 = var1.m00 * var2.m02 + var1.m01 * var2.m12 + var1.m02 * var2.m22;
         float var6 = var1.m10 * var2.m00 + var1.m11 * var2.m10 + var1.m12 * var2.m20;
         float var7 = var1.m10 * var2.m01 + var1.m11 * var2.m11 + var1.m12 * var2.m21;
         float var8 = var1.m10 * var2.m02 + var1.m11 * var2.m12 + var1.m12 * var2.m22;
         float var9 = var1.m20 * var2.m00 + var1.m21 * var2.m10 + var1.m22 * var2.m20;
         float var10 = var1.m20 * var2.m01 + var1.m21 * var2.m11 + var1.m22 * var2.m21;
         float var11 = var1.m20 * var2.m02 + var1.m21 * var2.m12 + var1.m22 * var2.m22;
         this.m00 = var3;
         this.m01 = var4;
         this.m02 = var5;
         this.m10 = var6;
         this.m11 = var7;
         this.m12 = var8;
         this.m20 = var9;
         this.m21 = var10;
         this.m22 = var11;
      }

   }

   public final void mulNormalize(Matrix3f var1) {
      double[] var2 = new double[9];
      double[] var3 = new double[9];
      double[] var4 = new double[3];
      var2[0] = (double)(this.m00 * var1.m00 + this.m01 * var1.m10 + this.m02 * var1.m20);
      var2[1] = (double)(this.m00 * var1.m01 + this.m01 * var1.m11 + this.m02 * var1.m21);
      var2[2] = (double)(this.m00 * var1.m02 + this.m01 * var1.m12 + this.m02 * var1.m22);
      var2[3] = (double)(this.m10 * var1.m00 + this.m11 * var1.m10 + this.m12 * var1.m20);
      var2[4] = (double)(this.m10 * var1.m01 + this.m11 * var1.m11 + this.m12 * var1.m21);
      var2[5] = (double)(this.m10 * var1.m02 + this.m11 * var1.m12 + this.m12 * var1.m22);
      var2[6] = (double)(this.m20 * var1.m00 + this.m21 * var1.m10 + this.m22 * var1.m20);
      var2[7] = (double)(this.m20 * var1.m01 + this.m21 * var1.m11 + this.m22 * var1.m21);
      var2[8] = (double)(this.m20 * var1.m02 + this.m21 * var1.m12 + this.m22 * var1.m22);
      Matrix3d.compute_svd(var2, var4, var3);
      this.m00 = (float)var3[0];
      this.m01 = (float)var3[1];
      this.m02 = (float)var3[2];
      this.m10 = (float)var3[3];
      this.m11 = (float)var3[4];
      this.m12 = (float)var3[5];
      this.m20 = (float)var3[6];
      this.m21 = (float)var3[7];
      this.m22 = (float)var3[8];
   }

   public final void mulNormalize(Matrix3f var1, Matrix3f var2) {
      double[] var3 = new double[9];
      double[] var4 = new double[9];
      double[] var5 = new double[3];
      var3[0] = (double)(var1.m00 * var2.m00 + var1.m01 * var2.m10 + var1.m02 * var2.m20);
      var3[1] = (double)(var1.m00 * var2.m01 + var1.m01 * var2.m11 + var1.m02 * var2.m21);
      var3[2] = (double)(var1.m00 * var2.m02 + var1.m01 * var2.m12 + var1.m02 * var2.m22);
      var3[3] = (double)(var1.m10 * var2.m00 + var1.m11 * var2.m10 + var1.m12 * var2.m20);
      var3[4] = (double)(var1.m10 * var2.m01 + var1.m11 * var2.m11 + var1.m12 * var2.m21);
      var3[5] = (double)(var1.m10 * var2.m02 + var1.m11 * var2.m12 + var1.m12 * var2.m22);
      var3[6] = (double)(var1.m20 * var2.m00 + var1.m21 * var2.m10 + var1.m22 * var2.m20);
      var3[7] = (double)(var1.m20 * var2.m01 + var1.m21 * var2.m11 + var1.m22 * var2.m21);
      var3[8] = (double)(var1.m20 * var2.m02 + var1.m21 * var2.m12 + var1.m22 * var2.m22);
      Matrix3d.compute_svd(var3, var5, var4);
      this.m00 = (float)var4[0];
      this.m01 = (float)var4[1];
      this.m02 = (float)var4[2];
      this.m10 = (float)var4[3];
      this.m11 = (float)var4[4];
      this.m12 = (float)var4[5];
      this.m20 = (float)var4[6];
      this.m21 = (float)var4[7];
      this.m22 = (float)var4[8];
   }

   public final void mulTransposeBoth(Matrix3f var1, Matrix3f var2) {
      if (this != var1 && this != var2) {
         this.m00 = var1.m00 * var2.m00 + var1.m10 * var2.m01 + var1.m20 * var2.m02;
         this.m01 = var1.m00 * var2.m10 + var1.m10 * var2.m11 + var1.m20 * var2.m12;
         this.m02 = var1.m00 * var2.m20 + var1.m10 * var2.m21 + var1.m20 * var2.m22;
         this.m10 = var1.m01 * var2.m00 + var1.m11 * var2.m01 + var1.m21 * var2.m02;
         this.m11 = var1.m01 * var2.m10 + var1.m11 * var2.m11 + var1.m21 * var2.m12;
         this.m12 = var1.m01 * var2.m20 + var1.m11 * var2.m21 + var1.m21 * var2.m22;
         this.m20 = var1.m02 * var2.m00 + var1.m12 * var2.m01 + var1.m22 * var2.m02;
         this.m21 = var1.m02 * var2.m10 + var1.m12 * var2.m11 + var1.m22 * var2.m12;
         this.m22 = var1.m02 * var2.m20 + var1.m12 * var2.m21 + var1.m22 * var2.m22;
      } else {
         float var3 = var1.m00 * var2.m00 + var1.m10 * var2.m01 + var1.m20 * var2.m02;
         float var4 = var1.m00 * var2.m10 + var1.m10 * var2.m11 + var1.m20 * var2.m12;
         float var5 = var1.m00 * var2.m20 + var1.m10 * var2.m21 + var1.m20 * var2.m22;
         float var6 = var1.m01 * var2.m00 + var1.m11 * var2.m01 + var1.m21 * var2.m02;
         float var7 = var1.m01 * var2.m10 + var1.m11 * var2.m11 + var1.m21 * var2.m12;
         float var8 = var1.m01 * var2.m20 + var1.m11 * var2.m21 + var1.m21 * var2.m22;
         float var9 = var1.m02 * var2.m00 + var1.m12 * var2.m01 + var1.m22 * var2.m02;
         float var10 = var1.m02 * var2.m10 + var1.m12 * var2.m11 + var1.m22 * var2.m12;
         float var11 = var1.m02 * var2.m20 + var1.m12 * var2.m21 + var1.m22 * var2.m22;
         this.m00 = var3;
         this.m01 = var4;
         this.m02 = var5;
         this.m10 = var6;
         this.m11 = var7;
         this.m12 = var8;
         this.m20 = var9;
         this.m21 = var10;
         this.m22 = var11;
      }

   }

   public final void mulTransposeRight(Matrix3f var1, Matrix3f var2) {
      if (this != var1 && this != var2) {
         this.m00 = var1.m00 * var2.m00 + var1.m01 * var2.m01 + var1.m02 * var2.m02;
         this.m01 = var1.m00 * var2.m10 + var1.m01 * var2.m11 + var1.m02 * var2.m12;
         this.m02 = var1.m00 * var2.m20 + var1.m01 * var2.m21 + var1.m02 * var2.m22;
         this.m10 = var1.m10 * var2.m00 + var1.m11 * var2.m01 + var1.m12 * var2.m02;
         this.m11 = var1.m10 * var2.m10 + var1.m11 * var2.m11 + var1.m12 * var2.m12;
         this.m12 = var1.m10 * var2.m20 + var1.m11 * var2.m21 + var1.m12 * var2.m22;
         this.m20 = var1.m20 * var2.m00 + var1.m21 * var2.m01 + var1.m22 * var2.m02;
         this.m21 = var1.m20 * var2.m10 + var1.m21 * var2.m11 + var1.m22 * var2.m12;
         this.m22 = var1.m20 * var2.m20 + var1.m21 * var2.m21 + var1.m22 * var2.m22;
      } else {
         float var3 = var1.m00 * var2.m00 + var1.m01 * var2.m01 + var1.m02 * var2.m02;
         float var4 = var1.m00 * var2.m10 + var1.m01 * var2.m11 + var1.m02 * var2.m12;
         float var5 = var1.m00 * var2.m20 + var1.m01 * var2.m21 + var1.m02 * var2.m22;
         float var6 = var1.m10 * var2.m00 + var1.m11 * var2.m01 + var1.m12 * var2.m02;
         float var7 = var1.m10 * var2.m10 + var1.m11 * var2.m11 + var1.m12 * var2.m12;
         float var8 = var1.m10 * var2.m20 + var1.m11 * var2.m21 + var1.m12 * var2.m22;
         float var9 = var1.m20 * var2.m00 + var1.m21 * var2.m01 + var1.m22 * var2.m02;
         float var10 = var1.m20 * var2.m10 + var1.m21 * var2.m11 + var1.m22 * var2.m12;
         float var11 = var1.m20 * var2.m20 + var1.m21 * var2.m21 + var1.m22 * var2.m22;
         this.m00 = var3;
         this.m01 = var4;
         this.m02 = var5;
         this.m10 = var6;
         this.m11 = var7;
         this.m12 = var8;
         this.m20 = var9;
         this.m21 = var10;
         this.m22 = var11;
      }

   }

   public final void mulTransposeLeft(Matrix3f var1, Matrix3f var2) {
      if (this != var1 && this != var2) {
         this.m00 = var1.m00 * var2.m00 + var1.m10 * var2.m10 + var1.m20 * var2.m20;
         this.m01 = var1.m00 * var2.m01 + var1.m10 * var2.m11 + var1.m20 * var2.m21;
         this.m02 = var1.m00 * var2.m02 + var1.m10 * var2.m12 + var1.m20 * var2.m22;
         this.m10 = var1.m01 * var2.m00 + var1.m11 * var2.m10 + var1.m21 * var2.m20;
         this.m11 = var1.m01 * var2.m01 + var1.m11 * var2.m11 + var1.m21 * var2.m21;
         this.m12 = var1.m01 * var2.m02 + var1.m11 * var2.m12 + var1.m21 * var2.m22;
         this.m20 = var1.m02 * var2.m00 + var1.m12 * var2.m10 + var1.m22 * var2.m20;
         this.m21 = var1.m02 * var2.m01 + var1.m12 * var2.m11 + var1.m22 * var2.m21;
         this.m22 = var1.m02 * var2.m02 + var1.m12 * var2.m12 + var1.m22 * var2.m22;
      } else {
         float var3 = var1.m00 * var2.m00 + var1.m10 * var2.m10 + var1.m20 * var2.m20;
         float var4 = var1.m00 * var2.m01 + var1.m10 * var2.m11 + var1.m20 * var2.m21;
         float var5 = var1.m00 * var2.m02 + var1.m10 * var2.m12 + var1.m20 * var2.m22;
         float var6 = var1.m01 * var2.m00 + var1.m11 * var2.m10 + var1.m21 * var2.m20;
         float var7 = var1.m01 * var2.m01 + var1.m11 * var2.m11 + var1.m21 * var2.m21;
         float var8 = var1.m01 * var2.m02 + var1.m11 * var2.m12 + var1.m21 * var2.m22;
         float var9 = var1.m02 * var2.m00 + var1.m12 * var2.m10 + var1.m22 * var2.m20;
         float var10 = var1.m02 * var2.m01 + var1.m12 * var2.m11 + var1.m22 * var2.m21;
         float var11 = var1.m02 * var2.m02 + var1.m12 * var2.m12 + var1.m22 * var2.m22;
         this.m00 = var3;
         this.m01 = var4;
         this.m02 = var5;
         this.m10 = var6;
         this.m11 = var7;
         this.m12 = var8;
         this.m20 = var9;
         this.m21 = var10;
         this.m22 = var11;
      }

   }

   public final void normalize() {
      double[] var1 = new double[9];
      double[] var2 = new double[3];
      this.getScaleRotate(var2, var1);
      this.m00 = (float)var1[0];
      this.m01 = (float)var1[1];
      this.m02 = (float)var1[2];
      this.m10 = (float)var1[3];
      this.m11 = (float)var1[4];
      this.m12 = (float)var1[5];
      this.m20 = (float)var1[6];
      this.m21 = (float)var1[7];
      this.m22 = (float)var1[8];
   }

   public final void normalize(Matrix3f var1) {
      double[] var2 = new double[9];
      double[] var3 = new double[9];
      double[] var4 = new double[3];
      var2[0] = (double)var1.m00;
      var2[1] = (double)var1.m01;
      var2[2] = (double)var1.m02;
      var2[3] = (double)var1.m10;
      var2[4] = (double)var1.m11;
      var2[5] = (double)var1.m12;
      var2[6] = (double)var1.m20;
      var2[7] = (double)var1.m21;
      var2[8] = (double)var1.m22;
      Matrix3d.compute_svd(var2, var4, var3);
      this.m00 = (float)var3[0];
      this.m01 = (float)var3[1];
      this.m02 = (float)var3[2];
      this.m10 = (float)var3[3];
      this.m11 = (float)var3[4];
      this.m12 = (float)var3[5];
      this.m20 = (float)var3[6];
      this.m21 = (float)var3[7];
      this.m22 = (float)var3[8];
   }

   public final void normalizeCP() {
      float var1 = 1.0F / (float)Math.sqrt((double)(this.m00 * this.m00 + this.m10 * this.m10 + this.m20 * this.m20));
      this.m00 *= var1;
      this.m10 *= var1;
      this.m20 *= var1;
      var1 = 1.0F / (float)Math.sqrt((double)(this.m01 * this.m01 + this.m11 * this.m11 + this.m21 * this.m21));
      this.m01 *= var1;
      this.m11 *= var1;
      this.m21 *= var1;
      this.m02 = this.m10 * this.m21 - this.m11 * this.m20;
      this.m12 = this.m01 * this.m20 - this.m00 * this.m21;
      this.m22 = this.m00 * this.m11 - this.m01 * this.m10;
   }

   public final void normalizeCP(Matrix3f var1) {
      float var2 = 1.0F / (float)Math.sqrt((double)(var1.m00 * var1.m00 + var1.m10 * var1.m10 + var1.m20 * var1.m20));
      this.m00 = var1.m00 * var2;
      this.m10 = var1.m10 * var2;
      this.m20 = var1.m20 * var2;
      var2 = 1.0F / (float)Math.sqrt((double)(var1.m01 * var1.m01 + var1.m11 * var1.m11 + var1.m21 * var1.m21));
      this.m01 = var1.m01 * var2;
      this.m11 = var1.m11 * var2;
      this.m21 = var1.m21 * var2;
      this.m02 = this.m10 * this.m21 - this.m11 * this.m20;
      this.m12 = this.m01 * this.m20 - this.m00 * this.m21;
      this.m22 = this.m00 * this.m11 - this.m01 * this.m10;
   }

   public boolean equals(Matrix3f var1) {
      try {
         return this.m00 == var1.m00 && this.m01 == var1.m01 && this.m02 == var1.m02 && this.m10 == var1.m10 && this.m11 == var1.m11 && this.m12 == var1.m12 && this.m20 == var1.m20 && this.m21 == var1.m21 && this.m22 == var1.m22;
      } catch (NullPointerException var3) {
         return false;
      }
   }

   public boolean equals(Object var1) {
      try {
         Matrix3f var2 = (Matrix3f)var1;
         return this.m00 == var2.m00 && this.m01 == var2.m01 && this.m02 == var2.m02 && this.m10 == var2.m10 && this.m11 == var2.m11 && this.m12 == var2.m12 && this.m20 == var2.m20 && this.m21 == var2.m21 && this.m22 == var2.m22;
      } catch (ClassCastException var3) {
         return false;
      } catch (NullPointerException var4) {
         return false;
      }
   }

   public boolean epsilonEquals(Matrix3f var1, float var2) {
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

      if (Math.abs(this.m10 - var1.m10) > var2) {
         var3 = false;
      }

      if (Math.abs(this.m11 - var1.m11) > var2) {
         var3 = false;
      }

      if (Math.abs(this.m12 - var1.m12) > var2) {
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

      return var3;
   }

   public int hashCode() {
      long var1 = 1L;
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.m00);
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.m01);
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.m02);
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.m10);
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.m11);
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.m12);
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.m20);
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.m21);
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.m22);
      return (int)(var1 ^ var1 >> 32);
   }

   public final void setZero() {
      this.m00 = 0.0F;
      this.m01 = 0.0F;
      this.m02 = 0.0F;
      this.m10 = 0.0F;
      this.m11 = 0.0F;
      this.m12 = 0.0F;
      this.m20 = 0.0F;
      this.m21 = 0.0F;
      this.m22 = 0.0F;
   }

   public final void negate() {
      this.m00 = -this.m00;
      this.m01 = -this.m01;
      this.m02 = -this.m02;
      this.m10 = -this.m10;
      this.m11 = -this.m11;
      this.m12 = -this.m12;
      this.m20 = -this.m20;
      this.m21 = -this.m21;
      this.m22 = -this.m22;
   }

   public final void negate(Matrix3f var1) {
      this.m00 = -var1.m00;
      this.m01 = -var1.m01;
      this.m02 = -var1.m02;
      this.m10 = -var1.m10;
      this.m11 = -var1.m11;
      this.m12 = -var1.m12;
      this.m20 = -var1.m20;
      this.m21 = -var1.m21;
      this.m22 = -var1.m22;
   }

   public final void transform(Tuple3f var1) {
      float var2 = this.m00 * var1.x + this.m01 * var1.y + this.m02 * var1.z;
      float var3 = this.m10 * var1.x + this.m11 * var1.y + this.m12 * var1.z;
      float var4 = this.m20 * var1.x + this.m21 * var1.y + this.m22 * var1.z;
      var1.set(var2, var3, var4);
   }

   public final void transform(Tuple3f var1, Tuple3f var2) {
      float var3 = this.m00 * var1.x + this.m01 * var1.y + this.m02 * var1.z;
      float var4 = this.m10 * var1.x + this.m11 * var1.y + this.m12 * var1.z;
      var2.z = this.m20 * var1.x + this.m21 * var1.y + this.m22 * var1.z;
      var2.x = var3;
      var2.y = var4;
   }

   void getScaleRotate(double[] var1, double[] var2) {
      double[] var3 = new double[]{(double)this.m00, (double)this.m01, (double)this.m02, (double)this.m10, (double)this.m11, (double)this.m12, (double)this.m20, (double)this.m21, (double)this.m22};
      Matrix3d.compute_svd(var3, var1, var2);
   }

   public Object clone() {
      Matrix3f var1 = null;

      try {
         var1 = (Matrix3f)super.clone();
         return var1;
      } catch (CloneNotSupportedException var3) {
         throw new InternalError();
      }
   }
}

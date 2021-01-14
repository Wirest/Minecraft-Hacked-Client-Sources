package javax.vecmath;

import java.io.Serializable;

public abstract class Tuple4f implements Serializable, Cloneable {
   static final long serialVersionUID = 7068460319248845763L;
   public float x;
   public float y;
   public float z;
   public float w;

   public Tuple4f(float var1, float var2, float var3, float var4) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
      this.w = var4;
   }

   public Tuple4f(float[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
      this.w = var1[3];
   }

   public Tuple4f(Tuple4f var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      this.w = var1.w;
   }

   public Tuple4f(Tuple4d var1) {
      this.x = (float)var1.x;
      this.y = (float)var1.y;
      this.z = (float)var1.z;
      this.w = (float)var1.w;
   }

   public Tuple4f() {
      this.x = 0.0F;
      this.y = 0.0F;
      this.z = 0.0F;
      this.w = 0.0F;
   }

   public final void set(float var1, float var2, float var3, float var4) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
      this.w = var4;
   }

   public final void set(float[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
      this.w = var1[3];
   }

   public final void set(Tuple4f var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      this.w = var1.w;
   }

   public final void set(Tuple4d var1) {
      this.x = (float)var1.x;
      this.y = (float)var1.y;
      this.z = (float)var1.z;
      this.w = (float)var1.w;
   }

   public final void get(float[] var1) {
      var1[0] = this.x;
      var1[1] = this.y;
      var1[2] = this.z;
      var1[3] = this.w;
   }

   public final void get(Tuple4f var1) {
      var1.x = this.x;
      var1.y = this.y;
      var1.z = this.z;
      var1.w = this.w;
   }

   public final void add(Tuple4f var1, Tuple4f var2) {
      this.x = var1.x + var2.x;
      this.y = var1.y + var2.y;
      this.z = var1.z + var2.z;
      this.w = var1.w + var2.w;
   }

   public final void add(Tuple4f var1) {
      this.x += var1.x;
      this.y += var1.y;
      this.z += var1.z;
      this.w += var1.w;
   }

   public final void sub(Tuple4f var1, Tuple4f var2) {
      this.x = var1.x - var2.x;
      this.y = var1.y - var2.y;
      this.z = var1.z - var2.z;
      this.w = var1.w - var2.w;
   }

   public final void sub(Tuple4f var1) {
      this.x -= var1.x;
      this.y -= var1.y;
      this.z -= var1.z;
      this.w -= var1.w;
   }

   public final void negate(Tuple4f var1) {
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

   public final void scale(float var1, Tuple4f var2) {
      this.x = var1 * var2.x;
      this.y = var1 * var2.y;
      this.z = var1 * var2.z;
      this.w = var1 * var2.w;
   }

   public final void scale(float var1) {
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
      this.w *= var1;
   }

   public final void scaleAdd(float var1, Tuple4f var2, Tuple4f var3) {
      this.x = var1 * var2.x + var3.x;
      this.y = var1 * var2.y + var3.y;
      this.z = var1 * var2.z + var3.z;
      this.w = var1 * var2.w + var3.w;
   }

   public final void scaleAdd(float var1, Tuple4f var2) {
      this.x = var1 * this.x + var2.x;
      this.y = var1 * this.y + var2.y;
      this.z = var1 * this.z + var2.z;
      this.w = var1 * this.w + var2.w;
   }

   public String toString() {
      return "(" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + ")";
   }

   public boolean equals(Tuple4f var1) {
      try {
         return this.x == var1.x && this.y == var1.y && this.z == var1.z && this.w == var1.w;
      } catch (NullPointerException var3) {
         return false;
      }
   }

   public boolean equals(Object var1) {
      try {
         Tuple4f var2 = (Tuple4f)var1;
         return this.x == var2.x && this.y == var2.y && this.z == var2.z && this.w == var2.w;
      } catch (NullPointerException var3) {
         return false;
      } catch (ClassCastException var4) {
         return false;
      }
   }

   public boolean epsilonEquals(Tuple4f var1, float var2) {
      float var3 = this.x - var1.x;
      if ((var3 < 0.0F ? -var3 : var3) > var2) {
         return false;
      } else {
         var3 = this.y - var1.y;
         if ((var3 < 0.0F ? -var3 : var3) > var2) {
            return false;
         } else {
            var3 = this.z - var1.z;
            if ((var3 < 0.0F ? -var3 : var3) > var2) {
               return false;
            } else {
               var3 = this.w - var1.w;
               return (var3 < 0.0F ? -var3 : var3) <= var2;
            }
         }
      }
   }

   public int hashCode() {
      long var1 = 1L;
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.x);
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.y);
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.z);
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.w);
      return (int)(var1 ^ var1 >> 32);
   }

   public final void clamp(float var1, float var2, Tuple4f var3) {
      if (var3.x > var2) {
         this.x = var2;
      } else if (var3.x < var1) {
         this.x = var1;
      } else {
         this.x = var3.x;
      }

      if (var3.y > var2) {
         this.y = var2;
      } else if (var3.y < var1) {
         this.y = var1;
      } else {
         this.y = var3.y;
      }

      if (var3.z > var2) {
         this.z = var2;
      } else if (var3.z < var1) {
         this.z = var1;
      } else {
         this.z = var3.z;
      }

      if (var3.w > var2) {
         this.w = var2;
      } else if (var3.w < var1) {
         this.w = var1;
      } else {
         this.w = var3.w;
      }

   }

   public final void clampMin(float var1, Tuple4f var2) {
      if (var2.x < var1) {
         this.x = var1;
      } else {
         this.x = var2.x;
      }

      if (var2.y < var1) {
         this.y = var1;
      } else {
         this.y = var2.y;
      }

      if (var2.z < var1) {
         this.z = var1;
      } else {
         this.z = var2.z;
      }

      if (var2.w < var1) {
         this.w = var1;
      } else {
         this.w = var2.w;
      }

   }

   public final void clampMax(float var1, Tuple4f var2) {
      if (var2.x > var1) {
         this.x = var1;
      } else {
         this.x = var2.x;
      }

      if (var2.y > var1) {
         this.y = var1;
      } else {
         this.y = var2.y;
      }

      if (var2.z > var1) {
         this.z = var1;
      } else {
         this.z = var2.z;
      }

      if (var2.w > var1) {
         this.w = var1;
      } else {
         this.w = var2.z;
      }

   }

   public final void absolute(Tuple4f var1) {
      this.x = Math.abs(var1.x);
      this.y = Math.abs(var1.y);
      this.z = Math.abs(var1.z);
      this.w = Math.abs(var1.w);
   }

   public final void clamp(float var1, float var2) {
      if (this.x > var2) {
         this.x = var2;
      } else if (this.x < var1) {
         this.x = var1;
      }

      if (this.y > var2) {
         this.y = var2;
      } else if (this.y < var1) {
         this.y = var1;
      }

      if (this.z > var2) {
         this.z = var2;
      } else if (this.z < var1) {
         this.z = var1;
      }

      if (this.w > var2) {
         this.w = var2;
      } else if (this.w < var1) {
         this.w = var1;
      }

   }

   public final void clampMin(float var1) {
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

   public final void clampMax(float var1) {
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

   public void interpolate(Tuple4f var1, Tuple4f var2, float var3) {
      this.x = (1.0F - var3) * var1.x + var3 * var2.x;
      this.y = (1.0F - var3) * var1.y + var3 * var2.y;
      this.z = (1.0F - var3) * var1.z + var3 * var2.z;
      this.w = (1.0F - var3) * var1.w + var3 * var2.w;
   }

   public void interpolate(Tuple4f var1, float var2) {
      this.x = (1.0F - var2) * this.x + var2 * var1.x;
      this.y = (1.0F - var2) * this.y + var2 * var1.y;
      this.z = (1.0F - var2) * this.z + var2 * var1.z;
      this.w = (1.0F - var2) * this.w + var2 * var1.w;
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new InternalError();
      }
   }
}

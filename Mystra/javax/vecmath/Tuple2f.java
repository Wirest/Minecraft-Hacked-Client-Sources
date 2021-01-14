package javax.vecmath;

import java.io.Serializable;

public abstract class Tuple2f implements Serializable, Cloneable {
   static final long serialVersionUID = 9011180388985266884L;
   public float x;
   public float y;

   public Tuple2f(float var1, float var2) {
      this.x = var1;
      this.y = var2;
   }

   public Tuple2f(float[] var1) {
      this.x = var1[0];
      this.y = var1[1];
   }

   public Tuple2f(Tuple2f var1) {
      this.x = var1.x;
      this.y = var1.y;
   }

   public Tuple2f(Tuple2d var1) {
      this.x = (float)var1.x;
      this.y = (float)var1.y;
   }

   public Tuple2f() {
      this.x = 0.0F;
      this.y = 0.0F;
   }

   public final void set(float var1, float var2) {
      this.x = var1;
      this.y = var2;
   }

   public final void set(float[] var1) {
      this.x = var1[0];
      this.y = var1[1];
   }

   public final void set(Tuple2f var1) {
      this.x = var1.x;
      this.y = var1.y;
   }

   public final void set(Tuple2d var1) {
      this.x = (float)var1.x;
      this.y = (float)var1.y;
   }

   public final void get(float[] var1) {
      var1[0] = this.x;
      var1[1] = this.y;
   }

   public final void add(Tuple2f var1, Tuple2f var2) {
      this.x = var1.x + var2.x;
      this.y = var1.y + var2.y;
   }

   public final void add(Tuple2f var1) {
      this.x += var1.x;
      this.y += var1.y;
   }

   public final void sub(Tuple2f var1, Tuple2f var2) {
      this.x = var1.x - var2.x;
      this.y = var1.y - var2.y;
   }

   public final void sub(Tuple2f var1) {
      this.x -= var1.x;
      this.y -= var1.y;
   }

   public final void negate(Tuple2f var1) {
      this.x = -var1.x;
      this.y = -var1.y;
   }

   public final void negate() {
      this.x = -this.x;
      this.y = -this.y;
   }

   public final void scale(float var1, Tuple2f var2) {
      this.x = var1 * var2.x;
      this.y = var1 * var2.y;
   }

   public final void scale(float var1) {
      this.x *= var1;
      this.y *= var1;
   }

   public final void scaleAdd(float var1, Tuple2f var2, Tuple2f var3) {
      this.x = var1 * var2.x + var3.x;
      this.y = var1 * var2.y + var3.y;
   }

   public final void scaleAdd(float var1, Tuple2f var2) {
      this.x = var1 * this.x + var2.x;
      this.y = var1 * this.y + var2.y;
   }

   public int hashCode() {
      long var1 = 1L;
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.x);
      var1 = 31L * var1 + (long)VecMathUtil.floatToIntBits(this.y);
      return (int)(var1 ^ var1 >> 32);
   }

   public boolean equals(Tuple2f var1) {
      try {
         return this.x == var1.x && this.y == var1.y;
      } catch (NullPointerException var3) {
         return false;
      }
   }

   public boolean equals(Object var1) {
      try {
         Tuple2f var2 = (Tuple2f)var1;
         return this.x == var2.x && this.y == var2.y;
      } catch (NullPointerException var3) {
         return false;
      } catch (ClassCastException var4) {
         return false;
      }
   }

   public boolean epsilonEquals(Tuple2f var1, float var2) {
      float var3 = this.x - var1.x;
      if ((var3 < 0.0F ? -var3 : var3) > var2) {
         return false;
      } else {
         var3 = this.y - var1.y;
         return (var3 < 0.0F ? -var3 : var3) <= var2;
      }
   }

   public String toString() {
      return "(" + this.x + ", " + this.y + ")";
   }

   public final void clamp(float var1, float var2, Tuple2f var3) {
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

   }

   public final void clampMin(float var1, Tuple2f var2) {
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

   }

   public final void clampMax(float var1, Tuple2f var2) {
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

   }

   public final void absolute(Tuple2f var1) {
      this.x = Math.abs(var1.x);
      this.y = Math.abs(var1.y);
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

   }

   public final void clampMin(float var1) {
      if (this.x < var1) {
         this.x = var1;
      }

      if (this.y < var1) {
         this.y = var1;
      }

   }

   public final void clampMax(float var1) {
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

   public final void interpolate(Tuple2f var1, Tuple2f var2, float var3) {
      this.x = (1.0F - var3) * var1.x + var3 * var2.x;
      this.y = (1.0F - var3) * var1.y + var3 * var2.y;
   }

   public final void interpolate(Tuple2f var1, float var2) {
      this.x = (1.0F - var2) * this.x + var2 * var1.x;
      this.y = (1.0F - var2) * this.y + var2 * var1.y;
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new InternalError();
      }
   }
}

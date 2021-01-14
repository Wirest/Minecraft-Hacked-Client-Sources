package javax.vecmath;

import java.io.Serializable;

public abstract class Tuple4i implements Serializable, Cloneable {
   static final long serialVersionUID = 8064614250942616720L;
   public int x;
   public int y;
   public int z;
   public int w;

   public Tuple4i(int var1, int var2, int var3, int var4) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
      this.w = var4;
   }

   public Tuple4i(int[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
      this.w = var1[3];
   }

   public Tuple4i(Tuple4i var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      this.w = var1.w;
   }

   public Tuple4i() {
      this.x = 0;
      this.y = 0;
      this.z = 0;
      this.w = 0;
   }

   public final void set(int var1, int var2, int var3, int var4) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
      this.w = var4;
   }

   public final void set(int[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
      this.w = var1[3];
   }

   public final void set(Tuple4i var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      this.w = var1.w;
   }

   public final void get(int[] var1) {
      var1[0] = this.x;
      var1[1] = this.y;
      var1[2] = this.z;
      var1[3] = this.w;
   }

   public final void get(Tuple4i var1) {
      var1.x = this.x;
      var1.y = this.y;
      var1.z = this.z;
      var1.w = this.w;
   }

   public final void add(Tuple4i var1, Tuple4i var2) {
      this.x = var1.x + var2.x;
      this.y = var1.y + var2.y;
      this.z = var1.z + var2.z;
      this.w = var1.w + var2.w;
   }

   public final void add(Tuple4i var1) {
      this.x += var1.x;
      this.y += var1.y;
      this.z += var1.z;
      this.w += var1.w;
   }

   public final void sub(Tuple4i var1, Tuple4i var2) {
      this.x = var1.x - var2.x;
      this.y = var1.y - var2.y;
      this.z = var1.z - var2.z;
      this.w = var1.w - var2.w;
   }

   public final void sub(Tuple4i var1) {
      this.x -= var1.x;
      this.y -= var1.y;
      this.z -= var1.z;
      this.w -= var1.w;
   }

   public final void negate(Tuple4i var1) {
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

   public final void scale(int var1, Tuple4i var2) {
      this.x = var1 * var2.x;
      this.y = var1 * var2.y;
      this.z = var1 * var2.z;
      this.w = var1 * var2.w;
   }

   public final void scale(int var1) {
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
      this.w *= var1;
   }

   public final void scaleAdd(int var1, Tuple4i var2, Tuple4i var3) {
      this.x = var1 * var2.x + var3.x;
      this.y = var1 * var2.y + var3.y;
      this.z = var1 * var2.z + var3.z;
      this.w = var1 * var2.w + var3.w;
   }

   public final void scaleAdd(int var1, Tuple4i var2) {
      this.x = var1 * this.x + var2.x;
      this.y = var1 * this.y + var2.y;
      this.z = var1 * this.z + var2.z;
      this.w = var1 * this.w + var2.w;
   }

   public String toString() {
      return "(" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + ")";
   }

   public boolean equals(Object var1) {
      try {
         Tuple4i var2 = (Tuple4i)var1;
         return this.x == var2.x && this.y == var2.y && this.z == var2.z && this.w == var2.w;
      } catch (NullPointerException var3) {
         return false;
      } catch (ClassCastException var4) {
         return false;
      }
   }

   public int hashCode() {
      long var1 = 1L;
      var1 = 31L * var1 + (long)this.x;
      var1 = 31L * var1 + (long)this.y;
      var1 = 31L * var1 + (long)this.z;
      var1 = 31L * var1 + (long)this.w;
      return (int)(var1 ^ var1 >> 32);
   }

   public final void clamp(int var1, int var2, Tuple4i var3) {
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

   public final void clampMin(int var1, Tuple4i var2) {
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

   public final void clampMax(int var1, Tuple4i var2) {
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

   public final void absolute(Tuple4i var1) {
      this.x = Math.abs(var1.x);
      this.y = Math.abs(var1.y);
      this.z = Math.abs(var1.z);
      this.w = Math.abs(var1.w);
   }

   public final void clamp(int var1, int var2) {
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

   public final void clampMin(int var1) {
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

   public final void clampMax(int var1) {
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

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new InternalError();
      }
   }
}

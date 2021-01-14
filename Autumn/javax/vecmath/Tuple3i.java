package javax.vecmath;

import java.io.Serializable;

public abstract class Tuple3i implements Serializable, Cloneable {
   static final long serialVersionUID = -732740491767276200L;
   public int x;
   public int y;
   public int z;

   public Tuple3i(int var1, int var2, int var3) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
   }

   public Tuple3i(int[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
   }

   public Tuple3i(Tuple3i var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
   }

   public Tuple3i() {
      this.x = 0;
      this.y = 0;
      this.z = 0;
   }

   public final void set(int var1, int var2, int var3) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
   }

   public final void set(int[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
   }

   public final void set(Tuple3i var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
   }

   public final void get(int[] var1) {
      var1[0] = this.x;
      var1[1] = this.y;
      var1[2] = this.z;
   }

   public final void get(Tuple3i var1) {
      var1.x = this.x;
      var1.y = this.y;
      var1.z = this.z;
   }

   public final void add(Tuple3i var1, Tuple3i var2) {
      this.x = var1.x + var2.x;
      this.y = var1.y + var2.y;
      this.z = var1.z + var2.z;
   }

   public final void add(Tuple3i var1) {
      this.x += var1.x;
      this.y += var1.y;
      this.z += var1.z;
   }

   public final void sub(Tuple3i var1, Tuple3i var2) {
      this.x = var1.x - var2.x;
      this.y = var1.y - var2.y;
      this.z = var1.z - var2.z;
   }

   public final void sub(Tuple3i var1) {
      this.x -= var1.x;
      this.y -= var1.y;
      this.z -= var1.z;
   }

   public final void negate(Tuple3i var1) {
      this.x = -var1.x;
      this.y = -var1.y;
      this.z = -var1.z;
   }

   public final void negate() {
      this.x = -this.x;
      this.y = -this.y;
      this.z = -this.z;
   }

   public final void scale(int var1, Tuple3i var2) {
      this.x = var1 * var2.x;
      this.y = var1 * var2.y;
      this.z = var1 * var2.z;
   }

   public final void scale(int var1) {
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
   }

   public final void scaleAdd(int var1, Tuple3i var2, Tuple3i var3) {
      this.x = var1 * var2.x + var3.x;
      this.y = var1 * var2.y + var3.y;
      this.z = var1 * var2.z + var3.z;
   }

   public final void scaleAdd(int var1, Tuple3i var2) {
      this.x = var1 * this.x + var2.x;
      this.y = var1 * this.y + var2.y;
      this.z = var1 * this.z + var2.z;
   }

   public String toString() {
      return "(" + this.x + ", " + this.y + ", " + this.z + ")";
   }

   public boolean equals(Object var1) {
      try {
         Tuple3i var2 = (Tuple3i)var1;
         return this.x == var2.x && this.y == var2.y && this.z == var2.z;
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
      return (int)(var1 ^ var1 >> 32);
   }

   public final void clamp(int var1, int var2, Tuple3i var3) {
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

   }

   public final void clampMin(int var1, Tuple3i var2) {
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

   }

   public final void clampMax(int var1, Tuple3i var2) {
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

   }

   public final void absolute(Tuple3i var1) {
      this.x = Math.abs(var1.x);
      this.y = Math.abs(var1.y);
      this.z = Math.abs(var1.z);
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

   }

   public final void absolute() {
      this.x = Math.abs(this.x);
      this.y = Math.abs(this.y);
      this.z = Math.abs(this.z);
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new InternalError();
      }
   }
}

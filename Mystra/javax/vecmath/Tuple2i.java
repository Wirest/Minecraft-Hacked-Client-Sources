package javax.vecmath;

import java.io.Serializable;

public abstract class Tuple2i implements Serializable, Cloneable {
   static final long serialVersionUID = -3555701650170169638L;
   public int x;
   public int y;

   public Tuple2i(int var1, int var2) {
      this.x = var1;
      this.y = var2;
   }

   public Tuple2i(int[] var1) {
      this.x = var1[0];
      this.y = var1[1];
   }

   public Tuple2i(Tuple2i var1) {
      this.x = var1.x;
      this.y = var1.y;
   }

   public Tuple2i() {
      this.x = 0;
      this.y = 0;
   }

   public final void set(int var1, int var2) {
      this.x = var1;
      this.y = var2;
   }

   public final void set(int[] var1) {
      this.x = var1[0];
      this.y = var1[1];
   }

   public final void set(Tuple2i var1) {
      this.x = var1.x;
      this.y = var1.y;
   }

   public final void get(int[] var1) {
      var1[0] = this.x;
      var1[1] = this.y;
   }

   public final void get(Tuple2i var1) {
      var1.x = this.x;
      var1.y = this.y;
   }

   public final void add(Tuple2i var1, Tuple2i var2) {
      this.x = var1.x + var2.x;
      this.y = var1.y + var2.y;
   }

   public final void add(Tuple2i var1) {
      this.x += var1.x;
      this.y += var1.y;
   }

   public final void sub(Tuple2i var1, Tuple2i var2) {
      this.x = var1.x - var2.x;
      this.y = var1.y - var2.y;
   }

   public final void sub(Tuple2i var1) {
      this.x -= var1.x;
      this.y -= var1.y;
   }

   public final void negate(Tuple2i var1) {
      this.x = -var1.x;
      this.y = -var1.y;
   }

   public final void negate() {
      this.x = -this.x;
      this.y = -this.y;
   }

   public final void scale(int var1, Tuple2i var2) {
      this.x = var1 * var2.x;
      this.y = var1 * var2.y;
   }

   public final void scale(int var1) {
      this.x *= var1;
      this.y *= var1;
   }

   public final void scaleAdd(int var1, Tuple2i var2, Tuple2i var3) {
      this.x = var1 * var2.x + var3.x;
      this.y = var1 * var2.y + var3.y;
   }

   public final void scaleAdd(int var1, Tuple2i var2) {
      this.x = var1 * this.x + var2.x;
      this.y = var1 * this.y + var2.y;
   }

   public String toString() {
      return "(" + this.x + ", " + this.y + ")";
   }

   public boolean equals(Object var1) {
      try {
         Tuple2i var2 = (Tuple2i)var1;
         return this.x == var2.x && this.y == var2.y;
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
      return (int)(var1 ^ var1 >> 32);
   }

   public final void clamp(int var1, int var2, Tuple2i var3) {
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

   public final void clampMin(int var1, Tuple2i var2) {
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

   public final void clampMax(int var1, Tuple2i var2) {
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

   public final void absolute(Tuple2i var1) {
      this.x = Math.abs(var1.x);
      this.y = Math.abs(var1.y);
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

   }

   public final void clampMin(int var1) {
      if (this.x < var1) {
         this.x = var1;
      }

      if (this.y < var1) {
         this.y = var1;
      }

   }

   public final void clampMax(int var1) {
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

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new InternalError();
      }
   }
}

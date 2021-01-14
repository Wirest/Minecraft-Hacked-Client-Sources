package javax.vecmath;

import java.io.Serializable;

public abstract class Tuple4b implements Serializable, Cloneable {
   static final long serialVersionUID = -8226727741811898211L;
   public byte x;
   public byte y;
   public byte z;
   public byte w;

   public Tuple4b(byte var1, byte var2, byte var3, byte var4) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
      this.w = var4;
   }

   public Tuple4b(byte[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
      this.w = var1[3];
   }

   public Tuple4b(Tuple4b var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      this.w = var1.w;
   }

   public Tuple4b() {
      this.x = 0;
      this.y = 0;
      this.z = 0;
      this.w = 0;
   }

   public String toString() {
      return "(" + (this.x & 255) + ", " + (this.y & 255) + ", " + (this.z & 255) + ", " + (this.w & 255) + ")";
   }

   public final void get(byte[] var1) {
      var1[0] = this.x;
      var1[1] = this.y;
      var1[2] = this.z;
      var1[3] = this.w;
   }

   public final void get(Tuple4b var1) {
      var1.x = this.x;
      var1.y = this.y;
      var1.z = this.z;
      var1.w = this.w;
   }

   public final void set(Tuple4b var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      this.w = var1.w;
   }

   public final void set(byte[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
      this.w = var1[3];
   }

   public boolean equals(Tuple4b var1) {
      try {
         return this.x == var1.x && this.y == var1.y && this.z == var1.z && this.w == var1.w;
      } catch (NullPointerException var3) {
         return false;
      }
   }

   public boolean equals(Object var1) {
      try {
         Tuple4b var2 = (Tuple4b)var1;
         return this.x == var2.x && this.y == var2.y && this.z == var2.z && this.w == var2.w;
      } catch (NullPointerException var3) {
         return false;
      } catch (ClassCastException var4) {
         return false;
      }
   }

   public int hashCode() {
      return (this.x & 255) << 0 | (this.y & 255) << 8 | (this.z & 255) << 16 | (this.w & 255) << 24;
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new InternalError();
      }
   }
}

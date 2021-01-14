package javax.vecmath;

import java.io.Serializable;

public abstract class Tuple3b implements Serializable, Cloneable {
   static final long serialVersionUID = -483782685323607044L;
   public byte x;
   public byte y;
   public byte z;

   public Tuple3b(byte var1, byte var2, byte var3) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
   }

   public Tuple3b(byte[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
   }

   public Tuple3b(Tuple3b var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
   }

   public Tuple3b() {
      this.x = 0;
      this.y = 0;
      this.z = 0;
   }

   public String toString() {
      return "(" + (this.x & 255) + ", " + (this.y & 255) + ", " + (this.z & 255) + ")";
   }

   public final void get(byte[] var1) {
      var1[0] = this.x;
      var1[1] = this.y;
      var1[2] = this.z;
   }

   public final void get(Tuple3b var1) {
      var1.x = this.x;
      var1.y = this.y;
      var1.z = this.z;
   }

   public final void set(Tuple3b var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
   }

   public final void set(byte[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
   }

   public boolean equals(Tuple3b var1) {
      try {
         return this.x == var1.x && this.y == var1.y && this.z == var1.z;
      } catch (NullPointerException var3) {
         return false;
      }
   }

   public boolean equals(Object var1) {
      try {
         Tuple3b var2 = (Tuple3b)var1;
         return this.x == var2.x && this.y == var2.y && this.z == var2.z;
      } catch (NullPointerException var3) {
         return false;
      } catch (ClassCastException var4) {
         return false;
      }
   }

   public int hashCode() {
      return (this.x & 255) << 0 | (this.y & 255) << 8 | (this.z & 255) << 16;
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new InternalError();
      }
   }
}

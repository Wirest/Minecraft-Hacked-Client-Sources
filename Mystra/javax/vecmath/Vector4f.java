package javax.vecmath;

import java.io.Serializable;

public class Vector4f extends Tuple4f implements Serializable {
   static final long serialVersionUID = 8749319902347760659L;

   public Vector4f(float var1, float var2, float var3, float var4) {
      super(var1, var2, var3, var4);
   }

   public Vector4f(float[] var1) {
      super(var1);
   }

   public Vector4f(Vector4f var1) {
      super((Tuple4f)var1);
   }

   public Vector4f(Vector4d var1) {
      super((Tuple4d)var1);
   }

   public Vector4f(Tuple4f var1) {
      super(var1);
   }

   public Vector4f(Tuple4d var1) {
      super(var1);
   }

   public Vector4f(Tuple3f var1) {
      super(var1.x, var1.y, var1.z, 0.0F);
   }

   public Vector4f() {
   }

   public final void set(Tuple3f var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      this.w = 0.0F;
   }

   public final float length() {
      return (float)Math.sqrt((double)(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w));
   }

   public final float lengthSquared() {
      return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
   }

   public final float dot(Vector4f var1) {
      return this.x * var1.x + this.y * var1.y + this.z * var1.z + this.w * var1.w;
   }

   public final void normalize(Vector4f var1) {
      float var2 = (float)(1.0D / Math.sqrt((double)(var1.x * var1.x + var1.y * var1.y + var1.z * var1.z + var1.w * var1.w)));
      this.x = var1.x * var2;
      this.y = var1.y * var2;
      this.z = var1.z * var2;
      this.w = var1.w * var2;
   }

   public final void normalize() {
      float var1 = (float)(1.0D / Math.sqrt((double)(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w)));
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
      this.w *= var1;
   }

   public final float angle(Vector4f var1) {
      double var2 = (double)(this.dot(var1) / (this.length() * var1.length()));
      if (var2 < -1.0D) {
         var2 = -1.0D;
      }

      if (var2 > 1.0D) {
         var2 = 1.0D;
      }

      return (float)Math.acos(var2);
   }
}

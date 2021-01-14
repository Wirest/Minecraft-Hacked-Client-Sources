package javax.vecmath;

import java.io.Serializable;

public class Vector3f extends Tuple3f implements Serializable {
   static final long serialVersionUID = -7031930069184524614L;

   public Vector3f(float var1, float var2, float var3) {
      super(var1, var2, var3);
   }

   public Vector3f(float[] var1) {
      super(var1);
   }

   public Vector3f(Vector3f var1) {
      super((Tuple3f)var1);
   }

   public Vector3f(Vector3d var1) {
      super((Tuple3d)var1);
   }

   public Vector3f(Tuple3f var1) {
      super(var1);
   }

   public Vector3f(Tuple3d var1) {
      super(var1);
   }

   public Vector3f() {
   }

   public final float lengthSquared() {
      return this.x * this.x + this.y * this.y + this.z * this.z;
   }

   public final float length() {
      return (float)Math.sqrt((double)(this.x * this.x + this.y * this.y + this.z * this.z));
   }

   public final void cross(Vector3f var1, Vector3f var2) {
      float var3 = var1.y * var2.z - var1.z * var2.y;
      float var4 = var2.x * var1.z - var2.z * var1.x;
      this.z = var1.x * var2.y - var1.y * var2.x;
      this.x = var3;
      this.y = var4;
   }

   public final float dot(Vector3f var1) {
      return this.x * var1.x + this.y * var1.y + this.z * var1.z;
   }

   public final void normalize(Vector3f var1) {
      float var2 = (float)(1.0D / Math.sqrt((double)(var1.x * var1.x + var1.y * var1.y + var1.z * var1.z)));
      this.x = var1.x * var2;
      this.y = var1.y * var2;
      this.z = var1.z * var2;
   }

   public final void normalize() {
      float var1 = (float)(1.0D / Math.sqrt((double)(this.x * this.x + this.y * this.y + this.z * this.z)));
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
   }

   public final float angle(Vector3f var1) {
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

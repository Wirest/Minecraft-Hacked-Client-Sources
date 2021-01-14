package javax.vecmath;

import java.io.Serializable;

public class Vector3d extends Tuple3d implements Serializable {
   static final long serialVersionUID = 3761969948420550442L;

   public Vector3d(double var1, double var3, double var5) {
      super(var1, var3, var5);
   }

   public Vector3d(double[] var1) {
      super(var1);
   }

   public Vector3d(Vector3d var1) {
      super((Tuple3d)var1);
   }

   public Vector3d(Vector3f var1) {
      super((Tuple3f)var1);
   }

   public Vector3d(Tuple3f var1) {
      super(var1);
   }

   public Vector3d(Tuple3d var1) {
      super(var1);
   }

   public Vector3d() {
   }

   public final void cross(Vector3d var1, Vector3d var2) {
      double var3 = var1.y * var2.z - var1.z * var2.y;
      double var5 = var2.x * var1.z - var2.z * var1.x;
      this.z = var1.x * var2.y - var1.y * var2.x;
      this.x = var3;
      this.y = var5;
   }

   public final void normalize(Vector3d var1) {
      double var2 = 1.0D / Math.sqrt(var1.x * var1.x + var1.y * var1.y + var1.z * var1.z);
      this.x = var1.x * var2;
      this.y = var1.y * var2;
      this.z = var1.z * var2;
   }

   public final void normalize() {
      double var1 = 1.0D / Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
   }

   public final double dot(Vector3d var1) {
      return this.x * var1.x + this.y * var1.y + this.z * var1.z;
   }

   public final double lengthSquared() {
      return this.x * this.x + this.y * this.y + this.z * this.z;
   }

   public final double length() {
      return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
   }

   public final double angle(Vector3d var1) {
      double var2 = this.dot(var1) / (this.length() * var1.length());
      if (var2 < -1.0D) {
         var2 = -1.0D;
      }

      if (var2 > 1.0D) {
         var2 = 1.0D;
      }

      return Math.acos(var2);
   }
}

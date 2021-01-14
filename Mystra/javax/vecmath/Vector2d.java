package javax.vecmath;

import java.io.Serializable;

public class Vector2d extends Tuple2d implements Serializable {
   static final long serialVersionUID = 8572646365302599857L;

   public Vector2d(double var1, double var3) {
      super(var1, var3);
   }

   public Vector2d(double[] var1) {
      super(var1);
   }

   public Vector2d(Vector2d var1) {
      super((Tuple2d)var1);
   }

   public Vector2d(Vector2f var1) {
      super((Tuple2f)var1);
   }

   public Vector2d(Tuple2d var1) {
      super(var1);
   }

   public Vector2d(Tuple2f var1) {
      super(var1);
   }

   public Vector2d() {
   }

   public final double dot(Vector2d var1) {
      return this.x * var1.x + this.y * var1.y;
   }

   public final double length() {
      return Math.sqrt(this.x * this.x + this.y * this.y);
   }

   public final double lengthSquared() {
      return this.x * this.x + this.y * this.y;
   }

   public final void normalize(Vector2d var1) {
      double var2 = 1.0D / Math.sqrt(var1.x * var1.x + var1.y * var1.y);
      this.x = var1.x * var2;
      this.y = var1.y * var2;
   }

   public final void normalize() {
      double var1 = 1.0D / Math.sqrt(this.x * this.x + this.y * this.y);
      this.x *= var1;
      this.y *= var1;
   }

   public final double angle(Vector2d var1) {
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

package javax.vecmath;

import java.io.Serializable;

public class Vector2f extends Tuple2f implements Serializable {
   static final long serialVersionUID = -2168194326883512320L;

   public Vector2f(float var1, float var2) {
      super(var1, var2);
   }

   public Vector2f(float[] var1) {
      super(var1);
   }

   public Vector2f(Vector2f var1) {
      super((Tuple2f)var1);
   }

   public Vector2f(Vector2d var1) {
      super((Tuple2d)var1);
   }

   public Vector2f(Tuple2f var1) {
      super(var1);
   }

   public Vector2f(Tuple2d var1) {
      super(var1);
   }

   public Vector2f() {
   }

   public final float dot(Vector2f var1) {
      return this.x * var1.x + this.y * var1.y;
   }

   public final float length() {
      return (float)Math.sqrt((double)(this.x * this.x + this.y * this.y));
   }

   public final float lengthSquared() {
      return this.x * this.x + this.y * this.y;
   }

   public final void normalize(Vector2f var1) {
      float var2 = (float)(1.0D / Math.sqrt((double)(var1.x * var1.x + var1.y * var1.y)));
      this.x = var1.x * var2;
      this.y = var1.y * var2;
   }

   public final void normalize() {
      float var1 = (float)(1.0D / Math.sqrt((double)(this.x * this.x + this.y * this.y)));
      this.x *= var1;
      this.y *= var1;
   }

   public final float angle(Vector2f var1) {
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

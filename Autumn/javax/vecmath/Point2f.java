package javax.vecmath;

import java.io.Serializable;

public class Point2f extends Tuple2f implements Serializable {
   static final long serialVersionUID = -4801347926528714435L;

   public Point2f(float var1, float var2) {
      super(var1, var2);
   }

   public Point2f(float[] var1) {
      super(var1);
   }

   public Point2f(Point2f var1) {
      super((Tuple2f)var1);
   }

   public Point2f(Point2d var1) {
      super((Tuple2d)var1);
   }

   public Point2f(Tuple2d var1) {
      super(var1);
   }

   public Point2f(Tuple2f var1) {
      super(var1);
   }

   public Point2f() {
   }

   public final float distanceSquared(Point2f var1) {
      float var2 = this.x - var1.x;
      float var3 = this.y - var1.y;
      return var2 * var2 + var3 * var3;
   }

   public final float distance(Point2f var1) {
      float var2 = this.x - var1.x;
      float var3 = this.y - var1.y;
      return (float)Math.sqrt((double)(var2 * var2 + var3 * var3));
   }

   public final float distanceL1(Point2f var1) {
      return Math.abs(this.x - var1.x) + Math.abs(this.y - var1.y);
   }

   public final float distanceLinf(Point2f var1) {
      return Math.max(Math.abs(this.x - var1.x), Math.abs(this.y - var1.y));
   }
}

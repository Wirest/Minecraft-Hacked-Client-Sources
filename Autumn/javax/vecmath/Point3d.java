package javax.vecmath;

import java.io.Serializable;

public class Point3d extends Tuple3d implements Serializable {
   static final long serialVersionUID = 5718062286069042927L;

   public Point3d(double var1, double var3, double var5) {
      super(var1, var3, var5);
   }

   public Point3d(double[] var1) {
      super(var1);
   }

   public Point3d(Point3d var1) {
      super((Tuple3d)var1);
   }

   public Point3d(Point3f var1) {
      super((Tuple3f)var1);
   }

   public Point3d(Tuple3f var1) {
      super(var1);
   }

   public Point3d(Tuple3d var1) {
      super(var1);
   }

   public Point3d() {
   }

   public final double distanceSquared(Point3d var1) {
      double var2 = this.x - var1.x;
      double var4 = this.y - var1.y;
      double var6 = this.z - var1.z;
      return var2 * var2 + var4 * var4 + var6 * var6;
   }

   public final double distance(Point3d var1) {
      double var2 = this.x - var1.x;
      double var4 = this.y - var1.y;
      double var6 = this.z - var1.z;
      return Math.sqrt(var2 * var2 + var4 * var4 + var6 * var6);
   }

   public final double distanceL1(Point3d var1) {
      return Math.abs(this.x - var1.x) + Math.abs(this.y - var1.y) + Math.abs(this.z - var1.z);
   }

   public final double distanceLinf(Point3d var1) {
      double var2 = Math.max(Math.abs(this.x - var1.x), Math.abs(this.y - var1.y));
      return Math.max(var2, Math.abs(this.z - var1.z));
   }

   public final void project(Point4d var1) {
      double var2 = 1.0D / var1.w;
      this.x = var1.x * var2;
      this.y = var1.y * var2;
      this.z = var1.z * var2;
   }
}

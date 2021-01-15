package me.xatzdevelopments.xatz.utils;
public class BlockUtil {

	//by StaticCode
	
	
   double x;
   double y;
   double z;

   public BlockUtil(double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public double getX() {
      return this.x;
   }

   public double getY() {
      return this.y;
   }

   public double getZ() {
      return this.z;
   }

   public void addX(double x) {
      this.x += x;
   }

   public void addY(double y) {
      this.y += y;
   }

   public void addZ(double z) {
      this.z += z;
   }

   public void add(double x, double y, double z) {
      this.y += y;
      this.x += x;
      this.z += z;
   }

   public double distance(BlockUtil Location) {
      double a = Math.abs(Location.getY() - this.y);
      double ba = Math.abs(Location.getZ() - this.z);
      double bb = Math.abs(Location.getX() - this.x);
      double b = Math.sqrt(ba * ba + bb * bb);
      double c = Math.sqrt(a * a + b * b);
      return c;
   }

   public String toPointString() {
      return "(" + this.x + " | " + this.y + " | " + this.z + ")";
   }
}
package me.existdev.exist.utils;

public class Location3D {
   // $FF: synthetic field
   double x;
   // $FF: synthetic field
   double y;
   // $FF: synthetic field
   double z;

   // $FF: synthetic method
   public Location3D(double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   // $FF: synthetic method
   public double getX() {
      return this.x;
   }

   // $FF: synthetic method
   public double getY() {
      return this.y;
   }

   // $FF: synthetic method
   public double getZ() {
      return this.z;
   }

   // $FF: synthetic method
   public void addX(double x) {
      this.x += x;
   }

   // $FF: synthetic method
   public void addY(double y) {
      this.y += y;
   }

   // $FF: synthetic method
   public void addZ(double z) {
      this.z += z;
   }

   // $FF: synthetic method
   public void add(double x, double y, double z) {
      this.y += y;
      this.x += x;
      this.z += z;
   }

   // $FF: synthetic method
   public double distance(Location3D Location) {
      double a = Math.abs(Location.getY() - this.y);
      double ba = Math.abs(Location.getZ() - this.z);
      double bb = Math.abs(Location.getX() - this.x);
      double b = Math.sqrt(ba * ba + bb * bb);
      double c = Math.sqrt(a * a + b * b);
      return c;
   }

   // $FF: synthetic method
   public String toPointString() {
      return "(" + this.x + " | " + this.y + " | " + this.z + ")";
   }
}

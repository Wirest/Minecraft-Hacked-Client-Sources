package me.existdev.exist.utils;

public class Location2D {
   // $FF: synthetic field
   double x;
   // $FF: synthetic field
   double y;

   // $FF: synthetic method
   public Location2D(double x, double y) {
      this.x = x;
      this.y = y;
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
   public double distance(Location2D Location) {
      double a = Math.abs(this.x - Location.getX());
      double b = Math.abs(this.y - Location.getY());
      double c = Math.sqrt(a * a + b * b);
      return c;
   }

   // $FF: synthetic method
   public String getPointString() {
      return "(" + this.x + " | " + this.y + ")";
   }
}

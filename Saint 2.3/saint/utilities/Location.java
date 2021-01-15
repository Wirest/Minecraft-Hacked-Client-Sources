package saint.utilities;

public class Location {
   public double x;
   public double y;
   public double z;

   public Location(double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public void offset(double xoff, double yoff, double zoff) {
      this.x += xoff;
      this.y += yoff;
      this.z += zoff;
   }

   public void rotate(float rot, boolean x, boolean y, boolean z) {
      double radians = Math.toRadians((double)rot);
      if (x) {
         this.y = this.y * Math.cos(radians) - this.z * Math.sin(radians);
         this.z = this.y * Math.sin(radians) + this.z * Math.cos(radians);
      } else if (y) {
         this.z = this.z * Math.cos(radians) - this.x * Math.sin(radians);
         this.x = this.z * Math.sin(radians) + this.x * Math.cos(radians);
      } else if (z) {
         this.x = this.x * Math.cos(radians) - this.y * Math.sin(radians);
         this.y = this.x * Math.sin(radians) + this.y * Math.cos(radians);
      }

   }
}

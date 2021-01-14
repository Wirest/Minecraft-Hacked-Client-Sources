package rip.autumn.utils.pathfinding;

import net.minecraft.util.Vec3;

public final class CustomVec3 {
   private double x;
   private double y;
   private double z;

   public CustomVec3(double x, double y, double z) {
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

   public CustomVec3 addVector(double x, double y, double z) {
      return new CustomVec3(this.x + x, this.y + y, this.z + z);
   }

   public CustomVec3 floor() {
      return new CustomVec3(Math.floor(this.x), Math.floor(this.y), Math.floor(this.z));
   }

   public double squareDistanceTo(CustomVec3 v) {
      return Math.pow(v.x - this.x, 2.0D) + Math.pow(v.y - this.y, 2.0D) + Math.pow(v.z - this.z, 2.0D);
   }

   public CustomVec3 add(CustomVec3 v) {
      return this.addVector(v.getX(), v.getY(), v.getZ());
   }

   public Vec3 mc() {
      return new Vec3(this.x, this.y, this.z);
   }

   public String toString() {
      return "[" + this.x + ";" + this.y + ";" + this.z + "]";
   }
}

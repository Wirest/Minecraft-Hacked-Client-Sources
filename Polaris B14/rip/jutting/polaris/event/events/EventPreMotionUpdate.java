/*    */ package rip.jutting.polaris.event.events;
/*    */ 
/*    */ import rip.jutting.polaris.utils.Location;
/*    */ 
/*    */ public class EventPreMotionUpdate extends rip.jutting.polaris.event.Event { private float yaw;
/*    */   private float pitch;
/*    */   private boolean ground;
/*    */   public double x;
/*    */   public double y;
/*    */   public double z;
/*    */   private static Location location;
/*    */   
/* 13 */   public EventPreMotionUpdate(Location location, float yaw, float pitch, boolean ground, double x, double y, double z) { location = location;
/* 14 */     this.yaw = yaw;
/* 15 */     this.pitch = pitch;
/* 16 */     this.ground = ground;
/* 17 */     this.x = x;
/* 18 */     this.y = y;
/* 19 */     this.z = z;
/*    */   }
/*    */   
/*    */   public double getX() {
/* 23 */     return this.x;
/*    */   }
/*    */   
/*    */   public double getY()
/*    */   {
/* 28 */     return this.y;
/*    */   }
/*    */   
/*    */   public double getZ() {
/* 32 */     return this.z;
/*    */   }
/*    */   
/*    */   public void setX(double x) {
/* 36 */     this.x = x;
/*    */   }
/*    */   
/*    */   public void setY(double y) {
/* 40 */     this.y = y;
/*    */   }
/*    */   
/*    */   public void setZ(double z) {
/* 44 */     this.z = z;
/*    */   }
/*    */   
/*    */   public static Location getLocation() {
/* 48 */     return location;
/*    */   }
/*    */   
/*    */   public void setLocation(Location location) {
/* 52 */     location = location;
/*    */   }
/*    */   
/*    */   public float getYaw() {
/* 56 */     return this.yaw;
/*    */   }
/*    */   
/* 59 */   public void setYaw(float yaw) { this.yaw = yaw; }
/*    */   
/*    */   public float getPitch()
/*    */   {
/* 63 */     return this.pitch;
/*    */   }
/*    */   
/* 66 */   public void setPitch(float pitch) { this.pitch = pitch; }
/*    */   
/*    */   public boolean onGround()
/*    */   {
/* 70 */     return this.ground;
/*    */   }
/*    */   
/* 73 */   public void setGround(boolean ground) { this.ground = ground; }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\event\events\EventPreMotionUpdate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */
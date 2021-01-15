package saint.eventstuff.events;

import saint.eventstuff.Cancellable;
import saint.eventstuff.Event;

public class PreMotion extends Event implements Cancellable {
   private boolean cancel;
   private boolean onGround;
   private float rotationYaw;
   private float rotationPitch;
   private double posX;
   private double posY;
   private double posZ;

   public PreMotion(float rotationYaw, float rotationPitch, double posX, double posY, double posZ, boolean onGround) {
      this.rotationPitch = rotationPitch;
      this.rotationYaw = rotationYaw;
      this.posX = posX;
      this.posY = posY;
      this.posZ = posZ;
      this.onGround = onGround;
   }

   public boolean isOnGround() {
      return this.onGround;
   }

   public void setOnGround(boolean onGround) {
      this.onGround = onGround;
   }

   public double getPosX() {
      return this.posX;
   }

   public void setPosX(double posX) {
      this.posX = posX;
   }

   public double getPosY() {
      return this.posY;
   }

   public void setPosY(double posY) {
      this.posY = posY;
   }

   public double getPosZ() {
      return this.posZ;
   }

   public void setPosZ(double posZ) {
      this.posZ = posZ;
   }

   public float getPitch() {
      return this.rotationPitch;
   }

   public float getYaw() {
      return this.rotationYaw;
   }

   public boolean isCancelled() {
      return this.cancel;
   }

   public void setCancelled(boolean cancel) {
      this.cancel = cancel;
   }

   public void setPitch(float pitch) {
      this.rotationPitch = pitch;
   }

   public void setYaw(float yaw) {
      this.rotationYaw = yaw;
   }
}

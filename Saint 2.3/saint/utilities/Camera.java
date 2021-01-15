package saint.utilities;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Timer;

public class Camera {
   private final Minecraft mc = Minecraft.getMinecraft();
   private Timer timer;
   private double posX;
   private double posY;
   private double posZ;
   private float rotationYaw;
   private float rotationPitch;

   public Camera(Entity entity) {
      if (this.timer == null) {
         this.timer = this.mc.timer;
      }

      this.posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)this.timer.renderPartialTicks;
      this.posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)this.timer.renderPartialTicks;
      this.posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)this.timer.renderPartialTicks;
      this.setRotationYaw(entity.rotationYaw);
      this.setRotationPitch(entity.rotationPitch);
      if (entity instanceof EntityPlayer && Minecraft.getMinecraft().gameSettings.viewBobbing && entity == Minecraft.getMinecraft().thePlayer) {
         EntityPlayer player = (EntityPlayer)entity;
         this.setRotationYaw(this.getRotationYaw() + player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * this.timer.renderPartialTicks);
         this.setRotationPitch(this.getRotationPitch() + player.prevCameraPitch + (player.cameraPitch - player.prevCameraPitch) * this.timer.renderPartialTicks);
      } else if (entity instanceof EntityLivingBase) {
         EntityLivingBase living = (EntityLivingBase)entity;
         this.setRotationYaw(living.rotationYawHead);
      }

   }

   public Camera(Entity entity, double offsetX, double offsetY, double offsetZ, double offsetRotationYaw, double offsetRotationPitch) {
      this.posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)this.timer.renderPartialTicks;
      this.posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)this.timer.renderPartialTicks;
      this.posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)this.timer.renderPartialTicks;
      this.setRotationYaw(entity.rotationYaw);
      this.setRotationPitch(entity.rotationPitch);
      if (entity instanceof EntityPlayer && Minecraft.getMinecraft().gameSettings.viewBobbing && entity == Minecraft.getMinecraft().thePlayer) {
         EntityPlayer player = (EntityPlayer)entity;
         this.setRotationYaw(this.getRotationYaw() + player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * this.timer.renderPartialTicks);
         this.setRotationPitch(this.getRotationPitch() + player.prevCameraPitch + (player.cameraPitch - player.prevCameraPitch) * this.timer.renderPartialTicks);
      }

      this.posX += offsetX;
      this.posY += offsetY;
      this.posZ += offsetZ;
      this.rotationYaw = (float)((double)this.rotationYaw + offsetRotationYaw);
      this.rotationPitch = (float)((double)this.rotationPitch + offsetRotationPitch);
   }

   public Camera(double posX, double posY, double posZ, float rotationYaw, float rotationPitch) {
      this.setPosX(posX);
      this.posY = posY;
      this.posZ = posZ;
      this.setRotationYaw(rotationYaw);
      this.setRotationPitch(rotationPitch);
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

   public float getRotationYaw() {
      return this.rotationYaw;
   }

   public void setRotationYaw(float rotationYaw) {
      this.rotationYaw = rotationYaw;
   }

   public float getRotationPitch() {
      return this.rotationPitch;
   }

   public void setRotationPitch(float rotationPitch) {
      this.rotationPitch = rotationPitch;
   }

   public static float[] getRotation(double posX1, double posY1, double posZ1, double posX2, double posY2, double posZ2) {
      float[] rotation = new float[2];
      double diffX = posX2 - posX1;
      double diffZ = posZ2 - posZ1;
      double diffY = posY2 - posY1;
      double dist = Math.sqrt(diffZ * diffZ + diffX * diffX);
      double pitch = -Math.toDegrees(Math.atan(diffY / dist));
      rotation[1] = (float)pitch;
      double yaw = 0.0D;
      if (diffZ >= 0.0D && diffX >= 0.0D) {
         yaw = Math.toDegrees(-Math.atan(diffX / diffZ));
      } else if (diffZ >= 0.0D && diffX <= 0.0D) {
         yaw = Math.toDegrees(-Math.atan(diffX / diffZ));
      } else if (diffZ <= 0.0D && diffX >= 0.0D) {
         yaw = -90.0D + Math.toDegrees(Math.atan(diffZ / diffX));
      } else if (diffZ <= 0.0D && diffX <= 0.0D) {
         yaw = 90.0D + Math.toDegrees(Math.atan(diffZ / diffX));
      }

      rotation[0] = (float)yaw;
      return rotation;
   }
}

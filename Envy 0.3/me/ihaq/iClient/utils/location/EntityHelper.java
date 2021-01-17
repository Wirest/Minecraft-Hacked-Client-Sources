package me.ihaq.iClient.utils.location;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class EntityHelper
{
  public static float[] getRotations(Entity ent)
  {
    double x = ent.posX;
    double z = ent.posZ;
    double y = ent.boundingBox.maxY - 4.5D;
    return getRotationFromPosition(x, z, y);
  }
  
  public static float[] getRotationsforBow(Entity ent)
  {
    double x = ent.posX;
    double z = ent.posZ;
    double y = ent.boundingBox.maxY;
    return getRotationFromPosition(x, z, y + 2.0D);
  }
  
  public static float[] getRotationFromPosition(double x, double z, double y)
  {
    Minecraft.getMinecraft();
    double xDiff = x - Minecraft.thePlayer.posX;
    Minecraft.getMinecraft();
    double zDiff = z - Minecraft.thePlayer.posZ;
    Minecraft.getMinecraft();
    Minecraft.getMinecraft();
    double yDiff = y - Minecraft.thePlayer.posY + Minecraft.thePlayer.getEyeHeight() - -1.0D;
    double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
    float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
    float pitch = (float)-(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);
    return new float[] { yaw, pitch };
  }
  
  public static float getYawChangeToEntity(Entity entity)
  {
    double deltaX = entity.posX - Minecraft.thePlayer.posX;
    double deltaZ = entity.posZ - Minecraft.thePlayer.posZ;
    double yawToEntity1 = 0.0D;
    if ((deltaZ < 0.0D) && (deltaX < 0.0D)) {
      yawToEntity1 = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
    } else if ((deltaZ < 0.0D) && (deltaX > 0.0D)) {
      double d1 = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
    } else {
      Math.toDegrees(-Math.atan(deltaX / deltaZ));
    }
    return MathHelper.wrapAngleTo180_float(-(Minecraft.thePlayer.rotationYaw - (float)yawToEntity1));
  }
  
  public static float getPitchChangeToEntity(Entity entity)
  {
    double deltaX = entity.posX - Minecraft.thePlayer.posX;
    double deltaZ = entity.posZ - Minecraft.thePlayer.posZ;
    double deltaY = entity.posY - 1.6D + entity.getEyeHeight() - Minecraft.thePlayer.posY;
    double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
    double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
    return -MathHelper.wrapAngleTo180_float(Minecraft.thePlayer.rotationPitch - (float)pitchToEntity);
  }
  
  public static float[] getAngles(Entity e)
  {
    return new float[] { getYawChangeToEntity(e) + Minecraft.thePlayer.rotationYaw, 
      getPitchChangeToEntity(e) + Minecraft.thePlayer.rotationPitch };
  }
  
  public static double getDirectionCheckVal(Entity e, Vec3 lookVec)
  {
    return directionCheck(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + Minecraft.thePlayer.getEyeHeight(), 
      Minecraft.thePlayer.posZ, lookVec.xCoord, lookVec.yCoord, lookVec.zCoord, e.posX, 
      e.posY + e.height / 2.0D, e.posZ, e.width, e.height, 5.0D);
  }
  
  public static double[] getRotationToEntity(Entity entity)
  {
    Minecraft.getMinecraft();
    double pX = Minecraft.thePlayer.posX;
    Minecraft.getMinecraft();
    double posY = Minecraft.thePlayer.posY;
    Minecraft.getMinecraft();
    double pY = posY + Minecraft.thePlayer.getEyeHeight();
    Minecraft.getMinecraft();
    double pZ = Minecraft.thePlayer.posZ;
    double eX = entity.posX;
    double eY = entity.posY + entity.height / 2.0F;
    double eZ = entity.posZ;
    double dX = pX - eX;
    double dY = pY - eY;
    double dZ = pZ - eZ;
    double dH = Math.sqrt(Math.pow(dX, 2.0D) + Math.pow(dZ, 2.0D));
    double yaw = Math.toDegrees(Math.atan2(dZ, dX)) + 90.0D;
    double pitch = Math.toDegrees(Math.atan2(dH, dY));
    return new double[] { yaw, 90.0D - pitch };
  }
  
  public static boolean isWithingFOV(Entity en, float angle)
  {
    angle = (float)(angle * 0.5D);
    Minecraft.getMinecraft();
    double a = Minecraft.thePlayer.rotationYaw;
    double angleDifference = angleDifference(a, getRotationToEntity(en)[0]);
    return ((angleDifference > 0.0D) && (angleDifference < angle)) || (
      (-angle < angleDifference) && (angleDifference < 0.0D));
  }
  
  private static double directionCheck(double sourceX, double sourceY, double sourceZ, double dirX, double dirY, double dirZ, double targetX, double targetY, double targetZ, double targetWidth, double targetHeight, double precision)
  {
    double dirLength = Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
    if (dirLength == 0.0D) {
      dirLength = 1.0D;
    }
    double dX = targetX - sourceX;
    double dY = targetY - sourceY;
    double dZ = targetZ - sourceZ;
    double targetDist = Math.sqrt(dX * dX + dY * dY + dZ * dZ);
    double xPrediction = targetDist * dirX / dirLength;
    double yPrediction = targetDist * dirY / dirLength;
    double zPrediction = targetDist * dirZ / dirLength;
    double off = 0.0D;
    off += Math.max(Math.abs(dX - xPrediction) - (targetWidth / 2.0D + precision), 0.0D);
    off += Math.max(Math.abs(dZ - zPrediction) - (targetWidth / 2.0D + precision), 0.0D);
    off += Math.max(Math.abs(dY - yPrediction) - (targetHeight / 2.0D + precision), 0.0D);
    if (off > 1.0D) {
      off = Math.sqrt(off);
    }
    return off;
  }
  
  public static double angleDifference(double a, double b)
  {
    return ((a - b) % 360.0D + 540.0D) % 360.0D - 180.0D;
  }
  
  public static float[] faceTarget(Entity target, float p_70625_2_, float p_70625_3_, boolean miss)
  {
    double var4 = target.posX - Minecraft.thePlayer.posX;
    double var5 = target.posZ - Minecraft.thePlayer.posZ;
    if ((target instanceof EntityLivingBase))
    {
      EntityLivingBase var6 = (EntityLivingBase)target;
      var4 = var6.posY + var6.getEyeHeight() - (Minecraft.thePlayer.posY + Minecraft.thePlayer.getEyeHeight());
    }
    else
    {
      var4 = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0D - (
        Minecraft.thePlayer.posY + Minecraft.thePlayer.getEyeHeight());
    }
    Random rnd = new Random();
    float offset = miss ? rnd.nextInt(15) * 0.25F + 5.0F : 0.0F;
    double var7 = MathHelper.sqrt_double(var4 * var4 + var5 * var5);
    float var8 = (float)(Math.atan2(var5 + offset, var4) * 180.0D / 3.141592653589793D) - 90.0F;
    float var9 = (float)-(Math.atan2(var4 - ((target instanceof EntityPlayer) ? 0.5F : 0.0F) + offset, var7) * 
      180.0D / 3.141592653589793D);
    float pitch = changeRotation(Minecraft.thePlayer.rotationPitch, var9, p_70625_3_);
    float yaw = changeRotation(Minecraft.thePlayer.rotationYaw, var8, p_70625_2_);
    return new float[] { yaw, pitch };
  }
  
  public static float changeRotation(float p_70663_1_, float p_70663_2_, float p_70663_3_)
  {
    float var4 = MathHelper.wrapAngleTo180_float(p_70663_2_ - p_70663_1_);
    if (var4 > p_70663_3_) {
      var4 = p_70663_3_;
    }
    if (var4 < -p_70663_3_) {
      var4 = -p_70663_3_;
    }
    return p_70663_1_ + var4;
  }
}

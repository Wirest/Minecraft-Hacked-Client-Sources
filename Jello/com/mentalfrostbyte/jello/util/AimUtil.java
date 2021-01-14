package com.mentalfrostbyte.jello.util;

import java.util.Random;

import com.mentalfrostbyte.jello.main.Jello;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class AimUtil {
	public static float[] getBlockRotations(int x, int y, int z)
	  {
	    //Minecraft mc = Wrapper.getMinecraft();
	    Entity temp = new EntitySnowball(Jello.core.world());
	    temp.posX = (x + 0.5D);
	    temp.posY = (y + 0.5D);
	    temp.posZ = (z + 0.5D);
	    return getAngles(temp);
	  }
	  
	  public static float[] getAngles(Entity e)
	  {
	    return new float[] { getYawChangeToEntity(e) + Jello.core.player().rotationYaw, getPitchChangeToEntity(e) + Jello.core.player().rotationPitch };
	  }
	  
	 /* public static float[] getRotations(Location location)
	  {
	    double diffX = location.getX() + 0.5D - Jello.core.player().posX;
	    double diffZ = location.getZ() + 0.5D - Jello.core.player().posZ;
	    double diffY = (location.getY() + 0.5D) / 2.0D - (Jello.core.player().posY + Jello.core.player().getEyeHeight());
	    
	    double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
	    float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
	    float pitch = (float)-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D);
	    
	    return new float[] { yaw, pitch };
	  }*/
	  
	  /*public static float[] getRotationsAAC(Location location)
	  {
	    double diffX = location.getX() + 0.5D - Jello.core.player().posX;
	    double diffZ = location.getZ() + 0.5D - Jello.core.player().posZ;
	    double diffY = (location.getY() + 0.5D) / 2.0D - (Jello.core.player().posY + Jello.core.player().getEyeHeight());
	    
	    double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
	    float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) * getRandom(-5, 5) - 90.0F;
	    float pitch = (float)-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D) * getRandom(-20, 20);
	    
	    return new float[] { yaw, pitch };
	  }*/
	  
	  public static int getRandom(int min, int max)
	  {
	    return min + new Random().nextInt(max - min);
	  }
	  
	  public static float[] getRotations(Entity entity)
	  {
	    if (entity == null) {
	      return null;
	    }
	    double diffX = entity.posX - Jello.core.player().posX;
	    double diffZ = entity.posZ - Jello.core.player().posZ;
	    double diffY;
	    if ((entity instanceof EntityLivingBase))
	    {
	      EntityLivingBase elb = (EntityLivingBase)entity;
	      diffY = elb.posY + (
	        elb.getEyeHeight() - 0.4D) - (
	        Jello.core.player().posY + Jello.core.player()
	        .getEyeHeight());
	    }
	    else
	    {
	      diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 
	        2.0D - (
	        Jello.core.player().posY + Jello.core.player()
	        .getEyeHeight());
	    }
	    double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
	    float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
	    float pitch = (float)-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D);
	    
	    return new float[] { yaw, pitch };
	  }
	  
	  public static float[] getRotationsAttacker(Entity ent) {
	        double x = ent.posX;
	        double z = ent.posZ;
	        double y = ent.boundingBox.maxY - 4.5;
	        return getRotationFromPosition(x, z, y);
	    }
	  public static float[] getRotationFromPosition(double x, double z, double y) {
	        double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
	        double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
	        double yDiff = y - Minecraft.getMinecraft().thePlayer.posY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight();
	        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
	        float yaw = (float)((double)((float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f) + Math.random() + Math.random() + Math.random() + Math.random() + Math.random() + Math.random());
	        float pitch = (float)(- Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793 + Math.random() + Math.random());
	        return new float[]{yaw, pitch};
	    }
	  public static float[] getRotationsAAC(Entity entity)
	  {
	    if (entity == null) {
	      return null;
	    }
	    double diffX = entity.posX - Jello.core.player().posX;
	    double diffZ = entity.posZ - Jello.core.player().posZ;
	    double diffY;
	    if ((entity instanceof EntityLivingBase))
	    {
	      EntityLivingBase elb = (EntityLivingBase)entity;
	      diffY = elb.posY + (
	        elb.getEyeHeight() - 0.4D) - (
	        Jello.core.player().posY + Jello.core.player()
	        .getEyeHeight());
	    }
	    else
	    {
	      diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 
	        2.0D - (
	        Jello.core.player().posY + Jello.core.player()
	        .getEyeHeight());
	    }
	    double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
	    float yaw = ((float)(Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F) * getRandom(-5, 5);
	    float pitch = (float)-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D) * getRandom(-15, 15);
	    
	    return new float[] { yaw, pitch };
	  }
	  
	  public static float getYawChangeToEntity(Entity entity)
	  {
	    double deltaX = entity.posX - Jello.core.player().posX;
	    double deltaZ = entity.posZ - Jello.core.player().posZ;
	    double yawToEntity;
	    if ((deltaZ < 0.0D) && (deltaX < 0.0D))
	    {
	      yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
	    }
	    else
	    {
	      if ((deltaZ < 0.0D) && (deltaX > 0.0D)) {
	        yawToEntity = -90.0D + 
	          Math.toDegrees(Math.atan(deltaZ / deltaX));
	      } else {
	        yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
	      }
	    }
	    return 
	      MathHelper.wrapAngleTo180_float(-(Jello.core.player().rotationYaw - (float)yawToEntity));
	  }
	  
	  public static float getPitchChangeToEntity(Entity entity)
	  {
	    double deltaX = entity.posX - Jello.core.player().posX;
	    double deltaZ = entity.posZ - Jello.core.player().posZ;
	    double deltaY = entity.posY - 1.6D + entity.getEyeHeight() - 0.4D - 
	      Jello.core.player().posY;
	    double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * 
	      deltaZ);
	    
	    double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
	    
	    return -MathHelper.wrapAngleTo180_float(Jello.core.player().rotationPitch - 
	      (float)pitchToEntity);
	  }
	  
	  public static float[] getBlockRotations(int x, int y, int z, EnumFacing facing)
	  {
	    Entity temp = new EntitySnowball(Jello.core.world());
	    temp.posX = (x + 0.5D);
	    temp.posY = (y + 0.5D);
	    temp.posZ = (z + 0.5D);
	    return getAngles(temp);
	  }
	  
	  public static float[] getRotationsBlock(BlockPos pos) {
	        Minecraft mc = Minecraft.getMinecraft();
	        double d0 = pos.getX() - mc.thePlayer.posX;
	        double d1 = pos.getY() - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight());
	        double d2 = pos.getZ() - mc.thePlayer.posZ;
	        double d3 = (double) MathHelper.sqrt_double(d0 * d0 + d2 * d2);
	        float f = (float) (Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
	        float f1 = (float) (-(Math.atan2(d1, d3) * 180.0D / Math.PI));
	        return new float[]{f, f1};
	    }

	  public static void faceEntity(EntityLivingBase entity) {
	        float[] rotations = getRotationsAttacker(entity);
	        if (rotations != null) {
	            Minecraft.getMinecraft().thePlayer.rotationYaw = rotations[0];
	            Minecraft.getMinecraft().thePlayer.rotationPitch = rotations[1] - 8.0f;
	        }
	    }
}

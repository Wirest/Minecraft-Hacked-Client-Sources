package com.mentalfrostbyte.jello.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RotationUtil {
    public static float[] faceEntity(Entity p_70625_1_, float p_70625_2_, float p_70625_3_) {
        double var6;
        double var4 = p_70625_1_.posX - Minecraft.getMinecraft().thePlayer.posX;
        double var8 = p_70625_1_.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        if (p_70625_1_ instanceof EntityLivingBase) {
            EntityLivingBase var14 = (EntityLivingBase)p_70625_1_;
            var6 = var14.posY + (double)var14.getEyeHeight() - (Minecraft.getMinecraft().thePlayer.posY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight());
        } else {
            var6 = (p_70625_1_.getEntityBoundingBox().minY + p_70625_1_.getEntityBoundingBox().maxY) / 2.0 - (Minecraft.getMinecraft().thePlayer.posY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight());
        }
        double var141 = MathHelper.sqrt_double(var4 * var4 + var8 * var8);
        float yaw = (float)(Math.atan2(var8, var4) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(- Math.atan2(var6, var141) * 180.0 / 3.141592653589793);
        return new float[]{yaw, pitch};
    }

    public static float[] getRotations(Entity entityIn) {
        double result;
        double x = entityIn.posX - Minecraft.getMinecraft().thePlayer.posX;
        double z = entityIn.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        if (entityIn instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase)entityIn;
            result = entity.posY + (double)entity.getEyeHeight() - (Minecraft.getMinecraft().thePlayer.posY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight());
        } else {
            result = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0 - (Minecraft.getMinecraft().thePlayer.posY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight());
        }
        double var141 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(- Math.atan2(result, var141) * 180.0 / 3.141592653589793);
        return new float[]{yaw, pitch};
    }

    public static float[] faceTileEntity(TileEntityChest p_70625_1_, float p_70625_2_, float p_70625_3_) {
        double var4 = (double)p_70625_1_.getPos().getX() - Minecraft.getMinecraft().thePlayer.posX + 0.25;
        double var8 = (double)p_70625_1_.getPos().getZ() - Minecraft.getMinecraft().thePlayer.posZ + 0.25;
        TileEntityChest var14 = p_70625_1_;
        double var6 = (double)var14.getPos().getY() + 0.5 - (Minecraft.getMinecraft().thePlayer.posY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight()) + 0.22;
        double var141 = MathHelper.sqrt_double(var4 * var4 + var8 * var8);
        float yaw = (float)(Math.atan2(var8, var4) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(- Math.atan2(var6, var141) * 180.0 / 3.141592653589793);
        return new float[]{yaw, pitch};
    }

    public void faceEntityHard(Entity p_70625_1_, float p_70625_2_, float p_70625_3_) {
        double var6;
        double var4 = p_70625_1_.posX - Minecraft.getMinecraft().thePlayer.posX;
        double var8 = p_70625_1_.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        if (p_70625_1_ instanceof EntityLivingBase) {
            EntityLivingBase var14 = (EntityLivingBase)p_70625_1_;
            var6 = var14.posY + (double)var14.getEyeHeight() - (Minecraft.getMinecraft().thePlayer.posY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight());
        } else {
            var6 = (p_70625_1_.getEntityBoundingBox().minY + p_70625_1_.getEntityBoundingBox().maxY) / 2.0 - (Minecraft.getMinecraft().thePlayer.posY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight());
        }
        double var141 = MathHelper.sqrt_double(var4 * var4 + var8 * var8);
        float var12 = (float)(Math.atan2(var8, var4) * 180.0 / 3.141592653589793) - 90.0f;
        float var13 = (float)(- Math.atan2(var6, var141) * 180.0 / 3.141592653589793);
        Minecraft.getMinecraft().thePlayer.rotationPitch = RotationUtil.updateRotation(Minecraft.getMinecraft().thePlayer.rotationPitch, var13, p_70625_3_);
        Minecraft.getMinecraft().thePlayer.rotationYaw = RotationUtil.updateRotation(Minecraft.getMinecraft().thePlayer.rotationYaw, var12, p_70625_2_);
    }

    public static float updateRotation(float p_70663_1_, float p_70663_2_, float p_70663_3_) {
        float var4 = MathHelper.wrapAngleTo180_float(p_70663_2_ - p_70663_1_);
        if (var4 > p_70663_3_) {
            var4 = p_70663_3_;
        }
        if (var4 < - p_70663_3_) {
            var4 = - p_70663_3_;
        }
        return p_70663_1_ + var4;
    }

    public static float[] getRotationFromPosition(double x, double z, double y)
    {
      double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
      double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
      double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - 1.2D;
      
      double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
      float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
      float pitch = (float)-(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);
      return new float[] { yaw, pitch };
    }


    public static float[] getRotations(Vec3 origin, Vec3 position)
    {
      Vec3 difference = position.subtract(origin);
      
      double distance = difference.flat().lengthVector();
      
      float yaw = (float)Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0F;
      
      float pitch = (float)-Math.toDegrees(Math.atan2(difference.yCoord, distance));
      
      return new float[] { yaw, pitch };
    }
    public static float[] getRotations(Vec3 position)
    {
      return getRotations(Minecraft.getMinecraft().thePlayer.getPositionVector().addVector(0.0D, Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0.0D), position);
    }
    public static float getAngleDifference(final float alpha, final float beta) {
        final float phi = Math.abs(beta - alpha) % 360.0f;
        return (phi > 180.0f) ? (360.0f - phi) : phi;
    }
    
    public static float getDistance(final float[] original, final float[] rotations) {
        return (float)Math.hypot(getAngleDifference(original[0], rotations[0]), getAngleDifference(original[1], rotations[1]));
    }
    
    public float getDistance(final Vec3 vector) {
        final float[] rotations = this.getRotations(Minecraft.getMinecraft().thePlayer.getPositionVector().addVector(0.0, Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0.0), vector);
        final float[] view = { Minecraft.getMinecraft().thePlayer.rotationYaw % 360.0f, Minecraft.getMinecraft().thePlayer.rotationPitch };
        return this.getDistance(view, rotations);
    }

    public static float getDistance(final float[] viewRotations, final Entity entity) {
        final float[] rotations = getRotations(entity);
        return getDistance(viewRotations, rotations);
    }
    
    public float getDistance(final Entity entity) {
        final float[] rotations = this.getRotations(entity);
        final float[] view = { Minecraft.getMinecraft().thePlayer.rotationYaw, Minecraft.getMinecraft().thePlayer.rotationPitch };
        return this.getDistance(view, rotations);
    }

	
}


// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.tileentity.TileEntity;

public class CombatUtils
{
    public static float yaw;
    public static float tyaw;
    public static float pitch;
    public static float tpitch;
    public static float blockYaw;
    public static float blockPitch;
    
    public static void faceTileEntity(final TileEntity target, final float p_70625_2_, final float p_70625_3_) {
        final double var4 = target.getPos().getX() + 0.5 - Wrapper.mc.thePlayer.posX;
        final double var5 = target.getPos().getZ() + 0.5 - Wrapper.mc.thePlayer.posZ;
        final double var6 = target.getPos().getY() - (Wrapper.mc.thePlayer.posY + 1.0);
        final double var7 = MathHelper.sqrt_double(var4 * var4 + var5 * var5);
        final float var8 = (float)(Math.atan2(var5, var4) * 180.0 / 3.141592653589793) - 90.0f;
        final float var9 = (float)(-(Math.atan2(var6, var7) * 180.0 / 3.141592653589793));
        CombatUtils.tpitch = changeRotation(Wrapper.mc.thePlayer.rotationPitch, var9, p_70625_3_);
        CombatUtils.tyaw = changeRotation(Wrapper.mc.thePlayer.rotationYaw, var8, p_70625_2_);
        Wrapper.mc.thePlayer.rotationPitch = CombatUtils.tpitch;
        Wrapper.mc.thePlayer.rotationYaw = CombatUtils.tyaw;
    }
    
    public static void faceBlock(final BlockPos block, final float p_70625_2_, final float p_70625_3_) {
        final double var4 = block.getX() - Wrapper.mc.thePlayer.posX;
        final double var5 = block.getZ() - Wrapper.mc.thePlayer.posZ;
        final double var6 = block.getY() + block.getY() - 6.0 - (Wrapper.mc.thePlayer.posY + Wrapper.mc.thePlayer.getEyeHeight());
        final double var7 = MathHelper.sqrt_double(var4 * var4 + var5 * var5);
        final float var8 = (float)(Math.atan2(var5, var4) * 180.0 / 3.141592653589793) - 90.0f;
        final float var9 = (float)(-(Math.atan2(var6, var7) * 180.0 / 3.141592653589793));
        CombatUtils.blockPitch = changeRotation(Wrapper.mc.thePlayer.rotationPitch, var9, p_70625_3_);
        CombatUtils.blockYaw = changeRotation(Wrapper.mc.thePlayer.rotationYaw, var8, p_70625_2_);
    }
    
    public static double angleDifference(final double a, final double b) {
        return ((a - b) % 360.0 + 540.0) % 360.0 - 180.0;
    }
    
    public static float[] faceTarget(final Entity target, final float p_70625_2_, final float p_70625_3_) {
        final double var4 = target.posX - Wrapper.getPlayer().posX;
        final double var5 = target.posZ - Wrapper.getPlayer().posZ;
        double var7;
        if (target instanceof EntityLivingBase) {
            final EntityLivingBase var6 = (EntityLivingBase)target;
            var7 = var6.posY + var6.getEyeHeight() - (Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight());
        }
        else {
            var7 = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0 - (Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight());
        }
        final double var8 = MathHelper.sqrt_double(var4 * var4 + var5 * var5);
        final float var9 = (float)(Math.atan2(var5, var4) * 180.0 / 3.141592653589793) - 90.0f;
        final float var10 = (float)(-(Math.atan2(var7 - ((target instanceof EntityPlayer) ? 0.5f : 0.0f), var8) * 180.0 / 3.141592653589793));
        final float pitch = changeRotation(Wrapper.getPlayer().rotationPitch, var10, p_70625_3_);
        final float yaw = changeRotation(Wrapper.getPlayer().rotationYaw, var9, p_70625_2_);
        return new float[] { yaw, pitch };
    }
    
    public static float changeRotation(final float p_70663_1_, final float p_70663_2_, final float p_70663_3_) {
        float var4 = MathHelper.wrapAngleTo180_float(p_70663_2_ - p_70663_1_);
        if (var4 > p_70663_3_) {
            var4 = p_70663_3_;
        }
        if (var4 < -p_70663_3_) {
            var4 = -p_70663_3_;
        }
        return p_70663_1_ + var4;
    }
    
    public boolean isCloser(final Entity now, final Entity first, final float error) {
        if (first.getClass().isAssignableFrom(now.getClass())) {
            return this.getDistanceToEntity(Minecraft.getMinecraft().thePlayer, now) < this.getDistanceToEntity(Minecraft.getMinecraft().thePlayer, first);
        }
        return this.getDistanceToEntity(Minecraft.getMinecraft().thePlayer, now) < this.getDistanceToEntity(Minecraft.getMinecraft().thePlayer, first) + error;
    }
    
    public float getDistanceToEntity(final Entity from, final Entity to) {
        return from.getDistanceToEntity(to);
    }
    
    public static float getDistanceBetweenAngles(final float par1, final float par2) {
        float angle = Math.abs(par1 - par2) % 360.0f;
        if (angle > 180.0f) {
            angle = 360.0f - angle;
        }
        return angle;
    }
    
    public static float[] getRotations(final Entity ent) {
        final double x = ent.posX;
        final double z = ent.posZ;
        final double y = ent.boundingBox.maxY - 4.0;
        return getRotationFromPosition(x, z, y);
    }
    
    public static float angleDifference(final float a, final float b) {
        return ((a - b) % 360.0f + 540.0f) % 360.0f - 180.0f;
    }
    
    public static float[] getRotationFromPosition(final double x, final double z, final double y) {
        final double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
        final double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
        final double yDiff = y - Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight();
        final double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        final float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793));
        return new float[] { yaw, pitch };
    }
}

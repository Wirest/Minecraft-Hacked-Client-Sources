// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.utils;

import net.minecraft.util.MathHelper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.MovingObjectPosition;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;

public class KillAuraUtils
{
    public static final KillAuraUtils INSTANCE;
    private final Minecraft MC;
    private float yaw;
    private float pitch;
    private static boolean pitching;
    
    static {
        INSTANCE = new KillAuraUtils();
    }
    
    public KillAuraUtils() {
        this.MC = Minecraft.getMinecraft();
    }
    
    public Entity targetCast(final double range, final Entity entity) {
        final Entity var2 = Minecraft.thePlayer;
        final Vec3 var3 = entity.getPositionVector().add(new Vec3(0.0, entity.getEyeHeight(), 0.0));
        final Vec3 var4 = Minecraft.thePlayer.getPositionVector().add(new Vec3(0.0, Minecraft.thePlayer.getEyeHeight(), 0.0));
        Vec3 var5 = null;
        final float var6 = 1.0f;
        final AxisAlignedBB a = Minecraft.thePlayer.getEntityBoundingBox().addCoord(var3.xCoord - var4.xCoord, var3.yCoord - var4.yCoord, var3.zCoord - var4.zCoord).expand(var6, var6, var6);
        final List var7 = this.MC.theWorld.getEntitiesWithinAABBExcludingEntity(var2, a);
        double var8 = range + 0.5;
        Entity b = null;
        for (int var9 = 0; var9 < var7.size(); ++var9) {
            final Entity var10 = var7.get(var9);
            if (var10.canBeCollidedWith()) {
                final float var11 = var10.getCollisionBorderSize();
                final AxisAlignedBB var12 = var10.getEntityBoundingBox().expand(var11, var11, var11);
                final MovingObjectPosition var13 = var12.calculateIntercept(var4, var3);
                if (var12.isVecInside(var4)) {
                    if (0.0 < var8 || var8 == 0.0) {
                        b = var10;
                        var5 = ((var13 == null) ? var4 : var13.hitVec);
                        var8 = 0.0;
                    }
                }
                else if (var13 != null) {
                    final double var14 = var4.distanceTo(var13.hitVec);
                    if (var14 < var8 || var8 == 0.0) {
                        b = var10;
                        var5 = var13.hitVec;
                        var8 = var14;
                    }
                }
            }
        }
        return b;
    }
    
    public boolean canAttack(final Entity entity) {
        return (entity instanceof EntityAnimal || entity instanceof EntityPlayer || entity instanceof EntityMob) && entity != Minecraft.thePlayer;
    }
    
    public float[] getRotations(final Vec3 vec) {
        Minecraft.getMinecraft();
        final double posX = Minecraft.thePlayer.posX;
        Minecraft.getMinecraft();
        final double posY = Minecraft.thePlayer.posY;
        Minecraft.getMinecraft();
        final double y = posY + Minecraft.thePlayer.getEyeHeight();
        Minecraft.getMinecraft();
        final Vec3 eyesPos = new Vec3(posX, y, Minecraft.thePlayer.posZ);
        final double diffX = vec.xCoord - eyesPos.xCoord;
        final double diffY = vec.yCoord - eyesPos.yCoord;
        final double diffZ = vec.zCoord - eyesPos.zCoord;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[] { yaw, MathHelper.wrapAngleTo180_float(pitch) };
    }
    
    public Vec3 getCenter(final AxisAlignedBB bb) {
        return new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.minY + (bb.maxY - bb.minY) * 0.8, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
    }
    
    public float[] faceEntitySmooth(final double curYaw, final double curPitch, final double intendedYaw, final double intendedPitch, final double yawSpeed, final double pitchSpeed) {
        final float yaw = this.updateRotation((float)curYaw, (float)intendedYaw, (float)yawSpeed);
        final float pitch = this.updateRotation((float)curPitch, (float)intendedPitch, (float)pitchSpeed);
        return new float[] { yaw, pitch };
    }
    
    public float updateRotation(final float current, final float intended, final float factor) {
        float var4 = MathHelper.wrapAngleTo180_float(intended - current);
        if (var4 > factor) {
            var4 = factor;
        }
        if (var4 < -factor) {
            var4 = -factor;
        }
        return current + var4;
    }
    
    public static boolean getPitching() {
        return KillAuraUtils.pitching;
    }
    
    public static void setPitching(final boolean pitchig) {
        KillAuraUtils.pitching = pitchig;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }
}

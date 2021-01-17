/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.util;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;

public class CombatUtil {
    public static Minecraft mc = Minecraft.getMinecraft();

    public static float[] getRotations(Entity entity) {
        double pX = Minecraft.getMinecraft().thePlayer.posX;
        double pY = Minecraft.getMinecraft().thePlayer.posY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight();
        double pZ = Minecraft.getMinecraft().thePlayer.posZ;
        double eX = entity.posX;
        double eY = entity.posY + (double)(entity.height / 2.0f);
        double eZ = entity.posZ;
        double dX = pX - eX;
        double dY = pY - eY;
        double dZ = pZ - eZ;
        double dH = Math.sqrt(Math.pow(dX, 2.0) + Math.pow(dZ, 2.0));
        double yaw = Math.toDegrees(Math.atan2(dZ, dX)) + 90.0;
        double pitch = Math.toDegrees(Math.atan2(dH, dY));
        return new float[]{(float)yaw, (float)(90.0 - pitch)};
    }

    private static float[] getDirectionToEntity(Entity var0) {
        return new float[]{CombatUtil.getYaw(var0) + CombatUtil.mc.thePlayer.rotationYaw, CombatUtil.getPitch(var0) + CombatUtil.mc.thePlayer.rotationPitch};
    }

    public static float[] getDirectionToBlock(double var0, double var1, double var2, EnumFacing var3) {
        EntityEgg var4 = new EntityEgg(CombatUtil.mc.theWorld);
        var4.posX = var0 + 0.5;
        var4.posY = var1 + 0.5;
        var4.posZ = var2 + 0.5;
        var4.posX += (double)var3.getDirectionVec().getX() * 0.25;
        var4.posY += (double)var3.getDirectionVec().getY() * 0.25;
        var4.posZ += (double)var3.getDirectionVec().getZ() * 0.25;
        return CombatUtil.getDirectionToEntity(var4);
    }

    public static float[] getRotationNeededForBlock(EntityPlayer paramEntityPlayer, BlockPos pos) {
        double d1 = (double)pos.getX() - paramEntityPlayer.posX;
        double d2 = (double)pos.getY() + 0.5 - (paramEntityPlayer.posY + (double)paramEntityPlayer.getEyeHeight());
        double d3 = (double)pos.getZ() - paramEntityPlayer.posZ;
        double d4 = Math.sqrt(d1 * d1 + d3 * d3);
        float f1 = (float)(Math.atan2(d3, d1) * 180.0 / 3.141592653589793) - 90.0f;
        float f2 = (float)(- Math.atan2(d2, d4) * 180.0 / 3.141592653589793);
        return new float[]{f1, f2};
    }

    public static float getYaw(Entity var0) {
        double var1 = var0.posX - CombatUtil.mc.thePlayer.posX;
        double var3 = var0.posZ - CombatUtil.mc.thePlayer.posZ;
        double var5 = var3 < 0.0 && var1 < 0.0 ? 90.0 + Math.toDegrees(Math.atan(var3 / var1)) : (var3 < 0.0 && var1 > 0.0 ? -90.0 + Math.toDegrees(Math.atan(var3 / var1)) : Math.toDegrees(- Math.atan(var1 / var3)));
        return MathHelper.wrapAngleTo180_float(- CombatUtil.mc.thePlayer.rotationYaw - (float)var5);
    }

    public static float getPitch(Entity var0) {
        double var1 = var0.posX - CombatUtil.mc.thePlayer.posX;
        double var3 = var0.posZ - CombatUtil.mc.thePlayer.posZ;
        double var5 = var0.posY - 1.6 + (double)var0.getEyeHeight() - CombatUtil.mc.thePlayer.posY;
        double var7 = MathHelper.sqrt_double(var1 * var1 + var3 * var3);
        double var9 = - Math.toDegrees(Math.atan(var5 / var7));
        return - MathHelper.wrapAngleTo180_float(CombatUtil.mc.thePlayer.rotationPitch - (float)var9);
    }

    public static float[] getRotationFromPosition(double x, double y, double z) {
        double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
        double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
        double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - (double)Minecraft.getMinecraft().thePlayer.getEyeHeight();
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(- Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793);
        return new float[]{yaw, pitch};
    }

    public static float[] getRotationsNeededBlock(double x, double y, double z) {
        double diffX = x + 0.5 - Minecraft.getMinecraft().thePlayer.posX;
        double diffZ = z + 0.5 - Minecraft.getMinecraft().thePlayer.posZ;
        double diffY = y + 0.5 - (Minecraft.getMinecraft().thePlayer.posY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight());
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(- Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
        return new float[]{Minecraft.getMinecraft().thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw), Minecraft.getMinecraft().thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch)};
    }

    public static float[] getHypixelRotationsNeededBlock(double x, double y, double z) {
        double diffX = x + 0.5 - Minecraft.getMinecraft().thePlayer.posX;
        double diffZ = z + 0.5 - Minecraft.getMinecraft().thePlayer.posZ;
        double diffY = y + 0.5 - (Minecraft.getMinecraft().thePlayer.posY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight());
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(- Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
        return new float[]{Minecraft.getMinecraft().thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - (float)(120 + new Random().nextInt(2))), Minecraft.getMinecraft().thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch)};
    }

    public static float[] getRotationsNeededBlock(double x, double y, double z, double x1, double y1, double z1) {
        double diffX = x1 + 0.5 - x;
        double diffZ = z1 + 0.5 - z;
        double diffY = y1 + 0.5 - (y + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight());
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(- Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
        return new float[]{yaw, pitch};
    }

    public static float getTrajAngleSolutionLow(float d3, float d1, float velocity) {
        float g = 0.006f;
        float sqrt = velocity * velocity * velocity * velocity - g * (g * (d3 * d3) + 2.0f * d1 * (velocity * velocity));
        return (float)Math.toDegrees(Math.atan(((double)(velocity * velocity) - Math.sqrt(sqrt)) / (double)(g * d3)));
    }

    public static float getNewAngle(float angle) {
        if ((angle %= 360.0f) >= 180.0f) {
            angle -= 360.0f;
        }
        if (angle < -180.0f) {
            angle += 360.0f;
        }
        return angle;
    }

    public static float getDistanceBetweenAngles(float angle1, float angle2) {
        float angle = Math.abs(angle1 - angle2) % 360.0f;
        if (angle > 180.0f) {
            angle = 360.0f - angle;
        }
        return angle;
    }

    public static Vec3[] getCorners(AxisAlignedBB box) {
        return new Vec3[]{new Vec3(box.minX, box.minY, box.minZ), new Vec3(box.maxX, box.minY, box.minZ), new Vec3(box.minX, box.maxY, box.minZ), new Vec3(box.minX, box.minY, box.maxZ), new Vec3(box.maxX, box.maxY, box.minZ), new Vec3(box.minX, box.maxY, box.maxZ), new Vec3(box.maxX, box.minY, box.maxZ), new Vec3(box.maxX, box.maxY, box.maxZ)};
    }

    public static AxisAlignedBB getCloserBox(AxisAlignedBB b1, AxisAlignedBB b2) {
        Vec3[] arrvec3 = CombatUtil.getCorners(b2);
        int n = arrvec3.length;
        int n2 = 0;
        while (n2 < n) {
            Vec3 pos = arrvec3[n2];
            if (CombatUtil.isRotationIn(CombatUtil.getRotationFromPosition(pos.xCoord, pos.yCoord, pos.zCoord), b1)) {
                return CombatUtil.getDistanceToBox(b2) < CombatUtil.getDistanceToBox(b1) ? b2 : b1;
            }
            ++n2;
        }
        return b2;
    }

    public static double getDistanceToBox(AxisAlignedBB box) {
        return Minecraft.getMinecraft().thePlayer.getDistance((box.minX + box.maxX) / 2.0, (box.minY + box.maxY) / 2.0, (box.minZ + box.maxZ) / 2.0);
    }

    public static boolean isRotationIn(float[] rotation, AxisAlignedBB box) {
        float[] maxRotations = CombatUtil.getMaxRotations(box);
        if (maxRotations[0] < rotation[0] && maxRotations[2] < rotation[1] && maxRotations[1] > rotation[0] && maxRotations[3] > rotation[1]) {
            return true;
        }
        return false;
    }

    public static float[] getRandomRotationsInBox(final AxisAlignedBB box) {
        final float[] maxRotations = getMaxRotations(box);
        final float yaw = (float) MathHelper.getRandomDoubleInRange(new Random(), maxRotations[0], maxRotations[1]);
        final float pitch = (float) MathHelper.getRandomDoubleInRange(new Random(), maxRotations[2], maxRotations[3]);
        return new float[] { yaw, pitch };
    }

    public static float[] getMaxRotations(AxisAlignedBB box) {
        float minYaw = 2.14748365E9f;
        float maxYaw = -2.14748365E9f;
        float minPitch = 2.14748365E9f;
        float maxPitch = -2.14748365E9f;
        Vec3[] arrvec3 = CombatUtil.getCorners(box);
        int n = arrvec3.length;
        int n2 = 0;
        while (n2 < n) {
            Vec3 pos = arrvec3[n2];
            float[] rot = CombatUtil.getRotationFromPosition(pos.xCoord, pos.yCoord, pos.zCoord);
            if (rot[0] < minYaw) {
                minYaw = rot[0];
            }
            if (rot[0] > maxYaw) {
                maxYaw = rot[0];
            }
            if (rot[1] < minPitch) {
                minPitch = rot[1];
            }
            if (rot[1] > maxPitch) {
                maxPitch = rot[1];
            }
            ++n2;
        }
        return new float[]{minYaw, maxYaw, minPitch, maxPitch};
    }

    public static AxisAlignedBB expandBox(AxisAlignedBB box, double multiplier) {
        multiplier = 1.0 - multiplier / 100.0;
        return box.expand((box.maxX - box.minX) * multiplier, 0.12, (box.maxZ - box.minZ) * multiplier);
    }

    public static AxisAlignedBB contractBox(AxisAlignedBB box, double multiplier) {
        multiplier = 1.0 - multiplier / 100.0;
        return box.contract((box.maxX - box.minX) * multiplier, 0.12, (box.maxZ - box.minZ) * multiplier);
    }

    public static float getYawDifference(final float current, final float target) {
        final float rot = (target + 180.0f - current) % 360.0f;
        return rot + ((rot > 0.0f) ? -180.0f : 180.0f);
    }
    
    public static float getPitchDifference(final float current, final float target) {
        final float rot = (target + 90.0f - current) % 180.0f;
        return rot + ((rot > 0.0f) ? -90.0f : 90.0f);
    }
}


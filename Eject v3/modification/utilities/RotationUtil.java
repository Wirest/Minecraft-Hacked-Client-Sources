package modification.utilities;

import modification.events.EventMoveFlying;
import modification.extenders.Rotation;
import modification.interfaces.MCHook;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class RotationUtil
        implements MCHook {
    public static final List<Float> YAW_SPEEDS = ;
    public static Rotation currentRotation;
    public static Rotation lastRotation = new Rotation(0.0F, 0.0F);
    public static boolean moveToRotation;
    public static boolean jumpFix;
    private float field_76336_a;
    private float field_76334_b;
    private float field_76335_c;

    public static Rotation fixedRotations(double paramDouble1, double paramDouble2) {
        float f1 = MC.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        float f2 = f1 * f1 * f1 * 8.0F;
        float f3 = (float) (paramDouble1 - lastRotation.yaw);
        float f4 = (float) (paramDouble2 - lastRotation.pitch);
        f3 = (float) (f3 - f3 % (f2 * 0.15D));
        f4 = (float) (f4 - f4 % (f2 * 0.15D));
        return new Rotation(lastRotation.yaw + f3, MathHelper.clamp_float(lastRotation.pitch + f4, -90.0F, 90.0F));
    }

    public static float[] calculateSilentStrafe(EventMoveFlying paramEventMoveFlying) {
        int i = (int) ((MathHelper.wrapAngleTo180_float(MC.thePlayer.rotationYaw - currentRotation.yaw) + 180.0F) / 45.0F);
        float f1 = paramEventMoveFlying.strafe;
        float f2 = paramEventMoveFlying.forward;
        float f3 = 0.0F;
        float f4 = 0.0F;
        switch (i) {
            case 0:
                f3 = f2;
                f4 = f1;
                break;
            case 1:
                f3 += f2;
                f4 -= f2;
                f3 += f1;
                f4 += f1;
                break;
            case 2:
                f3 = f1;
                f4 = -f2;
                break;
            case 3:
                f3 -= f2;
                f4 -= f2;
                f3 += f1;
                f4 -= f1;
                break;
            case 4:
                f3 = -f2;
                f4 = -f1;
                break;
            case 5:
                f3 -= f2;
                f4 += f2;
                f3 -= f1;
                f4 -= f1;
                break;
            case 6:
                f3 = -f1;
                f4 = f2;
                break;
            case 7:
                f3 += f2;
                f4 += f2;
                f3 -= f1;
                f4 += f1;
        }
        if ((f3 > 1.0F) || ((f3 < 0.9F) && (f3 > 0.3F)) || (f3 < -1.0F) || ((f3 > -0.9F) && (f3 < -0.3F))) {
            f3 *= 0.5F;
        }
        if ((f4 > 1.0F) || ((f4 < 0.9F) && (f4 > 0.3F)) || (f4 < -1.0F) || ((f4 > -0.9F) && (f4 < -0.3F))) {
            f4 *= 0.5F;
        }
        return new float[]{f4, f3};
    }

    public static void updateRotations(float paramFloat1, float paramFloat2) {
        MC.thePlayer.rotationYawHead = paramFloat1;
        MC.thePlayer.rotationPitchHead = paramFloat2;
        while (MC.thePlayer.rotationYawHead - MC.thePlayer.prevRotationYawHead < -180.0F) {
            MC.thePlayer.prevRotationYawHead -= 360.0F;
        }
        while (MC.thePlayer.rotationYawHead - MC.thePlayer.prevRotationYawHead >= 180.0F) {
            MC.thePlayer.prevRotationYawHead += 360.0F;
        }
    }

    public static void resetRotations() {
        currentRotation = null;
        moveToRotation = false;
    }

    public final float updateRotation(float paramFloat1, float paramFloat2, float paramFloat3) {
        float f = MathHelper.wrapAngleTo180_float(paramFloat2 - paramFloat1);
        if (f > paramFloat3) {
            f = paramFloat3;
        }
        if (f < -paramFloat3) {
            f = -paramFloat3;
        }
        return paramFloat1 + f;
    }

    public final float calcRot(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        float f = MathHelper.wrapAngleTo180_float(paramFloat2 - paramFloat1);
        if (Math.abs(f) > 90.0F) {
            paramFloat4 += paramFloat3;
        } else if (paramFloat4 > 20.0F) {
            paramFloat4 -= paramFloat3;
        } else {
            paramFloat4 += paramFloat3;
        }
        return MathHelper.clamp_float(paramFloat4, 0.0F, 180.0F);
    }

    public final float[] rotationsToEntity(Entity paramEntity) {
        double d1 = paramEntity.posX + (paramEntity.posX - paramEntity.prevPosX) * 2.0D - MC.thePlayer.posX - MC.thePlayer.motionX * 2.0D;
        double d2 = paramEntity.posY + paramEntity.getEyeHeight() - MC.thePlayer.posY - MC.thePlayer.getEyeHeight();
        double d3 = paramEntity.posZ + (paramEntity.posZ - paramEntity.prevPosZ) * 2.0D - MC.thePlayer.posZ - MC.thePlayer.motionZ * 2.0D;
        return new float[]{MathHelper.wrapAngleTo180_float((float) Math.toDegrees(Math.atan2(d3, d1)) - 90.0F), (float) -Math.toDegrees(Math.atan2(d2, Math.hypot(d1, d3)))};
    }

    public final float[] rotationsToVector(Vec3 paramVec3) {
        Vec3 localVec31 = MC.thePlayer.getPositionEyes(1.0F);
        Vec3 localVec32 = paramVec3.subtract(localVec31);
        return new float[]{(float) Math.toDegrees(Math.atan2(localVec32.zCoord, localVec32.xCoord)) - 90.0F, (float) -Math.toDegrees(Math.atan2(localVec32.yCoord, Math.hypot(localVec32.xCoord, localVec32.zCoord)))};
    }

    public final float[] rotationsToEntityWithBow(Entity paramEntity) {
        double d1 = Math.sqrt(MC.thePlayer.getDistanceToEntity(paramEntity) * MC.thePlayer.getDistanceToEntity(paramEntity)) / 1.5D;
        double d2 = paramEntity.posX + (paramEntity.posX - paramEntity.prevPosX) * d1 - MC.thePlayer.posX;
        double d3 = paramEntity.posZ + (paramEntity.posZ - paramEntity.prevPosZ) * d1 - MC.thePlayer.posZ;
        double d4 = paramEntity.posY + (paramEntity.posY - paramEntity.prevPosY) + MC.thePlayer.getDistanceToEntity(paramEntity) * MC.thePlayer.getDistanceToEntity(paramEntity) / 300.0F + paramEntity.getEyeHeight() - MC.thePlayer.posY - MC.thePlayer.getEyeHeight() - MC.thePlayer.motionY;
        return new float[]{(float) Math.toDegrees(Math.atan2(d3, d2)) - 90.0F, (float) -Math.toDegrees(Math.atan2(d4, Math.hypot(d2, d3)))};
    }

    public final float calculateRotation(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5) {
        float f = MathHelper.wrapAngleTo180_float(paramFloat2 - paramFloat1);
        if ((f < -paramFloat4) || (f > paramFloat5)) {
            return paramFloat1 + MathHelper.clamp_float(f, -paramFloat3, paramFloat3);
        }
        return paramFloat1;
    }

    public final void collect(float paramFloat) {
        if (paramFloat < 5.0F) {
            return;
        }
        if (YAW_SPEEDS.size() > 50) {
            YAW_SPEEDS.remove(Collections.min(YAW_SPEEDS));
            return;
        }
        YAW_SPEEDS.add(Float.valueOf(paramFloat));
    }

    public final float readSpeed() {
        if (YAW_SPEEDS.isEmpty()) {
            return 20.0F;
        }
        return ((Float) YAW_SPEEDS.get(ThreadLocalRandom.current().nextInt(0, YAW_SPEEDS.size() - 1))).floatValue();
    }

    public final float[] rotationsToPos(BlockPos paramBlockPos) {
        double d1 = paramBlockPos.getX() + 0.4D - MC.thePlayer.posX;
        double d2 = paramBlockPos.getY() + 0.5D - MC.thePlayer.posY - MC.thePlayer.getEyeHeight();
        double d3 = paramBlockPos.getZ() + 0.4D - MC.thePlayer.posZ;
        return new float[]{(float) Math.toDegrees(Math.atan2(d3, d1)) - 90.0F, (float) -Math.toDegrees(Math.atan2(d2, Math.hypot(d1, d3)))};
    }
}





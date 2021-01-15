package dev.astroclient.client.util.math;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

/**
* @author Zane for PublicBase
* @since 10/28/19
*/

public class RotationUtil {

    private static Minecraft mc = Minecraft.getMinecraft();

    /**
     *
     * @param entityLivingBase The entity to check if facing.
     * @return If you (the player)
     */
    public static boolean isFacingEntity(EntityLivingBase entityLivingBase) {
        float yaw = getNeededRotations(entityLivingBase)[0];
        float pitch = getNeededRotations(entityLivingBase)[1];
        float playerYaw = mc.thePlayer.rotationYaw;
        float playerPitch = mc.thePlayer.rotationPitch;
        float boudingBoxSize = 21.0F + entityLivingBase.getCollisionBorderSize();
        if (playerYaw < 0)
            playerYaw += 360;
        if (playerPitch < 0)
            playerPitch += 360;
        if (yaw < 0)
            yaw += 360;
        if (pitch < 0)
            pitch += 360;
        if (playerYaw >= (yaw - boudingBoxSize) && playerYaw <= (yaw + boudingBoxSize))
            return playerPitch >= (pitch - boudingBoxSize) && playerPitch <= (pitch + boudingBoxSize);
        return false;
    }

    /**
     * @param entityIn The entity to get the yaw change to.
     * @return The total angle change between the center of your and the center of entityIn's bounding box.
     */
    public static float getAngleChange(EntityLivingBase entityIn) {
        float yaw = getNeededRotations(entityIn)[0];
        float pitch = getNeededRotations(entityIn)[1];
        float playerYaw = mc.thePlayer.rotationYaw;
        float playerPitch = mc.thePlayer.rotationPitch;
        if (playerYaw < 0)
            playerYaw += 360;
        if (playerPitch < 0)
            playerPitch += 360;
        if (yaw < 0)
            yaw += 360;
        if (pitch < 0)
            pitch += 360;
        float yawChange = Math.max(playerYaw, yaw) - Math.min(playerYaw, yaw);
        float pitchChange = Math.max(playerPitch, pitch) - Math.min(playerPitch, pitch);
        return yawChange + pitchChange;
    }

    /**
     * @param entityIn The entity to get rotations to.
     * @return The needed rotations to entityIn.
     */
    public static float[] getNeededRotations(EntityLivingBase entityIn) {
        double d0 = entityIn.posX - mc.thePlayer.posX;
        double d1 = entityIn.posZ - mc.thePlayer.posZ;
        double d2 = entityIn.posY + entityIn.getEyeHeight() - (mc.thePlayer.getEntityBoundingBox().minY + mc.thePlayer.getEyeHeight());

        double d3 = MathHelper.sqrt_double(d0 * d0 + d1 * d1);
        float f = (float) (MathHelper.func_181159_b(d1, d0) * 180.0D / Math.PI) - 90.0F;
        float f1 = (float) (-(MathHelper.func_181159_b(d2, d3) * 180.0D / Math.PI));
        return new float[]{f, f1};
    }

    /**
     * @param entityIn     The entity to get rotations to.
     * @param speed        How fast the rotations are.
     * @return Rotations to entityIn with speed.
     */
    public static float[] getRotations(EntityLivingBase entityIn, float speed) {
        float yaw = updateRotation(mc.thePlayer.rotationYaw,
                getNeededRotations(entityIn)[0],
                speed);
        float pitch = updateRotation(mc.thePlayer.rotationPitch,
                getNeededRotations(entityIn)[1],
                speed);
        return new float[]{yaw, pitch};
    }

    private static float updateRotation(float currentRotation, float intendedRotation, float increment) {
        float f = MathHelper.wrapAngleTo180_float(intendedRotation - currentRotation);

        if (f > increment)
            f = increment;

        if (f < -increment)
            f = -increment;

        return currentRotation + f;
    }
}

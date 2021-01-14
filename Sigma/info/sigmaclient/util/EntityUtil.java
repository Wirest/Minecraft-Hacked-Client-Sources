package info.sigmaclient.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;

public class EntityUtil implements MinecraftUtil {
    public static void faceEntity(EntityLivingBase entity) {
        float[] rotations = getRotationsNeeded(entity);
        if (rotations != null) {
            mc.thePlayer.rotationYaw = rotations[0];
            mc.thePlayer.rotationPitch = rotations[1] + 1.0F;
        }
    }

    public static void faceEntitySilently(Entity entity) {
        float[] rotations = getRotationsNeeded(entity);
        if (rotations != null) {
            float yaw = rotations[0];
            float pitch = rotations[1] + 1.0F;
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(yaw, pitch, mc.thePlayer.onGround));
        }
    }

    public static float getDistanceFromMouse(Entity entity) {
        final float[] neededRotations = getRotationsNeeded(entity);
        if (neededRotations != null) {
            final float neededYaw = mc.thePlayer.rotationYaw - neededRotations[0];
            final float neededPitch = mc.thePlayer.rotationPitch - neededRotations[1];
            final float distanceFromMouse = MathHelper.sqrt_float((neededYaw * neededYaw) + (neededPitch * neededPitch));
            return distanceFromMouse;
        }
        return -1.0F;
    }

    public static float[] getRotationsNeeded(Entity entity) {
        if (entity == null) {
            return null;
        }
        double diffX = entity.posX - mc.thePlayer.posX;
        double diffZ = entity.posZ - mc.thePlayer.posZ;
        double diffY;
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase elb = (EntityLivingBase) entity;
            diffY = (elb.posY + elb.getEyeHeight()) - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        } else {
            diffY = ((entity.getBoundingBox().minY + entity.getBoundingBox().maxY) / 2.0D) - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        }
        double dist = MathHelper.sqrt_double((diffX * diffX) + (diffZ * diffZ));
        float yaw = (float) ((Math.atan2(diffZ, diffX) * 180.0D) / 3.141592653589793D) - 90.0F;
        float pitch = (float) -((Math.atan2(diffY, dist) * 180.0D) / 3.141592653589793D);
        return new float[]{mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw), mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch)};
    }
}

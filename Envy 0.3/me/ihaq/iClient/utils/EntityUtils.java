package me.ihaq.iClient.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class EntityUtils {

	public static float[] getBlockRotations(int x, int y, int z, EnumFacing facing) {
		final Entity temp = new EntitySnowball(Minecraft.getMinecraft().theWorld);
		temp.posX = x + 0.5;
		temp.posY = y + 0.5;
		temp.posZ = z + 0.5;
		Entity entity = temp;
		entity.posX += facing.getDirectionVec().getX() * 0.25;
		entity.posY += facing.getDirectionVec().getY() * 0.25;
		entity.posZ += facing.getDirectionVec().getZ() * 0.25;
		return getAngles(temp);
	}

	public static float[] getAngles(Entity e) {
		return new float[] { getYawChangeToEntity(e) + Minecraft.getMinecraft().thePlayer.rotationYaw,
				getPitchChangeToEntity(e) + Minecraft.getMinecraft().thePlayer.rotationPitch };
	}

	public static float getYawChangeToEntity(Entity entity) {
		Minecraft mc = Minecraft.getMinecraft();
		double deltaX = entity.posX - mc.thePlayer.posX;
		double deltaZ = entity.posZ - mc.thePlayer.posZ;
		double yawToEntity;
		if ((deltaZ < 0.0D) && (deltaX < 0.0D)) {
			yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
		} else {
			if ((deltaZ < 0.0D) && (deltaX > 0.0D)) {
				yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
			} else {
				yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
			}
		}

		return MathHelper.wrapAngleTo180_float(-(mc.thePlayer.rotationYaw - (float) yawToEntity));
	}

	public static float getPitchChangeToEntity(Entity entity) {
		Minecraft mc = Minecraft.getMinecraft();
		double deltaX = entity.posX - mc.thePlayer.posX;
		double deltaZ = entity.posZ - mc.thePlayer.posZ;
		double deltaY = entity.posY - 1.6D + entity.getEyeHeight() - 0.4 - mc.thePlayer.posY;
		double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);

		double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));

		return -MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch - (float) pitchToEntity);
	}

}

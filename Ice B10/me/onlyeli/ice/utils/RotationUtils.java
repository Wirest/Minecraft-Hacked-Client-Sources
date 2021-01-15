package me.onlyeli.ice.utils;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class RotationUtils {
	public static float[] getRotations(Entity ent) {
		double x = ent.posX;
		double z = ent.posZ;
		double y = ent.boundingBox.maxY - 4.0;
		return getRotationFromPosition(x, z, y);
	}

	public static float[] getAverageRotations(List<EntityLivingBase> targetList) {
		double posX = 0.0;
		double posY = 0.0;
		double posZ = 0.0;
		for (final Entity ent : targetList) {
			posX += ent.posX;
			posY += ent.boundingBox.maxY - 2.0;
			posZ += ent.posZ;
		}
		posX /= targetList.size();
		posY /= targetList.size();
		posZ /= targetList.size();
		return new float[] { getRotationFromPosition(posX, posZ, posY)[0],
				getRotationFromPosition(posX, posZ, posY)[1] };
	}

	public static float[] getRotationFromPosition(double x, double z, double y) {
		double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
		double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
		double yDiff = y - Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight();
		double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
		float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
		float pitch = (float) (-(Math.atan2(yDiff, dist) * 180.0 / Math.PI));
		return new float[] { yaw, pitch };
	}

	public static float getTrajAngleSolutionLow(float d3, float d1, float velocity) {
		float g = 0.006f;
		float sqrt = velocity * velocity * velocity * velocity
				- g * (g * (d3 * d3) + 2.0f * d1 * (velocity * velocity));
		return (float) Math.toDegrees(Math.atan((velocity * velocity - Math.sqrt(sqrt)) / (g * d3)));
	}

	public static float getNewAngle(float angle) {
		angle %= 360.0f;
		if (angle >= 180.0f) {
			angle -= 360.0f;
		}
		if (angle < -180.0f) {
			angle += 360.0f;
		}
		return angle;
	}

	public static float getDistanceBetweenAngles(float angle1, float angle2) {
		float angle3 = Math.abs(angle1 - angle2) % 360.0f;
		if (angle3 > 180.0f) {
			angle3 = 360.0f - angle3;
		}
		return angle3;
	}
}

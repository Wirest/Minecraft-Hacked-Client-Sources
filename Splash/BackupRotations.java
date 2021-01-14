package splash.utilities.player;

import org.apache.commons.lang3.RandomUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import splash.utilities.math.Vec3d;

public class BackupRotations {
	static {

	}

	public static float[] getRotations(EntityLivingBase ent) {
		double x = ent.posX;
		double y = ent.posY + ent.getEyeHeight() / 2.0f;
		double z = ent.posZ;
		return RotationUtils.getRotationFromPosition(x, y, z);
	}

	public static float[] doScaffoldRotations(Vec3d vec) {
		double diffX = vec.xCoord - Minecraft.getMinecraft().thePlayer.posX;
		double diffZ = vec.zCoord - Minecraft.getMinecraft().thePlayer.posZ;
		float yaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - RandomUtils.nextFloat(0F, 0.01F));
		return new float[] {
				Minecraft.getMinecraft().thePlayer.rotationYaw
						+ MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw),
				Minecraft.getMinecraft().thePlayer.rotationPitch
					+ MathHelper.wrapAngleTo180_float(90 - Minecraft.getMinecraft().thePlayer.rotationPitch) };
	}

	public static float[] getBowAngles(Entity entity) {
		double xDelta = entity.posX - entity.lastTickPosX;
		double zDelta = entity.posZ - entity.lastTickPosZ;
		double d = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity) % 0.8;
		boolean sprint = entity.isSprinting();
		double xMulti = d / 0.8 * xDelta * (sprint ? 1.25 : 1.0);
		double zMulti = d / 0.8 * zDelta * (sprint ? 1.25 : 1.0);
		double x = entity.posX + xMulti - Minecraft.getMinecraft().thePlayer.posX;
		double z = entity.posZ + zMulti - Minecraft.getMinecraft().thePlayer.posZ;
		double y = Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight()
				- (entity.posY + entity.getEyeHeight());
		double dist = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity);
		float yaw = (float) Math.toDegrees(Math.atan2(z, x)) - 90.0f;
		float pitch = (float) Math.toDegrees(Math.atan2(y, dist));
		return new float[] { yaw, pitch };
	}

	public static float[] getRotationFromPosition(double x, double y, double z) {
		double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
		double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
		double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - 1.2;

		double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
		float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
		float pitch = (float) -(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);
		return new float[] { yaw, pitch };
	}
}

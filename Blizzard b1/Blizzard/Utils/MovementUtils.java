package Blizzard.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

public class MovementUtils {

	private static Minecraft mc = Minecraft.getMinecraft();

	public static float getDirection() {
		float yaw = mc.thePlayer.rotationYawHead;
		float forward = mc.thePlayer.moveForward;
		float strafe = mc.thePlayer.moveStrafing;
		yaw += (forward < 0.0F ? 180 : 0);
		if (strafe < 0.0F) {
			yaw += (forward < 0.0F ? -45 : forward == 0.0F ? 90 : 45);
		}
		if (strafe > 0.0F) {
			yaw -= (forward < 0.0F ? -45 : forward == 0.0F ? 90 : 45);
		}
		return yaw * 0.017453292F;
	}

	public static double square(double in) {
		return in * in;
	}

	public static double getSpeed() {
		return Math.sqrt(square(mc.thePlayer.motionX) + square(mc.thePlayer.motionZ));
	}

	public static void setSpeed(double speed) {
		mc.thePlayer.motionX = (-MathHelper.sin(getDirection()) * speed);
		mc.thePlayer.motionZ = (MathHelper.cos(getDirection()) * speed);
	}

	public static boolean isMoving() {
		return (mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F);
	}

}
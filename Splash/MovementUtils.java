package splash.utilities.player;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovementInput;
import splash.Splash;
import splash.client.events.player.EventMove;
import splash.client.modules.combat.Aura;
import splash.client.modules.combat.TargetStrafe;

public class MovementUtils {

	static Minecraft mc = Minecraft.getMinecraft();

	public static double fallPacket() {
		double i;
		for (i = mc.thePlayer.posY; i > getGroundLevel(); i -= 8.0) {
			if (i < getGroundLevel()) {
				i = getGroundLevel();
			}
			mc.thePlayer.sendQueue.addToSendQueue(
					new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, i, mc.thePlayer.posZ, true));
		}
		return i;
	}

	public static void ascendPacket() {
		for (double i = getGroundLevel(); i < mc.thePlayer.posY; i += 8.0) {
			if (i > mc.thePlayer.posY) {
				i = mc.thePlayer.posY;
			}
			mc.thePlayer.sendQueue.addToSendQueue(
					new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, i, mc.thePlayer.posZ, true));
		}
	}

	public static double getGroundLevel() {
		for (int i = (int) Math.round(mc.thePlayer.posY); i > 0; --i) {
			final AxisAlignedBB box = mc.thePlayer.boundingBox.addCoord(0.0, 0.0, 0.0);
			box.minY = i - 1;
			box.maxY = i;
			if (!isColliding(box) || !(box.minY <= mc.thePlayer.posY)) {
				continue;
			}
			return i;
		}
		return 0.0;
	}

	public static boolean isColliding(final AxisAlignedBB box) {
		return mc.theWorld.checkBlockCollision(box);
	}

	public static double getDistanceToEntity(Entity entity) {
		if (Minecraft.getMinecraft().thePlayer != null && entity != null) {
			return Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity);
		}
		return Double.NaN;
	}

	public static void setSpeed(final EventMove moveEvent, final double moveSpeed, final float pseudoYaw,
			final double pseudoStrafe, final double pseudoForward) {
		double forward = pseudoForward;
		double strafe = pseudoStrafe;
		float yaw = pseudoYaw;

		if (forward == 0.0 && strafe == 0.0) {
			moveEvent.setZ(0);
			moveEvent.setX(0);
		} else {
			if (forward != 0.0) {
				if (strafe > 0.0) {
					yaw += ((forward > 0.0) ? -45 : 45);
				} else if (strafe < 0.0) {
					yaw += ((forward > 0.0) ? 45 : -45);
				}
				strafe = 0.0;
				if (forward > 0.0) {
					forward = 1.0;
				} else if (forward < 0.0) {
					forward = -1.0;
				}
			}
			final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
			final double sin = Math.sin(Math.toRadians(yaw + 90.0f));

			moveEvent.setX((forward * moveSpeed * cos + strafe * moveSpeed * sin));
			moveEvent.setZ((forward * moveSpeed * sin - strafe * moveSpeed * cos));
		}
	}

	public static void setMoveSpeed(EventMove event, double moveSpeed) {
		double range = ((Aura) Splash.getInstance().getModuleManager().getModuleByClass(Aura.class)).range.getValue();
		MovementInput movementInput = mc.thePlayer.movementInput;
		double moveForward = movementInput.getForward();
		boolean targetStrafe = TargetStrafe.canStrafe();
		if (targetStrafe) {
			if (mc.thePlayer.getDistanceToEntity(Aura.currentEntity) <= range - 1.9) {
				moveForward = 0;
			} else {
				moveForward = 1;
			}
		}
		double moveStrafe = targetStrafe ? TargetStrafe.direction : movementInput.getStrafe();
		double yaw = targetStrafe ? RotationUtils.getNeededRotations(Aura.currentEntity)[0] : mc.thePlayer.rotationYaw;
		if (moveForward == 0.0D && moveStrafe == 0.0D) {
			event.setX(0.0D);
			event.setZ(0.0D);
		} else {
			if (moveStrafe > 0) {
				moveStrafe = 1;
			} else if (moveStrafe < 0) {
				moveStrafe = -1;
			}
			if (moveForward != 0.0D) {
				if (moveStrafe > 0.0D) {
					yaw += (moveForward > 0.0D ? -45 : 45);
				} else if (moveStrafe < 0.0D) {
					yaw += (moveForward > 0.0D ? 45 : -45);
				}
				moveStrafe = 0.0D;
				if (moveForward > 0.0D) {
					moveForward = 1.0D;
				} else if (moveForward < 0.0D) {
					moveForward = -1.0D;
				}
			}
			double cos = Math.cos(Math.toRadians(yaw + 90.0F));
			double sin = Math.sin(Math.toRadians(yaw + 90.0F));
			event.setX(moveForward * moveSpeed * cos + moveStrafe * moveSpeed * sin);
			event.setZ(moveForward * moveSpeed * sin - moveStrafe * moveSpeed * cos);
		}
	}

	public static double getBaseMoveSpeed() {
		double baseSpeed = 0.2873;
		if (MovementUtils.mc.thePlayer != null && Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
			final int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
		}
		return baseSpeed;
	}

	public static void setMoveSpeed(double moveSpeed) {
		MovementInput movementInput = mc.thePlayer.movementInput;
		double range = ((Aura) Splash.INSTANCE.getModuleManager().getModuleByClass(Aura.class)).range.getValue();
		double forward = movementInput.getForward();
		boolean targetStrafe = TargetStrafe.canStrafe();
		if (targetStrafe) {
			if (mc.thePlayer.getDistanceToEntity(Aura.currentEntity) <= range - 1.9) {
				forward = 0;
			} else {
				forward = 1;
			}
		}
		double strafe = targetStrafe ? TargetStrafe.direction : movementInput.getStrafe();
		double yaw = targetStrafe ? RotationUtils.getNeededRotations(Aura.currentEntity)[0] : mc.thePlayer.rotationYaw;
		if (forward > 0) {
			forward = 1;
		} else if (forward < 0) {
			forward = -1;
		}
		if (strafe > 0) {
			strafe = 1;
		} else if (strafe < 0) {
			strafe = -1;
		}
		if (forward == 0.0 && strafe == 0.0) {
			MovementUtils.mc.thePlayer.motionX = 0.0;
			MovementUtils.mc.thePlayer.motionZ = 0.0;
		}
		if (forward != 0.0 && strafe != 0.0) {
			forward *= Math.sin(0.6398355709958845);
			strafe *= Math.cos(0.6398355709958845);
		}
		final double sin = -Math.sin(Math.toRadians(yaw));
		final double cos = Math.cos(Math.toRadians(yaw));
		MovementUtils.mc.thePlayer.motionX = forward * moveSpeed * sin + strafe * moveSpeed * cos;
		MovementUtils.mc.thePlayer.motionZ = forward * moveSpeed * cos - strafe * moveSpeed * sin;
	}
}
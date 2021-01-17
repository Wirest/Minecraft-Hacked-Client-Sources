package me.ihaq.iClient.modules.Movement;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.lwjgl.input.Keyboard;

import me.ihaq.iClient.event.EventTarget;
import me.ihaq.iClient.event.events.EventMove;
import me.ihaq.iClient.event.events.EventPreMotionUpdates;
import me.ihaq.iClient.modules.Module;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;

public class LongJump extends Module {

	public double yOffset;
	private int stage;
	private double boost;
	private double moveSpeed;
	private double lastDist;

	public LongJump() {
		super("LongJump", Keyboard.KEY_NONE, Category.MOVEMENT, "");
		this.moveSpeed = 0.2873D;
		boost = 2.5D;
	}

	@Override
	public void onEnable() {
		if (this.mc.thePlayer != null) {
			this.moveSpeed = getBaseMoveSpeed();
		}
		this.lastDist = 0.0D;
		this.stage = 1;
	}

	@Override
	public void onDisable() {
	}

	public double getBaseMoveSpeed() {
		double baseSpeed = 0.2873D;
		if (this.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = this.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
		}
		return baseSpeed;
	}

	public double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	@EventTarget
	public void onEvent(EventMove event) {
		if (!this.isToggled()) {
			return;
		}

		if ((this.mc.thePlayer.moveStrafing == 0.0F) && (this.mc.thePlayer.moveForward == 0.0F)) {
			this.stage = 1;
		}
		if (round(this.mc.thePlayer.posY - (int) this.mc.thePlayer.posY, 3) == round(0.93D, 3)) {
			this.mc.thePlayer.motionY -= 0.01D;
			event.y -= 0.01D;
		}
		if ((this.stage == 1) && ((this.mc.thePlayer.moveForward != 0.0F) || (this.mc.thePlayer.moveStrafing != 0.0F))
				&& (this.mc.thePlayer.onGround) && (this.mc.thePlayer.isCollidedVertically)
				&& (this.mc.thePlayer.motionY < 0.0D)) {
			this.stage = 2;
			this.moveSpeed = (this.boost * getBaseMoveSpeed() - 0.01D);
		} else if (this.stage == 2) {
			this.stage = 3;
			this.mc.thePlayer.motionY = 0.41764345D;
			event.y = 0.41764345D;
			this.moveSpeed *= 2.149802D;
		} else if (this.stage == 3) {
			this.stage = 4;
			double difference = 0.66D * (this.lastDist - getBaseMoveSpeed());
			this.moveSpeed = (this.lastDist - difference);
		} else {
			if ((mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer,
					this.mc.thePlayer.boundingBox.offset(0.0D, this.mc.thePlayer.motionY, 0.0D)).size() > 0)
					|| (this.mc.thePlayer.isCollidedVertically)) {
				this.stage = 1;
			}
			this.moveSpeed = (this.lastDist - this.lastDist / 159.0D);
		}
		this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
		MovementInput movementInput = this.mc.thePlayer.movementInput;
		float forward = movementInput.moveForward;
		float strafe = movementInput.moveStrafe;
		float yaw = this.mc.thePlayer.rotationYaw;
		if ((forward == 0.0F) && (strafe == 0.0F)) {
			event.x = 0.0D;
			event.z = 0.0D;
		} else if (forward != 0.0F) {
			if (strafe >= 1.0F) {
				yaw += (forward > 0.0F ? -45 : 45);
				strafe = 0.0F;
			} else if (strafe <= -1.0F) {
				yaw += (forward > 0.0F ? 45 : -45);
				strafe = 0.0F;
			}
			if (forward > 0.0F) {
				forward = 1.0F;
			} else if (forward < 0.0F) {
				forward = -1.0F;
			}
		}
		double mx = Math.cos(Math.toRadians(yaw + 90.0F));
		double mz = Math.sin(Math.toRadians(yaw + 90.0F));
		event.x = (forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz);
		event.z = (forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx);
	}

	@EventTarget
	public void onEvent(EventPreMotionUpdates event) {
		if (!this.isToggled()) {
			return;
		}
		boolean speedy = this.mc.thePlayer.isPotionActive(Potion.moveSpeed);
		double xDist = this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX;
		double zDist = this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ;
		this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
	}
}

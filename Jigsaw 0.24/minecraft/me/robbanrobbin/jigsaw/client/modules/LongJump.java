package me.robbanrobbin.jigsaw.client.modules;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.events.PreMotionEvent;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;

public class LongJump extends Module {

	private double speed;
	private int stage;
	private double moveSpeed;
	private double lastDist;

	public LongJump() {
		super("LongJump", Keyboard.KEY_NONE, Category.MOVEMENT, "Makes your jumps longer.");
	}

	@Override
	public void onToggle() {
		this.moveSpeed = getBaseMoveSpeed();
		speed = 1;
		this.stage = 0;
		super.onToggle();
	}

	@Override
	public void onPreMotion(PreMotionEvent event) {
		if (mc.thePlayer.isInWater() || mc.thePlayer.isInLava()) {
			this.stage = 1;
			return;
		}
		MovementInput movementInput = mc.thePlayer.movementInput;
		float forward = movementInput.moveForward;
		float strafe = movementInput.moveStrafe;
		float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
		double round = round(mc.thePlayer.posY - (int) mc.thePlayer.posY, 3);
		if ((this.stage == 1) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F))) {
			this.stage = 2;
			this.moveSpeed = (1.38D * getBaseMoveSpeed() - 0.01D);
		} else if (this.stage == 2) {
			this.stage = 3;
			mc.thePlayer.motionY = 0.399399995803833D;
			event.y = 0.399399995803833D;
			this.moveSpeed *= 2.149D;
		} else if (this.stage == 3) {
			this.stage = 4;
			double difference = 0.66D * (this.lastDist - getBaseMoveSpeed());
			this.moveSpeed = (this.lastDist - difference) * 2.5;
		} else {
			if ((mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
					mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D), true).size() > 0)
					|| (mc.thePlayer.isCollidedVertically)) {
				this.stage = 1;
			}
			this.moveSpeed = (this.lastDist - this.lastDist / 159.0D);
			if (mc.thePlayer.motionY < -0.1) {
				// mc.thePlayer.motionY += 0.05;
			}

		}

		this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
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
		super.onPreMotion(event);
	}

	public void onLateUpdate() {
		double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
		double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
		this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
		super.onLateUpdate();
	}

	private double getBaseMoveSpeed() {
		double baseSpeed = 0.2873D;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
		}
		return baseSpeed;
	}

	public static double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
}

package me.robbanrobbin.jigsaw.client.modules;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.events.PreMotionEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;

public class BunnyHop extends Module {

	private double speed;
	private int stage;
	private double moveSpeed;
	private double lastDist;
	private WaitTimer timer = new WaitTimer();
	private int jumps = 0;

	public BunnyHop() {
		super("BunnyHop", Keyboard.KEY_NONE, Category.MOVEMENT, "Makes you go faster and it bypasses nocheat.");
	}

	@Override
	public void onToggle() {
		timer.reset();
		this.moveSpeed = getBaseMoveSpeed();
		speed = 1;
		this.stage = 1;
		if (currentMode.equals("FastHop") || currentMode.equals("LowHop")) {
			this.stage = 2;
		}

		jumps = 0;
		super.onToggle();
	}

	@Override
	public void onPreMotion(PreMotionEvent event) {
		// Jigsaw.chatMessage(round(mc.thePlayer.posY - (int) mc.thePlayer.posY,
		// 3));
		// if(currentMode.equals("FastHop")
		// || currentMode.equals("LowHop")) {
		// if(mc.thePlayer.onGround) {
		// timer.time = 10000000;
		// }
		// if(!timer.hasTimeElapsed(100000, false)) {
		// return;
		// }
		// }
		if (mc.thePlayer.isInWater() || mc.thePlayer.isInLava() || mc.thePlayer.isInWeb
				|| Jigsaw.getModuleByName("Flight").isToggled()) {
			jumps = -1;
			this.stage = 2;
			return;
		}
		MovementInput movementInput = mc.thePlayer.movementInput;
		float forward = movementInput.moveForward;
		float strafe = movementInput.moveStrafe;
		float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
		// Jigsaw.chatMessage(round(mc.thePlayer.posY - (int) mc.thePlayer.posY,
		// 3) + " speed:" + (double)jumps / 1000);
		double round = round(mc.thePlayer.posY - (int) mc.thePlayer.posY, 3);
		if (this.currentMode.equals("DoubleHop") || this.currentMode.equals("TripleHop")) {
			if (round == 0.869 || round == 0.15) {
				Entity thePlayer = mc.thePlayer;
				thePlayer.motionY -= 0.08D;
				event.y -= 0.09316090325960147D;
				Entity thePlayer2 = mc.thePlayer;
				thePlayer2.posY -= 0.09316090325960147D;
			}
			if (round <= (this.currentMode.equals("DoubleHop") ? 0.550 : 0.590) && round >= 0.530) {
				event.y += 0.0094873;
				mc.thePlayer.motionY += 0.06;
			}
			if (round <= 0.529 && round >= 0.459) {
				event.y = -0.01;
				double difference = 0.66D * (this.lastDist - getBaseMoveSpeed());
				this.moveSpeed = (this.lastDist - difference);
			}
		}
		if(this.currentMode.equals("SlowHop") && forward != 0 || strafe != 0) {
			if (round == 0.869 || round == 0.15) {
				Entity thePlayer = mc.thePlayer;
				thePlayer.motionY -= 0.1D;
			}
		}
		// Jigsaw.chatMessage(round);
		if (round >= 0 && round <= 0.75 && mc.thePlayer.motionY <= 0 && mc.thePlayer.fallDistance < 1
				&& currentMode.equals("FastHop")) {
			// Jigsaw.chatMessage("§6HEH");
			mc.thePlayer.motionY -= 0.05;
			event.y -= 0.08;
		}
		if ((this.stage == 1) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F))) {
			this.stage = 2;

			this.moveSpeed = (2.012D * getBaseMoveSpeed());
			if (this.currentMode.equals("DoubleHop") || this.currentMode.equals("TripleHop")
					|| this.currentMode.equals("NormalHop")) {
				this.moveSpeed = (1.38D * getBaseMoveSpeed() - 0.01D);
			}
			if(this.currentMode.equals("SlowHop")) {
				this.moveSpeed = getBaseMoveSpeed();
			}
		} else if (this.stage == 2) {
			this.stage = 3;
			if (!this.currentMode.equals("LowHop")) {
				mc.thePlayer.motionY = 0.399399995803833D;
			}
			event.y = 0.399399995803833D;
			this.moveSpeed *= 2.149D;
			if(this.currentMode.equals("SlowHop")) {
				this.moveSpeed = getBaseMoveSpeed() * 1.98;
			}
			jumps++;

		} else if (this.stage == 3) {
			this.stage = 4;

			double difference = 0.66D * (this.lastDist - getBaseMoveSpeed());
			this.moveSpeed = (this.lastDist - difference);
		} else {
			boolean ground = false;
			if (this.currentMode.equals("FastHop")) {
				if ((mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
						mc.thePlayer.boundingBox.offset(0.0D,
								-Math.abs((mc.thePlayer.motionY * (mc.thePlayer.fallDistance > 2 ? 2 : 2))), 0.0D),
						true).size() > 0)) {
					ground = true;
				} else {
					ground = false;
				}
			} else {
				if ((mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
						mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D), true).size() > 0)) {
					ground = true;
				} else {
					ground = false;
				}
			}
			// Jigsaw.chatMessage(ground);
			if (ground || (mc.thePlayer.isCollidedVertically) || mc.thePlayer.onGround) {
				this.stage = 1;
			}
			this.moveSpeed = (this.lastDist - this.lastDist / 159.0D);
		}
		this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
		if ((forward == 0.0F) && (strafe == 0.0F)) {
			event.x = 0.0D;
			event.z = 0.0D;
			jumps = 0;
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

	@Override
	public String[] getModes() {
		return new String[] { "NormalHop", "FastHop", "SlowHop", "LowHop", "DoubleHop", "TripleHop" };
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

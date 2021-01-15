package me.robbanrobbin.jigsaw.client.modules;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.events.PreMotionEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovementInput;

public class HighJump extends Module {

	public static int height = 0;
	public static int delay = 0;
	WaitTimer timer = new WaitTimer();
	WaitTimer dmg = new WaitTimer();
	boolean jumped = false;
	boolean damaged = false;
	int times = 0;
	boolean ascending = false;
	int airjumptimes = 0;
	boolean dodmg = true;
	boolean descending = false;

	public HighJump() {
		super("HighJump", 0, Category.HIDDEN, "Makes you jump high in NCP.");
	}

	@Override
	public void onEnable() {
		if (height == 0) {
			this.toggle();
			Jigsaw.chatMessage("§cHeight can't be 0!");
		}
		if (height > 20) {
			this.toggle();
			Jigsaw.chatMessage("§cHeight can't be more than 7!");
		}
		if (delay > 0) {
			this.toggle();
			Jigsaw.chatMessage("§cDelay can't be more than 0!");
		}
		if (delay < 0) {
			this.toggle();
			Jigsaw.chatMessage("§cDelay can't be less than 0!");
		}
		Jigsaw.chatMessage("Jumping " + height + " times with delay: " + delay);
		super.onEnable();
	}

	@Override
	public void onToggle() {
		mc.timer.timerSpeed = 1f;
		timer.reset();
		dmg.reset();
		ascending = false;
		damaged = false;
		jumped = false;
		airjumptimes = 0;
		times = 0;
		dodmg = true;
		descending = false;
		super.onToggle();
	}

	@Override
	public void onUpdate() {
		if (mc.gameSettings.keyBindSneak.pressed) {
			setToggled(false, true);
			return;
		}
		if (!damaged) {
			if (times != height) {
				if (mc.thePlayer.onGround) {
					if (dmg.hasTimeElapsed(delay, true) && dodmg) {
						Jigsaw.sendChatMessage(".damage");
						times++;
						dodmg = false;
					}
					if (timer.hasTimeElapsed(0, true)) {
						mc.thePlayer.jump();
						mc.thePlayer.jump();
						mc.thePlayer.jump();
					}
				} else {
					dmg.reset();
					timer.reset();
					dodmg = true;
				}
				return;
			}
			damaged = true;
		}
		if (!mc.thePlayer.onGround && !ascending) {
			return;
		}
		ascending = true;
		if (airjumptimes + 1 >= height) {
			// mc.thePlayer.motionY = -0.1;
			// mc.gameSettings.keyBindForward.pressed = false;
			// setToggled(false, true);
			// return;
			if (mc.thePlayer.motionY <= -0.2) {
				// mc.timer.timerSpeed = 0.05f;
				descending = true;
			}
			if (mc.thePlayer.onGround) {
				mc.timer.timerSpeed = 1f;
				mc.gameSettings.keyBindForward.pressed = false;
				setToggled(false, true);
			}
		} else {
			if (!jumped) {
				dodmg = false;
				if (timer.hasTimeElapsed(100, false) && airjumptimes == 0) {
					mc.gameSettings.keyBindForward.pressed = true;
					mc.thePlayer.setSprinting(true);
				}
				if (timer.hasTimeElapsed(200, false) && mc.thePlayer.onGround && airjumptimes == 0) {
					Jigsaw.sendChatMessage(".damage");
					mc.thePlayer.jump();
					mc.thePlayer.jump();
					mc.thePlayer.jump();
				}
				if (airjumptimes == 1) {

				}
				if (timer.hasTimeElapsed(250, false) && !jumped) {

					jumped = true;
				}
			}
			// if(mc.thePlayer.motionY <= 0
			// && !mc.thePlayer.onGround) {
			// mc.thePlayer.motionY = 0.48;
			// jumped = false;
			// airjumptimes++;
			//
			// }
			if (airjumptimes < 2 && jumped) {
				descending = true;
			}
			if (timer.hasTimeElapsed(550, true)) {
				mc.thePlayer.motionY = 0.48;
				jumped = true;
				airjumptimes++;
				// sendPacket(new C03PacketPlayer(true));
			}
		}

		super.onUpdate();
	}

	private double moveSpeed;

	@Override
	public void onPreMotion(PreMotionEvent event) {
		if (!descending) {
			this.moveSpeed = getBaseMoveSpeed();
			return;
		}
		if (airjumptimes < 2) {
			this.moveSpeed = getBaseMoveSpeed() * 1.6;
		} else if (airjumptimes < 3) {
			this.moveSpeed = getBaseMoveSpeed() * 1.35;
		} else if (airjumptimes < 4) {
			this.moveSpeed = getBaseMoveSpeed() * 1.05;
		} else {
			this.moveSpeed = getBaseMoveSpeed();
			return;
		}

		MovementInput movementInput = mc.thePlayer.movementInput;
		float forward = movementInput.moveForward;
		float strafe = movementInput.moveStrafe;
		float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
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

	@Override
	public void onDisable() {
		height = 0;
		super.onDisable();
	}

	private double getBaseMoveSpeed() {
		double baseSpeed = 0.2873D;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
		}
		return baseSpeed;
	}
}

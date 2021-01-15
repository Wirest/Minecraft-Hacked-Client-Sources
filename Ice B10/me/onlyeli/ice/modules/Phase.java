package me.onlyeli.ice.modules;

import org.lwjgl.input.Keyboard;

import me.onlyeli.eventapi.EventManager;
import me.onlyeli.eventapi.EventTarget;
import me.onlyeli.ice.Category;
import me.onlyeli.ice.Module;
import me.onlyeli.ice.events.Event2;
import me.onlyeli.ice.events.EventBBSet;
import me.onlyeli.ice.events.Event2;
import me.onlyeli.ice.events.EventTick;
import me.onlyeli.ice.events.EventUpdate;
import me.onlyeli.ice.utils.BlockUtils;
import me.onlyeli.ice.utils.EntityUtils;
import me.onlyeli.ice.utils.ModeUtils;
import me.onlyeli.ice.utils.NetUtils;
import me.onlyeli.ice.utils.TimeHelper;
import me.onlyeli.ice.utils.Wrapper;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Timer;

public class Phase extends Module {

	public Phase() {
		super("Phase", Keyboard.KEY_RCONTROL, Category.MOVEMENT);
	}

	private TimeHelper timer;
	private int resetNext;

	@Override
	public void onEnable() {
		this.timer = new TimeHelper();
		EventManager.register(this);
	}

	@Override
	public void onDisable() {
		Timer.timerSpeed = 1;
		Wrapper.mc.thePlayer.noClip = false;
		EventManager.unregister(this);
	}

	@EventTarget
	public void onTick(EventTick event) {
		if (ModeUtils.phaseMode.equalsIgnoreCase("spider")) {
			this.setModName(String.format("%s %s", this.getModName(), "[Spider]"));
		} else if (ModeUtils.phaseMode.equalsIgnoreCase("vanilla")) {
			this.setModName(String.format("%s %s", this.getModName(), "[Vanilla]"));
		} else if (ModeUtils.phaseMode.equalsIgnoreCase("new")) {
			this.setModName(String.format("%s %s", this.getModName(), "[New]"));
		} else if (ModeUtils.phaseMode.equalsIgnoreCase("latest")) {
			this.setModName(String.format("%s %s", this.getModName(), "[Latest]"));
		}
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		Timer.timerSpeed = 1;
		if (ModeUtils.phaseMode.equalsIgnoreCase("vanilla") || ModeUtils.phaseMode.equalsIgnoreCase("spider")) {
			if (event.state == Event2.State.PRE) {
				if (ModeUtils.phaseMode.equalsIgnoreCase("spider")) {
					if (this.timer.hasPassed(150.0) && Wrapper.mc.thePlayer.isCollidedHorizontally) {
						float yaw = Wrapper.mc.thePlayer.rotationYaw;
						if (Wrapper.mc.thePlayer.moveForward < 0.0f) {
							yaw += 180.0f;
						}
						if (Wrapper.mc.thePlayer.moveStrafing > 0.0f) {
							yaw -= 90.0f * ((Wrapper.mc.thePlayer.moveForward < 0.0f) ? -0.5f
									: ((Wrapper.mc.thePlayer.moveForward > 0.0f) ? 0.5f : 1.0f));
						}
						if (Wrapper.mc.thePlayer.moveStrafing < 0.0f) {
							yaw += 90.0f * ((Wrapper.mc.thePlayer.moveForward < 0.0f) ? -0.5f
									: ((Wrapper.mc.thePlayer.moveForward > 0.0f) ? 0.5f : 1.0f));
						}
						double horizontalMultiplier = 0.3;
						double xOffset = (float) Math.cos((yaw + 90.0f) * Math.PI / 180.0) * 0.3;
						double zOffset = (float) Math.sin((yaw + 90.0f) * Math.PI / 180.0) * 0.3;
						double yOffset = 0.0;
						for (int i = 0; i < 3; ++i) {
							yOffset += 0.01;
							Wrapper.mc.getNetHandler()
									.addToSendQueue(new C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX,
											Wrapper.mc.thePlayer.posY - yOffset, Wrapper.mc.thePlayer.posZ,
											Wrapper.mc.thePlayer.onGround));
							Wrapper.mc.getNetHandler()
									.addToSendQueue(new C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX + xOffset * i,
											Wrapper.mc.thePlayer.posY, Wrapper.mc.thePlayer.posZ + zOffset * i,
											Wrapper.mc.thePlayer.onGround));
						}
					}
				} else if (ModeUtils.phaseMode.equalsIgnoreCase("vanilla")) {
					double mx2 = Math.cos(Math.toRadians(Wrapper.mc.thePlayer.rotationYaw + 90.0f));
					double mz2 = Math.sin(Math.toRadians(Wrapper.mc.thePlayer.rotationYaw + 90.0f));
					double x = Wrapper.mc.thePlayer.movementInput.moveForward * 2.6 * mx2
							+ Wrapper.mc.thePlayer.movementInput.moveStrafe * 2.6 * mz2;
					double z = Wrapper.mc.thePlayer.movementInput.moveForward * 2.6 * mz2
							- Wrapper.mc.thePlayer.movementInput.moveStrafe * 2.6 * mx2;
					if (Wrapper.mc.thePlayer.isCollidedHorizontally && !Wrapper.mc.thePlayer.isOnLadder()
							&& BlockUtils.isInsideBlock() && timer.hasPassed(150) && Wrapper.mc.thePlayer.isSneaking()) {
						// 8.988465674311579E307
						NetUtils.sendPacket(new C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX + x,
								Wrapper.mc.thePlayer.posY, Wrapper.mc.thePlayer.posZ + z, false));
						EntityUtils.blinkToPos(
								new double[] { Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY, Wrapper.mc.thePlayer.posZ },
								new BlockPos(Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY, Wrapper.mc.thePlayer.posZ), 1,
								new double[] { x, z });
						timer.reset();
					}
				} else if (!Wrapper.mc.thePlayer.isCollidedHorizontally) {
					this.timer.reset();
				}
			}
		} else if (ModeUtils.phaseMode.equalsIgnoreCase("new") || ModeUtils.phaseMode.equalsIgnoreCase("latest")) {
			double mx2 = Math.cos(Math.toRadians(Wrapper.mc.thePlayer.rotationYaw + 90.0f));
			double mz2 = Math.sin(Math.toRadians(Wrapper.mc.thePlayer.rotationYaw + 90.0f));
			double x = Wrapper.mc.thePlayer.movementInput.moveForward * 0.3 * mx2
					+ Wrapper.mc.thePlayer.movementInput.moveStrafe * 0.3 * mz2;
			double z = Wrapper.mc.thePlayer.movementInput.moveForward * 0.3 * mz2
					- Wrapper.mc.thePlayer.movementInput.moveStrafe * 0.3 * mx2;
			if (event.state == Event2.State.POST) {
				if (Wrapper.mc.thePlayer.isCollidedHorizontally && !Wrapper.mc.thePlayer.isOnLadder()
						&& !BlockUtils.isInsideBlock() && timer.hasPassed(150)) {
					NetUtils.sendPacket(new C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX + x, Wrapper.mc.thePlayer.posY,
							Wrapper.mc.thePlayer.posZ + z, true));
					if (ModeUtils.phaseMode.equalsIgnoreCase("new")) {
						for (int i = 0; i < 1; i++) {
							NetUtils.sendPacket(new C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX + x,
									Double.MIN_VALUE, Wrapper.mc.thePlayer.posZ + z, true));
						}
						Wrapper.mc.thePlayer.setPosition(Wrapper.mc.thePlayer.posX + x, Wrapper.mc.thePlayer.posY,
								Wrapper.mc.thePlayer.posZ + z);
					} else if (ModeUtils.phaseMode.equalsIgnoreCase("latest")) {
						EntityUtils.blinkToPos(
								new double[] { Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY, Wrapper.mc.thePlayer.posZ },
								(new BlockPos(Wrapper.mc.thePlayer.posX + x, Double.MIN_VALUE,
										Wrapper.mc.thePlayer.posZ + z)),
								0, new double[] { 0, 0 });
					}
					timer.reset();
				}
			}
		}
	}

	@EventTarget
	public void onBBSet(EventBBSet event) {
		if ((ModeUtils.phaseMode.equalsIgnoreCase("vanilla") || ModeUtils.phaseMode.equalsIgnoreCase("new")
				|| ModeUtils.phaseMode.equalsIgnoreCase("latest")) && !timer.hasPassed(150)) {
			return;
		}
		Wrapper.mc.thePlayer.noClip = true;
		if (event.pos.getY() > Wrapper.mc.thePlayer.posY + (BlockUtils.isInsideBlock() ? 0 : 1)) {
			event.boundingBox = null;
		}
		if (Wrapper.mc.thePlayer.isCollidedHorizontally && event.pos.getY() > Wrapper.mc.thePlayer.boundingBox.minY - 0.4) {
			event.boundingBox = null;
		}
	}

}

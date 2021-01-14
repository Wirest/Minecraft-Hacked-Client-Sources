package de.iotacb.client.module.modules.movement.flies;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.LivingUpdateEvent;
import de.iotacb.client.events.player.MoveEvent;
import de.iotacb.client.events.player.SafewalkEvent;
import de.iotacb.client.events.player.UpdateEvent;
import de.iotacb.client.events.render.RenderEvent;
import de.iotacb.client.events.states.PacketState;
import de.iotacb.client.events.states.UpdateState;
import de.iotacb.client.events.world.PacketEvent;
import de.iotacb.client.events.world.TickEvent;
import de.iotacb.client.module.modules.misc.Disabler;
import de.iotacb.client.module.modules.movement.Fly;
import de.iotacb.client.module.modules.movement.Speed;
import de.iotacb.client.utilities.misc.Timer;
import de.iotacb.client.utilities.player.MovementUtil;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class Hypixel extends FlyMode {

	private final Timer delay;

	private int ticks, stage;
	private double lastDist, moveSpeed, y;

	public Hypixel() {
		super("Hypixel");
		this.delay = new Timer();
	}

	@Override
	public void onEnable() {
		if (!Client.INSTANCE.getModuleManager().getModuleByClass(Disabler.class).isEnabled()) {
			Client.INSTANCE.getModuleManager().getModuleByClass(Fly.class).setEnabled(false);
			Client.PRINTER.printMessage("Please enable the Disabler module first and reconnect to the server!");
		}
		y = 0.0;
		lastDist = 0.0;
		moveSpeed = 0.0;
		stage = 0;
		ticks = 0;
		Client.MOVEMENT_UTIL.stop(false);
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onToggle() {
	}

	@Override
	public void onRender(RenderEvent event) {
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		if (event.getState() == UpdateState.PRE) {
			if (stage > 2) {
				getMc().thePlayer.motionY = 0.0;
			}
			if (stage > 2) {
				getMc().thePlayer.setPosition(getMc().thePlayer.posX, getMc().thePlayer.posY - .003, getMc().thePlayer.posZ);
				++ticks;
				final double offset = 3.25E-4;
				switch (this.ticks) {
				case 1:
					y *= -0.949999988079071;
					break;
				case 4:
					y += 3.25E-4;
					break;
				case 5:
					y += 5.0E-4;
					ticks = 0;
					break;
				}
				event.setY(getMc().thePlayer.posY + y);
			}
		} else {
			if (stage > 2) {
				getMc().thePlayer.setPosition(getMc().thePlayer.posX, getMc().thePlayer.posY + .003, getMc().thePlayer.posZ);
			}
		}
		if (event.getState() == UpdateState.PRE) {
			final double xDif = getMc().thePlayer.posX - getMc().thePlayer.prevPosX;
			final double zDif = getMc().thePlayer.posZ - getMc().thePlayer.prevPosZ;
			lastDist = Math.sqrt(xDif * xDif + zDif * zDif);
		}
	}

	@Override
	public void onLivingUpdate(LivingUpdateEvent event) {
	}

	@Override
	public void onMove(MoveEvent event) {
		if (Client.MOVEMENT_UTIL.isMoving()) {
			switch (this.stage) {
			case 0:
				if (getMc().thePlayer.onGround && getMc().thePlayer.isCollidedVertically) {
					final double offset = 0.060100000351667404;
					final double x = getMc().thePlayer.posX;
					final double y = getMc().thePlayer.posY;
					final double z = getMc().thePlayer.posZ;
					for (int i = 0; i < getMc().thePlayer.getMaxFallHeight() / 0.05510000046342611 + 1.0; ++i) {
						getMc().getNetHandler().addToSendQueue2(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.060100000351667404, z, false));
						getMc().getNetHandler().addToSendQueue2(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 5.000000237487257E-4, z, false));
						getMc().getNetHandler().addToSendQueue2(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.004999999888241291 + 6.01000003516674E-8, z, false));
					}
					moveSpeed = 0.5;
					break;
				}
				break;
			case 1:
				if (getMc().thePlayer.onGround && getMc().thePlayer.isCollidedVertically) {
					final double jumpBoostModifier = Client.MOVEMENT_UTIL.getJumpBoostModifier(0.39999994);
					getMc().thePlayer.motionY = jumpBoostModifier;
					event.setMotionY(jumpBoostModifier);
				}
				moveSpeed *= 1.8;
				break;
			case 2:
				moveSpeed = 1.3;
				break;
			default:
				moveSpeed = lastDist - lastDist / 159.0;
				break;
			}
			Client.MOVEMENT_UTIL.setSpeed(event, Math.max(moveSpeed, Client.MOVEMENT_UTIL.getBaseMoveSpeed()));
			++stage;
		}
	}

	@Override
	public void onTick(TickEvent event) {
	}

	@Override
	public void onSafe(SafewalkEvent event) {
	}

	@Override
	public void onPacket(PacketEvent event) {
		if (event.getPacketState() == PacketState.SEND) {
			if (this.stage == 0) {
				event.setCancelled(true);
			}
		}
		if (event.getPacketState() == PacketState.RECEIVE) {
			if (event.getPacket() instanceof S08PacketPlayerPosLook) {
				Client.INSTANCE.getModuleManager().getModuleByClass(Fly.class).setEnabled(false);
			}
		}
	}

}

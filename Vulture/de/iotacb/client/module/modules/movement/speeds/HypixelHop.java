package de.iotacb.client.module.modules.movement.speeds;

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
import de.iotacb.client.module.modules.movement.Speed;
import de.iotacb.client.utilities.player.MovementUtil;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.MathHelper;

public class HypixelHop extends SpeedMode {

	public HypixelHop() {
		super("HypixelHop");
	}

	private boolean doJump;
	private double moveSpeed;

	@Override
	public void onEnable() {
		doJump = false;
	}

	@Override
	public void onDisable() {
		getMc().thePlayer.speedInAir = 0.02F;
		getMc().timer.timerSpeed = 1F;
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
			final double yaw = Client.MOVEMENT_UTIL.getYaw(true);
			if (Client.MOVEMENT_UTIL.isMoving()) {
				if (getMc().thePlayer.onGround) {
//					getMc().thePlayer.onGround = false;
					getMc().thePlayer.jump();
					if (getMc().thePlayer.hurtTime > 0) {
						getMc().thePlayer.motionX -= (MathHelper.sin((float) Client.MOVEMENT_UTIL.getYaw(true)) * 0.25F);
						getMc().thePlayer.motionZ += (MathHelper.cos((float) Client.MOVEMENT_UTIL.getYaw(true)) * 0.25F);
					}
//					if (doJump) {
//						System.out.println("ff");
//					}
				} else {
					Client.MOVEMENT_UTIL.doStrafe(Client.MOVEMENT_UTIL.getSpeed(), yaw);
					if (getMc().thePlayer.hurtTime > 0) {
						getMc().thePlayer.speedInAir = 0.03F;
					} else {
						getMc().thePlayer.speedInAir = 0.022F;
					}
				}
			} else {
				getMc().thePlayer.motionX = 0D;
				getMc().thePlayer.motionZ = 0D;
			}
		}
	}

	@Override
	public void onLivingUpdate(LivingUpdateEvent event) {
	}

	@Override
	public void onMove(MoveEvent event) {
	}

	@Override
	public void onTick(TickEvent event) {
	}

	@Override
	public void onSafe(SafewalkEvent event) {
	}

	@Override
	public void onPacket(PacketEvent event) {
		if (event.getPacketState() == PacketState.RECEIVE) {
			if (event.getPacket() instanceof S08PacketPlayerPosLook) {
				Client.PRINTER.printMessage("Disabling speed because of a flag");
				Client.INSTANCE.getModuleManager().getModuleByClass(Speed.class).setEnabled(false);
			}
		}
	}

}

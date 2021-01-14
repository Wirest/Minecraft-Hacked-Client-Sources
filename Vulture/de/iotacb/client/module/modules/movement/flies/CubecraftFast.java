package de.iotacb.client.module.modules.movement.flies;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.LivingUpdateEvent;
import de.iotacb.client.events.player.MoveEvent;
import de.iotacb.client.events.player.SafewalkEvent;
import de.iotacb.client.events.player.UpdateEvent;
import de.iotacb.client.events.render.RenderEvent;
import de.iotacb.client.events.states.UpdateState;
import de.iotacb.client.events.world.PacketEvent;
import de.iotacb.client.events.world.TickEvent;
import de.iotacb.client.module.modules.movement.Fly;
import de.iotacb.client.utilities.player.MovementUtil;

public class CubecraftFast extends FlyMode {

	public CubecraftFast() {
		super("CubecraftFast");
	}

	private int stage;

	@Override
	public void onEnable() {
		stage = 0;
		getMc().timer.timerSpeed = .6F;
	}

	@Override
	public void onDisable() {
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
//			stage++;
//			getMc().thePlayer.onGround = false;
//			getMc().thePlayer.lastReportedPosY = 0;
//			if (stage > 2) stage = 0;
//			if (stage == 1) {
//				getMc().thePlayer.motionY = -.3;
//			} else if (stage == 2) {
//				final double yaw = Math.toRadians(getMc().thePlayer.rotationYaw);
//				final double x = -Math.sin(yaw) * 2;
//				final double z = Math.cos(yaw) * 2;
//				if (Client.MOVEMENT_UTIL.isMoving()) {
//					getMc().thePlayer.setPosition(getMc().thePlayer.posX + x, getMc().thePlayer.posY + .6, getMc().thePlayer.posZ + z);
//				}
//			}
			if (getMc().thePlayer.ticksExisted % 2 == 0) {
				getMc().timer.timerSpeed = 1.2F;
//				final double yaw = Math.toRadians(getMc().thePlayer.rotationYaw);
//				final double x = -Math.sin(yaw) * 2.5;
//				final double z = Math.cos(yaw) * 2.5;
				if (Client.MOVEMENT_UTIL.isMoving()) {
//					getMc().thePlayer.setPosition(getMc().thePlayer.posX + x, getMc().thePlayer.posY + .3, getMc().thePlayer.posZ + z);
					Client.MOVEMENT_UTIL.forward(2.5, .3);
				}
			} else {
				getMc().timer.timerSpeed = .4F;
				getMc().thePlayer.motionY = -.1;
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
	}

}

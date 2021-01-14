package de.iotacb.client.module.modules.movement.speeds;

import java.util.concurrent.ThreadLocalRandom;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.LivingUpdateEvent;
import de.iotacb.client.events.player.MoveEvent;
import de.iotacb.client.events.player.SafewalkEvent;
import de.iotacb.client.events.player.UpdateEvent;
import de.iotacb.client.events.render.RenderEvent;
import de.iotacb.client.events.states.UpdateState;
import de.iotacb.client.events.world.PacketEvent;
import de.iotacb.client.events.world.TickEvent;
import de.iotacb.client.utilities.player.MovementUtil;

public class Hive extends SpeedMode {
	
	public Hive() {
		super("HiveMC");
	}
	
	private int jumps;

	@Override
	public void onEnable() {
		jumps = 0;
	}

	@Override
	public void onDisable() {
		if (getMc().thePlayer == null) return;
		getMc().thePlayer.speedInAir = 0.02F;
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
			if (Client.MOVEMENT_UTIL.isMoving()) {
				if (getMc().thePlayer.onGround) {
					if (jumps > 1) {
						getMc().thePlayer.onGround = false;
						Client.MOVEMENT_UTIL.doStrafe(0.375F);
						getMc().thePlayer.jump();
						getMc().thePlayer.motionY = .4;
					} else {
						getMc().thePlayer.jump();
					}
				} else {
					getMc().thePlayer.speedInAir = 0.021F;
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
	}

}

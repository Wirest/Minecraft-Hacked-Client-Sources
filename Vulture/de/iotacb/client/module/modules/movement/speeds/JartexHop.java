package de.iotacb.client.module.modules.movement.speeds;

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

public class JartexHop extends SpeedMode {

	public JartexHop() {
		super("JartexHop");
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onToggle() {
		getMc().timer.timerSpeed = 1F;
	}

	@Override
	public void onRender(RenderEvent event) {
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		if (event.getState() == UpdateState.PRE) {
			if (!Client.MOVEMENT_UTIL.isMoving()) {
				getMc().timer.timerSpeed = 1F;
				Client.MOVEMENT_UTIL.stop(false);
				return;
			}
			if (getMc().thePlayer.onGround) {
				getMc().timer.timerSpeed = getMc().thePlayer.isSwingInProgress ? 1 : 2;
				getMc().thePlayer.jump();
				getMc().thePlayer.motionY = .4;
			} else {
				getMc().timer.timerSpeed = getMc().thePlayer.isSwingInProgress ? 1 : 1.4F;
			}
			Client.MOVEMENT_UTIL.doStrafe();
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

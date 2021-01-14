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
import de.iotacb.client.utilities.misc.Printer;
import de.iotacb.client.utilities.player.MovementUtil;

public class Redesky extends SpeedMode {

	private int stage;
	
	public Redesky() {
		super("Redesky");
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
		getMc().thePlayer.jumpMovementFactor = .02F;
	}

	@Override
	public void onToggle() {
		getMc().timer.timerSpeed = 1F;
		getMc().thePlayer.speedInAir = .02F;
		stage = 0;
		getMc().timer.timerSpeed = 1F;
	}

	@Override
	public void onRender(RenderEvent event) {
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		if (event.getState() == UpdateState.PRE) {
			if (Client.MOVEMENT_UTIL.isMoving()) {
				if (getMc().thePlayer.onGround) {
					Client.MOVEMENT_UTIL.doStrafe(.3);
					getMc().thePlayer.jump();
					getMc().thePlayer.motionY = .4;
					stage++;
					if (!getMc().thePlayer.isSwingInProgress) getMc().timer.timerSpeed = 4F;
				} else {
					if (getMc().thePlayer.motionY < -.3) {
						getMc().thePlayer.motionY = -10;
					}
					getMc().timer.timerSpeed = 1F;
					getMc().thePlayer.motionY -= .0003;
					getMc().thePlayer.jumpMovementFactor = .03F;
					if (stage >= 2) {
						getMc().thePlayer.speedInAir = .06F;
					}
				}
			} else {
				stage = 0;
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

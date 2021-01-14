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
import de.iotacb.client.utilities.misc.Timer;
import de.iotacb.client.utilities.player.MovementUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

public class CubecraftGround extends SpeedMode {

	private final Timer timer;

	public CubecraftGround() {
		super("CubecraftGround");
		timer = new Timer();
	}
	

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
		getMc().timer.timerSpeed = 1F;
		Client.MOVEMENT_UTIL.stop(false);
	}

	@Override
	public void onToggle() {
	}

	@Override
	public void onRender(RenderEvent event) {
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		if (event.getState() != UpdateState.PRE) return;
		
		if (!Client.MOVEMENT_UTIL.isMoving()) return;
		
		if (getMc().thePlayer.onGround) {
			if (timer.delay(120)) {
				Client.MOVEMENT_UTIL.forward(1.8, 0);
			}
		} else {
			timer.reset();
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

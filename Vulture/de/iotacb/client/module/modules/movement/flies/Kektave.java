package de.iotacb.client.module.modules.movement.flies;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.LivingUpdateEvent;
import de.iotacb.client.events.player.MoveEvent;
import de.iotacb.client.events.player.SafewalkEvent;
import de.iotacb.client.events.player.SpawnEvent;
import de.iotacb.client.events.player.UpdateEvent;
import de.iotacb.client.events.render.RenderEvent;
import de.iotacb.client.events.world.PacketEvent;
import de.iotacb.client.events.world.TickEvent;

public class Kektave extends FlyMode {

	public Kektave() {
		super("Kektave");
	}
	
	private boolean jump;
	private int count;

	@Override
	public void onEnable() {
		getMc().thePlayer.motionY = .4;
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
	}

	@Override
	public void onLivingUpdate(LivingUpdateEvent event) {
		if (Client.MOVEMENT_UTIL.isMoving()) {
			Client.MOVEMENT_UTIL.doStrafe(4);
			getMc().thePlayer.motionY = 0;
		}
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
	
	@Override
	public void onSpawn(SpawnEvent event) {
		super.onSpawn(event);
	}

}

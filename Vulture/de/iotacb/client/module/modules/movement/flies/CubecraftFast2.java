package de.iotacb.client.module.modules.movement.flies;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.LivingUpdateEvent;
import de.iotacb.client.events.player.MoveEvent;
import de.iotacb.client.events.player.SafewalkEvent;
import de.iotacb.client.events.player.UpdateEvent;
import de.iotacb.client.events.render.RenderEvent;
import de.iotacb.client.events.world.PacketEvent;
import de.iotacb.client.events.world.TickEvent;
import de.iotacb.client.module.modules.combat.Aura;
import de.iotacb.client.utilities.misc.Timer;
import de.iotacb.client.utilities.player.MovementUtil;

public class CubecraftFast2 extends FlyMode {

	public CubecraftFast2() {
		super("CubecraftFast2");
		timer = new Timer();
	}
	
	Timer timer;

	@Override
	public void onEnable() {
		getMc().thePlayer.jump();
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
	}

	@Override
	public void onLivingUpdate(LivingUpdateEvent event) {
		final double yaw = Client.MOVEMENT_UTIL.getYaw(true);
		final double x = -Math.sin(yaw) * 2.1;
		final double z = Math.cos(yaw) * 2.1;
		if (Client.MOVEMENT_UTIL.isMoving()) {
			if (timer.delay(170)) {
				if (getMc().thePlayer.ticksExisted % 2 == 0) {
					getMc().thePlayer.setPosition(getMc().thePlayer.posX + x, getMc().thePlayer.posY - .0025, getMc().thePlayer.posZ + z);
				} else {
					getMc().thePlayer.setPosition(getMc().thePlayer.posX + x, getMc().thePlayer.posY + .0026, getMc().thePlayer.posZ + z);
				}
			}
			getMc().thePlayer.motionY = .0025;
			getMc().timer.timerSpeed = 0.3F;
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

}

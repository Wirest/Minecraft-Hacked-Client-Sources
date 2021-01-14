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
import de.iotacb.client.utilities.misc.Timer;
import de.iotacb.client.utilities.player.MovementUtil;
import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.C03PacketPlayer;

public class CubecraftHop2 extends SpeedMode {

	private boolean doBoost;
	
	public CubecraftHop2() {
		super("CubecraftHop2");
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
		getMc().timer.timerSpeed = 1F;
		getMc().thePlayer.speedInAir = .02F;
		getMc().thePlayer.jumpMovementFactor = .02F;
		Client.MOVEMENT_UTIL.stop(false);
	}

	@Override
	public void onToggle() {
		doBoost = false;
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
			if (getMc().thePlayer.onGround) {
				getMc().thePlayer.jump();
				getMc().thePlayer.onGround = false;
			} else {
				Client.MOVEMENT_UTIL.doStrafe();
			}
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

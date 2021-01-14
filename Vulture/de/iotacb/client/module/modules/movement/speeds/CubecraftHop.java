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
import de.iotacb.client.module.modules.combat.Aura;
import de.iotacb.client.utilities.misc.Printer;
import de.iotacb.client.utilities.misc.Timer;
import de.iotacb.client.utilities.player.MovementUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

public class CubecraftHop extends SpeedMode {

	public CubecraftHop() {
		super("CubecraftHop");
	}
	
	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
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
			final double yaw = Client.MOVEMENT_UTIL.getYaw(true);
			if (Client.MOVEMENT_UTIL.isMoving()) {
				if (getMc().thePlayer.onGround) {
					getMc().thePlayer.onGround = getMc().thePlayer.isSwingInProgress;
//					Client.MOVEMENT_UTIL.doStrafe(0.35F, yaw);
					getMc().thePlayer.jump();
				} else {
					Client.MOVEMENT_UTIL.doStrafe(Client.MOVEMENT_UTIL.getSpeed(), yaw);
					getMc().thePlayer.speedInAir = getMc().thePlayer.isSwingInProgress ? 0.021F : 0.025F;
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

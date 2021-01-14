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
import de.iotacb.client.module.modules.movement.Speed;
import de.iotacb.client.utilities.misc.Printer;
import de.iotacb.client.utilities.player.MovementUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

public class CubecraftBoost extends FlyMode {

	public CubecraftBoost() {
		super("CubecraftBoost");
	}
	
	@Override
	public void onEnable() {
		getMc().thePlayer.motionY = .2;
	}

	@Override
	public void onDisable() {
		Client.MOVEMENT_UTIL.doStrafe(.2);
		getMc().thePlayer.motionY = 0;
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
			if (Client.MOVEMENT_UTIL.isMoving()) {
				if (getMc().thePlayer.ticksExisted % 3 == 0) {
					getMc().thePlayer.motionY = .2;
					getMc().timer.timerSpeed = 1F;
					Client.MOVEMENT_UTIL.doStrafe(3);
				} else {
					getMc().thePlayer.motionY = -.1;
					getMc().timer.timerSpeed = .5F;
					Client.MOVEMENT_UTIL.doStrafe(.2);
				}
			} else {
				Client.MOVEMENT_UTIL.stop(true);
				getMc().timer.timerSpeed = 1F;
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

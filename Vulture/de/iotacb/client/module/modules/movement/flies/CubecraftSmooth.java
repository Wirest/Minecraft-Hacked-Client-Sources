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
import de.iotacb.client.module.modules.combat.Aura;
import de.iotacb.client.utilities.misc.Timer;
import de.iotacb.client.utilities.player.MovementUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

public class CubecraftSmooth extends FlyMode {

	double stage;

	private Timer timer;

	public CubecraftSmooth() {
		super("CubecraftSmooth");
		this.timer = new Timer();
	}

	@Override
	public void onEnable() {
		stage = 0;
		getMc().timer.timerSpeed = .4F;
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
			getMc().thePlayer.motionY = 0;
			if (Client.MOVEMENT_UTIL.isMoving()) {
				if (timer.delay2(20)) {
					final double yaw = Client.MOVEMENT_UTIL.getYaw(true);
					final double x = -Math.sin(yaw) * 1.5;
					final double z = Math.cos(yaw) * 1.5;
					getMc().thePlayer.addPosition(x, -.175, z);
				}
				
				if (timer.delay2(40)) {
					final double yaw = Client.MOVEMENT_UTIL.getYaw(true);
					final double x = -Math.sin(yaw) * 1.8;
					final double z = Math.cos(yaw) * 1.8;
					getMc().thePlayer.addPosition(x, .195, z);
					timer.reset();
				}
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

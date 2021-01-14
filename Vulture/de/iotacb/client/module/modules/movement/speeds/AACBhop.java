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
import net.minecraft.util.MathHelper;

public class AACBhop extends SpeedMode {

	private boolean doJump;

	public AACBhop() {
		super("AAC Bhop");
	}

	@Override
	public void onEnable() {
		doJump = true;
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
		if (event.getState() == UpdateState.PRE) {
			if (Client.MOVEMENT_UTIL.isMoving()) {
				if (doJump) {
					if (getMc().thePlayer.onGround) {
						getMc().thePlayer.jump();
						doJump = false;
					}
					return;
				}

				if (getMc().thePlayer.onGround) {
					Client.MOVEMENT_UTIL.doStrafe(0.375F);
					getMc().thePlayer.jump();
					getMc().thePlayer.motionY = 0.405;
		            getMc().thePlayer.motionX -= (MathHelper.sin((float) Client.MOVEMENT_UTIL.getYaw(true)) * 0.2F);
		            getMc().thePlayer.motionZ += (MathHelper.cos((float) Client.MOVEMENT_UTIL.getYaw(true)) * 0.2F);
				} else
					getMc().thePlayer.speedInAir = 0.021F;
			} else {
				if (getMc().thePlayer.onGround) {
					doJump = true;
				}
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

package de.iotacb.client.module.modules.movement.speeds;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.LivingUpdateEvent;
import de.iotacb.client.events.player.MoveEvent;
import de.iotacb.client.events.player.SafewalkEvent;
import de.iotacb.client.events.player.UpdateEvent;
import de.iotacb.client.events.render.RenderEvent;
import de.iotacb.client.events.world.PacketEvent;
import de.iotacb.client.events.world.TickEvent;
import de.iotacb.client.utilities.player.MovementUtil;
import net.minecraft.util.MathHelper;

public class AAC3313 extends SpeedMode {

	public AAC3313() {
		super("AAC 3.3.13");
	}

	@Override
	public void onEnable() {
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
        if (!Client.MOVEMENT_UTIL.isMoving() || getMc().thePlayer.isInWater() || getMc().thePlayer.isInLava() ||
                getMc().thePlayer.isOnLadder() || getMc().thePlayer.isRiding() || getMc().thePlayer.hurtTime > 0)
            return;

        if (getMc().thePlayer.onGround && getMc().thePlayer.isCollidedVertically) {
            final float yaw = (float) Math.toRadians(getMc().thePlayer.rotationYaw);
            getMc().thePlayer.motionX -= MathHelper.sin(yaw) * 0.232F;
            getMc().thePlayer.motionZ += MathHelper.cos(yaw) * 0.232F;
            getMc().thePlayer.motionY = 0.405F;
            getMc().timer.timerSpeed = 2F;
        } else {
        	getMc().timer.timerSpeed = 1F;
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

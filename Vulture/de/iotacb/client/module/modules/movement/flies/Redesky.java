package de.iotacb.client.module.modules.movement.flies;

import java.util.concurrent.ThreadLocalRandom;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.LivingUpdateEvent;
import de.iotacb.client.events.player.MoveEvent;
import de.iotacb.client.events.player.SafewalkEvent;
import de.iotacb.client.events.player.UpdateEvent;
import de.iotacb.client.events.render.RenderEvent;
import de.iotacb.client.events.world.PacketEvent;
import de.iotacb.client.events.world.TickEvent;
import de.iotacb.client.utilities.player.MovementUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Redesky extends FlyMode {

	private int stage;
	private double startPos;
	
	public Redesky() {
		super("Redesky");
	}

	@Override
	public void onEnable() {
		startPos = getMc().thePlayer.posY;
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
		final double[] bypassValues = new double[] {0.108F, 0.113F, 0.117F, 0.1215F, 0.125F};
	    if (stage > bypassValues.length - 1) {
	    	stage = 0;
	    }
	    
	    getMc().thePlayer.setPosition(getMc().thePlayer.posX, startPos + bypassValues[stage++], getMc().thePlayer.posZ);
	    getMc().thePlayer.motionY = 0;
	}

	@Override
	public void onMove(MoveEvent event) {
		if (!getMc().thePlayer.onGround)
			Client.MOVEMENT_UTIL.doStrafe(.28);
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

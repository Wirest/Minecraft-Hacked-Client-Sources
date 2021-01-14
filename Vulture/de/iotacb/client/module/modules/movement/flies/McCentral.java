package de.iotacb.client.module.modules.movement.flies;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.LivingUpdateEvent;
import de.iotacb.client.events.player.MoveEvent;
import de.iotacb.client.events.player.SafewalkEvent;
import de.iotacb.client.events.player.UpdateEvent;
import de.iotacb.client.events.render.RenderEvent;
import de.iotacb.client.events.world.PacketEvent;
import de.iotacb.client.events.world.TickEvent;
import de.iotacb.client.module.modules.movement.Fly;
import de.iotacb.client.utilities.misc.Timer;
import de.iotacb.client.utilities.player.MovementUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

public class McCentral extends FlyMode {

	private final Timer delay;
	
	public McCentral() {
		super("McCentral");
		this.delay = new Timer();
	}
	
	@Override
	public void onEnable() {
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
		if (delay.delay(140)) {
			getMc().thePlayer.motionY = getMc().gameSettings.keyBindJump.pressed ? Client.INSTANCE.getModuleManager().getModuleByClass(Fly.class).getValueByName("FlyMotion y").getNumberValue() : getMc().gameSettings.keyBindSneak.pressed ? -Client.INSTANCE.getModuleManager().getModuleByClass(Fly.class).getValueByName("FlyMotion y").getNumberValue() : .1;
		}
		if (Client.MOVEMENT_UTIL.isMoving()) {
			Client.MOVEMENT_UTIL.doStrafe(Client.INSTANCE.getModuleManager().getModuleByClass(Fly.class).getValueByName("FlyBoost").getNumberValue());
		} else {
			Client.MOVEMENT_UTIL.stop(false);
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

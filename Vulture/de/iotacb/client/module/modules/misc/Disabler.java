package de.iotacb.client.module.modules.misc;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.UpdateEvent;
import de.iotacb.client.events.states.PacketState;
import de.iotacb.client.events.world.PacketEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.module.modules.movement.Fly;
import de.iotacb.client.module.modules.movement.Speed;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;

@ModuleInfo(name = "Disabler", description = "Disables some checks of Watchdog", category = Category.MISC)
public class Disabler extends Module {

	@Override
	public void onInit() {
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
	
	@EventTarget
	public void onUpdate(UpdateEvent event) {
		if (Client.INSTANCE.getModuleManager().getModuleByClass(Fly.class).isEnabled() || Client.INSTANCE.getModuleManager().getModuleByClass(Speed.class).isEnabled()) {
			final PlayerCapabilities playerCapabilities = new PlayerCapabilities();
			playerCapabilities.isFlying = true;
			playerCapabilities.setFlySpeed((float) ThreadLocalRandom.current().nextDouble(.1, 9.0));
			getMc().getNetHandler().addToSendQueue2(new C13PacketPlayerAbilities(playerCapabilities));
		}
	}
	
	@EventTarget
	public void onPacket(PacketEvent event) {
		if (event.getPacketState() == PacketState.SEND) {
            if (event.getPacket() instanceof C0FPacketConfirmTransaction) {
                final C0FPacketConfirmTransaction packetConfirmTransaction = (C0FPacketConfirmTransaction)event.getPacket();
                getMc().getNetHandler().addToSendQueue2(new C0FPacketConfirmTransaction(Integer.MAX_VALUE, packetConfirmTransaction.getUid(), false));
                event.setCancelled(true);
            }
            if (event.getPacket() instanceof C00PacketKeepAlive) {
            	getMc().getNetHandler().addToSendQueue2(new C00PacketKeepAlive(Integer.MIN_VALUE + new Random().nextInt(100)));
                event.setCancelled(true);
            }
		}
	}

}

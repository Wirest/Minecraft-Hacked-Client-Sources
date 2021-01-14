package me.memewaredevs.client.module.render;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.PacketInEvent;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;
import net.minecraft.network.play.server.S03PacketTimeUpdate;

import java.util.function.Consumer;

public class Ambiance extends Module {

	public Ambiance() {
		super("Ambience", 0, Module.Category.RENDER);
		this.addModes("Morning", "Night");
	}

	@Handler
	public Consumer<PacketInEvent> eventConsumer0 = (event) -> {
		if (event.getPacket() instanceof S03PacketTimeUpdate) {
			event.cancel();
		}
	};

	@Handler
	public Consumer<UpdateEvent> eventConsumer1 = (event) -> {
		mc.theWorld.getWorldInfo().setWorldTime(this.isMode("Morning") ? 0 : 20000);
	};

}

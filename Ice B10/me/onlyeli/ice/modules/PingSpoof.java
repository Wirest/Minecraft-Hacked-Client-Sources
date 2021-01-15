package me.onlyeli.ice.modules;

import org.lwjgl.input.Keyboard;

import me.onlyeli.eventapi.EventManager;
import me.onlyeli.eventapi.EventTarget;
import me.onlyeli.ice.Category;
import me.onlyeli.ice.Module;
import me.onlyeli.ice.events.EventPacketSend;
import net.minecraft.network.status.client.C01PacketPing;

public class PingSpoof extends Module {

	public PingSpoof() {
		super("PingSpoof", Keyboard.KEY_NONE, Category.PLAYER);
	}

	public void onEnable() {
		EventManager.register(this);
	}

	@Override
	public void onDisable() {
		EventManager.unregister(this);
	}

	@EventTarget
	public void onPacketSend(EventPacketSend event) {
		if (event.packet instanceof C01PacketPing) {
			C01PacketPing packet = (C01PacketPing) event.packet;
			packet.clientTime = 0;
			event.packet = packet;
		}
	}

}

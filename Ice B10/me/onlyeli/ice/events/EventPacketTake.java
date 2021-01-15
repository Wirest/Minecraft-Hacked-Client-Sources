package me.onlyeli.ice.events;

import me.onlyeli.eventapi.events.callables.EventCancellable;
import net.minecraft.network.Packet;

public class EventPacketTake extends EventCancellable {

	public Packet packet;

	public EventPacketTake(Packet packet) {
		this.packet = packet;
	}

}

package info.spicyclient.events.listeners;

import info.spicyclient.events.Event;
import net.minecraft.network.Packet;

public class EventSendPacket extends Event<EventSendPacket> {
	
	public EventSendPacket(Packet packet) {
		this.packet = packet;
	}
	
	public Packet packet = null;
	
}

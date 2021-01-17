package me.ihaq.iClient.event.events;

import me.ihaq.iClient.event.Event;
import net.minecraft.network.Packet;

public class EventPacketSend extends Event{
	private Packet p;

	public EventPacketSend(Packet packetIn) {
		this.p = packetIn;
	}
	
	public Packet getPacket(){
		return this.p;
	}

}

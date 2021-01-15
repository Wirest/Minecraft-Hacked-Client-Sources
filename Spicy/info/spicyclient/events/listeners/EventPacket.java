package info.spicyclient.events.listeners;

import info.spicyclient.events.Event;
import info.spicyclient.events.EventDirection;
import info.spicyclient.events.EventType;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.INetHandlerPlayServer;

public class EventPacket extends Event{
	
	public Packet packet;
	
	public EventPacket(EventType type, EventDirection dir, Packet packet) {
		
		this.setType(type);
		this.setDirection(dir);
		this.packet = packet;
		
	}
	
}

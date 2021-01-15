package nivia.events.events;

import net.minecraft.network.Packet;
import nivia.events.EventCancellable;

public class EventPacketReceive extends EventCancellable{
private Packet packet;

	public EventPacketReceive(Packet p){
		this.packet = p;
	}
	public Packet getPacket(){
		return this.packet;
	}
}

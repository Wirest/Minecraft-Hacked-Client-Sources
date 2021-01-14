package de.iotacb.client.events.world;

import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.events.callables.EventCancellable;

import de.iotacb.client.events.states.PacketState;
import net.minecraft.network.Packet;

public class PacketEvent extends EventCancellable {

	private final Packet packet;
	
	private final PacketState packetState;

	public PacketEvent(Packet packet, PacketState receive) {
		this.packet = packet;
		this.packetState = receive;
	}
	
	public Packet getPacket() {
		return packet;
	}
	
	public PacketState getPacketState() {
		return packetState;
	}
}

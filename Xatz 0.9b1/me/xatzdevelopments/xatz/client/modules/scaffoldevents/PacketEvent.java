package me.xatzdevelopments.xatz.client.modules.scaffoldevents;

import me.xatzdevelopments.xatz.client.darkmagician6.*;

import me.xatzdevelopments.xatz.client.modules.scaffoldevents.*;
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

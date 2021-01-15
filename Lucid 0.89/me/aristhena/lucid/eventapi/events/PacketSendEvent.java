package me.aristhena.lucid.eventapi.events;

import me.aristhena.lucid.eventapi.Event;
import net.minecraft.network.Packet;

public class PacketSendEvent extends Event
{
    public Packet packet;
    public State state;
    
    public PacketSendEvent(State state, Packet packet)
    {
	this.state = state;
	this.packet = packet;
    }
}

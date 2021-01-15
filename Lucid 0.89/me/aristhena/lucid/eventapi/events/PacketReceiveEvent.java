package me.aristhena.lucid.eventapi.events;

import me.aristhena.lucid.eventapi.Event;
import net.minecraft.network.Packet;

public class PacketReceiveEvent extends Event
{
    public Packet packet;
    
    public PacketReceiveEvent(Packet packet)
    {
	this.packet = packet;
    }
}

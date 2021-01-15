package me.onlyeli.ice.events;

import me.onlyeli.eventapi.events.Event;
import net.minecraft.network.Packet;

public class EventPacket
  implements Event
{
  public static final String EventPacketType = null;
public Packet packet;
  
  public EventPacket(Packet packet)
  {
    
    this.packet = packet;
  }

public Object getType() {
	return null;
}
}
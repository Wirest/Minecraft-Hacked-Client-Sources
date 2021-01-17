package me.rigamortis.faurax.events;

import com.darkmagician6.eventapi.events.callables.*;
import com.darkmagician6.eventapi.events.*;
import net.minecraft.network.*;

public class EventReceivePacket extends EventCancellable implements Event
{
    private Packet packet;
    
    public EventReceivePacket(final Packet packet) {
        this.packet = null;
        this.setPacket(packet);
    }
    
    public Packet getPacket() {
        return this.packet;
    }
    
    public void setPacket(final Packet packet) {
        this.packet = packet;
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.events;

import net.minecraft.network.Packet;
import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventSendPacket extends EventCancellable
{
    private Packet packet;
    
    public EventSendPacket(final Packet packet) {
        this.packet = packet;
    }
    
    public Packet getPacket() {
        return this.packet;
    }
    
    public void setPacket(final Packet packet) {
        this.packet = packet;
    }
}

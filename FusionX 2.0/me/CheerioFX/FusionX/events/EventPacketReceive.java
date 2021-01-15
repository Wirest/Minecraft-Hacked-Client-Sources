// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.events;

import net.minecraft.network.Packet;
import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventPacketReceive extends EventCancellable
{
    private boolean cancel;
    public Packet packet;
    private String string;
    
    public EventPacketReceive(final Packet packet) {
        this.packet = packet;
    }
    
    public Packet getPacket() {
        return this.packet;
    }
    
    @Override
    public boolean isCancelled() {
        return this.cancel;
    }
    
    @Override
    public void setCancelled(final boolean state) {
        this.cancel = state;
    }
    
    public void setPacket(final Packet packet) {
        this.packet = packet;
    }
}

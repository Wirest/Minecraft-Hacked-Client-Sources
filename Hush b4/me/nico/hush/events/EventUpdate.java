// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.events;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import com.darkmagician6.eventapi.events.Event;

public class EventUpdate implements Event
{
    public boolean Cancellable;
    
    public boolean isCancellable() {
        return this.Cancellable;
    }
    
    public void setCancellable(final boolean cancellable) {
        this.Cancellable = cancellable;
    }
    
    public C03PacketPlayer getPacket() {
        return null;
    }
    
    public void setPacket(final Packet p) {
    }
}

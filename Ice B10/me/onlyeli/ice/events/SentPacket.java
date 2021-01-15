package me.onlyeli.ice.events;

import net.minecraft.network.Packet;
import me.onlyeli.ice.events.Cancellable;
import me.onlyeli.ice.events.Events;

public class SentPacket extends Events implements Cancellable
{
    private Packet packet;
    private boolean cancel;
    
    public SentPacket(final Packet packet) {
        this.packet = packet;
    }
    
    public Packet getPacket() {
        return this.packet;
    }
    
    public void setPacket(final Packet packet) {
        this.packet = packet;
    }
    
    public boolean isCancelled() {
        return this.cancel;
    }
    
    public void setCancelled(final boolean cancel) {
        this.cancel = cancel;
    }
}

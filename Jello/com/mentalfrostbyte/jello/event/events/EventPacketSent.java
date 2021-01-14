package com.mentalfrostbyte.jello.event.events;

import com.mentalfrostbyte.jello.event.events.callables.EventCancellable;

import net.minecraft.network.Packet;

public class EventPacketSent
extends EventCancellable {
    private boolean cancel;
    private Packet packet;

    public EventPacketSent(Packet packet) {
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
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}


package me.onlyeli.ice.events;

import me.onlyeli.eventapi.events.callables.EventCancellable;
import net.minecraft.network.Packet;

public class EventPacketReceive extends EventCancellable {
    private Packet packet;

    public EventPacketReceive(Packet p) {
        this.packet = p;
    }

    public Packet getPacket() {
        return this.packet;
    }
}
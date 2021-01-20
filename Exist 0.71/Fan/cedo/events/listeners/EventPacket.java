package cedo.events.listeners;

import cedo.events.Event;
import cedo.events.EventDirection;
import net.minecraft.network.Packet;

@SuppressWarnings("rawtypes")
public class EventPacket extends Event<EventPacket> { //I (Foggy) did the code and hooks for sending packets, but receiving them is TODO

    public Packet packet;

    public EventPacket(Packet packet, EventDirection direction) {
        this.packet = packet;
        this.direction = direction;
    }


    public <T extends Packet> T getPacket() {
        return (T) packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

}

package com.ihl.client.event;

import net.minecraft.network.Packet;

public class EventPacket extends Event {

    public Packet packet;

    public EventPacket(Type type, Packet packet) {
        super(type);
        this.packet = packet;
    }

}

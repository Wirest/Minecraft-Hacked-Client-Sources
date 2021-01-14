package me.memewaredevs.client.event.events;

import net.minecraft.network.Packet;

public class PostPacketInEvent extends PacketInEvent {
    public PostPacketInEvent(Packet packet) {
        super(packet);
    }
}

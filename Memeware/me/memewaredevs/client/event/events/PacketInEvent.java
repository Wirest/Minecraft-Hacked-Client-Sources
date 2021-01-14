
package me.memewaredevs.client.event.events;

import net.minecraft.network.Packet;
import me.memewaredevs.client.event.Event;

public class PacketInEvent extends Event {
    private Packet packet;

    public PacketInEvent(final Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public void setPacket(final Packet packet) {
        this.packet = packet;
    }
}

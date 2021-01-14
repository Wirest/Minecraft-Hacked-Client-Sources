
package me.memewaredevs.client.event.events;

import java.util.List;

import net.minecraft.network.Packet;
import me.memewaredevs.client.event.Event;

public class PacketOutEvent extends Event {
    private Packet packet;

    public PacketOutEvent(final Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public void setPacket(final Packet packet) {
        this.packet = packet;
    }

    public void queueAndCancel(final List<Packet> list) {
        list.add(this.packet);
        this.cancel();
    }
}

package cheatware.event.events;

import cheatware.event.Event;
import net.minecraft.network.Packet;

public class EventSendPacket extends Event {
    private Packet packet;

    public EventSendPacket(Packet packet) {
        packet = null;
        setPacket(packet);
    }

    public Packet getPacket() {
        return packet;
    }
    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}

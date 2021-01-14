package info.sigmaclient.event.impl;

import info.sigmaclient.event.Event;
import net.minecraft.network.Packet;

public class EventPacket extends Event {
    private Packet packet;
    private boolean outgoing;

    public boolean isPre() {
        return pre;
    }

    public boolean isPost() {
        return !pre;
    }

    private boolean pre;

    public void fire(Packet packet, boolean outgoing) {
        this.packet = packet;
        this.outgoing = outgoing;
        this.pre = true;
        super.fire();
    }

    public void fire(Packet packet) {
        this.packet = packet;
        this.pre = false;
        super.fire();
    }

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public boolean isOutgoing() {
        return outgoing;
    }

    public boolean isIncoming() {
        return !outgoing;
    }
}

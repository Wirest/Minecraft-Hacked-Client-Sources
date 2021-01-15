package dev.astroclient.client.event.impl.packet;

import net.minecraft.network.Packet;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.asdw.EventCancellable;

public class EventSendPacket extends EventCancellable {

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public EventSendPacket(Packet packet) {
        this.packet = packet;
    }

    private Packet packet;
}

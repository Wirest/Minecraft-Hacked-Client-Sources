package dev.astroclient.client.event.impl.packet;

import net.minecraft.network.Packet;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.asdw.EventCancellable;

/**
 * @author Zane2711 for PublicBase
 * @since 10/28/19
 */

public class EventReceivePacket extends EventCancellable {

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public EventReceivePacket(Packet packet) {
        this.packet = packet;
    }

    private Packet packet;
}

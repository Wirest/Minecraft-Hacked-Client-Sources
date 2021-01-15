package me.xatzdevelopments.xatz.event;

import net.minecraft.network.Packet;
import me.xatzdevelopments.xatz.event.CancellableEvent;

/**
 * @author antja03
 */
public class ProcessPacketEvent extends CancellableEvent {
    private Packet packet;

    public ProcessPacketEvent(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}

/*
 * Decompiled with CFR 0_122.
 */
package darkmagician6.events;

import darkmagician6.EventCancellable;
import net.minecraft.network.Packet;

public class EventPacketRecieve
extends EventCancellable {
    private Packet packet;

    public EventPacketRecieve(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }
}


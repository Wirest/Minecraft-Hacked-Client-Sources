/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.events;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.network.Packet;

public class EventReceivePacket
implements Event {
    private Packet packet;
    public boolean cancel;

    public EventReceivePacket(Packet packet) {
        this.packet = packet;
        this.cancel = false;
    }

    public Packet getPacket() {
        return this.packet;
    }
}


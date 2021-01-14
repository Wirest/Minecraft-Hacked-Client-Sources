package me.Corbis.Execution.event.events;

import com.sun.jna.platform.mac.Carbon;
import me.Corbis.Execution.event.Event;
import net.minecraft.network.Packet;

public class EventSendPacket extends Event {
    Packet packet;
    public EventSendPacket(Packet packet){
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}

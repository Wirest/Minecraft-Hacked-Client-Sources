package me.Corbis.Execution.event.events;

import me.Corbis.Execution.event.Event;
import net.minecraft.network.Packet;

public class EventReceivePacket extends Event {
    Packet packet;
    public EventReceivePacket(Packet packet){
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}

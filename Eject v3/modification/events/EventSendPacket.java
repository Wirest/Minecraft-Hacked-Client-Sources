package modification.events;

import modification.interfaces.Event;
import net.minecraft.network.Packet;

public final class EventSendPacket
        implements Event {
    public Packet packet;

    public EventSendPacket(Packet paramPacket) {
        this.packet = paramPacket;
    }
}





package modification.events;

import modification.interfaces.Event;
import net.minecraft.network.Packet;

public final class EventProcessPacket
        implements Event {
    public final Packet packet;

    public EventProcessPacket(Packet paramPacket) {
        this.packet = paramPacket;
    }
}





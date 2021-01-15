package nivia.events.events;

import net.minecraft.network.Packet;
import nivia.events.EventCancellable;

public class EventPacketSend extends EventCancellable{
    private boolean cancel;
    private Packet packet;

    public EventPacketSend(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}

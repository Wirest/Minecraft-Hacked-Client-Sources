// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Events;

import net.minecraft.network.Packet;
import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventPacketSend extends EventCancellable
{
    public Packet packet;
    
    public EventPacketSend(final Packet packet) {
        this.packet = packet;
    }
}

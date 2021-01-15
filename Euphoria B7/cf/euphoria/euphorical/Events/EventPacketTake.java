// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Events;

import net.minecraft.network.Packet;
import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventPacketTake extends EventCancellable
{
    public Packet packet;
    
    public EventPacketTake(final Packet packet) {
        this.packet = packet;
    }
}

// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Events;

import com.darkmagician6.eventapi.events.Event;

public class EventMove implements Event
{
    public double x;
    public double y;
    public double z;
    
    public EventMove(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}

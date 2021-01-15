// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventUpdate extends EventCancellable
{
    public double y;
    public float yaw;
    public float pitch;
    public boolean onGround;
    public EventState state;
    
    public EventUpdate(final double y, final float yaw, final float pitch, final boolean onGround) {
        this.y = y;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.state = EventState.PRE;
    }
    
    public EventUpdate() {
        this.state = EventState.POST;
    }
}

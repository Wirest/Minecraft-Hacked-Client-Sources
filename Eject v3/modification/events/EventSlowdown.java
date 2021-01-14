package modification.events;

import modification.interfaces.Event;

public final class EventSlowdown
        implements Event {
    public float motionValue;

    public EventSlowdown(float paramFloat) {
        this.motionValue = paramFloat;
    }
}





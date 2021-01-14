package modification.events;

import modification.interfaces.Event;

public final class EventVelocity
        implements Event {
    public boolean canceled;
    public float value;

    public EventVelocity(float paramFloat) {
        this.value = paramFloat;
    }
}





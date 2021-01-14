package modification.events;

import modification.interfaces.Event;

public final class EventMouseClicked
        implements Event {
    public final int button;

    public EventMouseClicked(int paramInt) {
        this.button = paramInt;
    }
}





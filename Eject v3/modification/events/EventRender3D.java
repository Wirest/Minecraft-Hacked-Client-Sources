package modification.events;

import modification.interfaces.Event;

public final class EventRender3D
        implements Event {
    public final float partialTicks;

    public EventRender3D(float paramFloat) {
        this.partialTicks = paramFloat;
    }
}





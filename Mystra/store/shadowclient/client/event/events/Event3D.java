package store.shadowclient.client.event.events;

import store.shadowclient.client.event.Event;

public class Event3D extends Event {
    private static float partialTicks;

    public Event3D(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public static float getPartialTicks() {
        return partialTicks;
    }
}

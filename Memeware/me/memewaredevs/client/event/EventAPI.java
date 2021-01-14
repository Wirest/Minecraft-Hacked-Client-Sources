
package me.memewaredevs.client.event;

import me.memewaredevs.client.Memeware;

public class EventAPI {
    public static void put(final Object o) {
        Memeware.INSTANCE.getEventBus().subscribe(o);
    }

    public static void remove(final Object o) {
        Memeware.INSTANCE.getEventBus().unsubscribe(o);
    }

    public static void fire(final Event event) {
        Memeware.INSTANCE.getEventBus().post(event).dispatch();
    }
}

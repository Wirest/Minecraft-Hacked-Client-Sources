/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.events;

import com.darkmagician6.eventapi.events.Event;

public class EventSafeWalk
implements Event {
    public boolean safe;

    public EventSafeWalk(boolean safe) {
        this.safe = safe;
    }

    public void setSafe(boolean safe) {
        this.safe = safe;
    }
}


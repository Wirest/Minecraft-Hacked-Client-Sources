/*
 * Decompiled with CFR 0_122.
 */
package darkmagician6;

import darkmagician6.Event;

public abstract class EventStoppable
implements Event {
    private boolean stopped;

    protected EventStoppable() {
    }

    public void stop() {
        this.stopped = true;
    }

    public boolean isStopped() {
        return this.stopped;
    }
}


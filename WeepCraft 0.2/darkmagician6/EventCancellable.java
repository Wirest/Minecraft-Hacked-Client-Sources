/*
 * Decompiled with CFR 0_122.
 */
package darkmagician6;

import darkmagician6.Cancellable;
import darkmagician6.Event;

public abstract class EventCancellable
implements Event,
Cancellable {
    private boolean cancelled;

    protected EventCancellable() {
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean state) {
        this.cancelled = state;
    }
}


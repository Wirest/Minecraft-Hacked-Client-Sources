/*
 * Decompiled with CFR 0_122.
 */
package darkmagician6.events;

import darkmagician6.EventCancellable;

public class EventDimensionChange
extends EventCancellable {
    int dimension;

    public EventDimensionChange(int dimension) {
        this.dimension = dimension;
    }

    public int getDimension() {
        return this.dimension;
    }
}


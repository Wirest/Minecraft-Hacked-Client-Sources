/*
 * Decompiled with CFR 0_122.
 */
package darkmagician6;

import darkmagician6.Event;
import darkmagician6.Typed;

public abstract class EventTyped
implements Event,
Typed {
    private final byte type;

    protected EventTyped(byte eventType) {
        this.type = eventType;
    }

    @Override
    public byte getType() {
        return this.type;
    }
}


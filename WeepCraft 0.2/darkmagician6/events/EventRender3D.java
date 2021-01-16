/*
 * Decompiled with CFR 0_122.
 */
package darkmagician6.events;

import darkmagician6.EventCancellable;

public class EventRender3D
extends EventCancellable {
    public static float partialTicks;

    public EventRender3D(float partialTicks) {
        EventRender3D.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}


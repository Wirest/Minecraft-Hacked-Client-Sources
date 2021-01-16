/*
 * Decompiled with CFR 0_122.
 */
package darkmagician6.events;

import darkmagician6.EventCancellable;

public class EventPreMotionUpdates
extends EventCancellable {
    private boolean cancel;
    public float yaw;
    public float pitch;

    public EventPreMotionUpdates(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public float getPitch() {
        return this.pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean state) {
        this.cancel = state;
    }
}


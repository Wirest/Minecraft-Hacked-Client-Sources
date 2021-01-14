
package me.memewaredevs.client.event.events;

import me.memewaredevs.client.event.Event;

public class Render3DEvent extends Event {
    private final float tick;

    public Render3DEvent(float tick) {
        this.tick = tick;
    }

    public float getRenderTick() {
        return this.tick;
    }
}

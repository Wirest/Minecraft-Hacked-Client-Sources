package me.ihaq.iClient.event.events;

import me.ihaq.iClient.event.Event;

/**
 * Created by Hexeption on 07/01/2017.
 */
public class EventRender3D extends Event {

    public float particlTicks;

    public EventRender3D(float particlTicks) {

        this.particlTicks = particlTicks;
    }

    public float getParticalTicks() {

        return particlTicks;
    }

    public void setParticalTicks(float particlTicks) {

        this.particlTicks = particlTicks;
    }
}

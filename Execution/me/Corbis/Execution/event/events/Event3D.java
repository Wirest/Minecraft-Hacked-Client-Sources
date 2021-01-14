package me.Corbis.Execution.event.events;

import me.Corbis.Execution.event.Event;

public class Event3D extends Event {
    float partialTicks;
    public Event3D(float partialTicks){
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}

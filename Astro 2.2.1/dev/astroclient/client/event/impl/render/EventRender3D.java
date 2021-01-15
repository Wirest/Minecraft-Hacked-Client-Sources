package dev.astroclient.client.event.impl.render;

import awfdd.ksksk.ap.zajkb.rgds.Event;

public class EventRender3D extends Event {

    public EventRender3D(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    private float partialTicks;

}

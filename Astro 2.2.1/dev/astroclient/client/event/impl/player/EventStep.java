package dev.astroclient.client.event.impl.player;

import awfdd.ksksk.ap.zajkb.rgds.Event;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.asdw.EventType;

public class EventStep extends Event {

    private double stepHeight;

    public EventStep(EventType type, double stepHeight) {
        super(type);
        this.stepHeight = stepHeight;
    }

    public double getStepHeight() {
        return this.stepHeight;
    }

    public void setStepHeight(double stepHeight) {
        this.stepHeight = stepHeight;
    }

}

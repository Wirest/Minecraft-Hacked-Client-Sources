package me.memewaredevs.client.event.events;

import me.memewaredevs.client.event.Event;

public class ClimbSlabEvent extends Event {
    private double stepHeight;
    private double realHeight;
    private boolean pre;

    public ClimbSlabEvent(boolean state, double stepHeight, double realHeight) {
        this.pre = state;
        this.stepHeight = stepHeight;
        this.realHeight = realHeight;
    }

    public ClimbSlabEvent(boolean state, double stepHeight) {
        this.pre = state;
        this.realHeight = stepHeight;
        this.stepHeight = stepHeight;
    }

    public boolean isPre() {
        return pre;
    }

    public double getStepHeight() {
        return this.stepHeight;
    }

    public void setStepHeight(double stepHeight) {
        this.stepHeight = stepHeight;
    }

    public double getRealHeight() {
        return this.realHeight;
    }

    public void setRealHeight(double realHeight) {
        this.realHeight = realHeight;
    }
}

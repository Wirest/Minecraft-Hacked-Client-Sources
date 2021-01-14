package info.sigmaclient.event.impl;

import info.sigmaclient.event.Event;

/**
 * Created by cool1 on 1/16/2017.
 */
public class EventStep extends Event {
    private double stepHeight;
    private double realHeight;
    private boolean active;
    private boolean pre;

    public void fire(boolean state, double stepHeight, double realHeight) {
        this.pre = state;
        this.stepHeight = stepHeight;
        this.realHeight = realHeight;
        super.fire();
    }

    public void fire(boolean state, double stepHeight) {
        this.pre = state;
        this.realHeight = stepHeight;
        this.stepHeight = stepHeight;
        super.fire();
    }

    public boolean isPre() {
        return pre;
    }

    public double getStepHeight() {
        return this.stepHeight;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setStepHeight(double stepHeight) {
        this.stepHeight = stepHeight;
    }

    public void setActive(boolean bypass) {
        this.active = bypass;
    }

    public double getRealHeight() {
        return this.realHeight;
    }

    public void setRealHeight(double realHeight) {
        this.realHeight = realHeight;
    }

}

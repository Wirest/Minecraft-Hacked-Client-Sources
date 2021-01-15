// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.event.events;

import me.aristhena.event.Event;

public class StepEvent extends Event
{
    private double stepHeight;
    private double realHeight;
    private boolean active;
    private State state;
    
    public StepEvent(final double stepHeight, final State state, final double realHeight) {
        this.stepHeight = stepHeight;
        this.state = state;
        this.realHeight = realHeight;
    }
    
    public StepEvent(final double stepHeight, final State state) {
        this.stepHeight = stepHeight;
        this.state = state;
    }
    
    public double getStepHeight() {
        return this.stepHeight;
    }
    
    public boolean isActive() {
        return this.active;
    }
    
    public void setStepHeight(final double stepHeight) {
        this.stepHeight = stepHeight;
    }
    
    public void setActive(final boolean bypass) {
        this.active = bypass;
    }
    
    public State getState() {
        return this.state;
    }
    
    public void setState(final State state) {
        this.state = state;
    }
    
    public double getRealHeight() {
        return this.realHeight;
    }
    
    public void setRealHeight(final double realHeight) {
        this.realHeight = realHeight;
    }
}

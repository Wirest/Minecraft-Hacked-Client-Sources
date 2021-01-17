// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

public final class Event
{
    private Component component;
    private float value;
    private long nanos;
    
    public final void set(final Event other) {
        this.set(other.getComponent(), other.getValue(), other.getNanos());
    }
    
    public final void set(final Component component, final float value, final long nanos) {
        this.component = component;
        this.value = value;
        this.nanos = nanos;
    }
    
    public final Component getComponent() {
        return this.component;
    }
    
    public final float getValue() {
        return this.value;
    }
    
    public final long getNanos() {
        return this.nanos;
    }
    
    public final String toString() {
        return "Event: component = " + this.component + " | value = " + this.value;
    }
}

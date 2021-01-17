// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

final class LinuxAbsInfo
{
    private int value;
    private int minimum;
    private int maximum;
    private int fuzz;
    private int flat;
    
    public final void set(final int value, final int min, final int max, final int fuzz, final int flat) {
        this.value = value;
        this.minimum = min;
        this.maximum = max;
        this.fuzz = fuzz;
        this.flat = flat;
    }
    
    public final String toString() {
        return "AbsInfo: value = " + this.value + " | min = " + this.minimum + " | max = " + this.maximum + " | fuzz = " + this.fuzz + " | flat = " + this.flat;
    }
    
    public final int getValue() {
        return this.value;
    }
    
    final int getMax() {
        return this.maximum;
    }
    
    final int getMin() {
        return this.minimum;
    }
    
    final int getFlat() {
        return this.flat;
    }
    
    final int getFuzz() {
        return this.fuzz;
    }
}

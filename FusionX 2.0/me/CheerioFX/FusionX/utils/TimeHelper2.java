// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.utils;

public class TimeHelper2
{
    private long prevTime;
    
    public TimeHelper2() {
        this.reset();
    }
    
    public boolean hasPassed(final double milli) {
        return this.getTime() - this.prevTime >= milli;
    }
    
    public long getTime() {
        return System.nanoTime() / 1000000L;
    }
    
    public void reset() {
        this.prevTime = this.getTime();
    }
}

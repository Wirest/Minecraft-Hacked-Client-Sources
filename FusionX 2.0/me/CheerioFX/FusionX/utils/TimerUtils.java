// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.utils;

public final class TimerUtils
{
    private long time;
    private long prevMS;
    
    public TimerUtils() {
        this.time = System.nanoTime() / 1000000L;
        this.prevMS = 0L;
    }
    
    public boolean hasTimeElapsed(final long time, final boolean reset) {
        if (this.time() >= time) {
            if (reset) {
                this.reset();
            }
            return true;
        }
        return false;
    }
    
    public boolean delay(final float milliSec) {
        return this.time() - this.prevMS >= milliSec;
    }
    
    public long time() {
        return System.nanoTime() / 1000000L - this.time;
    }
    
    public void reset() {
        this.time = System.nanoTime() / 1000000L;
    }
    
    public long getDifference() {
        return this.time() - this.prevMS;
    }
}

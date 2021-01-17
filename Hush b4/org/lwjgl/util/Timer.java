// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util;

import org.lwjgl.Sys;

public class Timer
{
    private static long resolution;
    private static final int QUERY_INTERVAL = 50;
    private static int queryCount;
    private static long currentTime;
    private long startTime;
    private long lastTime;
    private boolean paused;
    
    public Timer() {
        this.reset();
        this.resume();
    }
    
    public float getTime() {
        if (!this.paused) {
            this.lastTime = Timer.currentTime - this.startTime;
        }
        return (float)(this.lastTime / (double)Timer.resolution);
    }
    
    public boolean isPaused() {
        return this.paused;
    }
    
    public void pause() {
        this.paused = true;
    }
    
    public void reset() {
        this.set(0.0f);
    }
    
    public void resume() {
        this.paused = false;
        this.startTime = Timer.currentTime - this.lastTime;
    }
    
    public void set(final float newTime) {
        final long newTimeInTicks = (long)(newTime * (double)Timer.resolution);
        this.startTime = Timer.currentTime - newTimeInTicks;
        this.lastTime = newTimeInTicks;
    }
    
    public static void tick() {
        Timer.currentTime = Sys.getTime();
        ++Timer.queryCount;
        if (Timer.queryCount > 50) {
            Timer.queryCount = 0;
            Timer.resolution = Sys.getTimerResolution();
        }
    }
    
    @Override
    public String toString() {
        return "Timer[Time=" + this.getTime() + ", Paused=" + this.paused + "]";
    }
    
    static {
        Timer.resolution = Sys.getTimerResolution();
        tick();
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.utils;

public class TimeHelper
{
    private static long lastMS;
    
    static {
        TimeHelper.lastMS = 0L;
    }
    
    public boolean isDelayComplete(final float f) {
        return System.currentTimeMillis() - TimeHelper.lastMS >= f;
    }
    
    public static long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }
    
    public void setLastMS(long lastMS) {
        lastMS = System.currentTimeMillis();
    }
    
    public int convertToMS(final int perSecond) {
        return 1000 / perSecond;
    }
    
    public static boolean hasReached(final long milliseconds) {
        return getCurrentMS() - TimeHelper.lastMS >= milliseconds;
    }
    
    public static void reset() {
        TimeHelper.lastMS = getCurrentMS();
    }
}

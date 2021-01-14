package com.minimap.animation;

public class Animation
{
    public static long lastTick;
    public static final double animationThing = 16.666666666666668;
    
    public static void tick() {
        Animation.lastTick = System.currentTimeMillis();
    }
    
    public static double animate(double a, final double factor) {
        final double times = (System.currentTimeMillis() - Animation.lastTick) / 16.666666666666668;
        a *= Math.pow(factor, times);
        return a;
    }
    
    static {
        Animation.lastTick = System.currentTimeMillis();
    }
}

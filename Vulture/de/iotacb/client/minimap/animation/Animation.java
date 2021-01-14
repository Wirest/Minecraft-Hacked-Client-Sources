package de.iotacb.client.minimap.animation;

public class Animation
{
    public static long lastTick;
    
    public static void tick() {
        Animation.lastTick = System.currentTimeMillis();
    }
    
    public static double animate(double a, final double factor) {
        final double times = (System.currentTimeMillis() - Animation.lastTick) / 16.666666666666668; // 16.66... Delta time
        a *= Math.pow(factor, times);
        return a;
    }
    
    static {
        Animation.lastTick = System.currentTimeMillis();
    }
}

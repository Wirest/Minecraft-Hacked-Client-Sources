// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.util;

public class FastTrig
{
    private static double reduceSinAngle(double radians) {
        final double orig = radians;
        radians %= 6.283185307179586;
        if (Math.abs(radians) > 3.141592653589793) {
            radians -= 6.283185307179586;
        }
        if (Math.abs(radians) > 1.5707963267948966) {
            radians = 3.141592653589793 - radians;
        }
        return radians;
    }
    
    public static double sin(double radians) {
        radians = reduceSinAngle(radians);
        if (Math.abs(radians) <= 0.7853981633974483) {
            return Math.sin(radians);
        }
        return Math.cos(1.5707963267948966 - radians);
    }
    
    public static double cos(final double radians) {
        return sin(radians + 1.5707963267948966);
    }
}

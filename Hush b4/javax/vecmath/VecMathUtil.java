// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

class VecMathUtil
{
    static int floatToIntBits(final float value) {
        if (value == 0.0f) {
            return 0;
        }
        return Float.floatToIntBits(value);
    }
    
    static long doubleToLongBits(final double value) {
        if (value == 0.0) {
            return 0L;
        }
        return Double.doubleToLongBits(value);
    }
    
    private VecMathUtil() {
    }
}

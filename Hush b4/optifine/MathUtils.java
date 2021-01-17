// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

public class MathUtils
{
    public static int getAverage(final int[] p_getAverage_0_) {
        if (p_getAverage_0_.length <= 0) {
            return 0;
        }
        int i = 0;
        for (int j = 0; j < p_getAverage_0_.length; ++j) {
            final int k = p_getAverage_0_[j];
            i += k;
        }
        final int l = i / p_getAverage_0_.length;
        return l;
    }
}

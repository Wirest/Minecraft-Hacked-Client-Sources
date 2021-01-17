// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

public class IntegerCache
{
    private static final int CACHE_SIZE = 4096;
    private static final Integer[] cache;
    
    static {
        cache = makeCache(4096);
    }
    
    private static Integer[] makeCache(final int p_makeCache_0_) {
        final Integer[] ainteger = new Integer[p_makeCache_0_];
        for (int i = 0; i < p_makeCache_0_; ++i) {
            ainteger[i] = new Integer(i);
        }
        return ainteger;
    }
    
    public static Integer valueOf(final int p_valueOf_0_) {
        return (p_valueOf_0_ >= 0 && p_valueOf_0_ < 4096) ? IntegerCache.cache[p_valueOf_0_] : new Integer(p_valueOf_0_);
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package joptsimple.internal;

public final class Objects
{
    private Objects() {
        throw new UnsupportedOperationException();
    }
    
    public static void ensureNotNull(final Object target) {
        if (target == null) {
            throw new NullPointerException();
        }
    }
}

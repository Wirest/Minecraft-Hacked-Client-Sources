// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.io.InputStream;
import com.ibm.icu.impl.Norm2AllModes;

public abstract class Normalizer2
{
    public static Normalizer2 getNFCInstance() {
        return Norm2AllModes.getNFCInstance().comp;
    }
    
    public static Normalizer2 getNFDInstance() {
        return Norm2AllModes.getNFCInstance().decomp;
    }
    
    public static Normalizer2 getNFKCInstance() {
        return Norm2AllModes.getNFKCInstance().comp;
    }
    
    public static Normalizer2 getNFKDInstance() {
        return Norm2AllModes.getNFKCInstance().decomp;
    }
    
    public static Normalizer2 getNFKCCasefoldInstance() {
        return Norm2AllModes.getNFKC_CFInstance().comp;
    }
    
    public static Normalizer2 getInstance(final InputStream data, final String name, final Mode mode) {
        final Norm2AllModes all2Modes = Norm2AllModes.getInstance(data, name);
        switch (mode) {
            case COMPOSE: {
                return all2Modes.comp;
            }
            case DECOMPOSE: {
                return all2Modes.decomp;
            }
            case FCD: {
                return all2Modes.fcd;
            }
            case COMPOSE_CONTIGUOUS: {
                return all2Modes.fcc;
            }
            default: {
                return null;
            }
        }
    }
    
    public String normalize(final CharSequence src) {
        if (!(src instanceof String)) {
            return this.normalize(src, new StringBuilder(src.length())).toString();
        }
        final int spanLength = this.spanQuickCheckYes(src);
        if (spanLength == src.length()) {
            return (String)src;
        }
        final StringBuilder sb = new StringBuilder(src.length()).append(src, 0, spanLength);
        return this.normalizeSecondAndAppend(sb, src.subSequence(spanLength, src.length())).toString();
    }
    
    public abstract StringBuilder normalize(final CharSequence p0, final StringBuilder p1);
    
    public abstract Appendable normalize(final CharSequence p0, final Appendable p1);
    
    public abstract StringBuilder normalizeSecondAndAppend(final StringBuilder p0, final CharSequence p1);
    
    public abstract StringBuilder append(final StringBuilder p0, final CharSequence p1);
    
    public abstract String getDecomposition(final int p0);
    
    public String getRawDecomposition(final int c) {
        return null;
    }
    
    public int composePair(final int a, final int b) {
        return -1;
    }
    
    public int getCombiningClass(final int c) {
        return 0;
    }
    
    public abstract boolean isNormalized(final CharSequence p0);
    
    public abstract Normalizer.QuickCheckResult quickCheck(final CharSequence p0);
    
    public abstract int spanQuickCheckYes(final CharSequence p0);
    
    public abstract boolean hasBoundaryBefore(final int p0);
    
    public abstract boolean hasBoundaryAfter(final int p0);
    
    public abstract boolean isInert(final int p0);
    
    @Deprecated
    protected Normalizer2() {
    }
    
    public enum Mode
    {
        COMPOSE, 
        DECOMPOSE, 
        FCD, 
        COMPOSE_CONTIGUOUS;
    }
}

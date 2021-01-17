// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

public abstract class UnicodeFilter implements UnicodeMatcher
{
    public abstract boolean contains(final int p0);
    
    public int matches(final Replaceable text, final int[] offset, final int limit, final boolean incremental) {
        final int c;
        if (offset[0] < limit && this.contains(c = text.char32At(offset[0]))) {
            final int n = 0;
            offset[n] += UTF16.getCharCount(c);
            return 2;
        }
        if (offset[0] > limit && this.contains(text.char32At(offset[0]))) {
            final int n2 = 0;
            --offset[n2];
            if (offset[0] >= 0) {
                final int n3 = 0;
                offset[n3] -= UTF16.getCharCount(text.char32At(offset[0])) - 1;
            }
            return 2;
        }
        if (incremental && offset[0] == limit) {
            return 1;
        }
        return 0;
    }
    
    @Deprecated
    protected UnicodeFilter() {
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.escape;

import com.google.common.base.Preconditions;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtCompatible
public abstract class ArrayBasedCharEscaper extends CharEscaper
{
    private final char[][] replacements;
    private final int replacementsLength;
    private final char safeMin;
    private final char safeMax;
    
    protected ArrayBasedCharEscaper(final Map<Character, String> replacementMap, final char safeMin, final char safeMax) {
        this(ArrayBasedEscaperMap.create(replacementMap), safeMin, safeMax);
    }
    
    protected ArrayBasedCharEscaper(final ArrayBasedEscaperMap escaperMap, char safeMin, char safeMax) {
        Preconditions.checkNotNull(escaperMap);
        this.replacements = escaperMap.getReplacementArray();
        this.replacementsLength = this.replacements.length;
        if (safeMax < safeMin) {
            safeMax = '\0';
            safeMin = '\uffff';
        }
        this.safeMin = safeMin;
        this.safeMax = safeMax;
    }
    
    @Override
    public final String escape(final String s) {
        Preconditions.checkNotNull(s);
        for (int i = 0; i < s.length(); ++i) {
            final char c = s.charAt(i);
            if ((c < this.replacementsLength && this.replacements[c] != null) || c > this.safeMax || c < this.safeMin) {
                return this.escapeSlow(s, i);
            }
        }
        return s;
    }
    
    @Override
    protected final char[] escape(final char c) {
        if (c < this.replacementsLength) {
            final char[] chars = this.replacements[c];
            if (chars != null) {
                return chars;
            }
        }
        if (c >= this.safeMin && c <= this.safeMax) {
            return null;
        }
        return this.escapeUnsafe(c);
    }
    
    protected abstract char[] escapeUnsafe(final char p0);
}

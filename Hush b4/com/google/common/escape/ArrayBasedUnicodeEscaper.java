// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.escape;

import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtCompatible
public abstract class ArrayBasedUnicodeEscaper extends UnicodeEscaper
{
    private final char[][] replacements;
    private final int replacementsLength;
    private final int safeMin;
    private final int safeMax;
    private final char safeMinChar;
    private final char safeMaxChar;
    
    protected ArrayBasedUnicodeEscaper(final Map<Character, String> replacementMap, final int safeMin, final int safeMax, @Nullable final String unsafeReplacement) {
        this(ArrayBasedEscaperMap.create(replacementMap), safeMin, safeMax, unsafeReplacement);
    }
    
    protected ArrayBasedUnicodeEscaper(final ArrayBasedEscaperMap escaperMap, int safeMin, int safeMax, @Nullable final String unsafeReplacement) {
        Preconditions.checkNotNull(escaperMap);
        this.replacements = escaperMap.getReplacementArray();
        this.replacementsLength = this.replacements.length;
        if (safeMax < safeMin) {
            safeMax = -1;
            safeMin = Integer.MAX_VALUE;
        }
        this.safeMin = safeMin;
        this.safeMax = safeMax;
        if (safeMin >= 55296) {
            this.safeMinChar = '\uffff';
            this.safeMaxChar = '\0';
        }
        else {
            this.safeMinChar = (char)safeMin;
            this.safeMaxChar = (char)Math.min(safeMax, 55295);
        }
    }
    
    @Override
    public final String escape(final String s) {
        Preconditions.checkNotNull(s);
        for (int i = 0; i < s.length(); ++i) {
            final char c = s.charAt(i);
            if ((c < this.replacementsLength && this.replacements[c] != null) || c > this.safeMaxChar || c < this.safeMinChar) {
                return this.escapeSlow(s, i);
            }
        }
        return s;
    }
    
    @Override
    protected final int nextEscapeIndex(final CharSequence csq, int index, final int end) {
        while (index < end) {
            final char c = csq.charAt(index);
            if ((c < this.replacementsLength && this.replacements[c] != null) || c > this.safeMaxChar) {
                break;
            }
            if (c < this.safeMinChar) {
                break;
            }
            ++index;
        }
        return index;
    }
    
    @Override
    protected final char[] escape(final int cp) {
        if (cp < this.replacementsLength) {
            final char[] chars = this.replacements[cp];
            if (chars != null) {
                return chars;
            }
        }
        if (cp >= this.safeMin && cp <= this.safeMax) {
            return null;
        }
        return this.escapeUnsafe(cp);
    }
    
    protected abstract char[] escapeUnsafe(final int p0);
}

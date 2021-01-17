// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.escape;

import java.util.Iterator;
import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtCompatible
public final class CharEscaperBuilder
{
    private final Map<Character, String> map;
    private int max;
    
    public CharEscaperBuilder() {
        this.max = -1;
        this.map = new HashMap<Character, String>();
    }
    
    public CharEscaperBuilder addEscape(final char c, final String r) {
        this.map.put(c, Preconditions.checkNotNull(r));
        if (c > this.max) {
            this.max = c;
        }
        return this;
    }
    
    public CharEscaperBuilder addEscapes(final char[] cs, final String r) {
        Preconditions.checkNotNull(r);
        for (final char c : cs) {
            this.addEscape(c, r);
        }
        return this;
    }
    
    public char[][] toArray() {
        final char[][] result = new char[this.max + 1][];
        for (final Map.Entry<Character, String> entry : this.map.entrySet()) {
            result[(char)entry.getKey()] = entry.getValue().toCharArray();
        }
        return result;
    }
    
    public Escaper toEscaper() {
        return new CharArrayDecorator(this.toArray());
    }
    
    private static class CharArrayDecorator extends CharEscaper
    {
        private final char[][] replacements;
        private final int replaceLength;
        
        CharArrayDecorator(final char[][] replacements) {
            this.replacements = replacements;
            this.replaceLength = replacements.length;
        }
        
        @Override
        public String escape(final String s) {
            for (int slen = s.length(), index = 0; index < slen; ++index) {
                final char c = s.charAt(index);
                if (c < this.replacements.length && this.replacements[c] != null) {
                    return this.escapeSlow(s, index);
                }
            }
            return s;
        }
        
        @Override
        protected char[] escape(final char c) {
            return (char[])((c < this.replaceLength) ? this.replacements[c] : null);
        }
    }
}

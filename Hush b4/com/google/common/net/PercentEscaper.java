// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.net;

import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;
import com.google.common.escape.UnicodeEscaper;

@Beta
@GwtCompatible
public final class PercentEscaper extends UnicodeEscaper
{
    private static final char[] PLUS_SIGN;
    private static final char[] UPPER_HEX_DIGITS;
    private final boolean plusForSpace;
    private final boolean[] safeOctets;
    
    public PercentEscaper(String safeChars, final boolean plusForSpace) {
        Preconditions.checkNotNull(safeChars);
        if (safeChars.matches(".*[0-9A-Za-z].*")) {
            throw new IllegalArgumentException("Alphanumeric characters are always 'safe' and should not be explicitly specified");
        }
        safeChars += "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        if (plusForSpace && safeChars.contains(" ")) {
            throw new IllegalArgumentException("plusForSpace cannot be specified when space is a 'safe' character");
        }
        this.plusForSpace = plusForSpace;
        this.safeOctets = createSafeOctets(safeChars);
    }
    
    private static boolean[] createSafeOctets(final String safeChars) {
        int maxChar = -1;
        final char[] arr$;
        final char[] safeCharArray = arr$ = safeChars.toCharArray();
        for (final char c : arr$) {
            maxChar = Math.max(c, maxChar);
        }
        final boolean[] octets = new boolean[maxChar + 1];
        for (final char c2 : safeCharArray) {
            octets[c2] = true;
        }
        return octets;
    }
    
    @Override
    protected int nextEscapeIndex(final CharSequence csq, int index, final int end) {
        Preconditions.checkNotNull(csq);
        while (index < end) {
            final char c = csq.charAt(index);
            if (c >= this.safeOctets.length) {
                break;
            }
            if (!this.safeOctets[c]) {
                break;
            }
            ++index;
        }
        return index;
    }
    
    @Override
    public String escape(final String s) {
        Preconditions.checkNotNull(s);
        for (int slen = s.length(), index = 0; index < slen; ++index) {
            final char c = s.charAt(index);
            if (c >= this.safeOctets.length || !this.safeOctets[c]) {
                return this.escapeSlow(s, index);
            }
        }
        return s;
    }
    
    @Override
    protected char[] escape(int cp) {
        if (cp < this.safeOctets.length && this.safeOctets[cp]) {
            return null;
        }
        if (cp == 32 && this.plusForSpace) {
            return PercentEscaper.PLUS_SIGN;
        }
        if (cp <= 127) {
            final char[] dest = { '%', PercentEscaper.UPPER_HEX_DIGITS[cp >>> 4], PercentEscaper.UPPER_HEX_DIGITS[cp & 0xF] };
            return dest;
        }
        if (cp <= 2047) {
            final char[] dest = { '%', '\0', '\0', '%', '\0', PercentEscaper.UPPER_HEX_DIGITS[cp & 0xF] };
            cp >>>= 4;
            dest[4] = PercentEscaper.UPPER_HEX_DIGITS[0x8 | (cp & 0x3)];
            cp >>>= 2;
            dest[2] = PercentEscaper.UPPER_HEX_DIGITS[cp & 0xF];
            cp >>>= 4;
            dest[1] = PercentEscaper.UPPER_HEX_DIGITS[0xC | cp];
            return dest;
        }
        if (cp <= 65535) {
            final char[] dest = { '%', 'E', '\0', '%', '\0', '\0', '%', '\0', PercentEscaper.UPPER_HEX_DIGITS[cp & 0xF] };
            cp >>>= 4;
            dest[7] = PercentEscaper.UPPER_HEX_DIGITS[0x8 | (cp & 0x3)];
            cp >>>= 2;
            dest[5] = PercentEscaper.UPPER_HEX_DIGITS[cp & 0xF];
            cp >>>= 4;
            dest[4] = PercentEscaper.UPPER_HEX_DIGITS[0x8 | (cp & 0x3)];
            cp >>>= 2;
            dest[2] = PercentEscaper.UPPER_HEX_DIGITS[cp];
            return dest;
        }
        if (cp <= 1114111) {
            final char[] dest = { '%', 'F', '\0', '%', '\0', '\0', '%', '\0', '\0', '%', '\0', PercentEscaper.UPPER_HEX_DIGITS[cp & 0xF] };
            cp >>>= 4;
            dest[10] = PercentEscaper.UPPER_HEX_DIGITS[0x8 | (cp & 0x3)];
            cp >>>= 2;
            dest[8] = PercentEscaper.UPPER_HEX_DIGITS[cp & 0xF];
            cp >>>= 4;
            dest[7] = PercentEscaper.UPPER_HEX_DIGITS[0x8 | (cp & 0x3)];
            cp >>>= 2;
            dest[5] = PercentEscaper.UPPER_HEX_DIGITS[cp & 0xF];
            cp >>>= 4;
            dest[4] = PercentEscaper.UPPER_HEX_DIGITS[0x8 | (cp & 0x3)];
            cp >>>= 2;
            dest[2] = PercentEscaper.UPPER_HEX_DIGITS[cp & 0x7];
            return dest;
        }
        throw new IllegalArgumentException("Invalid unicode character value " + cp);
    }
    
    static {
        PLUS_SIGN = new char[] { '+' };
        UPPER_HEX_DIGITS = "0123456789ABCDEF".toCharArray();
    }
}

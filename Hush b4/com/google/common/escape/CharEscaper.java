// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.escape;

import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtCompatible
public abstract class CharEscaper extends Escaper
{
    private static final int DEST_PAD_MULTIPLIER = 2;
    
    protected CharEscaper() {
    }
    
    @Override
    public String escape(final String string) {
        Preconditions.checkNotNull(string);
        for (int length = string.length(), index = 0; index < length; ++index) {
            if (this.escape(string.charAt(index)) != null) {
                return this.escapeSlow(string, index);
            }
        }
        return string;
    }
    
    protected final String escapeSlow(final String s, int index) {
        final int slen = s.length();
        char[] dest = Platform.charBufferFromThreadLocal();
        int destSize = dest.length;
        int destIndex = 0;
        int lastEscape = 0;
        while (index < slen) {
            final char[] r = this.escape(s.charAt(index));
            if (r != null) {
                final int rlen = r.length;
                final int charsSkipped = index - lastEscape;
                final int sizeNeeded = destIndex + charsSkipped + rlen;
                if (destSize < sizeNeeded) {
                    destSize = sizeNeeded + 2 * (slen - index);
                    dest = growBuffer(dest, destIndex, destSize);
                }
                if (charsSkipped > 0) {
                    s.getChars(lastEscape, index, dest, destIndex);
                    destIndex += charsSkipped;
                }
                if (rlen > 0) {
                    System.arraycopy(r, 0, dest, destIndex, rlen);
                    destIndex += rlen;
                }
                lastEscape = index + 1;
            }
            ++index;
        }
        final int charsLeft = slen - lastEscape;
        if (charsLeft > 0) {
            final int sizeNeeded2 = destIndex + charsLeft;
            if (destSize < sizeNeeded2) {
                dest = growBuffer(dest, destIndex, sizeNeeded2);
            }
            s.getChars(lastEscape, slen, dest, destIndex);
            destIndex = sizeNeeded2;
        }
        return new String(dest, 0, destIndex);
    }
    
    protected abstract char[] escape(final char p0);
    
    private static char[] growBuffer(final char[] dest, final int index, final int size) {
        final char[] copy = new char[size];
        if (index > 0) {
            System.arraycopy(dest, 0, copy, 0, index);
        }
        return copy;
    }
}

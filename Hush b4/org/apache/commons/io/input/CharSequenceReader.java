// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.input;

import java.io.Serializable;
import java.io.Reader;

public class CharSequenceReader extends Reader implements Serializable
{
    private final CharSequence charSequence;
    private int idx;
    private int mark;
    
    public CharSequenceReader(final CharSequence charSequence) {
        this.charSequence = ((charSequence != null) ? charSequence : "");
    }
    
    @Override
    public void close() {
        this.idx = 0;
        this.mark = 0;
    }
    
    @Override
    public void mark(final int readAheadLimit) {
        this.mark = this.idx;
    }
    
    @Override
    public boolean markSupported() {
        return true;
    }
    
    @Override
    public int read() {
        if (this.idx >= this.charSequence.length()) {
            return -1;
        }
        return this.charSequence.charAt(this.idx++);
    }
    
    @Override
    public int read(final char[] array, final int offset, final int length) {
        if (this.idx >= this.charSequence.length()) {
            return -1;
        }
        if (array == null) {
            throw new NullPointerException("Character array is missing");
        }
        if (length < 0 || offset < 0 || offset + length > array.length) {
            throw new IndexOutOfBoundsException("Array Size=" + array.length + ", offset=" + offset + ", length=" + length);
        }
        int count = 0;
        for (int i = 0; i < length; ++i) {
            final int c = this.read();
            if (c == -1) {
                return count;
            }
            array[offset + i] = (char)c;
            ++count;
        }
        return count;
    }
    
    @Override
    public void reset() {
        this.idx = this.mark;
    }
    
    @Override
    public long skip(final long n) {
        if (n < 0L) {
            throw new IllegalArgumentException("Number of characters to skip is less than zero: " + n);
        }
        if (this.idx >= this.charSequence.length()) {
            return -1L;
        }
        final int dest = (int)Math.min(this.charSequence.length(), this.idx + n);
        final int count = dest - this.idx;
        this.idx = dest;
        return count;
    }
    
    @Override
    public String toString() {
        return this.charSequence.toString();
    }
}

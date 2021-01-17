// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal;

import java.io.IOException;
import java.util.Arrays;

public final class AppendableCharSequence implements CharSequence, Appendable
{
    private char[] chars;
    private int pos;
    
    public AppendableCharSequence(final int length) {
        if (length < 1) {
            throw new IllegalArgumentException("length: " + length + " (length: >= 1)");
        }
        this.chars = new char[length];
    }
    
    private AppendableCharSequence(final char[] chars) {
        this.chars = chars;
        this.pos = chars.length;
    }
    
    @Override
    public int length() {
        return this.pos;
    }
    
    @Override
    public char charAt(final int index) {
        if (index > this.pos) {
            throw new IndexOutOfBoundsException();
        }
        return this.chars[index];
    }
    
    @Override
    public AppendableCharSequence subSequence(final int start, final int end) {
        return new AppendableCharSequence(Arrays.copyOfRange(this.chars, start, end));
    }
    
    @Override
    public AppendableCharSequence append(final char c) {
        if (this.pos == this.chars.length) {
            final char[] old = this.chars;
            final int len = old.length << 1;
            if (len < 0) {
                throw new IllegalStateException();
            }
            System.arraycopy(old, 0, this.chars = new char[len], 0, old.length);
        }
        this.chars[this.pos++] = c;
        return this;
    }
    
    @Override
    public AppendableCharSequence append(final CharSequence csq) {
        return this.append(csq, 0, csq.length());
    }
    
    @Override
    public AppendableCharSequence append(final CharSequence csq, final int start, final int end) {
        if (csq.length() < end) {
            throw new IndexOutOfBoundsException();
        }
        final int length = end - start;
        if (length > this.chars.length - this.pos) {
            this.chars = expand(this.chars, this.pos + length, this.pos);
        }
        if (csq instanceof AppendableCharSequence) {
            final AppendableCharSequence seq = (AppendableCharSequence)csq;
            final char[] src = seq.chars;
            System.arraycopy(src, start, this.chars, this.pos, length);
            this.pos += length;
            return this;
        }
        for (int i = start; i < end; ++i) {
            this.chars[this.pos++] = csq.charAt(i);
        }
        return this;
    }
    
    public void reset() {
        this.pos = 0;
    }
    
    @Override
    public String toString() {
        return new String(this.chars, 0, this.pos);
    }
    
    public String substring(final int start, final int end) {
        final int length = end - start;
        if (start > this.pos || length > this.pos) {
            throw new IndexOutOfBoundsException();
        }
        return new String(this.chars, start, length);
    }
    
    private static char[] expand(final char[] array, final int neededSpace, final int size) {
        int newCapacity = array.length;
        do {
            newCapacity <<= 1;
            if (newCapacity < 0) {
                throw new IllegalStateException();
            }
        } while (neededSpace > newCapacity);
        final char[] newArray = new char[newCapacity];
        System.arraycopy(array, 0, newArray, 0, size);
        return newArray;
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import com.ibm.icu.impl.Utility;
import java.nio.ByteBuffer;

public class ByteArrayWrapper implements Comparable<ByteArrayWrapper>
{
    public byte[] bytes;
    public int size;
    
    public ByteArrayWrapper() {
    }
    
    public ByteArrayWrapper(final byte[] bytesToAdopt, final int size) {
        if ((bytesToAdopt == null && size != 0) || size < 0 || size > bytesToAdopt.length) {
            throw new IndexOutOfBoundsException("illegal size: " + size);
        }
        this.bytes = bytesToAdopt;
        this.size = size;
    }
    
    public ByteArrayWrapper(final ByteBuffer source) {
        this.size = source.limit();
        source.get(this.bytes = new byte[this.size], 0, this.size);
    }
    
    public ByteArrayWrapper ensureCapacity(final int capacity) {
        if (this.bytes == null || this.bytes.length < capacity) {
            final byte[] newbytes = new byte[capacity];
            copyBytes(this.bytes, 0, newbytes, 0, this.size);
            this.bytes = newbytes;
        }
        return this;
    }
    
    public final ByteArrayWrapper set(final byte[] src, final int start, final int limit) {
        this.size = 0;
        this.append(src, start, limit);
        return this;
    }
    
    public final ByteArrayWrapper append(final byte[] src, final int start, final int limit) {
        final int len = limit - start;
        this.ensureCapacity(this.size + len);
        copyBytes(src, start, this.bytes, this.size, len);
        this.size += len;
        return this;
    }
    
    public final byte[] releaseBytes() {
        final byte[] result = this.bytes;
        this.bytes = null;
        this.size = 0;
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < this.size; ++i) {
            if (i != 0) {
                result.append(" ");
            }
            result.append(Utility.hex(this.bytes[i] & 0xFF, 2));
        }
        return result.toString();
    }
    
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        try {
            final ByteArrayWrapper that = (ByteArrayWrapper)other;
            if (this.size != that.size) {
                return false;
            }
            for (int i = 0; i < this.size; ++i) {
                if (this.bytes[i] != that.bytes[i]) {
                    return false;
                }
            }
            return true;
        }
        catch (ClassCastException e) {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        int result = this.bytes.length;
        for (int i = 0; i < this.size; ++i) {
            result = 37 * result + this.bytes[i];
        }
        return result;
    }
    
    public int compareTo(final ByteArrayWrapper other) {
        if (this == other) {
            return 0;
        }
        for (int minSize = (this.size < other.size) ? this.size : other.size, i = 0; i < minSize; ++i) {
            if (this.bytes[i] != other.bytes[i]) {
                return (this.bytes[i] & 0xFF) - (other.bytes[i] & 0xFF);
            }
        }
        return this.size - other.size;
    }
    
    private static final void copyBytes(final byte[] src, final int srcoff, final byte[] tgt, final int tgtoff, int length) {
        if (length < 64) {
            int i = srcoff;
            int n = tgtoff;
            while (--length >= 0) {
                tgt[n] = src[i];
                ++i;
                ++n;
            }
        }
        else {
            System.arraycopy(src, srcoff, tgt, tgtoff, length);
        }
    }
}

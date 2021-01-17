// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.util;

import org.apache.http.annotation.NotThreadSafe;
import java.io.Serializable;

@NotThreadSafe
public final class ByteArrayBuffer implements Serializable
{
    private static final long serialVersionUID = 4359112959524048036L;
    private byte[] buffer;
    private int len;
    
    public ByteArrayBuffer(final int capacity) {
        Args.notNegative(capacity, "Buffer capacity");
        this.buffer = new byte[capacity];
    }
    
    private void expand(final int newlen) {
        final byte[] newbuffer = new byte[Math.max(this.buffer.length << 1, newlen)];
        System.arraycopy(this.buffer, 0, newbuffer, 0, this.len);
        this.buffer = newbuffer;
    }
    
    public void append(final byte[] b, final int off, final int len) {
        if (b == null) {
            return;
        }
        if (off < 0 || off > b.length || len < 0 || off + len < 0 || off + len > b.length) {
            throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
        }
        if (len == 0) {
            return;
        }
        final int newlen = this.len + len;
        if (newlen > this.buffer.length) {
            this.expand(newlen);
        }
        System.arraycopy(b, off, this.buffer, this.len, len);
        this.len = newlen;
    }
    
    public void append(final int b) {
        final int newlen = this.len + 1;
        if (newlen > this.buffer.length) {
            this.expand(newlen);
        }
        this.buffer[this.len] = (byte)b;
        this.len = newlen;
    }
    
    public void append(final char[] b, final int off, final int len) {
        if (b == null) {
            return;
        }
        if (off < 0 || off > b.length || len < 0 || off + len < 0 || off + len > b.length) {
            throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
        }
        if (len == 0) {
            return;
        }
        final int oldlen = this.len;
        final int newlen = oldlen + len;
        if (newlen > this.buffer.length) {
            this.expand(newlen);
        }
        int i1 = off;
        for (int i2 = oldlen; i2 < newlen; ++i2) {
            this.buffer[i2] = (byte)b[i1];
            ++i1;
        }
        this.len = newlen;
    }
    
    public void append(final CharArrayBuffer b, final int off, final int len) {
        if (b == null) {
            return;
        }
        this.append(b.buffer(), off, len);
    }
    
    public void clear() {
        this.len = 0;
    }
    
    public byte[] toByteArray() {
        final byte[] b = new byte[this.len];
        if (this.len > 0) {
            System.arraycopy(this.buffer, 0, b, 0, this.len);
        }
        return b;
    }
    
    public int byteAt(final int i) {
        return this.buffer[i];
    }
    
    public int capacity() {
        return this.buffer.length;
    }
    
    public int length() {
        return this.len;
    }
    
    public void ensureCapacity(final int required) {
        if (required <= 0) {
            return;
        }
        final int available = this.buffer.length - this.len;
        if (required > available) {
            this.expand(this.len + required);
        }
    }
    
    public byte[] buffer() {
        return this.buffer;
    }
    
    public void setLength(final int len) {
        if (len < 0 || len > this.buffer.length) {
            throw new IndexOutOfBoundsException("len: " + len + " < 0 or > buffer len: " + this.buffer.length);
        }
        this.len = len;
    }
    
    public boolean isEmpty() {
        return this.len == 0;
    }
    
    public boolean isFull() {
        return this.len == this.buffer.length;
    }
    
    public int indexOf(final byte b, final int from, final int to) {
        int beginIndex = from;
        if (beginIndex < 0) {
            beginIndex = 0;
        }
        int endIndex = to;
        if (endIndex > this.len) {
            endIndex = this.len;
        }
        if (beginIndex > endIndex) {
            return -1;
        }
        for (int i = beginIndex; i < endIndex; ++i) {
            if (this.buffer[i] == b) {
                return i;
            }
        }
        return -1;
    }
    
    public int indexOf(final byte b) {
        return this.indexOf(b, 0, this.len);
    }
}

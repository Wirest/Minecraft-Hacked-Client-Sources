// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.util;

import org.apache.http.protocol.HTTP;
import org.apache.http.annotation.NotThreadSafe;
import java.io.Serializable;

@NotThreadSafe
public final class CharArrayBuffer implements Serializable
{
    private static final long serialVersionUID = -6208952725094867135L;
    private char[] buffer;
    private int len;
    
    public CharArrayBuffer(final int capacity) {
        Args.notNegative(capacity, "Buffer capacity");
        this.buffer = new char[capacity];
    }
    
    private void expand(final int newlen) {
        final char[] newbuffer = new char[Math.max(this.buffer.length << 1, newlen)];
        System.arraycopy(this.buffer, 0, newbuffer, 0, this.len);
        this.buffer = newbuffer;
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
        final int newlen = this.len + len;
        if (newlen > this.buffer.length) {
            this.expand(newlen);
        }
        System.arraycopy(b, off, this.buffer, this.len, len);
        this.len = newlen;
    }
    
    public void append(final String str) {
        final String s = (str != null) ? str : "null";
        final int strlen = s.length();
        final int newlen = this.len + strlen;
        if (newlen > this.buffer.length) {
            this.expand(newlen);
        }
        s.getChars(0, strlen, this.buffer, this.len);
        this.len = newlen;
    }
    
    public void append(final CharArrayBuffer b, final int off, final int len) {
        if (b == null) {
            return;
        }
        this.append(b.buffer, off, len);
    }
    
    public void append(final CharArrayBuffer b) {
        if (b == null) {
            return;
        }
        this.append(b.buffer, 0, b.len);
    }
    
    public void append(final char ch) {
        final int newlen = this.len + 1;
        if (newlen > this.buffer.length) {
            this.expand(newlen);
        }
        this.buffer[this.len] = ch;
        this.len = newlen;
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
        final int oldlen = this.len;
        final int newlen = oldlen + len;
        if (newlen > this.buffer.length) {
            this.expand(newlen);
        }
        int i1 = off;
        for (int i2 = oldlen; i2 < newlen; ++i2) {
            this.buffer[i2] = (char)(b[i1] & 0xFF);
            ++i1;
        }
        this.len = newlen;
    }
    
    public void append(final ByteArrayBuffer b, final int off, final int len) {
        if (b == null) {
            return;
        }
        this.append(b.buffer(), off, len);
    }
    
    public void append(final Object obj) {
        this.append(String.valueOf(obj));
    }
    
    public void clear() {
        this.len = 0;
    }
    
    public char[] toCharArray() {
        final char[] b = new char[this.len];
        if (this.len > 0) {
            System.arraycopy(this.buffer, 0, b, 0, this.len);
        }
        return b;
    }
    
    public char charAt(final int i) {
        return this.buffer[i];
    }
    
    public char[] buffer() {
        return this.buffer;
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
    
    public int indexOf(final int ch, final int from, final int to) {
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
            if (this.buffer[i] == ch) {
                return i;
            }
        }
        return -1;
    }
    
    public int indexOf(final int ch) {
        return this.indexOf(ch, 0, this.len);
    }
    
    public String substring(final int beginIndex, final int endIndex) {
        return new String(this.buffer, beginIndex, endIndex - beginIndex);
    }
    
    public String substringTrimmed(final int from, final int to) {
        int beginIndex = from;
        int endIndex = to;
        if (beginIndex < 0) {
            throw new IndexOutOfBoundsException("Negative beginIndex: " + beginIndex);
        }
        if (endIndex > this.len) {
            throw new IndexOutOfBoundsException("endIndex: " + endIndex + " > length: " + this.len);
        }
        if (beginIndex > endIndex) {
            throw new IndexOutOfBoundsException("beginIndex: " + beginIndex + " > endIndex: " + endIndex);
        }
        while (beginIndex < endIndex && HTTP.isWhitespace(this.buffer[beginIndex])) {
            ++beginIndex;
        }
        while (endIndex > beginIndex && HTTP.isWhitespace(this.buffer[endIndex - 1])) {
            --endIndex;
        }
        return new String(this.buffer, beginIndex, endIndex - beginIndex);
    }
    
    @Override
    public String toString() {
        return new String(this.buffer, 0, this.len);
    }
}

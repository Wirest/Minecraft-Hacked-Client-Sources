// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

final class HttpHeaderEntity implements CharSequence
{
    private final String name;
    private final int hash;
    private final byte[] bytes;
    private final int separatorLen;
    
    public HttpHeaderEntity(final String name) {
        this(name, null);
    }
    
    public HttpHeaderEntity(final String name, final byte[] separator) {
        this.name = name;
        this.hash = HttpHeaders.hash(name);
        final byte[] nameBytes = name.getBytes(CharsetUtil.US_ASCII);
        if (separator == null) {
            this.bytes = nameBytes;
            this.separatorLen = 0;
        }
        else {
            this.separatorLen = separator.length;
            System.arraycopy(nameBytes, 0, this.bytes = new byte[nameBytes.length + separator.length], 0, nameBytes.length);
            System.arraycopy(separator, 0, this.bytes, nameBytes.length, separator.length);
        }
    }
    
    int hash() {
        return this.hash;
    }
    
    @Override
    public int length() {
        return this.bytes.length - this.separatorLen;
    }
    
    @Override
    public char charAt(final int index) {
        if (this.bytes.length - this.separatorLen <= index) {
            throw new IndexOutOfBoundsException();
        }
        return (char)this.bytes[index];
    }
    
    @Override
    public CharSequence subSequence(final int start, final int end) {
        return new HttpHeaderEntity(this.name.substring(start, end));
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    boolean encode(final ByteBuf buf) {
        buf.writeBytes(this.bytes);
        return this.separatorLen > 0;
    }
}

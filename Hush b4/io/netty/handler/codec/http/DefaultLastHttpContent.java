// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import java.util.Iterator;
import java.util.Map;
import io.netty.util.internal.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class DefaultLastHttpContent extends DefaultHttpContent implements LastHttpContent
{
    private final HttpHeaders trailingHeaders;
    private final boolean validateHeaders;
    
    public DefaultLastHttpContent() {
        this(Unpooled.buffer(0));
    }
    
    public DefaultLastHttpContent(final ByteBuf content) {
        this(content, true);
    }
    
    public DefaultLastHttpContent(final ByteBuf content, final boolean validateHeaders) {
        super(content);
        this.trailingHeaders = new TrailingHeaders(validateHeaders);
        this.validateHeaders = validateHeaders;
    }
    
    @Override
    public LastHttpContent copy() {
        final DefaultLastHttpContent copy = new DefaultLastHttpContent(this.content().copy(), this.validateHeaders);
        copy.trailingHeaders().set(this.trailingHeaders());
        return copy;
    }
    
    @Override
    public LastHttpContent duplicate() {
        final DefaultLastHttpContent copy = new DefaultLastHttpContent(this.content().duplicate(), this.validateHeaders);
        copy.trailingHeaders().set(this.trailingHeaders());
        return copy;
    }
    
    @Override
    public LastHttpContent retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    @Override
    public LastHttpContent retain() {
        super.retain();
        return this;
    }
    
    @Override
    public HttpHeaders trailingHeaders() {
        return this.trailingHeaders;
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder(super.toString());
        buf.append(StringUtil.NEWLINE);
        this.appendHeaders(buf);
        buf.setLength(buf.length() - StringUtil.NEWLINE.length());
        return buf.toString();
    }
    
    private void appendHeaders(final StringBuilder buf) {
        for (final Map.Entry<String, String> e : this.trailingHeaders()) {
            buf.append(e.getKey());
            buf.append(": ");
            buf.append(e.getValue());
            buf.append(StringUtil.NEWLINE);
        }
    }
    
    private static final class TrailingHeaders extends DefaultHttpHeaders
    {
        TrailingHeaders(final boolean validate) {
            super(validate);
        }
        
        @Override
        void validateHeaderName0(final CharSequence name) {
            super.validateHeaderName0(name);
            if (HttpHeaders.equalsIgnoreCase("Content-Length", name) || HttpHeaders.equalsIgnoreCase("Transfer-Encoding", name) || HttpHeaders.equalsIgnoreCase("Trailer", name)) {
                throw new IllegalArgumentException("prohibited trailing header: " + (Object)name);
            }
        }
    }
}

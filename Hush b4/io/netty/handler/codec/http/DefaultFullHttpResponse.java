// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;

public class DefaultFullHttpResponse extends DefaultHttpResponse implements FullHttpResponse
{
    private final ByteBuf content;
    private final HttpHeaders trailingHeaders;
    private final boolean validateHeaders;
    
    public DefaultFullHttpResponse(final HttpVersion version, final HttpResponseStatus status) {
        this(version, status, Unpooled.buffer(0));
    }
    
    public DefaultFullHttpResponse(final HttpVersion version, final HttpResponseStatus status, final ByteBuf content) {
        this(version, status, content, true);
    }
    
    public DefaultFullHttpResponse(final HttpVersion version, final HttpResponseStatus status, final ByteBuf content, final boolean validateHeaders) {
        super(version, status, validateHeaders);
        if (content == null) {
            throw new NullPointerException("content");
        }
        this.content = content;
        this.trailingHeaders = new DefaultHttpHeaders(validateHeaders);
        this.validateHeaders = validateHeaders;
    }
    
    @Override
    public HttpHeaders trailingHeaders() {
        return this.trailingHeaders;
    }
    
    @Override
    public ByteBuf content() {
        return this.content;
    }
    
    @Override
    public int refCnt() {
        return this.content.refCnt();
    }
    
    @Override
    public FullHttpResponse retain() {
        this.content.retain();
        return this;
    }
    
    @Override
    public FullHttpResponse retain(final int increment) {
        this.content.retain(increment);
        return this;
    }
    
    @Override
    public boolean release() {
        return this.content.release();
    }
    
    @Override
    public boolean release(final int decrement) {
        return this.content.release(decrement);
    }
    
    @Override
    public FullHttpResponse setProtocolVersion(final HttpVersion version) {
        super.setProtocolVersion(version);
        return this;
    }
    
    @Override
    public FullHttpResponse setStatus(final HttpResponseStatus status) {
        super.setStatus(status);
        return this;
    }
    
    @Override
    public FullHttpResponse copy() {
        final DefaultFullHttpResponse copy = new DefaultFullHttpResponse(this.getProtocolVersion(), this.getStatus(), this.content().copy(), this.validateHeaders);
        copy.headers().set(this.headers());
        copy.trailingHeaders().set(this.trailingHeaders());
        return copy;
    }
    
    @Override
    public FullHttpResponse duplicate() {
        final DefaultFullHttpResponse duplicate = new DefaultFullHttpResponse(this.getProtocolVersion(), this.getStatus(), this.content().duplicate(), this.validateHeaders);
        duplicate.headers().set(this.headers());
        duplicate.trailingHeaders().set(this.trailingHeaders());
        return duplicate;
    }
}

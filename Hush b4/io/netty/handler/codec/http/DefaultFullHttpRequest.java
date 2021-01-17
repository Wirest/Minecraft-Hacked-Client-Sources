// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;

public class DefaultFullHttpRequest extends DefaultHttpRequest implements FullHttpRequest
{
    private final ByteBuf content;
    private final HttpHeaders trailingHeader;
    private final boolean validateHeaders;
    
    public DefaultFullHttpRequest(final HttpVersion httpVersion, final HttpMethod method, final String uri) {
        this(httpVersion, method, uri, Unpooled.buffer(0));
    }
    
    public DefaultFullHttpRequest(final HttpVersion httpVersion, final HttpMethod method, final String uri, final ByteBuf content) {
        this(httpVersion, method, uri, content, true);
    }
    
    public DefaultFullHttpRequest(final HttpVersion httpVersion, final HttpMethod method, final String uri, final ByteBuf content, final boolean validateHeaders) {
        super(httpVersion, method, uri, validateHeaders);
        if (content == null) {
            throw new NullPointerException("content");
        }
        this.content = content;
        this.trailingHeader = new DefaultHttpHeaders(validateHeaders);
        this.validateHeaders = validateHeaders;
    }
    
    @Override
    public HttpHeaders trailingHeaders() {
        return this.trailingHeader;
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
    public FullHttpRequest retain() {
        this.content.retain();
        return this;
    }
    
    @Override
    public FullHttpRequest retain(final int increment) {
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
    public FullHttpRequest setProtocolVersion(final HttpVersion version) {
        super.setProtocolVersion(version);
        return this;
    }
    
    @Override
    public FullHttpRequest setMethod(final HttpMethod method) {
        super.setMethod(method);
        return this;
    }
    
    @Override
    public FullHttpRequest setUri(final String uri) {
        super.setUri(uri);
        return this;
    }
    
    @Override
    public FullHttpRequest copy() {
        final DefaultFullHttpRequest copy = new DefaultFullHttpRequest(this.getProtocolVersion(), this.getMethod(), this.getUri(), this.content().copy(), this.validateHeaders);
        copy.headers().set(this.headers());
        copy.trailingHeaders().set(this.trailingHeaders());
        return copy;
    }
    
    @Override
    public FullHttpRequest duplicate() {
        final DefaultFullHttpRequest duplicate = new DefaultFullHttpRequest(this.getProtocolVersion(), this.getMethod(), this.getUri(), this.content().duplicate(), this.validateHeaders);
        duplicate.headers().set(this.headers());
        duplicate.trailingHeaders().set(this.trailingHeaders());
        return duplicate;
    }
}

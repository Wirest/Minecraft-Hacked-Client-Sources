// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderResult;

final class ComposedLastHttpContent implements LastHttpContent
{
    private final HttpHeaders trailingHeaders;
    private DecoderResult result;
    
    ComposedLastHttpContent(final HttpHeaders trailingHeaders) {
        this.trailingHeaders = trailingHeaders;
    }
    
    @Override
    public HttpHeaders trailingHeaders() {
        return this.trailingHeaders;
    }
    
    @Override
    public LastHttpContent copy() {
        final LastHttpContent content = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER);
        content.trailingHeaders().set(this.trailingHeaders());
        return content;
    }
    
    @Override
    public LastHttpContent retain(final int increment) {
        return this;
    }
    
    @Override
    public LastHttpContent retain() {
        return this;
    }
    
    @Override
    public HttpContent duplicate() {
        return this.copy();
    }
    
    @Override
    public ByteBuf content() {
        return Unpooled.EMPTY_BUFFER;
    }
    
    @Override
    public DecoderResult getDecoderResult() {
        return this.result;
    }
    
    @Override
    public void setDecoderResult(final DecoderResult result) {
        this.result = result;
    }
    
    @Override
    public int refCnt() {
        return 1;
    }
    
    @Override
    public boolean release() {
        return false;
    }
    
    @Override
    public boolean release(final int decrement) {
        return false;
    }
}

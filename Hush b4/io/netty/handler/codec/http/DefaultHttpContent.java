// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.util.internal.StringUtil;
import io.netty.buffer.ByteBuf;

public class DefaultHttpContent extends DefaultHttpObject implements HttpContent
{
    private final ByteBuf content;
    
    public DefaultHttpContent(final ByteBuf content) {
        if (content == null) {
            throw new NullPointerException("content");
        }
        this.content = content;
    }
    
    @Override
    public ByteBuf content() {
        return this.content;
    }
    
    @Override
    public HttpContent copy() {
        return new DefaultHttpContent(this.content.copy());
    }
    
    @Override
    public HttpContent duplicate() {
        return new DefaultHttpContent(this.content.duplicate());
    }
    
    @Override
    public int refCnt() {
        return this.content.refCnt();
    }
    
    @Override
    public HttpContent retain() {
        this.content.retain();
        return this;
    }
    
    @Override
    public HttpContent retain(final int increment) {
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
    public String toString() {
        return StringUtil.simpleClassName(this) + "(data: " + this.content() + ", decoderResult: " + this.getDecoderResult() + ')';
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http.websocketx;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.util.internal.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;

public abstract class WebSocketFrame extends DefaultByteBufHolder
{
    private final boolean finalFragment;
    private final int rsv;
    
    protected WebSocketFrame(final ByteBuf binaryData) {
        this(true, 0, binaryData);
    }
    
    protected WebSocketFrame(final boolean finalFragment, final int rsv, final ByteBuf binaryData) {
        super(binaryData);
        this.finalFragment = finalFragment;
        this.rsv = rsv;
    }
    
    public boolean isFinalFragment() {
        return this.finalFragment;
    }
    
    public int rsv() {
        return this.rsv;
    }
    
    @Override
    public abstract WebSocketFrame copy();
    
    @Override
    public abstract WebSocketFrame duplicate();
    
    @Override
    public String toString() {
        return StringUtil.simpleClassName(this) + "(data: " + this.content().toString() + ')';
    }
    
    @Override
    public WebSocketFrame retain() {
        super.retain();
        return this;
    }
    
    @Override
    public WebSocketFrame retain(final int increment) {
        super.retain(increment);
        return this;
    }
}

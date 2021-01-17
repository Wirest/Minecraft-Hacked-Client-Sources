// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http.websocketx;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class PongWebSocketFrame extends WebSocketFrame
{
    public PongWebSocketFrame() {
        super(Unpooled.buffer(0));
    }
    
    public PongWebSocketFrame(final ByteBuf binaryData) {
        super(binaryData);
    }
    
    public PongWebSocketFrame(final boolean finalFragment, final int rsv, final ByteBuf binaryData) {
        super(finalFragment, rsv, binaryData);
    }
    
    @Override
    public PongWebSocketFrame copy() {
        return new PongWebSocketFrame(this.isFinalFragment(), this.rsv(), this.content().copy());
    }
    
    @Override
    public PongWebSocketFrame duplicate() {
        return new PongWebSocketFrame(this.isFinalFragment(), this.rsv(), this.content().duplicate());
    }
    
    @Override
    public PongWebSocketFrame retain() {
        super.retain();
        return this;
    }
    
    @Override
    public PongWebSocketFrame retain(final int increment) {
        super.retain(increment);
        return this;
    }
}

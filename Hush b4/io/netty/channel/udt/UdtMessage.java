// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.udt;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;

public final class UdtMessage extends DefaultByteBufHolder
{
    public UdtMessage(final ByteBuf data) {
        super(data);
    }
    
    @Override
    public UdtMessage copy() {
        return new UdtMessage(this.content().copy());
    }
    
    @Override
    public UdtMessage duplicate() {
        return new UdtMessage(this.content().duplicate());
    }
    
    @Override
    public UdtMessage retain() {
        super.retain();
        return this;
    }
    
    @Override
    public UdtMessage retain(final int increment) {
        super.retain(increment);
        return this;
    }
}

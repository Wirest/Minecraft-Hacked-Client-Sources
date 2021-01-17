// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

import io.netty.util.ReferenceCounted;
import io.netty.util.internal.StringUtil;
import io.netty.util.IllegalReferenceCountException;

public class DefaultByteBufHolder implements ByteBufHolder
{
    private final ByteBuf data;
    
    public DefaultByteBufHolder(final ByteBuf data) {
        if (data == null) {
            throw new NullPointerException("data");
        }
        this.data = data;
    }
    
    @Override
    public ByteBuf content() {
        if (this.data.refCnt() <= 0) {
            throw new IllegalReferenceCountException(this.data.refCnt());
        }
        return this.data;
    }
    
    @Override
    public ByteBufHolder copy() {
        return new DefaultByteBufHolder(this.data.copy());
    }
    
    @Override
    public ByteBufHolder duplicate() {
        return new DefaultByteBufHolder(this.data.duplicate());
    }
    
    @Override
    public int refCnt() {
        return this.data.refCnt();
    }
    
    @Override
    public ByteBufHolder retain() {
        this.data.retain();
        return this;
    }
    
    @Override
    public ByteBufHolder retain(final int increment) {
        this.data.retain(increment);
        return this;
    }
    
    @Override
    public boolean release() {
        return this.data.release();
    }
    
    @Override
    public boolean release(final int decrement) {
        return this.data.release(decrement);
    }
    
    @Override
    public String toString() {
        return StringUtil.simpleClassName(this) + '(' + this.content().toString() + ')';
    }
}

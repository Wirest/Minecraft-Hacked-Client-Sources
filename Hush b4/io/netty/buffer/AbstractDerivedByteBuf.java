// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

import io.netty.util.ReferenceCounted;
import java.nio.ByteBuffer;

public abstract class AbstractDerivedByteBuf extends AbstractByteBuf
{
    protected AbstractDerivedByteBuf(final int maxCapacity) {
        super(maxCapacity);
    }
    
    @Override
    public final int refCnt() {
        return this.unwrap().refCnt();
    }
    
    @Override
    public final ByteBuf retain() {
        this.unwrap().retain();
        return this;
    }
    
    @Override
    public final ByteBuf retain(final int increment) {
        this.unwrap().retain(increment);
        return this;
    }
    
    @Override
    public final boolean release() {
        return this.unwrap().release();
    }
    
    @Override
    public final boolean release(final int decrement) {
        return this.unwrap().release(decrement);
    }
    
    @Override
    public ByteBuffer internalNioBuffer(final int index, final int length) {
        return this.nioBuffer(index, length);
    }
    
    @Override
    public ByteBuffer nioBuffer(final int index, final int length) {
        return this.unwrap().nioBuffer(index, length);
    }
}

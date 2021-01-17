// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

import java.nio.ByteOrder;
import io.netty.util.ResourceLeak;

final class SimpleLeakAwareByteBuf extends WrappedByteBuf
{
    private final ResourceLeak leak;
    
    SimpleLeakAwareByteBuf(final ByteBuf buf, final ResourceLeak leak) {
        super(buf);
        this.leak = leak;
    }
    
    @Override
    public boolean release() {
        final boolean deallocated = super.release();
        if (deallocated) {
            this.leak.close();
        }
        return deallocated;
    }
    
    @Override
    public boolean release(final int decrement) {
        final boolean deallocated = super.release(decrement);
        if (deallocated) {
            this.leak.close();
        }
        return deallocated;
    }
    
    @Override
    public ByteBuf order(final ByteOrder endianness) {
        this.leak.record();
        if (this.order() == endianness) {
            return this;
        }
        return new SimpleLeakAwareByteBuf(super.order(endianness), this.leak);
    }
    
    @Override
    public ByteBuf slice() {
        return new SimpleLeakAwareByteBuf(super.slice(), this.leak);
    }
    
    @Override
    public ByteBuf slice(final int index, final int length) {
        return new SimpleLeakAwareByteBuf(super.slice(index, length), this.leak);
    }
    
    @Override
    public ByteBuf duplicate() {
        return new SimpleLeakAwareByteBuf(super.duplicate(), this.leak);
    }
    
    @Override
    public ByteBuf readSlice(final int length) {
        return new SimpleLeakAwareByteBuf(super.readSlice(length), this.leak);
    }
}

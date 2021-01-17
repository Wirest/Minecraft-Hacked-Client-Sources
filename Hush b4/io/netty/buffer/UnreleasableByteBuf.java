// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

import io.netty.util.ReferenceCounted;
import java.nio.ByteOrder;

final class UnreleasableByteBuf extends WrappedByteBuf
{
    private SwappedByteBuf swappedBuf;
    
    UnreleasableByteBuf(final ByteBuf buf) {
        super(buf);
    }
    
    @Override
    public ByteBuf order(final ByteOrder endianness) {
        if (endianness == null) {
            throw new NullPointerException("endianness");
        }
        if (endianness == this.order()) {
            return this;
        }
        SwappedByteBuf swappedBuf = this.swappedBuf;
        if (swappedBuf == null) {
            swappedBuf = (this.swappedBuf = new SwappedByteBuf(this));
        }
        return swappedBuf;
    }
    
    @Override
    public ByteBuf readSlice(final int length) {
        return new UnreleasableByteBuf(this.buf.readSlice(length));
    }
    
    @Override
    public ByteBuf slice() {
        return new UnreleasableByteBuf(this.buf.slice());
    }
    
    @Override
    public ByteBuf slice(final int index, final int length) {
        return new UnreleasableByteBuf(this.buf.slice(index, length));
    }
    
    @Override
    public ByteBuf duplicate() {
        return new UnreleasableByteBuf(this.buf.duplicate());
    }
    
    @Override
    public ByteBuf retain(final int increment) {
        return this;
    }
    
    @Override
    public ByteBuf retain() {
        return this;
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

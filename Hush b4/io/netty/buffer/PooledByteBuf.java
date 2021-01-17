// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import io.netty.util.Recycler;

abstract class PooledByteBuf<T> extends AbstractReferenceCountedByteBuf
{
    private final Recycler.Handle recyclerHandle;
    protected PoolChunk<T> chunk;
    protected long handle;
    protected T memory;
    protected int offset;
    protected int length;
    int maxLength;
    private ByteBuffer tmpNioBuf;
    
    protected PooledByteBuf(final Recycler.Handle recyclerHandle, final int maxCapacity) {
        super(maxCapacity);
        this.recyclerHandle = recyclerHandle;
    }
    
    void init(final PoolChunk<T> chunk, final long handle, final int offset, final int length, final int maxLength) {
        assert handle >= 0L;
        assert chunk != null;
        this.chunk = chunk;
        this.handle = handle;
        this.memory = chunk.memory;
        this.offset = offset;
        this.length = length;
        this.maxLength = maxLength;
        this.setIndex(0, 0);
        this.tmpNioBuf = null;
    }
    
    void initUnpooled(final PoolChunk<T> chunk, final int length) {
        assert chunk != null;
        this.chunk = chunk;
        this.handle = 0L;
        this.memory = chunk.memory;
        this.offset = 0;
        this.maxLength = length;
        this.length = length;
        this.setIndex(0, 0);
        this.tmpNioBuf = null;
    }
    
    @Override
    public final int capacity() {
        return this.length;
    }
    
    @Override
    public final ByteBuf capacity(final int newCapacity) {
        this.ensureAccessible();
        if (this.chunk.unpooled) {
            if (newCapacity == this.length) {
                return this;
            }
        }
        else if (newCapacity > this.length) {
            if (newCapacity <= this.maxLength) {
                this.length = newCapacity;
                return this;
            }
        }
        else {
            if (newCapacity >= this.length) {
                return this;
            }
            if (newCapacity > this.maxLength >>> 1) {
                if (this.maxLength > 512) {
                    this.length = newCapacity;
                    this.setIndex(Math.min(this.readerIndex(), newCapacity), Math.min(this.writerIndex(), newCapacity));
                    return this;
                }
                if (newCapacity > this.maxLength - 16) {
                    this.length = newCapacity;
                    this.setIndex(Math.min(this.readerIndex(), newCapacity), Math.min(this.writerIndex(), newCapacity));
                    return this;
                }
            }
        }
        this.chunk.arena.reallocate(this, newCapacity, true);
        return this;
    }
    
    @Override
    public final ByteBufAllocator alloc() {
        return this.chunk.arena.parent;
    }
    
    @Override
    public final ByteOrder order() {
        return ByteOrder.BIG_ENDIAN;
    }
    
    @Override
    public final ByteBuf unwrap() {
        return null;
    }
    
    protected final ByteBuffer internalNioBuffer() {
        ByteBuffer tmpNioBuf = this.tmpNioBuf;
        if (tmpNioBuf == null) {
            tmpNioBuf = (this.tmpNioBuf = this.newInternalNioBuffer(this.memory));
        }
        return tmpNioBuf;
    }
    
    protected abstract ByteBuffer newInternalNioBuffer(final T p0);
    
    @Override
    protected final void deallocate() {
        if (this.handle >= 0L) {
            final long handle = this.handle;
            this.handle = -1L;
            this.memory = null;
            this.chunk.arena.free(this.chunk, handle, this.maxLength);
            this.recycle();
        }
    }
    
    private void recycle() {
        final Recycler.Handle recyclerHandle = this.recyclerHandle;
        if (recyclerHandle != null) {
            this.recycler().recycle(this, recyclerHandle);
        }
    }
    
    protected abstract Recycler<?> recycler();
    
    protected final int idx(final int index) {
        return this.offset + index;
    }
}

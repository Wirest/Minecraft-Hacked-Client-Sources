// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

import java.nio.ByteOrder;
import io.netty.util.internal.PlatformDependent;
import java.nio.ByteBuffer;

final class ReadOnlyUnsafeDirectByteBuf extends ReadOnlyByteBufferBuf
{
    private static final boolean NATIVE_ORDER;
    private final long memoryAddress;
    
    ReadOnlyUnsafeDirectByteBuf(final ByteBufAllocator allocator, final ByteBuffer buffer) {
        super(allocator, buffer);
        this.memoryAddress = PlatformDependent.directBufferAddress(buffer);
    }
    
    @Override
    protected byte _getByte(final int index) {
        return PlatformDependent.getByte(this.addr(index));
    }
    
    @Override
    protected short _getShort(final int index) {
        final short v = PlatformDependent.getShort(this.addr(index));
        return ReadOnlyUnsafeDirectByteBuf.NATIVE_ORDER ? v : Short.reverseBytes(v);
    }
    
    @Override
    protected int _getUnsignedMedium(final int index) {
        final long addr = this.addr(index);
        return (PlatformDependent.getByte(addr) & 0xFF) << 16 | (PlatformDependent.getByte(addr + 1L) & 0xFF) << 8 | (PlatformDependent.getByte(addr + 2L) & 0xFF);
    }
    
    @Override
    protected int _getInt(final int index) {
        final int v = PlatformDependent.getInt(this.addr(index));
        return ReadOnlyUnsafeDirectByteBuf.NATIVE_ORDER ? v : Integer.reverseBytes(v);
    }
    
    @Override
    protected long _getLong(final int index) {
        final long v = PlatformDependent.getLong(this.addr(index));
        return ReadOnlyUnsafeDirectByteBuf.NATIVE_ORDER ? v : Long.reverseBytes(v);
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuf dst, final int dstIndex, final int length) {
        this.checkIndex(index, length);
        if (dst == null) {
            throw new NullPointerException("dst");
        }
        if (dstIndex < 0 || dstIndex > dst.capacity() - length) {
            throw new IndexOutOfBoundsException("dstIndex: " + dstIndex);
        }
        if (dst.hasMemoryAddress()) {
            PlatformDependent.copyMemory(this.addr(index), dst.memoryAddress() + dstIndex, length);
        }
        else if (dst.hasArray()) {
            PlatformDependent.copyMemory(this.addr(index), dst.array(), dst.arrayOffset() + dstIndex, length);
        }
        else {
            dst.setBytes(dstIndex, this, index, length);
        }
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final byte[] dst, final int dstIndex, final int length) {
        this.checkIndex(index, length);
        if (dst == null) {
            throw new NullPointerException("dst");
        }
        if (dstIndex < 0 || dstIndex > dst.length - length) {
            throw new IndexOutOfBoundsException(String.format("dstIndex: %d, length: %d (expected: range(0, %d))", dstIndex, length, dst.length));
        }
        if (length != 0) {
            PlatformDependent.copyMemory(this.addr(index), dst, dstIndex, length);
        }
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuffer dst) {
        this.checkIndex(index);
        if (dst == null) {
            throw new NullPointerException("dst");
        }
        final int bytesToCopy = Math.min(this.capacity() - index, dst.remaining());
        final ByteBuffer tmpBuf = this.internalNioBuffer();
        tmpBuf.clear().position(index).limit(index + bytesToCopy);
        dst.put(tmpBuf);
        return this;
    }
    
    @Override
    public ByteBuf copy(final int index, final int length) {
        this.checkIndex(index, length);
        final ByteBuf copy = this.alloc().directBuffer(length, this.maxCapacity());
        if (length != 0) {
            if (copy.hasMemoryAddress()) {
                PlatformDependent.copyMemory(this.addr(index), copy.memoryAddress(), length);
                copy.setIndex(0, length);
            }
            else {
                copy.writeBytes(this, index, length);
            }
        }
        return copy;
    }
    
    private long addr(final int index) {
        return this.memoryAddress + index;
    }
    
    static {
        NATIVE_ORDER = (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN);
    }
}

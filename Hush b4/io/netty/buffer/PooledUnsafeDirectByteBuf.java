// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

import java.nio.ByteOrder;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ScatteringByteChannel;
import java.io.InputStream;
import java.nio.channels.GatheringByteChannel;
import java.io.IOException;
import java.io.OutputStream;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.Recycler;
import java.nio.ByteBuffer;

final class PooledUnsafeDirectByteBuf extends PooledByteBuf<ByteBuffer>
{
    private static final boolean NATIVE_ORDER;
    private static final Recycler<PooledUnsafeDirectByteBuf> RECYCLER;
    private long memoryAddress;
    
    static PooledUnsafeDirectByteBuf newInstance(final int maxCapacity) {
        final PooledUnsafeDirectByteBuf buf = PooledUnsafeDirectByteBuf.RECYCLER.get();
        buf.setRefCnt(1);
        buf.maxCapacity(maxCapacity);
        return buf;
    }
    
    private PooledUnsafeDirectByteBuf(final Recycler.Handle recyclerHandle, final int maxCapacity) {
        super(recyclerHandle, maxCapacity);
    }
    
    @Override
    void init(final PoolChunk<ByteBuffer> chunk, final long handle, final int offset, final int length, final int maxLength) {
        super.init(chunk, handle, offset, length, maxLength);
        this.initMemoryAddress();
    }
    
    @Override
    void initUnpooled(final PoolChunk<ByteBuffer> chunk, final int length) {
        super.initUnpooled(chunk, length);
        this.initMemoryAddress();
    }
    
    private void initMemoryAddress() {
        this.memoryAddress = PlatformDependent.directBufferAddress((ByteBuffer)this.memory) + this.offset;
    }
    
    @Override
    protected ByteBuffer newInternalNioBuffer(final ByteBuffer memory) {
        return memory.duplicate();
    }
    
    @Override
    public boolean isDirect() {
        return true;
    }
    
    @Override
    protected byte _getByte(final int index) {
        return PlatformDependent.getByte(this.addr(index));
    }
    
    @Override
    protected short _getShort(final int index) {
        final short v = PlatformDependent.getShort(this.addr(index));
        return PooledUnsafeDirectByteBuf.NATIVE_ORDER ? v : Short.reverseBytes(v);
    }
    
    @Override
    protected int _getUnsignedMedium(final int index) {
        final long addr = this.addr(index);
        return (PlatformDependent.getByte(addr) & 0xFF) << 16 | (PlatformDependent.getByte(addr + 1L) & 0xFF) << 8 | (PlatformDependent.getByte(addr + 2L) & 0xFF);
    }
    
    @Override
    protected int _getInt(final int index) {
        final int v = PlatformDependent.getInt(this.addr(index));
        return PooledUnsafeDirectByteBuf.NATIVE_ORDER ? v : Integer.reverseBytes(v);
    }
    
    @Override
    protected long _getLong(final int index) {
        final long v = PlatformDependent.getLong(this.addr(index));
        return PooledUnsafeDirectByteBuf.NATIVE_ORDER ? v : Long.reverseBytes(v);
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
        if (length != 0) {
            if (dst.hasMemoryAddress()) {
                PlatformDependent.copyMemory(this.addr(index), dst.memoryAddress() + dstIndex, length);
            }
            else if (dst.hasArray()) {
                PlatformDependent.copyMemory(this.addr(index), dst.array(), dst.arrayOffset() + dstIndex, length);
            }
            else {
                dst.setBytes(dstIndex, this, index, length);
            }
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
            throw new IndexOutOfBoundsException("dstIndex: " + dstIndex);
        }
        if (length != 0) {
            PlatformDependent.copyMemory(this.addr(index), dst, dstIndex, length);
        }
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuffer dst) {
        this.getBytes(index, dst, false);
        return this;
    }
    
    private void getBytes(int index, final ByteBuffer dst, final boolean internal) {
        this.checkIndex(index);
        final int bytesToCopy = Math.min(this.capacity() - index, dst.remaining());
        ByteBuffer tmpBuf;
        if (internal) {
            tmpBuf = this.internalNioBuffer();
        }
        else {
            tmpBuf = ((ByteBuffer)this.memory).duplicate();
        }
        index = this.idx(index);
        tmpBuf.clear().position(index).limit(index + bytesToCopy);
        dst.put(tmpBuf);
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuffer dst) {
        final int length = dst.remaining();
        this.checkReadableBytes(length);
        this.getBytes(this.readerIndex, dst, true);
        this.readerIndex += length;
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final OutputStream out, final int length) throws IOException {
        this.checkIndex(index, length);
        if (length != 0) {
            final byte[] tmp = new byte[length];
            PlatformDependent.copyMemory(this.addr(index), tmp, 0, length);
            out.write(tmp);
        }
        return this;
    }
    
    @Override
    public int getBytes(final int index, final GatheringByteChannel out, final int length) throws IOException {
        return this.getBytes(index, out, length, false);
    }
    
    private int getBytes(int index, final GatheringByteChannel out, final int length, final boolean internal) throws IOException {
        this.checkIndex(index, length);
        if (length == 0) {
            return 0;
        }
        ByteBuffer tmpBuf;
        if (internal) {
            tmpBuf = this.internalNioBuffer();
        }
        else {
            tmpBuf = ((ByteBuffer)this.memory).duplicate();
        }
        index = this.idx(index);
        tmpBuf.clear().position(index).limit(index + length);
        return out.write(tmpBuf);
    }
    
    @Override
    public int readBytes(final GatheringByteChannel out, final int length) throws IOException {
        this.checkReadableBytes(length);
        final int readBytes = this.getBytes(this.readerIndex, out, length, true);
        this.readerIndex += readBytes;
        return readBytes;
    }
    
    @Override
    protected void _setByte(final int index, final int value) {
        PlatformDependent.putByte(this.addr(index), (byte)value);
    }
    
    @Override
    protected void _setShort(final int index, final int value) {
        PlatformDependent.putShort(this.addr(index), PooledUnsafeDirectByteBuf.NATIVE_ORDER ? ((short)value) : Short.reverseBytes((short)value));
    }
    
    @Override
    protected void _setMedium(final int index, final int value) {
        final long addr = this.addr(index);
        PlatformDependent.putByte(addr, (byte)(value >>> 16));
        PlatformDependent.putByte(addr + 1L, (byte)(value >>> 8));
        PlatformDependent.putByte(addr + 2L, (byte)value);
    }
    
    @Override
    protected void _setInt(final int index, final int value) {
        PlatformDependent.putInt(this.addr(index), PooledUnsafeDirectByteBuf.NATIVE_ORDER ? value : Integer.reverseBytes(value));
    }
    
    @Override
    protected void _setLong(final int index, final long value) {
        PlatformDependent.putLong(this.addr(index), PooledUnsafeDirectByteBuf.NATIVE_ORDER ? value : Long.reverseBytes(value));
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuf src, final int srcIndex, final int length) {
        this.checkIndex(index, length);
        if (src == null) {
            throw new NullPointerException("src");
        }
        if (srcIndex < 0 || srcIndex > src.capacity() - length) {
            throw new IndexOutOfBoundsException("srcIndex: " + srcIndex);
        }
        if (length != 0) {
            if (src.hasMemoryAddress()) {
                PlatformDependent.copyMemory(src.memoryAddress() + srcIndex, this.addr(index), length);
            }
            else if (src.hasArray()) {
                PlatformDependent.copyMemory(src.array(), src.arrayOffset() + srcIndex, this.addr(index), length);
            }
            else {
                src.getBytes(srcIndex, this, index, length);
            }
        }
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final byte[] src, final int srcIndex, final int length) {
        this.checkIndex(index, length);
        if (length != 0) {
            PlatformDependent.copyMemory(src, srcIndex, this.addr(index), length);
        }
        return this;
    }
    
    @Override
    public ByteBuf setBytes(int index, ByteBuffer src) {
        this.checkIndex(index, src.remaining());
        final ByteBuffer tmpBuf = this.internalNioBuffer();
        if (src == tmpBuf) {
            src = src.duplicate();
        }
        index = this.idx(index);
        tmpBuf.clear().position(index).limit(index + src.remaining());
        tmpBuf.put(src);
        return this;
    }
    
    @Override
    public int setBytes(final int index, final InputStream in, final int length) throws IOException {
        this.checkIndex(index, length);
        final byte[] tmp = new byte[length];
        final int readBytes = in.read(tmp);
        if (readBytes > 0) {
            PlatformDependent.copyMemory(tmp, 0, this.addr(index), readBytes);
        }
        return readBytes;
    }
    
    @Override
    public int setBytes(int index, final ScatteringByteChannel in, final int length) throws IOException {
        this.checkIndex(index, length);
        final ByteBuffer tmpBuf = this.internalNioBuffer();
        index = this.idx(index);
        tmpBuf.clear().position(index).limit(index + length);
        try {
            return in.read(tmpBuf);
        }
        catch (ClosedChannelException ignored) {
            return -1;
        }
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
    
    @Override
    public int nioBufferCount() {
        return 1;
    }
    
    @Override
    public ByteBuffer[] nioBuffers(final int index, final int length) {
        return new ByteBuffer[] { this.nioBuffer(index, length) };
    }
    
    @Override
    public ByteBuffer nioBuffer(int index, final int length) {
        this.checkIndex(index, length);
        index = this.idx(index);
        return ((ByteBuffer)((ByteBuffer)this.memory).duplicate().position(index).limit(index + length)).slice();
    }
    
    @Override
    public ByteBuffer internalNioBuffer(int index, final int length) {
        this.checkIndex(index, length);
        index = this.idx(index);
        return (ByteBuffer)this.internalNioBuffer().clear().position(index).limit(index + length);
    }
    
    @Override
    public boolean hasArray() {
        return false;
    }
    
    @Override
    public byte[] array() {
        throw new UnsupportedOperationException("direct buffer");
    }
    
    @Override
    public int arrayOffset() {
        throw new UnsupportedOperationException("direct buffer");
    }
    
    @Override
    public boolean hasMemoryAddress() {
        return true;
    }
    
    @Override
    public long memoryAddress() {
        return this.memoryAddress;
    }
    
    private long addr(final int index) {
        return this.memoryAddress + index;
    }
    
    @Override
    protected Recycler<?> recycler() {
        return PooledUnsafeDirectByteBuf.RECYCLER;
    }
    
    @Override
    protected SwappedByteBuf newSwappedByteBuf() {
        return new UnsafeDirectSwappedByteBuf(this);
    }
    
    static {
        NATIVE_ORDER = (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN);
        RECYCLER = new Recycler<PooledUnsafeDirectByteBuf>() {
            @Override
            protected PooledUnsafeDirectByteBuf newObject(final Handle handle) {
                return new PooledUnsafeDirectByteBuf(handle, 0, null);
            }
        };
    }
}

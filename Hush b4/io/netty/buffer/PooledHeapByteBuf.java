// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.ScatteringByteChannel;
import java.io.InputStream;
import java.nio.channels.GatheringByteChannel;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.Recycler;

final class PooledHeapByteBuf extends PooledByteBuf<byte[]>
{
    private static final Recycler<PooledHeapByteBuf> RECYCLER;
    
    static PooledHeapByteBuf newInstance(final int maxCapacity) {
        final PooledHeapByteBuf buf = PooledHeapByteBuf.RECYCLER.get();
        buf.setRefCnt(1);
        buf.maxCapacity(maxCapacity);
        return buf;
    }
    
    private PooledHeapByteBuf(final Recycler.Handle recyclerHandle, final int maxCapacity) {
        super(recyclerHandle, maxCapacity);
    }
    
    @Override
    public boolean isDirect() {
        return false;
    }
    
    @Override
    protected byte _getByte(final int index) {
        return ((byte[])(Object)this.memory)[this.idx(index)];
    }
    
    @Override
    protected short _getShort(int index) {
        index = this.idx(index);
        return (short)(((byte[])(Object)this.memory)[index] << 8 | (((byte[])(Object)this.memory)[index + 1] & 0xFF));
    }
    
    @Override
    protected int _getUnsignedMedium(int index) {
        index = this.idx(index);
        return (((byte[])(Object)this.memory)[index] & 0xFF) << 16 | (((byte[])(Object)this.memory)[index + 1] & 0xFF) << 8 | (((byte[])(Object)this.memory)[index + 2] & 0xFF);
    }
    
    @Override
    protected int _getInt(int index) {
        index = this.idx(index);
        return (((byte[])(Object)this.memory)[index] & 0xFF) << 24 | (((byte[])(Object)this.memory)[index + 1] & 0xFF) << 16 | (((byte[])(Object)this.memory)[index + 2] & 0xFF) << 8 | (((byte[])(Object)this.memory)[index + 3] & 0xFF);
    }
    
    @Override
    protected long _getLong(int index) {
        index = this.idx(index);
        return ((long)((byte[])(Object)this.memory)[index] & 0xFFL) << 56 | ((long)((byte[])(Object)this.memory)[index + 1] & 0xFFL) << 48 | ((long)((byte[])(Object)this.memory)[index + 2] & 0xFFL) << 40 | ((long)((byte[])(Object)this.memory)[index + 3] & 0xFFL) << 32 | ((long)((byte[])(Object)this.memory)[index + 4] & 0xFFL) << 24 | ((long)((byte[])(Object)this.memory)[index + 5] & 0xFFL) << 16 | ((long)((byte[])(Object)this.memory)[index + 6] & 0xFFL) << 8 | ((long)((byte[])(Object)this.memory)[index + 7] & 0xFFL);
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuf dst, final int dstIndex, final int length) {
        this.checkDstIndex(index, length, dstIndex, dst.capacity());
        if (dst.hasMemoryAddress()) {
            PlatformDependent.copyMemory((byte[])(Object)this.memory, this.idx(index), dst.memoryAddress() + dstIndex, length);
        }
        else if (dst.hasArray()) {
            this.getBytes(index, dst.array(), dst.arrayOffset() + dstIndex, length);
        }
        else {
            dst.setBytes(dstIndex, (byte[])(Object)this.memory, this.idx(index), length);
        }
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final byte[] dst, final int dstIndex, final int length) {
        this.checkDstIndex(index, length, dstIndex, dst.length);
        System.arraycopy(this.memory, this.idx(index), dst, dstIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuffer dst) {
        this.checkIndex(index);
        dst.put((byte[])(Object)this.memory, this.idx(index), Math.min(this.capacity() - index, dst.remaining()));
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final OutputStream out, final int length) throws IOException {
        this.checkIndex(index, length);
        out.write((byte[])(Object)this.memory, this.idx(index), length);
        return this;
    }
    
    @Override
    public int getBytes(final int index, final GatheringByteChannel out, final int length) throws IOException {
        return this.getBytes(index, out, length, false);
    }
    
    private int getBytes(int index, final GatheringByteChannel out, final int length, final boolean internal) throws IOException {
        this.checkIndex(index, length);
        index = this.idx(index);
        ByteBuffer tmpBuf;
        if (internal) {
            tmpBuf = this.internalNioBuffer();
        }
        else {
            tmpBuf = ByteBuffer.wrap((byte[])(Object)this.memory);
        }
        return out.write((ByteBuffer)tmpBuf.clear().position(index).limit(index + length));
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
        ((byte[])(Object)this.memory)[this.idx(index)] = (byte)value;
    }
    
    @Override
    protected void _setShort(int index, final int value) {
        index = this.idx(index);
        ((byte[])(Object)this.memory)[index] = (byte)(value >>> 8);
        ((byte[])(Object)this.memory)[index + 1] = (byte)value;
    }
    
    @Override
    protected void _setMedium(int index, final int value) {
        index = this.idx(index);
        ((byte[])(Object)this.memory)[index] = (byte)(value >>> 16);
        ((byte[])(Object)this.memory)[index + 1] = (byte)(value >>> 8);
        ((byte[])(Object)this.memory)[index + 2] = (byte)value;
    }
    
    @Override
    protected void _setInt(int index, final int value) {
        index = this.idx(index);
        ((byte[])(Object)this.memory)[index] = (byte)(value >>> 24);
        ((byte[])(Object)this.memory)[index + 1] = (byte)(value >>> 16);
        ((byte[])(Object)this.memory)[index + 2] = (byte)(value >>> 8);
        ((byte[])(Object)this.memory)[index + 3] = (byte)value;
    }
    
    @Override
    protected void _setLong(int index, final long value) {
        index = this.idx(index);
        ((byte[])(Object)this.memory)[index] = (byte)(value >>> 56);
        ((byte[])(Object)this.memory)[index + 1] = (byte)(value >>> 48);
        ((byte[])(Object)this.memory)[index + 2] = (byte)(value >>> 40);
        ((byte[])(Object)this.memory)[index + 3] = (byte)(value >>> 32);
        ((byte[])(Object)this.memory)[index + 4] = (byte)(value >>> 24);
        ((byte[])(Object)this.memory)[index + 5] = (byte)(value >>> 16);
        ((byte[])(Object)this.memory)[index + 6] = (byte)(value >>> 8);
        ((byte[])(Object)this.memory)[index + 7] = (byte)value;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuf src, final int srcIndex, final int length) {
        this.checkSrcIndex(index, length, srcIndex, src.capacity());
        if (src.hasMemoryAddress()) {
            PlatformDependent.copyMemory(src.memoryAddress() + srcIndex, (byte[])(Object)this.memory, this.idx(index), length);
        }
        else if (src.hasArray()) {
            this.setBytes(index, src.array(), src.arrayOffset() + srcIndex, length);
        }
        else {
            src.getBytes(srcIndex, (byte[])(Object)this.memory, this.idx(index), length);
        }
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final byte[] src, final int srcIndex, final int length) {
        this.checkSrcIndex(index, length, srcIndex, src.length);
        System.arraycopy(src, srcIndex, this.memory, this.idx(index), length);
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuffer src) {
        final int length = src.remaining();
        this.checkIndex(index, length);
        src.get((byte[])(Object)this.memory, this.idx(index), length);
        return this;
    }
    
    @Override
    public int setBytes(final int index, final InputStream in, final int length) throws IOException {
        this.checkIndex(index, length);
        return in.read((byte[])(Object)this.memory, this.idx(index), length);
    }
    
    @Override
    public int setBytes(int index, final ScatteringByteChannel in, final int length) throws IOException {
        this.checkIndex(index, length);
        index = this.idx(index);
        try {
            return in.read((ByteBuffer)this.internalNioBuffer().clear().position(index).limit(index + length));
        }
        catch (ClosedChannelException ignored) {
            return -1;
        }
    }
    
    @Override
    public ByteBuf copy(final int index, final int length) {
        this.checkIndex(index, length);
        final ByteBuf copy = this.alloc().heapBuffer(length, this.maxCapacity());
        copy.writeBytes((byte[])(Object)this.memory, this.idx(index), length);
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
        final ByteBuffer buf = ByteBuffer.wrap((byte[])(Object)this.memory, index, length);
        return buf.slice();
    }
    
    @Override
    public ByteBuffer internalNioBuffer(int index, final int length) {
        this.checkIndex(index, length);
        index = this.idx(index);
        return (ByteBuffer)this.internalNioBuffer().clear().position(index).limit(index + length);
    }
    
    @Override
    public boolean hasArray() {
        return true;
    }
    
    @Override
    public byte[] array() {
        return (byte[])(Object)this.memory;
    }
    
    @Override
    public int arrayOffset() {
        return this.offset;
    }
    
    @Override
    public boolean hasMemoryAddress() {
        return false;
    }
    
    @Override
    public long memoryAddress() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected ByteBuffer newInternalNioBuffer(final byte[] memory) {
        return ByteBuffer.wrap(memory);
    }
    
    @Override
    protected Recycler<?> recycler() {
        return PooledHeapByteBuf.RECYCLER;
    }
    
    static {
        RECYCLER = new Recycler<PooledHeapByteBuf>() {
            @Override
            protected PooledHeapByteBuf newObject(final Handle handle) {
                return new PooledHeapByteBuf(handle, 0, null);
            }
        };
    }
}

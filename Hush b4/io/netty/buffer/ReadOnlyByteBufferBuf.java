// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

import java.nio.channels.ScatteringByteChannel;
import java.io.InputStream;
import java.nio.channels.GatheringByteChannel;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ReadOnlyBufferException;
import java.nio.ByteOrder;
import io.netty.util.internal.StringUtil;
import java.nio.ByteBuffer;

class ReadOnlyByteBufferBuf extends AbstractReferenceCountedByteBuf
{
    protected final ByteBuffer buffer;
    private final ByteBufAllocator allocator;
    private ByteBuffer tmpNioBuf;
    
    ReadOnlyByteBufferBuf(final ByteBufAllocator allocator, final ByteBuffer buffer) {
        super(buffer.remaining());
        if (!buffer.isReadOnly()) {
            throw new IllegalArgumentException("must be a readonly buffer: " + StringUtil.simpleClassName(buffer));
        }
        this.allocator = allocator;
        this.buffer = buffer.slice().order(ByteOrder.BIG_ENDIAN);
        this.writerIndex(this.buffer.limit());
    }
    
    @Override
    protected void deallocate() {
    }
    
    @Override
    public byte getByte(final int index) {
        this.ensureAccessible();
        return this._getByte(index);
    }
    
    @Override
    protected byte _getByte(final int index) {
        return this.buffer.get(index);
    }
    
    @Override
    public short getShort(final int index) {
        this.ensureAccessible();
        return this._getShort(index);
    }
    
    @Override
    protected short _getShort(final int index) {
        return this.buffer.getShort(index);
    }
    
    @Override
    public int getUnsignedMedium(final int index) {
        this.ensureAccessible();
        return this._getUnsignedMedium(index);
    }
    
    @Override
    protected int _getUnsignedMedium(final int index) {
        return (this.getByte(index) & 0xFF) << 16 | (this.getByte(index + 1) & 0xFF) << 8 | (this.getByte(index + 2) & 0xFF);
    }
    
    @Override
    public int getInt(final int index) {
        this.ensureAccessible();
        return this._getInt(index);
    }
    
    @Override
    protected int _getInt(final int index) {
        return this.buffer.getInt(index);
    }
    
    @Override
    public long getLong(final int index) {
        this.ensureAccessible();
        return this._getLong(index);
    }
    
    @Override
    protected long _getLong(final int index) {
        return this.buffer.getLong(index);
    }
    
    @Override
    public ByteBuf getBytes(int index, final ByteBuf dst, final int dstIndex, final int length) {
        this.checkDstIndex(index, length, dstIndex, dst.capacity());
        if (dst.hasArray()) {
            this.getBytes(index, dst.array(), dst.arrayOffset() + dstIndex, length);
        }
        else if (dst.nioBufferCount() > 0) {
            for (final ByteBuffer bb : dst.nioBuffers(dstIndex, length)) {
                final int bbLen = bb.remaining();
                this.getBytes(index, bb);
                index += bbLen;
            }
        }
        else {
            dst.setBytes(dstIndex, this, index, length);
        }
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final byte[] dst, final int dstIndex, final int length) {
        this.checkDstIndex(index, length, dstIndex, dst.length);
        if (dstIndex < 0 || dstIndex > dst.length - length) {
            throw new IndexOutOfBoundsException(String.format("dstIndex: %d, length: %d (expected: range(0, %d))", dstIndex, length, dst.length));
        }
        final ByteBuffer tmpBuf = this.internalNioBuffer();
        tmpBuf.clear().position(index).limit(index + length);
        tmpBuf.get(dst, dstIndex, length);
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
    protected void _setByte(final int index, final int value) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    protected void _setShort(final int index, final int value) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    protected void _setMedium(final int index, final int value) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    protected void _setInt(final int index, final int value) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    protected void _setLong(final int index, final long value) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    public int capacity() {
        return this.maxCapacity();
    }
    
    @Override
    public ByteBuf capacity(final int newCapacity) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    public ByteBufAllocator alloc() {
        return this.allocator;
    }
    
    @Override
    public ByteOrder order() {
        return ByteOrder.BIG_ENDIAN;
    }
    
    @Override
    public ByteBuf unwrap() {
        return null;
    }
    
    @Override
    public boolean isDirect() {
        return this.buffer.isDirect();
    }
    
    @Override
    public ByteBuf getBytes(final int index, final OutputStream out, final int length) throws IOException {
        this.ensureAccessible();
        if (length == 0) {
            return this;
        }
        if (this.buffer.hasArray()) {
            out.write(this.buffer.array(), index + this.buffer.arrayOffset(), length);
        }
        else {
            final byte[] tmp = new byte[length];
            final ByteBuffer tmpBuf = this.internalNioBuffer();
            tmpBuf.clear().position(index);
            tmpBuf.get(tmp);
            out.write(tmp);
        }
        return this;
    }
    
    @Override
    public int getBytes(final int index, final GatheringByteChannel out, final int length) throws IOException {
        this.ensureAccessible();
        if (length == 0) {
            return 0;
        }
        final ByteBuffer tmpBuf = this.internalNioBuffer();
        tmpBuf.clear().position(index).limit(index + length);
        return out.write(tmpBuf);
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuf src, final int srcIndex, final int length) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    public ByteBuf setBytes(final int index, final byte[] src, final int srcIndex, final int length) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuffer src) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    public int setBytes(final int index, final InputStream in, final int length) throws IOException {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    public int setBytes(final int index, final ScatteringByteChannel in, final int length) throws IOException {
        throw new ReadOnlyBufferException();
    }
    
    protected final ByteBuffer internalNioBuffer() {
        ByteBuffer tmpNioBuf = this.tmpNioBuf;
        if (tmpNioBuf == null) {
            tmpNioBuf = (this.tmpNioBuf = this.buffer.duplicate());
        }
        return tmpNioBuf;
    }
    
    @Override
    public ByteBuf copy(final int index, final int length) {
        this.ensureAccessible();
        ByteBuffer src;
        try {
            src = (ByteBuffer)this.internalNioBuffer().clear().position(index).limit(index + length);
        }
        catch (IllegalArgumentException ignored) {
            throw new IndexOutOfBoundsException("Too many bytes to read - Need " + (index + length));
        }
        final ByteBuffer dst = ByteBuffer.allocateDirect(length);
        dst.put(src);
        dst.order(this.order());
        dst.clear();
        return new UnpooledDirectByteBuf(this.alloc(), dst, this.maxCapacity());
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
    public ByteBuffer nioBuffer(final int index, final int length) {
        return (ByteBuffer)this.buffer.duplicate().position(index).limit(index + length);
    }
    
    @Override
    public ByteBuffer internalNioBuffer(final int index, final int length) {
        this.ensureAccessible();
        return (ByteBuffer)this.internalNioBuffer().clear().position(index).limit(index + length);
    }
    
    @Override
    public boolean hasArray() {
        return this.buffer.hasArray();
    }
    
    @Override
    public byte[] array() {
        return this.buffer.array();
    }
    
    @Override
    public int arrayOffset() {
        return this.buffer.arrayOffset();
    }
    
    @Override
    public boolean hasMemoryAddress() {
        return false;
    }
    
    @Override
    public long memoryAddress() {
        throw new UnsupportedOperationException();
    }
}

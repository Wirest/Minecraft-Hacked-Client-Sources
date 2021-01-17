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
import io.netty.util.internal.PlatformDependent;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;

public class UnpooledDirectByteBuf extends AbstractReferenceCountedByteBuf
{
    private final ByteBufAllocator alloc;
    private ByteBuffer buffer;
    private ByteBuffer tmpNioBuf;
    private int capacity;
    private boolean doNotFree;
    
    protected UnpooledDirectByteBuf(final ByteBufAllocator alloc, final int initialCapacity, final int maxCapacity) {
        super(maxCapacity);
        if (alloc == null) {
            throw new NullPointerException("alloc");
        }
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("initialCapacity: " + initialCapacity);
        }
        if (maxCapacity < 0) {
            throw new IllegalArgumentException("maxCapacity: " + maxCapacity);
        }
        if (initialCapacity > maxCapacity) {
            throw new IllegalArgumentException(String.format("initialCapacity(%d) > maxCapacity(%d)", initialCapacity, maxCapacity));
        }
        this.alloc = alloc;
        this.setByteBuffer(ByteBuffer.allocateDirect(initialCapacity));
    }
    
    protected UnpooledDirectByteBuf(final ByteBufAllocator alloc, final ByteBuffer initialBuffer, final int maxCapacity) {
        super(maxCapacity);
        if (alloc == null) {
            throw new NullPointerException("alloc");
        }
        if (initialBuffer == null) {
            throw new NullPointerException("initialBuffer");
        }
        if (!initialBuffer.isDirect()) {
            throw new IllegalArgumentException("initialBuffer is not a direct buffer.");
        }
        if (initialBuffer.isReadOnly()) {
            throw new IllegalArgumentException("initialBuffer is a read-only buffer.");
        }
        final int initialCapacity = initialBuffer.remaining();
        if (initialCapacity > maxCapacity) {
            throw new IllegalArgumentException(String.format("initialCapacity(%d) > maxCapacity(%d)", initialCapacity, maxCapacity));
        }
        this.alloc = alloc;
        this.doNotFree = true;
        this.setByteBuffer(initialBuffer.slice().order(ByteOrder.BIG_ENDIAN));
        this.writerIndex(initialCapacity);
    }
    
    protected ByteBuffer allocateDirect(final int initialCapacity) {
        return ByteBuffer.allocateDirect(initialCapacity);
    }
    
    protected void freeDirect(final ByteBuffer buffer) {
        PlatformDependent.freeDirectBuffer(buffer);
    }
    
    private void setByteBuffer(final ByteBuffer buffer) {
        final ByteBuffer oldBuffer = this.buffer;
        if (oldBuffer != null) {
            if (this.doNotFree) {
                this.doNotFree = false;
            }
            else {
                this.freeDirect(oldBuffer);
            }
        }
        this.buffer = buffer;
        this.tmpNioBuf = null;
        this.capacity = buffer.remaining();
    }
    
    @Override
    public boolean isDirect() {
        return true;
    }
    
    @Override
    public int capacity() {
        return this.capacity;
    }
    
    @Override
    public ByteBuf capacity(final int newCapacity) {
        this.ensureAccessible();
        if (newCapacity < 0 || newCapacity > this.maxCapacity()) {
            throw new IllegalArgumentException("newCapacity: " + newCapacity);
        }
        final int readerIndex = this.readerIndex();
        int writerIndex = this.writerIndex();
        final int oldCapacity = this.capacity;
        if (newCapacity > oldCapacity) {
            final ByteBuffer oldBuffer = this.buffer;
            final ByteBuffer newBuffer = this.allocateDirect(newCapacity);
            oldBuffer.position(0).limit(oldBuffer.capacity());
            newBuffer.position(0).limit(oldBuffer.capacity());
            newBuffer.put(oldBuffer);
            newBuffer.clear();
            this.setByteBuffer(newBuffer);
        }
        else if (newCapacity < oldCapacity) {
            final ByteBuffer oldBuffer = this.buffer;
            final ByteBuffer newBuffer = this.allocateDirect(newCapacity);
            if (readerIndex < newCapacity) {
                if (writerIndex > newCapacity) {
                    writerIndex = newCapacity;
                    this.writerIndex(newCapacity);
                }
                oldBuffer.position(readerIndex).limit(writerIndex);
                newBuffer.position(readerIndex).limit(writerIndex);
                newBuffer.put(oldBuffer);
                newBuffer.clear();
            }
            else {
                this.setIndex(newCapacity, newCapacity);
            }
            this.setByteBuffer(newBuffer);
        }
        return this;
    }
    
    @Override
    public ByteBufAllocator alloc() {
        return this.alloc;
    }
    
    @Override
    public ByteOrder order() {
        return ByteOrder.BIG_ENDIAN;
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
        return false;
    }
    
    @Override
    public long memoryAddress() {
        throw new UnsupportedOperationException();
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
        this.getBytes(index, dst, dstIndex, length, false);
        return this;
    }
    
    private void getBytes(final int index, final byte[] dst, final int dstIndex, final int length, final boolean internal) {
        this.checkDstIndex(index, length, dstIndex, dst.length);
        if (dstIndex < 0 || dstIndex > dst.length - length) {
            throw new IndexOutOfBoundsException(String.format("dstIndex: %d, length: %d (expected: range(0, %d))", dstIndex, length, dst.length));
        }
        ByteBuffer tmpBuf;
        if (internal) {
            tmpBuf = this.internalNioBuffer();
        }
        else {
            tmpBuf = this.buffer.duplicate();
        }
        tmpBuf.clear().position(index).limit(index + length);
        tmpBuf.get(dst, dstIndex, length);
    }
    
    @Override
    public ByteBuf readBytes(final byte[] dst, final int dstIndex, final int length) {
        this.checkReadableBytes(length);
        this.getBytes(this.readerIndex, dst, dstIndex, length, true);
        this.readerIndex += length;
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuffer dst) {
        this.getBytes(index, dst, false);
        return this;
    }
    
    private void getBytes(final int index, final ByteBuffer dst, final boolean internal) {
        this.checkIndex(index);
        if (dst == null) {
            throw new NullPointerException("dst");
        }
        final int bytesToCopy = Math.min(this.capacity() - index, dst.remaining());
        ByteBuffer tmpBuf;
        if (internal) {
            tmpBuf = this.internalNioBuffer();
        }
        else {
            tmpBuf = this.buffer.duplicate();
        }
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
    public ByteBuf setByte(final int index, final int value) {
        this.ensureAccessible();
        this._setByte(index, value);
        return this;
    }
    
    @Override
    protected void _setByte(final int index, final int value) {
        this.buffer.put(index, (byte)value);
    }
    
    @Override
    public ByteBuf setShort(final int index, final int value) {
        this.ensureAccessible();
        this._setShort(index, value);
        return this;
    }
    
    @Override
    protected void _setShort(final int index, final int value) {
        this.buffer.putShort(index, (short)value);
    }
    
    @Override
    public ByteBuf setMedium(final int index, final int value) {
        this.ensureAccessible();
        this._setMedium(index, value);
        return this;
    }
    
    @Override
    protected void _setMedium(final int index, final int value) {
        this.setByte(index, (byte)(value >>> 16));
        this.setByte(index + 1, (byte)(value >>> 8));
        this.setByte(index + 2, (byte)value);
    }
    
    @Override
    public ByteBuf setInt(final int index, final int value) {
        this.ensureAccessible();
        this._setInt(index, value);
        return this;
    }
    
    @Override
    protected void _setInt(final int index, final int value) {
        this.buffer.putInt(index, value);
    }
    
    @Override
    public ByteBuf setLong(final int index, final long value) {
        this.ensureAccessible();
        this._setLong(index, value);
        return this;
    }
    
    @Override
    protected void _setLong(final int index, final long value) {
        this.buffer.putLong(index, value);
    }
    
    @Override
    public ByteBuf setBytes(int index, final ByteBuf src, final int srcIndex, final int length) {
        this.checkSrcIndex(index, length, srcIndex, src.capacity());
        if (src.nioBufferCount() > 0) {
            for (final ByteBuffer bb : src.nioBuffers(srcIndex, length)) {
                final int bbLen = bb.remaining();
                this.setBytes(index, bb);
                index += bbLen;
            }
        }
        else {
            src.getBytes(srcIndex, this, index, length);
        }
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final byte[] src, final int srcIndex, final int length) {
        this.checkSrcIndex(index, length, srcIndex, src.length);
        final ByteBuffer tmpBuf = this.internalNioBuffer();
        tmpBuf.clear().position(index).limit(index + length);
        tmpBuf.put(src, srcIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, ByteBuffer src) {
        this.ensureAccessible();
        final ByteBuffer tmpBuf = this.internalNioBuffer();
        if (src == tmpBuf) {
            src = src.duplicate();
        }
        tmpBuf.clear().position(index).limit(index + src.remaining());
        tmpBuf.put(src);
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final OutputStream out, final int length) throws IOException {
        this.getBytes(index, out, length, false);
        return this;
    }
    
    private void getBytes(final int index, final OutputStream out, final int length, final boolean internal) throws IOException {
        this.ensureAccessible();
        if (length == 0) {
            return;
        }
        if (this.buffer.hasArray()) {
            out.write(this.buffer.array(), index + this.buffer.arrayOffset(), length);
        }
        else {
            final byte[] tmp = new byte[length];
            ByteBuffer tmpBuf;
            if (internal) {
                tmpBuf = this.internalNioBuffer();
            }
            else {
                tmpBuf = this.buffer.duplicate();
            }
            tmpBuf.clear().position(index);
            tmpBuf.get(tmp);
            out.write(tmp);
        }
    }
    
    @Override
    public ByteBuf readBytes(final OutputStream out, final int length) throws IOException {
        this.checkReadableBytes(length);
        this.getBytes(this.readerIndex, out, length, true);
        this.readerIndex += length;
        return this;
    }
    
    @Override
    public int getBytes(final int index, final GatheringByteChannel out, final int length) throws IOException {
        return this.getBytes(index, out, length, false);
    }
    
    private int getBytes(final int index, final GatheringByteChannel out, final int length, final boolean internal) throws IOException {
        this.ensureAccessible();
        if (length == 0) {
            return 0;
        }
        ByteBuffer tmpBuf;
        if (internal) {
            tmpBuf = this.internalNioBuffer();
        }
        else {
            tmpBuf = this.buffer.duplicate();
        }
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
    public int setBytes(final int index, final InputStream in, final int length) throws IOException {
        this.ensureAccessible();
        if (this.buffer.hasArray()) {
            return in.read(this.buffer.array(), this.buffer.arrayOffset() + index, length);
        }
        final byte[] tmp = new byte[length];
        final int readBytes = in.read(tmp);
        if (readBytes <= 0) {
            return readBytes;
        }
        final ByteBuffer tmpBuf = this.internalNioBuffer();
        tmpBuf.clear().position(index);
        tmpBuf.put(tmp, 0, readBytes);
        return readBytes;
    }
    
    @Override
    public int setBytes(final int index, final ScatteringByteChannel in, final int length) throws IOException {
        this.ensureAccessible();
        final ByteBuffer tmpBuf = this.internalNioBuffer();
        tmpBuf.clear().position(index).limit(index + length);
        try {
            return in.read(this.tmpNioBuf);
        }
        catch (ClosedChannelException ignored) {
            return -1;
        }
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
    public ByteBuf copy(final int index, final int length) {
        this.ensureAccessible();
        ByteBuffer src;
        try {
            src = (ByteBuffer)this.buffer.duplicate().clear().position(index).limit(index + length);
        }
        catch (IllegalArgumentException ignored) {
            throw new IndexOutOfBoundsException("Too many bytes to read - Need " + (index + length));
        }
        return this.alloc().directBuffer(length, this.maxCapacity()).writeBytes(src);
    }
    
    @Override
    public ByteBuffer internalNioBuffer(final int index, final int length) {
        this.checkIndex(index, length);
        return (ByteBuffer)this.internalNioBuffer().clear().position(index).limit(index + length);
    }
    
    private ByteBuffer internalNioBuffer() {
        ByteBuffer tmpNioBuf = this.tmpNioBuf;
        if (tmpNioBuf == null) {
            tmpNioBuf = (this.tmpNioBuf = this.buffer.duplicate());
        }
        return tmpNioBuf;
    }
    
    @Override
    public ByteBuffer nioBuffer(final int index, final int length) {
        this.checkIndex(index, length);
        return ((ByteBuffer)this.buffer.duplicate().position(index).limit(index + length)).slice();
    }
    
    @Override
    protected void deallocate() {
        final ByteBuffer buffer = this.buffer;
        if (buffer == null) {
            return;
        }
        this.buffer = null;
        if (!this.doNotFree) {
            this.freeDirect(buffer);
        }
    }
    
    @Override
    public ByteBuf unwrap() {
        return null;
    }
}

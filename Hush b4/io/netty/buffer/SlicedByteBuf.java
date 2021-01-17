// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

import java.nio.channels.ScatteringByteChannel;
import java.io.InputStream;
import java.nio.channels.GatheringByteChannel;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SlicedByteBuf extends AbstractDerivedByteBuf
{
    private final ByteBuf buffer;
    private final int adjustment;
    private final int length;
    
    public SlicedByteBuf(final ByteBuf buffer, final int index, final int length) {
        super(length);
        if (index < 0 || index > buffer.capacity() - length) {
            throw new IndexOutOfBoundsException(buffer + ".slice(" + index + ", " + length + ')');
        }
        if (buffer instanceof SlicedByteBuf) {
            this.buffer = ((SlicedByteBuf)buffer).buffer;
            this.adjustment = ((SlicedByteBuf)buffer).adjustment + index;
        }
        else if (buffer instanceof DuplicatedByteBuf) {
            this.buffer = buffer.unwrap();
            this.adjustment = index;
        }
        else {
            this.buffer = buffer;
            this.adjustment = index;
        }
        this.writerIndex(this.length = length);
    }
    
    @Override
    public ByteBuf unwrap() {
        return this.buffer;
    }
    
    @Override
    public ByteBufAllocator alloc() {
        return this.buffer.alloc();
    }
    
    @Override
    public ByteOrder order() {
        return this.buffer.order();
    }
    
    @Override
    public boolean isDirect() {
        return this.buffer.isDirect();
    }
    
    @Override
    public int capacity() {
        return this.length;
    }
    
    @Override
    public ByteBuf capacity(final int newCapacity) {
        throw new UnsupportedOperationException("sliced buffer");
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
        return this.buffer.arrayOffset() + this.adjustment;
    }
    
    @Override
    public boolean hasMemoryAddress() {
        return this.buffer.hasMemoryAddress();
    }
    
    @Override
    public long memoryAddress() {
        return this.buffer.memoryAddress() + this.adjustment;
    }
    
    @Override
    protected byte _getByte(final int index) {
        return this.buffer.getByte(index + this.adjustment);
    }
    
    @Override
    protected short _getShort(final int index) {
        return this.buffer.getShort(index + this.adjustment);
    }
    
    @Override
    protected int _getUnsignedMedium(final int index) {
        return this.buffer.getUnsignedMedium(index + this.adjustment);
    }
    
    @Override
    protected int _getInt(final int index) {
        return this.buffer.getInt(index + this.adjustment);
    }
    
    @Override
    protected long _getLong(final int index) {
        return this.buffer.getLong(index + this.adjustment);
    }
    
    @Override
    public ByteBuf duplicate() {
        final ByteBuf duplicate = this.buffer.slice(this.adjustment, this.length);
        duplicate.setIndex(this.readerIndex(), this.writerIndex());
        return duplicate;
    }
    
    @Override
    public ByteBuf copy(final int index, final int length) {
        this.checkIndex(index, length);
        return this.buffer.copy(index + this.adjustment, length);
    }
    
    @Override
    public ByteBuf slice(final int index, final int length) {
        this.checkIndex(index, length);
        if (length == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        return this.buffer.slice(index + this.adjustment, length);
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuf dst, final int dstIndex, final int length) {
        this.checkIndex(index, length);
        this.buffer.getBytes(index + this.adjustment, dst, dstIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final byte[] dst, final int dstIndex, final int length) {
        this.checkIndex(index, length);
        this.buffer.getBytes(index + this.adjustment, dst, dstIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuffer dst) {
        this.checkIndex(index, dst.remaining());
        this.buffer.getBytes(index + this.adjustment, dst);
        return this;
    }
    
    @Override
    protected void _setByte(final int index, final int value) {
        this.buffer.setByte(index + this.adjustment, value);
    }
    
    @Override
    protected void _setShort(final int index, final int value) {
        this.buffer.setShort(index + this.adjustment, value);
    }
    
    @Override
    protected void _setMedium(final int index, final int value) {
        this.buffer.setMedium(index + this.adjustment, value);
    }
    
    @Override
    protected void _setInt(final int index, final int value) {
        this.buffer.setInt(index + this.adjustment, value);
    }
    
    @Override
    protected void _setLong(final int index, final long value) {
        this.buffer.setLong(index + this.adjustment, value);
    }
    
    @Override
    public ByteBuf setBytes(final int index, final byte[] src, final int srcIndex, final int length) {
        this.checkIndex(index, length);
        this.buffer.setBytes(index + this.adjustment, src, srcIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuf src, final int srcIndex, final int length) {
        this.checkIndex(index, length);
        this.buffer.setBytes(index + this.adjustment, src, srcIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuffer src) {
        this.checkIndex(index, src.remaining());
        this.buffer.setBytes(index + this.adjustment, src);
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final OutputStream out, final int length) throws IOException {
        this.checkIndex(index, length);
        this.buffer.getBytes(index + this.adjustment, out, length);
        return this;
    }
    
    @Override
    public int getBytes(final int index, final GatheringByteChannel out, final int length) throws IOException {
        this.checkIndex(index, length);
        return this.buffer.getBytes(index + this.adjustment, out, length);
    }
    
    @Override
    public int setBytes(final int index, final InputStream in, final int length) throws IOException {
        this.checkIndex(index, length);
        return this.buffer.setBytes(index + this.adjustment, in, length);
    }
    
    @Override
    public int setBytes(final int index, final ScatteringByteChannel in, final int length) throws IOException {
        this.checkIndex(index, length);
        return this.buffer.setBytes(index + this.adjustment, in, length);
    }
    
    @Override
    public int nioBufferCount() {
        return this.buffer.nioBufferCount();
    }
    
    @Override
    public ByteBuffer nioBuffer(final int index, final int length) {
        this.checkIndex(index, length);
        return this.buffer.nioBuffer(index + this.adjustment, length);
    }
    
    @Override
    public ByteBuffer[] nioBuffers(final int index, final int length) {
        this.checkIndex(index, length);
        return this.buffer.nioBuffers(index + this.adjustment, length);
    }
    
    @Override
    public ByteBuffer internalNioBuffer(final int index, final int length) {
        this.checkIndex(index, length);
        return this.nioBuffer(index, length);
    }
    
    @Override
    public int forEachByte(final int index, final int length, final ByteBufProcessor processor) {
        final int ret = this.buffer.forEachByte(index + this.adjustment, length, processor);
        if (ret >= this.adjustment) {
            return ret - this.adjustment;
        }
        return -1;
    }
    
    @Override
    public int forEachByteDesc(final int index, final int length, final ByteBufProcessor processor) {
        final int ret = this.buffer.forEachByteDesc(index + this.adjustment, length, processor);
        if (ret >= this.adjustment) {
            return ret - this.adjustment;
        }
        return -1;
    }
}

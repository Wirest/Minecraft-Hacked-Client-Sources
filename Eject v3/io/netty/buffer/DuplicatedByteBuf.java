package io.netty.buffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

public class DuplicatedByteBuf
        extends AbstractDerivedByteBuf {
    private final ByteBuf buffer;

    public DuplicatedByteBuf(ByteBuf paramByteBuf) {
        super(paramByteBuf.maxCapacity());
        if ((paramByteBuf instanceof DuplicatedByteBuf)) {
            this.buffer = ((DuplicatedByteBuf) paramByteBuf).buffer;
        } else {
            this.buffer = paramByteBuf;
        }
        setIndex(paramByteBuf.readerIndex(), paramByteBuf.writerIndex());
    }

    public ByteBuf unwrap() {
        return this.buffer;
    }

    public ByteBufAllocator alloc() {
        return this.buffer.alloc();
    }

    public ByteOrder order() {
        return this.buffer.order();
    }

    public boolean isDirect() {
        return this.buffer.isDirect();
    }

    public int capacity() {
        return this.buffer.capacity();
    }

    public ByteBuf capacity(int paramInt) {
        this.buffer.capacity(paramInt);
        return this;
    }

    public boolean hasArray() {
        return this.buffer.hasArray();
    }

    public byte[] array() {
        return this.buffer.array();
    }

    public int arrayOffset() {
        return this.buffer.arrayOffset();
    }

    public boolean hasMemoryAddress() {
        return this.buffer.hasMemoryAddress();
    }

    public long memoryAddress() {
        return this.buffer.memoryAddress();
    }

    public byte getByte(int paramInt) {
        return _getByte(paramInt);
    }

    protected byte _getByte(int paramInt) {
        return this.buffer.getByte(paramInt);
    }

    public short getShort(int paramInt) {
        return _getShort(paramInt);
    }

    protected short _getShort(int paramInt) {
        return this.buffer.getShort(paramInt);
    }

    public int getUnsignedMedium(int paramInt) {
        return _getUnsignedMedium(paramInt);
    }

    protected int _getUnsignedMedium(int paramInt) {
        return this.buffer.getUnsignedMedium(paramInt);
    }

    public int getInt(int paramInt) {
        return _getInt(paramInt);
    }

    protected int _getInt(int paramInt) {
        return this.buffer.getInt(paramInt);
    }

    public long getLong(int paramInt) {
        return _getLong(paramInt);
    }

    protected long _getLong(int paramInt) {
        return this.buffer.getLong(paramInt);
    }

    public ByteBuf copy(int paramInt1, int paramInt2) {
        return this.buffer.copy(paramInt1, paramInt2);
    }

    public ByteBuf slice(int paramInt1, int paramInt2) {
        return this.buffer.slice(paramInt1, paramInt2);
    }

    public ByteBuf getBytes(int paramInt1, ByteBuf paramByteBuf, int paramInt2, int paramInt3) {
        this.buffer.getBytes(paramInt1, paramByteBuf, paramInt2, paramInt3);
        return this;
    }

    public ByteBuf getBytes(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3) {
        this.buffer.getBytes(paramInt1, paramArrayOfByte, paramInt2, paramInt3);
        return this;
    }

    public ByteBuf getBytes(int paramInt, ByteBuffer paramByteBuffer) {
        this.buffer.getBytes(paramInt, paramByteBuffer);
        return this;
    }

    public ByteBuf setByte(int paramInt1, int paramInt2) {
        _setByte(paramInt1, paramInt2);
        return this;
    }

    protected void _setByte(int paramInt1, int paramInt2) {
        this.buffer.setByte(paramInt1, paramInt2);
    }

    public ByteBuf setShort(int paramInt1, int paramInt2) {
        _setShort(paramInt1, paramInt2);
        return this;
    }

    protected void _setShort(int paramInt1, int paramInt2) {
        this.buffer.setShort(paramInt1, paramInt2);
    }

    public ByteBuf setMedium(int paramInt1, int paramInt2) {
        _setMedium(paramInt1, paramInt2);
        return this;
    }

    protected void _setMedium(int paramInt1, int paramInt2) {
        this.buffer.setMedium(paramInt1, paramInt2);
    }

    public ByteBuf setInt(int paramInt1, int paramInt2) {
        _setInt(paramInt1, paramInt2);
        return this;
    }

    protected void _setInt(int paramInt1, int paramInt2) {
        this.buffer.setInt(paramInt1, paramInt2);
    }

    public ByteBuf setLong(int paramInt, long paramLong) {
        _setLong(paramInt, paramLong);
        return this;
    }

    protected void _setLong(int paramInt, long paramLong) {
        this.buffer.setLong(paramInt, paramLong);
    }

    public ByteBuf setBytes(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3) {
        this.buffer.setBytes(paramInt1, paramArrayOfByte, paramInt2, paramInt3);
        return this;
    }

    public ByteBuf setBytes(int paramInt1, ByteBuf paramByteBuf, int paramInt2, int paramInt3) {
        this.buffer.setBytes(paramInt1, paramByteBuf, paramInt2, paramInt3);
        return this;
    }

    public ByteBuf setBytes(int paramInt, ByteBuffer paramByteBuffer) {
        this.buffer.setBytes(paramInt, paramByteBuffer);
        return this;
    }

    public ByteBuf getBytes(int paramInt1, OutputStream paramOutputStream, int paramInt2)
            throws IOException {
        this.buffer.getBytes(paramInt1, paramOutputStream, paramInt2);
        return this;
    }

    public int getBytes(int paramInt1, GatheringByteChannel paramGatheringByteChannel, int paramInt2)
            throws IOException {
        return this.buffer.getBytes(paramInt1, paramGatheringByteChannel, paramInt2);
    }

    public int setBytes(int paramInt1, InputStream paramInputStream, int paramInt2)
            throws IOException {
        return this.buffer.setBytes(paramInt1, paramInputStream, paramInt2);
    }

    public int setBytes(int paramInt1, ScatteringByteChannel paramScatteringByteChannel, int paramInt2)
            throws IOException {
        return this.buffer.setBytes(paramInt1, paramScatteringByteChannel, paramInt2);
    }

    public int nioBufferCount() {
        return this.buffer.nioBufferCount();
    }

    public ByteBuffer[] nioBuffers(int paramInt1, int paramInt2) {
        return this.buffer.nioBuffers(paramInt1, paramInt2);
    }

    public ByteBuffer internalNioBuffer(int paramInt1, int paramInt2) {
        return nioBuffer(paramInt1, paramInt2);
    }

    public int forEachByte(int paramInt1, int paramInt2, ByteBufProcessor paramByteBufProcessor) {
        return this.buffer.forEachByte(paramInt1, paramInt2, paramByteBufProcessor);
    }

    public int forEachByteDesc(int paramInt1, int paramInt2, ByteBufProcessor paramByteBufProcessor) {
        return this.buffer.forEachByteDesc(paramInt1, paramInt2, paramByteBufProcessor);
    }
}





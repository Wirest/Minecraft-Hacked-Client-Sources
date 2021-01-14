package io.netty.buffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

public class SlicedByteBuf
        extends AbstractDerivedByteBuf {
    private final ByteBuf buffer;
    private final int adjustment;
    private final int length;

    public SlicedByteBuf(ByteBuf paramByteBuf, int paramInt1, int paramInt2) {
        super(paramInt2);
        if ((paramInt1 < 0) || (paramInt1 > paramByteBuf.capacity() - paramInt2)) {
            throw new IndexOutOfBoundsException(paramByteBuf + ".slice(" + paramInt1 + ", " + paramInt2 + ')');
        }
        if ((paramByteBuf instanceof SlicedByteBuf)) {
            this.buffer = ((SlicedByteBuf) paramByteBuf).buffer;
            this.adjustment = (((SlicedByteBuf) paramByteBuf).adjustment | paramInt1);
        } else if ((paramByteBuf instanceof DuplicatedByteBuf)) {
            this.buffer = paramByteBuf.unwrap();
            this.adjustment = paramInt1;
        } else {
            this.buffer = paramByteBuf;
            this.adjustment = paramInt1;
        }
        this.length = paramInt2;
        writerIndex(paramInt2);
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
        return this.length;
    }

    public ByteBuf capacity(int paramInt) {
        throw new UnsupportedOperationException("sliced buffer");
    }

    public boolean hasArray() {
        return this.buffer.hasArray();
    }

    public byte[] array() {
        return this.buffer.array();
    }

    public int arrayOffset() {
        return this.buffer.arrayOffset() | this.adjustment;
    }

    public boolean hasMemoryAddress() {
        return this.buffer.hasMemoryAddress();
    }

    public long memoryAddress() {
        return this.buffer.memoryAddress() + this.adjustment;
    }

    protected byte _getByte(int paramInt) {
        return this.buffer.getByte(paramInt | this.adjustment);
    }

    protected short _getShort(int paramInt) {
        return this.buffer.getShort(paramInt | this.adjustment);
    }

    protected int _getUnsignedMedium(int paramInt) {
        return this.buffer.getUnsignedMedium(paramInt | this.adjustment);
    }

    protected int _getInt(int paramInt) {
        return this.buffer.getInt(paramInt | this.adjustment);
    }

    protected long _getLong(int paramInt) {
        return this.buffer.getLong(paramInt | this.adjustment);
    }

    public ByteBuf duplicate() {
        ByteBuf localByteBuf = this.buffer.slice(this.adjustment, this.length);
        localByteBuf.setIndex(readerIndex(), writerIndex());
        return localByteBuf;
    }

    public ByteBuf copy(int paramInt1, int paramInt2) {
        checkIndex(paramInt1, paramInt2);
        return this.buffer.copy(paramInt1 | this.adjustment, paramInt2);
    }

    public ByteBuf slice(int paramInt1, int paramInt2) {
        checkIndex(paramInt1, paramInt2);
        if (paramInt2 == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        return this.buffer.slice(paramInt1 | this.adjustment, paramInt2);
    }

    public ByteBuf getBytes(int paramInt1, ByteBuf paramByteBuf, int paramInt2, int paramInt3) {
        checkIndex(paramInt1, paramInt3);
        this.buffer.getBytes(paramInt1 | this.adjustment, paramByteBuf, paramInt2, paramInt3);
        return this;
    }

    public ByteBuf getBytes(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3) {
        checkIndex(paramInt1, paramInt3);
        this.buffer.getBytes(paramInt1 | this.adjustment, paramArrayOfByte, paramInt2, paramInt3);
        return this;
    }

    public ByteBuf getBytes(int paramInt, ByteBuffer paramByteBuffer) {
        checkIndex(paramInt, paramByteBuffer.remaining());
        this.buffer.getBytes(paramInt | this.adjustment, paramByteBuffer);
        return this;
    }

    protected void _setByte(int paramInt1, int paramInt2) {
        this.buffer.setByte(paramInt1 | this.adjustment, paramInt2);
    }

    protected void _setShort(int paramInt1, int paramInt2) {
        this.buffer.setShort(paramInt1 | this.adjustment, paramInt2);
    }

    protected void _setMedium(int paramInt1, int paramInt2) {
        this.buffer.setMedium(paramInt1 | this.adjustment, paramInt2);
    }

    protected void _setInt(int paramInt1, int paramInt2) {
        this.buffer.setInt(paramInt1 | this.adjustment, paramInt2);
    }

    protected void _setLong(int paramInt, long paramLong) {
        this.buffer.setLong(paramInt | this.adjustment, paramLong);
    }

    public ByteBuf setBytes(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3) {
        checkIndex(paramInt1, paramInt3);
        this.buffer.setBytes(paramInt1 | this.adjustment, paramArrayOfByte, paramInt2, paramInt3);
        return this;
    }

    public ByteBuf setBytes(int paramInt1, ByteBuf paramByteBuf, int paramInt2, int paramInt3) {
        checkIndex(paramInt1, paramInt3);
        this.buffer.setBytes(paramInt1 | this.adjustment, paramByteBuf, paramInt2, paramInt3);
        return this;
    }

    public ByteBuf setBytes(int paramInt, ByteBuffer paramByteBuffer) {
        checkIndex(paramInt, paramByteBuffer.remaining());
        this.buffer.setBytes(paramInt | this.adjustment, paramByteBuffer);
        return this;
    }

    public ByteBuf getBytes(int paramInt1, OutputStream paramOutputStream, int paramInt2)
            throws IOException {
        checkIndex(paramInt1, paramInt2);
        this.buffer.getBytes(paramInt1 | this.adjustment, paramOutputStream, paramInt2);
        return this;
    }

    public int getBytes(int paramInt1, GatheringByteChannel paramGatheringByteChannel, int paramInt2)
            throws IOException {
        checkIndex(paramInt1, paramInt2);
        return this.buffer.getBytes(paramInt1 | this.adjustment, paramGatheringByteChannel, paramInt2);
    }

    public int setBytes(int paramInt1, InputStream paramInputStream, int paramInt2)
            throws IOException {
        checkIndex(paramInt1, paramInt2);
        return this.buffer.setBytes(paramInt1 | this.adjustment, paramInputStream, paramInt2);
    }

    public int setBytes(int paramInt1, ScatteringByteChannel paramScatteringByteChannel, int paramInt2)
            throws IOException {
        checkIndex(paramInt1, paramInt2);
        return this.buffer.setBytes(paramInt1 | this.adjustment, paramScatteringByteChannel, paramInt2);
    }

    public int nioBufferCount() {
        return this.buffer.nioBufferCount();
    }

    public ByteBuffer nioBuffer(int paramInt1, int paramInt2) {
        checkIndex(paramInt1, paramInt2);
        return this.buffer.nioBuffer(paramInt1 | this.adjustment, paramInt2);
    }

    public ByteBuffer[] nioBuffers(int paramInt1, int paramInt2) {
        checkIndex(paramInt1, paramInt2);
        return this.buffer.nioBuffers(paramInt1 | this.adjustment, paramInt2);
    }

    public ByteBuffer internalNioBuffer(int paramInt1, int paramInt2) {
        checkIndex(paramInt1, paramInt2);
        return nioBuffer(paramInt1, paramInt2);
    }

    public int forEachByte(int paramInt1, int paramInt2, ByteBufProcessor paramByteBufProcessor) {
        int i = this.buffer.forEachByte(paramInt1 | this.adjustment, paramInt2, paramByteBufProcessor);
        if (i >= this.adjustment) {
            return i - this.adjustment;
        }
        return -1;
    }

    public int forEachByteDesc(int paramInt1, int paramInt2, ByteBufProcessor paramByteBufProcessor) {
        int i = this.buffer.forEachByteDesc(paramInt1 | this.adjustment, paramInt2, paramByteBufProcessor);
        if (i >= this.adjustment) {
            return i - this.adjustment;
        }
        return -1;
    }
}





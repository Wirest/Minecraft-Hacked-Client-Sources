package io.netty.buffer;

import io.netty.util.internal.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;

class WrappedByteBuf
        extends ByteBuf {
    protected final ByteBuf buf;

    protected WrappedByteBuf(ByteBuf paramByteBuf) {
        if (paramByteBuf == null) {
            throw new NullPointerException("buf");
        }
        this.buf = paramByteBuf;
    }

    public boolean hasMemoryAddress() {
        return this.buf.hasMemoryAddress();
    }

    public long memoryAddress() {
        return this.buf.memoryAddress();
    }

    public int capacity() {
        return this.buf.capacity();
    }

    public ByteBuf capacity(int paramInt) {
        this.buf.capacity(paramInt);
        return this;
    }

    public int maxCapacity() {
        return this.buf.maxCapacity();
    }

    public ByteBufAllocator alloc() {
        return this.buf.alloc();
    }

    public ByteOrder order() {
        return this.buf.order();
    }

    public ByteBuf order(ByteOrder paramByteOrder) {
        return this.buf.order(paramByteOrder);
    }

    public ByteBuf unwrap() {
        return this.buf;
    }

    public boolean isDirect() {
        return this.buf.isDirect();
    }

    public int readerIndex() {
        return this.buf.readerIndex();
    }

    public ByteBuf readerIndex(int paramInt) {
        this.buf.readerIndex(paramInt);
        return this;
    }

    public int writerIndex() {
        return this.buf.writerIndex();
    }

    public ByteBuf writerIndex(int paramInt) {
        this.buf.writerIndex(paramInt);
        return this;
    }

    public ByteBuf setIndex(int paramInt1, int paramInt2) {
        this.buf.setIndex(paramInt1, paramInt2);
        return this;
    }

    public int readableBytes() {
        return this.buf.readableBytes();
    }

    public int writableBytes() {
        return this.buf.writableBytes();
    }

    public int maxWritableBytes() {
        return this.buf.maxWritableBytes();
    }

    public boolean isReadable() {
        return this.buf.isReadable();
    }

    public boolean isWritable() {
        return this.buf.isWritable();
    }

    public ByteBuf clear() {
        this.buf.clear();
        return this;
    }

    public ByteBuf markReaderIndex() {
        this.buf.markReaderIndex();
        return this;
    }

    public ByteBuf resetReaderIndex() {
        this.buf.resetReaderIndex();
        return this;
    }

    public ByteBuf markWriterIndex() {
        this.buf.markWriterIndex();
        return this;
    }

    public ByteBuf resetWriterIndex() {
        this.buf.resetWriterIndex();
        return this;
    }

    public ByteBuf discardReadBytes() {
        this.buf.discardReadBytes();
        return this;
    }

    public ByteBuf discardSomeReadBytes() {
        this.buf.discardSomeReadBytes();
        return this;
    }

    public ByteBuf ensureWritable(int paramInt) {
        this.buf.ensureWritable(paramInt);
        return this;
    }

    public int ensureWritable(int paramInt, boolean paramBoolean) {
        return this.buf.ensureWritable(paramInt, paramBoolean);
    }

    public boolean getBoolean(int paramInt) {
        return this.buf.getBoolean(paramInt);
    }

    public byte getByte(int paramInt) {
        return this.buf.getByte(paramInt);
    }

    public short getUnsignedByte(int paramInt) {
        return this.buf.getUnsignedByte(paramInt);
    }

    public short getShort(int paramInt) {
        return this.buf.getShort(paramInt);
    }

    public int getUnsignedShort(int paramInt) {
        return this.buf.getUnsignedShort(paramInt);
    }

    public int getMedium(int paramInt) {
        return this.buf.getMedium(paramInt);
    }

    public int getUnsignedMedium(int paramInt) {
        return this.buf.getUnsignedMedium(paramInt);
    }

    public int getInt(int paramInt) {
        return this.buf.getInt(paramInt);
    }

    public long getUnsignedInt(int paramInt) {
        return this.buf.getUnsignedInt(paramInt);
    }

    public long getLong(int paramInt) {
        return this.buf.getLong(paramInt);
    }

    public char getChar(int paramInt) {
        return this.buf.getChar(paramInt);
    }

    public float getFloat(int paramInt) {
        return this.buf.getFloat(paramInt);
    }

    public double getDouble(int paramInt) {
        return this.buf.getDouble(paramInt);
    }

    public ByteBuf getBytes(int paramInt, ByteBuf paramByteBuf) {
        this.buf.getBytes(paramInt, paramByteBuf);
        return this;
    }

    public ByteBuf getBytes(int paramInt1, ByteBuf paramByteBuf, int paramInt2) {
        this.buf.getBytes(paramInt1, paramByteBuf, paramInt2);
        return this;
    }

    public ByteBuf getBytes(int paramInt1, ByteBuf paramByteBuf, int paramInt2, int paramInt3) {
        this.buf.getBytes(paramInt1, paramByteBuf, paramInt2, paramInt3);
        return this;
    }

    public ByteBuf getBytes(int paramInt, byte[] paramArrayOfByte) {
        this.buf.getBytes(paramInt, paramArrayOfByte);
        return this;
    }

    public ByteBuf getBytes(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3) {
        this.buf.getBytes(paramInt1, paramArrayOfByte, paramInt2, paramInt3);
        return this;
    }

    public ByteBuf getBytes(int paramInt, ByteBuffer paramByteBuffer) {
        this.buf.getBytes(paramInt, paramByteBuffer);
        return this;
    }

    public ByteBuf getBytes(int paramInt1, OutputStream paramOutputStream, int paramInt2)
            throws IOException {
        this.buf.getBytes(paramInt1, paramOutputStream, paramInt2);
        return this;
    }

    public int getBytes(int paramInt1, GatheringByteChannel paramGatheringByteChannel, int paramInt2)
            throws IOException {
        return this.buf.getBytes(paramInt1, paramGatheringByteChannel, paramInt2);
    }

    public ByteBuf setBoolean(int paramInt, boolean paramBoolean) {
        this.buf.setBoolean(paramInt, paramBoolean);
        return this;
    }

    public ByteBuf setByte(int paramInt1, int paramInt2) {
        this.buf.setByte(paramInt1, paramInt2);
        return this;
    }

    public ByteBuf setShort(int paramInt1, int paramInt2) {
        this.buf.setShort(paramInt1, paramInt2);
        return this;
    }

    public ByteBuf setMedium(int paramInt1, int paramInt2) {
        this.buf.setMedium(paramInt1, paramInt2);
        return this;
    }

    public ByteBuf setInt(int paramInt1, int paramInt2) {
        this.buf.setInt(paramInt1, paramInt2);
        return this;
    }

    public ByteBuf setLong(int paramInt, long paramLong) {
        this.buf.setLong(paramInt, paramLong);
        return this;
    }

    public ByteBuf setChar(int paramInt1, int paramInt2) {
        this.buf.setChar(paramInt1, paramInt2);
        return this;
    }

    public ByteBuf setFloat(int paramInt, float paramFloat) {
        this.buf.setFloat(paramInt, paramFloat);
        return this;
    }

    public ByteBuf setDouble(int paramInt, double paramDouble) {
        this.buf.setDouble(paramInt, paramDouble);
        return this;
    }

    public ByteBuf setBytes(int paramInt, ByteBuf paramByteBuf) {
        this.buf.setBytes(paramInt, paramByteBuf);
        return this;
    }

    public ByteBuf setBytes(int paramInt1, ByteBuf paramByteBuf, int paramInt2) {
        this.buf.setBytes(paramInt1, paramByteBuf, paramInt2);
        return this;
    }

    public ByteBuf setBytes(int paramInt1, ByteBuf paramByteBuf, int paramInt2, int paramInt3) {
        this.buf.setBytes(paramInt1, paramByteBuf, paramInt2, paramInt3);
        return this;
    }

    public ByteBuf setBytes(int paramInt, byte[] paramArrayOfByte) {
        this.buf.setBytes(paramInt, paramArrayOfByte);
        return this;
    }

    public ByteBuf setBytes(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3) {
        this.buf.setBytes(paramInt1, paramArrayOfByte, paramInt2, paramInt3);
        return this;
    }

    public ByteBuf setBytes(int paramInt, ByteBuffer paramByteBuffer) {
        this.buf.setBytes(paramInt, paramByteBuffer);
        return this;
    }

    public int setBytes(int paramInt1, InputStream paramInputStream, int paramInt2)
            throws IOException {
        return this.buf.setBytes(paramInt1, paramInputStream, paramInt2);
    }

    public int setBytes(int paramInt1, ScatteringByteChannel paramScatteringByteChannel, int paramInt2)
            throws IOException {
        return this.buf.setBytes(paramInt1, paramScatteringByteChannel, paramInt2);
    }

    public ByteBuf setZero(int paramInt1, int paramInt2) {
        this.buf.setZero(paramInt1, paramInt2);
        return this;
    }

    public boolean readBoolean() {
        return this.buf.readBoolean();
    }

    public byte readByte() {
        return this.buf.readByte();
    }

    public short readUnsignedByte() {
        return this.buf.readUnsignedByte();
    }

    public short readShort() {
        return this.buf.readShort();
    }

    public int readUnsignedShort() {
        return this.buf.readUnsignedShort();
    }

    public int readMedium() {
        return this.buf.readMedium();
    }

    public int readUnsignedMedium() {
        return this.buf.readUnsignedMedium();
    }

    public int readInt() {
        return this.buf.readInt();
    }

    public long readUnsignedInt() {
        return this.buf.readUnsignedInt();
    }

    public long readLong() {
        return this.buf.readLong();
    }

    public char readChar() {
        return this.buf.readChar();
    }

    public float readFloat() {
        return this.buf.readFloat();
    }

    public double readDouble() {
        return this.buf.readDouble();
    }

    public ByteBuf readBytes(int paramInt) {
        return this.buf.readBytes(paramInt);
    }

    public ByteBuf readSlice(int paramInt) {
        return this.buf.readSlice(paramInt);
    }

    public ByteBuf readBytes(ByteBuf paramByteBuf) {
        this.buf.readBytes(paramByteBuf);
        return this;
    }

    public ByteBuf readBytes(ByteBuf paramByteBuf, int paramInt) {
        this.buf.readBytes(paramByteBuf, paramInt);
        return this;
    }

    public ByteBuf readBytes(ByteBuf paramByteBuf, int paramInt1, int paramInt2) {
        this.buf.readBytes(paramByteBuf, paramInt1, paramInt2);
        return this;
    }

    public ByteBuf readBytes(byte[] paramArrayOfByte) {
        this.buf.readBytes(paramArrayOfByte);
        return this;
    }

    public ByteBuf readBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        this.buf.readBytes(paramArrayOfByte, paramInt1, paramInt2);
        return this;
    }

    public ByteBuf readBytes(ByteBuffer paramByteBuffer) {
        this.buf.readBytes(paramByteBuffer);
        return this;
    }

    public ByteBuf readBytes(OutputStream paramOutputStream, int paramInt)
            throws IOException {
        this.buf.readBytes(paramOutputStream, paramInt);
        return this;
    }

    public int readBytes(GatheringByteChannel paramGatheringByteChannel, int paramInt)
            throws IOException {
        return this.buf.readBytes(paramGatheringByteChannel, paramInt);
    }

    public ByteBuf skipBytes(int paramInt) {
        this.buf.skipBytes(paramInt);
        return this;
    }

    public ByteBuf writeBoolean(boolean paramBoolean) {
        this.buf.writeBoolean(paramBoolean);
        return this;
    }

    public ByteBuf writeByte(int paramInt) {
        this.buf.writeByte(paramInt);
        return this;
    }

    public ByteBuf writeShort(int paramInt) {
        this.buf.writeShort(paramInt);
        return this;
    }

    public ByteBuf writeMedium(int paramInt) {
        this.buf.writeMedium(paramInt);
        return this;
    }

    public ByteBuf writeInt(int paramInt) {
        this.buf.writeInt(paramInt);
        return this;
    }

    public ByteBuf writeLong(long paramLong) {
        this.buf.writeLong(paramLong);
        return this;
    }

    public ByteBuf writeChar(int paramInt) {
        this.buf.writeChar(paramInt);
        return this;
    }

    public ByteBuf writeFloat(float paramFloat) {
        this.buf.writeFloat(paramFloat);
        return this;
    }

    public ByteBuf writeDouble(double paramDouble) {
        this.buf.writeDouble(paramDouble);
        return this;
    }

    public ByteBuf writeBytes(ByteBuf paramByteBuf) {
        this.buf.writeBytes(paramByteBuf);
        return this;
    }

    public ByteBuf writeBytes(ByteBuf paramByteBuf, int paramInt) {
        this.buf.writeBytes(paramByteBuf, paramInt);
        return this;
    }

    public ByteBuf writeBytes(ByteBuf paramByteBuf, int paramInt1, int paramInt2) {
        this.buf.writeBytes(paramByteBuf, paramInt1, paramInt2);
        return this;
    }

    public ByteBuf writeBytes(byte[] paramArrayOfByte) {
        this.buf.writeBytes(paramArrayOfByte);
        return this;
    }

    public ByteBuf writeBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        this.buf.writeBytes(paramArrayOfByte, paramInt1, paramInt2);
        return this;
    }

    public ByteBuf writeBytes(ByteBuffer paramByteBuffer) {
        this.buf.writeBytes(paramByteBuffer);
        return this;
    }

    public int writeBytes(InputStream paramInputStream, int paramInt)
            throws IOException {
        return this.buf.writeBytes(paramInputStream, paramInt);
    }

    public int writeBytes(ScatteringByteChannel paramScatteringByteChannel, int paramInt)
            throws IOException {
        return this.buf.writeBytes(paramScatteringByteChannel, paramInt);
    }

    public ByteBuf writeZero(int paramInt) {
        this.buf.writeZero(paramInt);
        return this;
    }

    public int indexOf(int paramInt1, int paramInt2, byte paramByte) {
        return this.buf.indexOf(paramInt1, paramInt2, paramByte);
    }

    public int bytesBefore(byte paramByte) {
        return this.buf.bytesBefore(paramByte);
    }

    public int bytesBefore(int paramInt, byte paramByte) {
        return this.buf.bytesBefore(paramInt, paramByte);
    }

    public int bytesBefore(int paramInt1, int paramInt2, byte paramByte) {
        return this.buf.bytesBefore(paramInt1, paramInt2, paramByte);
    }

    public int forEachByte(ByteBufProcessor paramByteBufProcessor) {
        return this.buf.forEachByte(paramByteBufProcessor);
    }

    public int forEachByte(int paramInt1, int paramInt2, ByteBufProcessor paramByteBufProcessor) {
        return this.buf.forEachByte(paramInt1, paramInt2, paramByteBufProcessor);
    }

    public int forEachByteDesc(ByteBufProcessor paramByteBufProcessor) {
        return this.buf.forEachByteDesc(paramByteBufProcessor);
    }

    public int forEachByteDesc(int paramInt1, int paramInt2, ByteBufProcessor paramByteBufProcessor) {
        return this.buf.forEachByteDesc(paramInt1, paramInt2, paramByteBufProcessor);
    }

    public ByteBuf copy() {
        return this.buf.copy();
    }

    public ByteBuf copy(int paramInt1, int paramInt2) {
        return this.buf.copy(paramInt1, paramInt2);
    }

    public ByteBuf slice() {
        return this.buf.slice();
    }

    public ByteBuf slice(int paramInt1, int paramInt2) {
        return this.buf.slice(paramInt1, paramInt2);
    }

    public ByteBuf duplicate() {
        return this.buf.duplicate();
    }

    public int nioBufferCount() {
        return this.buf.nioBufferCount();
    }

    public ByteBuffer nioBuffer() {
        return this.buf.nioBuffer();
    }

    public ByteBuffer nioBuffer(int paramInt1, int paramInt2) {
        return this.buf.nioBuffer(paramInt1, paramInt2);
    }

    public ByteBuffer[] nioBuffers() {
        return this.buf.nioBuffers();
    }

    public ByteBuffer[] nioBuffers(int paramInt1, int paramInt2) {
        return this.buf.nioBuffers(paramInt1, paramInt2);
    }

    public ByteBuffer internalNioBuffer(int paramInt1, int paramInt2) {
        return this.buf.internalNioBuffer(paramInt1, paramInt2);
    }

    public boolean hasArray() {
        return this.buf.hasArray();
    }

    public byte[] array() {
        return this.buf.array();
    }

    public int arrayOffset() {
        return this.buf.arrayOffset();
    }

    public String toString(Charset paramCharset) {
        return this.buf.toString(paramCharset);
    }

    public String toString(int paramInt1, int paramInt2, Charset paramCharset) {
        return this.buf.toString(paramInt1, paramInt2, paramCharset);
    }

    public int hashCode() {
        return this.buf.hashCode();
    }

    public boolean equals(Object paramObject) {
        return this.buf.equals(paramObject);
    }

    public int compareTo(ByteBuf paramByteBuf) {
        return this.buf.compareTo(paramByteBuf);
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + '(' + this.buf.toString() + ')';
    }

    public ByteBuf retain(int paramInt) {
        this.buf.retain(paramInt);
        return this;
    }

    public ByteBuf retain() {
        this.buf.retain();
        return this;
    }

    public boolean isReadable(int paramInt) {
        return this.buf.isReadable(paramInt);
    }

    public boolean isWritable(int paramInt) {
        return this.buf.isWritable(paramInt);
    }

    public int refCnt() {
        return this.buf.refCnt();
    }

    public boolean release() {
        return this.buf.release();
    }

    public boolean release(int paramInt) {
        return this.buf.release(paramInt);
    }
}





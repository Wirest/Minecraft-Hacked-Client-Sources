// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

import io.netty.util.ReferenceCounted;
import java.nio.charset.Charset;
import java.nio.channels.ScatteringByteChannel;
import java.io.InputStream;
import java.nio.channels.GatheringByteChannel;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SwappedByteBuf extends ByteBuf
{
    private final ByteBuf buf;
    private final ByteOrder order;
    
    public SwappedByteBuf(final ByteBuf buf) {
        if (buf == null) {
            throw new NullPointerException("buf");
        }
        this.buf = buf;
        if (buf.order() == ByteOrder.BIG_ENDIAN) {
            this.order = ByteOrder.LITTLE_ENDIAN;
        }
        else {
            this.order = ByteOrder.BIG_ENDIAN;
        }
    }
    
    @Override
    public ByteOrder order() {
        return this.order;
    }
    
    @Override
    public ByteBuf order(final ByteOrder endianness) {
        if (endianness == null) {
            throw new NullPointerException("endianness");
        }
        if (endianness == this.order) {
            return this;
        }
        return this.buf;
    }
    
    @Override
    public ByteBuf unwrap() {
        return this.buf.unwrap();
    }
    
    @Override
    public ByteBufAllocator alloc() {
        return this.buf.alloc();
    }
    
    @Override
    public int capacity() {
        return this.buf.capacity();
    }
    
    @Override
    public ByteBuf capacity(final int newCapacity) {
        this.buf.capacity(newCapacity);
        return this;
    }
    
    @Override
    public int maxCapacity() {
        return this.buf.maxCapacity();
    }
    
    @Override
    public boolean isDirect() {
        return this.buf.isDirect();
    }
    
    @Override
    public int readerIndex() {
        return this.buf.readerIndex();
    }
    
    @Override
    public ByteBuf readerIndex(final int readerIndex) {
        this.buf.readerIndex(readerIndex);
        return this;
    }
    
    @Override
    public int writerIndex() {
        return this.buf.writerIndex();
    }
    
    @Override
    public ByteBuf writerIndex(final int writerIndex) {
        this.buf.writerIndex(writerIndex);
        return this;
    }
    
    @Override
    public ByteBuf setIndex(final int readerIndex, final int writerIndex) {
        this.buf.setIndex(readerIndex, writerIndex);
        return this;
    }
    
    @Override
    public int readableBytes() {
        return this.buf.readableBytes();
    }
    
    @Override
    public int writableBytes() {
        return this.buf.writableBytes();
    }
    
    @Override
    public int maxWritableBytes() {
        return this.buf.maxWritableBytes();
    }
    
    @Override
    public boolean isReadable() {
        return this.buf.isReadable();
    }
    
    @Override
    public boolean isReadable(final int size) {
        return this.buf.isReadable(size);
    }
    
    @Override
    public boolean isWritable() {
        return this.buf.isWritable();
    }
    
    @Override
    public boolean isWritable(final int size) {
        return this.buf.isWritable(size);
    }
    
    @Override
    public ByteBuf clear() {
        this.buf.clear();
        return this;
    }
    
    @Override
    public ByteBuf markReaderIndex() {
        this.buf.markReaderIndex();
        return this;
    }
    
    @Override
    public ByteBuf resetReaderIndex() {
        this.buf.resetReaderIndex();
        return this;
    }
    
    @Override
    public ByteBuf markWriterIndex() {
        this.buf.markWriterIndex();
        return this;
    }
    
    @Override
    public ByteBuf resetWriterIndex() {
        this.buf.resetWriterIndex();
        return this;
    }
    
    @Override
    public ByteBuf discardReadBytes() {
        this.buf.discardReadBytes();
        return this;
    }
    
    @Override
    public ByteBuf discardSomeReadBytes() {
        this.buf.discardSomeReadBytes();
        return this;
    }
    
    @Override
    public ByteBuf ensureWritable(final int writableBytes) {
        this.buf.ensureWritable(writableBytes);
        return this;
    }
    
    @Override
    public int ensureWritable(final int minWritableBytes, final boolean force) {
        return this.buf.ensureWritable(minWritableBytes, force);
    }
    
    @Override
    public boolean getBoolean(final int index) {
        return this.buf.getBoolean(index);
    }
    
    @Override
    public byte getByte(final int index) {
        return this.buf.getByte(index);
    }
    
    @Override
    public short getUnsignedByte(final int index) {
        return this.buf.getUnsignedByte(index);
    }
    
    @Override
    public short getShort(final int index) {
        return ByteBufUtil.swapShort(this.buf.getShort(index));
    }
    
    @Override
    public int getUnsignedShort(final int index) {
        return this.getShort(index) & 0xFFFF;
    }
    
    @Override
    public int getMedium(final int index) {
        return ByteBufUtil.swapMedium(this.buf.getMedium(index));
    }
    
    @Override
    public int getUnsignedMedium(final int index) {
        return this.getMedium(index) & 0xFFFFFF;
    }
    
    @Override
    public int getInt(final int index) {
        return ByteBufUtil.swapInt(this.buf.getInt(index));
    }
    
    @Override
    public long getUnsignedInt(final int index) {
        return (long)this.getInt(index) & 0xFFFFFFFFL;
    }
    
    @Override
    public long getLong(final int index) {
        return ByteBufUtil.swapLong(this.buf.getLong(index));
    }
    
    @Override
    public char getChar(final int index) {
        return (char)this.getShort(index);
    }
    
    @Override
    public float getFloat(final int index) {
        return Float.intBitsToFloat(this.getInt(index));
    }
    
    @Override
    public double getDouble(final int index) {
        return Double.longBitsToDouble(this.getLong(index));
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuf dst) {
        this.buf.getBytes(index, dst);
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuf dst, final int length) {
        this.buf.getBytes(index, dst, length);
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuf dst, final int dstIndex, final int length) {
        this.buf.getBytes(index, dst, dstIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final byte[] dst) {
        this.buf.getBytes(index, dst);
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final byte[] dst, final int dstIndex, final int length) {
        this.buf.getBytes(index, dst, dstIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuffer dst) {
        this.buf.getBytes(index, dst);
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final OutputStream out, final int length) throws IOException {
        this.buf.getBytes(index, out, length);
        return this;
    }
    
    @Override
    public int getBytes(final int index, final GatheringByteChannel out, final int length) throws IOException {
        return this.buf.getBytes(index, out, length);
    }
    
    @Override
    public ByteBuf setBoolean(final int index, final boolean value) {
        this.buf.setBoolean(index, value);
        return this;
    }
    
    @Override
    public ByteBuf setByte(final int index, final int value) {
        this.buf.setByte(index, value);
        return this;
    }
    
    @Override
    public ByteBuf setShort(final int index, final int value) {
        this.buf.setShort(index, ByteBufUtil.swapShort((short)value));
        return this;
    }
    
    @Override
    public ByteBuf setMedium(final int index, final int value) {
        this.buf.setMedium(index, ByteBufUtil.swapMedium(value));
        return this;
    }
    
    @Override
    public ByteBuf setInt(final int index, final int value) {
        this.buf.setInt(index, ByteBufUtil.swapInt(value));
        return this;
    }
    
    @Override
    public ByteBuf setLong(final int index, final long value) {
        this.buf.setLong(index, ByteBufUtil.swapLong(value));
        return this;
    }
    
    @Override
    public ByteBuf setChar(final int index, final int value) {
        this.setShort(index, value);
        return this;
    }
    
    @Override
    public ByteBuf setFloat(final int index, final float value) {
        this.setInt(index, Float.floatToRawIntBits(value));
        return this;
    }
    
    @Override
    public ByteBuf setDouble(final int index, final double value) {
        this.setLong(index, Double.doubleToRawLongBits(value));
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuf src) {
        this.buf.setBytes(index, src);
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuf src, final int length) {
        this.buf.setBytes(index, src, length);
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuf src, final int srcIndex, final int length) {
        this.buf.setBytes(index, src, srcIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final byte[] src) {
        this.buf.setBytes(index, src);
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final byte[] src, final int srcIndex, final int length) {
        this.buf.setBytes(index, src, srcIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuffer src) {
        this.buf.setBytes(index, src);
        return this;
    }
    
    @Override
    public int setBytes(final int index, final InputStream in, final int length) throws IOException {
        return this.buf.setBytes(index, in, length);
    }
    
    @Override
    public int setBytes(final int index, final ScatteringByteChannel in, final int length) throws IOException {
        return this.buf.setBytes(index, in, length);
    }
    
    @Override
    public ByteBuf setZero(final int index, final int length) {
        this.buf.setZero(index, length);
        return this;
    }
    
    @Override
    public boolean readBoolean() {
        return this.buf.readBoolean();
    }
    
    @Override
    public byte readByte() {
        return this.buf.readByte();
    }
    
    @Override
    public short readUnsignedByte() {
        return this.buf.readUnsignedByte();
    }
    
    @Override
    public short readShort() {
        return ByteBufUtil.swapShort(this.buf.readShort());
    }
    
    @Override
    public int readUnsignedShort() {
        return this.readShort() & 0xFFFF;
    }
    
    @Override
    public int readMedium() {
        return ByteBufUtil.swapMedium(this.buf.readMedium());
    }
    
    @Override
    public int readUnsignedMedium() {
        return this.readMedium() & 0xFFFFFF;
    }
    
    @Override
    public int readInt() {
        return ByteBufUtil.swapInt(this.buf.readInt());
    }
    
    @Override
    public long readUnsignedInt() {
        return (long)this.readInt() & 0xFFFFFFFFL;
    }
    
    @Override
    public long readLong() {
        return ByteBufUtil.swapLong(this.buf.readLong());
    }
    
    @Override
    public char readChar() {
        return (char)this.readShort();
    }
    
    @Override
    public float readFloat() {
        return Float.intBitsToFloat(this.readInt());
    }
    
    @Override
    public double readDouble() {
        return Double.longBitsToDouble(this.readLong());
    }
    
    @Override
    public ByteBuf readBytes(final int length) {
        return this.buf.readBytes(length).order(this.order());
    }
    
    @Override
    public ByteBuf readSlice(final int length) {
        return this.buf.readSlice(length).order(this.order);
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuf dst) {
        this.buf.readBytes(dst);
        return this;
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuf dst, final int length) {
        this.buf.readBytes(dst, length);
        return this;
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuf dst, final int dstIndex, final int length) {
        this.buf.readBytes(dst, dstIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf readBytes(final byte[] dst) {
        this.buf.readBytes(dst);
        return this;
    }
    
    @Override
    public ByteBuf readBytes(final byte[] dst, final int dstIndex, final int length) {
        this.buf.readBytes(dst, dstIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuffer dst) {
        this.buf.readBytes(dst);
        return this;
    }
    
    @Override
    public ByteBuf readBytes(final OutputStream out, final int length) throws IOException {
        this.buf.readBytes(out, length);
        return this;
    }
    
    @Override
    public int readBytes(final GatheringByteChannel out, final int length) throws IOException {
        return this.buf.readBytes(out, length);
    }
    
    @Override
    public ByteBuf skipBytes(final int length) {
        this.buf.skipBytes(length);
        return this;
    }
    
    @Override
    public ByteBuf writeBoolean(final boolean value) {
        this.buf.writeBoolean(value);
        return this;
    }
    
    @Override
    public ByteBuf writeByte(final int value) {
        this.buf.writeByte(value);
        return this;
    }
    
    @Override
    public ByteBuf writeShort(final int value) {
        this.buf.writeShort(ByteBufUtil.swapShort((short)value));
        return this;
    }
    
    @Override
    public ByteBuf writeMedium(final int value) {
        this.buf.writeMedium(ByteBufUtil.swapMedium(value));
        return this;
    }
    
    @Override
    public ByteBuf writeInt(final int value) {
        this.buf.writeInt(ByteBufUtil.swapInt(value));
        return this;
    }
    
    @Override
    public ByteBuf writeLong(final long value) {
        this.buf.writeLong(ByteBufUtil.swapLong(value));
        return this;
    }
    
    @Override
    public ByteBuf writeChar(final int value) {
        this.writeShort(value);
        return this;
    }
    
    @Override
    public ByteBuf writeFloat(final float value) {
        this.writeInt(Float.floatToRawIntBits(value));
        return this;
    }
    
    @Override
    public ByteBuf writeDouble(final double value) {
        this.writeLong(Double.doubleToRawLongBits(value));
        return this;
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuf src) {
        this.buf.writeBytes(src);
        return this;
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuf src, final int length) {
        this.buf.writeBytes(src, length);
        return this;
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuf src, final int srcIndex, final int length) {
        this.buf.writeBytes(src, srcIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf writeBytes(final byte[] src) {
        this.buf.writeBytes(src);
        return this;
    }
    
    @Override
    public ByteBuf writeBytes(final byte[] src, final int srcIndex, final int length) {
        this.buf.writeBytes(src, srcIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuffer src) {
        this.buf.writeBytes(src);
        return this;
    }
    
    @Override
    public int writeBytes(final InputStream in, final int length) throws IOException {
        return this.buf.writeBytes(in, length);
    }
    
    @Override
    public int writeBytes(final ScatteringByteChannel in, final int length) throws IOException {
        return this.buf.writeBytes(in, length);
    }
    
    @Override
    public ByteBuf writeZero(final int length) {
        this.buf.writeZero(length);
        return this;
    }
    
    @Override
    public int indexOf(final int fromIndex, final int toIndex, final byte value) {
        return this.buf.indexOf(fromIndex, toIndex, value);
    }
    
    @Override
    public int bytesBefore(final byte value) {
        return this.buf.bytesBefore(value);
    }
    
    @Override
    public int bytesBefore(final int length, final byte value) {
        return this.buf.bytesBefore(length, value);
    }
    
    @Override
    public int bytesBefore(final int index, final int length, final byte value) {
        return this.buf.bytesBefore(index, length, value);
    }
    
    @Override
    public int forEachByte(final ByteBufProcessor processor) {
        return this.buf.forEachByte(processor);
    }
    
    @Override
    public int forEachByte(final int index, final int length, final ByteBufProcessor processor) {
        return this.buf.forEachByte(index, length, processor);
    }
    
    @Override
    public int forEachByteDesc(final ByteBufProcessor processor) {
        return this.buf.forEachByteDesc(processor);
    }
    
    @Override
    public int forEachByteDesc(final int index, final int length, final ByteBufProcessor processor) {
        return this.buf.forEachByteDesc(index, length, processor);
    }
    
    @Override
    public ByteBuf copy() {
        return this.buf.copy().order(this.order);
    }
    
    @Override
    public ByteBuf copy(final int index, final int length) {
        return this.buf.copy(index, length).order(this.order);
    }
    
    @Override
    public ByteBuf slice() {
        return this.buf.slice().order(this.order);
    }
    
    @Override
    public ByteBuf slice(final int index, final int length) {
        return this.buf.slice(index, length).order(this.order);
    }
    
    @Override
    public ByteBuf duplicate() {
        return this.buf.duplicate().order(this.order);
    }
    
    @Override
    public int nioBufferCount() {
        return this.buf.nioBufferCount();
    }
    
    @Override
    public ByteBuffer nioBuffer() {
        return this.buf.nioBuffer().order(this.order);
    }
    
    @Override
    public ByteBuffer nioBuffer(final int index, final int length) {
        return this.buf.nioBuffer(index, length).order(this.order);
    }
    
    @Override
    public ByteBuffer internalNioBuffer(final int index, final int length) {
        return this.nioBuffer(index, length);
    }
    
    @Override
    public ByteBuffer[] nioBuffers() {
        final ByteBuffer[] nioBuffers = this.buf.nioBuffers();
        for (int i = 0; i < nioBuffers.length; ++i) {
            nioBuffers[i] = nioBuffers[i].order(this.order);
        }
        return nioBuffers;
    }
    
    @Override
    public ByteBuffer[] nioBuffers(final int index, final int length) {
        final ByteBuffer[] nioBuffers = this.buf.nioBuffers(index, length);
        for (int i = 0; i < nioBuffers.length; ++i) {
            nioBuffers[i] = nioBuffers[i].order(this.order);
        }
        return nioBuffers;
    }
    
    @Override
    public boolean hasArray() {
        return this.buf.hasArray();
    }
    
    @Override
    public byte[] array() {
        return this.buf.array();
    }
    
    @Override
    public int arrayOffset() {
        return this.buf.arrayOffset();
    }
    
    @Override
    public boolean hasMemoryAddress() {
        return this.buf.hasMemoryAddress();
    }
    
    @Override
    public long memoryAddress() {
        return this.buf.memoryAddress();
    }
    
    @Override
    public String toString(final Charset charset) {
        return this.buf.toString(charset);
    }
    
    @Override
    public String toString(final int index, final int length, final Charset charset) {
        return this.buf.toString(index, length, charset);
    }
    
    @Override
    public int refCnt() {
        return this.buf.refCnt();
    }
    
    @Override
    public ByteBuf retain() {
        this.buf.retain();
        return this;
    }
    
    @Override
    public ByteBuf retain(final int increment) {
        this.buf.retain(increment);
        return this;
    }
    
    @Override
    public boolean release() {
        return this.buf.release();
    }
    
    @Override
    public boolean release(final int decrement) {
        return this.buf.release(decrement);
    }
    
    @Override
    public int hashCode() {
        return this.buf.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        return this == obj || (obj instanceof ByteBuf && ByteBufUtil.equals(this, (ByteBuf)obj));
    }
    
    @Override
    public int compareTo(final ByteBuf buffer) {
        return ByteBufUtil.compare(this, buffer);
    }
    
    @Override
    public String toString() {
        return "Swapped(" + this.buf.toString() + ')';
    }
}

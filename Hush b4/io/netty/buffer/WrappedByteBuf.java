// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

import io.netty.util.ReferenceCounted;
import io.netty.util.internal.StringUtil;
import java.nio.charset.Charset;
import java.nio.channels.ScatteringByteChannel;
import java.io.InputStream;
import java.nio.channels.GatheringByteChannel;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class WrappedByteBuf extends ByteBuf
{
    protected final ByteBuf buf;
    
    protected WrappedByteBuf(final ByteBuf buf) {
        if (buf == null) {
            throw new NullPointerException("buf");
        }
        this.buf = buf;
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
    public ByteBufAllocator alloc() {
        return this.buf.alloc();
    }
    
    @Override
    public ByteOrder order() {
        return this.buf.order();
    }
    
    @Override
    public ByteBuf order(final ByteOrder endianness) {
        return this.buf.order(endianness);
    }
    
    @Override
    public ByteBuf unwrap() {
        return this.buf;
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
    public boolean isWritable() {
        return this.buf.isWritable();
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
    public ByteBuf ensureWritable(final int minWritableBytes) {
        this.buf.ensureWritable(minWritableBytes);
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
        return this.buf.getShort(index);
    }
    
    @Override
    public int getUnsignedShort(final int index) {
        return this.buf.getUnsignedShort(index);
    }
    
    @Override
    public int getMedium(final int index) {
        return this.buf.getMedium(index);
    }
    
    @Override
    public int getUnsignedMedium(final int index) {
        return this.buf.getUnsignedMedium(index);
    }
    
    @Override
    public int getInt(final int index) {
        return this.buf.getInt(index);
    }
    
    @Override
    public long getUnsignedInt(final int index) {
        return this.buf.getUnsignedInt(index);
    }
    
    @Override
    public long getLong(final int index) {
        return this.buf.getLong(index);
    }
    
    @Override
    public char getChar(final int index) {
        return this.buf.getChar(index);
    }
    
    @Override
    public float getFloat(final int index) {
        return this.buf.getFloat(index);
    }
    
    @Override
    public double getDouble(final int index) {
        return this.buf.getDouble(index);
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
        this.buf.setShort(index, value);
        return this;
    }
    
    @Override
    public ByteBuf setMedium(final int index, final int value) {
        this.buf.setMedium(index, value);
        return this;
    }
    
    @Override
    public ByteBuf setInt(final int index, final int value) {
        this.buf.setInt(index, value);
        return this;
    }
    
    @Override
    public ByteBuf setLong(final int index, final long value) {
        this.buf.setLong(index, value);
        return this;
    }
    
    @Override
    public ByteBuf setChar(final int index, final int value) {
        this.buf.setChar(index, value);
        return this;
    }
    
    @Override
    public ByteBuf setFloat(final int index, final float value) {
        this.buf.setFloat(index, value);
        return this;
    }
    
    @Override
    public ByteBuf setDouble(final int index, final double value) {
        this.buf.setDouble(index, value);
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
        return this.buf.readShort();
    }
    
    @Override
    public int readUnsignedShort() {
        return this.buf.readUnsignedShort();
    }
    
    @Override
    public int readMedium() {
        return this.buf.readMedium();
    }
    
    @Override
    public int readUnsignedMedium() {
        return this.buf.readUnsignedMedium();
    }
    
    @Override
    public int readInt() {
        return this.buf.readInt();
    }
    
    @Override
    public long readUnsignedInt() {
        return this.buf.readUnsignedInt();
    }
    
    @Override
    public long readLong() {
        return this.buf.readLong();
    }
    
    @Override
    public char readChar() {
        return this.buf.readChar();
    }
    
    @Override
    public float readFloat() {
        return this.buf.readFloat();
    }
    
    @Override
    public double readDouble() {
        return this.buf.readDouble();
    }
    
    @Override
    public ByteBuf readBytes(final int length) {
        return this.buf.readBytes(length);
    }
    
    @Override
    public ByteBuf readSlice(final int length) {
        return this.buf.readSlice(length);
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
        this.buf.writeShort(value);
        return this;
    }
    
    @Override
    public ByteBuf writeMedium(final int value) {
        this.buf.writeMedium(value);
        return this;
    }
    
    @Override
    public ByteBuf writeInt(final int value) {
        this.buf.writeInt(value);
        return this;
    }
    
    @Override
    public ByteBuf writeLong(final long value) {
        this.buf.writeLong(value);
        return this;
    }
    
    @Override
    public ByteBuf writeChar(final int value) {
        this.buf.writeChar(value);
        return this;
    }
    
    @Override
    public ByteBuf writeFloat(final float value) {
        this.buf.writeFloat(value);
        return this;
    }
    
    @Override
    public ByteBuf writeDouble(final double value) {
        this.buf.writeDouble(value);
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
        return this.buf.copy();
    }
    
    @Override
    public ByteBuf copy(final int index, final int length) {
        return this.buf.copy(index, length);
    }
    
    @Override
    public ByteBuf slice() {
        return this.buf.slice();
    }
    
    @Override
    public ByteBuf slice(final int index, final int length) {
        return this.buf.slice(index, length);
    }
    
    @Override
    public ByteBuf duplicate() {
        return this.buf.duplicate();
    }
    
    @Override
    public int nioBufferCount() {
        return this.buf.nioBufferCount();
    }
    
    @Override
    public ByteBuffer nioBuffer() {
        return this.buf.nioBuffer();
    }
    
    @Override
    public ByteBuffer nioBuffer(final int index, final int length) {
        return this.buf.nioBuffer(index, length);
    }
    
    @Override
    public ByteBuffer[] nioBuffers() {
        return this.buf.nioBuffers();
    }
    
    @Override
    public ByteBuffer[] nioBuffers(final int index, final int length) {
        return this.buf.nioBuffers(index, length);
    }
    
    @Override
    public ByteBuffer internalNioBuffer(final int index, final int length) {
        return this.buf.internalNioBuffer(index, length);
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
    public String toString(final Charset charset) {
        return this.buf.toString(charset);
    }
    
    @Override
    public String toString(final int index, final int length, final Charset charset) {
        return this.buf.toString(index, length, charset);
    }
    
    @Override
    public int hashCode() {
        return this.buf.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        return this.buf.equals(obj);
    }
    
    @Override
    public int compareTo(final ByteBuf buffer) {
        return this.buf.compareTo(buffer);
    }
    
    @Override
    public String toString() {
        return StringUtil.simpleClassName(this) + '(' + this.buf.toString() + ')';
    }
    
    @Override
    public ByteBuf retain(final int increment) {
        this.buf.retain(increment);
        return this;
    }
    
    @Override
    public ByteBuf retain() {
        this.buf.retain();
        return this;
    }
    
    @Override
    public boolean isReadable(final int size) {
        return this.buf.isReadable(size);
    }
    
    @Override
    public boolean isWritable(final int size) {
        return this.buf.isWritable(size);
    }
    
    @Override
    public int refCnt() {
        return this.buf.refCnt();
    }
    
    @Override
    public boolean release() {
        return this.buf.release();
    }
    
    @Override
    public boolean release(final int decrement) {
        return this.buf.release(decrement);
    }
}

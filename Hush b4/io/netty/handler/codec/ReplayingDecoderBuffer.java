// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec;

import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCounted;
import io.netty.util.internal.StringUtil;
import java.nio.charset.Charset;
import java.nio.channels.ScatteringByteChannel;
import java.io.InputStream;
import java.nio.ByteOrder;
import io.netty.buffer.ByteBufProcessor;
import java.io.OutputStream;
import java.nio.channels.GatheringByteChannel;
import java.nio.ByteBuffer;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.SwappedByteBuf;
import io.netty.util.Signal;
import io.netty.buffer.ByteBuf;

final class ReplayingDecoderBuffer extends ByteBuf
{
    private static final Signal REPLAY;
    private ByteBuf buffer;
    private boolean terminated;
    private SwappedByteBuf swapped;
    static final ReplayingDecoderBuffer EMPTY_BUFFER;
    
    ReplayingDecoderBuffer() {
    }
    
    ReplayingDecoderBuffer(final ByteBuf buffer) {
        this.setCumulation(buffer);
    }
    
    void setCumulation(final ByteBuf buffer) {
        this.buffer = buffer;
    }
    
    void terminate() {
        this.terminated = true;
    }
    
    @Override
    public int capacity() {
        if (this.terminated) {
            return this.buffer.capacity();
        }
        return Integer.MAX_VALUE;
    }
    
    @Override
    public ByteBuf capacity(final int newCapacity) {
        reject();
        return this;
    }
    
    @Override
    public int maxCapacity() {
        return this.capacity();
    }
    
    @Override
    public ByteBufAllocator alloc() {
        return this.buffer.alloc();
    }
    
    @Override
    public boolean isDirect() {
        return this.buffer.isDirect();
    }
    
    @Override
    public boolean hasArray() {
        return false;
    }
    
    @Override
    public byte[] array() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int arrayOffset() {
        throw new UnsupportedOperationException();
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
    public ByteBuf clear() {
        reject();
        return this;
    }
    
    @Override
    public boolean equals(final Object obj) {
        return this == obj;
    }
    
    @Override
    public int compareTo(final ByteBuf buffer) {
        reject();
        return 0;
    }
    
    @Override
    public ByteBuf copy() {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf copy(final int index, final int length) {
        this.checkIndex(index, length);
        return this.buffer.copy(index, length);
    }
    
    @Override
    public ByteBuf discardReadBytes() {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf ensureWritable(final int writableBytes) {
        reject();
        return this;
    }
    
    @Override
    public int ensureWritable(final int minWritableBytes, final boolean force) {
        reject();
        return 0;
    }
    
    @Override
    public ByteBuf duplicate() {
        reject();
        return this;
    }
    
    @Override
    public boolean getBoolean(final int index) {
        this.checkIndex(index, 1);
        return this.buffer.getBoolean(index);
    }
    
    @Override
    public byte getByte(final int index) {
        this.checkIndex(index, 1);
        return this.buffer.getByte(index);
    }
    
    @Override
    public short getUnsignedByte(final int index) {
        this.checkIndex(index, 1);
        return this.buffer.getUnsignedByte(index);
    }
    
    @Override
    public ByteBuf getBytes(final int index, final byte[] dst, final int dstIndex, final int length) {
        this.checkIndex(index, length);
        this.buffer.getBytes(index, dst, dstIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final byte[] dst) {
        this.checkIndex(index, dst.length);
        this.buffer.getBytes(index, dst);
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuffer dst) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuf dst, final int dstIndex, final int length) {
        this.checkIndex(index, length);
        this.buffer.getBytes(index, dst, dstIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuf dst, final int length) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuf dst) {
        reject();
        return this;
    }
    
    @Override
    public int getBytes(final int index, final GatheringByteChannel out, final int length) {
        reject();
        return 0;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final OutputStream out, final int length) {
        reject();
        return this;
    }
    
    @Override
    public int getInt(final int index) {
        this.checkIndex(index, 4);
        return this.buffer.getInt(index);
    }
    
    @Override
    public long getUnsignedInt(final int index) {
        this.checkIndex(index, 4);
        return this.buffer.getUnsignedInt(index);
    }
    
    @Override
    public long getLong(final int index) {
        this.checkIndex(index, 8);
        return this.buffer.getLong(index);
    }
    
    @Override
    public int getMedium(final int index) {
        this.checkIndex(index, 3);
        return this.buffer.getMedium(index);
    }
    
    @Override
    public int getUnsignedMedium(final int index) {
        this.checkIndex(index, 3);
        return this.buffer.getUnsignedMedium(index);
    }
    
    @Override
    public short getShort(final int index) {
        this.checkIndex(index, 2);
        return this.buffer.getShort(index);
    }
    
    @Override
    public int getUnsignedShort(final int index) {
        this.checkIndex(index, 2);
        return this.buffer.getUnsignedShort(index);
    }
    
    @Override
    public char getChar(final int index) {
        this.checkIndex(index, 2);
        return this.buffer.getChar(index);
    }
    
    @Override
    public float getFloat(final int index) {
        this.checkIndex(index, 4);
        return this.buffer.getFloat(index);
    }
    
    @Override
    public double getDouble(final int index) {
        this.checkIndex(index, 8);
        return this.buffer.getDouble(index);
    }
    
    @Override
    public int hashCode() {
        reject();
        return 0;
    }
    
    @Override
    public int indexOf(final int fromIndex, final int toIndex, final byte value) {
        if (fromIndex == toIndex) {
            return -1;
        }
        if (Math.max(fromIndex, toIndex) > this.buffer.writerIndex()) {
            throw ReplayingDecoderBuffer.REPLAY;
        }
        return this.buffer.indexOf(fromIndex, toIndex, value);
    }
    
    @Override
    public int bytesBefore(final byte value) {
        final int bytes = this.buffer.bytesBefore(value);
        if (bytes < 0) {
            throw ReplayingDecoderBuffer.REPLAY;
        }
        return bytes;
    }
    
    @Override
    public int bytesBefore(final int length, final byte value) {
        final int readerIndex = this.buffer.readerIndex();
        return this.bytesBefore(readerIndex, this.buffer.writerIndex() - readerIndex, value);
    }
    
    @Override
    public int bytesBefore(final int index, final int length, final byte value) {
        final int writerIndex = this.buffer.writerIndex();
        if (index >= writerIndex) {
            throw ReplayingDecoderBuffer.REPLAY;
        }
        if (index <= writerIndex - length) {
            return this.buffer.bytesBefore(index, length, value);
        }
        final int res = this.buffer.bytesBefore(index, writerIndex - index, value);
        if (res < 0) {
            throw ReplayingDecoderBuffer.REPLAY;
        }
        return res;
    }
    
    @Override
    public int forEachByte(final ByteBufProcessor processor) {
        final int ret = this.buffer.forEachByte(processor);
        if (ret < 0) {
            throw ReplayingDecoderBuffer.REPLAY;
        }
        return ret;
    }
    
    @Override
    public int forEachByte(final int index, final int length, final ByteBufProcessor processor) {
        final int writerIndex = this.buffer.writerIndex();
        if (index >= writerIndex) {
            throw ReplayingDecoderBuffer.REPLAY;
        }
        if (index <= writerIndex - length) {
            return this.buffer.forEachByte(index, length, processor);
        }
        final int ret = this.buffer.forEachByte(index, writerIndex - index, processor);
        if (ret < 0) {
            throw ReplayingDecoderBuffer.REPLAY;
        }
        return ret;
    }
    
    @Override
    public int forEachByteDesc(final ByteBufProcessor processor) {
        if (this.terminated) {
            return this.buffer.forEachByteDesc(processor);
        }
        reject();
        return 0;
    }
    
    @Override
    public int forEachByteDesc(final int index, final int length, final ByteBufProcessor processor) {
        if (index + length > this.buffer.writerIndex()) {
            throw ReplayingDecoderBuffer.REPLAY;
        }
        return this.buffer.forEachByteDesc(index, length, processor);
    }
    
    @Override
    public ByteBuf markReaderIndex() {
        this.buffer.markReaderIndex();
        return this;
    }
    
    @Override
    public ByteBuf markWriterIndex() {
        reject();
        return this;
    }
    
    @Override
    public ByteOrder order() {
        return this.buffer.order();
    }
    
    @Override
    public ByteBuf order(final ByteOrder endianness) {
        if (endianness == null) {
            throw new NullPointerException("endianness");
        }
        if (endianness == this.order()) {
            return this;
        }
        SwappedByteBuf swapped = this.swapped;
        if (swapped == null) {
            swapped = (this.swapped = new SwappedByteBuf(this));
        }
        return swapped;
    }
    
    @Override
    public boolean isReadable() {
        return !this.terminated || this.buffer.isReadable();
    }
    
    @Override
    public boolean isReadable(final int size) {
        return !this.terminated || this.buffer.isReadable(size);
    }
    
    @Override
    public int readableBytes() {
        if (this.terminated) {
            return this.buffer.readableBytes();
        }
        return Integer.MAX_VALUE - this.buffer.readerIndex();
    }
    
    @Override
    public boolean readBoolean() {
        this.checkReadableBytes(1);
        return this.buffer.readBoolean();
    }
    
    @Override
    public byte readByte() {
        this.checkReadableBytes(1);
        return this.buffer.readByte();
    }
    
    @Override
    public short readUnsignedByte() {
        this.checkReadableBytes(1);
        return this.buffer.readUnsignedByte();
    }
    
    @Override
    public ByteBuf readBytes(final byte[] dst, final int dstIndex, final int length) {
        this.checkReadableBytes(length);
        this.buffer.readBytes(dst, dstIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf readBytes(final byte[] dst) {
        this.checkReadableBytes(dst.length);
        this.buffer.readBytes(dst);
        return this;
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuffer dst) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuf dst, final int dstIndex, final int length) {
        this.checkReadableBytes(length);
        this.buffer.readBytes(dst, dstIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuf dst, final int length) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuf dst) {
        this.checkReadableBytes(dst.writableBytes());
        this.buffer.readBytes(dst);
        return this;
    }
    
    @Override
    public int readBytes(final GatheringByteChannel out, final int length) {
        reject();
        return 0;
    }
    
    @Override
    public ByteBuf readBytes(final int length) {
        this.checkReadableBytes(length);
        return this.buffer.readBytes(length);
    }
    
    @Override
    public ByteBuf readSlice(final int length) {
        this.checkReadableBytes(length);
        return this.buffer.readSlice(length);
    }
    
    @Override
    public ByteBuf readBytes(final OutputStream out, final int length) {
        reject();
        return this;
    }
    
    @Override
    public int readerIndex() {
        return this.buffer.readerIndex();
    }
    
    @Override
    public ByteBuf readerIndex(final int readerIndex) {
        this.buffer.readerIndex(readerIndex);
        return this;
    }
    
    @Override
    public int readInt() {
        this.checkReadableBytes(4);
        return this.buffer.readInt();
    }
    
    @Override
    public long readUnsignedInt() {
        this.checkReadableBytes(4);
        return this.buffer.readUnsignedInt();
    }
    
    @Override
    public long readLong() {
        this.checkReadableBytes(8);
        return this.buffer.readLong();
    }
    
    @Override
    public int readMedium() {
        this.checkReadableBytes(3);
        return this.buffer.readMedium();
    }
    
    @Override
    public int readUnsignedMedium() {
        this.checkReadableBytes(3);
        return this.buffer.readUnsignedMedium();
    }
    
    @Override
    public short readShort() {
        this.checkReadableBytes(2);
        return this.buffer.readShort();
    }
    
    @Override
    public int readUnsignedShort() {
        this.checkReadableBytes(2);
        return this.buffer.readUnsignedShort();
    }
    
    @Override
    public char readChar() {
        this.checkReadableBytes(2);
        return this.buffer.readChar();
    }
    
    @Override
    public float readFloat() {
        this.checkReadableBytes(4);
        return this.buffer.readFloat();
    }
    
    @Override
    public double readDouble() {
        this.checkReadableBytes(8);
        return this.buffer.readDouble();
    }
    
    @Override
    public ByteBuf resetReaderIndex() {
        this.buffer.resetReaderIndex();
        return this;
    }
    
    @Override
    public ByteBuf resetWriterIndex() {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf setBoolean(final int index, final boolean value) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf setByte(final int index, final int value) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final byte[] src, final int srcIndex, final int length) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final byte[] src) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuffer src) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuf src, final int srcIndex, final int length) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuf src, final int length) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuf src) {
        reject();
        return this;
    }
    
    @Override
    public int setBytes(final int index, final InputStream in, final int length) {
        reject();
        return 0;
    }
    
    @Override
    public ByteBuf setZero(final int index, final int length) {
        reject();
        return this;
    }
    
    @Override
    public int setBytes(final int index, final ScatteringByteChannel in, final int length) {
        reject();
        return 0;
    }
    
    @Override
    public ByteBuf setIndex(final int readerIndex, final int writerIndex) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf setInt(final int index, final int value) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf setLong(final int index, final long value) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf setMedium(final int index, final int value) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf setShort(final int index, final int value) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf setChar(final int index, final int value) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf setFloat(final int index, final float value) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf setDouble(final int index, final double value) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf skipBytes(final int length) {
        this.checkReadableBytes(length);
        this.buffer.skipBytes(length);
        return this;
    }
    
    @Override
    public ByteBuf slice() {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf slice(final int index, final int length) {
        this.checkIndex(index, length);
        return this.buffer.slice(index, length);
    }
    
    @Override
    public int nioBufferCount() {
        return this.buffer.nioBufferCount();
    }
    
    @Override
    public ByteBuffer nioBuffer() {
        reject();
        return null;
    }
    
    @Override
    public ByteBuffer nioBuffer(final int index, final int length) {
        this.checkIndex(index, length);
        return this.buffer.nioBuffer(index, length);
    }
    
    @Override
    public ByteBuffer[] nioBuffers() {
        reject();
        return null;
    }
    
    @Override
    public ByteBuffer[] nioBuffers(final int index, final int length) {
        this.checkIndex(index, length);
        return this.buffer.nioBuffers(index, length);
    }
    
    @Override
    public ByteBuffer internalNioBuffer(final int index, final int length) {
        this.checkIndex(index, length);
        return this.buffer.internalNioBuffer(index, length);
    }
    
    @Override
    public String toString(final int index, final int length, final Charset charset) {
        this.checkIndex(index, length);
        return this.buffer.toString(index, length, charset);
    }
    
    @Override
    public String toString(final Charset charsetName) {
        reject();
        return null;
    }
    
    @Override
    public String toString() {
        return StringUtil.simpleClassName(this) + '(' + "ridx=" + this.readerIndex() + ", " + "widx=" + this.writerIndex() + ')';
    }
    
    @Override
    public boolean isWritable() {
        return false;
    }
    
    @Override
    public boolean isWritable(final int size) {
        return false;
    }
    
    @Override
    public int writableBytes() {
        return 0;
    }
    
    @Override
    public int maxWritableBytes() {
        return 0;
    }
    
    @Override
    public ByteBuf writeBoolean(final boolean value) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf writeByte(final int value) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf writeBytes(final byte[] src, final int srcIndex, final int length) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf writeBytes(final byte[] src) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuffer src) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuf src, final int srcIndex, final int length) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuf src, final int length) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuf src) {
        reject();
        return this;
    }
    
    @Override
    public int writeBytes(final InputStream in, final int length) {
        reject();
        return 0;
    }
    
    @Override
    public int writeBytes(final ScatteringByteChannel in, final int length) {
        reject();
        return 0;
    }
    
    @Override
    public ByteBuf writeInt(final int value) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf writeLong(final long value) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf writeMedium(final int value) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf writeZero(final int length) {
        reject();
        return this;
    }
    
    @Override
    public int writerIndex() {
        return this.buffer.writerIndex();
    }
    
    @Override
    public ByteBuf writerIndex(final int writerIndex) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf writeShort(final int value) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf writeChar(final int value) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf writeFloat(final float value) {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf writeDouble(final double value) {
        reject();
        return this;
    }
    
    private void checkIndex(final int index, final int length) {
        if (index + length > this.buffer.writerIndex()) {
            throw ReplayingDecoderBuffer.REPLAY;
        }
    }
    
    private void checkReadableBytes(final int readableBytes) {
        if (this.buffer.readableBytes() < readableBytes) {
            throw ReplayingDecoderBuffer.REPLAY;
        }
    }
    
    @Override
    public ByteBuf discardSomeReadBytes() {
        reject();
        return this;
    }
    
    @Override
    public int refCnt() {
        return this.buffer.refCnt();
    }
    
    @Override
    public ByteBuf retain() {
        reject();
        return this;
    }
    
    @Override
    public ByteBuf retain(final int increment) {
        reject();
        return this;
    }
    
    @Override
    public boolean release() {
        reject();
        return false;
    }
    
    @Override
    public boolean release(final int decrement) {
        reject();
        return false;
    }
    
    @Override
    public ByteBuf unwrap() {
        reject();
        return this;
    }
    
    private static void reject() {
        throw new UnsupportedOperationException("not a replayable operation");
    }
    
    static {
        REPLAY = ReplayingDecoder.REPLAY;
        (EMPTY_BUFFER = new ReplayingDecoderBuffer(Unpooled.EMPTY_BUFFER)).terminate();
    }
}

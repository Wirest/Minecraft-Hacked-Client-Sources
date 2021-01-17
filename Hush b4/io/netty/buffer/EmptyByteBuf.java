// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.ReferenceCounted;
import java.nio.charset.Charset;
import io.netty.util.internal.EmptyArrays;
import java.nio.channels.ScatteringByteChannel;
import java.io.InputStream;
import java.nio.channels.GatheringByteChannel;
import java.io.OutputStream;
import java.nio.ReadOnlyBufferException;
import io.netty.util.internal.StringUtil;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;

public final class EmptyByteBuf extends ByteBuf
{
    private static final ByteBuffer EMPTY_BYTE_BUFFER;
    private static final long EMPTY_BYTE_BUFFER_ADDRESS;
    private final ByteBufAllocator alloc;
    private final ByteOrder order;
    private final String str;
    private EmptyByteBuf swapped;
    
    public EmptyByteBuf(final ByteBufAllocator alloc) {
        this(alloc, ByteOrder.BIG_ENDIAN);
    }
    
    private EmptyByteBuf(final ByteBufAllocator alloc, final ByteOrder order) {
        if (alloc == null) {
            throw new NullPointerException("alloc");
        }
        this.alloc = alloc;
        this.order = order;
        this.str = StringUtil.simpleClassName(this) + ((order == ByteOrder.BIG_ENDIAN) ? "BE" : "LE");
    }
    
    @Override
    public int capacity() {
        return 0;
    }
    
    @Override
    public ByteBuf capacity(final int newCapacity) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    public ByteBufAllocator alloc() {
        return this.alloc;
    }
    
    @Override
    public ByteOrder order() {
        return this.order;
    }
    
    @Override
    public ByteBuf unwrap() {
        return null;
    }
    
    @Override
    public boolean isDirect() {
        return true;
    }
    
    @Override
    public int maxCapacity() {
        return 0;
    }
    
    @Override
    public ByteBuf order(final ByteOrder endianness) {
        if (endianness == null) {
            throw new NullPointerException("endianness");
        }
        if (endianness == this.order()) {
            return this;
        }
        EmptyByteBuf swapped = this.swapped;
        if (swapped != null) {
            return swapped;
        }
        swapped = (this.swapped = new EmptyByteBuf(this.alloc(), endianness));
        return swapped;
    }
    
    @Override
    public int readerIndex() {
        return 0;
    }
    
    @Override
    public ByteBuf readerIndex(final int readerIndex) {
        return this.checkIndex(readerIndex);
    }
    
    @Override
    public int writerIndex() {
        return 0;
    }
    
    @Override
    public ByteBuf writerIndex(final int writerIndex) {
        return this.checkIndex(writerIndex);
    }
    
    @Override
    public ByteBuf setIndex(final int readerIndex, final int writerIndex) {
        this.checkIndex(readerIndex);
        this.checkIndex(writerIndex);
        return this;
    }
    
    @Override
    public int readableBytes() {
        return 0;
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
    public boolean isReadable() {
        return false;
    }
    
    @Override
    public boolean isWritable() {
        return false;
    }
    
    @Override
    public ByteBuf clear() {
        return this;
    }
    
    @Override
    public ByteBuf markReaderIndex() {
        return this;
    }
    
    @Override
    public ByteBuf resetReaderIndex() {
        return this;
    }
    
    @Override
    public ByteBuf markWriterIndex() {
        return this;
    }
    
    @Override
    public ByteBuf resetWriterIndex() {
        return this;
    }
    
    @Override
    public ByteBuf discardReadBytes() {
        return this;
    }
    
    @Override
    public ByteBuf discardSomeReadBytes() {
        return this;
    }
    
    @Override
    public ByteBuf ensureWritable(final int minWritableBytes) {
        if (minWritableBytes < 0) {
            throw new IllegalArgumentException("minWritableBytes: " + minWritableBytes + " (expected: >= 0)");
        }
        if (minWritableBytes != 0) {
            throw new IndexOutOfBoundsException();
        }
        return this;
    }
    
    @Override
    public int ensureWritable(final int minWritableBytes, final boolean force) {
        if (minWritableBytes < 0) {
            throw new IllegalArgumentException("minWritableBytes: " + minWritableBytes + " (expected: >= 0)");
        }
        if (minWritableBytes == 0) {
            return 0;
        }
        return 1;
    }
    
    @Override
    public boolean getBoolean(final int index) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public byte getByte(final int index) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public short getUnsignedByte(final int index) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public short getShort(final int index) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public int getUnsignedShort(final int index) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public int getMedium(final int index) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public int getUnsignedMedium(final int index) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public int getInt(final int index) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public long getUnsignedInt(final int index) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public long getLong(final int index) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public char getChar(final int index) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public float getFloat(final int index) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public double getDouble(final int index) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuf dst) {
        return this.checkIndex(index, dst.writableBytes());
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuf dst, final int length) {
        return this.checkIndex(index, length);
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuf dst, final int dstIndex, final int length) {
        return this.checkIndex(index, length);
    }
    
    @Override
    public ByteBuf getBytes(final int index, final byte[] dst) {
        return this.checkIndex(index, dst.length);
    }
    
    @Override
    public ByteBuf getBytes(final int index, final byte[] dst, final int dstIndex, final int length) {
        return this.checkIndex(index, length);
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuffer dst) {
        return this.checkIndex(index, dst.remaining());
    }
    
    @Override
    public ByteBuf getBytes(final int index, final OutputStream out, final int length) {
        return this.checkIndex(index, length);
    }
    
    @Override
    public int getBytes(final int index, final GatheringByteChannel out, final int length) {
        this.checkIndex(index, length);
        return 0;
    }
    
    @Override
    public ByteBuf setBoolean(final int index, final boolean value) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public ByteBuf setByte(final int index, final int value) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public ByteBuf setShort(final int index, final int value) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public ByteBuf setMedium(final int index, final int value) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public ByteBuf setInt(final int index, final int value) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public ByteBuf setLong(final int index, final long value) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public ByteBuf setChar(final int index, final int value) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public ByteBuf setFloat(final int index, final float value) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public ByteBuf setDouble(final int index, final double value) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuf src) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuf src, final int length) {
        return this.checkIndex(index, length);
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuf src, final int srcIndex, final int length) {
        return this.checkIndex(index, length);
    }
    
    @Override
    public ByteBuf setBytes(final int index, final byte[] src) {
        return this.checkIndex(index, src.length);
    }
    
    @Override
    public ByteBuf setBytes(final int index, final byte[] src, final int srcIndex, final int length) {
        return this.checkIndex(index, length);
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuffer src) {
        return this.checkIndex(index, src.remaining());
    }
    
    @Override
    public int setBytes(final int index, final InputStream in, final int length) {
        this.checkIndex(index, length);
        return 0;
    }
    
    @Override
    public int setBytes(final int index, final ScatteringByteChannel in, final int length) {
        this.checkIndex(index, length);
        return 0;
    }
    
    @Override
    public ByteBuf setZero(final int index, final int length) {
        return this.checkIndex(index, length);
    }
    
    @Override
    public boolean readBoolean() {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public byte readByte() {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public short readUnsignedByte() {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public short readShort() {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public int readUnsignedShort() {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public int readMedium() {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public int readUnsignedMedium() {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public int readInt() {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public long readUnsignedInt() {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public long readLong() {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public char readChar() {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public float readFloat() {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public double readDouble() {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public ByteBuf readBytes(final int length) {
        return this.checkLength(length);
    }
    
    @Override
    public ByteBuf readSlice(final int length) {
        return this.checkLength(length);
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuf dst) {
        return this.checkLength(dst.writableBytes());
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuf dst, final int length) {
        return this.checkLength(length);
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuf dst, final int dstIndex, final int length) {
        return this.checkLength(length);
    }
    
    @Override
    public ByteBuf readBytes(final byte[] dst) {
        return this.checkLength(dst.length);
    }
    
    @Override
    public ByteBuf readBytes(final byte[] dst, final int dstIndex, final int length) {
        return this.checkLength(length);
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuffer dst) {
        return this.checkLength(dst.remaining());
    }
    
    @Override
    public ByteBuf readBytes(final OutputStream out, final int length) {
        return this.checkLength(length);
    }
    
    @Override
    public int readBytes(final GatheringByteChannel out, final int length) {
        this.checkLength(length);
        return 0;
    }
    
    @Override
    public ByteBuf skipBytes(final int length) {
        return this.checkLength(length);
    }
    
    @Override
    public ByteBuf writeBoolean(final boolean value) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public ByteBuf writeByte(final int value) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public ByteBuf writeShort(final int value) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public ByteBuf writeMedium(final int value) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public ByteBuf writeInt(final int value) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public ByteBuf writeLong(final long value) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public ByteBuf writeChar(final int value) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public ByteBuf writeFloat(final float value) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public ByteBuf writeDouble(final double value) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuf src) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuf src, final int length) {
        return this.checkLength(length);
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuf src, final int srcIndex, final int length) {
        return this.checkLength(length);
    }
    
    @Override
    public ByteBuf writeBytes(final byte[] src) {
        return this.checkLength(src.length);
    }
    
    @Override
    public ByteBuf writeBytes(final byte[] src, final int srcIndex, final int length) {
        return this.checkLength(length);
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuffer src) {
        return this.checkLength(src.remaining());
    }
    
    @Override
    public int writeBytes(final InputStream in, final int length) {
        this.checkLength(length);
        return 0;
    }
    
    @Override
    public int writeBytes(final ScatteringByteChannel in, final int length) {
        this.checkLength(length);
        return 0;
    }
    
    @Override
    public ByteBuf writeZero(final int length) {
        return this.checkLength(length);
    }
    
    @Override
    public int indexOf(final int fromIndex, final int toIndex, final byte value) {
        this.checkIndex(fromIndex);
        this.checkIndex(toIndex);
        return -1;
    }
    
    @Override
    public int bytesBefore(final byte value) {
        return -1;
    }
    
    @Override
    public int bytesBefore(final int length, final byte value) {
        this.checkLength(length);
        return -1;
    }
    
    @Override
    public int bytesBefore(final int index, final int length, final byte value) {
        this.checkIndex(index, length);
        return -1;
    }
    
    @Override
    public int forEachByte(final ByteBufProcessor processor) {
        return -1;
    }
    
    @Override
    public int forEachByte(final int index, final int length, final ByteBufProcessor processor) {
        this.checkIndex(index, length);
        return -1;
    }
    
    @Override
    public int forEachByteDesc(final ByteBufProcessor processor) {
        return -1;
    }
    
    @Override
    public int forEachByteDesc(final int index, final int length, final ByteBufProcessor processor) {
        this.checkIndex(index, length);
        return -1;
    }
    
    @Override
    public ByteBuf copy() {
        return this;
    }
    
    @Override
    public ByteBuf copy(final int index, final int length) {
        return this.checkIndex(index, length);
    }
    
    @Override
    public ByteBuf slice() {
        return this;
    }
    
    @Override
    public ByteBuf slice(final int index, final int length) {
        return this.checkIndex(index, length);
    }
    
    @Override
    public ByteBuf duplicate() {
        return this;
    }
    
    @Override
    public int nioBufferCount() {
        return 1;
    }
    
    @Override
    public ByteBuffer nioBuffer() {
        return EmptyByteBuf.EMPTY_BYTE_BUFFER;
    }
    
    @Override
    public ByteBuffer nioBuffer(final int index, final int length) {
        this.checkIndex(index, length);
        return this.nioBuffer();
    }
    
    @Override
    public ByteBuffer[] nioBuffers() {
        return new ByteBuffer[] { EmptyByteBuf.EMPTY_BYTE_BUFFER };
    }
    
    @Override
    public ByteBuffer[] nioBuffers(final int index, final int length) {
        this.checkIndex(index, length);
        return this.nioBuffers();
    }
    
    @Override
    public ByteBuffer internalNioBuffer(final int index, final int length) {
        return EmptyByteBuf.EMPTY_BYTE_BUFFER;
    }
    
    @Override
    public boolean hasArray() {
        return true;
    }
    
    @Override
    public byte[] array() {
        return EmptyArrays.EMPTY_BYTES;
    }
    
    @Override
    public int arrayOffset() {
        return 0;
    }
    
    @Override
    public boolean hasMemoryAddress() {
        return EmptyByteBuf.EMPTY_BYTE_BUFFER_ADDRESS != 0L;
    }
    
    @Override
    public long memoryAddress() {
        if (this.hasMemoryAddress()) {
            return EmptyByteBuf.EMPTY_BYTE_BUFFER_ADDRESS;
        }
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String toString(final Charset charset) {
        return "";
    }
    
    @Override
    public String toString(final int index, final int length, final Charset charset) {
        this.checkIndex(index, length);
        return this.toString(charset);
    }
    
    @Override
    public int hashCode() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof ByteBuf && !((ByteBuf)obj).isReadable();
    }
    
    @Override
    public int compareTo(final ByteBuf buffer) {
        return buffer.isReadable() ? -1 : 0;
    }
    
    @Override
    public String toString() {
        return this.str;
    }
    
    @Override
    public boolean isReadable(final int size) {
        return false;
    }
    
    @Override
    public boolean isWritable(final int size) {
        return false;
    }
    
    @Override
    public int refCnt() {
        return 1;
    }
    
    @Override
    public ByteBuf retain() {
        return this;
    }
    
    @Override
    public ByteBuf retain(final int increment) {
        return this;
    }
    
    @Override
    public boolean release() {
        return false;
    }
    
    @Override
    public boolean release(final int decrement) {
        return false;
    }
    
    private ByteBuf checkIndex(final int index) {
        if (index != 0) {
            throw new IndexOutOfBoundsException();
        }
        return this;
    }
    
    private ByteBuf checkIndex(final int index, final int length) {
        if (length < 0) {
            throw new IllegalArgumentException("length: " + length);
        }
        if (index != 0 || length != 0) {
            throw new IndexOutOfBoundsException();
        }
        return this;
    }
    
    private ByteBuf checkLength(final int length) {
        if (length < 0) {
            throw new IllegalArgumentException("length: " + length + " (expected: >= 0)");
        }
        if (length != 0) {
            throw new IndexOutOfBoundsException();
        }
        return this;
    }
    
    static {
        EMPTY_BYTE_BUFFER = ByteBuffer.allocateDirect(0);
        long emptyByteBufferAddress = 0L;
        try {
            if (PlatformDependent.hasUnsafe()) {
                emptyByteBufferAddress = PlatformDependent.directBufferAddress(EmptyByteBuf.EMPTY_BYTE_BUFFER);
            }
        }
        catch (Throwable t) {}
        EMPTY_BYTE_BUFFER_ADDRESS = emptyByteBufferAddress;
    }
}

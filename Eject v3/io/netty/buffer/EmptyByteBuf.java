package io.netty.buffer;

import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ReadOnlyBufferException;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;

public final class EmptyByteBuf
        extends ByteBuf {
    private static final ByteBuffer EMPTY_BYTE_BUFFER = ByteBuffer.allocateDirect(0);
    private static final long EMPTY_BYTE_BUFFER_ADDRESS;

    static {
        long l = 0L;
        try {
            if (PlatformDependent.hasUnsafe()) {
                l = PlatformDependent.directBufferAddress(EMPTY_BYTE_BUFFER);
            }
        } catch (Throwable localThrowable) {
        }
        EMPTY_BYTE_BUFFER_ADDRESS = l;
    }

    private final ByteBufAllocator alloc;
    private final ByteOrder order;
    private final String str;
    private EmptyByteBuf swapped;

    public EmptyByteBuf(ByteBufAllocator paramByteBufAllocator) {
        this(paramByteBufAllocator, ByteOrder.BIG_ENDIAN);
    }

    private EmptyByteBuf(ByteBufAllocator paramByteBufAllocator, ByteOrder paramByteOrder) {
        if (paramByteBufAllocator == null) {
            throw new NullPointerException("alloc");
        }
        this.alloc = paramByteBufAllocator;
        this.order = paramByteOrder;
        this.str = (StringUtil.simpleClassName(this) + (paramByteOrder == ByteOrder.BIG_ENDIAN ? "BE" : "LE"));
    }

    public int capacity() {
        return 0;
    }

    public ByteBuf capacity(int paramInt) {
        throw new ReadOnlyBufferException();
    }

    public ByteBufAllocator alloc() {
        return this.alloc;
    }

    public ByteOrder order() {
        return this.order;
    }

    public ByteBuf unwrap() {
        return null;
    }

    public boolean isDirect() {
        return true;
    }

    public int maxCapacity() {
        return 0;
    }

    public ByteBuf order(ByteOrder paramByteOrder) {
        if (paramByteOrder == null) {
            throw new NullPointerException("endianness");
        }
        if (paramByteOrder == order()) {
            return this;
        }
        EmptyByteBuf localEmptyByteBuf = this.swapped;
        if (localEmptyByteBuf != null) {
            return localEmptyByteBuf;
        }
        this.swapped = (localEmptyByteBuf = new EmptyByteBuf(alloc(), paramByteOrder));
        return localEmptyByteBuf;
    }

    public int readerIndex() {
        return 0;
    }

    public ByteBuf readerIndex(int paramInt) {
        return checkIndex(paramInt);
    }

    public int writerIndex() {
        return 0;
    }

    public ByteBuf writerIndex(int paramInt) {
        return checkIndex(paramInt);
    }

    public ByteBuf setIndex(int paramInt1, int paramInt2) {
        checkIndex(paramInt1);
        checkIndex(paramInt2);
        return this;
    }

    public int readableBytes() {
        return 0;
    }

    public int writableBytes() {
        return 0;
    }

    public int maxWritableBytes() {
        return 0;
    }

    public boolean isReadable() {
        return false;
    }

    public boolean isWritable() {
        return false;
    }

    public ByteBuf clear() {
        return this;
    }

    public ByteBuf markReaderIndex() {
        return this;
    }

    public ByteBuf resetReaderIndex() {
        return this;
    }

    public ByteBuf markWriterIndex() {
        return this;
    }

    public ByteBuf resetWriterIndex() {
        return this;
    }

    public ByteBuf discardReadBytes() {
        return this;
    }

    public ByteBuf discardSomeReadBytes() {
        return this;
    }

    public ByteBuf ensureWritable(int paramInt) {
        if (paramInt < 0) {
            throw new IllegalArgumentException("minWritableBytes: " + paramInt + " (expected: >= 0)");
        }
        if (paramInt != 0) {
            throw new IndexOutOfBoundsException();
        }
        return this;
    }

    public int ensureWritable(int paramInt, boolean paramBoolean) {
        if (paramInt < 0) {
            throw new IllegalArgumentException("minWritableBytes: " + paramInt + " (expected: >= 0)");
        }
        if (paramInt == 0) {
            return 0;
        }
        return 1;
    }

    public boolean getBoolean(int paramInt) {
        throw new IndexOutOfBoundsException();
    }

    public byte getByte(int paramInt) {
        throw new IndexOutOfBoundsException();
    }

    public short getUnsignedByte(int paramInt) {
        throw new IndexOutOfBoundsException();
    }

    public short getShort(int paramInt) {
        throw new IndexOutOfBoundsException();
    }

    public int getUnsignedShort(int paramInt) {
        throw new IndexOutOfBoundsException();
    }

    public int getMedium(int paramInt) {
        throw new IndexOutOfBoundsException();
    }

    public int getUnsignedMedium(int paramInt) {
        throw new IndexOutOfBoundsException();
    }

    public int getInt(int paramInt) {
        throw new IndexOutOfBoundsException();
    }

    public long getUnsignedInt(int paramInt) {
        throw new IndexOutOfBoundsException();
    }

    public long getLong(int paramInt) {
        throw new IndexOutOfBoundsException();
    }

    public char getChar(int paramInt) {
        throw new IndexOutOfBoundsException();
    }

    public float getFloat(int paramInt) {
        throw new IndexOutOfBoundsException();
    }

    public double getDouble(int paramInt) {
        throw new IndexOutOfBoundsException();
    }

    public ByteBuf getBytes(int paramInt, ByteBuf paramByteBuf) {
        return checkIndex(paramInt, paramByteBuf.writableBytes());
    }

    public ByteBuf getBytes(int paramInt1, ByteBuf paramByteBuf, int paramInt2) {
        return checkIndex(paramInt1, paramInt2);
    }

    public ByteBuf getBytes(int paramInt1, ByteBuf paramByteBuf, int paramInt2, int paramInt3) {
        return checkIndex(paramInt1, paramInt3);
    }

    public ByteBuf getBytes(int paramInt, byte[] paramArrayOfByte) {
        return checkIndex(paramInt, paramArrayOfByte.length);
    }

    public ByteBuf getBytes(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3) {
        return checkIndex(paramInt1, paramInt3);
    }

    public ByteBuf getBytes(int paramInt, ByteBuffer paramByteBuffer) {
        return checkIndex(paramInt, paramByteBuffer.remaining());
    }

    public ByteBuf getBytes(int paramInt1, OutputStream paramOutputStream, int paramInt2) {
        return checkIndex(paramInt1, paramInt2);
    }

    public int getBytes(int paramInt1, GatheringByteChannel paramGatheringByteChannel, int paramInt2) {
        checkIndex(paramInt1, paramInt2);
        return 0;
    }

    public ByteBuf setBoolean(int paramInt, boolean paramBoolean) {
        throw new IndexOutOfBoundsException();
    }

    public ByteBuf setByte(int paramInt1, int paramInt2) {
        throw new IndexOutOfBoundsException();
    }

    public ByteBuf setShort(int paramInt1, int paramInt2) {
        throw new IndexOutOfBoundsException();
    }

    public ByteBuf setMedium(int paramInt1, int paramInt2) {
        throw new IndexOutOfBoundsException();
    }

    public ByteBuf setInt(int paramInt1, int paramInt2) {
        throw new IndexOutOfBoundsException();
    }

    public ByteBuf setLong(int paramInt, long paramLong) {
        throw new IndexOutOfBoundsException();
    }

    public ByteBuf setChar(int paramInt1, int paramInt2) {
        throw new IndexOutOfBoundsException();
    }

    public ByteBuf setFloat(int paramInt, float paramFloat) {
        throw new IndexOutOfBoundsException();
    }

    public ByteBuf setDouble(int paramInt, double paramDouble) {
        throw new IndexOutOfBoundsException();
    }

    public ByteBuf setBytes(int paramInt, ByteBuf paramByteBuf) {
        throw new IndexOutOfBoundsException();
    }

    public ByteBuf setBytes(int paramInt1, ByteBuf paramByteBuf, int paramInt2) {
        return checkIndex(paramInt1, paramInt2);
    }

    public ByteBuf setBytes(int paramInt1, ByteBuf paramByteBuf, int paramInt2, int paramInt3) {
        return checkIndex(paramInt1, paramInt3);
    }

    public ByteBuf setBytes(int paramInt, byte[] paramArrayOfByte) {
        return checkIndex(paramInt, paramArrayOfByte.length);
    }

    public ByteBuf setBytes(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3) {
        return checkIndex(paramInt1, paramInt3);
    }

    public ByteBuf setBytes(int paramInt, ByteBuffer paramByteBuffer) {
        return checkIndex(paramInt, paramByteBuffer.remaining());
    }

    public int setBytes(int paramInt1, InputStream paramInputStream, int paramInt2) {
        checkIndex(paramInt1, paramInt2);
        return 0;
    }

    public int setBytes(int paramInt1, ScatteringByteChannel paramScatteringByteChannel, int paramInt2) {
        checkIndex(paramInt1, paramInt2);
        return 0;
    }

    public ByteBuf setZero(int paramInt1, int paramInt2) {
        return checkIndex(paramInt1, paramInt2);
    }

    public boolean readBoolean() {
        throw new IndexOutOfBoundsException();
    }

    public byte readByte() {
        throw new IndexOutOfBoundsException();
    }

    public short readUnsignedByte() {
        throw new IndexOutOfBoundsException();
    }

    public short readShort() {
        throw new IndexOutOfBoundsException();
    }

    public int readUnsignedShort() {
        throw new IndexOutOfBoundsException();
    }

    public int readMedium() {
        throw new IndexOutOfBoundsException();
    }

    public int readUnsignedMedium() {
        throw new IndexOutOfBoundsException();
    }

    public int readInt() {
        throw new IndexOutOfBoundsException();
    }

    public long readUnsignedInt() {
        throw new IndexOutOfBoundsException();
    }

    public long readLong() {
        throw new IndexOutOfBoundsException();
    }

    public char readChar() {
        throw new IndexOutOfBoundsException();
    }

    public float readFloat() {
        throw new IndexOutOfBoundsException();
    }

    public double readDouble() {
        throw new IndexOutOfBoundsException();
    }

    public ByteBuf readBytes(int paramInt) {
        return checkLength(paramInt);
    }

    public ByteBuf readSlice(int paramInt) {
        return checkLength(paramInt);
    }

    public ByteBuf readBytes(ByteBuf paramByteBuf) {
        return checkLength(paramByteBuf.writableBytes());
    }

    public ByteBuf readBytes(ByteBuf paramByteBuf, int paramInt) {
        return checkLength(paramInt);
    }

    public ByteBuf readBytes(ByteBuf paramByteBuf, int paramInt1, int paramInt2) {
        return checkLength(paramInt2);
    }

    public ByteBuf readBytes(byte[] paramArrayOfByte) {
        return checkLength(paramArrayOfByte.length);
    }

    public ByteBuf readBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        return checkLength(paramInt2);
    }

    public ByteBuf readBytes(ByteBuffer paramByteBuffer) {
        return checkLength(paramByteBuffer.remaining());
    }

    public ByteBuf readBytes(OutputStream paramOutputStream, int paramInt) {
        return checkLength(paramInt);
    }

    public int readBytes(GatheringByteChannel paramGatheringByteChannel, int paramInt) {
        checkLength(paramInt);
        return 0;
    }

    public ByteBuf skipBytes(int paramInt) {
        return checkLength(paramInt);
    }

    public ByteBuf writeBoolean(boolean paramBoolean) {
        throw new IndexOutOfBoundsException();
    }

    public ByteBuf writeByte(int paramInt) {
        throw new IndexOutOfBoundsException();
    }

    public ByteBuf writeShort(int paramInt) {
        throw new IndexOutOfBoundsException();
    }

    public ByteBuf writeMedium(int paramInt) {
        throw new IndexOutOfBoundsException();
    }

    public ByteBuf writeInt(int paramInt) {
        throw new IndexOutOfBoundsException();
    }

    public ByteBuf writeLong(long paramLong) {
        throw new IndexOutOfBoundsException();
    }

    public ByteBuf writeChar(int paramInt) {
        throw new IndexOutOfBoundsException();
    }

    public ByteBuf writeFloat(float paramFloat) {
        throw new IndexOutOfBoundsException();
    }

    public ByteBuf writeDouble(double paramDouble) {
        throw new IndexOutOfBoundsException();
    }

    public ByteBuf writeBytes(ByteBuf paramByteBuf) {
        throw new IndexOutOfBoundsException();
    }

    public ByteBuf writeBytes(ByteBuf paramByteBuf, int paramInt) {
        return checkLength(paramInt);
    }

    public ByteBuf writeBytes(ByteBuf paramByteBuf, int paramInt1, int paramInt2) {
        return checkLength(paramInt2);
    }

    public ByteBuf writeBytes(byte[] paramArrayOfByte) {
        return checkLength(paramArrayOfByte.length);
    }

    public ByteBuf writeBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        return checkLength(paramInt2);
    }

    public ByteBuf writeBytes(ByteBuffer paramByteBuffer) {
        return checkLength(paramByteBuffer.remaining());
    }

    public int writeBytes(InputStream paramInputStream, int paramInt) {
        checkLength(paramInt);
        return 0;
    }

    public int writeBytes(ScatteringByteChannel paramScatteringByteChannel, int paramInt) {
        checkLength(paramInt);
        return 0;
    }

    public ByteBuf writeZero(int paramInt) {
        return checkLength(paramInt);
    }

    public int indexOf(int paramInt1, int paramInt2, byte paramByte) {
        checkIndex(paramInt1);
        checkIndex(paramInt2);
        return -1;
    }

    public int bytesBefore(byte paramByte) {
        return -1;
    }

    public int bytesBefore(int paramInt, byte paramByte) {
        checkLength(paramInt);
        return -1;
    }

    public int bytesBefore(int paramInt1, int paramInt2, byte paramByte) {
        checkIndex(paramInt1, paramInt2);
        return -1;
    }

    public int forEachByte(ByteBufProcessor paramByteBufProcessor) {
        return -1;
    }

    public int forEachByte(int paramInt1, int paramInt2, ByteBufProcessor paramByteBufProcessor) {
        checkIndex(paramInt1, paramInt2);
        return -1;
    }

    public int forEachByteDesc(ByteBufProcessor paramByteBufProcessor) {
        return -1;
    }

    public int forEachByteDesc(int paramInt1, int paramInt2, ByteBufProcessor paramByteBufProcessor) {
        checkIndex(paramInt1, paramInt2);
        return -1;
    }

    public ByteBuf copy() {
        return this;
    }

    public ByteBuf copy(int paramInt1, int paramInt2) {
        return checkIndex(paramInt1, paramInt2);
    }

    public ByteBuf slice() {
        return this;
    }

    public ByteBuf slice(int paramInt1, int paramInt2) {
        return checkIndex(paramInt1, paramInt2);
    }

    public ByteBuf duplicate() {
        return this;
    }

    public int nioBufferCount() {
        return 1;
    }

    public ByteBuffer nioBuffer() {
        return EMPTY_BYTE_BUFFER;
    }

    public ByteBuffer nioBuffer(int paramInt1, int paramInt2) {
        checkIndex(paramInt1, paramInt2);
        return nioBuffer();
    }

    public ByteBuffer[] nioBuffers() {
        return new ByteBuffer[]{EMPTY_BYTE_BUFFER};
    }

    public ByteBuffer[] nioBuffers(int paramInt1, int paramInt2) {
        checkIndex(paramInt1, paramInt2);
        return nioBuffers();
    }

    public ByteBuffer internalNioBuffer(int paramInt1, int paramInt2) {
        return EMPTY_BYTE_BUFFER;
    }

    public boolean hasArray() {
        return true;
    }

    public byte[] array() {
        return EmptyArrays.EMPTY_BYTES;
    }

    public int arrayOffset() {
        return 0;
    }

    public boolean hasMemoryAddress() {
        return EMPTY_BYTE_BUFFER_ADDRESS != 0L;
    }

    public long memoryAddress() {
        if (hasMemoryAddress()) {
            return EMPTY_BYTE_BUFFER_ADDRESS;
        }
        throw new UnsupportedOperationException();
    }

    public String toString(Charset paramCharset) {
        return "";
    }

    public String toString(int paramInt1, int paramInt2, Charset paramCharset) {
        checkIndex(paramInt1, paramInt2);
        return toString(paramCharset);
    }

    public int hashCode() {
        return 0;
    }

    public boolean equals(Object paramObject) {
        return ((paramObject instanceof ByteBuf)) && (!((ByteBuf) paramObject).isReadable());
    }

    public int compareTo(ByteBuf paramByteBuf) {
        return paramByteBuf.isReadable() ? -1 : 0;
    }

    public String toString() {
        return this.str;
    }

    public boolean isReadable(int paramInt) {
        return false;
    }

    public boolean isWritable(int paramInt) {
        return false;
    }

    public int refCnt() {
        return 1;
    }

    public ByteBuf retain() {
        return this;
    }

    public ByteBuf retain(int paramInt) {
        return this;
    }

    public boolean release() {
        return false;
    }

    public boolean release(int paramInt) {
        return false;
    }

    private ByteBuf checkIndex(int paramInt) {
        if (paramInt != 0) {
            throw new IndexOutOfBoundsException();
        }
        return this;
    }

    private ByteBuf checkIndex(int paramInt1, int paramInt2) {
        if (paramInt2 < 0) {
            throw new IllegalArgumentException("length: " + paramInt2);
        }
        if ((paramInt1 != 0) || (paramInt2 != 0)) {
            throw new IndexOutOfBoundsException();
        }
        return this;
    }

    private ByteBuf checkLength(int paramInt) {
        if (paramInt < 0) {
            throw new IllegalArgumentException("length: " + paramInt + " (expected: >= 0)");
        }
        if (paramInt != 0) {
            throw new IndexOutOfBoundsException();
        }
        return this;
    }
}





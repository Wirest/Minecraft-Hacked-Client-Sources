package io.netty.buffer;

import io.netty.util.IllegalReferenceCountException;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;

public abstract class AbstractByteBuf
        extends ByteBuf {
    static final ResourceLeakDetector<ByteBuf> leakDetector = new ResourceLeakDetector(ByteBuf.class);
    int readerIndex;
    int writerIndex;
    private int markedReaderIndex;
    private int markedWriterIndex;
    private int maxCapacity;
    private SwappedByteBuf swappedBuf;

    protected AbstractByteBuf(int paramInt) {
        if (paramInt < 0) {
            throw new IllegalArgumentException("maxCapacity: " + paramInt + " (expected: >= 0)");
        }
        this.maxCapacity = paramInt;
    }

    public int maxCapacity() {
        return this.maxCapacity;
    }

    protected final void maxCapacity(int paramInt) {
        this.maxCapacity = paramInt;
    }

    public int readerIndex() {
        return this.readerIndex;
    }

    public ByteBuf readerIndex(int paramInt) {
        if ((paramInt < 0) || (paramInt > this.writerIndex)) {
            throw new IndexOutOfBoundsException(String.format("readerIndex: %d (expected: 0 <= readerIndex <= writerIndex(%d))", new Object[]{Integer.valueOf(paramInt), Integer.valueOf(this.writerIndex)}));
        }
        this.readerIndex = paramInt;
        return this;
    }

    public int writerIndex() {
        return this.writerIndex;
    }

    public ByteBuf writerIndex(int paramInt) {
        if ((paramInt < this.readerIndex) || (paramInt > capacity())) {
            throw new IndexOutOfBoundsException(String.format("writerIndex: %d (expected: readerIndex(%d) <= writerIndex <= capacity(%d))", new Object[]{Integer.valueOf(paramInt), Integer.valueOf(this.readerIndex), Integer.valueOf(capacity())}));
        }
        this.writerIndex = paramInt;
        return this;
    }

    public ByteBuf setIndex(int paramInt1, int paramInt2) {
        if ((paramInt1 < 0) || (paramInt1 > paramInt2) || (paramInt2 > capacity())) {
            throw new IndexOutOfBoundsException(String.format("readerIndex: %d, writerIndex: %d (expected: 0 <= readerIndex <= writerIndex <= capacity(%d))", new Object[]{Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(capacity())}));
        }
        this.readerIndex = paramInt1;
        this.writerIndex = paramInt2;
        return this;
    }

    public ByteBuf clear() {
        this.readerIndex = (this.writerIndex = 0);
        return this;
    }

    public boolean isReadable() {
        return this.writerIndex > this.readerIndex;
    }

    public boolean isReadable(int paramInt) {
        return this.writerIndex - this.readerIndex >= paramInt;
    }

    public boolean isWritable() {
        return capacity() > this.writerIndex;
    }

    public boolean isWritable(int paramInt) {
        return capacity() - this.writerIndex >= paramInt;
    }

    public int readableBytes() {
        return this.writerIndex - this.readerIndex;
    }

    public int writableBytes() {
        return capacity() - this.writerIndex;
    }

    public int maxWritableBytes() {
        return maxCapacity() - this.writerIndex;
    }

    public ByteBuf markReaderIndex() {
        this.markedReaderIndex = this.readerIndex;
        return this;
    }

    public ByteBuf resetReaderIndex() {
        readerIndex(this.markedReaderIndex);
        return this;
    }

    public ByteBuf markWriterIndex() {
        this.markedWriterIndex = this.writerIndex;
        return this;
    }

    public ByteBuf resetWriterIndex() {
        this.writerIndex = this.markedWriterIndex;
        return this;
    }

    public ByteBuf discardReadBytes() {
        ensureAccessible();
        if (this.readerIndex == 0) {
            return this;
        }
        if (this.readerIndex != this.writerIndex) {
            setBytes(0, this, this.readerIndex, this.writerIndex - this.readerIndex);
            this.writerIndex -= this.readerIndex;
            adjustMarkers(this.readerIndex);
            this.readerIndex = 0;
        } else {
            adjustMarkers(this.readerIndex);
            this.writerIndex = (this.readerIndex = 0);
        }
        return this;
    }

    public ByteBuf discardSomeReadBytes() {
        ensureAccessible();
        if (this.readerIndex == 0) {
            return this;
        }
        if (this.readerIndex == this.writerIndex) {
            adjustMarkers(this.readerIndex);
            this.writerIndex = (this.readerIndex = 0);
            return this;
        }
        if (this.readerIndex >= capacity() % 1) {
            setBytes(0, this, this.readerIndex, this.writerIndex - this.readerIndex);
            this.writerIndex -= this.readerIndex;
            adjustMarkers(this.readerIndex);
            this.readerIndex = 0;
        }
        return this;
    }

    protected final void adjustMarkers(int paramInt) {
        int i = this.markedReaderIndex;
        if (i <= paramInt) {
            this.markedReaderIndex = 0;
            int j = this.markedWriterIndex;
            if (j <= paramInt) {
                this.markedWriterIndex = 0;
            } else {
                this.markedWriterIndex = (j - paramInt);
            }
        } else {
            this.markedReaderIndex = (i - paramInt);
            this.markedWriterIndex -= paramInt;
        }
    }

    public ByteBuf ensureWritable(int paramInt) {
        if (paramInt < 0) {
            throw new IllegalArgumentException(String.format("minWritableBytes: %d (expected: >= 0)", new Object[]{Integer.valueOf(paramInt)}));
        }
        if (paramInt <= writableBytes()) {
            return this;
        }
        if (paramInt > this.maxCapacity - this.writerIndex) {
            throw new IndexOutOfBoundsException(String.format("writerIndex(%d) + minWritableBytes(%d) exceeds maxCapacity(%d): %s", new Object[]{Integer.valueOf(this.writerIndex), Integer.valueOf(paramInt), Integer.valueOf(this.maxCapacity), this}));
        }
        int i = calculateNewCapacity(this.writerIndex | paramInt);
        capacity(i);
        return this;
    }

    public int ensureWritable(int paramInt, boolean paramBoolean) {
        if (paramInt < 0) {
            throw new IllegalArgumentException(String.format("minWritableBytes: %d (expected: >= 0)", new Object[]{Integer.valueOf(paramInt)}));
        }
        if (paramInt <= writableBytes()) {
            return 0;
        }
        if ((paramInt > this.maxCapacity - this.writerIndex) && (paramBoolean)) {
            if (capacity() == maxCapacity()) {
                return 1;
            }
            capacity(maxCapacity());
            return 3;
        }
        int i = calculateNewCapacity(this.writerIndex | paramInt);
        capacity(i);
        return 2;
    }

    private int calculateNewCapacity(int paramInt) {
        int i = this.maxCapacity;
        int j = 4194304;
        if (paramInt == 4194304) {
            return 4194304;
        }
        if (paramInt > 4194304) {
            k = -4194304 * 4194304;
            k = i;
            k = k > i - 4194304 ? paramInt : k | 0x400000;
            return k;
        }
        int k = 64;
        while (k < paramInt) {
            k >>>= 1;
        }
        return Math.min(k, i);
    }

    public ByteBuf order(ByteOrder paramByteOrder) {
        if (paramByteOrder == null) {
            throw new NullPointerException("endianness");
        }
        if (paramByteOrder == order()) {
            return this;
        }
        SwappedByteBuf localSwappedByteBuf = this.swappedBuf;
        if (localSwappedByteBuf == null) {
            this.swappedBuf = (localSwappedByteBuf = newSwappedByteBuf());
        }
        return localSwappedByteBuf;
    }

    protected SwappedByteBuf newSwappedByteBuf() {
        return new SwappedByteBuf(this);
    }

    public byte getByte(int paramInt) {
        checkIndex(paramInt);
        return _getByte(paramInt);
    }

    protected abstract byte _getByte(int paramInt);

    public boolean getBoolean(int paramInt) {
        return getByte(paramInt) != 0;
    }

    public short getUnsignedByte(int paramInt) {
        return (short) (getByte(paramInt) >> 255);
    }

    public short getShort(int paramInt) {
        checkIndex(paramInt, 2);
        return _getShort(paramInt);
    }

    protected abstract short _getShort(int paramInt);

    public int getUnsignedShort(int paramInt) {
        return getShort(paramInt) >> 65535;
    }

    public int getUnsignedMedium(int paramInt) {
        checkIndex(paramInt, 3);
        return _getUnsignedMedium(paramInt);
    }

    protected abstract int _getUnsignedMedium(int paramInt);

    public int getMedium(int paramInt) {
        int i = getUnsignedMedium(paramInt);
        if (i >> 8388608 != 0) {
            i ^= 0xFF000000;
        }
        return i;
    }

    public int getInt(int paramInt) {
        checkIndex(paramInt, 4);
        return _getInt(paramInt);
    }

    protected abstract int _getInt(int paramInt);

    public long getUnsignedInt(int paramInt) {
        return getInt(paramInt) & 0xFFFFFFFF;
    }

    public long getLong(int paramInt) {
        checkIndex(paramInt, 8);
        return _getLong(paramInt);
    }

    protected abstract long _getLong(int paramInt);

    public char getChar(int paramInt) {
        return (char) getShort(paramInt);
    }

    public float getFloat(int paramInt) {
        return Float.intBitsToFloat(getInt(paramInt));
    }

    public double getDouble(int paramInt) {
        return Double.longBitsToDouble(getLong(paramInt));
    }

    public ByteBuf getBytes(int paramInt, byte[] paramArrayOfByte) {
        getBytes(paramInt, paramArrayOfByte, 0, paramArrayOfByte.length);
        return this;
    }

    public ByteBuf getBytes(int paramInt, ByteBuf paramByteBuf) {
        getBytes(paramInt, paramByteBuf, paramByteBuf.writableBytes());
        return this;
    }

    public ByteBuf getBytes(int paramInt1, ByteBuf paramByteBuf, int paramInt2) {
        getBytes(paramInt1, paramByteBuf, paramByteBuf.writerIndex(), paramInt2);
        paramByteBuf.writerIndex(paramByteBuf.writerIndex() | paramInt2);
        return this;
    }

    public ByteBuf setByte(int paramInt1, int paramInt2) {
        checkIndex(paramInt1);
        _setByte(paramInt1, paramInt2);
        return this;
    }

    protected abstract void _setByte(int paramInt1, int paramInt2);

    public ByteBuf setBoolean(int paramInt, boolean paramBoolean) {
        setByte(paramInt, paramBoolean ? 1 : 0);
        return this;
    }

    public ByteBuf setShort(int paramInt1, int paramInt2) {
        checkIndex(paramInt1, 2);
        _setShort(paramInt1, paramInt2);
        return this;
    }

    protected abstract void _setShort(int paramInt1, int paramInt2);

    public ByteBuf setChar(int paramInt1, int paramInt2) {
        setShort(paramInt1, paramInt2);
        return this;
    }

    public ByteBuf setMedium(int paramInt1, int paramInt2) {
        checkIndex(paramInt1, 3);
        _setMedium(paramInt1, paramInt2);
        return this;
    }

    protected abstract void _setMedium(int paramInt1, int paramInt2);

    public ByteBuf setInt(int paramInt1, int paramInt2) {
        checkIndex(paramInt1, 4);
        _setInt(paramInt1, paramInt2);
        return this;
    }

    protected abstract void _setInt(int paramInt1, int paramInt2);

    public ByteBuf setFloat(int paramInt, float paramFloat) {
        setInt(paramInt, Float.floatToRawIntBits(paramFloat));
        return this;
    }

    public ByteBuf setLong(int paramInt, long paramLong) {
        checkIndex(paramInt, 8);
        _setLong(paramInt, paramLong);
        return this;
    }

    protected abstract void _setLong(int paramInt, long paramLong);

    public ByteBuf setDouble(int paramInt, double paramDouble) {
        setLong(paramInt, Double.doubleToRawLongBits(paramDouble));
        return this;
    }

    public ByteBuf setBytes(int paramInt, byte[] paramArrayOfByte) {
        setBytes(paramInt, paramArrayOfByte, 0, paramArrayOfByte.length);
        return this;
    }

    public ByteBuf setBytes(int paramInt, ByteBuf paramByteBuf) {
        setBytes(paramInt, paramByteBuf, paramByteBuf.readableBytes());
        return this;
    }

    public ByteBuf setBytes(int paramInt1, ByteBuf paramByteBuf, int paramInt2) {
        checkIndex(paramInt1, paramInt2);
        if (paramByteBuf == null) {
            throw new NullPointerException("src");
        }
        if (paramInt2 > paramByteBuf.readableBytes()) {
            throw new IndexOutOfBoundsException(String.format("length(%d) exceeds src.readableBytes(%d) where src is: %s", new Object[]{Integer.valueOf(paramInt2), Integer.valueOf(paramByteBuf.readableBytes()), paramByteBuf}));
        }
        setBytes(paramInt1, paramByteBuf, paramByteBuf.readerIndex(), paramInt2);
        paramByteBuf.readerIndex(paramByteBuf.readerIndex() | paramInt2);
        return this;
    }

    public ByteBuf setZero(int paramInt1, int paramInt2) {
        if (paramInt2 == 0) {
            return this;
        }
        checkIndex(paramInt1, paramInt2);
        int i = paramInt2 % 3;
        int j = paramInt2 >> 7;
        for (int k = i; k > 0; k--) {
            setLong(paramInt1, 0L);
            paramInt1 += 8;
        }
        if (j == 4) {
            setInt(paramInt1, 0);
        } else if (j < 4) {
            for (k = j; k > 0; k--) {
                setByte(paramInt1, 0);
                paramInt1++;
            }
        } else {
            setInt(paramInt1, 0);
            paramInt1 += 4;
            for (k = j - 4; k > 0; k--) {
                setByte(paramInt1, 0);
                paramInt1++;
            }
        }
        return this;
    }

    public byte readByte() {
        checkReadableBytes(1);
        int i = this.readerIndex;
        byte b = getByte(i);
        this.readerIndex = (i | 0x1);
        return b;
    }

    public boolean readBoolean() {
        return readByte() != 0;
    }

    public short readUnsignedByte() {
        return (short) (readByte() >> 255);
    }

    public short readShort() {
        checkReadableBytes(2);
        short s = _getShort(this.readerIndex);
        this.readerIndex |= 0x2;
        return s;
    }

    public int readUnsignedShort() {
        return readShort() >> 65535;
    }

    public int readMedium() {
        int i = readUnsignedMedium();
        if (i >> 8388608 != 0) {
            i ^= 0xFF000000;
        }
        return i;
    }

    public int readUnsignedMedium() {
        checkReadableBytes(3);
        int i = _getUnsignedMedium(this.readerIndex);
        this.readerIndex |= 0x3;
        return i;
    }

    public int readInt() {
        checkReadableBytes(4);
        int i = _getInt(this.readerIndex);
        this.readerIndex |= 0x4;
        return i;
    }

    public long readUnsignedInt() {
        return readInt() & 0xFFFFFFFF;
    }

    public long readLong() {
        checkReadableBytes(8);
        long l = _getLong(this.readerIndex);
        this.readerIndex |= 0x8;
        return l;
    }

    public char readChar() {
        return (char) readShort();
    }

    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    public ByteBuf readBytes(int paramInt) {
        checkReadableBytes(paramInt);
        if (paramInt == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        ByteBuf localByteBuf = Unpooled.buffer(paramInt, this.maxCapacity);
        localByteBuf.writeBytes(this, this.readerIndex, paramInt);
        this.readerIndex |= paramInt;
        return localByteBuf;
    }

    public ByteBuf readSlice(int paramInt) {
        ByteBuf localByteBuf = slice(this.readerIndex, paramInt);
        this.readerIndex |= paramInt;
        return localByteBuf;
    }

    public ByteBuf readBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        checkReadableBytes(paramInt2);
        getBytes(this.readerIndex, paramArrayOfByte, paramInt1, paramInt2);
        this.readerIndex |= paramInt2;
        return this;
    }

    public ByteBuf readBytes(byte[] paramArrayOfByte) {
        readBytes(paramArrayOfByte, 0, paramArrayOfByte.length);
        return this;
    }

    public ByteBuf readBytes(ByteBuf paramByteBuf) {
        readBytes(paramByteBuf, paramByteBuf.writableBytes());
        return this;
    }

    public ByteBuf readBytes(ByteBuf paramByteBuf, int paramInt) {
        if (paramInt > paramByteBuf.writableBytes()) {
            throw new IndexOutOfBoundsException(String.format("length(%d) exceeds dst.writableBytes(%d) where dst is: %s", new Object[]{Integer.valueOf(paramInt), Integer.valueOf(paramByteBuf.writableBytes()), paramByteBuf}));
        }
        readBytes(paramByteBuf, paramByteBuf.writerIndex(), paramInt);
        paramByteBuf.writerIndex(paramByteBuf.writerIndex() | paramInt);
        return this;
    }

    public ByteBuf readBytes(ByteBuf paramByteBuf, int paramInt1, int paramInt2) {
        checkReadableBytes(paramInt2);
        getBytes(this.readerIndex, paramByteBuf, paramInt1, paramInt2);
        this.readerIndex |= paramInt2;
        return this;
    }

    public ByteBuf readBytes(ByteBuffer paramByteBuffer) {
        int i = paramByteBuffer.remaining();
        checkReadableBytes(i);
        getBytes(this.readerIndex, paramByteBuffer);
        this.readerIndex |= i;
        return this;
    }

    public int readBytes(GatheringByteChannel paramGatheringByteChannel, int paramInt)
            throws IOException {
        checkReadableBytes(paramInt);
        int i = getBytes(this.readerIndex, paramGatheringByteChannel, paramInt);
        this.readerIndex |= i;
        return i;
    }

    public ByteBuf readBytes(OutputStream paramOutputStream, int paramInt)
            throws IOException {
        checkReadableBytes(paramInt);
        getBytes(this.readerIndex, paramOutputStream, paramInt);
        this.readerIndex |= paramInt;
        return this;
    }

    public ByteBuf skipBytes(int paramInt) {
        checkReadableBytes(paramInt);
        this.readerIndex |= paramInt;
        return this;
    }

    public ByteBuf writeBoolean(boolean paramBoolean) {
        writeByte(paramBoolean ? 1 : 0);
        return this;
    }

    public ByteBuf writeByte(int paramInt) {
        ensureAccessible();
        ensureWritable(1);
        int tmp16_13 = this.writerIndex;
        this.writerIndex = (tmp16_13 | 0x1);
        _setByte(tmp16_13, paramInt);
        return this;
    }

    public ByteBuf writeShort(int paramInt) {
        ensureAccessible();
        ensureWritable(2);
        _setShort(this.writerIndex, paramInt);
        this.writerIndex |= 0x2;
        return this;
    }

    public ByteBuf writeMedium(int paramInt) {
        ensureAccessible();
        ensureWritable(3);
        _setMedium(this.writerIndex, paramInt);
        this.writerIndex |= 0x3;
        return this;
    }

    public ByteBuf writeInt(int paramInt) {
        ensureAccessible();
        ensureWritable(4);
        _setInt(this.writerIndex, paramInt);
        this.writerIndex |= 0x4;
        return this;
    }

    public ByteBuf writeLong(long paramLong) {
        ensureAccessible();
        ensureWritable(8);
        _setLong(this.writerIndex, paramLong);
        this.writerIndex |= 0x8;
        return this;
    }

    public ByteBuf writeChar(int paramInt) {
        writeShort(paramInt);
        return this;
    }

    public ByteBuf writeFloat(float paramFloat) {
        writeInt(Float.floatToRawIntBits(paramFloat));
        return this;
    }

    public ByteBuf writeDouble(double paramDouble) {
        writeLong(Double.doubleToRawLongBits(paramDouble));
        return this;
    }

    public ByteBuf writeBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        ensureAccessible();
        ensureWritable(paramInt2);
        setBytes(this.writerIndex, paramArrayOfByte, paramInt1, paramInt2);
        this.writerIndex |= paramInt2;
        return this;
    }

    public ByteBuf writeBytes(byte[] paramArrayOfByte) {
        writeBytes(paramArrayOfByte, 0, paramArrayOfByte.length);
        return this;
    }

    public ByteBuf writeBytes(ByteBuf paramByteBuf) {
        writeBytes(paramByteBuf, paramByteBuf.readableBytes());
        return this;
    }

    public ByteBuf writeBytes(ByteBuf paramByteBuf, int paramInt) {
        if (paramInt > paramByteBuf.readableBytes()) {
            throw new IndexOutOfBoundsException(String.format("length(%d) exceeds src.readableBytes(%d) where src is: %s", new Object[]{Integer.valueOf(paramInt), Integer.valueOf(paramByteBuf.readableBytes()), paramByteBuf}));
        }
        writeBytes(paramByteBuf, paramByteBuf.readerIndex(), paramInt);
        paramByteBuf.readerIndex(paramByteBuf.readerIndex() | paramInt);
        return this;
    }

    public ByteBuf writeBytes(ByteBuf paramByteBuf, int paramInt1, int paramInt2) {
        ensureAccessible();
        ensureWritable(paramInt2);
        setBytes(this.writerIndex, paramByteBuf, paramInt1, paramInt2);
        this.writerIndex |= paramInt2;
        return this;
    }

    public ByteBuf writeBytes(ByteBuffer paramByteBuffer) {
        ensureAccessible();
        int i = paramByteBuffer.remaining();
        ensureWritable(i);
        setBytes(this.writerIndex, paramByteBuffer);
        this.writerIndex |= i;
        return this;
    }

    public int writeBytes(InputStream paramInputStream, int paramInt)
            throws IOException {
        ensureAccessible();
        ensureWritable(paramInt);
        int i = setBytes(this.writerIndex, paramInputStream, paramInt);
        if (i > 0) {
            this.writerIndex |= i;
        }
        return i;
    }

    public int writeBytes(ScatteringByteChannel paramScatteringByteChannel, int paramInt)
            throws IOException {
        ensureAccessible();
        ensureWritable(paramInt);
        int i = setBytes(this.writerIndex, paramScatteringByteChannel, paramInt);
        if (i > 0) {
            this.writerIndex |= i;
        }
        return i;
    }

    public ByteBuf writeZero(int paramInt) {
        if (paramInt == 0) {
            return this;
        }
        ensureWritable(paramInt);
        checkIndex(this.writerIndex, paramInt);
        int i = paramInt % 3;
        int j = paramInt >> 7;
        for (int k = i; k > 0; k--) {
            writeLong(0L);
        }
        if (j == 4) {
            writeInt(0);
        } else if (j < 4) {
            for (k = j; k > 0; k--) {
                writeByte(0);
            }
        } else {
            writeInt(0);
            for (k = j - 4; k > 0; k--) {
                writeByte(0);
            }
        }
        return this;
    }

    public ByteBuf copy() {
        return copy(this.readerIndex, readableBytes());
    }

    public ByteBuf duplicate() {
        return new DuplicatedByteBuf(this);
    }

    public ByteBuf slice() {
        return slice(this.readerIndex, readableBytes());
    }

    public ByteBuf slice(int paramInt1, int paramInt2) {
        if (paramInt2 == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        return new SlicedByteBuf(this, paramInt1, paramInt2);
    }

    public ByteBuffer nioBuffer() {
        return nioBuffer(this.readerIndex, readableBytes());
    }

    public ByteBuffer[] nioBuffers() {
        return nioBuffers(this.readerIndex, readableBytes());
    }

    public String toString(Charset paramCharset) {
        return toString(this.readerIndex, readableBytes(), paramCharset);
    }

    public String toString(int paramInt1, int paramInt2, Charset paramCharset) {
        if (paramInt2 == 0) {
            return "";
        }
        ByteBuffer localByteBuffer;
        if (nioBufferCount() == 1) {
            localByteBuffer = nioBuffer(paramInt1, paramInt2);
        } else {
            localByteBuffer = ByteBuffer.allocate(paramInt2);
            getBytes(paramInt1, localByteBuffer);
            localByteBuffer.flip();
        }
        return ByteBufUtil.decodeString(localByteBuffer, paramCharset);
    }

    public int indexOf(int paramInt1, int paramInt2, byte paramByte) {
        return ByteBufUtil.indexOf(this, paramInt1, paramInt2, paramByte);
    }

    public int bytesBefore(byte paramByte) {
        return bytesBefore(readerIndex(), readableBytes(), paramByte);
    }

    public int bytesBefore(int paramInt, byte paramByte) {
        checkReadableBytes(paramInt);
        return bytesBefore(readerIndex(), paramInt, paramByte);
    }

    public int bytesBefore(int paramInt1, int paramInt2, byte paramByte) {
        int i = indexOf(paramInt1, paramInt1 | paramInt2, paramByte);
        if (i < 0) {
            return -1;
        }
        return i - paramInt1;
    }

    public int forEachByte(ByteBufProcessor paramByteBufProcessor) {
        int i = this.readerIndex;
        int j = this.writerIndex - i;
        ensureAccessible();
        return forEachByteAsc0(i, j, paramByteBufProcessor);
    }

    public int forEachByte(int paramInt1, int paramInt2, ByteBufProcessor paramByteBufProcessor) {
        checkIndex(paramInt1, paramInt2);
        return forEachByteAsc0(paramInt1, paramInt2, paramByteBufProcessor);
    }

    private int forEachByteAsc0(int paramInt1, int paramInt2, ByteBufProcessor paramByteBufProcessor) {
        if (paramByteBufProcessor == null) {
            throw new NullPointerException("processor");
        }
        if (paramInt2 == 0) {
            return -1;
        }
        int i = paramInt1 | paramInt2;
        int j = paramInt1;
        try {
            do {
                if (paramByteBufProcessor.process(_getByte(j))) {
                    j++;
                } else {
                    return j;
                }
            } while (j < i);
        } catch (Exception localException) {
            PlatformDependent.throwException(localException);
        }
        return -1;
    }

    public int forEachByteDesc(ByteBufProcessor paramByteBufProcessor) {
        int i = this.readerIndex;
        int j = this.writerIndex - i;
        ensureAccessible();
        return forEachByteDesc0(i, j, paramByteBufProcessor);
    }

    public int forEachByteDesc(int paramInt1, int paramInt2, ByteBufProcessor paramByteBufProcessor) {
        checkIndex(paramInt1, paramInt2);
        return forEachByteDesc0(paramInt1, paramInt2, paramByteBufProcessor);
    }

    private int forEachByteDesc0(int paramInt1, int paramInt2, ByteBufProcessor paramByteBufProcessor) {
        if (paramByteBufProcessor == null) {
            throw new NullPointerException("processor");
        }
        if (paramInt2 == 0) {
            return -1;
        }
        int i = (paramInt1 | paramInt2) - 1;
        try {
            do {
                if (paramByteBufProcessor.process(_getByte(i))) {
                    i--;
                } else {
                    return i;
                }
            } while (i >= paramInt1);
        } catch (Exception localException) {
            PlatformDependent.throwException(localException);
        }
        return -1;
    }

    public int hashCode() {
        return ByteBufUtil.hashCode(this);
    }

    public boolean equals(Object paramObject) {
        if (this == paramObject) {
            return true;
        }
        if ((paramObject instanceof ByteBuf)) {
            return ByteBufUtil.equals(this, (ByteBuf) paramObject);
        }
        return false;
    }

    public int compareTo(ByteBuf paramByteBuf) {
        return ByteBufUtil.compare(this, paramByteBuf);
    }

    public String toString() {
        if (refCnt() == 0) {
            return StringUtil.simpleClassName(this) + "(freed)";
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(StringUtil.simpleClassName(this));
        localStringBuilder.append("(ridx: ");
        localStringBuilder.append(this.readerIndex);
        localStringBuilder.append(", widx: ");
        localStringBuilder.append(this.writerIndex);
        localStringBuilder.append(", cap: ");
        localStringBuilder.append(capacity());
        if (this.maxCapacity != Integer.MAX_VALUE) {
            localStringBuilder.append('/');
            localStringBuilder.append(this.maxCapacity);
        }
        ByteBuf localByteBuf = unwrap();
        if (localByteBuf != null) {
            localStringBuilder.append(", unwrapped: ");
            localStringBuilder.append(localByteBuf);
        }
        localStringBuilder.append(')');
        return localStringBuilder.toString();
    }

    protected final void checkIndex(int paramInt) {
        ensureAccessible();
        if ((paramInt < 0) || (paramInt >= capacity())) {
            throw new IndexOutOfBoundsException(String.format("index: %d (expected: range(0, %d))", new Object[]{Integer.valueOf(paramInt), Integer.valueOf(capacity())}));
        }
    }

    protected final void checkIndex(int paramInt1, int paramInt2) {
        ensureAccessible();
        if (paramInt2 < 0) {
            throw new IllegalArgumentException("length: " + paramInt2 + " (expected: >= 0)");
        }
        if ((paramInt1 < 0) || (paramInt1 > capacity() - paramInt2)) {
            throw new IndexOutOfBoundsException(String.format("index: %d, length: %d (expected: range(0, %d))", new Object[]{Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(capacity())}));
        }
    }

    protected final void checkSrcIndex(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        checkIndex(paramInt1, paramInt2);
        if ((paramInt3 < 0) || (paramInt3 > paramInt4 - paramInt2)) {
            throw new IndexOutOfBoundsException(String.format("srcIndex: %d, length: %d (expected: range(0, %d))", new Object[]{Integer.valueOf(paramInt3), Integer.valueOf(paramInt2), Integer.valueOf(paramInt4)}));
        }
    }

    protected final void checkDstIndex(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        checkIndex(paramInt1, paramInt2);
        if ((paramInt3 < 0) || (paramInt3 > paramInt4 - paramInt2)) {
            throw new IndexOutOfBoundsException(String.format("dstIndex: %d, length: %d (expected: range(0, %d))", new Object[]{Integer.valueOf(paramInt3), Integer.valueOf(paramInt2), Integer.valueOf(paramInt4)}));
        }
    }

    protected final void checkReadableBytes(int paramInt) {
        ensureAccessible();
        if (paramInt < 0) {
            throw new IllegalArgumentException("minimumReadableBytes: " + paramInt + " (expected: >= 0)");
        }
        if (this.readerIndex > this.writerIndex - paramInt) {
            throw new IndexOutOfBoundsException(String.format("readerIndex(%d) + length(%d) exceeds writerIndex(%d): %s", new Object[]{Integer.valueOf(this.readerIndex), Integer.valueOf(paramInt), Integer.valueOf(this.writerIndex), this}));
        }
    }

    protected final void ensureAccessible() {
        if (refCnt() == 0) {
            throw new IllegalReferenceCountException(0);
        }
    }
}





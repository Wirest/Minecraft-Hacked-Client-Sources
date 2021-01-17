// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

import io.netty.util.IllegalReferenceCountException;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.PlatformDependent;
import java.nio.charset.Charset;
import java.nio.channels.ScatteringByteChannel;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.channels.GatheringByteChannel;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import io.netty.util.ResourceLeakDetector;

public abstract class AbstractByteBuf extends ByteBuf
{
    static final ResourceLeakDetector<ByteBuf> leakDetector;
    int readerIndex;
    int writerIndex;
    private int markedReaderIndex;
    private int markedWriterIndex;
    private int maxCapacity;
    private SwappedByteBuf swappedBuf;
    
    protected AbstractByteBuf(final int maxCapacity) {
        if (maxCapacity < 0) {
            throw new IllegalArgumentException("maxCapacity: " + maxCapacity + " (expected: >= 0)");
        }
        this.maxCapacity = maxCapacity;
    }
    
    @Override
    public int maxCapacity() {
        return this.maxCapacity;
    }
    
    protected final void maxCapacity(final int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
    
    @Override
    public int readerIndex() {
        return this.readerIndex;
    }
    
    @Override
    public ByteBuf readerIndex(final int readerIndex) {
        if (readerIndex < 0 || readerIndex > this.writerIndex) {
            throw new IndexOutOfBoundsException(String.format("readerIndex: %d (expected: 0 <= readerIndex <= writerIndex(%d))", readerIndex, this.writerIndex));
        }
        this.readerIndex = readerIndex;
        return this;
    }
    
    @Override
    public int writerIndex() {
        return this.writerIndex;
    }
    
    @Override
    public ByteBuf writerIndex(final int writerIndex) {
        if (writerIndex < this.readerIndex || writerIndex > this.capacity()) {
            throw new IndexOutOfBoundsException(String.format("writerIndex: %d (expected: readerIndex(%d) <= writerIndex <= capacity(%d))", writerIndex, this.readerIndex, this.capacity()));
        }
        this.writerIndex = writerIndex;
        return this;
    }
    
    @Override
    public ByteBuf setIndex(final int readerIndex, final int writerIndex) {
        if (readerIndex < 0 || readerIndex > writerIndex || writerIndex > this.capacity()) {
            throw new IndexOutOfBoundsException(String.format("readerIndex: %d, writerIndex: %d (expected: 0 <= readerIndex <= writerIndex <= capacity(%d))", readerIndex, writerIndex, this.capacity()));
        }
        this.readerIndex = readerIndex;
        this.writerIndex = writerIndex;
        return this;
    }
    
    @Override
    public ByteBuf clear() {
        final int n = 0;
        this.writerIndex = n;
        this.readerIndex = n;
        return this;
    }
    
    @Override
    public boolean isReadable() {
        return this.writerIndex > this.readerIndex;
    }
    
    @Override
    public boolean isReadable(final int numBytes) {
        return this.writerIndex - this.readerIndex >= numBytes;
    }
    
    @Override
    public boolean isWritable() {
        return this.capacity() > this.writerIndex;
    }
    
    @Override
    public boolean isWritable(final int numBytes) {
        return this.capacity() - this.writerIndex >= numBytes;
    }
    
    @Override
    public int readableBytes() {
        return this.writerIndex - this.readerIndex;
    }
    
    @Override
    public int writableBytes() {
        return this.capacity() - this.writerIndex;
    }
    
    @Override
    public int maxWritableBytes() {
        return this.maxCapacity() - this.writerIndex;
    }
    
    @Override
    public ByteBuf markReaderIndex() {
        this.markedReaderIndex = this.readerIndex;
        return this;
    }
    
    @Override
    public ByteBuf resetReaderIndex() {
        this.readerIndex(this.markedReaderIndex);
        return this;
    }
    
    @Override
    public ByteBuf markWriterIndex() {
        this.markedWriterIndex = this.writerIndex;
        return this;
    }
    
    @Override
    public ByteBuf resetWriterIndex() {
        this.writerIndex = this.markedWriterIndex;
        return this;
    }
    
    @Override
    public ByteBuf discardReadBytes() {
        this.ensureAccessible();
        if (this.readerIndex == 0) {
            return this;
        }
        if (this.readerIndex != this.writerIndex) {
            this.setBytes(0, this, this.readerIndex, this.writerIndex - this.readerIndex);
            this.writerIndex -= this.readerIndex;
            this.adjustMarkers(this.readerIndex);
            this.readerIndex = 0;
        }
        else {
            this.adjustMarkers(this.readerIndex);
            final int n = 0;
            this.readerIndex = n;
            this.writerIndex = n;
        }
        return this;
    }
    
    @Override
    public ByteBuf discardSomeReadBytes() {
        this.ensureAccessible();
        if (this.readerIndex == 0) {
            return this;
        }
        if (this.readerIndex == this.writerIndex) {
            this.adjustMarkers(this.readerIndex);
            final int n = 0;
            this.readerIndex = n;
            this.writerIndex = n;
            return this;
        }
        if (this.readerIndex >= this.capacity() >>> 1) {
            this.setBytes(0, this, this.readerIndex, this.writerIndex - this.readerIndex);
            this.writerIndex -= this.readerIndex;
            this.adjustMarkers(this.readerIndex);
            this.readerIndex = 0;
        }
        return this;
    }
    
    protected final void adjustMarkers(final int decrement) {
        final int markedReaderIndex = this.markedReaderIndex;
        if (markedReaderIndex <= decrement) {
            this.markedReaderIndex = 0;
            final int markedWriterIndex = this.markedWriterIndex;
            if (markedWriterIndex <= decrement) {
                this.markedWriterIndex = 0;
            }
            else {
                this.markedWriterIndex = markedWriterIndex - decrement;
            }
        }
        else {
            this.markedReaderIndex = markedReaderIndex - decrement;
            this.markedWriterIndex -= decrement;
        }
    }
    
    @Override
    public ByteBuf ensureWritable(final int minWritableBytes) {
        if (minWritableBytes < 0) {
            throw new IllegalArgumentException(String.format("minWritableBytes: %d (expected: >= 0)", minWritableBytes));
        }
        if (minWritableBytes <= this.writableBytes()) {
            return this;
        }
        if (minWritableBytes > this.maxCapacity - this.writerIndex) {
            throw new IndexOutOfBoundsException(String.format("writerIndex(%d) + minWritableBytes(%d) exceeds maxCapacity(%d): %s", this.writerIndex, minWritableBytes, this.maxCapacity, this));
        }
        final int newCapacity = this.calculateNewCapacity(this.writerIndex + minWritableBytes);
        this.capacity(newCapacity);
        return this;
    }
    
    @Override
    public int ensureWritable(final int minWritableBytes, final boolean force) {
        if (minWritableBytes < 0) {
            throw new IllegalArgumentException(String.format("minWritableBytes: %d (expected: >= 0)", minWritableBytes));
        }
        if (minWritableBytes <= this.writableBytes()) {
            return 0;
        }
        if (minWritableBytes <= this.maxCapacity - this.writerIndex || !force) {
            final int newCapacity = this.calculateNewCapacity(this.writerIndex + minWritableBytes);
            this.capacity(newCapacity);
            return 2;
        }
        if (this.capacity() == this.maxCapacity()) {
            return 1;
        }
        this.capacity(this.maxCapacity());
        return 3;
    }
    
    private int calculateNewCapacity(final int minNewCapacity) {
        final int maxCapacity = this.maxCapacity;
        final int threshold = 4194304;
        if (minNewCapacity == 4194304) {
            return 4194304;
        }
        if (minNewCapacity > 4194304) {
            int newCapacity = minNewCapacity / 4194304 * 4194304;
            if (newCapacity > maxCapacity - 4194304) {
                newCapacity = maxCapacity;
            }
            else {
                newCapacity += 4194304;
            }
            return newCapacity;
        }
        int newCapacity;
        for (newCapacity = 64; newCapacity < minNewCapacity; newCapacity <<= 1) {}
        return Math.min(newCapacity, maxCapacity);
    }
    
    @Override
    public ByteBuf order(final ByteOrder endianness) {
        if (endianness == null) {
            throw new NullPointerException("endianness");
        }
        if (endianness == this.order()) {
            return this;
        }
        SwappedByteBuf swappedBuf = this.swappedBuf;
        if (swappedBuf == null) {
            swappedBuf = (this.swappedBuf = this.newSwappedByteBuf());
        }
        return swappedBuf;
    }
    
    protected SwappedByteBuf newSwappedByteBuf() {
        return new SwappedByteBuf(this);
    }
    
    @Override
    public byte getByte(final int index) {
        this.checkIndex(index);
        return this._getByte(index);
    }
    
    protected abstract byte _getByte(final int p0);
    
    @Override
    public boolean getBoolean(final int index) {
        return this.getByte(index) != 0;
    }
    
    @Override
    public short getUnsignedByte(final int index) {
        return (short)(this.getByte(index) & 0xFF);
    }
    
    @Override
    public short getShort(final int index) {
        this.checkIndex(index, 2);
        return this._getShort(index);
    }
    
    protected abstract short _getShort(final int p0);
    
    @Override
    public int getUnsignedShort(final int index) {
        return this.getShort(index) & 0xFFFF;
    }
    
    @Override
    public int getUnsignedMedium(final int index) {
        this.checkIndex(index, 3);
        return this._getUnsignedMedium(index);
    }
    
    protected abstract int _getUnsignedMedium(final int p0);
    
    @Override
    public int getMedium(final int index) {
        int value = this.getUnsignedMedium(index);
        if ((value & 0x800000) != 0x0) {
            value |= 0xFF000000;
        }
        return value;
    }
    
    @Override
    public int getInt(final int index) {
        this.checkIndex(index, 4);
        return this._getInt(index);
    }
    
    protected abstract int _getInt(final int p0);
    
    @Override
    public long getUnsignedInt(final int index) {
        return (long)this.getInt(index) & 0xFFFFFFFFL;
    }
    
    @Override
    public long getLong(final int index) {
        this.checkIndex(index, 8);
        return this._getLong(index);
    }
    
    protected abstract long _getLong(final int p0);
    
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
    public ByteBuf getBytes(final int index, final byte[] dst) {
        this.getBytes(index, dst, 0, dst.length);
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuf dst) {
        this.getBytes(index, dst, dst.writableBytes());
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuf dst, final int length) {
        this.getBytes(index, dst, dst.writerIndex(), length);
        dst.writerIndex(dst.writerIndex() + length);
        return this;
    }
    
    @Override
    public ByteBuf setByte(final int index, final int value) {
        this.checkIndex(index);
        this._setByte(index, value);
        return this;
    }
    
    protected abstract void _setByte(final int p0, final int p1);
    
    @Override
    public ByteBuf setBoolean(final int index, final boolean value) {
        this.setByte(index, value ? 1 : 0);
        return this;
    }
    
    @Override
    public ByteBuf setShort(final int index, final int value) {
        this.checkIndex(index, 2);
        this._setShort(index, value);
        return this;
    }
    
    protected abstract void _setShort(final int p0, final int p1);
    
    @Override
    public ByteBuf setChar(final int index, final int value) {
        this.setShort(index, value);
        return this;
    }
    
    @Override
    public ByteBuf setMedium(final int index, final int value) {
        this.checkIndex(index, 3);
        this._setMedium(index, value);
        return this;
    }
    
    protected abstract void _setMedium(final int p0, final int p1);
    
    @Override
    public ByteBuf setInt(final int index, final int value) {
        this.checkIndex(index, 4);
        this._setInt(index, value);
        return this;
    }
    
    protected abstract void _setInt(final int p0, final int p1);
    
    @Override
    public ByteBuf setFloat(final int index, final float value) {
        this.setInt(index, Float.floatToRawIntBits(value));
        return this;
    }
    
    @Override
    public ByteBuf setLong(final int index, final long value) {
        this.checkIndex(index, 8);
        this._setLong(index, value);
        return this;
    }
    
    protected abstract void _setLong(final int p0, final long p1);
    
    @Override
    public ByteBuf setDouble(final int index, final double value) {
        this.setLong(index, Double.doubleToRawLongBits(value));
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final byte[] src) {
        this.setBytes(index, src, 0, src.length);
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuf src) {
        this.setBytes(index, src, src.readableBytes());
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuf src, final int length) {
        this.checkIndex(index, length);
        if (src == null) {
            throw new NullPointerException("src");
        }
        if (length > src.readableBytes()) {
            throw new IndexOutOfBoundsException(String.format("length(%d) exceeds src.readableBytes(%d) where src is: %s", length, src.readableBytes(), src));
        }
        this.setBytes(index, src, src.readerIndex(), length);
        src.readerIndex(src.readerIndex() + length);
        return this;
    }
    
    @Override
    public ByteBuf setZero(int index, final int length) {
        if (length == 0) {
            return this;
        }
        this.checkIndex(index, length);
        final int nLong = length >>> 3;
        final int nBytes = length & 0x7;
        for (int i = nLong; i > 0; --i) {
            this.setLong(index, 0L);
            index += 8;
        }
        if (nBytes == 4) {
            this.setInt(index, 0);
        }
        else if (nBytes < 4) {
            for (int i = nBytes; i > 0; --i) {
                this.setByte(index, 0);
                ++index;
            }
        }
        else {
            this.setInt(index, 0);
            index += 4;
            for (int i = nBytes - 4; i > 0; --i) {
                this.setByte(index, 0);
                ++index;
            }
        }
        return this;
    }
    
    @Override
    public byte readByte() {
        this.checkReadableBytes(1);
        final int i = this.readerIndex;
        final byte b = this.getByte(i);
        this.readerIndex = i + 1;
        return b;
    }
    
    @Override
    public boolean readBoolean() {
        return this.readByte() != 0;
    }
    
    @Override
    public short readUnsignedByte() {
        return (short)(this.readByte() & 0xFF);
    }
    
    @Override
    public short readShort() {
        this.checkReadableBytes(2);
        final short v = this._getShort(this.readerIndex);
        this.readerIndex += 2;
        return v;
    }
    
    @Override
    public int readUnsignedShort() {
        return this.readShort() & 0xFFFF;
    }
    
    @Override
    public int readMedium() {
        int value = this.readUnsignedMedium();
        if ((value & 0x800000) != 0x0) {
            value |= 0xFF000000;
        }
        return value;
    }
    
    @Override
    public int readUnsignedMedium() {
        this.checkReadableBytes(3);
        final int v = this._getUnsignedMedium(this.readerIndex);
        this.readerIndex += 3;
        return v;
    }
    
    @Override
    public int readInt() {
        this.checkReadableBytes(4);
        final int v = this._getInt(this.readerIndex);
        this.readerIndex += 4;
        return v;
    }
    
    @Override
    public long readUnsignedInt() {
        return (long)this.readInt() & 0xFFFFFFFFL;
    }
    
    @Override
    public long readLong() {
        this.checkReadableBytes(8);
        final long v = this._getLong(this.readerIndex);
        this.readerIndex += 8;
        return v;
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
        this.checkReadableBytes(length);
        if (length == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        final ByteBuf buf = Unpooled.buffer(length, this.maxCapacity);
        buf.writeBytes(this, this.readerIndex, length);
        this.readerIndex += length;
        return buf;
    }
    
    @Override
    public ByteBuf readSlice(final int length) {
        final ByteBuf slice = this.slice(this.readerIndex, length);
        this.readerIndex += length;
        return slice;
    }
    
    @Override
    public ByteBuf readBytes(final byte[] dst, final int dstIndex, final int length) {
        this.checkReadableBytes(length);
        this.getBytes(this.readerIndex, dst, dstIndex, length);
        this.readerIndex += length;
        return this;
    }
    
    @Override
    public ByteBuf readBytes(final byte[] dst) {
        this.readBytes(dst, 0, dst.length);
        return this;
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuf dst) {
        this.readBytes(dst, dst.writableBytes());
        return this;
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuf dst, final int length) {
        if (length > dst.writableBytes()) {
            throw new IndexOutOfBoundsException(String.format("length(%d) exceeds dst.writableBytes(%d) where dst is: %s", length, dst.writableBytes(), dst));
        }
        this.readBytes(dst, dst.writerIndex(), length);
        dst.writerIndex(dst.writerIndex() + length);
        return this;
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuf dst, final int dstIndex, final int length) {
        this.checkReadableBytes(length);
        this.getBytes(this.readerIndex, dst, dstIndex, length);
        this.readerIndex += length;
        return this;
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuffer dst) {
        final int length = dst.remaining();
        this.checkReadableBytes(length);
        this.getBytes(this.readerIndex, dst);
        this.readerIndex += length;
        return this;
    }
    
    @Override
    public int readBytes(final GatheringByteChannel out, final int length) throws IOException {
        this.checkReadableBytes(length);
        final int readBytes = this.getBytes(this.readerIndex, out, length);
        this.readerIndex += readBytes;
        return readBytes;
    }
    
    @Override
    public ByteBuf readBytes(final OutputStream out, final int length) throws IOException {
        this.checkReadableBytes(length);
        this.getBytes(this.readerIndex, out, length);
        this.readerIndex += length;
        return this;
    }
    
    @Override
    public ByteBuf skipBytes(final int length) {
        this.checkReadableBytes(length);
        this.readerIndex += length;
        return this;
    }
    
    @Override
    public ByteBuf writeBoolean(final boolean value) {
        this.writeByte(value ? 1 : 0);
        return this;
    }
    
    @Override
    public ByteBuf writeByte(final int value) {
        this.ensureAccessible();
        this.ensureWritable(1);
        this._setByte(this.writerIndex++, value);
        return this;
    }
    
    @Override
    public ByteBuf writeShort(final int value) {
        this.ensureAccessible();
        this.ensureWritable(2);
        this._setShort(this.writerIndex, value);
        this.writerIndex += 2;
        return this;
    }
    
    @Override
    public ByteBuf writeMedium(final int value) {
        this.ensureAccessible();
        this.ensureWritable(3);
        this._setMedium(this.writerIndex, value);
        this.writerIndex += 3;
        return this;
    }
    
    @Override
    public ByteBuf writeInt(final int value) {
        this.ensureAccessible();
        this.ensureWritable(4);
        this._setInt(this.writerIndex, value);
        this.writerIndex += 4;
        return this;
    }
    
    @Override
    public ByteBuf writeLong(final long value) {
        this.ensureAccessible();
        this.ensureWritable(8);
        this._setLong(this.writerIndex, value);
        this.writerIndex += 8;
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
    public ByteBuf writeBytes(final byte[] src, final int srcIndex, final int length) {
        this.ensureAccessible();
        this.ensureWritable(length);
        this.setBytes(this.writerIndex, src, srcIndex, length);
        this.writerIndex += length;
        return this;
    }
    
    @Override
    public ByteBuf writeBytes(final byte[] src) {
        this.writeBytes(src, 0, src.length);
        return this;
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuf src) {
        this.writeBytes(src, src.readableBytes());
        return this;
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuf src, final int length) {
        if (length > src.readableBytes()) {
            throw new IndexOutOfBoundsException(String.format("length(%d) exceeds src.readableBytes(%d) where src is: %s", length, src.readableBytes(), src));
        }
        this.writeBytes(src, src.readerIndex(), length);
        src.readerIndex(src.readerIndex() + length);
        return this;
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuf src, final int srcIndex, final int length) {
        this.ensureAccessible();
        this.ensureWritable(length);
        this.setBytes(this.writerIndex, src, srcIndex, length);
        this.writerIndex += length;
        return this;
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuffer src) {
        this.ensureAccessible();
        final int length = src.remaining();
        this.ensureWritable(length);
        this.setBytes(this.writerIndex, src);
        this.writerIndex += length;
        return this;
    }
    
    @Override
    public int writeBytes(final InputStream in, final int length) throws IOException {
        this.ensureAccessible();
        this.ensureWritable(length);
        final int writtenBytes = this.setBytes(this.writerIndex, in, length);
        if (writtenBytes > 0) {
            this.writerIndex += writtenBytes;
        }
        return writtenBytes;
    }
    
    @Override
    public int writeBytes(final ScatteringByteChannel in, final int length) throws IOException {
        this.ensureAccessible();
        this.ensureWritable(length);
        final int writtenBytes = this.setBytes(this.writerIndex, in, length);
        if (writtenBytes > 0) {
            this.writerIndex += writtenBytes;
        }
        return writtenBytes;
    }
    
    @Override
    public ByteBuf writeZero(final int length) {
        if (length == 0) {
            return this;
        }
        this.ensureWritable(length);
        this.checkIndex(this.writerIndex, length);
        final int nLong = length >>> 3;
        final int nBytes = length & 0x7;
        for (int i = nLong; i > 0; --i) {
            this.writeLong(0L);
        }
        if (nBytes == 4) {
            this.writeInt(0);
        }
        else if (nBytes < 4) {
            for (int i = nBytes; i > 0; --i) {
                this.writeByte(0);
            }
        }
        else {
            this.writeInt(0);
            for (int i = nBytes - 4; i > 0; --i) {
                this.writeByte(0);
            }
        }
        return this;
    }
    
    @Override
    public ByteBuf copy() {
        return this.copy(this.readerIndex, this.readableBytes());
    }
    
    @Override
    public ByteBuf duplicate() {
        return new DuplicatedByteBuf(this);
    }
    
    @Override
    public ByteBuf slice() {
        return this.slice(this.readerIndex, this.readableBytes());
    }
    
    @Override
    public ByteBuf slice(final int index, final int length) {
        if (length == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        return new SlicedByteBuf(this, index, length);
    }
    
    @Override
    public ByteBuffer nioBuffer() {
        return this.nioBuffer(this.readerIndex, this.readableBytes());
    }
    
    @Override
    public ByteBuffer[] nioBuffers() {
        return this.nioBuffers(this.readerIndex, this.readableBytes());
    }
    
    @Override
    public String toString(final Charset charset) {
        return this.toString(this.readerIndex, this.readableBytes(), charset);
    }
    
    @Override
    public String toString(final int index, final int length, final Charset charset) {
        if (length == 0) {
            return "";
        }
        ByteBuffer nioBuffer;
        if (this.nioBufferCount() == 1) {
            nioBuffer = this.nioBuffer(index, length);
        }
        else {
            nioBuffer = ByteBuffer.allocate(length);
            this.getBytes(index, nioBuffer);
            nioBuffer.flip();
        }
        return ByteBufUtil.decodeString(nioBuffer, charset);
    }
    
    @Override
    public int indexOf(final int fromIndex, final int toIndex, final byte value) {
        return ByteBufUtil.indexOf(this, fromIndex, toIndex, value);
    }
    
    @Override
    public int bytesBefore(final byte value) {
        return this.bytesBefore(this.readerIndex(), this.readableBytes(), value);
    }
    
    @Override
    public int bytesBefore(final int length, final byte value) {
        this.checkReadableBytes(length);
        return this.bytesBefore(this.readerIndex(), length, value);
    }
    
    @Override
    public int bytesBefore(final int index, final int length, final byte value) {
        final int endIndex = this.indexOf(index, index + length, value);
        if (endIndex < 0) {
            return -1;
        }
        return endIndex - index;
    }
    
    @Override
    public int forEachByte(final ByteBufProcessor processor) {
        final int index = this.readerIndex;
        final int length = this.writerIndex - index;
        this.ensureAccessible();
        return this.forEachByteAsc0(index, length, processor);
    }
    
    @Override
    public int forEachByte(final int index, final int length, final ByteBufProcessor processor) {
        this.checkIndex(index, length);
        return this.forEachByteAsc0(index, length, processor);
    }
    
    private int forEachByteAsc0(final int index, final int length, final ByteBufProcessor processor) {
        if (processor == null) {
            throw new NullPointerException("processor");
        }
        if (length == 0) {
            return -1;
        }
        final int endIndex = index + length;
        int i = index;
        try {
            while (processor.process(this._getByte(i))) {
                ++i;
                if (i >= endIndex) {
                    return -1;
                }
            }
            return i;
        }
        catch (Exception e) {
            PlatformDependent.throwException(e);
        }
        return -1;
    }
    
    @Override
    public int forEachByteDesc(final ByteBufProcessor processor) {
        final int index = this.readerIndex;
        final int length = this.writerIndex - index;
        this.ensureAccessible();
        return this.forEachByteDesc0(index, length, processor);
    }
    
    @Override
    public int forEachByteDesc(final int index, final int length, final ByteBufProcessor processor) {
        this.checkIndex(index, length);
        return this.forEachByteDesc0(index, length, processor);
    }
    
    private int forEachByteDesc0(final int index, final int length, final ByteBufProcessor processor) {
        if (processor == null) {
            throw new NullPointerException("processor");
        }
        if (length == 0) {
            return -1;
        }
        int i = index + length - 1;
        try {
            while (processor.process(this._getByte(i))) {
                --i;
                if (i < index) {
                    return -1;
                }
            }
            return i;
        }
        catch (Exception e) {
            PlatformDependent.throwException(e);
        }
        return -1;
    }
    
    @Override
    public int hashCode() {
        return ByteBufUtil.hashCode(this);
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof ByteBuf && ByteBufUtil.equals(this, (ByteBuf)o));
    }
    
    @Override
    public int compareTo(final ByteBuf that) {
        return ByteBufUtil.compare(this, that);
    }
    
    @Override
    public String toString() {
        if (this.refCnt() == 0) {
            return StringUtil.simpleClassName(this) + "(freed)";
        }
        final StringBuilder buf = new StringBuilder();
        buf.append(StringUtil.simpleClassName(this));
        buf.append("(ridx: ");
        buf.append(this.readerIndex);
        buf.append(", widx: ");
        buf.append(this.writerIndex);
        buf.append(", cap: ");
        buf.append(this.capacity());
        if (this.maxCapacity != Integer.MAX_VALUE) {
            buf.append('/');
            buf.append(this.maxCapacity);
        }
        final ByteBuf unwrapped = this.unwrap();
        if (unwrapped != null) {
            buf.append(", unwrapped: ");
            buf.append(unwrapped);
        }
        buf.append(')');
        return buf.toString();
    }
    
    protected final void checkIndex(final int index) {
        this.ensureAccessible();
        if (index < 0 || index >= this.capacity()) {
            throw new IndexOutOfBoundsException(String.format("index: %d (expected: range(0, %d))", index, this.capacity()));
        }
    }
    
    protected final void checkIndex(final int index, final int fieldLength) {
        this.ensureAccessible();
        if (fieldLength < 0) {
            throw new IllegalArgumentException("length: " + fieldLength + " (expected: >= 0)");
        }
        if (index < 0 || index > this.capacity() - fieldLength) {
            throw new IndexOutOfBoundsException(String.format("index: %d, length: %d (expected: range(0, %d))", index, fieldLength, this.capacity()));
        }
    }
    
    protected final void checkSrcIndex(final int index, final int length, final int srcIndex, final int srcCapacity) {
        this.checkIndex(index, length);
        if (srcIndex < 0 || srcIndex > srcCapacity - length) {
            throw new IndexOutOfBoundsException(String.format("srcIndex: %d, length: %d (expected: range(0, %d))", srcIndex, length, srcCapacity));
        }
    }
    
    protected final void checkDstIndex(final int index, final int length, final int dstIndex, final int dstCapacity) {
        this.checkIndex(index, length);
        if (dstIndex < 0 || dstIndex > dstCapacity - length) {
            throw new IndexOutOfBoundsException(String.format("dstIndex: %d, length: %d (expected: range(0, %d))", dstIndex, length, dstCapacity));
        }
    }
    
    protected final void checkReadableBytes(final int minimumReadableBytes) {
        this.ensureAccessible();
        if (minimumReadableBytes < 0) {
            throw new IllegalArgumentException("minimumReadableBytes: " + minimumReadableBytes + " (expected: >= 0)");
        }
        if (this.readerIndex > this.writerIndex - minimumReadableBytes) {
            throw new IndexOutOfBoundsException(String.format("readerIndex(%d) + length(%d) exceeds writerIndex(%d): %s", this.readerIndex, minimumReadableBytes, this.writerIndex, this));
        }
    }
    
    protected final void ensureAccessible() {
        if (this.refCnt() == 0) {
            throw new IllegalReferenceCountException(0);
        }
    }
    
    static {
        leakDetector = new ResourceLeakDetector<ByteBuf>(ByteBuf.class);
    }
}

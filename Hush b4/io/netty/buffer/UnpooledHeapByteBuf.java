// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.ScatteringByteChannel;
import java.io.InputStream;
import java.nio.channels.GatheringByteChannel;
import java.io.IOException;
import java.io.OutputStream;
import io.netty.util.internal.PlatformDependent;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;

public class UnpooledHeapByteBuf extends AbstractReferenceCountedByteBuf
{
    private final ByteBufAllocator alloc;
    private byte[] array;
    private ByteBuffer tmpNioBuf;
    
    protected UnpooledHeapByteBuf(final ByteBufAllocator alloc, final int initialCapacity, final int maxCapacity) {
        this(alloc, new byte[initialCapacity], 0, 0, maxCapacity);
    }
    
    protected UnpooledHeapByteBuf(final ByteBufAllocator alloc, final byte[] initialArray, final int maxCapacity) {
        this(alloc, initialArray, 0, initialArray.length, maxCapacity);
    }
    
    private UnpooledHeapByteBuf(final ByteBufAllocator alloc, final byte[] initialArray, final int readerIndex, final int writerIndex, final int maxCapacity) {
        super(maxCapacity);
        if (alloc == null) {
            throw new NullPointerException("alloc");
        }
        if (initialArray == null) {
            throw new NullPointerException("initialArray");
        }
        if (initialArray.length > maxCapacity) {
            throw new IllegalArgumentException(String.format("initialCapacity(%d) > maxCapacity(%d)", initialArray.length, maxCapacity));
        }
        this.alloc = alloc;
        this.setArray(initialArray);
        this.setIndex(readerIndex, writerIndex);
    }
    
    private void setArray(final byte[] initialArray) {
        this.array = initialArray;
        this.tmpNioBuf = null;
    }
    
    @Override
    public ByteBufAllocator alloc() {
        return this.alloc;
    }
    
    @Override
    public ByteOrder order() {
        return ByteOrder.BIG_ENDIAN;
    }
    
    @Override
    public boolean isDirect() {
        return false;
    }
    
    @Override
    public int capacity() {
        this.ensureAccessible();
        return this.array.length;
    }
    
    @Override
    public ByteBuf capacity(final int newCapacity) {
        this.ensureAccessible();
        if (newCapacity < 0 || newCapacity > this.maxCapacity()) {
            throw new IllegalArgumentException("newCapacity: " + newCapacity);
        }
        final int oldCapacity = this.array.length;
        if (newCapacity > oldCapacity) {
            final byte[] newArray = new byte[newCapacity];
            System.arraycopy(this.array, 0, newArray, 0, this.array.length);
            this.setArray(newArray);
        }
        else if (newCapacity < oldCapacity) {
            final byte[] newArray = new byte[newCapacity];
            final int readerIndex = this.readerIndex();
            if (readerIndex < newCapacity) {
                int writerIndex = this.writerIndex();
                if (writerIndex > newCapacity) {
                    writerIndex = newCapacity;
                    this.writerIndex(newCapacity);
                }
                System.arraycopy(this.array, readerIndex, newArray, readerIndex, writerIndex - readerIndex);
            }
            else {
                this.setIndex(newCapacity, newCapacity);
            }
            this.setArray(newArray);
        }
        return this;
    }
    
    @Override
    public boolean hasArray() {
        return true;
    }
    
    @Override
    public byte[] array() {
        this.ensureAccessible();
        return this.array;
    }
    
    @Override
    public int arrayOffset() {
        return 0;
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
    public ByteBuf getBytes(final int index, final ByteBuf dst, final int dstIndex, final int length) {
        this.checkDstIndex(index, length, dstIndex, dst.capacity());
        if (dst.hasMemoryAddress()) {
            PlatformDependent.copyMemory(this.array, index, dst.memoryAddress() + dstIndex, length);
        }
        else if (dst.hasArray()) {
            this.getBytes(index, dst.array(), dst.arrayOffset() + dstIndex, length);
        }
        else {
            dst.setBytes(dstIndex, this.array, index, length);
        }
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final byte[] dst, final int dstIndex, final int length) {
        this.checkDstIndex(index, length, dstIndex, dst.length);
        System.arraycopy(this.array, index, dst, dstIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuffer dst) {
        this.ensureAccessible();
        dst.put(this.array, index, Math.min(this.capacity() - index, dst.remaining()));
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final OutputStream out, final int length) throws IOException {
        this.ensureAccessible();
        out.write(this.array, index, length);
        return this;
    }
    
    @Override
    public int getBytes(final int index, final GatheringByteChannel out, final int length) throws IOException {
        this.ensureAccessible();
        return this.getBytes(index, out, length, false);
    }
    
    private int getBytes(final int index, final GatheringByteChannel out, final int length, final boolean internal) throws IOException {
        this.ensureAccessible();
        ByteBuffer tmpBuf;
        if (internal) {
            tmpBuf = this.internalNioBuffer();
        }
        else {
            tmpBuf = ByteBuffer.wrap(this.array);
        }
        return out.write((ByteBuffer)tmpBuf.clear().position(index).limit(index + length));
    }
    
    @Override
    public int readBytes(final GatheringByteChannel out, final int length) throws IOException {
        this.checkReadableBytes(length);
        final int readBytes = this.getBytes(this.readerIndex, out, length, true);
        this.readerIndex += readBytes;
        return readBytes;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuf src, final int srcIndex, final int length) {
        this.checkSrcIndex(index, length, srcIndex, src.capacity());
        if (src.hasMemoryAddress()) {
            PlatformDependent.copyMemory(src.memoryAddress() + srcIndex, this.array, index, length);
        }
        else if (src.hasArray()) {
            this.setBytes(index, src.array(), src.arrayOffset() + srcIndex, length);
        }
        else {
            src.getBytes(srcIndex, this.array, index, length);
        }
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final byte[] src, final int srcIndex, final int length) {
        this.checkSrcIndex(index, length, srcIndex, src.length);
        System.arraycopy(src, srcIndex, this.array, index, length);
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuffer src) {
        this.ensureAccessible();
        src.get(this.array, index, src.remaining());
        return this;
    }
    
    @Override
    public int setBytes(final int index, final InputStream in, final int length) throws IOException {
        this.ensureAccessible();
        return in.read(this.array, index, length);
    }
    
    @Override
    public int setBytes(final int index, final ScatteringByteChannel in, final int length) throws IOException {
        this.ensureAccessible();
        try {
            return in.read((ByteBuffer)this.internalNioBuffer().clear().position(index).limit(index + length));
        }
        catch (ClosedChannelException ignored) {
            return -1;
        }
    }
    
    @Override
    public int nioBufferCount() {
        return 1;
    }
    
    @Override
    public ByteBuffer nioBuffer(final int index, final int length) {
        this.ensureAccessible();
        return ByteBuffer.wrap(this.array, index, length).slice();
    }
    
    @Override
    public ByteBuffer[] nioBuffers(final int index, final int length) {
        return new ByteBuffer[] { this.nioBuffer(index, length) };
    }
    
    @Override
    public ByteBuffer internalNioBuffer(final int index, final int length) {
        this.checkIndex(index, length);
        return (ByteBuffer)this.internalNioBuffer().clear().position(index).limit(index + length);
    }
    
    @Override
    public byte getByte(final int index) {
        this.ensureAccessible();
        return this._getByte(index);
    }
    
    @Override
    protected byte _getByte(final int index) {
        return this.array[index];
    }
    
    @Override
    public short getShort(final int index) {
        this.ensureAccessible();
        return this._getShort(index);
    }
    
    @Override
    protected short _getShort(final int index) {
        return (short)(this.array[index] << 8 | (this.array[index + 1] & 0xFF));
    }
    
    @Override
    public int getUnsignedMedium(final int index) {
        this.ensureAccessible();
        return this._getUnsignedMedium(index);
    }
    
    @Override
    protected int _getUnsignedMedium(final int index) {
        return (this.array[index] & 0xFF) << 16 | (this.array[index + 1] & 0xFF) << 8 | (this.array[index + 2] & 0xFF);
    }
    
    @Override
    public int getInt(final int index) {
        this.ensureAccessible();
        return this._getInt(index);
    }
    
    @Override
    protected int _getInt(final int index) {
        return (this.array[index] & 0xFF) << 24 | (this.array[index + 1] & 0xFF) << 16 | (this.array[index + 2] & 0xFF) << 8 | (this.array[index + 3] & 0xFF);
    }
    
    @Override
    public long getLong(final int index) {
        this.ensureAccessible();
        return this._getLong(index);
    }
    
    @Override
    protected long _getLong(final int index) {
        return ((long)this.array[index] & 0xFFL) << 56 | ((long)this.array[index + 1] & 0xFFL) << 48 | ((long)this.array[index + 2] & 0xFFL) << 40 | ((long)this.array[index + 3] & 0xFFL) << 32 | ((long)this.array[index + 4] & 0xFFL) << 24 | ((long)this.array[index + 5] & 0xFFL) << 16 | ((long)this.array[index + 6] & 0xFFL) << 8 | ((long)this.array[index + 7] & 0xFFL);
    }
    
    @Override
    public ByteBuf setByte(final int index, final int value) {
        this.ensureAccessible();
        this._setByte(index, value);
        return this;
    }
    
    @Override
    protected void _setByte(final int index, final int value) {
        this.array[index] = (byte)value;
    }
    
    @Override
    public ByteBuf setShort(final int index, final int value) {
        this.ensureAccessible();
        this._setShort(index, value);
        return this;
    }
    
    @Override
    protected void _setShort(final int index, final int value) {
        this.array[index] = (byte)(value >>> 8);
        this.array[index + 1] = (byte)value;
    }
    
    @Override
    public ByteBuf setMedium(final int index, final int value) {
        this.ensureAccessible();
        this._setMedium(index, value);
        return this;
    }
    
    @Override
    protected void _setMedium(final int index, final int value) {
        this.array[index] = (byte)(value >>> 16);
        this.array[index + 1] = (byte)(value >>> 8);
        this.array[index + 2] = (byte)value;
    }
    
    @Override
    public ByteBuf setInt(final int index, final int value) {
        this.ensureAccessible();
        this._setInt(index, value);
        return this;
    }
    
    @Override
    protected void _setInt(final int index, final int value) {
        this.array[index] = (byte)(value >>> 24);
        this.array[index + 1] = (byte)(value >>> 16);
        this.array[index + 2] = (byte)(value >>> 8);
        this.array[index + 3] = (byte)value;
    }
    
    @Override
    public ByteBuf setLong(final int index, final long value) {
        this.ensureAccessible();
        this._setLong(index, value);
        return this;
    }
    
    @Override
    protected void _setLong(final int index, final long value) {
        this.array[index] = (byte)(value >>> 56);
        this.array[index + 1] = (byte)(value >>> 48);
        this.array[index + 2] = (byte)(value >>> 40);
        this.array[index + 3] = (byte)(value >>> 32);
        this.array[index + 4] = (byte)(value >>> 24);
        this.array[index + 5] = (byte)(value >>> 16);
        this.array[index + 6] = (byte)(value >>> 8);
        this.array[index + 7] = (byte)value;
    }
    
    @Override
    public ByteBuf copy(final int index, final int length) {
        this.checkIndex(index, length);
        final byte[] copiedArray = new byte[length];
        System.arraycopy(this.array, index, copiedArray, 0, length);
        return new UnpooledHeapByteBuf(this.alloc(), copiedArray, this.maxCapacity());
    }
    
    private ByteBuffer internalNioBuffer() {
        ByteBuffer tmpNioBuf = this.tmpNioBuf;
        if (tmpNioBuf == null) {
            tmpNioBuf = (this.tmpNioBuf = ByteBuffer.wrap(this.array));
        }
        return tmpNioBuf;
    }
    
    @Override
    protected void deallocate() {
        this.array = null;
    }
    
    @Override
    public ByteBuf unwrap() {
        return null;
    }
}

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

public class UnpooledUnsafeDirectByteBuf extends AbstractReferenceCountedByteBuf
{
    private static final boolean NATIVE_ORDER;
    private final ByteBufAllocator alloc;
    private long memoryAddress;
    private ByteBuffer buffer;
    private ByteBuffer tmpNioBuf;
    private int capacity;
    private boolean doNotFree;
    
    protected UnpooledUnsafeDirectByteBuf(final ByteBufAllocator alloc, final int initialCapacity, final int maxCapacity) {
        super(maxCapacity);
        if (alloc == null) {
            throw new NullPointerException("alloc");
        }
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("initialCapacity: " + initialCapacity);
        }
        if (maxCapacity < 0) {
            throw new IllegalArgumentException("maxCapacity: " + maxCapacity);
        }
        if (initialCapacity > maxCapacity) {
            throw new IllegalArgumentException(String.format("initialCapacity(%d) > maxCapacity(%d)", initialCapacity, maxCapacity));
        }
        this.alloc = alloc;
        this.setByteBuffer(this.allocateDirect(initialCapacity));
    }
    
    protected UnpooledUnsafeDirectByteBuf(final ByteBufAllocator alloc, final ByteBuffer initialBuffer, final int maxCapacity) {
        super(maxCapacity);
        if (alloc == null) {
            throw new NullPointerException("alloc");
        }
        if (initialBuffer == null) {
            throw new NullPointerException("initialBuffer");
        }
        if (!initialBuffer.isDirect()) {
            throw new IllegalArgumentException("initialBuffer is not a direct buffer.");
        }
        if (initialBuffer.isReadOnly()) {
            throw new IllegalArgumentException("initialBuffer is a read-only buffer.");
        }
        final int initialCapacity = initialBuffer.remaining();
        if (initialCapacity > maxCapacity) {
            throw new IllegalArgumentException(String.format("initialCapacity(%d) > maxCapacity(%d)", initialCapacity, maxCapacity));
        }
        this.alloc = alloc;
        this.doNotFree = true;
        this.setByteBuffer(initialBuffer.slice().order(ByteOrder.BIG_ENDIAN));
        this.writerIndex(initialCapacity);
    }
    
    protected ByteBuffer allocateDirect(final int initialCapacity) {
        return ByteBuffer.allocateDirect(initialCapacity);
    }
    
    protected void freeDirect(final ByteBuffer buffer) {
        PlatformDependent.freeDirectBuffer(buffer);
    }
    
    private void setByteBuffer(final ByteBuffer buffer) {
        final ByteBuffer oldBuffer = this.buffer;
        if (oldBuffer != null) {
            if (this.doNotFree) {
                this.doNotFree = false;
            }
            else {
                this.freeDirect(oldBuffer);
            }
        }
        this.buffer = buffer;
        this.memoryAddress = PlatformDependent.directBufferAddress(buffer);
        this.tmpNioBuf = null;
        this.capacity = buffer.remaining();
    }
    
    @Override
    public boolean isDirect() {
        return true;
    }
    
    @Override
    public int capacity() {
        return this.capacity;
    }
    
    @Override
    public ByteBuf capacity(final int newCapacity) {
        this.ensureAccessible();
        if (newCapacity < 0 || newCapacity > this.maxCapacity()) {
            throw new IllegalArgumentException("newCapacity: " + newCapacity);
        }
        final int readerIndex = this.readerIndex();
        int writerIndex = this.writerIndex();
        final int oldCapacity = this.capacity;
        if (newCapacity > oldCapacity) {
            final ByteBuffer oldBuffer = this.buffer;
            final ByteBuffer newBuffer = this.allocateDirect(newCapacity);
            oldBuffer.position(0).limit(oldBuffer.capacity());
            newBuffer.position(0).limit(oldBuffer.capacity());
            newBuffer.put(oldBuffer);
            newBuffer.clear();
            this.setByteBuffer(newBuffer);
        }
        else if (newCapacity < oldCapacity) {
            final ByteBuffer oldBuffer = this.buffer;
            final ByteBuffer newBuffer = this.allocateDirect(newCapacity);
            if (readerIndex < newCapacity) {
                if (writerIndex > newCapacity) {
                    writerIndex = newCapacity;
                    this.writerIndex(newCapacity);
                }
                oldBuffer.position(readerIndex).limit(writerIndex);
                newBuffer.position(readerIndex).limit(writerIndex);
                newBuffer.put(oldBuffer);
                newBuffer.clear();
            }
            else {
                this.setIndex(newCapacity, newCapacity);
            }
            this.setByteBuffer(newBuffer);
        }
        return this;
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
    public boolean hasArray() {
        return false;
    }
    
    @Override
    public byte[] array() {
        throw new UnsupportedOperationException("direct buffer");
    }
    
    @Override
    public int arrayOffset() {
        throw new UnsupportedOperationException("direct buffer");
    }
    
    @Override
    public boolean hasMemoryAddress() {
        return true;
    }
    
    @Override
    public long memoryAddress() {
        return this.memoryAddress;
    }
    
    @Override
    protected byte _getByte(final int index) {
        return PlatformDependent.getByte(this.addr(index));
    }
    
    @Override
    protected short _getShort(final int index) {
        final short v = PlatformDependent.getShort(this.addr(index));
        return UnpooledUnsafeDirectByteBuf.NATIVE_ORDER ? v : Short.reverseBytes(v);
    }
    
    @Override
    protected int _getUnsignedMedium(final int index) {
        final long addr = this.addr(index);
        return (PlatformDependent.getByte(addr) & 0xFF) << 16 | (PlatformDependent.getByte(addr + 1L) & 0xFF) << 8 | (PlatformDependent.getByte(addr + 2L) & 0xFF);
    }
    
    @Override
    protected int _getInt(final int index) {
        final int v = PlatformDependent.getInt(this.addr(index));
        return UnpooledUnsafeDirectByteBuf.NATIVE_ORDER ? v : Integer.reverseBytes(v);
    }
    
    @Override
    protected long _getLong(final int index) {
        final long v = PlatformDependent.getLong(this.addr(index));
        return UnpooledUnsafeDirectByteBuf.NATIVE_ORDER ? v : Long.reverseBytes(v);
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuf dst, final int dstIndex, final int length) {
        this.checkIndex(index, length);
        if (dst == null) {
            throw new NullPointerException("dst");
        }
        if (dstIndex < 0 || dstIndex > dst.capacity() - length) {
            throw new IndexOutOfBoundsException("dstIndex: " + dstIndex);
        }
        if (dst.hasMemoryAddress()) {
            PlatformDependent.copyMemory(this.addr(index), dst.memoryAddress() + dstIndex, length);
        }
        else if (dst.hasArray()) {
            PlatformDependent.copyMemory(this.addr(index), dst.array(), dst.arrayOffset() + dstIndex, length);
        }
        else {
            dst.setBytes(dstIndex, this, index, length);
        }
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final byte[] dst, final int dstIndex, final int length) {
        this.checkIndex(index, length);
        if (dst == null) {
            throw new NullPointerException("dst");
        }
        if (dstIndex < 0 || dstIndex > dst.length - length) {
            throw new IndexOutOfBoundsException(String.format("dstIndex: %d, length: %d (expected: range(0, %d))", dstIndex, length, dst.length));
        }
        if (length != 0) {
            PlatformDependent.copyMemory(this.addr(index), dst, dstIndex, length);
        }
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuffer dst) {
        this.getBytes(index, dst, false);
        return this;
    }
    
    private void getBytes(final int index, final ByteBuffer dst, final boolean internal) {
        this.checkIndex(index);
        if (dst == null) {
            throw new NullPointerException("dst");
        }
        final int bytesToCopy = Math.min(this.capacity() - index, dst.remaining());
        ByteBuffer tmpBuf;
        if (internal) {
            tmpBuf = this.internalNioBuffer();
        }
        else {
            tmpBuf = this.buffer.duplicate();
        }
        tmpBuf.clear().position(index).limit(index + bytesToCopy);
        dst.put(tmpBuf);
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuffer dst) {
        final int length = dst.remaining();
        this.checkReadableBytes(length);
        this.getBytes(this.readerIndex, dst, true);
        this.readerIndex += length;
        return this;
    }
    
    @Override
    protected void _setByte(final int index, final int value) {
        PlatformDependent.putByte(this.addr(index), (byte)value);
    }
    
    @Override
    protected void _setShort(final int index, final int value) {
        PlatformDependent.putShort(this.addr(index), UnpooledUnsafeDirectByteBuf.NATIVE_ORDER ? ((short)value) : Short.reverseBytes((short)value));
    }
    
    @Override
    protected void _setMedium(final int index, final int value) {
        final long addr = this.addr(index);
        PlatformDependent.putByte(addr, (byte)(value >>> 16));
        PlatformDependent.putByte(addr + 1L, (byte)(value >>> 8));
        PlatformDependent.putByte(addr + 2L, (byte)value);
    }
    
    @Override
    protected void _setInt(final int index, final int value) {
        PlatformDependent.putInt(this.addr(index), UnpooledUnsafeDirectByteBuf.NATIVE_ORDER ? value : Integer.reverseBytes(value));
    }
    
    @Override
    protected void _setLong(final int index, final long value) {
        PlatformDependent.putLong(this.addr(index), UnpooledUnsafeDirectByteBuf.NATIVE_ORDER ? value : Long.reverseBytes(value));
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuf src, final int srcIndex, final int length) {
        this.checkIndex(index, length);
        if (src == null) {
            throw new NullPointerException("src");
        }
        if (srcIndex < 0 || srcIndex > src.capacity() - length) {
            throw new IndexOutOfBoundsException("srcIndex: " + srcIndex);
        }
        if (length != 0) {
            if (src.hasMemoryAddress()) {
                PlatformDependent.copyMemory(src.memoryAddress() + srcIndex, this.addr(index), length);
            }
            else if (src.hasArray()) {
                PlatformDependent.copyMemory(src.array(), src.arrayOffset() + srcIndex, this.addr(index), length);
            }
            else {
                src.getBytes(srcIndex, this, index, length);
            }
        }
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final byte[] src, final int srcIndex, final int length) {
        this.checkIndex(index, length);
        if (length != 0) {
            PlatformDependent.copyMemory(src, srcIndex, this.addr(index), length);
        }
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, ByteBuffer src) {
        this.ensureAccessible();
        final ByteBuffer tmpBuf = this.internalNioBuffer();
        if (src == tmpBuf) {
            src = src.duplicate();
        }
        tmpBuf.clear().position(index).limit(index + src.remaining());
        tmpBuf.put(src);
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final OutputStream out, final int length) throws IOException {
        this.ensureAccessible();
        if (length != 0) {
            final byte[] tmp = new byte[length];
            PlatformDependent.copyMemory(this.addr(index), tmp, 0, length);
            out.write(tmp);
        }
        return this;
    }
    
    @Override
    public int getBytes(final int index, final GatheringByteChannel out, final int length) throws IOException {
        return this.getBytes(index, out, length, false);
    }
    
    private int getBytes(final int index, final GatheringByteChannel out, final int length, final boolean internal) throws IOException {
        this.ensureAccessible();
        if (length == 0) {
            return 0;
        }
        ByteBuffer tmpBuf;
        if (internal) {
            tmpBuf = this.internalNioBuffer();
        }
        else {
            tmpBuf = this.buffer.duplicate();
        }
        tmpBuf.clear().position(index).limit(index + length);
        return out.write(tmpBuf);
    }
    
    @Override
    public int readBytes(final GatheringByteChannel out, final int length) throws IOException {
        this.checkReadableBytes(length);
        final int readBytes = this.getBytes(this.readerIndex, out, length, true);
        this.readerIndex += readBytes;
        return readBytes;
    }
    
    @Override
    public int setBytes(final int index, final InputStream in, final int length) throws IOException {
        this.checkIndex(index, length);
        final byte[] tmp = new byte[length];
        final int readBytes = in.read(tmp);
        if (readBytes > 0) {
            PlatformDependent.copyMemory(tmp, 0, this.addr(index), readBytes);
        }
        return readBytes;
    }
    
    @Override
    public int setBytes(final int index, final ScatteringByteChannel in, final int length) throws IOException {
        this.ensureAccessible();
        final ByteBuffer tmpBuf = this.internalNioBuffer();
        tmpBuf.clear().position(index).limit(index + length);
        try {
            return in.read(tmpBuf);
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
    public ByteBuffer[] nioBuffers(final int index, final int length) {
        return new ByteBuffer[] { this.nioBuffer(index, length) };
    }
    
    @Override
    public ByteBuf copy(final int index, final int length) {
        this.checkIndex(index, length);
        final ByteBuf copy = this.alloc().directBuffer(length, this.maxCapacity());
        if (length != 0) {
            if (copy.hasMemoryAddress()) {
                PlatformDependent.copyMemory(this.addr(index), copy.memoryAddress(), length);
                copy.setIndex(0, length);
            }
            else {
                copy.writeBytes(this, index, length);
            }
        }
        return copy;
    }
    
    @Override
    public ByteBuffer internalNioBuffer(final int index, final int length) {
        this.checkIndex(index, length);
        return (ByteBuffer)this.internalNioBuffer().clear().position(index).limit(index + length);
    }
    
    private ByteBuffer internalNioBuffer() {
        ByteBuffer tmpNioBuf = this.tmpNioBuf;
        if (tmpNioBuf == null) {
            tmpNioBuf = (this.tmpNioBuf = this.buffer.duplicate());
        }
        return tmpNioBuf;
    }
    
    @Override
    public ByteBuffer nioBuffer(final int index, final int length) {
        this.checkIndex(index, length);
        return ((ByteBuffer)this.buffer.duplicate().position(index).limit(index + length)).slice();
    }
    
    @Override
    protected void deallocate() {
        final ByteBuffer buffer = this.buffer;
        if (buffer == null) {
            return;
        }
        this.buffer = null;
        if (!this.doNotFree) {
            this.freeDirect(buffer);
        }
    }
    
    @Override
    public ByteBuf unwrap() {
        return null;
    }
    
    long addr(final int index) {
        return this.memoryAddress + index;
    }
    
    @Override
    protected SwappedByteBuf newSwappedByteBuf() {
        return new UnsafeDirectSwappedByteBuf(this);
    }
    
    static {
        NATIVE_ORDER = (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN);
    }
}

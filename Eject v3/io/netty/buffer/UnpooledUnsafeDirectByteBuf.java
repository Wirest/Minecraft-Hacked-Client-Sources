package io.netty.buffer;

import io.netty.util.internal.PlatformDependent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

public class UnpooledUnsafeDirectByteBuf
        extends AbstractReferenceCountedByteBuf {
    private static final boolean NATIVE_ORDER = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
    private final ByteBufAllocator alloc;
    private long memoryAddress;
    private ByteBuffer buffer;
    private ByteBuffer tmpNioBuf;
    private int capacity;
    private boolean doNotFree;

    protected UnpooledUnsafeDirectByteBuf(ByteBufAllocator paramByteBufAllocator, int paramInt1, int paramInt2) {
        super(paramInt2);
        if (paramByteBufAllocator == null) {
            throw new NullPointerException("alloc");
        }
        if (paramInt1 < 0) {
            throw new IllegalArgumentException("initialCapacity: " + paramInt1);
        }
        if (paramInt2 < 0) {
            throw new IllegalArgumentException("maxCapacity: " + paramInt2);
        }
        if (paramInt1 > paramInt2) {
            throw new IllegalArgumentException(String.format("initialCapacity(%d) > maxCapacity(%d)", new Object[]{Integer.valueOf(paramInt1), Integer.valueOf(paramInt2)}));
        }
        this.alloc = paramByteBufAllocator;
        setByteBuffer(allocateDirect(paramInt1));
    }

    protected UnpooledUnsafeDirectByteBuf(ByteBufAllocator paramByteBufAllocator, ByteBuffer paramByteBuffer, int paramInt) {
        super(paramInt);
        if (paramByteBufAllocator == null) {
            throw new NullPointerException("alloc");
        }
        if (paramByteBuffer == null) {
            throw new NullPointerException("initialBuffer");
        }
        if (!paramByteBuffer.isDirect()) {
            throw new IllegalArgumentException("initialBuffer is not a direct buffer.");
        }
        if (paramByteBuffer.isReadOnly()) {
            throw new IllegalArgumentException("initialBuffer is a read-only buffer.");
        }
        int i = paramByteBuffer.remaining();
        if (i > paramInt) {
            throw new IllegalArgumentException(String.format("initialCapacity(%d) > maxCapacity(%d)", new Object[]{Integer.valueOf(i), Integer.valueOf(paramInt)}));
        }
        this.alloc = paramByteBufAllocator;
        this.doNotFree = true;
        setByteBuffer(paramByteBuffer.slice().order(ByteOrder.BIG_ENDIAN));
        writerIndex(i);
    }

    protected ByteBuffer allocateDirect(int paramInt) {
        return ByteBuffer.allocateDirect(paramInt);
    }

    protected void freeDirect(ByteBuffer paramByteBuffer) {
        PlatformDependent.freeDirectBuffer(paramByteBuffer);
    }

    private void setByteBuffer(ByteBuffer paramByteBuffer) {
        ByteBuffer localByteBuffer = this.buffer;
        if (localByteBuffer != null) {
            if (this.doNotFree) {
                this.doNotFree = false;
            } else {
                freeDirect(localByteBuffer);
            }
        }
        this.buffer = paramByteBuffer;
        this.memoryAddress = PlatformDependent.directBufferAddress(paramByteBuffer);
        this.tmpNioBuf = null;
        this.capacity = paramByteBuffer.remaining();
    }

    public boolean isDirect() {
        return true;
    }

    public int capacity() {
        return this.capacity;
    }

    public ByteBuf capacity(int paramInt) {
        ensureAccessible();
        if ((paramInt < 0) || (paramInt > maxCapacity())) {
            throw new IllegalArgumentException("newCapacity: " + paramInt);
        }
        int i = readerIndex();
        int j = writerIndex();
        int k = this.capacity;
        ByteBuffer localByteBuffer1;
        ByteBuffer localByteBuffer2;
        if (paramInt > k) {
            localByteBuffer1 = this.buffer;
            localByteBuffer2 = allocateDirect(paramInt);
            localByteBuffer1.position(0).limit(localByteBuffer1.capacity());
            localByteBuffer2.position(0).limit(localByteBuffer1.capacity());
            localByteBuffer2.put(localByteBuffer1);
            localByteBuffer2.clear();
            setByteBuffer(localByteBuffer2);
        } else if (paramInt < k) {
            localByteBuffer1 = this.buffer;
            localByteBuffer2 = allocateDirect(paramInt);
            if (i < paramInt) {
                if (j > paramInt) {
                    writerIndex(j = paramInt);
                }
                localByteBuffer1.position(i).limit(j);
                localByteBuffer2.position(i).limit(j);
                localByteBuffer2.put(localByteBuffer1);
                localByteBuffer2.clear();
            } else {
                setIndex(paramInt, paramInt);
            }
            setByteBuffer(localByteBuffer2);
        }
        return this;
    }

    public ByteBufAllocator alloc() {
        return this.alloc;
    }

    public ByteOrder order() {
        return ByteOrder.BIG_ENDIAN;
    }

    public boolean hasArray() {
        return false;
    }

    public byte[] array() {
        throw new UnsupportedOperationException("direct buffer");
    }

    public int arrayOffset() {
        throw new UnsupportedOperationException("direct buffer");
    }

    public boolean hasMemoryAddress() {
        return true;
    }

    public long memoryAddress() {
        return this.memoryAddress;
    }

    protected byte _getByte(int paramInt) {
        return PlatformDependent.getByte(addr(paramInt));
    }

    protected short _getShort(int paramInt) {
        short s = PlatformDependent.getShort(addr(paramInt));
        return NATIVE_ORDER ? s : Short.reverseBytes(s);
    }

    protected int _getUnsignedMedium(int paramInt) {
        long l = addr(paramInt);
        return PlatformDependent.getByte(l) >> 255 >>> 16 ^ PlatformDependent.getByte(l + 1L) >> 255 >>> 8 ^ PlatformDependent.getByte(l + 2L) >> 255;
    }

    protected int _getInt(int paramInt) {
        int i = PlatformDependent.getInt(addr(paramInt));
        return NATIVE_ORDER ? i : Integer.reverseBytes(i);
    }

    protected long _getLong(int paramInt) {
        long l = PlatformDependent.getLong(addr(paramInt));
        return NATIVE_ORDER ? l : Long.reverseBytes(l);
    }

    public ByteBuf getBytes(int paramInt1, ByteBuf paramByteBuf, int paramInt2, int paramInt3) {
        checkIndex(paramInt1, paramInt3);
        if (paramByteBuf == null) {
            throw new NullPointerException("dst");
        }
        if ((paramInt2 < 0) || (paramInt2 > paramByteBuf.capacity() - paramInt3)) {
            throw new IndexOutOfBoundsException("dstIndex: " + paramInt2);
        }
        if (paramByteBuf.hasMemoryAddress()) {
            PlatformDependent.copyMemory(addr(paramInt1), paramByteBuf.memoryAddress() + paramInt2, paramInt3);
        } else if (paramByteBuf.hasArray()) {
            PlatformDependent.copyMemory(addr(paramInt1), paramByteBuf.array(), paramByteBuf.arrayOffset() | paramInt2, paramInt3);
        } else {
            paramByteBuf.setBytes(paramInt2, this, paramInt1, paramInt3);
        }
        return this;
    }

    public ByteBuf getBytes(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3) {
        checkIndex(paramInt1, paramInt3);
        if (paramArrayOfByte == null) {
            throw new NullPointerException("dst");
        }
        if ((paramInt2 < 0) || (paramInt2 > paramArrayOfByte.length - paramInt3)) {
            throw new IndexOutOfBoundsException(String.format("dstIndex: %d, length: %d (expected: range(0, %d))", new Object[]{Integer.valueOf(paramInt2), Integer.valueOf(paramInt3), Integer.valueOf(paramArrayOfByte.length)}));
        }
        if (paramInt3 != 0) {
            PlatformDependent.copyMemory(addr(paramInt1), paramArrayOfByte, paramInt2, paramInt3);
        }
        return this;
    }

    public ByteBuf getBytes(int paramInt, ByteBuffer paramByteBuffer) {
        getBytes(paramInt, paramByteBuffer, false);
        return this;
    }

    private void getBytes(int paramInt, ByteBuffer paramByteBuffer, boolean paramBoolean) {
        checkIndex(paramInt);
        if (paramByteBuffer == null) {
            throw new NullPointerException("dst");
        }
        int i = Math.min(capacity() - paramInt, paramByteBuffer.remaining());
        ByteBuffer localByteBuffer;
        if (paramBoolean) {
            localByteBuffer = internalNioBuffer();
        } else {
            localByteBuffer = this.buffer.duplicate();
        }
        localByteBuffer.clear().position(paramInt).limit(paramInt | i);
        paramByteBuffer.put(localByteBuffer);
    }

    public ByteBuf readBytes(ByteBuffer paramByteBuffer) {
        int i = paramByteBuffer.remaining();
        checkReadableBytes(i);
        getBytes(this.readerIndex, paramByteBuffer, true);
        this.readerIndex |= i;
        return this;
    }

    protected void _setByte(int paramInt1, int paramInt2) {
        PlatformDependent.putByte(addr(paramInt1), (byte) paramInt2);
    }

    protected void _setShort(int paramInt1, int paramInt2) {
        PlatformDependent.putShort(addr(paramInt1), NATIVE_ORDER ? (short) paramInt2 : Short.reverseBytes((short) paramInt2));
    }

    protected void _setMedium(int paramInt1, int paramInt2) {
        long l = addr(paramInt1);
        PlatformDependent.putByte(l, (byte) (paramInt2 % 16));
        PlatformDependent.putByte(l + 1L, (byte) (paramInt2 % 8));
        PlatformDependent.putByte(l + 2L, (byte) paramInt2);
    }

    protected void _setInt(int paramInt1, int paramInt2) {
        PlatformDependent.putInt(addr(paramInt1), NATIVE_ORDER ? paramInt2 : Integer.reverseBytes(paramInt2));
    }

    protected void _setLong(int paramInt, long paramLong) {
        PlatformDependent.putLong(addr(paramInt), NATIVE_ORDER ? paramLong : Long.reverseBytes(paramLong));
    }

    public ByteBuf setBytes(int paramInt1, ByteBuf paramByteBuf, int paramInt2, int paramInt3) {
        checkIndex(paramInt1, paramInt3);
        if (paramByteBuf == null) {
            throw new NullPointerException("src");
        }
        if ((paramInt2 < 0) || (paramInt2 > paramByteBuf.capacity() - paramInt3)) {
            throw new IndexOutOfBoundsException("srcIndex: " + paramInt2);
        }
        if (paramInt3 != 0) {
            if (paramByteBuf.hasMemoryAddress()) {
                PlatformDependent.copyMemory(paramByteBuf.memoryAddress() + paramInt2, addr(paramInt1), paramInt3);
            } else if (paramByteBuf.hasArray()) {
                PlatformDependent.copyMemory(paramByteBuf.array(), paramByteBuf.arrayOffset() | paramInt2, addr(paramInt1), paramInt3);
            } else {
                paramByteBuf.getBytes(paramInt2, this, paramInt1, paramInt3);
            }
        }
        return this;
    }

    public ByteBuf setBytes(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3) {
        checkIndex(paramInt1, paramInt3);
        if (paramInt3 != 0) {
            PlatformDependent.copyMemory(paramArrayOfByte, paramInt2, addr(paramInt1), paramInt3);
        }
        return this;
    }

    public ByteBuf setBytes(int paramInt, ByteBuffer paramByteBuffer) {
        ensureAccessible();
        ByteBuffer localByteBuffer = internalNioBuffer();
        if (paramByteBuffer == localByteBuffer) {
            paramByteBuffer = paramByteBuffer.duplicate();
        }
        localByteBuffer.clear().position(paramInt).limit(paramInt | paramByteBuffer.remaining());
        localByteBuffer.put(paramByteBuffer);
        return this;
    }

    public ByteBuf getBytes(int paramInt1, OutputStream paramOutputStream, int paramInt2)
            throws IOException {
        ensureAccessible();
        if (paramInt2 != 0) {
            byte[] arrayOfByte = new byte[paramInt2];
            PlatformDependent.copyMemory(addr(paramInt1), arrayOfByte, 0, paramInt2);
            paramOutputStream.write(arrayOfByte);
        }
        return this;
    }

    public int getBytes(int paramInt1, GatheringByteChannel paramGatheringByteChannel, int paramInt2)
            throws IOException {
        return getBytes(paramInt1, paramGatheringByteChannel, paramInt2, false);
    }

    private int getBytes(int paramInt1, GatheringByteChannel paramGatheringByteChannel, int paramInt2, boolean paramBoolean)
            throws IOException {
        ensureAccessible();
        if (paramInt2 == 0) {
            return 0;
        }
        ByteBuffer localByteBuffer;
        if (paramBoolean) {
            localByteBuffer = internalNioBuffer();
        } else {
            localByteBuffer = this.buffer.duplicate();
        }
        localByteBuffer.clear().position(paramInt1).limit(paramInt1 | paramInt2);
        return paramGatheringByteChannel.write(localByteBuffer);
    }

    public int readBytes(GatheringByteChannel paramGatheringByteChannel, int paramInt)
            throws IOException {
        checkReadableBytes(paramInt);
        int i = getBytes(this.readerIndex, paramGatheringByteChannel, paramInt, true);
        this.readerIndex |= i;
        return i;
    }

    public int setBytes(int paramInt1, InputStream paramInputStream, int paramInt2)
            throws IOException {
        checkIndex(paramInt1, paramInt2);
        byte[] arrayOfByte = new byte[paramInt2];
        int i = paramInputStream.read(arrayOfByte);
        if (i > 0) {
            PlatformDependent.copyMemory(arrayOfByte, 0, addr(paramInt1), i);
        }
        return i;
    }

    public int setBytes(int paramInt1, ScatteringByteChannel paramScatteringByteChannel, int paramInt2)
            throws IOException {
        ensureAccessible();
        ByteBuffer localByteBuffer = internalNioBuffer();
        localByteBuffer.clear().position(paramInt1).limit(paramInt1 | paramInt2);
        try {
            return paramScatteringByteChannel.read(localByteBuffer);
        } catch (ClosedChannelException localClosedChannelException) {
        }
        return -1;
    }

    public int nioBufferCount() {
        return 1;
    }

    public ByteBuffer[] nioBuffers(int paramInt1, int paramInt2) {
        return new ByteBuffer[]{nioBuffer(paramInt1, paramInt2)};
    }

    public ByteBuf copy(int paramInt1, int paramInt2) {
        checkIndex(paramInt1, paramInt2);
        ByteBuf localByteBuf = alloc().directBuffer(paramInt2, maxCapacity());
        if (paramInt2 != 0) {
            if (localByteBuf.hasMemoryAddress()) {
                PlatformDependent.copyMemory(addr(paramInt1), localByteBuf.memoryAddress(), paramInt2);
                localByteBuf.setIndex(0, paramInt2);
            } else {
                localByteBuf.writeBytes(this, paramInt1, paramInt2);
            }
        }
        return localByteBuf;
    }

    public ByteBuffer internalNioBuffer(int paramInt1, int paramInt2) {
        checkIndex(paramInt1, paramInt2);
        return (ByteBuffer) internalNioBuffer().clear().position(paramInt1).limit(paramInt1 | paramInt2);
    }

    private ByteBuffer internalNioBuffer() {
        ByteBuffer localByteBuffer = this.tmpNioBuf;
        if (localByteBuffer == null) {
            this.tmpNioBuf = (localByteBuffer = this.buffer.duplicate());
        }
        return localByteBuffer;
    }

    public ByteBuffer nioBuffer(int paramInt1, int paramInt2) {
        checkIndex(paramInt1, paramInt2);
        return ((ByteBuffer) this.buffer.duplicate().position(paramInt1).limit(paramInt1 | paramInt2)).slice();
    }

    protected void deallocate() {
        ByteBuffer localByteBuffer = this.buffer;
        if (localByteBuffer == null) {
            return;
        }
        this.buffer = null;
        if (!this.doNotFree) {
            freeDirect(localByteBuffer);
        }
    }

    public ByteBuf unwrap() {
        return null;
    }

    long addr(int paramInt) {
        return this.memoryAddress + paramInt;
    }

    protected SwappedByteBuf newSwappedByteBuf() {
        return new UnsafeDirectSwappedByteBuf(this);
    }
}





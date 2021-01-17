// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

import io.netty.util.Recycler;
import java.util.Locale;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.PlatformDependent;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.ByteBuffer;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CharacterCodingException;
import io.netty.util.CharsetUtil;
import java.nio.charset.Charset;
import java.nio.CharBuffer;
import java.nio.ByteOrder;
import io.netty.util.internal.logging.InternalLogger;

public final class ByteBufUtil
{
    private static final InternalLogger logger;
    private static final char[] HEXDUMP_TABLE;
    static final ByteBufAllocator DEFAULT_ALLOCATOR;
    private static final int THREAD_LOCAL_BUFFER_SIZE;
    
    public static String hexDump(final ByteBuf buffer) {
        return hexDump(buffer, buffer.readerIndex(), buffer.readableBytes());
    }
    
    public static String hexDump(final ByteBuf buffer, final int fromIndex, final int length) {
        if (length < 0) {
            throw new IllegalArgumentException("length: " + length);
        }
        if (length == 0) {
            return "";
        }
        final int endIndex = fromIndex + length;
        final char[] buf = new char[length << 1];
        for (int srcIdx = fromIndex, dstIdx = 0; srcIdx < endIndex; ++srcIdx, dstIdx += 2) {
            System.arraycopy(ByteBufUtil.HEXDUMP_TABLE, buffer.getUnsignedByte(srcIdx) << 1, buf, dstIdx, 2);
        }
        return new String(buf);
    }
    
    public static int hashCode(final ByteBuf buffer) {
        final int aLen = buffer.readableBytes();
        final int intCount = aLen >>> 2;
        final int byteCount = aLen & 0x3;
        int hashCode = 1;
        int arrayIndex = buffer.readerIndex();
        if (buffer.order() == ByteOrder.BIG_ENDIAN) {
            for (int i = intCount; i > 0; --i) {
                hashCode = 31 * hashCode + buffer.getInt(arrayIndex);
                arrayIndex += 4;
            }
        }
        else {
            for (int i = intCount; i > 0; --i) {
                hashCode = 31 * hashCode + swapInt(buffer.getInt(arrayIndex));
                arrayIndex += 4;
            }
        }
        for (int i = byteCount; i > 0; --i) {
            hashCode = 31 * hashCode + buffer.getByte(arrayIndex++);
        }
        if (hashCode == 0) {
            hashCode = 1;
        }
        return hashCode;
    }
    
    public static boolean equals(final ByteBuf bufferA, final ByteBuf bufferB) {
        final int aLen = bufferA.readableBytes();
        if (aLen != bufferB.readableBytes()) {
            return false;
        }
        final int longCount = aLen >>> 3;
        final int byteCount = aLen & 0x7;
        int aIndex = bufferA.readerIndex();
        int bIndex = bufferB.readerIndex();
        if (bufferA.order() == bufferB.order()) {
            for (int i = longCount; i > 0; --i) {
                if (bufferA.getLong(aIndex) != bufferB.getLong(bIndex)) {
                    return false;
                }
                aIndex += 8;
                bIndex += 8;
            }
        }
        else {
            for (int i = longCount; i > 0; --i) {
                if (bufferA.getLong(aIndex) != swapLong(bufferB.getLong(bIndex))) {
                    return false;
                }
                aIndex += 8;
                bIndex += 8;
            }
        }
        for (int i = byteCount; i > 0; --i) {
            if (bufferA.getByte(aIndex) != bufferB.getByte(bIndex)) {
                return false;
            }
            ++aIndex;
            ++bIndex;
        }
        return true;
    }
    
    public static int compare(final ByteBuf bufferA, final ByteBuf bufferB) {
        final int aLen = bufferA.readableBytes();
        final int bLen = bufferB.readableBytes();
        final int minLength = Math.min(aLen, bLen);
        final int uintCount = minLength >>> 2;
        final int byteCount = minLength & 0x3;
        int aIndex = bufferA.readerIndex();
        int bIndex = bufferB.readerIndex();
        if (bufferA.order() == bufferB.order()) {
            for (int i = uintCount; i > 0; --i) {
                final long va = bufferA.getUnsignedInt(aIndex);
                final long vb = bufferB.getUnsignedInt(bIndex);
                if (va > vb) {
                    return 1;
                }
                if (va < vb) {
                    return -1;
                }
                aIndex += 4;
                bIndex += 4;
            }
        }
        else {
            for (int i = uintCount; i > 0; --i) {
                final long va = bufferA.getUnsignedInt(aIndex);
                final long vb = (long)swapInt(bufferB.getInt(bIndex)) & 0xFFFFFFFFL;
                if (va > vb) {
                    return 1;
                }
                if (va < vb) {
                    return -1;
                }
                aIndex += 4;
                bIndex += 4;
            }
        }
        for (int i = byteCount; i > 0; --i) {
            final short va2 = bufferA.getUnsignedByte(aIndex);
            final short vb2 = bufferB.getUnsignedByte(bIndex);
            if (va2 > vb2) {
                return 1;
            }
            if (va2 < vb2) {
                return -1;
            }
            ++aIndex;
            ++bIndex;
        }
        return aLen - bLen;
    }
    
    public static int indexOf(final ByteBuf buffer, final int fromIndex, final int toIndex, final byte value) {
        if (fromIndex <= toIndex) {
            return firstIndexOf(buffer, fromIndex, toIndex, value);
        }
        return lastIndexOf(buffer, fromIndex, toIndex, value);
    }
    
    public static short swapShort(final short value) {
        return Short.reverseBytes(value);
    }
    
    public static int swapMedium(final int value) {
        int swapped = (value << 16 & 0xFF0000) | (value & 0xFF00) | (value >>> 16 & 0xFF);
        if ((swapped & 0x800000) != 0x0) {
            swapped |= 0xFF000000;
        }
        return swapped;
    }
    
    public static int swapInt(final int value) {
        return Integer.reverseBytes(value);
    }
    
    public static long swapLong(final long value) {
        return Long.reverseBytes(value);
    }
    
    public static ByteBuf readBytes(final ByteBufAllocator alloc, final ByteBuf buffer, final int length) {
        boolean release = true;
        final ByteBuf dst = alloc.buffer(length);
        try {
            buffer.readBytes(dst);
            release = false;
            return dst;
        }
        finally {
            if (release) {
                dst.release();
            }
        }
    }
    
    private static int firstIndexOf(final ByteBuf buffer, int fromIndex, final int toIndex, final byte value) {
        fromIndex = Math.max(fromIndex, 0);
        if (fromIndex >= toIndex || buffer.capacity() == 0) {
            return -1;
        }
        for (int i = fromIndex; i < toIndex; ++i) {
            if (buffer.getByte(i) == value) {
                return i;
            }
        }
        return -1;
    }
    
    private static int lastIndexOf(final ByteBuf buffer, int fromIndex, final int toIndex, final byte value) {
        fromIndex = Math.min(fromIndex, buffer.capacity());
        if (fromIndex < 0 || buffer.capacity() == 0) {
            return -1;
        }
        for (int i = fromIndex - 1; i >= toIndex; --i) {
            if (buffer.getByte(i) == value) {
                return i;
            }
        }
        return -1;
    }
    
    public static ByteBuf encodeString(final ByteBufAllocator alloc, final CharBuffer src, final Charset charset) {
        return encodeString0(alloc, false, src, charset);
    }
    
    static ByteBuf encodeString0(final ByteBufAllocator alloc, final boolean enforceHeap, final CharBuffer src, final Charset charset) {
        final CharsetEncoder encoder = CharsetUtil.getEncoder(charset);
        final int length = (int)(src.remaining() * (double)encoder.maxBytesPerChar());
        boolean release = true;
        ByteBuf dst;
        if (enforceHeap) {
            dst = alloc.heapBuffer(length);
        }
        else {
            dst = alloc.buffer(length);
        }
        try {
            final ByteBuffer dstBuf = dst.internalNioBuffer(0, length);
            final int pos = dstBuf.position();
            CoderResult cr = encoder.encode(src, dstBuf, true);
            if (!cr.isUnderflow()) {
                cr.throwException();
            }
            cr = encoder.flush(dstBuf);
            if (!cr.isUnderflow()) {
                cr.throwException();
            }
            dst.writerIndex(dst.writerIndex() + dstBuf.position() - pos);
            release = false;
            return dst;
        }
        catch (CharacterCodingException x) {
            throw new IllegalStateException(x);
        }
        finally {
            if (release) {
                dst.release();
            }
        }
    }
    
    static String decodeString(final ByteBuffer src, final Charset charset) {
        final CharsetDecoder decoder = CharsetUtil.getDecoder(charset);
        final CharBuffer dst = CharBuffer.allocate((int)(src.remaining() * (double)decoder.maxCharsPerByte()));
        try {
            CoderResult cr = decoder.decode(src, dst, true);
            if (!cr.isUnderflow()) {
                cr.throwException();
            }
            cr = decoder.flush(dst);
            if (!cr.isUnderflow()) {
                cr.throwException();
            }
        }
        catch (CharacterCodingException x) {
            throw new IllegalStateException(x);
        }
        return dst.flip().toString();
    }
    
    public static ByteBuf threadLocalDirectBuffer() {
        if (ByteBufUtil.THREAD_LOCAL_BUFFER_SIZE <= 0) {
            return null;
        }
        if (PlatformDependent.hasUnsafe()) {
            return ThreadLocalUnsafeDirectByteBuf.newInstance();
        }
        return ThreadLocalDirectByteBuf.newInstance();
    }
    
    private ByteBufUtil() {
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(ByteBufUtil.class);
        HEXDUMP_TABLE = new char[1024];
        final char[] DIGITS = "0123456789abcdef".toCharArray();
        for (int i = 0; i < 256; ++i) {
            ByteBufUtil.HEXDUMP_TABLE[i << 1] = DIGITS[i >>> 4 & 0xF];
            ByteBufUtil.HEXDUMP_TABLE[(i << 1) + 1] = DIGITS[i & 0xF];
        }
        final String allocType = SystemPropertyUtil.get("io.netty.allocator.type", "unpooled").toLowerCase(Locale.US).trim();
        ByteBufAllocator alloc;
        if ("unpooled".equals(allocType)) {
            alloc = UnpooledByteBufAllocator.DEFAULT;
            ByteBufUtil.logger.debug("-Dio.netty.allocator.type: {}", allocType);
        }
        else if ("pooled".equals(allocType)) {
            alloc = PooledByteBufAllocator.DEFAULT;
            ByteBufUtil.logger.debug("-Dio.netty.allocator.type: {}", allocType);
        }
        else {
            alloc = UnpooledByteBufAllocator.DEFAULT;
            ByteBufUtil.logger.debug("-Dio.netty.allocator.type: unpooled (unknown: {})", allocType);
        }
        DEFAULT_ALLOCATOR = alloc;
        THREAD_LOCAL_BUFFER_SIZE = SystemPropertyUtil.getInt("io.netty.threadLocalDirectBufferSize", 65536);
        ByteBufUtil.logger.debug("-Dio.netty.threadLocalDirectBufferSize: {}", (Object)ByteBufUtil.THREAD_LOCAL_BUFFER_SIZE);
    }
    
    static final class ThreadLocalUnsafeDirectByteBuf extends UnpooledUnsafeDirectByteBuf
    {
        private static final Recycler<ThreadLocalUnsafeDirectByteBuf> RECYCLER;
        private final Recycler.Handle handle;
        
        static ThreadLocalUnsafeDirectByteBuf newInstance() {
            final ThreadLocalUnsafeDirectByteBuf buf = ThreadLocalUnsafeDirectByteBuf.RECYCLER.get();
            buf.setRefCnt(1);
            return buf;
        }
        
        private ThreadLocalUnsafeDirectByteBuf(final Recycler.Handle handle) {
            super(UnpooledByteBufAllocator.DEFAULT, 256, Integer.MAX_VALUE);
            this.handle = handle;
        }
        
        @Override
        protected void deallocate() {
            if (this.capacity() > ByteBufUtil.THREAD_LOCAL_BUFFER_SIZE) {
                super.deallocate();
            }
            else {
                this.clear();
                ThreadLocalUnsafeDirectByteBuf.RECYCLER.recycle(this, this.handle);
            }
        }
        
        static {
            RECYCLER = new Recycler<ThreadLocalUnsafeDirectByteBuf>() {
                @Override
                protected ThreadLocalUnsafeDirectByteBuf newObject(final Handle handle) {
                    return new ThreadLocalUnsafeDirectByteBuf(handle);
                }
            };
        }
    }
    
    static final class ThreadLocalDirectByteBuf extends UnpooledDirectByteBuf
    {
        private static final Recycler<ThreadLocalDirectByteBuf> RECYCLER;
        private final Recycler.Handle handle;
        
        static ThreadLocalDirectByteBuf newInstance() {
            final ThreadLocalDirectByteBuf buf = ThreadLocalDirectByteBuf.RECYCLER.get();
            buf.setRefCnt(1);
            return buf;
        }
        
        private ThreadLocalDirectByteBuf(final Recycler.Handle handle) {
            super(UnpooledByteBufAllocator.DEFAULT, 256, Integer.MAX_VALUE);
            this.handle = handle;
        }
        
        @Override
        protected void deallocate() {
            if (this.capacity() > ByteBufUtil.THREAD_LOCAL_BUFFER_SIZE) {
                super.deallocate();
            }
            else {
                this.clear();
                ThreadLocalDirectByteBuf.RECYCLER.recycle(this, this.handle);
            }
        }
        
        static {
            RECYCLER = new Recycler<ThreadLocalDirectByteBuf>() {
                @Override
                protected ThreadLocalDirectByteBuf newObject(final Handle handle) {
                    return new ThreadLocalDirectByteBuf(handle);
                }
            };
        }
    }
}

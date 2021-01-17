// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl;

import java.nio.Buffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.LongBuffer;
import java.nio.IntBuffer;
import java.nio.CharBuffer;
import java.nio.ShortBuffer;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;

public final class BufferUtils
{
    public static ByteBuffer createByteBuffer(final int size) {
        return ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
    }
    
    public static ShortBuffer createShortBuffer(final int size) {
        return createByteBuffer(size << 1).asShortBuffer();
    }
    
    public static CharBuffer createCharBuffer(final int size) {
        return createByteBuffer(size << 1).asCharBuffer();
    }
    
    public static IntBuffer createIntBuffer(final int size) {
        return createByteBuffer(size << 2).asIntBuffer();
    }
    
    public static LongBuffer createLongBuffer(final int size) {
        return createByteBuffer(size << 3).asLongBuffer();
    }
    
    public static FloatBuffer createFloatBuffer(final int size) {
        return createByteBuffer(size << 2).asFloatBuffer();
    }
    
    public static DoubleBuffer createDoubleBuffer(final int size) {
        return createByteBuffer(size << 3).asDoubleBuffer();
    }
    
    public static PointerBuffer createPointerBuffer(final int size) {
        return PointerBuffer.allocateDirect(size);
    }
    
    public static int getElementSizeExponent(final Buffer buf) {
        if (buf instanceof ByteBuffer) {
            return 0;
        }
        if (buf instanceof ShortBuffer || buf instanceof CharBuffer) {
            return 1;
        }
        if (buf instanceof FloatBuffer || buf instanceof IntBuffer) {
            return 2;
        }
        if (buf instanceof LongBuffer || buf instanceof DoubleBuffer) {
            return 3;
        }
        throw new IllegalStateException("Unsupported buffer type: " + buf);
    }
    
    public static int getOffset(final Buffer buffer) {
        return buffer.position() << getElementSizeExponent(buffer);
    }
    
    public static void zeroBuffer(final ByteBuffer b) {
        zeroBuffer0(b, b.position(), b.remaining());
    }
    
    public static void zeroBuffer(final ShortBuffer b) {
        zeroBuffer0(b, b.position() * 2L, b.remaining() * 2L);
    }
    
    public static void zeroBuffer(final CharBuffer b) {
        zeroBuffer0(b, b.position() * 2L, b.remaining() * 2L);
    }
    
    public static void zeroBuffer(final IntBuffer b) {
        zeroBuffer0(b, b.position() * 4L, b.remaining() * 4L);
    }
    
    public static void zeroBuffer(final FloatBuffer b) {
        zeroBuffer0(b, b.position() * 4L, b.remaining() * 4L);
    }
    
    public static void zeroBuffer(final LongBuffer b) {
        zeroBuffer0(b, b.position() * 8L, b.remaining() * 8L);
    }
    
    public static void zeroBuffer(final DoubleBuffer b) {
        zeroBuffer0(b, b.position() * 8L, b.remaining() * 8L);
    }
    
    private static native void zeroBuffer0(final Buffer p0, final long p1, final long p2);
    
    static native long getBufferAddress(final Buffer p0);
}

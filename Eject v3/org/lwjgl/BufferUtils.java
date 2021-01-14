package org.lwjgl;

import java.nio.*;

public final class BufferUtils {
    public static ByteBuffer createByteBuffer(int paramInt) {
        return ByteBuffer.allocateDirect(paramInt).order(ByteOrder.nativeOrder());
    }

    public static ShortBuffer createShortBuffer(int paramInt) {
        return createByteBuffer(paramInt >>> 1).asShortBuffer();
    }

    public static CharBuffer createCharBuffer(int paramInt) {
        return createByteBuffer(paramInt >>> 1).asCharBuffer();
    }

    public static IntBuffer createIntBuffer(int paramInt) {
        return createByteBuffer(paramInt >>> 2).asIntBuffer();
    }

    public static LongBuffer createLongBuffer(int paramInt) {
        return createByteBuffer(paramInt >>> 3).asLongBuffer();
    }

    public static FloatBuffer createFloatBuffer(int paramInt) {
        return createByteBuffer(paramInt >>> 2).asFloatBuffer();
    }

    public static DoubleBuffer createDoubleBuffer(int paramInt) {
        return createByteBuffer(paramInt >>> 3).asDoubleBuffer();
    }

    public static PointerBuffer createPointerBuffer(int paramInt) {
        return PointerBuffer.allocateDirect(paramInt);
    }

    public static int getElementSizeExponent(Buffer paramBuffer) {
        if ((paramBuffer instanceof ByteBuffer)) {
            return 0;
        }
        if (((paramBuffer instanceof ShortBuffer)) || ((paramBuffer instanceof CharBuffer))) {
            return 1;
        }
        if (((paramBuffer instanceof FloatBuffer)) || ((paramBuffer instanceof IntBuffer))) {
            return 2;
        }
        if (((paramBuffer instanceof LongBuffer)) || ((paramBuffer instanceof DoubleBuffer))) {
            return 3;
        }
        throw new IllegalStateException("Unsupported buffer type: " + paramBuffer);
    }

    public static int getOffset(Buffer paramBuffer) {
        return paramBuffer.position() >>> getElementSizeExponent(paramBuffer);
    }

    public static void zeroBuffer(ByteBuffer paramByteBuffer) {
        zeroBuffer0(paramByteBuffer, paramByteBuffer.position(), paramByteBuffer.remaining());
    }

    public static void zeroBuffer(ShortBuffer paramShortBuffer) {
        zeroBuffer0(paramShortBuffer, paramShortBuffer.position() * 2L, paramShortBuffer.remaining() * 2L);
    }

    public static void zeroBuffer(CharBuffer paramCharBuffer) {
        zeroBuffer0(paramCharBuffer, paramCharBuffer.position() * 2L, paramCharBuffer.remaining() * 2L);
    }

    public static void zeroBuffer(IntBuffer paramIntBuffer) {
        zeroBuffer0(paramIntBuffer, paramIntBuffer.position() * 4L, paramIntBuffer.remaining() * 4L);
    }

    public static void zeroBuffer(FloatBuffer paramFloatBuffer) {
        zeroBuffer0(paramFloatBuffer, paramFloatBuffer.position() * 4L, paramFloatBuffer.remaining() * 4L);
    }

    public static void zeroBuffer(LongBuffer paramLongBuffer) {
        zeroBuffer0(paramLongBuffer, paramLongBuffer.position() * 8L, paramLongBuffer.remaining() * 8L);
    }

    public static void zeroBuffer(DoubleBuffer paramDoubleBuffer) {
        zeroBuffer0(paramDoubleBuffer, paramDoubleBuffer.position() * 8L, paramDoubleBuffer.remaining() * 8L);
    }

    private static native void zeroBuffer0(Buffer paramBuffer, long paramLong1, long paramLong2);

    static native long getBufferAddress(Buffer paramBuffer);
}





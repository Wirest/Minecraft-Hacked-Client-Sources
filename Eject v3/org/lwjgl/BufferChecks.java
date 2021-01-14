package org.lwjgl;

import java.nio.*;

public class BufferChecks {
    public static void checkFunctionAddress(long paramLong) {
        if ((LWJGLUtil.CHECKS) && (paramLong == 0L)) {
            throw new IllegalStateException("Function is not supported");
        }
    }

    public static void checkNullTerminated(ByteBuffer paramByteBuffer) {
        if ((LWJGLUtil.CHECKS) && (paramByteBuffer.get(paramByteBuffer.limit() - 1) != 0)) {
            throw new IllegalArgumentException("Missing null termination");
        }
    }

    public static void checkNullTerminated(ByteBuffer paramByteBuffer, int paramInt) {
        if (LWJGLUtil.CHECKS) {
            int i = 0;
            for (int j = paramByteBuffer.position(); j < paramByteBuffer.limit(); j++) {
                if (paramByteBuffer.get(j) == 0) {
                    i++;
                }
            }
            if (i < paramInt) {
                throw new IllegalArgumentException("Missing null termination");
            }
        }
    }

    public static void checkNullTerminated(IntBuffer paramIntBuffer) {
        if ((LWJGLUtil.CHECKS) && (paramIntBuffer.get(paramIntBuffer.limit() - 1) != 0)) {
            throw new IllegalArgumentException("Missing null termination");
        }
    }

    public static void checkNullTerminated(LongBuffer paramLongBuffer) {
        if ((LWJGLUtil.CHECKS) && (paramLongBuffer.get(paramLongBuffer.limit() - 1) != 0L)) {
            throw new IllegalArgumentException("Missing null termination");
        }
    }

    public static void checkNullTerminated(PointerBuffer paramPointerBuffer) {
        if ((LWJGLUtil.CHECKS) && (paramPointerBuffer.get(paramPointerBuffer.limit() - 1) != 0L)) {
            throw new IllegalArgumentException("Missing null termination");
        }
    }

    public static void checkNotNull(Object paramObject) {
        if ((LWJGLUtil.CHECKS) && (paramObject == null)) {
            throw new IllegalArgumentException("Null argument");
        }
    }

    public static void checkDirect(ByteBuffer paramByteBuffer) {
        if ((LWJGLUtil.CHECKS) && (!paramByteBuffer.isDirect())) {
            throw new IllegalArgumentException("ByteBuffer is not direct");
        }
    }

    public static void checkDirect(ShortBuffer paramShortBuffer) {
        if ((LWJGLUtil.CHECKS) && (!paramShortBuffer.isDirect())) {
            throw new IllegalArgumentException("ShortBuffer is not direct");
        }
    }

    public static void checkDirect(IntBuffer paramIntBuffer) {
        if ((LWJGLUtil.CHECKS) && (!paramIntBuffer.isDirect())) {
            throw new IllegalArgumentException("IntBuffer is not direct");
        }
    }

    public static void checkDirect(LongBuffer paramLongBuffer) {
        if ((LWJGLUtil.CHECKS) && (!paramLongBuffer.isDirect())) {
            throw new IllegalArgumentException("LongBuffer is not direct");
        }
    }

    public static void checkDirect(FloatBuffer paramFloatBuffer) {
        if ((LWJGLUtil.CHECKS) && (!paramFloatBuffer.isDirect())) {
            throw new IllegalArgumentException("FloatBuffer is not direct");
        }
    }

    public static void checkDirect(DoubleBuffer paramDoubleBuffer) {
        if ((LWJGLUtil.CHECKS) && (!paramDoubleBuffer.isDirect())) {
            throw new IllegalArgumentException("DoubleBuffer is not direct");
        }
    }

    public static void checkDirect(PointerBuffer paramPointerBuffer) {
    }

    public static void checkArray(Object[] paramArrayOfObject) {
        if ((LWJGLUtil.CHECKS) && ((paramArrayOfObject == null) || (paramArrayOfObject.length == 0))) {
            throw new IllegalArgumentException("Invalid array");
        }
    }

    private static void throwBufferSizeException(Buffer paramBuffer, int paramInt) {
        throw new IllegalArgumentException("Number of remaining buffer elements is " + paramBuffer.remaining() + ", must be at least " + paramInt + ". Because at most " + paramInt + " elements can be returned, a buffer with at least " + paramInt + " elements is required, regardless of actual returned element count");
    }

    private static void throwBufferSizeException(PointerBuffer paramPointerBuffer, int paramInt) {
        throw new IllegalArgumentException("Number of remaining pointer buffer elements is " + paramPointerBuffer.remaining() + ", must be at least " + paramInt);
    }

    private static void throwArraySizeException(Object[] paramArrayOfObject, int paramInt) {
        throw new IllegalArgumentException("Number of array elements is " + paramArrayOfObject.length + ", must be at least " + paramInt);
    }

    private static void throwArraySizeException(long[] paramArrayOfLong, int paramInt) {
        throw new IllegalArgumentException("Number of array elements is " + paramArrayOfLong.length + ", must be at least " + paramInt);
    }

    public static void checkBufferSize(Buffer paramBuffer, int paramInt) {
        if ((LWJGLUtil.CHECKS) && (paramBuffer.remaining() < paramInt)) {
            throwBufferSizeException(paramBuffer, paramInt);
        }
    }

    public static int checkBuffer(Buffer paramBuffer, int paramInt) {
        int i;
        if ((paramBuffer instanceof ByteBuffer)) {
            checkBuffer((ByteBuffer) paramBuffer, paramInt);
            i = 0;
        } else if ((paramBuffer instanceof ShortBuffer)) {
            checkBuffer((ShortBuffer) paramBuffer, paramInt);
            i = 1;
        } else if ((paramBuffer instanceof IntBuffer)) {
            checkBuffer((IntBuffer) paramBuffer, paramInt);
            i = 2;
        } else if ((paramBuffer instanceof LongBuffer)) {
            checkBuffer((LongBuffer) paramBuffer, paramInt);
            i = 4;
        } else if ((paramBuffer instanceof FloatBuffer)) {
            checkBuffer((FloatBuffer) paramBuffer, paramInt);
            i = 2;
        } else if ((paramBuffer instanceof DoubleBuffer)) {
            checkBuffer((DoubleBuffer) paramBuffer, paramInt);
            i = 4;
        } else {
            throw new IllegalArgumentException("Unsupported Buffer type specified: " + paramBuffer.getClass());
        }
        return paramBuffer.position() >>> i;
    }

    public static void checkBuffer(ByteBuffer paramByteBuffer, int paramInt) {
        if (LWJGLUtil.CHECKS) {
            checkBufferSize(paramByteBuffer, paramInt);
            checkDirect(paramByteBuffer);
        }
    }

    public static void checkBuffer(ShortBuffer paramShortBuffer, int paramInt) {
        if (LWJGLUtil.CHECKS) {
            checkBufferSize(paramShortBuffer, paramInt);
            checkDirect(paramShortBuffer);
        }
    }

    public static void checkBuffer(IntBuffer paramIntBuffer, int paramInt) {
        if (LWJGLUtil.CHECKS) {
            checkBufferSize(paramIntBuffer, paramInt);
            checkDirect(paramIntBuffer);
        }
    }

    public static void checkBuffer(LongBuffer paramLongBuffer, int paramInt) {
        if (LWJGLUtil.CHECKS) {
            checkBufferSize(paramLongBuffer, paramInt);
            checkDirect(paramLongBuffer);
        }
    }

    public static void checkBuffer(FloatBuffer paramFloatBuffer, int paramInt) {
        if (LWJGLUtil.CHECKS) {
            checkBufferSize(paramFloatBuffer, paramInt);
            checkDirect(paramFloatBuffer);
        }
    }

    public static void checkBuffer(DoubleBuffer paramDoubleBuffer, int paramInt) {
        if (LWJGLUtil.CHECKS) {
            checkBufferSize(paramDoubleBuffer, paramInt);
            checkDirect(paramDoubleBuffer);
        }
    }

    public static void checkBuffer(PointerBuffer paramPointerBuffer, int paramInt) {
        if ((LWJGLUtil.CHECKS) && (paramPointerBuffer.remaining() < paramInt)) {
            throwBufferSizeException(paramPointerBuffer, paramInt);
        }
    }

    public static void checkArray(Object[] paramArrayOfObject, int paramInt) {
        if ((LWJGLUtil.CHECKS) && (paramArrayOfObject.length < paramInt)) {
            throwArraySizeException(paramArrayOfObject, paramInt);
        }
    }

    public static void checkArray(long[] paramArrayOfLong, int paramInt) {
        if ((LWJGLUtil.CHECKS) && (paramArrayOfLong.length < paramInt)) {
            throwArraySizeException(paramArrayOfLong, paramInt);
        }
    }
}





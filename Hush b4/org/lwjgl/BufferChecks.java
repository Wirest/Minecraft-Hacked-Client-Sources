// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl;

import java.nio.Buffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.nio.LongBuffer;
import java.nio.IntBuffer;
import java.nio.ByteBuffer;

public class BufferChecks
{
    private BufferChecks() {
    }
    
    public static void checkFunctionAddress(final long pointer) {
        if (LWJGLUtil.CHECKS && pointer == 0L) {
            throw new IllegalStateException("Function is not supported");
        }
    }
    
    public static void checkNullTerminated(final ByteBuffer buf) {
        if (LWJGLUtil.CHECKS && buf.get(buf.limit() - 1) != 0) {
            throw new IllegalArgumentException("Missing null termination");
        }
    }
    
    public static void checkNullTerminated(final ByteBuffer buf, final int count) {
        if (LWJGLUtil.CHECKS) {
            int nullFound = 0;
            for (int i = buf.position(); i < buf.limit(); ++i) {
                if (buf.get(i) == 0) {
                    ++nullFound;
                }
            }
            if (nullFound < count) {
                throw new IllegalArgumentException("Missing null termination");
            }
        }
    }
    
    public static void checkNullTerminated(final IntBuffer buf) {
        if (LWJGLUtil.CHECKS && buf.get(buf.limit() - 1) != 0) {
            throw new IllegalArgumentException("Missing null termination");
        }
    }
    
    public static void checkNullTerminated(final LongBuffer buf) {
        if (LWJGLUtil.CHECKS && buf.get(buf.limit() - 1) != 0L) {
            throw new IllegalArgumentException("Missing null termination");
        }
    }
    
    public static void checkNullTerminated(final PointerBuffer buf) {
        if (LWJGLUtil.CHECKS && buf.get(buf.limit() - 1) != 0L) {
            throw new IllegalArgumentException("Missing null termination");
        }
    }
    
    public static void checkNotNull(final Object o) {
        if (LWJGLUtil.CHECKS && o == null) {
            throw new IllegalArgumentException("Null argument");
        }
    }
    
    public static void checkDirect(final ByteBuffer buf) {
        if (LWJGLUtil.CHECKS && !buf.isDirect()) {
            throw new IllegalArgumentException("ByteBuffer is not direct");
        }
    }
    
    public static void checkDirect(final ShortBuffer buf) {
        if (LWJGLUtil.CHECKS && !buf.isDirect()) {
            throw new IllegalArgumentException("ShortBuffer is not direct");
        }
    }
    
    public static void checkDirect(final IntBuffer buf) {
        if (LWJGLUtil.CHECKS && !buf.isDirect()) {
            throw new IllegalArgumentException("IntBuffer is not direct");
        }
    }
    
    public static void checkDirect(final LongBuffer buf) {
        if (LWJGLUtil.CHECKS && !buf.isDirect()) {
            throw new IllegalArgumentException("LongBuffer is not direct");
        }
    }
    
    public static void checkDirect(final FloatBuffer buf) {
        if (LWJGLUtil.CHECKS && !buf.isDirect()) {
            throw new IllegalArgumentException("FloatBuffer is not direct");
        }
    }
    
    public static void checkDirect(final DoubleBuffer buf) {
        if (LWJGLUtil.CHECKS && !buf.isDirect()) {
            throw new IllegalArgumentException("DoubleBuffer is not direct");
        }
    }
    
    public static void checkDirect(final PointerBuffer buf) {
    }
    
    public static void checkArray(final Object[] array) {
        if (LWJGLUtil.CHECKS && (array == null || array.length == 0)) {
            throw new IllegalArgumentException("Invalid array");
        }
    }
    
    private static void throwBufferSizeException(final Buffer buf, final int size) {
        throw new IllegalArgumentException("Number of remaining buffer elements is " + buf.remaining() + ", must be at least " + size + ". Because at most " + size + " elements can be returned, a buffer with at least " + size + " elements is required, regardless of actual returned element count");
    }
    
    private static void throwBufferSizeException(final PointerBuffer buf, final int size) {
        throw new IllegalArgumentException("Number of remaining pointer buffer elements is " + buf.remaining() + ", must be at least " + size);
    }
    
    private static void throwArraySizeException(final Object[] array, final int size) {
        throw new IllegalArgumentException("Number of array elements is " + array.length + ", must be at least " + size);
    }
    
    private static void throwArraySizeException(final long[] array, final int size) {
        throw new IllegalArgumentException("Number of array elements is " + array.length + ", must be at least " + size);
    }
    
    public static void checkBufferSize(final Buffer buf, final int size) {
        if (LWJGLUtil.CHECKS && buf.remaining() < size) {
            throwBufferSizeException(buf, size);
        }
    }
    
    public static int checkBuffer(final Buffer buffer, final int size) {
        int posShift;
        if (buffer instanceof ByteBuffer) {
            checkBuffer((ByteBuffer)buffer, size);
            posShift = 0;
        }
        else if (buffer instanceof ShortBuffer) {
            checkBuffer((ShortBuffer)buffer, size);
            posShift = 1;
        }
        else if (buffer instanceof IntBuffer) {
            checkBuffer((IntBuffer)buffer, size);
            posShift = 2;
        }
        else if (buffer instanceof LongBuffer) {
            checkBuffer((LongBuffer)buffer, size);
            posShift = 4;
        }
        else if (buffer instanceof FloatBuffer) {
            checkBuffer((FloatBuffer)buffer, size);
            posShift = 2;
        }
        else {
            if (!(buffer instanceof DoubleBuffer)) {
                throw new IllegalArgumentException("Unsupported Buffer type specified: " + buffer.getClass());
            }
            checkBuffer((DoubleBuffer)buffer, size);
            posShift = 4;
        }
        return buffer.position() << posShift;
    }
    
    public static void checkBuffer(final ByteBuffer buf, final int size) {
        if (LWJGLUtil.CHECKS) {
            checkBufferSize(buf, size);
            checkDirect(buf);
        }
    }
    
    public static void checkBuffer(final ShortBuffer buf, final int size) {
        if (LWJGLUtil.CHECKS) {
            checkBufferSize(buf, size);
            checkDirect(buf);
        }
    }
    
    public static void checkBuffer(final IntBuffer buf, final int size) {
        if (LWJGLUtil.CHECKS) {
            checkBufferSize(buf, size);
            checkDirect(buf);
        }
    }
    
    public static void checkBuffer(final LongBuffer buf, final int size) {
        if (LWJGLUtil.CHECKS) {
            checkBufferSize(buf, size);
            checkDirect(buf);
        }
    }
    
    public static void checkBuffer(final FloatBuffer buf, final int size) {
        if (LWJGLUtil.CHECKS) {
            checkBufferSize(buf, size);
            checkDirect(buf);
        }
    }
    
    public static void checkBuffer(final DoubleBuffer buf, final int size) {
        if (LWJGLUtil.CHECKS) {
            checkBufferSize(buf, size);
            checkDirect(buf);
        }
    }
    
    public static void checkBuffer(final PointerBuffer buf, final int size) {
        if (LWJGLUtil.CHECKS && buf.remaining() < size) {
            throwBufferSizeException(buf, size);
        }
    }
    
    public static void checkArray(final Object[] array, final int size) {
        if (LWJGLUtil.CHECKS && array.length < size) {
            throwArraySizeException(array, size);
        }
    }
    
    public static void checkArray(final long[] array, final int size) {
        if (LWJGLUtil.CHECKS && array.length < size) {
            throwArraySizeException(array, size);
        }
    }
}

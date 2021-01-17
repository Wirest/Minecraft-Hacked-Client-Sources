// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ShortBuffer;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import java.nio.DoubleBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.ByteBuffer;

public final class NVPixelDataRange
{
    public static final int GL_WRITE_PIXEL_DATA_RANGE_NV = 34936;
    public static final int GL_READ_PIXEL_DATA_RANGE_NV = 34937;
    public static final int GL_WRITE_PIXEL_DATA_RANGE_LENGTH_NV = 34938;
    public static final int GL_READ_PIXEL_DATA_RANGE_LENGTH_NV = 34939;
    public static final int GL_WRITE_PIXEL_DATA_RANGE_POINTER_NV = 34940;
    public static final int GL_READ_PIXEL_DATA_RANGE_POINTER_NV = 34941;
    
    private NVPixelDataRange() {
    }
    
    public static void glPixelDataRangeNV(final int target, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPixelDataRangeNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglPixelDataRangeNV(target, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glPixelDataRangeNV(final int target, final DoubleBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPixelDataRangeNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglPixelDataRangeNV(target, data.remaining() << 3, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glPixelDataRangeNV(final int target, final FloatBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPixelDataRangeNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglPixelDataRangeNV(target, data.remaining() << 2, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glPixelDataRangeNV(final int target, final IntBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPixelDataRangeNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglPixelDataRangeNV(target, data.remaining() << 2, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glPixelDataRangeNV(final int target, final ShortBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPixelDataRangeNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglPixelDataRangeNV(target, data.remaining() << 1, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglPixelDataRangeNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glFlushPixelDataRangeNV(final int target) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFlushPixelDataRangeNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglFlushPixelDataRangeNV(target, function_pointer);
    }
    
    static native void nglFlushPixelDataRangeNV(final int p0, final long p1);
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ByteOrder;
import org.lwjgl.LWJGLUtil;
import java.nio.ShortBuffer;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import java.nio.DoubleBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.ByteBuffer;

public final class NVVertexArrayRange
{
    public static final int GL_VERTEX_ARRAY_RANGE_NV = 34077;
    public static final int GL_VERTEX_ARRAY_RANGE_LENGTH_NV = 34078;
    public static final int GL_VERTEX_ARRAY_RANGE_VALID_NV = 34079;
    public static final int GL_MAX_VERTEX_ARRAY_RANGE_ELEMENT_NV = 34080;
    public static final int GL_VERTEX_ARRAY_RANGE_POINTER_NV = 34081;
    
    private NVVertexArrayRange() {
    }
    
    public static void glVertexArrayRangeNV(final ByteBuffer pPointer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexArrayRangeNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pPointer);
        nglVertexArrayRangeNV(pPointer.remaining(), MemoryUtil.getAddress(pPointer), function_pointer);
    }
    
    public static void glVertexArrayRangeNV(final DoubleBuffer pPointer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexArrayRangeNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pPointer);
        nglVertexArrayRangeNV(pPointer.remaining() << 3, MemoryUtil.getAddress(pPointer), function_pointer);
    }
    
    public static void glVertexArrayRangeNV(final FloatBuffer pPointer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexArrayRangeNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pPointer);
        nglVertexArrayRangeNV(pPointer.remaining() << 2, MemoryUtil.getAddress(pPointer), function_pointer);
    }
    
    public static void glVertexArrayRangeNV(final IntBuffer pPointer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexArrayRangeNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pPointer);
        nglVertexArrayRangeNV(pPointer.remaining() << 2, MemoryUtil.getAddress(pPointer), function_pointer);
    }
    
    public static void glVertexArrayRangeNV(final ShortBuffer pPointer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexArrayRangeNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pPointer);
        nglVertexArrayRangeNV(pPointer.remaining() << 1, MemoryUtil.getAddress(pPointer), function_pointer);
    }
    
    static native void nglVertexArrayRangeNV(final int p0, final long p1, final long p2);
    
    public static void glFlushVertexArrayRangeNV() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFlushVertexArrayRangeNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglFlushVertexArrayRangeNV(function_pointer);
    }
    
    static native void nglFlushVertexArrayRangeNV(final long p0);
    
    public static ByteBuffer glAllocateMemoryNV(final int size, final float readFrequency, final float writeFrequency, final float priority) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glAllocateMemoryNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final ByteBuffer __result = nglAllocateMemoryNV(size, readFrequency, writeFrequency, priority, size, function_pointer);
        return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
    }
    
    static native ByteBuffer nglAllocateMemoryNV(final int p0, final float p1, final float p2, final float p3, final long p4, final long p5);
    
    public static void glFreeMemoryNV(final ByteBuffer pointer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFreeMemoryNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pointer);
        nglFreeMemoryNV(MemoryUtil.getAddress(pointer), function_pointer);
    }
    
    static native void nglFreeMemoryNV(final long p0, final long p1);
}

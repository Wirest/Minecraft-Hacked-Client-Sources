// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import java.nio.LongBuffer;
import org.lwjgl.BufferChecks;

public final class NVVertexAttribInteger64bit
{
    public static final int GL_INT64_NV = 5134;
    public static final int GL_UNSIGNED_INT64_NV = 5135;
    
    private NVVertexAttribInteger64bit() {
    }
    
    public static void glVertexAttribL1i64NV(final int index, final long x) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL1i64NV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribL1i64NV(index, x, function_pointer);
    }
    
    static native void nglVertexAttribL1i64NV(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribL2i64NV(final int index, final long x, final long y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL2i64NV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribL2i64NV(index, x, y, function_pointer);
    }
    
    static native void nglVertexAttribL2i64NV(final int p0, final long p1, final long p2, final long p3);
    
    public static void glVertexAttribL3i64NV(final int index, final long x, final long y, final long z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL3i64NV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribL3i64NV(index, x, y, z, function_pointer);
    }
    
    static native void nglVertexAttribL3i64NV(final int p0, final long p1, final long p2, final long p3, final long p4);
    
    public static void glVertexAttribL4i64NV(final int index, final long x, final long y, final long z, final long w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL4i64NV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribL4i64NV(index, x, y, z, w, function_pointer);
    }
    
    static native void nglVertexAttribL4i64NV(final int p0, final long p1, final long p2, final long p3, final long p4, final long p5);
    
    public static void glVertexAttribL1NV(final int index, final LongBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL1i64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 1);
        nglVertexAttribL1i64vNV(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribL1i64vNV(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribL2NV(final int index, final LongBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL2i64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 2);
        nglVertexAttribL2i64vNV(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribL2i64vNV(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribL3NV(final int index, final LongBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL3i64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 3);
        nglVertexAttribL3i64vNV(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribL3i64vNV(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribL4NV(final int index, final LongBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL4i64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 4);
        nglVertexAttribL4i64vNV(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribL4i64vNV(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribL1ui64NV(final int index, final long x) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL1ui64NV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribL1ui64NV(index, x, function_pointer);
    }
    
    static native void nglVertexAttribL1ui64NV(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribL2ui64NV(final int index, final long x, final long y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL2ui64NV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribL2ui64NV(index, x, y, function_pointer);
    }
    
    static native void nglVertexAttribL2ui64NV(final int p0, final long p1, final long p2, final long p3);
    
    public static void glVertexAttribL3ui64NV(final int index, final long x, final long y, final long z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL3ui64NV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribL3ui64NV(index, x, y, z, function_pointer);
    }
    
    static native void nglVertexAttribL3ui64NV(final int p0, final long p1, final long p2, final long p3, final long p4);
    
    public static void glVertexAttribL4ui64NV(final int index, final long x, final long y, final long z, final long w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL4ui64NV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribL4ui64NV(index, x, y, z, w, function_pointer);
    }
    
    static native void nglVertexAttribL4ui64NV(final int p0, final long p1, final long p2, final long p3, final long p4, final long p5);
    
    public static void glVertexAttribL1uNV(final int index, final LongBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL1ui64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 1);
        nglVertexAttribL1ui64vNV(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribL1ui64vNV(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribL2uNV(final int index, final LongBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL2ui64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 2);
        nglVertexAttribL2ui64vNV(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribL2ui64vNV(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribL3uNV(final int index, final LongBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL3ui64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 3);
        nglVertexAttribL3ui64vNV(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribL3ui64vNV(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribL4uNV(final int index, final LongBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL4ui64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 4);
        nglVertexAttribL4ui64vNV(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribL4ui64vNV(final int p0, final long p1, final long p2);
    
    public static void glGetVertexAttribLNV(final int index, final int pname, final LongBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexAttribLi64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetVertexAttribLi64vNV(index, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetVertexAttribLi64vNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetVertexAttribLuNV(final int index, final int pname, final LongBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexAttribLui64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetVertexAttribLui64vNV(index, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetVertexAttribLui64vNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glVertexAttribLFormatNV(final int index, final int size, final int type, final int stride) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribLFormatNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribLFormatNV(index, size, type, stride, function_pointer);
    }
    
    static native void nglVertexAttribLFormatNV(final int p0, final int p1, final int p2, final int p3, final long p4);
}

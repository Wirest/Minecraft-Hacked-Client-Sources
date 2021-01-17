// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import java.nio.LongBuffer;
import org.lwjgl.BufferChecks;

public final class NVGpuShader5
{
    public static final int GL_INT64_NV = 5134;
    public static final int GL_UNSIGNED_INT64_NV = 5135;
    public static final int GL_INT8_NV = 36832;
    public static final int GL_INT8_VEC2_NV = 36833;
    public static final int GL_INT8_VEC3_NV = 36834;
    public static final int GL_INT8_VEC4_NV = 36835;
    public static final int GL_INT16_NV = 36836;
    public static final int GL_INT16_VEC2_NV = 36837;
    public static final int GL_INT16_VEC3_NV = 36838;
    public static final int GL_INT16_VEC4_NV = 36839;
    public static final int GL_INT64_VEC2_NV = 36841;
    public static final int GL_INT64_VEC3_NV = 36842;
    public static final int GL_INT64_VEC4_NV = 36843;
    public static final int GL_UNSIGNED_INT8_NV = 36844;
    public static final int GL_UNSIGNED_INT8_VEC2_NV = 36845;
    public static final int GL_UNSIGNED_INT8_VEC3_NV = 36846;
    public static final int GL_UNSIGNED_INT8_VEC4_NV = 36847;
    public static final int GL_UNSIGNED_INT16_NV = 36848;
    public static final int GL_UNSIGNED_INT16_VEC2_NV = 36849;
    public static final int GL_UNSIGNED_INT16_VEC3_NV = 36850;
    public static final int GL_UNSIGNED_INT16_VEC4_NV = 36851;
    public static final int GL_UNSIGNED_INT64_VEC2_NV = 36853;
    public static final int GL_UNSIGNED_INT64_VEC3_NV = 36854;
    public static final int GL_UNSIGNED_INT64_VEC4_NV = 36855;
    public static final int GL_FLOAT16_NV = 36856;
    public static final int GL_FLOAT16_VEC2_NV = 36857;
    public static final int GL_FLOAT16_VEC3_NV = 36858;
    public static final int GL_FLOAT16_VEC4_NV = 36859;
    public static final int GL_PATCHES = 14;
    
    private NVGpuShader5() {
    }
    
    public static void glUniform1i64NV(final int location, final long x) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform1i64NV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform1i64NV(location, x, function_pointer);
    }
    
    static native void nglUniform1i64NV(final int p0, final long p1, final long p2);
    
    public static void glUniform2i64NV(final int location, final long x, final long y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform2i64NV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform2i64NV(location, x, y, function_pointer);
    }
    
    static native void nglUniform2i64NV(final int p0, final long p1, final long p2, final long p3);
    
    public static void glUniform3i64NV(final int location, final long x, final long y, final long z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform3i64NV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform3i64NV(location, x, y, z, function_pointer);
    }
    
    static native void nglUniform3i64NV(final int p0, final long p1, final long p2, final long p3, final long p4);
    
    public static void glUniform4i64NV(final int location, final long x, final long y, final long z, final long w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform4i64NV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform4i64NV(location, x, y, z, w, function_pointer);
    }
    
    static native void nglUniform4i64NV(final int p0, final long p1, final long p2, final long p3, final long p4, final long p5);
    
    public static void glUniform1NV(final int location, final LongBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform1i64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglUniform1i64vNV(location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglUniform1i64vNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniform2NV(final int location, final LongBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform2i64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglUniform2i64vNV(location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglUniform2i64vNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniform3NV(final int location, final LongBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform3i64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglUniform3i64vNV(location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglUniform3i64vNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniform4NV(final int location, final LongBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform4i64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglUniform4i64vNV(location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglUniform4i64vNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniform1ui64NV(final int location, final long x) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform1ui64NV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform1ui64NV(location, x, function_pointer);
    }
    
    static native void nglUniform1ui64NV(final int p0, final long p1, final long p2);
    
    public static void glUniform2ui64NV(final int location, final long x, final long y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform2ui64NV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform2ui64NV(location, x, y, function_pointer);
    }
    
    static native void nglUniform2ui64NV(final int p0, final long p1, final long p2, final long p3);
    
    public static void glUniform3ui64NV(final int location, final long x, final long y, final long z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform3ui64NV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform3ui64NV(location, x, y, z, function_pointer);
    }
    
    static native void nglUniform3ui64NV(final int p0, final long p1, final long p2, final long p3, final long p4);
    
    public static void glUniform4ui64NV(final int location, final long x, final long y, final long z, final long w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform4ui64NV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform4ui64NV(location, x, y, z, w, function_pointer);
    }
    
    static native void nglUniform4ui64NV(final int p0, final long p1, final long p2, final long p3, final long p4, final long p5);
    
    public static void glUniform1uNV(final int location, final LongBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform1ui64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglUniform1ui64vNV(location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglUniform1ui64vNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniform2uNV(final int location, final LongBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform2ui64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglUniform2ui64vNV(location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglUniform2ui64vNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniform3uNV(final int location, final LongBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform3ui64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglUniform3ui64vNV(location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglUniform3ui64vNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniform4uNV(final int location, final LongBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform4ui64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglUniform4ui64vNV(location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglUniform4ui64vNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetUniformNV(final int program, final int location, final LongBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetUniformi64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetUniformi64vNV(program, location, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetUniformi64vNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetUniformuNV(final int program, final int location, final LongBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetUniformui64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetUniformui64vNV(program, location, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetUniformui64vNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glProgramUniform1i64NV(final int program, final int location, final long x) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform1i64NV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform1i64NV(program, location, x, function_pointer);
    }
    
    static native void nglProgramUniform1i64NV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glProgramUniform2i64NV(final int program, final int location, final long x, final long y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform2i64NV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform2i64NV(program, location, x, y, function_pointer);
    }
    
    static native void nglProgramUniform2i64NV(final int p0, final int p1, final long p2, final long p3, final long p4);
    
    public static void glProgramUniform3i64NV(final int program, final int location, final long x, final long y, final long z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform3i64NV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform3i64NV(program, location, x, y, z, function_pointer);
    }
    
    static native void nglProgramUniform3i64NV(final int p0, final int p1, final long p2, final long p3, final long p4, final long p5);
    
    public static void glProgramUniform4i64NV(final int program, final int location, final long x, final long y, final long z, final long w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform4i64NV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform4i64NV(program, location, x, y, z, w, function_pointer);
    }
    
    static native void nglProgramUniform4i64NV(final int p0, final int p1, final long p2, final long p3, final long p4, final long p5, final long p6);
    
    public static void glProgramUniform1NV(final int program, final int location, final LongBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform1i64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform1i64vNV(program, location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform1i64vNV(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform2NV(final int program, final int location, final LongBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform2i64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform2i64vNV(program, location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform2i64vNV(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform3NV(final int program, final int location, final LongBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform3i64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform3i64vNV(program, location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform3i64vNV(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform4NV(final int program, final int location, final LongBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform4i64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform4i64vNV(program, location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform4i64vNV(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform1ui64NV(final int program, final int location, final long x) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform1ui64NV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform1ui64NV(program, location, x, function_pointer);
    }
    
    static native void nglProgramUniform1ui64NV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glProgramUniform2ui64NV(final int program, final int location, final long x, final long y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform2ui64NV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform2ui64NV(program, location, x, y, function_pointer);
    }
    
    static native void nglProgramUniform2ui64NV(final int p0, final int p1, final long p2, final long p3, final long p4);
    
    public static void glProgramUniform3ui64NV(final int program, final int location, final long x, final long y, final long z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform3ui64NV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform3ui64NV(program, location, x, y, z, function_pointer);
    }
    
    static native void nglProgramUniform3ui64NV(final int p0, final int p1, final long p2, final long p3, final long p4, final long p5);
    
    public static void glProgramUniform4ui64NV(final int program, final int location, final long x, final long y, final long z, final long w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform4ui64NV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform4ui64NV(program, location, x, y, z, w, function_pointer);
    }
    
    static native void nglProgramUniform4ui64NV(final int p0, final int p1, final long p2, final long p3, final long p4, final long p5, final long p6);
    
    public static void glProgramUniform1uNV(final int program, final int location, final LongBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform1ui64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform1ui64vNV(program, location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform1ui64vNV(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform2uNV(final int program, final int location, final LongBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform2ui64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform2ui64vNV(program, location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform2ui64vNV(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform3uNV(final int program, final int location, final LongBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform3ui64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform3ui64vNV(program, location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform3ui64vNV(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform4uNV(final int program, final int location, final LongBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform4ui64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform4ui64vNV(program, location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform4ui64vNV(final int p0, final int p1, final int p2, final long p3, final long p4);
}

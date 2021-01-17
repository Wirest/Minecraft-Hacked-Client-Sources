// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.DoubleBuffer;

public final class ARBGpuShaderFp64
{
    public static final int GL_DOUBLE_VEC2 = 36860;
    public static final int GL_DOUBLE_VEC3 = 36861;
    public static final int GL_DOUBLE_VEC4 = 36862;
    public static final int GL_DOUBLE_MAT2 = 36678;
    public static final int GL_DOUBLE_MAT3 = 36679;
    public static final int GL_DOUBLE_MAT4 = 36680;
    public static final int GL_DOUBLE_MAT2x3 = 36681;
    public static final int GL_DOUBLE_MAT2x4 = 36682;
    public static final int GL_DOUBLE_MAT3x2 = 36683;
    public static final int GL_DOUBLE_MAT3x4 = 36684;
    public static final int GL_DOUBLE_MAT4x2 = 36685;
    public static final int GL_DOUBLE_MAT4x3 = 36686;
    
    private ARBGpuShaderFp64() {
    }
    
    public static void glUniform1d(final int location, final double x) {
        GL40.glUniform1d(location, x);
    }
    
    public static void glUniform2d(final int location, final double x, final double y) {
        GL40.glUniform2d(location, x, y);
    }
    
    public static void glUniform3d(final int location, final double x, final double y, final double z) {
        GL40.glUniform3d(location, x, y, z);
    }
    
    public static void glUniform4d(final int location, final double x, final double y, final double z, final double w) {
        GL40.glUniform4d(location, x, y, z, w);
    }
    
    public static void glUniform1(final int location, final DoubleBuffer value) {
        GL40.glUniform1(location, value);
    }
    
    public static void glUniform2(final int location, final DoubleBuffer value) {
        GL40.glUniform2(location, value);
    }
    
    public static void glUniform3(final int location, final DoubleBuffer value) {
        GL40.glUniform3(location, value);
    }
    
    public static void glUniform4(final int location, final DoubleBuffer value) {
        GL40.glUniform4(location, value);
    }
    
    public static void glUniformMatrix2(final int location, final boolean transpose, final DoubleBuffer value) {
        GL40.glUniformMatrix2(location, transpose, value);
    }
    
    public static void glUniformMatrix3(final int location, final boolean transpose, final DoubleBuffer value) {
        GL40.glUniformMatrix3(location, transpose, value);
    }
    
    public static void glUniformMatrix4(final int location, final boolean transpose, final DoubleBuffer value) {
        GL40.glUniformMatrix4(location, transpose, value);
    }
    
    public static void glUniformMatrix2x3(final int location, final boolean transpose, final DoubleBuffer value) {
        GL40.glUniformMatrix2x3(location, transpose, value);
    }
    
    public static void glUniformMatrix2x4(final int location, final boolean transpose, final DoubleBuffer value) {
        GL40.glUniformMatrix2x4(location, transpose, value);
    }
    
    public static void glUniformMatrix3x2(final int location, final boolean transpose, final DoubleBuffer value) {
        GL40.glUniformMatrix3x2(location, transpose, value);
    }
    
    public static void glUniformMatrix3x4(final int location, final boolean transpose, final DoubleBuffer value) {
        GL40.glUniformMatrix3x4(location, transpose, value);
    }
    
    public static void glUniformMatrix4x2(final int location, final boolean transpose, final DoubleBuffer value) {
        GL40.glUniformMatrix4x2(location, transpose, value);
    }
    
    public static void glUniformMatrix4x3(final int location, final boolean transpose, final DoubleBuffer value) {
        GL40.glUniformMatrix4x3(location, transpose, value);
    }
    
    public static void glGetUniform(final int program, final int location, final DoubleBuffer params) {
        GL40.glGetUniform(program, location, params);
    }
    
    public static void glProgramUniform1dEXT(final int program, final int location, final double x) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform1dEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform1dEXT(program, location, x, function_pointer);
    }
    
    static native void nglProgramUniform1dEXT(final int p0, final int p1, final double p2, final long p3);
    
    public static void glProgramUniform2dEXT(final int program, final int location, final double x, final double y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform2dEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform2dEXT(program, location, x, y, function_pointer);
    }
    
    static native void nglProgramUniform2dEXT(final int p0, final int p1, final double p2, final double p3, final long p4);
    
    public static void glProgramUniform3dEXT(final int program, final int location, final double x, final double y, final double z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform3dEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform3dEXT(program, location, x, y, z, function_pointer);
    }
    
    static native void nglProgramUniform3dEXT(final int p0, final int p1, final double p2, final double p3, final double p4, final long p5);
    
    public static void glProgramUniform4dEXT(final int program, final int location, final double x, final double y, final double z, final double w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform4dEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform4dEXT(program, location, x, y, z, w, function_pointer);
    }
    
    static native void nglProgramUniform4dEXT(final int p0, final int p1, final double p2, final double p3, final double p4, final double p5, final long p6);
    
    public static void glProgramUniform1EXT(final int program, final int location, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform1dvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform1dvEXT(program, location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform1dvEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform2EXT(final int program, final int location, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform2dvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform2dvEXT(program, location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform2dvEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform3EXT(final int program, final int location, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform3dvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform3dvEXT(program, location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform3dvEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform4EXT(final int program, final int location, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform4dvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform4dvEXT(program, location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform4dvEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniformMatrix2EXT(final int program, final int location, final boolean transpose, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix2dvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix2dvEXT(program, location, value.remaining() >> 2, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix2dvEXT(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix3EXT(final int program, final int location, final boolean transpose, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix3dvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix3dvEXT(program, location, value.remaining() / 9, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix3dvEXT(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix4EXT(final int program, final int location, final boolean transpose, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix4dvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix4dvEXT(program, location, value.remaining() >> 4, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix4dvEXT(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix2x3EXT(final int program, final int location, final boolean transpose, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix2x3dvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix2x3dvEXT(program, location, value.remaining() / 6, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix2x3dvEXT(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix2x4EXT(final int program, final int location, final boolean transpose, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix2x4dvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix2x4dvEXT(program, location, value.remaining() >> 3, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix2x4dvEXT(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix3x2EXT(final int program, final int location, final boolean transpose, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix3x2dvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix3x2dvEXT(program, location, value.remaining() / 6, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix3x2dvEXT(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix3x4EXT(final int program, final int location, final boolean transpose, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix3x4dvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix3x4dvEXT(program, location, value.remaining() / 12, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix3x4dvEXT(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix4x2EXT(final int program, final int location, final boolean transpose, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix4x2dvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix4x2dvEXT(program, location, value.remaining() >> 3, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix4x2dvEXT(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix4x3EXT(final int program, final int location, final boolean transpose, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix4x3dvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix4x3dvEXT(program, location, value.remaining() / 12, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix4x3dvEXT(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;

public final class NVGpuProgram4
{
    public static final int GL_PROGRAM_ATTRIB_COMPONENTS_NV = 35078;
    public static final int GL_PROGRAM_RESULT_COMPONENTS_NV = 35079;
    public static final int GL_MAX_PROGRAM_ATTRIB_COMPONENTS_NV = 35080;
    public static final int GL_MAX_PROGRAM_RESULT_COMPONENTS_NV = 35081;
    public static final int GL_MAX_PROGRAM_GENERIC_ATTRIBS_NV = 36261;
    public static final int GL_MAX_PROGRAM_GENERIC_RESULTS_NV = 36262;
    
    private NVGpuProgram4() {
    }
    
    public static void glProgramLocalParameterI4iNV(final int target, final int index, final int x, final int y, final int z, final int w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramLocalParameterI4iNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramLocalParameterI4iNV(target, index, x, y, z, w, function_pointer);
    }
    
    static native void nglProgramLocalParameterI4iNV(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6);
    
    public static void glProgramLocalParameterI4NV(final int target, final int index, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramLocalParameterI4ivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglProgramLocalParameterI4ivNV(target, index, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglProgramLocalParameterI4ivNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glProgramLocalParametersI4NV(final int target, final int index, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramLocalParametersI4ivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglProgramLocalParametersI4ivNV(target, index, params.remaining() >> 2, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglProgramLocalParametersI4ivNV(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramLocalParameterI4uiNV(final int target, final int index, final int x, final int y, final int z, final int w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramLocalParameterI4uiNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramLocalParameterI4uiNV(target, index, x, y, z, w, function_pointer);
    }
    
    static native void nglProgramLocalParameterI4uiNV(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6);
    
    public static void glProgramLocalParameterI4uNV(final int target, final int index, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramLocalParameterI4uivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglProgramLocalParameterI4uivNV(target, index, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglProgramLocalParameterI4uivNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glProgramLocalParametersI4uNV(final int target, final int index, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramLocalParametersI4uivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglProgramLocalParametersI4uivNV(target, index, params.remaining() >> 2, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglProgramLocalParametersI4uivNV(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramEnvParameterI4iNV(final int target, final int index, final int x, final int y, final int z, final int w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramEnvParameterI4iNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramEnvParameterI4iNV(target, index, x, y, z, w, function_pointer);
    }
    
    static native void nglProgramEnvParameterI4iNV(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6);
    
    public static void glProgramEnvParameterI4NV(final int target, final int index, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramEnvParameterI4ivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglProgramEnvParameterI4ivNV(target, index, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglProgramEnvParameterI4ivNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glProgramEnvParametersI4NV(final int target, final int index, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramEnvParametersI4ivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglProgramEnvParametersI4ivNV(target, index, params.remaining() >> 2, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglProgramEnvParametersI4ivNV(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramEnvParameterI4uiNV(final int target, final int index, final int x, final int y, final int z, final int w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramEnvParameterI4uiNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramEnvParameterI4uiNV(target, index, x, y, z, w, function_pointer);
    }
    
    static native void nglProgramEnvParameterI4uiNV(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6);
    
    public static void glProgramEnvParameterI4uNV(final int target, final int index, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramEnvParameterI4uivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglProgramEnvParameterI4uivNV(target, index, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglProgramEnvParameterI4uivNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glProgramEnvParametersI4uNV(final int target, final int index, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramEnvParametersI4uivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglProgramEnvParametersI4uivNV(target, index, params.remaining() >> 2, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglProgramEnvParametersI4uivNV(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetProgramLocalParameterINV(final int target, final int index, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramLocalParameterIivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetProgramLocalParameterIivNV(target, index, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetProgramLocalParameterIivNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetProgramLocalParameterIuNV(final int target, final int index, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramLocalParameterIuivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetProgramLocalParameterIuivNV(target, index, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetProgramLocalParameterIuivNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetProgramEnvParameterINV(final int target, final int index, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramEnvParameterIivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetProgramEnvParameterIivNV(target, index, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetProgramEnvParameterIivNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetProgramEnvParameterIuNV(final int target, final int index, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramEnvParameterIuivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetProgramEnvParameterIuivNV(target, index, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetProgramEnvParameterIuivNV(final int p0, final int p1, final long p2, final long p3);
}

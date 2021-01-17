// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import java.nio.LongBuffer;
import org.lwjgl.BufferChecks;

public final class NVShaderBufferLoad
{
    public static final int GL_BUFFER_GPU_ADDRESS_NV = 36637;
    public static final int GL_GPU_ADDRESS_NV = 36660;
    public static final int GL_MAX_SHADER_BUFFER_ADDRESS_NV = 36661;
    
    private NVShaderBufferLoad() {
    }
    
    public static void glMakeBufferResidentNV(final int target, final int access) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMakeBufferResidentNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMakeBufferResidentNV(target, access, function_pointer);
    }
    
    static native void nglMakeBufferResidentNV(final int p0, final int p1, final long p2);
    
    public static void glMakeBufferNonResidentNV(final int target) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMakeBufferNonResidentNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMakeBufferNonResidentNV(target, function_pointer);
    }
    
    static native void nglMakeBufferNonResidentNV(final int p0, final long p1);
    
    public static boolean glIsBufferResidentNV(final int target) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsBufferResidentNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsBufferResidentNV(target, function_pointer);
        return __result;
    }
    
    static native boolean nglIsBufferResidentNV(final int p0, final long p1);
    
    public static void glMakeNamedBufferResidentNV(final int buffer, final int access) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMakeNamedBufferResidentNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMakeNamedBufferResidentNV(buffer, access, function_pointer);
    }
    
    static native void nglMakeNamedBufferResidentNV(final int p0, final int p1, final long p2);
    
    public static void glMakeNamedBufferNonResidentNV(final int buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMakeNamedBufferNonResidentNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMakeNamedBufferNonResidentNV(buffer, function_pointer);
    }
    
    static native void nglMakeNamedBufferNonResidentNV(final int p0, final long p1);
    
    public static boolean glIsNamedBufferResidentNV(final int buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsNamedBufferResidentNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsNamedBufferResidentNV(buffer, function_pointer);
        return __result;
    }
    
    static native boolean nglIsNamedBufferResidentNV(final int p0, final long p1);
    
    public static void glGetBufferParameteruNV(final int target, final int pname, final LongBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetBufferParameterui64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetBufferParameterui64vNV(target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetBufferParameterui64vNV(final int p0, final int p1, final long p2, final long p3);
    
    public static long glGetBufferParameterui64NV(final int target, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetBufferParameterui64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final LongBuffer params = APIUtil.getBufferLong(caps);
        nglGetBufferParameterui64vNV(target, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetNamedBufferParameteruNV(final int buffer, final int pname, final LongBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedBufferParameterui64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetNamedBufferParameterui64vNV(buffer, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetNamedBufferParameterui64vNV(final int p0, final int p1, final long p2, final long p3);
    
    public static long glGetNamedBufferParameterui64NV(final int buffer, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedBufferParameterui64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final LongBuffer params = APIUtil.getBufferLong(caps);
        nglGetNamedBufferParameterui64vNV(buffer, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetIntegeruNV(final int value, final LongBuffer result) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetIntegerui64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(result, 1);
        nglGetIntegerui64vNV(value, MemoryUtil.getAddress(result), function_pointer);
    }
    
    static native void nglGetIntegerui64vNV(final int p0, final long p1, final long p2);
    
    public static long glGetIntegerui64NV(final int value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetIntegerui64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final LongBuffer result = APIUtil.getBufferLong(caps);
        nglGetIntegerui64vNV(value, MemoryUtil.getAddress(result), function_pointer);
        return result.get(0);
    }
    
    public static void glUniformui64NV(final int location, final long value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniformui64NV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniformui64NV(location, value, function_pointer);
    }
    
    static native void nglUniformui64NV(final int p0, final long p1, final long p2);
    
    public static void glUniformuNV(final int location, final LongBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniformui64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglUniformui64vNV(location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglUniformui64vNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetUniformuNV(final int program, final int location, final LongBuffer params) {
        NVGpuShader5.glGetUniformuNV(program, location, params);
    }
    
    public static void glProgramUniformui64NV(final int program, final int location, final long value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformui64NV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniformui64NV(program, location, value, function_pointer);
    }
    
    static native void nglProgramUniformui64NV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glProgramUniformuNV(final int program, final int location, final LongBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformui64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformui64vNV(program, location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformui64vNV(final int p0, final int p1, final int p2, final long p3, final long p4);
}

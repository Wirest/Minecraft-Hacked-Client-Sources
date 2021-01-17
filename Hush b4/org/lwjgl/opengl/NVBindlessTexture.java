// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import java.nio.LongBuffer;
import org.lwjgl.BufferChecks;

public final class NVBindlessTexture
{
    private NVBindlessTexture() {
    }
    
    public static long glGetTextureHandleNV(final int texture) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureHandleNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final long __result = nglGetTextureHandleNV(texture, function_pointer);
        return __result;
    }
    
    static native long nglGetTextureHandleNV(final int p0, final long p1);
    
    public static long glGetTextureSamplerHandleNV(final int texture, final int sampler) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureSamplerHandleNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final long __result = nglGetTextureSamplerHandleNV(texture, sampler, function_pointer);
        return __result;
    }
    
    static native long nglGetTextureSamplerHandleNV(final int p0, final int p1, final long p2);
    
    public static void glMakeTextureHandleResidentNV(final long handle) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMakeTextureHandleResidentNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMakeTextureHandleResidentNV(handle, function_pointer);
    }
    
    static native void nglMakeTextureHandleResidentNV(final long p0, final long p1);
    
    public static void glMakeTextureHandleNonResidentNV(final long handle) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMakeTextureHandleNonResidentNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMakeTextureHandleNonResidentNV(handle, function_pointer);
    }
    
    static native void nglMakeTextureHandleNonResidentNV(final long p0, final long p1);
    
    public static long glGetImageHandleNV(final int texture, final int level, final boolean layered, final int layer, final int format) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetImageHandleNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final long __result = nglGetImageHandleNV(texture, level, layered, layer, format, function_pointer);
        return __result;
    }
    
    static native long nglGetImageHandleNV(final int p0, final int p1, final boolean p2, final int p3, final int p4, final long p5);
    
    public static void glMakeImageHandleResidentNV(final long handle, final int access) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMakeImageHandleResidentNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMakeImageHandleResidentNV(handle, access, function_pointer);
    }
    
    static native void nglMakeImageHandleResidentNV(final long p0, final int p1, final long p2);
    
    public static void glMakeImageHandleNonResidentNV(final long handle) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMakeImageHandleNonResidentNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMakeImageHandleNonResidentNV(handle, function_pointer);
    }
    
    static native void nglMakeImageHandleNonResidentNV(final long p0, final long p1);
    
    public static void glUniformHandleui64NV(final int location, final long value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniformHandleui64NV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniformHandleui64NV(location, value, function_pointer);
    }
    
    static native void nglUniformHandleui64NV(final int p0, final long p1, final long p2);
    
    public static void glUniformHandleuNV(final int location, final LongBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniformHandleui64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglUniformHandleui64vNV(location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglUniformHandleui64vNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glProgramUniformHandleui64NV(final int program, final int location, final long value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformHandleui64NV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniformHandleui64NV(program, location, value, function_pointer);
    }
    
    static native void nglProgramUniformHandleui64NV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glProgramUniformHandleuNV(final int program, final int location, final LongBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformHandleui64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(values);
        nglProgramUniformHandleui64vNV(program, location, values.remaining(), MemoryUtil.getAddress(values), function_pointer);
    }
    
    static native void nglProgramUniformHandleui64vNV(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static boolean glIsTextureHandleResidentNV(final long handle) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsTextureHandleResidentNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsTextureHandleResidentNV(handle, function_pointer);
        return __result;
    }
    
    static native boolean nglIsTextureHandleResidentNV(final long p0, final long p1);
    
    public static boolean glIsImageHandleResidentNV(final long handle) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsImageHandleResidentNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsImageHandleResidentNV(handle, function_pointer);
        return __result;
    }
    
    static native boolean nglIsImageHandleResidentNV(final long p0, final long p1);
}

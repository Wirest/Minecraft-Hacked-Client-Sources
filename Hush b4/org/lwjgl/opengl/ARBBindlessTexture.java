// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import java.nio.LongBuffer;
import org.lwjgl.BufferChecks;

public final class ARBBindlessTexture
{
    public static final int GL_UNSIGNED_INT64_ARB = 5135;
    
    private ARBBindlessTexture() {
    }
    
    public static long glGetTextureHandleARB(final int texture) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureHandleARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final long __result = nglGetTextureHandleARB(texture, function_pointer);
        return __result;
    }
    
    static native long nglGetTextureHandleARB(final int p0, final long p1);
    
    public static long glGetTextureSamplerHandleARB(final int texture, final int sampler) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureSamplerHandleARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final long __result = nglGetTextureSamplerHandleARB(texture, sampler, function_pointer);
        return __result;
    }
    
    static native long nglGetTextureSamplerHandleARB(final int p0, final int p1, final long p2);
    
    public static void glMakeTextureHandleResidentARB(final long handle) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMakeTextureHandleResidentARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMakeTextureHandleResidentARB(handle, function_pointer);
    }
    
    static native void nglMakeTextureHandleResidentARB(final long p0, final long p1);
    
    public static void glMakeTextureHandleNonResidentARB(final long handle) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMakeTextureHandleNonResidentARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMakeTextureHandleNonResidentARB(handle, function_pointer);
    }
    
    static native void nglMakeTextureHandleNonResidentARB(final long p0, final long p1);
    
    public static long glGetImageHandleARB(final int texture, final int level, final boolean layered, final int layer, final int format) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetImageHandleARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final long __result = nglGetImageHandleARB(texture, level, layered, layer, format, function_pointer);
        return __result;
    }
    
    static native long nglGetImageHandleARB(final int p0, final int p1, final boolean p2, final int p3, final int p4, final long p5);
    
    public static void glMakeImageHandleResidentARB(final long handle, final int access) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMakeImageHandleResidentARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMakeImageHandleResidentARB(handle, access, function_pointer);
    }
    
    static native void nglMakeImageHandleResidentARB(final long p0, final int p1, final long p2);
    
    public static void glMakeImageHandleNonResidentARB(final long handle) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMakeImageHandleNonResidentARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMakeImageHandleNonResidentARB(handle, function_pointer);
    }
    
    static native void nglMakeImageHandleNonResidentARB(final long p0, final long p1);
    
    public static void glUniformHandleui64ARB(final int location, final long value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniformHandleui64ARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniformHandleui64ARB(location, value, function_pointer);
    }
    
    static native void nglUniformHandleui64ARB(final int p0, final long p1, final long p2);
    
    public static void glUniformHandleuARB(final int location, final LongBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniformHandleui64vARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglUniformHandleui64vARB(location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglUniformHandleui64vARB(final int p0, final int p1, final long p2, final long p3);
    
    public static void glProgramUniformHandleui64ARB(final int program, final int location, final long value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformHandleui64ARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniformHandleui64ARB(program, location, value, function_pointer);
    }
    
    static native void nglProgramUniformHandleui64ARB(final int p0, final int p1, final long p2, final long p3);
    
    public static void glProgramUniformHandleuARB(final int program, final int location, final LongBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformHandleui64vARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(values);
        nglProgramUniformHandleui64vARB(program, location, values.remaining(), MemoryUtil.getAddress(values), function_pointer);
    }
    
    static native void nglProgramUniformHandleui64vARB(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static boolean glIsTextureHandleResidentARB(final long handle) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsTextureHandleResidentARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsTextureHandleResidentARB(handle, function_pointer);
        return __result;
    }
    
    static native boolean nglIsTextureHandleResidentARB(final long p0, final long p1);
    
    public static boolean glIsImageHandleResidentARB(final long handle) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsImageHandleResidentARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsImageHandleResidentARB(handle, function_pointer);
        return __result;
    }
    
    static native boolean nglIsImageHandleResidentARB(final long p0, final long p1);
    
    public static void glVertexAttribL1ui64ARB(final int index, final long x) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL1ui64ARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribL1ui64ARB(index, x, function_pointer);
    }
    
    static native void nglVertexAttribL1ui64ARB(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribL1uARB(final int index, final LongBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL1ui64vARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 1);
        nglVertexAttribL1ui64vARB(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribL1ui64vARB(final int p0, final long p1, final long p2);
    
    public static void glGetVertexAttribLuARB(final int index, final int pname, final LongBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexAttribLui64vARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetVertexAttribLui64vARB(index, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetVertexAttribLui64vARB(final int p0, final int p1, final long p2, final long p3);
}

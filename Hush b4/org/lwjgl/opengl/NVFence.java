// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.IntBuffer;

public final class NVFence
{
    public static final int GL_ALL_COMPLETED_NV = 34034;
    public static final int GL_FENCE_STATUS_NV = 34035;
    public static final int GL_FENCE_CONDITION_NV = 34036;
    
    private NVFence() {
    }
    
    public static void glGenFencesNV(final IntBuffer piFences) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenFencesNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(piFences);
        nglGenFencesNV(piFences.remaining(), MemoryUtil.getAddress(piFences), function_pointer);
    }
    
    static native void nglGenFencesNV(final int p0, final long p1, final long p2);
    
    public static int glGenFencesNV() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenFencesNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer piFences = APIUtil.getBufferInt(caps);
        nglGenFencesNV(1, MemoryUtil.getAddress(piFences), function_pointer);
        return piFences.get(0);
    }
    
    public static void glDeleteFencesNV(final IntBuffer piFences) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteFencesNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(piFences);
        nglDeleteFencesNV(piFences.remaining(), MemoryUtil.getAddress(piFences), function_pointer);
    }
    
    static native void nglDeleteFencesNV(final int p0, final long p1, final long p2);
    
    public static void glDeleteFencesNV(final int fence) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteFencesNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDeleteFencesNV(1, APIUtil.getInt(caps, fence), function_pointer);
    }
    
    public static void glSetFenceNV(final int fence, final int condition) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSetFenceNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglSetFenceNV(fence, condition, function_pointer);
    }
    
    static native void nglSetFenceNV(final int p0, final int p1, final long p2);
    
    public static boolean glTestFenceNV(final int fence) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTestFenceNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglTestFenceNV(fence, function_pointer);
        return __result;
    }
    
    static native boolean nglTestFenceNV(final int p0, final long p1);
    
    public static void glFinishFenceNV(final int fence) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFinishFenceNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglFinishFenceNV(fence, function_pointer);
    }
    
    static native void nglFinishFenceNV(final int p0, final long p1);
    
    public static boolean glIsFenceNV(final int fence) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsFenceNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsFenceNV(fence, function_pointer);
        return __result;
    }
    
    static native boolean nglIsFenceNV(final int p0, final long p1);
    
    public static void glGetFenceivNV(final int fence, final int pname, final IntBuffer piParams) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetFenceivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(piParams, 4);
        nglGetFenceivNV(fence, pname, MemoryUtil.getAddress(piParams), function_pointer);
    }
    
    static native void nglGetFenceivNV(final int p0, final int p1, final long p2, final long p3);
}

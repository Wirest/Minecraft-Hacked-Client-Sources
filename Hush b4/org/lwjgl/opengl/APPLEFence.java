// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.IntBuffer;

public final class APPLEFence
{
    public static final int GL_DRAW_PIXELS_APPLE = 35338;
    public static final int GL_FENCE_APPLE = 35339;
    
    private APPLEFence() {
    }
    
    public static void glGenFencesAPPLE(final IntBuffer fences) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenFencesAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(fences);
        nglGenFencesAPPLE(fences.remaining(), MemoryUtil.getAddress(fences), function_pointer);
    }
    
    static native void nglGenFencesAPPLE(final int p0, final long p1, final long p2);
    
    public static int glGenFencesAPPLE() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenFencesAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer fences = APIUtil.getBufferInt(caps);
        nglGenFencesAPPLE(1, MemoryUtil.getAddress(fences), function_pointer);
        return fences.get(0);
    }
    
    public static void glDeleteFencesAPPLE(final IntBuffer fences) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteFencesAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(fences);
        nglDeleteFencesAPPLE(fences.remaining(), MemoryUtil.getAddress(fences), function_pointer);
    }
    
    static native void nglDeleteFencesAPPLE(final int p0, final long p1, final long p2);
    
    public static void glDeleteFencesAPPLE(final int fence) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteFencesAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDeleteFencesAPPLE(1, APIUtil.getInt(caps, fence), function_pointer);
    }
    
    public static void glSetFenceAPPLE(final int fence) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSetFenceAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglSetFenceAPPLE(fence, function_pointer);
    }
    
    static native void nglSetFenceAPPLE(final int p0, final long p1);
    
    public static boolean glIsFenceAPPLE(final int fence) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsFenceAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsFenceAPPLE(fence, function_pointer);
        return __result;
    }
    
    static native boolean nglIsFenceAPPLE(final int p0, final long p1);
    
    public static boolean glTestFenceAPPLE(final int fence) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTestFenceAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglTestFenceAPPLE(fence, function_pointer);
        return __result;
    }
    
    static native boolean nglTestFenceAPPLE(final int p0, final long p1);
    
    public static void glFinishFenceAPPLE(final int fence) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFinishFenceAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglFinishFenceAPPLE(fence, function_pointer);
    }
    
    static native void nglFinishFenceAPPLE(final int p0, final long p1);
    
    public static boolean glTestObjectAPPLE(final int object, final int name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTestObjectAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglTestObjectAPPLE(object, name, function_pointer);
        return __result;
    }
    
    static native boolean nglTestObjectAPPLE(final int p0, final int p1, final long p2);
    
    public static void glFinishObjectAPPLE(final int object, final int name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFinishObjectAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglFinishObjectAPPLE(object, name, function_pointer);
    }
    
    static native void nglFinishObjectAPPLE(final int p0, final int p1, final long p2);
}

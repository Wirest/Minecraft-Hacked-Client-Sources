// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;

public final class APPLEVertexArrayObject
{
    public static final int GL_VERTEX_ARRAY_BINDING_APPLE = 34229;
    
    private APPLEVertexArrayObject() {
    }
    
    public static void glBindVertexArrayAPPLE(final int array) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindVertexArrayAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBindVertexArrayAPPLE(array, function_pointer);
    }
    
    static native void nglBindVertexArrayAPPLE(final int p0, final long p1);
    
    public static void glDeleteVertexArraysAPPLE(final IntBuffer arrays) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteVertexArraysAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(arrays);
        nglDeleteVertexArraysAPPLE(arrays.remaining(), MemoryUtil.getAddress(arrays), function_pointer);
    }
    
    static native void nglDeleteVertexArraysAPPLE(final int p0, final long p1, final long p2);
    
    public static void glDeleteVertexArraysAPPLE(final int array) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteVertexArraysAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDeleteVertexArraysAPPLE(1, APIUtil.getInt(caps, array), function_pointer);
    }
    
    public static void glGenVertexArraysAPPLE(final IntBuffer arrays) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenVertexArraysAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(arrays);
        nglGenVertexArraysAPPLE(arrays.remaining(), MemoryUtil.getAddress(arrays), function_pointer);
    }
    
    static native void nglGenVertexArraysAPPLE(final int p0, final long p1, final long p2);
    
    public static int glGenVertexArraysAPPLE() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenVertexArraysAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer arrays = APIUtil.getBufferInt(caps);
        nglGenVertexArraysAPPLE(1, MemoryUtil.getAddress(arrays), function_pointer);
        return arrays.get(0);
    }
    
    public static boolean glIsVertexArrayAPPLE(final int array) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsVertexArrayAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsVertexArrayAPPLE(array, function_pointer);
        return __result;
    }
    
    static native boolean nglIsVertexArrayAPPLE(final int p0, final long p1);
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class NVPrimitiveRestart
{
    public static final int GL_PRIMITIVE_RESTART_NV = 34136;
    public static final int GL_PRIMITIVE_RESTART_INDEX_NV = 34137;
    
    private NVPrimitiveRestart() {
    }
    
    public static void glPrimitiveRestartNV() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPrimitiveRestartNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglPrimitiveRestartNV(function_pointer);
    }
    
    static native void nglPrimitiveRestartNV(final long p0);
    
    public static void glPrimitiveRestartIndexNV(final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPrimitiveRestartIndexNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglPrimitiveRestartIndexNV(index, function_pointer);
    }
    
    static native void nglPrimitiveRestartIndexNV(final int p0, final long p1);
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class NVConditionalRender
{
    public static final int GL_QUERY_WAIT_NV = 36371;
    public static final int GL_QUERY_NO_WAIT_NV = 36372;
    public static final int GL_QUERY_BY_REGION_WAIT_NV = 36373;
    public static final int GL_QUERY_BY_REGION_NO_WAIT_NV = 36374;
    
    private NVConditionalRender() {
    }
    
    public static void glBeginConditionalRenderNV(final int id, final int mode) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBeginConditionalRenderNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBeginConditionalRenderNV(id, mode, function_pointer);
    }
    
    static native void nglBeginConditionalRenderNV(final int p0, final int p1, final long p2);
    
    public static void glEndConditionalRenderNV() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glEndConditionalRenderNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglEndConditionalRenderNV(function_pointer);
    }
    
    static native void nglEndConditionalRenderNV(final long p0);
}

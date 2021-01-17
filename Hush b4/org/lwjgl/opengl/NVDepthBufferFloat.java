// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class NVDepthBufferFloat
{
    public static final int GL_DEPTH_COMPONENT32F_NV = 36267;
    public static final int GL_DEPTH32F_STENCIL8_NV = 36268;
    public static final int GL_FLOAT_32_UNSIGNED_INT_24_8_REV_NV = 36269;
    public static final int GL_DEPTH_BUFFER_FLOAT_MODE_NV = 36271;
    
    private NVDepthBufferFloat() {
    }
    
    public static void glDepthRangedNV(final double n, final double f) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDepthRangedNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDepthRangedNV(n, f, function_pointer);
    }
    
    static native void nglDepthRangedNV(final double p0, final double p1, final long p2);
    
    public static void glClearDepthdNV(final double d) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glClearDepthdNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglClearDepthdNV(d, function_pointer);
    }
    
    static native void nglClearDepthdNV(final double p0, final long p1);
    
    public static void glDepthBoundsdNV(final double zmin, final double zmax) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDepthBoundsdNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDepthBoundsdNV(zmin, zmax, function_pointer);
    }
    
    static native void nglDepthBoundsdNV(final double p0, final double p1, final long p2);
}

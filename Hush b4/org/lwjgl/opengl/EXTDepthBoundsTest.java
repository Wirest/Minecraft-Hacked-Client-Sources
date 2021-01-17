// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class EXTDepthBoundsTest
{
    public static final int GL_DEPTH_BOUNDS_TEST_EXT = 34960;
    public static final int GL_DEPTH_BOUNDS_EXT = 34961;
    
    private EXTDepthBoundsTest() {
    }
    
    public static void glDepthBoundsEXT(final double zmin, final double zmax) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDepthBoundsEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDepthBoundsEXT(zmin, zmax, function_pointer);
    }
    
    static native void nglDepthBoundsEXT(final double p0, final double p1, final long p2);
}

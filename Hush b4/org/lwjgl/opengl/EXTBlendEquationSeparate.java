// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class EXTBlendEquationSeparate
{
    public static final int GL_BLEND_EQUATION_RGB_EXT = 32777;
    public static final int GL_BLEND_EQUATION_ALPHA_EXT = 34877;
    
    private EXTBlendEquationSeparate() {
    }
    
    public static void glBlendEquationSeparateEXT(final int modeRGB, final int modeAlpha) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBlendEquationSeparateEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBlendEquationSeparateEXT(modeRGB, modeAlpha, function_pointer);
    }
    
    static native void nglBlendEquationSeparateEXT(final int p0, final int p1, final long p2);
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class EXTBlendFuncSeparate
{
    public static final int GL_BLEND_DST_RGB_EXT = 32968;
    public static final int GL_BLEND_SRC_RGB_EXT = 32969;
    public static final int GL_BLEND_DST_ALPHA_EXT = 32970;
    public static final int GL_BLEND_SRC_ALPHA_EXT = 32971;
    
    private EXTBlendFuncSeparate() {
    }
    
    public static void glBlendFuncSeparateEXT(final int sfactorRGB, final int dfactorRGB, final int sfactorAlpha, final int dfactorAlpha) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBlendFuncSeparateEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBlendFuncSeparateEXT(sfactorRGB, dfactorRGB, sfactorAlpha, dfactorAlpha, function_pointer);
    }
    
    static native void nglBlendFuncSeparateEXT(final int p0, final int p1, final int p2, final int p3, final long p4);
}

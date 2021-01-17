// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class EXTBlendColor
{
    public static final int GL_CONSTANT_COLOR_EXT = 32769;
    public static final int GL_ONE_MINUS_CONSTANT_COLOR_EXT = 32770;
    public static final int GL_CONSTANT_ALPHA_EXT = 32771;
    public static final int GL_ONE_MINUS_CONSTANT_ALPHA_EXT = 32772;
    public static final int GL_BLEND_COLOR_EXT = 32773;
    
    private EXTBlendColor() {
    }
    
    public static void glBlendColorEXT(final float red, final float green, final float blue, final float alpha) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBlendColorEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBlendColorEXT(red, green, blue, alpha, function_pointer);
    }
    
    static native void nglBlendColorEXT(final float p0, final float p1, final float p2, final float p3, final long p4);
}

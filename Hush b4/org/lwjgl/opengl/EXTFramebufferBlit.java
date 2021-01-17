// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class EXTFramebufferBlit
{
    public static final int GL_READ_FRAMEBUFFER_EXT = 36008;
    public static final int GL_DRAW_FRAMEBUFFER_EXT = 36009;
    public static final int GL_DRAW_FRAMEBUFFER_BINDING_EXT = 36006;
    public static final int GL_READ_FRAMEBUFFER_BINDING_EXT = 36010;
    
    private EXTFramebufferBlit() {
    }
    
    public static void glBlitFramebufferEXT(final int srcX0, final int srcY0, final int srcX1, final int srcY1, final int dstX0, final int dstY0, final int dstX1, final int dstY1, final int mask, final int filter) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBlitFramebufferEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBlitFramebufferEXT(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter, function_pointer);
    }
    
    static native void nglBlitFramebufferEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final long p10);
}

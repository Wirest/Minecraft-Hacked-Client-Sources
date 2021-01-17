// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class EXTFramebufferMultisample
{
    public static final int GL_RENDERBUFFER_SAMPLES_EXT = 36011;
    public static final int GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE_EXT = 36182;
    public static final int GL_MAX_SAMPLES_EXT = 36183;
    
    private EXTFramebufferMultisample() {
    }
    
    public static void glRenderbufferStorageMultisampleEXT(final int target, final int samples, final int internalformat, final int width, final int height) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glRenderbufferStorageMultisampleEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglRenderbufferStorageMultisampleEXT(target, samples, internalformat, width, height, function_pointer);
    }
    
    static native void nglRenderbufferStorageMultisampleEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
}

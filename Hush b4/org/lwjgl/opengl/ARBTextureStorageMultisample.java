// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ARBTextureStorageMultisample
{
    private ARBTextureStorageMultisample() {
    }
    
    public static void glTexStorage2DMultisample(final int target, final int samples, final int internalformat, final int width, final int height, final boolean fixedsamplelocations) {
        GL43.glTexStorage2DMultisample(target, samples, internalformat, width, height, fixedsamplelocations);
    }
    
    public static void glTexStorage3DMultisample(final int target, final int samples, final int internalformat, final int width, final int height, final int depth, final boolean fixedsamplelocations) {
        GL43.glTexStorage3DMultisample(target, samples, internalformat, width, height, depth, fixedsamplelocations);
    }
    
    public static void glTextureStorage2DMultisampleEXT(final int texture, final int target, final int samples, final int internalformat, final int width, final int height, final boolean fixedsamplelocations) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureStorage2DMultisampleEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTextureStorage2DMultisampleEXT(texture, target, samples, internalformat, width, height, fixedsamplelocations, function_pointer);
    }
    
    static native void nglTextureStorage2DMultisampleEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final boolean p6, final long p7);
    
    public static void glTextureStorage3DMultisampleEXT(final int texture, final int target, final int samples, final int internalformat, final int width, final int height, final int depth, final boolean fixedsamplelocations) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureStorage3DMultisampleEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTextureStorage3DMultisampleEXT(texture, target, samples, internalformat, width, height, depth, fixedsamplelocations, function_pointer);
    }
    
    static native void nglTextureStorage3DMultisampleEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final boolean p7, final long p8);
}

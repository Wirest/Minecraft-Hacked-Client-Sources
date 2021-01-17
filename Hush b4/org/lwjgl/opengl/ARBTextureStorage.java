// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ARBTextureStorage
{
    public static final int GL_TEXTURE_IMMUTABLE_FORMAT = 37167;
    
    private ARBTextureStorage() {
    }
    
    public static void glTexStorage1D(final int target, final int levels, final int internalformat, final int width) {
        GL42.glTexStorage1D(target, levels, internalformat, width);
    }
    
    public static void glTexStorage2D(final int target, final int levels, final int internalformat, final int width, final int height) {
        GL42.glTexStorage2D(target, levels, internalformat, width, height);
    }
    
    public static void glTexStorage3D(final int target, final int levels, final int internalformat, final int width, final int height, final int depth) {
        GL42.glTexStorage3D(target, levels, internalformat, width, height, depth);
    }
    
    public static void glTextureStorage1DEXT(final int texture, final int target, final int levels, final int internalformat, final int width) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureStorage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTextureStorage1DEXT(texture, target, levels, internalformat, width, function_pointer);
    }
    
    static native void nglTextureStorage1DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glTextureStorage2DEXT(final int texture, final int target, final int levels, final int internalformat, final int width, final int height) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureStorage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTextureStorage2DEXT(texture, target, levels, internalformat, width, height, function_pointer);
    }
    
    static native void nglTextureStorage2DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6);
    
    public static void glTextureStorage3DEXT(final int texture, final int target, final int levels, final int internalformat, final int width, final int height, final int depth) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureStorage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTextureStorage3DEXT(texture, target, levels, internalformat, width, height, depth, function_pointer);
    }
    
    static native void nglTextureStorage3DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final long p7);
}

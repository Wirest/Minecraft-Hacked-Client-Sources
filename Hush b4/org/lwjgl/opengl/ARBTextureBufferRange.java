// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ARBTextureBufferRange
{
    public static final int GL_TEXTURE_BUFFER_OFFSET = 37277;
    public static final int GL_TEXTURE_BUFFER_SIZE = 37278;
    public static final int GL_TEXTURE_BUFFER_OFFSET_ALIGNMENT = 37279;
    
    private ARBTextureBufferRange() {
    }
    
    public static void glTexBufferRange(final int target, final int internalformat, final int buffer, final long offset, final long size) {
        GL43.glTexBufferRange(target, internalformat, buffer, offset, size);
    }
    
    public static void glTextureBufferRangeEXT(final int texture, final int target, final int internalformat, final int buffer, final long offset, final long size) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureBufferRangeEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTextureBufferRangeEXT(texture, target, internalformat, buffer, offset, size, function_pointer);
    }
    
    static native void nglTextureBufferRangeEXT(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5, final long p6);
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ARBTextureBufferObject
{
    public static final int GL_TEXTURE_BUFFER_ARB = 35882;
    public static final int GL_MAX_TEXTURE_BUFFER_SIZE_ARB = 35883;
    public static final int GL_TEXTURE_BINDING_BUFFER_ARB = 35884;
    public static final int GL_TEXTURE_BUFFER_DATA_STORE_BINDING_ARB = 35885;
    public static final int GL_TEXTURE_BUFFER_FORMAT_ARB = 35886;
    
    private ARBTextureBufferObject() {
    }
    
    public static void glTexBufferARB(final int target, final int internalformat, final int buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTexBufferARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTexBufferARB(target, internalformat, buffer, function_pointer);
    }
    
    static native void nglTexBufferARB(final int p0, final int p1, final int p2, final long p3);
}

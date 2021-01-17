// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class EXTStencilClearTag
{
    public static final int GL_STENCIL_TAG_BITS_EXT = 35058;
    public static final int GL_STENCIL_CLEAR_TAG_VALUE_EXT = 35059;
    
    private EXTStencilClearTag() {
    }
    
    public static void glStencilClearTagEXT(final int stencilTagBits, final int stencilClearTag) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glStencilClearTagEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglStencilClearTagEXT(stencilTagBits, stencilClearTag, function_pointer);
    }
    
    static native void nglStencilClearTagEXT(final int p0, final int p1, final long p2);
}

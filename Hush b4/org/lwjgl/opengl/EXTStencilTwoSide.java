// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class EXTStencilTwoSide
{
    public static final int GL_STENCIL_TEST_TWO_SIDE_EXT = 35088;
    public static final int GL_ACTIVE_STENCIL_FACE_EXT = 35089;
    
    private EXTStencilTwoSide() {
    }
    
    public static void glActiveStencilFaceEXT(final int face) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glActiveStencilFaceEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglActiveStencilFaceEXT(face, function_pointer);
    }
    
    static native void nglActiveStencilFaceEXT(final int p0, final long p1);
}

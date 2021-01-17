// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ATISeparateStencil
{
    public static final int GL_STENCIL_BACK_FUNC_ATI = 34816;
    public static final int GL_STENCIL_BACK_FAIL_ATI = 34817;
    public static final int GL_STENCIL_BACK_PASS_DEPTH_FAIL_ATI = 34818;
    public static final int GL_STENCIL_BACK_PASS_DEPTH_PASS_ATI = 34819;
    
    private ATISeparateStencil() {
    }
    
    public static void glStencilOpSeparateATI(final int face, final int sfail, final int dpfail, final int dppass) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glStencilOpSeparateATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglStencilOpSeparateATI(face, sfail, dpfail, dppass, function_pointer);
    }
    
    static native void nglStencilOpSeparateATI(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glStencilFuncSeparateATI(final int frontfunc, final int backfunc, final int ref, final int mask) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glStencilFuncSeparateATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglStencilFuncSeparateATI(frontfunc, backfunc, ref, mask, function_pointer);
    }
    
    static native void nglStencilFuncSeparateATI(final int p0, final int p1, final int p2, final int p3, final long p4);
}

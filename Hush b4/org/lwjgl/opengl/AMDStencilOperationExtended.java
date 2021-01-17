// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class AMDStencilOperationExtended
{
    public static final int GL_SET_AMD = 34634;
    public static final int GL_REPLACE_VALUE_AMD = 34635;
    public static final int GL_STENCIL_OP_VALUE_AMD = 34636;
    public static final int GL_STENCIL_BACK_OP_VALUE_AMD = 34637;
    
    private AMDStencilOperationExtended() {
    }
    
    public static void glStencilOpValueAMD(final int face, final int value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glStencilOpValueAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglStencilOpValueAMD(face, value, function_pointer);
    }
    
    static native void nglStencilOpValueAMD(final int p0, final int p1, final long p2);
}

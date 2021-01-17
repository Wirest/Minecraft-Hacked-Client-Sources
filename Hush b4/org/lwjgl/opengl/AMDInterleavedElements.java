// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class AMDInterleavedElements
{
    public static final int GL_VERTEX_ELEMENT_SWIZZLE_AMD = 37284;
    public static final int GL_VERTEX_ID_SWIZZLE_AMD = 37285;
    
    private AMDInterleavedElements() {
    }
    
    public static void glVertexAttribParameteriAMD(final int index, final int pname, final int param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribParameteriAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribParameteriAMD(index, pname, param, function_pointer);
    }
    
    static native void nglVertexAttribParameteriAMD(final int p0, final int p1, final int p2, final long p3);
}

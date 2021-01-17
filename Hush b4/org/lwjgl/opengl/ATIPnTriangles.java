// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ATIPnTriangles
{
    public static final int GL_PN_TRIANGLES_ATI = 34800;
    public static final int GL_MAX_PN_TRIANGLES_TESSELATION_LEVEL_ATI = 34801;
    public static final int GL_PN_TRIANGLES_POINT_MODE_ATI = 34802;
    public static final int GL_PN_TRIANGLES_NORMAL_MODE_ATI = 34803;
    public static final int GL_PN_TRIANGLES_TESSELATION_LEVEL_ATI = 34804;
    public static final int GL_PN_TRIANGLES_POINT_MODE_LINEAR_ATI = 34805;
    public static final int GL_PN_TRIANGLES_POINT_MODE_CUBIC_ATI = 34806;
    public static final int GL_PN_TRIANGLES_NORMAL_MODE_LINEAR_ATI = 34807;
    public static final int GL_PN_TRIANGLES_NORMAL_MODE_QUADRATIC_ATI = 34808;
    
    private ATIPnTriangles() {
    }
    
    public static void glPNTrianglesfATI(final int pname, final float param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPNTrianglesfATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglPNTrianglesfATI(pname, param, function_pointer);
    }
    
    static native void nglPNTrianglesfATI(final int p0, final float p1, final long p2);
    
    public static void glPNTrianglesiATI(final int pname, final int param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPNTrianglesiATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglPNTrianglesiATI(pname, param, function_pointer);
    }
    
    static native void nglPNTrianglesiATI(final int p0, final int p1, final long p2);
}

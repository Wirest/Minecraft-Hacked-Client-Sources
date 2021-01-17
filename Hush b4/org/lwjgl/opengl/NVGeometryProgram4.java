// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class NVGeometryProgram4
{
    public static final int GL_GEOMETRY_PROGRAM_NV = 35878;
    public static final int GL_MAX_PROGRAM_OUTPUT_VERTICES_NV = 35879;
    public static final int GL_MAX_PROGRAM_TOTAL_OUTPUT_COMPONENTS_NV = 35880;
    
    private NVGeometryProgram4() {
    }
    
    public static void glProgramVertexLimitNV(final int target, final int limit) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramVertexLimitNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramVertexLimitNV(target, limit, function_pointer);
    }
    
    static native void nglProgramVertexLimitNV(final int p0, final int p1, final long p2);
    
    public static void glFramebufferTextureEXT(final int target, final int attachment, final int texture, final int level) {
        EXTGeometryShader4.glFramebufferTextureEXT(target, attachment, texture, level);
    }
    
    public static void glFramebufferTextureLayerEXT(final int target, final int attachment, final int texture, final int level, final int layer) {
        EXTGeometryShader4.glFramebufferTextureLayerEXT(target, attachment, texture, level, layer);
    }
    
    public static void glFramebufferTextureFaceEXT(final int target, final int attachment, final int texture, final int level, final int face) {
        EXTGeometryShader4.glFramebufferTextureFaceEXT(target, attachment, texture, level, face);
    }
}

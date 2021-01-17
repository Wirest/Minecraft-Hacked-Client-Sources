// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ARBGeometryShader4
{
    public static final int GL_GEOMETRY_SHADER_ARB = 36313;
    public static final int GL_GEOMETRY_VERTICES_OUT_ARB = 36314;
    public static final int GL_GEOMETRY_INPUT_TYPE_ARB = 36315;
    public static final int GL_GEOMETRY_OUTPUT_TYPE_ARB = 36316;
    public static final int GL_MAX_GEOMETRY_TEXTURE_IMAGE_UNITS_ARB = 35881;
    public static final int GL_MAX_GEOMETRY_VARYING_COMPONENTS_ARB = 36317;
    public static final int GL_MAX_VERTEX_VARYING_COMPONENTS_ARB = 36318;
    public static final int GL_MAX_VARYING_COMPONENTS_ARB = 35659;
    public static final int GL_MAX_GEOMETRY_UNIFORM_COMPONENTS_ARB = 36319;
    public static final int GL_MAX_GEOMETRY_OUTPUT_VERTICES_ARB = 36320;
    public static final int GL_MAX_GEOMETRY_TOTAL_OUTPUT_COMPONENTS_ARB = 36321;
    public static final int GL_LINES_ADJACENCY_ARB = 10;
    public static final int GL_LINE_STRIP_ADJACENCY_ARB = 11;
    public static final int GL_TRIANGLES_ADJACENCY_ARB = 12;
    public static final int GL_TRIANGLE_STRIP_ADJACENCY_ARB = 13;
    public static final int GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS_ARB = 36264;
    public static final int GL_FRAMEBUFFER_INCOMPLETE_LAYER_COUNT_ARB = 36265;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_LAYERED_ARB = 36263;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER_ARB = 36052;
    public static final int GL_PROGRAM_POINT_SIZE_ARB = 34370;
    
    private ARBGeometryShader4() {
    }
    
    public static void glProgramParameteriARB(final int program, final int pname, final int value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramParameteriARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramParameteriARB(program, pname, value, function_pointer);
    }
    
    static native void nglProgramParameteriARB(final int p0, final int p1, final int p2, final long p3);
    
    public static void glFramebufferTextureARB(final int target, final int attachment, final int texture, final int level) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFramebufferTextureARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglFramebufferTextureARB(target, attachment, texture, level, function_pointer);
    }
    
    static native void nglFramebufferTextureARB(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glFramebufferTextureLayerARB(final int target, final int attachment, final int texture, final int level, final int layer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFramebufferTextureLayerARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglFramebufferTextureLayerARB(target, attachment, texture, level, layer, function_pointer);
    }
    
    static native void nglFramebufferTextureLayerARB(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glFramebufferTextureFaceARB(final int target, final int attachment, final int texture, final int level, final int face) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFramebufferTextureFaceARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglFramebufferTextureFaceARB(target, attachment, texture, level, face, function_pointer);
    }
    
    static native void nglFramebufferTextureFaceARB(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
}

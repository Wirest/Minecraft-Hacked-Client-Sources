// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import org.lwjgl.LWJGLUtil;
import java.nio.FloatBuffer;
import org.lwjgl.BufferChecks;

public final class EXTVertexWeighting
{
    public static final int GL_MODELVIEW0_STACK_DEPTH_EXT = 2979;
    public static final int GL_MODELVIEW1_STACK_DEPTH_EXT = 34050;
    public static final int GL_MODELVIEW0_MATRIX_EXT = 2982;
    public static final int GL_MODELVIEW1_MATRIX_EXT = 34054;
    public static final int GL_VERTEX_WEIGHTING_EXT = 34057;
    public static final int GL_MODELVIEW0_EXT = 5888;
    public static final int GL_MODELVIEW1_EXT = 34058;
    public static final int GL_CURRENT_VERTEX_WEIGHT_EXT = 34059;
    public static final int GL_VERTEX_WEIGHT_ARRAY_EXT = 34060;
    public static final int GL_VERTEX_WEIGHT_ARRAY_SIZE_EXT = 34061;
    public static final int GL_VERTEX_WEIGHT_ARRAY_TYPE_EXT = 34062;
    public static final int GL_VERTEX_WEIGHT_ARRAY_STRIDE_EXT = 34063;
    public static final int GL_VERTEX_WEIGHT_ARRAY_POINTER_EXT = 34064;
    
    private EXTVertexWeighting() {
    }
    
    public static void glVertexWeightfEXT(final float weight) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexWeightfEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexWeightfEXT(weight, function_pointer);
    }
    
    static native void nglVertexWeightfEXT(final float p0, final long p1);
    
    public static void glVertexWeightPointerEXT(final int size, final int stride, final FloatBuffer pPointer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexWeightPointerEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(pPointer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).EXT_vertex_weighting_glVertexWeightPointerEXT_pPointer = pPointer;
        }
        nglVertexWeightPointerEXT(size, 5126, stride, MemoryUtil.getAddress(pPointer), function_pointer);
    }
    
    static native void nglVertexWeightPointerEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glVertexWeightPointerEXT(final int size, final int type, final int stride, final long pPointer_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexWeightPointerEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOenabled(caps);
        nglVertexWeightPointerEXTBO(size, type, stride, pPointer_buffer_offset, function_pointer);
    }
    
    static native void nglVertexWeightPointerEXTBO(final int p0, final int p1, final int p2, final long p3, final long p4);
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;
import java.nio.DoubleBuffer;

public final class ARBVertexAttrib64bit
{
    public static final int GL_DOUBLE_VEC2 = 36860;
    public static final int GL_DOUBLE_VEC3 = 36861;
    public static final int GL_DOUBLE_VEC4 = 36862;
    public static final int GL_DOUBLE_MAT2 = 36678;
    public static final int GL_DOUBLE_MAT3 = 36679;
    public static final int GL_DOUBLE_MAT4 = 36680;
    public static final int GL_DOUBLE_MAT2x3 = 36681;
    public static final int GL_DOUBLE_MAT2x4 = 36682;
    public static final int GL_DOUBLE_MAT3x2 = 36683;
    public static final int GL_DOUBLE_MAT3x4 = 36684;
    public static final int GL_DOUBLE_MAT4x2 = 36685;
    public static final int GL_DOUBLE_MAT4x3 = 36686;
    
    private ARBVertexAttrib64bit() {
    }
    
    public static void glVertexAttribL1d(final int index, final double x) {
        GL41.glVertexAttribL1d(index, x);
    }
    
    public static void glVertexAttribL2d(final int index, final double x, final double y) {
        GL41.glVertexAttribL2d(index, x, y);
    }
    
    public static void glVertexAttribL3d(final int index, final double x, final double y, final double z) {
        GL41.glVertexAttribL3d(index, x, y, z);
    }
    
    public static void glVertexAttribL4d(final int index, final double x, final double y, final double z, final double w) {
        GL41.glVertexAttribL4d(index, x, y, z, w);
    }
    
    public static void glVertexAttribL1(final int index, final DoubleBuffer v) {
        GL41.glVertexAttribL1(index, v);
    }
    
    public static void glVertexAttribL2(final int index, final DoubleBuffer v) {
        GL41.glVertexAttribL2(index, v);
    }
    
    public static void glVertexAttribL3(final int index, final DoubleBuffer v) {
        GL41.glVertexAttribL3(index, v);
    }
    
    public static void glVertexAttribL4(final int index, final DoubleBuffer v) {
        GL41.glVertexAttribL4(index, v);
    }
    
    public static void glVertexAttribLPointer(final int index, final int size, final int stride, final DoubleBuffer pointer) {
        GL41.glVertexAttribLPointer(index, size, stride, pointer);
    }
    
    public static void glVertexAttribLPointer(final int index, final int size, final int stride, final long pointer_buffer_offset) {
        GL41.glVertexAttribLPointer(index, size, stride, pointer_buffer_offset);
    }
    
    public static void glGetVertexAttribL(final int index, final int pname, final DoubleBuffer params) {
        GL41.glGetVertexAttribL(index, pname, params);
    }
    
    public static void glVertexArrayVertexAttribLOffsetEXT(final int vaobj, final int buffer, final int index, final int size, final int type, final int stride, final long offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexArrayVertexAttribLOffsetEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexArrayVertexAttribLOffsetEXT(vaobj, buffer, index, size, type, stride, offset, function_pointer);
    }
    
    static native void nglVertexArrayVertexAttribLOffsetEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6, final long p7);
}

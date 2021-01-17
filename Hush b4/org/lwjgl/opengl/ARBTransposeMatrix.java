// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.FloatBuffer;

public final class ARBTransposeMatrix
{
    public static final int GL_TRANSPOSE_MODELVIEW_MATRIX_ARB = 34019;
    public static final int GL_TRANSPOSE_PROJECTION_MATRIX_ARB = 34020;
    public static final int GL_TRANSPOSE_TEXTURE_MATRIX_ARB = 34021;
    public static final int GL_TRANSPOSE_COLOR_MATRIX_ARB = 34022;
    
    private ARBTransposeMatrix() {
    }
    
    public static void glLoadTransposeMatrixARB(final FloatBuffer pfMtx) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glLoadTransposeMatrixfARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(pfMtx, 16);
        nglLoadTransposeMatrixfARB(MemoryUtil.getAddress(pfMtx), function_pointer);
    }
    
    static native void nglLoadTransposeMatrixfARB(final long p0, final long p1);
    
    public static void glMultTransposeMatrixARB(final FloatBuffer pfMtx) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultTransposeMatrixfARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(pfMtx, 16);
        nglMultTransposeMatrixfARB(MemoryUtil.getAddress(pfMtx), function_pointer);
    }
    
    static native void nglMultTransposeMatrixfARB(final long p0, final long p1);
}

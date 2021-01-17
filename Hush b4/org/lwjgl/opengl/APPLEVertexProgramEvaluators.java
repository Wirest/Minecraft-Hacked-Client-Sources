// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.FloatBuffer;
import org.lwjgl.MemoryUtil;
import java.nio.DoubleBuffer;
import org.lwjgl.BufferChecks;

public final class APPLEVertexProgramEvaluators
{
    public static final int GL_VERTEX_ATTRIB_MAP1_APPLE = 35328;
    public static final int GL_VERTEX_ATTRIB_MAP2_APPLE = 35329;
    public static final int GL_VERTEX_ATTRIB_MAP1_SIZE_APPLE = 35330;
    public static final int GL_VERTEX_ATTRIB_MAP1_COEFF_APPLE = 35331;
    public static final int GL_VERTEX_ATTRIB_MAP1_ORDER_APPLE = 35332;
    public static final int GL_VERTEX_ATTRIB_MAP1_DOMAIN_APPLE = 35333;
    public static final int GL_VERTEX_ATTRIB_MAP2_SIZE_APPLE = 35334;
    public static final int GL_VERTEX_ATTRIB_MAP2_COEFF_APPLE = 35335;
    public static final int GL_VERTEX_ATTRIB_MAP2_ORDER_APPLE = 35336;
    public static final int GL_VERTEX_ATTRIB_MAP2_DOMAIN_APPLE = 35337;
    
    private APPLEVertexProgramEvaluators() {
    }
    
    public static void glEnableVertexAttribAPPLE(final int index, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glEnableVertexAttribAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglEnableVertexAttribAPPLE(index, pname, function_pointer);
    }
    
    static native void nglEnableVertexAttribAPPLE(final int p0, final int p1, final long p2);
    
    public static void glDisableVertexAttribAPPLE(final int index, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDisableVertexAttribAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDisableVertexAttribAPPLE(index, pname, function_pointer);
    }
    
    static native void nglDisableVertexAttribAPPLE(final int p0, final int p1, final long p2);
    
    public static boolean glIsVertexAttribEnabledAPPLE(final int index, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsVertexAttribEnabledAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsVertexAttribEnabledAPPLE(index, pname, function_pointer);
        return __result;
    }
    
    static native boolean nglIsVertexAttribEnabledAPPLE(final int p0, final int p1, final long p2);
    
    public static void glMapVertexAttrib1dAPPLE(final int index, final int size, final double u1, final double u2, final int stride, final int order, final DoubleBuffer points) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMapVertexAttrib1dAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(points);
        nglMapVertexAttrib1dAPPLE(index, size, u1, u2, stride, order, MemoryUtil.getAddress(points), function_pointer);
    }
    
    static native void nglMapVertexAttrib1dAPPLE(final int p0, final int p1, final double p2, final double p3, final int p4, final int p5, final long p6, final long p7);
    
    public static void glMapVertexAttrib1fAPPLE(final int index, final int size, final float u1, final float u2, final int stride, final int order, final FloatBuffer points) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMapVertexAttrib1fAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(points);
        nglMapVertexAttrib1fAPPLE(index, size, u1, u2, stride, order, MemoryUtil.getAddress(points), function_pointer);
    }
    
    static native void nglMapVertexAttrib1fAPPLE(final int p0, final int p1, final float p2, final float p3, final int p4, final int p5, final long p6, final long p7);
    
    public static void glMapVertexAttrib2dAPPLE(final int index, final int size, final double u1, final double u2, final int ustride, final int uorder, final double v1, final double v2, final int vstride, final int vorder, final DoubleBuffer points) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMapVertexAttrib2dAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(points);
        nglMapVertexAttrib2dAPPLE(index, size, u1, u2, ustride, uorder, v1, v2, vstride, vorder, MemoryUtil.getAddress(points), function_pointer);
    }
    
    static native void nglMapVertexAttrib2dAPPLE(final int p0, final int p1, final double p2, final double p3, final int p4, final int p5, final double p6, final double p7, final int p8, final int p9, final long p10, final long p11);
    
    public static void glMapVertexAttrib2fAPPLE(final int index, final int size, final float u1, final float u2, final int ustride, final int uorder, final float v1, final float v2, final int vstride, final int vorder, final FloatBuffer points) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMapVertexAttrib2fAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(points);
        nglMapVertexAttrib2fAPPLE(index, size, u1, u2, ustride, uorder, v1, v2, vstride, vorder, MemoryUtil.getAddress(points), function_pointer);
    }
    
    static native void nglMapVertexAttrib2fAPPLE(final int p0, final int p1, final float p2, final float p3, final int p4, final int p5, final float p6, final float p7, final int p8, final int p9, final long p10, final long p11);
}

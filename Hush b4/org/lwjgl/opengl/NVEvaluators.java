// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.FloatBuffer;

public final class NVEvaluators
{
    public static final int GL_EVAL_2D_NV = 34496;
    public static final int GL_EVAL_TRIANGULAR_2D_NV = 34497;
    public static final int GL_MAP_TESSELLATION_NV = 34498;
    public static final int GL_MAP_ATTRIB_U_ORDER_NV = 34499;
    public static final int GL_MAP_ATTRIB_V_ORDER_NV = 34500;
    public static final int GL_EVAL_FRACTIONAL_TESSELLATION_NV = 34501;
    public static final int GL_EVAL_VERTEX_ATTRIB0_NV = 34502;
    public static final int GL_EVAL_VERTEX_ATTRIB1_NV = 34503;
    public static final int GL_EVAL_VERTEX_ATTRIB2_NV = 34504;
    public static final int GL_EVAL_VERTEX_ATTRIB3_NV = 34505;
    public static final int GL_EVAL_VERTEX_ATTRIB4_NV = 34506;
    public static final int GL_EVAL_VERTEX_ATTRIB5_NV = 34507;
    public static final int GL_EVAL_VERTEX_ATTRIB6_NV = 34508;
    public static final int GL_EVAL_VERTEX_ATTRIB7_NV = 34509;
    public static final int GL_EVAL_VERTEX_ATTRIB8_NV = 34510;
    public static final int GL_EVAL_VERTEX_ATTRIB9_NV = 34511;
    public static final int GL_EVAL_VERTEX_ATTRIB10_NV = 34512;
    public static final int GL_EVAL_VERTEX_ATTRIB11_NV = 34513;
    public static final int GL_EVAL_VERTEX_ATTRIB12_NV = 34514;
    public static final int GL_EVAL_VERTEX_ATTRIB13_NV = 34515;
    public static final int GL_EVAL_VERTEX_ATTRIB14_NV = 34516;
    public static final int GL_EVAL_VERTEX_ATTRIB15_NV = 34517;
    public static final int GL_MAX_MAP_TESSELLATION_NV = 34518;
    public static final int GL_MAX_RATIONAL_EVAL_ORDER_NV = 34519;
    
    private NVEvaluators() {
    }
    
    public static void glGetMapControlPointsNV(final int target, final int index, final int type, final int ustride, final int vstride, final boolean packed, final FloatBuffer pPoints) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMapControlPointsNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pPoints);
        nglGetMapControlPointsNV(target, index, type, ustride, vstride, packed, MemoryUtil.getAddress(pPoints), function_pointer);
    }
    
    static native void nglGetMapControlPointsNV(final int p0, final int p1, final int p2, final int p3, final int p4, final boolean p5, final long p6, final long p7);
    
    public static void glMapControlPointsNV(final int target, final int index, final int type, final int ustride, final int vstride, final int uorder, final int vorder, final boolean packed, final FloatBuffer pPoints) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMapControlPointsNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pPoints);
        nglMapControlPointsNV(target, index, type, ustride, vstride, uorder, vorder, packed, MemoryUtil.getAddress(pPoints), function_pointer);
    }
    
    static native void nglMapControlPointsNV(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final boolean p7, final long p8, final long p9);
    
    public static void glMapParameterNV(final int target, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMapParameterfvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglMapParameterfvNV(target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglMapParameterfvNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glMapParameterNV(final int target, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMapParameterivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglMapParameterivNV(target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglMapParameterivNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetMapParameterNV(final int target, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMapParameterfvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetMapParameterfvNV(target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetMapParameterfvNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetMapParameterNV(final int target, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMapParameterivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetMapParameterivNV(target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetMapParameterivNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetMapAttribParameterNV(final int target, final int index, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMapAttribParameterfvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetMapAttribParameterfvNV(target, index, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetMapAttribParameterfvNV(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetMapAttribParameterNV(final int target, final int index, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMapAttribParameterivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetMapAttribParameterivNV(target, index, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetMapAttribParameterivNV(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glEvalMapsNV(final int target, final int mode) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glEvalMapsNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglEvalMapsNV(target, mode, function_pointer);
    }
    
    static native void nglEvalMapsNV(final int p0, final int p1, final long p2);
}

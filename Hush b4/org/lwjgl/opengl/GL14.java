// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.LWJGLUtil;
import java.nio.DoubleBuffer;
import org.lwjgl.BufferChecks;

public final class GL14
{
    public static final int GL_GENERATE_MIPMAP = 33169;
    public static final int GL_GENERATE_MIPMAP_HINT = 33170;
    public static final int GL_DEPTH_COMPONENT16 = 33189;
    public static final int GL_DEPTH_COMPONENT24 = 33190;
    public static final int GL_DEPTH_COMPONENT32 = 33191;
    public static final int GL_TEXTURE_DEPTH_SIZE = 34890;
    public static final int GL_DEPTH_TEXTURE_MODE = 34891;
    public static final int GL_TEXTURE_COMPARE_MODE = 34892;
    public static final int GL_TEXTURE_COMPARE_FUNC = 34893;
    public static final int GL_COMPARE_R_TO_TEXTURE = 34894;
    public static final int GL_FOG_COORDINATE_SOURCE = 33872;
    public static final int GL_FOG_COORDINATE = 33873;
    public static final int GL_FRAGMENT_DEPTH = 33874;
    public static final int GL_CURRENT_FOG_COORDINATE = 33875;
    public static final int GL_FOG_COORDINATE_ARRAY_TYPE = 33876;
    public static final int GL_FOG_COORDINATE_ARRAY_STRIDE = 33877;
    public static final int GL_FOG_COORDINATE_ARRAY_POINTER = 33878;
    public static final int GL_FOG_COORDINATE_ARRAY = 33879;
    public static final int GL_POINT_SIZE_MIN = 33062;
    public static final int GL_POINT_SIZE_MAX = 33063;
    public static final int GL_POINT_FADE_THRESHOLD_SIZE = 33064;
    public static final int GL_POINT_DISTANCE_ATTENUATION = 33065;
    public static final int GL_COLOR_SUM = 33880;
    public static final int GL_CURRENT_SECONDARY_COLOR = 33881;
    public static final int GL_SECONDARY_COLOR_ARRAY_SIZE = 33882;
    public static final int GL_SECONDARY_COLOR_ARRAY_TYPE = 33883;
    public static final int GL_SECONDARY_COLOR_ARRAY_STRIDE = 33884;
    public static final int GL_SECONDARY_COLOR_ARRAY_POINTER = 33885;
    public static final int GL_SECONDARY_COLOR_ARRAY = 33886;
    public static final int GL_BLEND_DST_RGB = 32968;
    public static final int GL_BLEND_SRC_RGB = 32969;
    public static final int GL_BLEND_DST_ALPHA = 32970;
    public static final int GL_BLEND_SRC_ALPHA = 32971;
    public static final int GL_INCR_WRAP = 34055;
    public static final int GL_DECR_WRAP = 34056;
    public static final int GL_TEXTURE_FILTER_CONTROL = 34048;
    public static final int GL_TEXTURE_LOD_BIAS = 34049;
    public static final int GL_MAX_TEXTURE_LOD_BIAS = 34045;
    public static final int GL_MIRRORED_REPEAT = 33648;
    public static final int GL_BLEND_COLOR = 32773;
    public static final int GL_BLEND_EQUATION = 32777;
    public static final int GL_FUNC_ADD = 32774;
    public static final int GL_FUNC_SUBTRACT = 32778;
    public static final int GL_FUNC_REVERSE_SUBTRACT = 32779;
    public static final int GL_MIN = 32775;
    public static final int GL_MAX = 32776;
    
    private GL14() {
    }
    
    public static void glBlendEquation(final int mode) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBlendEquation;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBlendEquation(mode, function_pointer);
    }
    
    static native void nglBlendEquation(final int p0, final long p1);
    
    public static void glBlendColor(final float red, final float green, final float blue, final float alpha) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBlendColor;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBlendColor(red, green, blue, alpha, function_pointer);
    }
    
    static native void nglBlendColor(final float p0, final float p1, final float p2, final float p3, final long p4);
    
    public static void glFogCoordf(final float coord) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFogCoordf;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglFogCoordf(coord, function_pointer);
    }
    
    static native void nglFogCoordf(final float p0, final long p1);
    
    public static void glFogCoordd(final double coord) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFogCoordd;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglFogCoordd(coord, function_pointer);
    }
    
    static native void nglFogCoordd(final double p0, final long p1);
    
    public static void glFogCoordPointer(final int stride, final DoubleBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFogCoordPointer;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(data);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).GL14_glFogCoordPointer_data = data;
        }
        nglFogCoordPointer(5130, stride, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glFogCoordPointer(final int stride, final FloatBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFogCoordPointer;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(data);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).GL14_glFogCoordPointer_data = data;
        }
        nglFogCoordPointer(5126, stride, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglFogCoordPointer(final int p0, final int p1, final long p2, final long p3);
    
    public static void glFogCoordPointer(final int type, final int stride, final long data_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFogCoordPointer;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOenabled(caps);
        nglFogCoordPointerBO(type, stride, data_buffer_offset, function_pointer);
    }
    
    static native void nglFogCoordPointerBO(final int p0, final int p1, final long p2, final long p3);
    
    public static void glMultiDrawArrays(final int mode, final IntBuffer piFirst, final IntBuffer piCount) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiDrawArrays;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(piFirst);
        BufferChecks.checkBuffer(piCount, piFirst.remaining());
        nglMultiDrawArrays(mode, MemoryUtil.getAddress(piFirst), MemoryUtil.getAddress(piCount), piFirst.remaining(), function_pointer);
    }
    
    static native void nglMultiDrawArrays(final int p0, final long p1, final long p2, final int p3, final long p4);
    
    public static void glPointParameteri(final int pname, final int param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPointParameteri;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglPointParameteri(pname, param, function_pointer);
    }
    
    static native void nglPointParameteri(final int p0, final int p1, final long p2);
    
    public static void glPointParameterf(final int pname, final float param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPointParameterf;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglPointParameterf(pname, param, function_pointer);
    }
    
    static native void nglPointParameterf(final int p0, final float p1, final long p2);
    
    public static void glPointParameter(final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPointParameteriv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglPointParameteriv(pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglPointParameteriv(final int p0, final long p1, final long p2);
    
    public static void glPointParameter(final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPointParameterfv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglPointParameterfv(pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglPointParameterfv(final int p0, final long p1, final long p2);
    
    public static void glSecondaryColor3b(final byte red, final byte green, final byte blue) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSecondaryColor3b;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglSecondaryColor3b(red, green, blue, function_pointer);
    }
    
    static native void nglSecondaryColor3b(final byte p0, final byte p1, final byte p2, final long p3);
    
    public static void glSecondaryColor3f(final float red, final float green, final float blue) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSecondaryColor3f;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglSecondaryColor3f(red, green, blue, function_pointer);
    }
    
    static native void nglSecondaryColor3f(final float p0, final float p1, final float p2, final long p3);
    
    public static void glSecondaryColor3d(final double red, final double green, final double blue) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSecondaryColor3d;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglSecondaryColor3d(red, green, blue, function_pointer);
    }
    
    static native void nglSecondaryColor3d(final double p0, final double p1, final double p2, final long p3);
    
    public static void glSecondaryColor3ub(final byte red, final byte green, final byte blue) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSecondaryColor3ub;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglSecondaryColor3ub(red, green, blue, function_pointer);
    }
    
    static native void nglSecondaryColor3ub(final byte p0, final byte p1, final byte p2, final long p3);
    
    public static void glSecondaryColorPointer(final int size, final int stride, final DoubleBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSecondaryColorPointer;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(data);
        nglSecondaryColorPointer(size, 5130, stride, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glSecondaryColorPointer(final int size, final int stride, final FloatBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSecondaryColorPointer;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(data);
        nglSecondaryColorPointer(size, 5126, stride, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glSecondaryColorPointer(final int size, final boolean unsigned, final int stride, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSecondaryColorPointer;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(data);
        nglSecondaryColorPointer(size, unsigned ? 5121 : 5120, stride, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglSecondaryColorPointer(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glSecondaryColorPointer(final int size, final int type, final int stride, final long data_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSecondaryColorPointer;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOenabled(caps);
        nglSecondaryColorPointerBO(size, type, stride, data_buffer_offset, function_pointer);
    }
    
    static native void nglSecondaryColorPointerBO(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glBlendFuncSeparate(final int sfactorRGB, final int dfactorRGB, final int sfactorAlpha, final int dfactorAlpha) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBlendFuncSeparate;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBlendFuncSeparate(sfactorRGB, dfactorRGB, sfactorAlpha, dfactorAlpha, function_pointer);
    }
    
    static native void nglBlendFuncSeparate(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glWindowPos2f(final float x, final float y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glWindowPos2f;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglWindowPos2f(x, y, function_pointer);
    }
    
    static native void nglWindowPos2f(final float p0, final float p1, final long p2);
    
    public static void glWindowPos2d(final double x, final double y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glWindowPos2d;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglWindowPos2d(x, y, function_pointer);
    }
    
    static native void nglWindowPos2d(final double p0, final double p1, final long p2);
    
    public static void glWindowPos2i(final int x, final int y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glWindowPos2i;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglWindowPos2i(x, y, function_pointer);
    }
    
    static native void nglWindowPos2i(final int p0, final int p1, final long p2);
    
    public static void glWindowPos3f(final float x, final float y, final float z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glWindowPos3f;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglWindowPos3f(x, y, z, function_pointer);
    }
    
    static native void nglWindowPos3f(final float p0, final float p1, final float p2, final long p3);
    
    public static void glWindowPos3d(final double x, final double y, final double z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glWindowPos3d;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglWindowPos3d(x, y, z, function_pointer);
    }
    
    static native void nglWindowPos3d(final double p0, final double p1, final double p2, final long p3);
    
    public static void glWindowPos3i(final int x, final int y, final int z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glWindowPos3i;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglWindowPos3i(x, y, z, function_pointer);
    }
    
    static native void nglWindowPos3i(final int p0, final int p1, final int p2, final long p3);
}

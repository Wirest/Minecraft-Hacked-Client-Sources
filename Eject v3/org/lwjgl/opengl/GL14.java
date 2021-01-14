package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public final class GL14 {
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

    public static void glBlendEquation(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glBlendEquation;
        BufferChecks.checkFunctionAddress(l);
        nglBlendEquation(paramInt, l);
    }

    static native void nglBlendEquation(int paramInt, long paramLong);

    public static void glBlendColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glBlendColor;
        BufferChecks.checkFunctionAddress(l);
        nglBlendColor(paramFloat1, paramFloat2, paramFloat3, paramFloat4, l);
    }

    static native void nglBlendColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong);

    public static void glFogCoordf(float paramFloat) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glFogCoordf;
        BufferChecks.checkFunctionAddress(l);
        nglFogCoordf(paramFloat, l);
    }

    static native void nglFogCoordf(float paramFloat, long paramLong);

    public static void glFogCoordd(double paramDouble) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glFogCoordd;
        BufferChecks.checkFunctionAddress(l);
        nglFogCoordd(paramDouble, l);
    }

    static native void nglFogCoordd(double paramDouble, long paramLong);

    public static void glFogCoordPointer(int paramInt, DoubleBuffer paramDoubleBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glFogCoordPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramDoubleBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).GL14_glFogCoordPointer_data = paramDoubleBuffer;
        }
        nglFogCoordPointer(5130, paramInt, MemoryUtil.getAddress(paramDoubleBuffer), l);
    }

    public static void glFogCoordPointer(int paramInt, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glFogCoordPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramFloatBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).GL14_glFogCoordPointer_data = paramFloatBuffer;
        }
        nglFogCoordPointer(5126, paramInt, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglFogCoordPointer(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glFogCoordPointer(int paramInt1, int paramInt2, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glFogCoordPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOenabled(localContextCapabilities);
        nglFogCoordPointerBO(paramInt1, paramInt2, paramLong, l);
    }

    static native void nglFogCoordPointerBO(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glMultiDrawArrays(int paramInt, IntBuffer paramIntBuffer1, IntBuffer paramIntBuffer2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glMultiDrawArrays;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer1);
        BufferChecks.checkBuffer(paramIntBuffer2, paramIntBuffer1.remaining());
        nglMultiDrawArrays(paramInt, MemoryUtil.getAddress(paramIntBuffer1), MemoryUtil.getAddress(paramIntBuffer2), paramIntBuffer1.remaining(), l);
    }

    static native void nglMultiDrawArrays(int paramInt1, long paramLong1, long paramLong2, int paramInt2, long paramLong3);

    public static void glPointParameteri(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glPointParameteri;
        BufferChecks.checkFunctionAddress(l);
        nglPointParameteri(paramInt1, paramInt2, l);
    }

    static native void nglPointParameteri(int paramInt1, int paramInt2, long paramLong);

    public static void glPointParameterf(int paramInt, float paramFloat) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glPointParameterf;
        BufferChecks.checkFunctionAddress(l);
        nglPointParameterf(paramInt, paramFloat, l);
    }

    static native void nglPointParameterf(int paramInt, float paramFloat, long paramLong);

    public static void glPointParameter(int paramInt, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glPointParameteriv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 4);
        nglPointParameteriv(paramInt, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglPointParameteriv(int paramInt, long paramLong1, long paramLong2);

    public static void glPointParameter(int paramInt, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glPointParameterfv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramFloatBuffer, 4);
        nglPointParameterfv(paramInt, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglPointParameterfv(int paramInt, long paramLong1, long paramLong2);

    public static void glSecondaryColor3b(byte paramByte1, byte paramByte2, byte paramByte3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glSecondaryColor3b;
        BufferChecks.checkFunctionAddress(l);
        nglSecondaryColor3b(paramByte1, paramByte2, paramByte3, l);
    }

    static native void nglSecondaryColor3b(byte paramByte1, byte paramByte2, byte paramByte3, long paramLong);

    public static void glSecondaryColor3f(float paramFloat1, float paramFloat2, float paramFloat3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glSecondaryColor3f;
        BufferChecks.checkFunctionAddress(l);
        nglSecondaryColor3f(paramFloat1, paramFloat2, paramFloat3, l);
    }

    static native void nglSecondaryColor3f(float paramFloat1, float paramFloat2, float paramFloat3, long paramLong);

    public static void glSecondaryColor3d(double paramDouble1, double paramDouble2, double paramDouble3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glSecondaryColor3d;
        BufferChecks.checkFunctionAddress(l);
        nglSecondaryColor3d(paramDouble1, paramDouble2, paramDouble3, l);
    }

    static native void nglSecondaryColor3d(double paramDouble1, double paramDouble2, double paramDouble3, long paramLong);

    public static void glSecondaryColor3ub(byte paramByte1, byte paramByte2, byte paramByte3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glSecondaryColor3ub;
        BufferChecks.checkFunctionAddress(l);
        nglSecondaryColor3ub(paramByte1, paramByte2, paramByte3, l);
    }

    static native void nglSecondaryColor3ub(byte paramByte1, byte paramByte2, byte paramByte3, long paramLong);

    public static void glSecondaryColorPointer(int paramInt1, int paramInt2, DoubleBuffer paramDoubleBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glSecondaryColorPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramDoubleBuffer);
        nglSecondaryColorPointer(paramInt1, 5130, paramInt2, MemoryUtil.getAddress(paramDoubleBuffer), l);
    }

    public static void glSecondaryColorPointer(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glSecondaryColorPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramFloatBuffer);
        nglSecondaryColorPointer(paramInt1, 5126, paramInt2, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    public static void glSecondaryColorPointer(int paramInt1, boolean paramBoolean, int paramInt2, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glSecondaryColorPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramByteBuffer);
        nglSecondaryColorPointer(paramInt1, paramBoolean ? 5121 : 5120, paramInt2, MemoryUtil.getAddress(paramByteBuffer), l);
    }

    static native void nglSecondaryColorPointer(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);

    public static void glSecondaryColorPointer(int paramInt1, int paramInt2, int paramInt3, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glSecondaryColorPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOenabled(localContextCapabilities);
        nglSecondaryColorPointerBO(paramInt1, paramInt2, paramInt3, paramLong, l);
    }

    static native void nglSecondaryColorPointerBO(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);

    public static void glBlendFuncSeparate(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glBlendFuncSeparate;
        BufferChecks.checkFunctionAddress(l);
        nglBlendFuncSeparate(paramInt1, paramInt2, paramInt3, paramInt4, l);
    }

    static native void nglBlendFuncSeparate(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);

    public static void glWindowPos2f(float paramFloat1, float paramFloat2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glWindowPos2f;
        BufferChecks.checkFunctionAddress(l);
        nglWindowPos2f(paramFloat1, paramFloat2, l);
    }

    static native void nglWindowPos2f(float paramFloat1, float paramFloat2, long paramLong);

    public static void glWindowPos2d(double paramDouble1, double paramDouble2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glWindowPos2d;
        BufferChecks.checkFunctionAddress(l);
        nglWindowPos2d(paramDouble1, paramDouble2, l);
    }

    static native void nglWindowPos2d(double paramDouble1, double paramDouble2, long paramLong);

    public static void glWindowPos2i(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glWindowPos2i;
        BufferChecks.checkFunctionAddress(l);
        nglWindowPos2i(paramInt1, paramInt2, l);
    }

    static native void nglWindowPos2i(int paramInt1, int paramInt2, long paramLong);

    public static void glWindowPos3f(float paramFloat1, float paramFloat2, float paramFloat3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glWindowPos3f;
        BufferChecks.checkFunctionAddress(l);
        nglWindowPos3f(paramFloat1, paramFloat2, paramFloat3, l);
    }

    static native void nglWindowPos3f(float paramFloat1, float paramFloat2, float paramFloat3, long paramLong);

    public static void glWindowPos3d(double paramDouble1, double paramDouble2, double paramDouble3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glWindowPos3d;
        BufferChecks.checkFunctionAddress(l);
        nglWindowPos3d(paramDouble1, paramDouble2, paramDouble3, l);
    }

    static native void nglWindowPos3d(double paramDouble1, double paramDouble2, double paramDouble3, long paramLong);

    public static void glWindowPos3i(int paramInt1, int paramInt2, int paramInt3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glWindowPos3i;
        BufferChecks.checkFunctionAddress(l);
        nglWindowPos3i(paramInt1, paramInt2, paramInt3, l);
    }

    static native void nglWindowPos3i(int paramInt1, int paramInt2, int paramInt3, long paramLong);
}





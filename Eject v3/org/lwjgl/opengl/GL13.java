package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

import java.nio.*;

public final class GL13 {
    public static final int GL_TEXTURE0 = 33984;
    public static final int GL_TEXTURE1 = 33985;
    public static final int GL_TEXTURE2 = 33986;
    public static final int GL_TEXTURE3 = 33987;
    public static final int GL_TEXTURE4 = 33988;
    public static final int GL_TEXTURE5 = 33989;
    public static final int GL_TEXTURE6 = 33990;
    public static final int GL_TEXTURE7 = 33991;
    public static final int GL_TEXTURE8 = 33992;
    public static final int GL_TEXTURE9 = 33993;
    public static final int GL_TEXTURE10 = 33994;
    public static final int GL_TEXTURE11 = 33995;
    public static final int GL_TEXTURE12 = 33996;
    public static final int GL_TEXTURE13 = 33997;
    public static final int GL_TEXTURE14 = 33998;
    public static final int GL_TEXTURE15 = 33999;
    public static final int GL_TEXTURE16 = 34000;
    public static final int GL_TEXTURE17 = 34001;
    public static final int GL_TEXTURE18 = 34002;
    public static final int GL_TEXTURE19 = 34003;
    public static final int GL_TEXTURE20 = 34004;
    public static final int GL_TEXTURE21 = 34005;
    public static final int GL_TEXTURE22 = 34006;
    public static final int GL_TEXTURE23 = 34007;
    public static final int GL_TEXTURE24 = 34008;
    public static final int GL_TEXTURE25 = 34009;
    public static final int GL_TEXTURE26 = 34010;
    public static final int GL_TEXTURE27 = 34011;
    public static final int GL_TEXTURE28 = 34012;
    public static final int GL_TEXTURE29 = 34013;
    public static final int GL_TEXTURE30 = 34014;
    public static final int GL_TEXTURE31 = 34015;
    public static final int GL_ACTIVE_TEXTURE = 34016;
    public static final int GL_CLIENT_ACTIVE_TEXTURE = 34017;
    public static final int GL_MAX_TEXTURE_UNITS = 34018;
    public static final int GL_NORMAL_MAP = 34065;
    public static final int GL_REFLECTION_MAP = 34066;
    public static final int GL_TEXTURE_CUBE_MAP = 34067;
    public static final int GL_TEXTURE_BINDING_CUBE_MAP = 34068;
    public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_X = 34069;
    public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_X = 34070;
    public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_Y = 34071;
    public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_Y = 34072;
    public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_Z = 34073;
    public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_Z = 34074;
    public static final int GL_PROXY_TEXTURE_CUBE_MAP = 34075;
    public static final int GL_MAX_CUBE_MAP_TEXTURE_SIZE = 34076;
    public static final int GL_COMPRESSED_ALPHA = 34025;
    public static final int GL_COMPRESSED_LUMINANCE = 34026;
    public static final int GL_COMPRESSED_LUMINANCE_ALPHA = 34027;
    public static final int GL_COMPRESSED_INTENSITY = 34028;
    public static final int GL_COMPRESSED_RGB = 34029;
    public static final int GL_COMPRESSED_RGBA = 34030;
    public static final int GL_TEXTURE_COMPRESSION_HINT = 34031;
    public static final int GL_TEXTURE_COMPRESSED_IMAGE_SIZE = 34464;
    public static final int GL_TEXTURE_COMPRESSED = 34465;
    public static final int GL_NUM_COMPRESSED_TEXTURE_FORMATS = 34466;
    public static final int GL_COMPRESSED_TEXTURE_FORMATS = 34467;
    public static final int GL_MULTISAMPLE = 32925;
    public static final int GL_SAMPLE_ALPHA_TO_COVERAGE = 32926;
    public static final int GL_SAMPLE_ALPHA_TO_ONE = 32927;
    public static final int GL_SAMPLE_COVERAGE = 32928;
    public static final int GL_SAMPLE_BUFFERS = 32936;
    public static final int GL_SAMPLES = 32937;
    public static final int GL_SAMPLE_COVERAGE_VALUE = 32938;
    public static final int GL_SAMPLE_COVERAGE_INVERT = 32939;
    public static final int GL_MULTISAMPLE_BIT = 536870912;
    public static final int GL_TRANSPOSE_MODELVIEW_MATRIX = 34019;
    public static final int GL_TRANSPOSE_PROJECTION_MATRIX = 34020;
    public static final int GL_TRANSPOSE_TEXTURE_MATRIX = 34021;
    public static final int GL_TRANSPOSE_COLOR_MATRIX = 34022;
    public static final int GL_COMBINE = 34160;
    public static final int GL_COMBINE_RGB = 34161;
    public static final int GL_COMBINE_ALPHA = 34162;
    public static final int GL_SOURCE0_RGB = 34176;
    public static final int GL_SOURCE1_RGB = 34177;
    public static final int GL_SOURCE2_RGB = 34178;
    public static final int GL_SOURCE0_ALPHA = 34184;
    public static final int GL_SOURCE1_ALPHA = 34185;
    public static final int GL_SOURCE2_ALPHA = 34186;
    public static final int GL_OPERAND0_RGB = 34192;
    public static final int GL_OPERAND1_RGB = 34193;
    public static final int GL_OPERAND2_RGB = 34194;
    public static final int GL_OPERAND0_ALPHA = 34200;
    public static final int GL_OPERAND1_ALPHA = 34201;
    public static final int GL_OPERAND2_ALPHA = 34202;
    public static final int GL_RGB_SCALE = 34163;
    public static final int GL_ADD_SIGNED = 34164;
    public static final int GL_INTERPOLATE = 34165;
    public static final int GL_SUBTRACT = 34023;
    public static final int GL_CONSTANT = 34166;
    public static final int GL_PRIMARY_COLOR = 34167;
    public static final int GL_PREVIOUS = 34168;
    public static final int GL_DOT3_RGB = 34478;
    public static final int GL_DOT3_RGBA = 34479;
    public static final int GL_CLAMP_TO_BORDER = 33069;

    public static void glActiveTexture(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glActiveTexture;
        BufferChecks.checkFunctionAddress(l);
        nglActiveTexture(paramInt, l);
    }

    static native void nglActiveTexture(int paramInt, long paramLong);

    public static void glClientActiveTexture(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glClientActiveTexture;
        BufferChecks.checkFunctionAddress(l);
        StateTracker.getReferences(localContextCapabilities).glClientActiveTexture = (paramInt - 33984);
        nglClientActiveTexture(paramInt, l);
    }

    static native void nglClientActiveTexture(int paramInt, long paramLong);

    public static void glCompressedTexImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCompressedTexImage1D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramByteBuffer);
        nglCompressedTexImage1D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramByteBuffer.remaining(), MemoryUtil.getAddress(paramByteBuffer), l);
    }

    static native void nglCompressedTexImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong1, long paramLong2);

    public static void glCompressedTexImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCompressedTexImage1D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOenabled(localContextCapabilities);
        nglCompressedTexImage1DBO(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramLong, l);
    }

    static native void nglCompressedTexImage1DBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong1, long paramLong2);

    public static void glCompressedTexImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCompressedTexImage1D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        nglCompressedTexImage1D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, 0L, l);
    }

    public static void glCompressedTexImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCompressedTexImage2D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramByteBuffer);
        nglCompressedTexImage2D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramByteBuffer.remaining(), MemoryUtil.getAddress(paramByteBuffer), l);
    }

    static native void nglCompressedTexImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, long paramLong1, long paramLong2);

    public static void glCompressedTexImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCompressedTexImage2D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOenabled(localContextCapabilities);
        nglCompressedTexImage2DBO(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramLong, l);
    }

    static native void nglCompressedTexImage2DBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, long paramLong1, long paramLong2);

    public static void glCompressedTexImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCompressedTexImage2D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        nglCompressedTexImage2D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, 0L, l);
    }

    public static void glCompressedTexImage3D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCompressedTexImage3D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramByteBuffer);
        nglCompressedTexImage3D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramByteBuffer.remaining(), MemoryUtil.getAddress(paramByteBuffer), l);
    }

    static native void nglCompressedTexImage3D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong1, long paramLong2);

    public static void glCompressedTexImage3D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCompressedTexImage3D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOenabled(localContextCapabilities);
        nglCompressedTexImage3DBO(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramLong, l);
    }

    static native void nglCompressedTexImage3DBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong1, long paramLong2);

    public static void glCompressedTexImage3D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCompressedTexImage3D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        nglCompressedTexImage3D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, 0L, l);
    }

    public static void glCompressedTexSubImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCompressedTexSubImage1D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramByteBuffer);
        nglCompressedTexSubImage1D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramByteBuffer.remaining(), MemoryUtil.getAddress(paramByteBuffer), l);
    }

    static native void nglCompressedTexSubImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong1, long paramLong2);

    public static void glCompressedTexSubImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCompressedTexSubImage1D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOenabled(localContextCapabilities);
        nglCompressedTexSubImage1DBO(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramLong, l);
    }

    static native void nglCompressedTexSubImage1DBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong1, long paramLong2);

    public static void glCompressedTexSubImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCompressedTexSubImage2D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramByteBuffer);
        nglCompressedTexSubImage2D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramByteBuffer.remaining(), MemoryUtil.getAddress(paramByteBuffer), l);
    }

    static native void nglCompressedTexSubImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong1, long paramLong2);

    public static void glCompressedTexSubImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCompressedTexSubImage2D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOenabled(localContextCapabilities);
        nglCompressedTexSubImage2DBO(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramLong, l);
    }

    static native void nglCompressedTexSubImage2DBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong1, long paramLong2);

    public static void glCompressedTexSubImage3D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCompressedTexSubImage3D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramByteBuffer);
        nglCompressedTexSubImage3D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramInt9, paramByteBuffer.remaining(), MemoryUtil.getAddress(paramByteBuffer), l);
    }

    static native void nglCompressedTexSubImage3D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, long paramLong1, long paramLong2);

    public static void glCompressedTexSubImage3D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCompressedTexSubImage3D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOenabled(localContextCapabilities);
        nglCompressedTexSubImage3DBO(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramInt9, paramInt10, paramLong, l);
    }

    static native void nglCompressedTexSubImage3DBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, long paramLong1, long paramLong2);

    public static void glGetCompressedTexImage(int paramInt1, int paramInt2, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetCompressedTexImage;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensurePackPBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramByteBuffer);
        nglGetCompressedTexImage(paramInt1, paramInt2, MemoryUtil.getAddress(paramByteBuffer), l);
    }

    public static void glGetCompressedTexImage(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetCompressedTexImage;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensurePackPBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramIntBuffer);
        nglGetCompressedTexImage(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    public static void glGetCompressedTexImage(int paramInt1, int paramInt2, ShortBuffer paramShortBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetCompressedTexImage;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensurePackPBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramShortBuffer);
        nglGetCompressedTexImage(paramInt1, paramInt2, MemoryUtil.getAddress(paramShortBuffer), l);
    }

    static native void nglGetCompressedTexImage(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glGetCompressedTexImage(int paramInt1, int paramInt2, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetCompressedTexImage;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensurePackPBOenabled(localContextCapabilities);
        nglGetCompressedTexImageBO(paramInt1, paramInt2, paramLong, l);
    }

    static native void nglGetCompressedTexImageBO(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glMultiTexCoord1f(int paramInt, float paramFloat) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glMultiTexCoord1f;
        BufferChecks.checkFunctionAddress(l);
        nglMultiTexCoord1f(paramInt, paramFloat, l);
    }

    static native void nglMultiTexCoord1f(int paramInt, float paramFloat, long paramLong);

    public static void glMultiTexCoord1d(int paramInt, double paramDouble) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glMultiTexCoord1d;
        BufferChecks.checkFunctionAddress(l);
        nglMultiTexCoord1d(paramInt, paramDouble, l);
    }

    static native void nglMultiTexCoord1d(int paramInt, double paramDouble, long paramLong);

    public static void glMultiTexCoord2f(int paramInt, float paramFloat1, float paramFloat2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glMultiTexCoord2f;
        BufferChecks.checkFunctionAddress(l);
        nglMultiTexCoord2f(paramInt, paramFloat1, paramFloat2, l);
    }

    static native void nglMultiTexCoord2f(int paramInt, float paramFloat1, float paramFloat2, long paramLong);

    public static void glMultiTexCoord2d(int paramInt, double paramDouble1, double paramDouble2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glMultiTexCoord2d;
        BufferChecks.checkFunctionAddress(l);
        nglMultiTexCoord2d(paramInt, paramDouble1, paramDouble2, l);
    }

    static native void nglMultiTexCoord2d(int paramInt, double paramDouble1, double paramDouble2, long paramLong);

    public static void glMultiTexCoord3f(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glMultiTexCoord3f;
        BufferChecks.checkFunctionAddress(l);
        nglMultiTexCoord3f(paramInt, paramFloat1, paramFloat2, paramFloat3, l);
    }

    static native void nglMultiTexCoord3f(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, long paramLong);

    public static void glMultiTexCoord3d(int paramInt, double paramDouble1, double paramDouble2, double paramDouble3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glMultiTexCoord3d;
        BufferChecks.checkFunctionAddress(l);
        nglMultiTexCoord3d(paramInt, paramDouble1, paramDouble2, paramDouble3, l);
    }

    static native void nglMultiTexCoord3d(int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, long paramLong);

    public static void glMultiTexCoord4f(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glMultiTexCoord4f;
        BufferChecks.checkFunctionAddress(l);
        nglMultiTexCoord4f(paramInt, paramFloat1, paramFloat2, paramFloat3, paramFloat4, l);
    }

    static native void nglMultiTexCoord4f(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong);

    public static void glMultiTexCoord4d(int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glMultiTexCoord4d;
        BufferChecks.checkFunctionAddress(l);
        nglMultiTexCoord4d(paramInt, paramDouble1, paramDouble2, paramDouble3, paramDouble4, l);
    }

    static native void nglMultiTexCoord4d(int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, long paramLong);

    public static void glLoadTransposeMatrix(FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glLoadTransposeMatrixf;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramFloatBuffer, 16);
        nglLoadTransposeMatrixf(MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglLoadTransposeMatrixf(long paramLong1, long paramLong2);

    public static void glLoadTransposeMatrix(DoubleBuffer paramDoubleBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glLoadTransposeMatrixd;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramDoubleBuffer, 16);
        nglLoadTransposeMatrixd(MemoryUtil.getAddress(paramDoubleBuffer), l);
    }

    static native void nglLoadTransposeMatrixd(long paramLong1, long paramLong2);

    public static void glMultTransposeMatrix(FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glMultTransposeMatrixf;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramFloatBuffer, 16);
        nglMultTransposeMatrixf(MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglMultTransposeMatrixf(long paramLong1, long paramLong2);

    public static void glMultTransposeMatrix(DoubleBuffer paramDoubleBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glMultTransposeMatrixd;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramDoubleBuffer, 16);
        nglMultTransposeMatrixd(MemoryUtil.getAddress(paramDoubleBuffer), l);
    }

    static native void nglMultTransposeMatrixd(long paramLong1, long paramLong2);

    public static void glSampleCoverage(float paramFloat, boolean paramBoolean) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glSampleCoverage;
        BufferChecks.checkFunctionAddress(l);
        nglSampleCoverage(paramFloat, paramBoolean, l);
    }

    static native void nglSampleCoverage(float paramFloat, boolean paramBoolean, long paramLong);
}





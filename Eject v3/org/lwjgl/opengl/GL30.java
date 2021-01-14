package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

import java.nio.*;

public final class GL30 {
    public static final int GL_MAJOR_VERSION = 33307;
    public static final int GL_MINOR_VERSION = 33308;
    public static final int GL_NUM_EXTENSIONS = 33309;
    public static final int GL_CONTEXT_FLAGS = 33310;
    public static final int GL_CONTEXT_FLAG_FORWARD_COMPATIBLE_BIT = 1;
    public static final int GL_DEPTH_BUFFER = 33315;
    public static final int GL_STENCIL_BUFFER = 33316;
    public static final int GL_COMPRESSED_RED = 33317;
    public static final int GL_COMPRESSED_RG = 33318;
    public static final int GL_COMPARE_REF_TO_TEXTURE = 34894;
    public static final int GL_CLIP_DISTANCE0 = 12288;
    public static final int GL_CLIP_DISTANCE1 = 12289;
    public static final int GL_CLIP_DISTANCE2 = 12290;
    public static final int GL_CLIP_DISTANCE3 = 12291;
    public static final int GL_CLIP_DISTANCE4 = 12292;
    public static final int GL_CLIP_DISTANCE5 = 12293;
    public static final int GL_CLIP_DISTANCE6 = 12294;
    public static final int GL_CLIP_DISTANCE7 = 12295;
    public static final int GL_MAX_CLIP_DISTANCES = 3378;
    public static final int GL_MAX_VARYING_COMPONENTS = 35659;
    public static final int GL_BUFFER_ACCESS_FLAGS = 37151;
    public static final int GL_BUFFER_MAP_LENGTH = 37152;
    public static final int GL_BUFFER_MAP_OFFSET = 37153;
    public static final int GL_VERTEX_ATTRIB_ARRAY_INTEGER = 35069;
    public static final int GL_SAMPLER_BUFFER = 36290;
    public static final int GL_SAMPLER_CUBE_SHADOW = 36293;
    public static final int GL_UNSIGNED_INT_VEC2 = 36294;
    public static final int GL_UNSIGNED_INT_VEC3 = 36295;
    public static final int GL_UNSIGNED_INT_VEC4 = 36296;
    public static final int GL_INT_SAMPLER_1D = 36297;
    public static final int GL_INT_SAMPLER_2D = 36298;
    public static final int GL_INT_SAMPLER_3D = 36299;
    public static final int GL_INT_SAMPLER_CUBE = 36300;
    public static final int GL_INT_SAMPLER_2D_RECT = 36301;
    public static final int GL_INT_SAMPLER_1D_ARRAY = 36302;
    public static final int GL_INT_SAMPLER_2D_ARRAY = 36303;
    public static final int GL_INT_SAMPLER_BUFFER = 36304;
    public static final int GL_UNSIGNED_INT_SAMPLER_1D = 36305;
    public static final int GL_UNSIGNED_INT_SAMPLER_2D = 36306;
    public static final int GL_UNSIGNED_INT_SAMPLER_3D = 36307;
    public static final int GL_UNSIGNED_INT_SAMPLER_CUBE = 36308;
    public static final int GL_UNSIGNED_INT_SAMPLER_2D_RECT = 36309;
    public static final int GL_UNSIGNED_INT_SAMPLER_1D_ARRAY = 36310;
    public static final int GL_UNSIGNED_INT_SAMPLER_2D_ARRAY = 36311;
    public static final int GL_UNSIGNED_INT_SAMPLER_BUFFER = 36312;
    public static final int GL_MIN_PROGRAM_TEXEL_OFFSET = 35076;
    public static final int GL_MAX_PROGRAM_TEXEL_OFFSET = 35077;
    public static final int GL_QUERY_WAIT = 36371;
    public static final int GL_QUERY_NO_WAIT = 36372;
    public static final int GL_QUERY_BY_REGION_WAIT = 36373;
    public static final int GL_QUERY_BY_REGION_NO_WAIT = 36374;
    public static final int GL_MAP_READ_BIT = 1;
    public static final int GL_MAP_WRITE_BIT = 2;
    public static final int GL_MAP_INVALIDATE_RANGE_BIT = 4;
    public static final int GL_MAP_INVALIDATE_BUFFER_BIT = 8;
    public static final int GL_MAP_FLUSH_EXPLICIT_BIT = 16;
    public static final int GL_MAP_UNSYNCHRONIZED_BIT = 32;
    public static final int GL_CLAMP_VERTEX_COLOR = 35098;
    public static final int GL_CLAMP_FRAGMENT_COLOR = 35099;
    public static final int GL_CLAMP_READ_COLOR = 35100;
    public static final int GL_FIXED_ONLY = 35101;
    public static final int GL_DEPTH_COMPONENT32F = 36012;
    public static final int GL_DEPTH32F_STENCIL8 = 36013;
    public static final int GL_FLOAT_32_UNSIGNED_INT_24_8_REV = 36269;
    public static final int GL_TEXTURE_RED_TYPE = 35856;
    public static final int GL_TEXTURE_GREEN_TYPE = 35857;
    public static final int GL_TEXTURE_BLUE_TYPE = 35858;
    public static final int GL_TEXTURE_ALPHA_TYPE = 35859;
    public static final int GL_TEXTURE_LUMINANCE_TYPE = 35860;
    public static final int GL_TEXTURE_INTENSITY_TYPE = 35861;
    public static final int GL_TEXTURE_DEPTH_TYPE = 35862;
    public static final int GL_UNSIGNED_NORMALIZED = 35863;
    public static final int GL_RGBA32F = 34836;
    public static final int GL_RGB32F = 34837;
    public static final int GL_ALPHA32F = 34838;
    public static final int GL_RGBA16F = 34842;
    public static final int GL_RGB16F = 34843;
    public static final int GL_ALPHA16F = 34844;
    public static final int GL_R11F_G11F_B10F = 35898;
    public static final int GL_UNSIGNED_INT_10F_11F_11F_REV = 35899;
    public static final int GL_RGB9_E5 = 35901;
    public static final int GL_UNSIGNED_INT_5_9_9_9_REV = 35902;
    public static final int GL_TEXTURE_SHARED_SIZE = 35903;
    public static final int GL_FRAMEBUFFER = 36160;
    public static final int GL_READ_FRAMEBUFFER = 36008;
    public static final int GL_DRAW_FRAMEBUFFER = 36009;
    public static final int GL_RENDERBUFFER = 36161;
    public static final int GL_STENCIL_INDEX1 = 36166;
    public static final int GL_STENCIL_INDEX4 = 36167;
    public static final int GL_STENCIL_INDEX8 = 36168;
    public static final int GL_STENCIL_INDEX16 = 36169;
    public static final int GL_RENDERBUFFER_WIDTH = 36162;
    public static final int GL_RENDERBUFFER_HEIGHT = 36163;
    public static final int GL_RENDERBUFFER_INTERNAL_FORMAT = 36164;
    public static final int GL_RENDERBUFFER_RED_SIZE = 36176;
    public static final int GL_RENDERBUFFER_GREEN_SIZE = 36177;
    public static final int GL_RENDERBUFFER_BLUE_SIZE = 36178;
    public static final int GL_RENDERBUFFER_ALPHA_SIZE = 36179;
    public static final int GL_RENDERBUFFER_DEPTH_SIZE = 36180;
    public static final int GL_RENDERBUFFER_STENCIL_SIZE = 36181;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE = 36048;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME = 36049;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL = 36050;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE = 36051;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_COLOR_ENCODING = 33296;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_COMPONENT_TYPE = 33297;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_RED_SIZE = 33298;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_GREEN_SIZE = 33299;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_BLUE_SIZE = 33300;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_ALPHA_SIZE = 33301;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE = 33302;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_STENCIL_SIZE = 33303;
    public static final int GL_FRAMEBUFFER_DEFAULT = 33304;
    public static final int GL_INDEX = 33314;
    public static final int GL_COLOR_ATTACHMENT0 = 36064;
    public static final int GL_COLOR_ATTACHMENT1 = 36065;
    public static final int GL_COLOR_ATTACHMENT2 = 36066;
    public static final int GL_COLOR_ATTACHMENT3 = 36067;
    public static final int GL_COLOR_ATTACHMENT4 = 36068;
    public static final int GL_COLOR_ATTACHMENT5 = 36069;
    public static final int GL_COLOR_ATTACHMENT6 = 36070;
    public static final int GL_COLOR_ATTACHMENT7 = 36071;
    public static final int GL_COLOR_ATTACHMENT8 = 36072;
    public static final int GL_COLOR_ATTACHMENT9 = 36073;
    public static final int GL_COLOR_ATTACHMENT10 = 36074;
    public static final int GL_COLOR_ATTACHMENT11 = 36075;
    public static final int GL_COLOR_ATTACHMENT12 = 36076;
    public static final int GL_COLOR_ATTACHMENT13 = 36077;
    public static final int GL_COLOR_ATTACHMENT14 = 36078;
    public static final int GL_COLOR_ATTACHMENT15 = 36079;
    public static final int GL_DEPTH_ATTACHMENT = 36096;
    public static final int GL_STENCIL_ATTACHMENT = 36128;
    public static final int GL_DEPTH_STENCIL_ATTACHMENT = 33306;
    public static final int GL_FRAMEBUFFER_COMPLETE = 36053;
    public static final int GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT = 36054;
    public static final int GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = 36055;
    public static final int GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER = 36059;
    public static final int GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER = 36060;
    public static final int GL_FRAMEBUFFER_UNSUPPORTED = 36061;
    public static final int GL_FRAMEBUFFER_UNDEFINED = 33305;
    public static final int GL_FRAMEBUFFER_BINDING = 36006;
    public static final int GL_RENDERBUFFER_BINDING = 36007;
    public static final int GL_MAX_COLOR_ATTACHMENTS = 36063;
    public static final int GL_MAX_RENDERBUFFER_SIZE = 34024;
    public static final int GL_INVALID_FRAMEBUFFER_OPERATION = 1286;
    public static final int GL_HALF_FLOAT = 5131;
    public static final int GL_RENDERBUFFER_SAMPLES = 36011;
    public static final int GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE = 36182;
    public static final int GL_MAX_SAMPLES = 36183;
    public static final int GL_DRAW_FRAMEBUFFER_BINDING = 36006;
    public static final int GL_READ_FRAMEBUFFER_BINDING = 36010;
    public static final int GL_RGBA_INTEGER_MODE = 36254;
    public static final int GL_RGBA32UI = 36208;
    public static final int GL_RGB32UI = 36209;
    public static final int GL_ALPHA32UI = 36210;
    public static final int GL_RGBA16UI = 36214;
    public static final int GL_RGB16UI = 36215;
    public static final int GL_ALPHA16UI = 36216;
    public static final int GL_RGBA8UI = 36220;
    public static final int GL_RGB8UI = 36221;
    public static final int GL_ALPHA8UI = 36222;
    public static final int GL_RGBA32I = 36226;
    public static final int GL_RGB32I = 36227;
    public static final int GL_ALPHA32I = 36228;
    public static final int GL_RGBA16I = 36232;
    public static final int GL_RGB16I = 36233;
    public static final int GL_ALPHA16I = 36234;
    public static final int GL_RGBA8I = 36238;
    public static final int GL_RGB8I = 36239;
    public static final int GL_ALPHA8I = 36240;
    public static final int GL_RED_INTEGER = 36244;
    public static final int GL_GREEN_INTEGER = 36245;
    public static final int GL_BLUE_INTEGER = 36246;
    public static final int GL_ALPHA_INTEGER = 36247;
    public static final int GL_RGB_INTEGER = 36248;
    public static final int GL_RGBA_INTEGER = 36249;
    public static final int GL_BGR_INTEGER = 36250;
    public static final int GL_BGRA_INTEGER = 36251;
    public static final int GL_TEXTURE_1D_ARRAY = 35864;
    public static final int GL_TEXTURE_2D_ARRAY = 35866;
    public static final int GL_PROXY_TEXTURE_2D_ARRAY = 35867;
    public static final int GL_PROXY_TEXTURE_1D_ARRAY = 35865;
    public static final int GL_TEXTURE_BINDING_1D_ARRAY = 35868;
    public static final int GL_TEXTURE_BINDING_2D_ARRAY = 35869;
    public static final int GL_MAX_ARRAY_TEXTURE_LAYERS = 35071;
    public static final int GL_COMPARE_REF_DEPTH_TO_TEXTURE = 34894;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER = 36052;
    public static final int GL_SAMPLER_1D_ARRAY = 36288;
    public static final int GL_SAMPLER_2D_ARRAY = 36289;
    public static final int GL_SAMPLER_1D_ARRAY_SHADOW = 36291;
    public static final int GL_SAMPLER_2D_ARRAY_SHADOW = 36292;
    public static final int GL_DEPTH_STENCIL = 34041;
    public static final int GL_UNSIGNED_INT_24_8 = 34042;
    public static final int GL_DEPTH24_STENCIL8 = 35056;
    public static final int GL_TEXTURE_STENCIL_SIZE = 35057;
    public static final int GL_COMPRESSED_RED_RGTC1 = 36283;
    public static final int GL_COMPRESSED_SIGNED_RED_RGTC1 = 36284;
    public static final int GL_COMPRESSED_RG_RGTC2 = 36285;
    public static final int GL_COMPRESSED_SIGNED_RG_RGTC2 = 36286;
    public static final int GL_R8 = 33321;
    public static final int GL_R16 = 33322;
    public static final int GL_RG8 = 33323;
    public static final int GL_RG16 = 33324;
    public static final int GL_R16F = 33325;
    public static final int GL_R32F = 33326;
    public static final int GL_RG16F = 33327;
    public static final int GL_RG32F = 33328;
    public static final int GL_R8I = 33329;
    public static final int GL_R8UI = 33330;
    public static final int GL_R16I = 33331;
    public static final int GL_R16UI = 33332;
    public static final int GL_R32I = 33333;
    public static final int GL_R32UI = 33334;
    public static final int GL_RG8I = 33335;
    public static final int GL_RG8UI = 33336;
    public static final int GL_RG16I = 33337;
    public static final int GL_RG16UI = 33338;
    public static final int GL_RG32I = 33339;
    public static final int GL_RG32UI = 33340;
    public static final int GL_RG = 33319;
    public static final int GL_RG_INTEGER = 33320;
    public static final int GL_TRANSFORM_FEEDBACK_BUFFER = 35982;
    public static final int GL_TRANSFORM_FEEDBACK_BUFFER_START = 35972;
    public static final int GL_TRANSFORM_FEEDBACK_BUFFER_SIZE = 35973;
    public static final int GL_TRANSFORM_FEEDBACK_BUFFER_BINDING = 35983;
    public static final int GL_INTERLEAVED_ATTRIBS = 35980;
    public static final int GL_SEPARATE_ATTRIBS = 35981;
    public static final int GL_PRIMITIVES_GENERATED = 35975;
    public static final int GL_TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN = 35976;
    public static final int GL_RASTERIZER_DISCARD = 35977;
    public static final int GL_MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS = 35978;
    public static final int GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS = 35979;
    public static final int GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS = 35968;
    public static final int GL_TRANSFORM_FEEDBACK_VARYINGS = 35971;
    public static final int GL_TRANSFORM_FEEDBACK_BUFFER_MODE = 35967;
    public static final int GL_TRANSFORM_FEEDBACK_VARYING_MAX_LENGTH = 35958;
    public static final int GL_VERTEX_ARRAY_BINDING = 34229;
    public static final int GL_FRAMEBUFFER_SRGB = 36281;
    public static final int GL_FRAMEBUFFER_SRGB_CAPABLE = 36282;

    public static String glGetStringi(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetStringi;
        BufferChecks.checkFunctionAddress(l);
        String str = nglGetStringi(paramInt1, paramInt2, l);
        return str;
    }

    static native String nglGetStringi(int paramInt1, int paramInt2, long paramLong);

    public static void glClearBuffer(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glClearBufferfv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramFloatBuffer, 4);
        nglClearBufferfv(paramInt1, paramInt2, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglClearBufferfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glClearBuffer(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glClearBufferiv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 4);
        nglClearBufferiv(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglClearBufferiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glClearBufferu(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glClearBufferuiv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 4);
        nglClearBufferuiv(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglClearBufferuiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glClearBufferfi(int paramInt1, int paramInt2, float paramFloat, int paramInt3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glClearBufferfi;
        BufferChecks.checkFunctionAddress(l);
        nglClearBufferfi(paramInt1, paramInt2, paramFloat, paramInt3, l);
    }

    static native void nglClearBufferfi(int paramInt1, int paramInt2, float paramFloat, int paramInt3, long paramLong);

    public static void glVertexAttribI1i(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribI1i;
        BufferChecks.checkFunctionAddress(l);
        nglVertexAttribI1i(paramInt1, paramInt2, l);
    }

    static native void nglVertexAttribI1i(int paramInt1, int paramInt2, long paramLong);

    public static void glVertexAttribI2i(int paramInt1, int paramInt2, int paramInt3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribI2i;
        BufferChecks.checkFunctionAddress(l);
        nglVertexAttribI2i(paramInt1, paramInt2, paramInt3, l);
    }

    static native void nglVertexAttribI2i(int paramInt1, int paramInt2, int paramInt3, long paramLong);

    public static void glVertexAttribI3i(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribI3i;
        BufferChecks.checkFunctionAddress(l);
        nglVertexAttribI3i(paramInt1, paramInt2, paramInt3, paramInt4, l);
    }

    static native void nglVertexAttribI3i(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);

    public static void glVertexAttribI4i(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribI4i;
        BufferChecks.checkFunctionAddress(l);
        nglVertexAttribI4i(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, l);
    }

    static native void nglVertexAttribI4i(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);

    public static void glVertexAttribI1ui(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribI1ui;
        BufferChecks.checkFunctionAddress(l);
        nglVertexAttribI1ui(paramInt1, paramInt2, l);
    }

    static native void nglVertexAttribI1ui(int paramInt1, int paramInt2, long paramLong);

    public static void glVertexAttribI2ui(int paramInt1, int paramInt2, int paramInt3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribI2ui;
        BufferChecks.checkFunctionAddress(l);
        nglVertexAttribI2ui(paramInt1, paramInt2, paramInt3, l);
    }

    static native void nglVertexAttribI2ui(int paramInt1, int paramInt2, int paramInt3, long paramLong);

    public static void glVertexAttribI3ui(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribI3ui;
        BufferChecks.checkFunctionAddress(l);
        nglVertexAttribI3ui(paramInt1, paramInt2, paramInt3, paramInt4, l);
    }

    static native void nglVertexAttribI3ui(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);

    public static void glVertexAttribI4ui(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribI4ui;
        BufferChecks.checkFunctionAddress(l);
        nglVertexAttribI4ui(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, l);
    }

    static native void nglVertexAttribI4ui(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);

    public static void glVertexAttribI1(int paramInt, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribI1iv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 1);
        nglVertexAttribI1iv(paramInt, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglVertexAttribI1iv(int paramInt, long paramLong1, long paramLong2);

    public static void glVertexAttribI2(int paramInt, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribI2iv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 2);
        nglVertexAttribI2iv(paramInt, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglVertexAttribI2iv(int paramInt, long paramLong1, long paramLong2);

    public static void glVertexAttribI3(int paramInt, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribI3iv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 3);
        nglVertexAttribI3iv(paramInt, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglVertexAttribI3iv(int paramInt, long paramLong1, long paramLong2);

    public static void glVertexAttribI4(int paramInt, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribI4iv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 4);
        nglVertexAttribI4iv(paramInt, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglVertexAttribI4iv(int paramInt, long paramLong1, long paramLong2);

    public static void glVertexAttribI1u(int paramInt, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribI1uiv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 1);
        nglVertexAttribI1uiv(paramInt, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglVertexAttribI1uiv(int paramInt, long paramLong1, long paramLong2);

    public static void glVertexAttribI2u(int paramInt, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribI2uiv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 2);
        nglVertexAttribI2uiv(paramInt, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglVertexAttribI2uiv(int paramInt, long paramLong1, long paramLong2);

    public static void glVertexAttribI3u(int paramInt, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribI3uiv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 3);
        nglVertexAttribI3uiv(paramInt, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglVertexAttribI3uiv(int paramInt, long paramLong1, long paramLong2);

    public static void glVertexAttribI4u(int paramInt, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribI4uiv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 4);
        nglVertexAttribI4uiv(paramInt, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglVertexAttribI4uiv(int paramInt, long paramLong1, long paramLong2);

    public static void glVertexAttribI4(int paramInt, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribI4bv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramByteBuffer, 4);
        nglVertexAttribI4bv(paramInt, MemoryUtil.getAddress(paramByteBuffer), l);
    }

    static native void nglVertexAttribI4bv(int paramInt, long paramLong1, long paramLong2);

    public static void glVertexAttribI4(int paramInt, ShortBuffer paramShortBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribI4sv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramShortBuffer, 4);
        nglVertexAttribI4sv(paramInt, MemoryUtil.getAddress(paramShortBuffer), l);
    }

    static native void nglVertexAttribI4sv(int paramInt, long paramLong1, long paramLong2);

    public static void glVertexAttribI4u(int paramInt, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribI4ubv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramByteBuffer, 4);
        nglVertexAttribI4ubv(paramInt, MemoryUtil.getAddress(paramByteBuffer), l);
    }

    static native void nglVertexAttribI4ubv(int paramInt, long paramLong1, long paramLong2);

    public static void glVertexAttribI4u(int paramInt, ShortBuffer paramShortBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribI4usv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramShortBuffer, 4);
        nglVertexAttribI4usv(paramInt, MemoryUtil.getAddress(paramShortBuffer), l);
    }

    static native void nglVertexAttribI4usv(int paramInt, long paramLong1, long paramLong2);

    public static void glVertexAttribIPointer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribIPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramByteBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).glVertexAttribPointer_buffer[paramInt1] = paramByteBuffer;
        }
        nglVertexAttribIPointer(paramInt1, paramInt2, paramInt3, paramInt4, MemoryUtil.getAddress(paramByteBuffer), l);
    }

    public static void glVertexAttribIPointer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribIPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramIntBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).glVertexAttribPointer_buffer[paramInt1] = paramIntBuffer;
        }
        nglVertexAttribIPointer(paramInt1, paramInt2, paramInt3, paramInt4, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    public static void glVertexAttribIPointer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ShortBuffer paramShortBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribIPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramShortBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).glVertexAttribPointer_buffer[paramInt1] = paramShortBuffer;
        }
        nglVertexAttribIPointer(paramInt1, paramInt2, paramInt3, paramInt4, MemoryUtil.getAddress(paramShortBuffer), l);
    }

    static native void nglVertexAttribIPointer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2);

    public static void glVertexAttribIPointer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribIPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOenabled(localContextCapabilities);
        nglVertexAttribIPointerBO(paramInt1, paramInt2, paramInt3, paramInt4, paramLong, l);
    }

    static native void nglVertexAttribIPointerBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2);

    public static void glGetVertexAttribI(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetVertexAttribIiv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 4);
        nglGetVertexAttribIiv(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglGetVertexAttribIiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glGetVertexAttribIu(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetVertexAttribIuiv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 4);
        nglGetVertexAttribIuiv(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglGetVertexAttribIuiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glUniform1ui(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform1ui;
        BufferChecks.checkFunctionAddress(l);
        nglUniform1ui(paramInt1, paramInt2, l);
    }

    static native void nglUniform1ui(int paramInt1, int paramInt2, long paramLong);

    public static void glUniform2ui(int paramInt1, int paramInt2, int paramInt3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform2ui;
        BufferChecks.checkFunctionAddress(l);
        nglUniform2ui(paramInt1, paramInt2, paramInt3, l);
    }

    static native void nglUniform2ui(int paramInt1, int paramInt2, int paramInt3, long paramLong);

    public static void glUniform3ui(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform3ui;
        BufferChecks.checkFunctionAddress(l);
        nglUniform3ui(paramInt1, paramInt2, paramInt3, paramInt4, l);
    }

    static native void nglUniform3ui(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);

    public static void glUniform4ui(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform4ui;
        BufferChecks.checkFunctionAddress(l);
        nglUniform4ui(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, l);
    }

    static native void nglUniform4ui(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);

    public static void glUniform1u(int paramInt, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform1uiv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer);
        nglUniform1uiv(paramInt, paramIntBuffer.remaining(), MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglUniform1uiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glUniform2u(int paramInt, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform2uiv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer);
        nglUniform2uiv(paramInt, paramIntBuffer.remaining() & 0x1, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglUniform2uiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glUniform3u(int paramInt, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform3uiv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer);
        nglUniform3uiv(paramIntBuffer.remaining(), -3, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglUniform3uiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glUniform4u(int paramInt, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform4uiv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer);
        nglUniform4uiv(paramInt, paramIntBuffer.remaining() & 0x2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglUniform4uiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glGetUniformu(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetUniformuiv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer);
        nglGetUniformuiv(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglGetUniformuiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glBindFragDataLocation(int paramInt1, int paramInt2, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glBindFragDataLocation;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramByteBuffer);
        BufferChecks.checkNullTerminated(paramByteBuffer);
        nglBindFragDataLocation(paramInt1, paramInt2, MemoryUtil.getAddress(paramByteBuffer), l);
    }

    static native void nglBindFragDataLocation(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glBindFragDataLocation(int paramInt1, int paramInt2, CharSequence paramCharSequence) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glBindFragDataLocation;
        BufferChecks.checkFunctionAddress(l);
        nglBindFragDataLocation(paramInt1, paramInt2, APIUtil.getBufferNT(localContextCapabilities, paramCharSequence), l);
    }

    public static int glGetFragDataLocation(int paramInt, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetFragDataLocation;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramByteBuffer);
        BufferChecks.checkNullTerminated(paramByteBuffer);
        int i = nglGetFragDataLocation(paramInt, MemoryUtil.getAddress(paramByteBuffer), l);
        return i;
    }

    static native int nglGetFragDataLocation(int paramInt, long paramLong1, long paramLong2);

    public static int glGetFragDataLocation(int paramInt, CharSequence paramCharSequence) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetFragDataLocation;
        BufferChecks.checkFunctionAddress(l);
        int i = nglGetFragDataLocation(paramInt, APIUtil.getBufferNT(localContextCapabilities, paramCharSequence), l);
        return i;
    }

    public static void glBeginConditionalRender(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glBeginConditionalRender;
        BufferChecks.checkFunctionAddress(l);
        nglBeginConditionalRender(paramInt1, paramInt2, l);
    }

    static native void nglBeginConditionalRender(int paramInt1, int paramInt2, long paramLong);

    public static void glEndConditionalRender() {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glEndConditionalRender;
        BufferChecks.checkFunctionAddress(l);
        nglEndConditionalRender(l);
    }

    static native void nglEndConditionalRender(long paramLong);

    public static ByteBuffer glMapBufferRange(int paramInt1, long paramLong1, long paramLong2, int paramInt2, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glMapBufferRange;
        BufferChecks.checkFunctionAddress(l);
        if (paramByteBuffer != null) {
            BufferChecks.checkDirect(paramByteBuffer);
        }
        ByteBuffer localByteBuffer = nglMapBufferRange(paramInt1, paramLong1, paramLong2, paramInt2, paramByteBuffer, l);
        return (LWJGLUtil.CHECKS) && (localByteBuffer == null) ? null : localByteBuffer.order(ByteOrder.nativeOrder());
    }

    static native ByteBuffer nglMapBufferRange(int paramInt1, long paramLong1, long paramLong2, int paramInt2, ByteBuffer paramByteBuffer, long paramLong3);

    public static void glFlushMappedBufferRange(int paramInt, long paramLong1, long paramLong2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glFlushMappedBufferRange;
        BufferChecks.checkFunctionAddress(l);
        nglFlushMappedBufferRange(paramInt, paramLong1, paramLong2, l);
    }

    static native void nglFlushMappedBufferRange(int paramInt, long paramLong1, long paramLong2, long paramLong3);

    public static void glClampColor(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glClampColor;
        BufferChecks.checkFunctionAddress(l);
        nglClampColor(paramInt1, paramInt2, l);
    }

    static native void nglClampColor(int paramInt1, int paramInt2, long paramLong);

    public static boolean glIsRenderbuffer(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glIsRenderbuffer;
        BufferChecks.checkFunctionAddress(l);
        boolean bool = nglIsRenderbuffer(paramInt, l);
        return bool;
    }

    static native boolean nglIsRenderbuffer(int paramInt, long paramLong);

    public static void glBindRenderbuffer(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glBindRenderbuffer;
        BufferChecks.checkFunctionAddress(l);
        nglBindRenderbuffer(paramInt1, paramInt2, l);
    }

    static native void nglBindRenderbuffer(int paramInt1, int paramInt2, long paramLong);

    public static void glDeleteRenderbuffers(IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDeleteRenderbuffers;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer);
        nglDeleteRenderbuffers(paramIntBuffer.remaining(), MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglDeleteRenderbuffers(int paramInt, long paramLong1, long paramLong2);

    public static void glDeleteRenderbuffers(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDeleteRenderbuffers;
        BufferChecks.checkFunctionAddress(l);
        nglDeleteRenderbuffers(1, APIUtil.getInt(localContextCapabilities, paramInt), l);
    }

    public static void glGenRenderbuffers(IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGenRenderbuffers;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer);
        nglGenRenderbuffers(paramIntBuffer.remaining(), MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglGenRenderbuffers(int paramInt, long paramLong1, long paramLong2);

    public static int glGenRenderbuffers() {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGenRenderbuffers;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getBufferInt(localContextCapabilities);
        nglGenRenderbuffers(1, MemoryUtil.getAddress(localIntBuffer), l);
        return localIntBuffer.get(0);
    }

    public static void glRenderbufferStorage(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glRenderbufferStorage;
        BufferChecks.checkFunctionAddress(l);
        nglRenderbufferStorage(paramInt1, paramInt2, paramInt3, paramInt4, l);
    }

    static native void nglRenderbufferStorage(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);

    public static void glGetRenderbufferParameter(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetRenderbufferParameteriv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 4);
        nglGetRenderbufferParameteriv(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglGetRenderbufferParameteriv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    @Deprecated
    public static int glGetRenderbufferParameter(int paramInt1, int paramInt2) {
        return glGetRenderbufferParameteri(paramInt1, paramInt2);
    }

    public static int glGetRenderbufferParameteri(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetRenderbufferParameteriv;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getBufferInt(localContextCapabilities);
        nglGetRenderbufferParameteriv(paramInt1, paramInt2, MemoryUtil.getAddress(localIntBuffer), l);
        return localIntBuffer.get(0);
    }

    public static boolean glIsFramebuffer(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glIsFramebuffer;
        BufferChecks.checkFunctionAddress(l);
        boolean bool = nglIsFramebuffer(paramInt, l);
        return bool;
    }

    static native boolean nglIsFramebuffer(int paramInt, long paramLong);

    public static void glBindFramebuffer(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glBindFramebuffer;
        BufferChecks.checkFunctionAddress(l);
        nglBindFramebuffer(paramInt1, paramInt2, l);
    }

    static native void nglBindFramebuffer(int paramInt1, int paramInt2, long paramLong);

    public static void glDeleteFramebuffers(IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDeleteFramebuffers;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer);
        nglDeleteFramebuffers(paramIntBuffer.remaining(), MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglDeleteFramebuffers(int paramInt, long paramLong1, long paramLong2);

    public static void glDeleteFramebuffers(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDeleteFramebuffers;
        BufferChecks.checkFunctionAddress(l);
        nglDeleteFramebuffers(1, APIUtil.getInt(localContextCapabilities, paramInt), l);
    }

    public static void glGenFramebuffers(IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGenFramebuffers;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer);
        nglGenFramebuffers(paramIntBuffer.remaining(), MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglGenFramebuffers(int paramInt, long paramLong1, long paramLong2);

    public static int glGenFramebuffers() {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGenFramebuffers;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getBufferInt(localContextCapabilities);
        nglGenFramebuffers(1, MemoryUtil.getAddress(localIntBuffer), l);
        return localIntBuffer.get(0);
    }

    public static int glCheckFramebufferStatus(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCheckFramebufferStatus;
        BufferChecks.checkFunctionAddress(l);
        int i = nglCheckFramebufferStatus(paramInt, l);
        return i;
    }

    static native int nglCheckFramebufferStatus(int paramInt, long paramLong);

    public static void glFramebufferTexture1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glFramebufferTexture1D;
        BufferChecks.checkFunctionAddress(l);
        nglFramebufferTexture1D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, l);
    }

    static native void nglFramebufferTexture1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);

    public static void glFramebufferTexture2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glFramebufferTexture2D;
        BufferChecks.checkFunctionAddress(l);
        nglFramebufferTexture2D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, l);
    }

    static native void nglFramebufferTexture2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);

    public static void glFramebufferTexture3D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glFramebufferTexture3D;
        BufferChecks.checkFunctionAddress(l);
        nglFramebufferTexture3D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, l);
    }

    static native void nglFramebufferTexture3D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong);

    public static void glFramebufferRenderbuffer(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glFramebufferRenderbuffer;
        BufferChecks.checkFunctionAddress(l);
        nglFramebufferRenderbuffer(paramInt1, paramInt2, paramInt3, paramInt4, l);
    }

    static native void nglFramebufferRenderbuffer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);

    public static void glGetFramebufferAttachmentParameter(int paramInt1, int paramInt2, int paramInt3, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetFramebufferAttachmentParameteriv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 4);
        nglGetFramebufferAttachmentParameteriv(paramInt1, paramInt2, paramInt3, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglGetFramebufferAttachmentParameteriv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);

    @Deprecated
    public static int glGetFramebufferAttachmentParameter(int paramInt1, int paramInt2, int paramInt3) {
        return glGetFramebufferAttachmentParameteri(paramInt1, paramInt2, paramInt3);
    }

    public static int glGetFramebufferAttachmentParameteri(int paramInt1, int paramInt2, int paramInt3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetFramebufferAttachmentParameteriv;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getBufferInt(localContextCapabilities);
        nglGetFramebufferAttachmentParameteriv(paramInt1, paramInt2, paramInt3, MemoryUtil.getAddress(localIntBuffer), l);
        return localIntBuffer.get(0);
    }

    public static void glGenerateMipmap(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGenerateMipmap;
        BufferChecks.checkFunctionAddress(l);
        nglGenerateMipmap(paramInt, l);
    }

    static native void nglGenerateMipmap(int paramInt, long paramLong);

    public static void glRenderbufferStorageMultisample(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glRenderbufferStorageMultisample;
        BufferChecks.checkFunctionAddress(l);
        nglRenderbufferStorageMultisample(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, l);
    }

    static native void nglRenderbufferStorageMultisample(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);

    public static void glBlitFramebuffer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glBlitFramebuffer;
        BufferChecks.checkFunctionAddress(l);
        nglBlitFramebuffer(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramInt9, paramInt10, l);
    }

    static native void nglBlitFramebuffer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, long paramLong);

    public static void glTexParameterI(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexParameterIiv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 4);
        nglTexParameterIiv(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglTexParameterIiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glTexParameterIi(int paramInt1, int paramInt2, int paramInt3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexParameterIiv;
        BufferChecks.checkFunctionAddress(l);
        nglTexParameterIiv(paramInt1, paramInt2, APIUtil.getInt(localContextCapabilities, paramInt3), l);
    }

    public static void glTexParameterIu(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexParameterIuiv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 4);
        nglTexParameterIuiv(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglTexParameterIuiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glTexParameterIui(int paramInt1, int paramInt2, int paramInt3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexParameterIuiv;
        BufferChecks.checkFunctionAddress(l);
        nglTexParameterIuiv(paramInt1, paramInt2, APIUtil.getInt(localContextCapabilities, paramInt3), l);
    }

    public static void glGetTexParameterI(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetTexParameterIiv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 4);
        nglGetTexParameterIiv(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglGetTexParameterIiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static int glGetTexParameterIi(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetTexParameterIiv;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getBufferInt(localContextCapabilities);
        nglGetTexParameterIiv(paramInt1, paramInt2, MemoryUtil.getAddress(localIntBuffer), l);
        return localIntBuffer.get(0);
    }

    public static void glGetTexParameterIu(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetTexParameterIuiv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 4);
        nglGetTexParameterIuiv(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglGetTexParameterIuiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static int glGetTexParameterIui(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetTexParameterIuiv;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getBufferInt(localContextCapabilities);
        nglGetTexParameterIuiv(paramInt1, paramInt2, MemoryUtil.getAddress(localIntBuffer), l);
        return localIntBuffer.get(0);
    }

    public static void glFramebufferTextureLayer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glFramebufferTextureLayer;
        BufferChecks.checkFunctionAddress(l);
        nglFramebufferTextureLayer(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, l);
    }

    static native void nglFramebufferTextureLayer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);

    public static void glColorMaski(int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glColorMaski;
        BufferChecks.checkFunctionAddress(l);
        nglColorMaski(paramInt, paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4, l);
    }

    static native void nglColorMaski(int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, long paramLong);

    public static void glGetBoolean(int paramInt1, int paramInt2, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetBooleani_v;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramByteBuffer, 4);
        nglGetBooleani_v(paramInt1, paramInt2, MemoryUtil.getAddress(paramByteBuffer), l);
    }

    static native void nglGetBooleani_v(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static boolean glGetBoolean(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetBooleani_v;
        BufferChecks.checkFunctionAddress(l);
        ByteBuffer localByteBuffer = APIUtil.getBufferByte(localContextCapabilities, 1);
        nglGetBooleani_v(paramInt1, paramInt2, MemoryUtil.getAddress(localByteBuffer), l);
        return localByteBuffer.get(0) == 1;
    }

    public static void glGetInteger(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetIntegeri_v;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 4);
        nglGetIntegeri_v(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglGetIntegeri_v(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static int glGetInteger(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetIntegeri_v;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getBufferInt(localContextCapabilities);
        nglGetIntegeri_v(paramInt1, paramInt2, MemoryUtil.getAddress(localIntBuffer), l);
        return localIntBuffer.get(0);
    }

    public static void glEnablei(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glEnablei;
        BufferChecks.checkFunctionAddress(l);
        nglEnablei(paramInt1, paramInt2, l);
    }

    static native void nglEnablei(int paramInt1, int paramInt2, long paramLong);

    public static void glDisablei(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDisablei;
        BufferChecks.checkFunctionAddress(l);
        nglDisablei(paramInt1, paramInt2, l);
    }

    static native void nglDisablei(int paramInt1, int paramInt2, long paramLong);

    public static boolean glIsEnabledi(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glIsEnabledi;
        BufferChecks.checkFunctionAddress(l);
        boolean bool = nglIsEnabledi(paramInt1, paramInt2, l);
        return bool;
    }

    static native boolean nglIsEnabledi(int paramInt1, int paramInt2, long paramLong);

    public static void glBindBufferRange(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glBindBufferRange;
        BufferChecks.checkFunctionAddress(l);
        nglBindBufferRange(paramInt1, paramInt2, paramInt3, paramLong1, paramLong2, l);
    }

    static native void nglBindBufferRange(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2, long paramLong3);

    public static void glBindBufferBase(int paramInt1, int paramInt2, int paramInt3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glBindBufferBase;
        BufferChecks.checkFunctionAddress(l);
        nglBindBufferBase(paramInt1, paramInt2, paramInt3, l);
    }

    static native void nglBindBufferBase(int paramInt1, int paramInt2, int paramInt3, long paramLong);

    public static void glBeginTransformFeedback(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glBeginTransformFeedback;
        BufferChecks.checkFunctionAddress(l);
        nglBeginTransformFeedback(paramInt, l);
    }

    static native void nglBeginTransformFeedback(int paramInt, long paramLong);

    public static void glEndTransformFeedback() {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glEndTransformFeedback;
        BufferChecks.checkFunctionAddress(l);
        nglEndTransformFeedback(l);
    }

    static native void nglEndTransformFeedback(long paramLong);

    public static void glTransformFeedbackVaryings(int paramInt1, int paramInt2, ByteBuffer paramByteBuffer, int paramInt3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTransformFeedbackVaryings;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramByteBuffer);
        BufferChecks.checkNullTerminated(paramByteBuffer, paramInt2);
        nglTransformFeedbackVaryings(paramInt1, paramInt2, MemoryUtil.getAddress(paramByteBuffer), paramInt3, l);
    }

    static native void nglTransformFeedbackVaryings(int paramInt1, int paramInt2, long paramLong1, int paramInt3, long paramLong2);

    public static void glTransformFeedbackVaryings(int paramInt1, CharSequence[] paramArrayOfCharSequence, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTransformFeedbackVaryings;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkArray(paramArrayOfCharSequence);
        nglTransformFeedbackVaryings(paramInt1, paramArrayOfCharSequence.length, APIUtil.getBufferNT(localContextCapabilities, paramArrayOfCharSequence), paramInt2, l);
    }

    public static void glGetTransformFeedbackVarying(int paramInt1, int paramInt2, IntBuffer paramIntBuffer1, IntBuffer paramIntBuffer2, IntBuffer paramIntBuffer3, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetTransformFeedbackVarying;
        BufferChecks.checkFunctionAddress(l);
        if (paramIntBuffer1 != null) {
            BufferChecks.checkBuffer(paramIntBuffer1, 1);
        }
        BufferChecks.checkBuffer(paramIntBuffer2, 1);
        BufferChecks.checkBuffer(paramIntBuffer3, 1);
        BufferChecks.checkDirect(paramByteBuffer);
        nglGetTransformFeedbackVarying(paramInt1, paramInt2, paramByteBuffer.remaining(), MemoryUtil.getAddressSafe(paramIntBuffer1), MemoryUtil.getAddress(paramIntBuffer2), MemoryUtil.getAddress(paramIntBuffer3), MemoryUtil.getAddress(paramByteBuffer), l);
    }

    static native void nglGetTransformFeedbackVarying(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5);

    public static String glGetTransformFeedbackVarying(int paramInt1, int paramInt2, int paramInt3, IntBuffer paramIntBuffer1, IntBuffer paramIntBuffer2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetTransformFeedbackVarying;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer1, 1);
        BufferChecks.checkBuffer(paramIntBuffer2, 1);
        IntBuffer localIntBuffer = APIUtil.getLengths(localContextCapabilities);
        ByteBuffer localByteBuffer = APIUtil.getBufferByte(localContextCapabilities, paramInt3);
        nglGetTransformFeedbackVarying(paramInt1, paramInt2, paramInt3, MemoryUtil.getAddress0(localIntBuffer), MemoryUtil.getAddress(paramIntBuffer1), MemoryUtil.getAddress(paramIntBuffer2), MemoryUtil.getAddress(localByteBuffer), l);
        localByteBuffer.limit(localIntBuffer.get(0));
        return APIUtil.getString(localContextCapabilities, localByteBuffer);
    }

    public static void glBindVertexArray(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glBindVertexArray;
        BufferChecks.checkFunctionAddress(l);
        StateTracker.bindVAO(localContextCapabilities, paramInt);
        nglBindVertexArray(paramInt, l);
    }

    static native void nglBindVertexArray(int paramInt, long paramLong);

    public static void glDeleteVertexArrays(IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDeleteVertexArrays;
        BufferChecks.checkFunctionAddress(l);
        StateTracker.deleteVAO(localContextCapabilities, paramIntBuffer);
        BufferChecks.checkDirect(paramIntBuffer);
        nglDeleteVertexArrays(paramIntBuffer.remaining(), MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglDeleteVertexArrays(int paramInt, long paramLong1, long paramLong2);

    public static void glDeleteVertexArrays(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDeleteVertexArrays;
        BufferChecks.checkFunctionAddress(l);
        StateTracker.deleteVAO(localContextCapabilities, paramInt);
        nglDeleteVertexArrays(1, APIUtil.getInt(localContextCapabilities, paramInt), l);
    }

    public static void glGenVertexArrays(IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGenVertexArrays;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer);
        nglGenVertexArrays(paramIntBuffer.remaining(), MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglGenVertexArrays(int paramInt, long paramLong1, long paramLong2);

    public static int glGenVertexArrays() {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGenVertexArrays;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getBufferInt(localContextCapabilities);
        nglGenVertexArrays(1, MemoryUtil.getAddress(localIntBuffer), l);
        return localIntBuffer.get(0);
    }

    public static boolean glIsVertexArray(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glIsVertexArray;
        BufferChecks.checkFunctionAddress(l);
        boolean bool = nglIsVertexArray(paramInt, l);
        return bool;
    }

    static native boolean nglIsVertexArray(int paramInt, long paramLong);
}





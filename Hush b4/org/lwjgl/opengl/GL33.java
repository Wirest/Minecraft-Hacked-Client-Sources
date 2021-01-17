// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.LongBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.ByteBuffer;

public final class GL33
{
    public static final int GL_SRC1_COLOR = 35065;
    public static final int GL_ONE_MINUS_SRC1_COLOR = 35066;
    public static final int GL_ONE_MINUS_SRC1_ALPHA = 35067;
    public static final int GL_MAX_DUAL_SOURCE_DRAW_BUFFERS = 35068;
    public static final int GL_ANY_SAMPLES_PASSED = 35887;
    public static final int GL_SAMPLER_BINDING = 35097;
    public static final int GL_RGB10_A2UI = 36975;
    public static final int GL_TEXTURE_SWIZZLE_R = 36418;
    public static final int GL_TEXTURE_SWIZZLE_G = 36419;
    public static final int GL_TEXTURE_SWIZZLE_B = 36420;
    public static final int GL_TEXTURE_SWIZZLE_A = 36421;
    public static final int GL_TEXTURE_SWIZZLE_RGBA = 36422;
    public static final int GL_TIME_ELAPSED = 35007;
    public static final int GL_TIMESTAMP = 36392;
    public static final int GL_VERTEX_ATTRIB_ARRAY_DIVISOR = 35070;
    public static final int GL_INT_2_10_10_10_REV = 36255;
    
    private GL33() {
    }
    
    public static void glBindFragDataLocationIndexed(final int program, final int colorNumber, final int index, final ByteBuffer name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindFragDataLocationIndexed;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(name);
        BufferChecks.checkNullTerminated(name);
        nglBindFragDataLocationIndexed(program, colorNumber, index, MemoryUtil.getAddress(name), function_pointer);
    }
    
    static native void nglBindFragDataLocationIndexed(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glBindFragDataLocationIndexed(final int program, final int colorNumber, final int index, final CharSequence name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindFragDataLocationIndexed;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBindFragDataLocationIndexed(program, colorNumber, index, APIUtil.getBufferNT(caps, name), function_pointer);
    }
    
    public static int glGetFragDataIndex(final int program, final ByteBuffer name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetFragDataIndex;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(name);
        BufferChecks.checkNullTerminated(name);
        final int __result = nglGetFragDataIndex(program, MemoryUtil.getAddress(name), function_pointer);
        return __result;
    }
    
    static native int nglGetFragDataIndex(final int p0, final long p1, final long p2);
    
    public static int glGetFragDataIndex(final int program, final CharSequence name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetFragDataIndex;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nglGetFragDataIndex(program, APIUtil.getBufferNT(caps, name), function_pointer);
        return __result;
    }
    
    public static void glGenSamplers(final IntBuffer samplers) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenSamplers;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(samplers);
        nglGenSamplers(samplers.remaining(), MemoryUtil.getAddress(samplers), function_pointer);
    }
    
    static native void nglGenSamplers(final int p0, final long p1, final long p2);
    
    public static int glGenSamplers() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenSamplers;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer samplers = APIUtil.getBufferInt(caps);
        nglGenSamplers(1, MemoryUtil.getAddress(samplers), function_pointer);
        return samplers.get(0);
    }
    
    public static void glDeleteSamplers(final IntBuffer samplers) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteSamplers;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(samplers);
        nglDeleteSamplers(samplers.remaining(), MemoryUtil.getAddress(samplers), function_pointer);
    }
    
    static native void nglDeleteSamplers(final int p0, final long p1, final long p2);
    
    public static void glDeleteSamplers(final int sampler) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteSamplers;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDeleteSamplers(1, APIUtil.getInt(caps, sampler), function_pointer);
    }
    
    public static boolean glIsSampler(final int sampler) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsSampler;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsSampler(sampler, function_pointer);
        return __result;
    }
    
    static native boolean nglIsSampler(final int p0, final long p1);
    
    public static void glBindSampler(final int unit, final int sampler) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindSampler;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBindSampler(unit, sampler, function_pointer);
    }
    
    static native void nglBindSampler(final int p0, final int p1, final long p2);
    
    public static void glSamplerParameteri(final int sampler, final int pname, final int param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSamplerParameteri;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglSamplerParameteri(sampler, pname, param, function_pointer);
    }
    
    static native void nglSamplerParameteri(final int p0, final int p1, final int p2, final long p3);
    
    public static void glSamplerParameterf(final int sampler, final int pname, final float param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSamplerParameterf;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglSamplerParameterf(sampler, pname, param, function_pointer);
    }
    
    static native void nglSamplerParameterf(final int p0, final int p1, final float p2, final long p3);
    
    public static void glSamplerParameter(final int sampler, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSamplerParameteriv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglSamplerParameteriv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglSamplerParameteriv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glSamplerParameter(final int sampler, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSamplerParameterfv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglSamplerParameterfv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglSamplerParameterfv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glSamplerParameterI(final int sampler, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSamplerParameterIiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglSamplerParameterIiv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglSamplerParameterIiv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glSamplerParameterIu(final int sampler, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSamplerParameterIuiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglSamplerParameterIuiv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglSamplerParameterIuiv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetSamplerParameter(final int sampler, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSamplerParameteriv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetSamplerParameteriv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetSamplerParameteriv(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetSamplerParameteri(final int sampler, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSamplerParameteriv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetSamplerParameteriv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetSamplerParameter(final int sampler, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSamplerParameterfv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetSamplerParameterfv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetSamplerParameterfv(final int p0, final int p1, final long p2, final long p3);
    
    public static float glGetSamplerParameterf(final int sampler, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSamplerParameterfv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final FloatBuffer params = APIUtil.getBufferFloat(caps);
        nglGetSamplerParameterfv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetSamplerParameterI(final int sampler, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSamplerParameterIiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetSamplerParameterIiv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetSamplerParameterIiv(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetSamplerParameterIi(final int sampler, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSamplerParameterIiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetSamplerParameterIiv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetSamplerParameterIu(final int sampler, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSamplerParameterIuiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetSamplerParameterIuiv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetSamplerParameterIuiv(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetSamplerParameterIui(final int sampler, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSamplerParameterIuiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetSamplerParameterIuiv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glQueryCounter(final int id, final int target) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glQueryCounter;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglQueryCounter(id, target, function_pointer);
    }
    
    static native void nglQueryCounter(final int p0, final int p1, final long p2);
    
    public static void glGetQueryObject(final int id, final int pname, final LongBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetQueryObjecti64v;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetQueryObjecti64v(id, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetQueryObjecti64v(final int p0, final int p1, final long p2, final long p3);
    
    @Deprecated
    public static long glGetQueryObject(final int id, final int pname) {
        return glGetQueryObjecti64(id, pname);
    }
    
    public static long glGetQueryObjecti64(final int id, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetQueryObjecti64v;
        BufferChecks.checkFunctionAddress(function_pointer);
        final LongBuffer params = APIUtil.getBufferLong(caps);
        nglGetQueryObjecti64v(id, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetQueryObjectu(final int id, final int pname, final LongBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetQueryObjectui64v;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetQueryObjectui64v(id, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetQueryObjectui64v(final int p0, final int p1, final long p2, final long p3);
    
    @Deprecated
    public static long glGetQueryObjectu(final int id, final int pname) {
        return glGetQueryObjectui64(id, pname);
    }
    
    public static long glGetQueryObjectui64(final int id, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetQueryObjectui64v;
        BufferChecks.checkFunctionAddress(function_pointer);
        final LongBuffer params = APIUtil.getBufferLong(caps);
        nglGetQueryObjectui64v(id, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glVertexAttribDivisor(final int index, final int divisor) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribDivisor;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribDivisor(index, divisor, function_pointer);
    }
    
    static native void nglVertexAttribDivisor(final int p0, final int p1, final long p2);
    
    public static void glVertexP2ui(final int type, final int value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexP2ui;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexP2ui(type, value, function_pointer);
    }
    
    static native void nglVertexP2ui(final int p0, final int p1, final long p2);
    
    public static void glVertexP3ui(final int type, final int value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexP3ui;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexP3ui(type, value, function_pointer);
    }
    
    static native void nglVertexP3ui(final int p0, final int p1, final long p2);
    
    public static void glVertexP4ui(final int type, final int value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexP4ui;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexP4ui(type, value, function_pointer);
    }
    
    static native void nglVertexP4ui(final int p0, final int p1, final long p2);
    
    public static void glVertexP2u(final int type, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexP2uiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(value, 2);
        nglVertexP2uiv(type, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglVertexP2uiv(final int p0, final long p1, final long p2);
    
    public static void glVertexP3u(final int type, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexP3uiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(value, 3);
        nglVertexP3uiv(type, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglVertexP3uiv(final int p0, final long p1, final long p2);
    
    public static void glVertexP4u(final int type, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexP4uiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(value, 4);
        nglVertexP4uiv(type, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglVertexP4uiv(final int p0, final long p1, final long p2);
    
    public static void glTexCoordP1ui(final int type, final int coords) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTexCoordP1ui;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTexCoordP1ui(type, coords, function_pointer);
    }
    
    static native void nglTexCoordP1ui(final int p0, final int p1, final long p2);
    
    public static void glTexCoordP2ui(final int type, final int coords) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTexCoordP2ui;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTexCoordP2ui(type, coords, function_pointer);
    }
    
    static native void nglTexCoordP2ui(final int p0, final int p1, final long p2);
    
    public static void glTexCoordP3ui(final int type, final int coords) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTexCoordP3ui;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTexCoordP3ui(type, coords, function_pointer);
    }
    
    static native void nglTexCoordP3ui(final int p0, final int p1, final long p2);
    
    public static void glTexCoordP4ui(final int type, final int coords) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTexCoordP4ui;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTexCoordP4ui(type, coords, function_pointer);
    }
    
    static native void nglTexCoordP4ui(final int p0, final int p1, final long p2);
    
    public static void glTexCoordP1u(final int type, final IntBuffer coords) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTexCoordP1uiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(coords, 1);
        nglTexCoordP1uiv(type, MemoryUtil.getAddress(coords), function_pointer);
    }
    
    static native void nglTexCoordP1uiv(final int p0, final long p1, final long p2);
    
    public static void glTexCoordP2u(final int type, final IntBuffer coords) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTexCoordP2uiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(coords, 2);
        nglTexCoordP2uiv(type, MemoryUtil.getAddress(coords), function_pointer);
    }
    
    static native void nglTexCoordP2uiv(final int p0, final long p1, final long p2);
    
    public static void glTexCoordP3u(final int type, final IntBuffer coords) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTexCoordP3uiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(coords, 3);
        nglTexCoordP3uiv(type, MemoryUtil.getAddress(coords), function_pointer);
    }
    
    static native void nglTexCoordP3uiv(final int p0, final long p1, final long p2);
    
    public static void glTexCoordP4u(final int type, final IntBuffer coords) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTexCoordP4uiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(coords, 4);
        nglTexCoordP4uiv(type, MemoryUtil.getAddress(coords), function_pointer);
    }
    
    static native void nglTexCoordP4uiv(final int p0, final long p1, final long p2);
    
    public static void glMultiTexCoordP1ui(final int texture, final int type, final int coords) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoordP1ui;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexCoordP1ui(texture, type, coords, function_pointer);
    }
    
    static native void nglMultiTexCoordP1ui(final int p0, final int p1, final int p2, final long p3);
    
    public static void glMultiTexCoordP2ui(final int texture, final int type, final int coords) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoordP2ui;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexCoordP2ui(texture, type, coords, function_pointer);
    }
    
    static native void nglMultiTexCoordP2ui(final int p0, final int p1, final int p2, final long p3);
    
    public static void glMultiTexCoordP3ui(final int texture, final int type, final int coords) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoordP3ui;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexCoordP3ui(texture, type, coords, function_pointer);
    }
    
    static native void nglMultiTexCoordP3ui(final int p0, final int p1, final int p2, final long p3);
    
    public static void glMultiTexCoordP4ui(final int texture, final int type, final int coords) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoordP4ui;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexCoordP4ui(texture, type, coords, function_pointer);
    }
    
    static native void nglMultiTexCoordP4ui(final int p0, final int p1, final int p2, final long p3);
    
    public static void glMultiTexCoordP1u(final int texture, final int type, final IntBuffer coords) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoordP1uiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(coords, 1);
        nglMultiTexCoordP1uiv(texture, type, MemoryUtil.getAddress(coords), function_pointer);
    }
    
    static native void nglMultiTexCoordP1uiv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glMultiTexCoordP2u(final int texture, final int type, final IntBuffer coords) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoordP2uiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(coords, 2);
        nglMultiTexCoordP2uiv(texture, type, MemoryUtil.getAddress(coords), function_pointer);
    }
    
    static native void nglMultiTexCoordP2uiv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glMultiTexCoordP3u(final int texture, final int type, final IntBuffer coords) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoordP3uiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(coords, 3);
        nglMultiTexCoordP3uiv(texture, type, MemoryUtil.getAddress(coords), function_pointer);
    }
    
    static native void nglMultiTexCoordP3uiv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glMultiTexCoordP4u(final int texture, final int type, final IntBuffer coords) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoordP4uiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(coords, 4);
        nglMultiTexCoordP4uiv(texture, type, MemoryUtil.getAddress(coords), function_pointer);
    }
    
    static native void nglMultiTexCoordP4uiv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glNormalP3ui(final int type, final int coords) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNormalP3ui;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNormalP3ui(type, coords, function_pointer);
    }
    
    static native void nglNormalP3ui(final int p0, final int p1, final long p2);
    
    public static void glNormalP3u(final int type, final IntBuffer coords) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNormalP3uiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(coords, 3);
        nglNormalP3uiv(type, MemoryUtil.getAddress(coords), function_pointer);
    }
    
    static native void nglNormalP3uiv(final int p0, final long p1, final long p2);
    
    public static void glColorP3ui(final int type, final int color) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColorP3ui;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglColorP3ui(type, color, function_pointer);
    }
    
    static native void nglColorP3ui(final int p0, final int p1, final long p2);
    
    public static void glColorP4ui(final int type, final int color) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColorP4ui;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglColorP4ui(type, color, function_pointer);
    }
    
    static native void nglColorP4ui(final int p0, final int p1, final long p2);
    
    public static void glColorP3u(final int type, final IntBuffer color) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColorP3uiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(color, 3);
        nglColorP3uiv(type, MemoryUtil.getAddress(color), function_pointer);
    }
    
    static native void nglColorP3uiv(final int p0, final long p1, final long p2);
    
    public static void glColorP4u(final int type, final IntBuffer color) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColorP4uiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(color, 4);
        nglColorP4uiv(type, MemoryUtil.getAddress(color), function_pointer);
    }
    
    static native void nglColorP4uiv(final int p0, final long p1, final long p2);
    
    public static void glSecondaryColorP3ui(final int type, final int color) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSecondaryColorP3ui;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglSecondaryColorP3ui(type, color, function_pointer);
    }
    
    static native void nglSecondaryColorP3ui(final int p0, final int p1, final long p2);
    
    public static void glSecondaryColorP3u(final int type, final IntBuffer color) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSecondaryColorP3uiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(color, 3);
        nglSecondaryColorP3uiv(type, MemoryUtil.getAddress(color), function_pointer);
    }
    
    static native void nglSecondaryColorP3uiv(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribP1ui(final int index, final int type, final boolean normalized, final int value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribP1ui;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribP1ui(index, type, normalized, value, function_pointer);
    }
    
    static native void nglVertexAttribP1ui(final int p0, final int p1, final boolean p2, final int p3, final long p4);
    
    public static void glVertexAttribP2ui(final int index, final int type, final boolean normalized, final int value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribP2ui;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribP2ui(index, type, normalized, value, function_pointer);
    }
    
    static native void nglVertexAttribP2ui(final int p0, final int p1, final boolean p2, final int p3, final long p4);
    
    public static void glVertexAttribP3ui(final int index, final int type, final boolean normalized, final int value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribP3ui;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribP3ui(index, type, normalized, value, function_pointer);
    }
    
    static native void nglVertexAttribP3ui(final int p0, final int p1, final boolean p2, final int p3, final long p4);
    
    public static void glVertexAttribP4ui(final int index, final int type, final boolean normalized, final int value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribP4ui;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribP4ui(index, type, normalized, value, function_pointer);
    }
    
    static native void nglVertexAttribP4ui(final int p0, final int p1, final boolean p2, final int p3, final long p4);
    
    public static void glVertexAttribP1u(final int index, final int type, final boolean normalized, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribP1uiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(value, 1);
        nglVertexAttribP1uiv(index, type, normalized, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglVertexAttribP1uiv(final int p0, final int p1, final boolean p2, final long p3, final long p4);
    
    public static void glVertexAttribP2u(final int index, final int type, final boolean normalized, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribP2uiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(value, 2);
        nglVertexAttribP2uiv(index, type, normalized, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglVertexAttribP2uiv(final int p0, final int p1, final boolean p2, final long p3, final long p4);
    
    public static void glVertexAttribP3u(final int index, final int type, final boolean normalized, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribP3uiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(value, 3);
        nglVertexAttribP3uiv(index, type, normalized, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglVertexAttribP3uiv(final int p0, final int p1, final boolean p2, final long p3, final long p4);
    
    public static void glVertexAttribP4u(final int index, final int type, final boolean normalized, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribP4uiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(value, 4);
        nglVertexAttribP4uiv(index, type, normalized, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglVertexAttribP4uiv(final int p0, final int p1, final boolean p2, final long p3, final long p4);
}

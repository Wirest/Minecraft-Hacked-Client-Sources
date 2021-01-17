// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.nio.IntBuffer;
import java.nio.ByteBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.LongBuffer;

public final class GL32
{
    public static final int GL_CONTEXT_PROFILE_MASK = 37158;
    public static final int GL_CONTEXT_CORE_PROFILE_BIT = 1;
    public static final int GL_CONTEXT_COMPATIBILITY_PROFILE_BIT = 2;
    public static final int GL_MAX_VERTEX_OUTPUT_COMPONENTS = 37154;
    public static final int GL_MAX_GEOMETRY_INPUT_COMPONENTS = 37155;
    public static final int GL_MAX_GEOMETRY_OUTPUT_COMPONENTS = 37156;
    public static final int GL_MAX_FRAGMENT_INPUT_COMPONENTS = 37157;
    public static final int GL_FIRST_VERTEX_CONVENTION = 36429;
    public static final int GL_LAST_VERTEX_CONVENTION = 36430;
    public static final int GL_PROVOKING_VERTEX = 36431;
    public static final int GL_QUADS_FOLLOW_PROVOKING_VERTEX_CONVENTION = 36428;
    public static final int GL_TEXTURE_CUBE_MAP_SEAMLESS = 34895;
    public static final int GL_SAMPLE_POSITION = 36432;
    public static final int GL_SAMPLE_MASK = 36433;
    public static final int GL_SAMPLE_MASK_VALUE = 36434;
    public static final int GL_TEXTURE_2D_MULTISAMPLE = 37120;
    public static final int GL_PROXY_TEXTURE_2D_MULTISAMPLE = 37121;
    public static final int GL_TEXTURE_2D_MULTISAMPLE_ARRAY = 37122;
    public static final int GL_PROXY_TEXTURE_2D_MULTISAMPLE_ARRAY = 37123;
    public static final int GL_MAX_SAMPLE_MASK_WORDS = 36441;
    public static final int GL_MAX_COLOR_TEXTURE_SAMPLES = 37134;
    public static final int GL_MAX_DEPTH_TEXTURE_SAMPLES = 37135;
    public static final int GL_MAX_INTEGER_SAMPLES = 37136;
    public static final int GL_TEXTURE_BINDING_2D_MULTISAMPLE = 37124;
    public static final int GL_TEXTURE_BINDING_2D_MULTISAMPLE_ARRAY = 37125;
    public static final int GL_TEXTURE_SAMPLES = 37126;
    public static final int GL_TEXTURE_FIXED_SAMPLE_LOCATIONS = 37127;
    public static final int GL_SAMPLER_2D_MULTISAMPLE = 37128;
    public static final int GL_INT_SAMPLER_2D_MULTISAMPLE = 37129;
    public static final int GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE = 37130;
    public static final int GL_SAMPLER_2D_MULTISAMPLE_ARRAY = 37131;
    public static final int GL_INT_SAMPLER_2D_MULTISAMPLE_ARRAY = 37132;
    public static final int GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE_ARRAY = 37133;
    public static final int GL_DEPTH_CLAMP = 34383;
    public static final int GL_GEOMETRY_SHADER = 36313;
    public static final int GL_GEOMETRY_VERTICES_OUT = 36314;
    public static final int GL_GEOMETRY_INPUT_TYPE = 36315;
    public static final int GL_GEOMETRY_OUTPUT_TYPE = 36316;
    public static final int GL_MAX_GEOMETRY_TEXTURE_IMAGE_UNITS = 35881;
    public static final int GL_MAX_GEOMETRY_UNIFORM_COMPONENTS = 36319;
    public static final int GL_MAX_GEOMETRY_OUTPUT_VERTICES = 36320;
    public static final int GL_MAX_GEOMETRY_TOTAL_OUTPUT_COMPONENTS = 36321;
    public static final int GL_LINES_ADJACENCY = 10;
    public static final int GL_LINE_STRIP_ADJACENCY = 11;
    public static final int GL_TRIANGLES_ADJACENCY = 12;
    public static final int GL_TRIANGLE_STRIP_ADJACENCY = 13;
    public static final int GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS = 36264;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_LAYERED = 36263;
    public static final int GL_PROGRAM_POINT_SIZE = 34370;
    public static final int GL_MAX_SERVER_WAIT_TIMEOUT = 37137;
    public static final int GL_OBJECT_TYPE = 37138;
    public static final int GL_SYNC_CONDITION = 37139;
    public static final int GL_SYNC_STATUS = 37140;
    public static final int GL_SYNC_FLAGS = 37141;
    public static final int GL_SYNC_FENCE = 37142;
    public static final int GL_SYNC_GPU_COMMANDS_COMPLETE = 37143;
    public static final int GL_UNSIGNALED = 37144;
    public static final int GL_SIGNALED = 37145;
    public static final int GL_SYNC_FLUSH_COMMANDS_BIT = 1;
    public static final long GL_TIMEOUT_IGNORED = -1L;
    public static final int GL_ALREADY_SIGNALED = 37146;
    public static final int GL_TIMEOUT_EXPIRED = 37147;
    public static final int GL_CONDITION_SATISFIED = 37148;
    public static final int GL_WAIT_FAILED = 37149;
    
    private GL32() {
    }
    
    public static void glGetBufferParameter(final int target, final int pname, final LongBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetBufferParameteri64v;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetBufferParameteri64v(target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetBufferParameteri64v(final int p0, final int p1, final long p2, final long p3);
    
    @Deprecated
    public static long glGetBufferParameter(final int target, final int pname) {
        return glGetBufferParameteri64(target, pname);
    }
    
    public static long glGetBufferParameteri64(final int target, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetBufferParameteri64v;
        BufferChecks.checkFunctionAddress(function_pointer);
        final LongBuffer params = APIUtil.getBufferLong(caps);
        nglGetBufferParameteri64v(target, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glDrawElementsBaseVertex(final int mode, final ByteBuffer indices, final int basevertex) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawElementsBaseVertex;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureElementVBOdisabled(caps);
        BufferChecks.checkDirect(indices);
        nglDrawElementsBaseVertex(mode, indices.remaining(), 5121, MemoryUtil.getAddress(indices), basevertex, function_pointer);
    }
    
    public static void glDrawElementsBaseVertex(final int mode, final IntBuffer indices, final int basevertex) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawElementsBaseVertex;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureElementVBOdisabled(caps);
        BufferChecks.checkDirect(indices);
        nglDrawElementsBaseVertex(mode, indices.remaining(), 5125, MemoryUtil.getAddress(indices), basevertex, function_pointer);
    }
    
    public static void glDrawElementsBaseVertex(final int mode, final ShortBuffer indices, final int basevertex) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawElementsBaseVertex;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureElementVBOdisabled(caps);
        BufferChecks.checkDirect(indices);
        nglDrawElementsBaseVertex(mode, indices.remaining(), 5123, MemoryUtil.getAddress(indices), basevertex, function_pointer);
    }
    
    static native void nglDrawElementsBaseVertex(final int p0, final int p1, final int p2, final long p3, final int p4, final long p5);
    
    public static void glDrawElementsBaseVertex(final int mode, final int indices_count, final int type, final long indices_buffer_offset, final int basevertex) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawElementsBaseVertex;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureElementVBOenabled(caps);
        nglDrawElementsBaseVertexBO(mode, indices_count, type, indices_buffer_offset, basevertex, function_pointer);
    }
    
    static native void nglDrawElementsBaseVertexBO(final int p0, final int p1, final int p2, final long p3, final int p4, final long p5);
    
    public static void glDrawRangeElementsBaseVertex(final int mode, final int start, final int end, final ByteBuffer indices, final int basevertex) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawRangeElementsBaseVertex;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureElementVBOdisabled(caps);
        BufferChecks.checkDirect(indices);
        nglDrawRangeElementsBaseVertex(mode, start, end, indices.remaining(), 5121, MemoryUtil.getAddress(indices), basevertex, function_pointer);
    }
    
    public static void glDrawRangeElementsBaseVertex(final int mode, final int start, final int end, final IntBuffer indices, final int basevertex) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawRangeElementsBaseVertex;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureElementVBOdisabled(caps);
        BufferChecks.checkDirect(indices);
        nglDrawRangeElementsBaseVertex(mode, start, end, indices.remaining(), 5125, MemoryUtil.getAddress(indices), basevertex, function_pointer);
    }
    
    public static void glDrawRangeElementsBaseVertex(final int mode, final int start, final int end, final ShortBuffer indices, final int basevertex) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawRangeElementsBaseVertex;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureElementVBOdisabled(caps);
        BufferChecks.checkDirect(indices);
        nglDrawRangeElementsBaseVertex(mode, start, end, indices.remaining(), 5123, MemoryUtil.getAddress(indices), basevertex, function_pointer);
    }
    
    static native void nglDrawRangeElementsBaseVertex(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5, final int p6, final long p7);
    
    public static void glDrawRangeElementsBaseVertex(final int mode, final int start, final int end, final int indices_count, final int type, final long indices_buffer_offset, final int basevertex) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawRangeElementsBaseVertex;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureElementVBOenabled(caps);
        nglDrawRangeElementsBaseVertexBO(mode, start, end, indices_count, type, indices_buffer_offset, basevertex, function_pointer);
    }
    
    static native void nglDrawRangeElementsBaseVertexBO(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5, final int p6, final long p7);
    
    public static void glDrawElementsInstancedBaseVertex(final int mode, final ByteBuffer indices, final int primcount, final int basevertex) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawElementsInstancedBaseVertex;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureElementVBOdisabled(caps);
        BufferChecks.checkDirect(indices);
        nglDrawElementsInstancedBaseVertex(mode, indices.remaining(), 5121, MemoryUtil.getAddress(indices), primcount, basevertex, function_pointer);
    }
    
    public static void glDrawElementsInstancedBaseVertex(final int mode, final IntBuffer indices, final int primcount, final int basevertex) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawElementsInstancedBaseVertex;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureElementVBOdisabled(caps);
        BufferChecks.checkDirect(indices);
        nglDrawElementsInstancedBaseVertex(mode, indices.remaining(), 5125, MemoryUtil.getAddress(indices), primcount, basevertex, function_pointer);
    }
    
    public static void glDrawElementsInstancedBaseVertex(final int mode, final ShortBuffer indices, final int primcount, final int basevertex) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawElementsInstancedBaseVertex;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureElementVBOdisabled(caps);
        BufferChecks.checkDirect(indices);
        nglDrawElementsInstancedBaseVertex(mode, indices.remaining(), 5123, MemoryUtil.getAddress(indices), primcount, basevertex, function_pointer);
    }
    
    static native void nglDrawElementsInstancedBaseVertex(final int p0, final int p1, final int p2, final long p3, final int p4, final int p5, final long p6);
    
    public static void glDrawElementsInstancedBaseVertex(final int mode, final int indices_count, final int type, final long indices_buffer_offset, final int primcount, final int basevertex) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawElementsInstancedBaseVertex;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureElementVBOenabled(caps);
        nglDrawElementsInstancedBaseVertexBO(mode, indices_count, type, indices_buffer_offset, primcount, basevertex, function_pointer);
    }
    
    static native void nglDrawElementsInstancedBaseVertexBO(final int p0, final int p1, final int p2, final long p3, final int p4, final int p5, final long p6);
    
    public static void glProvokingVertex(final int mode) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProvokingVertex;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProvokingVertex(mode, function_pointer);
    }
    
    static native void nglProvokingVertex(final int p0, final long p1);
    
    public static void glTexImage2DMultisample(final int target, final int samples, final int internalformat, final int width, final int height, final boolean fixedsamplelocations) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTexImage2DMultisample;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTexImage2DMultisample(target, samples, internalformat, width, height, fixedsamplelocations, function_pointer);
    }
    
    static native void nglTexImage2DMultisample(final int p0, final int p1, final int p2, final int p3, final int p4, final boolean p5, final long p6);
    
    public static void glTexImage3DMultisample(final int target, final int samples, final int internalformat, final int width, final int height, final int depth, final boolean fixedsamplelocations) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTexImage3DMultisample;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTexImage3DMultisample(target, samples, internalformat, width, height, depth, fixedsamplelocations, function_pointer);
    }
    
    static native void nglTexImage3DMultisample(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final boolean p6, final long p7);
    
    public static void glGetMultisample(final int pname, final int index, final FloatBuffer val) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMultisamplefv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(val, 2);
        nglGetMultisamplefv(pname, index, MemoryUtil.getAddress(val), function_pointer);
    }
    
    static native void nglGetMultisamplefv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glSampleMaski(final int index, final int mask) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSampleMaski;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglSampleMaski(index, mask, function_pointer);
    }
    
    static native void nglSampleMaski(final int p0, final int p1, final long p2);
    
    public static void glFramebufferTexture(final int target, final int attachment, final int texture, final int level) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFramebufferTexture;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglFramebufferTexture(target, attachment, texture, level, function_pointer);
    }
    
    static native void nglFramebufferTexture(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static GLSync glFenceSync(final int condition, final int flags) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFenceSync;
        BufferChecks.checkFunctionAddress(function_pointer);
        final GLSync __result = new GLSync(nglFenceSync(condition, flags, function_pointer));
        return __result;
    }
    
    static native long nglFenceSync(final int p0, final int p1, final long p2);
    
    public static boolean glIsSync(final GLSync sync) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsSync;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsSync(sync.getPointer(), function_pointer);
        return __result;
    }
    
    static native boolean nglIsSync(final long p0, final long p1);
    
    public static void glDeleteSync(final GLSync sync) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteSync;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDeleteSync(sync.getPointer(), function_pointer);
    }
    
    static native void nglDeleteSync(final long p0, final long p1);
    
    public static int glClientWaitSync(final GLSync sync, final int flags, final long timeout) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glClientWaitSync;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nglClientWaitSync(sync.getPointer(), flags, timeout, function_pointer);
        return __result;
    }
    
    static native int nglClientWaitSync(final long p0, final int p1, final long p2, final long p3);
    
    public static void glWaitSync(final GLSync sync, final int flags, final long timeout) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glWaitSync;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglWaitSync(sync.getPointer(), flags, timeout, function_pointer);
    }
    
    static native void nglWaitSync(final long p0, final int p1, final long p2, final long p3);
    
    public static void glGetInteger64(final int pname, final LongBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetInteger64v;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(data, 1);
        nglGetInteger64v(pname, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglGetInteger64v(final int p0, final long p1, final long p2);
    
    public static long glGetInteger64(final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetInteger64v;
        BufferChecks.checkFunctionAddress(function_pointer);
        final LongBuffer data = APIUtil.getBufferLong(caps);
        nglGetInteger64v(pname, MemoryUtil.getAddress(data), function_pointer);
        return data.get(0);
    }
    
    public static void glGetInteger64(final int value, final int index, final LongBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetInteger64i_v;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(data, 4);
        nglGetInteger64i_v(value, index, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglGetInteger64i_v(final int p0, final int p1, final long p2, final long p3);
    
    public static long glGetInteger64(final int value, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetInteger64i_v;
        BufferChecks.checkFunctionAddress(function_pointer);
        final LongBuffer data = APIUtil.getBufferLong(caps);
        nglGetInteger64i_v(value, index, MemoryUtil.getAddress(data), function_pointer);
        return data.get(0);
    }
    
    public static void glGetSync(final GLSync sync, final int pname, final IntBuffer length, final IntBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSynciv;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (length != null) {
            BufferChecks.checkBuffer(length, 1);
        }
        BufferChecks.checkDirect(values);
        nglGetSynciv(sync.getPointer(), pname, values.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(values), function_pointer);
    }
    
    static native void nglGetSynciv(final long p0, final int p1, final int p2, final long p3, final long p4, final long p5);
    
    @Deprecated
    public static int glGetSync(final GLSync sync, final int pname) {
        return glGetSynci(sync, pname);
    }
    
    public static int glGetSynci(final GLSync sync, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSynciv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer values = APIUtil.getBufferInt(caps);
        nglGetSynciv(sync.getPointer(), pname, 1, 0L, MemoryUtil.getAddress(values), function_pointer);
        return values.get(0);
    }
}

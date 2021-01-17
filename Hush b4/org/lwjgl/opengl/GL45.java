// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.PointerBuffer;
import java.nio.Buffer;
import java.nio.ByteOrder;
import org.lwjgl.LWJGLUtil;
import java.nio.ShortBuffer;
import java.nio.FloatBuffer;
import java.nio.DoubleBuffer;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import org.lwjgl.MemoryUtil;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;

public final class GL45
{
    public static final int GL_NEGATIVE_ONE_TO_ONE = 37726;
    public static final int GL_ZERO_TO_ONE = 37727;
    public static final int GL_CLIP_ORIGIN = 37724;
    public static final int GL_CLIP_DEPTH_MODE = 37725;
    public static final int GL_QUERY_WAIT_INVERTED = 36375;
    public static final int GL_QUERY_NO_WAIT_INVERTED = 36376;
    public static final int GL_QUERY_BY_REGION_WAIT_INVERTED = 36377;
    public static final int GL_QUERY_BY_REGION_NO_WAIT_INVERTED = 36378;
    public static final int GL_MAX_CULL_DISTANCES = 33529;
    public static final int GL_MAX_COMBINED_CLIP_AND_CULL_DISTANCES = 33530;
    public static final int GL_TEXTURE_TARGET = 4102;
    public static final int GL_QUERY_TARGET = 33514;
    public static final int GL_TEXTURE_BINDING = 33515;
    public static final int GL_CONTEXT_RELEASE_BEHAVIOR = 33531;
    public static final int GL_CONTEXT_RELEASE_BEHAVIOR_FLUSH = 33532;
    public static final int GL_GUILTY_CONTEXT_RESET = 33363;
    public static final int GL_INNOCENT_CONTEXT_RESET = 33364;
    public static final int GL_UNKNOWN_CONTEXT_RESET = 33365;
    public static final int GL_CONTEXT_ROBUST_ACCESS = 37107;
    public static final int GL_RESET_NOTIFICATION_STRATEGY = 33366;
    public static final int GL_LOSE_CONTEXT_ON_RESET = 33362;
    public static final int GL_NO_RESET_NOTIFICATION = 33377;
    public static final int GL_CONTEXT_LOST = 1287;
    
    private GL45() {
    }
    
    public static void glClipControl(final int origin, final int depth) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glClipControl;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglClipControl(origin, depth, function_pointer);
    }
    
    static native void nglClipControl(final int p0, final int p1, final long p2);
    
    public static void glCreateTransformFeedbacks(final IntBuffer ids) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCreateTransformFeedbacks;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(ids);
        nglCreateTransformFeedbacks(ids.remaining(), MemoryUtil.getAddress(ids), function_pointer);
    }
    
    static native void nglCreateTransformFeedbacks(final int p0, final long p1, final long p2);
    
    public static int glCreateTransformFeedbacks() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCreateTransformFeedbacks;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer ids = APIUtil.getBufferInt(caps);
        nglCreateTransformFeedbacks(1, MemoryUtil.getAddress(ids), function_pointer);
        return ids.get(0);
    }
    
    public static void glTransformFeedbackBufferBase(final int xfb, final int index, final int buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTransformFeedbackBufferBase;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTransformFeedbackBufferBase(xfb, index, buffer, function_pointer);
    }
    
    static native void nglTransformFeedbackBufferBase(final int p0, final int p1, final int p2, final long p3);
    
    public static void glTransformFeedbackBufferRange(final int xfb, final int index, final int buffer, final long offset, final long size) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTransformFeedbackBufferRange;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTransformFeedbackBufferRange(xfb, index, buffer, offset, size, function_pointer);
    }
    
    static native void nglTransformFeedbackBufferRange(final int p0, final int p1, final int p2, final long p3, final long p4, final long p5);
    
    public static void glGetTransformFeedback(final int xfb, final int pname, final IntBuffer param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTransformFeedbackiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(param, 1);
        nglGetTransformFeedbackiv(xfb, pname, MemoryUtil.getAddress(param), function_pointer);
    }
    
    static native void nglGetTransformFeedbackiv(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetTransformFeedbacki(final int xfb, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTransformFeedbackiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer param = APIUtil.getBufferInt(caps);
        nglGetTransformFeedbackiv(xfb, pname, MemoryUtil.getAddress(param), function_pointer);
        return param.get(0);
    }
    
    public static void glGetTransformFeedback(final int xfb, final int pname, final int index, final IntBuffer param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTransformFeedbacki_v;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(param, 1);
        nglGetTransformFeedbacki_v(xfb, pname, index, MemoryUtil.getAddress(param), function_pointer);
    }
    
    static native void nglGetTransformFeedbacki_v(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static int glGetTransformFeedbacki(final int xfb, final int pname, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTransformFeedbacki_v;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer param = APIUtil.getBufferInt(caps);
        nglGetTransformFeedbacki_v(xfb, pname, index, MemoryUtil.getAddress(param), function_pointer);
        return param.get(0);
    }
    
    public static void glGetTransformFeedback(final int xfb, final int pname, final int index, final LongBuffer param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTransformFeedbacki64_v;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(param, 1);
        nglGetTransformFeedbacki64_v(xfb, pname, index, MemoryUtil.getAddress(param), function_pointer);
    }
    
    static native void nglGetTransformFeedbacki64_v(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static long glGetTransformFeedbacki64(final int xfb, final int pname, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTransformFeedbacki64_v;
        BufferChecks.checkFunctionAddress(function_pointer);
        final LongBuffer param = APIUtil.getBufferLong(caps);
        nglGetTransformFeedbacki64_v(xfb, pname, index, MemoryUtil.getAddress(param), function_pointer);
        return param.get(0);
    }
    
    public static void glCreateBuffers(final IntBuffer buffers) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCreateBuffers;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(buffers);
        nglCreateBuffers(buffers.remaining(), MemoryUtil.getAddress(buffers), function_pointer);
    }
    
    static native void nglCreateBuffers(final int p0, final long p1, final long p2);
    
    public static int glCreateBuffers() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCreateBuffers;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer buffers = APIUtil.getBufferInt(caps);
        nglCreateBuffers(1, MemoryUtil.getAddress(buffers), function_pointer);
        return buffers.get(0);
    }
    
    public static void glNamedBufferStorage(final int buffer, final ByteBuffer data, final int flags) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferStorage;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferStorage(buffer, data.remaining(), MemoryUtil.getAddress(data), flags, function_pointer);
    }
    
    public static void glNamedBufferStorage(final int buffer, final DoubleBuffer data, final int flags) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferStorage;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferStorage(buffer, data.remaining() << 3, MemoryUtil.getAddress(data), flags, function_pointer);
    }
    
    public static void glNamedBufferStorage(final int buffer, final FloatBuffer data, final int flags) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferStorage;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferStorage(buffer, data.remaining() << 2, MemoryUtil.getAddress(data), flags, function_pointer);
    }
    
    public static void glNamedBufferStorage(final int buffer, final IntBuffer data, final int flags) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferStorage;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferStorage(buffer, data.remaining() << 2, MemoryUtil.getAddress(data), flags, function_pointer);
    }
    
    public static void glNamedBufferStorage(final int buffer, final ShortBuffer data, final int flags) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferStorage;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferStorage(buffer, data.remaining() << 1, MemoryUtil.getAddress(data), flags, function_pointer);
    }
    
    public static void glNamedBufferStorage(final int buffer, final LongBuffer data, final int flags) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferStorage;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferStorage(buffer, data.remaining() << 3, MemoryUtil.getAddress(data), flags, function_pointer);
    }
    
    static native void nglNamedBufferStorage(final int p0, final long p1, final long p2, final int p3, final long p4);
    
    public static void glNamedBufferStorage(final int buffer, final long size, final int flags) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferStorage;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNamedBufferStorage(buffer, size, 0L, flags, function_pointer);
    }
    
    public static void glNamedBufferData(final int buffer, final long data_size, final int usage) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferData;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNamedBufferData(buffer, data_size, 0L, usage, function_pointer);
    }
    
    public static void glNamedBufferData(final int buffer, final ByteBuffer data, final int usage) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferData(buffer, data.remaining(), MemoryUtil.getAddress(data), usage, function_pointer);
    }
    
    public static void glNamedBufferData(final int buffer, final DoubleBuffer data, final int usage) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferData(buffer, data.remaining() << 3, MemoryUtil.getAddress(data), usage, function_pointer);
    }
    
    public static void glNamedBufferData(final int buffer, final FloatBuffer data, final int usage) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferData(buffer, data.remaining() << 2, MemoryUtil.getAddress(data), usage, function_pointer);
    }
    
    public static void glNamedBufferData(final int buffer, final IntBuffer data, final int usage) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferData(buffer, data.remaining() << 2, MemoryUtil.getAddress(data), usage, function_pointer);
    }
    
    public static void glNamedBufferData(final int buffer, final ShortBuffer data, final int usage) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferData(buffer, data.remaining() << 1, MemoryUtil.getAddress(data), usage, function_pointer);
    }
    
    static native void nglNamedBufferData(final int p0, final long p1, final long p2, final int p3, final long p4);
    
    public static void glNamedBufferSubData(final int buffer, final long offset, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferSubData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferSubData(buffer, offset, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glNamedBufferSubData(final int buffer, final long offset, final DoubleBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferSubData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferSubData(buffer, offset, data.remaining() << 3, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glNamedBufferSubData(final int buffer, final long offset, final FloatBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferSubData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferSubData(buffer, offset, data.remaining() << 2, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glNamedBufferSubData(final int buffer, final long offset, final IntBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferSubData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferSubData(buffer, offset, data.remaining() << 2, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glNamedBufferSubData(final int buffer, final long offset, final ShortBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferSubData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferSubData(buffer, offset, data.remaining() << 1, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglNamedBufferSubData(final int p0, final long p1, final long p2, final long p3, final long p4);
    
    public static void glCopyNamedBufferSubData(final int readBuffer, final int writeBuffer, final long readOffset, final long writeOffset, final long size) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCopyNamedBufferSubData;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglCopyNamedBufferSubData(readBuffer, writeBuffer, readOffset, writeOffset, size, function_pointer);
    }
    
    static native void nglCopyNamedBufferSubData(final int p0, final int p1, final long p2, final long p3, final long p4, final long p5);
    
    public static void glClearNamedBufferData(final int buffer, final int internalformat, final int format, final int type, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glClearNamedBufferData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(data, 1);
        nglClearNamedBufferData(buffer, internalformat, format, type, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglClearNamedBufferData(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glClearNamedBufferSubData(final int buffer, final int internalformat, final long offset, final long size, final int format, final int type, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glClearNamedBufferSubData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(data, 1);
        nglClearNamedBufferSubData(buffer, internalformat, offset, size, format, type, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglClearNamedBufferSubData(final int p0, final int p1, final long p2, final long p3, final int p4, final int p5, final long p6, final long p7);
    
    public static ByteBuffer glMapNamedBuffer(final int buffer, final int access, final ByteBuffer old_buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMapNamedBuffer;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (old_buffer != null) {
            BufferChecks.checkDirect(old_buffer);
        }
        final ByteBuffer __result = nglMapNamedBuffer(buffer, access, glGetNamedBufferParameteri(buffer, 34660), old_buffer, function_pointer);
        return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
    }
    
    public static ByteBuffer glMapNamedBuffer(final int buffer, final int access, final long length, final ByteBuffer old_buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMapNamedBuffer;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (old_buffer != null) {
            BufferChecks.checkDirect(old_buffer);
        }
        final ByteBuffer __result = nglMapNamedBuffer(buffer, access, length, old_buffer, function_pointer);
        return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
    }
    
    static native ByteBuffer nglMapNamedBuffer(final int p0, final int p1, final long p2, final ByteBuffer p3, final long p4);
    
    public static ByteBuffer glMapNamedBufferRange(final int buffer, final long offset, final long length, final int access, final ByteBuffer old_buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMapNamedBufferRange;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (old_buffer != null) {
            BufferChecks.checkDirect(old_buffer);
        }
        final ByteBuffer __result = nglMapNamedBufferRange(buffer, offset, length, access, old_buffer, function_pointer);
        return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
    }
    
    static native ByteBuffer nglMapNamedBufferRange(final int p0, final long p1, final long p2, final int p3, final ByteBuffer p4, final long p5);
    
    public static boolean glUnmapNamedBuffer(final int buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUnmapNamedBuffer;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglUnmapNamedBuffer(buffer, function_pointer);
        return __result;
    }
    
    static native boolean nglUnmapNamedBuffer(final int p0, final long p1);
    
    public static void glFlushMappedNamedBufferRange(final int buffer, final long offset, final long length) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFlushMappedNamedBufferRange;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglFlushMappedNamedBufferRange(buffer, offset, length, function_pointer);
    }
    
    static native void nglFlushMappedNamedBufferRange(final int p0, final long p1, final long p2, final long p3);
    
    public static void glGetNamedBufferParameter(final int buffer, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedBufferParameteriv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglGetNamedBufferParameteriv(buffer, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetNamedBufferParameteriv(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetNamedBufferParameteri(final int buffer, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedBufferParameteriv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetNamedBufferParameteriv(buffer, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetNamedBufferParameter(final int buffer, final int pname, final LongBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedBufferParameteri64v;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetNamedBufferParameteri64v(buffer, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetNamedBufferParameteri64v(final int p0, final int p1, final long p2, final long p3);
    
    public static long glGetNamedBufferParameteri64(final int buffer, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedBufferParameteri64v;
        BufferChecks.checkFunctionAddress(function_pointer);
        final LongBuffer params = APIUtil.getBufferLong(caps);
        nglGetNamedBufferParameteri64v(buffer, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static ByteBuffer glGetNamedBufferPointer(final int buffer, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedBufferPointerv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final ByteBuffer __result = nglGetNamedBufferPointerv(buffer, pname, glGetNamedBufferParameteri(buffer, 34660), function_pointer);
        return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
    }
    
    static native ByteBuffer nglGetNamedBufferPointerv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetNamedBufferSubData(final int buffer, final long offset, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedBufferSubData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglGetNamedBufferSubData(buffer, offset, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glGetNamedBufferSubData(final int buffer, final long offset, final DoubleBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedBufferSubData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglGetNamedBufferSubData(buffer, offset, data.remaining() << 3, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glGetNamedBufferSubData(final int buffer, final long offset, final FloatBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedBufferSubData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglGetNamedBufferSubData(buffer, offset, data.remaining() << 2, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glGetNamedBufferSubData(final int buffer, final long offset, final IntBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedBufferSubData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglGetNamedBufferSubData(buffer, offset, data.remaining() << 2, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glGetNamedBufferSubData(final int buffer, final long offset, final ShortBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedBufferSubData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglGetNamedBufferSubData(buffer, offset, data.remaining() << 1, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglGetNamedBufferSubData(final int p0, final long p1, final long p2, final long p3, final long p4);
    
    public static void glCreateFramebuffers(final IntBuffer framebuffers) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCreateFramebuffers;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(framebuffers);
        nglCreateFramebuffers(framebuffers.remaining(), MemoryUtil.getAddress(framebuffers), function_pointer);
    }
    
    static native void nglCreateFramebuffers(final int p0, final long p1, final long p2);
    
    public static int glCreateFramebuffers() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCreateFramebuffers;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer framebuffers = APIUtil.getBufferInt(caps);
        nglCreateFramebuffers(1, MemoryUtil.getAddress(framebuffers), function_pointer);
        return framebuffers.get(0);
    }
    
    public static void glNamedFramebufferRenderbuffer(final int framebuffer, final int attachment, final int renderbuffertarget, final int renderbuffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedFramebufferRenderbuffer;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNamedFramebufferRenderbuffer(framebuffer, attachment, renderbuffertarget, renderbuffer, function_pointer);
    }
    
    static native void nglNamedFramebufferRenderbuffer(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glNamedFramebufferParameteri(final int framebuffer, final int pname, final int param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedFramebufferParameteri;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNamedFramebufferParameteri(framebuffer, pname, param, function_pointer);
    }
    
    static native void nglNamedFramebufferParameteri(final int p0, final int p1, final int p2, final long p3);
    
    public static void glNamedFramebufferTexture(final int framebuffer, final int attachment, final int texture, final int level) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedFramebufferTexture;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNamedFramebufferTexture(framebuffer, attachment, texture, level, function_pointer);
    }
    
    static native void nglNamedFramebufferTexture(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glNamedFramebufferTextureLayer(final int framebuffer, final int attachment, final int texture, final int level, final int layer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedFramebufferTextureLayer;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNamedFramebufferTextureLayer(framebuffer, attachment, texture, level, layer, function_pointer);
    }
    
    static native void nglNamedFramebufferTextureLayer(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glNamedFramebufferDrawBuffer(final int framebuffer, final int mode) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedFramebufferDrawBuffer;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNamedFramebufferDrawBuffer(framebuffer, mode, function_pointer);
    }
    
    static native void nglNamedFramebufferDrawBuffer(final int p0, final int p1, final long p2);
    
    public static void glNamedFramebufferDrawBuffers(final int framebuffer, final IntBuffer bufs) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedFramebufferDrawBuffers;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(bufs);
        nglNamedFramebufferDrawBuffers(framebuffer, bufs.remaining(), MemoryUtil.getAddress(bufs), function_pointer);
    }
    
    static native void nglNamedFramebufferDrawBuffers(final int p0, final int p1, final long p2, final long p3);
    
    public static void glNamedFramebufferReadBuffer(final int framebuffer, final int mode) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedFramebufferReadBuffer;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNamedFramebufferReadBuffer(framebuffer, mode, function_pointer);
    }
    
    static native void nglNamedFramebufferReadBuffer(final int p0, final int p1, final long p2);
    
    public static void glInvalidateNamedFramebufferData(final int framebuffer, final IntBuffer attachments) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glInvalidateNamedFramebufferData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(attachments);
        nglInvalidateNamedFramebufferData(framebuffer, attachments.remaining(), MemoryUtil.getAddress(attachments), function_pointer);
    }
    
    static native void nglInvalidateNamedFramebufferData(final int p0, final int p1, final long p2, final long p3);
    
    public static void glInvalidateNamedFramebufferSubData(final int framebuffer, final IntBuffer attachments, final int x, final int y, final int width, final int height) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glInvalidateNamedFramebufferSubData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(attachments);
        nglInvalidateNamedFramebufferSubData(framebuffer, attachments.remaining(), MemoryUtil.getAddress(attachments), x, y, width, height, function_pointer);
    }
    
    static native void nglInvalidateNamedFramebufferSubData(final int p0, final int p1, final long p2, final int p3, final int p4, final int p5, final int p6, final long p7);
    
    public static void glClearNamedFramebuffer(final int framebuffer, final int buffer, final int drawbuffer, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glClearNamedFramebufferiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(value, 1);
        nglClearNamedFramebufferiv(framebuffer, buffer, drawbuffer, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglClearNamedFramebufferiv(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glClearNamedFramebufferu(final int framebuffer, final int buffer, final int drawbuffer, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glClearNamedFramebufferuiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(value, 4);
        nglClearNamedFramebufferuiv(framebuffer, buffer, drawbuffer, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglClearNamedFramebufferuiv(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glClearNamedFramebuffer(final int framebuffer, final int buffer, final int drawbuffer, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glClearNamedFramebufferfv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(value, 1);
        nglClearNamedFramebufferfv(framebuffer, buffer, drawbuffer, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglClearNamedFramebufferfv(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glClearNamedFramebufferfi(final int framebuffer, final int buffer, final float depth, final int stencil) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glClearNamedFramebufferfi;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglClearNamedFramebufferfi(framebuffer, buffer, depth, stencil, function_pointer);
    }
    
    static native void nglClearNamedFramebufferfi(final int p0, final int p1, final float p2, final int p3, final long p4);
    
    public static void glBlitNamedFramebuffer(final int readFramebuffer, final int drawFramebuffer, final int srcX0, final int srcY0, final int srcX1, final int srcY1, final int dstX0, final int dstY0, final int dstX1, final int dstY1, final int mask, final int filter) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBlitNamedFramebuffer;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBlitNamedFramebuffer(readFramebuffer, drawFramebuffer, srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter, function_pointer);
    }
    
    static native void nglBlitNamedFramebuffer(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final int p10, final int p11, final long p12);
    
    public static int glCheckNamedFramebufferStatus(final int framebuffer, final int target) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCheckNamedFramebufferStatus;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nglCheckNamedFramebufferStatus(framebuffer, target, function_pointer);
        return __result;
    }
    
    static native int nglCheckNamedFramebufferStatus(final int p0, final int p1, final long p2);
    
    public static void glGetNamedFramebufferParameter(final int framebuffer, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedFramebufferParameteriv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetNamedFramebufferParameteriv(framebuffer, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetNamedFramebufferParameteriv(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetNamedFramebufferParameter(final int framebuffer, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedFramebufferParameteriv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetNamedFramebufferParameteriv(framebuffer, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetNamedFramebufferAttachmentParameter(final int framebuffer, final int attachment, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedFramebufferAttachmentParameteriv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetNamedFramebufferAttachmentParameteriv(framebuffer, attachment, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetNamedFramebufferAttachmentParameteriv(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static int glGetNamedFramebufferAttachmentParameter(final int framebuffer, final int attachment, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedFramebufferAttachmentParameteriv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetNamedFramebufferAttachmentParameteriv(framebuffer, attachment, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glCreateRenderbuffers(final IntBuffer renderbuffers) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCreateRenderbuffers;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(renderbuffers);
        nglCreateRenderbuffers(renderbuffers.remaining(), MemoryUtil.getAddress(renderbuffers), function_pointer);
    }
    
    static native void nglCreateRenderbuffers(final int p0, final long p1, final long p2);
    
    public static int glCreateRenderbuffers() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCreateRenderbuffers;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer renderbuffers = APIUtil.getBufferInt(caps);
        nglCreateRenderbuffers(1, MemoryUtil.getAddress(renderbuffers), function_pointer);
        return renderbuffers.get(0);
    }
    
    public static void glNamedRenderbufferStorage(final int renderbuffer, final int internalformat, final int width, final int height) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedRenderbufferStorage;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNamedRenderbufferStorage(renderbuffer, internalformat, width, height, function_pointer);
    }
    
    static native void nglNamedRenderbufferStorage(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glNamedRenderbufferStorageMultisample(final int renderbuffer, final int samples, final int internalformat, final int width, final int height) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedRenderbufferStorageMultisample;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNamedRenderbufferStorageMultisample(renderbuffer, samples, internalformat, width, height, function_pointer);
    }
    
    static native void nglNamedRenderbufferStorageMultisample(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glGetNamedRenderbufferParameter(final int renderbuffer, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedRenderbufferParameteriv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetNamedRenderbufferParameteriv(renderbuffer, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetNamedRenderbufferParameteriv(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetNamedRenderbufferParameter(final int renderbuffer, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedRenderbufferParameteriv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetNamedRenderbufferParameteriv(renderbuffer, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glCreateTextures(final int target, final IntBuffer textures) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCreateTextures;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(textures);
        nglCreateTextures(target, textures.remaining(), MemoryUtil.getAddress(textures), function_pointer);
    }
    
    static native void nglCreateTextures(final int p0, final int p1, final long p2, final long p3);
    
    public static int glCreateTextures(final int target) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCreateTextures;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer textures = APIUtil.getBufferInt(caps);
        nglCreateTextures(target, 1, MemoryUtil.getAddress(textures), function_pointer);
        return textures.get(0);
    }
    
    public static void glTextureBuffer(final int texture, final int internalformat, final int buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureBuffer;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTextureBuffer(texture, internalformat, buffer, function_pointer);
    }
    
    static native void nglTextureBuffer(final int p0, final int p1, final int p2, final long p3);
    
    public static void glTextureBufferRange(final int texture, final int internalformat, final int buffer, final long offset, final long size) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureBufferRange;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTextureBufferRange(texture, internalformat, buffer, offset, size, function_pointer);
    }
    
    static native void nglTextureBufferRange(final int p0, final int p1, final int p2, final long p3, final long p4, final long p5);
    
    public static void glTextureStorage1D(final int texture, final int levels, final int internalformat, final int width) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureStorage1D;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTextureStorage1D(texture, levels, internalformat, width, function_pointer);
    }
    
    static native void nglTextureStorage1D(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glTextureStorage2D(final int texture, final int levels, final int internalformat, final int width, final int height) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureStorage2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTextureStorage2D(texture, levels, internalformat, width, height, function_pointer);
    }
    
    static native void nglTextureStorage2D(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glTextureStorage3D(final int texture, final int levels, final int internalformat, final int width, final int height, final int depth) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureStorage3D;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTextureStorage3D(texture, levels, internalformat, width, height, depth, function_pointer);
    }
    
    static native void nglTextureStorage3D(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6);
    
    public static void glTextureStorage2DMultisample(final int texture, final int samples, final int internalformat, final int width, final int height, final boolean fixedsamplelocations) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureStorage2DMultisample;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTextureStorage2DMultisample(texture, samples, internalformat, width, height, fixedsamplelocations, function_pointer);
    }
    
    static native void nglTextureStorage2DMultisample(final int p0, final int p1, final int p2, final int p3, final int p4, final boolean p5, final long p6);
    
    public static void glTextureStorage3DMultisample(final int texture, final int samples, final int internalformat, final int width, final int height, final int depth, final boolean fixedsamplelocations) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureStorage3DMultisample;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTextureStorage3DMultisample(texture, samples, internalformat, width, height, depth, fixedsamplelocations, function_pointer);
    }
    
    static native void nglTextureStorage3DMultisample(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final boolean p6, final long p7);
    
    public static void glTextureSubImage1D(final int texture, final int level, final int xoffset, final int width, final int format, final int type, final ByteBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage1D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
        nglTextureSubImage1D(texture, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glTextureSubImage1D(final int texture, final int level, final int xoffset, final int width, final int format, final int type, final DoubleBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage1D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
        nglTextureSubImage1D(texture, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glTextureSubImage1D(final int texture, final int level, final int xoffset, final int width, final int format, final int type, final FloatBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage1D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
        nglTextureSubImage1D(texture, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glTextureSubImage1D(final int texture, final int level, final int xoffset, final int width, final int format, final int type, final IntBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage1D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
        nglTextureSubImage1D(texture, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glTextureSubImage1D(final int texture, final int level, final int xoffset, final int width, final int format, final int type, final ShortBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage1D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
        nglTextureSubImage1D(texture, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    static native void nglTextureSubImage1D(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6, final long p7);
    
    public static void glTextureSubImage1D(final int texture, final int level, final int xoffset, final int width, final int format, final int type, final long pixels_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage1D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglTextureSubImage1DBO(texture, level, xoffset, width, format, type, pixels_buffer_offset, function_pointer);
    }
    
    static native void nglTextureSubImage1DBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6, final long p7);
    
    public static void glTextureSubImage2D(final int texture, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final int type, final ByteBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
        nglTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glTextureSubImage2D(final int texture, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final int type, final DoubleBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
        nglTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glTextureSubImage2D(final int texture, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final int type, final FloatBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
        nglTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glTextureSubImage2D(final int texture, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final int type, final IntBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
        nglTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glTextureSubImage2D(final int texture, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final int type, final ShortBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
        nglTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    static native void nglTextureSubImage2D(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final long p8, final long p9);
    
    public static void glTextureSubImage2D(final int texture, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final int type, final long pixels_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglTextureSubImage2DBO(texture, level, xoffset, yoffset, width, height, format, type, pixels_buffer_offset, function_pointer);
    }
    
    static native void nglTextureSubImage2DBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final long p8, final long p9);
    
    public static void glTextureSubImage3D(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final ByteBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage3D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
        nglTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glTextureSubImage3D(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final DoubleBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage3D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
        nglTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glTextureSubImage3D(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final FloatBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage3D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
        nglTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glTextureSubImage3D(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final IntBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage3D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
        nglTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glTextureSubImage3D(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final ShortBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage3D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
        nglTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    static native void nglTextureSubImage3D(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final long p10, final long p11);
    
    public static void glTextureSubImage3D(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final long pixels_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage3D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglTextureSubImage3DBO(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels_buffer_offset, function_pointer);
    }
    
    static native void nglTextureSubImage3DBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final long p10, final long p11);
    
    public static void glCompressedTextureSubImage1D(final int texture, final int level, final int xoffset, final int width, final int format, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedTextureSubImage1D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(data);
        nglCompressedTextureSubImage1D(texture, level, xoffset, width, format, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglCompressedTextureSubImage1D(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6, final long p7);
    
    public static void glCompressedTextureSubImage1D(final int texture, final int level, final int xoffset, final int width, final int format, final int data_imageSize, final long data_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedTextureSubImage1D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglCompressedTextureSubImage1DBO(texture, level, xoffset, width, format, data_imageSize, data_buffer_offset, function_pointer);
    }
    
    static native void nglCompressedTextureSubImage1DBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6, final long p7);
    
    public static void glCompressedTextureSubImage2D(final int texture, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedTextureSubImage2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(data);
        nglCompressedTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglCompressedTextureSubImage2D(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final long p8, final long p9);
    
    public static void glCompressedTextureSubImage2D(final int texture, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final int data_imageSize, final long data_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedTextureSubImage2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglCompressedTextureSubImage2DBO(texture, level, xoffset, yoffset, width, height, format, data_imageSize, data_buffer_offset, function_pointer);
    }
    
    static native void nglCompressedTextureSubImage2DBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final long p8, final long p9);
    
    public static void glCompressedTextureSubImage3D(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int imageSize, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedTextureSubImage3D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(data);
        nglCompressedTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, imageSize, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglCompressedTextureSubImage3D(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final long p10, final long p11);
    
    public static void glCompressedTextureSubImage3D(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int imageSize, final long data_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedTextureSubImage3D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglCompressedTextureSubImage3DBO(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, imageSize, data_buffer_offset, function_pointer);
    }
    
    static native void nglCompressedTextureSubImage3DBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final long p10, final long p11);
    
    public static void glCopyTextureSubImage1D(final int texture, final int level, final int xoffset, final int x, final int y, final int width) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCopyTextureSubImage1D;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglCopyTextureSubImage1D(texture, level, xoffset, x, y, width, function_pointer);
    }
    
    static native void nglCopyTextureSubImage1D(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6);
    
    public static void glCopyTextureSubImage2D(final int texture, final int level, final int xoffset, final int yoffset, final int x, final int y, final int width, final int height) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCopyTextureSubImage2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglCopyTextureSubImage2D(texture, level, xoffset, yoffset, x, y, width, height, function_pointer);
    }
    
    static native void nglCopyTextureSubImage2D(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final long p8);
    
    public static void glCopyTextureSubImage3D(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int x, final int y, final int width, final int height) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCopyTextureSubImage3D;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglCopyTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, x, y, width, height, function_pointer);
    }
    
    static native void nglCopyTextureSubImage3D(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final long p9);
    
    public static void glTextureParameterf(final int texture, final int pname, final float param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureParameterf;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTextureParameterf(texture, pname, param, function_pointer);
    }
    
    static native void nglTextureParameterf(final int p0, final int p1, final float p2, final long p3);
    
    public static void glTextureParameter(final int texture, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureParameterfv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglTextureParameterfv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglTextureParameterfv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glTextureParameteri(final int texture, final int pname, final int param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureParameteri;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTextureParameteri(texture, pname, param, function_pointer);
    }
    
    static native void nglTextureParameteri(final int p0, final int p1, final int p2, final long p3);
    
    public static void glTextureParameterI(final int texture, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureParameterIiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglTextureParameterIiv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglTextureParameterIiv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glTextureParameterIu(final int texture, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureParameterIuiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglTextureParameterIuiv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglTextureParameterIuiv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glTextureParameter(final int texture, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureParameteriv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglTextureParameteriv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglTextureParameteriv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGenerateTextureMipmap(final int texture) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenerateTextureMipmap;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglGenerateTextureMipmap(texture, function_pointer);
    }
    
    static native void nglGenerateTextureMipmap(final int p0, final long p1);
    
    public static void glBindTextureUnit(final int unit, final int texture) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindTextureUnit;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBindTextureUnit(unit, texture, function_pointer);
    }
    
    static native void nglBindTextureUnit(final int p0, final int p1, final long p2);
    
    public static void glGetTextureImage(final int texture, final int level, final int format, final int type, final ByteBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(pixels);
        nglGetTextureImage(texture, level, format, type, pixels.remaining(), MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glGetTextureImage(final int texture, final int level, final int format, final int type, final DoubleBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(pixels);
        nglGetTextureImage(texture, level, format, type, pixels.remaining() << 3, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glGetTextureImage(final int texture, final int level, final int format, final int type, final FloatBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(pixels);
        nglGetTextureImage(texture, level, format, type, pixels.remaining() << 2, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glGetTextureImage(final int texture, final int level, final int format, final int type, final IntBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(pixels);
        nglGetTextureImage(texture, level, format, type, pixels.remaining() << 2, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glGetTextureImage(final int texture, final int level, final int format, final int type, final ShortBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(pixels);
        nglGetTextureImage(texture, level, format, type, pixels.remaining() << 1, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    static native void nglGetTextureImage(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5, final long p6);
    
    public static void glGetTextureImage(final int texture, final int level, final int format, final int type, final int pixels_bufSize, final long pixels_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOenabled(caps);
        nglGetTextureImageBO(texture, level, format, type, pixels_bufSize, pixels_buffer_offset, function_pointer);
    }
    
    static native void nglGetTextureImageBO(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5, final long p6);
    
    public static void glGetCompressedTextureImage(final int texture, final int level, final ByteBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetCompressedTextureImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(pixels);
        nglGetCompressedTextureImage(texture, level, pixels.remaining(), MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glGetCompressedTextureImage(final int texture, final int level, final IntBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetCompressedTextureImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(pixels);
        nglGetCompressedTextureImage(texture, level, pixels.remaining() << 2, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glGetCompressedTextureImage(final int texture, final int level, final ShortBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetCompressedTextureImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(pixels);
        nglGetCompressedTextureImage(texture, level, pixels.remaining() << 1, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    static native void nglGetCompressedTextureImage(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetCompressedTextureImage(final int texture, final int level, final int pixels_bufSize, final long pixels_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetCompressedTextureImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOenabled(caps);
        nglGetCompressedTextureImageBO(texture, level, pixels_bufSize, pixels_buffer_offset, function_pointer);
    }
    
    static native void nglGetCompressedTextureImageBO(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetTextureLevelParameter(final int texture, final int level, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureLevelParameterfv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetTextureLevelParameterfv(texture, level, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetTextureLevelParameterfv(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static float glGetTextureLevelParameterf(final int texture, final int level, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureLevelParameterfv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final FloatBuffer params = APIUtil.getBufferFloat(caps);
        nglGetTextureLevelParameterfv(texture, level, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetTextureLevelParameter(final int texture, final int level, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureLevelParameteriv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetTextureLevelParameteriv(texture, level, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetTextureLevelParameteriv(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static int glGetTextureLevelParameteri(final int texture, final int level, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureLevelParameteriv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetTextureLevelParameteriv(texture, level, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetTextureParameter(final int texture, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureParameterfv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetTextureParameterfv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetTextureParameterfv(final int p0, final int p1, final long p2, final long p3);
    
    public static float glGetTextureParameterf(final int texture, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureParameterfv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final FloatBuffer params = APIUtil.getBufferFloat(caps);
        nglGetTextureParameterfv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetTextureParameterI(final int texture, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureParameterIiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetTextureParameterIiv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetTextureParameterIiv(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetTextureParameterIi(final int texture, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureParameterIiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetTextureParameterIiv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetTextureParameterIu(final int texture, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureParameterIuiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetTextureParameterIuiv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetTextureParameterIuiv(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetTextureParameterIui(final int texture, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureParameterIuiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetTextureParameterIuiv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetTextureParameter(final int texture, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureParameteriv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetTextureParameteriv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetTextureParameteriv(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetTextureParameteri(final int texture, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureParameteriv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetTextureParameteriv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glCreateVertexArrays(final IntBuffer arrays) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCreateVertexArrays;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(arrays);
        nglCreateVertexArrays(arrays.remaining(), MemoryUtil.getAddress(arrays), function_pointer);
    }
    
    static native void nglCreateVertexArrays(final int p0, final long p1, final long p2);
    
    public static int glCreateVertexArrays() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCreateVertexArrays;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer arrays = APIUtil.getBufferInt(caps);
        nglCreateVertexArrays(1, MemoryUtil.getAddress(arrays), function_pointer);
        return arrays.get(0);
    }
    
    public static void glDisableVertexArrayAttrib(final int vaobj, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDisableVertexArrayAttrib;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDisableVertexArrayAttrib(vaobj, index, function_pointer);
    }
    
    static native void nglDisableVertexArrayAttrib(final int p0, final int p1, final long p2);
    
    public static void glEnableVertexArrayAttrib(final int vaobj, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glEnableVertexArrayAttrib;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglEnableVertexArrayAttrib(vaobj, index, function_pointer);
    }
    
    static native void nglEnableVertexArrayAttrib(final int p0, final int p1, final long p2);
    
    public static void glVertexArrayElementBuffer(final int vaobj, final int buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexArrayElementBuffer;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexArrayElementBuffer(vaobj, buffer, function_pointer);
    }
    
    static native void nglVertexArrayElementBuffer(final int p0, final int p1, final long p2);
    
    public static void glVertexArrayVertexBuffer(final int vaobj, final int bindingindex, final int buffer, final long offset, final int stride) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexArrayVertexBuffer;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexArrayVertexBuffer(vaobj, bindingindex, buffer, offset, stride, function_pointer);
    }
    
    static native void nglVertexArrayVertexBuffer(final int p0, final int p1, final int p2, final long p3, final int p4, final long p5);
    
    public static void glVertexArrayVertexBuffers(final int vaobj, final int first, final int count, final IntBuffer buffers, final PointerBuffer offsets, final IntBuffer strides) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexArrayVertexBuffers;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (buffers != null) {
            BufferChecks.checkBuffer(buffers, count);
        }
        if (offsets != null) {
            BufferChecks.checkBuffer(offsets, count);
        }
        if (strides != null) {
            BufferChecks.checkBuffer(strides, count);
        }
        nglVertexArrayVertexBuffers(vaobj, first, count, MemoryUtil.getAddressSafe(buffers), MemoryUtil.getAddressSafe(offsets), MemoryUtil.getAddressSafe(strides), function_pointer);
    }
    
    static native void nglVertexArrayVertexBuffers(final int p0, final int p1, final int p2, final long p3, final long p4, final long p5, final long p6);
    
    public static void glVertexArrayAttribFormat(final int vaobj, final int attribindex, final int size, final int type, final boolean normalized, final int relativeoffset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexArrayAttribFormat;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexArrayAttribFormat(vaobj, attribindex, size, type, normalized, relativeoffset, function_pointer);
    }
    
    static native void nglVertexArrayAttribFormat(final int p0, final int p1, final int p2, final int p3, final boolean p4, final int p5, final long p6);
    
    public static void glVertexArrayAttribIFormat(final int vaobj, final int attribindex, final int size, final int type, final int relativeoffset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexArrayAttribIFormat;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexArrayAttribIFormat(vaobj, attribindex, size, type, relativeoffset, function_pointer);
    }
    
    static native void nglVertexArrayAttribIFormat(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glVertexArrayAttribLFormat(final int vaobj, final int attribindex, final int size, final int type, final int relativeoffset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexArrayAttribLFormat;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexArrayAttribLFormat(vaobj, attribindex, size, type, relativeoffset, function_pointer);
    }
    
    static native void nglVertexArrayAttribLFormat(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glVertexArrayAttribBinding(final int vaobj, final int attribindex, final int bindingindex) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexArrayAttribBinding;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexArrayAttribBinding(vaobj, attribindex, bindingindex, function_pointer);
    }
    
    static native void nglVertexArrayAttribBinding(final int p0, final int p1, final int p2, final long p3);
    
    public static void glVertexArrayBindingDivisor(final int vaobj, final int bindingindex, final int divisor) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexArrayBindingDivisor;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexArrayBindingDivisor(vaobj, bindingindex, divisor, function_pointer);
    }
    
    static native void nglVertexArrayBindingDivisor(final int p0, final int p1, final int p2, final long p3);
    
    public static void glGetVertexArray(final int vaobj, final int pname, final IntBuffer param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexArrayiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(param, 1);
        nglGetVertexArrayiv(vaobj, pname, MemoryUtil.getAddress(param), function_pointer);
    }
    
    static native void nglGetVertexArrayiv(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetVertexArray(final int vaobj, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexArrayiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer param = APIUtil.getBufferInt(caps);
        nglGetVertexArrayiv(vaobj, pname, MemoryUtil.getAddress(param), function_pointer);
        return param.get(0);
    }
    
    public static void glGetVertexArrayIndexed(final int vaobj, final int index, final int pname, final IntBuffer param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexArrayIndexediv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(param, 1);
        nglGetVertexArrayIndexediv(vaobj, index, pname, MemoryUtil.getAddress(param), function_pointer);
    }
    
    static native void nglGetVertexArrayIndexediv(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static int glGetVertexArrayIndexed(final int vaobj, final int index, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexArrayIndexediv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer param = APIUtil.getBufferInt(caps);
        nglGetVertexArrayIndexediv(vaobj, index, pname, MemoryUtil.getAddress(param), function_pointer);
        return param.get(0);
    }
    
    public static void glGetVertexArrayIndexed64i(final int vaobj, final int index, final int pname, final LongBuffer param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexArrayIndexed64iv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(param, 1);
        nglGetVertexArrayIndexed64iv(vaobj, index, pname, MemoryUtil.getAddress(param), function_pointer);
    }
    
    static native void nglGetVertexArrayIndexed64iv(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static long glGetVertexArrayIndexed64i(final int vaobj, final int index, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexArrayIndexed64iv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final LongBuffer param = APIUtil.getBufferLong(caps);
        nglGetVertexArrayIndexed64iv(vaobj, index, pname, MemoryUtil.getAddress(param), function_pointer);
        return param.get(0);
    }
    
    public static void glCreateSamplers(final IntBuffer samplers) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCreateSamplers;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(samplers);
        nglCreateSamplers(samplers.remaining(), MemoryUtil.getAddress(samplers), function_pointer);
    }
    
    static native void nglCreateSamplers(final int p0, final long p1, final long p2);
    
    public static int glCreateSamplers() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCreateSamplers;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer samplers = APIUtil.getBufferInt(caps);
        nglCreateSamplers(1, MemoryUtil.getAddress(samplers), function_pointer);
        return samplers.get(0);
    }
    
    public static void glCreateProgramPipelines(final IntBuffer pipelines) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCreateProgramPipelines;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pipelines);
        nglCreateProgramPipelines(pipelines.remaining(), MemoryUtil.getAddress(pipelines), function_pointer);
    }
    
    static native void nglCreateProgramPipelines(final int p0, final long p1, final long p2);
    
    public static int glCreateProgramPipelines() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCreateProgramPipelines;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer pipelines = APIUtil.getBufferInt(caps);
        nglCreateProgramPipelines(1, MemoryUtil.getAddress(pipelines), function_pointer);
        return pipelines.get(0);
    }
    
    public static void glCreateQueries(final int target, final IntBuffer ids) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCreateQueries;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(ids);
        nglCreateQueries(target, ids.remaining(), MemoryUtil.getAddress(ids), function_pointer);
    }
    
    static native void nglCreateQueries(final int p0, final int p1, final long p2, final long p3);
    
    public static int glCreateQueries(final int target) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCreateQueries;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer ids = APIUtil.getBufferInt(caps);
        nglCreateQueries(target, 1, MemoryUtil.getAddress(ids), function_pointer);
        return ids.get(0);
    }
    
    public static void glMemoryBarrierByRegion(final int barriers) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMemoryBarrierByRegion;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMemoryBarrierByRegion(barriers, function_pointer);
    }
    
    static native void nglMemoryBarrierByRegion(final int p0, final long p1);
    
    public static void glGetTextureSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final ByteBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureSubImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(pixels);
        nglGetTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels.remaining(), MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glGetTextureSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final DoubleBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureSubImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(pixels);
        nglGetTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels.remaining() << 3, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glGetTextureSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final FloatBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureSubImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(pixels);
        nglGetTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels.remaining() << 2, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glGetTextureSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final IntBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureSubImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(pixels);
        nglGetTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels.remaining() << 2, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glGetTextureSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final ShortBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureSubImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(pixels);
        nglGetTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels.remaining() << 1, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    static native void nglGetTextureSubImage(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final int p10, final long p11, final long p12);
    
    public static void glGetTextureSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final int pixels_bufSize, final long pixels_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureSubImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOenabled(caps);
        nglGetTextureSubImageBO(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels_bufSize, pixels_buffer_offset, function_pointer);
    }
    
    static native void nglGetTextureSubImageBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final int p10, final long p11, final long p12);
    
    public static void glGetCompressedTextureSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final ByteBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetCompressedTextureSubImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(pixels);
        nglGetCompressedTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, pixels.remaining(), MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glGetCompressedTextureSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final DoubleBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetCompressedTextureSubImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(pixels);
        nglGetCompressedTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, pixels.remaining() << 3, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glGetCompressedTextureSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final FloatBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetCompressedTextureSubImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(pixels);
        nglGetCompressedTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, pixels.remaining() << 2, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glGetCompressedTextureSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final IntBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetCompressedTextureSubImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(pixels);
        nglGetCompressedTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, pixels.remaining() << 2, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glGetCompressedTextureSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final ShortBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetCompressedTextureSubImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(pixels);
        nglGetCompressedTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, pixels.remaining() << 1, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    static native void nglGetCompressedTextureSubImage(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final long p9, final long p10);
    
    public static void glGetCompressedTextureSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int pixels_bufSize, final long pixels_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetCompressedTextureSubImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOenabled(caps);
        nglGetCompressedTextureSubImageBO(texture, level, xoffset, yoffset, zoffset, width, height, depth, pixels_bufSize, pixels_buffer_offset, function_pointer);
    }
    
    static native void nglGetCompressedTextureSubImageBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final long p9, final long p10);
    
    public static void glTextureBarrier() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureBarrier;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTextureBarrier(function_pointer);
    }
    
    static native void nglTextureBarrier(final long p0);
    
    public static int glGetGraphicsResetStatus() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetGraphicsResetStatus;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nglGetGraphicsResetStatus(function_pointer);
        return __result;
    }
    
    static native int nglGetGraphicsResetStatus(final long p0);
    
    public static void glReadnPixels(final int x, final int y, final int width, final int height, final int format, final int type, final ByteBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glReadnPixels;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(pixels);
        nglReadnPixels(x, y, width, height, format, type, pixels.remaining(), MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glReadnPixels(final int x, final int y, final int width, final int height, final int format, final int type, final DoubleBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glReadnPixels;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(pixels);
        nglReadnPixels(x, y, width, height, format, type, pixels.remaining() << 3, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glReadnPixels(final int x, final int y, final int width, final int height, final int format, final int type, final FloatBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glReadnPixels;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(pixels);
        nglReadnPixels(x, y, width, height, format, type, pixels.remaining() << 2, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glReadnPixels(final int x, final int y, final int width, final int height, final int format, final int type, final IntBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glReadnPixels;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(pixels);
        nglReadnPixels(x, y, width, height, format, type, pixels.remaining() << 2, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glReadnPixels(final int x, final int y, final int width, final int height, final int format, final int type, final ShortBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glReadnPixels;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(pixels);
        nglReadnPixels(x, y, width, height, format, type, pixels.remaining() << 1, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    static native void nglReadnPixels(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final long p7, final long p8);
    
    public static void glReadnPixels(final int x, final int y, final int width, final int height, final int format, final int type, final int pixels_bufSize, final long pixels_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glReadnPixels;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOenabled(caps);
        nglReadnPixelsBO(x, y, width, height, format, type, pixels_bufSize, pixels_buffer_offset, function_pointer);
    }
    
    static native void nglReadnPixelsBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final long p7, final long p8);
    
    public static void glGetnUniform(final int program, final int location, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnUniformfv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglGetnUniformfv(program, location, params.remaining(), MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetnUniformfv(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetnUniform(final int program, final int location, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnUniformiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglGetnUniformiv(program, location, params.remaining(), MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetnUniformiv(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetnUniformu(final int program, final int location, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnUniformuiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglGetnUniformuiv(program, location, params.remaining(), MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetnUniformuiv(final int p0, final int p1, final int p2, final long p3, final long p4);
}

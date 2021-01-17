// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ByteOrder;
import org.lwjgl.LWJGLUtil;
import java.nio.ShortBuffer;
import java.nio.FloatBuffer;
import java.nio.DoubleBuffer;
import java.nio.ByteBuffer;
import org.lwjgl.MemoryUtil;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;

public class ARBBufferObject
{
    public static final int GL_STREAM_DRAW_ARB = 35040;
    public static final int GL_STREAM_READ_ARB = 35041;
    public static final int GL_STREAM_COPY_ARB = 35042;
    public static final int GL_STATIC_DRAW_ARB = 35044;
    public static final int GL_STATIC_READ_ARB = 35045;
    public static final int GL_STATIC_COPY_ARB = 35046;
    public static final int GL_DYNAMIC_DRAW_ARB = 35048;
    public static final int GL_DYNAMIC_READ_ARB = 35049;
    public static final int GL_DYNAMIC_COPY_ARB = 35050;
    public static final int GL_READ_ONLY_ARB = 35000;
    public static final int GL_WRITE_ONLY_ARB = 35001;
    public static final int GL_READ_WRITE_ARB = 35002;
    public static final int GL_BUFFER_SIZE_ARB = 34660;
    public static final int GL_BUFFER_USAGE_ARB = 34661;
    public static final int GL_BUFFER_ACCESS_ARB = 35003;
    public static final int GL_BUFFER_MAPPED_ARB = 35004;
    public static final int GL_BUFFER_MAP_POINTER_ARB = 35005;
    
    public static void glBindBufferARB(final int target, final int buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindBufferARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        StateTracker.bindBuffer(caps, target, buffer);
        nglBindBufferARB(target, buffer, function_pointer);
    }
    
    static native void nglBindBufferARB(final int p0, final int p1, final long p2);
    
    public static void glDeleteBuffersARB(final IntBuffer buffers) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteBuffersARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(buffers);
        nglDeleteBuffersARB(buffers.remaining(), MemoryUtil.getAddress(buffers), function_pointer);
    }
    
    static native void nglDeleteBuffersARB(final int p0, final long p1, final long p2);
    
    public static void glDeleteBuffersARB(final int buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteBuffersARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDeleteBuffersARB(1, APIUtil.getInt(caps, buffer), function_pointer);
    }
    
    public static void glGenBuffersARB(final IntBuffer buffers) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenBuffersARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(buffers);
        nglGenBuffersARB(buffers.remaining(), MemoryUtil.getAddress(buffers), function_pointer);
    }
    
    static native void nglGenBuffersARB(final int p0, final long p1, final long p2);
    
    public static int glGenBuffersARB() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenBuffersARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer buffers = APIUtil.getBufferInt(caps);
        nglGenBuffersARB(1, MemoryUtil.getAddress(buffers), function_pointer);
        return buffers.get(0);
    }
    
    public static boolean glIsBufferARB(final int buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsBufferARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsBufferARB(buffer, function_pointer);
        return __result;
    }
    
    static native boolean nglIsBufferARB(final int p0, final long p1);
    
    public static void glBufferDataARB(final int target, final long data_size, final int usage) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferDataARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBufferDataARB(target, data_size, 0L, usage, function_pointer);
    }
    
    public static void glBufferDataARB(final int target, final ByteBuffer data, final int usage) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferDataARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglBufferDataARB(target, data.remaining(), MemoryUtil.getAddress(data), usage, function_pointer);
    }
    
    public static void glBufferDataARB(final int target, final DoubleBuffer data, final int usage) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferDataARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglBufferDataARB(target, data.remaining() << 3, MemoryUtil.getAddress(data), usage, function_pointer);
    }
    
    public static void glBufferDataARB(final int target, final FloatBuffer data, final int usage) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferDataARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglBufferDataARB(target, data.remaining() << 2, MemoryUtil.getAddress(data), usage, function_pointer);
    }
    
    public static void glBufferDataARB(final int target, final IntBuffer data, final int usage) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferDataARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglBufferDataARB(target, data.remaining() << 2, MemoryUtil.getAddress(data), usage, function_pointer);
    }
    
    public static void glBufferDataARB(final int target, final ShortBuffer data, final int usage) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferDataARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglBufferDataARB(target, data.remaining() << 1, MemoryUtil.getAddress(data), usage, function_pointer);
    }
    
    static native void nglBufferDataARB(final int p0, final long p1, final long p2, final int p3, final long p4);
    
    public static void glBufferSubDataARB(final int target, final long offset, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferSubDataARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglBufferSubDataARB(target, offset, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glBufferSubDataARB(final int target, final long offset, final DoubleBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferSubDataARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglBufferSubDataARB(target, offset, data.remaining() << 3, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glBufferSubDataARB(final int target, final long offset, final FloatBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferSubDataARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglBufferSubDataARB(target, offset, data.remaining() << 2, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glBufferSubDataARB(final int target, final long offset, final IntBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferSubDataARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglBufferSubDataARB(target, offset, data.remaining() << 2, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glBufferSubDataARB(final int target, final long offset, final ShortBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferSubDataARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglBufferSubDataARB(target, offset, data.remaining() << 1, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglBufferSubDataARB(final int p0, final long p1, final long p2, final long p3, final long p4);
    
    public static void glGetBufferSubDataARB(final int target, final long offset, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetBufferSubDataARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglGetBufferSubDataARB(target, offset, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glGetBufferSubDataARB(final int target, final long offset, final DoubleBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetBufferSubDataARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglGetBufferSubDataARB(target, offset, data.remaining() << 3, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glGetBufferSubDataARB(final int target, final long offset, final FloatBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetBufferSubDataARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglGetBufferSubDataARB(target, offset, data.remaining() << 2, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glGetBufferSubDataARB(final int target, final long offset, final IntBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetBufferSubDataARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglGetBufferSubDataARB(target, offset, data.remaining() << 2, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glGetBufferSubDataARB(final int target, final long offset, final ShortBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetBufferSubDataARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglGetBufferSubDataARB(target, offset, data.remaining() << 1, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglGetBufferSubDataARB(final int p0, final long p1, final long p2, final long p3, final long p4);
    
    public static ByteBuffer glMapBufferARB(final int target, final int access, final ByteBuffer old_buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMapBufferARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (old_buffer != null) {
            BufferChecks.checkDirect(old_buffer);
        }
        final ByteBuffer __result = nglMapBufferARB(target, access, glGetBufferParameteriARB(target, 34660), old_buffer, function_pointer);
        return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
    }
    
    public static ByteBuffer glMapBufferARB(final int target, final int access, final long length, final ByteBuffer old_buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMapBufferARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (old_buffer != null) {
            BufferChecks.checkDirect(old_buffer);
        }
        final ByteBuffer __result = nglMapBufferARB(target, access, length, old_buffer, function_pointer);
        return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
    }
    
    static native ByteBuffer nglMapBufferARB(final int p0, final int p1, final long p2, final ByteBuffer p3, final long p4);
    
    public static boolean glUnmapBufferARB(final int target) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUnmapBufferARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglUnmapBufferARB(target, function_pointer);
        return __result;
    }
    
    static native boolean nglUnmapBufferARB(final int p0, final long p1);
    
    public static void glGetBufferParameterARB(final int target, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetBufferParameterivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetBufferParameterivARB(target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetBufferParameterivARB(final int p0, final int p1, final long p2, final long p3);
    
    @Deprecated
    public static int glGetBufferParameterARB(final int target, final int pname) {
        return glGetBufferParameteriARB(target, pname);
    }
    
    public static int glGetBufferParameteriARB(final int target, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetBufferParameterivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetBufferParameterivARB(target, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static ByteBuffer glGetBufferPointerARB(final int target, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetBufferPointervARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final ByteBuffer __result = nglGetBufferPointervARB(target, pname, glGetBufferParameteriARB(target, 34660), function_pointer);
        return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
    }
    
    static native ByteBuffer nglGetBufferPointervARB(final int p0, final int p1, final long p2, final long p3);
}

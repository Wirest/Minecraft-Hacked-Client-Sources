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

public final class GL15
{
    public static final int GL_ARRAY_BUFFER = 34962;
    public static final int GL_ELEMENT_ARRAY_BUFFER = 34963;
    public static final int GL_ARRAY_BUFFER_BINDING = 34964;
    public static final int GL_ELEMENT_ARRAY_BUFFER_BINDING = 34965;
    public static final int GL_VERTEX_ARRAY_BUFFER_BINDING = 34966;
    public static final int GL_NORMAL_ARRAY_BUFFER_BINDING = 34967;
    public static final int GL_COLOR_ARRAY_BUFFER_BINDING = 34968;
    public static final int GL_INDEX_ARRAY_BUFFER_BINDING = 34969;
    public static final int GL_TEXTURE_COORD_ARRAY_BUFFER_BINDING = 34970;
    public static final int GL_EDGE_FLAG_ARRAY_BUFFER_BINDING = 34971;
    public static final int GL_SECONDARY_COLOR_ARRAY_BUFFER_BINDING = 34972;
    public static final int GL_FOG_COORDINATE_ARRAY_BUFFER_BINDING = 34973;
    public static final int GL_WEIGHT_ARRAY_BUFFER_BINDING = 34974;
    public static final int GL_VERTEX_ATTRIB_ARRAY_BUFFER_BINDING = 34975;
    public static final int GL_STREAM_DRAW = 35040;
    public static final int GL_STREAM_READ = 35041;
    public static final int GL_STREAM_COPY = 35042;
    public static final int GL_STATIC_DRAW = 35044;
    public static final int GL_STATIC_READ = 35045;
    public static final int GL_STATIC_COPY = 35046;
    public static final int GL_DYNAMIC_DRAW = 35048;
    public static final int GL_DYNAMIC_READ = 35049;
    public static final int GL_DYNAMIC_COPY = 35050;
    public static final int GL_READ_ONLY = 35000;
    public static final int GL_WRITE_ONLY = 35001;
    public static final int GL_READ_WRITE = 35002;
    public static final int GL_BUFFER_SIZE = 34660;
    public static final int GL_BUFFER_USAGE = 34661;
    public static final int GL_BUFFER_ACCESS = 35003;
    public static final int GL_BUFFER_MAPPED = 35004;
    public static final int GL_BUFFER_MAP_POINTER = 35005;
    public static final int GL_FOG_COORD_SRC = 33872;
    public static final int GL_FOG_COORD = 33873;
    public static final int GL_CURRENT_FOG_COORD = 33875;
    public static final int GL_FOG_COORD_ARRAY_TYPE = 33876;
    public static final int GL_FOG_COORD_ARRAY_STRIDE = 33877;
    public static final int GL_FOG_COORD_ARRAY_POINTER = 33878;
    public static final int GL_FOG_COORD_ARRAY = 33879;
    public static final int GL_FOG_COORD_ARRAY_BUFFER_BINDING = 34973;
    public static final int GL_SRC0_RGB = 34176;
    public static final int GL_SRC1_RGB = 34177;
    public static final int GL_SRC2_RGB = 34178;
    public static final int GL_SRC0_ALPHA = 34184;
    public static final int GL_SRC1_ALPHA = 34185;
    public static final int GL_SRC2_ALPHA = 34186;
    public static final int GL_SAMPLES_PASSED = 35092;
    public static final int GL_QUERY_COUNTER_BITS = 34916;
    public static final int GL_CURRENT_QUERY = 34917;
    public static final int GL_QUERY_RESULT = 34918;
    public static final int GL_QUERY_RESULT_AVAILABLE = 34919;
    
    private GL15() {
    }
    
    public static void glBindBuffer(final int target, final int buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindBuffer;
        BufferChecks.checkFunctionAddress(function_pointer);
        StateTracker.bindBuffer(caps, target, buffer);
        nglBindBuffer(target, buffer, function_pointer);
    }
    
    static native void nglBindBuffer(final int p0, final int p1, final long p2);
    
    public static void glDeleteBuffers(final IntBuffer buffers) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteBuffers;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(buffers);
        nglDeleteBuffers(buffers.remaining(), MemoryUtil.getAddress(buffers), function_pointer);
    }
    
    static native void nglDeleteBuffers(final int p0, final long p1, final long p2);
    
    public static void glDeleteBuffers(final int buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteBuffers;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDeleteBuffers(1, APIUtil.getInt(caps, buffer), function_pointer);
    }
    
    public static void glGenBuffers(final IntBuffer buffers) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenBuffers;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(buffers);
        nglGenBuffers(buffers.remaining(), MemoryUtil.getAddress(buffers), function_pointer);
    }
    
    static native void nglGenBuffers(final int p0, final long p1, final long p2);
    
    public static int glGenBuffers() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenBuffers;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer buffers = APIUtil.getBufferInt(caps);
        nglGenBuffers(1, MemoryUtil.getAddress(buffers), function_pointer);
        return buffers.get(0);
    }
    
    public static boolean glIsBuffer(final int buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsBuffer;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsBuffer(buffer, function_pointer);
        return __result;
    }
    
    static native boolean nglIsBuffer(final int p0, final long p1);
    
    public static void glBufferData(final int target, final long data_size, final int usage) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferData;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBufferData(target, data_size, 0L, usage, function_pointer);
    }
    
    public static void glBufferData(final int target, final ByteBuffer data, final int usage) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglBufferData(target, data.remaining(), MemoryUtil.getAddress(data), usage, function_pointer);
    }
    
    public static void glBufferData(final int target, final DoubleBuffer data, final int usage) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglBufferData(target, data.remaining() << 3, MemoryUtil.getAddress(data), usage, function_pointer);
    }
    
    public static void glBufferData(final int target, final FloatBuffer data, final int usage) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglBufferData(target, data.remaining() << 2, MemoryUtil.getAddress(data), usage, function_pointer);
    }
    
    public static void glBufferData(final int target, final IntBuffer data, final int usage) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglBufferData(target, data.remaining() << 2, MemoryUtil.getAddress(data), usage, function_pointer);
    }
    
    public static void glBufferData(final int target, final ShortBuffer data, final int usage) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglBufferData(target, data.remaining() << 1, MemoryUtil.getAddress(data), usage, function_pointer);
    }
    
    static native void nglBufferData(final int p0, final long p1, final long p2, final int p3, final long p4);
    
    public static void glBufferSubData(final int target, final long offset, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferSubData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglBufferSubData(target, offset, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glBufferSubData(final int target, final long offset, final DoubleBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferSubData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglBufferSubData(target, offset, data.remaining() << 3, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glBufferSubData(final int target, final long offset, final FloatBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferSubData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglBufferSubData(target, offset, data.remaining() << 2, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glBufferSubData(final int target, final long offset, final IntBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferSubData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglBufferSubData(target, offset, data.remaining() << 2, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glBufferSubData(final int target, final long offset, final ShortBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferSubData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglBufferSubData(target, offset, data.remaining() << 1, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglBufferSubData(final int p0, final long p1, final long p2, final long p3, final long p4);
    
    public static void glGetBufferSubData(final int target, final long offset, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetBufferSubData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglGetBufferSubData(target, offset, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glGetBufferSubData(final int target, final long offset, final DoubleBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetBufferSubData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglGetBufferSubData(target, offset, data.remaining() << 3, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glGetBufferSubData(final int target, final long offset, final FloatBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetBufferSubData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglGetBufferSubData(target, offset, data.remaining() << 2, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glGetBufferSubData(final int target, final long offset, final IntBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetBufferSubData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglGetBufferSubData(target, offset, data.remaining() << 2, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glGetBufferSubData(final int target, final long offset, final ShortBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetBufferSubData;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglGetBufferSubData(target, offset, data.remaining() << 1, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglGetBufferSubData(final int p0, final long p1, final long p2, final long p3, final long p4);
    
    public static ByteBuffer glMapBuffer(final int target, final int access, final ByteBuffer old_buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMapBuffer;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (old_buffer != null) {
            BufferChecks.checkDirect(old_buffer);
        }
        final ByteBuffer __result = nglMapBuffer(target, access, glGetBufferParameteri(target, 34660), old_buffer, function_pointer);
        return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
    }
    
    public static ByteBuffer glMapBuffer(final int target, final int access, final long length, final ByteBuffer old_buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMapBuffer;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (old_buffer != null) {
            BufferChecks.checkDirect(old_buffer);
        }
        final ByteBuffer __result = nglMapBuffer(target, access, length, old_buffer, function_pointer);
        return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
    }
    
    static native ByteBuffer nglMapBuffer(final int p0, final int p1, final long p2, final ByteBuffer p3, final long p4);
    
    public static boolean glUnmapBuffer(final int target) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUnmapBuffer;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglUnmapBuffer(target, function_pointer);
        return __result;
    }
    
    static native boolean nglUnmapBuffer(final int p0, final long p1);
    
    public static void glGetBufferParameter(final int target, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetBufferParameteriv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetBufferParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetBufferParameteriv(final int p0, final int p1, final long p2, final long p3);
    
    @Deprecated
    public static int glGetBufferParameter(final int target, final int pname) {
        return glGetBufferParameteri(target, pname);
    }
    
    public static int glGetBufferParameteri(final int target, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetBufferParameteriv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetBufferParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static ByteBuffer glGetBufferPointer(final int target, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetBufferPointerv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final ByteBuffer __result = nglGetBufferPointerv(target, pname, glGetBufferParameteri(target, 34660), function_pointer);
        return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
    }
    
    static native ByteBuffer nglGetBufferPointerv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGenQueries(final IntBuffer ids) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenQueries;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(ids);
        nglGenQueries(ids.remaining(), MemoryUtil.getAddress(ids), function_pointer);
    }
    
    static native void nglGenQueries(final int p0, final long p1, final long p2);
    
    public static int glGenQueries() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenQueries;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer ids = APIUtil.getBufferInt(caps);
        nglGenQueries(1, MemoryUtil.getAddress(ids), function_pointer);
        return ids.get(0);
    }
    
    public static void glDeleteQueries(final IntBuffer ids) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteQueries;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(ids);
        nglDeleteQueries(ids.remaining(), MemoryUtil.getAddress(ids), function_pointer);
    }
    
    static native void nglDeleteQueries(final int p0, final long p1, final long p2);
    
    public static void glDeleteQueries(final int id) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteQueries;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDeleteQueries(1, APIUtil.getInt(caps, id), function_pointer);
    }
    
    public static boolean glIsQuery(final int id) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsQuery;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsQuery(id, function_pointer);
        return __result;
    }
    
    static native boolean nglIsQuery(final int p0, final long p1);
    
    public static void glBeginQuery(final int target, final int id) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBeginQuery;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBeginQuery(target, id, function_pointer);
    }
    
    static native void nglBeginQuery(final int p0, final int p1, final long p2);
    
    public static void glEndQuery(final int target) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glEndQuery;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglEndQuery(target, function_pointer);
    }
    
    static native void nglEndQuery(final int p0, final long p1);
    
    public static void glGetQuery(final int target, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetQueryiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetQueryiv(target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetQueryiv(final int p0, final int p1, final long p2, final long p3);
    
    @Deprecated
    public static int glGetQuery(final int target, final int pname) {
        return glGetQueryi(target, pname);
    }
    
    public static int glGetQueryi(final int target, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetQueryiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetQueryiv(target, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetQueryObject(final int id, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetQueryObjectiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetQueryObjectiv(id, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetQueryObjectiv(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetQueryObjecti(final int id, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetQueryObjectiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetQueryObjectiv(id, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetQueryObjectu(final int id, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetQueryObjectuiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetQueryObjectuiv(id, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetQueryObjectuiv(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetQueryObjectui(final int id, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetQueryObjectuiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetQueryObjectuiv(id, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
}

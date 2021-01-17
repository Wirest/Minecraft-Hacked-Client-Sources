// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.PointerBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import java.nio.DoubleBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.ByteBuffer;

public final class GL44
{
    public static final int GL_MAX_VERTEX_ATTRIB_STRIDE = 33509;
    public static final int GL_MAP_PERSISTENT_BIT = 64;
    public static final int GL_MAP_COHERENT_BIT = 128;
    public static final int GL_DYNAMIC_STORAGE_BIT = 256;
    public static final int GL_CLIENT_STORAGE_BIT = 512;
    public static final int GL_BUFFER_IMMUTABLE_STORAGE = 33311;
    public static final int GL_BUFFER_STORAGE_FLAGS = 33312;
    public static final int GL_CLIENT_MAPPED_BUFFER_BARRIER_BIT = 16384;
    public static final int GL_CLEAR_TEXTURE = 37733;
    public static final int GL_LOCATION_COMPONENT = 37706;
    public static final int GL_TRANSFORM_FEEDBACK_BUFFER_INDEX = 37707;
    public static final int GL_TRANSFORM_FEEDBACK_BUFFER_STRIDE = 37708;
    public static final int GL_QUERY_RESULT_NO_WAIT = 37268;
    public static final int GL_QUERY_BUFFER = 37266;
    public static final int GL_QUERY_BUFFER_BINDING = 37267;
    public static final int GL_QUERY_BUFFER_BARRIER_BIT = 32768;
    public static final int GL_MIRROR_CLAMP_TO_EDGE = 34627;
    
    private GL44() {
    }
    
    public static void glBufferStorage(final int target, final ByteBuffer data, final int flags) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferStorage;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglBufferStorage(target, data.remaining(), MemoryUtil.getAddress(data), flags, function_pointer);
    }
    
    public static void glBufferStorage(final int target, final DoubleBuffer data, final int flags) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferStorage;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglBufferStorage(target, data.remaining() << 3, MemoryUtil.getAddress(data), flags, function_pointer);
    }
    
    public static void glBufferStorage(final int target, final FloatBuffer data, final int flags) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferStorage;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglBufferStorage(target, data.remaining() << 2, MemoryUtil.getAddress(data), flags, function_pointer);
    }
    
    public static void glBufferStorage(final int target, final IntBuffer data, final int flags) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferStorage;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglBufferStorage(target, data.remaining() << 2, MemoryUtil.getAddress(data), flags, function_pointer);
    }
    
    public static void glBufferStorage(final int target, final ShortBuffer data, final int flags) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferStorage;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglBufferStorage(target, data.remaining() << 1, MemoryUtil.getAddress(data), flags, function_pointer);
    }
    
    public static void glBufferStorage(final int target, final LongBuffer data, final int flags) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferStorage;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglBufferStorage(target, data.remaining() << 3, MemoryUtil.getAddress(data), flags, function_pointer);
    }
    
    static native void nglBufferStorage(final int p0, final long p1, final long p2, final int p3, final long p4);
    
    public static void glBufferStorage(final int target, final long size, final int flags) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferStorage;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBufferStorage(target, size, 0L, flags, function_pointer);
    }
    
    public static void glClearTexImage(final int texture, final int level, final int format, final int type, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glClearTexImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (data != null) {
            BufferChecks.checkBuffer(data, 1);
        }
        nglClearTexImage(texture, level, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
    }
    
    public static void glClearTexImage(final int texture, final int level, final int format, final int type, final DoubleBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glClearTexImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (data != null) {
            BufferChecks.checkBuffer(data, 1);
        }
        nglClearTexImage(texture, level, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
    }
    
    public static void glClearTexImage(final int texture, final int level, final int format, final int type, final FloatBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glClearTexImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (data != null) {
            BufferChecks.checkBuffer(data, 1);
        }
        nglClearTexImage(texture, level, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
    }
    
    public static void glClearTexImage(final int texture, final int level, final int format, final int type, final IntBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glClearTexImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (data != null) {
            BufferChecks.checkBuffer(data, 1);
        }
        nglClearTexImage(texture, level, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
    }
    
    public static void glClearTexImage(final int texture, final int level, final int format, final int type, final ShortBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glClearTexImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (data != null) {
            BufferChecks.checkBuffer(data, 1);
        }
        nglClearTexImage(texture, level, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
    }
    
    public static void glClearTexImage(final int texture, final int level, final int format, final int type, final LongBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glClearTexImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (data != null) {
            BufferChecks.checkBuffer(data, 1);
        }
        nglClearTexImage(texture, level, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
    }
    
    static native void nglClearTexImage(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glClearTexSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glClearTexSubImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (data != null) {
            BufferChecks.checkBuffer(data, 1);
        }
        nglClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
    }
    
    public static void glClearTexSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final DoubleBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glClearTexSubImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (data != null) {
            BufferChecks.checkBuffer(data, 1);
        }
        nglClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
    }
    
    public static void glClearTexSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final FloatBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glClearTexSubImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (data != null) {
            BufferChecks.checkBuffer(data, 1);
        }
        nglClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
    }
    
    public static void glClearTexSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final IntBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glClearTexSubImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (data != null) {
            BufferChecks.checkBuffer(data, 1);
        }
        nglClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
    }
    
    public static void glClearTexSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final ShortBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glClearTexSubImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (data != null) {
            BufferChecks.checkBuffer(data, 1);
        }
        nglClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
    }
    
    public static void glClearTexSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final LongBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glClearTexSubImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (data != null) {
            BufferChecks.checkBuffer(data, 1);
        }
        nglClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
    }
    
    static native void nglClearTexSubImage(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final long p10, final long p11);
    
    public static void glBindBuffersBase(final int target, final int first, final int count, final IntBuffer buffers) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindBuffersBase;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (buffers != null) {
            BufferChecks.checkBuffer(buffers, count);
        }
        nglBindBuffersBase(target, first, count, MemoryUtil.getAddressSafe(buffers), function_pointer);
    }
    
    static native void nglBindBuffersBase(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glBindBuffersRange(final int target, final int first, final int count, final IntBuffer buffers, final PointerBuffer offsets, final PointerBuffer sizes) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindBuffersRange;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (buffers != null) {
            BufferChecks.checkBuffer(buffers, count);
        }
        if (offsets != null) {
            BufferChecks.checkBuffer(offsets, count);
        }
        if (sizes != null) {
            BufferChecks.checkBuffer(sizes, count);
        }
        nglBindBuffersRange(target, first, count, MemoryUtil.getAddressSafe(buffers), MemoryUtil.getAddressSafe(offsets), MemoryUtil.getAddressSafe(sizes), function_pointer);
    }
    
    static native void nglBindBuffersRange(final int p0, final int p1, final int p2, final long p3, final long p4, final long p5, final long p6);
    
    public static void glBindTextures(final int first, final int count, final IntBuffer textures) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindTextures;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (textures != null) {
            BufferChecks.checkBuffer(textures, count);
        }
        nglBindTextures(first, count, MemoryUtil.getAddressSafe(textures), function_pointer);
    }
    
    static native void nglBindTextures(final int p0, final int p1, final long p2, final long p3);
    
    public static void glBindSamplers(final int first, final int count, final IntBuffer samplers) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindSamplers;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (samplers != null) {
            BufferChecks.checkBuffer(samplers, count);
        }
        nglBindSamplers(first, count, MemoryUtil.getAddressSafe(samplers), function_pointer);
    }
    
    static native void nglBindSamplers(final int p0, final int p1, final long p2, final long p3);
    
    public static void glBindImageTextures(final int first, final int count, final IntBuffer textures) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindImageTextures;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (textures != null) {
            BufferChecks.checkBuffer(textures, count);
        }
        nglBindImageTextures(first, count, MemoryUtil.getAddressSafe(textures), function_pointer);
    }
    
    static native void nglBindImageTextures(final int p0, final int p1, final long p2, final long p3);
    
    public static void glBindVertexBuffers(final int first, final int count, final IntBuffer buffers, final PointerBuffer offsets, final IntBuffer strides) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindVertexBuffers;
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
        nglBindVertexBuffers(first, count, MemoryUtil.getAddressSafe(buffers), MemoryUtil.getAddressSafe(offsets), MemoryUtil.getAddressSafe(strides), function_pointer);
    }
    
    static native void nglBindVertexBuffers(final int p0, final int p1, final long p2, final long p3, final long p4, final long p5);
}

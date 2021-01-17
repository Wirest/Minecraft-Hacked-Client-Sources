// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import java.nio.DoubleBuffer;
import java.nio.ByteBuffer;

public final class ARBBufferStorage
{
    public static final int GL_MAP_PERSISTENT_BIT = 64;
    public static final int GL_MAP_COHERENT_BIT = 128;
    public static final int GL_DYNAMIC_STORAGE_BIT = 256;
    public static final int GL_CLIENT_STORAGE_BIT = 512;
    public static final int GL_BUFFER_IMMUTABLE_STORAGE = 33311;
    public static final int GL_BUFFER_STORAGE_FLAGS = 33312;
    public static final int GL_CLIENT_MAPPED_BUFFER_BARRIER_BIT = 16384;
    
    private ARBBufferStorage() {
    }
    
    public static void glBufferStorage(final int target, final ByteBuffer data, final int flags) {
        GL44.glBufferStorage(target, data, flags);
    }
    
    public static void glBufferStorage(final int target, final DoubleBuffer data, final int flags) {
        GL44.glBufferStorage(target, data, flags);
    }
    
    public static void glBufferStorage(final int target, final FloatBuffer data, final int flags) {
        GL44.glBufferStorage(target, data, flags);
    }
    
    public static void glBufferStorage(final int target, final IntBuffer data, final int flags) {
        GL44.glBufferStorage(target, data, flags);
    }
    
    public static void glBufferStorage(final int target, final ShortBuffer data, final int flags) {
        GL44.glBufferStorage(target, data, flags);
    }
    
    public static void glBufferStorage(final int target, final LongBuffer data, final int flags) {
        GL44.glBufferStorage(target, data, flags);
    }
    
    public static void glBufferStorage(final int target, final long size, final int flags) {
        GL44.glBufferStorage(target, size, flags);
    }
    
    public static void glNamedBufferStorageEXT(final int buffer, final ByteBuffer data, final int flags) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferStorageEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferStorageEXT(buffer, data.remaining(), MemoryUtil.getAddress(data), flags, function_pointer);
    }
    
    public static void glNamedBufferStorageEXT(final int buffer, final DoubleBuffer data, final int flags) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferStorageEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferStorageEXT(buffer, data.remaining() << 3, MemoryUtil.getAddress(data), flags, function_pointer);
    }
    
    public static void glNamedBufferStorageEXT(final int buffer, final FloatBuffer data, final int flags) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferStorageEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferStorageEXT(buffer, data.remaining() << 2, MemoryUtil.getAddress(data), flags, function_pointer);
    }
    
    public static void glNamedBufferStorageEXT(final int buffer, final IntBuffer data, final int flags) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferStorageEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferStorageEXT(buffer, data.remaining() << 2, MemoryUtil.getAddress(data), flags, function_pointer);
    }
    
    public static void glNamedBufferStorageEXT(final int buffer, final ShortBuffer data, final int flags) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferStorageEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferStorageEXT(buffer, data.remaining() << 1, MemoryUtil.getAddress(data), flags, function_pointer);
    }
    
    public static void glNamedBufferStorageEXT(final int buffer, final LongBuffer data, final int flags) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferStorageEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferStorageEXT(buffer, data.remaining() << 3, MemoryUtil.getAddress(data), flags, function_pointer);
    }
    
    static native void nglNamedBufferStorageEXT(final int p0, final long p1, final long p2, final int p3, final long p4);
    
    public static void glNamedBufferStorageEXT(final int buffer, final long size, final int flags) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferStorageEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNamedBufferStorageEXT(buffer, size, 0L, flags, function_pointer);
    }
}

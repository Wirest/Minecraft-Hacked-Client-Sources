// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ShortBuffer;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import java.nio.DoubleBuffer;
import org.lwjgl.MemoryUtil;
import java.nio.Buffer;
import org.lwjgl.BufferChecks;
import java.nio.ByteBuffer;

public final class EXTPalettedTexture
{
    public static final int GL_COLOR_INDEX1_EXT = 32994;
    public static final int GL_COLOR_INDEX2_EXT = 32995;
    public static final int GL_COLOR_INDEX4_EXT = 32996;
    public static final int GL_COLOR_INDEX8_EXT = 32997;
    public static final int GL_COLOR_INDEX12_EXT = 32998;
    public static final int GL_COLOR_INDEX16_EXT = 32999;
    public static final int GL_COLOR_TABLE_FORMAT_EXT = 32984;
    public static final int GL_COLOR_TABLE_WIDTH_EXT = 32985;
    public static final int GL_COLOR_TABLE_RED_SIZE_EXT = 32986;
    public static final int GL_COLOR_TABLE_GREEN_SIZE_EXT = 32987;
    public static final int GL_COLOR_TABLE_BLUE_SIZE_EXT = 32988;
    public static final int GL_COLOR_TABLE_ALPHA_SIZE_EXT = 32989;
    public static final int GL_COLOR_TABLE_LUMINANCE_SIZE_EXT = 32990;
    public static final int GL_COLOR_TABLE_INTENSITY_SIZE_EXT = 32991;
    public static final int GL_TEXTURE_INDEX_SIZE_EXT = 33005;
    
    private EXTPalettedTexture() {
    }
    
    public static void glColorTableEXT(final int target, final int internalFormat, final int width, final int format, final int type, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColorTableEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(data, GLChecks.calculateImageStorage(data, format, type, width, 1, 1));
        nglColorTableEXT(target, internalFormat, width, format, type, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glColorTableEXT(final int target, final int internalFormat, final int width, final int format, final int type, final DoubleBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColorTableEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(data, GLChecks.calculateImageStorage(data, format, type, width, 1, 1));
        nglColorTableEXT(target, internalFormat, width, format, type, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glColorTableEXT(final int target, final int internalFormat, final int width, final int format, final int type, final FloatBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColorTableEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(data, GLChecks.calculateImageStorage(data, format, type, width, 1, 1));
        nglColorTableEXT(target, internalFormat, width, format, type, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glColorTableEXT(final int target, final int internalFormat, final int width, final int format, final int type, final IntBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColorTableEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(data, GLChecks.calculateImageStorage(data, format, type, width, 1, 1));
        nglColorTableEXT(target, internalFormat, width, format, type, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glColorTableEXT(final int target, final int internalFormat, final int width, final int format, final int type, final ShortBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColorTableEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(data, GLChecks.calculateImageStorage(data, format, type, width, 1, 1));
        nglColorTableEXT(target, internalFormat, width, format, type, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglColorTableEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5, final long p6);
    
    public static void glColorSubTableEXT(final int target, final int start, final int count, final int format, final int type, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColorSubTableEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(data, GLChecks.calculateImageStorage(data, format, type, count, 1, 1));
        nglColorSubTableEXT(target, start, count, format, type, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glColorSubTableEXT(final int target, final int start, final int count, final int format, final int type, final DoubleBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColorSubTableEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(data, GLChecks.calculateImageStorage(data, format, type, count, 1, 1));
        nglColorSubTableEXT(target, start, count, format, type, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glColorSubTableEXT(final int target, final int start, final int count, final int format, final int type, final FloatBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColorSubTableEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(data, GLChecks.calculateImageStorage(data, format, type, count, 1, 1));
        nglColorSubTableEXT(target, start, count, format, type, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glColorSubTableEXT(final int target, final int start, final int count, final int format, final int type, final IntBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColorSubTableEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(data, GLChecks.calculateImageStorage(data, format, type, count, 1, 1));
        nglColorSubTableEXT(target, start, count, format, type, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glColorSubTableEXT(final int target, final int start, final int count, final int format, final int type, final ShortBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColorSubTableEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(data, GLChecks.calculateImageStorage(data, format, type, count, 1, 1));
        nglColorSubTableEXT(target, start, count, format, type, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglColorSubTableEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5, final long p6);
    
    public static void glGetColorTableEXT(final int target, final int format, final int type, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetColorTableEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglGetColorTableEXT(target, format, type, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glGetColorTableEXT(final int target, final int format, final int type, final DoubleBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetColorTableEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglGetColorTableEXT(target, format, type, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glGetColorTableEXT(final int target, final int format, final int type, final FloatBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetColorTableEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglGetColorTableEXT(target, format, type, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glGetColorTableEXT(final int target, final int format, final int type, final IntBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetColorTableEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglGetColorTableEXT(target, format, type, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glGetColorTableEXT(final int target, final int format, final int type, final ShortBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetColorTableEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglGetColorTableEXT(target, format, type, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglGetColorTableEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetColorTableParameterEXT(final int target, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetColorTableParameterivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetColorTableParameterivEXT(target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetColorTableParameterivEXT(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetColorTableParameterEXT(final int target, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetColorTableParameterfvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetColorTableParameterfvEXT(target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetColorTableParameterfvEXT(final int p0, final int p1, final long p2, final long p3);
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ByteOrder;
import org.lwjgl.LWJGLUtil;
import java.nio.ShortBuffer;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.DoubleBuffer;
import org.lwjgl.MemoryUtil;
import java.nio.FloatBuffer;
import org.lwjgl.BufferChecks;

public final class EXTDirectStateAccess
{
    public static final int GL_PROGRAM_MATRIX_EXT = 36397;
    public static final int GL_TRANSPOSE_PROGRAM_MATRIX_EXT = 36398;
    public static final int GL_PROGRAM_MATRIX_STACK_DEPTH_EXT = 36399;
    
    private EXTDirectStateAccess() {
    }
    
    public static void glClientAttribDefaultEXT(final int mask) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glClientAttribDefaultEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglClientAttribDefaultEXT(mask, function_pointer);
    }
    
    static native void nglClientAttribDefaultEXT(final int p0, final long p1);
    
    public static void glPushClientAttribDefaultEXT(final int mask) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPushClientAttribDefaultEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglPushClientAttribDefaultEXT(mask, function_pointer);
    }
    
    static native void nglPushClientAttribDefaultEXT(final int p0, final long p1);
    
    public static void glMatrixLoadEXT(final int matrixMode, final FloatBuffer m) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMatrixLoadfEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(m, 16);
        nglMatrixLoadfEXT(matrixMode, MemoryUtil.getAddress(m), function_pointer);
    }
    
    static native void nglMatrixLoadfEXT(final int p0, final long p1, final long p2);
    
    public static void glMatrixLoadEXT(final int matrixMode, final DoubleBuffer m) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMatrixLoaddEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(m, 16);
        nglMatrixLoaddEXT(matrixMode, MemoryUtil.getAddress(m), function_pointer);
    }
    
    static native void nglMatrixLoaddEXT(final int p0, final long p1, final long p2);
    
    public static void glMatrixMultEXT(final int matrixMode, final FloatBuffer m) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMatrixMultfEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(m, 16);
        nglMatrixMultfEXT(matrixMode, MemoryUtil.getAddress(m), function_pointer);
    }
    
    static native void nglMatrixMultfEXT(final int p0, final long p1, final long p2);
    
    public static void glMatrixMultEXT(final int matrixMode, final DoubleBuffer m) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMatrixMultdEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(m, 16);
        nglMatrixMultdEXT(matrixMode, MemoryUtil.getAddress(m), function_pointer);
    }
    
    static native void nglMatrixMultdEXT(final int p0, final long p1, final long p2);
    
    public static void glMatrixLoadIdentityEXT(final int matrixMode) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMatrixLoadIdentityEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMatrixLoadIdentityEXT(matrixMode, function_pointer);
    }
    
    static native void nglMatrixLoadIdentityEXT(final int p0, final long p1);
    
    public static void glMatrixRotatefEXT(final int matrixMode, final float angle, final float x, final float y, final float z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMatrixRotatefEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMatrixRotatefEXT(matrixMode, angle, x, y, z, function_pointer);
    }
    
    static native void nglMatrixRotatefEXT(final int p0, final float p1, final float p2, final float p3, final float p4, final long p5);
    
    public static void glMatrixRotatedEXT(final int matrixMode, final double angle, final double x, final double y, final double z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMatrixRotatedEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMatrixRotatedEXT(matrixMode, angle, x, y, z, function_pointer);
    }
    
    static native void nglMatrixRotatedEXT(final int p0, final double p1, final double p2, final double p3, final double p4, final long p5);
    
    public static void glMatrixScalefEXT(final int matrixMode, final float x, final float y, final float z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMatrixScalefEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMatrixScalefEXT(matrixMode, x, y, z, function_pointer);
    }
    
    static native void nglMatrixScalefEXT(final int p0, final float p1, final float p2, final float p3, final long p4);
    
    public static void glMatrixScaledEXT(final int matrixMode, final double x, final double y, final double z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMatrixScaledEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMatrixScaledEXT(matrixMode, x, y, z, function_pointer);
    }
    
    static native void nglMatrixScaledEXT(final int p0, final double p1, final double p2, final double p3, final long p4);
    
    public static void glMatrixTranslatefEXT(final int matrixMode, final float x, final float y, final float z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMatrixTranslatefEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMatrixTranslatefEXT(matrixMode, x, y, z, function_pointer);
    }
    
    static native void nglMatrixTranslatefEXT(final int p0, final float p1, final float p2, final float p3, final long p4);
    
    public static void glMatrixTranslatedEXT(final int matrixMode, final double x, final double y, final double z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMatrixTranslatedEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMatrixTranslatedEXT(matrixMode, x, y, z, function_pointer);
    }
    
    static native void nglMatrixTranslatedEXT(final int p0, final double p1, final double p2, final double p3, final long p4);
    
    public static void glMatrixOrthoEXT(final int matrixMode, final double l, final double r, final double b, final double t, final double n, final double f) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMatrixOrthoEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMatrixOrthoEXT(matrixMode, l, r, b, t, n, f, function_pointer);
    }
    
    static native void nglMatrixOrthoEXT(final int p0, final double p1, final double p2, final double p3, final double p4, final double p5, final double p6, final long p7);
    
    public static void glMatrixFrustumEXT(final int matrixMode, final double l, final double r, final double b, final double t, final double n, final double f) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMatrixFrustumEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMatrixFrustumEXT(matrixMode, l, r, b, t, n, f, function_pointer);
    }
    
    static native void nglMatrixFrustumEXT(final int p0, final double p1, final double p2, final double p3, final double p4, final double p5, final double p6, final long p7);
    
    public static void glMatrixPushEXT(final int matrixMode) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMatrixPushEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMatrixPushEXT(matrixMode, function_pointer);
    }
    
    static native void nglMatrixPushEXT(final int p0, final long p1);
    
    public static void glMatrixPopEXT(final int matrixMode) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMatrixPopEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMatrixPopEXT(matrixMode, function_pointer);
    }
    
    static native void nglMatrixPopEXT(final int p0, final long p1);
    
    public static void glTextureParameteriEXT(final int texture, final int target, final int pname, final int param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureParameteriEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTextureParameteriEXT(texture, target, pname, param, function_pointer);
    }
    
    static native void nglTextureParameteriEXT(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glTextureParameterEXT(final int texture, final int target, final int pname, final IntBuffer param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureParameterivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(param, 4);
        nglTextureParameterivEXT(texture, target, pname, MemoryUtil.getAddress(param), function_pointer);
    }
    
    static native void nglTextureParameterivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glTextureParameterfEXT(final int texture, final int target, final int pname, final float param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureParameterfEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTextureParameterfEXT(texture, target, pname, param, function_pointer);
    }
    
    static native void nglTextureParameterfEXT(final int p0, final int p1, final int p2, final float p3, final long p4);
    
    public static void glTextureParameterEXT(final int texture, final int target, final int pname, final FloatBuffer param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureParameterfvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(param, 4);
        nglTextureParameterfvEXT(texture, target, pname, MemoryUtil.getAddress(param), function_pointer);
    }
    
    static native void nglTextureParameterfvEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glTextureImage1DEXT(final int texture, final int target, final int level, final int internalformat, final int width, final int border, final int format, final int type, final ByteBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        if (pixels != null) {
            BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage1DStorage(pixels, format, type, width));
        }
        nglTextureImage1DEXT(texture, target, level, internalformat, width, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
    }
    
    public static void glTextureImage1DEXT(final int texture, final int target, final int level, final int internalformat, final int width, final int border, final int format, final int type, final DoubleBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        if (pixels != null) {
            BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage1DStorage(pixels, format, type, width));
        }
        nglTextureImage1DEXT(texture, target, level, internalformat, width, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
    }
    
    public static void glTextureImage1DEXT(final int texture, final int target, final int level, final int internalformat, final int width, final int border, final int format, final int type, final FloatBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        if (pixels != null) {
            BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage1DStorage(pixels, format, type, width));
        }
        nglTextureImage1DEXT(texture, target, level, internalformat, width, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
    }
    
    public static void glTextureImage1DEXT(final int texture, final int target, final int level, final int internalformat, final int width, final int border, final int format, final int type, final IntBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        if (pixels != null) {
            BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage1DStorage(pixels, format, type, width));
        }
        nglTextureImage1DEXT(texture, target, level, internalformat, width, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
    }
    
    public static void glTextureImage1DEXT(final int texture, final int target, final int level, final int internalformat, final int width, final int border, final int format, final int type, final ShortBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        if (pixels != null) {
            BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage1DStorage(pixels, format, type, width));
        }
        nglTextureImage1DEXT(texture, target, level, internalformat, width, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
    }
    
    static native void nglTextureImage1DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final long p8, final long p9);
    
    public static void glTextureImage1DEXT(final int texture, final int target, final int level, final int internalformat, final int width, final int border, final int format, final int type, final long pixels_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglTextureImage1DEXTBO(texture, target, level, internalformat, width, border, format, type, pixels_buffer_offset, function_pointer);
    }
    
    static native void nglTextureImage1DEXTBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final long p8, final long p9);
    
    public static void glTextureImage2DEXT(final int texture, final int target, final int level, final int internalformat, final int width, final int height, final int border, final int format, final int type, final ByteBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        if (pixels != null) {
            BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height));
        }
        nglTextureImage2DEXT(texture, target, level, internalformat, width, height, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
    }
    
    public static void glTextureImage2DEXT(final int texture, final int target, final int level, final int internalformat, final int width, final int height, final int border, final int format, final int type, final DoubleBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        if (pixels != null) {
            BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height));
        }
        nglTextureImage2DEXT(texture, target, level, internalformat, width, height, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
    }
    
    public static void glTextureImage2DEXT(final int texture, final int target, final int level, final int internalformat, final int width, final int height, final int border, final int format, final int type, final FloatBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        if (pixels != null) {
            BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height));
        }
        nglTextureImage2DEXT(texture, target, level, internalformat, width, height, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
    }
    
    public static void glTextureImage2DEXT(final int texture, final int target, final int level, final int internalformat, final int width, final int height, final int border, final int format, final int type, final IntBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        if (pixels != null) {
            BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height));
        }
        nglTextureImage2DEXT(texture, target, level, internalformat, width, height, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
    }
    
    public static void glTextureImage2DEXT(final int texture, final int target, final int level, final int internalformat, final int width, final int height, final int border, final int format, final int type, final ShortBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        if (pixels != null) {
            BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height));
        }
        nglTextureImage2DEXT(texture, target, level, internalformat, width, height, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
    }
    
    static native void nglTextureImage2DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final long p9, final long p10);
    
    public static void glTextureImage2DEXT(final int texture, final int target, final int level, final int internalformat, final int width, final int height, final int border, final int format, final int type, final long pixels_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglTextureImage2DEXTBO(texture, target, level, internalformat, width, height, border, format, type, pixels_buffer_offset, function_pointer);
    }
    
    static native void nglTextureImage2DEXTBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final long p9, final long p10);
    
    public static void glTextureSubImage1DEXT(final int texture, final int target, final int level, final int xoffset, final int width, final int format, final int type, final ByteBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
        nglTextureSubImage1DEXT(texture, target, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glTextureSubImage1DEXT(final int texture, final int target, final int level, final int xoffset, final int width, final int format, final int type, final DoubleBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
        nglTextureSubImage1DEXT(texture, target, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glTextureSubImage1DEXT(final int texture, final int target, final int level, final int xoffset, final int width, final int format, final int type, final FloatBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
        nglTextureSubImage1DEXT(texture, target, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glTextureSubImage1DEXT(final int texture, final int target, final int level, final int xoffset, final int width, final int format, final int type, final IntBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
        nglTextureSubImage1DEXT(texture, target, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glTextureSubImage1DEXT(final int texture, final int target, final int level, final int xoffset, final int width, final int format, final int type, final ShortBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
        nglTextureSubImage1DEXT(texture, target, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    static native void nglTextureSubImage1DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final long p7, final long p8);
    
    public static void glTextureSubImage1DEXT(final int texture, final int target, final int level, final int xoffset, final int width, final int format, final int type, final long pixels_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglTextureSubImage1DEXTBO(texture, target, level, xoffset, width, format, type, pixels_buffer_offset, function_pointer);
    }
    
    static native void nglTextureSubImage1DEXTBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final long p7, final long p8);
    
    public static void glTextureSubImage2DEXT(final int texture, final int target, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final int type, final ByteBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
        nglTextureSubImage2DEXT(texture, target, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glTextureSubImage2DEXT(final int texture, final int target, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final int type, final DoubleBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
        nglTextureSubImage2DEXT(texture, target, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glTextureSubImage2DEXT(final int texture, final int target, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final int type, final FloatBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
        nglTextureSubImage2DEXT(texture, target, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glTextureSubImage2DEXT(final int texture, final int target, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final int type, final IntBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
        nglTextureSubImage2DEXT(texture, target, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glTextureSubImage2DEXT(final int texture, final int target, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final int type, final ShortBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
        nglTextureSubImage2DEXT(texture, target, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    static native void nglTextureSubImage2DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final long p9, final long p10);
    
    public static void glTextureSubImage2DEXT(final int texture, final int target, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final int type, final long pixels_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglTextureSubImage2DEXTBO(texture, target, level, xoffset, yoffset, width, height, format, type, pixels_buffer_offset, function_pointer);
    }
    
    static native void nglTextureSubImage2DEXTBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final long p9, final long p10);
    
    public static void glCopyTextureImage1DEXT(final int texture, final int target, final int level, final int internalformat, final int x, final int y, final int width, final int border) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCopyTextureImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglCopyTextureImage1DEXT(texture, target, level, internalformat, x, y, width, border, function_pointer);
    }
    
    static native void nglCopyTextureImage1DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final long p8);
    
    public static void glCopyTextureImage2DEXT(final int texture, final int target, final int level, final int internalformat, final int x, final int y, final int width, final int height, final int border) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCopyTextureImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglCopyTextureImage2DEXT(texture, target, level, internalformat, x, y, width, height, border, function_pointer);
    }
    
    static native void nglCopyTextureImage2DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final long p9);
    
    public static void glCopyTextureSubImage1DEXT(final int texture, final int target, final int level, final int xoffset, final int x, final int y, final int width) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCopyTextureSubImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglCopyTextureSubImage1DEXT(texture, target, level, xoffset, x, y, width, function_pointer);
    }
    
    static native void nglCopyTextureSubImage1DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final long p7);
    
    public static void glCopyTextureSubImage2DEXT(final int texture, final int target, final int level, final int xoffset, final int yoffset, final int x, final int y, final int width, final int height) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCopyTextureSubImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglCopyTextureSubImage2DEXT(texture, target, level, xoffset, yoffset, x, y, width, height, function_pointer);
    }
    
    static native void nglCopyTextureSubImage2DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final long p9);
    
    public static void glGetTextureImageEXT(final int texture, final int target, final int level, final int format, final int type, final ByteBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureImageEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1));
        nglGetTextureImageEXT(texture, target, level, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glGetTextureImageEXT(final int texture, final int target, final int level, final int format, final int type, final DoubleBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureImageEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1));
        nglGetTextureImageEXT(texture, target, level, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glGetTextureImageEXT(final int texture, final int target, final int level, final int format, final int type, final FloatBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureImageEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1));
        nglGetTextureImageEXT(texture, target, level, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glGetTextureImageEXT(final int texture, final int target, final int level, final int format, final int type, final IntBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureImageEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1));
        nglGetTextureImageEXT(texture, target, level, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glGetTextureImageEXT(final int texture, final int target, final int level, final int format, final int type, final ShortBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureImageEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1));
        nglGetTextureImageEXT(texture, target, level, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    static native void nglGetTextureImageEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5, final long p6);
    
    public static void glGetTextureImageEXT(final int texture, final int target, final int level, final int format, final int type, final long pixels_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureImageEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOenabled(caps);
        nglGetTextureImageEXTBO(texture, target, level, format, type, pixels_buffer_offset, function_pointer);
    }
    
    static native void nglGetTextureImageEXTBO(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5, final long p6);
    
    public static void glGetTextureParameterEXT(final int texture, final int target, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureParameterfvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetTextureParameterfvEXT(texture, target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetTextureParameterfvEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static float glGetTextureParameterfEXT(final int texture, final int target, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureParameterfvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final FloatBuffer params = APIUtil.getBufferFloat(caps);
        nglGetTextureParameterfvEXT(texture, target, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetTextureParameterEXT(final int texture, final int target, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureParameterivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetTextureParameterivEXT(texture, target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetTextureParameterivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static int glGetTextureParameteriEXT(final int texture, final int target, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureParameterivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetTextureParameterivEXT(texture, target, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetTextureLevelParameterEXT(final int texture, final int target, final int level, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureLevelParameterfvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetTextureLevelParameterfvEXT(texture, target, level, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetTextureLevelParameterfvEXT(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static float glGetTextureLevelParameterfEXT(final int texture, final int target, final int level, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureLevelParameterfvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final FloatBuffer params = APIUtil.getBufferFloat(caps);
        nglGetTextureLevelParameterfvEXT(texture, target, level, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetTextureLevelParameterEXT(final int texture, final int target, final int level, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureLevelParameterivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetTextureLevelParameterivEXT(texture, target, level, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetTextureLevelParameterivEXT(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static int glGetTextureLevelParameteriEXT(final int texture, final int target, final int level, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureLevelParameterivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetTextureLevelParameterivEXT(texture, target, level, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glTextureImage3DEXT(final int texture, final int target, final int level, final int internalformat, final int width, final int height, final int depth, final int border, final int format, final int type, final ByteBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        if (pixels != null) {
            BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
        }
        nglTextureImage3DEXT(texture, target, level, internalformat, width, height, depth, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
    }
    
    public static void glTextureImage3DEXT(final int texture, final int target, final int level, final int internalformat, final int width, final int height, final int depth, final int border, final int format, final int type, final DoubleBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        if (pixels != null) {
            BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
        }
        nglTextureImage3DEXT(texture, target, level, internalformat, width, height, depth, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
    }
    
    public static void glTextureImage3DEXT(final int texture, final int target, final int level, final int internalformat, final int width, final int height, final int depth, final int border, final int format, final int type, final FloatBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        if (pixels != null) {
            BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
        }
        nglTextureImage3DEXT(texture, target, level, internalformat, width, height, depth, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
    }
    
    public static void glTextureImage3DEXT(final int texture, final int target, final int level, final int internalformat, final int width, final int height, final int depth, final int border, final int format, final int type, final IntBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        if (pixels != null) {
            BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
        }
        nglTextureImage3DEXT(texture, target, level, internalformat, width, height, depth, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
    }
    
    public static void glTextureImage3DEXT(final int texture, final int target, final int level, final int internalformat, final int width, final int height, final int depth, final int border, final int format, final int type, final ShortBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        if (pixels != null) {
            BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
        }
        nglTextureImage3DEXT(texture, target, level, internalformat, width, height, depth, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
    }
    
    static native void nglTextureImage3DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final long p10, final long p11);
    
    public static void glTextureImage3DEXT(final int texture, final int target, final int level, final int internalformat, final int width, final int height, final int depth, final int border, final int format, final int type, final long pixels_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglTextureImage3DEXTBO(texture, target, level, internalformat, width, height, depth, border, format, type, pixels_buffer_offset, function_pointer);
    }
    
    static native void nglTextureImage3DEXTBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final long p10, final long p11);
    
    public static void glTextureSubImage3DEXT(final int texture, final int target, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final ByteBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
        nglTextureSubImage3DEXT(texture, target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glTextureSubImage3DEXT(final int texture, final int target, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final DoubleBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
        nglTextureSubImage3DEXT(texture, target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glTextureSubImage3DEXT(final int texture, final int target, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final FloatBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
        nglTextureSubImage3DEXT(texture, target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glTextureSubImage3DEXT(final int texture, final int target, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final IntBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
        nglTextureSubImage3DEXT(texture, target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glTextureSubImage3DEXT(final int texture, final int target, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final ShortBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
        nglTextureSubImage3DEXT(texture, target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    static native void nglTextureSubImage3DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final int p10, final long p11, final long p12);
    
    public static void glTextureSubImage3DEXT(final int texture, final int target, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final long pixels_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureSubImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglTextureSubImage3DEXTBO(texture, target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels_buffer_offset, function_pointer);
    }
    
    static native void nglTextureSubImage3DEXTBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final int p10, final long p11, final long p12);
    
    public static void glCopyTextureSubImage3DEXT(final int texture, final int target, final int level, final int xoffset, final int yoffset, final int zoffset, final int x, final int y, final int width, final int height) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCopyTextureSubImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglCopyTextureSubImage3DEXT(texture, target, level, xoffset, yoffset, zoffset, x, y, width, height, function_pointer);
    }
    
    static native void nglCopyTextureSubImage3DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final long p10);
    
    public static void glBindMultiTextureEXT(final int texunit, final int target, final int texture) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindMultiTextureEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBindMultiTextureEXT(texunit, target, texture, function_pointer);
    }
    
    static native void nglBindMultiTextureEXT(final int p0, final int p1, final int p2, final long p3);
    
    public static void glMultiTexCoordPointerEXT(final int texunit, final int size, final int stride, final DoubleBuffer pointer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoordPointerEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(pointer);
        nglMultiTexCoordPointerEXT(texunit, size, 5130, stride, MemoryUtil.getAddress(pointer), function_pointer);
    }
    
    public static void glMultiTexCoordPointerEXT(final int texunit, final int size, final int stride, final FloatBuffer pointer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoordPointerEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(pointer);
        nglMultiTexCoordPointerEXT(texunit, size, 5126, stride, MemoryUtil.getAddress(pointer), function_pointer);
    }
    
    static native void nglMultiTexCoordPointerEXT(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glMultiTexCoordPointerEXT(final int texunit, final int size, final int type, final int stride, final long pointer_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoordPointerEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOenabled(caps);
        nglMultiTexCoordPointerEXTBO(texunit, size, type, stride, pointer_buffer_offset, function_pointer);
    }
    
    static native void nglMultiTexCoordPointerEXTBO(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glMultiTexEnvfEXT(final int texunit, final int target, final int pname, final float param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexEnvfEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexEnvfEXT(texunit, target, pname, param, function_pointer);
    }
    
    static native void nglMultiTexEnvfEXT(final int p0, final int p1, final int p2, final float p3, final long p4);
    
    public static void glMultiTexEnvEXT(final int texunit, final int target, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexEnvfvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglMultiTexEnvfvEXT(texunit, target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglMultiTexEnvfvEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glMultiTexEnviEXT(final int texunit, final int target, final int pname, final int param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexEnviEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexEnviEXT(texunit, target, pname, param, function_pointer);
    }
    
    static native void nglMultiTexEnviEXT(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glMultiTexEnvEXT(final int texunit, final int target, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexEnvivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglMultiTexEnvivEXT(texunit, target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglMultiTexEnvivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glMultiTexGendEXT(final int texunit, final int coord, final int pname, final double param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexGendEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexGendEXT(texunit, coord, pname, param, function_pointer);
    }
    
    static native void nglMultiTexGendEXT(final int p0, final int p1, final int p2, final double p3, final long p4);
    
    public static void glMultiTexGenEXT(final int texunit, final int coord, final int pname, final DoubleBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexGendvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglMultiTexGendvEXT(texunit, coord, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglMultiTexGendvEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glMultiTexGenfEXT(final int texunit, final int coord, final int pname, final float param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexGenfEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexGenfEXT(texunit, coord, pname, param, function_pointer);
    }
    
    static native void nglMultiTexGenfEXT(final int p0, final int p1, final int p2, final float p3, final long p4);
    
    public static void glMultiTexGenEXT(final int texunit, final int coord, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexGenfvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglMultiTexGenfvEXT(texunit, coord, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglMultiTexGenfvEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glMultiTexGeniEXT(final int texunit, final int coord, final int pname, final int param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexGeniEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexGeniEXT(texunit, coord, pname, param, function_pointer);
    }
    
    static native void nglMultiTexGeniEXT(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glMultiTexGenEXT(final int texunit, final int coord, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexGenivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglMultiTexGenivEXT(texunit, coord, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglMultiTexGenivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetMultiTexEnvEXT(final int texunit, final int target, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMultiTexEnvfvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetMultiTexEnvfvEXT(texunit, target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetMultiTexEnvfvEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetMultiTexEnvEXT(final int texunit, final int target, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMultiTexEnvivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetMultiTexEnvivEXT(texunit, target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetMultiTexEnvivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetMultiTexGenEXT(final int texunit, final int coord, final int pname, final DoubleBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMultiTexGendvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetMultiTexGendvEXT(texunit, coord, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetMultiTexGendvEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetMultiTexGenEXT(final int texunit, final int coord, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMultiTexGenfvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetMultiTexGenfvEXT(texunit, coord, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetMultiTexGenfvEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetMultiTexGenEXT(final int texunit, final int coord, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMultiTexGenivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetMultiTexGenivEXT(texunit, coord, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetMultiTexGenivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glMultiTexParameteriEXT(final int texunit, final int target, final int pname, final int param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexParameteriEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexParameteriEXT(texunit, target, pname, param, function_pointer);
    }
    
    static native void nglMultiTexParameteriEXT(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glMultiTexParameterEXT(final int texunit, final int target, final int pname, final IntBuffer param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexParameterivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(param, 4);
        nglMultiTexParameterivEXT(texunit, target, pname, MemoryUtil.getAddress(param), function_pointer);
    }
    
    static native void nglMultiTexParameterivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glMultiTexParameterfEXT(final int texunit, final int target, final int pname, final float param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexParameterfEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexParameterfEXT(texunit, target, pname, param, function_pointer);
    }
    
    static native void nglMultiTexParameterfEXT(final int p0, final int p1, final int p2, final float p3, final long p4);
    
    public static void glMultiTexParameterEXT(final int texunit, final int target, final int pname, final FloatBuffer param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexParameterfvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(param, 4);
        nglMultiTexParameterfvEXT(texunit, target, pname, MemoryUtil.getAddress(param), function_pointer);
    }
    
    static native void nglMultiTexParameterfvEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glMultiTexImage1DEXT(final int texunit, final int target, final int level, final int internalformat, final int width, final int border, final int format, final int type, final ByteBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        if (pixels != null) {
            BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage1DStorage(pixels, format, type, width));
        }
        nglMultiTexImage1DEXT(texunit, target, level, internalformat, width, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
    }
    
    public static void glMultiTexImage1DEXT(final int texunit, final int target, final int level, final int internalformat, final int width, final int border, final int format, final int type, final DoubleBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        if (pixels != null) {
            BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage1DStorage(pixels, format, type, width));
        }
        nglMultiTexImage1DEXT(texunit, target, level, internalformat, width, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
    }
    
    public static void glMultiTexImage1DEXT(final int texunit, final int target, final int level, final int internalformat, final int width, final int border, final int format, final int type, final FloatBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        if (pixels != null) {
            BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage1DStorage(pixels, format, type, width));
        }
        nglMultiTexImage1DEXT(texunit, target, level, internalformat, width, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
    }
    
    public static void glMultiTexImage1DEXT(final int texunit, final int target, final int level, final int internalformat, final int width, final int border, final int format, final int type, final IntBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        if (pixels != null) {
            BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage1DStorage(pixels, format, type, width));
        }
        nglMultiTexImage1DEXT(texunit, target, level, internalformat, width, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
    }
    
    public static void glMultiTexImage1DEXT(final int texunit, final int target, final int level, final int internalformat, final int width, final int border, final int format, final int type, final ShortBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        if (pixels != null) {
            BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage1DStorage(pixels, format, type, width));
        }
        nglMultiTexImage1DEXT(texunit, target, level, internalformat, width, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
    }
    
    static native void nglMultiTexImage1DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final long p8, final long p9);
    
    public static void glMultiTexImage1DEXT(final int texunit, final int target, final int level, final int internalformat, final int width, final int border, final int format, final int type, final long pixels_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglMultiTexImage1DEXTBO(texunit, target, level, internalformat, width, border, format, type, pixels_buffer_offset, function_pointer);
    }
    
    static native void nglMultiTexImage1DEXTBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final long p8, final long p9);
    
    public static void glMultiTexImage2DEXT(final int texunit, final int target, final int level, final int internalformat, final int width, final int height, final int border, final int format, final int type, final ByteBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        if (pixels != null) {
            BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height));
        }
        nglMultiTexImage2DEXT(texunit, target, level, internalformat, width, height, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
    }
    
    public static void glMultiTexImage2DEXT(final int texunit, final int target, final int level, final int internalformat, final int width, final int height, final int border, final int format, final int type, final DoubleBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        if (pixels != null) {
            BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height));
        }
        nglMultiTexImage2DEXT(texunit, target, level, internalformat, width, height, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
    }
    
    public static void glMultiTexImage2DEXT(final int texunit, final int target, final int level, final int internalformat, final int width, final int height, final int border, final int format, final int type, final FloatBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        if (pixels != null) {
            BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height));
        }
        nglMultiTexImage2DEXT(texunit, target, level, internalformat, width, height, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
    }
    
    public static void glMultiTexImage2DEXT(final int texunit, final int target, final int level, final int internalformat, final int width, final int height, final int border, final int format, final int type, final IntBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        if (pixels != null) {
            BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height));
        }
        nglMultiTexImage2DEXT(texunit, target, level, internalformat, width, height, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
    }
    
    public static void glMultiTexImage2DEXT(final int texunit, final int target, final int level, final int internalformat, final int width, final int height, final int border, final int format, final int type, final ShortBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        if (pixels != null) {
            BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height));
        }
        nglMultiTexImage2DEXT(texunit, target, level, internalformat, width, height, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
    }
    
    static native void nglMultiTexImage2DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final long p9, final long p10);
    
    public static void glMultiTexImage2DEXT(final int texunit, final int target, final int level, final int internalformat, final int width, final int height, final int border, final int format, final int type, final long pixels_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglMultiTexImage2DEXTBO(texunit, target, level, internalformat, width, height, border, format, type, pixels_buffer_offset, function_pointer);
    }
    
    static native void nglMultiTexImage2DEXTBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final long p9, final long p10);
    
    public static void glMultiTexSubImage1DEXT(final int texunit, final int target, final int level, final int xoffset, final int width, final int format, final int type, final ByteBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexSubImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
        nglMultiTexSubImage1DEXT(texunit, target, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glMultiTexSubImage1DEXT(final int texunit, final int target, final int level, final int xoffset, final int width, final int format, final int type, final DoubleBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexSubImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
        nglMultiTexSubImage1DEXT(texunit, target, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glMultiTexSubImage1DEXT(final int texunit, final int target, final int level, final int xoffset, final int width, final int format, final int type, final FloatBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexSubImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
        nglMultiTexSubImage1DEXT(texunit, target, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glMultiTexSubImage1DEXT(final int texunit, final int target, final int level, final int xoffset, final int width, final int format, final int type, final IntBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexSubImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
        nglMultiTexSubImage1DEXT(texunit, target, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glMultiTexSubImage1DEXT(final int texunit, final int target, final int level, final int xoffset, final int width, final int format, final int type, final ShortBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexSubImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
        nglMultiTexSubImage1DEXT(texunit, target, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    static native void nglMultiTexSubImage1DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final long p7, final long p8);
    
    public static void glMultiTexSubImage1DEXT(final int texunit, final int target, final int level, final int xoffset, final int width, final int format, final int type, final long pixels_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexSubImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglMultiTexSubImage1DEXTBO(texunit, target, level, xoffset, width, format, type, pixels_buffer_offset, function_pointer);
    }
    
    static native void nglMultiTexSubImage1DEXTBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final long p7, final long p8);
    
    public static void glMultiTexSubImage2DEXT(final int texunit, final int target, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final int type, final ByteBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexSubImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
        nglMultiTexSubImage2DEXT(texunit, target, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glMultiTexSubImage2DEXT(final int texunit, final int target, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final int type, final DoubleBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexSubImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
        nglMultiTexSubImage2DEXT(texunit, target, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glMultiTexSubImage2DEXT(final int texunit, final int target, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final int type, final FloatBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexSubImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
        nglMultiTexSubImage2DEXT(texunit, target, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glMultiTexSubImage2DEXT(final int texunit, final int target, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final int type, final IntBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexSubImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
        nglMultiTexSubImage2DEXT(texunit, target, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glMultiTexSubImage2DEXT(final int texunit, final int target, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final int type, final ShortBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexSubImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
        nglMultiTexSubImage2DEXT(texunit, target, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    static native void nglMultiTexSubImage2DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final long p9, final long p10);
    
    public static void glMultiTexSubImage2DEXT(final int texunit, final int target, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final int type, final long pixels_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexSubImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglMultiTexSubImage2DEXTBO(texunit, target, level, xoffset, yoffset, width, height, format, type, pixels_buffer_offset, function_pointer);
    }
    
    static native void nglMultiTexSubImage2DEXTBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final long p9, final long p10);
    
    public static void glCopyMultiTexImage1DEXT(final int texunit, final int target, final int level, final int internalformat, final int x, final int y, final int width, final int border) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCopyMultiTexImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglCopyMultiTexImage1DEXT(texunit, target, level, internalformat, x, y, width, border, function_pointer);
    }
    
    static native void nglCopyMultiTexImage1DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final long p8);
    
    public static void glCopyMultiTexImage2DEXT(final int texunit, final int target, final int level, final int internalformat, final int x, final int y, final int width, final int height, final int border) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCopyMultiTexImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglCopyMultiTexImage2DEXT(texunit, target, level, internalformat, x, y, width, height, border, function_pointer);
    }
    
    static native void nglCopyMultiTexImage2DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final long p9);
    
    public static void glCopyMultiTexSubImage1DEXT(final int texunit, final int target, final int level, final int xoffset, final int x, final int y, final int width) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCopyMultiTexSubImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglCopyMultiTexSubImage1DEXT(texunit, target, level, xoffset, x, y, width, function_pointer);
    }
    
    static native void nglCopyMultiTexSubImage1DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final long p7);
    
    public static void glCopyMultiTexSubImage2DEXT(final int texunit, final int target, final int level, final int xoffset, final int yoffset, final int x, final int y, final int width, final int height) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCopyMultiTexSubImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglCopyMultiTexSubImage2DEXT(texunit, target, level, xoffset, yoffset, x, y, width, height, function_pointer);
    }
    
    static native void nglCopyMultiTexSubImage2DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final long p9);
    
    public static void glGetMultiTexImageEXT(final int texunit, final int target, final int level, final int format, final int type, final ByteBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMultiTexImageEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1));
        nglGetMultiTexImageEXT(texunit, target, level, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glGetMultiTexImageEXT(final int texunit, final int target, final int level, final int format, final int type, final DoubleBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMultiTexImageEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1));
        nglGetMultiTexImageEXT(texunit, target, level, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glGetMultiTexImageEXT(final int texunit, final int target, final int level, final int format, final int type, final FloatBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMultiTexImageEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1));
        nglGetMultiTexImageEXT(texunit, target, level, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glGetMultiTexImageEXT(final int texunit, final int target, final int level, final int format, final int type, final IntBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMultiTexImageEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1));
        nglGetMultiTexImageEXT(texunit, target, level, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glGetMultiTexImageEXT(final int texunit, final int target, final int level, final int format, final int type, final ShortBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMultiTexImageEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1));
        nglGetMultiTexImageEXT(texunit, target, level, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    static native void nglGetMultiTexImageEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5, final long p6);
    
    public static void glGetMultiTexImageEXT(final int texunit, final int target, final int level, final int format, final int type, final long pixels_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMultiTexImageEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOenabled(caps);
        nglGetMultiTexImageEXTBO(texunit, target, level, format, type, pixels_buffer_offset, function_pointer);
    }
    
    static native void nglGetMultiTexImageEXTBO(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5, final long p6);
    
    public static void glGetMultiTexParameterEXT(final int texunit, final int target, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMultiTexParameterfvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetMultiTexParameterfvEXT(texunit, target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetMultiTexParameterfvEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static float glGetMultiTexParameterfEXT(final int texunit, final int target, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMultiTexParameterfvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final FloatBuffer params = APIUtil.getBufferFloat(caps);
        nglGetMultiTexParameterfvEXT(texunit, target, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetMultiTexParameterEXT(final int texunit, final int target, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMultiTexParameterivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetMultiTexParameterivEXT(texunit, target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetMultiTexParameterivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static int glGetMultiTexParameteriEXT(final int texunit, final int target, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMultiTexParameterivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetMultiTexParameterivEXT(texunit, target, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetMultiTexLevelParameterEXT(final int texunit, final int target, final int level, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMultiTexLevelParameterfvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetMultiTexLevelParameterfvEXT(texunit, target, level, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetMultiTexLevelParameterfvEXT(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static float glGetMultiTexLevelParameterfEXT(final int texunit, final int target, final int level, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMultiTexLevelParameterfvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final FloatBuffer params = APIUtil.getBufferFloat(caps);
        nglGetMultiTexLevelParameterfvEXT(texunit, target, level, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetMultiTexLevelParameterEXT(final int texunit, final int target, final int level, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMultiTexLevelParameterivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetMultiTexLevelParameterivEXT(texunit, target, level, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetMultiTexLevelParameterivEXT(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static int glGetMultiTexLevelParameteriEXT(final int texunit, final int target, final int level, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMultiTexLevelParameterivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetMultiTexLevelParameterivEXT(texunit, target, level, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glMultiTexImage3DEXT(final int texunit, final int target, final int level, final int internalformat, final int width, final int height, final int depth, final int border, final int format, final int type, final ByteBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        if (pixels != null) {
            BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
        }
        nglMultiTexImage3DEXT(texunit, target, level, internalformat, width, height, depth, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
    }
    
    public static void glMultiTexImage3DEXT(final int texunit, final int target, final int level, final int internalformat, final int width, final int height, final int depth, final int border, final int format, final int type, final DoubleBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        if (pixels != null) {
            BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
        }
        nglMultiTexImage3DEXT(texunit, target, level, internalformat, width, height, depth, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
    }
    
    public static void glMultiTexImage3DEXT(final int texunit, final int target, final int level, final int internalformat, final int width, final int height, final int depth, final int border, final int format, final int type, final FloatBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        if (pixels != null) {
            BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
        }
        nglMultiTexImage3DEXT(texunit, target, level, internalformat, width, height, depth, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
    }
    
    public static void glMultiTexImage3DEXT(final int texunit, final int target, final int level, final int internalformat, final int width, final int height, final int depth, final int border, final int format, final int type, final IntBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        if (pixels != null) {
            BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
        }
        nglMultiTexImage3DEXT(texunit, target, level, internalformat, width, height, depth, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
    }
    
    public static void glMultiTexImage3DEXT(final int texunit, final int target, final int level, final int internalformat, final int width, final int height, final int depth, final int border, final int format, final int type, final ShortBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        if (pixels != null) {
            BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
        }
        nglMultiTexImage3DEXT(texunit, target, level, internalformat, width, height, depth, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
    }
    
    static native void nglMultiTexImage3DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final long p10, final long p11);
    
    public static void glMultiTexImage3DEXT(final int texunit, final int target, final int level, final int internalformat, final int width, final int height, final int depth, final int border, final int format, final int type, final long pixels_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglMultiTexImage3DEXTBO(texunit, target, level, internalformat, width, height, depth, border, format, type, pixels_buffer_offset, function_pointer);
    }
    
    static native void nglMultiTexImage3DEXTBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final long p10, final long p11);
    
    public static void glMultiTexSubImage3DEXT(final int texunit, final int target, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final ByteBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexSubImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
        nglMultiTexSubImage3DEXT(texunit, target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glMultiTexSubImage3DEXT(final int texunit, final int target, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final DoubleBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexSubImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
        nglMultiTexSubImage3DEXT(texunit, target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glMultiTexSubImage3DEXT(final int texunit, final int target, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final FloatBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexSubImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
        nglMultiTexSubImage3DEXT(texunit, target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glMultiTexSubImage3DEXT(final int texunit, final int target, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final IntBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexSubImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
        nglMultiTexSubImage3DEXT(texunit, target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    public static void glMultiTexSubImage3DEXT(final int texunit, final int target, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final ShortBuffer pixels) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexSubImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
        nglMultiTexSubImage3DEXT(texunit, target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels), function_pointer);
    }
    
    static native void nglMultiTexSubImage3DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final int p10, final long p11, final long p12);
    
    public static void glMultiTexSubImage3DEXT(final int texunit, final int target, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final long pixels_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexSubImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglMultiTexSubImage3DEXTBO(texunit, target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels_buffer_offset, function_pointer);
    }
    
    static native void nglMultiTexSubImage3DEXTBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final int p10, final long p11, final long p12);
    
    public static void glCopyMultiTexSubImage3DEXT(final int texunit, final int target, final int level, final int xoffset, final int yoffset, final int zoffset, final int x, final int y, final int width, final int height) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCopyMultiTexSubImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglCopyMultiTexSubImage3DEXT(texunit, target, level, xoffset, yoffset, zoffset, x, y, width, height, function_pointer);
    }
    
    static native void nglCopyMultiTexSubImage3DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final long p10);
    
    public static void glEnableClientStateIndexedEXT(final int array, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glEnableClientStateIndexedEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglEnableClientStateIndexedEXT(array, index, function_pointer);
    }
    
    static native void nglEnableClientStateIndexedEXT(final int p0, final int p1, final long p2);
    
    public static void glDisableClientStateIndexedEXT(final int array, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDisableClientStateIndexedEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDisableClientStateIndexedEXT(array, index, function_pointer);
    }
    
    static native void nglDisableClientStateIndexedEXT(final int p0, final int p1, final long p2);
    
    public static void glEnableClientStateiEXT(final int array, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glEnableClientStateiEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglEnableClientStateiEXT(array, index, function_pointer);
    }
    
    static native void nglEnableClientStateiEXT(final int p0, final int p1, final long p2);
    
    public static void glDisableClientStateiEXT(final int array, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDisableClientStateiEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDisableClientStateiEXT(array, index, function_pointer);
    }
    
    static native void nglDisableClientStateiEXT(final int p0, final int p1, final long p2);
    
    public static void glGetFloatIndexedEXT(final int pname, final int index, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetFloatIndexedvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 16);
        nglGetFloatIndexedvEXT(pname, index, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetFloatIndexedvEXT(final int p0, final int p1, final long p2, final long p3);
    
    public static float glGetFloatIndexedEXT(final int pname, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetFloatIndexedvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final FloatBuffer params = APIUtil.getBufferFloat(caps);
        nglGetFloatIndexedvEXT(pname, index, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetDoubleIndexedEXT(final int pname, final int index, final DoubleBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetDoubleIndexedvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 16);
        nglGetDoubleIndexedvEXT(pname, index, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetDoubleIndexedvEXT(final int p0, final int p1, final long p2, final long p3);
    
    public static double glGetDoubleIndexedEXT(final int pname, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetDoubleIndexedvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final DoubleBuffer params = APIUtil.getBufferDouble(caps);
        nglGetDoubleIndexedvEXT(pname, index, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static ByteBuffer glGetPointerIndexedEXT(final int pname, final int index, final long result_size) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetPointerIndexedvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final ByteBuffer __result = nglGetPointerIndexedvEXT(pname, index, result_size, function_pointer);
        return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
    }
    
    static native ByteBuffer nglGetPointerIndexedvEXT(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetFloatEXT(final int pname, final int index, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetFloati_vEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 16);
        nglGetFloati_vEXT(pname, index, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetFloati_vEXT(final int p0, final int p1, final long p2, final long p3);
    
    public static float glGetFloatEXT(final int pname, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetFloati_vEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final FloatBuffer params = APIUtil.getBufferFloat(caps);
        nglGetFloati_vEXT(pname, index, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetDoubleEXT(final int pname, final int index, final DoubleBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetDoublei_vEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 16);
        nglGetDoublei_vEXT(pname, index, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetDoublei_vEXT(final int p0, final int p1, final long p2, final long p3);
    
    public static double glGetDoubleEXT(final int pname, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetDoublei_vEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final DoubleBuffer params = APIUtil.getBufferDouble(caps);
        nglGetDoublei_vEXT(pname, index, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static ByteBuffer glGetPointerEXT(final int pname, final int index, final long result_size) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetPointeri_vEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final ByteBuffer __result = nglGetPointeri_vEXT(pname, index, result_size, function_pointer);
        return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
    }
    
    static native ByteBuffer nglGetPointeri_vEXT(final int p0, final int p1, final long p2, final long p3);
    
    public static void glEnableIndexedEXT(final int cap, final int index) {
        EXTDrawBuffers2.glEnableIndexedEXT(cap, index);
    }
    
    public static void glDisableIndexedEXT(final int cap, final int index) {
        EXTDrawBuffers2.glDisableIndexedEXT(cap, index);
    }
    
    public static boolean glIsEnabledIndexedEXT(final int cap, final int index) {
        return EXTDrawBuffers2.glIsEnabledIndexedEXT(cap, index);
    }
    
    public static void glGetIntegerIndexedEXT(final int pname, final int index, final IntBuffer params) {
        EXTDrawBuffers2.glGetIntegerIndexedEXT(pname, index, params);
    }
    
    public static int glGetIntegerIndexedEXT(final int pname, final int index) {
        return EXTDrawBuffers2.glGetIntegerIndexedEXT(pname, index);
    }
    
    public static void glGetBooleanIndexedEXT(final int pname, final int index, final ByteBuffer params) {
        EXTDrawBuffers2.glGetBooleanIndexedEXT(pname, index, params);
    }
    
    public static boolean glGetBooleanIndexedEXT(final int pname, final int index) {
        return EXTDrawBuffers2.glGetBooleanIndexedEXT(pname, index);
    }
    
    public static void glNamedProgramStringEXT(final int program, final int target, final int format, final ByteBuffer string) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedProgramStringEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(string);
        nglNamedProgramStringEXT(program, target, format, string.remaining(), MemoryUtil.getAddress(string), function_pointer);
    }
    
    static native void nglNamedProgramStringEXT(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glNamedProgramStringEXT(final int program, final int target, final int format, final CharSequence string) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedProgramStringEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNamedProgramStringEXT(program, target, format, string.length(), APIUtil.getBuffer(caps, string), function_pointer);
    }
    
    public static void glNamedProgramLocalParameter4dEXT(final int program, final int target, final int index, final double x, final double y, final double z, final double w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedProgramLocalParameter4dEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNamedProgramLocalParameter4dEXT(program, target, index, x, y, z, w, function_pointer);
    }
    
    static native void nglNamedProgramLocalParameter4dEXT(final int p0, final int p1, final int p2, final double p3, final double p4, final double p5, final double p6, final long p7);
    
    public static void glNamedProgramLocalParameter4EXT(final int program, final int target, final int index, final DoubleBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedProgramLocalParameter4dvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglNamedProgramLocalParameter4dvEXT(program, target, index, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglNamedProgramLocalParameter4dvEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glNamedProgramLocalParameter4fEXT(final int program, final int target, final int index, final float x, final float y, final float z, final float w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedProgramLocalParameter4fEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNamedProgramLocalParameter4fEXT(program, target, index, x, y, z, w, function_pointer);
    }
    
    static native void nglNamedProgramLocalParameter4fEXT(final int p0, final int p1, final int p2, final float p3, final float p4, final float p5, final float p6, final long p7);
    
    public static void glNamedProgramLocalParameter4EXT(final int program, final int target, final int index, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedProgramLocalParameter4fvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglNamedProgramLocalParameter4fvEXT(program, target, index, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglNamedProgramLocalParameter4fvEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetNamedProgramLocalParameterEXT(final int program, final int target, final int index, final DoubleBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedProgramLocalParameterdvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetNamedProgramLocalParameterdvEXT(program, target, index, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetNamedProgramLocalParameterdvEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetNamedProgramLocalParameterEXT(final int program, final int target, final int index, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedProgramLocalParameterfvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetNamedProgramLocalParameterfvEXT(program, target, index, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetNamedProgramLocalParameterfvEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetNamedProgramEXT(final int program, final int target, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedProgramivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetNamedProgramivEXT(program, target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetNamedProgramivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static int glGetNamedProgramEXT(final int program, final int target, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedProgramivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetNamedProgramivEXT(program, target, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetNamedProgramStringEXT(final int program, final int target, final int pname, final ByteBuffer string) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedProgramStringEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(string);
        nglGetNamedProgramStringEXT(program, target, pname, MemoryUtil.getAddress(string), function_pointer);
    }
    
    static native void nglGetNamedProgramStringEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static String glGetNamedProgramStringEXT(final int program, final int target, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedProgramStringEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int programLength = glGetNamedProgramEXT(program, target, 34343);
        final ByteBuffer paramString = APIUtil.getBufferByte(caps, programLength);
        nglGetNamedProgramStringEXT(program, target, pname, MemoryUtil.getAddress(paramString), function_pointer);
        paramString.limit(programLength);
        return APIUtil.getString(caps, paramString);
    }
    
    public static void glCompressedTextureImage3DEXT(final int texture, final int target, final int level, final int internalformat, final int width, final int height, final int depth, final int border, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedTextureImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(data);
        nglCompressedTextureImage3DEXT(texture, target, level, internalformat, width, height, depth, border, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglCompressedTextureImage3DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final long p9, final long p10);
    
    public static void glCompressedTextureImage3DEXT(final int texture, final int target, final int level, final int internalformat, final int width, final int height, final int depth, final int border, final int data_imageSize, final long data_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedTextureImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglCompressedTextureImage3DEXTBO(texture, target, level, internalformat, width, height, depth, border, data_imageSize, data_buffer_offset, function_pointer);
    }
    
    static native void nglCompressedTextureImage3DEXTBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final long p9, final long p10);
    
    public static void glCompressedTextureImage2DEXT(final int texture, final int target, final int level, final int internalformat, final int width, final int height, final int border, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedTextureImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(data);
        nglCompressedTextureImage2DEXT(texture, target, level, internalformat, width, height, border, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglCompressedTextureImage2DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final long p8, final long p9);
    
    public static void glCompressedTextureImage2DEXT(final int texture, final int target, final int level, final int internalformat, final int width, final int height, final int border, final int data_imageSize, final long data_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedTextureImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglCompressedTextureImage2DEXTBO(texture, target, level, internalformat, width, height, border, data_imageSize, data_buffer_offset, function_pointer);
    }
    
    static native void nglCompressedTextureImage2DEXTBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final long p8, final long p9);
    
    public static void glCompressedTextureImage1DEXT(final int texture, final int target, final int level, final int internalformat, final int width, final int border, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedTextureImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(data);
        nglCompressedTextureImage1DEXT(texture, target, level, internalformat, width, border, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglCompressedTextureImage1DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final long p7, final long p8);
    
    public static void glCompressedTextureImage1DEXT(final int texture, final int target, final int level, final int internalformat, final int width, final int border, final int data_imageSize, final long data_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedTextureImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglCompressedTextureImage1DEXTBO(texture, target, level, internalformat, width, border, data_imageSize, data_buffer_offset, function_pointer);
    }
    
    static native void nglCompressedTextureImage1DEXTBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final long p7, final long p8);
    
    public static void glCompressedTextureSubImage3DEXT(final int texture, final int target, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedTextureSubImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(data);
        nglCompressedTextureSubImage3DEXT(texture, target, level, xoffset, yoffset, zoffset, width, height, depth, format, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglCompressedTextureSubImage3DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final int p10, final long p11, final long p12);
    
    public static void glCompressedTextureSubImage3DEXT(final int texture, final int target, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int data_imageSize, final long data_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedTextureSubImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglCompressedTextureSubImage3DEXTBO(texture, target, level, xoffset, yoffset, zoffset, width, height, depth, format, data_imageSize, data_buffer_offset, function_pointer);
    }
    
    static native void nglCompressedTextureSubImage3DEXTBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final int p10, final long p11, final long p12);
    
    public static void glCompressedTextureSubImage2DEXT(final int texture, final int target, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedTextureSubImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(data);
        nglCompressedTextureSubImage2DEXT(texture, target, level, xoffset, yoffset, width, height, format, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglCompressedTextureSubImage2DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final long p9, final long p10);
    
    public static void glCompressedTextureSubImage2DEXT(final int texture, final int target, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final int data_imageSize, final long data_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedTextureSubImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglCompressedTextureSubImage2DEXTBO(texture, target, level, xoffset, yoffset, width, height, format, data_imageSize, data_buffer_offset, function_pointer);
    }
    
    static native void nglCompressedTextureSubImage2DEXTBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final long p9, final long p10);
    
    public static void glCompressedTextureSubImage1DEXT(final int texture, final int target, final int level, final int xoffset, final int width, final int format, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedTextureSubImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(data);
        nglCompressedTextureSubImage1DEXT(texture, target, level, xoffset, width, format, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglCompressedTextureSubImage1DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final long p7, final long p8);
    
    public static void glCompressedTextureSubImage1DEXT(final int texture, final int target, final int level, final int xoffset, final int width, final int format, final int data_imageSize, final long data_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedTextureSubImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglCompressedTextureSubImage1DEXTBO(texture, target, level, xoffset, width, format, data_imageSize, data_buffer_offset, function_pointer);
    }
    
    static native void nglCompressedTextureSubImage1DEXTBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final long p7, final long p8);
    
    public static void glGetCompressedTextureImageEXT(final int texture, final int target, final int level, final ByteBuffer img) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetCompressedTextureImageEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(img);
        nglGetCompressedTextureImageEXT(texture, target, level, MemoryUtil.getAddress(img), function_pointer);
    }
    
    public static void glGetCompressedTextureImageEXT(final int texture, final int target, final int level, final IntBuffer img) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetCompressedTextureImageEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(img);
        nglGetCompressedTextureImageEXT(texture, target, level, MemoryUtil.getAddress(img), function_pointer);
    }
    
    public static void glGetCompressedTextureImageEXT(final int texture, final int target, final int level, final ShortBuffer img) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetCompressedTextureImageEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(img);
        nglGetCompressedTextureImageEXT(texture, target, level, MemoryUtil.getAddress(img), function_pointer);
    }
    
    static native void nglGetCompressedTextureImageEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetCompressedTextureImageEXT(final int texture, final int target, final int level, final long img_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetCompressedTextureImageEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOenabled(caps);
        nglGetCompressedTextureImageEXTBO(texture, target, level, img_buffer_offset, function_pointer);
    }
    
    static native void nglGetCompressedTextureImageEXTBO(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glCompressedMultiTexImage3DEXT(final int texunit, final int target, final int level, final int internalformat, final int width, final int height, final int depth, final int border, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedMultiTexImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(data);
        nglCompressedMultiTexImage3DEXT(texunit, target, level, internalformat, width, height, depth, border, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglCompressedMultiTexImage3DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final long p9, final long p10);
    
    public static void glCompressedMultiTexImage3DEXT(final int texunit, final int target, final int level, final int internalformat, final int width, final int height, final int depth, final int border, final int data_imageSize, final long data_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedMultiTexImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglCompressedMultiTexImage3DEXTBO(texunit, target, level, internalformat, width, height, depth, border, data_imageSize, data_buffer_offset, function_pointer);
    }
    
    static native void nglCompressedMultiTexImage3DEXTBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final long p9, final long p10);
    
    public static void glCompressedMultiTexImage2DEXT(final int texunit, final int target, final int level, final int internalformat, final int width, final int height, final int border, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedMultiTexImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(data);
        nglCompressedMultiTexImage2DEXT(texunit, target, level, internalformat, width, height, border, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglCompressedMultiTexImage2DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final long p8, final long p9);
    
    public static void glCompressedMultiTexImage2DEXT(final int texunit, final int target, final int level, final int internalformat, final int width, final int height, final int border, final int data_imageSize, final long data_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedMultiTexImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglCompressedMultiTexImage2DEXTBO(texunit, target, level, internalformat, width, height, border, data_imageSize, data_buffer_offset, function_pointer);
    }
    
    static native void nglCompressedMultiTexImage2DEXTBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final long p8, final long p9);
    
    public static void glCompressedMultiTexImage1DEXT(final int texunit, final int target, final int level, final int internalformat, final int width, final int border, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedMultiTexImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(data);
        nglCompressedMultiTexImage1DEXT(texunit, target, level, internalformat, width, border, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglCompressedMultiTexImage1DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final long p7, final long p8);
    
    public static void glCompressedMultiTexImage1DEXT(final int texunit, final int target, final int level, final int internalformat, final int width, final int border, final int data_imageSize, final long data_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedMultiTexImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglCompressedMultiTexImage1DEXTBO(texunit, target, level, internalformat, width, border, data_imageSize, data_buffer_offset, function_pointer);
    }
    
    static native void nglCompressedMultiTexImage1DEXTBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final long p7, final long p8);
    
    public static void glCompressedMultiTexSubImage3DEXT(final int texunit, final int target, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedMultiTexSubImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(data);
        nglCompressedMultiTexSubImage3DEXT(texunit, target, level, xoffset, yoffset, zoffset, width, height, depth, format, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglCompressedMultiTexSubImage3DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final int p10, final long p11, final long p12);
    
    public static void glCompressedMultiTexSubImage3DEXT(final int texunit, final int target, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int data_imageSize, final long data_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedMultiTexSubImage3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglCompressedMultiTexSubImage3DEXTBO(texunit, target, level, xoffset, yoffset, zoffset, width, height, depth, format, data_imageSize, data_buffer_offset, function_pointer);
    }
    
    static native void nglCompressedMultiTexSubImage3DEXTBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final int p10, final long p11, final long p12);
    
    public static void glCompressedMultiTexSubImage2DEXT(final int texunit, final int target, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedMultiTexSubImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(data);
        nglCompressedMultiTexSubImage2DEXT(texunit, target, level, xoffset, yoffset, width, height, format, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglCompressedMultiTexSubImage2DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final long p9, final long p10);
    
    public static void glCompressedMultiTexSubImage2DEXT(final int texunit, final int target, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final int data_imageSize, final long data_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedMultiTexSubImage2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglCompressedMultiTexSubImage2DEXTBO(texunit, target, level, xoffset, yoffset, width, height, format, data_imageSize, data_buffer_offset, function_pointer);
    }
    
    static native void nglCompressedMultiTexSubImage2DEXTBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final long p9, final long p10);
    
    public static void glCompressedMultiTexSubImage1DEXT(final int texunit, final int target, final int level, final int xoffset, final int width, final int format, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedMultiTexSubImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(data);
        nglCompressedMultiTexSubImage1DEXT(texunit, target, level, xoffset, width, format, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglCompressedMultiTexSubImage1DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final long p7, final long p8);
    
    public static void glCompressedMultiTexSubImage1DEXT(final int texunit, final int target, final int level, final int xoffset, final int width, final int format, final int data_imageSize, final long data_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedMultiTexSubImage1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglCompressedMultiTexSubImage1DEXTBO(texunit, target, level, xoffset, width, format, data_imageSize, data_buffer_offset, function_pointer);
    }
    
    static native void nglCompressedMultiTexSubImage1DEXTBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final long p7, final long p8);
    
    public static void glGetCompressedMultiTexImageEXT(final int texunit, final int target, final int level, final ByteBuffer img) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetCompressedMultiTexImageEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(img);
        nglGetCompressedMultiTexImageEXT(texunit, target, level, MemoryUtil.getAddress(img), function_pointer);
    }
    
    public static void glGetCompressedMultiTexImageEXT(final int texunit, final int target, final int level, final IntBuffer img) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetCompressedMultiTexImageEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(img);
        nglGetCompressedMultiTexImageEXT(texunit, target, level, MemoryUtil.getAddress(img), function_pointer);
    }
    
    public static void glGetCompressedMultiTexImageEXT(final int texunit, final int target, final int level, final ShortBuffer img) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetCompressedMultiTexImageEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(img);
        nglGetCompressedMultiTexImageEXT(texunit, target, level, MemoryUtil.getAddress(img), function_pointer);
    }
    
    static native void nglGetCompressedMultiTexImageEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetCompressedMultiTexImageEXT(final int texunit, final int target, final int level, final long img_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetCompressedMultiTexImageEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOenabled(caps);
        nglGetCompressedMultiTexImageEXTBO(texunit, target, level, img_buffer_offset, function_pointer);
    }
    
    static native void nglGetCompressedMultiTexImageEXTBO(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glMatrixLoadTransposeEXT(final int matrixMode, final FloatBuffer m) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMatrixLoadTransposefEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(m, 16);
        nglMatrixLoadTransposefEXT(matrixMode, MemoryUtil.getAddress(m), function_pointer);
    }
    
    static native void nglMatrixLoadTransposefEXT(final int p0, final long p1, final long p2);
    
    public static void glMatrixLoadTransposeEXT(final int matrixMode, final DoubleBuffer m) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMatrixLoadTransposedEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(m, 16);
        nglMatrixLoadTransposedEXT(matrixMode, MemoryUtil.getAddress(m), function_pointer);
    }
    
    static native void nglMatrixLoadTransposedEXT(final int p0, final long p1, final long p2);
    
    public static void glMatrixMultTransposeEXT(final int matrixMode, final FloatBuffer m) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMatrixMultTransposefEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(m, 16);
        nglMatrixMultTransposefEXT(matrixMode, MemoryUtil.getAddress(m), function_pointer);
    }
    
    static native void nglMatrixMultTransposefEXT(final int p0, final long p1, final long p2);
    
    public static void glMatrixMultTransposeEXT(final int matrixMode, final DoubleBuffer m) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMatrixMultTransposedEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(m, 16);
        nglMatrixMultTransposedEXT(matrixMode, MemoryUtil.getAddress(m), function_pointer);
    }
    
    static native void nglMatrixMultTransposedEXT(final int p0, final long p1, final long p2);
    
    public static void glNamedBufferDataEXT(final int buffer, final long data_size, final int usage) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferDataEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNamedBufferDataEXT(buffer, data_size, 0L, usage, function_pointer);
    }
    
    public static void glNamedBufferDataEXT(final int buffer, final ByteBuffer data, final int usage) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferDataEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferDataEXT(buffer, data.remaining(), MemoryUtil.getAddress(data), usage, function_pointer);
    }
    
    public static void glNamedBufferDataEXT(final int buffer, final DoubleBuffer data, final int usage) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferDataEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferDataEXT(buffer, data.remaining() << 3, MemoryUtil.getAddress(data), usage, function_pointer);
    }
    
    public static void glNamedBufferDataEXT(final int buffer, final FloatBuffer data, final int usage) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferDataEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferDataEXT(buffer, data.remaining() << 2, MemoryUtil.getAddress(data), usage, function_pointer);
    }
    
    public static void glNamedBufferDataEXT(final int buffer, final IntBuffer data, final int usage) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferDataEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferDataEXT(buffer, data.remaining() << 2, MemoryUtil.getAddress(data), usage, function_pointer);
    }
    
    public static void glNamedBufferDataEXT(final int buffer, final ShortBuffer data, final int usage) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferDataEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferDataEXT(buffer, data.remaining() << 1, MemoryUtil.getAddress(data), usage, function_pointer);
    }
    
    static native void nglNamedBufferDataEXT(final int p0, final long p1, final long p2, final int p3, final long p4);
    
    public static void glNamedBufferSubDataEXT(final int buffer, final long offset, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferSubDataEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferSubDataEXT(buffer, offset, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glNamedBufferSubDataEXT(final int buffer, final long offset, final DoubleBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferSubDataEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferSubDataEXT(buffer, offset, data.remaining() << 3, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glNamedBufferSubDataEXT(final int buffer, final long offset, final FloatBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferSubDataEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferSubDataEXT(buffer, offset, data.remaining() << 2, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glNamedBufferSubDataEXT(final int buffer, final long offset, final IntBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferSubDataEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferSubDataEXT(buffer, offset, data.remaining() << 2, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glNamedBufferSubDataEXT(final int buffer, final long offset, final ShortBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedBufferSubDataEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglNamedBufferSubDataEXT(buffer, offset, data.remaining() << 1, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglNamedBufferSubDataEXT(final int p0, final long p1, final long p2, final long p3, final long p4);
    
    public static ByteBuffer glMapNamedBufferEXT(final int buffer, final int access, final ByteBuffer old_buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMapNamedBufferEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (old_buffer != null) {
            BufferChecks.checkDirect(old_buffer);
        }
        final ByteBuffer __result = nglMapNamedBufferEXT(buffer, access, glGetNamedBufferParameterEXT(buffer, 34660), old_buffer, function_pointer);
        return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
    }
    
    public static ByteBuffer glMapNamedBufferEXT(final int buffer, final int access, final long length, final ByteBuffer old_buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMapNamedBufferEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (old_buffer != null) {
            BufferChecks.checkDirect(old_buffer);
        }
        final ByteBuffer __result = nglMapNamedBufferEXT(buffer, access, length, old_buffer, function_pointer);
        return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
    }
    
    static native ByteBuffer nglMapNamedBufferEXT(final int p0, final int p1, final long p2, final ByteBuffer p3, final long p4);
    
    public static boolean glUnmapNamedBufferEXT(final int buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUnmapNamedBufferEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglUnmapNamedBufferEXT(buffer, function_pointer);
        return __result;
    }
    
    static native boolean nglUnmapNamedBufferEXT(final int p0, final long p1);
    
    public static void glGetNamedBufferParameterEXT(final int buffer, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedBufferParameterivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetNamedBufferParameterivEXT(buffer, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetNamedBufferParameterivEXT(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetNamedBufferParameterEXT(final int buffer, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedBufferParameterivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetNamedBufferParameterivEXT(buffer, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static ByteBuffer glGetNamedBufferPointerEXT(final int buffer, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedBufferPointervEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final ByteBuffer __result = nglGetNamedBufferPointervEXT(buffer, pname, glGetNamedBufferParameterEXT(buffer, 34660), function_pointer);
        return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
    }
    
    static native ByteBuffer nglGetNamedBufferPointervEXT(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetNamedBufferSubDataEXT(final int buffer, final long offset, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedBufferSubDataEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglGetNamedBufferSubDataEXT(buffer, offset, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glGetNamedBufferSubDataEXT(final int buffer, final long offset, final DoubleBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedBufferSubDataEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglGetNamedBufferSubDataEXT(buffer, offset, data.remaining() << 3, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glGetNamedBufferSubDataEXT(final int buffer, final long offset, final FloatBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedBufferSubDataEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglGetNamedBufferSubDataEXT(buffer, offset, data.remaining() << 2, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glGetNamedBufferSubDataEXT(final int buffer, final long offset, final IntBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedBufferSubDataEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglGetNamedBufferSubDataEXT(buffer, offset, data.remaining() << 2, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glGetNamedBufferSubDataEXT(final int buffer, final long offset, final ShortBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedBufferSubDataEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglGetNamedBufferSubDataEXT(buffer, offset, data.remaining() << 1, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglGetNamedBufferSubDataEXT(final int p0, final long p1, final long p2, final long p3, final long p4);
    
    public static void glProgramUniform1fEXT(final int program, final int location, final float v0) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform1fEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform1fEXT(program, location, v0, function_pointer);
    }
    
    static native void nglProgramUniform1fEXT(final int p0, final int p1, final float p2, final long p3);
    
    public static void glProgramUniform2fEXT(final int program, final int location, final float v0, final float v1) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform2fEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform2fEXT(program, location, v0, v1, function_pointer);
    }
    
    static native void nglProgramUniform2fEXT(final int p0, final int p1, final float p2, final float p3, final long p4);
    
    public static void glProgramUniform3fEXT(final int program, final int location, final float v0, final float v1, final float v2) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform3fEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform3fEXT(program, location, v0, v1, v2, function_pointer);
    }
    
    static native void nglProgramUniform3fEXT(final int p0, final int p1, final float p2, final float p3, final float p4, final long p5);
    
    public static void glProgramUniform4fEXT(final int program, final int location, final float v0, final float v1, final float v2, final float v3) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform4fEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform4fEXT(program, location, v0, v1, v2, v3, function_pointer);
    }
    
    static native void nglProgramUniform4fEXT(final int p0, final int p1, final float p2, final float p3, final float p4, final float p5, final long p6);
    
    public static void glProgramUniform1iEXT(final int program, final int location, final int v0) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform1iEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform1iEXT(program, location, v0, function_pointer);
    }
    
    static native void nglProgramUniform1iEXT(final int p0, final int p1, final int p2, final long p3);
    
    public static void glProgramUniform2iEXT(final int program, final int location, final int v0, final int v1) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform2iEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform2iEXT(program, location, v0, v1, function_pointer);
    }
    
    static native void nglProgramUniform2iEXT(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glProgramUniform3iEXT(final int program, final int location, final int v0, final int v1, final int v2) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform3iEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform3iEXT(program, location, v0, v1, v2, function_pointer);
    }
    
    static native void nglProgramUniform3iEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glProgramUniform4iEXT(final int program, final int location, final int v0, final int v1, final int v2, final int v3) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform4iEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform4iEXT(program, location, v0, v1, v2, v3, function_pointer);
    }
    
    static native void nglProgramUniform4iEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6);
    
    public static void glProgramUniform1EXT(final int program, final int location, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform1fvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform1fvEXT(program, location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform1fvEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform2EXT(final int program, final int location, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform2fvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform2fvEXT(program, location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform2fvEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform3EXT(final int program, final int location, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform3fvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform3fvEXT(program, location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform3fvEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform4EXT(final int program, final int location, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform4fvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform4fvEXT(program, location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform4fvEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform1EXT(final int program, final int location, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform1ivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform1ivEXT(program, location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform1ivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform2EXT(final int program, final int location, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform2ivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform2ivEXT(program, location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform2ivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform3EXT(final int program, final int location, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform3ivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform3ivEXT(program, location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform3ivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform4EXT(final int program, final int location, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform4ivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform4ivEXT(program, location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform4ivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniformMatrix2EXT(final int program, final int location, final boolean transpose, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix2fvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix2fvEXT(program, location, value.remaining() >> 2, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix2fvEXT(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix3EXT(final int program, final int location, final boolean transpose, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix3fvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix3fvEXT(program, location, value.remaining() / 9, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix3fvEXT(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix4EXT(final int program, final int location, final boolean transpose, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix4fvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix4fvEXT(program, location, value.remaining() >> 4, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix4fvEXT(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix2x3EXT(final int program, final int location, final boolean transpose, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix2x3fvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix2x3fvEXT(program, location, value.remaining() / 6, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix2x3fvEXT(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix3x2EXT(final int program, final int location, final boolean transpose, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix3x2fvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix3x2fvEXT(program, location, value.remaining() / 6, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix3x2fvEXT(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix2x4EXT(final int program, final int location, final boolean transpose, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix2x4fvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix2x4fvEXT(program, location, value.remaining() >> 3, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix2x4fvEXT(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix4x2EXT(final int program, final int location, final boolean transpose, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix4x2fvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix4x2fvEXT(program, location, value.remaining() >> 3, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix4x2fvEXT(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix3x4EXT(final int program, final int location, final boolean transpose, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix3x4fvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix3x4fvEXT(program, location, value.remaining() / 12, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix3x4fvEXT(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix4x3EXT(final int program, final int location, final boolean transpose, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix4x3fvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix4x3fvEXT(program, location, value.remaining() / 12, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix4x3fvEXT(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glTextureBufferEXT(final int texture, final int target, final int internalformat, final int buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureBufferEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTextureBufferEXT(texture, target, internalformat, buffer, function_pointer);
    }
    
    static native void nglTextureBufferEXT(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glMultiTexBufferEXT(final int texunit, final int target, final int internalformat, final int buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexBufferEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexBufferEXT(texunit, target, internalformat, buffer, function_pointer);
    }
    
    static native void nglMultiTexBufferEXT(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glTextureParameterIEXT(final int texture, final int target, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureParameterIivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglTextureParameterIivEXT(texture, target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglTextureParameterIivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glTextureParameterIEXT(final int texture, final int target, final int pname, final int param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureParameterIivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTextureParameterIivEXT(texture, target, pname, APIUtil.getInt(caps, param), function_pointer);
    }
    
    public static void glTextureParameterIuEXT(final int texture, final int target, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureParameterIuivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglTextureParameterIuivEXT(texture, target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglTextureParameterIuivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glTextureParameterIuEXT(final int texture, final int target, final int pname, final int param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureParameterIuivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTextureParameterIuivEXT(texture, target, pname, APIUtil.getInt(caps, param), function_pointer);
    }
    
    public static void glGetTextureParameterIEXT(final int texture, final int target, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureParameterIivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetTextureParameterIivEXT(texture, target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetTextureParameterIivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static int glGetTextureParameterIiEXT(final int texture, final int target, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureParameterIivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetTextureParameterIivEXT(texture, target, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetTextureParameterIuEXT(final int texture, final int target, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureParameterIuivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetTextureParameterIuivEXT(texture, target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetTextureParameterIuivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static int glGetTextureParameterIuiEXT(final int texture, final int target, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTextureParameterIuivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetTextureParameterIuivEXT(texture, target, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glMultiTexParameterIEXT(final int texunit, final int target, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexParameterIivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglMultiTexParameterIivEXT(texunit, target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglMultiTexParameterIivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glMultiTexParameterIEXT(final int texunit, final int target, final int pname, final int param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexParameterIivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexParameterIivEXT(texunit, target, pname, APIUtil.getInt(caps, param), function_pointer);
    }
    
    public static void glMultiTexParameterIuEXT(final int texunit, final int target, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexParameterIuivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglMultiTexParameterIuivEXT(texunit, target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglMultiTexParameterIuivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glMultiTexParameterIuEXT(final int texunit, final int target, final int pname, final int param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexParameterIuivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexParameterIuivEXT(texunit, target, pname, APIUtil.getInt(caps, param), function_pointer);
    }
    
    public static void glGetMultiTexParameterIEXT(final int texunit, final int target, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMultiTexParameterIivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetMultiTexParameterIivEXT(texunit, target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetMultiTexParameterIivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static int glGetMultiTexParameterIiEXT(final int texunit, final int target, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMultiTexParameterIivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetMultiTexParameterIivEXT(texunit, target, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetMultiTexParameterIuEXT(final int texunit, final int target, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMultiTexParameterIuivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetMultiTexParameterIuivEXT(texunit, target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetMultiTexParameterIuivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static int glGetMultiTexParameterIuiEXT(final int texunit, final int target, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMultiTexParameterIuivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetMultiTexParameterIuivEXT(texunit, target, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glProgramUniform1uiEXT(final int program, final int location, final int v0) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform1uiEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform1uiEXT(program, location, v0, function_pointer);
    }
    
    static native void nglProgramUniform1uiEXT(final int p0, final int p1, final int p2, final long p3);
    
    public static void glProgramUniform2uiEXT(final int program, final int location, final int v0, final int v1) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform2uiEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform2uiEXT(program, location, v0, v1, function_pointer);
    }
    
    static native void nglProgramUniform2uiEXT(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glProgramUniform3uiEXT(final int program, final int location, final int v0, final int v1, final int v2) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform3uiEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform3uiEXT(program, location, v0, v1, v2, function_pointer);
    }
    
    static native void nglProgramUniform3uiEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glProgramUniform4uiEXT(final int program, final int location, final int v0, final int v1, final int v2, final int v3) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform4uiEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform4uiEXT(program, location, v0, v1, v2, v3, function_pointer);
    }
    
    static native void nglProgramUniform4uiEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6);
    
    public static void glProgramUniform1uEXT(final int program, final int location, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform1uivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform1uivEXT(program, location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform1uivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform2uEXT(final int program, final int location, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform2uivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform2uivEXT(program, location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform2uivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform3uEXT(final int program, final int location, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform3uivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform3uivEXT(program, location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform3uivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform4uEXT(final int program, final int location, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform4uivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform4uivEXT(program, location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform4uivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glNamedProgramLocalParameters4EXT(final int program, final int target, final int index, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedProgramLocalParameters4fvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglNamedProgramLocalParameters4fvEXT(program, target, index, params.remaining() >> 2, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglNamedProgramLocalParameters4fvEXT(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glNamedProgramLocalParameterI4iEXT(final int program, final int target, final int index, final int x, final int y, final int z, final int w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedProgramLocalParameterI4iEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNamedProgramLocalParameterI4iEXT(program, target, index, x, y, z, w, function_pointer);
    }
    
    static native void nglNamedProgramLocalParameterI4iEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final long p7);
    
    public static void glNamedProgramLocalParameterI4EXT(final int program, final int target, final int index, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedProgramLocalParameterI4ivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglNamedProgramLocalParameterI4ivEXT(program, target, index, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglNamedProgramLocalParameterI4ivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glNamedProgramLocalParametersI4EXT(final int program, final int target, final int index, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedProgramLocalParametersI4ivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglNamedProgramLocalParametersI4ivEXT(program, target, index, params.remaining() >> 2, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglNamedProgramLocalParametersI4ivEXT(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glNamedProgramLocalParameterI4uiEXT(final int program, final int target, final int index, final int x, final int y, final int z, final int w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedProgramLocalParameterI4uiEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNamedProgramLocalParameterI4uiEXT(program, target, index, x, y, z, w, function_pointer);
    }
    
    static native void nglNamedProgramLocalParameterI4uiEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final long p7);
    
    public static void glNamedProgramLocalParameterI4uEXT(final int program, final int target, final int index, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedProgramLocalParameterI4uivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglNamedProgramLocalParameterI4uivEXT(program, target, index, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglNamedProgramLocalParameterI4uivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glNamedProgramLocalParametersI4uEXT(final int program, final int target, final int index, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedProgramLocalParametersI4uivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglNamedProgramLocalParametersI4uivEXT(program, target, index, params.remaining() >> 2, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglNamedProgramLocalParametersI4uivEXT(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glGetNamedProgramLocalParameterIEXT(final int program, final int target, final int index, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedProgramLocalParameterIivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetNamedProgramLocalParameterIivEXT(program, target, index, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetNamedProgramLocalParameterIivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetNamedProgramLocalParameterIuEXT(final int program, final int target, final int index, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedProgramLocalParameterIuivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetNamedProgramLocalParameterIuivEXT(program, target, index, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetNamedProgramLocalParameterIuivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glNamedRenderbufferStorageEXT(final int renderbuffer, final int internalformat, final int width, final int height) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedRenderbufferStorageEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNamedRenderbufferStorageEXT(renderbuffer, internalformat, width, height, function_pointer);
    }
    
    static native void nglNamedRenderbufferStorageEXT(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glGetNamedRenderbufferParameterEXT(final int renderbuffer, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedRenderbufferParameterivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetNamedRenderbufferParameterivEXT(renderbuffer, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetNamedRenderbufferParameterivEXT(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetNamedRenderbufferParameterEXT(final int renderbuffer, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedRenderbufferParameterivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetNamedRenderbufferParameterivEXT(renderbuffer, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glNamedRenderbufferStorageMultisampleEXT(final int renderbuffer, final int samples, final int internalformat, final int width, final int height) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedRenderbufferStorageMultisampleEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNamedRenderbufferStorageMultisampleEXT(renderbuffer, samples, internalformat, width, height, function_pointer);
    }
    
    static native void nglNamedRenderbufferStorageMultisampleEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glNamedRenderbufferStorageMultisampleCoverageEXT(final int renderbuffer, final int coverageSamples, final int colorSamples, final int internalformat, final int width, final int height) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedRenderbufferStorageMultisampleCoverageEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNamedRenderbufferStorageMultisampleCoverageEXT(renderbuffer, coverageSamples, colorSamples, internalformat, width, height, function_pointer);
    }
    
    static native void nglNamedRenderbufferStorageMultisampleCoverageEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6);
    
    public static int glCheckNamedFramebufferStatusEXT(final int framebuffer, final int target) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCheckNamedFramebufferStatusEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nglCheckNamedFramebufferStatusEXT(framebuffer, target, function_pointer);
        return __result;
    }
    
    static native int nglCheckNamedFramebufferStatusEXT(final int p0, final int p1, final long p2);
    
    public static void glNamedFramebufferTexture1DEXT(final int framebuffer, final int attachment, final int textarget, final int texture, final int level) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedFramebufferTexture1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNamedFramebufferTexture1DEXT(framebuffer, attachment, textarget, texture, level, function_pointer);
    }
    
    static native void nglNamedFramebufferTexture1DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glNamedFramebufferTexture2DEXT(final int framebuffer, final int attachment, final int textarget, final int texture, final int level) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedFramebufferTexture2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNamedFramebufferTexture2DEXT(framebuffer, attachment, textarget, texture, level, function_pointer);
    }
    
    static native void nglNamedFramebufferTexture2DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glNamedFramebufferTexture3DEXT(final int framebuffer, final int attachment, final int textarget, final int texture, final int level, final int zoffset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedFramebufferTexture3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNamedFramebufferTexture3DEXT(framebuffer, attachment, textarget, texture, level, zoffset, function_pointer);
    }
    
    static native void nglNamedFramebufferTexture3DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6);
    
    public static void glNamedFramebufferRenderbufferEXT(final int framebuffer, final int attachment, final int renderbuffertarget, final int renderbuffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedFramebufferRenderbufferEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNamedFramebufferRenderbufferEXT(framebuffer, attachment, renderbuffertarget, renderbuffer, function_pointer);
    }
    
    static native void nglNamedFramebufferRenderbufferEXT(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glGetNamedFramebufferAttachmentParameterEXT(final int framebuffer, final int attachment, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedFramebufferAttachmentParameterivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetNamedFramebufferAttachmentParameterivEXT(framebuffer, attachment, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetNamedFramebufferAttachmentParameterivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static int glGetNamedFramebufferAttachmentParameterEXT(final int framebuffer, final int attachment, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedFramebufferAttachmentParameterivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetNamedFramebufferAttachmentParameterivEXT(framebuffer, attachment, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGenerateTextureMipmapEXT(final int texture, final int target) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenerateTextureMipmapEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglGenerateTextureMipmapEXT(texture, target, function_pointer);
    }
    
    static native void nglGenerateTextureMipmapEXT(final int p0, final int p1, final long p2);
    
    public static void glGenerateMultiTexMipmapEXT(final int texunit, final int target) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenerateMultiTexMipmapEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglGenerateMultiTexMipmapEXT(texunit, target, function_pointer);
    }
    
    static native void nglGenerateMultiTexMipmapEXT(final int p0, final int p1, final long p2);
    
    public static void glFramebufferDrawBufferEXT(final int framebuffer, final int mode) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFramebufferDrawBufferEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglFramebufferDrawBufferEXT(framebuffer, mode, function_pointer);
    }
    
    static native void nglFramebufferDrawBufferEXT(final int p0, final int p1, final long p2);
    
    public static void glFramebufferDrawBuffersEXT(final int framebuffer, final IntBuffer bufs) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFramebufferDrawBuffersEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(bufs);
        nglFramebufferDrawBuffersEXT(framebuffer, bufs.remaining(), MemoryUtil.getAddress(bufs), function_pointer);
    }
    
    static native void nglFramebufferDrawBuffersEXT(final int p0, final int p1, final long p2, final long p3);
    
    public static void glFramebufferReadBufferEXT(final int framebuffer, final int mode) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFramebufferReadBufferEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglFramebufferReadBufferEXT(framebuffer, mode, function_pointer);
    }
    
    static native void nglFramebufferReadBufferEXT(final int p0, final int p1, final long p2);
    
    public static void glGetFramebufferParameterEXT(final int framebuffer, final int pname, final IntBuffer param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetFramebufferParameterivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(param, 4);
        nglGetFramebufferParameterivEXT(framebuffer, pname, MemoryUtil.getAddress(param), function_pointer);
    }
    
    static native void nglGetFramebufferParameterivEXT(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetFramebufferParameterEXT(final int framebuffer, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetFramebufferParameterivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer param = APIUtil.getBufferInt(caps);
        nglGetFramebufferParameterivEXT(framebuffer, pname, MemoryUtil.getAddress(param), function_pointer);
        return param.get(0);
    }
    
    public static void glNamedCopyBufferSubDataEXT(final int readBuffer, final int writeBuffer, final long readoffset, final long writeoffset, final long size) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedCopyBufferSubDataEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNamedCopyBufferSubDataEXT(readBuffer, writeBuffer, readoffset, writeoffset, size, function_pointer);
    }
    
    static native void nglNamedCopyBufferSubDataEXT(final int p0, final int p1, final long p2, final long p3, final long p4, final long p5);
    
    public static void glNamedFramebufferTextureEXT(final int framebuffer, final int attachment, final int texture, final int level) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedFramebufferTextureEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNamedFramebufferTextureEXT(framebuffer, attachment, texture, level, function_pointer);
    }
    
    static native void nglNamedFramebufferTextureEXT(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glNamedFramebufferTextureLayerEXT(final int framebuffer, final int attachment, final int texture, final int level, final int layer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedFramebufferTextureLayerEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNamedFramebufferTextureLayerEXT(framebuffer, attachment, texture, level, layer, function_pointer);
    }
    
    static native void nglNamedFramebufferTextureLayerEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glNamedFramebufferTextureFaceEXT(final int framebuffer, final int attachment, final int texture, final int level, final int face) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedFramebufferTextureFaceEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNamedFramebufferTextureFaceEXT(framebuffer, attachment, texture, level, face, function_pointer);
    }
    
    static native void nglNamedFramebufferTextureFaceEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glTextureRenderbufferEXT(final int texture, final int target, final int renderbuffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureRenderbufferEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTextureRenderbufferEXT(texture, target, renderbuffer, function_pointer);
    }
    
    static native void nglTextureRenderbufferEXT(final int p0, final int p1, final int p2, final long p3);
    
    public static void glMultiTexRenderbufferEXT(final int texunit, final int target, final int renderbuffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexRenderbufferEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexRenderbufferEXT(texunit, target, renderbuffer, function_pointer);
    }
    
    static native void nglMultiTexRenderbufferEXT(final int p0, final int p1, final int p2, final long p3);
    
    public static void glVertexArrayVertexOffsetEXT(final int vaobj, final int buffer, final int size, final int type, final int stride, final long offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexArrayVertexOffsetEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexArrayVertexOffsetEXT(vaobj, buffer, size, type, stride, offset, function_pointer);
    }
    
    static native void nglVertexArrayVertexOffsetEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5, final long p6);
    
    public static void glVertexArrayColorOffsetEXT(final int vaobj, final int buffer, final int size, final int type, final int stride, final long offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexArrayColorOffsetEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexArrayColorOffsetEXT(vaobj, buffer, size, type, stride, offset, function_pointer);
    }
    
    static native void nglVertexArrayColorOffsetEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5, final long p6);
    
    public static void glVertexArrayEdgeFlagOffsetEXT(final int vaobj, final int buffer, final int stride, final long offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexArrayEdgeFlagOffsetEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexArrayEdgeFlagOffsetEXT(vaobj, buffer, stride, offset, function_pointer);
    }
    
    static native void nglVertexArrayEdgeFlagOffsetEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glVertexArrayIndexOffsetEXT(final int vaobj, final int buffer, final int type, final int stride, final long offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexArrayIndexOffsetEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexArrayIndexOffsetEXT(vaobj, buffer, type, stride, offset, function_pointer);
    }
    
    static native void nglVertexArrayIndexOffsetEXT(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glVertexArrayNormalOffsetEXT(final int vaobj, final int buffer, final int type, final int stride, final long offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexArrayNormalOffsetEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexArrayNormalOffsetEXT(vaobj, buffer, type, stride, offset, function_pointer);
    }
    
    static native void nglVertexArrayNormalOffsetEXT(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glVertexArrayTexCoordOffsetEXT(final int vaobj, final int buffer, final int size, final int type, final int stride, final long offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexArrayTexCoordOffsetEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexArrayTexCoordOffsetEXT(vaobj, buffer, size, type, stride, offset, function_pointer);
    }
    
    static native void nglVertexArrayTexCoordOffsetEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5, final long p6);
    
    public static void glVertexArrayMultiTexCoordOffsetEXT(final int vaobj, final int buffer, final int texunit, final int size, final int type, final int stride, final long offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexArrayMultiTexCoordOffsetEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexArrayMultiTexCoordOffsetEXT(vaobj, buffer, texunit, size, type, stride, offset, function_pointer);
    }
    
    static native void nglVertexArrayMultiTexCoordOffsetEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6, final long p7);
    
    public static void glVertexArrayFogCoordOffsetEXT(final int vaobj, final int buffer, final int type, final int stride, final long offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexArrayFogCoordOffsetEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexArrayFogCoordOffsetEXT(vaobj, buffer, type, stride, offset, function_pointer);
    }
    
    static native void nglVertexArrayFogCoordOffsetEXT(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glVertexArraySecondaryColorOffsetEXT(final int vaobj, final int buffer, final int size, final int type, final int stride, final long offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexArraySecondaryColorOffsetEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexArraySecondaryColorOffsetEXT(vaobj, buffer, size, type, stride, offset, function_pointer);
    }
    
    static native void nglVertexArraySecondaryColorOffsetEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5, final long p6);
    
    public static void glVertexArrayVertexAttribOffsetEXT(final int vaobj, final int buffer, final int index, final int size, final int type, final boolean normalized, final int stride, final long offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexArrayVertexAttribOffsetEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexArrayVertexAttribOffsetEXT(vaobj, buffer, index, size, type, normalized, stride, offset, function_pointer);
    }
    
    static native void nglVertexArrayVertexAttribOffsetEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final boolean p5, final int p6, final long p7, final long p8);
    
    public static void glVertexArrayVertexAttribIOffsetEXT(final int vaobj, final int buffer, final int index, final int size, final int type, final int stride, final long offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexArrayVertexAttribIOffsetEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexArrayVertexAttribIOffsetEXT(vaobj, buffer, index, size, type, stride, offset, function_pointer);
    }
    
    static native void nglVertexArrayVertexAttribIOffsetEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6, final long p7);
    
    public static void glEnableVertexArrayEXT(final int vaobj, final int array) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glEnableVertexArrayEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglEnableVertexArrayEXT(vaobj, array, function_pointer);
    }
    
    static native void nglEnableVertexArrayEXT(final int p0, final int p1, final long p2);
    
    public static void glDisableVertexArrayEXT(final int vaobj, final int array) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDisableVertexArrayEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDisableVertexArrayEXT(vaobj, array, function_pointer);
    }
    
    static native void nglDisableVertexArrayEXT(final int p0, final int p1, final long p2);
    
    public static void glEnableVertexArrayAttribEXT(final int vaobj, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glEnableVertexArrayAttribEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglEnableVertexArrayAttribEXT(vaobj, index, function_pointer);
    }
    
    static native void nglEnableVertexArrayAttribEXT(final int p0, final int p1, final long p2);
    
    public static void glDisableVertexArrayAttribEXT(final int vaobj, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDisableVertexArrayAttribEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDisableVertexArrayAttribEXT(vaobj, index, function_pointer);
    }
    
    static native void nglDisableVertexArrayAttribEXT(final int p0, final int p1, final long p2);
    
    public static void glGetVertexArrayIntegerEXT(final int vaobj, final int pname, final IntBuffer param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexArrayIntegervEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(param, 16);
        nglGetVertexArrayIntegervEXT(vaobj, pname, MemoryUtil.getAddress(param), function_pointer);
    }
    
    static native void nglGetVertexArrayIntegervEXT(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetVertexArrayIntegerEXT(final int vaobj, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexArrayIntegervEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer param = APIUtil.getBufferInt(caps);
        nglGetVertexArrayIntegervEXT(vaobj, pname, MemoryUtil.getAddress(param), function_pointer);
        return param.get(0);
    }
    
    public static ByteBuffer glGetVertexArrayPointerEXT(final int vaobj, final int pname, final long result_size) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexArrayPointervEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final ByteBuffer __result = nglGetVertexArrayPointervEXT(vaobj, pname, result_size, function_pointer);
        return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
    }
    
    static native ByteBuffer nglGetVertexArrayPointervEXT(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetVertexArrayIntegerEXT(final int vaobj, final int index, final int pname, final IntBuffer param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexArrayIntegeri_vEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(param, 16);
        nglGetVertexArrayIntegeri_vEXT(vaobj, index, pname, MemoryUtil.getAddress(param), function_pointer);
    }
    
    static native void nglGetVertexArrayIntegeri_vEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static int glGetVertexArrayIntegeriEXT(final int vaobj, final int index, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexArrayIntegeri_vEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer param = APIUtil.getBufferInt(caps);
        nglGetVertexArrayIntegeri_vEXT(vaobj, index, pname, MemoryUtil.getAddress(param), function_pointer);
        return param.get(0);
    }
    
    public static ByteBuffer glGetVertexArrayPointerEXT(final int vaobj, final int index, final int pname, final long result_size) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexArrayPointeri_vEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final ByteBuffer __result = nglGetVertexArrayPointeri_vEXT(vaobj, index, pname, result_size, function_pointer);
        return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
    }
    
    static native ByteBuffer nglGetVertexArrayPointeri_vEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static ByteBuffer glMapNamedBufferRangeEXT(final int buffer, final long offset, final long length, final int access, final ByteBuffer old_buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMapNamedBufferRangeEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (old_buffer != null) {
            BufferChecks.checkDirect(old_buffer);
        }
        final ByteBuffer __result = nglMapNamedBufferRangeEXT(buffer, offset, length, access, old_buffer, function_pointer);
        return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
    }
    
    static native ByteBuffer nglMapNamedBufferRangeEXT(final int p0, final long p1, final long p2, final int p3, final ByteBuffer p4, final long p5);
    
    public static void glFlushMappedNamedBufferRangeEXT(final int buffer, final long offset, final long length) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFlushMappedNamedBufferRangeEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglFlushMappedNamedBufferRangeEXT(buffer, offset, length, function_pointer);
    }
    
    static native void nglFlushMappedNamedBufferRangeEXT(final int p0, final long p1, final long p2, final long p3);
}

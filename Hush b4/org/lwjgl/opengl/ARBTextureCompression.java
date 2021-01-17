// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.ByteBuffer;

public final class ARBTextureCompression
{
    public static final int GL_COMPRESSED_ALPHA_ARB = 34025;
    public static final int GL_COMPRESSED_LUMINANCE_ARB = 34026;
    public static final int GL_COMPRESSED_LUMINANCE_ALPHA_ARB = 34027;
    public static final int GL_COMPRESSED_INTENSITY_ARB = 34028;
    public static final int GL_COMPRESSED_RGB_ARB = 34029;
    public static final int GL_COMPRESSED_RGBA_ARB = 34030;
    public static final int GL_TEXTURE_COMPRESSION_HINT_ARB = 34031;
    public static final int GL_TEXTURE_COMPRESSED_IMAGE_SIZE_ARB = 34464;
    public static final int GL_TEXTURE_COMPRESSED_ARB = 34465;
    public static final int GL_NUM_COMPRESSED_TEXTURE_FORMATS_ARB = 34466;
    public static final int GL_COMPRESSED_TEXTURE_FORMATS_ARB = 34467;
    
    private ARBTextureCompression() {
    }
    
    public static void glCompressedTexImage1DARB(final int target, final int level, final int internalformat, final int width, final int border, final ByteBuffer pData) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedTexImage1DARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(pData);
        nglCompressedTexImage1DARB(target, level, internalformat, width, border, pData.remaining(), MemoryUtil.getAddress(pData), function_pointer);
    }
    
    static native void nglCompressedTexImage1DARB(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6, final long p7);
    
    public static void glCompressedTexImage1DARB(final int target, final int level, final int internalformat, final int width, final int border, final int pData_imageSize, final long pData_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedTexImage1DARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglCompressedTexImage1DARBBO(target, level, internalformat, width, border, pData_imageSize, pData_buffer_offset, function_pointer);
    }
    
    static native void nglCompressedTexImage1DARBBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6, final long p7);
    
    public static void glCompressedTexImage2DARB(final int target, final int level, final int internalformat, final int width, final int height, final int border, final ByteBuffer pData) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedTexImage2DARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(pData);
        nglCompressedTexImage2DARB(target, level, internalformat, width, height, border, pData.remaining(), MemoryUtil.getAddress(pData), function_pointer);
    }
    
    static native void nglCompressedTexImage2DARB(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final long p7, final long p8);
    
    public static void glCompressedTexImage2DARB(final int target, final int level, final int internalformat, final int width, final int height, final int border, final int pData_imageSize, final long pData_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedTexImage2DARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglCompressedTexImage2DARBBO(target, level, internalformat, width, height, border, pData_imageSize, pData_buffer_offset, function_pointer);
    }
    
    static native void nglCompressedTexImage2DARBBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final long p7, final long p8);
    
    public static void glCompressedTexImage3DARB(final int target, final int level, final int internalformat, final int width, final int height, final int depth, final int border, final ByteBuffer pData) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedTexImage3DARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(pData);
        nglCompressedTexImage3DARB(target, level, internalformat, width, height, depth, border, pData.remaining(), MemoryUtil.getAddress(pData), function_pointer);
    }
    
    static native void nglCompressedTexImage3DARB(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final long p8, final long p9);
    
    public static void glCompressedTexImage3DARB(final int target, final int level, final int internalformat, final int width, final int height, final int depth, final int border, final int pData_imageSize, final long pData_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedTexImage3DARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglCompressedTexImage3DARBBO(target, level, internalformat, width, height, depth, border, pData_imageSize, pData_buffer_offset, function_pointer);
    }
    
    static native void nglCompressedTexImage3DARBBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final long p8, final long p9);
    
    public static void glCompressedTexSubImage1DARB(final int target, final int level, final int xoffset, final int width, final int format, final ByteBuffer pData) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedTexSubImage1DARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(pData);
        nglCompressedTexSubImage1DARB(target, level, xoffset, width, format, pData.remaining(), MemoryUtil.getAddress(pData), function_pointer);
    }
    
    static native void nglCompressedTexSubImage1DARB(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6, final long p7);
    
    public static void glCompressedTexSubImage1DARB(final int target, final int level, final int xoffset, final int width, final int format, final int pData_imageSize, final long pData_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedTexSubImage1DARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglCompressedTexSubImage1DARBBO(target, level, xoffset, width, format, pData_imageSize, pData_buffer_offset, function_pointer);
    }
    
    static native void nglCompressedTexSubImage1DARBBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6, final long p7);
    
    public static void glCompressedTexSubImage2DARB(final int target, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final ByteBuffer pData) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedTexSubImage2DARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(pData);
        nglCompressedTexSubImage2DARB(target, level, xoffset, yoffset, width, height, format, pData.remaining(), MemoryUtil.getAddress(pData), function_pointer);
    }
    
    static native void nglCompressedTexSubImage2DARB(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final long p8, final long p9);
    
    public static void glCompressedTexSubImage2DARB(final int target, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final int pData_imageSize, final long pData_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedTexSubImage2DARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglCompressedTexSubImage2DARBBO(target, level, xoffset, yoffset, width, height, format, pData_imageSize, pData_buffer_offset, function_pointer);
    }
    
    static native void nglCompressedTexSubImage2DARBBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final long p8, final long p9);
    
    public static void glCompressedTexSubImage3DARB(final int target, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final ByteBuffer pData) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedTexSubImage3DARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(pData);
        nglCompressedTexSubImage3DARB(target, level, xoffset, yoffset, zoffset, width, height, depth, format, pData.remaining(), MemoryUtil.getAddress(pData), function_pointer);
    }
    
    static native void nglCompressedTexSubImage3DARB(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final long p10, final long p11);
    
    public static void glCompressedTexSubImage3DARB(final int target, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int pData_imageSize, final long pData_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompressedTexSubImage3DARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglCompressedTexSubImage3DARBBO(target, level, xoffset, yoffset, zoffset, width, height, depth, format, pData_imageSize, pData_buffer_offset, function_pointer);
    }
    
    static native void nglCompressedTexSubImage3DARBBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final long p10, final long p11);
    
    public static void glGetCompressedTexImageARB(final int target, final int lod, final ByteBuffer pImg) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetCompressedTexImageARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(pImg);
        nglGetCompressedTexImageARB(target, lod, MemoryUtil.getAddress(pImg), function_pointer);
    }
    
    static native void nglGetCompressedTexImageARB(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetCompressedTexImageARB(final int target, final int lod, final long pImg_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetCompressedTexImageARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOenabled(caps);
        nglGetCompressedTexImageARBBO(target, lod, pImg_buffer_offset, function_pointer);
    }
    
    static native void nglGetCompressedTexImageARBBO(final int p0, final int p1, final long p2, final long p3);
}

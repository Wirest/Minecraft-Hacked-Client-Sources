// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.PointerBuffer;
import java.nio.ShortBuffer;
import java.nio.FloatBuffer;
import java.nio.DoubleBuffer;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.nio.IntBuffer;

public final class ARBDirectStateAccess
{
    public static final int GL_TEXTURE_TARGET = 4102;
    public static final int GL_QUERY_TARGET = 33514;
    public static final int GL_TEXTURE_BINDING = 33515;
    
    private ARBDirectStateAccess() {
    }
    
    public static void glCreateTransformFeedbacks(final IntBuffer ids) {
        GL45.glCreateTransformFeedbacks(ids);
    }
    
    public static int glCreateTransformFeedbacks() {
        return GL45.glCreateTransformFeedbacks();
    }
    
    public static void glTransformFeedbackBufferBase(final int xfb, final int index, final int buffer) {
        GL45.glTransformFeedbackBufferBase(xfb, index, buffer);
    }
    
    public static void glTransformFeedbackBufferRange(final int xfb, final int index, final int buffer, final long offset, final long size) {
        GL45.glTransformFeedbackBufferRange(xfb, index, buffer, offset, size);
    }
    
    public static void glGetTransformFeedback(final int xfb, final int pname, final IntBuffer param) {
        GL45.glGetTransformFeedback(xfb, pname, param);
    }
    
    public static int glGetTransformFeedbacki(final int xfb, final int pname) {
        return GL45.glGetTransformFeedbacki(xfb, pname);
    }
    
    public static void glGetTransformFeedback(final int xfb, final int pname, final int index, final IntBuffer param) {
        GL45.glGetTransformFeedback(xfb, pname, index, param);
    }
    
    public static int glGetTransformFeedbacki(final int xfb, final int pname, final int index) {
        return GL45.glGetTransformFeedbacki(xfb, pname, index);
    }
    
    public static void glGetTransformFeedback(final int xfb, final int pname, final int index, final LongBuffer param) {
        GL45.glGetTransformFeedback(xfb, pname, index, param);
    }
    
    public static long glGetTransformFeedbacki64(final int xfb, final int pname, final int index) {
        return GL45.glGetTransformFeedbacki64(xfb, pname, index);
    }
    
    public static void glCreateBuffers(final IntBuffer buffers) {
        GL45.glCreateBuffers(buffers);
    }
    
    public static int glCreateBuffers() {
        return GL45.glCreateBuffers();
    }
    
    public static void glNamedBufferStorage(final int buffer, final ByteBuffer data, final int flags) {
        GL45.glNamedBufferStorage(buffer, data, flags);
    }
    
    public static void glNamedBufferStorage(final int buffer, final DoubleBuffer data, final int flags) {
        GL45.glNamedBufferStorage(buffer, data, flags);
    }
    
    public static void glNamedBufferStorage(final int buffer, final FloatBuffer data, final int flags) {
        GL45.glNamedBufferStorage(buffer, data, flags);
    }
    
    public static void glNamedBufferStorage(final int buffer, final IntBuffer data, final int flags) {
        GL45.glNamedBufferStorage(buffer, data, flags);
    }
    
    public static void glNamedBufferStorage(final int buffer, final ShortBuffer data, final int flags) {
        GL45.glNamedBufferStorage(buffer, data, flags);
    }
    
    public static void glNamedBufferStorage(final int buffer, final LongBuffer data, final int flags) {
        GL45.glNamedBufferStorage(buffer, data, flags);
    }
    
    public static void glNamedBufferStorage(final int buffer, final long size, final int flags) {
        GL45.glNamedBufferStorage(buffer, size, flags);
    }
    
    public static void glNamedBufferData(final int buffer, final long data_size, final int usage) {
        GL45.glNamedBufferData(buffer, data_size, usage);
    }
    
    public static void glNamedBufferData(final int buffer, final ByteBuffer data, final int usage) {
        GL45.glNamedBufferData(buffer, data, usage);
    }
    
    public static void glNamedBufferData(final int buffer, final DoubleBuffer data, final int usage) {
        GL45.glNamedBufferData(buffer, data, usage);
    }
    
    public static void glNamedBufferData(final int buffer, final FloatBuffer data, final int usage) {
        GL45.glNamedBufferData(buffer, data, usage);
    }
    
    public static void glNamedBufferData(final int buffer, final IntBuffer data, final int usage) {
        GL45.glNamedBufferData(buffer, data, usage);
    }
    
    public static void glNamedBufferData(final int buffer, final ShortBuffer data, final int usage) {
        GL45.glNamedBufferData(buffer, data, usage);
    }
    
    public static void glNamedBufferSubData(final int buffer, final long offset, final ByteBuffer data) {
        GL45.glNamedBufferSubData(buffer, offset, data);
    }
    
    public static void glNamedBufferSubData(final int buffer, final long offset, final DoubleBuffer data) {
        GL45.glNamedBufferSubData(buffer, offset, data);
    }
    
    public static void glNamedBufferSubData(final int buffer, final long offset, final FloatBuffer data) {
        GL45.glNamedBufferSubData(buffer, offset, data);
    }
    
    public static void glNamedBufferSubData(final int buffer, final long offset, final IntBuffer data) {
        GL45.glNamedBufferSubData(buffer, offset, data);
    }
    
    public static void glNamedBufferSubData(final int buffer, final long offset, final ShortBuffer data) {
        GL45.glNamedBufferSubData(buffer, offset, data);
    }
    
    public static void glCopyNamedBufferSubData(final int readBuffer, final int writeBuffer, final long readOffset, final long writeOffset, final long size) {
        GL45.glCopyNamedBufferSubData(readBuffer, writeBuffer, readOffset, writeOffset, size);
    }
    
    public static void glClearNamedBufferData(final int buffer, final int internalformat, final int format, final int type, final ByteBuffer data) {
        GL45.glClearNamedBufferData(buffer, internalformat, format, type, data);
    }
    
    public static void glClearNamedBufferSubData(final int buffer, final int internalformat, final long offset, final long size, final int format, final int type, final ByteBuffer data) {
        GL45.glClearNamedBufferSubData(buffer, internalformat, offset, size, format, type, data);
    }
    
    public static ByteBuffer glMapNamedBuffer(final int buffer, final int access, final ByteBuffer old_buffer) {
        return GL45.glMapNamedBuffer(buffer, access, old_buffer);
    }
    
    public static ByteBuffer glMapNamedBuffer(final int buffer, final int access, final long length, final ByteBuffer old_buffer) {
        return GL45.glMapNamedBuffer(buffer, access, length, old_buffer);
    }
    
    public static ByteBuffer glMapNamedBufferRange(final int buffer, final long offset, final long length, final int access, final ByteBuffer old_buffer) {
        return GL45.glMapNamedBufferRange(buffer, offset, length, access, old_buffer);
    }
    
    public static boolean glUnmapNamedBuffer(final int buffer) {
        return GL45.glUnmapNamedBuffer(buffer);
    }
    
    public static void glFlushMappedNamedBufferRange(final int buffer, final long offset, final long length) {
        GL45.glFlushMappedNamedBufferRange(buffer, offset, length);
    }
    
    public static void glGetNamedBufferParameter(final int buffer, final int pname, final IntBuffer params) {
        GL45.glGetNamedBufferParameter(buffer, pname, params);
    }
    
    public static int glGetNamedBufferParameteri(final int buffer, final int pname) {
        return GL45.glGetNamedBufferParameteri(buffer, pname);
    }
    
    public static void glGetNamedBufferParameter(final int buffer, final int pname, final LongBuffer params) {
        GL45.glGetNamedBufferParameter(buffer, pname, params);
    }
    
    public static long glGetNamedBufferParameteri64(final int buffer, final int pname) {
        return GL45.glGetNamedBufferParameteri64(buffer, pname);
    }
    
    public static ByteBuffer glGetNamedBufferPointer(final int buffer, final int pname) {
        return GL45.glGetNamedBufferPointer(buffer, pname);
    }
    
    public static void glGetNamedBufferSubData(final int buffer, final long offset, final ByteBuffer data) {
        GL45.glGetNamedBufferSubData(buffer, offset, data);
    }
    
    public static void glGetNamedBufferSubData(final int buffer, final long offset, final DoubleBuffer data) {
        GL45.glGetNamedBufferSubData(buffer, offset, data);
    }
    
    public static void glGetNamedBufferSubData(final int buffer, final long offset, final FloatBuffer data) {
        GL45.glGetNamedBufferSubData(buffer, offset, data);
    }
    
    public static void glGetNamedBufferSubData(final int buffer, final long offset, final IntBuffer data) {
        GL45.glGetNamedBufferSubData(buffer, offset, data);
    }
    
    public static void glGetNamedBufferSubData(final int buffer, final long offset, final ShortBuffer data) {
        GL45.glGetNamedBufferSubData(buffer, offset, data);
    }
    
    public static void glCreateFramebuffers(final IntBuffer framebuffers) {
        GL45.glCreateFramebuffers(framebuffers);
    }
    
    public static int glCreateFramebuffers() {
        return GL45.glCreateFramebuffers();
    }
    
    public static void glNamedFramebufferRenderbuffer(final int framebuffer, final int attachment, final int renderbuffertarget, final int renderbuffer) {
        GL45.glNamedFramebufferRenderbuffer(framebuffer, attachment, renderbuffertarget, renderbuffer);
    }
    
    public static void glNamedFramebufferParameteri(final int framebuffer, final int pname, final int param) {
        GL45.glNamedFramebufferParameteri(framebuffer, pname, param);
    }
    
    public static void glNamedFramebufferTexture(final int framebuffer, final int attachment, final int texture, final int level) {
        GL45.glNamedFramebufferTexture(framebuffer, attachment, texture, level);
    }
    
    public static void glNamedFramebufferTextureLayer(final int framebuffer, final int attachment, final int texture, final int level, final int layer) {
        GL45.glNamedFramebufferTextureLayer(framebuffer, attachment, texture, level, layer);
    }
    
    public static void glNamedFramebufferDrawBuffer(final int framebuffer, final int mode) {
        GL45.glNamedFramebufferDrawBuffer(framebuffer, mode);
    }
    
    public static void glNamedFramebufferDrawBuffers(final int framebuffer, final IntBuffer bufs) {
        GL45.glNamedFramebufferDrawBuffers(framebuffer, bufs);
    }
    
    public static void glNamedFramebufferReadBuffer(final int framebuffer, final int mode) {
        GL45.glNamedFramebufferReadBuffer(framebuffer, mode);
    }
    
    public static void glInvalidateNamedFramebufferData(final int framebuffer, final IntBuffer attachments) {
        GL45.glInvalidateNamedFramebufferData(framebuffer, attachments);
    }
    
    public static void glInvalidateNamedFramebufferSubData(final int framebuffer, final IntBuffer attachments, final int x, final int y, final int width, final int height) {
        GL45.glInvalidateNamedFramebufferSubData(framebuffer, attachments, x, y, width, height);
    }
    
    public static void glClearNamedFramebuffer(final int framebuffer, final int buffer, final int drawbuffer, final IntBuffer value) {
        GL45.glClearNamedFramebuffer(framebuffer, buffer, drawbuffer, value);
    }
    
    public static void glClearNamedFramebufferu(final int framebuffer, final int buffer, final int drawbuffer, final IntBuffer value) {
        GL45.glClearNamedFramebufferu(framebuffer, buffer, drawbuffer, value);
    }
    
    public static void glClearNamedFramebuffer(final int framebuffer, final int buffer, final int drawbuffer, final FloatBuffer value) {
        GL45.glClearNamedFramebuffer(framebuffer, buffer, drawbuffer, value);
    }
    
    public static void glClearNamedFramebufferfi(final int framebuffer, final int buffer, final float depth, final int stencil) {
        GL45.glClearNamedFramebufferfi(framebuffer, buffer, depth, stencil);
    }
    
    public static void glBlitNamedFramebuffer(final int readFramebuffer, final int drawFramebuffer, final int srcX0, final int srcY0, final int srcX1, final int srcY1, final int dstX0, final int dstY0, final int dstX1, final int dstY1, final int mask, final int filter) {
        GL45.glBlitNamedFramebuffer(readFramebuffer, drawFramebuffer, srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
    }
    
    public static int glCheckNamedFramebufferStatus(final int framebuffer, final int target) {
        return GL45.glCheckNamedFramebufferStatus(framebuffer, target);
    }
    
    public static void glGetNamedFramebufferParameter(final int framebuffer, final int pname, final IntBuffer params) {
        GL45.glGetNamedFramebufferParameter(framebuffer, pname, params);
    }
    
    public static int glGetNamedFramebufferParameter(final int framebuffer, final int pname) {
        return GL45.glGetNamedFramebufferParameter(framebuffer, pname);
    }
    
    public static void glGetNamedFramebufferAttachmentParameter(final int framebuffer, final int attachment, final int pname, final IntBuffer params) {
        GL45.glGetNamedFramebufferAttachmentParameter(framebuffer, attachment, pname, params);
    }
    
    public static int glGetNamedFramebufferAttachmentParameter(final int framebuffer, final int attachment, final int pname) {
        return GL45.glGetNamedFramebufferAttachmentParameter(framebuffer, attachment, pname);
    }
    
    public static void glCreateRenderbuffers(final IntBuffer renderbuffers) {
        GL45.glCreateRenderbuffers(renderbuffers);
    }
    
    public static int glCreateRenderbuffers() {
        return GL45.glCreateRenderbuffers();
    }
    
    public static void glNamedRenderbufferStorage(final int renderbuffer, final int internalformat, final int width, final int height) {
        GL45.glNamedRenderbufferStorage(renderbuffer, internalformat, width, height);
    }
    
    public static void glNamedRenderbufferStorageMultisample(final int renderbuffer, final int samples, final int internalformat, final int width, final int height) {
        GL45.glNamedRenderbufferStorageMultisample(renderbuffer, samples, internalformat, width, height);
    }
    
    public static void glGetNamedRenderbufferParameter(final int renderbuffer, final int pname, final IntBuffer params) {
        GL45.glGetNamedRenderbufferParameter(renderbuffer, pname, params);
    }
    
    public static int glGetNamedRenderbufferParameter(final int renderbuffer, final int pname) {
        return GL45.glGetNamedRenderbufferParameter(renderbuffer, pname);
    }
    
    public static void glCreateTextures(final int target, final IntBuffer textures) {
        GL45.glCreateTextures(target, textures);
    }
    
    public static int glCreateTextures(final int target) {
        return GL45.glCreateTextures(target);
    }
    
    public static void glTextureBuffer(final int texture, final int internalformat, final int buffer) {
        GL45.glTextureBuffer(texture, internalformat, buffer);
    }
    
    public static void glTextureBufferRange(final int texture, final int internalformat, final int buffer, final long offset, final long size) {
        GL45.glTextureBufferRange(texture, internalformat, buffer, offset, size);
    }
    
    public static void glTextureStorage1D(final int texture, final int levels, final int internalformat, final int width) {
        GL45.glTextureStorage1D(texture, levels, internalformat, width);
    }
    
    public static void glTextureStorage2D(final int texture, final int levels, final int internalformat, final int width, final int height) {
        GL45.glTextureStorage2D(texture, levels, internalformat, width, height);
    }
    
    public static void glTextureStorage3D(final int texture, final int levels, final int internalformat, final int width, final int height, final int depth) {
        GL45.glTextureStorage3D(texture, levels, internalformat, width, height, depth);
    }
    
    public static void glTextureStorage2DMultisample(final int texture, final int samples, final int internalformat, final int width, final int height, final boolean fixedsamplelocations) {
        GL45.glTextureStorage2DMultisample(texture, samples, internalformat, width, height, fixedsamplelocations);
    }
    
    public static void glTextureStorage3DMultisample(final int texture, final int samples, final int internalformat, final int width, final int height, final int depth, final boolean fixedsamplelocations) {
        GL45.glTextureStorage3DMultisample(texture, samples, internalformat, width, height, depth, fixedsamplelocations);
    }
    
    public static void glTextureSubImage1D(final int texture, final int level, final int xoffset, final int width, final int format, final int type, final ByteBuffer pixels) {
        GL45.glTextureSubImage1D(texture, level, xoffset, width, format, type, pixels);
    }
    
    public static void glTextureSubImage1D(final int texture, final int level, final int xoffset, final int width, final int format, final int type, final DoubleBuffer pixels) {
        GL45.glTextureSubImage1D(texture, level, xoffset, width, format, type, pixels);
    }
    
    public static void glTextureSubImage1D(final int texture, final int level, final int xoffset, final int width, final int format, final int type, final FloatBuffer pixels) {
        GL45.glTextureSubImage1D(texture, level, xoffset, width, format, type, pixels);
    }
    
    public static void glTextureSubImage1D(final int texture, final int level, final int xoffset, final int width, final int format, final int type, final IntBuffer pixels) {
        GL45.glTextureSubImage1D(texture, level, xoffset, width, format, type, pixels);
    }
    
    public static void glTextureSubImage1D(final int texture, final int level, final int xoffset, final int width, final int format, final int type, final ShortBuffer pixels) {
        GL45.glTextureSubImage1D(texture, level, xoffset, width, format, type, pixels);
    }
    
    public static void glTextureSubImage1D(final int texture, final int level, final int xoffset, final int width, final int format, final int type, final long pixels_buffer_offset) {
        GL45.glTextureSubImage1D(texture, level, xoffset, width, format, type, pixels_buffer_offset);
    }
    
    public static void glTextureSubImage2D(final int texture, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final int type, final ByteBuffer pixels) {
        GL45.glTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, type, pixels);
    }
    
    public static void glTextureSubImage2D(final int texture, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final int type, final DoubleBuffer pixels) {
        GL45.glTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, type, pixels);
    }
    
    public static void glTextureSubImage2D(final int texture, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final int type, final FloatBuffer pixels) {
        GL45.glTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, type, pixels);
    }
    
    public static void glTextureSubImage2D(final int texture, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final int type, final IntBuffer pixels) {
        GL45.glTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, type, pixels);
    }
    
    public static void glTextureSubImage2D(final int texture, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final int type, final ShortBuffer pixels) {
        GL45.glTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, type, pixels);
    }
    
    public static void glTextureSubImage2D(final int texture, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final int type, final long pixels_buffer_offset) {
        GL45.glTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, type, pixels_buffer_offset);
    }
    
    public static void glTextureSubImage3D(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final ByteBuffer pixels) {
        GL45.glTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels);
    }
    
    public static void glTextureSubImage3D(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final DoubleBuffer pixels) {
        GL45.glTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels);
    }
    
    public static void glTextureSubImage3D(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final FloatBuffer pixels) {
        GL45.glTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels);
    }
    
    public static void glTextureSubImage3D(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final IntBuffer pixels) {
        GL45.glTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels);
    }
    
    public static void glTextureSubImage3D(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final ShortBuffer pixels) {
        GL45.glTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels);
    }
    
    public static void glTextureSubImage3D(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final long pixels_buffer_offset) {
        GL45.glTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels_buffer_offset);
    }
    
    public static void glCompressedTextureSubImage1D(final int texture, final int level, final int xoffset, final int width, final int format, final ByteBuffer data) {
        GL45.glCompressedTextureSubImage1D(texture, level, xoffset, width, format, data);
    }
    
    public static void glCompressedTextureSubImage1D(final int texture, final int level, final int xoffset, final int width, final int format, final int data_imageSize, final long data_buffer_offset) {
        GL45.glCompressedTextureSubImage1D(texture, level, xoffset, width, format, data_imageSize, data_buffer_offset);
    }
    
    public static void glCompressedTextureSubImage2D(final int texture, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final ByteBuffer data) {
        GL45.glCompressedTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, data);
    }
    
    public static void glCompressedTextureSubImage2D(final int texture, final int level, final int xoffset, final int yoffset, final int width, final int height, final int format, final int data_imageSize, final long data_buffer_offset) {
        GL45.glCompressedTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, data_imageSize, data_buffer_offset);
    }
    
    public static void glCompressedTextureSubImage3D(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int imageSize, final ByteBuffer data) {
        GL45.glCompressedTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, imageSize, data);
    }
    
    public static void glCompressedTextureSubImage3D(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int imageSize, final long data_buffer_offset) {
        GL45.glCompressedTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, imageSize, data_buffer_offset);
    }
    
    public static void glCopyTextureSubImage1D(final int texture, final int level, final int xoffset, final int x, final int y, final int width) {
        GL45.glCopyTextureSubImage1D(texture, level, xoffset, x, y, width);
    }
    
    public static void glCopyTextureSubImage2D(final int texture, final int level, final int xoffset, final int yoffset, final int x, final int y, final int width, final int height) {
        GL45.glCopyTextureSubImage2D(texture, level, xoffset, yoffset, x, y, width, height);
    }
    
    public static void glCopyTextureSubImage3D(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int x, final int y, final int width, final int height) {
        GL45.glCopyTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, x, y, width, height);
    }
    
    public static void glTextureParameterf(final int texture, final int pname, final float param) {
        GL45.glTextureParameterf(texture, pname, param);
    }
    
    public static void glTextureParameter(final int texture, final int pname, final FloatBuffer params) {
        GL45.glTextureParameter(texture, pname, params);
    }
    
    public static void glTextureParameteri(final int texture, final int pname, final int param) {
        GL45.glTextureParameteri(texture, pname, param);
    }
    
    public static void glTextureParameterI(final int texture, final int pname, final IntBuffer params) {
        GL45.glTextureParameterI(texture, pname, params);
    }
    
    public static void glTextureParameterIu(final int texture, final int pname, final IntBuffer params) {
        GL45.glTextureParameterIu(texture, pname, params);
    }
    
    public static void glTextureParameter(final int texture, final int pname, final IntBuffer params) {
        GL45.glTextureParameter(texture, pname, params);
    }
    
    public static void glGenerateTextureMipmap(final int texture) {
        GL45.glGenerateTextureMipmap(texture);
    }
    
    public static void glBindTextureUnit(final int unit, final int texture) {
        GL45.glBindTextureUnit(unit, texture);
    }
    
    public static void glGetTextureImage(final int texture, final int level, final int format, final int type, final ByteBuffer pixels) {
        GL45.glGetTextureImage(texture, level, format, type, pixels);
    }
    
    public static void glGetTextureImage(final int texture, final int level, final int format, final int type, final DoubleBuffer pixels) {
        GL45.glGetTextureImage(texture, level, format, type, pixels);
    }
    
    public static void glGetTextureImage(final int texture, final int level, final int format, final int type, final FloatBuffer pixels) {
        GL45.glGetTextureImage(texture, level, format, type, pixels);
    }
    
    public static void glGetTextureImage(final int texture, final int level, final int format, final int type, final IntBuffer pixels) {
        GL45.glGetTextureImage(texture, level, format, type, pixels);
    }
    
    public static void glGetTextureImage(final int texture, final int level, final int format, final int type, final ShortBuffer pixels) {
        GL45.glGetTextureImage(texture, level, format, type, pixels);
    }
    
    public static void glGetTextureImage(final int texture, final int level, final int format, final int type, final int pixels_bufSize, final long pixels_buffer_offset) {
        GL45.glGetTextureImage(texture, level, format, type, pixels_bufSize, pixels_buffer_offset);
    }
    
    public static void glGetCompressedTextureImage(final int texture, final int level, final ByteBuffer pixels) {
        GL45.glGetCompressedTextureImage(texture, level, pixels);
    }
    
    public static void glGetCompressedTextureImage(final int texture, final int level, final IntBuffer pixels) {
        GL45.glGetCompressedTextureImage(texture, level, pixels);
    }
    
    public static void glGetCompressedTextureImage(final int texture, final int level, final ShortBuffer pixels) {
        GL45.glGetCompressedTextureImage(texture, level, pixels);
    }
    
    public static void glGetCompressedTextureImage(final int texture, final int level, final int pixels_bufSize, final long pixels_buffer_offset) {
        GL45.glGetCompressedTextureImage(texture, level, pixels_bufSize, pixels_buffer_offset);
    }
    
    public static void glGetTextureLevelParameter(final int texture, final int level, final int pname, final FloatBuffer params) {
        GL45.glGetTextureLevelParameter(texture, level, pname, params);
    }
    
    public static float glGetTextureLevelParameterf(final int texture, final int level, final int pname) {
        return GL45.glGetTextureLevelParameterf(texture, level, pname);
    }
    
    public static void glGetTextureLevelParameter(final int texture, final int level, final int pname, final IntBuffer params) {
        GL45.glGetTextureLevelParameter(texture, level, pname, params);
    }
    
    public static int glGetTextureLevelParameteri(final int texture, final int level, final int pname) {
        return GL45.glGetTextureLevelParameteri(texture, level, pname);
    }
    
    public static void glGetTextureParameter(final int texture, final int pname, final FloatBuffer params) {
        GL45.glGetTextureParameter(texture, pname, params);
    }
    
    public static float glGetTextureParameterf(final int texture, final int pname) {
        return GL45.glGetTextureParameterf(texture, pname);
    }
    
    public static void glGetTextureParameterI(final int texture, final int pname, final IntBuffer params) {
        GL45.glGetTextureParameterI(texture, pname, params);
    }
    
    public static int glGetTextureParameterIi(final int texture, final int pname) {
        return GL45.glGetTextureParameterIi(texture, pname);
    }
    
    public static void glGetTextureParameterIu(final int texture, final int pname, final IntBuffer params) {
        GL45.glGetTextureParameterIu(texture, pname, params);
    }
    
    public static int glGetTextureParameterIui(final int texture, final int pname) {
        return GL45.glGetTextureParameterIui(texture, pname);
    }
    
    public static void glGetTextureParameter(final int texture, final int pname, final IntBuffer params) {
        GL45.glGetTextureParameter(texture, pname, params);
    }
    
    public static int glGetTextureParameteri(final int texture, final int pname) {
        return GL45.glGetTextureParameteri(texture, pname);
    }
    
    public static void glCreateVertexArrays(final IntBuffer arrays) {
        GL45.glCreateVertexArrays(arrays);
    }
    
    public static int glCreateVertexArrays() {
        return GL45.glCreateVertexArrays();
    }
    
    public static void glDisableVertexArrayAttrib(final int vaobj, final int index) {
        GL45.glDisableVertexArrayAttrib(vaobj, index);
    }
    
    public static void glEnableVertexArrayAttrib(final int vaobj, final int index) {
        GL45.glEnableVertexArrayAttrib(vaobj, index);
    }
    
    public static void glVertexArrayElementBuffer(final int vaobj, final int buffer) {
        GL45.glVertexArrayElementBuffer(vaobj, buffer);
    }
    
    public static void glVertexArrayVertexBuffer(final int vaobj, final int bindingindex, final int buffer, final long offset, final int stride) {
        GL45.glVertexArrayVertexBuffer(vaobj, bindingindex, buffer, offset, stride);
    }
    
    public static void glVertexArrayVertexBuffers(final int vaobj, final int first, final int count, final IntBuffer buffers, final PointerBuffer offsets, final IntBuffer strides) {
        GL45.glVertexArrayVertexBuffers(vaobj, first, count, buffers, offsets, strides);
    }
    
    public static void glVertexArrayAttribFormat(final int vaobj, final int attribindex, final int size, final int type, final boolean normalized, final int relativeoffset) {
        GL45.glVertexArrayAttribFormat(vaobj, attribindex, size, type, normalized, relativeoffset);
    }
    
    public static void glVertexArrayAttribIFormat(final int vaobj, final int attribindex, final int size, final int type, final int relativeoffset) {
        GL45.glVertexArrayAttribIFormat(vaobj, attribindex, size, type, relativeoffset);
    }
    
    public static void glVertexArrayAttribLFormat(final int vaobj, final int attribindex, final int size, final int type, final int relativeoffset) {
        GL45.glVertexArrayAttribLFormat(vaobj, attribindex, size, type, relativeoffset);
    }
    
    public static void glVertexArrayAttribBinding(final int vaobj, final int attribindex, final int bindingindex) {
        GL45.glVertexArrayAttribBinding(vaobj, attribindex, bindingindex);
    }
    
    public static void glVertexArrayBindingDivisor(final int vaobj, final int bindingindex, final int divisor) {
        GL45.glVertexArrayBindingDivisor(vaobj, bindingindex, divisor);
    }
    
    public static void glGetVertexArray(final int vaobj, final int pname, final IntBuffer param) {
        GL45.glGetVertexArray(vaobj, pname, param);
    }
    
    public static int glGetVertexArray(final int vaobj, final int pname) {
        return GL45.glGetVertexArray(vaobj, pname);
    }
    
    public static void glGetVertexArrayIndexed(final int vaobj, final int index, final int pname, final IntBuffer param) {
        GL45.glGetVertexArrayIndexed(vaobj, index, pname, param);
    }
    
    public static int glGetVertexArrayIndexed(final int vaobj, final int index, final int pname) {
        return GL45.glGetVertexArrayIndexed(vaobj, index, pname);
    }
    
    public static void glGetVertexArrayIndexed64i(final int vaobj, final int index, final int pname, final LongBuffer param) {
        GL45.glGetVertexArrayIndexed64i(vaobj, index, pname, param);
    }
    
    public static long glGetVertexArrayIndexed64i(final int vaobj, final int index, final int pname) {
        return GL45.glGetVertexArrayIndexed64i(vaobj, index, pname);
    }
    
    public static void glCreateSamplers(final IntBuffer samplers) {
        GL45.glCreateSamplers(samplers);
    }
    
    public static int glCreateSamplers() {
        return GL45.glCreateSamplers();
    }
    
    public static void glCreateProgramPipelines(final IntBuffer pipelines) {
        GL45.glCreateProgramPipelines(pipelines);
    }
    
    public static int glCreateProgramPipelines() {
        return GL45.glCreateProgramPipelines();
    }
    
    public static void glCreateQueries(final int target, final IntBuffer ids) {
        GL45.glCreateQueries(target, ids);
    }
    
    public static int glCreateQueries(final int target) {
        return GL45.glCreateQueries(target);
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;

public final class EXTFramebufferObject
{
    public static final int GL_FRAMEBUFFER_EXT = 36160;
    public static final int GL_RENDERBUFFER_EXT = 36161;
    public static final int GL_STENCIL_INDEX1_EXT = 36166;
    public static final int GL_STENCIL_INDEX4_EXT = 36167;
    public static final int GL_STENCIL_INDEX8_EXT = 36168;
    public static final int GL_STENCIL_INDEX16_EXT = 36169;
    public static final int GL_RENDERBUFFER_WIDTH_EXT = 36162;
    public static final int GL_RENDERBUFFER_HEIGHT_EXT = 36163;
    public static final int GL_RENDERBUFFER_INTERNAL_FORMAT_EXT = 36164;
    public static final int GL_RENDERBUFFER_RED_SIZE_EXT = 36176;
    public static final int GL_RENDERBUFFER_GREEN_SIZE_EXT = 36177;
    public static final int GL_RENDERBUFFER_BLUE_SIZE_EXT = 36178;
    public static final int GL_RENDERBUFFER_ALPHA_SIZE_EXT = 36179;
    public static final int GL_RENDERBUFFER_DEPTH_SIZE_EXT = 36180;
    public static final int GL_RENDERBUFFER_STENCIL_SIZE_EXT = 36181;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE_EXT = 36048;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME_EXT = 36049;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL_EXT = 36050;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE_EXT = 36051;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_3D_ZOFFSET_EXT = 36052;
    public static final int GL_COLOR_ATTACHMENT0_EXT = 36064;
    public static final int GL_COLOR_ATTACHMENT1_EXT = 36065;
    public static final int GL_COLOR_ATTACHMENT2_EXT = 36066;
    public static final int GL_COLOR_ATTACHMENT3_EXT = 36067;
    public static final int GL_COLOR_ATTACHMENT4_EXT = 36068;
    public static final int GL_COLOR_ATTACHMENT5_EXT = 36069;
    public static final int GL_COLOR_ATTACHMENT6_EXT = 36070;
    public static final int GL_COLOR_ATTACHMENT7_EXT = 36071;
    public static final int GL_COLOR_ATTACHMENT8_EXT = 36072;
    public static final int GL_COLOR_ATTACHMENT9_EXT = 36073;
    public static final int GL_COLOR_ATTACHMENT10_EXT = 36074;
    public static final int GL_COLOR_ATTACHMENT11_EXT = 36075;
    public static final int GL_COLOR_ATTACHMENT12_EXT = 36076;
    public static final int GL_COLOR_ATTACHMENT13_EXT = 36077;
    public static final int GL_COLOR_ATTACHMENT14_EXT = 36078;
    public static final int GL_COLOR_ATTACHMENT15_EXT = 36079;
    public static final int GL_DEPTH_ATTACHMENT_EXT = 36096;
    public static final int GL_STENCIL_ATTACHMENT_EXT = 36128;
    public static final int GL_FRAMEBUFFER_COMPLETE_EXT = 36053;
    public static final int GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT = 36054;
    public static final int GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT = 36055;
    public static final int GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT = 36057;
    public static final int GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT = 36058;
    public static final int GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT = 36059;
    public static final int GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT = 36060;
    public static final int GL_FRAMEBUFFER_UNSUPPORTED_EXT = 36061;
    public static final int GL_FRAMEBUFFER_BINDING_EXT = 36006;
    public static final int GL_RENDERBUFFER_BINDING_EXT = 36007;
    public static final int GL_MAX_COLOR_ATTACHMENTS_EXT = 36063;
    public static final int GL_MAX_RENDERBUFFER_SIZE_EXT = 34024;
    public static final int GL_INVALID_FRAMEBUFFER_OPERATION_EXT = 1286;
    
    private EXTFramebufferObject() {
    }
    
    public static boolean glIsRenderbufferEXT(final int renderbuffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsRenderbufferEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsRenderbufferEXT(renderbuffer, function_pointer);
        return __result;
    }
    
    static native boolean nglIsRenderbufferEXT(final int p0, final long p1);
    
    public static void glBindRenderbufferEXT(final int target, final int renderbuffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindRenderbufferEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBindRenderbufferEXT(target, renderbuffer, function_pointer);
    }
    
    static native void nglBindRenderbufferEXT(final int p0, final int p1, final long p2);
    
    public static void glDeleteRenderbuffersEXT(final IntBuffer renderbuffers) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteRenderbuffersEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(renderbuffers);
        nglDeleteRenderbuffersEXT(renderbuffers.remaining(), MemoryUtil.getAddress(renderbuffers), function_pointer);
    }
    
    static native void nglDeleteRenderbuffersEXT(final int p0, final long p1, final long p2);
    
    public static void glDeleteRenderbuffersEXT(final int renderbuffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteRenderbuffersEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDeleteRenderbuffersEXT(1, APIUtil.getInt(caps, renderbuffer), function_pointer);
    }
    
    public static void glGenRenderbuffersEXT(final IntBuffer renderbuffers) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenRenderbuffersEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(renderbuffers);
        nglGenRenderbuffersEXT(renderbuffers.remaining(), MemoryUtil.getAddress(renderbuffers), function_pointer);
    }
    
    static native void nglGenRenderbuffersEXT(final int p0, final long p1, final long p2);
    
    public static int glGenRenderbuffersEXT() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenRenderbuffersEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer renderbuffers = APIUtil.getBufferInt(caps);
        nglGenRenderbuffersEXT(1, MemoryUtil.getAddress(renderbuffers), function_pointer);
        return renderbuffers.get(0);
    }
    
    public static void glRenderbufferStorageEXT(final int target, final int internalformat, final int width, final int height) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glRenderbufferStorageEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglRenderbufferStorageEXT(target, internalformat, width, height, function_pointer);
    }
    
    static native void nglRenderbufferStorageEXT(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glGetRenderbufferParameterEXT(final int target, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetRenderbufferParameterivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetRenderbufferParameterivEXT(target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetRenderbufferParameterivEXT(final int p0, final int p1, final long p2, final long p3);
    
    @Deprecated
    public static int glGetRenderbufferParameterEXT(final int target, final int pname) {
        return glGetRenderbufferParameteriEXT(target, pname);
    }
    
    public static int glGetRenderbufferParameteriEXT(final int target, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetRenderbufferParameterivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetRenderbufferParameterivEXT(target, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static boolean glIsFramebufferEXT(final int framebuffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsFramebufferEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsFramebufferEXT(framebuffer, function_pointer);
        return __result;
    }
    
    static native boolean nglIsFramebufferEXT(final int p0, final long p1);
    
    public static void glBindFramebufferEXT(final int target, final int framebuffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindFramebufferEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBindFramebufferEXT(target, framebuffer, function_pointer);
    }
    
    static native void nglBindFramebufferEXT(final int p0, final int p1, final long p2);
    
    public static void glDeleteFramebuffersEXT(final IntBuffer framebuffers) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteFramebuffersEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(framebuffers);
        nglDeleteFramebuffersEXT(framebuffers.remaining(), MemoryUtil.getAddress(framebuffers), function_pointer);
    }
    
    static native void nglDeleteFramebuffersEXT(final int p0, final long p1, final long p2);
    
    public static void glDeleteFramebuffersEXT(final int framebuffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteFramebuffersEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDeleteFramebuffersEXT(1, APIUtil.getInt(caps, framebuffer), function_pointer);
    }
    
    public static void glGenFramebuffersEXT(final IntBuffer framebuffers) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenFramebuffersEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(framebuffers);
        nglGenFramebuffersEXT(framebuffers.remaining(), MemoryUtil.getAddress(framebuffers), function_pointer);
    }
    
    static native void nglGenFramebuffersEXT(final int p0, final long p1, final long p2);
    
    public static int glGenFramebuffersEXT() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenFramebuffersEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer framebuffers = APIUtil.getBufferInt(caps);
        nglGenFramebuffersEXT(1, MemoryUtil.getAddress(framebuffers), function_pointer);
        return framebuffers.get(0);
    }
    
    public static int glCheckFramebufferStatusEXT(final int target) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCheckFramebufferStatusEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nglCheckFramebufferStatusEXT(target, function_pointer);
        return __result;
    }
    
    static native int nglCheckFramebufferStatusEXT(final int p0, final long p1);
    
    public static void glFramebufferTexture1DEXT(final int target, final int attachment, final int textarget, final int texture, final int level) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFramebufferTexture1DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglFramebufferTexture1DEXT(target, attachment, textarget, texture, level, function_pointer);
    }
    
    static native void nglFramebufferTexture1DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glFramebufferTexture2DEXT(final int target, final int attachment, final int textarget, final int texture, final int level) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFramebufferTexture2DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglFramebufferTexture2DEXT(target, attachment, textarget, texture, level, function_pointer);
    }
    
    static native void nglFramebufferTexture2DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glFramebufferTexture3DEXT(final int target, final int attachment, final int textarget, final int texture, final int level, final int zoffset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFramebufferTexture3DEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglFramebufferTexture3DEXT(target, attachment, textarget, texture, level, zoffset, function_pointer);
    }
    
    static native void nglFramebufferTexture3DEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6);
    
    public static void glFramebufferRenderbufferEXT(final int target, final int attachment, final int renderbuffertarget, final int renderbuffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFramebufferRenderbufferEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglFramebufferRenderbufferEXT(target, attachment, renderbuffertarget, renderbuffer, function_pointer);
    }
    
    static native void nglFramebufferRenderbufferEXT(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glGetFramebufferAttachmentParameterEXT(final int target, final int attachment, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetFramebufferAttachmentParameterivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetFramebufferAttachmentParameterivEXT(target, attachment, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetFramebufferAttachmentParameterivEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    @Deprecated
    public static int glGetFramebufferAttachmentParameterEXT(final int target, final int attachment, final int pname) {
        return glGetFramebufferAttachmentParameteriEXT(target, attachment, pname);
    }
    
    public static int glGetFramebufferAttachmentParameteriEXT(final int target, final int attachment, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetFramebufferAttachmentParameterivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetFramebufferAttachmentParameterivEXT(target, attachment, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGenerateMipmapEXT(final int target) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenerateMipmapEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglGenerateMipmapEXT(target, function_pointer);
    }
    
    static native void nglGenerateMipmapEXT(final int p0, final long p1);
}

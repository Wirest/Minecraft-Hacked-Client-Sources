// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.IntBuffer;

public final class ARBFramebufferNoAttachments
{
    public static final int GL_FRAMEBUFFER_DEFAULT_WIDTH = 37648;
    public static final int GL_FRAMEBUFFER_DEFAULT_HEIGHT = 37649;
    public static final int GL_FRAMEBUFFER_DEFAULT_LAYERS = 37650;
    public static final int GL_FRAMEBUFFER_DEFAULT_SAMPLES = 37651;
    public static final int GL_FRAMEBUFFER_DEFAULT_FIXED_SAMPLE_LOCATIONS = 37652;
    public static final int GL_MAX_FRAMEBUFFER_WIDTH = 37653;
    public static final int GL_MAX_FRAMEBUFFER_HEIGHT = 37654;
    public static final int GL_MAX_FRAMEBUFFER_LAYERS = 37655;
    public static final int GL_MAX_FRAMEBUFFER_SAMPLES = 37656;
    
    private ARBFramebufferNoAttachments() {
    }
    
    public static void glFramebufferParameteri(final int target, final int pname, final int param) {
        GL43.glFramebufferParameteri(target, pname, param);
    }
    
    public static void glGetFramebufferParameter(final int target, final int pname, final IntBuffer params) {
        GL43.glGetFramebufferParameter(target, pname, params);
    }
    
    public static int glGetFramebufferParameteri(final int target, final int pname) {
        return GL43.glGetFramebufferParameteri(target, pname);
    }
    
    public static void glNamedFramebufferParameteriEXT(final int framebuffer, final int pname, final int param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedFramebufferParameteriEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNamedFramebufferParameteriEXT(framebuffer, pname, param, function_pointer);
    }
    
    static native void nglNamedFramebufferParameteriEXT(final int p0, final int p1, final int p2, final long p3);
    
    public static void glGetNamedFramebufferParameterEXT(final int framebuffer, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedFramebufferParameterivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetNamedFramebufferParameterivEXT(framebuffer, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetNamedFramebufferParameterivEXT(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetNamedFramebufferParameterEXT(final int framebuffer, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedFramebufferParameterivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetNamedFramebufferParameterivEXT(framebuffer, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
}

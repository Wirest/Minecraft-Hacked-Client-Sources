// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ByteBuffer;

public final class NVExplicitMultisample
{
    public static final int GL_SAMPLE_POSITION_NV = 36432;
    public static final int GL_SAMPLE_MASK_NV = 36433;
    public static final int GL_SAMPLE_MASK_VALUE_NV = 36434;
    public static final int GL_TEXTURE_BINDING_RENDERBUFFER_NV = 36435;
    public static final int GL_TEXTURE_RENDERBUFFER_DATA_STORE_BINDING_NV = 36436;
    public static final int GL_MAX_SAMPLE_MASK_WORDS_NV = 36441;
    public static final int GL_TEXTURE_RENDERBUFFER_NV = 36437;
    public static final int GL_SAMPLER_RENDERBUFFER_NV = 36438;
    public static final int GL_INT_SAMPLER_RENDERBUFFER_NV = 36439;
    public static final int GL_UNSIGNED_INT_SAMPLER_RENDERBUFFER_NV = 36440;
    
    private NVExplicitMultisample() {
    }
    
    public static void glGetBooleanIndexedEXT(final int pname, final int index, final ByteBuffer data) {
        EXTDrawBuffers2.glGetBooleanIndexedEXT(pname, index, data);
    }
    
    public static boolean glGetBooleanIndexedEXT(final int pname, final int index) {
        return EXTDrawBuffers2.glGetBooleanIndexedEXT(pname, index);
    }
    
    public static void glGetIntegerIndexedEXT(final int pname, final int index, final IntBuffer data) {
        EXTDrawBuffers2.glGetIntegerIndexedEXT(pname, index, data);
    }
    
    public static int glGetIntegerIndexedEXT(final int pname, final int index) {
        return EXTDrawBuffers2.glGetIntegerIndexedEXT(pname, index);
    }
    
    public static void glGetMultisampleNV(final int pname, final int index, final FloatBuffer val) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMultisamplefvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(val, 2);
        nglGetMultisamplefvNV(pname, index, MemoryUtil.getAddress(val), function_pointer);
    }
    
    static native void nglGetMultisamplefvNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glSampleMaskIndexedNV(final int index, final int mask) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSampleMaskIndexedNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglSampleMaskIndexedNV(index, mask, function_pointer);
    }
    
    static native void nglSampleMaskIndexedNV(final int p0, final int p1, final long p2);
    
    public static void glTexRenderbufferNV(final int target, final int renderbuffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTexRenderbufferNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTexRenderbufferNV(target, renderbuffer, function_pointer);
    }
    
    static native void nglTexRenderbufferNV(final int p0, final int p1, final long p2);
}

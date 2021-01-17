// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.LWJGLUtil;
import java.nio.DoubleBuffer;
import org.lwjgl.BufferChecks;

public final class EXTSecondaryColor
{
    public static final int GL_COLOR_SUM_EXT = 33880;
    public static final int GL_CURRENT_SECONDARY_COLOR_EXT = 33881;
    public static final int GL_SECONDARY_COLOR_ARRAY_SIZE_EXT = 33882;
    public static final int GL_SECONDARY_COLOR_ARRAY_TYPE_EXT = 33883;
    public static final int GL_SECONDARY_COLOR_ARRAY_STRIDE_EXT = 33884;
    public static final int GL_SECONDARY_COLOR_ARRAY_POINTER_EXT = 33885;
    public static final int GL_SECONDARY_COLOR_ARRAY_EXT = 33886;
    
    private EXTSecondaryColor() {
    }
    
    public static void glSecondaryColor3bEXT(final byte red, final byte green, final byte blue) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSecondaryColor3bEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglSecondaryColor3bEXT(red, green, blue, function_pointer);
    }
    
    static native void nglSecondaryColor3bEXT(final byte p0, final byte p1, final byte p2, final long p3);
    
    public static void glSecondaryColor3fEXT(final float red, final float green, final float blue) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSecondaryColor3fEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglSecondaryColor3fEXT(red, green, blue, function_pointer);
    }
    
    static native void nglSecondaryColor3fEXT(final float p0, final float p1, final float p2, final long p3);
    
    public static void glSecondaryColor3dEXT(final double red, final double green, final double blue) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSecondaryColor3dEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglSecondaryColor3dEXT(red, green, blue, function_pointer);
    }
    
    static native void nglSecondaryColor3dEXT(final double p0, final double p1, final double p2, final long p3);
    
    public static void glSecondaryColor3ubEXT(final byte red, final byte green, final byte blue) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSecondaryColor3ubEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglSecondaryColor3ubEXT(red, green, blue, function_pointer);
    }
    
    static native void nglSecondaryColor3ubEXT(final byte p0, final byte p1, final byte p2, final long p3);
    
    public static void glSecondaryColorPointerEXT(final int size, final int stride, final DoubleBuffer pPointer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSecondaryColorPointerEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(pPointer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).EXT_secondary_color_glSecondaryColorPointerEXT_pPointer = pPointer;
        }
        nglSecondaryColorPointerEXT(size, 5130, stride, MemoryUtil.getAddress(pPointer), function_pointer);
    }
    
    public static void glSecondaryColorPointerEXT(final int size, final int stride, final FloatBuffer pPointer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSecondaryColorPointerEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(pPointer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).EXT_secondary_color_glSecondaryColorPointerEXT_pPointer = pPointer;
        }
        nglSecondaryColorPointerEXT(size, 5126, stride, MemoryUtil.getAddress(pPointer), function_pointer);
    }
    
    public static void glSecondaryColorPointerEXT(final int size, final boolean unsigned, final int stride, final ByteBuffer pPointer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSecondaryColorPointerEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(pPointer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).EXT_secondary_color_glSecondaryColorPointerEXT_pPointer = pPointer;
        }
        nglSecondaryColorPointerEXT(size, unsigned ? 5121 : 5120, stride, MemoryUtil.getAddress(pPointer), function_pointer);
    }
    
    static native void nglSecondaryColorPointerEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glSecondaryColorPointerEXT(final int size, final int type, final int stride, final long pPointer_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSecondaryColorPointerEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOenabled(caps);
        nglSecondaryColorPointerEXTBO(size, type, stride, pPointer_buffer_offset, function_pointer);
    }
    
    static native void nglSecondaryColorPointerEXTBO(final int p0, final int p1, final int p2, final long p3, final long p4);
}

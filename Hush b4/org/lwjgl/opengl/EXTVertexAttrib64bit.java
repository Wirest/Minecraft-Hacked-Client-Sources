// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;
import java.nio.DoubleBuffer;
import org.lwjgl.BufferChecks;

public final class EXTVertexAttrib64bit
{
    public static final int GL_DOUBLE_VEC2_EXT = 36860;
    public static final int GL_DOUBLE_VEC3_EXT = 36861;
    public static final int GL_DOUBLE_VEC4_EXT = 36862;
    public static final int GL_DOUBLE_MAT2_EXT = 36678;
    public static final int GL_DOUBLE_MAT3_EXT = 36679;
    public static final int GL_DOUBLE_MAT4_EXT = 36680;
    public static final int GL_DOUBLE_MAT2x3_EXT = 36681;
    public static final int GL_DOUBLE_MAT2x4_EXT = 36682;
    public static final int GL_DOUBLE_MAT3x2_EXT = 36683;
    public static final int GL_DOUBLE_MAT3x4_EXT = 36684;
    public static final int GL_DOUBLE_MAT4x2_EXT = 36685;
    public static final int GL_DOUBLE_MAT4x3_EXT = 36686;
    
    private EXTVertexAttrib64bit() {
    }
    
    public static void glVertexAttribL1dEXT(final int index, final double x) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL1dEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribL1dEXT(index, x, function_pointer);
    }
    
    static native void nglVertexAttribL1dEXT(final int p0, final double p1, final long p2);
    
    public static void glVertexAttribL2dEXT(final int index, final double x, final double y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL2dEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribL2dEXT(index, x, y, function_pointer);
    }
    
    static native void nglVertexAttribL2dEXT(final int p0, final double p1, final double p2, final long p3);
    
    public static void glVertexAttribL3dEXT(final int index, final double x, final double y, final double z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL3dEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribL3dEXT(index, x, y, z, function_pointer);
    }
    
    static native void nglVertexAttribL3dEXT(final int p0, final double p1, final double p2, final double p3, final long p4);
    
    public static void glVertexAttribL4dEXT(final int index, final double x, final double y, final double z, final double w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL4dEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribL4dEXT(index, x, y, z, w, function_pointer);
    }
    
    static native void nglVertexAttribL4dEXT(final int p0, final double p1, final double p2, final double p3, final double p4, final long p5);
    
    public static void glVertexAttribL1EXT(final int index, final DoubleBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL1dvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 1);
        nglVertexAttribL1dvEXT(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribL1dvEXT(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribL2EXT(final int index, final DoubleBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL2dvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 2);
        nglVertexAttribL2dvEXT(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribL2dvEXT(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribL3EXT(final int index, final DoubleBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL3dvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 3);
        nglVertexAttribL3dvEXT(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribL3dvEXT(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribL4EXT(final int index, final DoubleBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL4dvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 4);
        nglVertexAttribL4dvEXT(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribL4dvEXT(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribLPointerEXT(final int index, final int size, final int stride, final DoubleBuffer pointer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribLPointerEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(pointer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = pointer;
        }
        nglVertexAttribLPointerEXT(index, size, 5130, stride, MemoryUtil.getAddress(pointer), function_pointer);
    }
    
    static native void nglVertexAttribLPointerEXT(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glVertexAttribLPointerEXT(final int index, final int size, final int stride, final long pointer_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribLPointerEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOenabled(caps);
        nglVertexAttribLPointerEXTBO(index, size, 5130, stride, pointer_buffer_offset, function_pointer);
    }
    
    static native void nglVertexAttribLPointerEXTBO(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glGetVertexAttribLEXT(final int index, final int pname, final DoubleBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexAttribLdvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetVertexAttribLdvEXT(index, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetVertexAttribLdvEXT(final int p0, final int p1, final long p2, final long p3);
    
    public static void glVertexArrayVertexAttribLOffsetEXT(final int vaobj, final int buffer, final int index, final int size, final int type, final int stride, final long offset) {
        ARBVertexAttrib64bit.glVertexArrayVertexAttribLOffsetEXT(vaobj, buffer, index, size, type, stride, offset);
    }
}

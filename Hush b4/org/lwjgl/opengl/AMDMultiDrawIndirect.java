// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.ByteBuffer;

public final class AMDMultiDrawIndirect
{
    private AMDMultiDrawIndirect() {
    }
    
    public static void glMultiDrawArraysIndirectAMD(final int mode, final ByteBuffer indirect, final int primcount, final int stride) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiDrawArraysIndirectAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureIndirectBOdisabled(caps);
        BufferChecks.checkBuffer(indirect, ((stride == 0) ? 16 : stride) * primcount);
        nglMultiDrawArraysIndirectAMD(mode, MemoryUtil.getAddress(indirect), primcount, stride, function_pointer);
    }
    
    static native void nglMultiDrawArraysIndirectAMD(final int p0, final long p1, final int p2, final int p3, final long p4);
    
    public static void glMultiDrawArraysIndirectAMD(final int mode, final long indirect_buffer_offset, final int primcount, final int stride) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiDrawArraysIndirectAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureIndirectBOenabled(caps);
        nglMultiDrawArraysIndirectAMDBO(mode, indirect_buffer_offset, primcount, stride, function_pointer);
    }
    
    static native void nglMultiDrawArraysIndirectAMDBO(final int p0, final long p1, final int p2, final int p3, final long p4);
    
    public static void glMultiDrawArraysIndirectAMD(final int mode, final IntBuffer indirect, final int primcount, final int stride) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiDrawArraysIndirectAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureIndirectBOdisabled(caps);
        BufferChecks.checkBuffer(indirect, ((stride == 0) ? 4 : (stride >> 2)) * primcount);
        nglMultiDrawArraysIndirectAMD(mode, MemoryUtil.getAddress(indirect), primcount, stride, function_pointer);
    }
    
    public static void glMultiDrawElementsIndirectAMD(final int mode, final int type, final ByteBuffer indirect, final int primcount, final int stride) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiDrawElementsIndirectAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureIndirectBOdisabled(caps);
        BufferChecks.checkBuffer(indirect, ((stride == 0) ? 20 : stride) * primcount);
        nglMultiDrawElementsIndirectAMD(mode, type, MemoryUtil.getAddress(indirect), primcount, stride, function_pointer);
    }
    
    static native void nglMultiDrawElementsIndirectAMD(final int p0, final int p1, final long p2, final int p3, final int p4, final long p5);
    
    public static void glMultiDrawElementsIndirectAMD(final int mode, final int type, final long indirect_buffer_offset, final int primcount, final int stride) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiDrawElementsIndirectAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureIndirectBOenabled(caps);
        nglMultiDrawElementsIndirectAMDBO(mode, type, indirect_buffer_offset, primcount, stride, function_pointer);
    }
    
    static native void nglMultiDrawElementsIndirectAMDBO(final int p0, final int p1, final long p2, final int p3, final int p4, final long p5);
    
    public static void glMultiDrawElementsIndirectAMD(final int mode, final int type, final IntBuffer indirect, final int primcount, final int stride) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiDrawElementsIndirectAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureIndirectBOdisabled(caps);
        BufferChecks.checkBuffer(indirect, ((stride == 0) ? 5 : (stride >> 2)) * primcount);
        nglMultiDrawElementsIndirectAMD(mode, type, MemoryUtil.getAddress(indirect), primcount, stride, function_pointer);
    }
}

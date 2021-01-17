// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.ByteBuffer;

public final class NVBindlessMultiDrawIndirect
{
    private NVBindlessMultiDrawIndirect() {
    }
    
    public static void glMultiDrawArraysIndirectBindlessNV(final int mode, final ByteBuffer indirect, final int drawCount, final int stride, final int vertexBufferCount) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiDrawArraysIndirectBindlessNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureIndirectBOdisabled(caps);
        BufferChecks.checkBuffer(indirect, ((stride == 0) ? (20 + 24 * vertexBufferCount) : stride) * drawCount);
        nglMultiDrawArraysIndirectBindlessNV(mode, MemoryUtil.getAddress(indirect), drawCount, stride, vertexBufferCount, function_pointer);
    }
    
    static native void nglMultiDrawArraysIndirectBindlessNV(final int p0, final long p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glMultiDrawArraysIndirectBindlessNV(final int mode, final long indirect_buffer_offset, final int drawCount, final int stride, final int vertexBufferCount) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiDrawArraysIndirectBindlessNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureIndirectBOenabled(caps);
        nglMultiDrawArraysIndirectBindlessNVBO(mode, indirect_buffer_offset, drawCount, stride, vertexBufferCount, function_pointer);
    }
    
    static native void nglMultiDrawArraysIndirectBindlessNVBO(final int p0, final long p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glMultiDrawElementsIndirectBindlessNV(final int mode, final int type, final ByteBuffer indirect, final int drawCount, final int stride, final int vertexBufferCount) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiDrawElementsIndirectBindlessNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureIndirectBOdisabled(caps);
        BufferChecks.checkBuffer(indirect, ((stride == 0) ? (48 + 24 * vertexBufferCount) : stride) * drawCount);
        nglMultiDrawElementsIndirectBindlessNV(mode, type, MemoryUtil.getAddress(indirect), drawCount, stride, vertexBufferCount, function_pointer);
    }
    
    static native void nglMultiDrawElementsIndirectBindlessNV(final int p0, final int p1, final long p2, final int p3, final int p4, final int p5, final long p6);
    
    public static void glMultiDrawElementsIndirectBindlessNV(final int mode, final int type, final long indirect_buffer_offset, final int drawCount, final int stride, final int vertexBufferCount) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiDrawElementsIndirectBindlessNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureIndirectBOenabled(caps);
        nglMultiDrawElementsIndirectBindlessNVBO(mode, type, indirect_buffer_offset, drawCount, stride, vertexBufferCount, function_pointer);
    }
    
    static native void nglMultiDrawElementsIndirectBindlessNVBO(final int p0, final int p1, final long p2, final int p3, final int p4, final int p5, final long p6);
}

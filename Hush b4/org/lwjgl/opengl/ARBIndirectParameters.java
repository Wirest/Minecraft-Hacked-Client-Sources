// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.ByteBuffer;

public final class ARBIndirectParameters
{
    public static final int GL_PARAMETER_BUFFER_ARB = 33006;
    public static final int GL_PARAMETER_BUFFER_BINDING_ARB = 33007;
    
    private ARBIndirectParameters() {
    }
    
    public static void glMultiDrawArraysIndirectCountARB(final int mode, final ByteBuffer indirect, final long drawcount, final int maxdrawcount, final int stride) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiDrawArraysIndirectCountARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureIndirectBOdisabled(caps);
        BufferChecks.checkBuffer(indirect, ((stride == 0) ? 16 : stride) * maxdrawcount);
        nglMultiDrawArraysIndirectCountARB(mode, MemoryUtil.getAddress(indirect), drawcount, maxdrawcount, stride, function_pointer);
    }
    
    static native void nglMultiDrawArraysIndirectCountARB(final int p0, final long p1, final long p2, final int p3, final int p4, final long p5);
    
    public static void glMultiDrawArraysIndirectCountARB(final int mode, final long indirect_buffer_offset, final long drawcount, final int maxdrawcount, final int stride) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiDrawArraysIndirectCountARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureIndirectBOenabled(caps);
        nglMultiDrawArraysIndirectCountARBBO(mode, indirect_buffer_offset, drawcount, maxdrawcount, stride, function_pointer);
    }
    
    static native void nglMultiDrawArraysIndirectCountARBBO(final int p0, final long p1, final long p2, final int p3, final int p4, final long p5);
    
    public static void glMultiDrawArraysIndirectCountARB(final int mode, final IntBuffer indirect, final long drawcount, final int maxdrawcount, final int stride) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiDrawArraysIndirectCountARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureIndirectBOdisabled(caps);
        BufferChecks.checkBuffer(indirect, ((stride == 0) ? 4 : (stride >> 2)) * maxdrawcount);
        nglMultiDrawArraysIndirectCountARB(mode, MemoryUtil.getAddress(indirect), drawcount, maxdrawcount, stride, function_pointer);
    }
    
    public static void glMultiDrawElementsIndirectCountARB(final int mode, final int type, final ByteBuffer indirect, final long drawcount, final int maxdrawcount, final int stride) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiDrawElementsIndirectCountARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureIndirectBOdisabled(caps);
        BufferChecks.checkBuffer(indirect, ((stride == 0) ? 20 : stride) * maxdrawcount);
        nglMultiDrawElementsIndirectCountARB(mode, type, MemoryUtil.getAddress(indirect), drawcount, maxdrawcount, stride, function_pointer);
    }
    
    static native void nglMultiDrawElementsIndirectCountARB(final int p0, final int p1, final long p2, final long p3, final int p4, final int p5, final long p6);
    
    public static void glMultiDrawElementsIndirectCountARB(final int mode, final int type, final long indirect_buffer_offset, final long drawcount, final int maxdrawcount, final int stride) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiDrawElementsIndirectCountARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureIndirectBOenabled(caps);
        nglMultiDrawElementsIndirectCountARBBO(mode, type, indirect_buffer_offset, drawcount, maxdrawcount, stride, function_pointer);
    }
    
    static native void nglMultiDrawElementsIndirectCountARBBO(final int p0, final int p1, final long p2, final long p3, final int p4, final int p5, final long p6);
    
    public static void glMultiDrawElementsIndirectCountARB(final int mode, final int type, final IntBuffer indirect, final long drawcount, final int maxdrawcount, final int stride) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiDrawElementsIndirectCountARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureIndirectBOdisabled(caps);
        BufferChecks.checkBuffer(indirect, ((stride == 0) ? 5 : (stride >> 2)) * maxdrawcount);
        nglMultiDrawElementsIndirectCountARB(mode, type, MemoryUtil.getAddress(indirect), drawcount, maxdrawcount, stride, function_pointer);
    }
}

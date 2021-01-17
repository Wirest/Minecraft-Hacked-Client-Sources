// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ShortBuffer;
import java.nio.IntBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.ByteBuffer;

public final class EXTDrawRangeElements
{
    public static final int GL_MAX_ELEMENTS_VERTICES_EXT = 33000;
    public static final int GL_MAX_ELEMENTS_INDICES_EXT = 33001;
    
    private EXTDrawRangeElements() {
    }
    
    public static void glDrawRangeElementsEXT(final int mode, final int start, final int end, final ByteBuffer pIndices) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawRangeElementsEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureElementVBOdisabled(caps);
        BufferChecks.checkDirect(pIndices);
        nglDrawRangeElementsEXT(mode, start, end, pIndices.remaining(), 5121, MemoryUtil.getAddress(pIndices), function_pointer);
    }
    
    public static void glDrawRangeElementsEXT(final int mode, final int start, final int end, final IntBuffer pIndices) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawRangeElementsEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureElementVBOdisabled(caps);
        BufferChecks.checkDirect(pIndices);
        nglDrawRangeElementsEXT(mode, start, end, pIndices.remaining(), 5125, MemoryUtil.getAddress(pIndices), function_pointer);
    }
    
    public static void glDrawRangeElementsEXT(final int mode, final int start, final int end, final ShortBuffer pIndices) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawRangeElementsEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureElementVBOdisabled(caps);
        BufferChecks.checkDirect(pIndices);
        nglDrawRangeElementsEXT(mode, start, end, pIndices.remaining(), 5123, MemoryUtil.getAddress(pIndices), function_pointer);
    }
    
    static native void nglDrawRangeElementsEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5, final long p6);
    
    public static void glDrawRangeElementsEXT(final int mode, final int start, final int end, final int pIndices_count, final int type, final long pIndices_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawRangeElementsEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureElementVBOenabled(caps);
        nglDrawRangeElementsEXTBO(mode, start, end, pIndices_count, type, pIndices_buffer_offset, function_pointer);
    }
    
    static native void nglDrawRangeElementsEXTBO(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5, final long p6);
}

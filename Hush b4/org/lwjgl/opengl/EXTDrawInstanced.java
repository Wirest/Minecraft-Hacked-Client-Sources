// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ShortBuffer;
import java.nio.IntBuffer;
import org.lwjgl.MemoryUtil;
import java.nio.ByteBuffer;
import org.lwjgl.BufferChecks;

public final class EXTDrawInstanced
{
    private EXTDrawInstanced() {
    }
    
    public static void glDrawArraysInstancedEXT(final int mode, final int first, final int count, final int primcount) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawArraysInstancedEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDrawArraysInstancedEXT(mode, first, count, primcount, function_pointer);
    }
    
    static native void nglDrawArraysInstancedEXT(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glDrawElementsInstancedEXT(final int mode, final ByteBuffer indices, final int primcount) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawElementsInstancedEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureElementVBOdisabled(caps);
        BufferChecks.checkDirect(indices);
        nglDrawElementsInstancedEXT(mode, indices.remaining(), 5121, MemoryUtil.getAddress(indices), primcount, function_pointer);
    }
    
    public static void glDrawElementsInstancedEXT(final int mode, final IntBuffer indices, final int primcount) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawElementsInstancedEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureElementVBOdisabled(caps);
        BufferChecks.checkDirect(indices);
        nglDrawElementsInstancedEXT(mode, indices.remaining(), 5125, MemoryUtil.getAddress(indices), primcount, function_pointer);
    }
    
    public static void glDrawElementsInstancedEXT(final int mode, final ShortBuffer indices, final int primcount) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawElementsInstancedEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureElementVBOdisabled(caps);
        BufferChecks.checkDirect(indices);
        nglDrawElementsInstancedEXT(mode, indices.remaining(), 5123, MemoryUtil.getAddress(indices), primcount, function_pointer);
    }
    
    static native void nglDrawElementsInstancedEXT(final int p0, final int p1, final int p2, final long p3, final int p4, final long p5);
    
    public static void glDrawElementsInstancedEXT(final int mode, final int indices_count, final int type, final long indices_buffer_offset, final int primcount) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawElementsInstancedEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureElementVBOenabled(caps);
        nglDrawElementsInstancedEXTBO(mode, indices_count, type, indices_buffer_offset, primcount, function_pointer);
    }
    
    static native void nglDrawElementsInstancedEXTBO(final int p0, final int p1, final int p2, final long p3, final int p4, final long p5);
}

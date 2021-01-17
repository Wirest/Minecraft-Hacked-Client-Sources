// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ShortBuffer;
import java.nio.IntBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.ByteBuffer;

public final class APPLEElementArray
{
    public static final int GL_ELEMENT_ARRAY_APPLE = 34664;
    public static final int GL_ELEMENT_ARRAY_TYPE_APPLE = 34665;
    public static final int GL_ELEMENT_ARRAY_POINTER_APPLE = 34666;
    
    private APPLEElementArray() {
    }
    
    public static void glElementPointerAPPLE(final ByteBuffer pointer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glElementPointerAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pointer);
        nglElementPointerAPPLE(5121, MemoryUtil.getAddress(pointer), function_pointer);
    }
    
    public static void glElementPointerAPPLE(final IntBuffer pointer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glElementPointerAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pointer);
        nglElementPointerAPPLE(5125, MemoryUtil.getAddress(pointer), function_pointer);
    }
    
    public static void glElementPointerAPPLE(final ShortBuffer pointer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glElementPointerAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pointer);
        nglElementPointerAPPLE(5123, MemoryUtil.getAddress(pointer), function_pointer);
    }
    
    static native void nglElementPointerAPPLE(final int p0, final long p1, final long p2);
    
    public static void glDrawElementArrayAPPLE(final int mode, final int first, final int count) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawElementArrayAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDrawElementArrayAPPLE(mode, first, count, function_pointer);
    }
    
    static native void nglDrawElementArrayAPPLE(final int p0, final int p1, final int p2, final long p3);
    
    public static void glDrawRangeElementArrayAPPLE(final int mode, final int start, final int end, final int first, final int count) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawRangeElementArrayAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDrawRangeElementArrayAPPLE(mode, start, end, first, count, function_pointer);
    }
    
    static native void nglDrawRangeElementArrayAPPLE(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glMultiDrawElementArrayAPPLE(final int mode, final IntBuffer first, final IntBuffer count) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiDrawElementArrayAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(first);
        BufferChecks.checkBuffer(count, first.remaining());
        nglMultiDrawElementArrayAPPLE(mode, MemoryUtil.getAddress(first), MemoryUtil.getAddress(count), first.remaining(), function_pointer);
    }
    
    static native void nglMultiDrawElementArrayAPPLE(final int p0, final long p1, final long p2, final int p3, final long p4);
    
    public static void glMultiDrawRangeElementArrayAPPLE(final int mode, final int start, final int end, final IntBuffer first, final IntBuffer count) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiDrawRangeElementArrayAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(first);
        BufferChecks.checkBuffer(count, first.remaining());
        nglMultiDrawRangeElementArrayAPPLE(mode, start, end, MemoryUtil.getAddress(first), MemoryUtil.getAddress(count), first.remaining(), function_pointer);
    }
    
    static native void nglMultiDrawRangeElementArrayAPPLE(final int p0, final int p1, final int p2, final long p3, final long p4, final int p5, final long p6);
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ShortBuffer;
import java.nio.IntBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.ByteBuffer;

public final class ATIElementArray
{
    public static final int GL_ELEMENT_ARRAY_ATI = 34664;
    public static final int GL_ELEMENT_ARRAY_TYPE_ATI = 34665;
    public static final int GL_ELEMENT_ARRAY_POINTER_ATI = 34666;
    
    private ATIElementArray() {
    }
    
    public static void glElementPointerATI(final ByteBuffer pPointer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glElementPointerATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pPointer);
        nglElementPointerATI(5121, MemoryUtil.getAddress(pPointer), function_pointer);
    }
    
    public static void glElementPointerATI(final IntBuffer pPointer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glElementPointerATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pPointer);
        nglElementPointerATI(5125, MemoryUtil.getAddress(pPointer), function_pointer);
    }
    
    public static void glElementPointerATI(final ShortBuffer pPointer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glElementPointerATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pPointer);
        nglElementPointerATI(5123, MemoryUtil.getAddress(pPointer), function_pointer);
    }
    
    static native void nglElementPointerATI(final int p0, final long p1, final long p2);
    
    public static void glDrawElementArrayATI(final int mode, final int count) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawElementArrayATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDrawElementArrayATI(mode, count, function_pointer);
    }
    
    static native void nglDrawElementArrayATI(final int p0, final int p1, final long p2);
    
    public static void glDrawRangeElementArrayATI(final int mode, final int start, final int end, final int count) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawRangeElementArrayATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDrawRangeElementArrayATI(mode, start, end, count, function_pointer);
    }
    
    static native void nglDrawRangeElementArrayATI(final int p0, final int p1, final int p2, final int p3, final long p4);
}

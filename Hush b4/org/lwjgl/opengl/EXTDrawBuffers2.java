// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.MemoryUtil;
import java.nio.ByteBuffer;
import org.lwjgl.BufferChecks;

public final class EXTDrawBuffers2
{
    private EXTDrawBuffers2() {
    }
    
    public static void glColorMaskIndexedEXT(final int buf, final boolean r, final boolean g, final boolean b, final boolean a) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColorMaskIndexedEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglColorMaskIndexedEXT(buf, r, g, b, a, function_pointer);
    }
    
    static native void nglColorMaskIndexedEXT(final int p0, final boolean p1, final boolean p2, final boolean p3, final boolean p4, final long p5);
    
    public static void glGetBooleanIndexedEXT(final int value, final int index, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetBooleanIndexedvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(data, 4);
        nglGetBooleanIndexedvEXT(value, index, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglGetBooleanIndexedvEXT(final int p0, final int p1, final long p2, final long p3);
    
    public static boolean glGetBooleanIndexedEXT(final int value, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetBooleanIndexedvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final ByteBuffer data = APIUtil.getBufferByte(caps, 1);
        nglGetBooleanIndexedvEXT(value, index, MemoryUtil.getAddress(data), function_pointer);
        return data.get(0) == 1;
    }
    
    public static void glGetIntegerIndexedEXT(final int value, final int index, final IntBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetIntegerIndexedvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(data, 4);
        nglGetIntegerIndexedvEXT(value, index, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglGetIntegerIndexedvEXT(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetIntegerIndexedEXT(final int value, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetIntegerIndexedvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer data = APIUtil.getBufferInt(caps);
        nglGetIntegerIndexedvEXT(value, index, MemoryUtil.getAddress(data), function_pointer);
        return data.get(0);
    }
    
    public static void glEnableIndexedEXT(final int target, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glEnableIndexedEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglEnableIndexedEXT(target, index, function_pointer);
    }
    
    static native void nglEnableIndexedEXT(final int p0, final int p1, final long p2);
    
    public static void glDisableIndexedEXT(final int target, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDisableIndexedEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDisableIndexedEXT(target, index, function_pointer);
    }
    
    static native void nglDisableIndexedEXT(final int p0, final int p1, final long p2);
    
    public static boolean glIsEnabledIndexedEXT(final int target, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsEnabledIndexedEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsEnabledIndexedEXT(target, index, function_pointer);
        return __result;
    }
    
    static native boolean nglIsEnabledIndexedEXT(final int p0, final int p1, final long p2);
}

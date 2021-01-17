// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ARBDrawBuffersBlend
{
    private ARBDrawBuffersBlend() {
    }
    
    public static void glBlendEquationiARB(final int buf, final int mode) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBlendEquationiARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBlendEquationiARB(buf, mode, function_pointer);
    }
    
    static native void nglBlendEquationiARB(final int p0, final int p1, final long p2);
    
    public static void glBlendEquationSeparateiARB(final int buf, final int modeRGB, final int modeAlpha) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBlendEquationSeparateiARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBlendEquationSeparateiARB(buf, modeRGB, modeAlpha, function_pointer);
    }
    
    static native void nglBlendEquationSeparateiARB(final int p0, final int p1, final int p2, final long p3);
    
    public static void glBlendFunciARB(final int buf, final int src, final int dst) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBlendFunciARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBlendFunciARB(buf, src, dst, function_pointer);
    }
    
    static native void nglBlendFunciARB(final int p0, final int p1, final int p2, final long p3);
    
    public static void glBlendFuncSeparateiARB(final int buf, final int srcRGB, final int dstRGB, final int srcAlpha, final int dstAlpha) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBlendFuncSeparateiARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBlendFuncSeparateiARB(buf, srcRGB, dstRGB, srcAlpha, dstAlpha, function_pointer);
    }
    
    static native void nglBlendFuncSeparateiARB(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
}

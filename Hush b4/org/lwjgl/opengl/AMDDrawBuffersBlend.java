// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class AMDDrawBuffersBlend
{
    private AMDDrawBuffersBlend() {
    }
    
    public static void glBlendFuncIndexedAMD(final int buf, final int src, final int dst) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBlendFuncIndexedAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBlendFuncIndexedAMD(buf, src, dst, function_pointer);
    }
    
    static native void nglBlendFuncIndexedAMD(final int p0, final int p1, final int p2, final long p3);
    
    public static void glBlendFuncSeparateIndexedAMD(final int buf, final int srcRGB, final int dstRGB, final int srcAlpha, final int dstAlpha) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBlendFuncSeparateIndexedAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBlendFuncSeparateIndexedAMD(buf, srcRGB, dstRGB, srcAlpha, dstAlpha, function_pointer);
    }
    
    static native void nglBlendFuncSeparateIndexedAMD(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glBlendEquationIndexedAMD(final int buf, final int mode) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBlendEquationIndexedAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBlendEquationIndexedAMD(buf, mode, function_pointer);
    }
    
    static native void nglBlendEquationIndexedAMD(final int p0, final int p1, final long p2);
    
    public static void glBlendEquationSeparateIndexedAMD(final int buf, final int modeRGB, final int modeAlpha) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBlendEquationSeparateIndexedAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBlendEquationSeparateIndexedAMD(buf, modeRGB, modeAlpha, function_pointer);
    }
    
    static native void nglBlendEquationSeparateIndexedAMD(final int p0, final int p1, final int p2, final long p3);
}

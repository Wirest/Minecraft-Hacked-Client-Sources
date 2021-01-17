// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ARBWindowPos
{
    private ARBWindowPos() {
    }
    
    public static void glWindowPos2fARB(final float x, final float y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glWindowPos2fARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglWindowPos2fARB(x, y, function_pointer);
    }
    
    static native void nglWindowPos2fARB(final float p0, final float p1, final long p2);
    
    public static void glWindowPos2dARB(final double x, final double y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glWindowPos2dARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglWindowPos2dARB(x, y, function_pointer);
    }
    
    static native void nglWindowPos2dARB(final double p0, final double p1, final long p2);
    
    public static void glWindowPos2iARB(final int x, final int y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glWindowPos2iARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglWindowPos2iARB(x, y, function_pointer);
    }
    
    static native void nglWindowPos2iARB(final int p0, final int p1, final long p2);
    
    public static void glWindowPos2sARB(final short x, final short y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glWindowPos2sARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglWindowPos2sARB(x, y, function_pointer);
    }
    
    static native void nglWindowPos2sARB(final short p0, final short p1, final long p2);
    
    public static void glWindowPos3fARB(final float x, final float y, final float z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glWindowPos3fARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglWindowPos3fARB(x, y, z, function_pointer);
    }
    
    static native void nglWindowPos3fARB(final float p0, final float p1, final float p2, final long p3);
    
    public static void glWindowPos3dARB(final double x, final double y, final double z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glWindowPos3dARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglWindowPos3dARB(x, y, z, function_pointer);
    }
    
    static native void nglWindowPos3dARB(final double p0, final double p1, final double p2, final long p3);
    
    public static void glWindowPos3iARB(final int x, final int y, final int z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glWindowPos3iARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglWindowPos3iARB(x, y, z, function_pointer);
    }
    
    static native void nglWindowPos3iARB(final int p0, final int p1, final int p2, final long p3);
    
    public static void glWindowPos3sARB(final short x, final short y, final short z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glWindowPos3sARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglWindowPos3sARB(x, y, z, function_pointer);
    }
    
    static native void nglWindowPos3sARB(final short p0, final short p1, final short p2, final long p3);
}

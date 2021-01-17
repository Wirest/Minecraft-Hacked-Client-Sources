// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class NVCopyImage
{
    private NVCopyImage() {
    }
    
    public static void glCopyImageSubDataNV(final int srcName, final int srcTarget, final int srcLevel, final int srcX, final int srcY, final int srcZ, final int dstName, final int dstTarget, final int dstLevel, final int dstX, final int dstY, final int dstZ, final int width, final int height, final int depth) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCopyImageSubDataNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglCopyImageSubDataNV(srcName, srcTarget, srcLevel, srcX, srcY, srcZ, dstName, dstTarget, dstLevel, dstX, dstY, dstZ, width, height, depth, function_pointer);
    }
    
    static native void nglCopyImageSubDataNV(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final int p10, final int p11, final int p12, final int p13, final int p14, final long p15);
}

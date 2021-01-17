// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class NVDrawTexture
{
    private NVDrawTexture() {
    }
    
    public static void glDrawTextureNV(final int texture, final int sampler, final float x0, final float y0, final float x1, final float y1, final float z, final float s0, final float t0, final float s1, final float t1) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawTextureNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDrawTextureNV(texture, sampler, x0, y0, x1, y1, z, s0, t0, s1, t1, function_pointer);
    }
    
    static native void nglDrawTextureNV(final int p0, final int p1, final float p2, final float p3, final float p4, final float p5, final float p6, final float p7, final float p8, final float p9, final float p10, final long p11);
}

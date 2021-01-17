// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class NVTextureBarrier
{
    private NVTextureBarrier() {
    }
    
    public static void glTextureBarrierNV() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureBarrierNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTextureBarrierNV(function_pointer);
    }
    
    static native void nglTextureBarrierNV(final long p0);
}

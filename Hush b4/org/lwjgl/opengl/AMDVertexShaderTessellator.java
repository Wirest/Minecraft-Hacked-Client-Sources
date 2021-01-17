// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class AMDVertexShaderTessellator
{
    public static final int GL_SAMPLER_BUFFER_AMD = 36865;
    public static final int GL_INT_SAMPLER_BUFFER_AMD = 36866;
    public static final int GL_UNSIGNED_INT_SAMPLER_BUFFER_AMD = 36867;
    public static final int GL_DISCRETE_AMD = 36870;
    public static final int GL_CONTINUOUS_AMD = 36871;
    public static final int GL_TESSELLATION_MODE_AMD = 36868;
    public static final int GL_TESSELLATION_FACTOR_AMD = 36869;
    
    private AMDVertexShaderTessellator() {
    }
    
    public static void glTessellationFactorAMD(final float factor) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTessellationFactorAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTessellationFactorAMD(factor, function_pointer);
    }
    
    static native void nglTessellationFactorAMD(final float p0, final long p1);
    
    public static void glTessellationModeAMD(final int mode) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTessellationModeAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTessellationModeAMD(mode, function_pointer);
    }
    
    static native void nglTessellationModeAMD(final int p0, final long p1);
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class NVTextureMultisample
{
    public static final int GL_TEXTURE_COVERAGE_SAMPLES_NV = 36933;
    public static final int GL_TEXTURE_COLOR_SAMPLES_NV = 36934;
    
    private NVTextureMultisample() {
    }
    
    public static void glTexImage2DMultisampleCoverageNV(final int target, final int coverageSamples, final int colorSamples, final int internalFormat, final int width, final int height, final boolean fixedSampleLocations) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTexImage2DMultisampleCoverageNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTexImage2DMultisampleCoverageNV(target, coverageSamples, colorSamples, internalFormat, width, height, fixedSampleLocations, function_pointer);
    }
    
    static native void nglTexImage2DMultisampleCoverageNV(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final boolean p6, final long p7);
    
    public static void glTexImage3DMultisampleCoverageNV(final int target, final int coverageSamples, final int colorSamples, final int internalFormat, final int width, final int height, final int depth, final boolean fixedSampleLocations) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTexImage3DMultisampleCoverageNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTexImage3DMultisampleCoverageNV(target, coverageSamples, colorSamples, internalFormat, width, height, depth, fixedSampleLocations, function_pointer);
    }
    
    static native void nglTexImage3DMultisampleCoverageNV(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final boolean p7, final long p8);
    
    public static void glTextureImage2DMultisampleNV(final int texture, final int target, final int samples, final int internalFormat, final int width, final int height, final boolean fixedSampleLocations) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureImage2DMultisampleNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTextureImage2DMultisampleNV(texture, target, samples, internalFormat, width, height, fixedSampleLocations, function_pointer);
    }
    
    static native void nglTextureImage2DMultisampleNV(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final boolean p6, final long p7);
    
    public static void glTextureImage3DMultisampleNV(final int texture, final int target, final int samples, final int internalFormat, final int width, final int height, final int depth, final boolean fixedSampleLocations) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureImage3DMultisampleNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTextureImage3DMultisampleNV(texture, target, samples, internalFormat, width, height, depth, fixedSampleLocations, function_pointer);
    }
    
    static native void nglTextureImage3DMultisampleNV(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final boolean p7, final long p8);
    
    public static void glTextureImage2DMultisampleCoverageNV(final int texture, final int target, final int coverageSamples, final int colorSamples, final int internalFormat, final int width, final int height, final boolean fixedSampleLocations) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureImage2DMultisampleCoverageNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTextureImage2DMultisampleCoverageNV(texture, target, coverageSamples, colorSamples, internalFormat, width, height, fixedSampleLocations, function_pointer);
    }
    
    static native void nglTextureImage2DMultisampleCoverageNV(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final boolean p7, final long p8);
    
    public static void glTextureImage3DMultisampleCoverageNV(final int texture, final int target, final int coverageSamples, final int colorSamples, final int internalFormat, final int width, final int height, final int depth, final boolean fixedSampleLocations) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureImage3DMultisampleCoverageNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTextureImage3DMultisampleCoverageNV(texture, target, coverageSamples, colorSamples, internalFormat, width, height, depth, fixedSampleLocations, function_pointer);
    }
    
    static native void nglTextureImage3DMultisampleCoverageNV(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final boolean p8, final long p9);
}

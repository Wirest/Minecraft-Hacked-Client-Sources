// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class NVFramebufferMultisampleCoverage
{
    public static final int GL_RENDERBUFFER_COVERAGE_SAMPLES_NV = 36011;
    public static final int GL_RENDERBUFFER_COLOR_SAMPLES_NV = 36368;
    public static final int GL_MAX_MULTISAMPLE_COVERAGE_MODES_NV = 36369;
    public static final int GL_MULTISAMPLE_COVERAGE_MODES_NV = 36370;
    
    private NVFramebufferMultisampleCoverage() {
    }
    
    public static void glRenderbufferStorageMultisampleCoverageNV(final int target, final int coverageSamples, final int colorSamples, final int internalformat, final int width, final int height) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glRenderbufferStorageMultisampleCoverageNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglRenderbufferStorageMultisampleCoverageNV(target, coverageSamples, colorSamples, internalformat, width, height, function_pointer);
    }
    
    static native void nglRenderbufferStorageMultisampleCoverageNV(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6);
}

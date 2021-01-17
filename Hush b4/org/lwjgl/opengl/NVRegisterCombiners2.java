// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.FloatBuffer;

public final class NVRegisterCombiners2
{
    public static final int GL_PER_STAGE_CONSTANTS_NV = 34101;
    
    private NVRegisterCombiners2() {
    }
    
    public static void glCombinerStageParameterNV(final int stage, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCombinerStageParameterfvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglCombinerStageParameterfvNV(stage, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglCombinerStageParameterfvNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetCombinerStageParameterNV(final int stage, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetCombinerStageParameterfvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetCombinerStageParameterfvNV(stage, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetCombinerStageParameterfvNV(final int p0, final int p1, final long p2, final long p3);
}

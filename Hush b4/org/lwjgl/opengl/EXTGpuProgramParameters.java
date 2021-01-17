// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.FloatBuffer;

public final class EXTGpuProgramParameters
{
    private EXTGpuProgramParameters() {
    }
    
    public static void glProgramEnvParameters4EXT(final int target, final int index, final int count, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramEnvParameters4fvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, count << 2);
        nglProgramEnvParameters4fvEXT(target, index, count, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglProgramEnvParameters4fvEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramLocalParameters4EXT(final int target, final int index, final int count, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramLocalParameters4fvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, count << 2);
        nglProgramLocalParameters4fvEXT(target, index, count, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglProgramLocalParameters4fvEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
}

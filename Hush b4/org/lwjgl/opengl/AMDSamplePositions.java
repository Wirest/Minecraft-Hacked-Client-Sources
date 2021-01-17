// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.FloatBuffer;

public final class AMDSamplePositions
{
    public static final int GL_SUBSAMPLE_DISTANCE_AMD = 34879;
    
    private AMDSamplePositions() {
    }
    
    public static void glSetMultisampleAMD(final int pname, final int index, final FloatBuffer val) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSetMultisamplefvAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(val, 2);
        nglSetMultisamplefvAMD(pname, index, MemoryUtil.getAddress(val), function_pointer);
    }
    
    static native void nglSetMultisamplefvAMD(final int p0, final int p1, final long p2, final long p3);
}

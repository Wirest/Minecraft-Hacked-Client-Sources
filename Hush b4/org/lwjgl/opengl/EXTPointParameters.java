// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import java.nio.FloatBuffer;
import org.lwjgl.BufferChecks;

public final class EXTPointParameters
{
    public static final int GL_POINT_SIZE_MIN_EXT = 33062;
    public static final int GL_POINT_SIZE_MAX_EXT = 33063;
    public static final int GL_POINT_FADE_THRESHOLD_SIZE_EXT = 33064;
    public static final int GL_DISTANCE_ATTENUATION_EXT = 33065;
    
    private EXTPointParameters() {
    }
    
    public static void glPointParameterfEXT(final int pname, final float param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPointParameterfEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglPointParameterfEXT(pname, param, function_pointer);
    }
    
    static native void nglPointParameterfEXT(final int p0, final float p1, final long p2);
    
    public static void glPointParameterEXT(final int pname, final FloatBuffer pfParams) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPointParameterfvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(pfParams, 4);
        nglPointParameterfvEXT(pname, MemoryUtil.getAddress(pfParams), function_pointer);
    }
    
    static native void nglPointParameterfvEXT(final int p0, final long p1, final long p2);
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import java.nio.FloatBuffer;
import org.lwjgl.BufferChecks;

public final class ARBPointParameters
{
    public static final int GL_POINT_SIZE_MIN_ARB = 33062;
    public static final int GL_POINT_SIZE_MAX_ARB = 33063;
    public static final int GL_POINT_FADE_THRESHOLD_SIZE_ARB = 33064;
    public static final int GL_POINT_DISTANCE_ATTENUATION_ARB = 33065;
    
    private ARBPointParameters() {
    }
    
    public static void glPointParameterfARB(final int pname, final float param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPointParameterfARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglPointParameterfARB(pname, param, function_pointer);
    }
    
    static native void nglPointParameterfARB(final int p0, final float p1, final long p2);
    
    public static void glPointParameterARB(final int pname, final FloatBuffer pfParams) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPointParameterfvARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(pfParams, 4);
        nglPointParameterfvARB(pname, MemoryUtil.getAddress(pfParams), function_pointer);
    }
    
    static native void nglPointParameterfvARB(final int p0, final long p1, final long p2);
}

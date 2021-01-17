// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;

public final class NVPointSprite
{
    public static final int GL_POINT_SPRITE_NV = 34913;
    public static final int GL_COORD_REPLACE_NV = 34914;
    public static final int GL_POINT_SPRITE_R_MODE_NV = 34915;
    
    private NVPointSprite() {
    }
    
    public static void glPointParameteriNV(final int pname, final int param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPointParameteriNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglPointParameteriNV(pname, param, function_pointer);
    }
    
    static native void nglPointParameteriNV(final int p0, final int p1, final long p2);
    
    public static void glPointParameterNV(final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPointParameterivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglPointParameterivNV(pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglPointParameterivNV(final int p0, final long p1, final long p2);
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.FloatBuffer;

public final class ATIEnvmapBumpmap
{
    public static final int GL_BUMP_ROT_MATRIX_ATI = 34677;
    public static final int GL_BUMP_ROT_MATRIX_SIZE_ATI = 34678;
    public static final int GL_BUMP_NUM_TEX_UNITS_ATI = 34679;
    public static final int GL_BUMP_TEX_UNITS_ATI = 34680;
    public static final int GL_DUDV_ATI = 34681;
    public static final int GL_DU8DV8_ATI = 34682;
    public static final int GL_BUMP_ENVMAP_ATI = 34683;
    public static final int GL_BUMP_TARGET_ATI = 34684;
    
    private ATIEnvmapBumpmap() {
    }
    
    public static void glTexBumpParameterATI(final int pname, final FloatBuffer param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTexBumpParameterfvATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(param, 4);
        nglTexBumpParameterfvATI(pname, MemoryUtil.getAddress(param), function_pointer);
    }
    
    static native void nglTexBumpParameterfvATI(final int p0, final long p1, final long p2);
    
    public static void glTexBumpParameterATI(final int pname, final IntBuffer param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTexBumpParameterivATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(param, 4);
        nglTexBumpParameterivATI(pname, MemoryUtil.getAddress(param), function_pointer);
    }
    
    static native void nglTexBumpParameterivATI(final int p0, final long p1, final long p2);
    
    public static void glGetTexBumpParameterATI(final int pname, final FloatBuffer param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTexBumpParameterfvATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(param, 4);
        nglGetTexBumpParameterfvATI(pname, MemoryUtil.getAddress(param), function_pointer);
    }
    
    static native void nglGetTexBumpParameterfvATI(final int p0, final long p1, final long p2);
    
    public static void glGetTexBumpParameterATI(final int pname, final IntBuffer param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTexBumpParameterivATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(param, 4);
        nglGetTexBumpParameterivATI(pname, MemoryUtil.getAddress(param), function_pointer);
    }
    
    static native void nglGetTexBumpParameterivATI(final int p0, final long p1, final long p2);
}

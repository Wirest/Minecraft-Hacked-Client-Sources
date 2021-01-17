// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.ByteBuffer;

public final class NVFragmentProgram extends NVProgram
{
    public static final int GL_FRAGMENT_PROGRAM_NV = 34928;
    public static final int GL_MAX_TEXTURE_COORDS_NV = 34929;
    public static final int GL_MAX_TEXTURE_IMAGE_UNITS_NV = 34930;
    public static final int GL_FRAGMENT_PROGRAM_BINDING_NV = 34931;
    public static final int GL_MAX_FRAGMENT_PROGRAM_LOCAL_PARAMETERS_NV = 34920;
    
    private NVFragmentProgram() {
    }
    
    public static void glProgramNamedParameter4fNV(final int id, final ByteBuffer name, final float x, final float y, final float z, final float w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramNamedParameter4fNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(name);
        nglProgramNamedParameter4fNV(id, name.remaining(), MemoryUtil.getAddress(name), x, y, z, w, function_pointer);
    }
    
    static native void nglProgramNamedParameter4fNV(final int p0, final int p1, final long p2, final float p3, final float p4, final float p5, final float p6, final long p7);
    
    public static void glProgramNamedParameter4dNV(final int id, final ByteBuffer name, final double x, final double y, final double z, final double w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramNamedParameter4dNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(name);
        nglProgramNamedParameter4dNV(id, name.remaining(), MemoryUtil.getAddress(name), x, y, z, w, function_pointer);
    }
    
    static native void nglProgramNamedParameter4dNV(final int p0, final int p1, final long p2, final double p3, final double p4, final double p5, final double p6, final long p7);
    
    public static void glGetProgramNamedParameterNV(final int id, final ByteBuffer name, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramNamedParameterfvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(name);
        BufferChecks.checkBuffer(params, 4);
        nglGetProgramNamedParameterfvNV(id, name.remaining(), MemoryUtil.getAddress(name), MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetProgramNamedParameterfvNV(final int p0, final int p1, final long p2, final long p3, final long p4);
    
    public static void glGetProgramNamedParameterNV(final int id, final ByteBuffer name, final DoubleBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramNamedParameterdvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(name);
        BufferChecks.checkBuffer(params, 4);
        nglGetProgramNamedParameterdvNV(id, name.remaining(), MemoryUtil.getAddress(name), MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetProgramNamedParameterdvNV(final int p0, final int p1, final long p2, final long p3, final long p4);
}

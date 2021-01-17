// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.FloatBuffer;

public final class GL21
{
    public static final int GL_FLOAT_MAT2x3 = 35685;
    public static final int GL_FLOAT_MAT2x4 = 35686;
    public static final int GL_FLOAT_MAT3x2 = 35687;
    public static final int GL_FLOAT_MAT3x4 = 35688;
    public static final int GL_FLOAT_MAT4x2 = 35689;
    public static final int GL_FLOAT_MAT4x3 = 35690;
    public static final int GL_PIXEL_PACK_BUFFER = 35051;
    public static final int GL_PIXEL_UNPACK_BUFFER = 35052;
    public static final int GL_PIXEL_PACK_BUFFER_BINDING = 35053;
    public static final int GL_PIXEL_UNPACK_BUFFER_BINDING = 35055;
    public static final int GL_SRGB = 35904;
    public static final int GL_SRGB8 = 35905;
    public static final int GL_SRGB_ALPHA = 35906;
    public static final int GL_SRGB8_ALPHA8 = 35907;
    public static final int GL_SLUMINANCE_ALPHA = 35908;
    public static final int GL_SLUMINANCE8_ALPHA8 = 35909;
    public static final int GL_SLUMINANCE = 35910;
    public static final int GL_SLUMINANCE8 = 35911;
    public static final int GL_COMPRESSED_SRGB = 35912;
    public static final int GL_COMPRESSED_SRGB_ALPHA = 35913;
    public static final int GL_COMPRESSED_SLUMINANCE = 35914;
    public static final int GL_COMPRESSED_SLUMINANCE_ALPHA = 35915;
    public static final int GL_CURRENT_RASTER_SECONDARY_COLOR = 33887;
    
    private GL21() {
    }
    
    public static void glUniformMatrix2x3(final int location, final boolean transpose, final FloatBuffer matrices) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniformMatrix2x3fv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(matrices);
        nglUniformMatrix2x3fv(location, matrices.remaining() / 6, transpose, MemoryUtil.getAddress(matrices), function_pointer);
    }
    
    static native void nglUniformMatrix2x3fv(final int p0, final int p1, final boolean p2, final long p3, final long p4);
    
    public static void glUniformMatrix3x2(final int location, final boolean transpose, final FloatBuffer matrices) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniformMatrix3x2fv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(matrices);
        nglUniformMatrix3x2fv(location, matrices.remaining() / 6, transpose, MemoryUtil.getAddress(matrices), function_pointer);
    }
    
    static native void nglUniformMatrix3x2fv(final int p0, final int p1, final boolean p2, final long p3, final long p4);
    
    public static void glUniformMatrix2x4(final int location, final boolean transpose, final FloatBuffer matrices) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniformMatrix2x4fv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(matrices);
        nglUniformMatrix2x4fv(location, matrices.remaining() >> 3, transpose, MemoryUtil.getAddress(matrices), function_pointer);
    }
    
    static native void nglUniformMatrix2x4fv(final int p0, final int p1, final boolean p2, final long p3, final long p4);
    
    public static void glUniformMatrix4x2(final int location, final boolean transpose, final FloatBuffer matrices) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniformMatrix4x2fv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(matrices);
        nglUniformMatrix4x2fv(location, matrices.remaining() >> 3, transpose, MemoryUtil.getAddress(matrices), function_pointer);
    }
    
    static native void nglUniformMatrix4x2fv(final int p0, final int p1, final boolean p2, final long p3, final long p4);
    
    public static void glUniformMatrix3x4(final int location, final boolean transpose, final FloatBuffer matrices) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniformMatrix3x4fv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(matrices);
        nglUniformMatrix3x4fv(location, matrices.remaining() / 12, transpose, MemoryUtil.getAddress(matrices), function_pointer);
    }
    
    static native void nglUniformMatrix3x4fv(final int p0, final int p1, final boolean p2, final long p3, final long p4);
    
    public static void glUniformMatrix4x3(final int location, final boolean transpose, final FloatBuffer matrices) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniformMatrix4x3fv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(matrices);
        nglUniformMatrix4x3fv(location, matrices.remaining() / 12, transpose, MemoryUtil.getAddress(matrices), function_pointer);
    }
    
    static native void nglUniformMatrix4x3fv(final int p0, final int p1, final boolean p2, final long p3, final long p4);
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.FloatBuffer;

public final class NVParameterBufferObject
{
    public static final int GL_MAX_PROGRAM_PARAMETER_BUFFER_BINDINGS_NV = 36256;
    public static final int GL_MAX_PROGRAM_PARAMETER_BUFFER_SIZE_NV = 36257;
    public static final int GL_VERTEX_PROGRAM_PARAMETER_BUFFER_NV = 36258;
    public static final int GL_GEOMETRY_PROGRAM_PARAMETER_BUFFER_NV = 36259;
    public static final int GL_FRAGMENT_PROGRAM_PARAMETER_BUFFER_NV = 36260;
    
    private NVParameterBufferObject() {
    }
    
    public static void glProgramBufferParametersNV(final int target, final int buffer, final int index, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramBufferParametersfvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglProgramBufferParametersfvNV(target, buffer, index, params.remaining() >> 2, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglProgramBufferParametersfvNV(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glProgramBufferParametersINV(final int target, final int buffer, final int index, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramBufferParametersIivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglProgramBufferParametersIivNV(target, buffer, index, params.remaining() >> 2, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglProgramBufferParametersIivNV(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glProgramBufferParametersIuNV(final int target, final int buffer, final int index, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramBufferParametersIuivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglProgramBufferParametersIuivNV(target, buffer, index, params.remaining() >> 2, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglProgramBufferParametersIuivNV(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
}

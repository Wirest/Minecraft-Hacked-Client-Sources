// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.MemoryUtil;
import java.nio.FloatBuffer;
import org.lwjgl.BufferChecks;

public final class ATIVertexAttribArrayObject
{
    private ATIVertexAttribArrayObject() {
    }
    
    public static void glVertexAttribArrayObjectATI(final int index, final int size, final int type, final boolean normalized, final int stride, final int buffer, final int offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribArrayObjectATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribArrayObjectATI(index, size, type, normalized, stride, buffer, offset, function_pointer);
    }
    
    static native void nglVertexAttribArrayObjectATI(final int p0, final int p1, final int p2, final boolean p3, final int p4, final int p5, final int p6, final long p7);
    
    public static void glGetVertexAttribArrayObjectATI(final int index, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexAttribArrayObjectfvATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetVertexAttribArrayObjectfvATI(index, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetVertexAttribArrayObjectfvATI(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetVertexAttribArrayObjectATI(final int index, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexAttribArrayObjectivATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetVertexAttribArrayObjectivATI(index, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetVertexAttribArrayObjectivATI(final int p0, final int p1, final long p2, final long p3);
}

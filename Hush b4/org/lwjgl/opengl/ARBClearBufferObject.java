// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.ByteBuffer;

public final class ARBClearBufferObject
{
    private ARBClearBufferObject() {
    }
    
    public static void glClearBufferData(final int target, final int internalformat, final int format, final int type, final ByteBuffer data) {
        GL43.glClearBufferData(target, internalformat, format, type, data);
    }
    
    public static void glClearBufferSubData(final int target, final int internalformat, final long offset, final long size, final int format, final int type, final ByteBuffer data) {
        GL43.glClearBufferSubData(target, internalformat, offset, size, format, type, data);
    }
    
    public static void glClearNamedBufferDataEXT(final int buffer, final int internalformat, final int format, final int type, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glClearNamedBufferDataEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(data, 1);
        nglClearNamedBufferDataEXT(buffer, internalformat, format, type, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglClearNamedBufferDataEXT(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glClearNamedBufferSubDataEXT(final int buffer, final int internalformat, final long offset, final long size, final int format, final int type, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glClearNamedBufferSubDataEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(data, 1);
        nglClearNamedBufferSubDataEXT(buffer, internalformat, offset, size, format, type, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglClearNamedBufferSubDataEXT(final int p0, final int p1, final long p2, final long p3, final int p4, final int p5, final long p6, final long p7);
}

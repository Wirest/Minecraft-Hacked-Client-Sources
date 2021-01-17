// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.LongBuffer;

public final class EXTTimerQuery
{
    public static final int GL_TIME_ELAPSED_EXT = 35007;
    
    private EXTTimerQuery() {
    }
    
    public static void glGetQueryObjectEXT(final int id, final int pname, final LongBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetQueryObjecti64vEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetQueryObjecti64vEXT(id, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetQueryObjecti64vEXT(final int p0, final int p1, final long p2, final long p3);
    
    public static long glGetQueryObjectEXT(final int id, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetQueryObjecti64vEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final LongBuffer params = APIUtil.getBufferLong(caps);
        nglGetQueryObjecti64vEXT(id, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetQueryObjectuEXT(final int id, final int pname, final LongBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetQueryObjectui64vEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetQueryObjectui64vEXT(id, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetQueryObjectui64vEXT(final int p0, final int p1, final long p2, final long p3);
    
    public static long glGetQueryObjectuEXT(final int id, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetQueryObjectui64vEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final LongBuffer params = APIUtil.getBufferLong(caps);
        nglGetQueryObjectui64vEXT(id, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
}

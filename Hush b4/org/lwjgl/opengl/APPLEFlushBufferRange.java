// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class APPLEFlushBufferRange
{
    public static final int GL_BUFFER_SERIALIZED_MODIFY_APPLE = 35346;
    public static final int GL_BUFFER_FLUSHING_UNMAP_APPLE = 35347;
    
    private APPLEFlushBufferRange() {
    }
    
    public static void glBufferParameteriAPPLE(final int target, final int pname, final int param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferParameteriAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBufferParameteriAPPLE(target, pname, param, function_pointer);
    }
    
    static native void nglBufferParameteriAPPLE(final int p0, final int p1, final int p2, final long p3);
    
    public static void glFlushMappedBufferRangeAPPLE(final int target, final long offset, final long size) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFlushMappedBufferRangeAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglFlushMappedBufferRangeAPPLE(target, offset, size, function_pointer);
    }
    
    static native void nglFlushMappedBufferRangeAPPLE(final int p0, final long p1, final long p2, final long p3);
}

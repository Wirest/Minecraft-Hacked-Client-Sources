// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ByteOrder;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.BufferChecks;
import java.nio.ByteBuffer;

public final class ATIMapObjectBuffer
{
    private ATIMapObjectBuffer() {
    }
    
    public static ByteBuffer glMapObjectBufferATI(final int buffer, final ByteBuffer old_buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMapObjectBufferATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (old_buffer != null) {
            BufferChecks.checkDirect(old_buffer);
        }
        final ByteBuffer __result = nglMapObjectBufferATI(buffer, ATIVertexArrayObject.glGetObjectBufferiATI(buffer, 34660), old_buffer, function_pointer);
        return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
    }
    
    public static ByteBuffer glMapObjectBufferATI(final int buffer, final long length, final ByteBuffer old_buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMapObjectBufferATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (old_buffer != null) {
            BufferChecks.checkDirect(old_buffer);
        }
        final ByteBuffer __result = nglMapObjectBufferATI(buffer, length, old_buffer, function_pointer);
        return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
    }
    
    static native ByteBuffer nglMapObjectBufferATI(final int p0, final long p1, final ByteBuffer p2, final long p3);
    
    public static void glUnmapObjectBufferATI(final int buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUnmapObjectBufferATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUnmapObjectBufferATI(buffer, function_pointer);
    }
    
    static native void nglUnmapObjectBufferATI(final int p0, final long p1);
}

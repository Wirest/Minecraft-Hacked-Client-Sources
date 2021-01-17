// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ByteOrder;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public final class INTELMapTexture
{
    public static final int GL_TEXTURE_MEMORY_LAYOUT_INTEL = 33791;
    public static final int GL_LAYOUT_DEFAULT_INTEL = 0;
    public static final int GL_LAYOUT_LINEAR_INTEL = 1;
    public static final int GL_LAYOUT_LINEAR_CPU_CACHED_INTEL = 2;
    
    private INTELMapTexture() {
    }
    
    public static ByteBuffer glMapTexture2DINTEL(final int texture, final int level, final long length, final int access, final IntBuffer stride, final IntBuffer layout, final ByteBuffer old_buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMapTexture2DINTEL;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(stride, 1);
        BufferChecks.checkBuffer(layout, 1);
        if (old_buffer != null) {
            BufferChecks.checkDirect(old_buffer);
        }
        final ByteBuffer __result = nglMapTexture2DINTEL(texture, level, length, access, MemoryUtil.getAddress(stride), MemoryUtil.getAddress(layout), old_buffer, function_pointer);
        return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
    }
    
    static native ByteBuffer nglMapTexture2DINTEL(final int p0, final int p1, final long p2, final int p3, final long p4, final long p5, final ByteBuffer p6, final long p7);
    
    public static void glUnmapTexture2DINTEL(final int texture, final int level) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUnmapTexture2DINTEL;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUnmapTexture2DINTEL(texture, level, function_pointer);
    }
    
    static native void nglUnmapTexture2DINTEL(final int p0, final int p1, final long p2);
    
    public static void glSyncTextureINTEL(final int texture) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSyncTextureINTEL;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglSyncTextureINTEL(texture, function_pointer);
    }
    
    static native void nglSyncTextureINTEL(final int p0, final long p1);
}

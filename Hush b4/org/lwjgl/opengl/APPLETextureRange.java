// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.Buffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.ByteBuffer;

public final class APPLETextureRange
{
    public static final int GL_TEXTURE_STORAGE_HINT_APPLE = 34236;
    public static final int GL_STORAGE_PRIVATE_APPLE = 34237;
    public static final int GL_STORAGE_CACHED_APPLE = 34238;
    public static final int GL_STORAGE_SHARED_APPLE = 34239;
    public static final int GL_TEXTURE_RANGE_LENGTH_APPLE = 34231;
    public static final int GL_TEXTURE_RANGE_POINTER_APPLE = 34232;
    
    private APPLETextureRange() {
    }
    
    public static void glTextureRangeAPPLE(final int target, final ByteBuffer pointer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureRangeAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pointer);
        nglTextureRangeAPPLE(target, pointer.remaining(), MemoryUtil.getAddress(pointer), function_pointer);
    }
    
    static native void nglTextureRangeAPPLE(final int p0, final int p1, final long p2, final long p3);
    
    public static Buffer glGetTexParameterPointervAPPLE(final int target, final int pname, final long result_size) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTexParameterPointervAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        final Buffer __result = nglGetTexParameterPointervAPPLE(target, pname, result_size, function_pointer);
        return __result;
    }
    
    static native Buffer nglGetTexParameterPointervAPPLE(final int p0, final int p1, final long p2, final long p3);
}

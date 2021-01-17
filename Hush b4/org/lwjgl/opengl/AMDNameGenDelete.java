// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.IntBuffer;

public final class AMDNameGenDelete
{
    public static final int GL_DATA_BUFFER_AMD = 37201;
    public static final int GL_PERFORMANCE_MONITOR_AMD = 37202;
    public static final int GL_QUERY_OBJECT_AMD = 37203;
    public static final int GL_VERTEX_ARRAY_OBJECT_AMD = 37204;
    public static final int GL_SAMPLER_OBJECT_AMD = 37205;
    
    private AMDNameGenDelete() {
    }
    
    public static void glGenNamesAMD(final int identifier, final IntBuffer names) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenNamesAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(names);
        nglGenNamesAMD(identifier, names.remaining(), MemoryUtil.getAddress(names), function_pointer);
    }
    
    static native void nglGenNamesAMD(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGenNamesAMD(final int identifier) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenNamesAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer names = APIUtil.getBufferInt(caps);
        nglGenNamesAMD(identifier, 1, MemoryUtil.getAddress(names), function_pointer);
        return names.get(0);
    }
    
    public static void glDeleteNamesAMD(final int identifier, final IntBuffer names) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteNamesAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(names);
        nglDeleteNamesAMD(identifier, names.remaining(), MemoryUtil.getAddress(names), function_pointer);
    }
    
    static native void nglDeleteNamesAMD(final int p0, final int p1, final long p2, final long p3);
    
    public static void glDeleteNamesAMD(final int identifier, final int name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteNamesAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDeleteNamesAMD(identifier, 1, APIUtil.getInt(caps, name), function_pointer);
    }
    
    public static boolean glIsNameAMD(final int identifier, final int name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsNameAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsNameAMD(identifier, name, function_pointer);
        return __result;
    }
    
    static native boolean nglIsNameAMD(final int p0, final int p1, final long p2);
}

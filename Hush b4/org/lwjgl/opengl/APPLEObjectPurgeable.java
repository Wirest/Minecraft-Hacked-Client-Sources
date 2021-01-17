// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;

public final class APPLEObjectPurgeable
{
    public static final int GL_RELEASED_APPLE = 35353;
    public static final int GL_VOLATILE_APPLE = 35354;
    public static final int GL_RETAINED_APPLE = 35355;
    public static final int GL_UNDEFINED_APPLE = 35356;
    public static final int GL_PURGEABLE_APPLE = 35357;
    public static final int GL_BUFFER_OBJECT_APPLE = 34227;
    
    private APPLEObjectPurgeable() {
    }
    
    public static int glObjectPurgeableAPPLE(final int objectType, final int name, final int option) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glObjectPurgeableAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nglObjectPurgeableAPPLE(objectType, name, option, function_pointer);
        return __result;
    }
    
    static native int nglObjectPurgeableAPPLE(final int p0, final int p1, final int p2, final long p3);
    
    public static int glObjectUnpurgeableAPPLE(final int objectType, final int name, final int option) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glObjectUnpurgeableAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nglObjectUnpurgeableAPPLE(objectType, name, option, function_pointer);
        return __result;
    }
    
    static native int nglObjectUnpurgeableAPPLE(final int p0, final int p1, final int p2, final long p3);
    
    public static void glGetObjectParameterAPPLE(final int objectType, final int name, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetObjectParameterivAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetObjectParameterivAPPLE(objectType, name, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetObjectParameterivAPPLE(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static int glGetObjectParameteriAPPLE(final int objectType, final int name, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetObjectParameterivAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetObjectParameterivAPPLE(objectType, name, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
}

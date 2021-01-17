// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.IntBuffer;

public final class NVOcclusionQuery
{
    public static final int GL_OCCLUSION_TEST_HP = 33125;
    public static final int GL_OCCLUSION_TEST_RESULT_HP = 33126;
    public static final int GL_PIXEL_COUNTER_BITS_NV = 34916;
    public static final int GL_CURRENT_OCCLUSION_QUERY_ID_NV = 34917;
    public static final int GL_PIXEL_COUNT_NV = 34918;
    public static final int GL_PIXEL_COUNT_AVAILABLE_NV = 34919;
    
    private NVOcclusionQuery() {
    }
    
    public static void glGenOcclusionQueriesNV(final IntBuffer piIDs) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenOcclusionQueriesNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(piIDs);
        nglGenOcclusionQueriesNV(piIDs.remaining(), MemoryUtil.getAddress(piIDs), function_pointer);
    }
    
    static native void nglGenOcclusionQueriesNV(final int p0, final long p1, final long p2);
    
    public static int glGenOcclusionQueriesNV() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenOcclusionQueriesNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer piIDs = APIUtil.getBufferInt(caps);
        nglGenOcclusionQueriesNV(1, MemoryUtil.getAddress(piIDs), function_pointer);
        return piIDs.get(0);
    }
    
    public static void glDeleteOcclusionQueriesNV(final IntBuffer piIDs) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteOcclusionQueriesNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(piIDs);
        nglDeleteOcclusionQueriesNV(piIDs.remaining(), MemoryUtil.getAddress(piIDs), function_pointer);
    }
    
    static native void nglDeleteOcclusionQueriesNV(final int p0, final long p1, final long p2);
    
    public static void glDeleteOcclusionQueriesNV(final int piID) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteOcclusionQueriesNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDeleteOcclusionQueriesNV(1, APIUtil.getInt(caps, piID), function_pointer);
    }
    
    public static boolean glIsOcclusionQueryNV(final int id) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsOcclusionQueryNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsOcclusionQueryNV(id, function_pointer);
        return __result;
    }
    
    static native boolean nglIsOcclusionQueryNV(final int p0, final long p1);
    
    public static void glBeginOcclusionQueryNV(final int id) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBeginOcclusionQueryNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBeginOcclusionQueryNV(id, function_pointer);
    }
    
    static native void nglBeginOcclusionQueryNV(final int p0, final long p1);
    
    public static void glEndOcclusionQueryNV() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glEndOcclusionQueryNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglEndOcclusionQueryNV(function_pointer);
    }
    
    static native void nglEndOcclusionQueryNV(final long p0);
    
    public static void glGetOcclusionQueryuNV(final int id, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetOcclusionQueryuivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetOcclusionQueryuivNV(id, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetOcclusionQueryuivNV(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetOcclusionQueryuiNV(final int id, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetOcclusionQueryuivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetOcclusionQueryuivNV(id, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetOcclusionQueryNV(final int id, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetOcclusionQueryivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetOcclusionQueryivNV(id, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetOcclusionQueryivNV(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetOcclusionQueryiNV(final int id, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetOcclusionQueryivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetOcclusionQueryivNV(id, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
}

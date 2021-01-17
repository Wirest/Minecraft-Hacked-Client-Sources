// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.IntBuffer;

public final class ARBOcclusionQuery
{
    public static final int GL_SAMPLES_PASSED_ARB = 35092;
    public static final int GL_QUERY_COUNTER_BITS_ARB = 34916;
    public static final int GL_CURRENT_QUERY_ARB = 34917;
    public static final int GL_QUERY_RESULT_ARB = 34918;
    public static final int GL_QUERY_RESULT_AVAILABLE_ARB = 34919;
    
    private ARBOcclusionQuery() {
    }
    
    public static void glGenQueriesARB(final IntBuffer ids) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenQueriesARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(ids);
        nglGenQueriesARB(ids.remaining(), MemoryUtil.getAddress(ids), function_pointer);
    }
    
    static native void nglGenQueriesARB(final int p0, final long p1, final long p2);
    
    public static int glGenQueriesARB() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenQueriesARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer ids = APIUtil.getBufferInt(caps);
        nglGenQueriesARB(1, MemoryUtil.getAddress(ids), function_pointer);
        return ids.get(0);
    }
    
    public static void glDeleteQueriesARB(final IntBuffer ids) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteQueriesARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(ids);
        nglDeleteQueriesARB(ids.remaining(), MemoryUtil.getAddress(ids), function_pointer);
    }
    
    static native void nglDeleteQueriesARB(final int p0, final long p1, final long p2);
    
    public static void glDeleteQueriesARB(final int id) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteQueriesARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDeleteQueriesARB(1, APIUtil.getInt(caps, id), function_pointer);
    }
    
    public static boolean glIsQueryARB(final int id) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsQueryARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsQueryARB(id, function_pointer);
        return __result;
    }
    
    static native boolean nglIsQueryARB(final int p0, final long p1);
    
    public static void glBeginQueryARB(final int target, final int id) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBeginQueryARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBeginQueryARB(target, id, function_pointer);
    }
    
    static native void nglBeginQueryARB(final int p0, final int p1, final long p2);
    
    public static void glEndQueryARB(final int target) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glEndQueryARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglEndQueryARB(target, function_pointer);
    }
    
    static native void nglEndQueryARB(final int p0, final long p1);
    
    public static void glGetQueryARB(final int target, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetQueryivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetQueryivARB(target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetQueryivARB(final int p0, final int p1, final long p2, final long p3);
    
    @Deprecated
    public static int glGetQueryARB(final int target, final int pname) {
        return glGetQueryiARB(target, pname);
    }
    
    public static int glGetQueryiARB(final int target, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetQueryivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetQueryivARB(target, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetQueryObjectARB(final int id, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetQueryObjectivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetQueryObjectivARB(id, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetQueryObjectivARB(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetQueryObjectiARB(final int id, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetQueryObjectivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetQueryObjectivARB(id, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetQueryObjectuARB(final int id, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetQueryObjectuivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetQueryObjectuivARB(id, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetQueryObjectuivARB(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetQueryObjectuiARB(final int id, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetQueryObjectuivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetQueryObjectuivARB(id, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
}

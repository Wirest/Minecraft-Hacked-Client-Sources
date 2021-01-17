// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class EXTProvokingVertex
{
    public static final int GL_FIRST_VERTEX_CONVENTION_EXT = 36429;
    public static final int GL_LAST_VERTEX_CONVENTION_EXT = 36430;
    public static final int GL_PROVOKING_VERTEX_EXT = 36431;
    public static final int GL_QUADS_FOLLOW_PROVOKING_VERTEX_CONVENTION_EXT = 36428;
    
    private EXTProvokingVertex() {
    }
    
    public static void glProvokingVertexEXT(final int mode) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProvokingVertexEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProvokingVertexEXT(mode, function_pointer);
    }
    
    static native void nglProvokingVertexEXT(final int p0, final long p1);
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class EXTCompiledVertexArray
{
    public static final int GL_ARRAY_ELEMENT_LOCK_FIRST_EXT = 33192;
    public static final int GL_ARRAY_ELEMENT_LOCK_COUNT_EXT = 33193;
    
    private EXTCompiledVertexArray() {
    }
    
    public static void glLockArraysEXT(final int first, final int count) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glLockArraysEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglLockArraysEXT(first, count, function_pointer);
    }
    
    static native void nglLockArraysEXT(final int p0, final int p1, final long p2);
    
    public static void glUnlockArraysEXT() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUnlockArraysEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUnlockArraysEXT(function_pointer);
    }
    
    static native void nglUnlockArraysEXT(final long p0);
}

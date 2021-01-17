// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.IntBuffer;

public final class EXTMultiDrawArrays
{
    private EXTMultiDrawArrays() {
    }
    
    public static void glMultiDrawArraysEXT(final int mode, final IntBuffer piFirst, final IntBuffer piCount) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiDrawArraysEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(piFirst);
        BufferChecks.checkBuffer(piCount, piFirst.remaining());
        nglMultiDrawArraysEXT(mode, MemoryUtil.getAddress(piFirst), MemoryUtil.getAddress(piCount), piFirst.remaining(), function_pointer);
    }
    
    static native void nglMultiDrawArraysEXT(final int p0, final long p1, final long p2, final int p3, final long p4);
}

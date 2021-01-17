// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ARBSparseBuffer
{
    public static final int GL_SPARSE_STORAGE_BIT_ARB = 1024;
    public static final int GL_SPARSE_BUFFER_PAGE_SIZE_ARB = 33528;
    
    private ARBSparseBuffer() {
    }
    
    public static void glBufferPageCommitmentARB(final int target, final long offset, final long size, final boolean commit) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferPageCommitmentARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBufferPageCommitmentARB(target, offset, size, commit, function_pointer);
    }
    
    static native void nglBufferPageCommitmentARB(final int p0, final long p1, final long p2, final boolean p3, final long p4);
}

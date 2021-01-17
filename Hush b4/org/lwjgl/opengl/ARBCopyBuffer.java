// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

public final class ARBCopyBuffer
{
    public static final int GL_COPY_READ_BUFFER = 36662;
    public static final int GL_COPY_WRITE_BUFFER = 36663;
    
    private ARBCopyBuffer() {
    }
    
    public static void glCopyBufferSubData(final int readTarget, final int writeTarget, final long readOffset, final long writeOffset, final long size) {
        GL31.glCopyBufferSubData(readTarget, writeTarget, readOffset, writeOffset, size);
    }
}

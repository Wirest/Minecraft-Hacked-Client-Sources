// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ByteBuffer;

public final class ARBMapBufferRange
{
    public static final int GL_MAP_READ_BIT = 1;
    public static final int GL_MAP_WRITE_BIT = 2;
    public static final int GL_MAP_INVALIDATE_RANGE_BIT = 4;
    public static final int GL_MAP_INVALIDATE_BUFFER_BIT = 8;
    public static final int GL_MAP_FLUSH_EXPLICIT_BIT = 16;
    public static final int GL_MAP_UNSYNCHRONIZED_BIT = 32;
    
    private ARBMapBufferRange() {
    }
    
    public static ByteBuffer glMapBufferRange(final int target, final long offset, final long length, final int access, final ByteBuffer old_buffer) {
        return GL30.glMapBufferRange(target, offset, length, access, old_buffer);
    }
    
    public static void glFlushMappedBufferRange(final int target, final long offset, final long length) {
        GL30.glFlushMappedBufferRange(target, offset, length);
    }
}

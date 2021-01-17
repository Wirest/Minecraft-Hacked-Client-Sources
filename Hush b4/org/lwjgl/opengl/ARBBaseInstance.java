// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ShortBuffer;
import java.nio.IntBuffer;
import java.nio.ByteBuffer;

public final class ARBBaseInstance
{
    private ARBBaseInstance() {
    }
    
    public static void glDrawArraysInstancedBaseInstance(final int mode, final int first, final int count, final int primcount, final int baseinstance) {
        GL42.glDrawArraysInstancedBaseInstance(mode, first, count, primcount, baseinstance);
    }
    
    public static void glDrawElementsInstancedBaseInstance(final int mode, final ByteBuffer indices, final int primcount, final int baseinstance) {
        GL42.glDrawElementsInstancedBaseInstance(mode, indices, primcount, baseinstance);
    }
    
    public static void glDrawElementsInstancedBaseInstance(final int mode, final IntBuffer indices, final int primcount, final int baseinstance) {
        GL42.glDrawElementsInstancedBaseInstance(mode, indices, primcount, baseinstance);
    }
    
    public static void glDrawElementsInstancedBaseInstance(final int mode, final ShortBuffer indices, final int primcount, final int baseinstance) {
        GL42.glDrawElementsInstancedBaseInstance(mode, indices, primcount, baseinstance);
    }
    
    public static void glDrawElementsInstancedBaseInstance(final int mode, final int indices_count, final int type, final long indices_buffer_offset, final int primcount, final int baseinstance) {
        GL42.glDrawElementsInstancedBaseInstance(mode, indices_count, type, indices_buffer_offset, primcount, baseinstance);
    }
    
    public static void glDrawElementsInstancedBaseVertexBaseInstance(final int mode, final ByteBuffer indices, final int primcount, final int basevertex, final int baseinstance) {
        GL42.glDrawElementsInstancedBaseVertexBaseInstance(mode, indices, primcount, basevertex, baseinstance);
    }
    
    public static void glDrawElementsInstancedBaseVertexBaseInstance(final int mode, final IntBuffer indices, final int primcount, final int basevertex, final int baseinstance) {
        GL42.glDrawElementsInstancedBaseVertexBaseInstance(mode, indices, primcount, basevertex, baseinstance);
    }
    
    public static void glDrawElementsInstancedBaseVertexBaseInstance(final int mode, final ShortBuffer indices, final int primcount, final int basevertex, final int baseinstance) {
        GL42.glDrawElementsInstancedBaseVertexBaseInstance(mode, indices, primcount, basevertex, baseinstance);
    }
    
    public static void glDrawElementsInstancedBaseVertexBaseInstance(final int mode, final int indices_count, final int type, final long indices_buffer_offset, final int primcount, final int basevertex, final int baseinstance) {
        GL42.glDrawElementsInstancedBaseVertexBaseInstance(mode, indices_count, type, indices_buffer_offset, primcount, basevertex, baseinstance);
    }
}

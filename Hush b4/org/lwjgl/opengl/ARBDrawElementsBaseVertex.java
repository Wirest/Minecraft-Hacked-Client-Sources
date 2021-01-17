// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ShortBuffer;
import java.nio.IntBuffer;
import java.nio.ByteBuffer;

public final class ARBDrawElementsBaseVertex
{
    private ARBDrawElementsBaseVertex() {
    }
    
    public static void glDrawElementsBaseVertex(final int mode, final ByteBuffer indices, final int basevertex) {
        GL32.glDrawElementsBaseVertex(mode, indices, basevertex);
    }
    
    public static void glDrawElementsBaseVertex(final int mode, final IntBuffer indices, final int basevertex) {
        GL32.glDrawElementsBaseVertex(mode, indices, basevertex);
    }
    
    public static void glDrawElementsBaseVertex(final int mode, final ShortBuffer indices, final int basevertex) {
        GL32.glDrawElementsBaseVertex(mode, indices, basevertex);
    }
    
    public static void glDrawElementsBaseVertex(final int mode, final int indices_count, final int type, final long indices_buffer_offset, final int basevertex) {
        GL32.glDrawElementsBaseVertex(mode, indices_count, type, indices_buffer_offset, basevertex);
    }
    
    public static void glDrawRangeElementsBaseVertex(final int mode, final int start, final int end, final ByteBuffer indices, final int basevertex) {
        GL32.glDrawRangeElementsBaseVertex(mode, start, end, indices, basevertex);
    }
    
    public static void glDrawRangeElementsBaseVertex(final int mode, final int start, final int end, final IntBuffer indices, final int basevertex) {
        GL32.glDrawRangeElementsBaseVertex(mode, start, end, indices, basevertex);
    }
    
    public static void glDrawRangeElementsBaseVertex(final int mode, final int start, final int end, final ShortBuffer indices, final int basevertex) {
        GL32.glDrawRangeElementsBaseVertex(mode, start, end, indices, basevertex);
    }
    
    public static void glDrawRangeElementsBaseVertex(final int mode, final int start, final int end, final int indices_count, final int type, final long indices_buffer_offset, final int basevertex) {
        GL32.glDrawRangeElementsBaseVertex(mode, start, end, indices_count, type, indices_buffer_offset, basevertex);
    }
    
    public static void glDrawElementsInstancedBaseVertex(final int mode, final ByteBuffer indices, final int primcount, final int basevertex) {
        GL32.glDrawElementsInstancedBaseVertex(mode, indices, primcount, basevertex);
    }
    
    public static void glDrawElementsInstancedBaseVertex(final int mode, final IntBuffer indices, final int primcount, final int basevertex) {
        GL32.glDrawElementsInstancedBaseVertex(mode, indices, primcount, basevertex);
    }
    
    public static void glDrawElementsInstancedBaseVertex(final int mode, final ShortBuffer indices, final int primcount, final int basevertex) {
        GL32.glDrawElementsInstancedBaseVertex(mode, indices, primcount, basevertex);
    }
    
    public static void glDrawElementsInstancedBaseVertex(final int mode, final int indices_count, final int type, final long indices_buffer_offset, final int primcount, final int basevertex) {
        GL32.glDrawElementsInstancedBaseVertex(mode, indices_count, type, indices_buffer_offset, primcount, basevertex);
    }
}

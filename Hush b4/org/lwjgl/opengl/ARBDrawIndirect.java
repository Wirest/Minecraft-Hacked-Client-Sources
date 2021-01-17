// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.IntBuffer;
import java.nio.ByteBuffer;

public final class ARBDrawIndirect
{
    public static final int GL_DRAW_INDIRECT_BUFFER = 36671;
    public static final int GL_DRAW_INDIRECT_BUFFER_BINDING = 36675;
    
    private ARBDrawIndirect() {
    }
    
    public static void glDrawArraysIndirect(final int mode, final ByteBuffer indirect) {
        GL40.glDrawArraysIndirect(mode, indirect);
    }
    
    public static void glDrawArraysIndirect(final int mode, final long indirect_buffer_offset) {
        GL40.glDrawArraysIndirect(mode, indirect_buffer_offset);
    }
    
    public static void glDrawArraysIndirect(final int mode, final IntBuffer indirect) {
        GL40.glDrawArraysIndirect(mode, indirect);
    }
    
    public static void glDrawElementsIndirect(final int mode, final int type, final ByteBuffer indirect) {
        GL40.glDrawElementsIndirect(mode, type, indirect);
    }
    
    public static void glDrawElementsIndirect(final int mode, final int type, final long indirect_buffer_offset) {
        GL40.glDrawElementsIndirect(mode, type, indirect_buffer_offset);
    }
    
    public static void glDrawElementsIndirect(final int mode, final int type, final IntBuffer indirect) {
        GL40.glDrawElementsIndirect(mode, type, indirect);
    }
}

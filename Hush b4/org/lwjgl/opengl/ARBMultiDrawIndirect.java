// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.IntBuffer;
import java.nio.ByteBuffer;

public final class ARBMultiDrawIndirect
{
    private ARBMultiDrawIndirect() {
    }
    
    public static void glMultiDrawArraysIndirect(final int mode, final ByteBuffer indirect, final int primcount, final int stride) {
        GL43.glMultiDrawArraysIndirect(mode, indirect, primcount, stride);
    }
    
    public static void glMultiDrawArraysIndirect(final int mode, final long indirect_buffer_offset, final int primcount, final int stride) {
        GL43.glMultiDrawArraysIndirect(mode, indirect_buffer_offset, primcount, stride);
    }
    
    public static void glMultiDrawArraysIndirect(final int mode, final IntBuffer indirect, final int primcount, final int stride) {
        GL43.glMultiDrawArraysIndirect(mode, indirect, primcount, stride);
    }
    
    public static void glMultiDrawElementsIndirect(final int mode, final int type, final ByteBuffer indirect, final int primcount, final int stride) {
        GL43.glMultiDrawElementsIndirect(mode, type, indirect, primcount, stride);
    }
    
    public static void glMultiDrawElementsIndirect(final int mode, final int type, final long indirect_buffer_offset, final int primcount, final int stride) {
        GL43.glMultiDrawElementsIndirect(mode, type, indirect_buffer_offset, primcount, stride);
    }
    
    public static void glMultiDrawElementsIndirect(final int mode, final int type, final IntBuffer indirect, final int primcount, final int stride) {
        GL43.glMultiDrawElementsIndirect(mode, type, indirect, primcount, stride);
    }
}

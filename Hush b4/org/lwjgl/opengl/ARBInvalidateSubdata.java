// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.IntBuffer;

public final class ARBInvalidateSubdata
{
    private ARBInvalidateSubdata() {
    }
    
    public static void glInvalidateTexSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth) {
        GL43.glInvalidateTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth);
    }
    
    public static void glInvalidateTexImage(final int texture, final int level) {
        GL43.glInvalidateTexImage(texture, level);
    }
    
    public static void glInvalidateBufferSubData(final int buffer, final long offset, final long length) {
        GL43.glInvalidateBufferSubData(buffer, offset, length);
    }
    
    public static void glInvalidateBufferData(final int buffer) {
        GL43.glInvalidateBufferData(buffer);
    }
    
    public static void glInvalidateFramebuffer(final int target, final IntBuffer attachments) {
        GL43.glInvalidateFramebuffer(target, attachments);
    }
    
    public static void glInvalidateSubFramebuffer(final int target, final IntBuffer attachments, final int x, final int y, final int width, final int height) {
        GL43.glInvalidateSubFramebuffer(target, attachments, x, y, width, height);
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import java.nio.DoubleBuffer;
import java.nio.ByteBuffer;

public final class ARBClearTexture
{
    public static final int GL_CLEAR_TEXTURE = 37733;
    
    private ARBClearTexture() {
    }
    
    public static void glClearTexImage(final int texture, final int level, final int format, final int type, final ByteBuffer data) {
        GL44.glClearTexImage(texture, level, format, type, data);
    }
    
    public static void glClearTexImage(final int texture, final int level, final int format, final int type, final DoubleBuffer data) {
        GL44.glClearTexImage(texture, level, format, type, data);
    }
    
    public static void glClearTexImage(final int texture, final int level, final int format, final int type, final FloatBuffer data) {
        GL44.glClearTexImage(texture, level, format, type, data);
    }
    
    public static void glClearTexImage(final int texture, final int level, final int format, final int type, final IntBuffer data) {
        GL44.glClearTexImage(texture, level, format, type, data);
    }
    
    public static void glClearTexImage(final int texture, final int level, final int format, final int type, final ShortBuffer data) {
        GL44.glClearTexImage(texture, level, format, type, data);
    }
    
    public static void glClearTexImage(final int texture, final int level, final int format, final int type, final LongBuffer data) {
        GL44.glClearTexImage(texture, level, format, type, data);
    }
    
    public static void glClearTexSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final ByteBuffer data) {
        GL44.glClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, data);
    }
    
    public static void glClearTexSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final DoubleBuffer data) {
        GL44.glClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, data);
    }
    
    public static void glClearTexSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final FloatBuffer data) {
        GL44.glClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, data);
    }
    
    public static void glClearTexSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final IntBuffer data) {
        GL44.glClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, data);
    }
    
    public static void glClearTexSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final ShortBuffer data) {
        GL44.glClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, data);
    }
    
    public static void glClearTexSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final LongBuffer data) {
        GL44.glClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, data);
    }
}

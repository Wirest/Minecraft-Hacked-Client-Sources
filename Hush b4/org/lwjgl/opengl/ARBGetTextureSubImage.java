// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ShortBuffer;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import java.nio.DoubleBuffer;
import java.nio.ByteBuffer;

public final class ARBGetTextureSubImage
{
    private ARBGetTextureSubImage() {
    }
    
    public static void glGetTextureSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final ByteBuffer pixels) {
        GL45.glGetTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels);
    }
    
    public static void glGetTextureSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final DoubleBuffer pixels) {
        GL45.glGetTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels);
    }
    
    public static void glGetTextureSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final FloatBuffer pixels) {
        GL45.glGetTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels);
    }
    
    public static void glGetTextureSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final IntBuffer pixels) {
        GL45.glGetTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels);
    }
    
    public static void glGetTextureSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final ShortBuffer pixels) {
        GL45.glGetTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels);
    }
    
    public static void glGetTextureSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int format, final int type, final int pixels_bufSize, final long pixels_buffer_offset) {
        GL45.glGetTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels_bufSize, pixels_buffer_offset);
    }
    
    public static void glGetCompressedTextureSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final ByteBuffer pixels) {
        GL45.glGetCompressedTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, pixels);
    }
    
    public static void glGetCompressedTextureSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final DoubleBuffer pixels) {
        GL45.glGetCompressedTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, pixels);
    }
    
    public static void glGetCompressedTextureSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final FloatBuffer pixels) {
        GL45.glGetCompressedTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, pixels);
    }
    
    public static void glGetCompressedTextureSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final IntBuffer pixels) {
        GL45.glGetCompressedTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, pixels);
    }
    
    public static void glGetCompressedTextureSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final ShortBuffer pixels) {
        GL45.glGetCompressedTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, pixels);
    }
    
    public static void glGetCompressedTextureSubImage(final int texture, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final int pixels_bufSize, final long pixels_buffer_offset) {
        GL45.glGetCompressedTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, pixels_bufSize, pixels_buffer_offset);
    }
}

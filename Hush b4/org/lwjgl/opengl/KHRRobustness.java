// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ShortBuffer;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import java.nio.DoubleBuffer;
import java.nio.ByteBuffer;

public final class KHRRobustness
{
    public static final int GL_GUILTY_CONTEXT_RESET = 33363;
    public static final int GL_INNOCENT_CONTEXT_RESET = 33364;
    public static final int GL_UNKNOWN_CONTEXT_RESET = 33365;
    public static final int GL_CONTEXT_ROBUST_ACCESS = 37107;
    public static final int GL_RESET_NOTIFICATION_STRATEGY = 33366;
    public static final int GL_LOSE_CONTEXT_ON_RESET = 33362;
    public static final int GL_NO_RESET_NOTIFICATION = 33377;
    public static final int GL_CONTEXT_LOST = 1287;
    
    private KHRRobustness() {
    }
    
    public static int glGetGraphicsResetStatus() {
        return GL45.glGetGraphicsResetStatus();
    }
    
    public static void glReadnPixels(final int x, final int y, final int width, final int height, final int format, final int type, final ByteBuffer pixels) {
        GL45.glReadnPixels(x, y, width, height, format, type, pixels);
    }
    
    public static void glReadnPixels(final int x, final int y, final int width, final int height, final int format, final int type, final DoubleBuffer pixels) {
        GL45.glReadnPixels(x, y, width, height, format, type, pixels);
    }
    
    public static void glReadnPixels(final int x, final int y, final int width, final int height, final int format, final int type, final FloatBuffer pixels) {
        GL45.glReadnPixels(x, y, width, height, format, type, pixels);
    }
    
    public static void glReadnPixels(final int x, final int y, final int width, final int height, final int format, final int type, final IntBuffer pixels) {
        GL45.glReadnPixels(x, y, width, height, format, type, pixels);
    }
    
    public static void glReadnPixels(final int x, final int y, final int width, final int height, final int format, final int type, final ShortBuffer pixels) {
        GL45.glReadnPixels(x, y, width, height, format, type, pixels);
    }
    
    public static void glReadnPixels(final int x, final int y, final int width, final int height, final int format, final int type, final int pixels_bufSize, final long pixels_buffer_offset) {
        GL45.glReadnPixels(x, y, width, height, format, type, pixels_bufSize, pixels_buffer_offset);
    }
    
    public static void glGetnUniform(final int program, final int location, final FloatBuffer params) {
        GL45.glGetnUniform(program, location, params);
    }
    
    public static void glGetnUniform(final int program, final int location, final IntBuffer params) {
        GL45.glGetnUniform(program, location, params);
    }
    
    public static void glGetnUniformu(final int program, final int location, final IntBuffer params) {
        GL45.glGetnUniformu(program, location, params);
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;

public final class ARBViewportArray
{
    public static final int GL_MAX_VIEWPORTS = 33371;
    public static final int GL_VIEWPORT_SUBPIXEL_BITS = 33372;
    public static final int GL_VIEWPORT_BOUNDS_RANGE = 33373;
    public static final int GL_LAYER_PROVOKING_VERTEX = 33374;
    public static final int GL_VIEWPORT_INDEX_PROVOKING_VERTEX = 33375;
    public static final int GL_FIRST_VERTEX_CONVENTION = 36429;
    public static final int GL_LAST_VERTEX_CONVENTION = 36430;
    public static final int GL_PROVOKING_VERTEX = 36431;
    public static final int GL_UNDEFINED_VERTEX = 33376;
    
    private ARBViewportArray() {
    }
    
    public static void glViewportArray(final int first, final FloatBuffer v) {
        GL41.glViewportArray(first, v);
    }
    
    public static void glViewportIndexedf(final int index, final float x, final float y, final float w, final float h) {
        GL41.glViewportIndexedf(index, x, y, w, h);
    }
    
    public static void glViewportIndexed(final int index, final FloatBuffer v) {
        GL41.glViewportIndexed(index, v);
    }
    
    public static void glScissorArray(final int first, final IntBuffer v) {
        GL41.glScissorArray(first, v);
    }
    
    public static void glScissorIndexed(final int index, final int left, final int bottom, final int width, final int height) {
        GL41.glScissorIndexed(index, left, bottom, width, height);
    }
    
    public static void glScissorIndexed(final int index, final IntBuffer v) {
        GL41.glScissorIndexed(index, v);
    }
    
    public static void glDepthRangeArray(final int first, final DoubleBuffer v) {
        GL41.glDepthRangeArray(first, v);
    }
    
    public static void glDepthRangeIndexed(final int index, final double n, final double f) {
        GL41.glDepthRangeIndexed(index, n, f);
    }
    
    public static void glGetFloat(final int target, final int index, final FloatBuffer data) {
        GL41.glGetFloat(target, index, data);
    }
    
    public static float glGetFloat(final int target, final int index) {
        return GL41.glGetFloat(target, index);
    }
    
    public static void glGetDouble(final int target, final int index, final DoubleBuffer data) {
        GL41.glGetDouble(target, index, data);
    }
    
    public static double glGetDouble(final int target, final int index) {
        return GL41.glGetDouble(target, index);
    }
    
    public static void glGetIntegerIndexedEXT(final int target, final int index, final IntBuffer v) {
        EXTDrawBuffers2.glGetIntegerIndexedEXT(target, index, v);
    }
    
    public static int glGetIntegerIndexedEXT(final int target, final int index) {
        return EXTDrawBuffers2.glGetIntegerIndexedEXT(target, index);
    }
    
    public static void glEnableIndexedEXT(final int target, final int index) {
        EXTDrawBuffers2.glEnableIndexedEXT(target, index);
    }
    
    public static void glDisableIndexedEXT(final int target, final int index) {
        EXTDrawBuffers2.glDisableIndexedEXT(target, index);
    }
    
    public static boolean glIsEnabledIndexedEXT(final int target, final int index) {
        return EXTDrawBuffers2.glIsEnabledIndexedEXT(target, index);
    }
}

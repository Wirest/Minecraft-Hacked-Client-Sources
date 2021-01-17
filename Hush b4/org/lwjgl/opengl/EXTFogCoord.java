// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.FloatBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.LWJGLUtil;
import java.nio.DoubleBuffer;
import org.lwjgl.BufferChecks;

public final class EXTFogCoord
{
    public static final int GL_FOG_COORDINATE_SOURCE_EXT = 33872;
    public static final int GL_FOG_COORDINATE_EXT = 33873;
    public static final int GL_FRAGMENT_DEPTH_EXT = 33874;
    public static final int GL_CURRENT_FOG_COORDINATE_EXT = 33875;
    public static final int GL_FOG_COORDINATE_ARRAY_TYPE_EXT = 33876;
    public static final int GL_FOG_COORDINATE_ARRAY_STRIDE_EXT = 33877;
    public static final int GL_FOG_COORDINATE_ARRAY_POINTER_EXT = 33878;
    public static final int GL_FOG_COORDINATE_ARRAY_EXT = 33879;
    
    private EXTFogCoord() {
    }
    
    public static void glFogCoordfEXT(final float coord) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFogCoordfEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglFogCoordfEXT(coord, function_pointer);
    }
    
    static native void nglFogCoordfEXT(final float p0, final long p1);
    
    public static void glFogCoorddEXT(final double coord) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFogCoorddEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglFogCoorddEXT(coord, function_pointer);
    }
    
    static native void nglFogCoorddEXT(final double p0, final long p1);
    
    public static void glFogCoordPointerEXT(final int stride, final DoubleBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFogCoordPointerEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(data);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).EXT_fog_coord_glFogCoordPointerEXT_data = data;
        }
        nglFogCoordPointerEXT(5130, stride, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glFogCoordPointerEXT(final int stride, final FloatBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFogCoordPointerEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(data);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).EXT_fog_coord_glFogCoordPointerEXT_data = data;
        }
        nglFogCoordPointerEXT(5126, stride, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglFogCoordPointerEXT(final int p0, final int p1, final long p2, final long p3);
    
    public static void glFogCoordPointerEXT(final int type, final int stride, final long data_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFogCoordPointerEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOenabled(caps);
        nglFogCoordPointerEXTBO(type, stride, data_buffer_offset, function_pointer);
    }
    
    static native void nglFogCoordPointerEXTBO(final int p0, final int p1, final long p2, final long p3);
}

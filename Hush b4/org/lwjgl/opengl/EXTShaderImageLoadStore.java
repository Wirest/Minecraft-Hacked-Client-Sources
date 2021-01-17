// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class EXTShaderImageLoadStore
{
    public static final int GL_MAX_IMAGE_UNITS_EXT = 36664;
    public static final int GL_MAX_COMBINED_IMAGE_UNITS_AND_FRAGMENT_OUTPUTS_EXT = 36665;
    public static final int GL_MAX_IMAGE_SAMPLES_EXT = 36973;
    public static final int GL_IMAGE_BINDING_NAME_EXT = 36666;
    public static final int GL_IMAGE_BINDING_LEVEL_EXT = 36667;
    public static final int GL_IMAGE_BINDING_LAYERED_EXT = 36668;
    public static final int GL_IMAGE_BINDING_LAYER_EXT = 36669;
    public static final int GL_IMAGE_BINDING_ACCESS_EXT = 36670;
    public static final int GL_IMAGE_BINDING_FORMAT_EXT = 36974;
    public static final int GL_VERTEX_ATTRIB_ARRAY_BARRIER_BIT_EXT = 1;
    public static final int GL_ELEMENT_ARRAY_BARRIER_BIT_EXT = 2;
    public static final int GL_UNIFORM_BARRIER_BIT_EXT = 4;
    public static final int GL_TEXTURE_FETCH_BARRIER_BIT_EXT = 8;
    public static final int GL_SHADER_IMAGE_ACCESS_BARRIER_BIT_EXT = 32;
    public static final int GL_COMMAND_BARRIER_BIT_EXT = 64;
    public static final int GL_PIXEL_BUFFER_BARRIER_BIT_EXT = 128;
    public static final int GL_TEXTURE_UPDATE_BARRIER_BIT_EXT = 256;
    public static final int GL_BUFFER_UPDATE_BARRIER_BIT_EXT = 512;
    public static final int GL_FRAMEBUFFER_BARRIER_BIT_EXT = 1024;
    public static final int GL_TRANSFORM_FEEDBACK_BARRIER_BIT_EXT = 2048;
    public static final int GL_ATOMIC_COUNTER_BARRIER_BIT_EXT = 4096;
    public static final int GL_ALL_BARRIER_BITS_EXT = -1;
    public static final int GL_IMAGE_1D_EXT = 36940;
    public static final int GL_IMAGE_2D_EXT = 36941;
    public static final int GL_IMAGE_3D_EXT = 36942;
    public static final int GL_IMAGE_2D_RECT_EXT = 36943;
    public static final int GL_IMAGE_CUBE_EXT = 36944;
    public static final int GL_IMAGE_BUFFER_EXT = 36945;
    public static final int GL_IMAGE_1D_ARRAY_EXT = 36946;
    public static final int GL_IMAGE_2D_ARRAY_EXT = 36947;
    public static final int GL_IMAGE_CUBE_MAP_ARRAY_EXT = 36948;
    public static final int GL_IMAGE_2D_MULTISAMPLE_EXT = 36949;
    public static final int GL_IMAGE_2D_MULTISAMPLE_ARRAY_EXT = 36950;
    public static final int GL_INT_IMAGE_1D_EXT = 36951;
    public static final int GL_INT_IMAGE_2D_EXT = 36952;
    public static final int GL_INT_IMAGE_3D_EXT = 36953;
    public static final int GL_INT_IMAGE_2D_RECT_EXT = 36954;
    public static final int GL_INT_IMAGE_CUBE_EXT = 36955;
    public static final int GL_INT_IMAGE_BUFFER_EXT = 36956;
    public static final int GL_INT_IMAGE_1D_ARRAY_EXT = 36957;
    public static final int GL_INT_IMAGE_2D_ARRAY_EXT = 36958;
    public static final int GL_INT_IMAGE_CUBE_MAP_ARRAY_EXT = 36959;
    public static final int GL_INT_IMAGE_2D_MULTISAMPLE_EXT = 36960;
    public static final int GL_INT_IMAGE_2D_MULTISAMPLE_ARRAY_EXT = 36961;
    public static final int GL_UNSIGNED_INT_IMAGE_1D_EXT = 36962;
    public static final int GL_UNSIGNED_INT_IMAGE_2D_EXT = 36963;
    public static final int GL_UNSIGNED_INT_IMAGE_3D_EXT = 36964;
    public static final int GL_UNSIGNED_INT_IMAGE_2D_RECT_EXT = 36965;
    public static final int GL_UNSIGNED_INT_IMAGE_CUBE_EXT = 36966;
    public static final int GL_UNSIGNED_INT_IMAGE_BUFFER_EXT = 36967;
    public static final int GL_UNSIGNED_INT_IMAGE_1D_ARRAY_EXT = 36968;
    public static final int GL_UNSIGNED_INT_IMAGE_2D_ARRAY_EXT = 36969;
    public static final int GL_UNSIGNED_INT_IMAGE_CUBE_MAP_ARRAY_EXT = 36970;
    public static final int GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_EXT = 36971;
    public static final int GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_ARRAY_EXT = 36972;
    
    private EXTShaderImageLoadStore() {
    }
    
    public static void glBindImageTextureEXT(final int index, final int texture, final int level, final boolean layered, final int layer, final int access, final int format) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindImageTextureEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBindImageTextureEXT(index, texture, level, layered, layer, access, format, function_pointer);
    }
    
    static native void nglBindImageTextureEXT(final int p0, final int p1, final int p2, final boolean p3, final int p4, final int p5, final int p6, final long p7);
    
    public static void glMemoryBarrierEXT(final int barriers) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMemoryBarrierEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMemoryBarrierEXT(barriers, function_pointer);
    }
    
    static native void nglMemoryBarrierEXT(final int p0, final long p1);
}

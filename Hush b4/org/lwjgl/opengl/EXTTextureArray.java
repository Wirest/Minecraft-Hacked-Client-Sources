// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

public final class EXTTextureArray
{
    public static final int GL_TEXTURE_1D_ARRAY_EXT = 35864;
    public static final int GL_TEXTURE_2D_ARRAY_EXT = 35866;
    public static final int GL_PROXY_TEXTURE_2D_ARRAY_EXT = 35867;
    public static final int GL_PROXY_TEXTURE_1D_ARRAY_EXT = 35865;
    public static final int GL_TEXTURE_BINDING_1D_ARRAY_EXT = 35868;
    public static final int GL_TEXTURE_BINDING_2D_ARRAY_EXT = 35869;
    public static final int GL_MAX_ARRAY_TEXTURE_LAYERS_EXT = 35071;
    public static final int GL_COMPARE_REF_DEPTH_TO_TEXTURE_EXT = 34894;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER_EXT = 36052;
    public static final int GL_SAMPLER_1D_ARRAY_EXT = 36288;
    public static final int GL_SAMPLER_2D_ARRAY_EXT = 36289;
    public static final int GL_SAMPLER_1D_ARRAY_SHADOW_EXT = 36291;
    public static final int GL_SAMPLER_2D_ARRAY_SHADOW_EXT = 36292;
    
    private EXTTextureArray() {
    }
    
    public static void glFramebufferTextureLayerEXT(final int target, final int attachment, final int texture, final int level, final int layer) {
        EXTGeometryShader4.glFramebufferTextureLayerEXT(target, attachment, texture, level, layer);
    }
}

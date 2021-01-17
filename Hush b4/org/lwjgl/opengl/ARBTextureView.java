// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

public final class ARBTextureView
{
    public static final int GL_TEXTURE_VIEW_MIN_LEVEL = 33499;
    public static final int GL_TEXTURE_VIEW_NUM_LEVELS = 33500;
    public static final int GL_TEXTURE_VIEW_MIN_LAYER = 33501;
    public static final int GL_TEXTURE_VIEW_NUM_LAYERS = 33502;
    public static final int GL_TEXTURE_IMMUTABLE_LEVELS = 33503;
    
    private ARBTextureView() {
    }
    
    public static void glTextureView(final int texture, final int target, final int origtexture, final int internalformat, final int minlevel, final int numlevels, final int minlayer, final int numlayers) {
        GL43.glTextureView(texture, target, origtexture, internalformat, minlevel, numlevels, minlayer, numlayers);
    }
}

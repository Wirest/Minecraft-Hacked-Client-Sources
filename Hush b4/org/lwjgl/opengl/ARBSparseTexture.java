// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ARBSparseTexture
{
    public static final int GL_TEXTURE_SPARSE_ARB = 37286;
    public static final int GL_VIRTUAL_PAGE_SIZE_INDEX_ARB = 37287;
    public static final int GL_NUM_SPARSE_LEVELS_ARB = 37290;
    public static final int GL_NUM_VIRTUAL_PAGE_SIZES_ARB = 37288;
    public static final int GL_VIRTUAL_PAGE_SIZE_X_ARB = 37269;
    public static final int GL_VIRTUAL_PAGE_SIZE_Y_ARB = 37270;
    public static final int GL_VIRTUAL_PAGE_SIZE_Z_ARB = 37271;
    public static final int GL_MAX_SPARSE_TEXTURE_SIZE_ARB = 37272;
    public static final int GL_MAX_SPARSE_3D_TEXTURE_SIZE_ARB = 37273;
    public static final int GL_MAX_SPARSE_ARRAY_TEXTURE_LAYERS_ARB = 37274;
    public static final int GL_SPARSE_TEXTURE_FULL_ARRAY_CUBE_MIPMAPS_ARB = 37289;
    
    private ARBSparseTexture() {
    }
    
    public static void glTexPageCommitmentARB(final int target, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final boolean commit) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTexPageCommitmentARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTexPageCommitmentARB(target, level, xoffset, yoffset, zoffset, width, height, depth, commit, function_pointer);
    }
    
    static native void nglTexPageCommitmentARB(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final boolean p8, final long p9);
    
    public static void glTexturePageCommitmentEXT(final int texture, final int target, final int level, final int xoffset, final int yoffset, final int zoffset, final int width, final int height, final int depth, final boolean commit) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTexturePageCommitmentEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTexturePageCommitmentEXT(texture, target, level, xoffset, yoffset, zoffset, width, height, depth, commit, function_pointer);
    }
    
    static native void nglTexturePageCommitmentEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final boolean p9, final long p10);
}

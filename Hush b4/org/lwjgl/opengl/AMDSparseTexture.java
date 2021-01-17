// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class AMDSparseTexture
{
    public static final int GL_TEXTURE_STORAGE_SPARSE_BIT_AMD = 1;
    public static final int GL_VIRTUAL_PAGE_SIZE_X_AMD = 37269;
    public static final int GL_VIRTUAL_PAGE_SIZE_Y_AMD = 37270;
    public static final int GL_VIRTUAL_PAGE_SIZE_Z_AMD = 37271;
    public static final int GL_MAX_SPARSE_TEXTURE_SIZE_AMD = 37272;
    public static final int GL_MAX_SPARSE_3D_TEXTURE_SIZE_AMD = 37273;
    public static final int GL_MAX_SPARSE_ARRAY_TEXTURE_LAYERS = 37274;
    public static final int GL_MIN_SPARSE_LEVEL_AMD = 37275;
    public static final int GL_MIN_LOD_WARNING_AMD = 37276;
    
    private AMDSparseTexture() {
    }
    
    public static void glTexStorageSparseAMD(final int target, final int internalFormat, final int width, final int height, final int depth, final int layers, final int flags) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTexStorageSparseAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTexStorageSparseAMD(target, internalFormat, width, height, depth, layers, flags, function_pointer);
    }
    
    static native void nglTexStorageSparseAMD(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final long p7);
    
    public static void glTextureStorageSparseAMD(final int texture, final int target, final int internalFormat, final int width, final int height, final int depth, final int layers, final int flags) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTextureStorageSparseAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTextureStorageSparseAMD(texture, target, internalFormat, width, height, depth, layers, flags, function_pointer);
    }
    
    static native void nglTextureStorageSparseAMD(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final long p8);
}

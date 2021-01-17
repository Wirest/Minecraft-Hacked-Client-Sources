// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.PointerBuffer;
import java.nio.IntBuffer;

public final class ARBMultiBind
{
    private ARBMultiBind() {
    }
    
    public static void glBindBuffersBase(final int target, final int first, final int count, final IntBuffer buffers) {
        GL44.glBindBuffersBase(target, first, count, buffers);
    }
    
    public static void glBindBuffersRange(final int target, final int first, final int count, final IntBuffer buffers, final PointerBuffer offsets, final PointerBuffer sizes) {
        GL44.glBindBuffersRange(target, first, count, buffers, offsets, sizes);
    }
    
    public static void glBindTextures(final int first, final int count, final IntBuffer textures) {
        GL44.glBindTextures(first, count, textures);
    }
    
    public static void glBindSamplers(final int first, final int count, final IntBuffer samplers) {
        GL44.glBindSamplers(first, count, samplers);
    }
    
    public static void glBindImageTextures(final int first, final int count, final IntBuffer textures) {
        GL44.glBindImageTextures(first, count, textures);
    }
    
    public static void glBindVertexBuffers(final int first, final int count, final IntBuffer buffers, final PointerBuffer offsets, final IntBuffer strides) {
        GL44.glBindVertexBuffers(first, count, buffers, offsets, strides);
    }
}

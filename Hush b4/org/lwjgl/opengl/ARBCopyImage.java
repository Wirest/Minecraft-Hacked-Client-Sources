// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

public final class ARBCopyImage
{
    private ARBCopyImage() {
    }
    
    public static void glCopyImageSubData(final int srcName, final int srcTarget, final int srcLevel, final int srcX, final int srcY, final int srcZ, final int dstName, final int dstTarget, final int dstLevel, final int dstX, final int dstY, final int dstZ, final int srcWidth, final int srcHeight, final int srcDepth) {
        GL43.glCopyImageSubData(srcName, srcTarget, srcLevel, srcX, srcY, srcZ, dstName, dstTarget, dstLevel, dstX, dstY, dstZ, srcWidth, srcHeight, srcDepth);
    }
}

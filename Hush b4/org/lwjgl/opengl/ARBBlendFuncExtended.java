// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ByteBuffer;

public final class ARBBlendFuncExtended
{
    public static final int GL_SRC1_COLOR = 35065;
    public static final int GL_SRC1_ALPHA = 34185;
    public static final int GL_ONE_MINUS_SRC1_COLOR = 35066;
    public static final int GL_ONE_MINUS_SRC1_ALPHA = 35067;
    public static final int GL_MAX_DUAL_SOURCE_DRAW_BUFFERS = 35068;
    
    private ARBBlendFuncExtended() {
    }
    
    public static void glBindFragDataLocationIndexed(final int program, final int colorNumber, final int index, final ByteBuffer name) {
        GL33.glBindFragDataLocationIndexed(program, colorNumber, index, name);
    }
    
    public static void glBindFragDataLocationIndexed(final int program, final int colorNumber, final int index, final CharSequence name) {
        GL33.glBindFragDataLocationIndexed(program, colorNumber, index, name);
    }
    
    public static int glGetFragDataIndex(final int program, final ByteBuffer name) {
        return GL33.glGetFragDataIndex(program, name);
    }
    
    public static int glGetFragDataIndex(final int program, final CharSequence name) {
        return GL33.glGetFragDataIndex(program, name);
    }
}

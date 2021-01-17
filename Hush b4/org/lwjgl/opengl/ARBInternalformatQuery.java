// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.IntBuffer;

public final class ARBInternalformatQuery
{
    public static final int GL_NUM_SAMPLE_COUNTS = 37760;
    
    private ARBInternalformatQuery() {
    }
    
    public static void glGetInternalformat(final int target, final int internalformat, final int pname, final IntBuffer params) {
        GL42.glGetInternalformat(target, internalformat, pname, params);
    }
    
    public static int glGetInternalformat(final int target, final int internalformat, final int pname) {
        return GL42.glGetInternalformat(target, internalformat, pname);
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.LongBuffer;

public final class ARBTimerQuery
{
    public static final int GL_TIME_ELAPSED = 35007;
    public static final int GL_TIMESTAMP = 36392;
    
    private ARBTimerQuery() {
    }
    
    public static void glQueryCounter(final int id, final int target) {
        GL33.glQueryCounter(id, target);
    }
    
    public static void glGetQueryObject(final int id, final int pname, final LongBuffer params) {
        GL33.glGetQueryObject(id, pname, params);
    }
    
    public static long glGetQueryObjecti64(final int id, final int pname) {
        return GL33.glGetQueryObjecti64(id, pname);
    }
    
    public static void glGetQueryObjectu(final int id, final int pname, final LongBuffer params) {
        GL33.glGetQueryObjectu(id, pname, params);
    }
    
    public static long glGetQueryObjectui64(final int id, final int pname) {
        return GL33.glGetQueryObjectui64(id, pname);
    }
}

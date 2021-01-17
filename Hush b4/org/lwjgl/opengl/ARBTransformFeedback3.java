// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.IntBuffer;

public final class ARBTransformFeedback3
{
    public static final int GL_MAX_TRANSFORM_FEEDBACK_BUFFERS = 36464;
    public static final int GL_MAX_VERTEX_STREAMS = 36465;
    
    private ARBTransformFeedback3() {
    }
    
    public static void glDrawTransformFeedbackStream(final int mode, final int id, final int stream) {
        GL40.glDrawTransformFeedbackStream(mode, id, stream);
    }
    
    public static void glBeginQueryIndexed(final int target, final int index, final int id) {
        GL40.glBeginQueryIndexed(target, index, id);
    }
    
    public static void glEndQueryIndexed(final int target, final int index) {
        GL40.glEndQueryIndexed(target, index);
    }
    
    public static void glGetQueryIndexed(final int target, final int index, final int pname, final IntBuffer params) {
        GL40.glGetQueryIndexed(target, index, pname, params);
    }
    
    public static int glGetQueryIndexedi(final int target, final int index, final int pname) {
        return GL40.glGetQueryIndexedi(target, index, pname);
    }
}

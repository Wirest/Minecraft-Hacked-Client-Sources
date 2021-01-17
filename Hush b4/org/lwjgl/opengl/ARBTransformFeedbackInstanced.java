// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

public final class ARBTransformFeedbackInstanced
{
    private ARBTransformFeedbackInstanced() {
    }
    
    public static void glDrawTransformFeedbackInstanced(final int mode, final int id, final int primcount) {
        GL42.glDrawTransformFeedbackInstanced(mode, id, primcount);
    }
    
    public static void glDrawTransformFeedbackStreamInstanced(final int mode, final int id, final int stream, final int primcount) {
        GL42.glDrawTransformFeedbackStreamInstanced(mode, id, stream, primcount);
    }
}

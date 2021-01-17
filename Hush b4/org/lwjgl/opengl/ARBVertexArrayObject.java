// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.IntBuffer;

public final class ARBVertexArrayObject
{
    public static final int GL_VERTEX_ARRAY_BINDING = 34229;
    
    private ARBVertexArrayObject() {
    }
    
    public static void glBindVertexArray(final int array) {
        GL30.glBindVertexArray(array);
    }
    
    public static void glDeleteVertexArrays(final IntBuffer arrays) {
        GL30.glDeleteVertexArrays(arrays);
    }
    
    public static void glDeleteVertexArrays(final int array) {
        GL30.glDeleteVertexArrays(array);
    }
    
    public static void glGenVertexArrays(final IntBuffer arrays) {
        GL30.glGenVertexArrays(arrays);
    }
    
    public static int glGenVertexArrays() {
        return GL30.glGenVertexArrays();
    }
    
    public static boolean glIsVertexArray(final int array) {
        return GL30.glIsVertexArray(array);
    }
}

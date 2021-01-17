// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.opengl.GL11;

public class GLAllocation
{
    public static synchronized int generateDisplayLists(final int range) {
        final int i = GL11.glGenLists(range);
        if (i == 0) {
            final int j = GL11.glGetError();
            String s = "No error code reported";
            if (j != 0) {
                s = GLU.gluErrorString(j);
            }
            throw new IllegalStateException("glGenLists returned an ID of 0 for a count of " + range + ", GL error (" + j + "): " + s);
        }
        return i;
    }
    
    public static synchronized void deleteDisplayLists(final int list, final int range) {
        GL11.glDeleteLists(list, range);
    }
    
    public static synchronized void deleteDisplayLists(final int list) {
        GL11.glDeleteLists(list, 1);
    }
    
    public static synchronized ByteBuffer createDirectByteBuffer(final int capacity) {
        return ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder());
    }
    
    public static IntBuffer createDirectIntBuffer(final int capacity) {
        return createDirectByteBuffer(capacity << 2).asIntBuffer();
    }
    
    public static FloatBuffer createDirectFloatBuffer(final int capacity) {
        return createDirectByteBuffer(capacity << 2).asFloatBuffer();
    }
}

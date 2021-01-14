package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class GLAllocation {
    private static final String __OBFID = "CL_00000630";

    /**
     * Generates the specified number of display lists and returns the first index.
     */
    public static synchronized int generateDisplayLists(int p_74526_0_) {
        int var1 = GL11.glGenLists(p_74526_0_);

        if (var1 == 0) {
            int var2 = GL11.glGetError();
            String var3 = "No error code reported";

            if (var2 != 0) {
                var3 = GLU.gluErrorString(var2);
            }

            throw new IllegalStateException("glGenLists returned an ID of 0 for a count of " + p_74526_0_ + ", GL error (" + var2 + "): " + var3);
        } else {
            return var1;
        }
    }

    public static synchronized void func_178874_a(int p_178874_0_, int p_178874_1_) {
        GL11.glDeleteLists(p_178874_0_, p_178874_1_);
    }

    public static synchronized void deleteDisplayLists(int p_74523_0_) {
        GL11.glDeleteLists(p_74523_0_, 1);
    }

    /**
     * Creates and returns a direct byte buffer with the specified capacity. Applies native ordering to speed up access.
     */
    public static synchronized ByteBuffer createDirectByteBuffer(int p_74524_0_) {
        return ByteBuffer.allocateDirect(p_74524_0_).order(ByteOrder.nativeOrder());
    }

    /**
     * Creates and returns a direct int buffer with the specified capacity. Applies native ordering to speed up access.
     */
    public static IntBuffer createDirectIntBuffer(int p_74527_0_) {
        return createDirectByteBuffer(p_74527_0_ << 2).asIntBuffer();
    }

    /**
     * Creates and returns a direct float buffer with the specified capacity. Applies native ordering to speed up
     * access.
     */
    public static FloatBuffer createDirectFloatBuffer(int p_74529_0_) {
        return createDirectByteBuffer(p_74529_0_ << 2).asFloatBuffer();
    }
}

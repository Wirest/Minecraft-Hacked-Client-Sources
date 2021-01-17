// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ShortBuffer;
import java.nio.IntBuffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.DoubleBuffer;

public final class ARBVertexProgram extends ARBProgram
{
    public static final int GL_VERTEX_PROGRAM_ARB = 34336;
    public static final int GL_VERTEX_PROGRAM_POINT_SIZE_ARB = 34370;
    public static final int GL_VERTEX_PROGRAM_TWO_SIDE_ARB = 34371;
    public static final int GL_COLOR_SUM_ARB = 33880;
    public static final int GL_VERTEX_ATTRIB_ARRAY_ENABLED_ARB = 34338;
    public static final int GL_VERTEX_ATTRIB_ARRAY_SIZE_ARB = 34339;
    public static final int GL_VERTEX_ATTRIB_ARRAY_STRIDE_ARB = 34340;
    public static final int GL_VERTEX_ATTRIB_ARRAY_TYPE_ARB = 34341;
    public static final int GL_VERTEX_ATTRIB_ARRAY_NORMALIZED_ARB = 34922;
    public static final int GL_CURRENT_VERTEX_ATTRIB_ARB = 34342;
    public static final int GL_VERTEX_ATTRIB_ARRAY_POINTER_ARB = 34373;
    public static final int GL_PROGRAM_ADDRESS_REGISTERS_ARB = 34992;
    public static final int GL_MAX_PROGRAM_ADDRESS_REGISTERS_ARB = 34993;
    public static final int GL_PROGRAM_NATIVE_ADDRESS_REGISTERS_ARB = 34994;
    public static final int GL_MAX_PROGRAM_NATIVE_ADDRESS_REGISTERS_ARB = 34995;
    public static final int GL_MAX_VERTEX_ATTRIBS_ARB = 34921;
    
    private ARBVertexProgram() {
    }
    
    public static void glVertexAttrib1sARB(final int index, final short x) {
        ARBVertexShader.glVertexAttrib1sARB(index, x);
    }
    
    public static void glVertexAttrib1fARB(final int index, final float x) {
        ARBVertexShader.glVertexAttrib1fARB(index, x);
    }
    
    public static void glVertexAttrib1dARB(final int index, final double x) {
        ARBVertexShader.glVertexAttrib1dARB(index, x);
    }
    
    public static void glVertexAttrib2sARB(final int index, final short x, final short y) {
        ARBVertexShader.glVertexAttrib2sARB(index, x, y);
    }
    
    public static void glVertexAttrib2fARB(final int index, final float x, final float y) {
        ARBVertexShader.glVertexAttrib2fARB(index, x, y);
    }
    
    public static void glVertexAttrib2dARB(final int index, final double x, final double y) {
        ARBVertexShader.glVertexAttrib2dARB(index, x, y);
    }
    
    public static void glVertexAttrib3sARB(final int index, final short x, final short y, final short z) {
        ARBVertexShader.glVertexAttrib3sARB(index, x, y, z);
    }
    
    public static void glVertexAttrib3fARB(final int index, final float x, final float y, final float z) {
        ARBVertexShader.glVertexAttrib3fARB(index, x, y, z);
    }
    
    public static void glVertexAttrib3dARB(final int index, final double x, final double y, final double z) {
        ARBVertexShader.glVertexAttrib3dARB(index, x, y, z);
    }
    
    public static void glVertexAttrib4sARB(final int index, final short x, final short y, final short z, final short w) {
        ARBVertexShader.glVertexAttrib4sARB(index, x, y, z, w);
    }
    
    public static void glVertexAttrib4fARB(final int index, final float x, final float y, final float z, final float w) {
        ARBVertexShader.glVertexAttrib4fARB(index, x, y, z, w);
    }
    
    public static void glVertexAttrib4dARB(final int index, final double x, final double y, final double z, final double w) {
        ARBVertexShader.glVertexAttrib4dARB(index, x, y, z, w);
    }
    
    public static void glVertexAttrib4NubARB(final int index, final byte x, final byte y, final byte z, final byte w) {
        ARBVertexShader.glVertexAttrib4NubARB(index, x, y, z, w);
    }
    
    public static void glVertexAttribPointerARB(final int index, final int size, final boolean normalized, final int stride, final DoubleBuffer buffer) {
        ARBVertexShader.glVertexAttribPointerARB(index, size, normalized, stride, buffer);
    }
    
    public static void glVertexAttribPointerARB(final int index, final int size, final boolean normalized, final int stride, final FloatBuffer buffer) {
        ARBVertexShader.glVertexAttribPointerARB(index, size, normalized, stride, buffer);
    }
    
    public static void glVertexAttribPointerARB(final int index, final int size, final boolean unsigned, final boolean normalized, final int stride, final ByteBuffer buffer) {
        ARBVertexShader.glVertexAttribPointerARB(index, size, unsigned, normalized, stride, buffer);
    }
    
    public static void glVertexAttribPointerARB(final int index, final int size, final boolean unsigned, final boolean normalized, final int stride, final IntBuffer buffer) {
        ARBVertexShader.glVertexAttribPointerARB(index, size, unsigned, normalized, stride, buffer);
    }
    
    public static void glVertexAttribPointerARB(final int index, final int size, final boolean unsigned, final boolean normalized, final int stride, final ShortBuffer buffer) {
        ARBVertexShader.glVertexAttribPointerARB(index, size, unsigned, normalized, stride, buffer);
    }
    
    public static void glVertexAttribPointerARB(final int index, final int size, final int type, final boolean normalized, final int stride, final long buffer_buffer_offset) {
        ARBVertexShader.glVertexAttribPointerARB(index, size, type, normalized, stride, buffer_buffer_offset);
    }
    
    public static void glEnableVertexAttribArrayARB(final int index) {
        ARBVertexShader.glEnableVertexAttribArrayARB(index);
    }
    
    public static void glDisableVertexAttribArrayARB(final int index) {
        ARBVertexShader.glDisableVertexAttribArrayARB(index);
    }
    
    public static void glGetVertexAttribARB(final int index, final int pname, final FloatBuffer params) {
        ARBVertexShader.glGetVertexAttribARB(index, pname, params);
    }
    
    public static void glGetVertexAttribARB(final int index, final int pname, final DoubleBuffer params) {
        ARBVertexShader.glGetVertexAttribARB(index, pname, params);
    }
    
    public static void glGetVertexAttribARB(final int index, final int pname, final IntBuffer params) {
        ARBVertexShader.glGetVertexAttribARB(index, pname, params);
    }
    
    public static ByteBuffer glGetVertexAttribPointerARB(final int index, final int pname, final long result_size) {
        return ARBVertexShader.glGetVertexAttribPointerARB(index, pname, result_size);
    }
}

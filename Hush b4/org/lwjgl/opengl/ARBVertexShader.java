// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ByteOrder;
import java.nio.Buffer;
import java.nio.ShortBuffer;
import java.nio.IntBuffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.LWJGLUtil;
import java.nio.DoubleBuffer;
import org.lwjgl.BufferChecks;

public final class ARBVertexShader
{
    public static final int GL_VERTEX_SHADER_ARB = 35633;
    public static final int GL_MAX_VERTEX_UNIFORM_COMPONENTS_ARB = 35658;
    public static final int GL_MAX_VARYING_FLOATS_ARB = 35659;
    public static final int GL_MAX_VERTEX_ATTRIBS_ARB = 34921;
    public static final int GL_MAX_TEXTURE_IMAGE_UNITS_ARB = 34930;
    public static final int GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS_ARB = 35660;
    public static final int GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS_ARB = 35661;
    public static final int GL_MAX_TEXTURE_COORDS_ARB = 34929;
    public static final int GL_VERTEX_PROGRAM_POINT_SIZE_ARB = 34370;
    public static final int GL_VERTEX_PROGRAM_TWO_SIDE_ARB = 34371;
    public static final int GL_OBJECT_ACTIVE_ATTRIBUTES_ARB = 35721;
    public static final int GL_OBJECT_ACTIVE_ATTRIBUTE_MAX_LENGTH_ARB = 35722;
    public static final int GL_VERTEX_ATTRIB_ARRAY_ENABLED_ARB = 34338;
    public static final int GL_VERTEX_ATTRIB_ARRAY_SIZE_ARB = 34339;
    public static final int GL_VERTEX_ATTRIB_ARRAY_STRIDE_ARB = 34340;
    public static final int GL_VERTEX_ATTRIB_ARRAY_TYPE_ARB = 34341;
    public static final int GL_VERTEX_ATTRIB_ARRAY_NORMALIZED_ARB = 34922;
    public static final int GL_CURRENT_VERTEX_ATTRIB_ARB = 34342;
    public static final int GL_VERTEX_ATTRIB_ARRAY_POINTER_ARB = 34373;
    public static final int GL_FLOAT_VEC2_ARB = 35664;
    public static final int GL_FLOAT_VEC3_ARB = 35665;
    public static final int GL_FLOAT_VEC4_ARB = 35666;
    public static final int GL_FLOAT_MAT2_ARB = 35674;
    public static final int GL_FLOAT_MAT3_ARB = 35675;
    public static final int GL_FLOAT_MAT4_ARB = 35676;
    
    private ARBVertexShader() {
    }
    
    public static void glVertexAttrib1sARB(final int index, final short v0) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib1sARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib1sARB(index, v0, function_pointer);
    }
    
    static native void nglVertexAttrib1sARB(final int p0, final short p1, final long p2);
    
    public static void glVertexAttrib1fARB(final int index, final float v0) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib1fARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib1fARB(index, v0, function_pointer);
    }
    
    static native void nglVertexAttrib1fARB(final int p0, final float p1, final long p2);
    
    public static void glVertexAttrib1dARB(final int index, final double v0) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib1dARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib1dARB(index, v0, function_pointer);
    }
    
    static native void nglVertexAttrib1dARB(final int p0, final double p1, final long p2);
    
    public static void glVertexAttrib2sARB(final int index, final short v0, final short v1) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib2sARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib2sARB(index, v0, v1, function_pointer);
    }
    
    static native void nglVertexAttrib2sARB(final int p0, final short p1, final short p2, final long p3);
    
    public static void glVertexAttrib2fARB(final int index, final float v0, final float v1) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib2fARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib2fARB(index, v0, v1, function_pointer);
    }
    
    static native void nglVertexAttrib2fARB(final int p0, final float p1, final float p2, final long p3);
    
    public static void glVertexAttrib2dARB(final int index, final double v0, final double v1) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib2dARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib2dARB(index, v0, v1, function_pointer);
    }
    
    static native void nglVertexAttrib2dARB(final int p0, final double p1, final double p2, final long p3);
    
    public static void glVertexAttrib3sARB(final int index, final short v0, final short v1, final short v2) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib3sARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib3sARB(index, v0, v1, v2, function_pointer);
    }
    
    static native void nglVertexAttrib3sARB(final int p0, final short p1, final short p2, final short p3, final long p4);
    
    public static void glVertexAttrib3fARB(final int index, final float v0, final float v1, final float v2) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib3fARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib3fARB(index, v0, v1, v2, function_pointer);
    }
    
    static native void nglVertexAttrib3fARB(final int p0, final float p1, final float p2, final float p3, final long p4);
    
    public static void glVertexAttrib3dARB(final int index, final double v0, final double v1, final double v2) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib3dARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib3dARB(index, v0, v1, v2, function_pointer);
    }
    
    static native void nglVertexAttrib3dARB(final int p0, final double p1, final double p2, final double p3, final long p4);
    
    public static void glVertexAttrib4sARB(final int index, final short v0, final short v1, final short v2, final short v3) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib4sARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib4sARB(index, v0, v1, v2, v3, function_pointer);
    }
    
    static native void nglVertexAttrib4sARB(final int p0, final short p1, final short p2, final short p3, final short p4, final long p5);
    
    public static void glVertexAttrib4fARB(final int index, final float v0, final float v1, final float v2, final float v3) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib4fARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib4fARB(index, v0, v1, v2, v3, function_pointer);
    }
    
    static native void nglVertexAttrib4fARB(final int p0, final float p1, final float p2, final float p3, final float p4, final long p5);
    
    public static void glVertexAttrib4dARB(final int index, final double v0, final double v1, final double v2, final double v3) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib4dARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib4dARB(index, v0, v1, v2, v3, function_pointer);
    }
    
    static native void nglVertexAttrib4dARB(final int p0, final double p1, final double p2, final double p3, final double p4, final long p5);
    
    public static void glVertexAttrib4NubARB(final int index, final byte x, final byte y, final byte z, final byte w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib4NubARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib4NubARB(index, x, y, z, w, function_pointer);
    }
    
    static native void nglVertexAttrib4NubARB(final int p0, final byte p1, final byte p2, final byte p3, final byte p4, final long p5);
    
    public static void glVertexAttribPointerARB(final int index, final int size, final boolean normalized, final int stride, final DoubleBuffer buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribPointerARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(buffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
        }
        nglVertexAttribPointerARB(index, size, 5130, normalized, stride, MemoryUtil.getAddress(buffer), function_pointer);
    }
    
    public static void glVertexAttribPointerARB(final int index, final int size, final boolean normalized, final int stride, final FloatBuffer buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribPointerARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(buffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
        }
        nglVertexAttribPointerARB(index, size, 5126, normalized, stride, MemoryUtil.getAddress(buffer), function_pointer);
    }
    
    public static void glVertexAttribPointerARB(final int index, final int size, final boolean unsigned, final boolean normalized, final int stride, final ByteBuffer buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribPointerARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(buffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
        }
        nglVertexAttribPointerARB(index, size, unsigned ? 5121 : 5120, normalized, stride, MemoryUtil.getAddress(buffer), function_pointer);
    }
    
    public static void glVertexAttribPointerARB(final int index, final int size, final boolean unsigned, final boolean normalized, final int stride, final IntBuffer buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribPointerARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(buffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
        }
        nglVertexAttribPointerARB(index, size, unsigned ? 5125 : 5124, normalized, stride, MemoryUtil.getAddress(buffer), function_pointer);
    }
    
    public static void glVertexAttribPointerARB(final int index, final int size, final boolean unsigned, final boolean normalized, final int stride, final ShortBuffer buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribPointerARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(buffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
        }
        nglVertexAttribPointerARB(index, size, unsigned ? 5123 : 5122, normalized, stride, MemoryUtil.getAddress(buffer), function_pointer);
    }
    
    static native void nglVertexAttribPointerARB(final int p0, final int p1, final int p2, final boolean p3, final int p4, final long p5, final long p6);
    
    public static void glVertexAttribPointerARB(final int index, final int size, final int type, final boolean normalized, final int stride, final long buffer_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribPointerARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOenabled(caps);
        nglVertexAttribPointerARBBO(index, size, type, normalized, stride, buffer_buffer_offset, function_pointer);
    }
    
    static native void nglVertexAttribPointerARBBO(final int p0, final int p1, final int p2, final boolean p3, final int p4, final long p5, final long p6);
    
    public static void glVertexAttribPointerARB(final int index, final int size, final int type, final boolean normalized, final int stride, final ByteBuffer buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribPointerARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(buffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
        }
        nglVertexAttribPointerARB(index, size, type, normalized, stride, MemoryUtil.getAddress(buffer), function_pointer);
    }
    
    public static void glEnableVertexAttribArrayARB(final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glEnableVertexAttribArrayARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglEnableVertexAttribArrayARB(index, function_pointer);
    }
    
    static native void nglEnableVertexAttribArrayARB(final int p0, final long p1);
    
    public static void glDisableVertexAttribArrayARB(final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDisableVertexAttribArrayARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDisableVertexAttribArrayARB(index, function_pointer);
    }
    
    static native void nglDisableVertexAttribArrayARB(final int p0, final long p1);
    
    public static void glBindAttribLocationARB(final int programObj, final int index, final ByteBuffer name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindAttribLocationARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(name);
        BufferChecks.checkNullTerminated(name);
        nglBindAttribLocationARB(programObj, index, MemoryUtil.getAddress(name), function_pointer);
    }
    
    static native void nglBindAttribLocationARB(final int p0, final int p1, final long p2, final long p3);
    
    public static void glBindAttribLocationARB(final int programObj, final int index, final CharSequence name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindAttribLocationARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBindAttribLocationARB(programObj, index, APIUtil.getBufferNT(caps, name), function_pointer);
    }
    
    public static void glGetActiveAttribARB(final int programObj, final int index, final IntBuffer length, final IntBuffer size, final IntBuffer type, final ByteBuffer name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveAttribARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (length != null) {
            BufferChecks.checkBuffer(length, 1);
        }
        BufferChecks.checkBuffer(size, 1);
        BufferChecks.checkBuffer(type, 1);
        BufferChecks.checkDirect(name);
        nglGetActiveAttribARB(programObj, index, name.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(size), MemoryUtil.getAddress(type), MemoryUtil.getAddress(name), function_pointer);
    }
    
    static native void nglGetActiveAttribARB(final int p0, final int p1, final int p2, final long p3, final long p4, final long p5, final long p6, final long p7);
    
    public static String glGetActiveAttribARB(final int programObj, final int index, final int maxLength, final IntBuffer sizeType) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveAttribARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(sizeType, 2);
        final IntBuffer name_length = APIUtil.getLengths(caps);
        final ByteBuffer name = APIUtil.getBufferByte(caps, maxLength);
        nglGetActiveAttribARB(programObj, index, maxLength, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress(sizeType), MemoryUtil.getAddress(sizeType, sizeType.position() + 1), MemoryUtil.getAddress(name), function_pointer);
        name.limit(name_length.get(0));
        return APIUtil.getString(caps, name);
    }
    
    public static String glGetActiveAttribARB(final int programObj, final int index, final int maxLength) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveAttribARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer name_length = APIUtil.getLengths(caps);
        final ByteBuffer name = APIUtil.getBufferByte(caps, maxLength);
        nglGetActiveAttribARB(programObj, index, maxLength, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress0(APIUtil.getBufferInt(caps)), MemoryUtil.getAddress(APIUtil.getBufferInt(caps), 1), MemoryUtil.getAddress(name), function_pointer);
        name.limit(name_length.get(0));
        return APIUtil.getString(caps, name);
    }
    
    public static int glGetActiveAttribSizeARB(final int programObj, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveAttribARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer size = APIUtil.getBufferInt(caps);
        nglGetActiveAttribARB(programObj, index, 0, 0L, MemoryUtil.getAddress(size), MemoryUtil.getAddress(size, 1), APIUtil.getBufferByte0(caps), function_pointer);
        return size.get(0);
    }
    
    public static int glGetActiveAttribTypeARB(final int programObj, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveAttribARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer type = APIUtil.getBufferInt(caps);
        nglGetActiveAttribARB(programObj, index, 0, 0L, MemoryUtil.getAddress(type, 1), MemoryUtil.getAddress(type), APIUtil.getBufferByte0(caps), function_pointer);
        return type.get(0);
    }
    
    public static int glGetAttribLocationARB(final int programObj, final ByteBuffer name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetAttribLocationARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(name);
        BufferChecks.checkNullTerminated(name);
        final int __result = nglGetAttribLocationARB(programObj, MemoryUtil.getAddress(name), function_pointer);
        return __result;
    }
    
    static native int nglGetAttribLocationARB(final int p0, final long p1, final long p2);
    
    public static int glGetAttribLocationARB(final int programObj, final CharSequence name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetAttribLocationARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nglGetAttribLocationARB(programObj, APIUtil.getBufferNT(caps, name), function_pointer);
        return __result;
    }
    
    public static void glGetVertexAttribARB(final int index, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexAttribfvARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetVertexAttribfvARB(index, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetVertexAttribfvARB(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetVertexAttribARB(final int index, final int pname, final DoubleBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexAttribdvARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetVertexAttribdvARB(index, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetVertexAttribdvARB(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetVertexAttribARB(final int index, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexAttribivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetVertexAttribivARB(index, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetVertexAttribivARB(final int p0, final int p1, final long p2, final long p3);
    
    public static ByteBuffer glGetVertexAttribPointerARB(final int index, final int pname, final long result_size) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexAttribPointervARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final ByteBuffer __result = nglGetVertexAttribPointervARB(index, pname, result_size, function_pointer);
        return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
    }
    
    static native ByteBuffer nglGetVertexAttribPointervARB(final int p0, final int p1, final long p2, final long p3);
}

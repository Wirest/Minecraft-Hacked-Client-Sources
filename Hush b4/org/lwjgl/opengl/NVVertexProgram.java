// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ShortBuffer;
import java.nio.ByteOrder;
import org.lwjgl.LWJGLUtil;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.DoubleBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.FloatBuffer;

public final class NVVertexProgram extends NVProgram
{
    public static final int GL_VERTEX_PROGRAM_NV = 34336;
    public static final int GL_VERTEX_PROGRAM_POINT_SIZE_NV = 34370;
    public static final int GL_VERTEX_PROGRAM_TWO_SIDE_NV = 34371;
    public static final int GL_VERTEX_STATE_PROGRAM_NV = 34337;
    public static final int GL_ATTRIB_ARRAY_SIZE_NV = 34339;
    public static final int GL_ATTRIB_ARRAY_STRIDE_NV = 34340;
    public static final int GL_ATTRIB_ARRAY_TYPE_NV = 34341;
    public static final int GL_CURRENT_ATTRIB_NV = 34342;
    public static final int GL_PROGRAM_PARAMETER_NV = 34372;
    public static final int GL_ATTRIB_ARRAY_POINTER_NV = 34373;
    public static final int GL_TRACK_MATRIX_NV = 34376;
    public static final int GL_TRACK_MATRIX_TRANSFORM_NV = 34377;
    public static final int GL_MAX_TRACK_MATRIX_STACK_DEPTH_NV = 34350;
    public static final int GL_MAX_TRACK_MATRICES_NV = 34351;
    public static final int GL_CURRENT_MATRIX_STACK_DEPTH_NV = 34368;
    public static final int GL_CURRENT_MATRIX_NV = 34369;
    public static final int GL_VERTEX_PROGRAM_BINDING_NV = 34378;
    public static final int GL_MODELVIEW_PROJECTION_NV = 34345;
    public static final int GL_MATRIX0_NV = 34352;
    public static final int GL_MATRIX1_NV = 34353;
    public static final int GL_MATRIX2_NV = 34354;
    public static final int GL_MATRIX3_NV = 34355;
    public static final int GL_MATRIX4_NV = 34356;
    public static final int GL_MATRIX5_NV = 34357;
    public static final int GL_MATRIX6_NV = 34358;
    public static final int GL_MATRIX7_NV = 34359;
    public static final int GL_IDENTITY_NV = 34346;
    public static final int GL_INVERSE_NV = 34347;
    public static final int GL_TRANSPOSE_NV = 34348;
    public static final int GL_INVERSE_TRANSPOSE_NV = 34349;
    public static final int GL_VERTEX_ATTRIB_ARRAY0_NV = 34384;
    public static final int GL_VERTEX_ATTRIB_ARRAY1_NV = 34385;
    public static final int GL_VERTEX_ATTRIB_ARRAY2_NV = 34386;
    public static final int GL_VERTEX_ATTRIB_ARRAY3_NV = 34387;
    public static final int GL_VERTEX_ATTRIB_ARRAY4_NV = 34388;
    public static final int GL_VERTEX_ATTRIB_ARRAY5_NV = 34389;
    public static final int GL_VERTEX_ATTRIB_ARRAY6_NV = 34390;
    public static final int GL_VERTEX_ATTRIB_ARRAY7_NV = 34391;
    public static final int GL_VERTEX_ATTRIB_ARRAY8_NV = 34392;
    public static final int GL_VERTEX_ATTRIB_ARRAY9_NV = 34393;
    public static final int GL_VERTEX_ATTRIB_ARRAY10_NV = 34394;
    public static final int GL_VERTEX_ATTRIB_ARRAY11_NV = 34395;
    public static final int GL_VERTEX_ATTRIB_ARRAY12_NV = 34396;
    public static final int GL_VERTEX_ATTRIB_ARRAY13_NV = 34397;
    public static final int GL_VERTEX_ATTRIB_ARRAY14_NV = 34398;
    public static final int GL_VERTEX_ATTRIB_ARRAY15_NV = 34399;
    public static final int GL_MAP1_VERTEX_ATTRIB0_4_NV = 34400;
    public static final int GL_MAP1_VERTEX_ATTRIB1_4_NV = 34401;
    public static final int GL_MAP1_VERTEX_ATTRIB2_4_NV = 34402;
    public static final int GL_MAP1_VERTEX_ATTRIB3_4_NV = 34403;
    public static final int GL_MAP1_VERTEX_ATTRIB4_4_NV = 34404;
    public static final int GL_MAP1_VERTEX_ATTRIB5_4_NV = 34405;
    public static final int GL_MAP1_VERTEX_ATTRIB6_4_NV = 34406;
    public static final int GL_MAP1_VERTEX_ATTRIB7_4_NV = 34407;
    public static final int GL_MAP1_VERTEX_ATTRIB8_4_NV = 34408;
    public static final int GL_MAP1_VERTEX_ATTRIB9_4_NV = 34409;
    public static final int GL_MAP1_VERTEX_ATTRIB10_4_NV = 34410;
    public static final int GL_MAP1_VERTEX_ATTRIB11_4_NV = 34411;
    public static final int GL_MAP1_VERTEX_ATTRIB12_4_NV = 34412;
    public static final int GL_MAP1_VERTEX_ATTRIB13_4_NV = 34413;
    public static final int GL_MAP1_VERTEX_ATTRIB14_4_NV = 34414;
    public static final int GL_MAP1_VERTEX_ATTRIB15_4_NV = 34415;
    public static final int GL_MAP2_VERTEX_ATTRIB0_4_NV = 34416;
    public static final int GL_MAP2_VERTEX_ATTRIB1_4_NV = 34417;
    public static final int GL_MAP2_VERTEX_ATTRIB2_4_NV = 34418;
    public static final int GL_MAP2_VERTEX_ATTRIB3_4_NV = 34419;
    public static final int GL_MAP2_VERTEX_ATTRIB4_4_NV = 34420;
    public static final int GL_MAP2_VERTEX_ATTRIB5_4_NV = 34421;
    public static final int GL_MAP2_VERTEX_ATTRIB6_4_NV = 34422;
    public static final int GL_MAP2_VERTEX_ATTRIB7_4_NV = 34423;
    public static final int GL_MAP2_VERTEX_ATTRIB8_4_NV = 34424;
    public static final int GL_MAP2_VERTEX_ATTRIB9_4_NV = 34425;
    public static final int GL_MAP2_VERTEX_ATTRIB10_4_NV = 34426;
    public static final int GL_MAP2_VERTEX_ATTRIB11_4_NV = 34427;
    public static final int GL_MAP2_VERTEX_ATTRIB12_4_NV = 34428;
    public static final int GL_MAP2_VERTEX_ATTRIB13_4_NV = 34429;
    public static final int GL_MAP2_VERTEX_ATTRIB14_4_NV = 34430;
    public static final int GL_MAP2_VERTEX_ATTRIB15_4_NV = 34431;
    
    private NVVertexProgram() {
    }
    
    public static void glExecuteProgramNV(final int target, final int id, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glExecuteProgramNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglExecuteProgramNV(target, id, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglExecuteProgramNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetProgramParameterNV(final int target, final int index, final int parameterName, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramParameterfvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetProgramParameterfvNV(target, index, parameterName, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetProgramParameterfvNV(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetProgramParameterNV(final int target, final int index, final int parameterName, final DoubleBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramParameterdvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetProgramParameterdvNV(target, index, parameterName, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetProgramParameterdvNV(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetTrackMatrixNV(final int target, final int address, final int parameterName, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTrackMatrixivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetTrackMatrixivNV(target, address, parameterName, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetTrackMatrixivNV(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetVertexAttribNV(final int index, final int parameterName, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexAttribfvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetVertexAttribfvNV(index, parameterName, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetVertexAttribfvNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetVertexAttribNV(final int index, final int parameterName, final DoubleBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexAttribdvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetVertexAttribdvNV(index, parameterName, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetVertexAttribdvNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetVertexAttribNV(final int index, final int parameterName, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexAttribivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetVertexAttribivNV(index, parameterName, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetVertexAttribivNV(final int p0, final int p1, final long p2, final long p3);
    
    public static ByteBuffer glGetVertexAttribPointerNV(final int index, final int parameterName, final long result_size) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexAttribPointervNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final ByteBuffer __result = nglGetVertexAttribPointervNV(index, parameterName, result_size, function_pointer);
        return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
    }
    
    static native ByteBuffer nglGetVertexAttribPointervNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glProgramParameter4fNV(final int target, final int index, final float x, final float y, final float z, final float w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramParameter4fNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramParameter4fNV(target, index, x, y, z, w, function_pointer);
    }
    
    static native void nglProgramParameter4fNV(final int p0, final int p1, final float p2, final float p3, final float p4, final float p5, final long p6);
    
    public static void glProgramParameter4dNV(final int target, final int index, final double x, final double y, final double z, final double w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramParameter4dNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramParameter4dNV(target, index, x, y, z, w, function_pointer);
    }
    
    static native void nglProgramParameter4dNV(final int p0, final int p1, final double p2, final double p3, final double p4, final double p5, final long p6);
    
    public static void glProgramParameters4NV(final int target, final int index, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramParameters4fvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglProgramParameters4fvNV(target, index, params.remaining() >> 2, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglProgramParameters4fvNV(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramParameters4NV(final int target, final int index, final DoubleBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramParameters4dvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglProgramParameters4dvNV(target, index, params.remaining() >> 2, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglProgramParameters4dvNV(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glTrackMatrixNV(final int target, final int address, final int matrix, final int transform) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTrackMatrixNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTrackMatrixNV(target, address, matrix, transform, function_pointer);
    }
    
    static native void nglTrackMatrixNV(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glVertexAttribPointerNV(final int index, final int size, final int type, final int stride, final DoubleBuffer buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribPointerNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(buffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
        }
        nglVertexAttribPointerNV(index, size, type, stride, MemoryUtil.getAddress(buffer), function_pointer);
    }
    
    public static void glVertexAttribPointerNV(final int index, final int size, final int type, final int stride, final FloatBuffer buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribPointerNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(buffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
        }
        nglVertexAttribPointerNV(index, size, type, stride, MemoryUtil.getAddress(buffer), function_pointer);
    }
    
    public static void glVertexAttribPointerNV(final int index, final int size, final int type, final int stride, final ByteBuffer buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribPointerNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(buffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
        }
        nglVertexAttribPointerNV(index, size, type, stride, MemoryUtil.getAddress(buffer), function_pointer);
    }
    
    public static void glVertexAttribPointerNV(final int index, final int size, final int type, final int stride, final IntBuffer buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribPointerNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(buffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
        }
        nglVertexAttribPointerNV(index, size, type, stride, MemoryUtil.getAddress(buffer), function_pointer);
    }
    
    public static void glVertexAttribPointerNV(final int index, final int size, final int type, final int stride, final ShortBuffer buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribPointerNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(buffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
        }
        nglVertexAttribPointerNV(index, size, type, stride, MemoryUtil.getAddress(buffer), function_pointer);
    }
    
    static native void nglVertexAttribPointerNV(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glVertexAttribPointerNV(final int index, final int size, final int type, final int stride, final long buffer_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribPointerNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOenabled(caps);
        nglVertexAttribPointerNVBO(index, size, type, stride, buffer_buffer_offset, function_pointer);
    }
    
    static native void nglVertexAttribPointerNVBO(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glVertexAttrib1sNV(final int index, final short x) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib1sNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib1sNV(index, x, function_pointer);
    }
    
    static native void nglVertexAttrib1sNV(final int p0, final short p1, final long p2);
    
    public static void glVertexAttrib1fNV(final int index, final float x) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib1fNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib1fNV(index, x, function_pointer);
    }
    
    static native void nglVertexAttrib1fNV(final int p0, final float p1, final long p2);
    
    public static void glVertexAttrib1dNV(final int index, final double x) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib1dNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib1dNV(index, x, function_pointer);
    }
    
    static native void nglVertexAttrib1dNV(final int p0, final double p1, final long p2);
    
    public static void glVertexAttrib2sNV(final int index, final short x, final short y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib2sNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib2sNV(index, x, y, function_pointer);
    }
    
    static native void nglVertexAttrib2sNV(final int p0, final short p1, final short p2, final long p3);
    
    public static void glVertexAttrib2fNV(final int index, final float x, final float y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib2fNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib2fNV(index, x, y, function_pointer);
    }
    
    static native void nglVertexAttrib2fNV(final int p0, final float p1, final float p2, final long p3);
    
    public static void glVertexAttrib2dNV(final int index, final double x, final double y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib2dNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib2dNV(index, x, y, function_pointer);
    }
    
    static native void nglVertexAttrib2dNV(final int p0, final double p1, final double p2, final long p3);
    
    public static void glVertexAttrib3sNV(final int index, final short x, final short y, final short z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib3sNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib3sNV(index, x, y, z, function_pointer);
    }
    
    static native void nglVertexAttrib3sNV(final int p0, final short p1, final short p2, final short p3, final long p4);
    
    public static void glVertexAttrib3fNV(final int index, final float x, final float y, final float z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib3fNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib3fNV(index, x, y, z, function_pointer);
    }
    
    static native void nglVertexAttrib3fNV(final int p0, final float p1, final float p2, final float p3, final long p4);
    
    public static void glVertexAttrib3dNV(final int index, final double x, final double y, final double z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib3dNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib3dNV(index, x, y, z, function_pointer);
    }
    
    static native void nglVertexAttrib3dNV(final int p0, final double p1, final double p2, final double p3, final long p4);
    
    public static void glVertexAttrib4sNV(final int index, final short x, final short y, final short z, final short w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib4sNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib4sNV(index, x, y, z, w, function_pointer);
    }
    
    static native void nglVertexAttrib4sNV(final int p0, final short p1, final short p2, final short p3, final short p4, final long p5);
    
    public static void glVertexAttrib4fNV(final int index, final float x, final float y, final float z, final float w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib4fNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib4fNV(index, x, y, z, w, function_pointer);
    }
    
    static native void nglVertexAttrib4fNV(final int p0, final float p1, final float p2, final float p3, final float p4, final long p5);
    
    public static void glVertexAttrib4dNV(final int index, final double x, final double y, final double z, final double w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib4dNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib4dNV(index, x, y, z, w, function_pointer);
    }
    
    static native void nglVertexAttrib4dNV(final int p0, final double p1, final double p2, final double p3, final double p4, final long p5);
    
    public static void glVertexAttrib4ubNV(final int index, final byte x, final byte y, final byte z, final byte w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib4ubNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib4ubNV(index, x, y, z, w, function_pointer);
    }
    
    static native void nglVertexAttrib4ubNV(final int p0, final byte p1, final byte p2, final byte p3, final byte p4, final long p5);
    
    public static void glVertexAttribs1NV(final int index, final ShortBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribs1svNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(v);
        nglVertexAttribs1svNV(index, v.remaining(), MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribs1svNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glVertexAttribs1NV(final int index, final FloatBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribs1fvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(v);
        nglVertexAttribs1fvNV(index, v.remaining(), MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribs1fvNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glVertexAttribs1NV(final int index, final DoubleBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribs1dvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(v);
        nglVertexAttribs1dvNV(index, v.remaining(), MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribs1dvNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glVertexAttribs2NV(final int index, final ShortBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribs2svNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(v);
        nglVertexAttribs2svNV(index, v.remaining() >> 1, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribs2svNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glVertexAttribs2NV(final int index, final FloatBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribs2fvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(v);
        nglVertexAttribs2fvNV(index, v.remaining() >> 1, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribs2fvNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glVertexAttribs2NV(final int index, final DoubleBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribs2dvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(v);
        nglVertexAttribs2dvNV(index, v.remaining() >> 1, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribs2dvNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glVertexAttribs3NV(final int index, final ShortBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribs3svNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(v);
        nglVertexAttribs3svNV(index, v.remaining() / 3, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribs3svNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glVertexAttribs3NV(final int index, final FloatBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribs3fvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(v);
        nglVertexAttribs3fvNV(index, v.remaining() / 3, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribs3fvNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glVertexAttribs3NV(final int index, final DoubleBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribs3dvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(v);
        nglVertexAttribs3dvNV(index, v.remaining() / 3, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribs3dvNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glVertexAttribs4NV(final int index, final ShortBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribs4svNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(v);
        nglVertexAttribs4svNV(index, v.remaining() >> 2, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribs4svNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glVertexAttribs4NV(final int index, final FloatBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribs4fvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(v);
        nglVertexAttribs4fvNV(index, v.remaining() >> 2, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribs4fvNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glVertexAttribs4NV(final int index, final DoubleBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribs4dvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(v);
        nglVertexAttribs4dvNV(index, v.remaining() >> 2, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribs4dvNV(final int p0, final int p1, final long p2, final long p3);
}

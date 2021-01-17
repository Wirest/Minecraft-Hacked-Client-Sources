// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import org.lwjgl.MemoryUtil;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;

public final class NVTransformFeedback
{
    public static final int GL_TRANSFORM_FEEDBACK_BUFFER_NV = 35982;
    public static final int GL_TRANSFORM_FEEDBACK_BUFFER_START_NV = 35972;
    public static final int GL_TRANSFORM_FEEDBACK_BUFFER_SIZE_NV = 35973;
    public static final int GL_TRANSFORM_FEEDBACK_RECORD_NV = 35974;
    public static final int GL_TRANSFORM_FEEDBACK_BUFFER_BINDING_NV = 35983;
    public static final int GL_INTERLEAVED_ATTRIBS_NV = 35980;
    public static final int GL_SEPARATE_ATTRIBS_NV = 35981;
    public static final int GL_PRIMITIVES_GENERATED_NV = 35975;
    public static final int GL_TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN_NV = 35976;
    public static final int GL_RASTERIZER_DISCARD_NV = 35977;
    public static final int GL_MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS_NV = 35978;
    public static final int GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS_NV = 35979;
    public static final int GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS_NV = 35968;
    public static final int GL_TRANSFORM_FEEDBACK_ATTRIBS_NV = 35966;
    public static final int GL_ACTIVE_VARYINGS_NV = 35969;
    public static final int GL_ACTIVE_VARYING_MAX_LENGTH_NV = 35970;
    public static final int GL_TRANSFORM_FEEDBACK_VARYINGS_NV = 35971;
    public static final int GL_TRANSFORM_FEEDBACK_BUFFER_MODE_NV = 35967;
    public static final int GL_BACK_PRIMARY_COLOR_NV = 35959;
    public static final int GL_BACK_SECONDARY_COLOR_NV = 35960;
    public static final int GL_TEXTURE_COORD_NV = 35961;
    public static final int GL_CLIP_DISTANCE_NV = 35962;
    public static final int GL_VERTEX_ID_NV = 35963;
    public static final int GL_PRIMITIVE_ID_NV = 35964;
    public static final int GL_GENERIC_ATTRIB_NV = 35965;
    public static final int GL_LAYER_NV = 36266;
    
    private NVTransformFeedback() {
    }
    
    public static void glBindBufferRangeNV(final int target, final int index, final int buffer, final long offset, final long size) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindBufferRangeNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBindBufferRangeNV(target, index, buffer, offset, size, function_pointer);
    }
    
    static native void nglBindBufferRangeNV(final int p0, final int p1, final int p2, final long p3, final long p4, final long p5);
    
    public static void glBindBufferOffsetNV(final int target, final int index, final int buffer, final long offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindBufferOffsetNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBindBufferOffsetNV(target, index, buffer, offset, function_pointer);
    }
    
    static native void nglBindBufferOffsetNV(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glBindBufferBaseNV(final int target, final int index, final int buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindBufferBaseNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBindBufferBaseNV(target, index, buffer, function_pointer);
    }
    
    static native void nglBindBufferBaseNV(final int p0, final int p1, final int p2, final long p3);
    
    public static void glTransformFeedbackAttribsNV(final IntBuffer attribs, final int bufferMode) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTransformFeedbackAttribsNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(attribs, 3);
        nglTransformFeedbackAttribsNV(attribs.remaining() / 3, MemoryUtil.getAddress(attribs), bufferMode, function_pointer);
    }
    
    static native void nglTransformFeedbackAttribsNV(final int p0, final long p1, final int p2, final long p3);
    
    public static void glTransformFeedbackVaryingsNV(final int program, final IntBuffer locations, final int bufferMode) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTransformFeedbackVaryingsNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(locations);
        nglTransformFeedbackVaryingsNV(program, locations.remaining(), MemoryUtil.getAddress(locations), bufferMode, function_pointer);
    }
    
    static native void nglTransformFeedbackVaryingsNV(final int p0, final int p1, final long p2, final int p3, final long p4);
    
    public static void glBeginTransformFeedbackNV(final int primitiveMode) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBeginTransformFeedbackNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBeginTransformFeedbackNV(primitiveMode, function_pointer);
    }
    
    static native void nglBeginTransformFeedbackNV(final int p0, final long p1);
    
    public static void glEndTransformFeedbackNV() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glEndTransformFeedbackNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglEndTransformFeedbackNV(function_pointer);
    }
    
    static native void nglEndTransformFeedbackNV(final long p0);
    
    public static int glGetVaryingLocationNV(final int program, final ByteBuffer name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVaryingLocationNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(name);
        BufferChecks.checkNullTerminated(name);
        final int __result = nglGetVaryingLocationNV(program, MemoryUtil.getAddress(name), function_pointer);
        return __result;
    }
    
    static native int nglGetVaryingLocationNV(final int p0, final long p1, final long p2);
    
    public static int glGetVaryingLocationNV(final int program, final CharSequence name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVaryingLocationNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nglGetVaryingLocationNV(program, APIUtil.getBufferNT(caps, name), function_pointer);
        return __result;
    }
    
    public static void glGetActiveVaryingNV(final int program, final int index, final IntBuffer length, final IntBuffer size, final IntBuffer type, final ByteBuffer name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveVaryingNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (length != null) {
            BufferChecks.checkBuffer(length, 1);
        }
        BufferChecks.checkBuffer(size, 1);
        BufferChecks.checkBuffer(type, 1);
        BufferChecks.checkDirect(name);
        nglGetActiveVaryingNV(program, index, name.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(size), MemoryUtil.getAddress(type), MemoryUtil.getAddress(name), function_pointer);
    }
    
    static native void nglGetActiveVaryingNV(final int p0, final int p1, final int p2, final long p3, final long p4, final long p5, final long p6, final long p7);
    
    public static String glGetActiveVaryingNV(final int program, final int index, final int bufSize, final IntBuffer sizeType) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveVaryingNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(sizeType, 2);
        final IntBuffer name_length = APIUtil.getLengths(caps);
        final ByteBuffer name = APIUtil.getBufferByte(caps, bufSize);
        nglGetActiveVaryingNV(program, index, bufSize, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress(sizeType), MemoryUtil.getAddress(sizeType, sizeType.position() + 1), MemoryUtil.getAddress(name), function_pointer);
        name.limit(name_length.get(0));
        return APIUtil.getString(caps, name);
    }
    
    public static String glGetActiveVaryingNV(final int program, final int index, final int bufSize) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveVaryingNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer name_length = APIUtil.getLengths(caps);
        final ByteBuffer name = APIUtil.getBufferByte(caps, bufSize);
        nglGetActiveVaryingNV(program, index, bufSize, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress0(APIUtil.getBufferInt(caps)), MemoryUtil.getAddress(APIUtil.getBufferInt(caps), 1), MemoryUtil.getAddress(name), function_pointer);
        name.limit(name_length.get(0));
        return APIUtil.getString(caps, name);
    }
    
    public static int glGetActiveVaryingSizeNV(final int program, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveVaryingNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer size = APIUtil.getBufferInt(caps);
        nglGetActiveVaryingNV(program, index, 0, 0L, MemoryUtil.getAddress(size), MemoryUtil.getAddress(size, 1), APIUtil.getBufferByte0(caps), function_pointer);
        return size.get(0);
    }
    
    public static int glGetActiveVaryingTypeNV(final int program, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveVaryingNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer type = APIUtil.getBufferInt(caps);
        nglGetActiveVaryingNV(program, index, 0, 0L, MemoryUtil.getAddress(type, 1), MemoryUtil.getAddress(type), APIUtil.getBufferByte0(caps), function_pointer);
        return type.get(0);
    }
    
    public static void glActiveVaryingNV(final int program, final ByteBuffer name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glActiveVaryingNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(name);
        BufferChecks.checkNullTerminated(name);
        nglActiveVaryingNV(program, MemoryUtil.getAddress(name), function_pointer);
    }
    
    static native void nglActiveVaryingNV(final int p0, final long p1, final long p2);
    
    public static void glActiveVaryingNV(final int program, final CharSequence name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glActiveVaryingNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglActiveVaryingNV(program, APIUtil.getBufferNT(caps, name), function_pointer);
    }
    
    public static void glGetTransformFeedbackVaryingNV(final int program, final int index, final IntBuffer location) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTransformFeedbackVaryingNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(location, 1);
        nglGetTransformFeedbackVaryingNV(program, index, MemoryUtil.getAddress(location), function_pointer);
    }
    
    static native void nglGetTransformFeedbackVaryingNV(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetTransformFeedbackVaryingNV(final int program, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTransformFeedbackVaryingNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer location = APIUtil.getBufferInt(caps);
        nglGetTransformFeedbackVaryingNV(program, index, MemoryUtil.getAddress(location), function_pointer);
        return location.get(0);
    }
}

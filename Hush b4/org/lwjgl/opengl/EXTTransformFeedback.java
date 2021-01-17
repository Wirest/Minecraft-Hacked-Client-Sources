// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.Buffer;
import java.nio.IntBuffer;
import org.lwjgl.MemoryUtil;
import java.nio.ByteBuffer;
import org.lwjgl.BufferChecks;

public final class EXTTransformFeedback
{
    public static final int GL_TRANSFORM_FEEDBACK_BUFFER_EXT = 35982;
    public static final int GL_TRANSFORM_FEEDBACK_BUFFER_START_EXT = 35972;
    public static final int GL_TRANSFORM_FEEDBACK_BUFFER_SIZE_EXT = 35973;
    public static final int GL_TRANSFORM_FEEDBACK_BUFFER_BINDING_EXT = 35983;
    public static final int GL_INTERLEAVED_ATTRIBS_EXT = 35980;
    public static final int GL_SEPARATE_ATTRIBS_EXT = 35981;
    public static final int GL_PRIMITIVES_GENERATED_EXT = 35975;
    public static final int GL_TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN_EXT = 35976;
    public static final int GL_RASTERIZER_DISCARD_EXT = 35977;
    public static final int GL_MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS_EXT = 35978;
    public static final int GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS_EXT = 35979;
    public static final int GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS_EXT = 35968;
    public static final int GL_TRANSFORM_FEEDBACK_VARYINGS_EXT = 35971;
    public static final int GL_TRANSFORM_FEEDBACK_BUFFER_MODE_EXT = 35967;
    public static final int GL_TRANSFORM_FEEDBACK_VARYING_MAX_LENGTH_EXT = 35958;
    
    private EXTTransformFeedback() {
    }
    
    public static void glBindBufferRangeEXT(final int target, final int index, final int buffer, final long offset, final long size) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindBufferRangeEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBindBufferRangeEXT(target, index, buffer, offset, size, function_pointer);
    }
    
    static native void nglBindBufferRangeEXT(final int p0, final int p1, final int p2, final long p3, final long p4, final long p5);
    
    public static void glBindBufferOffsetEXT(final int target, final int index, final int buffer, final long offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindBufferOffsetEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBindBufferOffsetEXT(target, index, buffer, offset, function_pointer);
    }
    
    static native void nglBindBufferOffsetEXT(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glBindBufferBaseEXT(final int target, final int index, final int buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindBufferBaseEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBindBufferBaseEXT(target, index, buffer, function_pointer);
    }
    
    static native void nglBindBufferBaseEXT(final int p0, final int p1, final int p2, final long p3);
    
    public static void glBeginTransformFeedbackEXT(final int primitiveMode) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBeginTransformFeedbackEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBeginTransformFeedbackEXT(primitiveMode, function_pointer);
    }
    
    static native void nglBeginTransformFeedbackEXT(final int p0, final long p1);
    
    public static void glEndTransformFeedbackEXT() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glEndTransformFeedbackEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglEndTransformFeedbackEXT(function_pointer);
    }
    
    static native void nglEndTransformFeedbackEXT(final long p0);
    
    public static void glTransformFeedbackVaryingsEXT(final int program, final int count, final ByteBuffer varyings, final int bufferMode) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTransformFeedbackVaryingsEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(varyings);
        BufferChecks.checkNullTerminated(varyings, count);
        nglTransformFeedbackVaryingsEXT(program, count, MemoryUtil.getAddress(varyings), bufferMode, function_pointer);
    }
    
    static native void nglTransformFeedbackVaryingsEXT(final int p0, final int p1, final long p2, final int p3, final long p4);
    
    public static void glTransformFeedbackVaryingsEXT(final int program, final CharSequence[] varyings, final int bufferMode) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTransformFeedbackVaryingsEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkArray(varyings);
        nglTransformFeedbackVaryingsEXT(program, varyings.length, APIUtil.getBufferNT(caps, varyings), bufferMode, function_pointer);
    }
    
    public static void glGetTransformFeedbackVaryingEXT(final int program, final int index, final IntBuffer length, final IntBuffer size, final IntBuffer type, final ByteBuffer name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTransformFeedbackVaryingEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (length != null) {
            BufferChecks.checkBuffer(length, 1);
        }
        BufferChecks.checkBuffer(size, 1);
        BufferChecks.checkBuffer(type, 1);
        BufferChecks.checkDirect(name);
        nglGetTransformFeedbackVaryingEXT(program, index, name.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(size), MemoryUtil.getAddress(type), MemoryUtil.getAddress(name), function_pointer);
    }
    
    static native void nglGetTransformFeedbackVaryingEXT(final int p0, final int p1, final int p2, final long p3, final long p4, final long p5, final long p6, final long p7);
    
    public static String glGetTransformFeedbackVaryingEXT(final int program, final int index, final int bufSize, final IntBuffer size, final IntBuffer type) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetTransformFeedbackVaryingEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(size, 1);
        BufferChecks.checkBuffer(type, 1);
        final IntBuffer name_length = APIUtil.getLengths(caps);
        final ByteBuffer name = APIUtil.getBufferByte(caps, bufSize);
        nglGetTransformFeedbackVaryingEXT(program, index, bufSize, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress(size), MemoryUtil.getAddress(type), MemoryUtil.getAddress(name), function_pointer);
        name.limit(name_length.get(0));
        return APIUtil.getString(caps, name);
    }
}

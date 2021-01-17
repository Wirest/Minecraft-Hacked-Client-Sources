// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.Buffer;
import java.nio.ShortBuffer;
import java.nio.IntBuffer;
import org.lwjgl.MemoryUtil;
import java.nio.ByteBuffer;
import org.lwjgl.BufferChecks;

public final class GL31
{
    public static final int GL_RED_SNORM = 36752;
    public static final int GL_RG_SNORM = 36753;
    public static final int GL_RGB_SNORM = 36754;
    public static final int GL_RGBA_SNORM = 36755;
    public static final int GL_R8_SNORM = 36756;
    public static final int GL_RG8_SNORM = 36757;
    public static final int GL_RGB8_SNORM = 36758;
    public static final int GL_RGBA8_SNORM = 36759;
    public static final int GL_R16_SNORM = 36760;
    public static final int GL_RG16_SNORM = 36761;
    public static final int GL_RGB16_SNORM = 36762;
    public static final int GL_RGBA16_SNORM = 36763;
    public static final int GL_SIGNED_NORMALIZED = 36764;
    public static final int GL_COPY_READ_BUFFER_BINDING = 36662;
    public static final int GL_COPY_WRITE_BUFFER_BINDING = 36663;
    public static final int GL_COPY_READ_BUFFER = 36662;
    public static final int GL_COPY_WRITE_BUFFER = 36663;
    public static final int GL_PRIMITIVE_RESTART = 36765;
    public static final int GL_PRIMITIVE_RESTART_INDEX = 36766;
    public static final int GL_TEXTURE_BUFFER = 35882;
    public static final int GL_MAX_TEXTURE_BUFFER_SIZE = 35883;
    public static final int GL_TEXTURE_BINDING_BUFFER = 35884;
    public static final int GL_TEXTURE_BUFFER_DATA_STORE_BINDING = 35885;
    public static final int GL_TEXTURE_BUFFER_FORMAT = 35886;
    public static final int GL_TEXTURE_RECTANGLE = 34037;
    public static final int GL_TEXTURE_BINDING_RECTANGLE = 34038;
    public static final int GL_PROXY_TEXTURE_RECTANGLE = 34039;
    public static final int GL_MAX_RECTANGLE_TEXTURE_SIZE = 34040;
    public static final int GL_SAMPLER_2D_RECT = 35683;
    public static final int GL_SAMPLER_2D_RECT_SHADOW = 35684;
    public static final int GL_UNIFORM_BUFFER = 35345;
    public static final int GL_UNIFORM_BUFFER_BINDING = 35368;
    public static final int GL_UNIFORM_BUFFER_START = 35369;
    public static final int GL_UNIFORM_BUFFER_SIZE = 35370;
    public static final int GL_MAX_VERTEX_UNIFORM_BLOCKS = 35371;
    public static final int GL_MAX_GEOMETRY_UNIFORM_BLOCKS = 35372;
    public static final int GL_MAX_FRAGMENT_UNIFORM_BLOCKS = 35373;
    public static final int GL_MAX_COMBINED_UNIFORM_BLOCKS = 35374;
    public static final int GL_MAX_UNIFORM_BUFFER_BINDINGS = 35375;
    public static final int GL_MAX_UNIFORM_BLOCK_SIZE = 35376;
    public static final int GL_MAX_COMBINED_VERTEX_UNIFORM_COMPONENTS = 35377;
    public static final int GL_MAX_COMBINED_GEOMETRY_UNIFORM_COMPONENTS = 35378;
    public static final int GL_MAX_COMBINED_FRAGMENT_UNIFORM_COMPONENTS = 35379;
    public static final int GL_UNIFORM_BUFFER_OFFSET_ALIGNMENT = 35380;
    public static final int GL_ACTIVE_UNIFORM_BLOCK_MAX_NAME_LENGTH = 35381;
    public static final int GL_ACTIVE_UNIFORM_BLOCKS = 35382;
    public static final int GL_UNIFORM_TYPE = 35383;
    public static final int GL_UNIFORM_SIZE = 35384;
    public static final int GL_UNIFORM_NAME_LENGTH = 35385;
    public static final int GL_UNIFORM_BLOCK_INDEX = 35386;
    public static final int GL_UNIFORM_OFFSET = 35387;
    public static final int GL_UNIFORM_ARRAY_STRIDE = 35388;
    public static final int GL_UNIFORM_MATRIX_STRIDE = 35389;
    public static final int GL_UNIFORM_IS_ROW_MAJOR = 35390;
    public static final int GL_UNIFORM_BLOCK_BINDING = 35391;
    public static final int GL_UNIFORM_BLOCK_DATA_SIZE = 35392;
    public static final int GL_UNIFORM_BLOCK_NAME_LENGTH = 35393;
    public static final int GL_UNIFORM_BLOCK_ACTIVE_UNIFORMS = 35394;
    public static final int GL_UNIFORM_BLOCK_ACTIVE_UNIFORM_INDICES = 35395;
    public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_VERTEX_SHADER = 35396;
    public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_GEOMETRY_SHADER = 35397;
    public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_FRAGMENT_SHADER = 35398;
    public static final int GL_INVALID_INDEX = -1;
    
    private GL31() {
    }
    
    public static void glDrawArraysInstanced(final int mode, final int first, final int count, final int primcount) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawArraysInstanced;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDrawArraysInstanced(mode, first, count, primcount, function_pointer);
    }
    
    static native void nglDrawArraysInstanced(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glDrawElementsInstanced(final int mode, final ByteBuffer indices, final int primcount) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawElementsInstanced;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureElementVBOdisabled(caps);
        BufferChecks.checkDirect(indices);
        nglDrawElementsInstanced(mode, indices.remaining(), 5121, MemoryUtil.getAddress(indices), primcount, function_pointer);
    }
    
    public static void glDrawElementsInstanced(final int mode, final IntBuffer indices, final int primcount) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawElementsInstanced;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureElementVBOdisabled(caps);
        BufferChecks.checkDirect(indices);
        nglDrawElementsInstanced(mode, indices.remaining(), 5125, MemoryUtil.getAddress(indices), primcount, function_pointer);
    }
    
    public static void glDrawElementsInstanced(final int mode, final ShortBuffer indices, final int primcount) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawElementsInstanced;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureElementVBOdisabled(caps);
        BufferChecks.checkDirect(indices);
        nglDrawElementsInstanced(mode, indices.remaining(), 5123, MemoryUtil.getAddress(indices), primcount, function_pointer);
    }
    
    static native void nglDrawElementsInstanced(final int p0, final int p1, final int p2, final long p3, final int p4, final long p5);
    
    public static void glDrawElementsInstanced(final int mode, final int indices_count, final int type, final long indices_buffer_offset, final int primcount) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawElementsInstanced;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureElementVBOenabled(caps);
        nglDrawElementsInstancedBO(mode, indices_count, type, indices_buffer_offset, primcount, function_pointer);
    }
    
    static native void nglDrawElementsInstancedBO(final int p0, final int p1, final int p2, final long p3, final int p4, final long p5);
    
    public static void glCopyBufferSubData(final int readtarget, final int writetarget, final long readoffset, final long writeoffset, final long size) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCopyBufferSubData;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglCopyBufferSubData(readtarget, writetarget, readoffset, writeoffset, size, function_pointer);
    }
    
    static native void nglCopyBufferSubData(final int p0, final int p1, final long p2, final long p3, final long p4, final long p5);
    
    public static void glPrimitiveRestartIndex(final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPrimitiveRestartIndex;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglPrimitiveRestartIndex(index, function_pointer);
    }
    
    static native void nglPrimitiveRestartIndex(final int p0, final long p1);
    
    public static void glTexBuffer(final int target, final int internalformat, final int buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTexBuffer;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTexBuffer(target, internalformat, buffer, function_pointer);
    }
    
    static native void nglTexBuffer(final int p0, final int p1, final int p2, final long p3);
    
    public static void glGetUniformIndices(final int program, final ByteBuffer uniformNames, final IntBuffer uniformIndices) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetUniformIndices;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(uniformNames);
        BufferChecks.checkNullTerminated(uniformNames, uniformIndices.remaining());
        BufferChecks.checkDirect(uniformIndices);
        nglGetUniformIndices(program, uniformIndices.remaining(), MemoryUtil.getAddress(uniformNames), MemoryUtil.getAddress(uniformIndices), function_pointer);
    }
    
    static native void nglGetUniformIndices(final int p0, final int p1, final long p2, final long p3, final long p4);
    
    public static void glGetUniformIndices(final int program, final CharSequence[] uniformNames, final IntBuffer uniformIndices) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetUniformIndices;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkArray(uniformNames);
        BufferChecks.checkBuffer(uniformIndices, uniformNames.length);
        nglGetUniformIndices(program, uniformNames.length, APIUtil.getBufferNT(caps, uniformNames), MemoryUtil.getAddress(uniformIndices), function_pointer);
    }
    
    public static void glGetActiveUniforms(final int program, final IntBuffer uniformIndices, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveUniformsiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(uniformIndices);
        BufferChecks.checkBuffer(params, uniformIndices.remaining());
        nglGetActiveUniformsiv(program, uniformIndices.remaining(), MemoryUtil.getAddress(uniformIndices), pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetActiveUniformsiv(final int p0, final int p1, final long p2, final int p3, final long p4, final long p5);
    
    @Deprecated
    public static int glGetActiveUniforms(final int program, final int uniformIndex, final int pname) {
        return glGetActiveUniformsi(program, uniformIndex, pname);
    }
    
    public static int glGetActiveUniformsi(final int program, final int uniformIndex, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveUniformsiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetActiveUniformsiv(program, 1, MemoryUtil.getAddress(params.put(1, uniformIndex), 1), pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetActiveUniformName(final int program, final int uniformIndex, final IntBuffer length, final ByteBuffer uniformName) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveUniformName;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (length != null) {
            BufferChecks.checkBuffer(length, 1);
        }
        BufferChecks.checkDirect(uniformName);
        nglGetActiveUniformName(program, uniformIndex, uniformName.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(uniformName), function_pointer);
    }
    
    static native void nglGetActiveUniformName(final int p0, final int p1, final int p2, final long p3, final long p4, final long p5);
    
    public static String glGetActiveUniformName(final int program, final int uniformIndex, final int bufSize) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveUniformName;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer uniformName_length = APIUtil.getLengths(caps);
        final ByteBuffer uniformName = APIUtil.getBufferByte(caps, bufSize);
        nglGetActiveUniformName(program, uniformIndex, bufSize, MemoryUtil.getAddress0(uniformName_length), MemoryUtil.getAddress(uniformName), function_pointer);
        uniformName.limit(uniformName_length.get(0));
        return APIUtil.getString(caps, uniformName);
    }
    
    public static int glGetUniformBlockIndex(final int program, final ByteBuffer uniformBlockName) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetUniformBlockIndex;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(uniformBlockName);
        BufferChecks.checkNullTerminated(uniformBlockName);
        final int __result = nglGetUniformBlockIndex(program, MemoryUtil.getAddress(uniformBlockName), function_pointer);
        return __result;
    }
    
    static native int nglGetUniformBlockIndex(final int p0, final long p1, final long p2);
    
    public static int glGetUniformBlockIndex(final int program, final CharSequence uniformBlockName) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetUniformBlockIndex;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nglGetUniformBlockIndex(program, APIUtil.getBufferNT(caps, uniformBlockName), function_pointer);
        return __result;
    }
    
    public static void glGetActiveUniformBlock(final int program, final int uniformBlockIndex, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveUniformBlockiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 16);
        nglGetActiveUniformBlockiv(program, uniformBlockIndex, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetActiveUniformBlockiv(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    @Deprecated
    public static int glGetActiveUniformBlock(final int program, final int uniformBlockIndex, final int pname) {
        return glGetActiveUniformBlocki(program, uniformBlockIndex, pname);
    }
    
    public static int glGetActiveUniformBlocki(final int program, final int uniformBlockIndex, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveUniformBlockiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetActiveUniformBlockiv(program, uniformBlockIndex, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetActiveUniformBlockName(final int program, final int uniformBlockIndex, final IntBuffer length, final ByteBuffer uniformBlockName) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveUniformBlockName;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (length != null) {
            BufferChecks.checkBuffer(length, 1);
        }
        BufferChecks.checkDirect(uniformBlockName);
        nglGetActiveUniformBlockName(program, uniformBlockIndex, uniformBlockName.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(uniformBlockName), function_pointer);
    }
    
    static native void nglGetActiveUniformBlockName(final int p0, final int p1, final int p2, final long p3, final long p4, final long p5);
    
    public static String glGetActiveUniformBlockName(final int program, final int uniformBlockIndex, final int bufSize) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveUniformBlockName;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer uniformBlockName_length = APIUtil.getLengths(caps);
        final ByteBuffer uniformBlockName = APIUtil.getBufferByte(caps, bufSize);
        nglGetActiveUniformBlockName(program, uniformBlockIndex, bufSize, MemoryUtil.getAddress0(uniformBlockName_length), MemoryUtil.getAddress(uniformBlockName), function_pointer);
        uniformBlockName.limit(uniformBlockName_length.get(0));
        return APIUtil.getString(caps, uniformBlockName);
    }
    
    public static void glUniformBlockBinding(final int program, final int uniformBlockIndex, final int uniformBlockBinding) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniformBlockBinding;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniformBlockBinding(program, uniformBlockIndex, uniformBlockBinding, function_pointer);
    }
    
    static native void nglUniformBlockBinding(final int p0, final int p1, final int p2, final long p3);
}

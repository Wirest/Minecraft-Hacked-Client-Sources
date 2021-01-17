// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.LWJGLUtil;
import java.nio.ShortBuffer;
import java.nio.ByteBuffer;
import org.lwjgl.MemoryUtil;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;

public final class EXTGpuShader4
{
    public static final int GL_VERTEX_ATTRIB_ARRAY_INTEGER_EXT = 35069;
    public static final int GL_SAMPLER_1D_ARRAY_EXT = 36288;
    public static final int GL_SAMPLER_2D_ARRAY_EXT = 36289;
    public static final int GL_SAMPLER_BUFFER_EXT = 36290;
    public static final int GL_SAMPLER_1D_ARRAY_SHADOW_EXT = 36291;
    public static final int GL_SAMPLER_2D_ARRAY_SHADOW_EXT = 36292;
    public static final int GL_SAMPLER_CUBE_SHADOW_EXT = 36293;
    public static final int GL_UNSIGNED_INT_VEC2_EXT = 36294;
    public static final int GL_UNSIGNED_INT_VEC3_EXT = 36295;
    public static final int GL_UNSIGNED_INT_VEC4_EXT = 36296;
    public static final int GL_INT_SAMPLER_1D_EXT = 36297;
    public static final int GL_INT_SAMPLER_2D_EXT = 36298;
    public static final int GL_INT_SAMPLER_3D_EXT = 36299;
    public static final int GL_INT_SAMPLER_CUBE_EXT = 36300;
    public static final int GL_INT_SAMPLER_2D_RECT_EXT = 36301;
    public static final int GL_INT_SAMPLER_1D_ARRAY_EXT = 36302;
    public static final int GL_INT_SAMPLER_2D_ARRAY_EXT = 36303;
    public static final int GL_INT_SAMPLER_BUFFER_EXT = 36304;
    public static final int GL_UNSIGNED_INT_SAMPLER_1D_EXT = 36305;
    public static final int GL_UNSIGNED_INT_SAMPLER_2D_EXT = 36306;
    public static final int GL_UNSIGNED_INT_SAMPLER_3D_EXT = 36307;
    public static final int GL_UNSIGNED_INT_SAMPLER_CUBE_EXT = 36308;
    public static final int GL_UNSIGNED_INT_SAMPLER_2D_RECT_EXT = 36309;
    public static final int GL_UNSIGNED_INT_SAMPLER_1D_ARRAY_EXT = 36310;
    public static final int GL_UNSIGNED_INT_SAMPLER_2D_ARRAY_EXT = 36311;
    public static final int GL_UNSIGNED_INT_SAMPLER_BUFFER_EXT = 36312;
    public static final int GL_MIN_PROGRAM_TEXEL_OFFSET_EXT = 35076;
    public static final int GL_MAX_PROGRAM_TEXEL_OFFSET_EXT = 35077;
    
    private EXTGpuShader4() {
    }
    
    public static void glVertexAttribI1iEXT(final int index, final int x) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribI1iEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribI1iEXT(index, x, function_pointer);
    }
    
    static native void nglVertexAttribI1iEXT(final int p0, final int p1, final long p2);
    
    public static void glVertexAttribI2iEXT(final int index, final int x, final int y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribI2iEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribI2iEXT(index, x, y, function_pointer);
    }
    
    static native void nglVertexAttribI2iEXT(final int p0, final int p1, final int p2, final long p3);
    
    public static void glVertexAttribI3iEXT(final int index, final int x, final int y, final int z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribI3iEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribI3iEXT(index, x, y, z, function_pointer);
    }
    
    static native void nglVertexAttribI3iEXT(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glVertexAttribI4iEXT(final int index, final int x, final int y, final int z, final int w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribI4iEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribI4iEXT(index, x, y, z, w, function_pointer);
    }
    
    static native void nglVertexAttribI4iEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glVertexAttribI1uiEXT(final int index, final int x) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribI1uiEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribI1uiEXT(index, x, function_pointer);
    }
    
    static native void nglVertexAttribI1uiEXT(final int p0, final int p1, final long p2);
    
    public static void glVertexAttribI2uiEXT(final int index, final int x, final int y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribI2uiEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribI2uiEXT(index, x, y, function_pointer);
    }
    
    static native void nglVertexAttribI2uiEXT(final int p0, final int p1, final int p2, final long p3);
    
    public static void glVertexAttribI3uiEXT(final int index, final int x, final int y, final int z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribI3uiEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribI3uiEXT(index, x, y, z, function_pointer);
    }
    
    static native void nglVertexAttribI3uiEXT(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glVertexAttribI4uiEXT(final int index, final int x, final int y, final int z, final int w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribI4uiEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribI4uiEXT(index, x, y, z, w, function_pointer);
    }
    
    static native void nglVertexAttribI4uiEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glVertexAttribI1EXT(final int index, final IntBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribI1ivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 1);
        nglVertexAttribI1ivEXT(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribI1ivEXT(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribI2EXT(final int index, final IntBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribI2ivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 2);
        nglVertexAttribI2ivEXT(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribI2ivEXT(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribI3EXT(final int index, final IntBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribI3ivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 3);
        nglVertexAttribI3ivEXT(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribI3ivEXT(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribI4EXT(final int index, final IntBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribI4ivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 4);
        nglVertexAttribI4ivEXT(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribI4ivEXT(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribI1uEXT(final int index, final IntBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribI1uivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 1);
        nglVertexAttribI1uivEXT(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribI1uivEXT(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribI2uEXT(final int index, final IntBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribI2uivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 2);
        nglVertexAttribI2uivEXT(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribI2uivEXT(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribI3uEXT(final int index, final IntBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribI3uivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 3);
        nglVertexAttribI3uivEXT(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribI3uivEXT(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribI4uEXT(final int index, final IntBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribI4uivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 4);
        nglVertexAttribI4uivEXT(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribI4uivEXT(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribI4EXT(final int index, final ByteBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribI4bvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 4);
        nglVertexAttribI4bvEXT(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribI4bvEXT(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribI4EXT(final int index, final ShortBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribI4svEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 4);
        nglVertexAttribI4svEXT(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribI4svEXT(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribI4uEXT(final int index, final ByteBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribI4ubvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 4);
        nglVertexAttribI4ubvEXT(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribI4ubvEXT(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribI4uEXT(final int index, final ShortBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribI4usvEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 4);
        nglVertexAttribI4usvEXT(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribI4usvEXT(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribIPointerEXT(final int index, final int size, final int type, final int stride, final ByteBuffer buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribIPointerEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(buffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
        }
        nglVertexAttribIPointerEXT(index, size, type, stride, MemoryUtil.getAddress(buffer), function_pointer);
    }
    
    public static void glVertexAttribIPointerEXT(final int index, final int size, final int type, final int stride, final IntBuffer buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribIPointerEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(buffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
        }
        nglVertexAttribIPointerEXT(index, size, type, stride, MemoryUtil.getAddress(buffer), function_pointer);
    }
    
    public static void glVertexAttribIPointerEXT(final int index, final int size, final int type, final int stride, final ShortBuffer buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribIPointerEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(buffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
        }
        nglVertexAttribIPointerEXT(index, size, type, stride, MemoryUtil.getAddress(buffer), function_pointer);
    }
    
    static native void nglVertexAttribIPointerEXT(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glVertexAttribIPointerEXT(final int index, final int size, final int type, final int stride, final long buffer_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribIPointerEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOenabled(caps);
        nglVertexAttribIPointerEXTBO(index, size, type, stride, buffer_buffer_offset, function_pointer);
    }
    
    static native void nglVertexAttribIPointerEXTBO(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glGetVertexAttribIEXT(final int index, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexAttribIivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetVertexAttribIivEXT(index, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetVertexAttribIivEXT(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetVertexAttribIuEXT(final int index, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexAttribIuivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetVertexAttribIuivEXT(index, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetVertexAttribIuivEXT(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniform1uiEXT(final int location, final int v0) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform1uiEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform1uiEXT(location, v0, function_pointer);
    }
    
    static native void nglUniform1uiEXT(final int p0, final int p1, final long p2);
    
    public static void glUniform2uiEXT(final int location, final int v0, final int v1) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform2uiEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform2uiEXT(location, v0, v1, function_pointer);
    }
    
    static native void nglUniform2uiEXT(final int p0, final int p1, final int p2, final long p3);
    
    public static void glUniform3uiEXT(final int location, final int v0, final int v1, final int v2) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform3uiEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform3uiEXT(location, v0, v1, v2, function_pointer);
    }
    
    static native void nglUniform3uiEXT(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glUniform4uiEXT(final int location, final int v0, final int v1, final int v2, final int v3) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform4uiEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform4uiEXT(location, v0, v1, v2, v3, function_pointer);
    }
    
    static native void nglUniform4uiEXT(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glUniform1uEXT(final int location, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform1uivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglUniform1uivEXT(location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglUniform1uivEXT(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniform2uEXT(final int location, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform2uivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglUniform2uivEXT(location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglUniform2uivEXT(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniform3uEXT(final int location, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform3uivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglUniform3uivEXT(location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglUniform3uivEXT(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniform4uEXT(final int location, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform4uivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglUniform4uivEXT(location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglUniform4uivEXT(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetUniformuEXT(final int program, final int location, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetUniformuivEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglGetUniformuivEXT(program, location, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetUniformuivEXT(final int p0, final int p1, final long p2, final long p3);
    
    public static void glBindFragDataLocationEXT(final int program, final int colorNumber, final ByteBuffer name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindFragDataLocationEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(name);
        BufferChecks.checkNullTerminated(name);
        nglBindFragDataLocationEXT(program, colorNumber, MemoryUtil.getAddress(name), function_pointer);
    }
    
    static native void nglBindFragDataLocationEXT(final int p0, final int p1, final long p2, final long p3);
    
    public static void glBindFragDataLocationEXT(final int program, final int colorNumber, final CharSequence name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindFragDataLocationEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBindFragDataLocationEXT(program, colorNumber, APIUtil.getBufferNT(caps, name), function_pointer);
    }
    
    public static int glGetFragDataLocationEXT(final int program, final ByteBuffer name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetFragDataLocationEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(name);
        BufferChecks.checkNullTerminated(name);
        final int __result = nglGetFragDataLocationEXT(program, MemoryUtil.getAddress(name), function_pointer);
        return __result;
    }
    
    static native int nglGetFragDataLocationEXT(final int p0, final long p1, final long p2);
    
    public static int glGetFragDataLocationEXT(final int program, final CharSequence name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetFragDataLocationEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nglGetFragDataLocationEXT(program, APIUtil.getBufferNT(caps, name), function_pointer);
        return __result;
    }
}

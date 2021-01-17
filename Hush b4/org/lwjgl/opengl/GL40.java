// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.FloatBuffer;
import java.nio.Buffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import org.lwjgl.MemoryUtil;
import java.nio.ByteBuffer;
import org.lwjgl.BufferChecks;

public final class GL40
{
    public static final int GL_DRAW_INDIRECT_BUFFER = 36671;
    public static final int GL_DRAW_INDIRECT_BUFFER_BINDING = 36675;
    public static final int GL_GEOMETRY_SHADER_INVOCATIONS = 34943;
    public static final int GL_MAX_GEOMETRY_SHADER_INVOCATIONS = 36442;
    public static final int GL_MIN_FRAGMENT_INTERPOLATION_OFFSET = 36443;
    public static final int GL_MAX_FRAGMENT_INTERPOLATION_OFFSET = 36444;
    public static final int GL_FRAGMENT_INTERPOLATION_OFFSET_BITS = 36445;
    public static final int GL_MAX_VERTEX_STREAMS = 36465;
    public static final int GL_DOUBLE_VEC2 = 36860;
    public static final int GL_DOUBLE_VEC3 = 36861;
    public static final int GL_DOUBLE_VEC4 = 36862;
    public static final int GL_DOUBLE_MAT2 = 36678;
    public static final int GL_DOUBLE_MAT3 = 36679;
    public static final int GL_DOUBLE_MAT4 = 36680;
    public static final int GL_DOUBLE_MAT2x3 = 36681;
    public static final int GL_DOUBLE_MAT2x4 = 36682;
    public static final int GL_DOUBLE_MAT3x2 = 36683;
    public static final int GL_DOUBLE_MAT3x4 = 36684;
    public static final int GL_DOUBLE_MAT4x2 = 36685;
    public static final int GL_DOUBLE_MAT4x3 = 36686;
    public static final int GL_SAMPLE_SHADING = 35894;
    public static final int GL_MIN_SAMPLE_SHADING_VALUE = 35895;
    public static final int GL_ACTIVE_SUBROUTINES = 36325;
    public static final int GL_ACTIVE_SUBROUTINE_UNIFORMS = 36326;
    public static final int GL_ACTIVE_SUBROUTINE_UNIFORM_LOCATIONS = 36423;
    public static final int GL_ACTIVE_SUBROUTINE_MAX_LENGTH = 36424;
    public static final int GL_ACTIVE_SUBROUTINE_UNIFORM_MAX_LENGTH = 36425;
    public static final int GL_MAX_SUBROUTINES = 36327;
    public static final int GL_MAX_SUBROUTINE_UNIFORM_LOCATIONS = 36328;
    public static final int GL_NUM_COMPATIBLE_SUBROUTINES = 36426;
    public static final int GL_COMPATIBLE_SUBROUTINES = 36427;
    public static final int GL_PATCHES = 14;
    public static final int GL_PATCH_VERTICES = 36466;
    public static final int GL_PATCH_DEFAULT_INNER_LEVEL = 36467;
    public static final int GL_PATCH_DEFAULT_OUTER_LEVEL = 36468;
    public static final int GL_TESS_CONTROL_OUTPUT_VERTICES = 36469;
    public static final int GL_TESS_GEN_MODE = 36470;
    public static final int GL_TESS_GEN_SPACING = 36471;
    public static final int GL_TESS_GEN_VERTEX_ORDER = 36472;
    public static final int GL_TESS_GEN_POINT_MODE = 36473;
    public static final int GL_ISOLINES = 36474;
    public static final int GL_FRACTIONAL_ODD = 36475;
    public static final int GL_FRACTIONAL_EVEN = 36476;
    public static final int GL_MAX_PATCH_VERTICES = 36477;
    public static final int GL_MAX_TESS_GEN_LEVEL = 36478;
    public static final int GL_MAX_TESS_CONTROL_UNIFORM_COMPONENTS = 36479;
    public static final int GL_MAX_TESS_EVALUATION_UNIFORM_COMPONENTS = 36480;
    public static final int GL_MAX_TESS_CONTROL_TEXTURE_IMAGE_UNITS = 36481;
    public static final int GL_MAX_TESS_EVALUATION_TEXTURE_IMAGE_UNITS = 36482;
    public static final int GL_MAX_TESS_CONTROL_OUTPUT_COMPONENTS = 36483;
    public static final int GL_MAX_TESS_PATCH_COMPONENTS = 36484;
    public static final int GL_MAX_TESS_CONTROL_TOTAL_OUTPUT_COMPONENTS = 36485;
    public static final int GL_MAX_TESS_EVALUATION_OUTPUT_COMPONENTS = 36486;
    public static final int GL_MAX_TESS_CONTROL_UNIFORM_BLOCKS = 36489;
    public static final int GL_MAX_TESS_EVALUATION_UNIFORM_BLOCKS = 36490;
    public static final int GL_MAX_TESS_CONTROL_INPUT_COMPONENTS = 34924;
    public static final int GL_MAX_TESS_EVALUATION_INPUT_COMPONENTS = 34925;
    public static final int GL_MAX_COMBINED_TESS_CONTROL_UNIFORM_COMPONENTS = 36382;
    public static final int GL_MAX_COMBINED_TESS_EVALUATION_UNIFORM_COMPONENTS = 36383;
    public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_TESS_CONTROL_SHADER = 34032;
    public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_TESS_EVALUATION_SHADER = 34033;
    public static final int GL_TESS_EVALUATION_SHADER = 36487;
    public static final int GL_TESS_CONTROL_SHADER = 36488;
    public static final int GL_TEXTURE_CUBE_MAP_ARRAY = 36873;
    public static final int GL_TEXTURE_BINDING_CUBE_MAP_ARRAY = 36874;
    public static final int GL_PROXY_TEXTURE_CUBE_MAP_ARRAY = 36875;
    public static final int GL_SAMPLER_CUBE_MAP_ARRAY = 36876;
    public static final int GL_SAMPLER_CUBE_MAP_ARRAY_SHADOW = 36877;
    public static final int GL_INT_SAMPLER_CUBE_MAP_ARRAY = 36878;
    public static final int GL_UNSIGNED_INT_SAMPLER_CUBE_MAP_ARRAY = 36879;
    public static final int GL_MIN_PROGRAM_TEXTURE_GATHER_OFFSET_ARB = 36446;
    public static final int GL_MAX_PROGRAM_TEXTURE_GATHER_OFFSET_ARB = 36447;
    public static final int GL_MAX_PROGRAM_TEXTURE_GATHER_COMPONENTS_ARB = 36767;
    public static final int GL_TRANSFORM_FEEDBACK = 36386;
    public static final int GL_TRANSFORM_FEEDBACK_PAUSED = 36387;
    public static final int GL_TRANSFORM_FEEDBACK_ACTIVE = 36388;
    public static final int GL_TRANSFORM_FEEDBACK_BUFFER_PAUSED = 36387;
    public static final int GL_TRANSFORM_FEEDBACK_BUFFER_ACTIVE = 36388;
    public static final int GL_TRANSFORM_FEEDBACK_BINDING = 36389;
    public static final int GL_MAX_TRANSFORM_FEEDBACK_BUFFERS = 36464;
    
    private GL40() {
    }
    
    public static void glBlendEquationi(final int buf, final int mode) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBlendEquationi;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBlendEquationi(buf, mode, function_pointer);
    }
    
    static native void nglBlendEquationi(final int p0, final int p1, final long p2);
    
    public static void glBlendEquationSeparatei(final int buf, final int modeRGB, final int modeAlpha) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBlendEquationSeparatei;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBlendEquationSeparatei(buf, modeRGB, modeAlpha, function_pointer);
    }
    
    static native void nglBlendEquationSeparatei(final int p0, final int p1, final int p2, final long p3);
    
    public static void glBlendFunci(final int buf, final int src, final int dst) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBlendFunci;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBlendFunci(buf, src, dst, function_pointer);
    }
    
    static native void nglBlendFunci(final int p0, final int p1, final int p2, final long p3);
    
    public static void glBlendFuncSeparatei(final int buf, final int srcRGB, final int dstRGB, final int srcAlpha, final int dstAlpha) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBlendFuncSeparatei;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBlendFuncSeparatei(buf, srcRGB, dstRGB, srcAlpha, dstAlpha, function_pointer);
    }
    
    static native void nglBlendFuncSeparatei(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glDrawArraysIndirect(final int mode, final ByteBuffer indirect) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawArraysIndirect;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureIndirectBOdisabled(caps);
        BufferChecks.checkBuffer(indirect, 16);
        nglDrawArraysIndirect(mode, MemoryUtil.getAddress(indirect), function_pointer);
    }
    
    static native void nglDrawArraysIndirect(final int p0, final long p1, final long p2);
    
    public static void glDrawArraysIndirect(final int mode, final long indirect_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawArraysIndirect;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureIndirectBOenabled(caps);
        nglDrawArraysIndirectBO(mode, indirect_buffer_offset, function_pointer);
    }
    
    static native void nglDrawArraysIndirectBO(final int p0, final long p1, final long p2);
    
    public static void glDrawArraysIndirect(final int mode, final IntBuffer indirect) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawArraysIndirect;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureIndirectBOdisabled(caps);
        BufferChecks.checkBuffer(indirect, 4);
        nglDrawArraysIndirect(mode, MemoryUtil.getAddress(indirect), function_pointer);
    }
    
    public static void glDrawElementsIndirect(final int mode, final int type, final ByteBuffer indirect) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawElementsIndirect;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureIndirectBOdisabled(caps);
        BufferChecks.checkBuffer(indirect, 20);
        nglDrawElementsIndirect(mode, type, MemoryUtil.getAddress(indirect), function_pointer);
    }
    
    static native void nglDrawElementsIndirect(final int p0, final int p1, final long p2, final long p3);
    
    public static void glDrawElementsIndirect(final int mode, final int type, final long indirect_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawElementsIndirect;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureIndirectBOenabled(caps);
        nglDrawElementsIndirectBO(mode, type, indirect_buffer_offset, function_pointer);
    }
    
    static native void nglDrawElementsIndirectBO(final int p0, final int p1, final long p2, final long p3);
    
    public static void glDrawElementsIndirect(final int mode, final int type, final IntBuffer indirect) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawElementsIndirect;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureIndirectBOdisabled(caps);
        BufferChecks.checkBuffer(indirect, 5);
        nglDrawElementsIndirect(mode, type, MemoryUtil.getAddress(indirect), function_pointer);
    }
    
    public static void glUniform1d(final int location, final double x) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform1d;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform1d(location, x, function_pointer);
    }
    
    static native void nglUniform1d(final int p0, final double p1, final long p2);
    
    public static void glUniform2d(final int location, final double x, final double y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform2d;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform2d(location, x, y, function_pointer);
    }
    
    static native void nglUniform2d(final int p0, final double p1, final double p2, final long p3);
    
    public static void glUniform3d(final int location, final double x, final double y, final double z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform3d;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform3d(location, x, y, z, function_pointer);
    }
    
    static native void nglUniform3d(final int p0, final double p1, final double p2, final double p3, final long p4);
    
    public static void glUniform4d(final int location, final double x, final double y, final double z, final double w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform4d;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform4d(location, x, y, z, w, function_pointer);
    }
    
    static native void nglUniform4d(final int p0, final double p1, final double p2, final double p3, final double p4, final long p5);
    
    public static void glUniform1(final int location, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform1dv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglUniform1dv(location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglUniform1dv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniform2(final int location, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform2dv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglUniform2dv(location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglUniform2dv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniform3(final int location, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform3dv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglUniform3dv(location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglUniform3dv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniform4(final int location, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform4dv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglUniform4dv(location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglUniform4dv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniformMatrix2(final int location, final boolean transpose, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniformMatrix2dv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglUniformMatrix2dv(location, value.remaining() >> 2, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglUniformMatrix2dv(final int p0, final int p1, final boolean p2, final long p3, final long p4);
    
    public static void glUniformMatrix3(final int location, final boolean transpose, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniformMatrix3dv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglUniformMatrix3dv(location, value.remaining() / 9, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglUniformMatrix3dv(final int p0, final int p1, final boolean p2, final long p3, final long p4);
    
    public static void glUniformMatrix4(final int location, final boolean transpose, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniformMatrix4dv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglUniformMatrix4dv(location, value.remaining() >> 4, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglUniformMatrix4dv(final int p0, final int p1, final boolean p2, final long p3, final long p4);
    
    public static void glUniformMatrix2x3(final int location, final boolean transpose, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniformMatrix2x3dv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglUniformMatrix2x3dv(location, value.remaining() / 6, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglUniformMatrix2x3dv(final int p0, final int p1, final boolean p2, final long p3, final long p4);
    
    public static void glUniformMatrix2x4(final int location, final boolean transpose, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniformMatrix2x4dv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglUniformMatrix2x4dv(location, value.remaining() >> 3, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglUniformMatrix2x4dv(final int p0, final int p1, final boolean p2, final long p3, final long p4);
    
    public static void glUniformMatrix3x2(final int location, final boolean transpose, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniformMatrix3x2dv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglUniformMatrix3x2dv(location, value.remaining() / 6, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglUniformMatrix3x2dv(final int p0, final int p1, final boolean p2, final long p3, final long p4);
    
    public static void glUniformMatrix3x4(final int location, final boolean transpose, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniformMatrix3x4dv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglUniformMatrix3x4dv(location, value.remaining() / 12, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglUniformMatrix3x4dv(final int p0, final int p1, final boolean p2, final long p3, final long p4);
    
    public static void glUniformMatrix4x2(final int location, final boolean transpose, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniformMatrix4x2dv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglUniformMatrix4x2dv(location, value.remaining() >> 3, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglUniformMatrix4x2dv(final int p0, final int p1, final boolean p2, final long p3, final long p4);
    
    public static void glUniformMatrix4x3(final int location, final boolean transpose, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniformMatrix4x3dv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglUniformMatrix4x3dv(location, value.remaining() / 12, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglUniformMatrix4x3dv(final int p0, final int p1, final boolean p2, final long p3, final long p4);
    
    public static void glGetUniform(final int program, final int location, final DoubleBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetUniformdv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglGetUniformdv(program, location, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetUniformdv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glMinSampleShading(final float value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMinSampleShading;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMinSampleShading(value, function_pointer);
    }
    
    static native void nglMinSampleShading(final float p0, final long p1);
    
    public static int glGetSubroutineUniformLocation(final int program, final int shadertype, final ByteBuffer name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSubroutineUniformLocation;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(name);
        BufferChecks.checkNullTerminated(name);
        final int __result = nglGetSubroutineUniformLocation(program, shadertype, MemoryUtil.getAddress(name), function_pointer);
        return __result;
    }
    
    static native int nglGetSubroutineUniformLocation(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetSubroutineUniformLocation(final int program, final int shadertype, final CharSequence name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSubroutineUniformLocation;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nglGetSubroutineUniformLocation(program, shadertype, APIUtil.getBufferNT(caps, name), function_pointer);
        return __result;
    }
    
    public static int glGetSubroutineIndex(final int program, final int shadertype, final ByteBuffer name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSubroutineIndex;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(name);
        BufferChecks.checkNullTerminated(name);
        final int __result = nglGetSubroutineIndex(program, shadertype, MemoryUtil.getAddress(name), function_pointer);
        return __result;
    }
    
    static native int nglGetSubroutineIndex(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetSubroutineIndex(final int program, final int shadertype, final CharSequence name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSubroutineIndex;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nglGetSubroutineIndex(program, shadertype, APIUtil.getBufferNT(caps, name), function_pointer);
        return __result;
    }
    
    public static void glGetActiveSubroutineUniform(final int program, final int shadertype, final int index, final int pname, final IntBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveSubroutineUniformiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(values, 1);
        nglGetActiveSubroutineUniformiv(program, shadertype, index, pname, MemoryUtil.getAddress(values), function_pointer);
    }
    
    static native void nglGetActiveSubroutineUniformiv(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    @Deprecated
    public static int glGetActiveSubroutineUniform(final int program, final int shadertype, final int index, final int pname) {
        return glGetActiveSubroutineUniformi(program, shadertype, index, pname);
    }
    
    public static int glGetActiveSubroutineUniformi(final int program, final int shadertype, final int index, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveSubroutineUniformiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer values = APIUtil.getBufferInt(caps);
        nglGetActiveSubroutineUniformiv(program, shadertype, index, pname, MemoryUtil.getAddress(values), function_pointer);
        return values.get(0);
    }
    
    public static void glGetActiveSubroutineUniformName(final int program, final int shadertype, final int index, final IntBuffer length, final ByteBuffer name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveSubroutineUniformName;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (length != null) {
            BufferChecks.checkBuffer(length, 1);
        }
        BufferChecks.checkDirect(name);
        nglGetActiveSubroutineUniformName(program, shadertype, index, name.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(name), function_pointer);
    }
    
    static native void nglGetActiveSubroutineUniformName(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5, final long p6);
    
    public static String glGetActiveSubroutineUniformName(final int program, final int shadertype, final int index, final int bufsize) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveSubroutineUniformName;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer name_length = APIUtil.getLengths(caps);
        final ByteBuffer name = APIUtil.getBufferByte(caps, bufsize);
        nglGetActiveSubroutineUniformName(program, shadertype, index, bufsize, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress(name), function_pointer);
        name.limit(name_length.get(0));
        return APIUtil.getString(caps, name);
    }
    
    public static void glGetActiveSubroutineName(final int program, final int shadertype, final int index, final IntBuffer length, final ByteBuffer name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveSubroutineName;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (length != null) {
            BufferChecks.checkBuffer(length, 1);
        }
        BufferChecks.checkDirect(name);
        nglGetActiveSubroutineName(program, shadertype, index, name.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(name), function_pointer);
    }
    
    static native void nglGetActiveSubroutineName(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5, final long p6);
    
    public static String glGetActiveSubroutineName(final int program, final int shadertype, final int index, final int bufsize) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveSubroutineName;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer name_length = APIUtil.getLengths(caps);
        final ByteBuffer name = APIUtil.getBufferByte(caps, bufsize);
        nglGetActiveSubroutineName(program, shadertype, index, bufsize, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress(name), function_pointer);
        name.limit(name_length.get(0));
        return APIUtil.getString(caps, name);
    }
    
    public static void glUniformSubroutinesu(final int shadertype, final IntBuffer indices) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniformSubroutinesuiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(indices);
        nglUniformSubroutinesuiv(shadertype, indices.remaining(), MemoryUtil.getAddress(indices), function_pointer);
    }
    
    static native void nglUniformSubroutinesuiv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetUniformSubroutineu(final int shadertype, final int location, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetUniformSubroutineuiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetUniformSubroutineuiv(shadertype, location, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetUniformSubroutineuiv(final int p0, final int p1, final long p2, final long p3);
    
    @Deprecated
    public static int glGetUniformSubroutineu(final int shadertype, final int location) {
        return glGetUniformSubroutineui(shadertype, location);
    }
    
    public static int glGetUniformSubroutineui(final int shadertype, final int location) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetUniformSubroutineuiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetUniformSubroutineuiv(shadertype, location, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetProgramStage(final int program, final int shadertype, final int pname, final IntBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramStageiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(values, 1);
        nglGetProgramStageiv(program, shadertype, pname, MemoryUtil.getAddress(values), function_pointer);
    }
    
    static native void nglGetProgramStageiv(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    @Deprecated
    public static int glGetProgramStage(final int program, final int shadertype, final int pname) {
        return glGetProgramStagei(program, shadertype, pname);
    }
    
    public static int glGetProgramStagei(final int program, final int shadertype, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramStageiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer values = APIUtil.getBufferInt(caps);
        nglGetProgramStageiv(program, shadertype, pname, MemoryUtil.getAddress(values), function_pointer);
        return values.get(0);
    }
    
    public static void glPatchParameteri(final int pname, final int value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPatchParameteri;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglPatchParameteri(pname, value, function_pointer);
    }
    
    static native void nglPatchParameteri(final int p0, final int p1, final long p2);
    
    public static void glPatchParameter(final int pname, final FloatBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPatchParameterfv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(values, 4);
        nglPatchParameterfv(pname, MemoryUtil.getAddress(values), function_pointer);
    }
    
    static native void nglPatchParameterfv(final int p0, final long p1, final long p2);
    
    public static void glBindTransformFeedback(final int target, final int id) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindTransformFeedback;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBindTransformFeedback(target, id, function_pointer);
    }
    
    static native void nglBindTransformFeedback(final int p0, final int p1, final long p2);
    
    public static void glDeleteTransformFeedbacks(final IntBuffer ids) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteTransformFeedbacks;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(ids);
        nglDeleteTransformFeedbacks(ids.remaining(), MemoryUtil.getAddress(ids), function_pointer);
    }
    
    static native void nglDeleteTransformFeedbacks(final int p0, final long p1, final long p2);
    
    public static void glDeleteTransformFeedbacks(final int id) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteTransformFeedbacks;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDeleteTransformFeedbacks(1, APIUtil.getInt(caps, id), function_pointer);
    }
    
    public static void glGenTransformFeedbacks(final IntBuffer ids) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenTransformFeedbacks;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(ids);
        nglGenTransformFeedbacks(ids.remaining(), MemoryUtil.getAddress(ids), function_pointer);
    }
    
    static native void nglGenTransformFeedbacks(final int p0, final long p1, final long p2);
    
    public static int glGenTransformFeedbacks() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenTransformFeedbacks;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer ids = APIUtil.getBufferInt(caps);
        nglGenTransformFeedbacks(1, MemoryUtil.getAddress(ids), function_pointer);
        return ids.get(0);
    }
    
    public static boolean glIsTransformFeedback(final int id) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsTransformFeedback;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsTransformFeedback(id, function_pointer);
        return __result;
    }
    
    static native boolean nglIsTransformFeedback(final int p0, final long p1);
    
    public static void glPauseTransformFeedback() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPauseTransformFeedback;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglPauseTransformFeedback(function_pointer);
    }
    
    static native void nglPauseTransformFeedback(final long p0);
    
    public static void glResumeTransformFeedback() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glResumeTransformFeedback;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglResumeTransformFeedback(function_pointer);
    }
    
    static native void nglResumeTransformFeedback(final long p0);
    
    public static void glDrawTransformFeedback(final int mode, final int id) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawTransformFeedback;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDrawTransformFeedback(mode, id, function_pointer);
    }
    
    static native void nglDrawTransformFeedback(final int p0, final int p1, final long p2);
    
    public static void glDrawTransformFeedbackStream(final int mode, final int id, final int stream) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawTransformFeedbackStream;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDrawTransformFeedbackStream(mode, id, stream, function_pointer);
    }
    
    static native void nglDrawTransformFeedbackStream(final int p0, final int p1, final int p2, final long p3);
    
    public static void glBeginQueryIndexed(final int target, final int index, final int id) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBeginQueryIndexed;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBeginQueryIndexed(target, index, id, function_pointer);
    }
    
    static native void nglBeginQueryIndexed(final int p0, final int p1, final int p2, final long p3);
    
    public static void glEndQueryIndexed(final int target, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glEndQueryIndexed;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglEndQueryIndexed(target, index, function_pointer);
    }
    
    static native void nglEndQueryIndexed(final int p0, final int p1, final long p2);
    
    public static void glGetQueryIndexed(final int target, final int index, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetQueryIndexediv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetQueryIndexediv(target, index, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetQueryIndexediv(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    @Deprecated
    public static int glGetQueryIndexed(final int target, final int index, final int pname) {
        return glGetQueryIndexedi(target, index, pname);
    }
    
    public static int glGetQueryIndexedi(final int target, final int index, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetQueryIndexediv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetQueryIndexediv(target, index, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
}

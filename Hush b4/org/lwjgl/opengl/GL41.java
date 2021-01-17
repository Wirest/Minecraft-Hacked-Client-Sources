// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.LWJGLUtil;
import java.nio.Buffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.MemoryUtil;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;

public final class GL41
{
    public static final int GL_SHADER_COMPILER = 36346;
    public static final int GL_NUM_SHADER_BINARY_FORMATS = 36345;
    public static final int GL_MAX_VERTEX_UNIFORM_VECTORS = 36347;
    public static final int GL_MAX_VARYING_VECTORS = 36348;
    public static final int GL_MAX_FRAGMENT_UNIFORM_VECTORS = 36349;
    public static final int GL_IMPLEMENTATION_COLOR_READ_TYPE = 35738;
    public static final int GL_IMPLEMENTATION_COLOR_READ_FORMAT = 35739;
    public static final int GL_FIXED = 5132;
    public static final int GL_LOW_FLOAT = 36336;
    public static final int GL_MEDIUM_FLOAT = 36337;
    public static final int GL_HIGH_FLOAT = 36338;
    public static final int GL_LOW_INT = 36339;
    public static final int GL_MEDIUM_INT = 36340;
    public static final int GL_HIGH_INT = 36341;
    public static final int GL_RGB565 = 36194;
    public static final int GL_PROGRAM_BINARY_RETRIEVABLE_HINT = 33367;
    public static final int GL_PROGRAM_BINARY_LENGTH = 34625;
    public static final int GL_NUM_PROGRAM_BINARY_FORMATS = 34814;
    public static final int GL_PROGRAM_BINARY_FORMATS = 34815;
    public static final int GL_VERTEX_SHADER_BIT = 1;
    public static final int GL_FRAGMENT_SHADER_BIT = 2;
    public static final int GL_GEOMETRY_SHADER_BIT = 4;
    public static final int GL_TESS_CONTROL_SHADER_BIT = 8;
    public static final int GL_TESS_EVALUATION_SHADER_BIT = 16;
    public static final int GL_ALL_SHADER_BITS = -1;
    public static final int GL_PROGRAM_SEPARABLE = 33368;
    public static final int GL_ACTIVE_PROGRAM = 33369;
    public static final int GL_PROGRAM_PIPELINE_BINDING = 33370;
    public static final int GL_MAX_VIEWPORTS = 33371;
    public static final int GL_VIEWPORT_SUBPIXEL_BITS = 33372;
    public static final int GL_VIEWPORT_BOUNDS_RANGE = 33373;
    public static final int GL_LAYER_PROVOKING_VERTEX = 33374;
    public static final int GL_VIEWPORT_INDEX_PROVOKING_VERTEX = 33375;
    public static final int GL_UNDEFINED_VERTEX = 33376;
    
    private GL41() {
    }
    
    public static void glReleaseShaderCompiler() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glReleaseShaderCompiler;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglReleaseShaderCompiler(function_pointer);
    }
    
    static native void nglReleaseShaderCompiler(final long p0);
    
    public static void glShaderBinary(final IntBuffer shaders, final int binaryformat, final ByteBuffer binary) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glShaderBinary;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(shaders);
        BufferChecks.checkDirect(binary);
        nglShaderBinary(shaders.remaining(), MemoryUtil.getAddress(shaders), binaryformat, MemoryUtil.getAddress(binary), binary.remaining(), function_pointer);
    }
    
    static native void nglShaderBinary(final int p0, final long p1, final int p2, final long p3, final int p4, final long p5);
    
    public static void glGetShaderPrecisionFormat(final int shadertype, final int precisiontype, final IntBuffer range, final IntBuffer precision) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetShaderPrecisionFormat;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(range, 2);
        BufferChecks.checkBuffer(precision, 1);
        nglGetShaderPrecisionFormat(shadertype, precisiontype, MemoryUtil.getAddress(range), MemoryUtil.getAddress(precision), function_pointer);
    }
    
    static native void nglGetShaderPrecisionFormat(final int p0, final int p1, final long p2, final long p3, final long p4);
    
    public static void glDepthRangef(final float n, final float f) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDepthRangef;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDepthRangef(n, f, function_pointer);
    }
    
    static native void nglDepthRangef(final float p0, final float p1, final long p2);
    
    public static void glClearDepthf(final float d) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glClearDepthf;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglClearDepthf(d, function_pointer);
    }
    
    static native void nglClearDepthf(final float p0, final long p1);
    
    public static void glGetProgramBinary(final int program, final IntBuffer length, final IntBuffer binaryFormat, final ByteBuffer binary) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramBinary;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (length != null) {
            BufferChecks.checkBuffer(length, 1);
        }
        BufferChecks.checkBuffer(binaryFormat, 1);
        BufferChecks.checkDirect(binary);
        nglGetProgramBinary(program, binary.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(binaryFormat), MemoryUtil.getAddress(binary), function_pointer);
    }
    
    static native void nglGetProgramBinary(final int p0, final int p1, final long p2, final long p3, final long p4, final long p5);
    
    public static void glProgramBinary(final int program, final int binaryFormat, final ByteBuffer binary) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramBinary;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(binary);
        nglProgramBinary(program, binaryFormat, MemoryUtil.getAddress(binary), binary.remaining(), function_pointer);
    }
    
    static native void nglProgramBinary(final int p0, final int p1, final long p2, final int p3, final long p4);
    
    public static void glProgramParameteri(final int program, final int pname, final int value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramParameteri;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramParameteri(program, pname, value, function_pointer);
    }
    
    static native void nglProgramParameteri(final int p0, final int p1, final int p2, final long p3);
    
    public static void glUseProgramStages(final int pipeline, final int stages, final int program) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUseProgramStages;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUseProgramStages(pipeline, stages, program, function_pointer);
    }
    
    static native void nglUseProgramStages(final int p0, final int p1, final int p2, final long p3);
    
    public static void glActiveShaderProgram(final int pipeline, final int program) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glActiveShaderProgram;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglActiveShaderProgram(pipeline, program, function_pointer);
    }
    
    static native void nglActiveShaderProgram(final int p0, final int p1, final long p2);
    
    public static int glCreateShaderProgram(final int type, final ByteBuffer string) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCreateShaderProgramv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(string);
        BufferChecks.checkNullTerminated(string);
        final int __result = nglCreateShaderProgramv(type, 1, MemoryUtil.getAddress(string), function_pointer);
        return __result;
    }
    
    static native int nglCreateShaderProgramv(final int p0, final int p1, final long p2, final long p3);
    
    public static int glCreateShaderProgram(final int type, final int count, final ByteBuffer strings) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCreateShaderProgramv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(strings);
        BufferChecks.checkNullTerminated(strings, count);
        final int __result = nglCreateShaderProgramv2(type, count, MemoryUtil.getAddress(strings), function_pointer);
        return __result;
    }
    
    static native int nglCreateShaderProgramv2(final int p0, final int p1, final long p2, final long p3);
    
    public static int glCreateShaderProgram(final int type, final ByteBuffer[] strings) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCreateShaderProgramv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkArray(strings, 1);
        final int __result = nglCreateShaderProgramv3(type, strings.length, strings, function_pointer);
        return __result;
    }
    
    static native int nglCreateShaderProgramv3(final int p0, final int p1, final ByteBuffer[] p2, final long p3);
    
    public static int glCreateShaderProgram(final int type, final CharSequence string) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCreateShaderProgramv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nglCreateShaderProgramv(type, 1, APIUtil.getBufferNT(caps, string), function_pointer);
        return __result;
    }
    
    public static int glCreateShaderProgram(final int type, final CharSequence[] strings) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCreateShaderProgramv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkArray(strings);
        final int __result = nglCreateShaderProgramv2(type, strings.length, APIUtil.getBufferNT(caps, strings), function_pointer);
        return __result;
    }
    
    public static void glBindProgramPipeline(final int pipeline) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindProgramPipeline;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBindProgramPipeline(pipeline, function_pointer);
    }
    
    static native void nglBindProgramPipeline(final int p0, final long p1);
    
    public static void glDeleteProgramPipelines(final IntBuffer pipelines) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteProgramPipelines;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pipelines);
        nglDeleteProgramPipelines(pipelines.remaining(), MemoryUtil.getAddress(pipelines), function_pointer);
    }
    
    static native void nglDeleteProgramPipelines(final int p0, final long p1, final long p2);
    
    public static void glDeleteProgramPipelines(final int pipeline) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteProgramPipelines;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDeleteProgramPipelines(1, APIUtil.getInt(caps, pipeline), function_pointer);
    }
    
    public static void glGenProgramPipelines(final IntBuffer pipelines) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenProgramPipelines;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pipelines);
        nglGenProgramPipelines(pipelines.remaining(), MemoryUtil.getAddress(pipelines), function_pointer);
    }
    
    static native void nglGenProgramPipelines(final int p0, final long p1, final long p2);
    
    public static int glGenProgramPipelines() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenProgramPipelines;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer pipelines = APIUtil.getBufferInt(caps);
        nglGenProgramPipelines(1, MemoryUtil.getAddress(pipelines), function_pointer);
        return pipelines.get(0);
    }
    
    public static boolean glIsProgramPipeline(final int pipeline) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsProgramPipeline;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsProgramPipeline(pipeline, function_pointer);
        return __result;
    }
    
    static native boolean nglIsProgramPipeline(final int p0, final long p1);
    
    public static void glGetProgramPipeline(final int pipeline, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramPipelineiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetProgramPipelineiv(pipeline, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetProgramPipelineiv(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetProgramPipelinei(final int pipeline, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramPipelineiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetProgramPipelineiv(pipeline, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glProgramUniform1i(final int program, final int location, final int v0) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform1i;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform1i(program, location, v0, function_pointer);
    }
    
    static native void nglProgramUniform1i(final int p0, final int p1, final int p2, final long p3);
    
    public static void glProgramUniform2i(final int program, final int location, final int v0, final int v1) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform2i;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform2i(program, location, v0, v1, function_pointer);
    }
    
    static native void nglProgramUniform2i(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glProgramUniform3i(final int program, final int location, final int v0, final int v1, final int v2) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform3i;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform3i(program, location, v0, v1, v2, function_pointer);
    }
    
    static native void nglProgramUniform3i(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glProgramUniform4i(final int program, final int location, final int v0, final int v1, final int v2, final int v3) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform4i;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform4i(program, location, v0, v1, v2, v3, function_pointer);
    }
    
    static native void nglProgramUniform4i(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6);
    
    public static void glProgramUniform1f(final int program, final int location, final float v0) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform1f;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform1f(program, location, v0, function_pointer);
    }
    
    static native void nglProgramUniform1f(final int p0, final int p1, final float p2, final long p3);
    
    public static void glProgramUniform2f(final int program, final int location, final float v0, final float v1) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform2f;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform2f(program, location, v0, v1, function_pointer);
    }
    
    static native void nglProgramUniform2f(final int p0, final int p1, final float p2, final float p3, final long p4);
    
    public static void glProgramUniform3f(final int program, final int location, final float v0, final float v1, final float v2) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform3f;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform3f(program, location, v0, v1, v2, function_pointer);
    }
    
    static native void nglProgramUniform3f(final int p0, final int p1, final float p2, final float p3, final float p4, final long p5);
    
    public static void glProgramUniform4f(final int program, final int location, final float v0, final float v1, final float v2, final float v3) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform4f;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform4f(program, location, v0, v1, v2, v3, function_pointer);
    }
    
    static native void nglProgramUniform4f(final int p0, final int p1, final float p2, final float p3, final float p4, final float p5, final long p6);
    
    public static void glProgramUniform1d(final int program, final int location, final double v0) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform1d;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform1d(program, location, v0, function_pointer);
    }
    
    static native void nglProgramUniform1d(final int p0, final int p1, final double p2, final long p3);
    
    public static void glProgramUniform2d(final int program, final int location, final double v0, final double v1) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform2d;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform2d(program, location, v0, v1, function_pointer);
    }
    
    static native void nglProgramUniform2d(final int p0, final int p1, final double p2, final double p3, final long p4);
    
    public static void glProgramUniform3d(final int program, final int location, final double v0, final double v1, final double v2) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform3d;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform3d(program, location, v0, v1, v2, function_pointer);
    }
    
    static native void nglProgramUniform3d(final int p0, final int p1, final double p2, final double p3, final double p4, final long p5);
    
    public static void glProgramUniform4d(final int program, final int location, final double v0, final double v1, final double v2, final double v3) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform4d;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform4d(program, location, v0, v1, v2, v3, function_pointer);
    }
    
    static native void nglProgramUniform4d(final int p0, final int p1, final double p2, final double p3, final double p4, final double p5, final long p6);
    
    public static void glProgramUniform1(final int program, final int location, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform1iv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform1iv(program, location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform1iv(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform2(final int program, final int location, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform2iv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform2iv(program, location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform2iv(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform3(final int program, final int location, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform3iv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform3iv(program, location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform3iv(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform4(final int program, final int location, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform4iv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform4iv(program, location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform4iv(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform1(final int program, final int location, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform1fv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform1fv(program, location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform1fv(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform2(final int program, final int location, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform2fv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform2fv(program, location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform2fv(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform3(final int program, final int location, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform3fv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform3fv(program, location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform3fv(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform4(final int program, final int location, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform4fv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform4fv(program, location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform4fv(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform1(final int program, final int location, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform1dv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform1dv(program, location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform1dv(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform2(final int program, final int location, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform2dv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform2dv(program, location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform2dv(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform3(final int program, final int location, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform3dv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform3dv(program, location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform3dv(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform4(final int program, final int location, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform4dv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform4dv(program, location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform4dv(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform1ui(final int program, final int location, final int v0) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform1ui;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform1ui(program, location, v0, function_pointer);
    }
    
    static native void nglProgramUniform1ui(final int p0, final int p1, final int p2, final long p3);
    
    public static void glProgramUniform2ui(final int program, final int location, final int v0, final int v1) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform2ui;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform2ui(program, location, v0, v1, function_pointer);
    }
    
    static native void nglProgramUniform2ui(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glProgramUniform3ui(final int program, final int location, final int v0, final int v1, final int v2) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform3ui;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform3ui(program, location, v0, v1, v2, function_pointer);
    }
    
    static native void nglProgramUniform3ui(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glProgramUniform4ui(final int program, final int location, final int v0, final int v1, final int v2, final int v3) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform4ui;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramUniform4ui(program, location, v0, v1, v2, v3, function_pointer);
    }
    
    static native void nglProgramUniform4ui(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6);
    
    public static void glProgramUniform1u(final int program, final int location, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform1uiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform1uiv(program, location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform1uiv(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform2u(final int program, final int location, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform2uiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform2uiv(program, location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform2uiv(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform3u(final int program, final int location, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform3uiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform3uiv(program, location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform3uiv(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniform4u(final int program, final int location, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniform4uiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniform4uiv(program, location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniform4uiv(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramUniformMatrix2(final int program, final int location, final boolean transpose, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix2fv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix2fv(program, location, value.remaining() >> 2, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix2fv(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix3(final int program, final int location, final boolean transpose, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix3fv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix3fv(program, location, value.remaining() / 9, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix3fv(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix4(final int program, final int location, final boolean transpose, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix4fv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix4fv(program, location, value.remaining() >> 4, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix4fv(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix2(final int program, final int location, final boolean transpose, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix2dv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix2dv(program, location, value.remaining() >> 2, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix2dv(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix3(final int program, final int location, final boolean transpose, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix3dv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix3dv(program, location, value.remaining() / 9, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix3dv(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix4(final int program, final int location, final boolean transpose, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix4dv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix4dv(program, location, value.remaining() >> 4, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix4dv(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix2x3(final int program, final int location, final boolean transpose, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix2x3fv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix2x3fv(program, location, value.remaining() / 6, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix2x3fv(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix3x2(final int program, final int location, final boolean transpose, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix3x2fv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix3x2fv(program, location, value.remaining() / 6, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix3x2fv(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix2x4(final int program, final int location, final boolean transpose, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix2x4fv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix2x4fv(program, location, value.remaining() >> 3, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix2x4fv(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix4x2(final int program, final int location, final boolean transpose, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix4x2fv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix4x2fv(program, location, value.remaining() >> 3, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix4x2fv(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix3x4(final int program, final int location, final boolean transpose, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix3x4fv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix3x4fv(program, location, value.remaining() / 12, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix3x4fv(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix4x3(final int program, final int location, final boolean transpose, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix4x3fv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix4x3fv(program, location, value.remaining() / 12, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix4x3fv(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix2x3(final int program, final int location, final boolean transpose, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix2x3dv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix2x3dv(program, location, value.remaining() / 6, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix2x3dv(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix3x2(final int program, final int location, final boolean transpose, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix3x2dv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix3x2dv(program, location, value.remaining() / 6, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix3x2dv(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix2x4(final int program, final int location, final boolean transpose, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix2x4dv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix2x4dv(program, location, value.remaining() >> 3, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix2x4dv(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix4x2(final int program, final int location, final boolean transpose, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix4x2dv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix4x2dv(program, location, value.remaining() >> 3, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix4x2dv(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix3x4(final int program, final int location, final boolean transpose, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix3x4dv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix3x4dv(program, location, value.remaining() / 12, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix3x4dv(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glProgramUniformMatrix4x3(final int program, final int location, final boolean transpose, final DoubleBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramUniformMatrix4x3dv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(value);
        nglProgramUniformMatrix4x3dv(program, location, value.remaining() / 12, transpose, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglProgramUniformMatrix4x3dv(final int p0, final int p1, final int p2, final boolean p3, final long p4, final long p5);
    
    public static void glValidateProgramPipeline(final int pipeline) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glValidateProgramPipeline;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglValidateProgramPipeline(pipeline, function_pointer);
    }
    
    static native void nglValidateProgramPipeline(final int p0, final long p1);
    
    public static void glGetProgramPipelineInfoLog(final int pipeline, final IntBuffer length, final ByteBuffer infoLog) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramPipelineInfoLog;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (length != null) {
            BufferChecks.checkBuffer(length, 1);
        }
        BufferChecks.checkDirect(infoLog);
        nglGetProgramPipelineInfoLog(pipeline, infoLog.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(infoLog), function_pointer);
    }
    
    static native void nglGetProgramPipelineInfoLog(final int p0, final int p1, final long p2, final long p3, final long p4);
    
    public static String glGetProgramPipelineInfoLog(final int pipeline, final int bufSize) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramPipelineInfoLog;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer infoLog_length = APIUtil.getLengths(caps);
        final ByteBuffer infoLog = APIUtil.getBufferByte(caps, bufSize);
        nglGetProgramPipelineInfoLog(pipeline, bufSize, MemoryUtil.getAddress0(infoLog_length), MemoryUtil.getAddress(infoLog), function_pointer);
        infoLog.limit(infoLog_length.get(0));
        return APIUtil.getString(caps, infoLog);
    }
    
    public static void glVertexAttribL1d(final int index, final double x) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL1d;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribL1d(index, x, function_pointer);
    }
    
    static native void nglVertexAttribL1d(final int p0, final double p1, final long p2);
    
    public static void glVertexAttribL2d(final int index, final double x, final double y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL2d;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribL2d(index, x, y, function_pointer);
    }
    
    static native void nglVertexAttribL2d(final int p0, final double p1, final double p2, final long p3);
    
    public static void glVertexAttribL3d(final int index, final double x, final double y, final double z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL3d;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribL3d(index, x, y, z, function_pointer);
    }
    
    static native void nglVertexAttribL3d(final int p0, final double p1, final double p2, final double p3, final long p4);
    
    public static void glVertexAttribL4d(final int index, final double x, final double y, final double z, final double w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL4d;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribL4d(index, x, y, z, w, function_pointer);
    }
    
    static native void nglVertexAttribL4d(final int p0, final double p1, final double p2, final double p3, final double p4, final long p5);
    
    public static void glVertexAttribL1(final int index, final DoubleBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL1dv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 1);
        nglVertexAttribL1dv(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribL1dv(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribL2(final int index, final DoubleBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL2dv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 2);
        nglVertexAttribL2dv(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribL2dv(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribL3(final int index, final DoubleBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL3dv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 3);
        nglVertexAttribL3dv(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribL3dv(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribL4(final int index, final DoubleBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribL4dv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 4);
        nglVertexAttribL4dv(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglVertexAttribL4dv(final int p0, final long p1, final long p2);
    
    public static void glVertexAttribLPointer(final int index, final int size, final int stride, final DoubleBuffer pointer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribLPointer;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(pointer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = pointer;
        }
        nglVertexAttribLPointer(index, size, 5130, stride, MemoryUtil.getAddress(pointer), function_pointer);
    }
    
    static native void nglVertexAttribLPointer(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glVertexAttribLPointer(final int index, final int size, final int stride, final long pointer_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribLPointer;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOenabled(caps);
        nglVertexAttribLPointerBO(index, size, 5130, stride, pointer_buffer_offset, function_pointer);
    }
    
    static native void nglVertexAttribLPointerBO(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glGetVertexAttribL(final int index, final int pname, final DoubleBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexAttribLdv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetVertexAttribLdv(index, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetVertexAttribLdv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glViewportArray(final int first, final FloatBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glViewportArrayv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(v);
        nglViewportArrayv(first, v.remaining() >> 2, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglViewportArrayv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glViewportIndexedf(final int index, final float x, final float y, final float w, final float h) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glViewportIndexedf;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglViewportIndexedf(index, x, y, w, h, function_pointer);
    }
    
    static native void nglViewportIndexedf(final int p0, final float p1, final float p2, final float p3, final float p4, final long p5);
    
    public static void glViewportIndexed(final int index, final FloatBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glViewportIndexedfv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 4);
        nglViewportIndexedfv(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglViewportIndexedfv(final int p0, final long p1, final long p2);
    
    public static void glScissorArray(final int first, final IntBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glScissorArrayv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(v);
        nglScissorArrayv(first, v.remaining() >> 2, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglScissorArrayv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glScissorIndexed(final int index, final int left, final int bottom, final int width, final int height) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glScissorIndexed;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglScissorIndexed(index, left, bottom, width, height, function_pointer);
    }
    
    static native void nglScissorIndexed(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glScissorIndexed(final int index, final IntBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glScissorIndexedv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(v, 4);
        nglScissorIndexedv(index, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglScissorIndexedv(final int p0, final long p1, final long p2);
    
    public static void glDepthRangeArray(final int first, final DoubleBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDepthRangeArrayv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(v);
        nglDepthRangeArrayv(first, v.remaining() >> 1, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglDepthRangeArrayv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glDepthRangeIndexed(final int index, final double n, final double f) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDepthRangeIndexed;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDepthRangeIndexed(index, n, f, function_pointer);
    }
    
    static native void nglDepthRangeIndexed(final int p0, final double p1, final double p2, final long p3);
    
    public static void glGetFloat(final int target, final int index, final FloatBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetFloati_v;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglGetFloati_v(target, index, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglGetFloati_v(final int p0, final int p1, final long p2, final long p3);
    
    public static float glGetFloat(final int target, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetFloati_v;
        BufferChecks.checkFunctionAddress(function_pointer);
        final FloatBuffer data = APIUtil.getBufferFloat(caps);
        nglGetFloati_v(target, index, MemoryUtil.getAddress(data), function_pointer);
        return data.get(0);
    }
    
    public static void glGetDouble(final int target, final int index, final DoubleBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetDoublei_v;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        nglGetDoublei_v(target, index, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglGetDoublei_v(final int p0, final int p1, final long p2, final long p3);
    
    public static double glGetDouble(final int target, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetDoublei_v;
        BufferChecks.checkFunctionAddress(function_pointer);
        final DoubleBuffer data = APIUtil.getBufferDouble(caps);
        nglGetDoublei_v(target, index, MemoryUtil.getAddress(data), function_pointer);
        return data.get(0);
    }
}

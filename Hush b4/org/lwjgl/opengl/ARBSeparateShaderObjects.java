// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ByteBuffer;

public final class ARBSeparateShaderObjects
{
    public static final int GL_VERTEX_SHADER_BIT = 1;
    public static final int GL_FRAGMENT_SHADER_BIT = 2;
    public static final int GL_GEOMETRY_SHADER_BIT = 4;
    public static final int GL_TESS_CONTROL_SHADER_BIT = 8;
    public static final int GL_TESS_EVALUATION_SHADER_BIT = 16;
    public static final int GL_ALL_SHADER_BITS = -1;
    public static final int GL_PROGRAM_SEPARABLE = 33368;
    public static final int GL_ACTIVE_PROGRAM = 33369;
    public static final int GL_PROGRAM_PIPELINE_BINDING = 33370;
    
    private ARBSeparateShaderObjects() {
    }
    
    public static void glUseProgramStages(final int pipeline, final int stages, final int program) {
        GL41.glUseProgramStages(pipeline, stages, program);
    }
    
    public static void glActiveShaderProgram(final int pipeline, final int program) {
        GL41.glActiveShaderProgram(pipeline, program);
    }
    
    public static int glCreateShaderProgram(final int type, final ByteBuffer string) {
        return GL41.glCreateShaderProgram(type, string);
    }
    
    public static int glCreateShaderProgram(final int type, final int count, final ByteBuffer strings) {
        return GL41.glCreateShaderProgram(type, count, strings);
    }
    
    public static int glCreateShaderProgram(final int type, final ByteBuffer[] strings) {
        return GL41.glCreateShaderProgram(type, strings);
    }
    
    public static int glCreateShaderProgram(final int type, final CharSequence string) {
        return GL41.glCreateShaderProgram(type, string);
    }
    
    public static int glCreateShaderProgram(final int type, final CharSequence[] strings) {
        return GL41.glCreateShaderProgram(type, strings);
    }
    
    public static void glBindProgramPipeline(final int pipeline) {
        GL41.glBindProgramPipeline(pipeline);
    }
    
    public static void glDeleteProgramPipelines(final IntBuffer pipelines) {
        GL41.glDeleteProgramPipelines(pipelines);
    }
    
    public static void glDeleteProgramPipelines(final int pipeline) {
        GL41.glDeleteProgramPipelines(pipeline);
    }
    
    public static void glGenProgramPipelines(final IntBuffer pipelines) {
        GL41.glGenProgramPipelines(pipelines);
    }
    
    public static int glGenProgramPipelines() {
        return GL41.glGenProgramPipelines();
    }
    
    public static boolean glIsProgramPipeline(final int pipeline) {
        return GL41.glIsProgramPipeline(pipeline);
    }
    
    public static void glProgramParameteri(final int program, final int pname, final int value) {
        GL41.glProgramParameteri(program, pname, value);
    }
    
    public static void glGetProgramPipeline(final int pipeline, final int pname, final IntBuffer params) {
        GL41.glGetProgramPipeline(pipeline, pname, params);
    }
    
    public static int glGetProgramPipelinei(final int pipeline, final int pname) {
        return GL41.glGetProgramPipelinei(pipeline, pname);
    }
    
    public static void glProgramUniform1i(final int program, final int location, final int v0) {
        GL41.glProgramUniform1i(program, location, v0);
    }
    
    public static void glProgramUniform2i(final int program, final int location, final int v0, final int v1) {
        GL41.glProgramUniform2i(program, location, v0, v1);
    }
    
    public static void glProgramUniform3i(final int program, final int location, final int v0, final int v1, final int v2) {
        GL41.glProgramUniform3i(program, location, v0, v1, v2);
    }
    
    public static void glProgramUniform4i(final int program, final int location, final int v0, final int v1, final int v2, final int v3) {
        GL41.glProgramUniform4i(program, location, v0, v1, v2, v3);
    }
    
    public static void glProgramUniform1f(final int program, final int location, final float v0) {
        GL41.glProgramUniform1f(program, location, v0);
    }
    
    public static void glProgramUniform2f(final int program, final int location, final float v0, final float v1) {
        GL41.glProgramUniform2f(program, location, v0, v1);
    }
    
    public static void glProgramUniform3f(final int program, final int location, final float v0, final float v1, final float v2) {
        GL41.glProgramUniform3f(program, location, v0, v1, v2);
    }
    
    public static void glProgramUniform4f(final int program, final int location, final float v0, final float v1, final float v2, final float v3) {
        GL41.glProgramUniform4f(program, location, v0, v1, v2, v3);
    }
    
    public static void glProgramUniform1d(final int program, final int location, final double v0) {
        GL41.glProgramUniform1d(program, location, v0);
    }
    
    public static void glProgramUniform2d(final int program, final int location, final double v0, final double v1) {
        GL41.glProgramUniform2d(program, location, v0, v1);
    }
    
    public static void glProgramUniform3d(final int program, final int location, final double v0, final double v1, final double v2) {
        GL41.glProgramUniform3d(program, location, v0, v1, v2);
    }
    
    public static void glProgramUniform4d(final int program, final int location, final double v0, final double v1, final double v2, final double v3) {
        GL41.glProgramUniform4d(program, location, v0, v1, v2, v3);
    }
    
    public static void glProgramUniform1(final int program, final int location, final IntBuffer value) {
        GL41.glProgramUniform1(program, location, value);
    }
    
    public static void glProgramUniform2(final int program, final int location, final IntBuffer value) {
        GL41.glProgramUniform2(program, location, value);
    }
    
    public static void glProgramUniform3(final int program, final int location, final IntBuffer value) {
        GL41.glProgramUniform3(program, location, value);
    }
    
    public static void glProgramUniform4(final int program, final int location, final IntBuffer value) {
        GL41.glProgramUniform4(program, location, value);
    }
    
    public static void glProgramUniform1(final int program, final int location, final FloatBuffer value) {
        GL41.glProgramUniform1(program, location, value);
    }
    
    public static void glProgramUniform2(final int program, final int location, final FloatBuffer value) {
        GL41.glProgramUniform2(program, location, value);
    }
    
    public static void glProgramUniform3(final int program, final int location, final FloatBuffer value) {
        GL41.glProgramUniform3(program, location, value);
    }
    
    public static void glProgramUniform4(final int program, final int location, final FloatBuffer value) {
        GL41.glProgramUniform4(program, location, value);
    }
    
    public static void glProgramUniform1(final int program, final int location, final DoubleBuffer value) {
        GL41.glProgramUniform1(program, location, value);
    }
    
    public static void glProgramUniform2(final int program, final int location, final DoubleBuffer value) {
        GL41.glProgramUniform2(program, location, value);
    }
    
    public static void glProgramUniform3(final int program, final int location, final DoubleBuffer value) {
        GL41.glProgramUniform3(program, location, value);
    }
    
    public static void glProgramUniform4(final int program, final int location, final DoubleBuffer value) {
        GL41.glProgramUniform4(program, location, value);
    }
    
    public static void glProgramUniform1ui(final int program, final int location, final int v0) {
        GL41.glProgramUniform1ui(program, location, v0);
    }
    
    public static void glProgramUniform2ui(final int program, final int location, final int v0, final int v1) {
        GL41.glProgramUniform2ui(program, location, v0, v1);
    }
    
    public static void glProgramUniform3ui(final int program, final int location, final int v0, final int v1, final int v2) {
        GL41.glProgramUniform3ui(program, location, v0, v1, v2);
    }
    
    public static void glProgramUniform4ui(final int program, final int location, final int v0, final int v1, final int v2, final int v3) {
        GL41.glProgramUniform4ui(program, location, v0, v1, v2, v3);
    }
    
    public static void glProgramUniform1u(final int program, final int location, final IntBuffer value) {
        GL41.glProgramUniform1u(program, location, value);
    }
    
    public static void glProgramUniform2u(final int program, final int location, final IntBuffer value) {
        GL41.glProgramUniform2u(program, location, value);
    }
    
    public static void glProgramUniform3u(final int program, final int location, final IntBuffer value) {
        GL41.glProgramUniform3u(program, location, value);
    }
    
    public static void glProgramUniform4u(final int program, final int location, final IntBuffer value) {
        GL41.glProgramUniform4u(program, location, value);
    }
    
    public static void glProgramUniformMatrix2(final int program, final int location, final boolean transpose, final FloatBuffer value) {
        GL41.glProgramUniformMatrix2(program, location, transpose, value);
    }
    
    public static void glProgramUniformMatrix3(final int program, final int location, final boolean transpose, final FloatBuffer value) {
        GL41.glProgramUniformMatrix3(program, location, transpose, value);
    }
    
    public static void glProgramUniformMatrix4(final int program, final int location, final boolean transpose, final FloatBuffer value) {
        GL41.glProgramUniformMatrix4(program, location, transpose, value);
    }
    
    public static void glProgramUniformMatrix2(final int program, final int location, final boolean transpose, final DoubleBuffer value) {
        GL41.glProgramUniformMatrix2(program, location, transpose, value);
    }
    
    public static void glProgramUniformMatrix3(final int program, final int location, final boolean transpose, final DoubleBuffer value) {
        GL41.glProgramUniformMatrix3(program, location, transpose, value);
    }
    
    public static void glProgramUniformMatrix4(final int program, final int location, final boolean transpose, final DoubleBuffer value) {
        GL41.glProgramUniformMatrix4(program, location, transpose, value);
    }
    
    public static void glProgramUniformMatrix2x3(final int program, final int location, final boolean transpose, final FloatBuffer value) {
        GL41.glProgramUniformMatrix2x3(program, location, transpose, value);
    }
    
    public static void glProgramUniformMatrix3x2(final int program, final int location, final boolean transpose, final FloatBuffer value) {
        GL41.glProgramUniformMatrix3x2(program, location, transpose, value);
    }
    
    public static void glProgramUniformMatrix2x4(final int program, final int location, final boolean transpose, final FloatBuffer value) {
        GL41.glProgramUniformMatrix2x4(program, location, transpose, value);
    }
    
    public static void glProgramUniformMatrix4x2(final int program, final int location, final boolean transpose, final FloatBuffer value) {
        GL41.glProgramUniformMatrix4x2(program, location, transpose, value);
    }
    
    public static void glProgramUniformMatrix3x4(final int program, final int location, final boolean transpose, final FloatBuffer value) {
        GL41.glProgramUniformMatrix3x4(program, location, transpose, value);
    }
    
    public static void glProgramUniformMatrix4x3(final int program, final int location, final boolean transpose, final FloatBuffer value) {
        GL41.glProgramUniformMatrix4x3(program, location, transpose, value);
    }
    
    public static void glProgramUniformMatrix2x3(final int program, final int location, final boolean transpose, final DoubleBuffer value) {
        GL41.glProgramUniformMatrix2x3(program, location, transpose, value);
    }
    
    public static void glProgramUniformMatrix3x2(final int program, final int location, final boolean transpose, final DoubleBuffer value) {
        GL41.glProgramUniformMatrix3x2(program, location, transpose, value);
    }
    
    public static void glProgramUniformMatrix2x4(final int program, final int location, final boolean transpose, final DoubleBuffer value) {
        GL41.glProgramUniformMatrix2x4(program, location, transpose, value);
    }
    
    public static void glProgramUniformMatrix4x2(final int program, final int location, final boolean transpose, final DoubleBuffer value) {
        GL41.glProgramUniformMatrix4x2(program, location, transpose, value);
    }
    
    public static void glProgramUniformMatrix3x4(final int program, final int location, final boolean transpose, final DoubleBuffer value) {
        GL41.glProgramUniformMatrix3x4(program, location, transpose, value);
    }
    
    public static void glProgramUniformMatrix4x3(final int program, final int location, final boolean transpose, final DoubleBuffer value) {
        GL41.glProgramUniformMatrix4x3(program, location, transpose, value);
    }
    
    public static void glValidateProgramPipeline(final int pipeline) {
        GL41.glValidateProgramPipeline(pipeline);
    }
    
    public static void glGetProgramPipelineInfoLog(final int pipeline, final IntBuffer length, final ByteBuffer infoLog) {
        GL41.glGetProgramPipelineInfoLog(pipeline, length, infoLog);
    }
    
    public static String glGetProgramPipelineInfoLog(final int pipeline, final int bufSize) {
        return GL41.glGetProgramPipelineInfoLog(pipeline, bufSize);
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.IntBuffer;
import java.nio.ByteBuffer;

public final class ARBShaderSubroutine
{
    public static final int GL_ACTIVE_SUBROUTINES = 36325;
    public static final int GL_ACTIVE_SUBROUTINE_UNIFORMS = 36326;
    public static final int GL_ACTIVE_SUBROUTINE_UNIFORM_LOCATIONS = 36423;
    public static final int GL_ACTIVE_SUBROUTINE_MAX_LENGTH = 36424;
    public static final int GL_ACTIVE_SUBROUTINE_UNIFORM_MAX_LENGTH = 36425;
    public static final int GL_MAX_SUBROUTINES = 36327;
    public static final int GL_MAX_SUBROUTINE_UNIFORM_LOCATIONS = 36328;
    public static final int GL_NUM_COMPATIBLE_SUBROUTINES = 36426;
    public static final int GL_COMPATIBLE_SUBROUTINES = 36427;
    
    private ARBShaderSubroutine() {
    }
    
    public static int glGetSubroutineUniformLocation(final int program, final int shadertype, final ByteBuffer name) {
        return GL40.glGetSubroutineUniformLocation(program, shadertype, name);
    }
    
    public static int glGetSubroutineUniformLocation(final int program, final int shadertype, final CharSequence name) {
        return GL40.glGetSubroutineUniformLocation(program, shadertype, name);
    }
    
    public static int glGetSubroutineIndex(final int program, final int shadertype, final ByteBuffer name) {
        return GL40.glGetSubroutineIndex(program, shadertype, name);
    }
    
    public static int glGetSubroutineIndex(final int program, final int shadertype, final CharSequence name) {
        return GL40.glGetSubroutineIndex(program, shadertype, name);
    }
    
    public static void glGetActiveSubroutineUniform(final int program, final int shadertype, final int index, final int pname, final IntBuffer values) {
        GL40.glGetActiveSubroutineUniform(program, shadertype, index, pname, values);
    }
    
    public static int glGetActiveSubroutineUniformi(final int program, final int shadertype, final int index, final int pname) {
        return GL40.glGetActiveSubroutineUniformi(program, shadertype, index, pname);
    }
    
    public static void glGetActiveSubroutineUniformName(final int program, final int shadertype, final int index, final IntBuffer length, final ByteBuffer name) {
        GL40.glGetActiveSubroutineUniformName(program, shadertype, index, length, name);
    }
    
    public static String glGetActiveSubroutineUniformName(final int program, final int shadertype, final int index, final int bufsize) {
        return GL40.glGetActiveSubroutineUniformName(program, shadertype, index, bufsize);
    }
    
    public static void glGetActiveSubroutineName(final int program, final int shadertype, final int index, final IntBuffer length, final ByteBuffer name) {
        GL40.glGetActiveSubroutineName(program, shadertype, index, length, name);
    }
    
    public static String glGetActiveSubroutineName(final int program, final int shadertype, final int index, final int bufsize) {
        return GL40.glGetActiveSubroutineName(program, shadertype, index, bufsize);
    }
    
    public static void glUniformSubroutinesu(final int shadertype, final IntBuffer indices) {
        GL40.glUniformSubroutinesu(shadertype, indices);
    }
    
    public static void glGetUniformSubroutineu(final int shadertype, final int location, final IntBuffer params) {
        GL40.glGetUniformSubroutineu(shadertype, location, params);
    }
    
    public static int glGetUniformSubroutineui(final int shadertype, final int location) {
        return GL40.glGetUniformSubroutineui(shadertype, location);
    }
    
    public static void glGetProgramStage(final int program, final int shadertype, final int pname, final IntBuffer values) {
        GL40.glGetProgramStage(program, shadertype, pname, values);
    }
    
    public static int glGetProgramStagei(final int program, final int shadertype, final int pname) {
        return GL40.glGetProgramStagei(program, shadertype, pname);
    }
}

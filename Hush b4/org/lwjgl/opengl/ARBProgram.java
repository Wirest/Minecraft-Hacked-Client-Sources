// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.ByteBuffer;

public class ARBProgram
{
    public static final int GL_PROGRAM_FORMAT_ASCII_ARB = 34933;
    public static final int GL_PROGRAM_LENGTH_ARB = 34343;
    public static final int GL_PROGRAM_FORMAT_ARB = 34934;
    public static final int GL_PROGRAM_BINDING_ARB = 34423;
    public static final int GL_PROGRAM_INSTRUCTIONS_ARB = 34976;
    public static final int GL_MAX_PROGRAM_INSTRUCTIONS_ARB = 34977;
    public static final int GL_PROGRAM_NATIVE_INSTRUCTIONS_ARB = 34978;
    public static final int GL_MAX_PROGRAM_NATIVE_INSTRUCTIONS_ARB = 34979;
    public static final int GL_PROGRAM_TEMPORARIES_ARB = 34980;
    public static final int GL_MAX_PROGRAM_TEMPORARIES_ARB = 34981;
    public static final int GL_PROGRAM_NATIVE_TEMPORARIES_ARB = 34982;
    public static final int GL_MAX_PROGRAM_NATIVE_TEMPORARIES_ARB = 34983;
    public static final int GL_PROGRAM_PARAMETERS_ARB = 34984;
    public static final int GL_MAX_PROGRAM_PARAMETERS_ARB = 34985;
    public static final int GL_PROGRAM_NATIVE_PARAMETERS_ARB = 34986;
    public static final int GL_MAX_PROGRAM_NATIVE_PARAMETERS_ARB = 34987;
    public static final int GL_PROGRAM_ATTRIBS_ARB = 34988;
    public static final int GL_MAX_PROGRAM_ATTRIBS_ARB = 34989;
    public static final int GL_PROGRAM_NATIVE_ATTRIBS_ARB = 34990;
    public static final int GL_MAX_PROGRAM_NATIVE_ATTRIBS_ARB = 34991;
    public static final int GL_MAX_PROGRAM_LOCAL_PARAMETERS_ARB = 34996;
    public static final int GL_MAX_PROGRAM_ENV_PARAMETERS_ARB = 34997;
    public static final int GL_PROGRAM_UNDER_NATIVE_LIMITS_ARB = 34998;
    public static final int GL_PROGRAM_STRING_ARB = 34344;
    public static final int GL_PROGRAM_ERROR_POSITION_ARB = 34379;
    public static final int GL_CURRENT_MATRIX_ARB = 34369;
    public static final int GL_TRANSPOSE_CURRENT_MATRIX_ARB = 34999;
    public static final int GL_CURRENT_MATRIX_STACK_DEPTH_ARB = 34368;
    public static final int GL_MAX_PROGRAM_MATRICES_ARB = 34351;
    public static final int GL_MAX_PROGRAM_MATRIX_STACK_DEPTH_ARB = 34350;
    public static final int GL_PROGRAM_ERROR_STRING_ARB = 34932;
    public static final int GL_MATRIX0_ARB = 35008;
    public static final int GL_MATRIX1_ARB = 35009;
    public static final int GL_MATRIX2_ARB = 35010;
    public static final int GL_MATRIX3_ARB = 35011;
    public static final int GL_MATRIX4_ARB = 35012;
    public static final int GL_MATRIX5_ARB = 35013;
    public static final int GL_MATRIX6_ARB = 35014;
    public static final int GL_MATRIX7_ARB = 35015;
    public static final int GL_MATRIX8_ARB = 35016;
    public static final int GL_MATRIX9_ARB = 35017;
    public static final int GL_MATRIX10_ARB = 35018;
    public static final int GL_MATRIX11_ARB = 35019;
    public static final int GL_MATRIX12_ARB = 35020;
    public static final int GL_MATRIX13_ARB = 35021;
    public static final int GL_MATRIX14_ARB = 35022;
    public static final int GL_MATRIX15_ARB = 35023;
    public static final int GL_MATRIX16_ARB = 35024;
    public static final int GL_MATRIX17_ARB = 35025;
    public static final int GL_MATRIX18_ARB = 35026;
    public static final int GL_MATRIX19_ARB = 35027;
    public static final int GL_MATRIX20_ARB = 35028;
    public static final int GL_MATRIX21_ARB = 35029;
    public static final int GL_MATRIX22_ARB = 35030;
    public static final int GL_MATRIX23_ARB = 35031;
    public static final int GL_MATRIX24_ARB = 35032;
    public static final int GL_MATRIX25_ARB = 35033;
    public static final int GL_MATRIX26_ARB = 35034;
    public static final int GL_MATRIX27_ARB = 35035;
    public static final int GL_MATRIX28_ARB = 35036;
    public static final int GL_MATRIX29_ARB = 35037;
    public static final int GL_MATRIX30_ARB = 35038;
    public static final int GL_MATRIX31_ARB = 35039;
    
    public static void glProgramStringARB(final int target, final int format, final ByteBuffer string) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramStringARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(string);
        nglProgramStringARB(target, format, string.remaining(), MemoryUtil.getAddress(string), function_pointer);
    }
    
    static native void nglProgramStringARB(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glProgramStringARB(final int target, final int format, final CharSequence string) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramStringARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramStringARB(target, format, string.length(), APIUtil.getBuffer(caps, string), function_pointer);
    }
    
    public static void glBindProgramARB(final int target, final int program) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindProgramARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBindProgramARB(target, program, function_pointer);
    }
    
    static native void nglBindProgramARB(final int p0, final int p1, final long p2);
    
    public static void glDeleteProgramsARB(final IntBuffer programs) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteProgramsARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(programs);
        nglDeleteProgramsARB(programs.remaining(), MemoryUtil.getAddress(programs), function_pointer);
    }
    
    static native void nglDeleteProgramsARB(final int p0, final long p1, final long p2);
    
    public static void glDeleteProgramsARB(final int program) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteProgramsARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDeleteProgramsARB(1, APIUtil.getInt(caps, program), function_pointer);
    }
    
    public static void glGenProgramsARB(final IntBuffer programs) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenProgramsARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(programs);
        nglGenProgramsARB(programs.remaining(), MemoryUtil.getAddress(programs), function_pointer);
    }
    
    static native void nglGenProgramsARB(final int p0, final long p1, final long p2);
    
    public static int glGenProgramsARB() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenProgramsARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer programs = APIUtil.getBufferInt(caps);
        nglGenProgramsARB(1, MemoryUtil.getAddress(programs), function_pointer);
        return programs.get(0);
    }
    
    public static void glProgramEnvParameter4fARB(final int target, final int index, final float x, final float y, final float z, final float w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramEnvParameter4fARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramEnvParameter4fARB(target, index, x, y, z, w, function_pointer);
    }
    
    static native void nglProgramEnvParameter4fARB(final int p0, final int p1, final float p2, final float p3, final float p4, final float p5, final long p6);
    
    public static void glProgramEnvParameter4dARB(final int target, final int index, final double x, final double y, final double z, final double w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramEnvParameter4dARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramEnvParameter4dARB(target, index, x, y, z, w, function_pointer);
    }
    
    static native void nglProgramEnvParameter4dARB(final int p0, final int p1, final double p2, final double p3, final double p4, final double p5, final long p6);
    
    public static void glProgramEnvParameter4ARB(final int target, final int index, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramEnvParameter4fvARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglProgramEnvParameter4fvARB(target, index, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglProgramEnvParameter4fvARB(final int p0, final int p1, final long p2, final long p3);
    
    public static void glProgramEnvParameter4ARB(final int target, final int index, final DoubleBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramEnvParameter4dvARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglProgramEnvParameter4dvARB(target, index, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglProgramEnvParameter4dvARB(final int p0, final int p1, final long p2, final long p3);
    
    public static void glProgramLocalParameter4fARB(final int target, final int index, final float x, final float y, final float z, final float w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramLocalParameter4fARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramLocalParameter4fARB(target, index, x, y, z, w, function_pointer);
    }
    
    static native void nglProgramLocalParameter4fARB(final int p0, final int p1, final float p2, final float p3, final float p4, final float p5, final long p6);
    
    public static void glProgramLocalParameter4dARB(final int target, final int index, final double x, final double y, final double z, final double w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramLocalParameter4dARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglProgramLocalParameter4dARB(target, index, x, y, z, w, function_pointer);
    }
    
    static native void nglProgramLocalParameter4dARB(final int p0, final int p1, final double p2, final double p3, final double p4, final double p5, final long p6);
    
    public static void glProgramLocalParameter4ARB(final int target, final int index, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramLocalParameter4fvARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglProgramLocalParameter4fvARB(target, index, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglProgramLocalParameter4fvARB(final int p0, final int p1, final long p2, final long p3);
    
    public static void glProgramLocalParameter4ARB(final int target, final int index, final DoubleBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glProgramLocalParameter4dvARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglProgramLocalParameter4dvARB(target, index, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglProgramLocalParameter4dvARB(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetProgramEnvParameterARB(final int target, final int index, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramEnvParameterfvARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetProgramEnvParameterfvARB(target, index, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetProgramEnvParameterfvARB(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetProgramEnvParameterARB(final int target, final int index, final DoubleBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramEnvParameterdvARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetProgramEnvParameterdvARB(target, index, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetProgramEnvParameterdvARB(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetProgramLocalParameterARB(final int target, final int index, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramLocalParameterfvARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetProgramLocalParameterfvARB(target, index, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetProgramLocalParameterfvARB(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetProgramLocalParameterARB(final int target, final int index, final DoubleBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramLocalParameterdvARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetProgramLocalParameterdvARB(target, index, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetProgramLocalParameterdvARB(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetProgramARB(final int target, final int parameterName, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetProgramivARB(target, parameterName, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetProgramivARB(final int p0, final int p1, final long p2, final long p3);
    
    @Deprecated
    public static int glGetProgramARB(final int target, final int parameterName) {
        return glGetProgramiARB(target, parameterName);
    }
    
    public static int glGetProgramiARB(final int target, final int parameterName) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetProgramivARB(target, parameterName, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetProgramStringARB(final int target, final int parameterName, final ByteBuffer paramString) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramStringARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(paramString);
        nglGetProgramStringARB(target, parameterName, MemoryUtil.getAddress(paramString), function_pointer);
    }
    
    static native void nglGetProgramStringARB(final int p0, final int p1, final long p2, final long p3);
    
    public static String glGetProgramStringARB(final int target, final int parameterName) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramStringARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int programLength = glGetProgramiARB(target, 34343);
        final ByteBuffer paramString = APIUtil.getBufferByte(caps, programLength);
        nglGetProgramStringARB(target, parameterName, MemoryUtil.getAddress(paramString), function_pointer);
        paramString.limit(programLength);
        return APIUtil.getString(caps, paramString);
    }
    
    public static boolean glIsProgramARB(final int program) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsProgramARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsProgramARB(program, function_pointer);
        return __result;
    }
    
    static native boolean nglIsProgramARB(final int p0, final long p1);
}

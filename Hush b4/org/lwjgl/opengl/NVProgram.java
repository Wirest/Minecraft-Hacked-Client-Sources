// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.ByteBuffer;

public class NVProgram
{
    public static final int GL_PROGRAM_TARGET_NV = 34374;
    public static final int GL_PROGRAM_LENGTH_NV = 34343;
    public static final int GL_PROGRAM_RESIDENT_NV = 34375;
    public static final int GL_PROGRAM_STRING_NV = 34344;
    public static final int GL_PROGRAM_ERROR_POSITION_NV = 34379;
    public static final int GL_PROGRAM_ERROR_STRING_NV = 34932;
    
    public static void glLoadProgramNV(final int target, final int programID, final ByteBuffer string) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glLoadProgramNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(string);
        nglLoadProgramNV(target, programID, string.remaining(), MemoryUtil.getAddress(string), function_pointer);
    }
    
    static native void nglLoadProgramNV(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glLoadProgramNV(final int target, final int programID, final CharSequence string) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glLoadProgramNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglLoadProgramNV(target, programID, string.length(), APIUtil.getBuffer(caps, string), function_pointer);
    }
    
    public static void glBindProgramNV(final int target, final int programID) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindProgramNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBindProgramNV(target, programID, function_pointer);
    }
    
    static native void nglBindProgramNV(final int p0, final int p1, final long p2);
    
    public static void glDeleteProgramsNV(final IntBuffer programs) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteProgramsNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(programs);
        nglDeleteProgramsNV(programs.remaining(), MemoryUtil.getAddress(programs), function_pointer);
    }
    
    static native void nglDeleteProgramsNV(final int p0, final long p1, final long p2);
    
    public static void glDeleteProgramsNV(final int program) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteProgramsNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDeleteProgramsNV(1, APIUtil.getInt(caps, program), function_pointer);
    }
    
    public static void glGenProgramsNV(final IntBuffer programs) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenProgramsNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(programs);
        nglGenProgramsNV(programs.remaining(), MemoryUtil.getAddress(programs), function_pointer);
    }
    
    static native void nglGenProgramsNV(final int p0, final long p1, final long p2);
    
    public static int glGenProgramsNV() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenProgramsNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer programs = APIUtil.getBufferInt(caps);
        nglGenProgramsNV(1, MemoryUtil.getAddress(programs), function_pointer);
        return programs.get(0);
    }
    
    public static void glGetProgramNV(final int programID, final int parameterName, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglGetProgramivNV(programID, parameterName, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetProgramivNV(final int p0, final int p1, final long p2, final long p3);
    
    @Deprecated
    public static int glGetProgramNV(final int programID, final int parameterName) {
        return glGetProgramiNV(programID, parameterName);
    }
    
    public static int glGetProgramiNV(final int programID, final int parameterName) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetProgramivNV(programID, parameterName, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetProgramStringNV(final int programID, final int parameterName, final ByteBuffer paramString) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramStringNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(paramString);
        nglGetProgramStringNV(programID, parameterName, MemoryUtil.getAddress(paramString), function_pointer);
    }
    
    static native void nglGetProgramStringNV(final int p0, final int p1, final long p2, final long p3);
    
    public static String glGetProgramStringNV(final int programID, final int parameterName) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramStringNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int programLength = glGetProgramiNV(programID, 34343);
        final ByteBuffer paramString = APIUtil.getBufferByte(caps, programLength);
        nglGetProgramStringNV(programID, parameterName, MemoryUtil.getAddress(paramString), function_pointer);
        paramString.limit(programLength);
        return APIUtil.getString(caps, paramString);
    }
    
    public static boolean glIsProgramNV(final int programID) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsProgramNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsProgramNV(programID, function_pointer);
        return __result;
    }
    
    static native boolean nglIsProgramNV(final int p0, final long p1);
    
    public static boolean glAreProgramsResidentNV(final IntBuffer programIDs, final ByteBuffer programResidences) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glAreProgramsResidentNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(programIDs);
        BufferChecks.checkBuffer(programResidences, programIDs.remaining());
        final boolean __result = nglAreProgramsResidentNV(programIDs.remaining(), MemoryUtil.getAddress(programIDs), MemoryUtil.getAddress(programResidences), function_pointer);
        return __result;
    }
    
    static native boolean nglAreProgramsResidentNV(final int p0, final long p1, final long p2, final long p3);
    
    public static void glRequestResidentProgramsNV(final IntBuffer programIDs) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glRequestResidentProgramsNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(programIDs);
        nglRequestResidentProgramsNV(programIDs.remaining(), MemoryUtil.getAddress(programIDs), function_pointer);
    }
    
    static native void nglRequestResidentProgramsNV(final int p0, final long p1, final long p2);
    
    public static void glRequestResidentProgramsNV(final int programID) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glRequestResidentProgramsNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglRequestResidentProgramsNV(1, APIUtil.getInt(caps, programID), function_pointer);
    }
}

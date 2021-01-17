// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import java.nio.ByteBuffer;
import org.lwjgl.BufferChecks;

public final class EXTSeparateShaderObjects
{
    public static final int GL_ACTIVE_PROGRAM_EXT = 35725;
    
    private EXTSeparateShaderObjects() {
    }
    
    public static void glUseShaderProgramEXT(final int type, final int program) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUseShaderProgramEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUseShaderProgramEXT(type, program, function_pointer);
    }
    
    static native void nglUseShaderProgramEXT(final int p0, final int p1, final long p2);
    
    public static void glActiveProgramEXT(final int program) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glActiveProgramEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglActiveProgramEXT(program, function_pointer);
    }
    
    static native void nglActiveProgramEXT(final int p0, final long p1);
    
    public static int glCreateShaderProgramEXT(final int type, final ByteBuffer string) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCreateShaderProgramEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(string);
        BufferChecks.checkNullTerminated(string);
        final int __result = nglCreateShaderProgramEXT(type, MemoryUtil.getAddress(string), function_pointer);
        return __result;
    }
    
    static native int nglCreateShaderProgramEXT(final int p0, final long p1, final long p2);
    
    public static int glCreateShaderProgramEXT(final int type, final CharSequence string) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCreateShaderProgramEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nglCreateShaderProgramEXT(type, APIUtil.getBufferNT(caps, string), function_pointer);
        return __result;
    }
}

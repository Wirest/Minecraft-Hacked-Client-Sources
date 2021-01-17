// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class EXTBindableUniform
{
    public static final int GL_MAX_VERTEX_BINDABLE_UNIFORMS_EXT = 36322;
    public static final int GL_MAX_FRAGMENT_BINDABLE_UNIFORMS_EXT = 36323;
    public static final int GL_MAX_GEOMETRY_BINDABLE_UNIFORMS_EXT = 36324;
    public static final int GL_MAX_BINDABLE_UNIFORM_SIZE_EXT = 36333;
    public static final int GL_UNIFORM_BUFFER_BINDING_EXT = 36335;
    public static final int GL_UNIFORM_BUFFER_EXT = 36334;
    
    private EXTBindableUniform() {
    }
    
    public static void glUniformBufferEXT(final int program, final int location, final int buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniformBufferEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniformBufferEXT(program, location, buffer, function_pointer);
    }
    
    static native void nglUniformBufferEXT(final int p0, final int p1, final int p2, final long p3);
    
    public static int glGetUniformBufferSizeEXT(final int program, final int location) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetUniformBufferSizeEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nglGetUniformBufferSizeEXT(program, location, function_pointer);
        return __result;
    }
    
    static native int nglGetUniformBufferSizeEXT(final int p0, final int p1, final long p2);
    
    public static long glGetUniformOffsetEXT(final int program, final int location) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetUniformOffsetEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final long __result = nglGetUniformOffsetEXT(program, location, function_pointer);
        return __result;
    }
    
    static native long nglGetUniformOffsetEXT(final int p0, final int p1, final long p2);
}

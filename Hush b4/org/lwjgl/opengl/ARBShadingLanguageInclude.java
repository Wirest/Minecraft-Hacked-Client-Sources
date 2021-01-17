// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.Buffer;
import java.nio.IntBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.ByteBuffer;

public final class ARBShadingLanguageInclude
{
    public static final int GL_SHADER_INCLUDE_ARB = 36270;
    public static final int GL_NAMED_STRING_LENGTH_ARB = 36329;
    public static final int GL_NAMED_STRING_TYPE_ARB = 36330;
    
    private ARBShadingLanguageInclude() {
    }
    
    public static void glNamedStringARB(final int type, final ByteBuffer name, final ByteBuffer string) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedStringARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(name);
        BufferChecks.checkDirect(string);
        nglNamedStringARB(type, name.remaining(), MemoryUtil.getAddress(name), string.remaining(), MemoryUtil.getAddress(string), function_pointer);
    }
    
    static native void nglNamedStringARB(final int p0, final int p1, final long p2, final int p3, final long p4, final long p5);
    
    public static void glNamedStringARB(final int type, final CharSequence name, final CharSequence string) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNamedStringARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNamedStringARB(type, name.length(), APIUtil.getBuffer(caps, name), string.length(), APIUtil.getBuffer(caps, string, name.length()), function_pointer);
    }
    
    public static void glDeleteNamedStringARB(final ByteBuffer name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteNamedStringARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(name);
        nglDeleteNamedStringARB(name.remaining(), MemoryUtil.getAddress(name), function_pointer);
    }
    
    static native void nglDeleteNamedStringARB(final int p0, final long p1, final long p2);
    
    public static void glDeleteNamedStringARB(final CharSequence name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteNamedStringARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDeleteNamedStringARB(name.length(), APIUtil.getBuffer(caps, name), function_pointer);
    }
    
    public static void glCompileShaderIncludeARB(final int shader, final int count, final ByteBuffer path) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompileShaderIncludeARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(path);
        BufferChecks.checkNullTerminated(path, count);
        nglCompileShaderIncludeARB(shader, count, MemoryUtil.getAddress(path), 0L, function_pointer);
    }
    
    static native void nglCompileShaderIncludeARB(final int p0, final int p1, final long p2, final long p3, final long p4);
    
    public static void glCompileShaderIncludeARB(final int shader, final CharSequence[] path) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompileShaderIncludeARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkArray(path);
        nglCompileShaderIncludeARB2(shader, path.length, APIUtil.getBuffer(caps, path), APIUtil.getLengths(caps, path), function_pointer);
    }
    
    static native void nglCompileShaderIncludeARB2(final int p0, final int p1, final long p2, final long p3, final long p4);
    
    public static boolean glIsNamedStringARB(final ByteBuffer name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsNamedStringARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(name);
        final boolean __result = nglIsNamedStringARB(name.remaining(), MemoryUtil.getAddress(name), function_pointer);
        return __result;
    }
    
    static native boolean nglIsNamedStringARB(final int p0, final long p1, final long p2);
    
    public static boolean glIsNamedStringARB(final CharSequence name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsNamedStringARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsNamedStringARB(name.length(), APIUtil.getBuffer(caps, name), function_pointer);
        return __result;
    }
    
    public static void glGetNamedStringARB(final ByteBuffer name, final IntBuffer stringlen, final ByteBuffer string) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedStringARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(name);
        if (stringlen != null) {
            BufferChecks.checkBuffer(stringlen, 1);
        }
        BufferChecks.checkDirect(string);
        nglGetNamedStringARB(name.remaining(), MemoryUtil.getAddress(name), string.remaining(), MemoryUtil.getAddressSafe(stringlen), MemoryUtil.getAddress(string), function_pointer);
    }
    
    static native void nglGetNamedStringARB(final int p0, final long p1, final int p2, final long p3, final long p4, final long p5);
    
    public static void glGetNamedStringARB(final CharSequence name, final IntBuffer stringlen, final ByteBuffer string) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedStringARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (stringlen != null) {
            BufferChecks.checkBuffer(stringlen, 1);
        }
        BufferChecks.checkDirect(string);
        nglGetNamedStringARB(name.length(), APIUtil.getBuffer(caps, name), string.remaining(), MemoryUtil.getAddressSafe(stringlen), MemoryUtil.getAddress(string), function_pointer);
    }
    
    public static String glGetNamedStringARB(final CharSequence name, final int bufSize) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedStringARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer string_length = APIUtil.getLengths(caps);
        final ByteBuffer string = APIUtil.getBufferByte(caps, bufSize + name.length());
        nglGetNamedStringARB(name.length(), APIUtil.getBuffer(caps, name), bufSize, MemoryUtil.getAddress0(string_length), MemoryUtil.getAddress(string), function_pointer);
        string.limit(name.length() + string_length.get(0));
        return APIUtil.getString(caps, string);
    }
    
    public static void glGetNamedStringARB(final ByteBuffer name, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedStringivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(name);
        BufferChecks.checkBuffer(params, 1);
        nglGetNamedStringivARB(name.remaining(), MemoryUtil.getAddress(name), pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetNamedStringivARB(final int p0, final long p1, final int p2, final long p3, final long p4);
    
    public static void glGetNamedStringiARB(final CharSequence name, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedStringivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetNamedStringivARB(name.length(), APIUtil.getBuffer(caps, name), pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    public static int glGetNamedStringiARB(final CharSequence name, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetNamedStringivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetNamedStringivARB(name.length(), APIUtil.getBuffer(caps, name), pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
}

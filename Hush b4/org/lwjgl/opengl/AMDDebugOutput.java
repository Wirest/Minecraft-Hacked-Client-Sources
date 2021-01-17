// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.IntBuffer;

public final class AMDDebugOutput
{
    public static final int GL_MAX_DEBUG_MESSAGE_LENGTH_AMD = 37187;
    public static final int GL_MAX_DEBUG_LOGGED_MESSAGES_AMD = 37188;
    public static final int GL_DEBUG_LOGGED_MESSAGES_AMD = 37189;
    public static final int GL_DEBUG_SEVERITY_HIGH_AMD = 37190;
    public static final int GL_DEBUG_SEVERITY_MEDIUM_AMD = 37191;
    public static final int GL_DEBUG_SEVERITY_LOW_AMD = 37192;
    public static final int GL_DEBUG_CATEGORY_API_ERROR_AMD = 37193;
    public static final int GL_DEBUG_CATEGORY_WINDOW_SYSTEM_AMD = 37194;
    public static final int GL_DEBUG_CATEGORY_DEPRECATION_AMD = 37195;
    public static final int GL_DEBUG_CATEGORY_UNDEFINED_BEHAVIOR_AMD = 37196;
    public static final int GL_DEBUG_CATEGORY_PERFORMANCE_AMD = 37197;
    public static final int GL_DEBUG_CATEGORY_SHADER_COMPILER_AMD = 37198;
    public static final int GL_DEBUG_CATEGORY_APPLICATION_AMD = 37199;
    public static final int GL_DEBUG_CATEGORY_OTHER_AMD = 37200;
    
    private AMDDebugOutput() {
    }
    
    public static void glDebugMessageEnableAMD(final int category, final int severity, final IntBuffer ids, final boolean enabled) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDebugMessageEnableAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (ids != null) {
            BufferChecks.checkDirect(ids);
        }
        nglDebugMessageEnableAMD(category, severity, (ids == null) ? 0 : ids.remaining(), MemoryUtil.getAddressSafe(ids), enabled, function_pointer);
    }
    
    static native void nglDebugMessageEnableAMD(final int p0, final int p1, final int p2, final long p3, final boolean p4, final long p5);
    
    public static void glDebugMessageInsertAMD(final int category, final int severity, final int id, final ByteBuffer buf) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDebugMessageInsertAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(buf);
        nglDebugMessageInsertAMD(category, severity, id, buf.remaining(), MemoryUtil.getAddress(buf), function_pointer);
    }
    
    static native void nglDebugMessageInsertAMD(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glDebugMessageInsertAMD(final int category, final int severity, final int id, final CharSequence buf) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDebugMessageInsertAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDebugMessageInsertAMD(category, severity, id, buf.length(), APIUtil.getBuffer(caps, buf), function_pointer);
    }
    
    public static void glDebugMessageCallbackAMD(final AMDDebugOutputCallback callback) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDebugMessageCallbackAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        final long userParam = (callback == null) ? 0L : CallbackUtil.createGlobalRef(callback.getHandler());
        CallbackUtil.registerContextCallbackAMD(userParam);
        nglDebugMessageCallbackAMD((callback == null) ? 0L : callback.getPointer(), userParam, function_pointer);
    }
    
    static native void nglDebugMessageCallbackAMD(final long p0, final long p1, final long p2);
    
    public static int glGetDebugMessageLogAMD(final int count, final IntBuffer categories, final IntBuffer severities, final IntBuffer ids, final IntBuffer lengths, final ByteBuffer messageLog) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetDebugMessageLogAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (categories != null) {
            BufferChecks.checkBuffer(categories, count);
        }
        if (severities != null) {
            BufferChecks.checkBuffer(severities, count);
        }
        if (ids != null) {
            BufferChecks.checkBuffer(ids, count);
        }
        if (lengths != null) {
            BufferChecks.checkBuffer(lengths, count);
        }
        if (messageLog != null) {
            BufferChecks.checkDirect(messageLog);
        }
        final int __result = nglGetDebugMessageLogAMD(count, (messageLog == null) ? 0 : messageLog.remaining(), MemoryUtil.getAddressSafe(categories), MemoryUtil.getAddressSafe(severities), MemoryUtil.getAddressSafe(ids), MemoryUtil.getAddressSafe(lengths), MemoryUtil.getAddressSafe(messageLog), function_pointer);
        return __result;
    }
    
    static native int nglGetDebugMessageLogAMD(final int p0, final int p1, final long p2, final long p3, final long p4, final long p5, final long p6, final long p7);
}

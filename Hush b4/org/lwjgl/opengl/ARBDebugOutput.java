// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.IntBuffer;

public final class ARBDebugOutput
{
    public static final int GL_DEBUG_OUTPUT_SYNCHRONOUS_ARB = 33346;
    public static final int GL_MAX_DEBUG_MESSAGE_LENGTH_ARB = 37187;
    public static final int GL_MAX_DEBUG_LOGGED_MESSAGES_ARB = 37188;
    public static final int GL_DEBUG_LOGGED_MESSAGES_ARB = 37189;
    public static final int GL_DEBUG_NEXT_LOGGED_MESSAGE_LENGTH_ARB = 33347;
    public static final int GL_DEBUG_CALLBACK_FUNCTION_ARB = 33348;
    public static final int GL_DEBUG_CALLBACK_USER_PARAM_ARB = 33349;
    public static final int GL_DEBUG_SOURCE_API_ARB = 33350;
    public static final int GL_DEBUG_SOURCE_WINDOW_SYSTEM_ARB = 33351;
    public static final int GL_DEBUG_SOURCE_SHADER_COMPILER_ARB = 33352;
    public static final int GL_DEBUG_SOURCE_THIRD_PARTY_ARB = 33353;
    public static final int GL_DEBUG_SOURCE_APPLICATION_ARB = 33354;
    public static final int GL_DEBUG_SOURCE_OTHER_ARB = 33355;
    public static final int GL_DEBUG_TYPE_ERROR_ARB = 33356;
    public static final int GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR_ARB = 33357;
    public static final int GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR_ARB = 33358;
    public static final int GL_DEBUG_TYPE_PORTABILITY_ARB = 33359;
    public static final int GL_DEBUG_TYPE_PERFORMANCE_ARB = 33360;
    public static final int GL_DEBUG_TYPE_OTHER_ARB = 33361;
    public static final int GL_DEBUG_SEVERITY_HIGH_ARB = 37190;
    public static final int GL_DEBUG_SEVERITY_MEDIUM_ARB = 37191;
    public static final int GL_DEBUG_SEVERITY_LOW_ARB = 37192;
    
    private ARBDebugOutput() {
    }
    
    public static void glDebugMessageControlARB(final int source, final int type, final int severity, final IntBuffer ids, final boolean enabled) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDebugMessageControlARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (ids != null) {
            BufferChecks.checkDirect(ids);
        }
        nglDebugMessageControlARB(source, type, severity, (ids == null) ? 0 : ids.remaining(), MemoryUtil.getAddressSafe(ids), enabled, function_pointer);
    }
    
    static native void nglDebugMessageControlARB(final int p0, final int p1, final int p2, final int p3, final long p4, final boolean p5, final long p6);
    
    public static void glDebugMessageInsertARB(final int source, final int type, final int id, final int severity, final ByteBuffer buf) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDebugMessageInsertARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(buf);
        nglDebugMessageInsertARB(source, type, id, severity, buf.remaining(), MemoryUtil.getAddress(buf), function_pointer);
    }
    
    static native void nglDebugMessageInsertARB(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5, final long p6);
    
    public static void glDebugMessageInsertARB(final int source, final int type, final int id, final int severity, final CharSequence buf) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDebugMessageInsertARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDebugMessageInsertARB(source, type, id, severity, buf.length(), APIUtil.getBuffer(caps, buf), function_pointer);
    }
    
    public static void glDebugMessageCallbackARB(final ARBDebugOutputCallback callback) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDebugMessageCallbackARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final long userParam = (callback == null) ? 0L : CallbackUtil.createGlobalRef(callback.getHandler());
        CallbackUtil.registerContextCallbackARB(userParam);
        nglDebugMessageCallbackARB((callback == null) ? 0L : callback.getPointer(), userParam, function_pointer);
    }
    
    static native void nglDebugMessageCallbackARB(final long p0, final long p1, final long p2);
    
    public static int glGetDebugMessageLogARB(final int count, final IntBuffer sources, final IntBuffer types, final IntBuffer ids, final IntBuffer severities, final IntBuffer lengths, final ByteBuffer messageLog) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetDebugMessageLogARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (sources != null) {
            BufferChecks.checkBuffer(sources, count);
        }
        if (types != null) {
            BufferChecks.checkBuffer(types, count);
        }
        if (ids != null) {
            BufferChecks.checkBuffer(ids, count);
        }
        if (severities != null) {
            BufferChecks.checkBuffer(severities, count);
        }
        if (lengths != null) {
            BufferChecks.checkBuffer(lengths, count);
        }
        if (messageLog != null) {
            BufferChecks.checkDirect(messageLog);
        }
        final int __result = nglGetDebugMessageLogARB(count, (messageLog == null) ? 0 : messageLog.remaining(), MemoryUtil.getAddressSafe(sources), MemoryUtil.getAddressSafe(types), MemoryUtil.getAddressSafe(ids), MemoryUtil.getAddressSafe(severities), MemoryUtil.getAddressSafe(lengths), MemoryUtil.getAddressSafe(messageLog), function_pointer);
        return __result;
    }
    
    static native int nglGetDebugMessageLogARB(final int p0, final int p1, final long p2, final long p3, final long p4, final long p5, final long p6, final long p7, final long p8);
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;

public final class NVTransformFeedback2
{
    public static final int GL_TRANSFORM_FEEDBACK_NV = 36386;
    public static final int GL_TRANSFORM_FEEDBACK_BUFFER_PAUSED_NV = 36387;
    public static final int GL_TRANSFORM_FEEDBACK_BUFFER_ACTIVE_NV = 36388;
    public static final int GL_TRANSFORM_FEEDBACK_BINDING_NV = 36389;
    
    private NVTransformFeedback2() {
    }
    
    public static void glBindTransformFeedbackNV(final int target, final int id) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindTransformFeedbackNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBindTransformFeedbackNV(target, id, function_pointer);
    }
    
    static native void nglBindTransformFeedbackNV(final int p0, final int p1, final long p2);
    
    public static void glDeleteTransformFeedbacksNV(final IntBuffer ids) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteTransformFeedbacksNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(ids);
        nglDeleteTransformFeedbacksNV(ids.remaining(), MemoryUtil.getAddress(ids), function_pointer);
    }
    
    static native void nglDeleteTransformFeedbacksNV(final int p0, final long p1, final long p2);
    
    public static void glDeleteTransformFeedbacksNV(final int id) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteTransformFeedbacksNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDeleteTransformFeedbacksNV(1, APIUtil.getInt(caps, id), function_pointer);
    }
    
    public static void glGenTransformFeedbacksNV(final IntBuffer ids) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenTransformFeedbacksNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(ids);
        nglGenTransformFeedbacksNV(ids.remaining(), MemoryUtil.getAddress(ids), function_pointer);
    }
    
    static native void nglGenTransformFeedbacksNV(final int p0, final long p1, final long p2);
    
    public static int glGenTransformFeedbacksNV() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenTransformFeedbacksNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer ids = APIUtil.getBufferInt(caps);
        nglGenTransformFeedbacksNV(1, MemoryUtil.getAddress(ids), function_pointer);
        return ids.get(0);
    }
    
    public static boolean glIsTransformFeedbackNV(final int id) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsTransformFeedbackNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsTransformFeedbackNV(id, function_pointer);
        return __result;
    }
    
    static native boolean nglIsTransformFeedbackNV(final int p0, final long p1);
    
    public static void glPauseTransformFeedbackNV() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPauseTransformFeedbackNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglPauseTransformFeedbackNV(function_pointer);
    }
    
    static native void nglPauseTransformFeedbackNV(final long p0);
    
    public static void glResumeTransformFeedbackNV() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glResumeTransformFeedbackNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglResumeTransformFeedbackNV(function_pointer);
    }
    
    static native void nglResumeTransformFeedbackNV(final long p0);
    
    public static void glDrawTransformFeedbackNV(final int mode, final int id) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawTransformFeedbackNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDrawTransformFeedbackNV(mode, id, function_pointer);
    }
    
    static native void nglDrawTransformFeedbackNV(final int p0, final int p1, final long p2);
}

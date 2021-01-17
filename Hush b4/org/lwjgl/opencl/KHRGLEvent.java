// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GLSync;

public final class KHRGLEvent
{
    public static final int CL_COMMAND_GL_FENCE_SYNC_OBJECT_KHR = 8205;
    
    private KHRGLEvent() {
    }
    
    public static CLEvent clCreateEventFromGLsyncKHR(final CLContext context, final GLSync sync, final IntBuffer errcode_ret) {
        final long function_pointer = CLCapabilities.clCreateEventFromGLsyncKHR;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (errcode_ret != null) {
            BufferChecks.checkBuffer(errcode_ret, 1);
        }
        final CLEvent __result = new CLEvent(nclCreateEventFromGLsyncKHR(context.getPointer(), sync.getPointer(), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
        return __result;
    }
    
    static native long nclCreateEventFromGLsyncKHR(final long p0, final long p1, final long p2, final long p3);
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

import org.lwjgl.BufferChecks;

public final class KHRTerminateContext
{
    public static final int CL_DEVICE_TERMINATE_CAPABILITY_KHR = 8207;
    public static final int CL_CONTEXT_TERMINATE_KHR = 8208;
    
    private KHRTerminateContext() {
    }
    
    public static int clTerminateContextKHR(final CLContext context) {
        final long function_pointer = CLCapabilities.clTerminateContextKHR;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nclTerminateContextKHR(context.getPointer(), function_pointer);
        return __result;
    }
    
    static native int nclTerminateContextKHR(final long p0, final long p1);
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import org.lwjgl.PointerBuffer;
import java.nio.ByteBuffer;

public final class KHRSubgroups
{
    private KHRSubgroups() {
    }
    
    public static int clGetKernelSubGroupInfoKHR(final CLKernel kernel, final CLDevice device, final int param_name, final ByteBuffer input_value, final ByteBuffer param_value, final PointerBuffer param_value_size_ret) {
        final long function_pointer = CLCapabilities.clGetKernelSubGroupInfoKHR;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (input_value != null) {
            BufferChecks.checkDirect(input_value);
        }
        if (param_value != null) {
            BufferChecks.checkDirect(param_value);
        }
        if (param_value_size_ret != null) {
            BufferChecks.checkBuffer(param_value_size_ret, 1);
        }
        final int __result = nclGetKernelSubGroupInfoKHR(kernel.getPointer(), (device == null) ? 0L : device.getPointer(), param_name, (input_value == null) ? 0 : input_value.remaining(), MemoryUtil.getAddressSafe(input_value), (param_value == null) ? 0 : param_value.remaining(), MemoryUtil.getAddressSafe(param_value), MemoryUtil.getAddressSafe(param_value_size_ret), function_pointer);
        return __result;
    }
    
    static native int nclGetKernelSubGroupInfoKHR(final long p0, final long p1, final int p2, final long p3, final long p4, final long p5, final long p6, final long p7, final long p8);
}

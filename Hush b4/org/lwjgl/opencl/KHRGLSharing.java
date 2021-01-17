// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.ByteBuffer;
import org.lwjgl.PointerBuffer;

public final class KHRGLSharing
{
    public static final int CL_INVALID_GL_SHAREGROUP_REFERENCE_KHR = -1000;
    public static final int CL_CURRENT_DEVICE_FOR_GL_CONTEXT_KHR = 8198;
    public static final int CL_DEVICES_FOR_GL_CONTEXT_KHR = 8199;
    public static final int CL_GL_CONTEXT_KHR = 8200;
    public static final int CL_EGL_DISPLAY_KHR = 8201;
    public static final int CL_GLX_DISPLAY_KHR = 8202;
    public static final int CL_WGL_HDC_KHR = 8203;
    public static final int CL_CGL_SHAREGROUP_KHR = 8204;
    
    private KHRGLSharing() {
    }
    
    public static int clGetGLContextInfoKHR(final PointerBuffer properties, final int param_name, final ByteBuffer param_value, PointerBuffer param_value_size_ret) {
        final long function_pointer = CLCapabilities.clGetGLContextInfoKHR;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(properties);
        BufferChecks.checkNullTerminated(properties);
        if (param_value != null) {
            BufferChecks.checkDirect(param_value);
        }
        if (param_value_size_ret != null) {
            BufferChecks.checkBuffer(param_value_size_ret, 1);
        }
        if (param_value_size_ret == null && APIUtil.isDevicesParam(param_name)) {
            param_value_size_ret = APIUtil.getBufferPointer();
        }
        final int __result = nclGetGLContextInfoKHR(MemoryUtil.getAddress(properties), param_name, (param_value == null) ? 0 : param_value.remaining(), MemoryUtil.getAddressSafe(param_value), MemoryUtil.getAddressSafe(param_value_size_ret), function_pointer);
        if (__result == 0 && param_value != null && APIUtil.isDevicesParam(param_name)) {
            APIUtil.getCLPlatform(properties).registerCLDevices(param_value, param_value_size_ret);
        }
        return __result;
    }
    
    static native int nclGetGLContextInfoKHR(final long p0, final int p1, final long p2, final long p3, final long p4, final long p5);
}

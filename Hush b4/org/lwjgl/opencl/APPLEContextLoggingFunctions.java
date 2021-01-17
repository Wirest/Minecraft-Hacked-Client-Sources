// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.ByteBuffer;

final class APPLEContextLoggingFunctions
{
    private APPLEContextLoggingFunctions() {
    }
    
    static void clLogMessagesToSystemLogAPPLE(final ByteBuffer errstr, final ByteBuffer private_info, final ByteBuffer user_data) {
        final long function_pointer = CLCapabilities.clLogMessagesToSystemLogAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(errstr);
        BufferChecks.checkDirect(private_info);
        BufferChecks.checkDirect(user_data);
        nclLogMessagesToSystemLogAPPLE(MemoryUtil.getAddress(errstr), MemoryUtil.getAddress(private_info), private_info.remaining(), MemoryUtil.getAddress(user_data), function_pointer);
    }
    
    static native void nclLogMessagesToSystemLogAPPLE(final long p0, final long p1, final long p2, final long p3, final long p4);
    
    static void clLogMessagesToStdoutAPPLE(final ByteBuffer errstr, final ByteBuffer private_info, final ByteBuffer user_data) {
        final long function_pointer = CLCapabilities.clLogMessagesToStdoutAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(errstr);
        BufferChecks.checkDirect(private_info);
        BufferChecks.checkDirect(user_data);
        nclLogMessagesToStdoutAPPLE(MemoryUtil.getAddress(errstr), MemoryUtil.getAddress(private_info), private_info.remaining(), MemoryUtil.getAddress(user_data), function_pointer);
    }
    
    static native void nclLogMessagesToStdoutAPPLE(final long p0, final long p1, final long p2, final long p3, final long p4);
    
    static void clLogMessagesToStderrAPPLE(final ByteBuffer errstr, final ByteBuffer private_info, final ByteBuffer user_data) {
        final long function_pointer = CLCapabilities.clLogMessagesToStderrAPPLE;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(errstr);
        BufferChecks.checkDirect(private_info);
        BufferChecks.checkDirect(user_data);
        nclLogMessagesToStderrAPPLE(MemoryUtil.getAddress(errstr), MemoryUtil.getAddress(private_info), private_info.remaining(), MemoryUtil.getAddress(user_data), function_pointer);
    }
    
    static native void nclLogMessagesToStderrAPPLE(final long p0, final long p1, final long p2, final long p3, final long p4);
}

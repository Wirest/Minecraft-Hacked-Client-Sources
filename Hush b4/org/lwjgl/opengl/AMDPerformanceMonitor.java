// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.IntBuffer;

public final class AMDPerformanceMonitor
{
    public static final int GL_COUNTER_TYPE_AMD = 35776;
    public static final int GL_COUNTER_RANGE_AMD = 35777;
    public static final int GL_UNSIGNED_INT64_AMD = 35778;
    public static final int GL_PERCENTAGE_AMD = 35779;
    public static final int GL_PERFMON_RESULT_AVAILABLE_AMD = 35780;
    public static final int GL_PERFMON_RESULT_SIZE_AMD = 35781;
    public static final int GL_PERFMON_RESULT_AMD = 35782;
    
    private AMDPerformanceMonitor() {
    }
    
    public static void glGetPerfMonitorGroupsAMD(final IntBuffer numGroups, final IntBuffer groups) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetPerfMonitorGroupsAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (numGroups != null) {
            BufferChecks.checkBuffer(numGroups, 1);
        }
        BufferChecks.checkDirect(groups);
        nglGetPerfMonitorGroupsAMD(MemoryUtil.getAddressSafe(numGroups), groups.remaining(), MemoryUtil.getAddress(groups), function_pointer);
    }
    
    static native void nglGetPerfMonitorGroupsAMD(final long p0, final int p1, final long p2, final long p3);
    
    public static void glGetPerfMonitorCountersAMD(final int group, final IntBuffer numCounters, final IntBuffer maxActiveCounters, final IntBuffer counters) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetPerfMonitorCountersAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(numCounters, 1);
        BufferChecks.checkBuffer(maxActiveCounters, 1);
        if (counters != null) {
            BufferChecks.checkDirect(counters);
        }
        nglGetPerfMonitorCountersAMD(group, MemoryUtil.getAddress(numCounters), MemoryUtil.getAddress(maxActiveCounters), (counters == null) ? 0 : counters.remaining(), MemoryUtil.getAddressSafe(counters), function_pointer);
    }
    
    static native void nglGetPerfMonitorCountersAMD(final int p0, final long p1, final long p2, final int p3, final long p4, final long p5);
    
    public static void glGetPerfMonitorGroupStringAMD(final int group, final IntBuffer length, final ByteBuffer groupString) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetPerfMonitorGroupStringAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (length != null) {
            BufferChecks.checkBuffer(length, 1);
        }
        if (groupString != null) {
            BufferChecks.checkDirect(groupString);
        }
        nglGetPerfMonitorGroupStringAMD(group, (groupString == null) ? 0 : groupString.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddressSafe(groupString), function_pointer);
    }
    
    static native void nglGetPerfMonitorGroupStringAMD(final int p0, final int p1, final long p2, final long p3, final long p4);
    
    public static String glGetPerfMonitorGroupStringAMD(final int group, final int bufSize) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetPerfMonitorGroupStringAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer groupString_length = APIUtil.getLengths(caps);
        final ByteBuffer groupString = APIUtil.getBufferByte(caps, bufSize);
        nglGetPerfMonitorGroupStringAMD(group, bufSize, MemoryUtil.getAddress0(groupString_length), MemoryUtil.getAddress(groupString), function_pointer);
        groupString.limit(groupString_length.get(0));
        return APIUtil.getString(caps, groupString);
    }
    
    public static void glGetPerfMonitorCounterStringAMD(final int group, final int counter, final IntBuffer length, final ByteBuffer counterString) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetPerfMonitorCounterStringAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (length != null) {
            BufferChecks.checkBuffer(length, 1);
        }
        if (counterString != null) {
            BufferChecks.checkDirect(counterString);
        }
        nglGetPerfMonitorCounterStringAMD(group, counter, (counterString == null) ? 0 : counterString.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddressSafe(counterString), function_pointer);
    }
    
    static native void nglGetPerfMonitorCounterStringAMD(final int p0, final int p1, final int p2, final long p3, final long p4, final long p5);
    
    public static String glGetPerfMonitorCounterStringAMD(final int group, final int counter, final int bufSize) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetPerfMonitorCounterStringAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer counterString_length = APIUtil.getLengths(caps);
        final ByteBuffer counterString = APIUtil.getBufferByte(caps, bufSize);
        nglGetPerfMonitorCounterStringAMD(group, counter, bufSize, MemoryUtil.getAddress0(counterString_length), MemoryUtil.getAddress(counterString), function_pointer);
        counterString.limit(counterString_length.get(0));
        return APIUtil.getString(caps, counterString);
    }
    
    public static void glGetPerfMonitorCounterInfoAMD(final int group, final int counter, final int pname, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetPerfMonitorCounterInfoAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(data, 16);
        nglGetPerfMonitorCounterInfoAMD(group, counter, pname, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglGetPerfMonitorCounterInfoAMD(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGenPerfMonitorsAMD(final IntBuffer monitors) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenPerfMonitorsAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(monitors);
        nglGenPerfMonitorsAMD(monitors.remaining(), MemoryUtil.getAddress(monitors), function_pointer);
    }
    
    static native void nglGenPerfMonitorsAMD(final int p0, final long p1, final long p2);
    
    public static int glGenPerfMonitorsAMD() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenPerfMonitorsAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer monitors = APIUtil.getBufferInt(caps);
        nglGenPerfMonitorsAMD(1, MemoryUtil.getAddress(monitors), function_pointer);
        return monitors.get(0);
    }
    
    public static void glDeletePerfMonitorsAMD(final IntBuffer monitors) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeletePerfMonitorsAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(monitors);
        nglDeletePerfMonitorsAMD(monitors.remaining(), MemoryUtil.getAddress(monitors), function_pointer);
    }
    
    static native void nglDeletePerfMonitorsAMD(final int p0, final long p1, final long p2);
    
    public static void glDeletePerfMonitorsAMD(final int monitor) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeletePerfMonitorsAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDeletePerfMonitorsAMD(1, APIUtil.getInt(caps, monitor), function_pointer);
    }
    
    public static void glSelectPerfMonitorCountersAMD(final int monitor, final boolean enable, final int group, final IntBuffer counterList) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSelectPerfMonitorCountersAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(counterList);
        nglSelectPerfMonitorCountersAMD(monitor, enable, group, counterList.remaining(), MemoryUtil.getAddress(counterList), function_pointer);
    }
    
    static native void nglSelectPerfMonitorCountersAMD(final int p0, final boolean p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glSelectPerfMonitorCountersAMD(final int monitor, final boolean enable, final int group, final int counter) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSelectPerfMonitorCountersAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglSelectPerfMonitorCountersAMD(monitor, enable, group, 1, APIUtil.getInt(caps, counter), function_pointer);
    }
    
    public static void glBeginPerfMonitorAMD(final int monitor) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBeginPerfMonitorAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBeginPerfMonitorAMD(monitor, function_pointer);
    }
    
    static native void nglBeginPerfMonitorAMD(final int p0, final long p1);
    
    public static void glEndPerfMonitorAMD(final int monitor) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glEndPerfMonitorAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglEndPerfMonitorAMD(monitor, function_pointer);
    }
    
    static native void nglEndPerfMonitorAMD(final int p0, final long p1);
    
    public static void glGetPerfMonitorCounterDataAMD(final int monitor, final int pname, final IntBuffer data, final IntBuffer bytesWritten) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetPerfMonitorCounterDataAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(data);
        if (bytesWritten != null) {
            BufferChecks.checkBuffer(bytesWritten, 1);
        }
        nglGetPerfMonitorCounterDataAMD(monitor, pname, data.remaining(), MemoryUtil.getAddress(data), MemoryUtil.getAddressSafe(bytesWritten), function_pointer);
    }
    
    static native void nglGetPerfMonitorCounterDataAMD(final int p0, final int p1, final int p2, final long p3, final long p4, final long p5);
    
    public static int glGetPerfMonitorCounterDataAMD(final int monitor, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetPerfMonitorCounterDataAMD;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer data = APIUtil.getBufferInt(caps);
        nglGetPerfMonitorCounterDataAMD(monitor, pname, 4, MemoryUtil.getAddress(data), 0L, function_pointer);
        return data.get(0);
    }
}

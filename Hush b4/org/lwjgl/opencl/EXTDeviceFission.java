// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

import org.lwjgl.MemoryUtil;
import java.nio.IntBuffer;
import org.lwjgl.PointerBuffer;
import java.nio.LongBuffer;
import org.lwjgl.BufferChecks;

public final class EXTDeviceFission
{
    public static final int CL_DEVICE_PARTITION_EQUALLY_EXT = 16464;
    public static final int CL_DEVICE_PARTITION_BY_COUNTS_EXT = 16465;
    public static final int CL_DEVICE_PARTITION_BY_NAMES_EXT = 16466;
    public static final int CL_DEVICE_PARTITION_BY_AFFINITY_DOMAIN_EXT = 16467;
    public static final int CL_AFFINITY_DOMAIN_L1_CACHE_EXT = 1;
    public static final int CL_AFFINITY_DOMAIN_L2_CACHE_EXT = 2;
    public static final int CL_AFFINITY_DOMAIN_L3_CACHE_EXT = 3;
    public static final int CL_AFFINITY_DOMAIN_L4_CACHE_EXT = 4;
    public static final int CL_AFFINITY_DOMAIN_NUMA_EXT = 16;
    public static final int CL_AFFINITY_DOMAIN_NEXT_FISSIONABLE_EXT = 256;
    public static final int CL_DEVICE_PARENT_DEVICE_EXT = 16468;
    public static final int CL_DEVICE_PARITION_TYPES_EXT = 16469;
    public static final int CL_DEVICE_AFFINITY_DOMAINS_EXT = 16470;
    public static final int CL_DEVICE_REFERENCE_COUNT_EXT = 16471;
    public static final int CL_DEVICE_PARTITION_STYLE_EXT = 16472;
    public static final int CL_PROPERTIES_LIST_END_EXT = 0;
    public static final int CL_PARTITION_BY_COUNTS_LIST_END_EXT = 0;
    public static final int CL_PARTITION_BY_NAMES_LIST_END_EXT = -1;
    public static final int CL_DEVICE_PARTITION_FAILED_EXT = -1057;
    public static final int CL_INVALID_PARTITION_COUNT_EXT = -1058;
    public static final int CL_INVALID_PARTITION_NAME_EXT = -1059;
    
    private EXTDeviceFission() {
    }
    
    public static int clRetainDeviceEXT(final CLDevice device) {
        final long function_pointer = CLCapabilities.clRetainDeviceEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nclRetainDeviceEXT(device.getPointer(), function_pointer);
        if (__result == 0) {
            device.retain();
        }
        return __result;
    }
    
    static native int nclRetainDeviceEXT(final long p0, final long p1);
    
    public static int clReleaseDeviceEXT(final CLDevice device) {
        final long function_pointer = CLCapabilities.clReleaseDeviceEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        APIUtil.releaseObjects(device);
        final int __result = nclReleaseDeviceEXT(device.getPointer(), function_pointer);
        if (__result == 0) {
            device.release();
        }
        return __result;
    }
    
    static native int nclReleaseDeviceEXT(final long p0, final long p1);
    
    public static int clCreateSubDevicesEXT(final CLDevice in_device, final LongBuffer properties, final PointerBuffer out_devices, final IntBuffer num_devices) {
        final long function_pointer = CLCapabilities.clCreateSubDevicesEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(properties);
        BufferChecks.checkNullTerminated(properties);
        if (out_devices != null) {
            BufferChecks.checkDirect(out_devices);
        }
        if (num_devices != null) {
            BufferChecks.checkBuffer(num_devices, 1);
        }
        final int __result = nclCreateSubDevicesEXT(in_device.getPointer(), MemoryUtil.getAddress(properties), (out_devices == null) ? 0 : out_devices.remaining(), MemoryUtil.getAddressSafe(out_devices), MemoryUtil.getAddressSafe(num_devices), function_pointer);
        if (__result == 0 && out_devices != null) {
            in_device.registerSubCLDevices(out_devices);
        }
        return __result;
    }
    
    static native int nclCreateSubDevicesEXT(final long p0, final long p1, final int p2, final long p3, final long p4, final long p5);
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

import java.nio.ShortBuffer;
import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import org.lwjgl.MemoryUtil;
import java.nio.IntBuffer;
import org.lwjgl.PointerBuffer;
import java.nio.LongBuffer;
import org.lwjgl.BufferChecks;

public final class CL12
{
    public static final int CL_COMPILE_PROGRAM_FAILURE = -15;
    public static final int CL_LINKER_NOT_AVAILABLE = -16;
    public static final int CL_LINK_PROGRAM_FAILURE = -17;
    public static final int CL_DEVICE_PARTITION_FAILED = -18;
    public static final int CL_KERNEL_ARG_INFO_NOT_AVAILABLE = -19;
    public static final int CL_INVALID_IMAGE_DESCRIPTOR = -65;
    public static final int CL_INVALID_COMPILER_OPTIONS = -66;
    public static final int CL_INVALID_LINKER_OPTIONS = -67;
    public static final int CL_INVALID_DEVICE_PARTITION_COUNT = -68;
    public static final int CL_VERSION_1_2 = 1;
    public static final int CL_BLOCKING = 1;
    public static final int CL_NON_BLOCKING = 0;
    public static final int CL_DEVICE_TYPE_CUSTOM = 16;
    public static final int CL_DEVICE_DOUBLE_FP_CONFIG = 4146;
    public static final int CL_DEVICE_LINKER_AVAILABLE = 4158;
    public static final int CL_DEVICE_BUILT_IN_KERNELS = 4159;
    public static final int CL_DEVICE_IMAGE_MAX_BUFFER_SIZE = 4160;
    public static final int CL_DEVICE_IMAGE_MAX_ARRAY_SIZE = 4161;
    public static final int CL_DEVICE_PARENT_DEVICE = 4162;
    public static final int CL_DEVICE_PARTITION_MAX_SUB_DEVICES = 4163;
    public static final int CL_DEVICE_PARTITION_PROPERTIES = 4164;
    public static final int CL_DEVICE_PARTITION_AFFINITY_DOMAIN = 4165;
    public static final int CL_DEVICE_PARTITION_TYPE = 4166;
    public static final int CL_DEVICE_REFERENCE_COUNT = 4167;
    public static final int CL_DEVICE_PREFERRED_INTEROP_USER_SYNC = 4168;
    public static final int CL_DEVICE_PRINTF_BUFFER_SIZE = 4169;
    public static final int CL_FP_CORRECTLY_ROUNDED_DIVIDE_SQRT = 128;
    public static final int CL_CONTEXT_INTEROP_USER_SYNC = 4229;
    public static final int CL_DEVICE_PARTITION_EQUALLY = 4230;
    public static final int CL_DEVICE_PARTITION_BY_COUNTS = 4231;
    public static final int CL_DEVICE_PARTITION_BY_COUNTS_LIST_END = 0;
    public static final int CL_DEVICE_PARTITION_BY_AFFINITY_DOMAIN = 4232;
    public static final int CL_DEVICE_AFFINITY_DOMAIN_NUMA = 1;
    public static final int CL_DEVICE_AFFINITY_DOMAIN_L4_CACHE = 2;
    public static final int CL_DEVICE_AFFINITY_DOMAIN_L3_CACHE = 4;
    public static final int CL_DEVICE_AFFINITY_DOMAIN_L2_CACHE = 8;
    public static final int CL_DEVICE_AFFINITY_DOMAIN_L1_CACHE = 16;
    public static final int CL_DEVICE_AFFINITY_DOMAIN_NEXT_PARTITIONABLE = 32;
    public static final int CL_MEM_HOST_WRITE_ONLY = 128;
    public static final int CL_MEM_HOST_READ_ONLY = 256;
    public static final int CL_MEM_HOST_NO_ACCESS = 512;
    public static final int CL_MIGRATE_MEM_OBJECT_HOST = 1;
    public static final int CL_MIGRATE_MEM_OBJECT_CONTENT_UNDEFINED = 2;
    public static final int CL_MEM_OBJECT_IMAGE2D_ARRAY = 4339;
    public static final int CL_MEM_OBJECT_IMAGE1D = 4340;
    public static final int CL_MEM_OBJECT_IMAGE1D_ARRAY = 4341;
    public static final int CL_MEM_OBJECT_IMAGE1D_BUFFER = 4342;
    public static final int CL_IMAGE_ARRAY_SIZE = 4375;
    public static final int CL_IMAGE_BUFFER = 4376;
    public static final int CL_IMAGE_NUM_MIP_LEVELS = 4377;
    public static final int CL_IMAGE_NUM_SAMPLES = 4378;
    public static final int CL_MAP_WRITE_INVALIDATE_REGION = 4;
    public static final int CL_PROGRAM_NUM_KERNELS = 4455;
    public static final int CL_PROGRAM_KERNEL_NAMES = 4456;
    public static final int CL_PROGRAM_BINARY_TYPE = 4484;
    public static final int CL_PROGRAM_BINARY_TYPE_NONE = 0;
    public static final int CL_PROGRAM_BINARY_TYPE_COMPILED_OBJECT = 1;
    public static final int CL_PROGRAM_BINARY_TYPE_LIBRARY = 2;
    public static final int CL_PROGRAM_BINARY_TYPE_EXECUTABLE = 4;
    public static final int CL_KERNEL_ATTRIBUTES = 4501;
    public static final int CL_KERNEL_ARG_ADDRESS_QUALIFIER = 4502;
    public static final int CL_KERNEL_ARG_ACCESS_QUALIFIER = 4503;
    public static final int CL_KERNEL_ARG_TYPE_NAME = 4504;
    public static final int CL_KERNEL_ARG_TYPE_QUALIFIER = 4505;
    public static final int CL_KERNEL_ARG_NAME = 4506;
    public static final int CL_KERNEL_ARG_ADDRESS_GLOBAL = 4506;
    public static final int CL_KERNEL_ARG_ADDRESS_LOCAL = 4507;
    public static final int CL_KERNEL_ARG_ADDRESS_CONSTANT = 4508;
    public static final int CL_KERNEL_ARG_ADDRESS_PRIVATE = 4509;
    public static final int CL_KERNEL_ARG_ACCESS_READ_ONLY = 4512;
    public static final int CL_KERNEL_ARG_ACCESS_WRITE_ONLY = 4513;
    public static final int CL_KERNEL_ARG_ACCESS_READ_WRITE = 4514;
    public static final int CL_KERNEL_ARG_ACCESS_NONE = 4515;
    public static final int CL_KERNEL_ARG_TYPE_NONE = 0;
    public static final int CL_KERNEL_ARG_TYPE_CONST = 1;
    public static final int CL_KERNEL_ARG_TYPE_RESTRICT = 2;
    public static final int CL_KERNEL_ARG_TYPE_VOLATILE = 4;
    public static final int CL_KERNEL_GLOBAL_WORK_SIZE = 4533;
    public static final int CL_COMMAND_BARRIER = 4613;
    public static final int CL_COMMAND_MIGRATE_MEM_OBJECTS = 4614;
    public static final int CL_COMMAND_FILL_BUFFER = 4615;
    public static final int CL_COMMAND_FILL_IMAGE = 4616;
    
    private CL12() {
    }
    
    public static int clRetainDevice(final CLDevice device) {
        final long function_pointer = CLCapabilities.clRetainDevice;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nclRetainDevice(device.getPointer(), function_pointer);
        if (__result == 0) {
            device.retain();
        }
        return __result;
    }
    
    static native int nclRetainDevice(final long p0, final long p1);
    
    public static int clReleaseDevice(final CLDevice device) {
        final long function_pointer = CLCapabilities.clReleaseDevice;
        BufferChecks.checkFunctionAddress(function_pointer);
        APIUtil.releaseObjects(device);
        final int __result = nclReleaseDevice(device.getPointer(), function_pointer);
        if (__result == 0) {
            device.release();
        }
        return __result;
    }
    
    static native int nclReleaseDevice(final long p0, final long p1);
    
    public static int clCreateSubDevices(final CLDevice in_device, final LongBuffer properties, final PointerBuffer out_devices, final IntBuffer num_devices_ret) {
        final long function_pointer = CLCapabilities.clCreateSubDevices;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(properties);
        BufferChecks.checkNullTerminated(properties);
        if (out_devices != null) {
            BufferChecks.checkDirect(out_devices);
        }
        if (num_devices_ret != null) {
            BufferChecks.checkBuffer(num_devices_ret, 1);
        }
        final int __result = nclCreateSubDevices(in_device.getPointer(), MemoryUtil.getAddress(properties), (out_devices == null) ? 0 : out_devices.remaining(), MemoryUtil.getAddressSafe(out_devices), MemoryUtil.getAddressSafe(num_devices_ret), function_pointer);
        if (__result == 0 && out_devices != null) {
            in_device.registerSubCLDevices(out_devices);
        }
        return __result;
    }
    
    static native int nclCreateSubDevices(final long p0, final long p1, final int p2, final long p3, final long p4, final long p5);
    
    public static CLMem clCreateImage(final CLContext context, final long flags, final ByteBuffer image_format, final ByteBuffer image_desc, final ByteBuffer host_ptr, final IntBuffer errcode_ret) {
        final long function_pointer = CLCapabilities.clCreateImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(image_format, 8);
        BufferChecks.checkBuffer(image_desc, 7 * PointerBuffer.getPointerSize() + 8 + PointerBuffer.getPointerSize());
        if (host_ptr != null) {
            BufferChecks.checkDirect(host_ptr);
        }
        if (errcode_ret != null) {
            BufferChecks.checkBuffer(errcode_ret, 1);
        }
        final CLMem __result = new CLMem(nclCreateImage(context.getPointer(), flags, MemoryUtil.getAddress(image_format), MemoryUtil.getAddress(image_desc), MemoryUtil.getAddressSafe(host_ptr), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
        return __result;
    }
    
    public static CLMem clCreateImage(final CLContext context, final long flags, final ByteBuffer image_format, final ByteBuffer image_desc, final FloatBuffer host_ptr, final IntBuffer errcode_ret) {
        final long function_pointer = CLCapabilities.clCreateImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(image_format, 8);
        BufferChecks.checkBuffer(image_desc, 7 * PointerBuffer.getPointerSize() + 8 + PointerBuffer.getPointerSize());
        if (host_ptr != null) {
            BufferChecks.checkDirect(host_ptr);
        }
        if (errcode_ret != null) {
            BufferChecks.checkBuffer(errcode_ret, 1);
        }
        final CLMem __result = new CLMem(nclCreateImage(context.getPointer(), flags, MemoryUtil.getAddress(image_format), MemoryUtil.getAddress(image_desc), MemoryUtil.getAddressSafe(host_ptr), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
        return __result;
    }
    
    public static CLMem clCreateImage(final CLContext context, final long flags, final ByteBuffer image_format, final ByteBuffer image_desc, final IntBuffer host_ptr, final IntBuffer errcode_ret) {
        final long function_pointer = CLCapabilities.clCreateImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(image_format, 8);
        BufferChecks.checkBuffer(image_desc, 7 * PointerBuffer.getPointerSize() + 8 + PointerBuffer.getPointerSize());
        if (host_ptr != null) {
            BufferChecks.checkDirect(host_ptr);
        }
        if (errcode_ret != null) {
            BufferChecks.checkBuffer(errcode_ret, 1);
        }
        final CLMem __result = new CLMem(nclCreateImage(context.getPointer(), flags, MemoryUtil.getAddress(image_format), MemoryUtil.getAddress(image_desc), MemoryUtil.getAddressSafe(host_ptr), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
        return __result;
    }
    
    public static CLMem clCreateImage(final CLContext context, final long flags, final ByteBuffer image_format, final ByteBuffer image_desc, final ShortBuffer host_ptr, final IntBuffer errcode_ret) {
        final long function_pointer = CLCapabilities.clCreateImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(image_format, 8);
        BufferChecks.checkBuffer(image_desc, 7 * PointerBuffer.getPointerSize() + 8 + PointerBuffer.getPointerSize());
        if (host_ptr != null) {
            BufferChecks.checkDirect(host_ptr);
        }
        if (errcode_ret != null) {
            BufferChecks.checkBuffer(errcode_ret, 1);
        }
        final CLMem __result = new CLMem(nclCreateImage(context.getPointer(), flags, MemoryUtil.getAddress(image_format), MemoryUtil.getAddress(image_desc), MemoryUtil.getAddressSafe(host_ptr), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
        return __result;
    }
    
    static native long nclCreateImage(final long p0, final long p1, final long p2, final long p3, final long p4, final long p5, final long p6);
    
    public static CLProgram clCreateProgramWithBuiltInKernels(final CLContext context, final PointerBuffer device_list, final ByteBuffer kernel_names, final IntBuffer errcode_ret) {
        final long function_pointer = CLCapabilities.clCreateProgramWithBuiltInKernels;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(device_list, 1);
        BufferChecks.checkDirect(kernel_names);
        if (errcode_ret != null) {
            BufferChecks.checkBuffer(errcode_ret, 1);
        }
        final CLProgram __result = new CLProgram(nclCreateProgramWithBuiltInKernels(context.getPointer(), device_list.remaining(), MemoryUtil.getAddress(device_list), MemoryUtil.getAddress(kernel_names), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
        return __result;
    }
    
    static native long nclCreateProgramWithBuiltInKernels(final long p0, final int p1, final long p2, final long p3, final long p4, final long p5);
    
    public static CLProgram clCreateProgramWithBuiltInKernels(final CLContext context, final PointerBuffer device_list, final CharSequence kernel_names, final IntBuffer errcode_ret) {
        final long function_pointer = CLCapabilities.clCreateProgramWithBuiltInKernels;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(device_list, 1);
        if (errcode_ret != null) {
            BufferChecks.checkBuffer(errcode_ret, 1);
        }
        final CLProgram __result = new CLProgram(nclCreateProgramWithBuiltInKernels(context.getPointer(), device_list.remaining(), MemoryUtil.getAddress(device_list), APIUtil.getBuffer(kernel_names), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
        return __result;
    }
    
    public static int clCompileProgram(final CLProgram program, final PointerBuffer device_list, final ByteBuffer options, final PointerBuffer input_header, final ByteBuffer header_include_name, final CLCompileProgramCallback pfn_notify) {
        final long function_pointer = CLCapabilities.clCompileProgram;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (device_list != null) {
            BufferChecks.checkDirect(device_list);
        }
        BufferChecks.checkDirect(options);
        BufferChecks.checkNullTerminated(options);
        BufferChecks.checkBuffer(input_header, 1);
        BufferChecks.checkDirect(header_include_name);
        BufferChecks.checkNullTerminated(header_include_name);
        final long user_data = CallbackUtil.createGlobalRef(pfn_notify);
        if (pfn_notify != null) {
            pfn_notify.setContext(program.getParent());
        }
        int __result = 0;
        try {
            __result = nclCompileProgram(program.getPointer(), (device_list == null) ? 0 : device_list.remaining(), MemoryUtil.getAddressSafe(device_list), MemoryUtil.getAddress(options), 1, MemoryUtil.getAddress(input_header), MemoryUtil.getAddress(header_include_name), (pfn_notify == null) ? 0L : pfn_notify.getPointer(), user_data, function_pointer);
            return __result;
        }
        finally {
            CallbackUtil.checkCallback(__result, user_data);
        }
    }
    
    static native int nclCompileProgram(final long p0, final int p1, final long p2, final long p3, final int p4, final long p5, final long p6, final long p7, final long p8, final long p9);
    
    public static int clCompileProgramMulti(final CLProgram program, final PointerBuffer device_list, final ByteBuffer options, final PointerBuffer input_headers, final ByteBuffer header_include_names, final CLCompileProgramCallback pfn_notify) {
        final long function_pointer = CLCapabilities.clCompileProgram;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (device_list != null) {
            BufferChecks.checkDirect(device_list);
        }
        BufferChecks.checkDirect(options);
        BufferChecks.checkNullTerminated(options);
        BufferChecks.checkBuffer(input_headers, 1);
        BufferChecks.checkDirect(header_include_names);
        BufferChecks.checkNullTerminated(header_include_names, input_headers.remaining());
        final long user_data = CallbackUtil.createGlobalRef(pfn_notify);
        if (pfn_notify != null) {
            pfn_notify.setContext(program.getParent());
        }
        int __result = 0;
        try {
            __result = nclCompileProgramMulti(program.getPointer(), (device_list == null) ? 0 : device_list.remaining(), MemoryUtil.getAddressSafe(device_list), MemoryUtil.getAddress(options), input_headers.remaining(), MemoryUtil.getAddress(input_headers), MemoryUtil.getAddress(header_include_names), (pfn_notify == null) ? 0L : pfn_notify.getPointer(), user_data, function_pointer);
            return __result;
        }
        finally {
            CallbackUtil.checkCallback(__result, user_data);
        }
    }
    
    static native int nclCompileProgramMulti(final long p0, final int p1, final long p2, final long p3, final int p4, final long p5, final long p6, final long p7, final long p8, final long p9);
    
    public static int clCompileProgram(final CLProgram program, final PointerBuffer device_list, final ByteBuffer options, final PointerBuffer input_headers, final ByteBuffer[] header_include_names, final CLCompileProgramCallback pfn_notify) {
        final long function_pointer = CLCapabilities.clCompileProgram;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (device_list != null) {
            BufferChecks.checkDirect(device_list);
        }
        BufferChecks.checkDirect(options);
        BufferChecks.checkNullTerminated(options);
        BufferChecks.checkBuffer(input_headers, header_include_names.length);
        BufferChecks.checkArray(header_include_names, 1);
        final long user_data = CallbackUtil.createGlobalRef(pfn_notify);
        if (pfn_notify != null) {
            pfn_notify.setContext(program.getParent());
        }
        int __result = 0;
        try {
            __result = nclCompileProgram3(program.getPointer(), (device_list == null) ? 0 : device_list.remaining(), MemoryUtil.getAddressSafe(device_list), MemoryUtil.getAddress(options), header_include_names.length, MemoryUtil.getAddress(input_headers), header_include_names, (pfn_notify == null) ? 0L : pfn_notify.getPointer(), user_data, function_pointer);
            return __result;
        }
        finally {
            CallbackUtil.checkCallback(__result, user_data);
        }
    }
    
    static native int nclCompileProgram3(final long p0, final int p1, final long p2, final long p3, final int p4, final long p5, final ByteBuffer[] p6, final long p7, final long p8, final long p9);
    
    public static int clCompileProgram(final CLProgram program, final PointerBuffer device_list, final CharSequence options, final PointerBuffer input_header, final CharSequence header_include_name, final CLCompileProgramCallback pfn_notify) {
        final long function_pointer = CLCapabilities.clCompileProgram;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (device_list != null) {
            BufferChecks.checkDirect(device_list);
        }
        BufferChecks.checkBuffer(input_header, 1);
        final long user_data = CallbackUtil.createGlobalRef(pfn_notify);
        if (pfn_notify != null) {
            pfn_notify.setContext(program.getParent());
        }
        int __result = 0;
        try {
            __result = nclCompileProgram(program.getPointer(), (device_list == null) ? 0 : device_list.remaining(), MemoryUtil.getAddressSafe(device_list), APIUtil.getBufferNT(options), 1, MemoryUtil.getAddress(input_header), APIUtil.getBufferNT(header_include_name), (pfn_notify == null) ? 0L : pfn_notify.getPointer(), user_data, function_pointer);
            return __result;
        }
        finally {
            CallbackUtil.checkCallback(__result, user_data);
        }
    }
    
    public static int clCompileProgram(final CLProgram program, final PointerBuffer device_list, final CharSequence options, final PointerBuffer input_header, final CharSequence[] header_include_name, final CLCompileProgramCallback pfn_notify) {
        final long function_pointer = CLCapabilities.clCompileProgram;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (device_list != null) {
            BufferChecks.checkDirect(device_list);
        }
        BufferChecks.checkBuffer(input_header, 1);
        BufferChecks.checkArray(header_include_name);
        final long user_data = CallbackUtil.createGlobalRef(pfn_notify);
        if (pfn_notify != null) {
            pfn_notify.setContext(program.getParent());
        }
        int __result = 0;
        try {
            __result = nclCompileProgramMulti(program.getPointer(), (device_list == null) ? 0 : device_list.remaining(), MemoryUtil.getAddressSafe(device_list), APIUtil.getBufferNT(options), input_header.remaining(), MemoryUtil.getAddress(input_header), APIUtil.getBufferNT(header_include_name), (pfn_notify == null) ? 0L : pfn_notify.getPointer(), user_data, function_pointer);
            return __result;
        }
        finally {
            CallbackUtil.checkCallback(__result, user_data);
        }
    }
    
    public static CLProgram clLinkProgram(final CLContext context, final PointerBuffer device_list, final ByteBuffer options, final PointerBuffer input_programs, final CLLinkProgramCallback pfn_notify, final IntBuffer errcode_ret) {
        final long function_pointer = CLCapabilities.clLinkProgram;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (device_list != null) {
            BufferChecks.checkDirect(device_list);
        }
        BufferChecks.checkDirect(options);
        BufferChecks.checkNullTerminated(options);
        BufferChecks.checkDirect(input_programs);
        BufferChecks.checkBuffer(errcode_ret, 1);
        final long user_data = CallbackUtil.createGlobalRef(pfn_notify);
        if (pfn_notify != null) {
            pfn_notify.setContext(context);
        }
        CLProgram __result = null;
        try {
            __result = new CLProgram(nclLinkProgram(context.getPointer(), (device_list == null) ? 0 : device_list.remaining(), MemoryUtil.getAddressSafe(device_list), MemoryUtil.getAddress(options), input_programs.remaining(), MemoryUtil.getAddress(input_programs), (pfn_notify == null) ? 0L : pfn_notify.getPointer(), user_data, MemoryUtil.getAddress(errcode_ret), function_pointer), context);
            return __result;
        }
        finally {
            CallbackUtil.checkCallback(errcode_ret.get(errcode_ret.position()), user_data);
        }
    }
    
    static native long nclLinkProgram(final long p0, final int p1, final long p2, final long p3, final int p4, final long p5, final long p6, final long p7, final long p8, final long p9);
    
    public static CLProgram clLinkProgram(final CLContext context, final PointerBuffer device_list, final CharSequence options, final PointerBuffer input_programs, final CLLinkProgramCallback pfn_notify, final IntBuffer errcode_ret) {
        final long function_pointer = CLCapabilities.clLinkProgram;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (device_list != null) {
            BufferChecks.checkDirect(device_list);
        }
        BufferChecks.checkDirect(input_programs);
        BufferChecks.checkBuffer(errcode_ret, 1);
        final long user_data = CallbackUtil.createGlobalRef(pfn_notify);
        if (pfn_notify != null) {
            pfn_notify.setContext(context);
        }
        CLProgram __result = null;
        try {
            __result = new CLProgram(nclLinkProgram(context.getPointer(), (device_list == null) ? 0 : device_list.remaining(), MemoryUtil.getAddressSafe(device_list), APIUtil.getBufferNT(options), input_programs.remaining(), MemoryUtil.getAddress(input_programs), (pfn_notify == null) ? 0L : pfn_notify.getPointer(), user_data, MemoryUtil.getAddress(errcode_ret), function_pointer), context);
            return __result;
        }
        finally {
            CallbackUtil.checkCallback(errcode_ret.get(errcode_ret.position()), user_data);
        }
    }
    
    public static int clUnloadPlatformCompiler(final CLPlatform platform) {
        final long function_pointer = CLCapabilities.clUnloadPlatformCompiler;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nclUnloadPlatformCompiler(platform.getPointer(), function_pointer);
        return __result;
    }
    
    static native int nclUnloadPlatformCompiler(final long p0, final long p1);
    
    public static int clGetKernelArgInfo(final CLKernel kernel, final int arg_indx, final int param_name, final ByteBuffer param_value, final PointerBuffer param_value_size_ret) {
        final long function_pointer = CLCapabilities.clGetKernelArgInfo;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (param_value != null) {
            BufferChecks.checkDirect(param_value);
        }
        if (param_value_size_ret != null) {
            BufferChecks.checkBuffer(param_value_size_ret, 1);
        }
        final int __result = nclGetKernelArgInfo(kernel.getPointer(), arg_indx, param_name, (param_value == null) ? 0 : param_value.remaining(), MemoryUtil.getAddressSafe(param_value), MemoryUtil.getAddressSafe(param_value_size_ret), function_pointer);
        return __result;
    }
    
    static native int nclGetKernelArgInfo(final long p0, final int p1, final int p2, final long p3, final long p4, final long p5, final long p6);
    
    public static int clEnqueueFillBuffer(final CLCommandQueue command_queue, final CLMem buffer, final ByteBuffer pattern, final long offset, final long size, final PointerBuffer event_wait_list, final PointerBuffer event) {
        final long function_pointer = CLCapabilities.clEnqueueFillBuffer;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pattern);
        if (event_wait_list != null) {
            BufferChecks.checkDirect(event_wait_list);
        }
        if (event != null) {
            BufferChecks.checkBuffer(event, 1);
        }
        final int __result = nclEnqueueFillBuffer(command_queue.getPointer(), buffer.getPointer(), MemoryUtil.getAddress(pattern), pattern.remaining(), offset, size, (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
        return __result;
    }
    
    static native int nclEnqueueFillBuffer(final long p0, final long p1, final long p2, final long p3, final long p4, final long p5, final int p6, final long p7, final long p8, final long p9);
    
    public static int clEnqueueFillImage(final CLCommandQueue command_queue, final CLMem image, final ByteBuffer fill_color, final PointerBuffer origin, final PointerBuffer region, final PointerBuffer event_wait_list, final PointerBuffer event) {
        final long function_pointer = CLCapabilities.clEnqueueFillImage;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(fill_color, 16);
        BufferChecks.checkBuffer(origin, 3);
        BufferChecks.checkBuffer(region, 3);
        if (event_wait_list != null) {
            BufferChecks.checkDirect(event_wait_list);
        }
        if (event != null) {
            BufferChecks.checkBuffer(event, 1);
        }
        final int __result = nclEnqueueFillImage(command_queue.getPointer(), image.getPointer(), MemoryUtil.getAddress(fill_color), MemoryUtil.getAddress(origin), MemoryUtil.getAddress(region), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
        return __result;
    }
    
    static native int nclEnqueueFillImage(final long p0, final long p1, final long p2, final long p3, final long p4, final int p5, final long p6, final long p7, final long p8);
    
    public static int clEnqueueMigrateMemObjects(final CLCommandQueue command_queue, final PointerBuffer mem_objects, final long flags, final PointerBuffer event_wait_list, final PointerBuffer event) {
        final long function_pointer = CLCapabilities.clEnqueueMigrateMemObjects;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(mem_objects);
        if (event_wait_list != null) {
            BufferChecks.checkDirect(event_wait_list);
        }
        if (event != null) {
            BufferChecks.checkBuffer(event, 1);
        }
        final int __result = nclEnqueueMigrateMemObjects(command_queue.getPointer(), mem_objects.remaining(), MemoryUtil.getAddress(mem_objects), flags, (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
        return __result;
    }
    
    static native int nclEnqueueMigrateMemObjects(final long p0, final int p1, final long p2, final long p3, final int p4, final long p5, final long p6, final long p7);
    
    public static int clEnqueueMarkerWithWaitList(final CLCommandQueue command_queue, final PointerBuffer event_wait_list, final PointerBuffer event) {
        final long function_pointer = CLCapabilities.clEnqueueMarkerWithWaitList;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (event_wait_list != null) {
            BufferChecks.checkDirect(event_wait_list);
        }
        if (event != null) {
            BufferChecks.checkBuffer(event, 1);
        }
        final int __result = nclEnqueueMarkerWithWaitList(command_queue.getPointer(), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
        return __result;
    }
    
    static native int nclEnqueueMarkerWithWaitList(final long p0, final int p1, final long p2, final long p3, final long p4);
    
    public static int clEnqueueBarrierWithWaitList(final CLCommandQueue command_queue, final PointerBuffer event_wait_list, final PointerBuffer event) {
        final long function_pointer = CLCapabilities.clEnqueueBarrierWithWaitList;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (event_wait_list != null) {
            BufferChecks.checkDirect(event_wait_list);
        }
        if (event != null) {
            BufferChecks.checkBuffer(event, 1);
        }
        final int __result = nclEnqueueBarrierWithWaitList(command_queue.getPointer(), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
        return __result;
    }
    
    static native int nclEnqueueBarrierWithWaitList(final long p0, final int p1, final long p2, final long p3, final long p4);
    
    public static int clSetPrintfCallback(final CLContext context, final CLPrintfCallback pfn_notify) {
        final long function_pointer = CLCapabilities.clSetPrintfCallback;
        BufferChecks.checkFunctionAddress(function_pointer);
        final long user_data = CallbackUtil.createGlobalRef(pfn_notify);
        int __result = 0;
        try {
            __result = nclSetPrintfCallback(context.getPointer(), pfn_notify.getPointer(), user_data, function_pointer);
            return __result;
        }
        finally {
            context.setPrintfCallback(user_data, __result);
        }
    }
    
    static native int nclSetPrintfCallback(final long p0, final long p1, final long p2, final long p3);
    
    static CLFunctionAddress clGetExtensionFunctionAddressForPlatform(final CLPlatform platform, final ByteBuffer func_name) {
        final long function_pointer = CLCapabilities.clGetExtensionFunctionAddressForPlatform;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(func_name);
        BufferChecks.checkNullTerminated(func_name);
        final CLFunctionAddress __result = new CLFunctionAddress(nclGetExtensionFunctionAddressForPlatform(platform.getPointer(), MemoryUtil.getAddress(func_name), function_pointer));
        return __result;
    }
    
    static native long nclGetExtensionFunctionAddressForPlatform(final long p0, final long p1, final long p2);
    
    static CLFunctionAddress clGetExtensionFunctionAddressForPlatform(final CLPlatform platform, final CharSequence func_name) {
        final long function_pointer = CLCapabilities.clGetExtensionFunctionAddressForPlatform;
        BufferChecks.checkFunctionAddress(function_pointer);
        final CLFunctionAddress __result = new CLFunctionAddress(nclGetExtensionFunctionAddressForPlatform(platform.getPointer(), APIUtil.getBufferNT(func_name), function_pointer));
        return __result;
    }
}

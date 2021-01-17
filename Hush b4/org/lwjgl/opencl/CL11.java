// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

import java.nio.ShortBuffer;
import java.nio.LongBuffer;
import java.nio.FloatBuffer;
import java.nio.DoubleBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.PointerBuffer;
import org.lwjgl.BufferChecks;
import java.nio.IntBuffer;
import java.nio.ByteBuffer;

public final class CL11
{
    public static final int CL_MISALIGNED_SUB_BUFFER_OFFSET = -13;
    public static final int CL_EXEC_STATUS_ERROR_FOR_EVENTS_IN_WAIT_LIST = -14;
    public static final int CL_INVALID_PROPERTY = -64;
    public static final int CL_VERSION_1_1 = 1;
    public static final int CL_DEVICE_PREFERRED_VECTOR_WIDTH_HALF = 4148;
    public static final int CL_DEVICE_HOST_UNIFIED_MEMORY = 4149;
    public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_CHAR = 4150;
    public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_SHORT = 4151;
    public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_INT = 4152;
    public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_LONG = 4153;
    public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_FLOAT = 4154;
    public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_DOUBLE = 4155;
    public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_HALF = 4156;
    public static final int CL_DEVICE_OPENCL_C_VERSION = 4157;
    public static final int CL_FP_SOFT_FLOAT = 64;
    public static final int CL_CONTEXT_NUM_DEVICES = 4227;
    public static final int CL_Rx = 4282;
    public static final int CL_RGx = 4283;
    public static final int CL_RGBx = 4284;
    public static final int CL_MEM_ASSOCIATED_MEMOBJECT = 4359;
    public static final int CL_MEM_OFFSET = 4360;
    public static final int CL_ADDRESS_MIRRORED_REPEAT = 4404;
    public static final int CL_KERNEL_PREFERRED_WORK_GROUP_SIZE_MULTIPLE = 4531;
    public static final int CL_KERNEL_PRIVATE_MEM_SIZE = 4532;
    public static final int CL_EVENT_CONTEXT = 4564;
    public static final int CL_COMMAND_READ_BUFFER_RECT = 4609;
    public static final int CL_COMMAND_WRITE_BUFFER_RECT = 4610;
    public static final int CL_COMMAND_COPY_BUFFER_RECT = 4611;
    public static final int CL_COMMAND_USER = 4612;
    public static final int CL_BUFFER_CREATE_TYPE_REGION = 4640;
    
    private CL11() {
    }
    
    public static CLMem clCreateSubBuffer(final CLMem buffer, final long flags, final int buffer_create_type, final ByteBuffer buffer_create_info, final IntBuffer errcode_ret) {
        final long function_pointer = CLCapabilities.clCreateSubBuffer;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(buffer_create_info, 2 * PointerBuffer.getPointerSize());
        if (errcode_ret != null) {
            BufferChecks.checkBuffer(errcode_ret, 1);
        }
        final CLMem __result = CLMem.create(nclCreateSubBuffer(buffer.getPointer(), flags, buffer_create_type, MemoryUtil.getAddress(buffer_create_info), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), buffer.getParent());
        return __result;
    }
    
    static native long nclCreateSubBuffer(final long p0, final long p1, final int p2, final long p3, final long p4, final long p5);
    
    public static int clSetMemObjectDestructorCallback(final CLMem memobj, final CLMemObjectDestructorCallback pfn_notify) {
        final long function_pointer = CLCapabilities.clSetMemObjectDestructorCallback;
        BufferChecks.checkFunctionAddress(function_pointer);
        final long user_data = CallbackUtil.createGlobalRef(pfn_notify);
        int __result = 0;
        try {
            __result = nclSetMemObjectDestructorCallback(memobj.getPointer(), pfn_notify.getPointer(), user_data, function_pointer);
            return __result;
        }
        finally {
            CallbackUtil.checkCallback(__result, user_data);
        }
    }
    
    static native int nclSetMemObjectDestructorCallback(final long p0, final long p1, final long p2, final long p3);
    
    public static int clEnqueueReadBufferRect(final CLCommandQueue command_queue, final CLMem buffer, final int blocking_read, final PointerBuffer buffer_offset, final PointerBuffer host_offset, final PointerBuffer region, final long buffer_row_pitch, final long buffer_slice_pitch, final long host_row_pitch, final long host_slice_pitch, final ByteBuffer ptr, final PointerBuffer event_wait_list, final PointerBuffer event) {
        final long function_pointer = CLCapabilities.clEnqueueReadBufferRect;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(buffer_offset, 3);
        BufferChecks.checkBuffer(host_offset, 3);
        BufferChecks.checkBuffer(region, 3);
        BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
        if (event_wait_list != null) {
            BufferChecks.checkDirect(event_wait_list);
        }
        if (event != null) {
            BufferChecks.checkBuffer(event, 1);
        }
        final int __result = nclEnqueueReadBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_read, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
        if (__result == 0) {
            command_queue.registerCLEvent(event);
        }
        return __result;
    }
    
    public static int clEnqueueReadBufferRect(final CLCommandQueue command_queue, final CLMem buffer, final int blocking_read, final PointerBuffer buffer_offset, final PointerBuffer host_offset, final PointerBuffer region, final long buffer_row_pitch, final long buffer_slice_pitch, final long host_row_pitch, final long host_slice_pitch, final DoubleBuffer ptr, final PointerBuffer event_wait_list, final PointerBuffer event) {
        final long function_pointer = CLCapabilities.clEnqueueReadBufferRect;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(buffer_offset, 3);
        BufferChecks.checkBuffer(host_offset, 3);
        BufferChecks.checkBuffer(region, 3);
        BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
        if (event_wait_list != null) {
            BufferChecks.checkDirect(event_wait_list);
        }
        if (event != null) {
            BufferChecks.checkBuffer(event, 1);
        }
        final int __result = nclEnqueueReadBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_read, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
        if (__result == 0) {
            command_queue.registerCLEvent(event);
        }
        return __result;
    }
    
    public static int clEnqueueReadBufferRect(final CLCommandQueue command_queue, final CLMem buffer, final int blocking_read, final PointerBuffer buffer_offset, final PointerBuffer host_offset, final PointerBuffer region, final long buffer_row_pitch, final long buffer_slice_pitch, final long host_row_pitch, final long host_slice_pitch, final FloatBuffer ptr, final PointerBuffer event_wait_list, final PointerBuffer event) {
        final long function_pointer = CLCapabilities.clEnqueueReadBufferRect;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(buffer_offset, 3);
        BufferChecks.checkBuffer(host_offset, 3);
        BufferChecks.checkBuffer(region, 3);
        BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
        if (event_wait_list != null) {
            BufferChecks.checkDirect(event_wait_list);
        }
        if (event != null) {
            BufferChecks.checkBuffer(event, 1);
        }
        final int __result = nclEnqueueReadBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_read, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
        if (__result == 0) {
            command_queue.registerCLEvent(event);
        }
        return __result;
    }
    
    public static int clEnqueueReadBufferRect(final CLCommandQueue command_queue, final CLMem buffer, final int blocking_read, final PointerBuffer buffer_offset, final PointerBuffer host_offset, final PointerBuffer region, final long buffer_row_pitch, final long buffer_slice_pitch, final long host_row_pitch, final long host_slice_pitch, final IntBuffer ptr, final PointerBuffer event_wait_list, final PointerBuffer event) {
        final long function_pointer = CLCapabilities.clEnqueueReadBufferRect;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(buffer_offset, 3);
        BufferChecks.checkBuffer(host_offset, 3);
        BufferChecks.checkBuffer(region, 3);
        BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
        if (event_wait_list != null) {
            BufferChecks.checkDirect(event_wait_list);
        }
        if (event != null) {
            BufferChecks.checkBuffer(event, 1);
        }
        final int __result = nclEnqueueReadBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_read, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
        if (__result == 0) {
            command_queue.registerCLEvent(event);
        }
        return __result;
    }
    
    public static int clEnqueueReadBufferRect(final CLCommandQueue command_queue, final CLMem buffer, final int blocking_read, final PointerBuffer buffer_offset, final PointerBuffer host_offset, final PointerBuffer region, final long buffer_row_pitch, final long buffer_slice_pitch, final long host_row_pitch, final long host_slice_pitch, final LongBuffer ptr, final PointerBuffer event_wait_list, final PointerBuffer event) {
        final long function_pointer = CLCapabilities.clEnqueueReadBufferRect;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(buffer_offset, 3);
        BufferChecks.checkBuffer(host_offset, 3);
        BufferChecks.checkBuffer(region, 3);
        BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
        if (event_wait_list != null) {
            BufferChecks.checkDirect(event_wait_list);
        }
        if (event != null) {
            BufferChecks.checkBuffer(event, 1);
        }
        final int __result = nclEnqueueReadBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_read, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
        if (__result == 0) {
            command_queue.registerCLEvent(event);
        }
        return __result;
    }
    
    public static int clEnqueueReadBufferRect(final CLCommandQueue command_queue, final CLMem buffer, final int blocking_read, final PointerBuffer buffer_offset, final PointerBuffer host_offset, final PointerBuffer region, final long buffer_row_pitch, final long buffer_slice_pitch, final long host_row_pitch, final long host_slice_pitch, final ShortBuffer ptr, final PointerBuffer event_wait_list, final PointerBuffer event) {
        final long function_pointer = CLCapabilities.clEnqueueReadBufferRect;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(buffer_offset, 3);
        BufferChecks.checkBuffer(host_offset, 3);
        BufferChecks.checkBuffer(region, 3);
        BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
        if (event_wait_list != null) {
            BufferChecks.checkDirect(event_wait_list);
        }
        if (event != null) {
            BufferChecks.checkBuffer(event, 1);
        }
        final int __result = nclEnqueueReadBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_read, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
        if (__result == 0) {
            command_queue.registerCLEvent(event);
        }
        return __result;
    }
    
    static native int nclEnqueueReadBufferRect(final long p0, final long p1, final int p2, final long p3, final long p4, final long p5, final long p6, final long p7, final long p8, final long p9, final long p10, final int p11, final long p12, final long p13, final long p14);
    
    public static int clEnqueueWriteBufferRect(final CLCommandQueue command_queue, final CLMem buffer, final int blocking_write, final PointerBuffer buffer_offset, final PointerBuffer host_offset, final PointerBuffer region, final long buffer_row_pitch, final long buffer_slice_pitch, final long host_row_pitch, final long host_slice_pitch, final ByteBuffer ptr, final PointerBuffer event_wait_list, final PointerBuffer event) {
        final long function_pointer = CLCapabilities.clEnqueueWriteBufferRect;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(buffer_offset, 3);
        BufferChecks.checkBuffer(host_offset, 3);
        BufferChecks.checkBuffer(region, 3);
        BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
        if (event_wait_list != null) {
            BufferChecks.checkDirect(event_wait_list);
        }
        if (event != null) {
            BufferChecks.checkBuffer(event, 1);
        }
        final int __result = nclEnqueueWriteBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_write, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
        if (__result == 0) {
            command_queue.registerCLEvent(event);
        }
        return __result;
    }
    
    public static int clEnqueueWriteBufferRect(final CLCommandQueue command_queue, final CLMem buffer, final int blocking_write, final PointerBuffer buffer_offset, final PointerBuffer host_offset, final PointerBuffer region, final long buffer_row_pitch, final long buffer_slice_pitch, final long host_row_pitch, final long host_slice_pitch, final DoubleBuffer ptr, final PointerBuffer event_wait_list, final PointerBuffer event) {
        final long function_pointer = CLCapabilities.clEnqueueWriteBufferRect;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(buffer_offset, 3);
        BufferChecks.checkBuffer(host_offset, 3);
        BufferChecks.checkBuffer(region, 3);
        BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
        if (event_wait_list != null) {
            BufferChecks.checkDirect(event_wait_list);
        }
        if (event != null) {
            BufferChecks.checkBuffer(event, 1);
        }
        final int __result = nclEnqueueWriteBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_write, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
        if (__result == 0) {
            command_queue.registerCLEvent(event);
        }
        return __result;
    }
    
    public static int clEnqueueWriteBufferRect(final CLCommandQueue command_queue, final CLMem buffer, final int blocking_write, final PointerBuffer buffer_offset, final PointerBuffer host_offset, final PointerBuffer region, final long buffer_row_pitch, final long buffer_slice_pitch, final long host_row_pitch, final long host_slice_pitch, final FloatBuffer ptr, final PointerBuffer event_wait_list, final PointerBuffer event) {
        final long function_pointer = CLCapabilities.clEnqueueWriteBufferRect;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(buffer_offset, 3);
        BufferChecks.checkBuffer(host_offset, 3);
        BufferChecks.checkBuffer(region, 3);
        BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
        if (event_wait_list != null) {
            BufferChecks.checkDirect(event_wait_list);
        }
        if (event != null) {
            BufferChecks.checkBuffer(event, 1);
        }
        final int __result = nclEnqueueWriteBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_write, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
        if (__result == 0) {
            command_queue.registerCLEvent(event);
        }
        return __result;
    }
    
    public static int clEnqueueWriteBufferRect(final CLCommandQueue command_queue, final CLMem buffer, final int blocking_write, final PointerBuffer buffer_offset, final PointerBuffer host_offset, final PointerBuffer region, final long buffer_row_pitch, final long buffer_slice_pitch, final long host_row_pitch, final long host_slice_pitch, final IntBuffer ptr, final PointerBuffer event_wait_list, final PointerBuffer event) {
        final long function_pointer = CLCapabilities.clEnqueueWriteBufferRect;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(buffer_offset, 3);
        BufferChecks.checkBuffer(host_offset, 3);
        BufferChecks.checkBuffer(region, 3);
        BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
        if (event_wait_list != null) {
            BufferChecks.checkDirect(event_wait_list);
        }
        if (event != null) {
            BufferChecks.checkBuffer(event, 1);
        }
        final int __result = nclEnqueueWriteBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_write, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
        if (__result == 0) {
            command_queue.registerCLEvent(event);
        }
        return __result;
    }
    
    public static int clEnqueueWriteBufferRect(final CLCommandQueue command_queue, final CLMem buffer, final int blocking_write, final PointerBuffer buffer_offset, final PointerBuffer host_offset, final PointerBuffer region, final long buffer_row_pitch, final long buffer_slice_pitch, final long host_row_pitch, final long host_slice_pitch, final LongBuffer ptr, final PointerBuffer event_wait_list, final PointerBuffer event) {
        final long function_pointer = CLCapabilities.clEnqueueWriteBufferRect;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(buffer_offset, 3);
        BufferChecks.checkBuffer(host_offset, 3);
        BufferChecks.checkBuffer(region, 3);
        BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
        if (event_wait_list != null) {
            BufferChecks.checkDirect(event_wait_list);
        }
        if (event != null) {
            BufferChecks.checkBuffer(event, 1);
        }
        final int __result = nclEnqueueWriteBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_write, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
        if (__result == 0) {
            command_queue.registerCLEvent(event);
        }
        return __result;
    }
    
    public static int clEnqueueWriteBufferRect(final CLCommandQueue command_queue, final CLMem buffer, final int blocking_write, final PointerBuffer buffer_offset, final PointerBuffer host_offset, final PointerBuffer region, final long buffer_row_pitch, final long buffer_slice_pitch, final long host_row_pitch, final long host_slice_pitch, final ShortBuffer ptr, final PointerBuffer event_wait_list, final PointerBuffer event) {
        final long function_pointer = CLCapabilities.clEnqueueWriteBufferRect;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(buffer_offset, 3);
        BufferChecks.checkBuffer(host_offset, 3);
        BufferChecks.checkBuffer(region, 3);
        BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
        if (event_wait_list != null) {
            BufferChecks.checkDirect(event_wait_list);
        }
        if (event != null) {
            BufferChecks.checkBuffer(event, 1);
        }
        final int __result = nclEnqueueWriteBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_write, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
        if (__result == 0) {
            command_queue.registerCLEvent(event);
        }
        return __result;
    }
    
    static native int nclEnqueueWriteBufferRect(final long p0, final long p1, final int p2, final long p3, final long p4, final long p5, final long p6, final long p7, final long p8, final long p9, final long p10, final int p11, final long p12, final long p13, final long p14);
    
    public static int clEnqueueCopyBufferRect(final CLCommandQueue command_queue, final CLMem src_buffer, final CLMem dst_buffer, final PointerBuffer src_origin, final PointerBuffer dst_origin, final PointerBuffer region, final long src_row_pitch, final long src_slice_pitch, final long dst_row_pitch, final long dst_slice_pitch, final PointerBuffer event_wait_list, final PointerBuffer event) {
        final long function_pointer = CLCapabilities.clEnqueueCopyBufferRect;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(src_origin, 3);
        BufferChecks.checkBuffer(dst_origin, 3);
        BufferChecks.checkBuffer(region, 3);
        if (event_wait_list != null) {
            BufferChecks.checkDirect(event_wait_list);
        }
        if (event != null) {
            BufferChecks.checkBuffer(event, 1);
        }
        final int __result = nclEnqueueCopyBufferRect(command_queue.getPointer(), src_buffer.getPointer(), dst_buffer.getPointer(), MemoryUtil.getAddress(src_origin), MemoryUtil.getAddress(dst_origin), MemoryUtil.getAddress(region), src_row_pitch, src_slice_pitch, dst_row_pitch, dst_slice_pitch, (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
        if (__result == 0) {
            command_queue.registerCLEvent(event);
        }
        return __result;
    }
    
    static native int nclEnqueueCopyBufferRect(final long p0, final long p1, final long p2, final long p3, final long p4, final long p5, final long p6, final long p7, final long p8, final long p9, final int p10, final long p11, final long p12, final long p13);
    
    public static CLEvent clCreateUserEvent(final CLContext context, final IntBuffer errcode_ret) {
        final long function_pointer = CLCapabilities.clCreateUserEvent;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (errcode_ret != null) {
            BufferChecks.checkBuffer(errcode_ret, 1);
        }
        final CLEvent __result = new CLEvent(nclCreateUserEvent(context.getPointer(), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
        return __result;
    }
    
    static native long nclCreateUserEvent(final long p0, final long p1, final long p2);
    
    public static int clSetUserEventStatus(final CLEvent event, final int execution_status) {
        final long function_pointer = CLCapabilities.clSetUserEventStatus;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nclSetUserEventStatus(event.getPointer(), execution_status, function_pointer);
        return __result;
    }
    
    static native int nclSetUserEventStatus(final long p0, final int p1, final long p2);
    
    public static int clSetEventCallback(final CLEvent event, final int command_exec_callback_type, final CLEventCallback pfn_notify) {
        final long function_pointer = CLCapabilities.clSetEventCallback;
        BufferChecks.checkFunctionAddress(function_pointer);
        final long user_data = CallbackUtil.createGlobalRef(pfn_notify);
        pfn_notify.setRegistry(event.getParentRegistry());
        int __result = 0;
        try {
            __result = nclSetEventCallback(event.getPointer(), command_exec_callback_type, pfn_notify.getPointer(), user_data, function_pointer);
            return __result;
        }
        finally {
            CallbackUtil.checkCallback(__result, user_data);
        }
    }
    
    static native int nclSetEventCallback(final long p0, final int p1, final long p2, final long p3, final long p4);
}

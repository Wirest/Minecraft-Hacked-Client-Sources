// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

import org.lwjgl.PointerWrapper;
import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import org.lwjgl.PointerBuffer;

public final class EXTMigrateMemobject
{
    public static final int CL_MIGRATE_MEM_OBJECT_HOST_EXT = 1;
    public static final int CL_COMMAND_MIGRATE_MEM_OBJECT_EXT = 16448;
    
    private EXTMigrateMemobject() {
    }
    
    public static int clEnqueueMigrateMemObjectEXT(final CLCommandQueue command_queue, final PointerBuffer mem_objects, final long flags, final PointerBuffer event_wait_list, final PointerBuffer event) {
        final long function_pointer = CLCapabilities.clEnqueueMigrateMemObjectEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(mem_objects, 1);
        if (event_wait_list != null) {
            BufferChecks.checkDirect(event_wait_list);
        }
        if (event != null) {
            BufferChecks.checkBuffer(event, 1);
        }
        final int __result = nclEnqueueMigrateMemObjectEXT(command_queue.getPointer(), mem_objects.remaining(), MemoryUtil.getAddress(mem_objects), flags, (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
        if (__result == 0) {
            command_queue.registerCLEvent(event);
        }
        return __result;
    }
    
    static native int nclEnqueueMigrateMemObjectEXT(final long p0, final int p1, final long p2, final long p3, final int p4, final long p5, final long p6, final long p7);
    
    public static int clEnqueueMigrateMemObjectEXT(final CLCommandQueue command_queue, final CLMem mem_object, final long flags, final PointerBuffer event_wait_list, final PointerBuffer event) {
        final long function_pointer = CLCapabilities.clEnqueueMigrateMemObjectEXT;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (event_wait_list != null) {
            BufferChecks.checkDirect(event_wait_list);
        }
        if (event != null) {
            BufferChecks.checkBuffer(event, 1);
        }
        final int __result = nclEnqueueMigrateMemObjectEXT(command_queue.getPointer(), 1, APIUtil.getPointer(mem_object), flags, (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
        if (__result == 0) {
            command_queue.registerCLEvent(event);
        }
        return __result;
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

import org.lwjgl.PointerWrapper;
import org.lwjgl.PointerBuffer;
import java.nio.ByteBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.IntBuffer;

public final class CL10GL
{
    public static final int CL_GL_OBJECT_BUFFER = 8192;
    public static final int CL_GL_OBJECT_TEXTURE2D = 8193;
    public static final int CL_GL_OBJECT_TEXTURE3D = 8194;
    public static final int CL_GL_OBJECT_RENDERBUFFER = 8195;
    public static final int CL_GL_TEXTURE_TARGET = 8196;
    public static final int CL_GL_MIPMAP_LEVEL = 8197;
    
    private CL10GL() {
    }
    
    public static CLMem clCreateFromGLBuffer(final CLContext context, final long flags, final int bufobj, final IntBuffer errcode_ret) {
        final long function_pointer = CLCapabilities.clCreateFromGLBuffer;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (errcode_ret != null) {
            BufferChecks.checkBuffer(errcode_ret, 1);
        }
        final CLMem __result = new CLMem(nclCreateFromGLBuffer(context.getPointer(), flags, bufobj, MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
        return __result;
    }
    
    static native long nclCreateFromGLBuffer(final long p0, final long p1, final int p2, final long p3, final long p4);
    
    public static CLMem clCreateFromGLTexture2D(final CLContext context, final long flags, final int target, final int miplevel, final int texture, final IntBuffer errcode_ret) {
        final long function_pointer = CLCapabilities.clCreateFromGLTexture2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (errcode_ret != null) {
            BufferChecks.checkBuffer(errcode_ret, 1);
        }
        final CLMem __result = new CLMem(nclCreateFromGLTexture2D(context.getPointer(), flags, target, miplevel, texture, MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
        return __result;
    }
    
    static native long nclCreateFromGLTexture2D(final long p0, final long p1, final int p2, final int p3, final int p4, final long p5, final long p6);
    
    public static CLMem clCreateFromGLTexture3D(final CLContext context, final long flags, final int target, final int miplevel, final int texture, final IntBuffer errcode_ret) {
        final long function_pointer = CLCapabilities.clCreateFromGLTexture3D;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (errcode_ret != null) {
            BufferChecks.checkBuffer(errcode_ret, 1);
        }
        final CLMem __result = new CLMem(nclCreateFromGLTexture3D(context.getPointer(), flags, target, miplevel, texture, MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
        return __result;
    }
    
    static native long nclCreateFromGLTexture3D(final long p0, final long p1, final int p2, final int p3, final int p4, final long p5, final long p6);
    
    public static CLMem clCreateFromGLRenderbuffer(final CLContext context, final long flags, final int renderbuffer, final IntBuffer errcode_ret) {
        final long function_pointer = CLCapabilities.clCreateFromGLRenderbuffer;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (errcode_ret != null) {
            BufferChecks.checkBuffer(errcode_ret, 1);
        }
        final CLMem __result = new CLMem(nclCreateFromGLRenderbuffer(context.getPointer(), flags, renderbuffer, MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
        return __result;
    }
    
    static native long nclCreateFromGLRenderbuffer(final long p0, final long p1, final int p2, final long p3, final long p4);
    
    public static int clGetGLObjectInfo(final CLMem memobj, final IntBuffer gl_object_type, final IntBuffer gl_object_name) {
        final long function_pointer = CLCapabilities.clGetGLObjectInfo;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (gl_object_type != null) {
            BufferChecks.checkBuffer(gl_object_type, 1);
        }
        if (gl_object_name != null) {
            BufferChecks.checkBuffer(gl_object_name, 1);
        }
        final int __result = nclGetGLObjectInfo(memobj.getPointer(), MemoryUtil.getAddressSafe(gl_object_type), MemoryUtil.getAddressSafe(gl_object_name), function_pointer);
        return __result;
    }
    
    static native int nclGetGLObjectInfo(final long p0, final long p1, final long p2, final long p3);
    
    public static int clGetGLTextureInfo(final CLMem memobj, final int param_name, final ByteBuffer param_value, final PointerBuffer param_value_size_ret) {
        final long function_pointer = CLCapabilities.clGetGLTextureInfo;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (param_value != null) {
            BufferChecks.checkDirect(param_value);
        }
        if (param_value_size_ret != null) {
            BufferChecks.checkBuffer(param_value_size_ret, 1);
        }
        final int __result = nclGetGLTextureInfo(memobj.getPointer(), param_name, (param_value == null) ? 0 : param_value.remaining(), MemoryUtil.getAddressSafe(param_value), MemoryUtil.getAddressSafe(param_value_size_ret), function_pointer);
        return __result;
    }
    
    static native int nclGetGLTextureInfo(final long p0, final int p1, final long p2, final long p3, final long p4, final long p5);
    
    public static int clEnqueueAcquireGLObjects(final CLCommandQueue command_queue, final PointerBuffer mem_objects, final PointerBuffer event_wait_list, final PointerBuffer event) {
        final long function_pointer = CLCapabilities.clEnqueueAcquireGLObjects;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(mem_objects, 1);
        if (event_wait_list != null) {
            BufferChecks.checkDirect(event_wait_list);
        }
        if (event != null) {
            BufferChecks.checkBuffer(event, 1);
        }
        final int __result = nclEnqueueAcquireGLObjects(command_queue.getPointer(), mem_objects.remaining(), MemoryUtil.getAddress(mem_objects), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
        if (__result == 0) {
            command_queue.registerCLEvent(event);
        }
        return __result;
    }
    
    static native int nclEnqueueAcquireGLObjects(final long p0, final int p1, final long p2, final int p3, final long p4, final long p5, final long p6);
    
    public static int clEnqueueAcquireGLObjects(final CLCommandQueue command_queue, final CLMem mem_object, final PointerBuffer event_wait_list, final PointerBuffer event) {
        final long function_pointer = CLCapabilities.clEnqueueAcquireGLObjects;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (event_wait_list != null) {
            BufferChecks.checkDirect(event_wait_list);
        }
        if (event != null) {
            BufferChecks.checkBuffer(event, 1);
        }
        final int __result = nclEnqueueAcquireGLObjects(command_queue.getPointer(), 1, APIUtil.getPointer(mem_object), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
        if (__result == 0) {
            command_queue.registerCLEvent(event);
        }
        return __result;
    }
    
    public static int clEnqueueReleaseGLObjects(final CLCommandQueue command_queue, final PointerBuffer mem_objects, final PointerBuffer event_wait_list, final PointerBuffer event) {
        final long function_pointer = CLCapabilities.clEnqueueReleaseGLObjects;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(mem_objects, 1);
        if (event_wait_list != null) {
            BufferChecks.checkDirect(event_wait_list);
        }
        if (event != null) {
            BufferChecks.checkBuffer(event, 1);
        }
        final int __result = nclEnqueueReleaseGLObjects(command_queue.getPointer(), mem_objects.remaining(), MemoryUtil.getAddress(mem_objects), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
        if (__result == 0) {
            command_queue.registerCLEvent(event);
        }
        return __result;
    }
    
    static native int nclEnqueueReleaseGLObjects(final long p0, final int p1, final long p2, final int p3, final long p4, final long p5, final long p6);
    
    public static int clEnqueueReleaseGLObjects(final CLCommandQueue command_queue, final CLMem mem_object, final PointerBuffer event_wait_list, final PointerBuffer event) {
        final long function_pointer = CLCapabilities.clEnqueueReleaseGLObjects;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (event_wait_list != null) {
            BufferChecks.checkDirect(event_wait_list);
        }
        if (event != null) {
            BufferChecks.checkBuffer(event, 1);
        }
        final int __result = nclEnqueueReleaseGLObjects(command_queue.getPointer(), 1, APIUtil.getPointer(mem_object), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
        if (__result == 0) {
            command_queue.registerCLEvent(event);
        }
        return __result;
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

public final class CLCapabilities
{
    static final boolean CL_APPLE_ContextLoggingFunctions;
    static final long clLogMessagesToSystemLogAPPLE;
    static final long clLogMessagesToStdoutAPPLE;
    static final long clLogMessagesToStderrAPPLE;
    static final boolean CL_APPLE_SetMemObjectDestructor;
    static final long clSetMemObjectDestructorAPPLE;
    static final boolean CL_APPLE_gl_sharing;
    static final long clGetGLContextInfoAPPLE;
    static final boolean OpenCL10;
    static final long clGetPlatformIDs;
    static final long clGetPlatformInfo;
    static final long clGetDeviceIDs;
    static final long clGetDeviceInfo;
    static final long clCreateContext;
    static final long clCreateContextFromType;
    static final long clRetainContext;
    static final long clReleaseContext;
    static final long clGetContextInfo;
    static final long clCreateCommandQueue;
    static final long clRetainCommandQueue;
    static final long clReleaseCommandQueue;
    static final long clGetCommandQueueInfo;
    static final long clCreateBuffer;
    static final long clEnqueueReadBuffer;
    static final long clEnqueueWriteBuffer;
    static final long clEnqueueCopyBuffer;
    static final long clEnqueueMapBuffer;
    static final long clCreateImage2D;
    static final long clCreateImage3D;
    static final long clGetSupportedImageFormats;
    static final long clEnqueueReadImage;
    static final long clEnqueueWriteImage;
    static final long clEnqueueCopyImage;
    static final long clEnqueueCopyImageToBuffer;
    static final long clEnqueueCopyBufferToImage;
    static final long clEnqueueMapImage;
    static final long clGetImageInfo;
    static final long clRetainMemObject;
    static final long clReleaseMemObject;
    static final long clEnqueueUnmapMemObject;
    static final long clGetMemObjectInfo;
    static final long clCreateSampler;
    static final long clRetainSampler;
    static final long clReleaseSampler;
    static final long clGetSamplerInfo;
    static final long clCreateProgramWithSource;
    static final long clCreateProgramWithBinary;
    static final long clRetainProgram;
    static final long clReleaseProgram;
    static final long clBuildProgram;
    static final long clUnloadCompiler;
    static final long clGetProgramInfo;
    static final long clGetProgramBuildInfo;
    static final long clCreateKernel;
    static final long clCreateKernelsInProgram;
    static final long clRetainKernel;
    static final long clReleaseKernel;
    static final long clSetKernelArg;
    static final long clGetKernelInfo;
    static final long clGetKernelWorkGroupInfo;
    static final long clEnqueueNDRangeKernel;
    static final long clEnqueueTask;
    static final long clEnqueueNativeKernel;
    static final long clWaitForEvents;
    static final long clGetEventInfo;
    static final long clRetainEvent;
    static final long clReleaseEvent;
    static final long clEnqueueMarker;
    static final long clEnqueueBarrier;
    static final long clEnqueueWaitForEvents;
    static final long clGetEventProfilingInfo;
    static final long clFlush;
    static final long clFinish;
    static final long clGetExtensionFunctionAddress;
    static final boolean OpenCL10GL;
    static final long clCreateFromGLBuffer;
    static final long clCreateFromGLTexture2D;
    static final long clCreateFromGLTexture3D;
    static final long clCreateFromGLRenderbuffer;
    static final long clGetGLObjectInfo;
    static final long clGetGLTextureInfo;
    static final long clEnqueueAcquireGLObjects;
    static final long clEnqueueReleaseGLObjects;
    static final boolean OpenCL11;
    static final long clCreateSubBuffer;
    static final long clSetMemObjectDestructorCallback;
    static final long clEnqueueReadBufferRect;
    static final long clEnqueueWriteBufferRect;
    static final long clEnqueueCopyBufferRect;
    static final long clCreateUserEvent;
    static final long clSetUserEventStatus;
    static final long clSetEventCallback;
    static final boolean OpenCL12;
    static final long clRetainDevice;
    static final long clReleaseDevice;
    static final long clCreateSubDevices;
    static final long clCreateImage;
    static final long clCreateProgramWithBuiltInKernels;
    static final long clCompileProgram;
    static final long clLinkProgram;
    static final long clUnloadPlatformCompiler;
    static final long clGetKernelArgInfo;
    static final long clEnqueueFillBuffer;
    static final long clEnqueueFillImage;
    static final long clEnqueueMigrateMemObjects;
    static final long clEnqueueMarkerWithWaitList;
    static final long clEnqueueBarrierWithWaitList;
    static final long clSetPrintfCallback;
    static final long clGetExtensionFunctionAddressForPlatform;
    static final boolean OpenCL12GL;
    static final long clCreateFromGLTexture;
    static final boolean CL_EXT_device_fission;
    static final long clRetainDeviceEXT;
    static final long clReleaseDeviceEXT;
    static final long clCreateSubDevicesEXT;
    static final boolean CL_EXT_migrate_memobject;
    static final long clEnqueueMigrateMemObjectEXT;
    static final boolean CL_KHR_gl_event;
    static final long clCreateEventFromGLsyncKHR;
    static final boolean CL_KHR_gl_sharing;
    static final long clGetGLContextInfoKHR;
    static final boolean CL_KHR_icd;
    static final long clIcdGetPlatformIDsKHR;
    static final boolean CL_KHR_subgroups;
    static final long clGetKernelSubGroupInfoKHR;
    static final boolean CL_KHR_terminate_context;
    static final long clTerminateContextKHR;
    
    private CLCapabilities() {
    }
    
    public static CLPlatformCapabilities getPlatformCapabilities(final CLPlatform platform) {
        platform.checkValid();
        CLPlatformCapabilities caps = (CLPlatformCapabilities)platform.getCapabilities();
        if (caps == null) {
            platform.setCapabilities(caps = new CLPlatformCapabilities(platform));
        }
        return caps;
    }
    
    public static CLDeviceCapabilities getDeviceCapabilities(final CLDevice device) {
        device.checkValid();
        CLDeviceCapabilities caps = (CLDeviceCapabilities)device.getCapabilities();
        if (caps == null) {
            device.setCapabilities(caps = new CLDeviceCapabilities(device));
        }
        return caps;
    }
    
    private static boolean isAPPLE_ContextLoggingFunctionsSupported() {
        return CLCapabilities.clLogMessagesToSystemLogAPPLE != 0L & CLCapabilities.clLogMessagesToStdoutAPPLE != 0L & CLCapabilities.clLogMessagesToStderrAPPLE != 0L;
    }
    
    private static boolean isAPPLE_SetMemObjectDestructorSupported() {
        return CLCapabilities.clSetMemObjectDestructorAPPLE != 0L;
    }
    
    private static boolean isAPPLE_gl_sharingSupported() {
        return CLCapabilities.clGetGLContextInfoAPPLE != 0L;
    }
    
    private static boolean isCL10Supported() {
        return CLCapabilities.clGetPlatformIDs != 0L & CLCapabilities.clGetPlatformInfo != 0L & CLCapabilities.clGetDeviceIDs != 0L & CLCapabilities.clGetDeviceInfo != 0L & CLCapabilities.clCreateContext != 0L & CLCapabilities.clCreateContextFromType != 0L & CLCapabilities.clRetainContext != 0L & CLCapabilities.clReleaseContext != 0L & CLCapabilities.clGetContextInfo != 0L & CLCapabilities.clCreateCommandQueue != 0L & CLCapabilities.clRetainCommandQueue != 0L & CLCapabilities.clReleaseCommandQueue != 0L & CLCapabilities.clGetCommandQueueInfo != 0L & CLCapabilities.clCreateBuffer != 0L & CLCapabilities.clEnqueueReadBuffer != 0L & CLCapabilities.clEnqueueWriteBuffer != 0L & CLCapabilities.clEnqueueCopyBuffer != 0L & CLCapabilities.clEnqueueMapBuffer != 0L & CLCapabilities.clCreateImage2D != 0L & CLCapabilities.clCreateImage3D != 0L & CLCapabilities.clGetSupportedImageFormats != 0L & CLCapabilities.clEnqueueReadImage != 0L & CLCapabilities.clEnqueueWriteImage != 0L & CLCapabilities.clEnqueueCopyImage != 0L & CLCapabilities.clEnqueueCopyImageToBuffer != 0L & CLCapabilities.clEnqueueCopyBufferToImage != 0L & CLCapabilities.clEnqueueMapImage != 0L & CLCapabilities.clGetImageInfo != 0L & CLCapabilities.clRetainMemObject != 0L & CLCapabilities.clReleaseMemObject != 0L & CLCapabilities.clEnqueueUnmapMemObject != 0L & CLCapabilities.clGetMemObjectInfo != 0L & CLCapabilities.clCreateSampler != 0L & CLCapabilities.clRetainSampler != 0L & CLCapabilities.clReleaseSampler != 0L & CLCapabilities.clGetSamplerInfo != 0L & CLCapabilities.clCreateProgramWithSource != 0L & CLCapabilities.clCreateProgramWithBinary != 0L & CLCapabilities.clRetainProgram != 0L & CLCapabilities.clReleaseProgram != 0L & CLCapabilities.clBuildProgram != 0L & CLCapabilities.clUnloadCompiler != 0L & CLCapabilities.clGetProgramInfo != 0L & CLCapabilities.clGetProgramBuildInfo != 0L & CLCapabilities.clCreateKernel != 0L & CLCapabilities.clCreateKernelsInProgram != 0L & CLCapabilities.clRetainKernel != 0L & CLCapabilities.clReleaseKernel != 0L & CLCapabilities.clSetKernelArg != 0L & CLCapabilities.clGetKernelInfo != 0L & CLCapabilities.clGetKernelWorkGroupInfo != 0L & CLCapabilities.clEnqueueNDRangeKernel != 0L & CLCapabilities.clEnqueueTask != 0L & CLCapabilities.clEnqueueNativeKernel != 0L & CLCapabilities.clWaitForEvents != 0L & CLCapabilities.clGetEventInfo != 0L & CLCapabilities.clRetainEvent != 0L & CLCapabilities.clReleaseEvent != 0L & CLCapabilities.clEnqueueMarker != 0L & CLCapabilities.clEnqueueBarrier != 0L & CLCapabilities.clEnqueueWaitForEvents != 0L & CLCapabilities.clGetEventProfilingInfo != 0L & CLCapabilities.clFlush != 0L & CLCapabilities.clFinish != 0L & CLCapabilities.clGetExtensionFunctionAddress != 0L;
    }
    
    private static boolean isCL10GLSupported() {
        return CLCapabilities.clCreateFromGLBuffer != 0L & CLCapabilities.clCreateFromGLTexture2D != 0L & CLCapabilities.clCreateFromGLTexture3D != 0L & CLCapabilities.clCreateFromGLRenderbuffer != 0L & CLCapabilities.clGetGLObjectInfo != 0L & CLCapabilities.clGetGLTextureInfo != 0L & CLCapabilities.clEnqueueAcquireGLObjects != 0L & CLCapabilities.clEnqueueReleaseGLObjects != 0L;
    }
    
    private static boolean isCL11Supported() {
        return CLCapabilities.clCreateSubBuffer != 0L & CLCapabilities.clSetMemObjectDestructorCallback != 0L & CLCapabilities.clEnqueueReadBufferRect != 0L & CLCapabilities.clEnqueueWriteBufferRect != 0L & CLCapabilities.clEnqueueCopyBufferRect != 0L & CLCapabilities.clCreateUserEvent != 0L & CLCapabilities.clSetUserEventStatus != 0L & CLCapabilities.clSetEventCallback != 0L;
    }
    
    private static boolean isCL12Supported() {
        final boolean b = CLCapabilities.clRetainDevice != 0L & CLCapabilities.clReleaseDevice != 0L & CLCapabilities.clCreateSubDevices != 0L & CLCapabilities.clCreateImage != 0L & CLCapabilities.clCreateProgramWithBuiltInKernels != 0L & CLCapabilities.clCompileProgram != 0L & CLCapabilities.clLinkProgram != 0L & CLCapabilities.clUnloadPlatformCompiler != 0L & CLCapabilities.clGetKernelArgInfo != 0L & CLCapabilities.clEnqueueFillBuffer != 0L & CLCapabilities.clEnqueueFillImage != 0L & CLCapabilities.clEnqueueMigrateMemObjects != 0L & CLCapabilities.clEnqueueMarkerWithWaitList != 0L & CLCapabilities.clEnqueueBarrierWithWaitList != 0L;
        if (CLCapabilities.clSetPrintfCallback == 0L) {}
        return b & true & CLCapabilities.clGetExtensionFunctionAddressForPlatform != 0L;
    }
    
    private static boolean isCL12GLSupported() {
        return CLCapabilities.clCreateFromGLTexture != 0L;
    }
    
    private static boolean isEXT_device_fissionSupported() {
        return CLCapabilities.clRetainDeviceEXT != 0L & CLCapabilities.clReleaseDeviceEXT != 0L & CLCapabilities.clCreateSubDevicesEXT != 0L;
    }
    
    private static boolean isEXT_migrate_memobjectSupported() {
        return CLCapabilities.clEnqueueMigrateMemObjectEXT != 0L;
    }
    
    private static boolean isKHR_gl_eventSupported() {
        return CLCapabilities.clCreateEventFromGLsyncKHR != 0L;
    }
    
    private static boolean isKHR_gl_sharingSupported() {
        return CLCapabilities.clGetGLContextInfoKHR != 0L;
    }
    
    private static boolean isKHR_icdSupported() {
        if (CLCapabilities.clIcdGetPlatformIDsKHR == 0L) {}
        return true;
    }
    
    private static boolean isKHR_subgroupsSupported() {
        return CLCapabilities.clGetKernelSubGroupInfoKHR != 0L;
    }
    
    private static boolean isKHR_terminate_contextSupported() {
        return CLCapabilities.clTerminateContextKHR != 0L;
    }
    
    static {
        clLogMessagesToSystemLogAPPLE = CL.getFunctionAddress("clLogMessagesToSystemLogAPPLE");
        clLogMessagesToStdoutAPPLE = CL.getFunctionAddress("clLogMessagesToStdoutAPPLE");
        clLogMessagesToStderrAPPLE = CL.getFunctionAddress("clLogMessagesToStderrAPPLE");
        clSetMemObjectDestructorAPPLE = CL.getFunctionAddress("clSetMemObjectDestructorAPPLE");
        clGetGLContextInfoAPPLE = CL.getFunctionAddress("clGetGLContextInfoAPPLE");
        clGetPlatformIDs = CL.getFunctionAddress("clGetPlatformIDs");
        clGetPlatformInfo = CL.getFunctionAddress("clGetPlatformInfo");
        clGetDeviceIDs = CL.getFunctionAddress("clGetDeviceIDs");
        clGetDeviceInfo = CL.getFunctionAddress("clGetDeviceInfo");
        clCreateContext = CL.getFunctionAddress("clCreateContext");
        clCreateContextFromType = CL.getFunctionAddress("clCreateContextFromType");
        clRetainContext = CL.getFunctionAddress("clRetainContext");
        clReleaseContext = CL.getFunctionAddress("clReleaseContext");
        clGetContextInfo = CL.getFunctionAddress("clGetContextInfo");
        clCreateCommandQueue = CL.getFunctionAddress("clCreateCommandQueue");
        clRetainCommandQueue = CL.getFunctionAddress("clRetainCommandQueue");
        clReleaseCommandQueue = CL.getFunctionAddress("clReleaseCommandQueue");
        clGetCommandQueueInfo = CL.getFunctionAddress("clGetCommandQueueInfo");
        clCreateBuffer = CL.getFunctionAddress("clCreateBuffer");
        clEnqueueReadBuffer = CL.getFunctionAddress("clEnqueueReadBuffer");
        clEnqueueWriteBuffer = CL.getFunctionAddress("clEnqueueWriteBuffer");
        clEnqueueCopyBuffer = CL.getFunctionAddress("clEnqueueCopyBuffer");
        clEnqueueMapBuffer = CL.getFunctionAddress("clEnqueueMapBuffer");
        clCreateImage2D = CL.getFunctionAddress("clCreateImage2D");
        clCreateImage3D = CL.getFunctionAddress("clCreateImage3D");
        clGetSupportedImageFormats = CL.getFunctionAddress("clGetSupportedImageFormats");
        clEnqueueReadImage = CL.getFunctionAddress("clEnqueueReadImage");
        clEnqueueWriteImage = CL.getFunctionAddress("clEnqueueWriteImage");
        clEnqueueCopyImage = CL.getFunctionAddress("clEnqueueCopyImage");
        clEnqueueCopyImageToBuffer = CL.getFunctionAddress("clEnqueueCopyImageToBuffer");
        clEnqueueCopyBufferToImage = CL.getFunctionAddress("clEnqueueCopyBufferToImage");
        clEnqueueMapImage = CL.getFunctionAddress("clEnqueueMapImage");
        clGetImageInfo = CL.getFunctionAddress("clGetImageInfo");
        clRetainMemObject = CL.getFunctionAddress("clRetainMemObject");
        clReleaseMemObject = CL.getFunctionAddress("clReleaseMemObject");
        clEnqueueUnmapMemObject = CL.getFunctionAddress("clEnqueueUnmapMemObject");
        clGetMemObjectInfo = CL.getFunctionAddress("clGetMemObjectInfo");
        clCreateSampler = CL.getFunctionAddress("clCreateSampler");
        clRetainSampler = CL.getFunctionAddress("clRetainSampler");
        clReleaseSampler = CL.getFunctionAddress("clReleaseSampler");
        clGetSamplerInfo = CL.getFunctionAddress("clGetSamplerInfo");
        clCreateProgramWithSource = CL.getFunctionAddress("clCreateProgramWithSource");
        clCreateProgramWithBinary = CL.getFunctionAddress("clCreateProgramWithBinary");
        clRetainProgram = CL.getFunctionAddress("clRetainProgram");
        clReleaseProgram = CL.getFunctionAddress("clReleaseProgram");
        clBuildProgram = CL.getFunctionAddress("clBuildProgram");
        clUnloadCompiler = CL.getFunctionAddress("clUnloadCompiler");
        clGetProgramInfo = CL.getFunctionAddress("clGetProgramInfo");
        clGetProgramBuildInfo = CL.getFunctionAddress("clGetProgramBuildInfo");
        clCreateKernel = CL.getFunctionAddress("clCreateKernel");
        clCreateKernelsInProgram = CL.getFunctionAddress("clCreateKernelsInProgram");
        clRetainKernel = CL.getFunctionAddress("clRetainKernel");
        clReleaseKernel = CL.getFunctionAddress("clReleaseKernel");
        clSetKernelArg = CL.getFunctionAddress("clSetKernelArg");
        clGetKernelInfo = CL.getFunctionAddress("clGetKernelInfo");
        clGetKernelWorkGroupInfo = CL.getFunctionAddress("clGetKernelWorkGroupInfo");
        clEnqueueNDRangeKernel = CL.getFunctionAddress("clEnqueueNDRangeKernel");
        clEnqueueTask = CL.getFunctionAddress("clEnqueueTask");
        clEnqueueNativeKernel = CL.getFunctionAddress("clEnqueueNativeKernel");
        clWaitForEvents = CL.getFunctionAddress("clWaitForEvents");
        clGetEventInfo = CL.getFunctionAddress("clGetEventInfo");
        clRetainEvent = CL.getFunctionAddress("clRetainEvent");
        clReleaseEvent = CL.getFunctionAddress("clReleaseEvent");
        clEnqueueMarker = CL.getFunctionAddress("clEnqueueMarker");
        clEnqueueBarrier = CL.getFunctionAddress("clEnqueueBarrier");
        clEnqueueWaitForEvents = CL.getFunctionAddress("clEnqueueWaitForEvents");
        clGetEventProfilingInfo = CL.getFunctionAddress("clGetEventProfilingInfo");
        clFlush = CL.getFunctionAddress("clFlush");
        clFinish = CL.getFunctionAddress("clFinish");
        clGetExtensionFunctionAddress = CL.getFunctionAddress("clGetExtensionFunctionAddress");
        clCreateFromGLBuffer = CL.getFunctionAddress("clCreateFromGLBuffer");
        clCreateFromGLTexture2D = CL.getFunctionAddress("clCreateFromGLTexture2D");
        clCreateFromGLTexture3D = CL.getFunctionAddress("clCreateFromGLTexture3D");
        clCreateFromGLRenderbuffer = CL.getFunctionAddress("clCreateFromGLRenderbuffer");
        clGetGLObjectInfo = CL.getFunctionAddress("clGetGLObjectInfo");
        clGetGLTextureInfo = CL.getFunctionAddress("clGetGLTextureInfo");
        clEnqueueAcquireGLObjects = CL.getFunctionAddress("clEnqueueAcquireGLObjects");
        clEnqueueReleaseGLObjects = CL.getFunctionAddress("clEnqueueReleaseGLObjects");
        clCreateSubBuffer = CL.getFunctionAddress("clCreateSubBuffer");
        clSetMemObjectDestructorCallback = CL.getFunctionAddress("clSetMemObjectDestructorCallback");
        clEnqueueReadBufferRect = CL.getFunctionAddress("clEnqueueReadBufferRect");
        clEnqueueWriteBufferRect = CL.getFunctionAddress("clEnqueueWriteBufferRect");
        clEnqueueCopyBufferRect = CL.getFunctionAddress("clEnqueueCopyBufferRect");
        clCreateUserEvent = CL.getFunctionAddress("clCreateUserEvent");
        clSetUserEventStatus = CL.getFunctionAddress("clSetUserEventStatus");
        clSetEventCallback = CL.getFunctionAddress("clSetEventCallback");
        clRetainDevice = CL.getFunctionAddress("clRetainDevice");
        clReleaseDevice = CL.getFunctionAddress("clReleaseDevice");
        clCreateSubDevices = CL.getFunctionAddress("clCreateSubDevices");
        clCreateImage = CL.getFunctionAddress("clCreateImage");
        clCreateProgramWithBuiltInKernels = CL.getFunctionAddress("clCreateProgramWithBuiltInKernels");
        clCompileProgram = CL.getFunctionAddress("clCompileProgram");
        clLinkProgram = CL.getFunctionAddress("clLinkProgram");
        clUnloadPlatformCompiler = CL.getFunctionAddress("clUnloadPlatformCompiler");
        clGetKernelArgInfo = CL.getFunctionAddress("clGetKernelArgInfo");
        clEnqueueFillBuffer = CL.getFunctionAddress("clEnqueueFillBuffer");
        clEnqueueFillImage = CL.getFunctionAddress("clEnqueueFillImage");
        clEnqueueMigrateMemObjects = CL.getFunctionAddress("clEnqueueMigrateMemObjects");
        clEnqueueMarkerWithWaitList = CL.getFunctionAddress("clEnqueueMarkerWithWaitList");
        clEnqueueBarrierWithWaitList = CL.getFunctionAddress("clEnqueueBarrierWithWaitList");
        clSetPrintfCallback = CL.getFunctionAddress("clSetPrintfCallback");
        clGetExtensionFunctionAddressForPlatform = CL.getFunctionAddress("clGetExtensionFunctionAddressForPlatform");
        clCreateFromGLTexture = CL.getFunctionAddress("clCreateFromGLTexture");
        clRetainDeviceEXT = CL.getFunctionAddress("clRetainDeviceEXT");
        clReleaseDeviceEXT = CL.getFunctionAddress("clReleaseDeviceEXT");
        clCreateSubDevicesEXT = CL.getFunctionAddress("clCreateSubDevicesEXT");
        clEnqueueMigrateMemObjectEXT = CL.getFunctionAddress("clEnqueueMigrateMemObjectEXT");
        clCreateEventFromGLsyncKHR = CL.getFunctionAddress("clCreateEventFromGLsyncKHR");
        clGetGLContextInfoKHR = CL.getFunctionAddress("clGetGLContextInfoKHR");
        clIcdGetPlatformIDsKHR = CL.getFunctionAddress("clIcdGetPlatformIDsKHR");
        clGetKernelSubGroupInfoKHR = CL.getFunctionAddress("clGetKernelSubGroupInfoKHR");
        clTerminateContextKHR = CL.getFunctionAddress("clTerminateContextKHR");
        CL_APPLE_ContextLoggingFunctions = isAPPLE_ContextLoggingFunctionsSupported();
        CL_APPLE_SetMemObjectDestructor = isAPPLE_SetMemObjectDestructorSupported();
        CL_APPLE_gl_sharing = isAPPLE_gl_sharingSupported();
        OpenCL10 = isCL10Supported();
        OpenCL10GL = isCL10GLSupported();
        OpenCL11 = isCL11Supported();
        OpenCL12 = isCL12Supported();
        OpenCL12GL = isCL12GLSupported();
        CL_EXT_device_fission = isEXT_device_fissionSupported();
        CL_EXT_migrate_memobject = isEXT_migrate_memobjectSupported();
        CL_KHR_gl_event = isKHR_gl_eventSupported();
        CL_KHR_gl_sharing = isKHR_gl_sharingSupported();
        CL_KHR_icd = isKHR_icdSupported();
        CL_KHR_subgroups = isKHR_subgroupsSupported();
        CL_KHR_terminate_context = isKHR_terminate_contextSupported();
    }
}

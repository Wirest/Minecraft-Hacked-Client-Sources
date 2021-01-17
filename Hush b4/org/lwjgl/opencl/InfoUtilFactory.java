// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

import org.lwjgl.opencl.api.CLBufferRegion;
import org.lwjgl.BufferUtils;
import org.lwjgl.opencl.api.CLImageFormat;
import org.lwjgl.opencl.api.Filter;
import org.lwjgl.LWJGLException;
import java.util.Iterator;
import java.nio.Buffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.BufferChecks;
import org.lwjgl.PointerWrapper;
import java.nio.IntBuffer;
import org.lwjgl.opengl.Drawable;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.PointerBuffer;
import java.nio.ByteBuffer;

final class InfoUtilFactory
{
    static final InfoUtil<CLCommandQueue> CL_COMMAND_QUEUE_UTIL;
    static final CLContext.CLContextUtil CL_CONTEXT_UTIL;
    static final InfoUtil<CLDevice> CL_DEVICE_UTIL;
    static final CLEvent.CLEventUtil CL_EVENT_UTIL;
    static final CLKernel.CLKernelUtil CL_KERNEL_UTIL;
    static final CLMem.CLMemUtil CL_MEM_UTIL;
    static final CLPlatform.CLPlatformUtil CL_PLATFORM_UTIL;
    static final CLProgram.CLProgramUtil CL_PROGRAM_UTIL;
    static final InfoUtil<CLSampler> CL_SAMPLER_UTIL;
    
    private InfoUtilFactory() {
    }
    
    static {
        CL_COMMAND_QUEUE_UTIL = new InfoUtilAbstract<CLCommandQueue>() {
            @Override
            protected int getInfo(final CLCommandQueue object, final int param_name, final ByteBuffer param_value, final PointerBuffer param_value_size_ret) {
                return CL10.clGetCommandQueueInfo(object, param_name, param_value, null);
            }
        };
        CL_CONTEXT_UTIL = new CLContextUtil();
        CL_DEVICE_UTIL = new CLDeviceUtil();
        CL_EVENT_UTIL = new CLEventUtil();
        CL_KERNEL_UTIL = new CLKernelUtil();
        CL_MEM_UTIL = new CLMemUtil();
        CL_PLATFORM_UTIL = new CLPlatformUtil();
        CL_PROGRAM_UTIL = new CLProgramUtil();
        CL_SAMPLER_UTIL = new InfoUtilAbstract<CLSampler>() {
            @Override
            protected int getInfo(final CLSampler sampler, final int param_name, final ByteBuffer param_value, final PointerBuffer param_value_size_ret) {
                return CL10.clGetSamplerInfo(sampler, param_name, param_value, param_value_size_ret);
            }
        };
    }
    
    private static final class CLContextUtil extends InfoUtilAbstract<CLContext> implements CLContext.CLContextUtil
    {
        @Override
        protected int getInfo(final CLContext context, final int param_name, final ByteBuffer param_value, final PointerBuffer param_value_size_ret) {
            return CL10.clGetContextInfo(context, param_name, param_value, param_value_size_ret);
        }
        
        public List<CLDevice> getInfoDevices(final CLContext context) {
            context.checkValid();
            int num_devices;
            if (CLCapabilities.getPlatformCapabilities(context.getParent()).OpenCL11) {
                num_devices = this.getInfoInt(context, 4227);
            }
            else {
                final PointerBuffer size_ret = APIUtil.getBufferPointer();
                CL10.clGetContextInfo(context, 4225, null, size_ret);
                num_devices = (int)(size_ret.get(0) / PointerBuffer.getPointerSize());
            }
            final PointerBuffer deviceIDs = APIUtil.getBufferPointer(num_devices);
            CL10.clGetContextInfo(context, 4225, deviceIDs.getBuffer(), null);
            final List<CLDevice> devices = new ArrayList<CLDevice>(num_devices);
            for (int i = 0; i < num_devices; ++i) {
                devices.add(context.getParent().getCLDevice(deviceIDs.get(i)));
            }
            return (devices.size() == 0) ? null : devices;
        }
        
        public CLContext create(final CLPlatform platform, final List<CLDevice> devices, final CLContextCallback pfn_notify, final Drawable share_drawable, IntBuffer errcode_ret) throws LWJGLException {
            final int propertyCount = 2 + ((share_drawable == null) ? 0 : 4) + 1;
            final PointerBuffer properties = APIUtil.getBufferPointer(propertyCount + devices.size());
            properties.put(4228L).put(platform);
            if (share_drawable != null) {
                share_drawable.setCLSharingProperties(properties);
            }
            properties.put(0L);
            properties.position(propertyCount);
            for (final CLDevice device : devices) {
                properties.put(device);
            }
            final long function_pointer = CLCapabilities.clCreateContext;
            BufferChecks.checkFunctionAddress(function_pointer);
            if (errcode_ret != null) {
                BufferChecks.checkBuffer(errcode_ret, 1);
            }
            else if (LWJGLUtil.DEBUG) {
                errcode_ret = APIUtil.getBufferInt();
            }
            final long user_data = (pfn_notify == null || pfn_notify.isCustom()) ? 0L : CallbackUtil.createGlobalRef(pfn_notify);
            CLContext __result = null;
            try {
                __result = new CLContext(CL10.nclCreateContext(MemoryUtil.getAddress0(properties.getBuffer()), devices.size(), MemoryUtil.getAddress(properties, propertyCount), (pfn_notify == null) ? 0L : pfn_notify.getPointer(), user_data, MemoryUtil.getAddressSafe(errcode_ret), function_pointer), platform);
                if (LWJGLUtil.DEBUG) {
                    Util.checkCLError(errcode_ret.get(0));
                }
                return __result;
            }
            finally {
                if (__result != null) {
                    __result.setContextCallback(user_data);
                }
            }
        }
        
        public CLContext createFromType(final CLPlatform platform, final long device_type, final CLContextCallback pfn_notify, final Drawable share_drawable, final IntBuffer errcode_ret) throws LWJGLException {
            final int propertyCount = 2 + ((share_drawable == null) ? 0 : 4) + 1;
            final PointerBuffer properties = APIUtil.getBufferPointer(propertyCount);
            properties.put(4228L).put(platform);
            if (share_drawable != null) {
                share_drawable.setCLSharingProperties(properties);
            }
            properties.put(0L);
            properties.flip();
            return CL10.clCreateContextFromType(properties, device_type, pfn_notify, errcode_ret);
        }
        
        public List<CLImageFormat> getSupportedImageFormats(final CLContext context, final long flags, final int image_type, final Filter<CLImageFormat> filter) {
            final IntBuffer numBuffer = APIUtil.getBufferInt();
            CL10.clGetSupportedImageFormats(context, flags, image_type, null, numBuffer);
            final int num_image_formats = numBuffer.get(0);
            if (num_image_formats == 0) {
                return null;
            }
            final ByteBuffer formatBuffer = BufferUtils.createByteBuffer(num_image_formats * 8);
            CL10.clGetSupportedImageFormats(context, flags, image_type, formatBuffer, null);
            final List<CLImageFormat> formats = new ArrayList<CLImageFormat>(num_image_formats);
            for (int i = 0; i < num_image_formats; ++i) {
                final int offset = num_image_formats * 8;
                final CLImageFormat format = new CLImageFormat(formatBuffer.getInt(offset), formatBuffer.getInt(offset + 4));
                if (filter == null || filter.accept(format)) {
                    formats.add(format);
                }
            }
            return (formats.size() == 0) ? null : formats;
        }
    }
    
    private static final class CLDeviceUtil extends InfoUtilAbstract<CLDevice>
    {
        @Override
        protected int getInfo(final CLDevice device, final int param_name, final ByteBuffer param_value, final PointerBuffer param_value_size_ret) {
            return CL10.clGetDeviceInfo(device, param_name, param_value, param_value_size_ret);
        }
        
        @Override
        protected int getInfoSizeArraySize(final CLDevice device, final int param_name) {
            switch (param_name) {
                case 4101: {
                    return this.getInfoInt(device, 4099);
                }
                default: {
                    throw new IllegalArgumentException("Unsupported parameter: " + LWJGLUtil.toHexString(param_name));
                }
            }
        }
    }
    
    private static final class CLEventUtil extends InfoUtilAbstract<CLEvent> implements CLEvent.CLEventUtil
    {
        @Override
        protected int getInfo(final CLEvent event, final int param_name, final ByteBuffer param_value, final PointerBuffer param_value_size_ret) {
            return CL10.clGetEventInfo(event, param_name, param_value, param_value_size_ret);
        }
        
        public long getProfilingInfoLong(final CLEvent event, final int param_name) {
            event.checkValid();
            final ByteBuffer buffer = APIUtil.getBufferByte(8);
            CL10.clGetEventProfilingInfo(event, param_name, buffer, null);
            return buffer.getLong(0);
        }
    }
    
    private static final class CLKernelUtil extends InfoUtilAbstract<CLKernel> implements CLKernel.CLKernelUtil
    {
        public void setArg(final CLKernel kernel, final int index, final byte value) {
            CL10.clSetKernelArg(kernel, index, 1L, APIUtil.getBufferByte(1).put(0, value));
        }
        
        public void setArg(final CLKernel kernel, final int index, final short value) {
            CL10.clSetKernelArg(kernel, index, 2L, APIUtil.getBufferShort().put(0, value));
        }
        
        public void setArg(final CLKernel kernel, final int index, final int value) {
            CL10.clSetKernelArg(kernel, index, 4L, APIUtil.getBufferInt().put(0, value));
        }
        
        public void setArg(final CLKernel kernel, final int index, final long value) {
            CL10.clSetKernelArg(kernel, index, 8L, APIUtil.getBufferLong().put(0, value));
        }
        
        public void setArg(final CLKernel kernel, final int index, final float value) {
            CL10.clSetKernelArg(kernel, index, 4L, APIUtil.getBufferFloat().put(0, value));
        }
        
        public void setArg(final CLKernel kernel, final int index, final double value) {
            CL10.clSetKernelArg(kernel, index, 8L, APIUtil.getBufferDouble().put(0, value));
        }
        
        public void setArg(final CLKernel kernel, final int index, final CLObject value) {
            CL10.clSetKernelArg(kernel, index, value);
        }
        
        public void setArgSize(final CLKernel kernel, final int index, final long size) {
            CL10.clSetKernelArg(kernel, index, size);
        }
        
        @Override
        protected int getInfo(final CLKernel kernel, final int param_name, final ByteBuffer param_value, final PointerBuffer param_value_size_ret) {
            return CL10.clGetKernelInfo(kernel, param_name, param_value, param_value_size_ret);
        }
        
        public long getWorkGroupInfoSize(final CLKernel kernel, final CLDevice device, final int param_name) {
            device.checkValid();
            final PointerBuffer buffer = APIUtil.getBufferPointer();
            CL10.clGetKernelWorkGroupInfo(kernel, device, param_name, buffer.getBuffer(), null);
            return buffer.get(0);
        }
        
        public long[] getWorkGroupInfoSizeArray(final CLKernel kernel, final CLDevice device, final int param_name) {
            device.checkValid();
            switch (param_name) {
                case 4529: {
                    final int size = 3;
                    final PointerBuffer buffer = APIUtil.getBufferPointer(size);
                    CL10.clGetKernelWorkGroupInfo(kernel, device, param_name, buffer.getBuffer(), null);
                    final long[] array = new long[size];
                    for (int i = 0; i < size; ++i) {
                        array[i] = buffer.get(i);
                    }
                    return array;
                }
                default: {
                    throw new IllegalArgumentException("Unsupported parameter: " + LWJGLUtil.toHexString(param_name));
                }
            }
        }
        
        public long getWorkGroupInfoLong(final CLKernel kernel, final CLDevice device, final int param_name) {
            device.checkValid();
            final ByteBuffer buffer = APIUtil.getBufferByte(8);
            CL10.clGetKernelWorkGroupInfo(kernel, device, param_name, buffer, null);
            return buffer.getLong(0);
        }
    }
    
    private static final class CLMemUtil extends InfoUtilAbstract<CLMem> implements CLMem.CLMemUtil
    {
        @Override
        protected int getInfo(final CLMem mem, final int param_name, final ByteBuffer param_value, final PointerBuffer param_value_size_ret) {
            return CL10.clGetMemObjectInfo(mem, param_name, param_value, param_value_size_ret);
        }
        
        public CLMem createImage2D(final CLContext context, final long flags, final CLImageFormat image_format, final long image_width, final long image_height, final long image_row_pitch, final Buffer host_ptr, IntBuffer errcode_ret) {
            final ByteBuffer formatBuffer = APIUtil.getBufferByte(8);
            formatBuffer.putInt(0, image_format.getChannelOrder());
            formatBuffer.putInt(4, image_format.getChannelType());
            final long function_pointer = CLCapabilities.clCreateImage2D;
            BufferChecks.checkFunctionAddress(function_pointer);
            if (errcode_ret != null) {
                BufferChecks.checkBuffer(errcode_ret, 1);
            }
            else if (LWJGLUtil.DEBUG) {
                errcode_ret = APIUtil.getBufferInt();
            }
            final CLMem __result = new CLMem(CL10.nclCreateImage2D(context.getPointer(), flags, MemoryUtil.getAddress(formatBuffer, 0), image_width, image_height, image_row_pitch, MemoryUtil.getAddress0Safe(host_ptr) + ((host_ptr != null) ? BufferChecks.checkBuffer(host_ptr, CLChecks.calculateImage2DSize(formatBuffer, image_width, image_height, image_row_pitch)) : 0), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
            if (LWJGLUtil.DEBUG) {
                Util.checkCLError(errcode_ret.get(0));
            }
            return __result;
        }
        
        public CLMem createImage3D(final CLContext context, final long flags, final CLImageFormat image_format, final long image_width, final long image_height, final long image_depth, final long image_row_pitch, final long image_slice_pitch, final Buffer host_ptr, IntBuffer errcode_ret) {
            final ByteBuffer formatBuffer = APIUtil.getBufferByte(8);
            formatBuffer.putInt(0, image_format.getChannelOrder());
            formatBuffer.putInt(4, image_format.getChannelType());
            final long function_pointer = CLCapabilities.clCreateImage3D;
            BufferChecks.checkFunctionAddress(function_pointer);
            if (errcode_ret != null) {
                BufferChecks.checkBuffer(errcode_ret, 1);
            }
            else if (LWJGLUtil.DEBUG) {
                errcode_ret = APIUtil.getBufferInt();
            }
            final CLMem __result = new CLMem(CL10.nclCreateImage3D(context.getPointer(), flags, MemoryUtil.getAddress(formatBuffer, 0), image_width, image_height, image_depth, image_row_pitch, image_slice_pitch, MemoryUtil.getAddress0Safe(host_ptr) + ((host_ptr != null) ? BufferChecks.checkBuffer(host_ptr, CLChecks.calculateImage3DSize(formatBuffer, image_width, image_height, image_depth, image_row_pitch, image_slice_pitch)) : 0), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
            if (LWJGLUtil.DEBUG) {
                Util.checkCLError(errcode_ret.get(0));
            }
            return __result;
        }
        
        public CLMem createSubBuffer(final CLMem mem, final long flags, final int buffer_create_type, final CLBufferRegion buffer_create_info, final IntBuffer errcode_ret) {
            final PointerBuffer infoBuffer = APIUtil.getBufferPointer(2);
            infoBuffer.put(buffer_create_info.getOrigin());
            infoBuffer.put(buffer_create_info.getSize());
            return CL11.clCreateSubBuffer(mem, flags, buffer_create_type, infoBuffer.getBuffer(), errcode_ret);
        }
        
        public ByteBuffer getInfoHostBuffer(final CLMem mem) {
            mem.checkValid();
            if (LWJGLUtil.DEBUG) {
                final long mem_flags = this.getInfoLong(mem, 4353);
                if ((mem_flags & 0x8L) != 0x8L) {
                    throw new IllegalArgumentException("The specified CLMem object does not use host memory.");
                }
            }
            final long size = this.getInfoSize(mem, 4354);
            if (size == 0L) {
                return null;
            }
            final long address = this.getInfoSize(mem, 4355);
            return CL.getHostBuffer(address, (int)size);
        }
        
        public long getImageInfoSize(final CLMem mem, final int param_name) {
            mem.checkValid();
            final PointerBuffer buffer = APIUtil.getBufferPointer();
            CL10.clGetImageInfo(mem, param_name, buffer.getBuffer(), null);
            return buffer.get(0);
        }
        
        public CLImageFormat getImageInfoFormat(final CLMem mem) {
            mem.checkValid();
            final ByteBuffer format = APIUtil.getBufferByte(8);
            CL10.clGetImageInfo(mem, 4368, format, null);
            return new CLImageFormat(format.getInt(0), format.getInt(4));
        }
        
        public int getImageInfoFormat(final CLMem mem, final int index) {
            mem.checkValid();
            final ByteBuffer format = APIUtil.getBufferByte(8);
            CL10.clGetImageInfo(mem, 4368, format, null);
            return format.getInt(index << 2);
        }
        
        public int getGLObjectType(final CLMem mem) {
            mem.checkValid();
            final IntBuffer buffer = APIUtil.getBufferInt();
            CL10GL.clGetGLObjectInfo(mem, buffer, null);
            return buffer.get(0);
        }
        
        public int getGLObjectName(final CLMem mem) {
            mem.checkValid();
            final IntBuffer buffer = APIUtil.getBufferInt();
            CL10GL.clGetGLObjectInfo(mem, null, buffer);
            return buffer.get(0);
        }
        
        public int getGLTextureInfoInt(final CLMem mem, final int param_name) {
            mem.checkValid();
            final ByteBuffer buffer = APIUtil.getBufferByte(4);
            CL10GL.clGetGLTextureInfo(mem, param_name, buffer, null);
            return buffer.getInt(0);
        }
    }
    
    private static final class CLPlatformUtil extends InfoUtilAbstract<CLPlatform> implements CLPlatform.CLPlatformUtil
    {
        @Override
        protected int getInfo(final CLPlatform platform, final int param_name, final ByteBuffer param_value, final PointerBuffer param_value_size_ret) {
            return CL10.clGetPlatformInfo(platform, param_name, param_value, param_value_size_ret);
        }
        
        public List<CLPlatform> getPlatforms(final Filter<CLPlatform> filter) {
            final IntBuffer numBuffer = APIUtil.getBufferInt();
            CL10.clGetPlatformIDs(null, numBuffer);
            final int num_platforms = numBuffer.get(0);
            if (num_platforms == 0) {
                return null;
            }
            final PointerBuffer platformIDs = APIUtil.getBufferPointer(num_platforms);
            CL10.clGetPlatformIDs(platformIDs, null);
            final List<CLPlatform> platforms = new ArrayList<CLPlatform>(num_platforms);
            for (int i = 0; i < num_platforms; ++i) {
                final CLPlatform platform = CLPlatform.getCLPlatform(platformIDs.get(i));
                if (filter == null || filter.accept(platform)) {
                    platforms.add(platform);
                }
            }
            return (platforms.size() == 0) ? null : platforms;
        }
        
        public List<CLDevice> getDevices(final CLPlatform platform, final int device_type, final Filter<CLDevice> filter) {
            platform.checkValid();
            final IntBuffer numBuffer = APIUtil.getBufferInt();
            CL10.clGetDeviceIDs(platform, device_type, null, numBuffer);
            final int num_devices = numBuffer.get(0);
            if (num_devices == 0) {
                return null;
            }
            final PointerBuffer deviceIDs = APIUtil.getBufferPointer(num_devices);
            CL10.clGetDeviceIDs(platform, device_type, deviceIDs, null);
            final List<CLDevice> devices = new ArrayList<CLDevice>(num_devices);
            for (int i = 0; i < num_devices; ++i) {
                final CLDevice device = platform.getCLDevice(deviceIDs.get(i));
                if (filter == null || filter.accept(device)) {
                    devices.add(device);
                }
            }
            return (devices.size() == 0) ? null : devices;
        }
    }
    
    private static final class CLProgramUtil extends InfoUtilAbstract<CLProgram> implements CLProgram.CLProgramUtil
    {
        @Override
        protected int getInfo(final CLProgram program, final int param_name, final ByteBuffer param_value, final PointerBuffer param_value_size_ret) {
            return CL10.clGetProgramInfo(program, param_name, param_value, param_value_size_ret);
        }
        
        @Override
        protected int getInfoSizeArraySize(final CLProgram program, final int param_name) {
            switch (param_name) {
                case 4453: {
                    return this.getInfoInt(program, 4450);
                }
                default: {
                    throw new IllegalArgumentException("Unsupported parameter: " + LWJGLUtil.toHexString(param_name));
                }
            }
        }
        
        public CLKernel[] createKernelsInProgram(final CLProgram program) {
            final IntBuffer numBuffer = APIUtil.getBufferInt();
            CL10.clCreateKernelsInProgram(program, null, numBuffer);
            final int num_kernels = numBuffer.get(0);
            if (num_kernels == 0) {
                return null;
            }
            final PointerBuffer kernelIDs = APIUtil.getBufferPointer(num_kernels);
            CL10.clCreateKernelsInProgram(program, kernelIDs, null);
            final CLKernel[] kernels = new CLKernel[num_kernels];
            for (int i = 0; i < num_kernels; ++i) {
                kernels[i] = program.getCLKernel(kernelIDs.get(i));
            }
            return kernels;
        }
        
        public CLDevice[] getInfoDevices(final CLProgram program) {
            program.checkValid();
            final int size = this.getInfoInt(program, 4450);
            final PointerBuffer buffer = APIUtil.getBufferPointer(size);
            CL10.clGetProgramInfo(program, 4451, buffer.getBuffer(), null);
            final CLPlatform platform = program.getParent().getParent();
            final CLDevice[] array = new CLDevice[size];
            for (int i = 0; i < size; ++i) {
                array[i] = platform.getCLDevice(buffer.get(i));
            }
            return array;
        }
        
        public ByteBuffer getInfoBinaries(final CLProgram program, ByteBuffer target) {
            program.checkValid();
            final PointerBuffer sizes = this.getSizesBuffer(program, 4453);
            int totalSize = 0;
            for (int i = 0; i < sizes.limit(); ++i) {
                totalSize += (int)sizes.get(i);
            }
            if (target == null) {
                target = BufferUtils.createByteBuffer(totalSize);
            }
            else if (LWJGLUtil.DEBUG) {
                BufferChecks.checkBuffer(target, totalSize);
            }
            CL10.clGetProgramInfo(program, sizes, target, null);
            return target;
        }
        
        public ByteBuffer[] getInfoBinaries(final CLProgram program, ByteBuffer[] target) {
            program.checkValid();
            if (target == null) {
                final PointerBuffer sizes = this.getSizesBuffer(program, 4453);
                target = new ByteBuffer[sizes.remaining()];
                for (int i = 0; i < sizes.remaining(); ++i) {
                    target[i] = BufferUtils.createByteBuffer((int)sizes.get(i));
                }
            }
            else if (LWJGLUtil.DEBUG) {
                final PointerBuffer sizes = this.getSizesBuffer(program, 4453);
                if (target.length < sizes.remaining()) {
                    throw new IllegalArgumentException("The target array is not big enough: " + sizes.remaining() + " buffers are required.");
                }
                for (int i = 0; i < target.length; ++i) {
                    BufferChecks.checkBuffer(target[i], (int)sizes.get(i));
                }
            }
            CL10.clGetProgramInfo(program, target, null);
            return target;
        }
        
        public String getBuildInfoString(final CLProgram program, final CLDevice device, final int param_name) {
            program.checkValid();
            final int bytes = getBuildSizeRet(program, device, param_name);
            if (bytes <= 1) {
                return null;
            }
            final ByteBuffer buffer = APIUtil.getBufferByte(bytes);
            CL10.clGetProgramBuildInfo(program, device, param_name, buffer, null);
            buffer.limit(bytes - 1);
            return APIUtil.getString(buffer);
        }
        
        public int getBuildInfoInt(final CLProgram program, final CLDevice device, final int param_name) {
            program.checkValid();
            final ByteBuffer buffer = APIUtil.getBufferByte(4);
            CL10.clGetProgramBuildInfo(program, device, param_name, buffer, null);
            return buffer.getInt(0);
        }
        
        private static int getBuildSizeRet(final CLProgram program, final CLDevice device, final int param_name) {
            final PointerBuffer bytes = APIUtil.getBufferPointer();
            final int errcode = CL10.clGetProgramBuildInfo(program, device, param_name, null, bytes);
            if (errcode != 0) {
                throw new IllegalArgumentException("Invalid parameter specified: " + LWJGLUtil.toHexString(param_name));
            }
            return (int)bytes.get(0);
        }
    }
}

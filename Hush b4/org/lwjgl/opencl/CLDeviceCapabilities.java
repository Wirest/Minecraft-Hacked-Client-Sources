// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

import java.util.Set;
import java.util.StringTokenizer;

public class CLDeviceCapabilities
{
    public final int majorVersion;
    public final int minorVersion;
    public final boolean OpenCL11;
    public final boolean OpenCL12;
    public final boolean CL_AMD_device_attribute_query;
    public final boolean CL_AMD_device_memory_flags;
    public final boolean CL_AMD_fp64;
    public final boolean CL_AMD_media_ops;
    public final boolean CL_AMD_media_ops2;
    public final boolean CL_AMD_offline_devices;
    public final boolean CL_AMD_popcnt;
    public final boolean CL_AMD_printf;
    public final boolean CL_AMD_vec3;
    final boolean CL_APPLE_ContextLoggingFunctions;
    public final boolean CL_APPLE_SetMemObjectDestructor;
    public final boolean CL_APPLE_gl_sharing;
    public final boolean CL_EXT_atomic_counters_32;
    public final boolean CL_EXT_atomic_counters_64;
    public final boolean CL_EXT_device_fission;
    public final boolean CL_EXT_migrate_memobject;
    public final boolean CL_INTEL_immediate_execution;
    public final boolean CL_INTEL_printf;
    public final boolean CL_INTEL_thread_local_exec;
    public final boolean CL_KHR_3d_image_writes;
    public final boolean CL_KHR_byte_addressable_store;
    public final boolean CL_KHR_depth_images;
    public final boolean CL_KHR_fp16;
    public final boolean CL_KHR_fp64;
    public final boolean CL_KHR_gl_depth_images;
    public final boolean CL_KHR_gl_event;
    public final boolean CL_KHR_gl_msaa_sharing;
    public final boolean CL_KHR_gl_sharing;
    public final boolean CL_KHR_global_int32_base_atomics;
    public final boolean CL_KHR_global_int32_extended_atomics;
    public final boolean CL_KHR_image2d_from_buffer;
    public final boolean CL_KHR_initialize_memory;
    public final boolean CL_KHR_int64_base_atomics;
    public final boolean CL_KHR_int64_extended_atomics;
    public final boolean CL_KHR_local_int32_base_atomics;
    public final boolean CL_KHR_local_int32_extended_atomics;
    public final boolean CL_KHR_mipmap_image;
    public final boolean CL_KHR_mipmap_image_writes;
    public final boolean CL_KHR_select_fprounding_mode;
    public final boolean CL_KHR_spir;
    public final boolean CL_KHR_srgb_image_writes;
    public final boolean CL_KHR_subgroups;
    public final boolean CL_KHR_terminate_context;
    public final boolean CL_NV_compiler_options;
    public final boolean CL_NV_device_attribute_query;
    public final boolean CL_NV_pragma_unroll;
    
    public CLDeviceCapabilities(final CLDevice device) {
        final String extensionList = device.getInfoString(4144);
        final String version = device.getInfoString(4143);
        if (!version.startsWith("OpenCL ")) {
            throw new RuntimeException("Invalid OpenCL version string: " + version);
        }
        try {
            final StringTokenizer tokenizer = new StringTokenizer(version.substring(7), ". ");
            this.majorVersion = Integer.parseInt(tokenizer.nextToken());
            this.minorVersion = Integer.parseInt(tokenizer.nextToken());
            this.OpenCL11 = (1 < this.majorVersion || (1 == this.majorVersion && 1 <= this.minorVersion));
            this.OpenCL12 = (1 < this.majorVersion || (1 == this.majorVersion && 2 <= this.minorVersion));
        }
        catch (RuntimeException e) {
            throw new RuntimeException("The major and/or minor OpenCL version \"" + version + "\" is malformed: " + e.getMessage());
        }
        final Set<String> extensions = APIUtil.getExtensions(extensionList);
        this.CL_AMD_device_attribute_query = extensions.contains("cl_amd_device_attribute_query");
        this.CL_AMD_device_memory_flags = extensions.contains("cl_amd_device_memory_flags");
        this.CL_AMD_fp64 = extensions.contains("cl_amd_fp64");
        this.CL_AMD_media_ops = extensions.contains("cl_amd_media_ops");
        this.CL_AMD_media_ops2 = extensions.contains("cl_amd_media_ops2");
        this.CL_AMD_offline_devices = extensions.contains("cl_amd_offline_devices");
        this.CL_AMD_popcnt = extensions.contains("cl_amd_popcnt");
        this.CL_AMD_printf = extensions.contains("cl_amd_printf");
        this.CL_AMD_vec3 = extensions.contains("cl_amd_vec3");
        this.CL_APPLE_ContextLoggingFunctions = (extensions.contains("cl_APPLE_ContextLoggingFunctions") && CLCapabilities.CL_APPLE_ContextLoggingFunctions);
        this.CL_APPLE_SetMemObjectDestructor = (extensions.contains("cl_APPLE_SetMemObjectDestructor") && CLCapabilities.CL_APPLE_SetMemObjectDestructor);
        this.CL_APPLE_gl_sharing = (extensions.contains("cl_APPLE_gl_sharing") && CLCapabilities.CL_APPLE_gl_sharing);
        this.CL_EXT_atomic_counters_32 = extensions.contains("cl_ext_atomic_counters_32");
        this.CL_EXT_atomic_counters_64 = extensions.contains("cl_ext_atomic_counters_64");
        this.CL_EXT_device_fission = (extensions.contains("cl_ext_device_fission") && CLCapabilities.CL_EXT_device_fission);
        this.CL_EXT_migrate_memobject = (extensions.contains("cl_ext_migrate_memobject") && CLCapabilities.CL_EXT_migrate_memobject);
        this.CL_INTEL_immediate_execution = extensions.contains("cl_intel_immediate_execution");
        this.CL_INTEL_printf = extensions.contains("cl_intel_printf");
        this.CL_INTEL_thread_local_exec = extensions.contains("cl_intel_thread_local_exec");
        this.CL_KHR_3d_image_writes = extensions.contains("cl_khr_3d_image_writes");
        this.CL_KHR_byte_addressable_store = extensions.contains("cl_khr_byte_addressable_store");
        this.CL_KHR_depth_images = extensions.contains("cl_khr_depth_images");
        this.CL_KHR_fp16 = extensions.contains("cl_khr_fp16");
        this.CL_KHR_fp64 = extensions.contains("cl_khr_fp64");
        this.CL_KHR_gl_depth_images = extensions.contains("cl_khr_gl_depth_images");
        this.CL_KHR_gl_event = (extensions.contains("cl_khr_gl_event") && CLCapabilities.CL_KHR_gl_event);
        this.CL_KHR_gl_msaa_sharing = extensions.contains("cl_khr_gl_msaa_sharing");
        this.CL_KHR_gl_sharing = (extensions.contains("cl_khr_gl_sharing") && CLCapabilities.CL_KHR_gl_sharing);
        this.CL_KHR_global_int32_base_atomics = extensions.contains("cl_khr_global_int32_base_atomics");
        this.CL_KHR_global_int32_extended_atomics = extensions.contains("cl_khr_global_int32_extended_atomics");
        this.CL_KHR_image2d_from_buffer = extensions.contains("cl_khr_image2d_from_buffer");
        this.CL_KHR_initialize_memory = extensions.contains("cl_khr_initialize_memory");
        this.CL_KHR_int64_base_atomics = extensions.contains("cl_khr_int64_base_atomics");
        this.CL_KHR_int64_extended_atomics = extensions.contains("cl_khr_int64_extended_atomics");
        this.CL_KHR_local_int32_base_atomics = extensions.contains("cl_khr_local_int32_base_atomics");
        this.CL_KHR_local_int32_extended_atomics = extensions.contains("cl_khr_local_int32_extended_atomics");
        this.CL_KHR_mipmap_image = extensions.contains("cl_khr_mipmap_image");
        this.CL_KHR_mipmap_image_writes = extensions.contains("cl_khr_mipmap_image_writes");
        this.CL_KHR_select_fprounding_mode = extensions.contains("cl_khr_select_fprounding_mode");
        this.CL_KHR_spir = extensions.contains("cl_khr_spir");
        this.CL_KHR_srgb_image_writes = extensions.contains("cl_khr_srgb_image_writes");
        this.CL_KHR_subgroups = (extensions.contains("cl_khr_subgroups") && CLCapabilities.CL_KHR_subgroups);
        this.CL_KHR_terminate_context = (extensions.contains("cl_khr_terminate_context") && CLCapabilities.CL_KHR_terminate_context);
        this.CL_NV_compiler_options = extensions.contains("cl_nv_compiler_options");
        this.CL_NV_device_attribute_query = extensions.contains("cl_nv_device_attribute_query");
        this.CL_NV_pragma_unroll = extensions.contains("cl_nv_pragma_unroll");
    }
    
    public int getMajorVersion() {
        return this.majorVersion;
    }
    
    public int getMinorVersion() {
        return this.minorVersion;
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append("OpenCL ").append(this.majorVersion).append('.').append(this.minorVersion);
        buf.append(" - Extensions: ");
        if (this.CL_AMD_device_attribute_query) {
            buf.append("cl_amd_device_attribute_query ");
        }
        if (this.CL_AMD_device_memory_flags) {
            buf.append("cl_amd_device_memory_flags ");
        }
        if (this.CL_AMD_fp64) {
            buf.append("cl_amd_fp64 ");
        }
        if (this.CL_AMD_media_ops) {
            buf.append("cl_amd_media_ops ");
        }
        if (this.CL_AMD_media_ops2) {
            buf.append("cl_amd_media_ops2 ");
        }
        if (this.CL_AMD_offline_devices) {
            buf.append("cl_amd_offline_devices ");
        }
        if (this.CL_AMD_popcnt) {
            buf.append("cl_amd_popcnt ");
        }
        if (this.CL_AMD_printf) {
            buf.append("cl_amd_printf ");
        }
        if (this.CL_AMD_vec3) {
            buf.append("cl_amd_vec3 ");
        }
        if (this.CL_APPLE_ContextLoggingFunctions) {
            buf.append("cl_apple_contextloggingfunctions ");
        }
        if (this.CL_APPLE_SetMemObjectDestructor) {
            buf.append("cl_apple_setmemobjectdestructor ");
        }
        if (this.CL_APPLE_gl_sharing) {
            buf.append("cl_apple_gl_sharing ");
        }
        if (this.CL_EXT_atomic_counters_32) {
            buf.append("cl_ext_atomic_counters_32 ");
        }
        if (this.CL_EXT_atomic_counters_64) {
            buf.append("cl_ext_atomic_counters_64 ");
        }
        if (this.CL_EXT_device_fission) {
            buf.append("cl_ext_device_fission ");
        }
        if (this.CL_EXT_migrate_memobject) {
            buf.append("cl_ext_migrate_memobject ");
        }
        if (this.CL_INTEL_immediate_execution) {
            buf.append("cl_intel_immediate_execution ");
        }
        if (this.CL_INTEL_printf) {
            buf.append("cl_intel_printf ");
        }
        if (this.CL_INTEL_thread_local_exec) {
            buf.append("cl_intel_thread_local_exec ");
        }
        if (this.CL_KHR_3d_image_writes) {
            buf.append("cl_khr_3d_image_writes ");
        }
        if (this.CL_KHR_byte_addressable_store) {
            buf.append("cl_khr_byte_addressable_store ");
        }
        if (this.CL_KHR_depth_images) {
            buf.append("cl_khr_depth_images ");
        }
        if (this.CL_KHR_fp16) {
            buf.append("cl_khr_fp16 ");
        }
        if (this.CL_KHR_fp64) {
            buf.append("cl_khr_fp64 ");
        }
        if (this.CL_KHR_gl_depth_images) {
            buf.append("cl_khr_gl_depth_images ");
        }
        if (this.CL_KHR_gl_event) {
            buf.append("cl_khr_gl_event ");
        }
        if (this.CL_KHR_gl_msaa_sharing) {
            buf.append("cl_khr_gl_msaa_sharing ");
        }
        if (this.CL_KHR_gl_sharing) {
            buf.append("cl_khr_gl_sharing ");
        }
        if (this.CL_KHR_global_int32_base_atomics) {
            buf.append("cl_khr_global_int32_base_atomics ");
        }
        if (this.CL_KHR_global_int32_extended_atomics) {
            buf.append("cl_khr_global_int32_extended_atomics ");
        }
        if (this.CL_KHR_image2d_from_buffer) {
            buf.append("cl_khr_image2d_from_buffer ");
        }
        if (this.CL_KHR_initialize_memory) {
            buf.append("cl_khr_initialize_memory ");
        }
        if (this.CL_KHR_int64_base_atomics) {
            buf.append("cl_khr_int64_base_atomics ");
        }
        if (this.CL_KHR_int64_extended_atomics) {
            buf.append("cl_khr_int64_extended_atomics ");
        }
        if (this.CL_KHR_local_int32_base_atomics) {
            buf.append("cl_khr_local_int32_base_atomics ");
        }
        if (this.CL_KHR_local_int32_extended_atomics) {
            buf.append("cl_khr_local_int32_extended_atomics ");
        }
        if (this.CL_KHR_mipmap_image) {
            buf.append("cl_khr_mipmap_image ");
        }
        if (this.CL_KHR_mipmap_image_writes) {
            buf.append("cl_khr_mipmap_image_writes ");
        }
        if (this.CL_KHR_select_fprounding_mode) {
            buf.append("cl_khr_select_fprounding_mode ");
        }
        if (this.CL_KHR_spir) {
            buf.append("cl_khr_spir ");
        }
        if (this.CL_KHR_srgb_image_writes) {
            buf.append("cl_khr_srgb_image_writes ");
        }
        if (this.CL_KHR_subgroups) {
            buf.append("cl_khr_subgroups ");
        }
        if (this.CL_KHR_terminate_context) {
            buf.append("cl_khr_terminate_context ");
        }
        if (this.CL_NV_compiler_options) {
            buf.append("cl_nv_compiler_options ");
        }
        if (this.CL_NV_device_attribute_query) {
            buf.append("cl_nv_device_attribute_query ");
        }
        if (this.CL_NV_pragma_unroll) {
            buf.append("cl_nv_pragma_unroll ");
        }
        return buf.toString();
    }
}

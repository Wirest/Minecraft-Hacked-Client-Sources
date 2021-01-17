// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import java.util.HashSet;
import org.lwjgl.LWJGLUtil;
import java.util.Set;

public class ContextCapabilities
{
    static final boolean DEBUG = false;
    final APIUtil util;
    final StateTracker tracker;
    public final boolean GL_AMD_blend_minmax_factor;
    public final boolean GL_AMD_conservative_depth;
    public final boolean GL_AMD_debug_output;
    public final boolean GL_AMD_depth_clamp_separate;
    public final boolean GL_AMD_draw_buffers_blend;
    public final boolean GL_AMD_interleaved_elements;
    public final boolean GL_AMD_multi_draw_indirect;
    public final boolean GL_AMD_name_gen_delete;
    public final boolean GL_AMD_performance_monitor;
    public final boolean GL_AMD_pinned_memory;
    public final boolean GL_AMD_query_buffer_object;
    public final boolean GL_AMD_sample_positions;
    public final boolean GL_AMD_seamless_cubemap_per_texture;
    public final boolean GL_AMD_shader_atomic_counter_ops;
    public final boolean GL_AMD_shader_stencil_export;
    public final boolean GL_AMD_shader_trinary_minmax;
    public final boolean GL_AMD_sparse_texture;
    public final boolean GL_AMD_stencil_operation_extended;
    public final boolean GL_AMD_texture_texture4;
    public final boolean GL_AMD_transform_feedback3_lines_triangles;
    public final boolean GL_AMD_vertex_shader_layer;
    public final boolean GL_AMD_vertex_shader_tessellator;
    public final boolean GL_AMD_vertex_shader_viewport_index;
    public final boolean GL_APPLE_aux_depth_stencil;
    public final boolean GL_APPLE_client_storage;
    public final boolean GL_APPLE_element_array;
    public final boolean GL_APPLE_fence;
    public final boolean GL_APPLE_float_pixels;
    public final boolean GL_APPLE_flush_buffer_range;
    public final boolean GL_APPLE_object_purgeable;
    public final boolean GL_APPLE_packed_pixels;
    public final boolean GL_APPLE_rgb_422;
    public final boolean GL_APPLE_row_bytes;
    public final boolean GL_APPLE_texture_range;
    public final boolean GL_APPLE_vertex_array_object;
    public final boolean GL_APPLE_vertex_array_range;
    public final boolean GL_APPLE_vertex_program_evaluators;
    public final boolean GL_APPLE_ycbcr_422;
    public final boolean GL_ARB_ES2_compatibility;
    public final boolean GL_ARB_ES3_1_compatibility;
    public final boolean GL_ARB_ES3_compatibility;
    public final boolean GL_ARB_arrays_of_arrays;
    public final boolean GL_ARB_base_instance;
    public final boolean GL_ARB_bindless_texture;
    public final boolean GL_ARB_blend_func_extended;
    public final boolean GL_ARB_buffer_storage;
    public final boolean GL_ARB_cl_event;
    public final boolean GL_ARB_clear_buffer_object;
    public final boolean GL_ARB_clear_texture;
    public final boolean GL_ARB_clip_control;
    public final boolean GL_ARB_color_buffer_float;
    public final boolean GL_ARB_compatibility;
    public final boolean GL_ARB_compressed_texture_pixel_storage;
    public final boolean GL_ARB_compute_shader;
    public final boolean GL_ARB_compute_variable_group_size;
    public final boolean GL_ARB_conditional_render_inverted;
    public final boolean GL_ARB_conservative_depth;
    public final boolean GL_ARB_copy_buffer;
    public final boolean GL_ARB_copy_image;
    public final boolean GL_ARB_cull_distance;
    public final boolean GL_ARB_debug_output;
    public final boolean GL_ARB_depth_buffer_float;
    public final boolean GL_ARB_depth_clamp;
    public final boolean GL_ARB_depth_texture;
    public final boolean GL_ARB_derivative_control;
    public final boolean GL_ARB_direct_state_access;
    public final boolean GL_ARB_draw_buffers;
    public final boolean GL_ARB_draw_buffers_blend;
    public final boolean GL_ARB_draw_elements_base_vertex;
    public final boolean GL_ARB_draw_indirect;
    public final boolean GL_ARB_draw_instanced;
    public final boolean GL_ARB_enhanced_layouts;
    public final boolean GL_ARB_explicit_attrib_location;
    public final boolean GL_ARB_explicit_uniform_location;
    public final boolean GL_ARB_fragment_coord_conventions;
    public final boolean GL_ARB_fragment_layer_viewport;
    public final boolean GL_ARB_fragment_program;
    public final boolean GL_ARB_fragment_program_shadow;
    public final boolean GL_ARB_fragment_shader;
    public final boolean GL_ARB_framebuffer_no_attachments;
    public final boolean GL_ARB_framebuffer_object;
    public final boolean GL_ARB_framebuffer_sRGB;
    public final boolean GL_ARB_geometry_shader4;
    public final boolean GL_ARB_get_program_binary;
    public final boolean GL_ARB_get_texture_sub_image;
    public final boolean GL_ARB_gpu_shader5;
    public final boolean GL_ARB_gpu_shader_fp64;
    public final boolean GL_ARB_half_float_pixel;
    public final boolean GL_ARB_half_float_vertex;
    public final boolean GL_ARB_imaging;
    public final boolean GL_ARB_indirect_parameters;
    public final boolean GL_ARB_instanced_arrays;
    public final boolean GL_ARB_internalformat_query;
    public final boolean GL_ARB_internalformat_query2;
    public final boolean GL_ARB_invalidate_subdata;
    public final boolean GL_ARB_map_buffer_alignment;
    public final boolean GL_ARB_map_buffer_range;
    public final boolean GL_ARB_matrix_palette;
    public final boolean GL_ARB_multi_bind;
    public final boolean GL_ARB_multi_draw_indirect;
    public final boolean GL_ARB_multisample;
    public final boolean GL_ARB_multitexture;
    public final boolean GL_ARB_occlusion_query;
    public final boolean GL_ARB_occlusion_query2;
    public final boolean GL_ARB_pipeline_statistics_query;
    public final boolean GL_ARB_pixel_buffer_object;
    public final boolean GL_ARB_point_parameters;
    public final boolean GL_ARB_point_sprite;
    public final boolean GL_ARB_program_interface_query;
    public final boolean GL_ARB_provoking_vertex;
    public final boolean GL_ARB_query_buffer_object;
    public final boolean GL_ARB_robust_buffer_access_behavior;
    public final boolean GL_ARB_robustness;
    public final boolean GL_ARB_robustness_isolation;
    public final boolean GL_ARB_sample_shading;
    public final boolean GL_ARB_sampler_objects;
    public final boolean GL_ARB_seamless_cube_map;
    public final boolean GL_ARB_seamless_cubemap_per_texture;
    public final boolean GL_ARB_separate_shader_objects;
    public final boolean GL_ARB_shader_atomic_counters;
    public final boolean GL_ARB_shader_bit_encoding;
    public final boolean GL_ARB_shader_draw_parameters;
    public final boolean GL_ARB_shader_group_vote;
    public final boolean GL_ARB_shader_image_load_store;
    public final boolean GL_ARB_shader_image_size;
    public final boolean GL_ARB_shader_objects;
    public final boolean GL_ARB_shader_precision;
    public final boolean GL_ARB_shader_stencil_export;
    public final boolean GL_ARB_shader_storage_buffer_object;
    public final boolean GL_ARB_shader_subroutine;
    public final boolean GL_ARB_shader_texture_image_samples;
    public final boolean GL_ARB_shader_texture_lod;
    public final boolean GL_ARB_shading_language_100;
    public final boolean GL_ARB_shading_language_420pack;
    public final boolean GL_ARB_shading_language_include;
    public final boolean GL_ARB_shading_language_packing;
    public final boolean GL_ARB_shadow;
    public final boolean GL_ARB_shadow_ambient;
    public final boolean GL_ARB_sparse_buffer;
    public final boolean GL_ARB_sparse_texture;
    public final boolean GL_ARB_stencil_texturing;
    public final boolean GL_ARB_sync;
    public final boolean GL_ARB_tessellation_shader;
    public final boolean GL_ARB_texture_barrier;
    public final boolean GL_ARB_texture_border_clamp;
    public final boolean GL_ARB_texture_buffer_object;
    public final boolean GL_ARB_texture_buffer_object_rgb32;
    public final boolean GL_ARB_texture_buffer_range;
    public final boolean GL_ARB_texture_compression;
    public final boolean GL_ARB_texture_compression_bptc;
    public final boolean GL_ARB_texture_compression_rgtc;
    public final boolean GL_ARB_texture_cube_map;
    public final boolean GL_ARB_texture_cube_map_array;
    public final boolean GL_ARB_texture_env_add;
    public final boolean GL_ARB_texture_env_combine;
    public final boolean GL_ARB_texture_env_crossbar;
    public final boolean GL_ARB_texture_env_dot3;
    public final boolean GL_ARB_texture_float;
    public final boolean GL_ARB_texture_gather;
    public final boolean GL_ARB_texture_mirror_clamp_to_edge;
    public final boolean GL_ARB_texture_mirrored_repeat;
    public final boolean GL_ARB_texture_multisample;
    public final boolean GL_ARB_texture_non_power_of_two;
    public final boolean GL_ARB_texture_query_levels;
    public final boolean GL_ARB_texture_query_lod;
    public final boolean GL_ARB_texture_rectangle;
    public final boolean GL_ARB_texture_rg;
    public final boolean GL_ARB_texture_rgb10_a2ui;
    public final boolean GL_ARB_texture_stencil8;
    public final boolean GL_ARB_texture_storage;
    public final boolean GL_ARB_texture_storage_multisample;
    public final boolean GL_ARB_texture_swizzle;
    public final boolean GL_ARB_texture_view;
    public final boolean GL_ARB_timer_query;
    public final boolean GL_ARB_transform_feedback2;
    public final boolean GL_ARB_transform_feedback3;
    public final boolean GL_ARB_transform_feedback_instanced;
    public final boolean GL_ARB_transform_feedback_overflow_query;
    public final boolean GL_ARB_transpose_matrix;
    public final boolean GL_ARB_uniform_buffer_object;
    public final boolean GL_ARB_vertex_array_bgra;
    public final boolean GL_ARB_vertex_array_object;
    public final boolean GL_ARB_vertex_attrib_64bit;
    public final boolean GL_ARB_vertex_attrib_binding;
    public final boolean GL_ARB_vertex_blend;
    public final boolean GL_ARB_vertex_buffer_object;
    public final boolean GL_ARB_vertex_program;
    public final boolean GL_ARB_vertex_shader;
    public final boolean GL_ARB_vertex_type_10f_11f_11f_rev;
    public final boolean GL_ARB_vertex_type_2_10_10_10_rev;
    public final boolean GL_ARB_viewport_array;
    public final boolean GL_ARB_window_pos;
    public final boolean GL_ATI_draw_buffers;
    public final boolean GL_ATI_element_array;
    public final boolean GL_ATI_envmap_bumpmap;
    public final boolean GL_ATI_fragment_shader;
    public final boolean GL_ATI_map_object_buffer;
    public final boolean GL_ATI_meminfo;
    public final boolean GL_ATI_pn_triangles;
    public final boolean GL_ATI_separate_stencil;
    public final boolean GL_ATI_shader_texture_lod;
    public final boolean GL_ATI_text_fragment_shader;
    public final boolean GL_ATI_texture_compression_3dc;
    public final boolean GL_ATI_texture_env_combine3;
    public final boolean GL_ATI_texture_float;
    public final boolean GL_ATI_texture_mirror_once;
    public final boolean GL_ATI_vertex_array_object;
    public final boolean GL_ATI_vertex_attrib_array_object;
    public final boolean GL_ATI_vertex_streams;
    public final boolean GL_EXT_Cg_shader;
    public final boolean GL_EXT_abgr;
    public final boolean GL_EXT_bgra;
    public final boolean GL_EXT_bindable_uniform;
    public final boolean GL_EXT_blend_color;
    public final boolean GL_EXT_blend_equation_separate;
    public final boolean GL_EXT_blend_func_separate;
    public final boolean GL_EXT_blend_minmax;
    public final boolean GL_EXT_blend_subtract;
    public final boolean GL_EXT_compiled_vertex_array;
    public final boolean GL_EXT_depth_bounds_test;
    public final boolean GL_EXT_direct_state_access;
    public final boolean GL_EXT_draw_buffers2;
    public final boolean GL_EXT_draw_instanced;
    public final boolean GL_EXT_draw_range_elements;
    public final boolean GL_EXT_fog_coord;
    public final boolean GL_EXT_framebuffer_blit;
    public final boolean GL_EXT_framebuffer_multisample;
    public final boolean GL_EXT_framebuffer_multisample_blit_scaled;
    public final boolean GL_EXT_framebuffer_object;
    public final boolean GL_EXT_framebuffer_sRGB;
    public final boolean GL_EXT_geometry_shader4;
    public final boolean GL_EXT_gpu_program_parameters;
    public final boolean GL_EXT_gpu_shader4;
    public final boolean GL_EXT_multi_draw_arrays;
    public final boolean GL_EXT_packed_depth_stencil;
    public final boolean GL_EXT_packed_float;
    public final boolean GL_EXT_packed_pixels;
    public final boolean GL_EXT_paletted_texture;
    public final boolean GL_EXT_pixel_buffer_object;
    public final boolean GL_EXT_point_parameters;
    public final boolean GL_EXT_provoking_vertex;
    public final boolean GL_EXT_rescale_normal;
    public final boolean GL_EXT_secondary_color;
    public final boolean GL_EXT_separate_shader_objects;
    public final boolean GL_EXT_separate_specular_color;
    public final boolean GL_EXT_shader_image_load_store;
    public final boolean GL_EXT_shadow_funcs;
    public final boolean GL_EXT_shared_texture_palette;
    public final boolean GL_EXT_stencil_clear_tag;
    public final boolean GL_EXT_stencil_two_side;
    public final boolean GL_EXT_stencil_wrap;
    public final boolean GL_EXT_texture_3d;
    public final boolean GL_EXT_texture_array;
    public final boolean GL_EXT_texture_buffer_object;
    public final boolean GL_EXT_texture_compression_latc;
    public final boolean GL_EXT_texture_compression_rgtc;
    public final boolean GL_EXT_texture_compression_s3tc;
    public final boolean GL_EXT_texture_env_combine;
    public final boolean GL_EXT_texture_env_dot3;
    public final boolean GL_EXT_texture_filter_anisotropic;
    public final boolean GL_EXT_texture_integer;
    public final boolean GL_EXT_texture_lod_bias;
    public final boolean GL_EXT_texture_mirror_clamp;
    public final boolean GL_EXT_texture_rectangle;
    public final boolean GL_EXT_texture_sRGB;
    public final boolean GL_EXT_texture_sRGB_decode;
    public final boolean GL_EXT_texture_shared_exponent;
    public final boolean GL_EXT_texture_snorm;
    public final boolean GL_EXT_texture_swizzle;
    public final boolean GL_EXT_timer_query;
    public final boolean GL_EXT_transform_feedback;
    public final boolean GL_EXT_vertex_array_bgra;
    public final boolean GL_EXT_vertex_attrib_64bit;
    public final boolean GL_EXT_vertex_shader;
    public final boolean GL_EXT_vertex_weighting;
    public final boolean OpenGL11;
    public final boolean OpenGL12;
    public final boolean OpenGL13;
    public final boolean OpenGL14;
    public final boolean OpenGL15;
    public final boolean OpenGL20;
    public final boolean OpenGL21;
    public final boolean OpenGL30;
    public final boolean OpenGL31;
    public final boolean OpenGL32;
    public final boolean OpenGL33;
    public final boolean OpenGL40;
    public final boolean OpenGL41;
    public final boolean OpenGL42;
    public final boolean OpenGL43;
    public final boolean OpenGL44;
    public final boolean OpenGL45;
    public final boolean GL_GREMEDY_frame_terminator;
    public final boolean GL_GREMEDY_string_marker;
    public final boolean GL_HP_occlusion_test;
    public final boolean GL_IBM_rasterpos_clip;
    public final boolean GL_INTEL_map_texture;
    public final boolean GL_KHR_context_flush_control;
    public final boolean GL_KHR_debug;
    public final boolean GL_KHR_robust_buffer_access_behavior;
    public final boolean GL_KHR_robustness;
    public final boolean GL_KHR_texture_compression_astc_ldr;
    public final boolean GL_NVX_gpu_memory_info;
    public final boolean GL_NV_bindless_multi_draw_indirect;
    public final boolean GL_NV_bindless_texture;
    public final boolean GL_NV_blend_equation_advanced;
    public final boolean GL_NV_blend_square;
    public final boolean GL_NV_compute_program5;
    public final boolean GL_NV_conditional_render;
    public final boolean GL_NV_copy_depth_to_color;
    public final boolean GL_NV_copy_image;
    public final boolean GL_NV_deep_texture3D;
    public final boolean GL_NV_depth_buffer_float;
    public final boolean GL_NV_depth_clamp;
    public final boolean GL_NV_draw_texture;
    public final boolean GL_NV_evaluators;
    public final boolean GL_NV_explicit_multisample;
    public final boolean GL_NV_fence;
    public final boolean GL_NV_float_buffer;
    public final boolean GL_NV_fog_distance;
    public final boolean GL_NV_fragment_program;
    public final boolean GL_NV_fragment_program2;
    public final boolean GL_NV_fragment_program4;
    public final boolean GL_NV_fragment_program_option;
    public final boolean GL_NV_framebuffer_multisample_coverage;
    public final boolean GL_NV_geometry_program4;
    public final boolean GL_NV_geometry_shader4;
    public final boolean GL_NV_gpu_program4;
    public final boolean GL_NV_gpu_program5;
    public final boolean GL_NV_gpu_program5_mem_extended;
    public final boolean GL_NV_gpu_shader5;
    public final boolean GL_NV_half_float;
    public final boolean GL_NV_light_max_exponent;
    public final boolean GL_NV_multisample_coverage;
    public final boolean GL_NV_multisample_filter_hint;
    public final boolean GL_NV_occlusion_query;
    public final boolean GL_NV_packed_depth_stencil;
    public final boolean GL_NV_parameter_buffer_object;
    public final boolean GL_NV_parameter_buffer_object2;
    public final boolean GL_NV_path_rendering;
    public final boolean GL_NV_pixel_data_range;
    public final boolean GL_NV_point_sprite;
    public final boolean GL_NV_present_video;
    public final boolean GL_NV_primitive_restart;
    public final boolean GL_NV_register_combiners;
    public final boolean GL_NV_register_combiners2;
    public final boolean GL_NV_shader_atomic_counters;
    public final boolean GL_NV_shader_atomic_float;
    public final boolean GL_NV_shader_buffer_load;
    public final boolean GL_NV_shader_buffer_store;
    public final boolean GL_NV_shader_storage_buffer_object;
    public final boolean GL_NV_tessellation_program5;
    public final boolean GL_NV_texgen_reflection;
    public final boolean GL_NV_texture_barrier;
    public final boolean GL_NV_texture_compression_vtc;
    public final boolean GL_NV_texture_env_combine4;
    public final boolean GL_NV_texture_expand_normal;
    public final boolean GL_NV_texture_multisample;
    public final boolean GL_NV_texture_rectangle;
    public final boolean GL_NV_texture_shader;
    public final boolean GL_NV_texture_shader2;
    public final boolean GL_NV_texture_shader3;
    public final boolean GL_NV_transform_feedback;
    public final boolean GL_NV_transform_feedback2;
    public final boolean GL_NV_vertex_array_range;
    public final boolean GL_NV_vertex_array_range2;
    public final boolean GL_NV_vertex_attrib_integer_64bit;
    public final boolean GL_NV_vertex_buffer_unified_memory;
    public final boolean GL_NV_vertex_program;
    public final boolean GL_NV_vertex_program1_1;
    public final boolean GL_NV_vertex_program2;
    public final boolean GL_NV_vertex_program2_option;
    public final boolean GL_NV_vertex_program3;
    public final boolean GL_NV_vertex_program4;
    public final boolean GL_NV_video_capture;
    public final boolean GL_SGIS_generate_mipmap;
    public final boolean GL_SGIS_texture_lod;
    public final boolean GL_SUN_slice_accum;
    long glDebugMessageEnableAMD;
    long glDebugMessageInsertAMD;
    long glDebugMessageCallbackAMD;
    long glGetDebugMessageLogAMD;
    long glBlendFuncIndexedAMD;
    long glBlendFuncSeparateIndexedAMD;
    long glBlendEquationIndexedAMD;
    long glBlendEquationSeparateIndexedAMD;
    long glVertexAttribParameteriAMD;
    long glMultiDrawArraysIndirectAMD;
    long glMultiDrawElementsIndirectAMD;
    long glGenNamesAMD;
    long glDeleteNamesAMD;
    long glIsNameAMD;
    long glGetPerfMonitorGroupsAMD;
    long glGetPerfMonitorCountersAMD;
    long glGetPerfMonitorGroupStringAMD;
    long glGetPerfMonitorCounterStringAMD;
    long glGetPerfMonitorCounterInfoAMD;
    long glGenPerfMonitorsAMD;
    long glDeletePerfMonitorsAMD;
    long glSelectPerfMonitorCountersAMD;
    long glBeginPerfMonitorAMD;
    long glEndPerfMonitorAMD;
    long glGetPerfMonitorCounterDataAMD;
    long glSetMultisamplefvAMD;
    long glTexStorageSparseAMD;
    long glTextureStorageSparseAMD;
    long glStencilOpValueAMD;
    long glTessellationFactorAMD;
    long glTessellationModeAMD;
    long glElementPointerAPPLE;
    long glDrawElementArrayAPPLE;
    long glDrawRangeElementArrayAPPLE;
    long glMultiDrawElementArrayAPPLE;
    long glMultiDrawRangeElementArrayAPPLE;
    long glGenFencesAPPLE;
    long glDeleteFencesAPPLE;
    long glSetFenceAPPLE;
    long glIsFenceAPPLE;
    long glTestFenceAPPLE;
    long glFinishFenceAPPLE;
    long glTestObjectAPPLE;
    long glFinishObjectAPPLE;
    long glBufferParameteriAPPLE;
    long glFlushMappedBufferRangeAPPLE;
    long glObjectPurgeableAPPLE;
    long glObjectUnpurgeableAPPLE;
    long glGetObjectParameterivAPPLE;
    long glTextureRangeAPPLE;
    long glGetTexParameterPointervAPPLE;
    long glBindVertexArrayAPPLE;
    long glDeleteVertexArraysAPPLE;
    long glGenVertexArraysAPPLE;
    long glIsVertexArrayAPPLE;
    long glVertexArrayRangeAPPLE;
    long glFlushVertexArrayRangeAPPLE;
    long glVertexArrayParameteriAPPLE;
    long glEnableVertexAttribAPPLE;
    long glDisableVertexAttribAPPLE;
    long glIsVertexAttribEnabledAPPLE;
    long glMapVertexAttrib1dAPPLE;
    long glMapVertexAttrib1fAPPLE;
    long glMapVertexAttrib2dAPPLE;
    long glMapVertexAttrib2fAPPLE;
    long glGetTextureHandleARB;
    long glGetTextureSamplerHandleARB;
    long glMakeTextureHandleResidentARB;
    long glMakeTextureHandleNonResidentARB;
    long glGetImageHandleARB;
    long glMakeImageHandleResidentARB;
    long glMakeImageHandleNonResidentARB;
    long glUniformHandleui64ARB;
    long glUniformHandleui64vARB;
    long glProgramUniformHandleui64ARB;
    long glProgramUniformHandleui64vARB;
    long glIsTextureHandleResidentARB;
    long glIsImageHandleResidentARB;
    long glVertexAttribL1ui64ARB;
    long glVertexAttribL1ui64vARB;
    long glGetVertexAttribLui64vARB;
    long glBindBufferARB;
    long glDeleteBuffersARB;
    long glGenBuffersARB;
    long glIsBufferARB;
    long glBufferDataARB;
    long glBufferSubDataARB;
    long glGetBufferSubDataARB;
    long glMapBufferARB;
    long glUnmapBufferARB;
    long glGetBufferParameterivARB;
    long glGetBufferPointervARB;
    long glNamedBufferStorageEXT;
    long glCreateSyncFromCLeventARB;
    long glClearNamedBufferDataEXT;
    long glClearNamedBufferSubDataEXT;
    long glClampColorARB;
    long glDispatchComputeGroupSizeARB;
    long glDebugMessageControlARB;
    long glDebugMessageInsertARB;
    long glDebugMessageCallbackARB;
    long glGetDebugMessageLogARB;
    long glDrawBuffersARB;
    long glBlendEquationiARB;
    long glBlendEquationSeparateiARB;
    long glBlendFunciARB;
    long glBlendFuncSeparateiARB;
    long glDrawArraysInstancedARB;
    long glDrawElementsInstancedARB;
    long glNamedFramebufferParameteriEXT;
    long glGetNamedFramebufferParameterivEXT;
    long glProgramParameteriARB;
    long glFramebufferTextureARB;
    long glFramebufferTextureLayerARB;
    long glFramebufferTextureFaceARB;
    long glProgramUniform1dEXT;
    long glProgramUniform2dEXT;
    long glProgramUniform3dEXT;
    long glProgramUniform4dEXT;
    long glProgramUniform1dvEXT;
    long glProgramUniform2dvEXT;
    long glProgramUniform3dvEXT;
    long glProgramUniform4dvEXT;
    long glProgramUniformMatrix2dvEXT;
    long glProgramUniformMatrix3dvEXT;
    long glProgramUniformMatrix4dvEXT;
    long glProgramUniformMatrix2x3dvEXT;
    long glProgramUniformMatrix2x4dvEXT;
    long glProgramUniformMatrix3x2dvEXT;
    long glProgramUniformMatrix3x4dvEXT;
    long glProgramUniformMatrix4x2dvEXT;
    long glProgramUniformMatrix4x3dvEXT;
    long glColorTable;
    long glColorSubTable;
    long glColorTableParameteriv;
    long glColorTableParameterfv;
    long glCopyColorSubTable;
    long glCopyColorTable;
    long glGetColorTable;
    long glGetColorTableParameteriv;
    long glGetColorTableParameterfv;
    long glHistogram;
    long glResetHistogram;
    long glGetHistogram;
    long glGetHistogramParameterfv;
    long glGetHistogramParameteriv;
    long glMinmax;
    long glResetMinmax;
    long glGetMinmax;
    long glGetMinmaxParameterfv;
    long glGetMinmaxParameteriv;
    long glConvolutionFilter1D;
    long glConvolutionFilter2D;
    long glConvolutionParameterf;
    long glConvolutionParameterfv;
    long glConvolutionParameteri;
    long glConvolutionParameteriv;
    long glCopyConvolutionFilter1D;
    long glCopyConvolutionFilter2D;
    long glGetConvolutionFilter;
    long glGetConvolutionParameterfv;
    long glGetConvolutionParameteriv;
    long glSeparableFilter2D;
    long glGetSeparableFilter;
    long glMultiDrawArraysIndirectCountARB;
    long glMultiDrawElementsIndirectCountARB;
    long glVertexAttribDivisorARB;
    long glCurrentPaletteMatrixARB;
    long glMatrixIndexPointerARB;
    long glMatrixIndexubvARB;
    long glMatrixIndexusvARB;
    long glMatrixIndexuivARB;
    long glSampleCoverageARB;
    long glClientActiveTextureARB;
    long glActiveTextureARB;
    long glMultiTexCoord1fARB;
    long glMultiTexCoord1dARB;
    long glMultiTexCoord1iARB;
    long glMultiTexCoord1sARB;
    long glMultiTexCoord2fARB;
    long glMultiTexCoord2dARB;
    long glMultiTexCoord2iARB;
    long glMultiTexCoord2sARB;
    long glMultiTexCoord3fARB;
    long glMultiTexCoord3dARB;
    long glMultiTexCoord3iARB;
    long glMultiTexCoord3sARB;
    long glMultiTexCoord4fARB;
    long glMultiTexCoord4dARB;
    long glMultiTexCoord4iARB;
    long glMultiTexCoord4sARB;
    long glGenQueriesARB;
    long glDeleteQueriesARB;
    long glIsQueryARB;
    long glBeginQueryARB;
    long glEndQueryARB;
    long glGetQueryivARB;
    long glGetQueryObjectivARB;
    long glGetQueryObjectuivARB;
    long glPointParameterfARB;
    long glPointParameterfvARB;
    long glProgramStringARB;
    long glBindProgramARB;
    long glDeleteProgramsARB;
    long glGenProgramsARB;
    long glProgramEnvParameter4fARB;
    long glProgramEnvParameter4dARB;
    long glProgramEnvParameter4fvARB;
    long glProgramEnvParameter4dvARB;
    long glProgramLocalParameter4fARB;
    long glProgramLocalParameter4dARB;
    long glProgramLocalParameter4fvARB;
    long glProgramLocalParameter4dvARB;
    long glGetProgramEnvParameterfvARB;
    long glGetProgramEnvParameterdvARB;
    long glGetProgramLocalParameterfvARB;
    long glGetProgramLocalParameterdvARB;
    long glGetProgramivARB;
    long glGetProgramStringARB;
    long glIsProgramARB;
    long glGetGraphicsResetStatusARB;
    long glGetnMapdvARB;
    long glGetnMapfvARB;
    long glGetnMapivARB;
    long glGetnPixelMapfvARB;
    long glGetnPixelMapuivARB;
    long glGetnPixelMapusvARB;
    long glGetnPolygonStippleARB;
    long glGetnTexImageARB;
    long glReadnPixelsARB;
    long glGetnColorTableARB;
    long glGetnConvolutionFilterARB;
    long glGetnSeparableFilterARB;
    long glGetnHistogramARB;
    long glGetnMinmaxARB;
    long glGetnCompressedTexImageARB;
    long glGetnUniformfvARB;
    long glGetnUniformivARB;
    long glGetnUniformuivARB;
    long glGetnUniformdvARB;
    long glMinSampleShadingARB;
    long glDeleteObjectARB;
    long glGetHandleARB;
    long glDetachObjectARB;
    long glCreateShaderObjectARB;
    long glShaderSourceARB;
    long glCompileShaderARB;
    long glCreateProgramObjectARB;
    long glAttachObjectARB;
    long glLinkProgramARB;
    long glUseProgramObjectARB;
    long glValidateProgramARB;
    long glUniform1fARB;
    long glUniform2fARB;
    long glUniform3fARB;
    long glUniform4fARB;
    long glUniform1iARB;
    long glUniform2iARB;
    long glUniform3iARB;
    long glUniform4iARB;
    long glUniform1fvARB;
    long glUniform2fvARB;
    long glUniform3fvARB;
    long glUniform4fvARB;
    long glUniform1ivARB;
    long glUniform2ivARB;
    long glUniform3ivARB;
    long glUniform4ivARB;
    long glUniformMatrix2fvARB;
    long glUniformMatrix3fvARB;
    long glUniformMatrix4fvARB;
    long glGetObjectParameterfvARB;
    long glGetObjectParameterivARB;
    long glGetInfoLogARB;
    long glGetAttachedObjectsARB;
    long glGetUniformLocationARB;
    long glGetActiveUniformARB;
    long glGetUniformfvARB;
    long glGetUniformivARB;
    long glGetShaderSourceARB;
    long glNamedStringARB;
    long glDeleteNamedStringARB;
    long glCompileShaderIncludeARB;
    long glIsNamedStringARB;
    long glGetNamedStringARB;
    long glGetNamedStringivARB;
    long glBufferPageCommitmentARB;
    long glTexPageCommitmentARB;
    long glTexturePageCommitmentEXT;
    long glTexBufferARB;
    long glTextureBufferRangeEXT;
    long glCompressedTexImage1DARB;
    long glCompressedTexImage2DARB;
    long glCompressedTexImage3DARB;
    long glCompressedTexSubImage1DARB;
    long glCompressedTexSubImage2DARB;
    long glCompressedTexSubImage3DARB;
    long glGetCompressedTexImageARB;
    long glTextureStorage1DEXT;
    long glTextureStorage2DEXT;
    long glTextureStorage3DEXT;
    long glTextureStorage2DMultisampleEXT;
    long glTextureStorage3DMultisampleEXT;
    long glLoadTransposeMatrixfARB;
    long glMultTransposeMatrixfARB;
    long glVertexArrayVertexAttribLOffsetEXT;
    long glWeightbvARB;
    long glWeightsvARB;
    long glWeightivARB;
    long glWeightfvARB;
    long glWeightdvARB;
    long glWeightubvARB;
    long glWeightusvARB;
    long glWeightuivARB;
    long glWeightPointerARB;
    long glVertexBlendARB;
    long glVertexAttrib1sARB;
    long glVertexAttrib1fARB;
    long glVertexAttrib1dARB;
    long glVertexAttrib2sARB;
    long glVertexAttrib2fARB;
    long glVertexAttrib2dARB;
    long glVertexAttrib3sARB;
    long glVertexAttrib3fARB;
    long glVertexAttrib3dARB;
    long glVertexAttrib4sARB;
    long glVertexAttrib4fARB;
    long glVertexAttrib4dARB;
    long glVertexAttrib4NubARB;
    long glVertexAttribPointerARB;
    long glEnableVertexAttribArrayARB;
    long glDisableVertexAttribArrayARB;
    long glBindAttribLocationARB;
    long glGetActiveAttribARB;
    long glGetAttribLocationARB;
    long glGetVertexAttribfvARB;
    long glGetVertexAttribdvARB;
    long glGetVertexAttribivARB;
    long glGetVertexAttribPointervARB;
    long glWindowPos2fARB;
    long glWindowPos2dARB;
    long glWindowPos2iARB;
    long glWindowPos2sARB;
    long glWindowPos3fARB;
    long glWindowPos3dARB;
    long glWindowPos3iARB;
    long glWindowPos3sARB;
    long glDrawBuffersATI;
    long glElementPointerATI;
    long glDrawElementArrayATI;
    long glDrawRangeElementArrayATI;
    long glTexBumpParameterfvATI;
    long glTexBumpParameterivATI;
    long glGetTexBumpParameterfvATI;
    long glGetTexBumpParameterivATI;
    long glGenFragmentShadersATI;
    long glBindFragmentShaderATI;
    long glDeleteFragmentShaderATI;
    long glBeginFragmentShaderATI;
    long glEndFragmentShaderATI;
    long glPassTexCoordATI;
    long glSampleMapATI;
    long glColorFragmentOp1ATI;
    long glColorFragmentOp2ATI;
    long glColorFragmentOp3ATI;
    long glAlphaFragmentOp1ATI;
    long glAlphaFragmentOp2ATI;
    long glAlphaFragmentOp3ATI;
    long glSetFragmentShaderConstantATI;
    long glMapObjectBufferATI;
    long glUnmapObjectBufferATI;
    long glPNTrianglesfATI;
    long glPNTrianglesiATI;
    long glStencilOpSeparateATI;
    long glStencilFuncSeparateATI;
    long glNewObjectBufferATI;
    long glIsObjectBufferATI;
    long glUpdateObjectBufferATI;
    long glGetObjectBufferfvATI;
    long glGetObjectBufferivATI;
    long glFreeObjectBufferATI;
    long glArrayObjectATI;
    long glGetArrayObjectfvATI;
    long glGetArrayObjectivATI;
    long glVariantArrayObjectATI;
    long glGetVariantArrayObjectfvATI;
    long glGetVariantArrayObjectivATI;
    long glVertexAttribArrayObjectATI;
    long glGetVertexAttribArrayObjectfvATI;
    long glGetVertexAttribArrayObjectivATI;
    long glVertexStream2fATI;
    long glVertexStream2dATI;
    long glVertexStream2iATI;
    long glVertexStream2sATI;
    long glVertexStream3fATI;
    long glVertexStream3dATI;
    long glVertexStream3iATI;
    long glVertexStream3sATI;
    long glVertexStream4fATI;
    long glVertexStream4dATI;
    long glVertexStream4iATI;
    long glVertexStream4sATI;
    long glNormalStream3bATI;
    long glNormalStream3fATI;
    long glNormalStream3dATI;
    long glNormalStream3iATI;
    long glNormalStream3sATI;
    long glClientActiveVertexStreamATI;
    long glVertexBlendEnvfATI;
    long glVertexBlendEnviATI;
    long glUniformBufferEXT;
    long glGetUniformBufferSizeEXT;
    long glGetUniformOffsetEXT;
    long glBlendColorEXT;
    long glBlendEquationSeparateEXT;
    long glBlendFuncSeparateEXT;
    long glBlendEquationEXT;
    long glLockArraysEXT;
    long glUnlockArraysEXT;
    long glDepthBoundsEXT;
    long glClientAttribDefaultEXT;
    long glPushClientAttribDefaultEXT;
    long glMatrixLoadfEXT;
    long glMatrixLoaddEXT;
    long glMatrixMultfEXT;
    long glMatrixMultdEXT;
    long glMatrixLoadIdentityEXT;
    long glMatrixRotatefEXT;
    long glMatrixRotatedEXT;
    long glMatrixScalefEXT;
    long glMatrixScaledEXT;
    long glMatrixTranslatefEXT;
    long glMatrixTranslatedEXT;
    long glMatrixOrthoEXT;
    long glMatrixFrustumEXT;
    long glMatrixPushEXT;
    long glMatrixPopEXT;
    long glTextureParameteriEXT;
    long glTextureParameterivEXT;
    long glTextureParameterfEXT;
    long glTextureParameterfvEXT;
    long glTextureImage1DEXT;
    long glTextureImage2DEXT;
    long glTextureSubImage1DEXT;
    long glTextureSubImage2DEXT;
    long glCopyTextureImage1DEXT;
    long glCopyTextureImage2DEXT;
    long glCopyTextureSubImage1DEXT;
    long glCopyTextureSubImage2DEXT;
    long glGetTextureImageEXT;
    long glGetTextureParameterfvEXT;
    long glGetTextureParameterivEXT;
    long glGetTextureLevelParameterfvEXT;
    long glGetTextureLevelParameterivEXT;
    long glTextureImage3DEXT;
    long glTextureSubImage3DEXT;
    long glCopyTextureSubImage3DEXT;
    long glBindMultiTextureEXT;
    long glMultiTexCoordPointerEXT;
    long glMultiTexEnvfEXT;
    long glMultiTexEnvfvEXT;
    long glMultiTexEnviEXT;
    long glMultiTexEnvivEXT;
    long glMultiTexGendEXT;
    long glMultiTexGendvEXT;
    long glMultiTexGenfEXT;
    long glMultiTexGenfvEXT;
    long glMultiTexGeniEXT;
    long glMultiTexGenivEXT;
    long glGetMultiTexEnvfvEXT;
    long glGetMultiTexEnvivEXT;
    long glGetMultiTexGendvEXT;
    long glGetMultiTexGenfvEXT;
    long glGetMultiTexGenivEXT;
    long glMultiTexParameteriEXT;
    long glMultiTexParameterivEXT;
    long glMultiTexParameterfEXT;
    long glMultiTexParameterfvEXT;
    long glMultiTexImage1DEXT;
    long glMultiTexImage2DEXT;
    long glMultiTexSubImage1DEXT;
    long glMultiTexSubImage2DEXT;
    long glCopyMultiTexImage1DEXT;
    long glCopyMultiTexImage2DEXT;
    long glCopyMultiTexSubImage1DEXT;
    long glCopyMultiTexSubImage2DEXT;
    long glGetMultiTexImageEXT;
    long glGetMultiTexParameterfvEXT;
    long glGetMultiTexParameterivEXT;
    long glGetMultiTexLevelParameterfvEXT;
    long glGetMultiTexLevelParameterivEXT;
    long glMultiTexImage3DEXT;
    long glMultiTexSubImage3DEXT;
    long glCopyMultiTexSubImage3DEXT;
    long glEnableClientStateIndexedEXT;
    long glDisableClientStateIndexedEXT;
    long glEnableClientStateiEXT;
    long glDisableClientStateiEXT;
    long glGetFloatIndexedvEXT;
    long glGetDoubleIndexedvEXT;
    long glGetPointerIndexedvEXT;
    long glGetFloati_vEXT;
    long glGetDoublei_vEXT;
    long glGetPointeri_vEXT;
    long glNamedProgramStringEXT;
    long glNamedProgramLocalParameter4dEXT;
    long glNamedProgramLocalParameter4dvEXT;
    long glNamedProgramLocalParameter4fEXT;
    long glNamedProgramLocalParameter4fvEXT;
    long glGetNamedProgramLocalParameterdvEXT;
    long glGetNamedProgramLocalParameterfvEXT;
    long glGetNamedProgramivEXT;
    long glGetNamedProgramStringEXT;
    long glCompressedTextureImage3DEXT;
    long glCompressedTextureImage2DEXT;
    long glCompressedTextureImage1DEXT;
    long glCompressedTextureSubImage3DEXT;
    long glCompressedTextureSubImage2DEXT;
    long glCompressedTextureSubImage1DEXT;
    long glGetCompressedTextureImageEXT;
    long glCompressedMultiTexImage3DEXT;
    long glCompressedMultiTexImage2DEXT;
    long glCompressedMultiTexImage1DEXT;
    long glCompressedMultiTexSubImage3DEXT;
    long glCompressedMultiTexSubImage2DEXT;
    long glCompressedMultiTexSubImage1DEXT;
    long glGetCompressedMultiTexImageEXT;
    long glMatrixLoadTransposefEXT;
    long glMatrixLoadTransposedEXT;
    long glMatrixMultTransposefEXT;
    long glMatrixMultTransposedEXT;
    long glNamedBufferDataEXT;
    long glNamedBufferSubDataEXT;
    long glMapNamedBufferEXT;
    long glUnmapNamedBufferEXT;
    long glGetNamedBufferParameterivEXT;
    long glGetNamedBufferPointervEXT;
    long glGetNamedBufferSubDataEXT;
    long glProgramUniform1fEXT;
    long glProgramUniform2fEXT;
    long glProgramUniform3fEXT;
    long glProgramUniform4fEXT;
    long glProgramUniform1iEXT;
    long glProgramUniform2iEXT;
    long glProgramUniform3iEXT;
    long glProgramUniform4iEXT;
    long glProgramUniform1fvEXT;
    long glProgramUniform2fvEXT;
    long glProgramUniform3fvEXT;
    long glProgramUniform4fvEXT;
    long glProgramUniform1ivEXT;
    long glProgramUniform2ivEXT;
    long glProgramUniform3ivEXT;
    long glProgramUniform4ivEXT;
    long glProgramUniformMatrix2fvEXT;
    long glProgramUniformMatrix3fvEXT;
    long glProgramUniformMatrix4fvEXT;
    long glProgramUniformMatrix2x3fvEXT;
    long glProgramUniformMatrix3x2fvEXT;
    long glProgramUniformMatrix2x4fvEXT;
    long glProgramUniformMatrix4x2fvEXT;
    long glProgramUniformMatrix3x4fvEXT;
    long glProgramUniformMatrix4x3fvEXT;
    long glTextureBufferEXT;
    long glMultiTexBufferEXT;
    long glTextureParameterIivEXT;
    long glTextureParameterIuivEXT;
    long glGetTextureParameterIivEXT;
    long glGetTextureParameterIuivEXT;
    long glMultiTexParameterIivEXT;
    long glMultiTexParameterIuivEXT;
    long glGetMultiTexParameterIivEXT;
    long glGetMultiTexParameterIuivEXT;
    long glProgramUniform1uiEXT;
    long glProgramUniform2uiEXT;
    long glProgramUniform3uiEXT;
    long glProgramUniform4uiEXT;
    long glProgramUniform1uivEXT;
    long glProgramUniform2uivEXT;
    long glProgramUniform3uivEXT;
    long glProgramUniform4uivEXT;
    long glNamedProgramLocalParameters4fvEXT;
    long glNamedProgramLocalParameterI4iEXT;
    long glNamedProgramLocalParameterI4ivEXT;
    long glNamedProgramLocalParametersI4ivEXT;
    long glNamedProgramLocalParameterI4uiEXT;
    long glNamedProgramLocalParameterI4uivEXT;
    long glNamedProgramLocalParametersI4uivEXT;
    long glGetNamedProgramLocalParameterIivEXT;
    long glGetNamedProgramLocalParameterIuivEXT;
    long glNamedRenderbufferStorageEXT;
    long glGetNamedRenderbufferParameterivEXT;
    long glNamedRenderbufferStorageMultisampleEXT;
    long glNamedRenderbufferStorageMultisampleCoverageEXT;
    long glCheckNamedFramebufferStatusEXT;
    long glNamedFramebufferTexture1DEXT;
    long glNamedFramebufferTexture2DEXT;
    long glNamedFramebufferTexture3DEXT;
    long glNamedFramebufferRenderbufferEXT;
    long glGetNamedFramebufferAttachmentParameterivEXT;
    long glGenerateTextureMipmapEXT;
    long glGenerateMultiTexMipmapEXT;
    long glFramebufferDrawBufferEXT;
    long glFramebufferDrawBuffersEXT;
    long glFramebufferReadBufferEXT;
    long glGetFramebufferParameterivEXT;
    long glNamedCopyBufferSubDataEXT;
    long glNamedFramebufferTextureEXT;
    long glNamedFramebufferTextureLayerEXT;
    long glNamedFramebufferTextureFaceEXT;
    long glTextureRenderbufferEXT;
    long glMultiTexRenderbufferEXT;
    long glVertexArrayVertexOffsetEXT;
    long glVertexArrayColorOffsetEXT;
    long glVertexArrayEdgeFlagOffsetEXT;
    long glVertexArrayIndexOffsetEXT;
    long glVertexArrayNormalOffsetEXT;
    long glVertexArrayTexCoordOffsetEXT;
    long glVertexArrayMultiTexCoordOffsetEXT;
    long glVertexArrayFogCoordOffsetEXT;
    long glVertexArraySecondaryColorOffsetEXT;
    long glVertexArrayVertexAttribOffsetEXT;
    long glVertexArrayVertexAttribIOffsetEXT;
    long glEnableVertexArrayEXT;
    long glDisableVertexArrayEXT;
    long glEnableVertexArrayAttribEXT;
    long glDisableVertexArrayAttribEXT;
    long glGetVertexArrayIntegervEXT;
    long glGetVertexArrayPointervEXT;
    long glGetVertexArrayIntegeri_vEXT;
    long glGetVertexArrayPointeri_vEXT;
    long glMapNamedBufferRangeEXT;
    long glFlushMappedNamedBufferRangeEXT;
    long glColorMaskIndexedEXT;
    long glGetBooleanIndexedvEXT;
    long glGetIntegerIndexedvEXT;
    long glEnableIndexedEXT;
    long glDisableIndexedEXT;
    long glIsEnabledIndexedEXT;
    long glDrawArraysInstancedEXT;
    long glDrawElementsInstancedEXT;
    long glDrawRangeElementsEXT;
    long glFogCoordfEXT;
    long glFogCoorddEXT;
    long glFogCoordPointerEXT;
    long glBlitFramebufferEXT;
    long glRenderbufferStorageMultisampleEXT;
    long glIsRenderbufferEXT;
    long glBindRenderbufferEXT;
    long glDeleteRenderbuffersEXT;
    long glGenRenderbuffersEXT;
    long glRenderbufferStorageEXT;
    long glGetRenderbufferParameterivEXT;
    long glIsFramebufferEXT;
    long glBindFramebufferEXT;
    long glDeleteFramebuffersEXT;
    long glGenFramebuffersEXT;
    long glCheckFramebufferStatusEXT;
    long glFramebufferTexture1DEXT;
    long glFramebufferTexture2DEXT;
    long glFramebufferTexture3DEXT;
    long glFramebufferRenderbufferEXT;
    long glGetFramebufferAttachmentParameterivEXT;
    long glGenerateMipmapEXT;
    long glProgramParameteriEXT;
    long glFramebufferTextureEXT;
    long glFramebufferTextureLayerEXT;
    long glFramebufferTextureFaceEXT;
    long glProgramEnvParameters4fvEXT;
    long glProgramLocalParameters4fvEXT;
    long glVertexAttribI1iEXT;
    long glVertexAttribI2iEXT;
    long glVertexAttribI3iEXT;
    long glVertexAttribI4iEXT;
    long glVertexAttribI1uiEXT;
    long glVertexAttribI2uiEXT;
    long glVertexAttribI3uiEXT;
    long glVertexAttribI4uiEXT;
    long glVertexAttribI1ivEXT;
    long glVertexAttribI2ivEXT;
    long glVertexAttribI3ivEXT;
    long glVertexAttribI4ivEXT;
    long glVertexAttribI1uivEXT;
    long glVertexAttribI2uivEXT;
    long glVertexAttribI3uivEXT;
    long glVertexAttribI4uivEXT;
    long glVertexAttribI4bvEXT;
    long glVertexAttribI4svEXT;
    long glVertexAttribI4ubvEXT;
    long glVertexAttribI4usvEXT;
    long glVertexAttribIPointerEXT;
    long glGetVertexAttribIivEXT;
    long glGetVertexAttribIuivEXT;
    long glUniform1uiEXT;
    long glUniform2uiEXT;
    long glUniform3uiEXT;
    long glUniform4uiEXT;
    long glUniform1uivEXT;
    long glUniform2uivEXT;
    long glUniform3uivEXT;
    long glUniform4uivEXT;
    long glGetUniformuivEXT;
    long glBindFragDataLocationEXT;
    long glGetFragDataLocationEXT;
    long glMultiDrawArraysEXT;
    long glColorTableEXT;
    long glColorSubTableEXT;
    long glGetColorTableEXT;
    long glGetColorTableParameterivEXT;
    long glGetColorTableParameterfvEXT;
    long glPointParameterfEXT;
    long glPointParameterfvEXT;
    long glProvokingVertexEXT;
    long glSecondaryColor3bEXT;
    long glSecondaryColor3fEXT;
    long glSecondaryColor3dEXT;
    long glSecondaryColor3ubEXT;
    long glSecondaryColorPointerEXT;
    long glUseShaderProgramEXT;
    long glActiveProgramEXT;
    long glCreateShaderProgramEXT;
    long glBindImageTextureEXT;
    long glMemoryBarrierEXT;
    long glStencilClearTagEXT;
    long glActiveStencilFaceEXT;
    long glTexBufferEXT;
    long glClearColorIiEXT;
    long glClearColorIuiEXT;
    long glTexParameterIivEXT;
    long glTexParameterIuivEXT;
    long glGetTexParameterIivEXT;
    long glGetTexParameterIuivEXT;
    long glGetQueryObjecti64vEXT;
    long glGetQueryObjectui64vEXT;
    long glBindBufferRangeEXT;
    long glBindBufferOffsetEXT;
    long glBindBufferBaseEXT;
    long glBeginTransformFeedbackEXT;
    long glEndTransformFeedbackEXT;
    long glTransformFeedbackVaryingsEXT;
    long glGetTransformFeedbackVaryingEXT;
    long glVertexAttribL1dEXT;
    long glVertexAttribL2dEXT;
    long glVertexAttribL3dEXT;
    long glVertexAttribL4dEXT;
    long glVertexAttribL1dvEXT;
    long glVertexAttribL2dvEXT;
    long glVertexAttribL3dvEXT;
    long glVertexAttribL4dvEXT;
    long glVertexAttribLPointerEXT;
    long glGetVertexAttribLdvEXT;
    long glBeginVertexShaderEXT;
    long glEndVertexShaderEXT;
    long glBindVertexShaderEXT;
    long glGenVertexShadersEXT;
    long glDeleteVertexShaderEXT;
    long glShaderOp1EXT;
    long glShaderOp2EXT;
    long glShaderOp3EXT;
    long glSwizzleEXT;
    long glWriteMaskEXT;
    long glInsertComponentEXT;
    long glExtractComponentEXT;
    long glGenSymbolsEXT;
    long glSetInvariantEXT;
    long glSetLocalConstantEXT;
    long glVariantbvEXT;
    long glVariantsvEXT;
    long glVariantivEXT;
    long glVariantfvEXT;
    long glVariantdvEXT;
    long glVariantubvEXT;
    long glVariantusvEXT;
    long glVariantuivEXT;
    long glVariantPointerEXT;
    long glEnableVariantClientStateEXT;
    long glDisableVariantClientStateEXT;
    long glBindLightParameterEXT;
    long glBindMaterialParameterEXT;
    long glBindTexGenParameterEXT;
    long glBindTextureUnitParameterEXT;
    long glBindParameterEXT;
    long glIsVariantEnabledEXT;
    long glGetVariantBooleanvEXT;
    long glGetVariantIntegervEXT;
    long glGetVariantFloatvEXT;
    long glGetVariantPointervEXT;
    long glGetInvariantBooleanvEXT;
    long glGetInvariantIntegervEXT;
    long glGetInvariantFloatvEXT;
    long glGetLocalConstantBooleanvEXT;
    long glGetLocalConstantIntegervEXT;
    long glGetLocalConstantFloatvEXT;
    long glVertexWeightfEXT;
    long glVertexWeightPointerEXT;
    long glAccum;
    long glAlphaFunc;
    long glClearColor;
    long glClearAccum;
    long glClear;
    long glCallLists;
    long glCallList;
    long glBlendFunc;
    long glBitmap;
    long glBindTexture;
    long glPrioritizeTextures;
    long glAreTexturesResident;
    long glBegin;
    long glEnd;
    long glArrayElement;
    long glClearDepth;
    long glDeleteLists;
    long glDeleteTextures;
    long glCullFace;
    long glCopyTexSubImage2D;
    long glCopyTexSubImage1D;
    long glCopyTexImage2D;
    long glCopyTexImage1D;
    long glCopyPixels;
    long glColorPointer;
    long glColorMaterial;
    long glColorMask;
    long glColor3b;
    long glColor3f;
    long glColor3d;
    long glColor3ub;
    long glColor4b;
    long glColor4f;
    long glColor4d;
    long glColor4ub;
    long glClipPlane;
    long glClearStencil;
    long glEvalPoint1;
    long glEvalPoint2;
    long glEvalMesh1;
    long glEvalMesh2;
    long glEvalCoord1f;
    long glEvalCoord1d;
    long glEvalCoord2f;
    long glEvalCoord2d;
    long glEnableClientState;
    long glDisableClientState;
    long glEnable;
    long glDisable;
    long glEdgeFlagPointer;
    long glEdgeFlag;
    long glDrawPixels;
    long glDrawElements;
    long glDrawBuffer;
    long glDrawArrays;
    long glDepthRange;
    long glDepthMask;
    long glDepthFunc;
    long glFeedbackBuffer;
    long glGetPixelMapfv;
    long glGetPixelMapuiv;
    long glGetPixelMapusv;
    long glGetMaterialfv;
    long glGetMaterialiv;
    long glGetMapfv;
    long glGetMapdv;
    long glGetMapiv;
    long glGetLightfv;
    long glGetLightiv;
    long glGetError;
    long glGetClipPlane;
    long glGetBooleanv;
    long glGetDoublev;
    long glGetFloatv;
    long glGetIntegerv;
    long glGenTextures;
    long glGenLists;
    long glFrustum;
    long glFrontFace;
    long glFogf;
    long glFogi;
    long glFogfv;
    long glFogiv;
    long glFlush;
    long glFinish;
    long glGetPointerv;
    long glIsEnabled;
    long glInterleavedArrays;
    long glInitNames;
    long glHint;
    long glGetTexParameterfv;
    long glGetTexParameteriv;
    long glGetTexLevelParameterfv;
    long glGetTexLevelParameteriv;
    long glGetTexImage;
    long glGetTexGeniv;
    long glGetTexGenfv;
    long glGetTexGendv;
    long glGetTexEnviv;
    long glGetTexEnvfv;
    long glGetString;
    long glGetPolygonStipple;
    long glIsList;
    long glMaterialf;
    long glMateriali;
    long glMaterialfv;
    long glMaterialiv;
    long glMapGrid1f;
    long glMapGrid1d;
    long glMapGrid2f;
    long glMapGrid2d;
    long glMap2f;
    long glMap2d;
    long glMap1f;
    long glMap1d;
    long glLogicOp;
    long glLoadName;
    long glLoadMatrixf;
    long glLoadMatrixd;
    long glLoadIdentity;
    long glListBase;
    long glLineWidth;
    long glLineStipple;
    long glLightModelf;
    long glLightModeli;
    long glLightModelfv;
    long glLightModeliv;
    long glLightf;
    long glLighti;
    long glLightfv;
    long glLightiv;
    long glIsTexture;
    long glMatrixMode;
    long glPolygonStipple;
    long glPolygonOffset;
    long glPolygonMode;
    long glPointSize;
    long glPixelZoom;
    long glPixelTransferf;
    long glPixelTransferi;
    long glPixelStoref;
    long glPixelStorei;
    long glPixelMapfv;
    long glPixelMapuiv;
    long glPixelMapusv;
    long glPassThrough;
    long glOrtho;
    long glNormalPointer;
    long glNormal3b;
    long glNormal3f;
    long glNormal3d;
    long glNormal3i;
    long glNewList;
    long glEndList;
    long glMultMatrixf;
    long glMultMatrixd;
    long glShadeModel;
    long glSelectBuffer;
    long glScissor;
    long glScalef;
    long glScaled;
    long glRotatef;
    long glRotated;
    long glRenderMode;
    long glRectf;
    long glRectd;
    long glRecti;
    long glReadPixels;
    long glReadBuffer;
    long glRasterPos2f;
    long glRasterPos2d;
    long glRasterPos2i;
    long glRasterPos3f;
    long glRasterPos3d;
    long glRasterPos3i;
    long glRasterPos4f;
    long glRasterPos4d;
    long glRasterPos4i;
    long glPushName;
    long glPopName;
    long glPushMatrix;
    long glPopMatrix;
    long glPushClientAttrib;
    long glPopClientAttrib;
    long glPushAttrib;
    long glPopAttrib;
    long glStencilFunc;
    long glVertexPointer;
    long glVertex2f;
    long glVertex2d;
    long glVertex2i;
    long glVertex3f;
    long glVertex3d;
    long glVertex3i;
    long glVertex4f;
    long glVertex4d;
    long glVertex4i;
    long glTranslatef;
    long glTranslated;
    long glTexImage1D;
    long glTexImage2D;
    long glTexSubImage1D;
    long glTexSubImage2D;
    long glTexParameterf;
    long glTexParameteri;
    long glTexParameterfv;
    long glTexParameteriv;
    long glTexGenf;
    long glTexGend;
    long glTexGenfv;
    long glTexGendv;
    long glTexGeni;
    long glTexGeniv;
    long glTexEnvf;
    long glTexEnvi;
    long glTexEnvfv;
    long glTexEnviv;
    long glTexCoordPointer;
    long glTexCoord1f;
    long glTexCoord1d;
    long glTexCoord2f;
    long glTexCoord2d;
    long glTexCoord3f;
    long glTexCoord3d;
    long glTexCoord4f;
    long glTexCoord4d;
    long glStencilOp;
    long glStencilMask;
    long glViewport;
    long glDrawRangeElements;
    long glTexImage3D;
    long glTexSubImage3D;
    long glCopyTexSubImage3D;
    long glActiveTexture;
    long glClientActiveTexture;
    long glCompressedTexImage1D;
    long glCompressedTexImage2D;
    long glCompressedTexImage3D;
    long glCompressedTexSubImage1D;
    long glCompressedTexSubImage2D;
    long glCompressedTexSubImage3D;
    long glGetCompressedTexImage;
    long glMultiTexCoord1f;
    long glMultiTexCoord1d;
    long glMultiTexCoord2f;
    long glMultiTexCoord2d;
    long glMultiTexCoord3f;
    long glMultiTexCoord3d;
    long glMultiTexCoord4f;
    long glMultiTexCoord4d;
    long glLoadTransposeMatrixf;
    long glLoadTransposeMatrixd;
    long glMultTransposeMatrixf;
    long glMultTransposeMatrixd;
    long glSampleCoverage;
    long glBlendEquation;
    long glBlendColor;
    long glFogCoordf;
    long glFogCoordd;
    long glFogCoordPointer;
    long glMultiDrawArrays;
    long glPointParameteri;
    long glPointParameterf;
    long glPointParameteriv;
    long glPointParameterfv;
    long glSecondaryColor3b;
    long glSecondaryColor3f;
    long glSecondaryColor3d;
    long glSecondaryColor3ub;
    long glSecondaryColorPointer;
    long glBlendFuncSeparate;
    long glWindowPos2f;
    long glWindowPos2d;
    long glWindowPos2i;
    long glWindowPos3f;
    long glWindowPos3d;
    long glWindowPos3i;
    long glBindBuffer;
    long glDeleteBuffers;
    long glGenBuffers;
    long glIsBuffer;
    long glBufferData;
    long glBufferSubData;
    long glGetBufferSubData;
    long glMapBuffer;
    long glUnmapBuffer;
    long glGetBufferParameteriv;
    long glGetBufferPointerv;
    long glGenQueries;
    long glDeleteQueries;
    long glIsQuery;
    long glBeginQuery;
    long glEndQuery;
    long glGetQueryiv;
    long glGetQueryObjectiv;
    long glGetQueryObjectuiv;
    long glShaderSource;
    long glCreateShader;
    long glIsShader;
    long glCompileShader;
    long glDeleteShader;
    long glCreateProgram;
    long glIsProgram;
    long glAttachShader;
    long glDetachShader;
    long glLinkProgram;
    long glUseProgram;
    long glValidateProgram;
    long glDeleteProgram;
    long glUniform1f;
    long glUniform2f;
    long glUniform3f;
    long glUniform4f;
    long glUniform1i;
    long glUniform2i;
    long glUniform3i;
    long glUniform4i;
    long glUniform1fv;
    long glUniform2fv;
    long glUniform3fv;
    long glUniform4fv;
    long glUniform1iv;
    long glUniform2iv;
    long glUniform3iv;
    long glUniform4iv;
    long glUniformMatrix2fv;
    long glUniformMatrix3fv;
    long glUniformMatrix4fv;
    long glGetShaderiv;
    long glGetProgramiv;
    long glGetShaderInfoLog;
    long glGetProgramInfoLog;
    long glGetAttachedShaders;
    long glGetUniformLocation;
    long glGetActiveUniform;
    long glGetUniformfv;
    long glGetUniformiv;
    long glGetShaderSource;
    long glVertexAttrib1s;
    long glVertexAttrib1f;
    long glVertexAttrib1d;
    long glVertexAttrib2s;
    long glVertexAttrib2f;
    long glVertexAttrib2d;
    long glVertexAttrib3s;
    long glVertexAttrib3f;
    long glVertexAttrib3d;
    long glVertexAttrib4s;
    long glVertexAttrib4f;
    long glVertexAttrib4d;
    long glVertexAttrib4Nub;
    long glVertexAttribPointer;
    long glEnableVertexAttribArray;
    long glDisableVertexAttribArray;
    long glGetVertexAttribfv;
    long glGetVertexAttribdv;
    long glGetVertexAttribiv;
    long glGetVertexAttribPointerv;
    long glBindAttribLocation;
    long glGetActiveAttrib;
    long glGetAttribLocation;
    long glDrawBuffers;
    long glStencilOpSeparate;
    long glStencilFuncSeparate;
    long glStencilMaskSeparate;
    long glBlendEquationSeparate;
    long glUniformMatrix2x3fv;
    long glUniformMatrix3x2fv;
    long glUniformMatrix2x4fv;
    long glUniformMatrix4x2fv;
    long glUniformMatrix3x4fv;
    long glUniformMatrix4x3fv;
    long glGetStringi;
    long glClearBufferfv;
    long glClearBufferiv;
    long glClearBufferuiv;
    long glClearBufferfi;
    long glVertexAttribI1i;
    long glVertexAttribI2i;
    long glVertexAttribI3i;
    long glVertexAttribI4i;
    long glVertexAttribI1ui;
    long glVertexAttribI2ui;
    long glVertexAttribI3ui;
    long glVertexAttribI4ui;
    long glVertexAttribI1iv;
    long glVertexAttribI2iv;
    long glVertexAttribI3iv;
    long glVertexAttribI4iv;
    long glVertexAttribI1uiv;
    long glVertexAttribI2uiv;
    long glVertexAttribI3uiv;
    long glVertexAttribI4uiv;
    long glVertexAttribI4bv;
    long glVertexAttribI4sv;
    long glVertexAttribI4ubv;
    long glVertexAttribI4usv;
    long glVertexAttribIPointer;
    long glGetVertexAttribIiv;
    long glGetVertexAttribIuiv;
    long glUniform1ui;
    long glUniform2ui;
    long glUniform3ui;
    long glUniform4ui;
    long glUniform1uiv;
    long glUniform2uiv;
    long glUniform3uiv;
    long glUniform4uiv;
    long glGetUniformuiv;
    long glBindFragDataLocation;
    long glGetFragDataLocation;
    long glBeginConditionalRender;
    long glEndConditionalRender;
    long glMapBufferRange;
    long glFlushMappedBufferRange;
    long glClampColor;
    long glIsRenderbuffer;
    long glBindRenderbuffer;
    long glDeleteRenderbuffers;
    long glGenRenderbuffers;
    long glRenderbufferStorage;
    long glGetRenderbufferParameteriv;
    long glIsFramebuffer;
    long glBindFramebuffer;
    long glDeleteFramebuffers;
    long glGenFramebuffers;
    long glCheckFramebufferStatus;
    long glFramebufferTexture1D;
    long glFramebufferTexture2D;
    long glFramebufferTexture3D;
    long glFramebufferRenderbuffer;
    long glGetFramebufferAttachmentParameteriv;
    long glGenerateMipmap;
    long glRenderbufferStorageMultisample;
    long glBlitFramebuffer;
    long glTexParameterIiv;
    long glTexParameterIuiv;
    long glGetTexParameterIiv;
    long glGetTexParameterIuiv;
    long glFramebufferTextureLayer;
    long glColorMaski;
    long glGetBooleani_v;
    long glGetIntegeri_v;
    long glEnablei;
    long glDisablei;
    long glIsEnabledi;
    long glBindBufferRange;
    long glBindBufferBase;
    long glBeginTransformFeedback;
    long glEndTransformFeedback;
    long glTransformFeedbackVaryings;
    long glGetTransformFeedbackVarying;
    long glBindVertexArray;
    long glDeleteVertexArrays;
    long glGenVertexArrays;
    long glIsVertexArray;
    long glDrawArraysInstanced;
    long glDrawElementsInstanced;
    long glCopyBufferSubData;
    long glPrimitiveRestartIndex;
    long glTexBuffer;
    long glGetUniformIndices;
    long glGetActiveUniformsiv;
    long glGetActiveUniformName;
    long glGetUniformBlockIndex;
    long glGetActiveUniformBlockiv;
    long glGetActiveUniformBlockName;
    long glUniformBlockBinding;
    long glGetBufferParameteri64v;
    long glDrawElementsBaseVertex;
    long glDrawRangeElementsBaseVertex;
    long glDrawElementsInstancedBaseVertex;
    long glProvokingVertex;
    long glTexImage2DMultisample;
    long glTexImage3DMultisample;
    long glGetMultisamplefv;
    long glSampleMaski;
    long glFramebufferTexture;
    long glFenceSync;
    long glIsSync;
    long glDeleteSync;
    long glClientWaitSync;
    long glWaitSync;
    long glGetInteger64v;
    long glGetInteger64i_v;
    long glGetSynciv;
    long glBindFragDataLocationIndexed;
    long glGetFragDataIndex;
    long glGenSamplers;
    long glDeleteSamplers;
    long glIsSampler;
    long glBindSampler;
    long glSamplerParameteri;
    long glSamplerParameterf;
    long glSamplerParameteriv;
    long glSamplerParameterfv;
    long glSamplerParameterIiv;
    long glSamplerParameterIuiv;
    long glGetSamplerParameteriv;
    long glGetSamplerParameterfv;
    long glGetSamplerParameterIiv;
    long glGetSamplerParameterIuiv;
    long glQueryCounter;
    long glGetQueryObjecti64v;
    long glGetQueryObjectui64v;
    long glVertexAttribDivisor;
    long glVertexP2ui;
    long glVertexP3ui;
    long glVertexP4ui;
    long glVertexP2uiv;
    long glVertexP3uiv;
    long glVertexP4uiv;
    long glTexCoordP1ui;
    long glTexCoordP2ui;
    long glTexCoordP3ui;
    long glTexCoordP4ui;
    long glTexCoordP1uiv;
    long glTexCoordP2uiv;
    long glTexCoordP3uiv;
    long glTexCoordP4uiv;
    long glMultiTexCoordP1ui;
    long glMultiTexCoordP2ui;
    long glMultiTexCoordP3ui;
    long glMultiTexCoordP4ui;
    long glMultiTexCoordP1uiv;
    long glMultiTexCoordP2uiv;
    long glMultiTexCoordP3uiv;
    long glMultiTexCoordP4uiv;
    long glNormalP3ui;
    long glNormalP3uiv;
    long glColorP3ui;
    long glColorP4ui;
    long glColorP3uiv;
    long glColorP4uiv;
    long glSecondaryColorP3ui;
    long glSecondaryColorP3uiv;
    long glVertexAttribP1ui;
    long glVertexAttribP2ui;
    long glVertexAttribP3ui;
    long glVertexAttribP4ui;
    long glVertexAttribP1uiv;
    long glVertexAttribP2uiv;
    long glVertexAttribP3uiv;
    long glVertexAttribP4uiv;
    long glBlendEquationi;
    long glBlendEquationSeparatei;
    long glBlendFunci;
    long glBlendFuncSeparatei;
    long glDrawArraysIndirect;
    long glDrawElementsIndirect;
    long glUniform1d;
    long glUniform2d;
    long glUniform3d;
    long glUniform4d;
    long glUniform1dv;
    long glUniform2dv;
    long glUniform3dv;
    long glUniform4dv;
    long glUniformMatrix2dv;
    long glUniformMatrix3dv;
    long glUniformMatrix4dv;
    long glUniformMatrix2x3dv;
    long glUniformMatrix2x4dv;
    long glUniformMatrix3x2dv;
    long glUniformMatrix3x4dv;
    long glUniformMatrix4x2dv;
    long glUniformMatrix4x3dv;
    long glGetUniformdv;
    long glMinSampleShading;
    long glGetSubroutineUniformLocation;
    long glGetSubroutineIndex;
    long glGetActiveSubroutineUniformiv;
    long glGetActiveSubroutineUniformName;
    long glGetActiveSubroutineName;
    long glUniformSubroutinesuiv;
    long glGetUniformSubroutineuiv;
    long glGetProgramStageiv;
    long glPatchParameteri;
    long glPatchParameterfv;
    long glBindTransformFeedback;
    long glDeleteTransformFeedbacks;
    long glGenTransformFeedbacks;
    long glIsTransformFeedback;
    long glPauseTransformFeedback;
    long glResumeTransformFeedback;
    long glDrawTransformFeedback;
    long glDrawTransformFeedbackStream;
    long glBeginQueryIndexed;
    long glEndQueryIndexed;
    long glGetQueryIndexediv;
    long glReleaseShaderCompiler;
    long glShaderBinary;
    long glGetShaderPrecisionFormat;
    long glDepthRangef;
    long glClearDepthf;
    long glGetProgramBinary;
    long glProgramBinary;
    long glProgramParameteri;
    long glUseProgramStages;
    long glActiveShaderProgram;
    long glCreateShaderProgramv;
    long glBindProgramPipeline;
    long glDeleteProgramPipelines;
    long glGenProgramPipelines;
    long glIsProgramPipeline;
    long glGetProgramPipelineiv;
    long glProgramUniform1i;
    long glProgramUniform2i;
    long glProgramUniform3i;
    long glProgramUniform4i;
    long glProgramUniform1f;
    long glProgramUniform2f;
    long glProgramUniform3f;
    long glProgramUniform4f;
    long glProgramUniform1d;
    long glProgramUniform2d;
    long glProgramUniform3d;
    long glProgramUniform4d;
    long glProgramUniform1iv;
    long glProgramUniform2iv;
    long glProgramUniform3iv;
    long glProgramUniform4iv;
    long glProgramUniform1fv;
    long glProgramUniform2fv;
    long glProgramUniform3fv;
    long glProgramUniform4fv;
    long glProgramUniform1dv;
    long glProgramUniform2dv;
    long glProgramUniform3dv;
    long glProgramUniform4dv;
    long glProgramUniform1ui;
    long glProgramUniform2ui;
    long glProgramUniform3ui;
    long glProgramUniform4ui;
    long glProgramUniform1uiv;
    long glProgramUniform2uiv;
    long glProgramUniform3uiv;
    long glProgramUniform4uiv;
    long glProgramUniformMatrix2fv;
    long glProgramUniformMatrix3fv;
    long glProgramUniformMatrix4fv;
    long glProgramUniformMatrix2dv;
    long glProgramUniformMatrix3dv;
    long glProgramUniformMatrix4dv;
    long glProgramUniformMatrix2x3fv;
    long glProgramUniformMatrix3x2fv;
    long glProgramUniformMatrix2x4fv;
    long glProgramUniformMatrix4x2fv;
    long glProgramUniformMatrix3x4fv;
    long glProgramUniformMatrix4x3fv;
    long glProgramUniformMatrix2x3dv;
    long glProgramUniformMatrix3x2dv;
    long glProgramUniformMatrix2x4dv;
    long glProgramUniformMatrix4x2dv;
    long glProgramUniformMatrix3x4dv;
    long glProgramUniformMatrix4x3dv;
    long glValidateProgramPipeline;
    long glGetProgramPipelineInfoLog;
    long glVertexAttribL1d;
    long glVertexAttribL2d;
    long glVertexAttribL3d;
    long glVertexAttribL4d;
    long glVertexAttribL1dv;
    long glVertexAttribL2dv;
    long glVertexAttribL3dv;
    long glVertexAttribL4dv;
    long glVertexAttribLPointer;
    long glGetVertexAttribLdv;
    long glViewportArrayv;
    long glViewportIndexedf;
    long glViewportIndexedfv;
    long glScissorArrayv;
    long glScissorIndexed;
    long glScissorIndexedv;
    long glDepthRangeArrayv;
    long glDepthRangeIndexed;
    long glGetFloati_v;
    long glGetDoublei_v;
    long glGetActiveAtomicCounterBufferiv;
    long glTexStorage1D;
    long glTexStorage2D;
    long glTexStorage3D;
    long glDrawTransformFeedbackInstanced;
    long glDrawTransformFeedbackStreamInstanced;
    long glDrawArraysInstancedBaseInstance;
    long glDrawElementsInstancedBaseInstance;
    long glDrawElementsInstancedBaseVertexBaseInstance;
    long glBindImageTexture;
    long glMemoryBarrier;
    long glGetInternalformativ;
    long glClearBufferData;
    long glClearBufferSubData;
    long glDispatchCompute;
    long glDispatchComputeIndirect;
    long glCopyImageSubData;
    long glDebugMessageControl;
    long glDebugMessageInsert;
    long glDebugMessageCallback;
    long glGetDebugMessageLog;
    long glPushDebugGroup;
    long glPopDebugGroup;
    long glObjectLabel;
    long glGetObjectLabel;
    long glObjectPtrLabel;
    long glGetObjectPtrLabel;
    long glFramebufferParameteri;
    long glGetFramebufferParameteriv;
    long glGetInternalformati64v;
    long glInvalidateTexSubImage;
    long glInvalidateTexImage;
    long glInvalidateBufferSubData;
    long glInvalidateBufferData;
    long glInvalidateFramebuffer;
    long glInvalidateSubFramebuffer;
    long glMultiDrawArraysIndirect;
    long glMultiDrawElementsIndirect;
    long glGetProgramInterfaceiv;
    long glGetProgramResourceIndex;
    long glGetProgramResourceName;
    long glGetProgramResourceiv;
    long glGetProgramResourceLocation;
    long glGetProgramResourceLocationIndex;
    long glShaderStorageBlockBinding;
    long glTexBufferRange;
    long glTexStorage2DMultisample;
    long glTexStorage3DMultisample;
    long glTextureView;
    long glBindVertexBuffer;
    long glVertexAttribFormat;
    long glVertexAttribIFormat;
    long glVertexAttribLFormat;
    long glVertexAttribBinding;
    long glVertexBindingDivisor;
    long glBufferStorage;
    long glClearTexImage;
    long glClearTexSubImage;
    long glBindBuffersBase;
    long glBindBuffersRange;
    long glBindTextures;
    long glBindSamplers;
    long glBindImageTextures;
    long glBindVertexBuffers;
    long glClipControl;
    long glCreateTransformFeedbacks;
    long glTransformFeedbackBufferBase;
    long glTransformFeedbackBufferRange;
    long glGetTransformFeedbackiv;
    long glGetTransformFeedbacki_v;
    long glGetTransformFeedbacki64_v;
    long glCreateBuffers;
    long glNamedBufferStorage;
    long glNamedBufferData;
    long glNamedBufferSubData;
    long glCopyNamedBufferSubData;
    long glClearNamedBufferData;
    long glClearNamedBufferSubData;
    long glMapNamedBuffer;
    long glMapNamedBufferRange;
    long glUnmapNamedBuffer;
    long glFlushMappedNamedBufferRange;
    long glGetNamedBufferParameteriv;
    long glGetNamedBufferParameteri64v;
    long glGetNamedBufferPointerv;
    long glGetNamedBufferSubData;
    long glCreateFramebuffers;
    long glNamedFramebufferRenderbuffer;
    long glNamedFramebufferParameteri;
    long glNamedFramebufferTexture;
    long glNamedFramebufferTextureLayer;
    long glNamedFramebufferDrawBuffer;
    long glNamedFramebufferDrawBuffers;
    long glNamedFramebufferReadBuffer;
    long glInvalidateNamedFramebufferData;
    long glInvalidateNamedFramebufferSubData;
    long glClearNamedFramebufferiv;
    long glClearNamedFramebufferuiv;
    long glClearNamedFramebufferfv;
    long glClearNamedFramebufferfi;
    long glBlitNamedFramebuffer;
    long glCheckNamedFramebufferStatus;
    long glGetNamedFramebufferParameteriv;
    long glGetNamedFramebufferAttachmentParameteriv;
    long glCreateRenderbuffers;
    long glNamedRenderbufferStorage;
    long glNamedRenderbufferStorageMultisample;
    long glGetNamedRenderbufferParameteriv;
    long glCreateTextures;
    long glTextureBuffer;
    long glTextureBufferRange;
    long glTextureStorage1D;
    long glTextureStorage2D;
    long glTextureStorage3D;
    long glTextureStorage2DMultisample;
    long glTextureStorage3DMultisample;
    long glTextureSubImage1D;
    long glTextureSubImage2D;
    long glTextureSubImage3D;
    long glCompressedTextureSubImage1D;
    long glCompressedTextureSubImage2D;
    long glCompressedTextureSubImage3D;
    long glCopyTextureSubImage1D;
    long glCopyTextureSubImage2D;
    long glCopyTextureSubImage3D;
    long glTextureParameterf;
    long glTextureParameterfv;
    long glTextureParameteri;
    long glTextureParameterIiv;
    long glTextureParameterIuiv;
    long glTextureParameteriv;
    long glGenerateTextureMipmap;
    long glBindTextureUnit;
    long glGetTextureImage;
    long glGetCompressedTextureImage;
    long glGetTextureLevelParameterfv;
    long glGetTextureLevelParameteriv;
    long glGetTextureParameterfv;
    long glGetTextureParameterIiv;
    long glGetTextureParameterIuiv;
    long glGetTextureParameteriv;
    long glCreateVertexArrays;
    long glDisableVertexArrayAttrib;
    long glEnableVertexArrayAttrib;
    long glVertexArrayElementBuffer;
    long glVertexArrayVertexBuffer;
    long glVertexArrayVertexBuffers;
    long glVertexArrayAttribFormat;
    long glVertexArrayAttribIFormat;
    long glVertexArrayAttribLFormat;
    long glVertexArrayAttribBinding;
    long glVertexArrayBindingDivisor;
    long glGetVertexArrayiv;
    long glGetVertexArrayIndexediv;
    long glGetVertexArrayIndexed64iv;
    long glCreateSamplers;
    long glCreateProgramPipelines;
    long glCreateQueries;
    long glMemoryBarrierByRegion;
    long glGetTextureSubImage;
    long glGetCompressedTextureSubImage;
    long glTextureBarrier;
    long glGetGraphicsResetStatus;
    long glReadnPixels;
    long glGetnUniformfv;
    long glGetnUniformiv;
    long glGetnUniformuiv;
    long glFrameTerminatorGREMEDY;
    long glStringMarkerGREMEDY;
    long glMapTexture2DINTEL;
    long glUnmapTexture2DINTEL;
    long glSyncTextureINTEL;
    long glMultiDrawArraysIndirectBindlessNV;
    long glMultiDrawElementsIndirectBindlessNV;
    long glGetTextureHandleNV;
    long glGetTextureSamplerHandleNV;
    long glMakeTextureHandleResidentNV;
    long glMakeTextureHandleNonResidentNV;
    long glGetImageHandleNV;
    long glMakeImageHandleResidentNV;
    long glMakeImageHandleNonResidentNV;
    long glUniformHandleui64NV;
    long glUniformHandleui64vNV;
    long glProgramUniformHandleui64NV;
    long glProgramUniformHandleui64vNV;
    long glIsTextureHandleResidentNV;
    long glIsImageHandleResidentNV;
    long glBlendParameteriNV;
    long glBlendBarrierNV;
    long glBeginConditionalRenderNV;
    long glEndConditionalRenderNV;
    long glCopyImageSubDataNV;
    long glDepthRangedNV;
    long glClearDepthdNV;
    long glDepthBoundsdNV;
    long glDrawTextureNV;
    long glGetMapControlPointsNV;
    long glMapControlPointsNV;
    long glMapParameterfvNV;
    long glMapParameterivNV;
    long glGetMapParameterfvNV;
    long glGetMapParameterivNV;
    long glGetMapAttribParameterfvNV;
    long glGetMapAttribParameterivNV;
    long glEvalMapsNV;
    long glGetMultisamplefvNV;
    long glSampleMaskIndexedNV;
    long glTexRenderbufferNV;
    long glGenFencesNV;
    long glDeleteFencesNV;
    long glSetFenceNV;
    long glTestFenceNV;
    long glFinishFenceNV;
    long glIsFenceNV;
    long glGetFenceivNV;
    long glProgramNamedParameter4fNV;
    long glProgramNamedParameter4dNV;
    long glGetProgramNamedParameterfvNV;
    long glGetProgramNamedParameterdvNV;
    long glRenderbufferStorageMultisampleCoverageNV;
    long glProgramVertexLimitNV;
    long glProgramLocalParameterI4iNV;
    long glProgramLocalParameterI4ivNV;
    long glProgramLocalParametersI4ivNV;
    long glProgramLocalParameterI4uiNV;
    long glProgramLocalParameterI4uivNV;
    long glProgramLocalParametersI4uivNV;
    long glProgramEnvParameterI4iNV;
    long glProgramEnvParameterI4ivNV;
    long glProgramEnvParametersI4ivNV;
    long glProgramEnvParameterI4uiNV;
    long glProgramEnvParameterI4uivNV;
    long glProgramEnvParametersI4uivNV;
    long glGetProgramLocalParameterIivNV;
    long glGetProgramLocalParameterIuivNV;
    long glGetProgramEnvParameterIivNV;
    long glGetProgramEnvParameterIuivNV;
    long glUniform1i64NV;
    long glUniform2i64NV;
    long glUniform3i64NV;
    long glUniform4i64NV;
    long glUniform1i64vNV;
    long glUniform2i64vNV;
    long glUniform3i64vNV;
    long glUniform4i64vNV;
    long glUniform1ui64NV;
    long glUniform2ui64NV;
    long glUniform3ui64NV;
    long glUniform4ui64NV;
    long glUniform1ui64vNV;
    long glUniform2ui64vNV;
    long glUniform3ui64vNV;
    long glUniform4ui64vNV;
    long glGetUniformi64vNV;
    long glGetUniformui64vNV;
    long glProgramUniform1i64NV;
    long glProgramUniform2i64NV;
    long glProgramUniform3i64NV;
    long glProgramUniform4i64NV;
    long glProgramUniform1i64vNV;
    long glProgramUniform2i64vNV;
    long glProgramUniform3i64vNV;
    long glProgramUniform4i64vNV;
    long glProgramUniform1ui64NV;
    long glProgramUniform2ui64NV;
    long glProgramUniform3ui64NV;
    long glProgramUniform4ui64NV;
    long glProgramUniform1ui64vNV;
    long glProgramUniform2ui64vNV;
    long glProgramUniform3ui64vNV;
    long glProgramUniform4ui64vNV;
    long glVertex2hNV;
    long glVertex3hNV;
    long glVertex4hNV;
    long glNormal3hNV;
    long glColor3hNV;
    long glColor4hNV;
    long glTexCoord1hNV;
    long glTexCoord2hNV;
    long glTexCoord3hNV;
    long glTexCoord4hNV;
    long glMultiTexCoord1hNV;
    long glMultiTexCoord2hNV;
    long glMultiTexCoord3hNV;
    long glMultiTexCoord4hNV;
    long glFogCoordhNV;
    long glSecondaryColor3hNV;
    long glVertexWeighthNV;
    long glVertexAttrib1hNV;
    long glVertexAttrib2hNV;
    long glVertexAttrib3hNV;
    long glVertexAttrib4hNV;
    long glVertexAttribs1hvNV;
    long glVertexAttribs2hvNV;
    long glVertexAttribs3hvNV;
    long glVertexAttribs4hvNV;
    long glGenOcclusionQueriesNV;
    long glDeleteOcclusionQueriesNV;
    long glIsOcclusionQueryNV;
    long glBeginOcclusionQueryNV;
    long glEndOcclusionQueryNV;
    long glGetOcclusionQueryuivNV;
    long glGetOcclusionQueryivNV;
    long glProgramBufferParametersfvNV;
    long glProgramBufferParametersIivNV;
    long glProgramBufferParametersIuivNV;
    long glPathCommandsNV;
    long glPathCoordsNV;
    long glPathSubCommandsNV;
    long glPathSubCoordsNV;
    long glPathStringNV;
    long glPathGlyphsNV;
    long glPathGlyphRangeNV;
    long glWeightPathsNV;
    long glCopyPathNV;
    long glInterpolatePathsNV;
    long glTransformPathNV;
    long glPathParameterivNV;
    long glPathParameteriNV;
    long glPathParameterfvNV;
    long glPathParameterfNV;
    long glPathDashArrayNV;
    long glGenPathsNV;
    long glDeletePathsNV;
    long glIsPathNV;
    long glPathStencilFuncNV;
    long glPathStencilDepthOffsetNV;
    long glStencilFillPathNV;
    long glStencilStrokePathNV;
    long glStencilFillPathInstancedNV;
    long glStencilStrokePathInstancedNV;
    long glPathCoverDepthFuncNV;
    long glPathColorGenNV;
    long glPathTexGenNV;
    long glPathFogGenNV;
    long glCoverFillPathNV;
    long glCoverStrokePathNV;
    long glCoverFillPathInstancedNV;
    long glCoverStrokePathInstancedNV;
    long glGetPathParameterivNV;
    long glGetPathParameterfvNV;
    long glGetPathCommandsNV;
    long glGetPathCoordsNV;
    long glGetPathDashArrayNV;
    long glGetPathMetricsNV;
    long glGetPathMetricRangeNV;
    long glGetPathSpacingNV;
    long glGetPathColorGenivNV;
    long glGetPathColorGenfvNV;
    long glGetPathTexGenivNV;
    long glGetPathTexGenfvNV;
    long glIsPointInFillPathNV;
    long glIsPointInStrokePathNV;
    long glGetPathLengthNV;
    long glPointAlongPathNV;
    long glPixelDataRangeNV;
    long glFlushPixelDataRangeNV;
    long glPointParameteriNV;
    long glPointParameterivNV;
    long glPresentFrameKeyedNV;
    long glPresentFrameDualFillNV;
    long glGetVideoivNV;
    long glGetVideouivNV;
    long glGetVideoi64vNV;
    long glGetVideoui64vNV;
    long glPrimitiveRestartNV;
    long glPrimitiveRestartIndexNV;
    long glLoadProgramNV;
    long glBindProgramNV;
    long glDeleteProgramsNV;
    long glGenProgramsNV;
    long glGetProgramivNV;
    long glGetProgramStringNV;
    long glIsProgramNV;
    long glAreProgramsResidentNV;
    long glRequestResidentProgramsNV;
    long glCombinerParameterfNV;
    long glCombinerParameterfvNV;
    long glCombinerParameteriNV;
    long glCombinerParameterivNV;
    long glCombinerInputNV;
    long glCombinerOutputNV;
    long glFinalCombinerInputNV;
    long glGetCombinerInputParameterfvNV;
    long glGetCombinerInputParameterivNV;
    long glGetCombinerOutputParameterfvNV;
    long glGetCombinerOutputParameterivNV;
    long glGetFinalCombinerInputParameterfvNV;
    long glGetFinalCombinerInputParameterivNV;
    long glCombinerStageParameterfvNV;
    long glGetCombinerStageParameterfvNV;
    long glMakeBufferResidentNV;
    long glMakeBufferNonResidentNV;
    long glIsBufferResidentNV;
    long glMakeNamedBufferResidentNV;
    long glMakeNamedBufferNonResidentNV;
    long glIsNamedBufferResidentNV;
    long glGetBufferParameterui64vNV;
    long glGetNamedBufferParameterui64vNV;
    long glGetIntegerui64vNV;
    long glUniformui64NV;
    long glUniformui64vNV;
    long glProgramUniformui64NV;
    long glProgramUniformui64vNV;
    long glTextureBarrierNV;
    long glTexImage2DMultisampleCoverageNV;
    long glTexImage3DMultisampleCoverageNV;
    long glTextureImage2DMultisampleNV;
    long glTextureImage3DMultisampleNV;
    long glTextureImage2DMultisampleCoverageNV;
    long glTextureImage3DMultisampleCoverageNV;
    long glBindBufferRangeNV;
    long glBindBufferOffsetNV;
    long glBindBufferBaseNV;
    long glTransformFeedbackAttribsNV;
    long glTransformFeedbackVaryingsNV;
    long glBeginTransformFeedbackNV;
    long glEndTransformFeedbackNV;
    long glGetVaryingLocationNV;
    long glGetActiveVaryingNV;
    long glActiveVaryingNV;
    long glGetTransformFeedbackVaryingNV;
    long glBindTransformFeedbackNV;
    long glDeleteTransformFeedbacksNV;
    long glGenTransformFeedbacksNV;
    long glIsTransformFeedbackNV;
    long glPauseTransformFeedbackNV;
    long glResumeTransformFeedbackNV;
    long glDrawTransformFeedbackNV;
    long glVertexArrayRangeNV;
    long glFlushVertexArrayRangeNV;
    long glAllocateMemoryNV;
    long glFreeMemoryNV;
    long glVertexAttribL1i64NV;
    long glVertexAttribL2i64NV;
    long glVertexAttribL3i64NV;
    long glVertexAttribL4i64NV;
    long glVertexAttribL1i64vNV;
    long glVertexAttribL2i64vNV;
    long glVertexAttribL3i64vNV;
    long glVertexAttribL4i64vNV;
    long glVertexAttribL1ui64NV;
    long glVertexAttribL2ui64NV;
    long glVertexAttribL3ui64NV;
    long glVertexAttribL4ui64NV;
    long glVertexAttribL1ui64vNV;
    long glVertexAttribL2ui64vNV;
    long glVertexAttribL3ui64vNV;
    long glVertexAttribL4ui64vNV;
    long glGetVertexAttribLi64vNV;
    long glGetVertexAttribLui64vNV;
    long glVertexAttribLFormatNV;
    long glBufferAddressRangeNV;
    long glVertexFormatNV;
    long glNormalFormatNV;
    long glColorFormatNV;
    long glIndexFormatNV;
    long glTexCoordFormatNV;
    long glEdgeFlagFormatNV;
    long glSecondaryColorFormatNV;
    long glFogCoordFormatNV;
    long glVertexAttribFormatNV;
    long glVertexAttribIFormatNV;
    long glGetIntegerui64i_vNV;
    long glExecuteProgramNV;
    long glGetProgramParameterfvNV;
    long glGetProgramParameterdvNV;
    long glGetTrackMatrixivNV;
    long glGetVertexAttribfvNV;
    long glGetVertexAttribdvNV;
    long glGetVertexAttribivNV;
    long glGetVertexAttribPointervNV;
    long glProgramParameter4fNV;
    long glProgramParameter4dNV;
    long glProgramParameters4fvNV;
    long glProgramParameters4dvNV;
    long glTrackMatrixNV;
    long glVertexAttribPointerNV;
    long glVertexAttrib1sNV;
    long glVertexAttrib1fNV;
    long glVertexAttrib1dNV;
    long glVertexAttrib2sNV;
    long glVertexAttrib2fNV;
    long glVertexAttrib2dNV;
    long glVertexAttrib3sNV;
    long glVertexAttrib3fNV;
    long glVertexAttrib3dNV;
    long glVertexAttrib4sNV;
    long glVertexAttrib4fNV;
    long glVertexAttrib4dNV;
    long glVertexAttrib4ubNV;
    long glVertexAttribs1svNV;
    long glVertexAttribs1fvNV;
    long glVertexAttribs1dvNV;
    long glVertexAttribs2svNV;
    long glVertexAttribs2fvNV;
    long glVertexAttribs2dvNV;
    long glVertexAttribs3svNV;
    long glVertexAttribs3fvNV;
    long glVertexAttribs3dvNV;
    long glVertexAttribs4svNV;
    long glVertexAttribs4fvNV;
    long glVertexAttribs4dvNV;
    long glBeginVideoCaptureNV;
    long glBindVideoCaptureStreamBufferNV;
    long glBindVideoCaptureStreamTextureNV;
    long glEndVideoCaptureNV;
    long glGetVideoCaptureivNV;
    long glGetVideoCaptureStreamivNV;
    long glGetVideoCaptureStreamfvNV;
    long glGetVideoCaptureStreamdvNV;
    long glVideoCaptureNV;
    long glVideoCaptureStreamParameterivNV;
    long glVideoCaptureStreamParameterfvNV;
    long glVideoCaptureStreamParameterdvNV;
    
    private boolean AMD_debug_output_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress(new String[] { "glDebugMessageEnableAMD", "glDebugMessageEnableAMDX" });
        this.glDebugMessageEnableAMD = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress(new String[] { "glDebugMessageInsertAMD", "glDebugMessageInsertAMDX" });
        this.glDebugMessageInsertAMD = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress(new String[] { "glDebugMessageCallbackAMD", "glDebugMessageCallbackAMDX" });
        this.glDebugMessageCallbackAMD = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress(new String[] { "glGetDebugMessageLogAMD", "glGetDebugMessageLogAMDX" });
        this.glGetDebugMessageLogAMD = functionAddress4;
        return b3 & functionAddress4 != 0L;
    }
    
    private boolean AMD_draw_buffers_blend_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glBlendFuncIndexedAMD");
        this.glBlendFuncIndexedAMD = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glBlendFuncSeparateIndexedAMD");
        this.glBlendFuncSeparateIndexedAMD = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glBlendEquationIndexedAMD");
        this.glBlendEquationIndexedAMD = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glBlendEquationSeparateIndexedAMD");
        this.glBlendEquationSeparateIndexedAMD = functionAddress4;
        return b3 & functionAddress4 != 0L;
    }
    
    private boolean AMD_interleaved_elements_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glVertexAttribParameteriAMD");
        this.glVertexAttribParameteriAMD = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean AMD_multi_draw_indirect_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glMultiDrawArraysIndirectAMD");
        this.glMultiDrawArraysIndirectAMD = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glMultiDrawElementsIndirectAMD");
        this.glMultiDrawElementsIndirectAMD = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean AMD_name_gen_delete_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glGenNamesAMD");
        this.glGenNamesAMD = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glDeleteNamesAMD");
        this.glDeleteNamesAMD = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glIsNameAMD");
        this.glIsNameAMD = functionAddress3;
        return b2 & functionAddress3 != 0L;
    }
    
    private boolean AMD_performance_monitor_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glGetPerfMonitorGroupsAMD");
        this.glGetPerfMonitorGroupsAMD = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glGetPerfMonitorCountersAMD");
        this.glGetPerfMonitorCountersAMD = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glGetPerfMonitorGroupStringAMD");
        this.glGetPerfMonitorGroupStringAMD = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glGetPerfMonitorCounterStringAMD");
        this.glGetPerfMonitorCounterStringAMD = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glGetPerfMonitorCounterInfoAMD");
        this.glGetPerfMonitorCounterInfoAMD = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glGenPerfMonitorsAMD");
        this.glGenPerfMonitorsAMD = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glDeletePerfMonitorsAMD");
        this.glDeletePerfMonitorsAMD = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glSelectPerfMonitorCountersAMD");
        this.glSelectPerfMonitorCountersAMD = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glBeginPerfMonitorAMD");
        this.glBeginPerfMonitorAMD = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glEndPerfMonitorAMD");
        this.glEndPerfMonitorAMD = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glGetPerfMonitorCounterDataAMD");
        this.glGetPerfMonitorCounterDataAMD = functionAddress11;
        return b10 & functionAddress11 != 0L;
    }
    
    private boolean AMD_sample_positions_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glSetMultisamplefvAMD");
        this.glSetMultisamplefvAMD = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean AMD_sparse_texture_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glTexStorageSparseAMD");
        this.glTexStorageSparseAMD = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glTextureStorageSparseAMD");
        this.glTextureStorageSparseAMD = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean AMD_stencil_operation_extended_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glStencilOpValueAMD");
        this.glStencilOpValueAMD = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean AMD_vertex_shader_tessellator_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glTessellationFactorAMD");
        this.glTessellationFactorAMD = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glTessellationModeAMD");
        this.glTessellationModeAMD = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean APPLE_element_array_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glElementPointerAPPLE");
        this.glElementPointerAPPLE = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glDrawElementArrayAPPLE");
        this.glDrawElementArrayAPPLE = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glDrawRangeElementArrayAPPLE");
        this.glDrawRangeElementArrayAPPLE = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glMultiDrawElementArrayAPPLE");
        this.glMultiDrawElementArrayAPPLE = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glMultiDrawRangeElementArrayAPPLE");
        this.glMultiDrawRangeElementArrayAPPLE = functionAddress5;
        return b4 & functionAddress5 != 0L;
    }
    
    private boolean APPLE_fence_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glGenFencesAPPLE");
        this.glGenFencesAPPLE = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glDeleteFencesAPPLE");
        this.glDeleteFencesAPPLE = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glSetFenceAPPLE");
        this.glSetFenceAPPLE = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glIsFenceAPPLE");
        this.glIsFenceAPPLE = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glTestFenceAPPLE");
        this.glTestFenceAPPLE = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glFinishFenceAPPLE");
        this.glFinishFenceAPPLE = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glTestObjectAPPLE");
        this.glTestObjectAPPLE = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glFinishObjectAPPLE");
        this.glFinishObjectAPPLE = functionAddress8;
        return b7 & functionAddress8 != 0L;
    }
    
    private boolean APPLE_flush_buffer_range_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glBufferParameteriAPPLE");
        this.glBufferParameteriAPPLE = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glFlushMappedBufferRangeAPPLE");
        this.glFlushMappedBufferRangeAPPLE = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean APPLE_object_purgeable_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glObjectPurgeableAPPLE");
        this.glObjectPurgeableAPPLE = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glObjectUnpurgeableAPPLE");
        this.glObjectUnpurgeableAPPLE = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glGetObjectParameterivAPPLE");
        this.glGetObjectParameterivAPPLE = functionAddress3;
        return b2 & functionAddress3 != 0L;
    }
    
    private boolean APPLE_texture_range_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glTextureRangeAPPLE");
        this.glTextureRangeAPPLE = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glGetTexParameterPointervAPPLE");
        this.glGetTexParameterPointervAPPLE = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean APPLE_vertex_array_object_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glBindVertexArrayAPPLE");
        this.glBindVertexArrayAPPLE = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glDeleteVertexArraysAPPLE");
        this.glDeleteVertexArraysAPPLE = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glGenVertexArraysAPPLE");
        this.glGenVertexArraysAPPLE = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glIsVertexArrayAPPLE");
        this.glIsVertexArrayAPPLE = functionAddress4;
        return b3 & functionAddress4 != 0L;
    }
    
    private boolean APPLE_vertex_array_range_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glVertexArrayRangeAPPLE");
        this.glVertexArrayRangeAPPLE = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glFlushVertexArrayRangeAPPLE");
        this.glFlushVertexArrayRangeAPPLE = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glVertexArrayParameteriAPPLE");
        this.glVertexArrayParameteriAPPLE = functionAddress3;
        return b2 & functionAddress3 != 0L;
    }
    
    private boolean APPLE_vertex_program_evaluators_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glEnableVertexAttribAPPLE");
        this.glEnableVertexAttribAPPLE = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glDisableVertexAttribAPPLE");
        this.glDisableVertexAttribAPPLE = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glIsVertexAttribEnabledAPPLE");
        this.glIsVertexAttribEnabledAPPLE = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glMapVertexAttrib1dAPPLE");
        this.glMapVertexAttrib1dAPPLE = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glMapVertexAttrib1fAPPLE");
        this.glMapVertexAttrib1fAPPLE = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glMapVertexAttrib2dAPPLE");
        this.glMapVertexAttrib2dAPPLE = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glMapVertexAttrib2fAPPLE");
        this.glMapVertexAttrib2fAPPLE = functionAddress7;
        return b6 & functionAddress7 != 0L;
    }
    
    private boolean ARB_ES2_compatibility_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glReleaseShaderCompiler");
        this.glReleaseShaderCompiler = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glShaderBinary");
        this.glShaderBinary = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glGetShaderPrecisionFormat");
        this.glGetShaderPrecisionFormat = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glDepthRangef");
        this.glDepthRangef = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glClearDepthf");
        this.glClearDepthf = functionAddress5;
        return b4 & functionAddress5 != 0L;
    }
    
    private boolean ARB_ES3_1_compatibility_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glMemoryBarrierByRegion");
        this.glMemoryBarrierByRegion = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean ARB_base_instance_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glDrawArraysInstancedBaseInstance");
        this.glDrawArraysInstancedBaseInstance = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glDrawElementsInstancedBaseInstance");
        this.glDrawElementsInstancedBaseInstance = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glDrawElementsInstancedBaseVertexBaseInstance");
        this.glDrawElementsInstancedBaseVertexBaseInstance = functionAddress3;
        return b2 & functionAddress3 != 0L;
    }
    
    private boolean ARB_bindless_texture_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glGetTextureHandleARB");
        this.glGetTextureHandleARB = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glGetTextureSamplerHandleARB");
        this.glGetTextureSamplerHandleARB = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glMakeTextureHandleResidentARB");
        this.glMakeTextureHandleResidentARB = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glMakeTextureHandleNonResidentARB");
        this.glMakeTextureHandleNonResidentARB = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glGetImageHandleARB");
        this.glGetImageHandleARB = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glMakeImageHandleResidentARB");
        this.glMakeImageHandleResidentARB = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glMakeImageHandleNonResidentARB");
        this.glMakeImageHandleNonResidentARB = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glUniformHandleui64ARB");
        this.glUniformHandleui64ARB = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glUniformHandleui64vARB");
        this.glUniformHandleui64vARB = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glProgramUniformHandleui64ARB");
        this.glProgramUniformHandleui64ARB = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glProgramUniformHandleui64vARB");
        this.glProgramUniformHandleui64vARB = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glIsTextureHandleResidentARB");
        this.glIsTextureHandleResidentARB = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glIsImageHandleResidentARB");
        this.glIsImageHandleResidentARB = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glVertexAttribL1ui64ARB");
        this.glVertexAttribL1ui64ARB = functionAddress14;
        final boolean b14 = b13 & functionAddress14 != 0L;
        final long functionAddress15 = GLContext.getFunctionAddress("glVertexAttribL1ui64vARB");
        this.glVertexAttribL1ui64vARB = functionAddress15;
        final boolean b15 = b14 & functionAddress15 != 0L;
        final long functionAddress16 = GLContext.getFunctionAddress("glGetVertexAttribLui64vARB");
        this.glGetVertexAttribLui64vARB = functionAddress16;
        return b15 & functionAddress16 != 0L;
    }
    
    private boolean ARB_blend_func_extended_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glBindFragDataLocationIndexed");
        this.glBindFragDataLocationIndexed = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glGetFragDataIndex");
        this.glGetFragDataIndex = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean ARB_buffer_object_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glBindBufferARB");
        this.glBindBufferARB = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glDeleteBuffersARB");
        this.glDeleteBuffersARB = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glGenBuffersARB");
        this.glGenBuffersARB = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glIsBufferARB");
        this.glIsBufferARB = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glBufferDataARB");
        this.glBufferDataARB = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glBufferSubDataARB");
        this.glBufferSubDataARB = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glGetBufferSubDataARB");
        this.glGetBufferSubDataARB = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glMapBufferARB");
        this.glMapBufferARB = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glUnmapBufferARB");
        this.glUnmapBufferARB = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glGetBufferParameterivARB");
        this.glGetBufferParameterivARB = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glGetBufferPointervARB");
        this.glGetBufferPointervARB = functionAddress11;
        return b10 & functionAddress11 != 0L;
    }
    
    private boolean ARB_buffer_storage_initNativeFunctionAddresses(final Set<String> supported_extensions) {
        final long functionAddress = GLContext.getFunctionAddress("glBufferStorage");
        this.glBufferStorage = functionAddress;
        final boolean b = functionAddress != 0L;
        if (supported_extensions.contains("GL_EXT_direct_state_access")) {
            final long functionAddress2 = GLContext.getFunctionAddress("glNamedBufferStorageEXT");
            this.glNamedBufferStorageEXT = functionAddress2;
            if (functionAddress2 == 0L) {
                final boolean b2 = false;
                return b & b2;
            }
        }
        final boolean b2 = true;
        return b & b2;
    }
    
    private boolean ARB_cl_event_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glCreateSyncFromCLeventARB");
        this.glCreateSyncFromCLeventARB = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean ARB_clear_buffer_object_initNativeFunctionAddresses(final Set<String> supported_extensions) {
        final long functionAddress = GLContext.getFunctionAddress("glClearBufferData");
        this.glClearBufferData = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glClearBufferSubData");
        this.glClearBufferSubData = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        boolean b3 = false;
        Label_0072: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress3 = GLContext.getFunctionAddress("glClearNamedBufferDataEXT");
                this.glClearNamedBufferDataEXT = functionAddress3;
                if (functionAddress3 == 0L) {
                    b3 = false;
                    break Label_0072;
                }
            }
            b3 = true;
        }
        final boolean b4 = b2 & b3;
        if (supported_extensions.contains("GL_EXT_direct_state_access")) {
            final long functionAddress4 = GLContext.getFunctionAddress("glClearNamedBufferSubDataEXT");
            this.glClearNamedBufferSubDataEXT = functionAddress4;
            if (functionAddress4 == 0L) {
                final boolean b5 = false;
                return b4 & b5;
            }
        }
        final boolean b5 = true;
        return b4 & b5;
    }
    
    private boolean ARB_clear_texture_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glClearTexImage");
        this.glClearTexImage = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glClearTexSubImage");
        this.glClearTexSubImage = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean ARB_clip_control_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glClipControl");
        this.glClipControl = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean ARB_color_buffer_float_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glClampColorARB");
        this.glClampColorARB = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean ARB_compute_shader_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glDispatchCompute");
        this.glDispatchCompute = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glDispatchComputeIndirect");
        this.glDispatchComputeIndirect = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean ARB_compute_variable_group_size_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glDispatchComputeGroupSizeARB");
        this.glDispatchComputeGroupSizeARB = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean ARB_copy_buffer_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glCopyBufferSubData");
        this.glCopyBufferSubData = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean ARB_copy_image_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glCopyImageSubData");
        this.glCopyImageSubData = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean ARB_debug_output_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glDebugMessageControlARB");
        this.glDebugMessageControlARB = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glDebugMessageInsertARB");
        this.glDebugMessageInsertARB = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glDebugMessageCallbackARB");
        this.glDebugMessageCallbackARB = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glGetDebugMessageLogARB");
        this.glGetDebugMessageLogARB = functionAddress4;
        return b3 & functionAddress4 != 0L;
    }
    
    private boolean ARB_direct_state_access_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glCreateTransformFeedbacks");
        this.glCreateTransformFeedbacks = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glTransformFeedbackBufferBase");
        this.glTransformFeedbackBufferBase = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glTransformFeedbackBufferRange");
        this.glTransformFeedbackBufferRange = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glGetTransformFeedbackiv");
        this.glGetTransformFeedbackiv = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glGetTransformFeedbacki_v");
        this.glGetTransformFeedbacki_v = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glGetTransformFeedbacki64_v");
        this.glGetTransformFeedbacki64_v = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glCreateBuffers");
        this.glCreateBuffers = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glNamedBufferStorage");
        this.glNamedBufferStorage = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glNamedBufferData");
        this.glNamedBufferData = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glNamedBufferSubData");
        this.glNamedBufferSubData = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glCopyNamedBufferSubData");
        this.glCopyNamedBufferSubData = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glClearNamedBufferData");
        this.glClearNamedBufferData = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glClearNamedBufferSubData");
        this.glClearNamedBufferSubData = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glMapNamedBuffer");
        this.glMapNamedBuffer = functionAddress14;
        final boolean b14 = b13 & functionAddress14 != 0L;
        final long functionAddress15 = GLContext.getFunctionAddress("glMapNamedBufferRange");
        this.glMapNamedBufferRange = functionAddress15;
        final boolean b15 = b14 & functionAddress15 != 0L;
        final long functionAddress16 = GLContext.getFunctionAddress("glUnmapNamedBuffer");
        this.glUnmapNamedBuffer = functionAddress16;
        final boolean b16 = b15 & functionAddress16 != 0L;
        final long functionAddress17 = GLContext.getFunctionAddress("glFlushMappedNamedBufferRange");
        this.glFlushMappedNamedBufferRange = functionAddress17;
        final boolean b17 = b16 & functionAddress17 != 0L;
        final long functionAddress18 = GLContext.getFunctionAddress("glGetNamedBufferParameteriv");
        this.glGetNamedBufferParameteriv = functionAddress18;
        final boolean b18 = b17 & functionAddress18 != 0L;
        final long functionAddress19 = GLContext.getFunctionAddress("glGetNamedBufferParameteri64v");
        this.glGetNamedBufferParameteri64v = functionAddress19;
        final boolean b19 = b18 & functionAddress19 != 0L;
        final long functionAddress20 = GLContext.getFunctionAddress("glGetNamedBufferPointerv");
        this.glGetNamedBufferPointerv = functionAddress20;
        final boolean b20 = b19 & functionAddress20 != 0L;
        final long functionAddress21 = GLContext.getFunctionAddress("glGetNamedBufferSubData");
        this.glGetNamedBufferSubData = functionAddress21;
        final boolean b21 = b20 & functionAddress21 != 0L;
        final long functionAddress22 = GLContext.getFunctionAddress("glCreateFramebuffers");
        this.glCreateFramebuffers = functionAddress22;
        final boolean b22 = b21 & functionAddress22 != 0L;
        final long functionAddress23 = GLContext.getFunctionAddress("glNamedFramebufferRenderbuffer");
        this.glNamedFramebufferRenderbuffer = functionAddress23;
        final boolean b23 = b22 & functionAddress23 != 0L;
        final long functionAddress24 = GLContext.getFunctionAddress("glNamedFramebufferParameteri");
        this.glNamedFramebufferParameteri = functionAddress24;
        final boolean b24 = b23 & functionAddress24 != 0L;
        final long functionAddress25 = GLContext.getFunctionAddress("glNamedFramebufferTexture");
        this.glNamedFramebufferTexture = functionAddress25;
        final boolean b25 = b24 & functionAddress25 != 0L;
        final long functionAddress26 = GLContext.getFunctionAddress("glNamedFramebufferTextureLayer");
        this.glNamedFramebufferTextureLayer = functionAddress26;
        final boolean b26 = b25 & functionAddress26 != 0L;
        final long functionAddress27 = GLContext.getFunctionAddress("glNamedFramebufferDrawBuffer");
        this.glNamedFramebufferDrawBuffer = functionAddress27;
        final boolean b27 = b26 & functionAddress27 != 0L;
        final long functionAddress28 = GLContext.getFunctionAddress("glNamedFramebufferDrawBuffers");
        this.glNamedFramebufferDrawBuffers = functionAddress28;
        final boolean b28 = b27 & functionAddress28 != 0L;
        final long functionAddress29 = GLContext.getFunctionAddress("glNamedFramebufferReadBuffer");
        this.glNamedFramebufferReadBuffer = functionAddress29;
        final boolean b29 = b28 & functionAddress29 != 0L;
        final long functionAddress30 = GLContext.getFunctionAddress("glInvalidateNamedFramebufferData");
        this.glInvalidateNamedFramebufferData = functionAddress30;
        final boolean b30 = b29 & functionAddress30 != 0L;
        final long functionAddress31 = GLContext.getFunctionAddress("glInvalidateNamedFramebufferSubData");
        this.glInvalidateNamedFramebufferSubData = functionAddress31;
        final boolean b31 = b30 & functionAddress31 != 0L;
        final long functionAddress32 = GLContext.getFunctionAddress("glClearNamedFramebufferiv");
        this.glClearNamedFramebufferiv = functionAddress32;
        final boolean b32 = b31 & functionAddress32 != 0L;
        final long functionAddress33 = GLContext.getFunctionAddress("glClearNamedFramebufferuiv");
        this.glClearNamedFramebufferuiv = functionAddress33;
        final boolean b33 = b32 & functionAddress33 != 0L;
        final long functionAddress34 = GLContext.getFunctionAddress("glClearNamedFramebufferfv");
        this.glClearNamedFramebufferfv = functionAddress34;
        final boolean b34 = b33 & functionAddress34 != 0L;
        final long functionAddress35 = GLContext.getFunctionAddress("glClearNamedFramebufferfi");
        this.glClearNamedFramebufferfi = functionAddress35;
        final boolean b35 = b34 & functionAddress35 != 0L;
        final long functionAddress36 = GLContext.getFunctionAddress("glBlitNamedFramebuffer");
        this.glBlitNamedFramebuffer = functionAddress36;
        final boolean b36 = b35 & functionAddress36 != 0L;
        final long functionAddress37 = GLContext.getFunctionAddress("glCheckNamedFramebufferStatus");
        this.glCheckNamedFramebufferStatus = functionAddress37;
        final boolean b37 = b36 & functionAddress37 != 0L;
        final long functionAddress38 = GLContext.getFunctionAddress("glGetNamedFramebufferParameteriv");
        this.glGetNamedFramebufferParameteriv = functionAddress38;
        final boolean b38 = b37 & functionAddress38 != 0L;
        final long functionAddress39 = GLContext.getFunctionAddress("glGetNamedFramebufferAttachmentParameteriv");
        this.glGetNamedFramebufferAttachmentParameteriv = functionAddress39;
        final boolean b39 = b38 & functionAddress39 != 0L;
        final long functionAddress40 = GLContext.getFunctionAddress("glCreateRenderbuffers");
        this.glCreateRenderbuffers = functionAddress40;
        final boolean b40 = b39 & functionAddress40 != 0L;
        final long functionAddress41 = GLContext.getFunctionAddress("glNamedRenderbufferStorage");
        this.glNamedRenderbufferStorage = functionAddress41;
        final boolean b41 = b40 & functionAddress41 != 0L;
        final long functionAddress42 = GLContext.getFunctionAddress("glNamedRenderbufferStorageMultisample");
        this.glNamedRenderbufferStorageMultisample = functionAddress42;
        final boolean b42 = b41 & functionAddress42 != 0L;
        final long functionAddress43 = GLContext.getFunctionAddress("glGetNamedRenderbufferParameteriv");
        this.glGetNamedRenderbufferParameteriv = functionAddress43;
        final boolean b43 = b42 & functionAddress43 != 0L;
        final long functionAddress44 = GLContext.getFunctionAddress("glCreateTextures");
        this.glCreateTextures = functionAddress44;
        final boolean b44 = b43 & functionAddress44 != 0L;
        final long functionAddress45 = GLContext.getFunctionAddress("glTextureBuffer");
        this.glTextureBuffer = functionAddress45;
        final boolean b45 = b44 & functionAddress45 != 0L;
        final long functionAddress46 = GLContext.getFunctionAddress("glTextureBufferRange");
        this.glTextureBufferRange = functionAddress46;
        final boolean b46 = b45 & functionAddress46 != 0L;
        final long functionAddress47 = GLContext.getFunctionAddress("glTextureStorage1D");
        this.glTextureStorage1D = functionAddress47;
        final boolean b47 = b46 & functionAddress47 != 0L;
        final long functionAddress48 = GLContext.getFunctionAddress("glTextureStorage2D");
        this.glTextureStorage2D = functionAddress48;
        final boolean b48 = b47 & functionAddress48 != 0L;
        final long functionAddress49 = GLContext.getFunctionAddress("glTextureStorage3D");
        this.glTextureStorage3D = functionAddress49;
        final boolean b49 = b48 & functionAddress49 != 0L;
        final long functionAddress50 = GLContext.getFunctionAddress("glTextureStorage2DMultisample");
        this.glTextureStorage2DMultisample = functionAddress50;
        final boolean b50 = b49 & functionAddress50 != 0L;
        final long functionAddress51 = GLContext.getFunctionAddress("glTextureStorage3DMultisample");
        this.glTextureStorage3DMultisample = functionAddress51;
        final boolean b51 = b50 & functionAddress51 != 0L;
        final long functionAddress52 = GLContext.getFunctionAddress("glTextureSubImage1D");
        this.glTextureSubImage1D = functionAddress52;
        final boolean b52 = b51 & functionAddress52 != 0L;
        final long functionAddress53 = GLContext.getFunctionAddress("glTextureSubImage2D");
        this.glTextureSubImage2D = functionAddress53;
        final boolean b53 = b52 & functionAddress53 != 0L;
        final long functionAddress54 = GLContext.getFunctionAddress("glTextureSubImage3D");
        this.glTextureSubImage3D = functionAddress54;
        final boolean b54 = b53 & functionAddress54 != 0L;
        final long functionAddress55 = GLContext.getFunctionAddress("glCompressedTextureSubImage1D");
        this.glCompressedTextureSubImage1D = functionAddress55;
        final boolean b55 = b54 & functionAddress55 != 0L;
        final long functionAddress56 = GLContext.getFunctionAddress("glCompressedTextureSubImage2D");
        this.glCompressedTextureSubImage2D = functionAddress56;
        final boolean b56 = b55 & functionAddress56 != 0L;
        final long functionAddress57 = GLContext.getFunctionAddress("glCompressedTextureSubImage3D");
        this.glCompressedTextureSubImage3D = functionAddress57;
        final boolean b57 = b56 & functionAddress57 != 0L;
        final long functionAddress58 = GLContext.getFunctionAddress("glCopyTextureSubImage1D");
        this.glCopyTextureSubImage1D = functionAddress58;
        final boolean b58 = b57 & functionAddress58 != 0L;
        final long functionAddress59 = GLContext.getFunctionAddress("glCopyTextureSubImage2D");
        this.glCopyTextureSubImage2D = functionAddress59;
        final boolean b59 = b58 & functionAddress59 != 0L;
        final long functionAddress60 = GLContext.getFunctionAddress("glCopyTextureSubImage3D");
        this.glCopyTextureSubImage3D = functionAddress60;
        final boolean b60 = b59 & functionAddress60 != 0L;
        final long functionAddress61 = GLContext.getFunctionAddress("glTextureParameterf");
        this.glTextureParameterf = functionAddress61;
        final boolean b61 = b60 & functionAddress61 != 0L;
        final long functionAddress62 = GLContext.getFunctionAddress("glTextureParameterfv");
        this.glTextureParameterfv = functionAddress62;
        final boolean b62 = b61 & functionAddress62 != 0L;
        final long functionAddress63 = GLContext.getFunctionAddress("glTextureParameteri");
        this.glTextureParameteri = functionAddress63;
        final boolean b63 = b62 & functionAddress63 != 0L;
        final long functionAddress64 = GLContext.getFunctionAddress("glTextureParameterIiv");
        this.glTextureParameterIiv = functionAddress64;
        final boolean b64 = b63 & functionAddress64 != 0L;
        final long functionAddress65 = GLContext.getFunctionAddress("glTextureParameterIuiv");
        this.glTextureParameterIuiv = functionAddress65;
        final boolean b65 = b64 & functionAddress65 != 0L;
        final long functionAddress66 = GLContext.getFunctionAddress("glTextureParameteriv");
        this.glTextureParameteriv = functionAddress66;
        final boolean b66 = b65 & functionAddress66 != 0L;
        final long functionAddress67 = GLContext.getFunctionAddress("glGenerateTextureMipmap");
        this.glGenerateTextureMipmap = functionAddress67;
        final boolean b67 = b66 & functionAddress67 != 0L;
        final long functionAddress68 = GLContext.getFunctionAddress("glBindTextureUnit");
        this.glBindTextureUnit = functionAddress68;
        final boolean b68 = b67 & functionAddress68 != 0L;
        final long functionAddress69 = GLContext.getFunctionAddress("glGetTextureImage");
        this.glGetTextureImage = functionAddress69;
        final boolean b69 = b68 & functionAddress69 != 0L;
        final long functionAddress70 = GLContext.getFunctionAddress("glGetCompressedTextureImage");
        this.glGetCompressedTextureImage = functionAddress70;
        final boolean b70 = b69 & functionAddress70 != 0L;
        final long functionAddress71 = GLContext.getFunctionAddress("glGetTextureLevelParameterfv");
        this.glGetTextureLevelParameterfv = functionAddress71;
        final boolean b71 = b70 & functionAddress71 != 0L;
        final long functionAddress72 = GLContext.getFunctionAddress("glGetTextureLevelParameteriv");
        this.glGetTextureLevelParameteriv = functionAddress72;
        final boolean b72 = b71 & functionAddress72 != 0L;
        final long functionAddress73 = GLContext.getFunctionAddress("glGetTextureParameterfv");
        this.glGetTextureParameterfv = functionAddress73;
        final boolean b73 = b72 & functionAddress73 != 0L;
        final long functionAddress74 = GLContext.getFunctionAddress("glGetTextureParameterIiv");
        this.glGetTextureParameterIiv = functionAddress74;
        final boolean b74 = b73 & functionAddress74 != 0L;
        final long functionAddress75 = GLContext.getFunctionAddress("glGetTextureParameterIuiv");
        this.glGetTextureParameterIuiv = functionAddress75;
        final boolean b75 = b74 & functionAddress75 != 0L;
        final long functionAddress76 = GLContext.getFunctionAddress("glGetTextureParameteriv");
        this.glGetTextureParameteriv = functionAddress76;
        final boolean b76 = b75 & functionAddress76 != 0L;
        final long functionAddress77 = GLContext.getFunctionAddress("glCreateVertexArrays");
        this.glCreateVertexArrays = functionAddress77;
        final boolean b77 = b76 & functionAddress77 != 0L;
        final long functionAddress78 = GLContext.getFunctionAddress("glDisableVertexArrayAttrib");
        this.glDisableVertexArrayAttrib = functionAddress78;
        final boolean b78 = b77 & functionAddress78 != 0L;
        final long functionAddress79 = GLContext.getFunctionAddress("glEnableVertexArrayAttrib");
        this.glEnableVertexArrayAttrib = functionAddress79;
        final boolean b79 = b78 & functionAddress79 != 0L;
        final long functionAddress80 = GLContext.getFunctionAddress("glVertexArrayElementBuffer");
        this.glVertexArrayElementBuffer = functionAddress80;
        final boolean b80 = b79 & functionAddress80 != 0L;
        final long functionAddress81 = GLContext.getFunctionAddress("glVertexArrayVertexBuffer");
        this.glVertexArrayVertexBuffer = functionAddress81;
        final boolean b81 = b80 & functionAddress81 != 0L;
        final long functionAddress82 = GLContext.getFunctionAddress("glVertexArrayVertexBuffers");
        this.glVertexArrayVertexBuffers = functionAddress82;
        final boolean b82 = b81 & functionAddress82 != 0L;
        final long functionAddress83 = GLContext.getFunctionAddress("glVertexArrayAttribFormat");
        this.glVertexArrayAttribFormat = functionAddress83;
        final boolean b83 = b82 & functionAddress83 != 0L;
        final long functionAddress84 = GLContext.getFunctionAddress("glVertexArrayAttribIFormat");
        this.glVertexArrayAttribIFormat = functionAddress84;
        final boolean b84 = b83 & functionAddress84 != 0L;
        final long functionAddress85 = GLContext.getFunctionAddress("glVertexArrayAttribLFormat");
        this.glVertexArrayAttribLFormat = functionAddress85;
        final boolean b85 = b84 & functionAddress85 != 0L;
        final long functionAddress86 = GLContext.getFunctionAddress("glVertexArrayAttribBinding");
        this.glVertexArrayAttribBinding = functionAddress86;
        final boolean b86 = b85 & functionAddress86 != 0L;
        final long functionAddress87 = GLContext.getFunctionAddress("glVertexArrayBindingDivisor");
        this.glVertexArrayBindingDivisor = functionAddress87;
        final boolean b87 = b86 & functionAddress87 != 0L;
        final long functionAddress88 = GLContext.getFunctionAddress("glGetVertexArrayiv");
        this.glGetVertexArrayiv = functionAddress88;
        final boolean b88 = b87 & functionAddress88 != 0L;
        final long functionAddress89 = GLContext.getFunctionAddress("glGetVertexArrayIndexediv");
        this.glGetVertexArrayIndexediv = functionAddress89;
        final boolean b89 = b88 & functionAddress89 != 0L;
        final long functionAddress90 = GLContext.getFunctionAddress("glGetVertexArrayIndexed64iv");
        this.glGetVertexArrayIndexed64iv = functionAddress90;
        final boolean b90 = b89 & functionAddress90 != 0L;
        final long functionAddress91 = GLContext.getFunctionAddress("glCreateSamplers");
        this.glCreateSamplers = functionAddress91;
        final boolean b91 = b90 & functionAddress91 != 0L;
        final long functionAddress92 = GLContext.getFunctionAddress("glCreateProgramPipelines");
        this.glCreateProgramPipelines = functionAddress92;
        final boolean b92 = b91 & functionAddress92 != 0L;
        final long functionAddress93 = GLContext.getFunctionAddress("glCreateQueries");
        this.glCreateQueries = functionAddress93;
        return b92 & functionAddress93 != 0L;
    }
    
    private boolean ARB_draw_buffers_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glDrawBuffersARB");
        this.glDrawBuffersARB = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean ARB_draw_buffers_blend_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glBlendEquationiARB");
        this.glBlendEquationiARB = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glBlendEquationSeparateiARB");
        this.glBlendEquationSeparateiARB = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glBlendFunciARB");
        this.glBlendFunciARB = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glBlendFuncSeparateiARB");
        this.glBlendFuncSeparateiARB = functionAddress4;
        return b3 & functionAddress4 != 0L;
    }
    
    private boolean ARB_draw_elements_base_vertex_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glDrawElementsBaseVertex");
        this.glDrawElementsBaseVertex = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glDrawRangeElementsBaseVertex");
        this.glDrawRangeElementsBaseVertex = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glDrawElementsInstancedBaseVertex");
        this.glDrawElementsInstancedBaseVertex = functionAddress3;
        return b2 & functionAddress3 != 0L;
    }
    
    private boolean ARB_draw_indirect_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glDrawArraysIndirect");
        this.glDrawArraysIndirect = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glDrawElementsIndirect");
        this.glDrawElementsIndirect = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean ARB_draw_instanced_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glDrawArraysInstancedARB");
        this.glDrawArraysInstancedARB = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glDrawElementsInstancedARB");
        this.glDrawElementsInstancedARB = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean ARB_framebuffer_no_attachments_initNativeFunctionAddresses(final Set<String> supported_extensions) {
        final long functionAddress = GLContext.getFunctionAddress("glFramebufferParameteri");
        this.glFramebufferParameteri = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glGetFramebufferParameteriv");
        this.glGetFramebufferParameteriv = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        boolean b3 = false;
        Label_0075: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress3 = GLContext.getFunctionAddress("glNamedFramebufferParameteriEXT");
                this.glNamedFramebufferParameteriEXT = functionAddress3;
                if (functionAddress3 == 0L) {
                    b3 = false;
                    break Label_0075;
                }
            }
            b3 = true;
        }
        final boolean b4 = b2 & b3;
        if (supported_extensions.contains("GL_EXT_direct_state_access")) {
            final long functionAddress4 = GLContext.getFunctionAddress("glGetNamedFramebufferParameterivEXT");
            this.glGetNamedFramebufferParameterivEXT = functionAddress4;
            if (functionAddress4 == 0L) {
                final boolean b5 = false;
                return b4 & b5;
            }
        }
        final boolean b5 = true;
        return b4 & b5;
    }
    
    private boolean ARB_framebuffer_object_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glIsRenderbuffer");
        this.glIsRenderbuffer = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glBindRenderbuffer");
        this.glBindRenderbuffer = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glDeleteRenderbuffers");
        this.glDeleteRenderbuffers = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glGenRenderbuffers");
        this.glGenRenderbuffers = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glRenderbufferStorage");
        this.glRenderbufferStorage = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glRenderbufferStorageMultisample");
        this.glRenderbufferStorageMultisample = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glGetRenderbufferParameteriv");
        this.glGetRenderbufferParameteriv = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glIsFramebuffer");
        this.glIsFramebuffer = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glBindFramebuffer");
        this.glBindFramebuffer = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glDeleteFramebuffers");
        this.glDeleteFramebuffers = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glGenFramebuffers");
        this.glGenFramebuffers = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glCheckFramebufferStatus");
        this.glCheckFramebufferStatus = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glFramebufferTexture1D");
        this.glFramebufferTexture1D = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glFramebufferTexture2D");
        this.glFramebufferTexture2D = functionAddress14;
        final boolean b14 = b13 & functionAddress14 != 0L;
        final long functionAddress15 = GLContext.getFunctionAddress("glFramebufferTexture3D");
        this.glFramebufferTexture3D = functionAddress15;
        final boolean b15 = b14 & functionAddress15 != 0L;
        final long functionAddress16 = GLContext.getFunctionAddress("glFramebufferTextureLayer");
        this.glFramebufferTextureLayer = functionAddress16;
        final boolean b16 = b15 & functionAddress16 != 0L;
        final long functionAddress17 = GLContext.getFunctionAddress("glFramebufferRenderbuffer");
        this.glFramebufferRenderbuffer = functionAddress17;
        final boolean b17 = b16 & functionAddress17 != 0L;
        final long functionAddress18 = GLContext.getFunctionAddress("glGetFramebufferAttachmentParameteriv");
        this.glGetFramebufferAttachmentParameteriv = functionAddress18;
        final boolean b18 = b17 & functionAddress18 != 0L;
        final long functionAddress19 = GLContext.getFunctionAddress("glBlitFramebuffer");
        this.glBlitFramebuffer = functionAddress19;
        final boolean b19 = b18 & functionAddress19 != 0L;
        final long functionAddress20 = GLContext.getFunctionAddress("glGenerateMipmap");
        this.glGenerateMipmap = functionAddress20;
        return b19 & functionAddress20 != 0L;
    }
    
    private boolean ARB_geometry_shader4_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glProgramParameteriARB");
        this.glProgramParameteriARB = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glFramebufferTextureARB");
        this.glFramebufferTextureARB = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glFramebufferTextureLayerARB");
        this.glFramebufferTextureLayerARB = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glFramebufferTextureFaceARB");
        this.glFramebufferTextureFaceARB = functionAddress4;
        return b3 & functionAddress4 != 0L;
    }
    
    private boolean ARB_get_program_binary_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glGetProgramBinary");
        this.glGetProgramBinary = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glProgramBinary");
        this.glProgramBinary = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glProgramParameteri");
        this.glProgramParameteri = functionAddress3;
        return b2 & functionAddress3 != 0L;
    }
    
    private boolean ARB_get_texture_sub_image_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glGetTextureSubImage");
        this.glGetTextureSubImage = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glGetCompressedTextureSubImage");
        this.glGetCompressedTextureSubImage = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean ARB_gpu_shader_fp64_initNativeFunctionAddresses(final Set<String> supported_extensions) {
        final long functionAddress = GLContext.getFunctionAddress("glUniform1d");
        this.glUniform1d = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glUniform2d");
        this.glUniform2d = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glUniform3d");
        this.glUniform3d = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glUniform4d");
        this.glUniform4d = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glUniform1dv");
        this.glUniform1dv = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glUniform2dv");
        this.glUniform2dv = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glUniform3dv");
        this.glUniform3dv = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glUniform4dv");
        this.glUniform4dv = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glUniformMatrix2dv");
        this.glUniformMatrix2dv = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glUniformMatrix3dv");
        this.glUniformMatrix3dv = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glUniformMatrix4dv");
        this.glUniformMatrix4dv = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glUniformMatrix2x3dv");
        this.glUniformMatrix2x3dv = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glUniformMatrix2x4dv");
        this.glUniformMatrix2x4dv = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glUniformMatrix3x2dv");
        this.glUniformMatrix3x2dv = functionAddress14;
        final boolean b14 = b13 & functionAddress14 != 0L;
        final long functionAddress15 = GLContext.getFunctionAddress("glUniformMatrix3x4dv");
        this.glUniformMatrix3x4dv = functionAddress15;
        final boolean b15 = b14 & functionAddress15 != 0L;
        final long functionAddress16 = GLContext.getFunctionAddress("glUniformMatrix4x2dv");
        this.glUniformMatrix4x2dv = functionAddress16;
        final boolean b16 = b15 & functionAddress16 != 0L;
        final long functionAddress17 = GLContext.getFunctionAddress("glUniformMatrix4x3dv");
        this.glUniformMatrix4x3dv = functionAddress17;
        final boolean b17 = b16 & functionAddress17 != 0L;
        final long functionAddress18 = GLContext.getFunctionAddress("glGetUniformdv");
        this.glGetUniformdv = functionAddress18;
        final boolean b18 = b17 & functionAddress18 != 0L;
        boolean b19 = false;
        Label_0427: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress19 = GLContext.getFunctionAddress("glProgramUniform1dEXT");
                this.glProgramUniform1dEXT = functionAddress19;
                if (functionAddress19 == 0L) {
                    b19 = false;
                    break Label_0427;
                }
            }
            b19 = true;
        }
        final boolean b20 = b18 & b19;
        boolean b21 = false;
        Label_0460: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress20 = GLContext.getFunctionAddress("glProgramUniform2dEXT");
                this.glProgramUniform2dEXT = functionAddress20;
                if (functionAddress20 == 0L) {
                    b21 = false;
                    break Label_0460;
                }
            }
            b21 = true;
        }
        final boolean b22 = b20 & b21;
        boolean b23 = false;
        Label_0493: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress21 = GLContext.getFunctionAddress("glProgramUniform3dEXT");
                this.glProgramUniform3dEXT = functionAddress21;
                if (functionAddress21 == 0L) {
                    b23 = false;
                    break Label_0493;
                }
            }
            b23 = true;
        }
        final boolean b24 = b22 & b23;
        boolean b25 = false;
        Label_0526: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress22 = GLContext.getFunctionAddress("glProgramUniform4dEXT");
                this.glProgramUniform4dEXT = functionAddress22;
                if (functionAddress22 == 0L) {
                    b25 = false;
                    break Label_0526;
                }
            }
            b25 = true;
        }
        final boolean b26 = b24 & b25;
        boolean b27 = false;
        Label_0559: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress23 = GLContext.getFunctionAddress("glProgramUniform1dvEXT");
                this.glProgramUniform1dvEXT = functionAddress23;
                if (functionAddress23 == 0L) {
                    b27 = false;
                    break Label_0559;
                }
            }
            b27 = true;
        }
        final boolean b28 = b26 & b27;
        boolean b29 = false;
        Label_0592: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress24 = GLContext.getFunctionAddress("glProgramUniform2dvEXT");
                this.glProgramUniform2dvEXT = functionAddress24;
                if (functionAddress24 == 0L) {
                    b29 = false;
                    break Label_0592;
                }
            }
            b29 = true;
        }
        final boolean b30 = b28 & b29;
        boolean b31 = false;
        Label_0625: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress25 = GLContext.getFunctionAddress("glProgramUniform3dvEXT");
                this.glProgramUniform3dvEXT = functionAddress25;
                if (functionAddress25 == 0L) {
                    b31 = false;
                    break Label_0625;
                }
            }
            b31 = true;
        }
        final boolean b32 = b30 & b31;
        boolean b33 = false;
        Label_0658: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress26 = GLContext.getFunctionAddress("glProgramUniform4dvEXT");
                this.glProgramUniform4dvEXT = functionAddress26;
                if (functionAddress26 == 0L) {
                    b33 = false;
                    break Label_0658;
                }
            }
            b33 = true;
        }
        final boolean b34 = b32 & b33;
        boolean b35 = false;
        Label_0691: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress27 = GLContext.getFunctionAddress("glProgramUniformMatrix2dvEXT");
                this.glProgramUniformMatrix2dvEXT = functionAddress27;
                if (functionAddress27 == 0L) {
                    b35 = false;
                    break Label_0691;
                }
            }
            b35 = true;
        }
        final boolean b36 = b34 & b35;
        boolean b37 = false;
        Label_0724: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress28 = GLContext.getFunctionAddress("glProgramUniformMatrix3dvEXT");
                this.glProgramUniformMatrix3dvEXT = functionAddress28;
                if (functionAddress28 == 0L) {
                    b37 = false;
                    break Label_0724;
                }
            }
            b37 = true;
        }
        final boolean b38 = b36 & b37;
        boolean b39 = false;
        Label_0757: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress29 = GLContext.getFunctionAddress("glProgramUniformMatrix4dvEXT");
                this.glProgramUniformMatrix4dvEXT = functionAddress29;
                if (functionAddress29 == 0L) {
                    b39 = false;
                    break Label_0757;
                }
            }
            b39 = true;
        }
        final boolean b40 = b38 & b39;
        boolean b41 = false;
        Label_0790: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress30 = GLContext.getFunctionAddress("glProgramUniformMatrix2x3dvEXT");
                this.glProgramUniformMatrix2x3dvEXT = functionAddress30;
                if (functionAddress30 == 0L) {
                    b41 = false;
                    break Label_0790;
                }
            }
            b41 = true;
        }
        final boolean b42 = b40 & b41;
        boolean b43 = false;
        Label_0823: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress31 = GLContext.getFunctionAddress("glProgramUniformMatrix2x4dvEXT");
                this.glProgramUniformMatrix2x4dvEXT = functionAddress31;
                if (functionAddress31 == 0L) {
                    b43 = false;
                    break Label_0823;
                }
            }
            b43 = true;
        }
        final boolean b44 = b42 & b43;
        boolean b45 = false;
        Label_0856: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress32 = GLContext.getFunctionAddress("glProgramUniformMatrix3x2dvEXT");
                this.glProgramUniformMatrix3x2dvEXT = functionAddress32;
                if (functionAddress32 == 0L) {
                    b45 = false;
                    break Label_0856;
                }
            }
            b45 = true;
        }
        final boolean b46 = b44 & b45;
        boolean b47 = false;
        Label_0889: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress33 = GLContext.getFunctionAddress("glProgramUniformMatrix3x4dvEXT");
                this.glProgramUniformMatrix3x4dvEXT = functionAddress33;
                if (functionAddress33 == 0L) {
                    b47 = false;
                    break Label_0889;
                }
            }
            b47 = true;
        }
        final boolean b48 = b46 & b47;
        boolean b49 = false;
        Label_0922: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress34 = GLContext.getFunctionAddress("glProgramUniformMatrix4x2dvEXT");
                this.glProgramUniformMatrix4x2dvEXT = functionAddress34;
                if (functionAddress34 == 0L) {
                    b49 = false;
                    break Label_0922;
                }
            }
            b49 = true;
        }
        final boolean b50 = b48 & b49;
        if (supported_extensions.contains("GL_EXT_direct_state_access")) {
            final long functionAddress35 = GLContext.getFunctionAddress("glProgramUniformMatrix4x3dvEXT");
            this.glProgramUniformMatrix4x3dvEXT = functionAddress35;
            if (functionAddress35 == 0L) {
                final boolean b51 = false;
                return b50 & b51;
            }
        }
        final boolean b51 = true;
        return b50 & b51;
    }
    
    private boolean ARB_imaging_initNativeFunctionAddresses(final boolean forwardCompatible) {
        boolean b = false;
        Label_0025: {
            if (!forwardCompatible) {
                final long functionAddress = GLContext.getFunctionAddress("glColorTable");
                this.glColorTable = functionAddress;
                if (functionAddress == 0L) {
                    b = false;
                    break Label_0025;
                }
            }
            b = true;
        }
        boolean b2 = false;
        Label_0050: {
            if (!forwardCompatible) {
                final long functionAddress2 = GLContext.getFunctionAddress("glColorSubTable");
                this.glColorSubTable = functionAddress2;
                if (functionAddress2 == 0L) {
                    b2 = false;
                    break Label_0050;
                }
            }
            b2 = true;
        }
        final boolean b3 = b & b2;
        boolean b4 = false;
        Label_0076: {
            if (!forwardCompatible) {
                final long functionAddress3 = GLContext.getFunctionAddress("glColorTableParameteriv");
                this.glColorTableParameteriv = functionAddress3;
                if (functionAddress3 == 0L) {
                    b4 = false;
                    break Label_0076;
                }
            }
            b4 = true;
        }
        final boolean b5 = b3 & b4;
        boolean b6 = false;
        Label_0102: {
            if (!forwardCompatible) {
                final long functionAddress4 = GLContext.getFunctionAddress("glColorTableParameterfv");
                this.glColorTableParameterfv = functionAddress4;
                if (functionAddress4 == 0L) {
                    b6 = false;
                    break Label_0102;
                }
            }
            b6 = true;
        }
        final boolean b7 = b5 & b6;
        boolean b8 = false;
        Label_0128: {
            if (!forwardCompatible) {
                final long functionAddress5 = GLContext.getFunctionAddress("glCopyColorSubTable");
                this.glCopyColorSubTable = functionAddress5;
                if (functionAddress5 == 0L) {
                    b8 = false;
                    break Label_0128;
                }
            }
            b8 = true;
        }
        final boolean b9 = b7 & b8;
        boolean b10 = false;
        Label_0154: {
            if (!forwardCompatible) {
                final long functionAddress6 = GLContext.getFunctionAddress("glCopyColorTable");
                this.glCopyColorTable = functionAddress6;
                if (functionAddress6 == 0L) {
                    b10 = false;
                    break Label_0154;
                }
            }
            b10 = true;
        }
        final boolean b11 = b9 & b10;
        boolean b12 = false;
        Label_0180: {
            if (!forwardCompatible) {
                final long functionAddress7 = GLContext.getFunctionAddress("glGetColorTable");
                this.glGetColorTable = functionAddress7;
                if (functionAddress7 == 0L) {
                    b12 = false;
                    break Label_0180;
                }
            }
            b12 = true;
        }
        final boolean b13 = b11 & b12;
        boolean b14 = false;
        Label_0206: {
            if (!forwardCompatible) {
                final long functionAddress8 = GLContext.getFunctionAddress("glGetColorTableParameteriv");
                this.glGetColorTableParameteriv = functionAddress8;
                if (functionAddress8 == 0L) {
                    b14 = false;
                    break Label_0206;
                }
            }
            b14 = true;
        }
        final boolean b15 = b13 & b14;
        boolean b16 = false;
        Label_0232: {
            if (!forwardCompatible) {
                final long functionAddress9 = GLContext.getFunctionAddress("glGetColorTableParameterfv");
                this.glGetColorTableParameterfv = functionAddress9;
                if (functionAddress9 == 0L) {
                    b16 = false;
                    break Label_0232;
                }
            }
            b16 = true;
        }
        final boolean b17 = b15 & b16;
        final long functionAddress10 = GLContext.getFunctionAddress("glBlendEquation");
        this.glBlendEquation = functionAddress10;
        final boolean b18 = b17 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glBlendColor");
        this.glBlendColor = functionAddress11;
        final boolean b19 = b18 & functionAddress11 != 0L;
        boolean b20 = false;
        Label_0302: {
            if (!forwardCompatible) {
                final long functionAddress12 = GLContext.getFunctionAddress("glHistogram");
                this.glHistogram = functionAddress12;
                if (functionAddress12 == 0L) {
                    b20 = false;
                    break Label_0302;
                }
            }
            b20 = true;
        }
        final boolean b21 = b19 & b20;
        boolean b22 = false;
        Label_0328: {
            if (!forwardCompatible) {
                final long functionAddress13 = GLContext.getFunctionAddress("glResetHistogram");
                this.glResetHistogram = functionAddress13;
                if (functionAddress13 == 0L) {
                    b22 = false;
                    break Label_0328;
                }
            }
            b22 = true;
        }
        final boolean b23 = b21 & b22;
        boolean b24 = false;
        Label_0354: {
            if (!forwardCompatible) {
                final long functionAddress14 = GLContext.getFunctionAddress("glGetHistogram");
                this.glGetHistogram = functionAddress14;
                if (functionAddress14 == 0L) {
                    b24 = false;
                    break Label_0354;
                }
            }
            b24 = true;
        }
        final boolean b25 = b23 & b24;
        boolean b26 = false;
        Label_0380: {
            if (!forwardCompatible) {
                final long functionAddress15 = GLContext.getFunctionAddress("glGetHistogramParameterfv");
                this.glGetHistogramParameterfv = functionAddress15;
                if (functionAddress15 == 0L) {
                    b26 = false;
                    break Label_0380;
                }
            }
            b26 = true;
        }
        final boolean b27 = b25 & b26;
        boolean b28 = false;
        Label_0406: {
            if (!forwardCompatible) {
                final long functionAddress16 = GLContext.getFunctionAddress("glGetHistogramParameteriv");
                this.glGetHistogramParameteriv = functionAddress16;
                if (functionAddress16 == 0L) {
                    b28 = false;
                    break Label_0406;
                }
            }
            b28 = true;
        }
        final boolean b29 = b27 & b28;
        boolean b30 = false;
        Label_0432: {
            if (!forwardCompatible) {
                final long functionAddress17 = GLContext.getFunctionAddress("glMinmax");
                this.glMinmax = functionAddress17;
                if (functionAddress17 == 0L) {
                    b30 = false;
                    break Label_0432;
                }
            }
            b30 = true;
        }
        final boolean b31 = b29 & b30;
        boolean b32 = false;
        Label_0458: {
            if (!forwardCompatible) {
                final long functionAddress18 = GLContext.getFunctionAddress("glResetMinmax");
                this.glResetMinmax = functionAddress18;
                if (functionAddress18 == 0L) {
                    b32 = false;
                    break Label_0458;
                }
            }
            b32 = true;
        }
        final boolean b33 = b31 & b32;
        boolean b34 = false;
        Label_0484: {
            if (!forwardCompatible) {
                final long functionAddress19 = GLContext.getFunctionAddress("glGetMinmax");
                this.glGetMinmax = functionAddress19;
                if (functionAddress19 == 0L) {
                    b34 = false;
                    break Label_0484;
                }
            }
            b34 = true;
        }
        final boolean b35 = b33 & b34;
        boolean b36 = false;
        Label_0510: {
            if (!forwardCompatible) {
                final long functionAddress20 = GLContext.getFunctionAddress("glGetMinmaxParameterfv");
                this.glGetMinmaxParameterfv = functionAddress20;
                if (functionAddress20 == 0L) {
                    b36 = false;
                    break Label_0510;
                }
            }
            b36 = true;
        }
        final boolean b37 = b35 & b36;
        boolean b38 = false;
        Label_0536: {
            if (!forwardCompatible) {
                final long functionAddress21 = GLContext.getFunctionAddress("glGetMinmaxParameteriv");
                this.glGetMinmaxParameteriv = functionAddress21;
                if (functionAddress21 == 0L) {
                    b38 = false;
                    break Label_0536;
                }
            }
            b38 = true;
        }
        final boolean b39 = b37 & b38;
        boolean b40 = false;
        Label_0562: {
            if (!forwardCompatible) {
                final long functionAddress22 = GLContext.getFunctionAddress("glConvolutionFilter1D");
                this.glConvolutionFilter1D = functionAddress22;
                if (functionAddress22 == 0L) {
                    b40 = false;
                    break Label_0562;
                }
            }
            b40 = true;
        }
        final boolean b41 = b39 & b40;
        boolean b42 = false;
        Label_0588: {
            if (!forwardCompatible) {
                final long functionAddress23 = GLContext.getFunctionAddress("glConvolutionFilter2D");
                this.glConvolutionFilter2D = functionAddress23;
                if (functionAddress23 == 0L) {
                    b42 = false;
                    break Label_0588;
                }
            }
            b42 = true;
        }
        final boolean b43 = b41 & b42;
        boolean b44 = false;
        Label_0614: {
            if (!forwardCompatible) {
                final long functionAddress24 = GLContext.getFunctionAddress("glConvolutionParameterf");
                this.glConvolutionParameterf = functionAddress24;
                if (functionAddress24 == 0L) {
                    b44 = false;
                    break Label_0614;
                }
            }
            b44 = true;
        }
        final boolean b45 = b43 & b44;
        boolean b46 = false;
        Label_0640: {
            if (!forwardCompatible) {
                final long functionAddress25 = GLContext.getFunctionAddress("glConvolutionParameterfv");
                this.glConvolutionParameterfv = functionAddress25;
                if (functionAddress25 == 0L) {
                    b46 = false;
                    break Label_0640;
                }
            }
            b46 = true;
        }
        final boolean b47 = b45 & b46;
        boolean b48 = false;
        Label_0666: {
            if (!forwardCompatible) {
                final long functionAddress26 = GLContext.getFunctionAddress("glConvolutionParameteri");
                this.glConvolutionParameteri = functionAddress26;
                if (functionAddress26 == 0L) {
                    b48 = false;
                    break Label_0666;
                }
            }
            b48 = true;
        }
        final boolean b49 = b47 & b48;
        boolean b50 = false;
        Label_0692: {
            if (!forwardCompatible) {
                final long functionAddress27 = GLContext.getFunctionAddress("glConvolutionParameteriv");
                this.glConvolutionParameteriv = functionAddress27;
                if (functionAddress27 == 0L) {
                    b50 = false;
                    break Label_0692;
                }
            }
            b50 = true;
        }
        final boolean b51 = b49 & b50;
        boolean b52 = false;
        Label_0718: {
            if (!forwardCompatible) {
                final long functionAddress28 = GLContext.getFunctionAddress("glCopyConvolutionFilter1D");
                this.glCopyConvolutionFilter1D = functionAddress28;
                if (functionAddress28 == 0L) {
                    b52 = false;
                    break Label_0718;
                }
            }
            b52 = true;
        }
        final boolean b53 = b51 & b52;
        boolean b54 = false;
        Label_0744: {
            if (!forwardCompatible) {
                final long functionAddress29 = GLContext.getFunctionAddress("glCopyConvolutionFilter2D");
                this.glCopyConvolutionFilter2D = functionAddress29;
                if (functionAddress29 == 0L) {
                    b54 = false;
                    break Label_0744;
                }
            }
            b54 = true;
        }
        final boolean b55 = b53 & b54;
        boolean b56 = false;
        Label_0770: {
            if (!forwardCompatible) {
                final long functionAddress30 = GLContext.getFunctionAddress("glGetConvolutionFilter");
                this.glGetConvolutionFilter = functionAddress30;
                if (functionAddress30 == 0L) {
                    b56 = false;
                    break Label_0770;
                }
            }
            b56 = true;
        }
        final boolean b57 = b55 & b56;
        boolean b58 = false;
        Label_0796: {
            if (!forwardCompatible) {
                final long functionAddress31 = GLContext.getFunctionAddress("glGetConvolutionParameterfv");
                this.glGetConvolutionParameterfv = functionAddress31;
                if (functionAddress31 == 0L) {
                    b58 = false;
                    break Label_0796;
                }
            }
            b58 = true;
        }
        final boolean b59 = b57 & b58;
        boolean b60 = false;
        Label_0822: {
            if (!forwardCompatible) {
                final long functionAddress32 = GLContext.getFunctionAddress("glGetConvolutionParameteriv");
                this.glGetConvolutionParameteriv = functionAddress32;
                if (functionAddress32 == 0L) {
                    b60 = false;
                    break Label_0822;
                }
            }
            b60 = true;
        }
        final boolean b61 = b59 & b60;
        boolean b62 = false;
        Label_0848: {
            if (!forwardCompatible) {
                final long functionAddress33 = GLContext.getFunctionAddress("glSeparableFilter2D");
                this.glSeparableFilter2D = functionAddress33;
                if (functionAddress33 == 0L) {
                    b62 = false;
                    break Label_0848;
                }
            }
            b62 = true;
        }
        final boolean b63 = b61 & b62;
        if (!forwardCompatible) {
            final long functionAddress34 = GLContext.getFunctionAddress("glGetSeparableFilter");
            this.glGetSeparableFilter = functionAddress34;
            if (functionAddress34 == 0L) {
                final boolean b64 = false;
                return b63 & b64;
            }
        }
        final boolean b64 = true;
        return b63 & b64;
    }
    
    private boolean ARB_indirect_parameters_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glMultiDrawArraysIndirectCountARB");
        this.glMultiDrawArraysIndirectCountARB = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glMultiDrawElementsIndirectCountARB");
        this.glMultiDrawElementsIndirectCountARB = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean ARB_instanced_arrays_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glVertexAttribDivisorARB");
        this.glVertexAttribDivisorARB = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean ARB_internalformat_query_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glGetInternalformativ");
        this.glGetInternalformativ = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean ARB_internalformat_query2_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glGetInternalformati64v");
        this.glGetInternalformati64v = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean ARB_invalidate_subdata_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glInvalidateTexSubImage");
        this.glInvalidateTexSubImage = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glInvalidateTexImage");
        this.glInvalidateTexImage = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glInvalidateBufferSubData");
        this.glInvalidateBufferSubData = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glInvalidateBufferData");
        this.glInvalidateBufferData = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glInvalidateFramebuffer");
        this.glInvalidateFramebuffer = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glInvalidateSubFramebuffer");
        this.glInvalidateSubFramebuffer = functionAddress6;
        return b5 & functionAddress6 != 0L;
    }
    
    private boolean ARB_map_buffer_range_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glMapBufferRange");
        this.glMapBufferRange = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glFlushMappedBufferRange");
        this.glFlushMappedBufferRange = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean ARB_matrix_palette_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glCurrentPaletteMatrixARB");
        this.glCurrentPaletteMatrixARB = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glMatrixIndexPointerARB");
        this.glMatrixIndexPointerARB = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glMatrixIndexubvARB");
        this.glMatrixIndexubvARB = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glMatrixIndexusvARB");
        this.glMatrixIndexusvARB = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glMatrixIndexuivARB");
        this.glMatrixIndexuivARB = functionAddress5;
        return b4 & functionAddress5 != 0L;
    }
    
    private boolean ARB_multi_bind_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glBindBuffersBase");
        this.glBindBuffersBase = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glBindBuffersRange");
        this.glBindBuffersRange = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glBindTextures");
        this.glBindTextures = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glBindSamplers");
        this.glBindSamplers = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glBindImageTextures");
        this.glBindImageTextures = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glBindVertexBuffers");
        this.glBindVertexBuffers = functionAddress6;
        return b5 & functionAddress6 != 0L;
    }
    
    private boolean ARB_multi_draw_indirect_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glMultiDrawArraysIndirect");
        this.glMultiDrawArraysIndirect = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glMultiDrawElementsIndirect");
        this.glMultiDrawElementsIndirect = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean ARB_multisample_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glSampleCoverageARB");
        this.glSampleCoverageARB = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean ARB_multitexture_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glClientActiveTextureARB");
        this.glClientActiveTextureARB = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glActiveTextureARB");
        this.glActiveTextureARB = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glMultiTexCoord1fARB");
        this.glMultiTexCoord1fARB = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glMultiTexCoord1dARB");
        this.glMultiTexCoord1dARB = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glMultiTexCoord1iARB");
        this.glMultiTexCoord1iARB = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glMultiTexCoord1sARB");
        this.glMultiTexCoord1sARB = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glMultiTexCoord2fARB");
        this.glMultiTexCoord2fARB = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glMultiTexCoord2dARB");
        this.glMultiTexCoord2dARB = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glMultiTexCoord2iARB");
        this.glMultiTexCoord2iARB = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glMultiTexCoord2sARB");
        this.glMultiTexCoord2sARB = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glMultiTexCoord3fARB");
        this.glMultiTexCoord3fARB = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glMultiTexCoord3dARB");
        this.glMultiTexCoord3dARB = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glMultiTexCoord3iARB");
        this.glMultiTexCoord3iARB = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glMultiTexCoord3sARB");
        this.glMultiTexCoord3sARB = functionAddress14;
        final boolean b14 = b13 & functionAddress14 != 0L;
        final long functionAddress15 = GLContext.getFunctionAddress("glMultiTexCoord4fARB");
        this.glMultiTexCoord4fARB = functionAddress15;
        final boolean b15 = b14 & functionAddress15 != 0L;
        final long functionAddress16 = GLContext.getFunctionAddress("glMultiTexCoord4dARB");
        this.glMultiTexCoord4dARB = functionAddress16;
        final boolean b16 = b15 & functionAddress16 != 0L;
        final long functionAddress17 = GLContext.getFunctionAddress("glMultiTexCoord4iARB");
        this.glMultiTexCoord4iARB = functionAddress17;
        final boolean b17 = b16 & functionAddress17 != 0L;
        final long functionAddress18 = GLContext.getFunctionAddress("glMultiTexCoord4sARB");
        this.glMultiTexCoord4sARB = functionAddress18;
        return b17 & functionAddress18 != 0L;
    }
    
    private boolean ARB_occlusion_query_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glGenQueriesARB");
        this.glGenQueriesARB = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glDeleteQueriesARB");
        this.glDeleteQueriesARB = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glIsQueryARB");
        this.glIsQueryARB = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glBeginQueryARB");
        this.glBeginQueryARB = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glEndQueryARB");
        this.glEndQueryARB = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glGetQueryivARB");
        this.glGetQueryivARB = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glGetQueryObjectivARB");
        this.glGetQueryObjectivARB = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glGetQueryObjectuivARB");
        this.glGetQueryObjectuivARB = functionAddress8;
        return b7 & functionAddress8 != 0L;
    }
    
    private boolean ARB_point_parameters_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glPointParameterfARB");
        this.glPointParameterfARB = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glPointParameterfvARB");
        this.glPointParameterfvARB = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean ARB_program_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glProgramStringARB");
        this.glProgramStringARB = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glBindProgramARB");
        this.glBindProgramARB = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glDeleteProgramsARB");
        this.glDeleteProgramsARB = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glGenProgramsARB");
        this.glGenProgramsARB = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glProgramEnvParameter4fARB");
        this.glProgramEnvParameter4fARB = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glProgramEnvParameter4dARB");
        this.glProgramEnvParameter4dARB = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glProgramEnvParameter4fvARB");
        this.glProgramEnvParameter4fvARB = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glProgramEnvParameter4dvARB");
        this.glProgramEnvParameter4dvARB = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glProgramLocalParameter4fARB");
        this.glProgramLocalParameter4fARB = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glProgramLocalParameter4dARB");
        this.glProgramLocalParameter4dARB = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glProgramLocalParameter4fvARB");
        this.glProgramLocalParameter4fvARB = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glProgramLocalParameter4dvARB");
        this.glProgramLocalParameter4dvARB = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glGetProgramEnvParameterfvARB");
        this.glGetProgramEnvParameterfvARB = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glGetProgramEnvParameterdvARB");
        this.glGetProgramEnvParameterdvARB = functionAddress14;
        final boolean b14 = b13 & functionAddress14 != 0L;
        final long functionAddress15 = GLContext.getFunctionAddress("glGetProgramLocalParameterfvARB");
        this.glGetProgramLocalParameterfvARB = functionAddress15;
        final boolean b15 = b14 & functionAddress15 != 0L;
        final long functionAddress16 = GLContext.getFunctionAddress("glGetProgramLocalParameterdvARB");
        this.glGetProgramLocalParameterdvARB = functionAddress16;
        final boolean b16 = b15 & functionAddress16 != 0L;
        final long functionAddress17 = GLContext.getFunctionAddress("glGetProgramivARB");
        this.glGetProgramivARB = functionAddress17;
        final boolean b17 = b16 & functionAddress17 != 0L;
        final long functionAddress18 = GLContext.getFunctionAddress("glGetProgramStringARB");
        this.glGetProgramStringARB = functionAddress18;
        final boolean b18 = b17 & functionAddress18 != 0L;
        final long functionAddress19 = GLContext.getFunctionAddress("glIsProgramARB");
        this.glIsProgramARB = functionAddress19;
        return b18 & functionAddress19 != 0L;
    }
    
    private boolean ARB_program_interface_query_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glGetProgramInterfaceiv");
        this.glGetProgramInterfaceiv = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glGetProgramResourceIndex");
        this.glGetProgramResourceIndex = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glGetProgramResourceName");
        this.glGetProgramResourceName = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glGetProgramResourceiv");
        this.glGetProgramResourceiv = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glGetProgramResourceLocation");
        this.glGetProgramResourceLocation = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glGetProgramResourceLocationIndex");
        this.glGetProgramResourceLocationIndex = functionAddress6;
        return b5 & functionAddress6 != 0L;
    }
    
    private boolean ARB_provoking_vertex_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glProvokingVertex");
        this.glProvokingVertex = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean ARB_robustness_initNativeFunctionAddresses(final boolean forwardCompatible, final Set<String> supported_extensions) {
        final long functionAddress = GLContext.getFunctionAddress("glGetGraphicsResetStatusARB");
        this.glGetGraphicsResetStatusARB = functionAddress;
        final boolean b = functionAddress != 0L;
        boolean b2 = false;
        Label_0046: {
            if (!forwardCompatible) {
                final long functionAddress2 = GLContext.getFunctionAddress("glGetnMapdvARB");
                this.glGetnMapdvARB = functionAddress2;
                if (functionAddress2 == 0L) {
                    b2 = false;
                    break Label_0046;
                }
            }
            b2 = true;
        }
        final boolean b3 = b & b2;
        boolean b4 = false;
        Label_0072: {
            if (!forwardCompatible) {
                final long functionAddress3 = GLContext.getFunctionAddress("glGetnMapfvARB");
                this.glGetnMapfvARB = functionAddress3;
                if (functionAddress3 == 0L) {
                    b4 = false;
                    break Label_0072;
                }
            }
            b4 = true;
        }
        final boolean b5 = b3 & b4;
        boolean b6 = false;
        Label_0098: {
            if (!forwardCompatible) {
                final long functionAddress4 = GLContext.getFunctionAddress("glGetnMapivARB");
                this.glGetnMapivARB = functionAddress4;
                if (functionAddress4 == 0L) {
                    b6 = false;
                    break Label_0098;
                }
            }
            b6 = true;
        }
        final boolean b7 = b5 & b6;
        boolean b8 = false;
        Label_0124: {
            if (!forwardCompatible) {
                final long functionAddress5 = GLContext.getFunctionAddress("glGetnPixelMapfvARB");
                this.glGetnPixelMapfvARB = functionAddress5;
                if (functionAddress5 == 0L) {
                    b8 = false;
                    break Label_0124;
                }
            }
            b8 = true;
        }
        final boolean b9 = b7 & b8;
        boolean b10 = false;
        Label_0150: {
            if (!forwardCompatible) {
                final long functionAddress6 = GLContext.getFunctionAddress("glGetnPixelMapuivARB");
                this.glGetnPixelMapuivARB = functionAddress6;
                if (functionAddress6 == 0L) {
                    b10 = false;
                    break Label_0150;
                }
            }
            b10 = true;
        }
        final boolean b11 = b9 & b10;
        boolean b12 = false;
        Label_0176: {
            if (!forwardCompatible) {
                final long functionAddress7 = GLContext.getFunctionAddress("glGetnPixelMapusvARB");
                this.glGetnPixelMapusvARB = functionAddress7;
                if (functionAddress7 == 0L) {
                    b12 = false;
                    break Label_0176;
                }
            }
            b12 = true;
        }
        final boolean b13 = b11 & b12;
        boolean b14 = false;
        Label_0202: {
            if (!forwardCompatible) {
                final long functionAddress8 = GLContext.getFunctionAddress("glGetnPolygonStippleARB");
                this.glGetnPolygonStippleARB = functionAddress8;
                if (functionAddress8 == 0L) {
                    b14 = false;
                    break Label_0202;
                }
            }
            b14 = true;
        }
        final boolean b15 = b13 & b14;
        final long functionAddress9 = GLContext.getFunctionAddress("glGetnTexImageARB");
        this.glGetnTexImageARB = functionAddress9;
        final boolean b16 = b15 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glReadnPixelsARB");
        this.glReadnPixelsARB = functionAddress10;
        final boolean b17 = b16 & functionAddress10 != 0L;
        boolean b18 = false;
        Label_0280: {
            if (supported_extensions.contains("GL_ARB_imaging")) {
                final long functionAddress11 = GLContext.getFunctionAddress("glGetnColorTableARB");
                this.glGetnColorTableARB = functionAddress11;
                if (functionAddress11 == 0L) {
                    b18 = false;
                    break Label_0280;
                }
            }
            b18 = true;
        }
        final boolean b19 = b17 & b18;
        boolean b20 = false;
        Label_0314: {
            if (supported_extensions.contains("GL_ARB_imaging")) {
                final long functionAddress12 = GLContext.getFunctionAddress("glGetnConvolutionFilterARB");
                this.glGetnConvolutionFilterARB = functionAddress12;
                if (functionAddress12 == 0L) {
                    b20 = false;
                    break Label_0314;
                }
            }
            b20 = true;
        }
        final boolean b21 = b19 & b20;
        boolean b22 = false;
        Label_0348: {
            if (supported_extensions.contains("GL_ARB_imaging")) {
                final long functionAddress13 = GLContext.getFunctionAddress("glGetnSeparableFilterARB");
                this.glGetnSeparableFilterARB = functionAddress13;
                if (functionAddress13 == 0L) {
                    b22 = false;
                    break Label_0348;
                }
            }
            b22 = true;
        }
        final boolean b23 = b21 & b22;
        boolean b24 = false;
        Label_0382: {
            if (supported_extensions.contains("GL_ARB_imaging")) {
                final long functionAddress14 = GLContext.getFunctionAddress("glGetnHistogramARB");
                this.glGetnHistogramARB = functionAddress14;
                if (functionAddress14 == 0L) {
                    b24 = false;
                    break Label_0382;
                }
            }
            b24 = true;
        }
        final boolean b25 = b23 & b24;
        boolean b26 = false;
        Label_0416: {
            if (supported_extensions.contains("GL_ARB_imaging")) {
                final long functionAddress15 = GLContext.getFunctionAddress("glGetnMinmaxARB");
                this.glGetnMinmaxARB = functionAddress15;
                if (functionAddress15 == 0L) {
                    b26 = false;
                    break Label_0416;
                }
            }
            b26 = true;
        }
        final boolean b27 = b25 & b26;
        boolean b28 = false;
        Label_0450: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress16 = GLContext.getFunctionAddress("glGetnCompressedTexImageARB");
                this.glGetnCompressedTexImageARB = functionAddress16;
                if (functionAddress16 == 0L) {
                    b28 = false;
                    break Label_0450;
                }
            }
            b28 = true;
        }
        final boolean b29 = b27 & b28;
        boolean b30 = false;
        Label_0484: {
            if (supported_extensions.contains("OpenGL20")) {
                final long functionAddress17 = GLContext.getFunctionAddress("glGetnUniformfvARB");
                this.glGetnUniformfvARB = functionAddress17;
                if (functionAddress17 == 0L) {
                    b30 = false;
                    break Label_0484;
                }
            }
            b30 = true;
        }
        final boolean b31 = b29 & b30;
        boolean b32 = false;
        Label_0518: {
            if (supported_extensions.contains("OpenGL20")) {
                final long functionAddress18 = GLContext.getFunctionAddress("glGetnUniformivARB");
                this.glGetnUniformivARB = functionAddress18;
                if (functionAddress18 == 0L) {
                    b32 = false;
                    break Label_0518;
                }
            }
            b32 = true;
        }
        final boolean b33 = b31 & b32;
        boolean b34 = false;
        Label_0552: {
            if (supported_extensions.contains("OpenGL20")) {
                final long functionAddress19 = GLContext.getFunctionAddress("glGetnUniformuivARB");
                this.glGetnUniformuivARB = functionAddress19;
                if (functionAddress19 == 0L) {
                    b34 = false;
                    break Label_0552;
                }
            }
            b34 = true;
        }
        final boolean b35 = b33 & b34;
        if (supported_extensions.contains("OpenGL20")) {
            final long functionAddress20 = GLContext.getFunctionAddress("glGetnUniformdvARB");
            this.glGetnUniformdvARB = functionAddress20;
            if (functionAddress20 == 0L) {
                final boolean b36 = false;
                return b35 & b36;
            }
        }
        final boolean b36 = true;
        return b35 & b36;
    }
    
    private boolean ARB_sample_shading_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glMinSampleShadingARB");
        this.glMinSampleShadingARB = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean ARB_sampler_objects_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glGenSamplers");
        this.glGenSamplers = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glDeleteSamplers");
        this.glDeleteSamplers = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glIsSampler");
        this.glIsSampler = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glBindSampler");
        this.glBindSampler = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glSamplerParameteri");
        this.glSamplerParameteri = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glSamplerParameterf");
        this.glSamplerParameterf = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glSamplerParameteriv");
        this.glSamplerParameteriv = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glSamplerParameterfv");
        this.glSamplerParameterfv = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glSamplerParameterIiv");
        this.glSamplerParameterIiv = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glSamplerParameterIuiv");
        this.glSamplerParameterIuiv = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glGetSamplerParameteriv");
        this.glGetSamplerParameteriv = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glGetSamplerParameterfv");
        this.glGetSamplerParameterfv = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glGetSamplerParameterIiv");
        this.glGetSamplerParameterIiv = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glGetSamplerParameterIuiv");
        this.glGetSamplerParameterIuiv = functionAddress14;
        return b13 & functionAddress14 != 0L;
    }
    
    private boolean ARB_separate_shader_objects_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glUseProgramStages");
        this.glUseProgramStages = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glActiveShaderProgram");
        this.glActiveShaderProgram = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glCreateShaderProgramv");
        this.glCreateShaderProgramv = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glBindProgramPipeline");
        this.glBindProgramPipeline = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glDeleteProgramPipelines");
        this.glDeleteProgramPipelines = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glGenProgramPipelines");
        this.glGenProgramPipelines = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glIsProgramPipeline");
        this.glIsProgramPipeline = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glProgramParameteri");
        this.glProgramParameteri = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glGetProgramPipelineiv");
        this.glGetProgramPipelineiv = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glProgramUniform1i");
        this.glProgramUniform1i = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glProgramUniform2i");
        this.glProgramUniform2i = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glProgramUniform3i");
        this.glProgramUniform3i = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glProgramUniform4i");
        this.glProgramUniform4i = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glProgramUniform1f");
        this.glProgramUniform1f = functionAddress14;
        final boolean b14 = b13 & functionAddress14 != 0L;
        final long functionAddress15 = GLContext.getFunctionAddress("glProgramUniform2f");
        this.glProgramUniform2f = functionAddress15;
        final boolean b15 = b14 & functionAddress15 != 0L;
        final long functionAddress16 = GLContext.getFunctionAddress("glProgramUniform3f");
        this.glProgramUniform3f = functionAddress16;
        final boolean b16 = b15 & functionAddress16 != 0L;
        final long functionAddress17 = GLContext.getFunctionAddress("glProgramUniform4f");
        this.glProgramUniform4f = functionAddress17;
        final boolean b17 = b16 & functionAddress17 != 0L;
        final long functionAddress18 = GLContext.getFunctionAddress("glProgramUniform1d");
        this.glProgramUniform1d = functionAddress18;
        final boolean b18 = b17 & functionAddress18 != 0L;
        final long functionAddress19 = GLContext.getFunctionAddress("glProgramUniform2d");
        this.glProgramUniform2d = functionAddress19;
        final boolean b19 = b18 & functionAddress19 != 0L;
        final long functionAddress20 = GLContext.getFunctionAddress("glProgramUniform3d");
        this.glProgramUniform3d = functionAddress20;
        final boolean b20 = b19 & functionAddress20 != 0L;
        final long functionAddress21 = GLContext.getFunctionAddress("glProgramUniform4d");
        this.glProgramUniform4d = functionAddress21;
        final boolean b21 = b20 & functionAddress21 != 0L;
        final long functionAddress22 = GLContext.getFunctionAddress("glProgramUniform1iv");
        this.glProgramUniform1iv = functionAddress22;
        final boolean b22 = b21 & functionAddress22 != 0L;
        final long functionAddress23 = GLContext.getFunctionAddress("glProgramUniform2iv");
        this.glProgramUniform2iv = functionAddress23;
        final boolean b23 = b22 & functionAddress23 != 0L;
        final long functionAddress24 = GLContext.getFunctionAddress("glProgramUniform3iv");
        this.glProgramUniform3iv = functionAddress24;
        final boolean b24 = b23 & functionAddress24 != 0L;
        final long functionAddress25 = GLContext.getFunctionAddress("glProgramUniform4iv");
        this.glProgramUniform4iv = functionAddress25;
        final boolean b25 = b24 & functionAddress25 != 0L;
        final long functionAddress26 = GLContext.getFunctionAddress("glProgramUniform1fv");
        this.glProgramUniform1fv = functionAddress26;
        final boolean b26 = b25 & functionAddress26 != 0L;
        final long functionAddress27 = GLContext.getFunctionAddress("glProgramUniform2fv");
        this.glProgramUniform2fv = functionAddress27;
        final boolean b27 = b26 & functionAddress27 != 0L;
        final long functionAddress28 = GLContext.getFunctionAddress("glProgramUniform3fv");
        this.glProgramUniform3fv = functionAddress28;
        final boolean b28 = b27 & functionAddress28 != 0L;
        final long functionAddress29 = GLContext.getFunctionAddress("glProgramUniform4fv");
        this.glProgramUniform4fv = functionAddress29;
        final boolean b29 = b28 & functionAddress29 != 0L;
        final long functionAddress30 = GLContext.getFunctionAddress("glProgramUniform1dv");
        this.glProgramUniform1dv = functionAddress30;
        final boolean b30 = b29 & functionAddress30 != 0L;
        final long functionAddress31 = GLContext.getFunctionAddress("glProgramUniform2dv");
        this.glProgramUniform2dv = functionAddress31;
        final boolean b31 = b30 & functionAddress31 != 0L;
        final long functionAddress32 = GLContext.getFunctionAddress("glProgramUniform3dv");
        this.glProgramUniform3dv = functionAddress32;
        final boolean b32 = b31 & functionAddress32 != 0L;
        final long functionAddress33 = GLContext.getFunctionAddress("glProgramUniform4dv");
        this.glProgramUniform4dv = functionAddress33;
        final boolean b33 = b32 & functionAddress33 != 0L;
        final long functionAddress34 = GLContext.getFunctionAddress("glProgramUniform1ui");
        this.glProgramUniform1ui = functionAddress34;
        final boolean b34 = b33 & functionAddress34 != 0L;
        final long functionAddress35 = GLContext.getFunctionAddress("glProgramUniform2ui");
        this.glProgramUniform2ui = functionAddress35;
        final boolean b35 = b34 & functionAddress35 != 0L;
        final long functionAddress36 = GLContext.getFunctionAddress("glProgramUniform3ui");
        this.glProgramUniform3ui = functionAddress36;
        final boolean b36 = b35 & functionAddress36 != 0L;
        final long functionAddress37 = GLContext.getFunctionAddress("glProgramUniform4ui");
        this.glProgramUniform4ui = functionAddress37;
        final boolean b37 = b36 & functionAddress37 != 0L;
        final long functionAddress38 = GLContext.getFunctionAddress("glProgramUniform1uiv");
        this.glProgramUniform1uiv = functionAddress38;
        final boolean b38 = b37 & functionAddress38 != 0L;
        final long functionAddress39 = GLContext.getFunctionAddress("glProgramUniform2uiv");
        this.glProgramUniform2uiv = functionAddress39;
        final boolean b39 = b38 & functionAddress39 != 0L;
        final long functionAddress40 = GLContext.getFunctionAddress("glProgramUniform3uiv");
        this.glProgramUniform3uiv = functionAddress40;
        final boolean b40 = b39 & functionAddress40 != 0L;
        final long functionAddress41 = GLContext.getFunctionAddress("glProgramUniform4uiv");
        this.glProgramUniform4uiv = functionAddress41;
        final boolean b41 = b40 & functionAddress41 != 0L;
        final long functionAddress42 = GLContext.getFunctionAddress("glProgramUniformMatrix2fv");
        this.glProgramUniformMatrix2fv = functionAddress42;
        final boolean b42 = b41 & functionAddress42 != 0L;
        final long functionAddress43 = GLContext.getFunctionAddress("glProgramUniformMatrix3fv");
        this.glProgramUniformMatrix3fv = functionAddress43;
        final boolean b43 = b42 & functionAddress43 != 0L;
        final long functionAddress44 = GLContext.getFunctionAddress("glProgramUniformMatrix4fv");
        this.glProgramUniformMatrix4fv = functionAddress44;
        final boolean b44 = b43 & functionAddress44 != 0L;
        final long functionAddress45 = GLContext.getFunctionAddress("glProgramUniformMatrix2dv");
        this.glProgramUniformMatrix2dv = functionAddress45;
        final boolean b45 = b44 & functionAddress45 != 0L;
        final long functionAddress46 = GLContext.getFunctionAddress("glProgramUniformMatrix3dv");
        this.glProgramUniformMatrix3dv = functionAddress46;
        final boolean b46 = b45 & functionAddress46 != 0L;
        final long functionAddress47 = GLContext.getFunctionAddress("glProgramUniformMatrix4dv");
        this.glProgramUniformMatrix4dv = functionAddress47;
        final boolean b47 = b46 & functionAddress47 != 0L;
        final long functionAddress48 = GLContext.getFunctionAddress("glProgramUniformMatrix2x3fv");
        this.glProgramUniformMatrix2x3fv = functionAddress48;
        final boolean b48 = b47 & functionAddress48 != 0L;
        final long functionAddress49 = GLContext.getFunctionAddress("glProgramUniformMatrix3x2fv");
        this.glProgramUniformMatrix3x2fv = functionAddress49;
        final boolean b49 = b48 & functionAddress49 != 0L;
        final long functionAddress50 = GLContext.getFunctionAddress("glProgramUniformMatrix2x4fv");
        this.glProgramUniformMatrix2x4fv = functionAddress50;
        final boolean b50 = b49 & functionAddress50 != 0L;
        final long functionAddress51 = GLContext.getFunctionAddress("glProgramUniformMatrix4x2fv");
        this.glProgramUniformMatrix4x2fv = functionAddress51;
        final boolean b51 = b50 & functionAddress51 != 0L;
        final long functionAddress52 = GLContext.getFunctionAddress("glProgramUniformMatrix3x4fv");
        this.glProgramUniformMatrix3x4fv = functionAddress52;
        final boolean b52 = b51 & functionAddress52 != 0L;
        final long functionAddress53 = GLContext.getFunctionAddress("glProgramUniformMatrix4x3fv");
        this.glProgramUniformMatrix4x3fv = functionAddress53;
        final boolean b53 = b52 & functionAddress53 != 0L;
        final long functionAddress54 = GLContext.getFunctionAddress("glProgramUniformMatrix2x3dv");
        this.glProgramUniformMatrix2x3dv = functionAddress54;
        final boolean b54 = b53 & functionAddress54 != 0L;
        final long functionAddress55 = GLContext.getFunctionAddress("glProgramUniformMatrix3x2dv");
        this.glProgramUniformMatrix3x2dv = functionAddress55;
        final boolean b55 = b54 & functionAddress55 != 0L;
        final long functionAddress56 = GLContext.getFunctionAddress("glProgramUniformMatrix2x4dv");
        this.glProgramUniformMatrix2x4dv = functionAddress56;
        final boolean b56 = b55 & functionAddress56 != 0L;
        final long functionAddress57 = GLContext.getFunctionAddress("glProgramUniformMatrix4x2dv");
        this.glProgramUniformMatrix4x2dv = functionAddress57;
        final boolean b57 = b56 & functionAddress57 != 0L;
        final long functionAddress58 = GLContext.getFunctionAddress("glProgramUniformMatrix3x4dv");
        this.glProgramUniformMatrix3x4dv = functionAddress58;
        final boolean b58 = b57 & functionAddress58 != 0L;
        final long functionAddress59 = GLContext.getFunctionAddress("glProgramUniformMatrix4x3dv");
        this.glProgramUniformMatrix4x3dv = functionAddress59;
        final boolean b59 = b58 & functionAddress59 != 0L;
        final long functionAddress60 = GLContext.getFunctionAddress("glValidateProgramPipeline");
        this.glValidateProgramPipeline = functionAddress60;
        final boolean b60 = b59 & functionAddress60 != 0L;
        final long functionAddress61 = GLContext.getFunctionAddress("glGetProgramPipelineInfoLog");
        this.glGetProgramPipelineInfoLog = functionAddress61;
        return b60 & functionAddress61 != 0L;
    }
    
    private boolean ARB_shader_atomic_counters_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glGetActiveAtomicCounterBufferiv");
        this.glGetActiveAtomicCounterBufferiv = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean ARB_shader_image_load_store_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glBindImageTexture");
        this.glBindImageTexture = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glMemoryBarrier");
        this.glMemoryBarrier = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean ARB_shader_objects_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glDeleteObjectARB");
        this.glDeleteObjectARB = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glGetHandleARB");
        this.glGetHandleARB = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glDetachObjectARB");
        this.glDetachObjectARB = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glCreateShaderObjectARB");
        this.glCreateShaderObjectARB = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glShaderSourceARB");
        this.glShaderSourceARB = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glCompileShaderARB");
        this.glCompileShaderARB = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glCreateProgramObjectARB");
        this.glCreateProgramObjectARB = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glAttachObjectARB");
        this.glAttachObjectARB = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glLinkProgramARB");
        this.glLinkProgramARB = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glUseProgramObjectARB");
        this.glUseProgramObjectARB = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glValidateProgramARB");
        this.glValidateProgramARB = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glUniform1fARB");
        this.glUniform1fARB = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glUniform2fARB");
        this.glUniform2fARB = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glUniform3fARB");
        this.glUniform3fARB = functionAddress14;
        final boolean b14 = b13 & functionAddress14 != 0L;
        final long functionAddress15 = GLContext.getFunctionAddress("glUniform4fARB");
        this.glUniform4fARB = functionAddress15;
        final boolean b15 = b14 & functionAddress15 != 0L;
        final long functionAddress16 = GLContext.getFunctionAddress("glUniform1iARB");
        this.glUniform1iARB = functionAddress16;
        final boolean b16 = b15 & functionAddress16 != 0L;
        final long functionAddress17 = GLContext.getFunctionAddress("glUniform2iARB");
        this.glUniform2iARB = functionAddress17;
        final boolean b17 = b16 & functionAddress17 != 0L;
        final long functionAddress18 = GLContext.getFunctionAddress("glUniform3iARB");
        this.glUniform3iARB = functionAddress18;
        final boolean b18 = b17 & functionAddress18 != 0L;
        final long functionAddress19 = GLContext.getFunctionAddress("glUniform4iARB");
        this.glUniform4iARB = functionAddress19;
        final boolean b19 = b18 & functionAddress19 != 0L;
        final long functionAddress20 = GLContext.getFunctionAddress("glUniform1fvARB");
        this.glUniform1fvARB = functionAddress20;
        final boolean b20 = b19 & functionAddress20 != 0L;
        final long functionAddress21 = GLContext.getFunctionAddress("glUniform2fvARB");
        this.glUniform2fvARB = functionAddress21;
        final boolean b21 = b20 & functionAddress21 != 0L;
        final long functionAddress22 = GLContext.getFunctionAddress("glUniform3fvARB");
        this.glUniform3fvARB = functionAddress22;
        final boolean b22 = b21 & functionAddress22 != 0L;
        final long functionAddress23 = GLContext.getFunctionAddress("glUniform4fvARB");
        this.glUniform4fvARB = functionAddress23;
        final boolean b23 = b22 & functionAddress23 != 0L;
        final long functionAddress24 = GLContext.getFunctionAddress("glUniform1ivARB");
        this.glUniform1ivARB = functionAddress24;
        final boolean b24 = b23 & functionAddress24 != 0L;
        final long functionAddress25 = GLContext.getFunctionAddress("glUniform2ivARB");
        this.glUniform2ivARB = functionAddress25;
        final boolean b25 = b24 & functionAddress25 != 0L;
        final long functionAddress26 = GLContext.getFunctionAddress("glUniform3ivARB");
        this.glUniform3ivARB = functionAddress26;
        final boolean b26 = b25 & functionAddress26 != 0L;
        final long functionAddress27 = GLContext.getFunctionAddress("glUniform4ivARB");
        this.glUniform4ivARB = functionAddress27;
        final boolean b27 = b26 & functionAddress27 != 0L;
        final long functionAddress28 = GLContext.getFunctionAddress("glUniformMatrix2fvARB");
        this.glUniformMatrix2fvARB = functionAddress28;
        final boolean b28 = b27 & functionAddress28 != 0L;
        final long functionAddress29 = GLContext.getFunctionAddress("glUniformMatrix3fvARB");
        this.glUniformMatrix3fvARB = functionAddress29;
        final boolean b29 = b28 & functionAddress29 != 0L;
        final long functionAddress30 = GLContext.getFunctionAddress("glUniformMatrix4fvARB");
        this.glUniformMatrix4fvARB = functionAddress30;
        final boolean b30 = b29 & functionAddress30 != 0L;
        final long functionAddress31 = GLContext.getFunctionAddress("glGetObjectParameterfvARB");
        this.glGetObjectParameterfvARB = functionAddress31;
        final boolean b31 = b30 & functionAddress31 != 0L;
        final long functionAddress32 = GLContext.getFunctionAddress("glGetObjectParameterivARB");
        this.glGetObjectParameterivARB = functionAddress32;
        final boolean b32 = b31 & functionAddress32 != 0L;
        final long functionAddress33 = GLContext.getFunctionAddress("glGetInfoLogARB");
        this.glGetInfoLogARB = functionAddress33;
        final boolean b33 = b32 & functionAddress33 != 0L;
        final long functionAddress34 = GLContext.getFunctionAddress("glGetAttachedObjectsARB");
        this.glGetAttachedObjectsARB = functionAddress34;
        final boolean b34 = b33 & functionAddress34 != 0L;
        final long functionAddress35 = GLContext.getFunctionAddress("glGetUniformLocationARB");
        this.glGetUniformLocationARB = functionAddress35;
        final boolean b35 = b34 & functionAddress35 != 0L;
        final long functionAddress36 = GLContext.getFunctionAddress("glGetActiveUniformARB");
        this.glGetActiveUniformARB = functionAddress36;
        final boolean b36 = b35 & functionAddress36 != 0L;
        final long functionAddress37 = GLContext.getFunctionAddress("glGetUniformfvARB");
        this.glGetUniformfvARB = functionAddress37;
        final boolean b37 = b36 & functionAddress37 != 0L;
        final long functionAddress38 = GLContext.getFunctionAddress("glGetUniformivARB");
        this.glGetUniformivARB = functionAddress38;
        final boolean b38 = b37 & functionAddress38 != 0L;
        final long functionAddress39 = GLContext.getFunctionAddress("glGetShaderSourceARB");
        this.glGetShaderSourceARB = functionAddress39;
        return b38 & functionAddress39 != 0L;
    }
    
    private boolean ARB_shader_storage_buffer_object_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glShaderStorageBlockBinding");
        this.glShaderStorageBlockBinding = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean ARB_shader_subroutine_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glGetSubroutineUniformLocation");
        this.glGetSubroutineUniformLocation = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glGetSubroutineIndex");
        this.glGetSubroutineIndex = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glGetActiveSubroutineUniformiv");
        this.glGetActiveSubroutineUniformiv = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glGetActiveSubroutineUniformName");
        this.glGetActiveSubroutineUniformName = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glGetActiveSubroutineName");
        this.glGetActiveSubroutineName = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glUniformSubroutinesuiv");
        this.glUniformSubroutinesuiv = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glGetUniformSubroutineuiv");
        this.glGetUniformSubroutineuiv = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glGetProgramStageiv");
        this.glGetProgramStageiv = functionAddress8;
        return b7 & functionAddress8 != 0L;
    }
    
    private boolean ARB_shading_language_include_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glNamedStringARB");
        this.glNamedStringARB = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glDeleteNamedStringARB");
        this.glDeleteNamedStringARB = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glCompileShaderIncludeARB");
        this.glCompileShaderIncludeARB = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glIsNamedStringARB");
        this.glIsNamedStringARB = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glGetNamedStringARB");
        this.glGetNamedStringARB = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glGetNamedStringivARB");
        this.glGetNamedStringivARB = functionAddress6;
        return b5 & functionAddress6 != 0L;
    }
    
    private boolean ARB_sparse_buffer_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glBufferPageCommitmentARB");
        this.glBufferPageCommitmentARB = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean ARB_sparse_texture_initNativeFunctionAddresses(final Set<String> supported_extensions) {
        final long functionAddress = GLContext.getFunctionAddress("glTexPageCommitmentARB");
        this.glTexPageCommitmentARB = functionAddress;
        final boolean b = functionAddress != 0L;
        if (supported_extensions.contains("GL_EXT_direct_state_access")) {
            final long functionAddress2 = GLContext.getFunctionAddress("glTexturePageCommitmentEXT");
            this.glTexturePageCommitmentEXT = functionAddress2;
            if (functionAddress2 == 0L) {
                final boolean b2 = false;
                return b & b2;
            }
        }
        final boolean b2 = true;
        return b & b2;
    }
    
    private boolean ARB_sync_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glFenceSync");
        this.glFenceSync = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glIsSync");
        this.glIsSync = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glDeleteSync");
        this.glDeleteSync = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glClientWaitSync");
        this.glClientWaitSync = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glWaitSync");
        this.glWaitSync = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glGetInteger64v");
        this.glGetInteger64v = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glGetSynciv");
        this.glGetSynciv = functionAddress7;
        return b6 & functionAddress7 != 0L;
    }
    
    private boolean ARB_tessellation_shader_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glPatchParameteri");
        this.glPatchParameteri = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glPatchParameterfv");
        this.glPatchParameterfv = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean ARB_texture_barrier_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glTextureBarrier");
        this.glTextureBarrier = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean ARB_texture_buffer_object_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glTexBufferARB");
        this.glTexBufferARB = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean ARB_texture_buffer_range_initNativeFunctionAddresses(final Set<String> supported_extensions) {
        final long functionAddress = GLContext.getFunctionAddress("glTexBufferRange");
        this.glTexBufferRange = functionAddress;
        final boolean b = functionAddress != 0L;
        if (supported_extensions.contains("GL_EXT_direct_state_access")) {
            final long functionAddress2 = GLContext.getFunctionAddress("glTextureBufferRangeEXT");
            this.glTextureBufferRangeEXT = functionAddress2;
            if (functionAddress2 == 0L) {
                final boolean b2 = false;
                return b & b2;
            }
        }
        final boolean b2 = true;
        return b & b2;
    }
    
    private boolean ARB_texture_compression_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glCompressedTexImage1DARB");
        this.glCompressedTexImage1DARB = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glCompressedTexImage2DARB");
        this.glCompressedTexImage2DARB = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glCompressedTexImage3DARB");
        this.glCompressedTexImage3DARB = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glCompressedTexSubImage1DARB");
        this.glCompressedTexSubImage1DARB = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glCompressedTexSubImage2DARB");
        this.glCompressedTexSubImage2DARB = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glCompressedTexSubImage3DARB");
        this.glCompressedTexSubImage3DARB = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glGetCompressedTexImageARB");
        this.glGetCompressedTexImageARB = functionAddress7;
        return b6 & functionAddress7 != 0L;
    }
    
    private boolean ARB_texture_multisample_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glTexImage2DMultisample");
        this.glTexImage2DMultisample = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glTexImage3DMultisample");
        this.glTexImage3DMultisample = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glGetMultisamplefv");
        this.glGetMultisamplefv = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glSampleMaski");
        this.glSampleMaski = functionAddress4;
        return b3 & functionAddress4 != 0L;
    }
    
    private boolean ARB_texture_storage_initNativeFunctionAddresses(final Set<String> supported_extensions) {
        final long functionAddress = GLContext.getFunctionAddress(new String[] { "glTexStorage1D", "glTexStorage1DEXT" });
        this.glTexStorage1D = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress(new String[] { "glTexStorage2D", "glTexStorage2DEXT" });
        this.glTexStorage2D = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress(new String[] { "glTexStorage3D", "glTexStorage3DEXT" });
        this.glTexStorage3D = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        boolean b4 = false;
        Label_0149: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress4 = GLContext.getFunctionAddress(new String[] { "glTextureStorage1DEXT", "glTextureStorage1DEXTEXT" });
                this.glTextureStorage1DEXT = functionAddress4;
                if (functionAddress4 == 0L) {
                    b4 = false;
                    break Label_0149;
                }
            }
            b4 = true;
        }
        final boolean b5 = b3 & b4;
        boolean b6 = false;
        Label_0195: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress5 = GLContext.getFunctionAddress(new String[] { "glTextureStorage2DEXT", "glTextureStorage2DEXTEXT" });
                this.glTextureStorage2DEXT = functionAddress5;
                if (functionAddress5 == 0L) {
                    b6 = false;
                    break Label_0195;
                }
            }
            b6 = true;
        }
        final boolean b7 = b5 & b6;
        if (supported_extensions.contains("GL_EXT_direct_state_access")) {
            final long functionAddress6 = GLContext.getFunctionAddress(new String[] { "glTextureStorage3DEXT", "glTextureStorage3DEXTEXT" });
            this.glTextureStorage3DEXT = functionAddress6;
            if (functionAddress6 == 0L) {
                final boolean b8 = false;
                return b7 & b8;
            }
        }
        final boolean b8 = true;
        return b7 & b8;
    }
    
    private boolean ARB_texture_storage_multisample_initNativeFunctionAddresses(final Set<String> supported_extensions) {
        final long functionAddress = GLContext.getFunctionAddress("glTexStorage2DMultisample");
        this.glTexStorage2DMultisample = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glTexStorage3DMultisample");
        this.glTexStorage3DMultisample = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        boolean b3 = false;
        Label_0075: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress3 = GLContext.getFunctionAddress("glTextureStorage2DMultisampleEXT");
                this.glTextureStorage2DMultisampleEXT = functionAddress3;
                if (functionAddress3 == 0L) {
                    b3 = false;
                    break Label_0075;
                }
            }
            b3 = true;
        }
        final boolean b4 = b2 & b3;
        if (supported_extensions.contains("GL_EXT_direct_state_access")) {
            final long functionAddress4 = GLContext.getFunctionAddress("glTextureStorage3DMultisampleEXT");
            this.glTextureStorage3DMultisampleEXT = functionAddress4;
            if (functionAddress4 == 0L) {
                final boolean b5 = false;
                return b4 & b5;
            }
        }
        final boolean b5 = true;
        return b4 & b5;
    }
    
    private boolean ARB_texture_view_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glTextureView");
        this.glTextureView = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean ARB_timer_query_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glQueryCounter");
        this.glQueryCounter = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glGetQueryObjecti64v");
        this.glGetQueryObjecti64v = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glGetQueryObjectui64v");
        this.glGetQueryObjectui64v = functionAddress3;
        return b2 & functionAddress3 != 0L;
    }
    
    private boolean ARB_transform_feedback2_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glBindTransformFeedback");
        this.glBindTransformFeedback = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glDeleteTransformFeedbacks");
        this.glDeleteTransformFeedbacks = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glGenTransformFeedbacks");
        this.glGenTransformFeedbacks = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glIsTransformFeedback");
        this.glIsTransformFeedback = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glPauseTransformFeedback");
        this.glPauseTransformFeedback = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glResumeTransformFeedback");
        this.glResumeTransformFeedback = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glDrawTransformFeedback");
        this.glDrawTransformFeedback = functionAddress7;
        return b6 & functionAddress7 != 0L;
    }
    
    private boolean ARB_transform_feedback3_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glDrawTransformFeedbackStream");
        this.glDrawTransformFeedbackStream = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glBeginQueryIndexed");
        this.glBeginQueryIndexed = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glEndQueryIndexed");
        this.glEndQueryIndexed = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glGetQueryIndexediv");
        this.glGetQueryIndexediv = functionAddress4;
        return b3 & functionAddress4 != 0L;
    }
    
    private boolean ARB_transform_feedback_instanced_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glDrawTransformFeedbackInstanced");
        this.glDrawTransformFeedbackInstanced = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glDrawTransformFeedbackStreamInstanced");
        this.glDrawTransformFeedbackStreamInstanced = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean ARB_transpose_matrix_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glLoadTransposeMatrixfARB");
        this.glLoadTransposeMatrixfARB = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glMultTransposeMatrixfARB");
        this.glMultTransposeMatrixfARB = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean ARB_uniform_buffer_object_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glGetUniformIndices");
        this.glGetUniformIndices = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glGetActiveUniformsiv");
        this.glGetActiveUniformsiv = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glGetActiveUniformName");
        this.glGetActiveUniformName = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glGetUniformBlockIndex");
        this.glGetUniformBlockIndex = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glGetActiveUniformBlockiv");
        this.glGetActiveUniformBlockiv = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glGetActiveUniformBlockName");
        this.glGetActiveUniformBlockName = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glBindBufferRange");
        this.glBindBufferRange = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glBindBufferBase");
        this.glBindBufferBase = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glGetIntegeri_v");
        this.glGetIntegeri_v = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glUniformBlockBinding");
        this.glUniformBlockBinding = functionAddress10;
        return b9 & functionAddress10 != 0L;
    }
    
    private boolean ARB_vertex_array_object_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glBindVertexArray");
        this.glBindVertexArray = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glDeleteVertexArrays");
        this.glDeleteVertexArrays = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glGenVertexArrays");
        this.glGenVertexArrays = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glIsVertexArray");
        this.glIsVertexArray = functionAddress4;
        return b3 & functionAddress4 != 0L;
    }
    
    private boolean ARB_vertex_attrib_64bit_initNativeFunctionAddresses(final Set<String> supported_extensions) {
        final long functionAddress = GLContext.getFunctionAddress("glVertexAttribL1d");
        this.glVertexAttribL1d = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glVertexAttribL2d");
        this.glVertexAttribL2d = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glVertexAttribL3d");
        this.glVertexAttribL3d = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glVertexAttribL4d");
        this.glVertexAttribL4d = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glVertexAttribL1dv");
        this.glVertexAttribL1dv = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glVertexAttribL2dv");
        this.glVertexAttribL2dv = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glVertexAttribL3dv");
        this.glVertexAttribL3dv = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glVertexAttribL4dv");
        this.glVertexAttribL4dv = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glVertexAttribLPointer");
        this.glVertexAttribLPointer = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glGetVertexAttribLdv");
        this.glGetVertexAttribLdv = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        if (supported_extensions.contains("GL_EXT_direct_state_access")) {
            final long functionAddress11 = GLContext.getFunctionAddress("glVertexArrayVertexAttribLOffsetEXT");
            this.glVertexArrayVertexAttribLOffsetEXT = functionAddress11;
            if (functionAddress11 == 0L) {
                final boolean b11 = false;
                return b10 & b11;
            }
        }
        final boolean b11 = true;
        return b10 & b11;
    }
    
    private boolean ARB_vertex_attrib_binding_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glBindVertexBuffer");
        this.glBindVertexBuffer = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glVertexAttribFormat");
        this.glVertexAttribFormat = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glVertexAttribIFormat");
        this.glVertexAttribIFormat = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glVertexAttribLFormat");
        this.glVertexAttribLFormat = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glVertexAttribBinding");
        this.glVertexAttribBinding = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glVertexBindingDivisor");
        this.glVertexBindingDivisor = functionAddress6;
        return b5 & functionAddress6 != 0L;
    }
    
    private boolean ARB_vertex_blend_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glWeightbvARB");
        this.glWeightbvARB = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glWeightsvARB");
        this.glWeightsvARB = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glWeightivARB");
        this.glWeightivARB = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glWeightfvARB");
        this.glWeightfvARB = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glWeightdvARB");
        this.glWeightdvARB = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glWeightubvARB");
        this.glWeightubvARB = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glWeightusvARB");
        this.glWeightusvARB = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glWeightuivARB");
        this.glWeightuivARB = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glWeightPointerARB");
        this.glWeightPointerARB = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glVertexBlendARB");
        this.glVertexBlendARB = functionAddress10;
        return b9 & functionAddress10 != 0L;
    }
    
    private boolean ARB_vertex_program_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glVertexAttrib1sARB");
        this.glVertexAttrib1sARB = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glVertexAttrib1fARB");
        this.glVertexAttrib1fARB = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glVertexAttrib1dARB");
        this.glVertexAttrib1dARB = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glVertexAttrib2sARB");
        this.glVertexAttrib2sARB = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glVertexAttrib2fARB");
        this.glVertexAttrib2fARB = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glVertexAttrib2dARB");
        this.glVertexAttrib2dARB = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glVertexAttrib3sARB");
        this.glVertexAttrib3sARB = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glVertexAttrib3fARB");
        this.glVertexAttrib3fARB = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glVertexAttrib3dARB");
        this.glVertexAttrib3dARB = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glVertexAttrib4sARB");
        this.glVertexAttrib4sARB = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glVertexAttrib4fARB");
        this.glVertexAttrib4fARB = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glVertexAttrib4dARB");
        this.glVertexAttrib4dARB = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glVertexAttrib4NubARB");
        this.glVertexAttrib4NubARB = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glVertexAttribPointerARB");
        this.glVertexAttribPointerARB = functionAddress14;
        final boolean b14 = b13 & functionAddress14 != 0L;
        final long functionAddress15 = GLContext.getFunctionAddress("glEnableVertexAttribArrayARB");
        this.glEnableVertexAttribArrayARB = functionAddress15;
        final boolean b15 = b14 & functionAddress15 != 0L;
        final long functionAddress16 = GLContext.getFunctionAddress("glDisableVertexAttribArrayARB");
        this.glDisableVertexAttribArrayARB = functionAddress16;
        final boolean b16 = b15 & functionAddress16 != 0L;
        final long functionAddress17 = GLContext.getFunctionAddress("glGetVertexAttribfvARB");
        this.glGetVertexAttribfvARB = functionAddress17;
        final boolean b17 = b16 & functionAddress17 != 0L;
        final long functionAddress18 = GLContext.getFunctionAddress("glGetVertexAttribdvARB");
        this.glGetVertexAttribdvARB = functionAddress18;
        final boolean b18 = b17 & functionAddress18 != 0L;
        final long functionAddress19 = GLContext.getFunctionAddress("glGetVertexAttribivARB");
        this.glGetVertexAttribivARB = functionAddress19;
        final boolean b19 = b18 & functionAddress19 != 0L;
        final long functionAddress20 = GLContext.getFunctionAddress("glGetVertexAttribPointervARB");
        this.glGetVertexAttribPointervARB = functionAddress20;
        return b19 & functionAddress20 != 0L;
    }
    
    private boolean ARB_vertex_shader_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glVertexAttrib1sARB");
        this.glVertexAttrib1sARB = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glVertexAttrib1fARB");
        this.glVertexAttrib1fARB = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glVertexAttrib1dARB");
        this.glVertexAttrib1dARB = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glVertexAttrib2sARB");
        this.glVertexAttrib2sARB = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glVertexAttrib2fARB");
        this.glVertexAttrib2fARB = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glVertexAttrib2dARB");
        this.glVertexAttrib2dARB = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glVertexAttrib3sARB");
        this.glVertexAttrib3sARB = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glVertexAttrib3fARB");
        this.glVertexAttrib3fARB = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glVertexAttrib3dARB");
        this.glVertexAttrib3dARB = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glVertexAttrib4sARB");
        this.glVertexAttrib4sARB = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glVertexAttrib4fARB");
        this.glVertexAttrib4fARB = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glVertexAttrib4dARB");
        this.glVertexAttrib4dARB = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glVertexAttrib4NubARB");
        this.glVertexAttrib4NubARB = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glVertexAttribPointerARB");
        this.glVertexAttribPointerARB = functionAddress14;
        final boolean b14 = b13 & functionAddress14 != 0L;
        final long functionAddress15 = GLContext.getFunctionAddress("glEnableVertexAttribArrayARB");
        this.glEnableVertexAttribArrayARB = functionAddress15;
        final boolean b15 = b14 & functionAddress15 != 0L;
        final long functionAddress16 = GLContext.getFunctionAddress("glDisableVertexAttribArrayARB");
        this.glDisableVertexAttribArrayARB = functionAddress16;
        final boolean b16 = b15 & functionAddress16 != 0L;
        final long functionAddress17 = GLContext.getFunctionAddress("glBindAttribLocationARB");
        this.glBindAttribLocationARB = functionAddress17;
        final boolean b17 = b16 & functionAddress17 != 0L;
        final long functionAddress18 = GLContext.getFunctionAddress("glGetActiveAttribARB");
        this.glGetActiveAttribARB = functionAddress18;
        final boolean b18 = b17 & functionAddress18 != 0L;
        final long functionAddress19 = GLContext.getFunctionAddress("glGetAttribLocationARB");
        this.glGetAttribLocationARB = functionAddress19;
        final boolean b19 = b18 & functionAddress19 != 0L;
        final long functionAddress20 = GLContext.getFunctionAddress("glGetVertexAttribfvARB");
        this.glGetVertexAttribfvARB = functionAddress20;
        final boolean b20 = b19 & functionAddress20 != 0L;
        final long functionAddress21 = GLContext.getFunctionAddress("glGetVertexAttribdvARB");
        this.glGetVertexAttribdvARB = functionAddress21;
        final boolean b21 = b20 & functionAddress21 != 0L;
        final long functionAddress22 = GLContext.getFunctionAddress("glGetVertexAttribivARB");
        this.glGetVertexAttribivARB = functionAddress22;
        final boolean b22 = b21 & functionAddress22 != 0L;
        final long functionAddress23 = GLContext.getFunctionAddress("glGetVertexAttribPointervARB");
        this.glGetVertexAttribPointervARB = functionAddress23;
        return b22 & functionAddress23 != 0L;
    }
    
    private boolean ARB_vertex_type_2_10_10_10_rev_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glVertexP2ui");
        this.glVertexP2ui = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glVertexP3ui");
        this.glVertexP3ui = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glVertexP4ui");
        this.glVertexP4ui = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glVertexP2uiv");
        this.glVertexP2uiv = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glVertexP3uiv");
        this.glVertexP3uiv = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glVertexP4uiv");
        this.glVertexP4uiv = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glTexCoordP1ui");
        this.glTexCoordP1ui = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glTexCoordP2ui");
        this.glTexCoordP2ui = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glTexCoordP3ui");
        this.glTexCoordP3ui = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glTexCoordP4ui");
        this.glTexCoordP4ui = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glTexCoordP1uiv");
        this.glTexCoordP1uiv = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glTexCoordP2uiv");
        this.glTexCoordP2uiv = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glTexCoordP3uiv");
        this.glTexCoordP3uiv = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glTexCoordP4uiv");
        this.glTexCoordP4uiv = functionAddress14;
        final boolean b14 = b13 & functionAddress14 != 0L;
        final long functionAddress15 = GLContext.getFunctionAddress("glMultiTexCoordP1ui");
        this.glMultiTexCoordP1ui = functionAddress15;
        final boolean b15 = b14 & functionAddress15 != 0L;
        final long functionAddress16 = GLContext.getFunctionAddress("glMultiTexCoordP2ui");
        this.glMultiTexCoordP2ui = functionAddress16;
        final boolean b16 = b15 & functionAddress16 != 0L;
        final long functionAddress17 = GLContext.getFunctionAddress("glMultiTexCoordP3ui");
        this.glMultiTexCoordP3ui = functionAddress17;
        final boolean b17 = b16 & functionAddress17 != 0L;
        final long functionAddress18 = GLContext.getFunctionAddress("glMultiTexCoordP4ui");
        this.glMultiTexCoordP4ui = functionAddress18;
        final boolean b18 = b17 & functionAddress18 != 0L;
        final long functionAddress19 = GLContext.getFunctionAddress("glMultiTexCoordP1uiv");
        this.glMultiTexCoordP1uiv = functionAddress19;
        final boolean b19 = b18 & functionAddress19 != 0L;
        final long functionAddress20 = GLContext.getFunctionAddress("glMultiTexCoordP2uiv");
        this.glMultiTexCoordP2uiv = functionAddress20;
        final boolean b20 = b19 & functionAddress20 != 0L;
        final long functionAddress21 = GLContext.getFunctionAddress("glMultiTexCoordP3uiv");
        this.glMultiTexCoordP3uiv = functionAddress21;
        final boolean b21 = b20 & functionAddress21 != 0L;
        final long functionAddress22 = GLContext.getFunctionAddress("glMultiTexCoordP4uiv");
        this.glMultiTexCoordP4uiv = functionAddress22;
        final boolean b22 = b21 & functionAddress22 != 0L;
        final long functionAddress23 = GLContext.getFunctionAddress("glNormalP3ui");
        this.glNormalP3ui = functionAddress23;
        final boolean b23 = b22 & functionAddress23 != 0L;
        final long functionAddress24 = GLContext.getFunctionAddress("glNormalP3uiv");
        this.glNormalP3uiv = functionAddress24;
        final boolean b24 = b23 & functionAddress24 != 0L;
        final long functionAddress25 = GLContext.getFunctionAddress("glColorP3ui");
        this.glColorP3ui = functionAddress25;
        final boolean b25 = b24 & functionAddress25 != 0L;
        final long functionAddress26 = GLContext.getFunctionAddress("glColorP4ui");
        this.glColorP4ui = functionAddress26;
        final boolean b26 = b25 & functionAddress26 != 0L;
        final long functionAddress27 = GLContext.getFunctionAddress("glColorP3uiv");
        this.glColorP3uiv = functionAddress27;
        final boolean b27 = b26 & functionAddress27 != 0L;
        final long functionAddress28 = GLContext.getFunctionAddress("glColorP4uiv");
        this.glColorP4uiv = functionAddress28;
        final boolean b28 = b27 & functionAddress28 != 0L;
        final long functionAddress29 = GLContext.getFunctionAddress("glSecondaryColorP3ui");
        this.glSecondaryColorP3ui = functionAddress29;
        final boolean b29 = b28 & functionAddress29 != 0L;
        final long functionAddress30 = GLContext.getFunctionAddress("glSecondaryColorP3uiv");
        this.glSecondaryColorP3uiv = functionAddress30;
        final boolean b30 = b29 & functionAddress30 != 0L;
        final long functionAddress31 = GLContext.getFunctionAddress("glVertexAttribP1ui");
        this.glVertexAttribP1ui = functionAddress31;
        final boolean b31 = b30 & functionAddress31 != 0L;
        final long functionAddress32 = GLContext.getFunctionAddress("glVertexAttribP2ui");
        this.glVertexAttribP2ui = functionAddress32;
        final boolean b32 = b31 & functionAddress32 != 0L;
        final long functionAddress33 = GLContext.getFunctionAddress("glVertexAttribP3ui");
        this.glVertexAttribP3ui = functionAddress33;
        final boolean b33 = b32 & functionAddress33 != 0L;
        final long functionAddress34 = GLContext.getFunctionAddress("glVertexAttribP4ui");
        this.glVertexAttribP4ui = functionAddress34;
        final boolean b34 = b33 & functionAddress34 != 0L;
        final long functionAddress35 = GLContext.getFunctionAddress("glVertexAttribP1uiv");
        this.glVertexAttribP1uiv = functionAddress35;
        final boolean b35 = b34 & functionAddress35 != 0L;
        final long functionAddress36 = GLContext.getFunctionAddress("glVertexAttribP2uiv");
        this.glVertexAttribP2uiv = functionAddress36;
        final boolean b36 = b35 & functionAddress36 != 0L;
        final long functionAddress37 = GLContext.getFunctionAddress("glVertexAttribP3uiv");
        this.glVertexAttribP3uiv = functionAddress37;
        final boolean b37 = b36 & functionAddress37 != 0L;
        final long functionAddress38 = GLContext.getFunctionAddress("glVertexAttribP4uiv");
        this.glVertexAttribP4uiv = functionAddress38;
        return b37 & functionAddress38 != 0L;
    }
    
    private boolean ARB_viewport_array_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glViewportArrayv");
        this.glViewportArrayv = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glViewportIndexedf");
        this.glViewportIndexedf = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glViewportIndexedfv");
        this.glViewportIndexedfv = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glScissorArrayv");
        this.glScissorArrayv = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glScissorIndexed");
        this.glScissorIndexed = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glScissorIndexedv");
        this.glScissorIndexedv = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glDepthRangeArrayv");
        this.glDepthRangeArrayv = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glDepthRangeIndexed");
        this.glDepthRangeIndexed = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glGetFloati_v");
        this.glGetFloati_v = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glGetDoublei_v");
        this.glGetDoublei_v = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glGetIntegerIndexedvEXT");
        this.glGetIntegerIndexedvEXT = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glEnableIndexedEXT");
        this.glEnableIndexedEXT = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glDisableIndexedEXT");
        this.glDisableIndexedEXT = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glIsEnabledIndexedEXT");
        this.glIsEnabledIndexedEXT = functionAddress14;
        return b13 & functionAddress14 != 0L;
    }
    
    private boolean ARB_window_pos_initNativeFunctionAddresses(final boolean forwardCompatible) {
        boolean b = false;
        Label_0025: {
            if (!forwardCompatible) {
                final long functionAddress = GLContext.getFunctionAddress("glWindowPos2fARB");
                this.glWindowPos2fARB = functionAddress;
                if (functionAddress == 0L) {
                    b = false;
                    break Label_0025;
                }
            }
            b = true;
        }
        boolean b2 = false;
        Label_0050: {
            if (!forwardCompatible) {
                final long functionAddress2 = GLContext.getFunctionAddress("glWindowPos2dARB");
                this.glWindowPos2dARB = functionAddress2;
                if (functionAddress2 == 0L) {
                    b2 = false;
                    break Label_0050;
                }
            }
            b2 = true;
        }
        final boolean b3 = b & b2;
        boolean b4 = false;
        Label_0076: {
            if (!forwardCompatible) {
                final long functionAddress3 = GLContext.getFunctionAddress("glWindowPos2iARB");
                this.glWindowPos2iARB = functionAddress3;
                if (functionAddress3 == 0L) {
                    b4 = false;
                    break Label_0076;
                }
            }
            b4 = true;
        }
        final boolean b5 = b3 & b4;
        boolean b6 = false;
        Label_0102: {
            if (!forwardCompatible) {
                final long functionAddress4 = GLContext.getFunctionAddress("glWindowPos2sARB");
                this.glWindowPos2sARB = functionAddress4;
                if (functionAddress4 == 0L) {
                    b6 = false;
                    break Label_0102;
                }
            }
            b6 = true;
        }
        final boolean b7 = b5 & b6;
        boolean b8 = false;
        Label_0128: {
            if (!forwardCompatible) {
                final long functionAddress5 = GLContext.getFunctionAddress("glWindowPos3fARB");
                this.glWindowPos3fARB = functionAddress5;
                if (functionAddress5 == 0L) {
                    b8 = false;
                    break Label_0128;
                }
            }
            b8 = true;
        }
        final boolean b9 = b7 & b8;
        boolean b10 = false;
        Label_0154: {
            if (!forwardCompatible) {
                final long functionAddress6 = GLContext.getFunctionAddress("glWindowPos3dARB");
                this.glWindowPos3dARB = functionAddress6;
                if (functionAddress6 == 0L) {
                    b10 = false;
                    break Label_0154;
                }
            }
            b10 = true;
        }
        final boolean b11 = b9 & b10;
        boolean b12 = false;
        Label_0180: {
            if (!forwardCompatible) {
                final long functionAddress7 = GLContext.getFunctionAddress("glWindowPos3iARB");
                this.glWindowPos3iARB = functionAddress7;
                if (functionAddress7 == 0L) {
                    b12 = false;
                    break Label_0180;
                }
            }
            b12 = true;
        }
        final boolean b13 = b11 & b12;
        if (!forwardCompatible) {
            final long functionAddress8 = GLContext.getFunctionAddress("glWindowPos3sARB");
            this.glWindowPos3sARB = functionAddress8;
            if (functionAddress8 == 0L) {
                final boolean b14 = false;
                return b13 & b14;
            }
        }
        final boolean b14 = true;
        return b13 & b14;
    }
    
    private boolean ATI_draw_buffers_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glDrawBuffersATI");
        this.glDrawBuffersATI = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean ATI_element_array_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glElementPointerATI");
        this.glElementPointerATI = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glDrawElementArrayATI");
        this.glDrawElementArrayATI = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glDrawRangeElementArrayATI");
        this.glDrawRangeElementArrayATI = functionAddress3;
        return b2 & functionAddress3 != 0L;
    }
    
    private boolean ATI_envmap_bumpmap_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glTexBumpParameterfvATI");
        this.glTexBumpParameterfvATI = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glTexBumpParameterivATI");
        this.glTexBumpParameterivATI = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glGetTexBumpParameterfvATI");
        this.glGetTexBumpParameterfvATI = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glGetTexBumpParameterivATI");
        this.glGetTexBumpParameterivATI = functionAddress4;
        return b3 & functionAddress4 != 0L;
    }
    
    private boolean ATI_fragment_shader_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glGenFragmentShadersATI");
        this.glGenFragmentShadersATI = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glBindFragmentShaderATI");
        this.glBindFragmentShaderATI = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glDeleteFragmentShaderATI");
        this.glDeleteFragmentShaderATI = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glBeginFragmentShaderATI");
        this.glBeginFragmentShaderATI = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glEndFragmentShaderATI");
        this.glEndFragmentShaderATI = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glPassTexCoordATI");
        this.glPassTexCoordATI = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glSampleMapATI");
        this.glSampleMapATI = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glColorFragmentOp1ATI");
        this.glColorFragmentOp1ATI = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glColorFragmentOp2ATI");
        this.glColorFragmentOp2ATI = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glColorFragmentOp3ATI");
        this.glColorFragmentOp3ATI = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glAlphaFragmentOp1ATI");
        this.glAlphaFragmentOp1ATI = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glAlphaFragmentOp2ATI");
        this.glAlphaFragmentOp2ATI = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glAlphaFragmentOp3ATI");
        this.glAlphaFragmentOp3ATI = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glSetFragmentShaderConstantATI");
        this.glSetFragmentShaderConstantATI = functionAddress14;
        return b13 & functionAddress14 != 0L;
    }
    
    private boolean ATI_map_object_buffer_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glMapObjectBufferATI");
        this.glMapObjectBufferATI = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glUnmapObjectBufferATI");
        this.glUnmapObjectBufferATI = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean ATI_pn_triangles_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glPNTrianglesfATI");
        this.glPNTrianglesfATI = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glPNTrianglesiATI");
        this.glPNTrianglesiATI = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean ATI_separate_stencil_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glStencilOpSeparateATI");
        this.glStencilOpSeparateATI = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glStencilFuncSeparateATI");
        this.glStencilFuncSeparateATI = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean ATI_vertex_array_object_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glNewObjectBufferATI");
        this.glNewObjectBufferATI = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glIsObjectBufferATI");
        this.glIsObjectBufferATI = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glUpdateObjectBufferATI");
        this.glUpdateObjectBufferATI = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glGetObjectBufferfvATI");
        this.glGetObjectBufferfvATI = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glGetObjectBufferivATI");
        this.glGetObjectBufferivATI = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glFreeObjectBufferATI");
        this.glFreeObjectBufferATI = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glArrayObjectATI");
        this.glArrayObjectATI = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glGetArrayObjectfvATI");
        this.glGetArrayObjectfvATI = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glGetArrayObjectivATI");
        this.glGetArrayObjectivATI = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glVariantArrayObjectATI");
        this.glVariantArrayObjectATI = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glGetVariantArrayObjectfvATI");
        this.glGetVariantArrayObjectfvATI = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glGetVariantArrayObjectivATI");
        this.glGetVariantArrayObjectivATI = functionAddress12;
        return b11 & functionAddress12 != 0L;
    }
    
    private boolean ATI_vertex_attrib_array_object_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glVertexAttribArrayObjectATI");
        this.glVertexAttribArrayObjectATI = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glGetVertexAttribArrayObjectfvATI");
        this.glGetVertexAttribArrayObjectfvATI = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glGetVertexAttribArrayObjectivATI");
        this.glGetVertexAttribArrayObjectivATI = functionAddress3;
        return b2 & functionAddress3 != 0L;
    }
    
    private boolean ATI_vertex_streams_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glVertexStream2fATI");
        this.glVertexStream2fATI = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glVertexStream2dATI");
        this.glVertexStream2dATI = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glVertexStream2iATI");
        this.glVertexStream2iATI = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glVertexStream2sATI");
        this.glVertexStream2sATI = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glVertexStream3fATI");
        this.glVertexStream3fATI = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glVertexStream3dATI");
        this.glVertexStream3dATI = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glVertexStream3iATI");
        this.glVertexStream3iATI = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glVertexStream3sATI");
        this.glVertexStream3sATI = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glVertexStream4fATI");
        this.glVertexStream4fATI = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glVertexStream4dATI");
        this.glVertexStream4dATI = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glVertexStream4iATI");
        this.glVertexStream4iATI = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glVertexStream4sATI");
        this.glVertexStream4sATI = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glNormalStream3bATI");
        this.glNormalStream3bATI = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glNormalStream3fATI");
        this.glNormalStream3fATI = functionAddress14;
        final boolean b14 = b13 & functionAddress14 != 0L;
        final long functionAddress15 = GLContext.getFunctionAddress("glNormalStream3dATI");
        this.glNormalStream3dATI = functionAddress15;
        final boolean b15 = b14 & functionAddress15 != 0L;
        final long functionAddress16 = GLContext.getFunctionAddress("glNormalStream3iATI");
        this.glNormalStream3iATI = functionAddress16;
        final boolean b16 = b15 & functionAddress16 != 0L;
        final long functionAddress17 = GLContext.getFunctionAddress("glNormalStream3sATI");
        this.glNormalStream3sATI = functionAddress17;
        final boolean b17 = b16 & functionAddress17 != 0L;
        final long functionAddress18 = GLContext.getFunctionAddress("glClientActiveVertexStreamATI");
        this.glClientActiveVertexStreamATI = functionAddress18;
        final boolean b18 = b17 & functionAddress18 != 0L;
        final long functionAddress19 = GLContext.getFunctionAddress("glVertexBlendEnvfATI");
        this.glVertexBlendEnvfATI = functionAddress19;
        final boolean b19 = b18 & functionAddress19 != 0L;
        final long functionAddress20 = GLContext.getFunctionAddress("glVertexBlendEnviATI");
        this.glVertexBlendEnviATI = functionAddress20;
        return b19 & functionAddress20 != 0L;
    }
    
    private boolean EXT_bindable_uniform_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glUniformBufferEXT");
        this.glUniformBufferEXT = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glGetUniformBufferSizeEXT");
        this.glGetUniformBufferSizeEXT = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glGetUniformOffsetEXT");
        this.glGetUniformOffsetEXT = functionAddress3;
        return b2 & functionAddress3 != 0L;
    }
    
    private boolean EXT_blend_color_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glBlendColorEXT");
        this.glBlendColorEXT = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean EXT_blend_equation_separate_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glBlendEquationSeparateEXT");
        this.glBlendEquationSeparateEXT = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean EXT_blend_func_separate_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glBlendFuncSeparateEXT");
        this.glBlendFuncSeparateEXT = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean EXT_blend_minmax_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glBlendEquationEXT");
        this.glBlendEquationEXT = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean EXT_compiled_vertex_array_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glLockArraysEXT");
        this.glLockArraysEXT = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glUnlockArraysEXT");
        this.glUnlockArraysEXT = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean EXT_depth_bounds_test_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glDepthBoundsEXT");
        this.glDepthBoundsEXT = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean EXT_direct_state_access_initNativeFunctionAddresses(final boolean forwardCompatible, final Set<String> supported_extensions) {
        boolean b = false;
        Label_0025: {
            if (!forwardCompatible) {
                final long functionAddress = GLContext.getFunctionAddress("glClientAttribDefaultEXT");
                this.glClientAttribDefaultEXT = functionAddress;
                if (functionAddress == 0L) {
                    b = false;
                    break Label_0025;
                }
            }
            b = true;
        }
        boolean b2 = false;
        Label_0050: {
            if (!forwardCompatible) {
                final long functionAddress2 = GLContext.getFunctionAddress("glPushClientAttribDefaultEXT");
                this.glPushClientAttribDefaultEXT = functionAddress2;
                if (functionAddress2 == 0L) {
                    b2 = false;
                    break Label_0050;
                }
            }
            b2 = true;
        }
        final boolean b3 = b & b2;
        boolean b4 = false;
        Label_0076: {
            if (!forwardCompatible) {
                final long functionAddress3 = GLContext.getFunctionAddress("glMatrixLoadfEXT");
                this.glMatrixLoadfEXT = functionAddress3;
                if (functionAddress3 == 0L) {
                    b4 = false;
                    break Label_0076;
                }
            }
            b4 = true;
        }
        final boolean b5 = b3 & b4;
        boolean b6 = false;
        Label_0102: {
            if (!forwardCompatible) {
                final long functionAddress4 = GLContext.getFunctionAddress("glMatrixLoaddEXT");
                this.glMatrixLoaddEXT = functionAddress4;
                if (functionAddress4 == 0L) {
                    b6 = false;
                    break Label_0102;
                }
            }
            b6 = true;
        }
        final boolean b7 = b5 & b6;
        boolean b8 = false;
        Label_0128: {
            if (!forwardCompatible) {
                final long functionAddress5 = GLContext.getFunctionAddress("glMatrixMultfEXT");
                this.glMatrixMultfEXT = functionAddress5;
                if (functionAddress5 == 0L) {
                    b8 = false;
                    break Label_0128;
                }
            }
            b8 = true;
        }
        final boolean b9 = b7 & b8;
        boolean b10 = false;
        Label_0154: {
            if (!forwardCompatible) {
                final long functionAddress6 = GLContext.getFunctionAddress("glMatrixMultdEXT");
                this.glMatrixMultdEXT = functionAddress6;
                if (functionAddress6 == 0L) {
                    b10 = false;
                    break Label_0154;
                }
            }
            b10 = true;
        }
        final boolean b11 = b9 & b10;
        boolean b12 = false;
        Label_0180: {
            if (!forwardCompatible) {
                final long functionAddress7 = GLContext.getFunctionAddress("glMatrixLoadIdentityEXT");
                this.glMatrixLoadIdentityEXT = functionAddress7;
                if (functionAddress7 == 0L) {
                    b12 = false;
                    break Label_0180;
                }
            }
            b12 = true;
        }
        final boolean b13 = b11 & b12;
        boolean b14 = false;
        Label_0206: {
            if (!forwardCompatible) {
                final long functionAddress8 = GLContext.getFunctionAddress("glMatrixRotatefEXT");
                this.glMatrixRotatefEXT = functionAddress8;
                if (functionAddress8 == 0L) {
                    b14 = false;
                    break Label_0206;
                }
            }
            b14 = true;
        }
        final boolean b15 = b13 & b14;
        boolean b16 = false;
        Label_0232: {
            if (!forwardCompatible) {
                final long functionAddress9 = GLContext.getFunctionAddress("glMatrixRotatedEXT");
                this.glMatrixRotatedEXT = functionAddress9;
                if (functionAddress9 == 0L) {
                    b16 = false;
                    break Label_0232;
                }
            }
            b16 = true;
        }
        final boolean b17 = b15 & b16;
        boolean b18 = false;
        Label_0258: {
            if (!forwardCompatible) {
                final long functionAddress10 = GLContext.getFunctionAddress("glMatrixScalefEXT");
                this.glMatrixScalefEXT = functionAddress10;
                if (functionAddress10 == 0L) {
                    b18 = false;
                    break Label_0258;
                }
            }
            b18 = true;
        }
        final boolean b19 = b17 & b18;
        boolean b20 = false;
        Label_0284: {
            if (!forwardCompatible) {
                final long functionAddress11 = GLContext.getFunctionAddress("glMatrixScaledEXT");
                this.glMatrixScaledEXT = functionAddress11;
                if (functionAddress11 == 0L) {
                    b20 = false;
                    break Label_0284;
                }
            }
            b20 = true;
        }
        final boolean b21 = b19 & b20;
        boolean b22 = false;
        Label_0310: {
            if (!forwardCompatible) {
                final long functionAddress12 = GLContext.getFunctionAddress("glMatrixTranslatefEXT");
                this.glMatrixTranslatefEXT = functionAddress12;
                if (functionAddress12 == 0L) {
                    b22 = false;
                    break Label_0310;
                }
            }
            b22 = true;
        }
        final boolean b23 = b21 & b22;
        boolean b24 = false;
        Label_0336: {
            if (!forwardCompatible) {
                final long functionAddress13 = GLContext.getFunctionAddress("glMatrixTranslatedEXT");
                this.glMatrixTranslatedEXT = functionAddress13;
                if (functionAddress13 == 0L) {
                    b24 = false;
                    break Label_0336;
                }
            }
            b24 = true;
        }
        final boolean b25 = b23 & b24;
        boolean b26 = false;
        Label_0362: {
            if (!forwardCompatible) {
                final long functionAddress14 = GLContext.getFunctionAddress("glMatrixOrthoEXT");
                this.glMatrixOrthoEXT = functionAddress14;
                if (functionAddress14 == 0L) {
                    b26 = false;
                    break Label_0362;
                }
            }
            b26 = true;
        }
        final boolean b27 = b25 & b26;
        boolean b28 = false;
        Label_0388: {
            if (!forwardCompatible) {
                final long functionAddress15 = GLContext.getFunctionAddress("glMatrixFrustumEXT");
                this.glMatrixFrustumEXT = functionAddress15;
                if (functionAddress15 == 0L) {
                    b28 = false;
                    break Label_0388;
                }
            }
            b28 = true;
        }
        final boolean b29 = b27 & b28;
        boolean b30 = false;
        Label_0414: {
            if (!forwardCompatible) {
                final long functionAddress16 = GLContext.getFunctionAddress("glMatrixPushEXT");
                this.glMatrixPushEXT = functionAddress16;
                if (functionAddress16 == 0L) {
                    b30 = false;
                    break Label_0414;
                }
            }
            b30 = true;
        }
        final boolean b31 = b29 & b30;
        boolean b32 = false;
        Label_0440: {
            if (!forwardCompatible) {
                final long functionAddress17 = GLContext.getFunctionAddress("glMatrixPopEXT");
                this.glMatrixPopEXT = functionAddress17;
                if (functionAddress17 == 0L) {
                    b32 = false;
                    break Label_0440;
                }
            }
            b32 = true;
        }
        final boolean b33 = b31 & b32;
        final long functionAddress18 = GLContext.getFunctionAddress("glTextureParameteriEXT");
        this.glTextureParameteriEXT = functionAddress18;
        final boolean b34 = b33 & functionAddress18 != 0L;
        final long functionAddress19 = GLContext.getFunctionAddress("glTextureParameterivEXT");
        this.glTextureParameterivEXT = functionAddress19;
        final boolean b35 = b34 & functionAddress19 != 0L;
        final long functionAddress20 = GLContext.getFunctionAddress("glTextureParameterfEXT");
        this.glTextureParameterfEXT = functionAddress20;
        final boolean b36 = b35 & functionAddress20 != 0L;
        final long functionAddress21 = GLContext.getFunctionAddress("glTextureParameterfvEXT");
        this.glTextureParameterfvEXT = functionAddress21;
        final boolean b37 = b36 & functionAddress21 != 0L;
        final long functionAddress22 = GLContext.getFunctionAddress("glTextureImage1DEXT");
        this.glTextureImage1DEXT = functionAddress22;
        final boolean b38 = b37 & functionAddress22 != 0L;
        final long functionAddress23 = GLContext.getFunctionAddress("glTextureImage2DEXT");
        this.glTextureImage2DEXT = functionAddress23;
        final boolean b39 = b38 & functionAddress23 != 0L;
        final long functionAddress24 = GLContext.getFunctionAddress("glTextureSubImage1DEXT");
        this.glTextureSubImage1DEXT = functionAddress24;
        final boolean b40 = b39 & functionAddress24 != 0L;
        final long functionAddress25 = GLContext.getFunctionAddress("glTextureSubImage2DEXT");
        this.glTextureSubImage2DEXT = functionAddress25;
        final boolean b41 = b40 & functionAddress25 != 0L;
        final long functionAddress26 = GLContext.getFunctionAddress("glCopyTextureImage1DEXT");
        this.glCopyTextureImage1DEXT = functionAddress26;
        final boolean b42 = b41 & functionAddress26 != 0L;
        final long functionAddress27 = GLContext.getFunctionAddress("glCopyTextureImage2DEXT");
        this.glCopyTextureImage2DEXT = functionAddress27;
        final boolean b43 = b42 & functionAddress27 != 0L;
        final long functionAddress28 = GLContext.getFunctionAddress("glCopyTextureSubImage1DEXT");
        this.glCopyTextureSubImage1DEXT = functionAddress28;
        final boolean b44 = b43 & functionAddress28 != 0L;
        final long functionAddress29 = GLContext.getFunctionAddress("glCopyTextureSubImage2DEXT");
        this.glCopyTextureSubImage2DEXT = functionAddress29;
        final boolean b45 = b44 & functionAddress29 != 0L;
        final long functionAddress30 = GLContext.getFunctionAddress("glGetTextureImageEXT");
        this.glGetTextureImageEXT = functionAddress30;
        final boolean b46 = b45 & functionAddress30 != 0L;
        final long functionAddress31 = GLContext.getFunctionAddress("glGetTextureParameterfvEXT");
        this.glGetTextureParameterfvEXT = functionAddress31;
        final boolean b47 = b46 & functionAddress31 != 0L;
        final long functionAddress32 = GLContext.getFunctionAddress("glGetTextureParameterivEXT");
        this.glGetTextureParameterivEXT = functionAddress32;
        final boolean b48 = b47 & functionAddress32 != 0L;
        final long functionAddress33 = GLContext.getFunctionAddress("glGetTextureLevelParameterfvEXT");
        this.glGetTextureLevelParameterfvEXT = functionAddress33;
        final boolean b49 = b48 & functionAddress33 != 0L;
        final long functionAddress34 = GLContext.getFunctionAddress("glGetTextureLevelParameterivEXT");
        this.glGetTextureLevelParameterivEXT = functionAddress34;
        final boolean b50 = b49 & functionAddress34 != 0L;
        boolean b51 = false;
        Label_0848: {
            if (supported_extensions.contains("OpenGL12")) {
                final long functionAddress35 = GLContext.getFunctionAddress("glTextureImage3DEXT");
                this.glTextureImage3DEXT = functionAddress35;
                if (functionAddress35 == 0L) {
                    b51 = false;
                    break Label_0848;
                }
            }
            b51 = true;
        }
        final boolean b52 = b50 & b51;
        boolean b53 = false;
        Label_0882: {
            if (supported_extensions.contains("OpenGL12")) {
                final long functionAddress36 = GLContext.getFunctionAddress("glTextureSubImage3DEXT");
                this.glTextureSubImage3DEXT = functionAddress36;
                if (functionAddress36 == 0L) {
                    b53 = false;
                    break Label_0882;
                }
            }
            b53 = true;
        }
        final boolean b54 = b52 & b53;
        boolean b55 = false;
        Label_0916: {
            if (supported_extensions.contains("OpenGL12")) {
                final long functionAddress37 = GLContext.getFunctionAddress("glCopyTextureSubImage3DEXT");
                this.glCopyTextureSubImage3DEXT = functionAddress37;
                if (functionAddress37 == 0L) {
                    b55 = false;
                    break Label_0916;
                }
            }
            b55 = true;
        }
        final boolean b56 = b54 & b55;
        boolean b57 = false;
        Label_0950: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress38 = GLContext.getFunctionAddress("glBindMultiTextureEXT");
                this.glBindMultiTextureEXT = functionAddress38;
                if (functionAddress38 == 0L) {
                    b57 = false;
                    break Label_0950;
                }
            }
            b57 = true;
        }
        final boolean b58 = b56 & b57;
        boolean b59 = false;
        Label_0988: {
            if (!forwardCompatible && supported_extensions.contains("OpenGL13")) {
                final long functionAddress39 = GLContext.getFunctionAddress("glMultiTexCoordPointerEXT");
                this.glMultiTexCoordPointerEXT = functionAddress39;
                if (functionAddress39 == 0L) {
                    b59 = false;
                    break Label_0988;
                }
            }
            b59 = true;
        }
        final boolean b60 = b58 & b59;
        boolean b61 = false;
        Label_1026: {
            if (!forwardCompatible && supported_extensions.contains("OpenGL13")) {
                final long functionAddress40 = GLContext.getFunctionAddress("glMultiTexEnvfEXT");
                this.glMultiTexEnvfEXT = functionAddress40;
                if (functionAddress40 == 0L) {
                    b61 = false;
                    break Label_1026;
                }
            }
            b61 = true;
        }
        final boolean b62 = b60 & b61;
        boolean b63 = false;
        Label_1064: {
            if (!forwardCompatible && supported_extensions.contains("OpenGL13")) {
                final long functionAddress41 = GLContext.getFunctionAddress("glMultiTexEnvfvEXT");
                this.glMultiTexEnvfvEXT = functionAddress41;
                if (functionAddress41 == 0L) {
                    b63 = false;
                    break Label_1064;
                }
            }
            b63 = true;
        }
        final boolean b64 = b62 & b63;
        boolean b65 = false;
        Label_1102: {
            if (!forwardCompatible && supported_extensions.contains("OpenGL13")) {
                final long functionAddress42 = GLContext.getFunctionAddress("glMultiTexEnviEXT");
                this.glMultiTexEnviEXT = functionAddress42;
                if (functionAddress42 == 0L) {
                    b65 = false;
                    break Label_1102;
                }
            }
            b65 = true;
        }
        final boolean b66 = b64 & b65;
        boolean b67 = false;
        Label_1140: {
            if (!forwardCompatible && supported_extensions.contains("OpenGL13")) {
                final long functionAddress43 = GLContext.getFunctionAddress("glMultiTexEnvivEXT");
                this.glMultiTexEnvivEXT = functionAddress43;
                if (functionAddress43 == 0L) {
                    b67 = false;
                    break Label_1140;
                }
            }
            b67 = true;
        }
        final boolean b68 = b66 & b67;
        boolean b69 = false;
        Label_1178: {
            if (!forwardCompatible && supported_extensions.contains("OpenGL13")) {
                final long functionAddress44 = GLContext.getFunctionAddress("glMultiTexGendEXT");
                this.glMultiTexGendEXT = functionAddress44;
                if (functionAddress44 == 0L) {
                    b69 = false;
                    break Label_1178;
                }
            }
            b69 = true;
        }
        final boolean b70 = b68 & b69;
        boolean b71 = false;
        Label_1216: {
            if (!forwardCompatible && supported_extensions.contains("OpenGL13")) {
                final long functionAddress45 = GLContext.getFunctionAddress("glMultiTexGendvEXT");
                this.glMultiTexGendvEXT = functionAddress45;
                if (functionAddress45 == 0L) {
                    b71 = false;
                    break Label_1216;
                }
            }
            b71 = true;
        }
        final boolean b72 = b70 & b71;
        boolean b73 = false;
        Label_1254: {
            if (!forwardCompatible && supported_extensions.contains("OpenGL13")) {
                final long functionAddress46 = GLContext.getFunctionAddress("glMultiTexGenfEXT");
                this.glMultiTexGenfEXT = functionAddress46;
                if (functionAddress46 == 0L) {
                    b73 = false;
                    break Label_1254;
                }
            }
            b73 = true;
        }
        final boolean b74 = b72 & b73;
        boolean b75 = false;
        Label_1292: {
            if (!forwardCompatible && supported_extensions.contains("OpenGL13")) {
                final long functionAddress47 = GLContext.getFunctionAddress("glMultiTexGenfvEXT");
                this.glMultiTexGenfvEXT = functionAddress47;
                if (functionAddress47 == 0L) {
                    b75 = false;
                    break Label_1292;
                }
            }
            b75 = true;
        }
        final boolean b76 = b74 & b75;
        boolean b77 = false;
        Label_1330: {
            if (!forwardCompatible && supported_extensions.contains("OpenGL13")) {
                final long functionAddress48 = GLContext.getFunctionAddress("glMultiTexGeniEXT");
                this.glMultiTexGeniEXT = functionAddress48;
                if (functionAddress48 == 0L) {
                    b77 = false;
                    break Label_1330;
                }
            }
            b77 = true;
        }
        final boolean b78 = b76 & b77;
        boolean b79 = false;
        Label_1368: {
            if (!forwardCompatible && supported_extensions.contains("OpenGL13")) {
                final long functionAddress49 = GLContext.getFunctionAddress("glMultiTexGenivEXT");
                this.glMultiTexGenivEXT = functionAddress49;
                if (functionAddress49 == 0L) {
                    b79 = false;
                    break Label_1368;
                }
            }
            b79 = true;
        }
        final boolean b80 = b78 & b79;
        boolean b81 = false;
        Label_1406: {
            if (!forwardCompatible && supported_extensions.contains("OpenGL13")) {
                final long functionAddress50 = GLContext.getFunctionAddress("glGetMultiTexEnvfvEXT");
                this.glGetMultiTexEnvfvEXT = functionAddress50;
                if (functionAddress50 == 0L) {
                    b81 = false;
                    break Label_1406;
                }
            }
            b81 = true;
        }
        final boolean b82 = b80 & b81;
        boolean b83 = false;
        Label_1444: {
            if (!forwardCompatible && supported_extensions.contains("OpenGL13")) {
                final long functionAddress51 = GLContext.getFunctionAddress("glGetMultiTexEnvivEXT");
                this.glGetMultiTexEnvivEXT = functionAddress51;
                if (functionAddress51 == 0L) {
                    b83 = false;
                    break Label_1444;
                }
            }
            b83 = true;
        }
        final boolean b84 = b82 & b83;
        boolean b85 = false;
        Label_1482: {
            if (!forwardCompatible && supported_extensions.contains("OpenGL13")) {
                final long functionAddress52 = GLContext.getFunctionAddress("glGetMultiTexGendvEXT");
                this.glGetMultiTexGendvEXT = functionAddress52;
                if (functionAddress52 == 0L) {
                    b85 = false;
                    break Label_1482;
                }
            }
            b85 = true;
        }
        final boolean b86 = b84 & b85;
        boolean b87 = false;
        Label_1520: {
            if (!forwardCompatible && supported_extensions.contains("OpenGL13")) {
                final long functionAddress53 = GLContext.getFunctionAddress("glGetMultiTexGenfvEXT");
                this.glGetMultiTexGenfvEXT = functionAddress53;
                if (functionAddress53 == 0L) {
                    b87 = false;
                    break Label_1520;
                }
            }
            b87 = true;
        }
        final boolean b88 = b86 & b87;
        boolean b89 = false;
        Label_1558: {
            if (!forwardCompatible && supported_extensions.contains("OpenGL13")) {
                final long functionAddress54 = GLContext.getFunctionAddress("glGetMultiTexGenivEXT");
                this.glGetMultiTexGenivEXT = functionAddress54;
                if (functionAddress54 == 0L) {
                    b89 = false;
                    break Label_1558;
                }
            }
            b89 = true;
        }
        final boolean b90 = b88 & b89;
        boolean b91 = false;
        Label_1592: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress55 = GLContext.getFunctionAddress("glMultiTexParameteriEXT");
                this.glMultiTexParameteriEXT = functionAddress55;
                if (functionAddress55 == 0L) {
                    b91 = false;
                    break Label_1592;
                }
            }
            b91 = true;
        }
        final boolean b92 = b90 & b91;
        boolean b93 = false;
        Label_1626: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress56 = GLContext.getFunctionAddress("glMultiTexParameterivEXT");
                this.glMultiTexParameterivEXT = functionAddress56;
                if (functionAddress56 == 0L) {
                    b93 = false;
                    break Label_1626;
                }
            }
            b93 = true;
        }
        final boolean b94 = b92 & b93;
        boolean b95 = false;
        Label_1660: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress57 = GLContext.getFunctionAddress("glMultiTexParameterfEXT");
                this.glMultiTexParameterfEXT = functionAddress57;
                if (functionAddress57 == 0L) {
                    b95 = false;
                    break Label_1660;
                }
            }
            b95 = true;
        }
        final boolean b96 = b94 & b95;
        boolean b97 = false;
        Label_1694: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress58 = GLContext.getFunctionAddress("glMultiTexParameterfvEXT");
                this.glMultiTexParameterfvEXT = functionAddress58;
                if (functionAddress58 == 0L) {
                    b97 = false;
                    break Label_1694;
                }
            }
            b97 = true;
        }
        final boolean b98 = b96 & b97;
        boolean b99 = false;
        Label_1728: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress59 = GLContext.getFunctionAddress("glMultiTexImage1DEXT");
                this.glMultiTexImage1DEXT = functionAddress59;
                if (functionAddress59 == 0L) {
                    b99 = false;
                    break Label_1728;
                }
            }
            b99 = true;
        }
        final boolean b100 = b98 & b99;
        boolean b101 = false;
        Label_1762: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress60 = GLContext.getFunctionAddress("glMultiTexImage2DEXT");
                this.glMultiTexImage2DEXT = functionAddress60;
                if (functionAddress60 == 0L) {
                    b101 = false;
                    break Label_1762;
                }
            }
            b101 = true;
        }
        final boolean b102 = b100 & b101;
        boolean b103 = false;
        Label_1796: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress61 = GLContext.getFunctionAddress("glMultiTexSubImage1DEXT");
                this.glMultiTexSubImage1DEXT = functionAddress61;
                if (functionAddress61 == 0L) {
                    b103 = false;
                    break Label_1796;
                }
            }
            b103 = true;
        }
        final boolean b104 = b102 & b103;
        boolean b105 = false;
        Label_1830: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress62 = GLContext.getFunctionAddress("glMultiTexSubImage2DEXT");
                this.glMultiTexSubImage2DEXT = functionAddress62;
                if (functionAddress62 == 0L) {
                    b105 = false;
                    break Label_1830;
                }
            }
            b105 = true;
        }
        final boolean b106 = b104 & b105;
        boolean b107 = false;
        Label_1864: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress63 = GLContext.getFunctionAddress("glCopyMultiTexImage1DEXT");
                this.glCopyMultiTexImage1DEXT = functionAddress63;
                if (functionAddress63 == 0L) {
                    b107 = false;
                    break Label_1864;
                }
            }
            b107 = true;
        }
        final boolean b108 = b106 & b107;
        boolean b109 = false;
        Label_1898: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress64 = GLContext.getFunctionAddress("glCopyMultiTexImage2DEXT");
                this.glCopyMultiTexImage2DEXT = functionAddress64;
                if (functionAddress64 == 0L) {
                    b109 = false;
                    break Label_1898;
                }
            }
            b109 = true;
        }
        final boolean b110 = b108 & b109;
        boolean b111 = false;
        Label_1932: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress65 = GLContext.getFunctionAddress("glCopyMultiTexSubImage1DEXT");
                this.glCopyMultiTexSubImage1DEXT = functionAddress65;
                if (functionAddress65 == 0L) {
                    b111 = false;
                    break Label_1932;
                }
            }
            b111 = true;
        }
        final boolean b112 = b110 & b111;
        boolean b113 = false;
        Label_1966: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress66 = GLContext.getFunctionAddress("glCopyMultiTexSubImage2DEXT");
                this.glCopyMultiTexSubImage2DEXT = functionAddress66;
                if (functionAddress66 == 0L) {
                    b113 = false;
                    break Label_1966;
                }
            }
            b113 = true;
        }
        final boolean b114 = b112 & b113;
        boolean b115 = false;
        Label_2000: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress67 = GLContext.getFunctionAddress("glGetMultiTexImageEXT");
                this.glGetMultiTexImageEXT = functionAddress67;
                if (functionAddress67 == 0L) {
                    b115 = false;
                    break Label_2000;
                }
            }
            b115 = true;
        }
        final boolean b116 = b114 & b115;
        boolean b117 = false;
        Label_2034: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress68 = GLContext.getFunctionAddress("glGetMultiTexParameterfvEXT");
                this.glGetMultiTexParameterfvEXT = functionAddress68;
                if (functionAddress68 == 0L) {
                    b117 = false;
                    break Label_2034;
                }
            }
            b117 = true;
        }
        final boolean b118 = b116 & b117;
        boolean b119 = false;
        Label_2068: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress69 = GLContext.getFunctionAddress("glGetMultiTexParameterivEXT");
                this.glGetMultiTexParameterivEXT = functionAddress69;
                if (functionAddress69 == 0L) {
                    b119 = false;
                    break Label_2068;
                }
            }
            b119 = true;
        }
        final boolean b120 = b118 & b119;
        boolean b121 = false;
        Label_2102: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress70 = GLContext.getFunctionAddress("glGetMultiTexLevelParameterfvEXT");
                this.glGetMultiTexLevelParameterfvEXT = functionAddress70;
                if (functionAddress70 == 0L) {
                    b121 = false;
                    break Label_2102;
                }
            }
            b121 = true;
        }
        final boolean b122 = b120 & b121;
        boolean b123 = false;
        Label_2136: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress71 = GLContext.getFunctionAddress("glGetMultiTexLevelParameterivEXT");
                this.glGetMultiTexLevelParameterivEXT = functionAddress71;
                if (functionAddress71 == 0L) {
                    b123 = false;
                    break Label_2136;
                }
            }
            b123 = true;
        }
        final boolean b124 = b122 & b123;
        boolean b125 = false;
        Label_2170: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress72 = GLContext.getFunctionAddress("glMultiTexImage3DEXT");
                this.glMultiTexImage3DEXT = functionAddress72;
                if (functionAddress72 == 0L) {
                    b125 = false;
                    break Label_2170;
                }
            }
            b125 = true;
        }
        final boolean b126 = b124 & b125;
        boolean b127 = false;
        Label_2204: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress73 = GLContext.getFunctionAddress("glMultiTexSubImage3DEXT");
                this.glMultiTexSubImage3DEXT = functionAddress73;
                if (functionAddress73 == 0L) {
                    b127 = false;
                    break Label_2204;
                }
            }
            b127 = true;
        }
        final boolean b128 = b126 & b127;
        boolean b129 = false;
        Label_2238: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress74 = GLContext.getFunctionAddress("glCopyMultiTexSubImage3DEXT");
                this.glCopyMultiTexSubImage3DEXT = functionAddress74;
                if (functionAddress74 == 0L) {
                    b129 = false;
                    break Label_2238;
                }
            }
            b129 = true;
        }
        final boolean b130 = b128 & b129;
        boolean b131 = false;
        Label_2276: {
            if (!forwardCompatible && supported_extensions.contains("OpenGL13")) {
                final long functionAddress75 = GLContext.getFunctionAddress("glEnableClientStateIndexedEXT");
                this.glEnableClientStateIndexedEXT = functionAddress75;
                if (functionAddress75 == 0L) {
                    b131 = false;
                    break Label_2276;
                }
            }
            b131 = true;
        }
        final boolean b132 = b130 & b131;
        boolean b133 = false;
        Label_2314: {
            if (!forwardCompatible && supported_extensions.contains("OpenGL13")) {
                final long functionAddress76 = GLContext.getFunctionAddress("glDisableClientStateIndexedEXT");
                this.glDisableClientStateIndexedEXT = functionAddress76;
                if (functionAddress76 == 0L) {
                    b133 = false;
                    break Label_2314;
                }
            }
            b133 = true;
        }
        final boolean b134 = b132 & b133;
        if (supported_extensions.contains("OpenGL30")) {
            final long functionAddress77 = GLContext.getFunctionAddress("glEnableClientStateiEXT");
            this.glEnableClientStateiEXT = functionAddress77;
            if (functionAddress77 == 0L) {}
        }
        final boolean b135 = b134 & true;
        if (supported_extensions.contains("OpenGL30")) {
            final long functionAddress78 = GLContext.getFunctionAddress("glDisableClientStateiEXT");
            this.glDisableClientStateiEXT = functionAddress78;
            if (functionAddress78 == 0L) {}
        }
        final boolean b136 = b135 & true;
        boolean b137 = false;
        Label_2408: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress79 = GLContext.getFunctionAddress("glGetFloatIndexedvEXT");
                this.glGetFloatIndexedvEXT = functionAddress79;
                if (functionAddress79 == 0L) {
                    b137 = false;
                    break Label_2408;
                }
            }
            b137 = true;
        }
        final boolean b138 = b136 & b137;
        boolean b139 = false;
        Label_2442: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress80 = GLContext.getFunctionAddress("glGetDoubleIndexedvEXT");
                this.glGetDoubleIndexedvEXT = functionAddress80;
                if (functionAddress80 == 0L) {
                    b139 = false;
                    break Label_2442;
                }
            }
            b139 = true;
        }
        final boolean b140 = b138 & b139;
        boolean b141 = false;
        Label_2476: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress81 = GLContext.getFunctionAddress("glGetPointerIndexedvEXT");
                this.glGetPointerIndexedvEXT = functionAddress81;
                if (functionAddress81 == 0L) {
                    b141 = false;
                    break Label_2476;
                }
            }
            b141 = true;
        }
        final boolean b142 = b140 & b141;
        if (supported_extensions.contains("OpenGL30")) {
            final long functionAddress82 = GLContext.getFunctionAddress("glGetFloati_vEXT");
            this.glGetFloati_vEXT = functionAddress82;
            if (functionAddress82 == 0L) {}
        }
        final boolean b143 = b142 & true;
        if (supported_extensions.contains("OpenGL30")) {
            final long functionAddress83 = GLContext.getFunctionAddress("glGetDoublei_vEXT");
            this.glGetDoublei_vEXT = functionAddress83;
            if (functionAddress83 == 0L) {}
        }
        final boolean b144 = b143 & true;
        if (supported_extensions.contains("OpenGL30")) {
            final long functionAddress84 = GLContext.getFunctionAddress("glGetPointeri_vEXT");
            this.glGetPointeri_vEXT = functionAddress84;
            if (functionAddress84 == 0L) {}
        }
        final boolean b145 = b144 & true;
        boolean b146 = false;
        Label_2600: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress85 = GLContext.getFunctionAddress("glEnableIndexedEXT");
                this.glEnableIndexedEXT = functionAddress85;
                if (functionAddress85 == 0L) {
                    b146 = false;
                    break Label_2600;
                }
            }
            b146 = true;
        }
        final boolean b147 = b145 & b146;
        boolean b148 = false;
        Label_2634: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress86 = GLContext.getFunctionAddress("glDisableIndexedEXT");
                this.glDisableIndexedEXT = functionAddress86;
                if (functionAddress86 == 0L) {
                    b148 = false;
                    break Label_2634;
                }
            }
            b148 = true;
        }
        final boolean b149 = b147 & b148;
        boolean b150 = false;
        Label_2668: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress87 = GLContext.getFunctionAddress("glIsEnabledIndexedEXT");
                this.glIsEnabledIndexedEXT = functionAddress87;
                if (functionAddress87 == 0L) {
                    b150 = false;
                    break Label_2668;
                }
            }
            b150 = true;
        }
        final boolean b151 = b149 & b150;
        boolean b152 = false;
        Label_2702: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress88 = GLContext.getFunctionAddress("glGetIntegerIndexedvEXT");
                this.glGetIntegerIndexedvEXT = functionAddress88;
                if (functionAddress88 == 0L) {
                    b152 = false;
                    break Label_2702;
                }
            }
            b152 = true;
        }
        final boolean b153 = b151 & b152;
        boolean b154 = false;
        Label_2736: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress89 = GLContext.getFunctionAddress("glGetBooleanIndexedvEXT");
                this.glGetBooleanIndexedvEXT = functionAddress89;
                if (functionAddress89 == 0L) {
                    b154 = false;
                    break Label_2736;
                }
            }
            b154 = true;
        }
        final boolean b155 = b153 & b154;
        boolean b156 = false;
        Label_2770: {
            if (supported_extensions.contains("GL_ARB_vertex_program")) {
                final long functionAddress90 = GLContext.getFunctionAddress("glNamedProgramStringEXT");
                this.glNamedProgramStringEXT = functionAddress90;
                if (functionAddress90 == 0L) {
                    b156 = false;
                    break Label_2770;
                }
            }
            b156 = true;
        }
        final boolean b157 = b155 & b156;
        boolean b158 = false;
        Label_2804: {
            if (supported_extensions.contains("GL_ARB_vertex_program")) {
                final long functionAddress91 = GLContext.getFunctionAddress("glNamedProgramLocalParameter4dEXT");
                this.glNamedProgramLocalParameter4dEXT = functionAddress91;
                if (functionAddress91 == 0L) {
                    b158 = false;
                    break Label_2804;
                }
            }
            b158 = true;
        }
        final boolean b159 = b157 & b158;
        boolean b160 = false;
        Label_2838: {
            if (supported_extensions.contains("GL_ARB_vertex_program")) {
                final long functionAddress92 = GLContext.getFunctionAddress("glNamedProgramLocalParameter4dvEXT");
                this.glNamedProgramLocalParameter4dvEXT = functionAddress92;
                if (functionAddress92 == 0L) {
                    b160 = false;
                    break Label_2838;
                }
            }
            b160 = true;
        }
        final boolean b161 = b159 & b160;
        boolean b162 = false;
        Label_2872: {
            if (supported_extensions.contains("GL_ARB_vertex_program")) {
                final long functionAddress93 = GLContext.getFunctionAddress("glNamedProgramLocalParameter4fEXT");
                this.glNamedProgramLocalParameter4fEXT = functionAddress93;
                if (functionAddress93 == 0L) {
                    b162 = false;
                    break Label_2872;
                }
            }
            b162 = true;
        }
        final boolean b163 = b161 & b162;
        boolean b164 = false;
        Label_2906: {
            if (supported_extensions.contains("GL_ARB_vertex_program")) {
                final long functionAddress94 = GLContext.getFunctionAddress("glNamedProgramLocalParameter4fvEXT");
                this.glNamedProgramLocalParameter4fvEXT = functionAddress94;
                if (functionAddress94 == 0L) {
                    b164 = false;
                    break Label_2906;
                }
            }
            b164 = true;
        }
        final boolean b165 = b163 & b164;
        boolean b166 = false;
        Label_2940: {
            if (supported_extensions.contains("GL_ARB_vertex_program")) {
                final long functionAddress95 = GLContext.getFunctionAddress("glGetNamedProgramLocalParameterdvEXT");
                this.glGetNamedProgramLocalParameterdvEXT = functionAddress95;
                if (functionAddress95 == 0L) {
                    b166 = false;
                    break Label_2940;
                }
            }
            b166 = true;
        }
        final boolean b167 = b165 & b166;
        boolean b168 = false;
        Label_2974: {
            if (supported_extensions.contains("GL_ARB_vertex_program")) {
                final long functionAddress96 = GLContext.getFunctionAddress("glGetNamedProgramLocalParameterfvEXT");
                this.glGetNamedProgramLocalParameterfvEXT = functionAddress96;
                if (functionAddress96 == 0L) {
                    b168 = false;
                    break Label_2974;
                }
            }
            b168 = true;
        }
        final boolean b169 = b167 & b168;
        boolean b170 = false;
        Label_3008: {
            if (supported_extensions.contains("GL_ARB_vertex_program")) {
                final long functionAddress97 = GLContext.getFunctionAddress("glGetNamedProgramivEXT");
                this.glGetNamedProgramivEXT = functionAddress97;
                if (functionAddress97 == 0L) {
                    b170 = false;
                    break Label_3008;
                }
            }
            b170 = true;
        }
        final boolean b171 = b169 & b170;
        boolean b172 = false;
        Label_3042: {
            if (supported_extensions.contains("GL_ARB_vertex_program")) {
                final long functionAddress98 = GLContext.getFunctionAddress("glGetNamedProgramStringEXT");
                this.glGetNamedProgramStringEXT = functionAddress98;
                if (functionAddress98 == 0L) {
                    b172 = false;
                    break Label_3042;
                }
            }
            b172 = true;
        }
        final boolean b173 = b171 & b172;
        boolean b174 = false;
        Label_3076: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress99 = GLContext.getFunctionAddress("glCompressedTextureImage3DEXT");
                this.glCompressedTextureImage3DEXT = functionAddress99;
                if (functionAddress99 == 0L) {
                    b174 = false;
                    break Label_3076;
                }
            }
            b174 = true;
        }
        final boolean b175 = b173 & b174;
        boolean b176 = false;
        Label_3110: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress100 = GLContext.getFunctionAddress("glCompressedTextureImage2DEXT");
                this.glCompressedTextureImage2DEXT = functionAddress100;
                if (functionAddress100 == 0L) {
                    b176 = false;
                    break Label_3110;
                }
            }
            b176 = true;
        }
        final boolean b177 = b175 & b176;
        boolean b178 = false;
        Label_3144: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress101 = GLContext.getFunctionAddress("glCompressedTextureImage1DEXT");
                this.glCompressedTextureImage1DEXT = functionAddress101;
                if (functionAddress101 == 0L) {
                    b178 = false;
                    break Label_3144;
                }
            }
            b178 = true;
        }
        final boolean b179 = b177 & b178;
        boolean b180 = false;
        Label_3178: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress102 = GLContext.getFunctionAddress("glCompressedTextureSubImage3DEXT");
                this.glCompressedTextureSubImage3DEXT = functionAddress102;
                if (functionAddress102 == 0L) {
                    b180 = false;
                    break Label_3178;
                }
            }
            b180 = true;
        }
        final boolean b181 = b179 & b180;
        boolean b182 = false;
        Label_3212: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress103 = GLContext.getFunctionAddress("glCompressedTextureSubImage2DEXT");
                this.glCompressedTextureSubImage2DEXT = functionAddress103;
                if (functionAddress103 == 0L) {
                    b182 = false;
                    break Label_3212;
                }
            }
            b182 = true;
        }
        final boolean b183 = b181 & b182;
        boolean b184 = false;
        Label_3246: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress104 = GLContext.getFunctionAddress("glCompressedTextureSubImage1DEXT");
                this.glCompressedTextureSubImage1DEXT = functionAddress104;
                if (functionAddress104 == 0L) {
                    b184 = false;
                    break Label_3246;
                }
            }
            b184 = true;
        }
        final boolean b185 = b183 & b184;
        boolean b186 = false;
        Label_3280: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress105 = GLContext.getFunctionAddress("glGetCompressedTextureImageEXT");
                this.glGetCompressedTextureImageEXT = functionAddress105;
                if (functionAddress105 == 0L) {
                    b186 = false;
                    break Label_3280;
                }
            }
            b186 = true;
        }
        final boolean b187 = b185 & b186;
        boolean b188 = false;
        Label_3314: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress106 = GLContext.getFunctionAddress("glCompressedMultiTexImage3DEXT");
                this.glCompressedMultiTexImage3DEXT = functionAddress106;
                if (functionAddress106 == 0L) {
                    b188 = false;
                    break Label_3314;
                }
            }
            b188 = true;
        }
        final boolean b189 = b187 & b188;
        boolean b190 = false;
        Label_3348: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress107 = GLContext.getFunctionAddress("glCompressedMultiTexImage2DEXT");
                this.glCompressedMultiTexImage2DEXT = functionAddress107;
                if (functionAddress107 == 0L) {
                    b190 = false;
                    break Label_3348;
                }
            }
            b190 = true;
        }
        final boolean b191 = b189 & b190;
        boolean b192 = false;
        Label_3382: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress108 = GLContext.getFunctionAddress("glCompressedMultiTexImage1DEXT");
                this.glCompressedMultiTexImage1DEXT = functionAddress108;
                if (functionAddress108 == 0L) {
                    b192 = false;
                    break Label_3382;
                }
            }
            b192 = true;
        }
        final boolean b193 = b191 & b192;
        boolean b194 = false;
        Label_3416: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress109 = GLContext.getFunctionAddress("glCompressedMultiTexSubImage3DEXT");
                this.glCompressedMultiTexSubImage3DEXT = functionAddress109;
                if (functionAddress109 == 0L) {
                    b194 = false;
                    break Label_3416;
                }
            }
            b194 = true;
        }
        final boolean b195 = b193 & b194;
        boolean b196 = false;
        Label_3450: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress110 = GLContext.getFunctionAddress("glCompressedMultiTexSubImage2DEXT");
                this.glCompressedMultiTexSubImage2DEXT = functionAddress110;
                if (functionAddress110 == 0L) {
                    b196 = false;
                    break Label_3450;
                }
            }
            b196 = true;
        }
        final boolean b197 = b195 & b196;
        boolean b198 = false;
        Label_3484: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress111 = GLContext.getFunctionAddress("glCompressedMultiTexSubImage1DEXT");
                this.glCompressedMultiTexSubImage1DEXT = functionAddress111;
                if (functionAddress111 == 0L) {
                    b198 = false;
                    break Label_3484;
                }
            }
            b198 = true;
        }
        final boolean b199 = b197 & b198;
        boolean b200 = false;
        Label_3518: {
            if (supported_extensions.contains("OpenGL13")) {
                final long functionAddress112 = GLContext.getFunctionAddress("glGetCompressedMultiTexImageEXT");
                this.glGetCompressedMultiTexImageEXT = functionAddress112;
                if (functionAddress112 == 0L) {
                    b200 = false;
                    break Label_3518;
                }
            }
            b200 = true;
        }
        final boolean b201 = b199 & b200;
        boolean b202 = false;
        Label_3556: {
            if (!forwardCompatible && supported_extensions.contains("OpenGL13")) {
                final long functionAddress113 = GLContext.getFunctionAddress("glMatrixLoadTransposefEXT");
                this.glMatrixLoadTransposefEXT = functionAddress113;
                if (functionAddress113 == 0L) {
                    b202 = false;
                    break Label_3556;
                }
            }
            b202 = true;
        }
        final boolean b203 = b201 & b202;
        boolean b204 = false;
        Label_3594: {
            if (!forwardCompatible && supported_extensions.contains("OpenGL13")) {
                final long functionAddress114 = GLContext.getFunctionAddress("glMatrixLoadTransposedEXT");
                this.glMatrixLoadTransposedEXT = functionAddress114;
                if (functionAddress114 == 0L) {
                    b204 = false;
                    break Label_3594;
                }
            }
            b204 = true;
        }
        final boolean b205 = b203 & b204;
        boolean b206 = false;
        Label_3632: {
            if (!forwardCompatible && supported_extensions.contains("OpenGL13")) {
                final long functionAddress115 = GLContext.getFunctionAddress("glMatrixMultTransposefEXT");
                this.glMatrixMultTransposefEXT = functionAddress115;
                if (functionAddress115 == 0L) {
                    b206 = false;
                    break Label_3632;
                }
            }
            b206 = true;
        }
        final boolean b207 = b205 & b206;
        boolean b208 = false;
        Label_3670: {
            if (!forwardCompatible && supported_extensions.contains("OpenGL13")) {
                final long functionAddress116 = GLContext.getFunctionAddress("glMatrixMultTransposedEXT");
                this.glMatrixMultTransposedEXT = functionAddress116;
                if (functionAddress116 == 0L) {
                    b208 = false;
                    break Label_3670;
                }
            }
            b208 = true;
        }
        final boolean b209 = b207 & b208;
        boolean b210 = false;
        Label_3704: {
            if (supported_extensions.contains("OpenGL15")) {
                final long functionAddress117 = GLContext.getFunctionAddress("glNamedBufferDataEXT");
                this.glNamedBufferDataEXT = functionAddress117;
                if (functionAddress117 == 0L) {
                    b210 = false;
                    break Label_3704;
                }
            }
            b210 = true;
        }
        final boolean b211 = b209 & b210;
        boolean b212 = false;
        Label_3738: {
            if (supported_extensions.contains("OpenGL15")) {
                final long functionAddress118 = GLContext.getFunctionAddress("glNamedBufferSubDataEXT");
                this.glNamedBufferSubDataEXT = functionAddress118;
                if (functionAddress118 == 0L) {
                    b212 = false;
                    break Label_3738;
                }
            }
            b212 = true;
        }
        final boolean b213 = b211 & b212;
        boolean b214 = false;
        Label_3772: {
            if (supported_extensions.contains("OpenGL15")) {
                final long functionAddress119 = GLContext.getFunctionAddress("glMapNamedBufferEXT");
                this.glMapNamedBufferEXT = functionAddress119;
                if (functionAddress119 == 0L) {
                    b214 = false;
                    break Label_3772;
                }
            }
            b214 = true;
        }
        final boolean b215 = b213 & b214;
        boolean b216 = false;
        Label_3806: {
            if (supported_extensions.contains("OpenGL15")) {
                final long functionAddress120 = GLContext.getFunctionAddress("glUnmapNamedBufferEXT");
                this.glUnmapNamedBufferEXT = functionAddress120;
                if (functionAddress120 == 0L) {
                    b216 = false;
                    break Label_3806;
                }
            }
            b216 = true;
        }
        final boolean b217 = b215 & b216;
        boolean b218 = false;
        Label_3840: {
            if (supported_extensions.contains("OpenGL15")) {
                final long functionAddress121 = GLContext.getFunctionAddress("glGetNamedBufferParameterivEXT");
                this.glGetNamedBufferParameterivEXT = functionAddress121;
                if (functionAddress121 == 0L) {
                    b218 = false;
                    break Label_3840;
                }
            }
            b218 = true;
        }
        final boolean b219 = b217 & b218;
        boolean b220 = false;
        Label_3874: {
            if (supported_extensions.contains("OpenGL15")) {
                final long functionAddress122 = GLContext.getFunctionAddress("glGetNamedBufferPointervEXT");
                this.glGetNamedBufferPointervEXT = functionAddress122;
                if (functionAddress122 == 0L) {
                    b220 = false;
                    break Label_3874;
                }
            }
            b220 = true;
        }
        final boolean b221 = b219 & b220;
        boolean b222 = false;
        Label_3908: {
            if (supported_extensions.contains("OpenGL15")) {
                final long functionAddress123 = GLContext.getFunctionAddress("glGetNamedBufferSubDataEXT");
                this.glGetNamedBufferSubDataEXT = functionAddress123;
                if (functionAddress123 == 0L) {
                    b222 = false;
                    break Label_3908;
                }
            }
            b222 = true;
        }
        final boolean b223 = b221 & b222;
        boolean b224 = false;
        Label_3942: {
            if (supported_extensions.contains("OpenGL20")) {
                final long functionAddress124 = GLContext.getFunctionAddress("glProgramUniform1fEXT");
                this.glProgramUniform1fEXT = functionAddress124;
                if (functionAddress124 == 0L) {
                    b224 = false;
                    break Label_3942;
                }
            }
            b224 = true;
        }
        final boolean b225 = b223 & b224;
        boolean b226 = false;
        Label_3976: {
            if (supported_extensions.contains("OpenGL20")) {
                final long functionAddress125 = GLContext.getFunctionAddress("glProgramUniform2fEXT");
                this.glProgramUniform2fEXT = functionAddress125;
                if (functionAddress125 == 0L) {
                    b226 = false;
                    break Label_3976;
                }
            }
            b226 = true;
        }
        final boolean b227 = b225 & b226;
        boolean b228 = false;
        Label_4010: {
            if (supported_extensions.contains("OpenGL20")) {
                final long functionAddress126 = GLContext.getFunctionAddress("glProgramUniform3fEXT");
                this.glProgramUniform3fEXT = functionAddress126;
                if (functionAddress126 == 0L) {
                    b228 = false;
                    break Label_4010;
                }
            }
            b228 = true;
        }
        final boolean b229 = b227 & b228;
        boolean b230 = false;
        Label_4044: {
            if (supported_extensions.contains("OpenGL20")) {
                final long functionAddress127 = GLContext.getFunctionAddress("glProgramUniform4fEXT");
                this.glProgramUniform4fEXT = functionAddress127;
                if (functionAddress127 == 0L) {
                    b230 = false;
                    break Label_4044;
                }
            }
            b230 = true;
        }
        final boolean b231 = b229 & b230;
        boolean b232 = false;
        Label_4078: {
            if (supported_extensions.contains("OpenGL20")) {
                final long functionAddress128 = GLContext.getFunctionAddress("glProgramUniform1iEXT");
                this.glProgramUniform1iEXT = functionAddress128;
                if (functionAddress128 == 0L) {
                    b232 = false;
                    break Label_4078;
                }
            }
            b232 = true;
        }
        final boolean b233 = b231 & b232;
        boolean b234 = false;
        Label_4112: {
            if (supported_extensions.contains("OpenGL20")) {
                final long functionAddress129 = GLContext.getFunctionAddress("glProgramUniform2iEXT");
                this.glProgramUniform2iEXT = functionAddress129;
                if (functionAddress129 == 0L) {
                    b234 = false;
                    break Label_4112;
                }
            }
            b234 = true;
        }
        final boolean b235 = b233 & b234;
        boolean b236 = false;
        Label_4146: {
            if (supported_extensions.contains("OpenGL20")) {
                final long functionAddress130 = GLContext.getFunctionAddress("glProgramUniform3iEXT");
                this.glProgramUniform3iEXT = functionAddress130;
                if (functionAddress130 == 0L) {
                    b236 = false;
                    break Label_4146;
                }
            }
            b236 = true;
        }
        final boolean b237 = b235 & b236;
        boolean b238 = false;
        Label_4180: {
            if (supported_extensions.contains("OpenGL20")) {
                final long functionAddress131 = GLContext.getFunctionAddress("glProgramUniform4iEXT");
                this.glProgramUniform4iEXT = functionAddress131;
                if (functionAddress131 == 0L) {
                    b238 = false;
                    break Label_4180;
                }
            }
            b238 = true;
        }
        final boolean b239 = b237 & b238;
        boolean b240 = false;
        Label_4214: {
            if (supported_extensions.contains("OpenGL20")) {
                final long functionAddress132 = GLContext.getFunctionAddress("glProgramUniform1fvEXT");
                this.glProgramUniform1fvEXT = functionAddress132;
                if (functionAddress132 == 0L) {
                    b240 = false;
                    break Label_4214;
                }
            }
            b240 = true;
        }
        final boolean b241 = b239 & b240;
        boolean b242 = false;
        Label_4248: {
            if (supported_extensions.contains("OpenGL20")) {
                final long functionAddress133 = GLContext.getFunctionAddress("glProgramUniform2fvEXT");
                this.glProgramUniform2fvEXT = functionAddress133;
                if (functionAddress133 == 0L) {
                    b242 = false;
                    break Label_4248;
                }
            }
            b242 = true;
        }
        final boolean b243 = b241 & b242;
        boolean b244 = false;
        Label_4282: {
            if (supported_extensions.contains("OpenGL20")) {
                final long functionAddress134 = GLContext.getFunctionAddress("glProgramUniform3fvEXT");
                this.glProgramUniform3fvEXT = functionAddress134;
                if (functionAddress134 == 0L) {
                    b244 = false;
                    break Label_4282;
                }
            }
            b244 = true;
        }
        final boolean b245 = b243 & b244;
        boolean b246 = false;
        Label_4316: {
            if (supported_extensions.contains("OpenGL20")) {
                final long functionAddress135 = GLContext.getFunctionAddress("glProgramUniform4fvEXT");
                this.glProgramUniform4fvEXT = functionAddress135;
                if (functionAddress135 == 0L) {
                    b246 = false;
                    break Label_4316;
                }
            }
            b246 = true;
        }
        final boolean b247 = b245 & b246;
        boolean b248 = false;
        Label_4350: {
            if (supported_extensions.contains("OpenGL20")) {
                final long functionAddress136 = GLContext.getFunctionAddress("glProgramUniform1ivEXT");
                this.glProgramUniform1ivEXT = functionAddress136;
                if (functionAddress136 == 0L) {
                    b248 = false;
                    break Label_4350;
                }
            }
            b248 = true;
        }
        final boolean b249 = b247 & b248;
        boolean b250 = false;
        Label_4384: {
            if (supported_extensions.contains("OpenGL20")) {
                final long functionAddress137 = GLContext.getFunctionAddress("glProgramUniform2ivEXT");
                this.glProgramUniform2ivEXT = functionAddress137;
                if (functionAddress137 == 0L) {
                    b250 = false;
                    break Label_4384;
                }
            }
            b250 = true;
        }
        final boolean b251 = b249 & b250;
        boolean b252 = false;
        Label_4418: {
            if (supported_extensions.contains("OpenGL20")) {
                final long functionAddress138 = GLContext.getFunctionAddress("glProgramUniform3ivEXT");
                this.glProgramUniform3ivEXT = functionAddress138;
                if (functionAddress138 == 0L) {
                    b252 = false;
                    break Label_4418;
                }
            }
            b252 = true;
        }
        final boolean b253 = b251 & b252;
        boolean b254 = false;
        Label_4452: {
            if (supported_extensions.contains("OpenGL20")) {
                final long functionAddress139 = GLContext.getFunctionAddress("glProgramUniform4ivEXT");
                this.glProgramUniform4ivEXT = functionAddress139;
                if (functionAddress139 == 0L) {
                    b254 = false;
                    break Label_4452;
                }
            }
            b254 = true;
        }
        final boolean b255 = b253 & b254;
        boolean b256 = false;
        Label_4486: {
            if (supported_extensions.contains("OpenGL20")) {
                final long functionAddress140 = GLContext.getFunctionAddress("glProgramUniformMatrix2fvEXT");
                this.glProgramUniformMatrix2fvEXT = functionAddress140;
                if (functionAddress140 == 0L) {
                    b256 = false;
                    break Label_4486;
                }
            }
            b256 = true;
        }
        final boolean b257 = b255 & b256;
        boolean b258 = false;
        Label_4520: {
            if (supported_extensions.contains("OpenGL20")) {
                final long functionAddress141 = GLContext.getFunctionAddress("glProgramUniformMatrix3fvEXT");
                this.glProgramUniformMatrix3fvEXT = functionAddress141;
                if (functionAddress141 == 0L) {
                    b258 = false;
                    break Label_4520;
                }
            }
            b258 = true;
        }
        final boolean b259 = b257 & b258;
        boolean b260 = false;
        Label_4554: {
            if (supported_extensions.contains("OpenGL20")) {
                final long functionAddress142 = GLContext.getFunctionAddress("glProgramUniformMatrix4fvEXT");
                this.glProgramUniformMatrix4fvEXT = functionAddress142;
                if (functionAddress142 == 0L) {
                    b260 = false;
                    break Label_4554;
                }
            }
            b260 = true;
        }
        final boolean b261 = b259 & b260;
        boolean b262 = false;
        Label_4588: {
            if (supported_extensions.contains("OpenGL21")) {
                final long functionAddress143 = GLContext.getFunctionAddress("glProgramUniformMatrix2x3fvEXT");
                this.glProgramUniformMatrix2x3fvEXT = functionAddress143;
                if (functionAddress143 == 0L) {
                    b262 = false;
                    break Label_4588;
                }
            }
            b262 = true;
        }
        final boolean b263 = b261 & b262;
        boolean b264 = false;
        Label_4622: {
            if (supported_extensions.contains("OpenGL21")) {
                final long functionAddress144 = GLContext.getFunctionAddress("glProgramUniformMatrix3x2fvEXT");
                this.glProgramUniformMatrix3x2fvEXT = functionAddress144;
                if (functionAddress144 == 0L) {
                    b264 = false;
                    break Label_4622;
                }
            }
            b264 = true;
        }
        final boolean b265 = b263 & b264;
        boolean b266 = false;
        Label_4656: {
            if (supported_extensions.contains("OpenGL21")) {
                final long functionAddress145 = GLContext.getFunctionAddress("glProgramUniformMatrix2x4fvEXT");
                this.glProgramUniformMatrix2x4fvEXT = functionAddress145;
                if (functionAddress145 == 0L) {
                    b266 = false;
                    break Label_4656;
                }
            }
            b266 = true;
        }
        final boolean b267 = b265 & b266;
        boolean b268 = false;
        Label_4690: {
            if (supported_extensions.contains("OpenGL21")) {
                final long functionAddress146 = GLContext.getFunctionAddress("glProgramUniformMatrix4x2fvEXT");
                this.glProgramUniformMatrix4x2fvEXT = functionAddress146;
                if (functionAddress146 == 0L) {
                    b268 = false;
                    break Label_4690;
                }
            }
            b268 = true;
        }
        final boolean b269 = b267 & b268;
        boolean b270 = false;
        Label_4724: {
            if (supported_extensions.contains("OpenGL21")) {
                final long functionAddress147 = GLContext.getFunctionAddress("glProgramUniformMatrix3x4fvEXT");
                this.glProgramUniformMatrix3x4fvEXT = functionAddress147;
                if (functionAddress147 == 0L) {
                    b270 = false;
                    break Label_4724;
                }
            }
            b270 = true;
        }
        final boolean b271 = b269 & b270;
        boolean b272 = false;
        Label_4758: {
            if (supported_extensions.contains("OpenGL21")) {
                final long functionAddress148 = GLContext.getFunctionAddress("glProgramUniformMatrix4x3fvEXT");
                this.glProgramUniformMatrix4x3fvEXT = functionAddress148;
                if (functionAddress148 == 0L) {
                    b272 = false;
                    break Label_4758;
                }
            }
            b272 = true;
        }
        final boolean b273 = b271 & b272;
        boolean b274 = false;
        Label_4792: {
            if (supported_extensions.contains("GL_EXT_texture_buffer_object")) {
                final long functionAddress149 = GLContext.getFunctionAddress("glTextureBufferEXT");
                this.glTextureBufferEXT = functionAddress149;
                if (functionAddress149 == 0L) {
                    b274 = false;
                    break Label_4792;
                }
            }
            b274 = true;
        }
        final boolean b275 = b273 & b274;
        boolean b276 = false;
        Label_4826: {
            if (supported_extensions.contains("GL_EXT_texture_buffer_object")) {
                final long functionAddress150 = GLContext.getFunctionAddress("glMultiTexBufferEXT");
                this.glMultiTexBufferEXT = functionAddress150;
                if (functionAddress150 == 0L) {
                    b276 = false;
                    break Label_4826;
                }
            }
            b276 = true;
        }
        final boolean b277 = b275 & b276;
        boolean b278 = false;
        Label_4860: {
            if (supported_extensions.contains("GL_EXT_texture_integer")) {
                final long functionAddress151 = GLContext.getFunctionAddress("glTextureParameterIivEXT");
                this.glTextureParameterIivEXT = functionAddress151;
                if (functionAddress151 == 0L) {
                    b278 = false;
                    break Label_4860;
                }
            }
            b278 = true;
        }
        final boolean b279 = b277 & b278;
        boolean b280 = false;
        Label_4894: {
            if (supported_extensions.contains("GL_EXT_texture_integer")) {
                final long functionAddress152 = GLContext.getFunctionAddress("glTextureParameterIuivEXT");
                this.glTextureParameterIuivEXT = functionAddress152;
                if (functionAddress152 == 0L) {
                    b280 = false;
                    break Label_4894;
                }
            }
            b280 = true;
        }
        final boolean b281 = b279 & b280;
        boolean b282 = false;
        Label_4928: {
            if (supported_extensions.contains("GL_EXT_texture_integer")) {
                final long functionAddress153 = GLContext.getFunctionAddress("glGetTextureParameterIivEXT");
                this.glGetTextureParameterIivEXT = functionAddress153;
                if (functionAddress153 == 0L) {
                    b282 = false;
                    break Label_4928;
                }
            }
            b282 = true;
        }
        final boolean b283 = b281 & b282;
        boolean b284 = false;
        Label_4962: {
            if (supported_extensions.contains("GL_EXT_texture_integer")) {
                final long functionAddress154 = GLContext.getFunctionAddress("glGetTextureParameterIuivEXT");
                this.glGetTextureParameterIuivEXT = functionAddress154;
                if (functionAddress154 == 0L) {
                    b284 = false;
                    break Label_4962;
                }
            }
            b284 = true;
        }
        final boolean b285 = b283 & b284;
        boolean b286 = false;
        Label_4996: {
            if (supported_extensions.contains("GL_EXT_texture_integer")) {
                final long functionAddress155 = GLContext.getFunctionAddress("glMultiTexParameterIivEXT");
                this.glMultiTexParameterIivEXT = functionAddress155;
                if (functionAddress155 == 0L) {
                    b286 = false;
                    break Label_4996;
                }
            }
            b286 = true;
        }
        final boolean b287 = b285 & b286;
        boolean b288 = false;
        Label_5030: {
            if (supported_extensions.contains("GL_EXT_texture_integer")) {
                final long functionAddress156 = GLContext.getFunctionAddress("glMultiTexParameterIuivEXT");
                this.glMultiTexParameterIuivEXT = functionAddress156;
                if (functionAddress156 == 0L) {
                    b288 = false;
                    break Label_5030;
                }
            }
            b288 = true;
        }
        final boolean b289 = b287 & b288;
        boolean b290 = false;
        Label_5064: {
            if (supported_extensions.contains("GL_EXT_texture_integer")) {
                final long functionAddress157 = GLContext.getFunctionAddress("glGetMultiTexParameterIivEXT");
                this.glGetMultiTexParameterIivEXT = functionAddress157;
                if (functionAddress157 == 0L) {
                    b290 = false;
                    break Label_5064;
                }
            }
            b290 = true;
        }
        final boolean b291 = b289 & b290;
        boolean b292 = false;
        Label_5098: {
            if (supported_extensions.contains("GL_EXT_texture_integer")) {
                final long functionAddress158 = GLContext.getFunctionAddress("glGetMultiTexParameterIuivEXT");
                this.glGetMultiTexParameterIuivEXT = functionAddress158;
                if (functionAddress158 == 0L) {
                    b292 = false;
                    break Label_5098;
                }
            }
            b292 = true;
        }
        final boolean b293 = b291 & b292;
        boolean b294 = false;
        Label_5132: {
            if (supported_extensions.contains("GL_EXT_gpu_shader4")) {
                final long functionAddress159 = GLContext.getFunctionAddress("glProgramUniform1uiEXT");
                this.glProgramUniform1uiEXT = functionAddress159;
                if (functionAddress159 == 0L) {
                    b294 = false;
                    break Label_5132;
                }
            }
            b294 = true;
        }
        final boolean b295 = b293 & b294;
        boolean b296 = false;
        Label_5166: {
            if (supported_extensions.contains("GL_EXT_gpu_shader4")) {
                final long functionAddress160 = GLContext.getFunctionAddress("glProgramUniform2uiEXT");
                this.glProgramUniform2uiEXT = functionAddress160;
                if (functionAddress160 == 0L) {
                    b296 = false;
                    break Label_5166;
                }
            }
            b296 = true;
        }
        final boolean b297 = b295 & b296;
        boolean b298 = false;
        Label_5200: {
            if (supported_extensions.contains("GL_EXT_gpu_shader4")) {
                final long functionAddress161 = GLContext.getFunctionAddress("glProgramUniform3uiEXT");
                this.glProgramUniform3uiEXT = functionAddress161;
                if (functionAddress161 == 0L) {
                    b298 = false;
                    break Label_5200;
                }
            }
            b298 = true;
        }
        final boolean b299 = b297 & b298;
        boolean b300 = false;
        Label_5234: {
            if (supported_extensions.contains("GL_EXT_gpu_shader4")) {
                final long functionAddress162 = GLContext.getFunctionAddress("glProgramUniform4uiEXT");
                this.glProgramUniform4uiEXT = functionAddress162;
                if (functionAddress162 == 0L) {
                    b300 = false;
                    break Label_5234;
                }
            }
            b300 = true;
        }
        final boolean b301 = b299 & b300;
        boolean b302 = false;
        Label_5268: {
            if (supported_extensions.contains("GL_EXT_gpu_shader4")) {
                final long functionAddress163 = GLContext.getFunctionAddress("glProgramUniform1uivEXT");
                this.glProgramUniform1uivEXT = functionAddress163;
                if (functionAddress163 == 0L) {
                    b302 = false;
                    break Label_5268;
                }
            }
            b302 = true;
        }
        final boolean b303 = b301 & b302;
        boolean b304 = false;
        Label_5302: {
            if (supported_extensions.contains("GL_EXT_gpu_shader4")) {
                final long functionAddress164 = GLContext.getFunctionAddress("glProgramUniform2uivEXT");
                this.glProgramUniform2uivEXT = functionAddress164;
                if (functionAddress164 == 0L) {
                    b304 = false;
                    break Label_5302;
                }
            }
            b304 = true;
        }
        final boolean b305 = b303 & b304;
        boolean b306 = false;
        Label_5336: {
            if (supported_extensions.contains("GL_EXT_gpu_shader4")) {
                final long functionAddress165 = GLContext.getFunctionAddress("glProgramUniform3uivEXT");
                this.glProgramUniform3uivEXT = functionAddress165;
                if (functionAddress165 == 0L) {
                    b306 = false;
                    break Label_5336;
                }
            }
            b306 = true;
        }
        final boolean b307 = b305 & b306;
        boolean b308 = false;
        Label_5370: {
            if (supported_extensions.contains("GL_EXT_gpu_shader4")) {
                final long functionAddress166 = GLContext.getFunctionAddress("glProgramUniform4uivEXT");
                this.glProgramUniform4uivEXT = functionAddress166;
                if (functionAddress166 == 0L) {
                    b308 = false;
                    break Label_5370;
                }
            }
            b308 = true;
        }
        final boolean b309 = b307 & b308;
        boolean b310 = false;
        Label_5404: {
            if (supported_extensions.contains("GL_EXT_gpu_program_parameters")) {
                final long functionAddress167 = GLContext.getFunctionAddress("glNamedProgramLocalParameters4fvEXT");
                this.glNamedProgramLocalParameters4fvEXT = functionAddress167;
                if (functionAddress167 == 0L) {
                    b310 = false;
                    break Label_5404;
                }
            }
            b310 = true;
        }
        final boolean b311 = b309 & b310;
        boolean b312 = false;
        Label_5438: {
            if (supported_extensions.contains("GL_NV_gpu_program4")) {
                final long functionAddress168 = GLContext.getFunctionAddress("glNamedProgramLocalParameterI4iEXT");
                this.glNamedProgramLocalParameterI4iEXT = functionAddress168;
                if (functionAddress168 == 0L) {
                    b312 = false;
                    break Label_5438;
                }
            }
            b312 = true;
        }
        final boolean b313 = b311 & b312;
        boolean b314 = false;
        Label_5472: {
            if (supported_extensions.contains("GL_NV_gpu_program4")) {
                final long functionAddress169 = GLContext.getFunctionAddress("glNamedProgramLocalParameterI4ivEXT");
                this.glNamedProgramLocalParameterI4ivEXT = functionAddress169;
                if (functionAddress169 == 0L) {
                    b314 = false;
                    break Label_5472;
                }
            }
            b314 = true;
        }
        final boolean b315 = b313 & b314;
        boolean b316 = false;
        Label_5506: {
            if (supported_extensions.contains("GL_NV_gpu_program4")) {
                final long functionAddress170 = GLContext.getFunctionAddress("glNamedProgramLocalParametersI4ivEXT");
                this.glNamedProgramLocalParametersI4ivEXT = functionAddress170;
                if (functionAddress170 == 0L) {
                    b316 = false;
                    break Label_5506;
                }
            }
            b316 = true;
        }
        final boolean b317 = b315 & b316;
        boolean b318 = false;
        Label_5540: {
            if (supported_extensions.contains("GL_NV_gpu_program4")) {
                final long functionAddress171 = GLContext.getFunctionAddress("glNamedProgramLocalParameterI4uiEXT");
                this.glNamedProgramLocalParameterI4uiEXT = functionAddress171;
                if (functionAddress171 == 0L) {
                    b318 = false;
                    break Label_5540;
                }
            }
            b318 = true;
        }
        final boolean b319 = b317 & b318;
        boolean b320 = false;
        Label_5574: {
            if (supported_extensions.contains("GL_NV_gpu_program4")) {
                final long functionAddress172 = GLContext.getFunctionAddress("glNamedProgramLocalParameterI4uivEXT");
                this.glNamedProgramLocalParameterI4uivEXT = functionAddress172;
                if (functionAddress172 == 0L) {
                    b320 = false;
                    break Label_5574;
                }
            }
            b320 = true;
        }
        final boolean b321 = b319 & b320;
        boolean b322 = false;
        Label_5608: {
            if (supported_extensions.contains("GL_NV_gpu_program4")) {
                final long functionAddress173 = GLContext.getFunctionAddress("glNamedProgramLocalParametersI4uivEXT");
                this.glNamedProgramLocalParametersI4uivEXT = functionAddress173;
                if (functionAddress173 == 0L) {
                    b322 = false;
                    break Label_5608;
                }
            }
            b322 = true;
        }
        final boolean b323 = b321 & b322;
        boolean b324 = false;
        Label_5642: {
            if (supported_extensions.contains("GL_NV_gpu_program4")) {
                final long functionAddress174 = GLContext.getFunctionAddress("glGetNamedProgramLocalParameterIivEXT");
                this.glGetNamedProgramLocalParameterIivEXT = functionAddress174;
                if (functionAddress174 == 0L) {
                    b324 = false;
                    break Label_5642;
                }
            }
            b324 = true;
        }
        final boolean b325 = b323 & b324;
        boolean b326 = false;
        Label_5676: {
            if (supported_extensions.contains("GL_NV_gpu_program4")) {
                final long functionAddress175 = GLContext.getFunctionAddress("glGetNamedProgramLocalParameterIuivEXT");
                this.glGetNamedProgramLocalParameterIuivEXT = functionAddress175;
                if (functionAddress175 == 0L) {
                    b326 = false;
                    break Label_5676;
                }
            }
            b326 = true;
        }
        final boolean b327 = b325 & b326;
        boolean b328 = false;
        Label_5722: {
            if (supported_extensions.contains("OpenGL30") || supported_extensions.contains("GL_EXT_framebuffer_object")) {
                final long functionAddress176 = GLContext.getFunctionAddress("glNamedRenderbufferStorageEXT");
                this.glNamedRenderbufferStorageEXT = functionAddress176;
                if (functionAddress176 == 0L) {
                    b328 = false;
                    break Label_5722;
                }
            }
            b328 = true;
        }
        final boolean b329 = b327 & b328;
        boolean b330 = false;
        Label_5768: {
            if (supported_extensions.contains("OpenGL30") || supported_extensions.contains("GL_EXT_framebuffer_object")) {
                final long functionAddress177 = GLContext.getFunctionAddress("glGetNamedRenderbufferParameterivEXT");
                this.glGetNamedRenderbufferParameterivEXT = functionAddress177;
                if (functionAddress177 == 0L) {
                    b330 = false;
                    break Label_5768;
                }
            }
            b330 = true;
        }
        final boolean b331 = b329 & b330;
        boolean b332 = false;
        Label_5814: {
            if (supported_extensions.contains("OpenGL30") || supported_extensions.contains("GL_EXT_framebuffer_multisample")) {
                final long functionAddress178 = GLContext.getFunctionAddress("glNamedRenderbufferStorageMultisampleEXT");
                this.glNamedRenderbufferStorageMultisampleEXT = functionAddress178;
                if (functionAddress178 == 0L) {
                    b332 = false;
                    break Label_5814;
                }
            }
            b332 = true;
        }
        final boolean b333 = b331 & b332;
        boolean b334 = false;
        Label_5848: {
            if (supported_extensions.contains("GL_NV_framebuffer_multisample_coverage")) {
                final long functionAddress179 = GLContext.getFunctionAddress("glNamedRenderbufferStorageMultisampleCoverageEXT");
                this.glNamedRenderbufferStorageMultisampleCoverageEXT = functionAddress179;
                if (functionAddress179 == 0L) {
                    b334 = false;
                    break Label_5848;
                }
            }
            b334 = true;
        }
        final boolean b335 = b333 & b334;
        boolean b336 = false;
        Label_5894: {
            if (supported_extensions.contains("OpenGL30") || supported_extensions.contains("GL_EXT_framebuffer_object")) {
                final long functionAddress180 = GLContext.getFunctionAddress("glCheckNamedFramebufferStatusEXT");
                this.glCheckNamedFramebufferStatusEXT = functionAddress180;
                if (functionAddress180 == 0L) {
                    b336 = false;
                    break Label_5894;
                }
            }
            b336 = true;
        }
        final boolean b337 = b335 & b336;
        boolean b338 = false;
        Label_5940: {
            if (supported_extensions.contains("OpenGL30") || supported_extensions.contains("GL_EXT_framebuffer_object")) {
                final long functionAddress181 = GLContext.getFunctionAddress("glNamedFramebufferTexture1DEXT");
                this.glNamedFramebufferTexture1DEXT = functionAddress181;
                if (functionAddress181 == 0L) {
                    b338 = false;
                    break Label_5940;
                }
            }
            b338 = true;
        }
        final boolean b339 = b337 & b338;
        boolean b340 = false;
        Label_5986: {
            if (supported_extensions.contains("OpenGL30") || supported_extensions.contains("GL_EXT_framebuffer_object")) {
                final long functionAddress182 = GLContext.getFunctionAddress("glNamedFramebufferTexture2DEXT");
                this.glNamedFramebufferTexture2DEXT = functionAddress182;
                if (functionAddress182 == 0L) {
                    b340 = false;
                    break Label_5986;
                }
            }
            b340 = true;
        }
        final boolean b341 = b339 & b340;
        boolean b342 = false;
        Label_6032: {
            if (supported_extensions.contains("OpenGL30") || supported_extensions.contains("GL_EXT_framebuffer_object")) {
                final long functionAddress183 = GLContext.getFunctionAddress("glNamedFramebufferTexture3DEXT");
                this.glNamedFramebufferTexture3DEXT = functionAddress183;
                if (functionAddress183 == 0L) {
                    b342 = false;
                    break Label_6032;
                }
            }
            b342 = true;
        }
        final boolean b343 = b341 & b342;
        boolean b344 = false;
        Label_6078: {
            if (supported_extensions.contains("OpenGL30") || supported_extensions.contains("GL_EXT_framebuffer_object")) {
                final long functionAddress184 = GLContext.getFunctionAddress("glNamedFramebufferRenderbufferEXT");
                this.glNamedFramebufferRenderbufferEXT = functionAddress184;
                if (functionAddress184 == 0L) {
                    b344 = false;
                    break Label_6078;
                }
            }
            b344 = true;
        }
        final boolean b345 = b343 & b344;
        boolean b346 = false;
        Label_6124: {
            if (supported_extensions.contains("OpenGL30") || supported_extensions.contains("GL_EXT_framebuffer_object")) {
                final long functionAddress185 = GLContext.getFunctionAddress("glGetNamedFramebufferAttachmentParameterivEXT");
                this.glGetNamedFramebufferAttachmentParameterivEXT = functionAddress185;
                if (functionAddress185 == 0L) {
                    b346 = false;
                    break Label_6124;
                }
            }
            b346 = true;
        }
        final boolean b347 = b345 & b346;
        boolean b348 = false;
        Label_6170: {
            if (supported_extensions.contains("OpenGL30") || supported_extensions.contains("GL_EXT_framebuffer_object")) {
                final long functionAddress186 = GLContext.getFunctionAddress("glGenerateTextureMipmapEXT");
                this.glGenerateTextureMipmapEXT = functionAddress186;
                if (functionAddress186 == 0L) {
                    b348 = false;
                    break Label_6170;
                }
            }
            b348 = true;
        }
        final boolean b349 = b347 & b348;
        boolean b350 = false;
        Label_6216: {
            if (supported_extensions.contains("OpenGL30") || supported_extensions.contains("GL_EXT_framebuffer_object")) {
                final long functionAddress187 = GLContext.getFunctionAddress("glGenerateMultiTexMipmapEXT");
                this.glGenerateMultiTexMipmapEXT = functionAddress187;
                if (functionAddress187 == 0L) {
                    b350 = false;
                    break Label_6216;
                }
            }
            b350 = true;
        }
        final boolean b351 = b349 & b350;
        boolean b352 = false;
        Label_6262: {
            if (supported_extensions.contains("OpenGL30") || supported_extensions.contains("GL_EXT_framebuffer_object")) {
                final long functionAddress188 = GLContext.getFunctionAddress("glFramebufferDrawBufferEXT");
                this.glFramebufferDrawBufferEXT = functionAddress188;
                if (functionAddress188 == 0L) {
                    b352 = false;
                    break Label_6262;
                }
            }
            b352 = true;
        }
        final boolean b353 = b351 & b352;
        boolean b354 = false;
        Label_6308: {
            if (supported_extensions.contains("OpenGL30") || supported_extensions.contains("GL_EXT_framebuffer_object")) {
                final long functionAddress189 = GLContext.getFunctionAddress("glFramebufferDrawBuffersEXT");
                this.glFramebufferDrawBuffersEXT = functionAddress189;
                if (functionAddress189 == 0L) {
                    b354 = false;
                    break Label_6308;
                }
            }
            b354 = true;
        }
        final boolean b355 = b353 & b354;
        boolean b356 = false;
        Label_6354: {
            if (supported_extensions.contains("OpenGL30") || supported_extensions.contains("GL_EXT_framebuffer_object")) {
                final long functionAddress190 = GLContext.getFunctionAddress("glFramebufferReadBufferEXT");
                this.glFramebufferReadBufferEXT = functionAddress190;
                if (functionAddress190 == 0L) {
                    b356 = false;
                    break Label_6354;
                }
            }
            b356 = true;
        }
        final boolean b357 = b355 & b356;
        boolean b358 = false;
        Label_6400: {
            if (supported_extensions.contains("OpenGL30") || supported_extensions.contains("GL_EXT_framebuffer_object")) {
                final long functionAddress191 = GLContext.getFunctionAddress("glGetFramebufferParameterivEXT");
                this.glGetFramebufferParameterivEXT = functionAddress191;
                if (functionAddress191 == 0L) {
                    b358 = false;
                    break Label_6400;
                }
            }
            b358 = true;
        }
        final boolean b359 = b357 & b358;
        boolean b360 = false;
        Label_6446: {
            if (supported_extensions.contains("OpenGL31") || supported_extensions.contains("GL_ARB_copy_buffer")) {
                final long functionAddress192 = GLContext.getFunctionAddress("glNamedCopyBufferSubDataEXT");
                this.glNamedCopyBufferSubDataEXT = functionAddress192;
                if (functionAddress192 == 0L) {
                    b360 = false;
                    break Label_6446;
                }
            }
            b360 = true;
        }
        final boolean b361 = b359 & b360;
        boolean b362 = false;
        Label_6492: {
            if (supported_extensions.contains("GL_EXT_geometry_shader4") || supported_extensions.contains("GL_NV_geometry_program4")) {
                final long functionAddress193 = GLContext.getFunctionAddress("glNamedFramebufferTextureEXT");
                this.glNamedFramebufferTextureEXT = functionAddress193;
                if (functionAddress193 == 0L) {
                    b362 = false;
                    break Label_6492;
                }
            }
            b362 = true;
        }
        final boolean b363 = b361 & b362;
        boolean b364 = false;
        Label_6538: {
            if (supported_extensions.contains("GL_EXT_geometry_shader4") || supported_extensions.contains("GL_NV_geometry_program4")) {
                final long functionAddress194 = GLContext.getFunctionAddress("glNamedFramebufferTextureLayerEXT");
                this.glNamedFramebufferTextureLayerEXT = functionAddress194;
                if (functionAddress194 == 0L) {
                    b364 = false;
                    break Label_6538;
                }
            }
            b364 = true;
        }
        final boolean b365 = b363 & b364;
        boolean b366 = false;
        Label_6584: {
            if (supported_extensions.contains("GL_EXT_geometry_shader4") || supported_extensions.contains("GL_NV_geometry_program4")) {
                final long functionAddress195 = GLContext.getFunctionAddress("glNamedFramebufferTextureFaceEXT");
                this.glNamedFramebufferTextureFaceEXT = functionAddress195;
                if (functionAddress195 == 0L) {
                    b366 = false;
                    break Label_6584;
                }
            }
            b366 = true;
        }
        final boolean b367 = b365 & b366;
        boolean b368 = false;
        Label_6618: {
            if (supported_extensions.contains("GL_NV_explicit_multisample")) {
                final long functionAddress196 = GLContext.getFunctionAddress("glTextureRenderbufferEXT");
                this.glTextureRenderbufferEXT = functionAddress196;
                if (functionAddress196 == 0L) {
                    b368 = false;
                    break Label_6618;
                }
            }
            b368 = true;
        }
        final boolean b369 = b367 & b368;
        boolean b370 = false;
        Label_6652: {
            if (supported_extensions.contains("GL_NV_explicit_multisample")) {
                final long functionAddress197 = GLContext.getFunctionAddress("glMultiTexRenderbufferEXT");
                this.glMultiTexRenderbufferEXT = functionAddress197;
                if (functionAddress197 == 0L) {
                    b370 = false;
                    break Label_6652;
                }
            }
            b370 = true;
        }
        final boolean b371 = b369 & b370;
        boolean b372 = false;
        Label_6690: {
            if (!forwardCompatible && supported_extensions.contains("OpenGL30")) {
                final long functionAddress198 = GLContext.getFunctionAddress("glVertexArrayVertexOffsetEXT");
                this.glVertexArrayVertexOffsetEXT = functionAddress198;
                if (functionAddress198 == 0L) {
                    b372 = false;
                    break Label_6690;
                }
            }
            b372 = true;
        }
        final boolean b373 = b371 & b372;
        boolean b374 = false;
        Label_6728: {
            if (!forwardCompatible && supported_extensions.contains("OpenGL30")) {
                final long functionAddress199 = GLContext.getFunctionAddress("glVertexArrayColorOffsetEXT");
                this.glVertexArrayColorOffsetEXT = functionAddress199;
                if (functionAddress199 == 0L) {
                    b374 = false;
                    break Label_6728;
                }
            }
            b374 = true;
        }
        final boolean b375 = b373 & b374;
        boolean b376 = false;
        Label_6766: {
            if (!forwardCompatible && supported_extensions.contains("OpenGL30")) {
                final long functionAddress200 = GLContext.getFunctionAddress("glVertexArrayEdgeFlagOffsetEXT");
                this.glVertexArrayEdgeFlagOffsetEXT = functionAddress200;
                if (functionAddress200 == 0L) {
                    b376 = false;
                    break Label_6766;
                }
            }
            b376 = true;
        }
        final boolean b377 = b375 & b376;
        boolean b378 = false;
        Label_6800: {
            if (supported_extensions.contains("OpenGL30")) {
                final long functionAddress201 = GLContext.getFunctionAddress("glVertexArrayIndexOffsetEXT");
                this.glVertexArrayIndexOffsetEXT = functionAddress201;
                if (functionAddress201 == 0L) {
                    b378 = false;
                    break Label_6800;
                }
            }
            b378 = true;
        }
        final boolean b379 = b377 & b378;
        boolean b380 = false;
        Label_6838: {
            if (!forwardCompatible && supported_extensions.contains("OpenGL30")) {
                final long functionAddress202 = GLContext.getFunctionAddress("glVertexArrayNormalOffsetEXT");
                this.glVertexArrayNormalOffsetEXT = functionAddress202;
                if (functionAddress202 == 0L) {
                    b380 = false;
                    break Label_6838;
                }
            }
            b380 = true;
        }
        final boolean b381 = b379 & b380;
        boolean b382 = false;
        Label_6876: {
            if (!forwardCompatible && supported_extensions.contains("OpenGL30")) {
                final long functionAddress203 = GLContext.getFunctionAddress("glVertexArrayTexCoordOffsetEXT");
                this.glVertexArrayTexCoordOffsetEXT = functionAddress203;
                if (functionAddress203 == 0L) {
                    b382 = false;
                    break Label_6876;
                }
            }
            b382 = true;
        }
        final boolean b383 = b381 & b382;
        boolean b384 = false;
        Label_6914: {
            if (!forwardCompatible && supported_extensions.contains("OpenGL30")) {
                final long functionAddress204 = GLContext.getFunctionAddress("glVertexArrayMultiTexCoordOffsetEXT");
                this.glVertexArrayMultiTexCoordOffsetEXT = functionAddress204;
                if (functionAddress204 == 0L) {
                    b384 = false;
                    break Label_6914;
                }
            }
            b384 = true;
        }
        final boolean b385 = b383 & b384;
        boolean b386 = false;
        Label_6952: {
            if (!forwardCompatible && supported_extensions.contains("OpenGL30")) {
                final long functionAddress205 = GLContext.getFunctionAddress("glVertexArrayFogCoordOffsetEXT");
                this.glVertexArrayFogCoordOffsetEXT = functionAddress205;
                if (functionAddress205 == 0L) {
                    b386 = false;
                    break Label_6952;
                }
            }
            b386 = true;
        }
        final boolean b387 = b385 & b386;
        boolean b388 = false;
        Label_6990: {
            if (!forwardCompatible && supported_extensions.contains("OpenGL30")) {
                final long functionAddress206 = GLContext.getFunctionAddress("glVertexArraySecondaryColorOffsetEXT");
                this.glVertexArraySecondaryColorOffsetEXT = functionAddress206;
                if (functionAddress206 == 0L) {
                    b388 = false;
                    break Label_6990;
                }
            }
            b388 = true;
        }
        final boolean b389 = b387 & b388;
        boolean b390 = false;
        Label_7024: {
            if (supported_extensions.contains("OpenGL30")) {
                final long functionAddress207 = GLContext.getFunctionAddress("glVertexArrayVertexAttribOffsetEXT");
                this.glVertexArrayVertexAttribOffsetEXT = functionAddress207;
                if (functionAddress207 == 0L) {
                    b390 = false;
                    break Label_7024;
                }
            }
            b390 = true;
        }
        final boolean b391 = b389 & b390;
        boolean b392 = false;
        Label_7058: {
            if (supported_extensions.contains("OpenGL30")) {
                final long functionAddress208 = GLContext.getFunctionAddress("glVertexArrayVertexAttribIOffsetEXT");
                this.glVertexArrayVertexAttribIOffsetEXT = functionAddress208;
                if (functionAddress208 == 0L) {
                    b392 = false;
                    break Label_7058;
                }
            }
            b392 = true;
        }
        final boolean b393 = b391 & b392;
        boolean b394 = false;
        Label_7092: {
            if (supported_extensions.contains("OpenGL30")) {
                final long functionAddress209 = GLContext.getFunctionAddress("glEnableVertexArrayEXT");
                this.glEnableVertexArrayEXT = functionAddress209;
                if (functionAddress209 == 0L) {
                    b394 = false;
                    break Label_7092;
                }
            }
            b394 = true;
        }
        final boolean b395 = b393 & b394;
        boolean b396 = false;
        Label_7126: {
            if (supported_extensions.contains("OpenGL30")) {
                final long functionAddress210 = GLContext.getFunctionAddress("glDisableVertexArrayEXT");
                this.glDisableVertexArrayEXT = functionAddress210;
                if (functionAddress210 == 0L) {
                    b396 = false;
                    break Label_7126;
                }
            }
            b396 = true;
        }
        final boolean b397 = b395 & b396;
        boolean b398 = false;
        Label_7160: {
            if (supported_extensions.contains("OpenGL30")) {
                final long functionAddress211 = GLContext.getFunctionAddress("glEnableVertexArrayAttribEXT");
                this.glEnableVertexArrayAttribEXT = functionAddress211;
                if (functionAddress211 == 0L) {
                    b398 = false;
                    break Label_7160;
                }
            }
            b398 = true;
        }
        final boolean b399 = b397 & b398;
        boolean b400 = false;
        Label_7194: {
            if (supported_extensions.contains("OpenGL30")) {
                final long functionAddress212 = GLContext.getFunctionAddress("glDisableVertexArrayAttribEXT");
                this.glDisableVertexArrayAttribEXT = functionAddress212;
                if (functionAddress212 == 0L) {
                    b400 = false;
                    break Label_7194;
                }
            }
            b400 = true;
        }
        final boolean b401 = b399 & b400;
        boolean b402 = false;
        Label_7228: {
            if (supported_extensions.contains("OpenGL30")) {
                final long functionAddress213 = GLContext.getFunctionAddress("glGetVertexArrayIntegervEXT");
                this.glGetVertexArrayIntegervEXT = functionAddress213;
                if (functionAddress213 == 0L) {
                    b402 = false;
                    break Label_7228;
                }
            }
            b402 = true;
        }
        final boolean b403 = b401 & b402;
        boolean b404 = false;
        Label_7262: {
            if (supported_extensions.contains("OpenGL30")) {
                final long functionAddress214 = GLContext.getFunctionAddress("glGetVertexArrayPointervEXT");
                this.glGetVertexArrayPointervEXT = functionAddress214;
                if (functionAddress214 == 0L) {
                    b404 = false;
                    break Label_7262;
                }
            }
            b404 = true;
        }
        final boolean b405 = b403 & b404;
        boolean b406 = false;
        Label_7296: {
            if (supported_extensions.contains("OpenGL30")) {
                final long functionAddress215 = GLContext.getFunctionAddress("glGetVertexArrayIntegeri_vEXT");
                this.glGetVertexArrayIntegeri_vEXT = functionAddress215;
                if (functionAddress215 == 0L) {
                    b406 = false;
                    break Label_7296;
                }
            }
            b406 = true;
        }
        final boolean b407 = b405 & b406;
        boolean b408 = false;
        Label_7330: {
            if (supported_extensions.contains("OpenGL30")) {
                final long functionAddress216 = GLContext.getFunctionAddress("glGetVertexArrayPointeri_vEXT");
                this.glGetVertexArrayPointeri_vEXT = functionAddress216;
                if (functionAddress216 == 0L) {
                    b408 = false;
                    break Label_7330;
                }
            }
            b408 = true;
        }
        final boolean b409 = b407 & b408;
        boolean b410 = false;
        Label_7364: {
            if (supported_extensions.contains("OpenGL30")) {
                final long functionAddress217 = GLContext.getFunctionAddress("glMapNamedBufferRangeEXT");
                this.glMapNamedBufferRangeEXT = functionAddress217;
                if (functionAddress217 == 0L) {
                    b410 = false;
                    break Label_7364;
                }
            }
            b410 = true;
        }
        final boolean b411 = b409 & b410;
        if (supported_extensions.contains("OpenGL30")) {
            final long functionAddress218 = GLContext.getFunctionAddress("glFlushMappedNamedBufferRangeEXT");
            this.glFlushMappedNamedBufferRangeEXT = functionAddress218;
            if (functionAddress218 == 0L) {
                final boolean b412 = false;
                return b411 & b412;
            }
        }
        final boolean b412 = true;
        return b411 & b412;
    }
    
    private boolean EXT_draw_buffers2_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glColorMaskIndexedEXT");
        this.glColorMaskIndexedEXT = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glGetBooleanIndexedvEXT");
        this.glGetBooleanIndexedvEXT = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glGetIntegerIndexedvEXT");
        this.glGetIntegerIndexedvEXT = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glEnableIndexedEXT");
        this.glEnableIndexedEXT = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glDisableIndexedEXT");
        this.glDisableIndexedEXT = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glIsEnabledIndexedEXT");
        this.glIsEnabledIndexedEXT = functionAddress6;
        return b5 & functionAddress6 != 0L;
    }
    
    private boolean EXT_draw_instanced_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glDrawArraysInstancedEXT");
        this.glDrawArraysInstancedEXT = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glDrawElementsInstancedEXT");
        this.glDrawElementsInstancedEXT = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean EXT_draw_range_elements_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glDrawRangeElementsEXT");
        this.glDrawRangeElementsEXT = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean EXT_fog_coord_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glFogCoordfEXT");
        this.glFogCoordfEXT = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glFogCoorddEXT");
        this.glFogCoorddEXT = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glFogCoordPointerEXT");
        this.glFogCoordPointerEXT = functionAddress3;
        return b2 & functionAddress3 != 0L;
    }
    
    private boolean EXT_framebuffer_blit_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glBlitFramebufferEXT");
        this.glBlitFramebufferEXT = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean EXT_framebuffer_multisample_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glRenderbufferStorageMultisampleEXT");
        this.glRenderbufferStorageMultisampleEXT = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean EXT_framebuffer_object_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glIsRenderbufferEXT");
        this.glIsRenderbufferEXT = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glBindRenderbufferEXT");
        this.glBindRenderbufferEXT = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glDeleteRenderbuffersEXT");
        this.glDeleteRenderbuffersEXT = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glGenRenderbuffersEXT");
        this.glGenRenderbuffersEXT = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glRenderbufferStorageEXT");
        this.glRenderbufferStorageEXT = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glGetRenderbufferParameterivEXT");
        this.glGetRenderbufferParameterivEXT = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glIsFramebufferEXT");
        this.glIsFramebufferEXT = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glBindFramebufferEXT");
        this.glBindFramebufferEXT = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glDeleteFramebuffersEXT");
        this.glDeleteFramebuffersEXT = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glGenFramebuffersEXT");
        this.glGenFramebuffersEXT = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glCheckFramebufferStatusEXT");
        this.glCheckFramebufferStatusEXT = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glFramebufferTexture1DEXT");
        this.glFramebufferTexture1DEXT = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glFramebufferTexture2DEXT");
        this.glFramebufferTexture2DEXT = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glFramebufferTexture3DEXT");
        this.glFramebufferTexture3DEXT = functionAddress14;
        final boolean b14 = b13 & functionAddress14 != 0L;
        final long functionAddress15 = GLContext.getFunctionAddress("glFramebufferRenderbufferEXT");
        this.glFramebufferRenderbufferEXT = functionAddress15;
        final boolean b15 = b14 & functionAddress15 != 0L;
        final long functionAddress16 = GLContext.getFunctionAddress("glGetFramebufferAttachmentParameterivEXT");
        this.glGetFramebufferAttachmentParameterivEXT = functionAddress16;
        final boolean b16 = b15 & functionAddress16 != 0L;
        final long functionAddress17 = GLContext.getFunctionAddress("glGenerateMipmapEXT");
        this.glGenerateMipmapEXT = functionAddress17;
        return b16 & functionAddress17 != 0L;
    }
    
    private boolean EXT_geometry_shader4_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glProgramParameteriEXT");
        this.glProgramParameteriEXT = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glFramebufferTextureEXT");
        this.glFramebufferTextureEXT = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glFramebufferTextureLayerEXT");
        this.glFramebufferTextureLayerEXT = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glFramebufferTextureFaceEXT");
        this.glFramebufferTextureFaceEXT = functionAddress4;
        return b3 & functionAddress4 != 0L;
    }
    
    private boolean EXT_gpu_program_parameters_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glProgramEnvParameters4fvEXT");
        this.glProgramEnvParameters4fvEXT = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glProgramLocalParameters4fvEXT");
        this.glProgramLocalParameters4fvEXT = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean EXT_gpu_shader4_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glVertexAttribI1iEXT");
        this.glVertexAttribI1iEXT = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glVertexAttribI2iEXT");
        this.glVertexAttribI2iEXT = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glVertexAttribI3iEXT");
        this.glVertexAttribI3iEXT = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glVertexAttribI4iEXT");
        this.glVertexAttribI4iEXT = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glVertexAttribI1uiEXT");
        this.glVertexAttribI1uiEXT = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glVertexAttribI2uiEXT");
        this.glVertexAttribI2uiEXT = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glVertexAttribI3uiEXT");
        this.glVertexAttribI3uiEXT = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glVertexAttribI4uiEXT");
        this.glVertexAttribI4uiEXT = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glVertexAttribI1ivEXT");
        this.glVertexAttribI1ivEXT = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glVertexAttribI2ivEXT");
        this.glVertexAttribI2ivEXT = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glVertexAttribI3ivEXT");
        this.glVertexAttribI3ivEXT = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glVertexAttribI4ivEXT");
        this.glVertexAttribI4ivEXT = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glVertexAttribI1uivEXT");
        this.glVertexAttribI1uivEXT = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glVertexAttribI2uivEXT");
        this.glVertexAttribI2uivEXT = functionAddress14;
        final boolean b14 = b13 & functionAddress14 != 0L;
        final long functionAddress15 = GLContext.getFunctionAddress("glVertexAttribI3uivEXT");
        this.glVertexAttribI3uivEXT = functionAddress15;
        final boolean b15 = b14 & functionAddress15 != 0L;
        final long functionAddress16 = GLContext.getFunctionAddress("glVertexAttribI4uivEXT");
        this.glVertexAttribI4uivEXT = functionAddress16;
        final boolean b16 = b15 & functionAddress16 != 0L;
        final long functionAddress17 = GLContext.getFunctionAddress("glVertexAttribI4bvEXT");
        this.glVertexAttribI4bvEXT = functionAddress17;
        final boolean b17 = b16 & functionAddress17 != 0L;
        final long functionAddress18 = GLContext.getFunctionAddress("glVertexAttribI4svEXT");
        this.glVertexAttribI4svEXT = functionAddress18;
        final boolean b18 = b17 & functionAddress18 != 0L;
        final long functionAddress19 = GLContext.getFunctionAddress("glVertexAttribI4ubvEXT");
        this.glVertexAttribI4ubvEXT = functionAddress19;
        final boolean b19 = b18 & functionAddress19 != 0L;
        final long functionAddress20 = GLContext.getFunctionAddress("glVertexAttribI4usvEXT");
        this.glVertexAttribI4usvEXT = functionAddress20;
        final boolean b20 = b19 & functionAddress20 != 0L;
        final long functionAddress21 = GLContext.getFunctionAddress("glVertexAttribIPointerEXT");
        this.glVertexAttribIPointerEXT = functionAddress21;
        final boolean b21 = b20 & functionAddress21 != 0L;
        final long functionAddress22 = GLContext.getFunctionAddress("glGetVertexAttribIivEXT");
        this.glGetVertexAttribIivEXT = functionAddress22;
        final boolean b22 = b21 & functionAddress22 != 0L;
        final long functionAddress23 = GLContext.getFunctionAddress("glGetVertexAttribIuivEXT");
        this.glGetVertexAttribIuivEXT = functionAddress23;
        final boolean b23 = b22 & functionAddress23 != 0L;
        final long functionAddress24 = GLContext.getFunctionAddress("glUniform1uiEXT");
        this.glUniform1uiEXT = functionAddress24;
        final boolean b24 = b23 & functionAddress24 != 0L;
        final long functionAddress25 = GLContext.getFunctionAddress("glUniform2uiEXT");
        this.glUniform2uiEXT = functionAddress25;
        final boolean b25 = b24 & functionAddress25 != 0L;
        final long functionAddress26 = GLContext.getFunctionAddress("glUniform3uiEXT");
        this.glUniform3uiEXT = functionAddress26;
        final boolean b26 = b25 & functionAddress26 != 0L;
        final long functionAddress27 = GLContext.getFunctionAddress("glUniform4uiEXT");
        this.glUniform4uiEXT = functionAddress27;
        final boolean b27 = b26 & functionAddress27 != 0L;
        final long functionAddress28 = GLContext.getFunctionAddress("glUniform1uivEXT");
        this.glUniform1uivEXT = functionAddress28;
        final boolean b28 = b27 & functionAddress28 != 0L;
        final long functionAddress29 = GLContext.getFunctionAddress("glUniform2uivEXT");
        this.glUniform2uivEXT = functionAddress29;
        final boolean b29 = b28 & functionAddress29 != 0L;
        final long functionAddress30 = GLContext.getFunctionAddress("glUniform3uivEXT");
        this.glUniform3uivEXT = functionAddress30;
        final boolean b30 = b29 & functionAddress30 != 0L;
        final long functionAddress31 = GLContext.getFunctionAddress("glUniform4uivEXT");
        this.glUniform4uivEXT = functionAddress31;
        final boolean b31 = b30 & functionAddress31 != 0L;
        final long functionAddress32 = GLContext.getFunctionAddress("glGetUniformuivEXT");
        this.glGetUniformuivEXT = functionAddress32;
        final boolean b32 = b31 & functionAddress32 != 0L;
        final long functionAddress33 = GLContext.getFunctionAddress("glBindFragDataLocationEXT");
        this.glBindFragDataLocationEXT = functionAddress33;
        final boolean b33 = b32 & functionAddress33 != 0L;
        final long functionAddress34 = GLContext.getFunctionAddress("glGetFragDataLocationEXT");
        this.glGetFragDataLocationEXT = functionAddress34;
        return b33 & functionAddress34 != 0L;
    }
    
    private boolean EXT_multi_draw_arrays_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glMultiDrawArraysEXT");
        this.glMultiDrawArraysEXT = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean EXT_paletted_texture_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glColorTableEXT");
        this.glColorTableEXT = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glColorSubTableEXT");
        this.glColorSubTableEXT = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glGetColorTableEXT");
        this.glGetColorTableEXT = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glGetColorTableParameterivEXT");
        this.glGetColorTableParameterivEXT = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glGetColorTableParameterfvEXT");
        this.glGetColorTableParameterfvEXT = functionAddress5;
        return b4 & functionAddress5 != 0L;
    }
    
    private boolean EXT_point_parameters_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glPointParameterfEXT");
        this.glPointParameterfEXT = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glPointParameterfvEXT");
        this.glPointParameterfvEXT = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean EXT_provoking_vertex_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glProvokingVertexEXT");
        this.glProvokingVertexEXT = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean EXT_secondary_color_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glSecondaryColor3bEXT");
        this.glSecondaryColor3bEXT = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glSecondaryColor3fEXT");
        this.glSecondaryColor3fEXT = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glSecondaryColor3dEXT");
        this.glSecondaryColor3dEXT = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glSecondaryColor3ubEXT");
        this.glSecondaryColor3ubEXT = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glSecondaryColorPointerEXT");
        this.glSecondaryColorPointerEXT = functionAddress5;
        return b4 & functionAddress5 != 0L;
    }
    
    private boolean EXT_separate_shader_objects_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glUseShaderProgramEXT");
        this.glUseShaderProgramEXT = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glActiveProgramEXT");
        this.glActiveProgramEXT = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glCreateShaderProgramEXT");
        this.glCreateShaderProgramEXT = functionAddress3;
        return b2 & functionAddress3 != 0L;
    }
    
    private boolean EXT_shader_image_load_store_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glBindImageTextureEXT");
        this.glBindImageTextureEXT = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glMemoryBarrierEXT");
        this.glMemoryBarrierEXT = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean EXT_stencil_clear_tag_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glStencilClearTagEXT");
        this.glStencilClearTagEXT = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean EXT_stencil_two_side_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glActiveStencilFaceEXT");
        this.glActiveStencilFaceEXT = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean EXT_texture_array_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glFramebufferTextureLayerEXT");
        this.glFramebufferTextureLayerEXT = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean EXT_texture_buffer_object_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glTexBufferEXT");
        this.glTexBufferEXT = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean EXT_texture_integer_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glClearColorIiEXT");
        this.glClearColorIiEXT = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glClearColorIuiEXT");
        this.glClearColorIuiEXT = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glTexParameterIivEXT");
        this.glTexParameterIivEXT = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glTexParameterIuivEXT");
        this.glTexParameterIuivEXT = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glGetTexParameterIivEXT");
        this.glGetTexParameterIivEXT = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glGetTexParameterIuivEXT");
        this.glGetTexParameterIuivEXT = functionAddress6;
        return b5 & functionAddress6 != 0L;
    }
    
    private boolean EXT_timer_query_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glGetQueryObjecti64vEXT");
        this.glGetQueryObjecti64vEXT = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glGetQueryObjectui64vEXT");
        this.glGetQueryObjectui64vEXT = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean EXT_transform_feedback_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glBindBufferRangeEXT");
        this.glBindBufferRangeEXT = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glBindBufferOffsetEXT");
        this.glBindBufferOffsetEXT = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glBindBufferBaseEXT");
        this.glBindBufferBaseEXT = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glBeginTransformFeedbackEXT");
        this.glBeginTransformFeedbackEXT = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glEndTransformFeedbackEXT");
        this.glEndTransformFeedbackEXT = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glTransformFeedbackVaryingsEXT");
        this.glTransformFeedbackVaryingsEXT = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glGetTransformFeedbackVaryingEXT");
        this.glGetTransformFeedbackVaryingEXT = functionAddress7;
        return b6 & functionAddress7 != 0L;
    }
    
    private boolean EXT_vertex_attrib_64bit_initNativeFunctionAddresses(final Set<String> supported_extensions) {
        final long functionAddress = GLContext.getFunctionAddress("glVertexAttribL1dEXT");
        this.glVertexAttribL1dEXT = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glVertexAttribL2dEXT");
        this.glVertexAttribL2dEXT = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glVertexAttribL3dEXT");
        this.glVertexAttribL3dEXT = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glVertexAttribL4dEXT");
        this.glVertexAttribL4dEXT = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glVertexAttribL1dvEXT");
        this.glVertexAttribL1dvEXT = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glVertexAttribL2dvEXT");
        this.glVertexAttribL2dvEXT = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glVertexAttribL3dvEXT");
        this.glVertexAttribL3dvEXT = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glVertexAttribL4dvEXT");
        this.glVertexAttribL4dvEXT = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glVertexAttribLPointerEXT");
        this.glVertexAttribLPointerEXT = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glGetVertexAttribLdvEXT");
        this.glGetVertexAttribLdvEXT = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        if (supported_extensions.contains("GL_EXT_direct_state_access")) {
            final long functionAddress11 = GLContext.getFunctionAddress("glVertexArrayVertexAttribLOffsetEXT");
            this.glVertexArrayVertexAttribLOffsetEXT = functionAddress11;
            if (functionAddress11 == 0L) {
                final boolean b11 = false;
                return b10 & b11;
            }
        }
        final boolean b11 = true;
        return b10 & b11;
    }
    
    private boolean EXT_vertex_shader_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glBeginVertexShaderEXT");
        this.glBeginVertexShaderEXT = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glEndVertexShaderEXT");
        this.glEndVertexShaderEXT = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glBindVertexShaderEXT");
        this.glBindVertexShaderEXT = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glGenVertexShadersEXT");
        this.glGenVertexShadersEXT = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glDeleteVertexShaderEXT");
        this.glDeleteVertexShaderEXT = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glShaderOp1EXT");
        this.glShaderOp1EXT = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glShaderOp2EXT");
        this.glShaderOp2EXT = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glShaderOp3EXT");
        this.glShaderOp3EXT = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glSwizzleEXT");
        this.glSwizzleEXT = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glWriteMaskEXT");
        this.glWriteMaskEXT = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glInsertComponentEXT");
        this.glInsertComponentEXT = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glExtractComponentEXT");
        this.glExtractComponentEXT = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glGenSymbolsEXT");
        this.glGenSymbolsEXT = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glSetInvariantEXT");
        this.glSetInvariantEXT = functionAddress14;
        final boolean b14 = b13 & functionAddress14 != 0L;
        final long functionAddress15 = GLContext.getFunctionAddress("glSetLocalConstantEXT");
        this.glSetLocalConstantEXT = functionAddress15;
        final boolean b15 = b14 & functionAddress15 != 0L;
        final long functionAddress16 = GLContext.getFunctionAddress("glVariantbvEXT");
        this.glVariantbvEXT = functionAddress16;
        final boolean b16 = b15 & functionAddress16 != 0L;
        final long functionAddress17 = GLContext.getFunctionAddress("glVariantsvEXT");
        this.glVariantsvEXT = functionAddress17;
        final boolean b17 = b16 & functionAddress17 != 0L;
        final long functionAddress18 = GLContext.getFunctionAddress("glVariantivEXT");
        this.glVariantivEXT = functionAddress18;
        final boolean b18 = b17 & functionAddress18 != 0L;
        final long functionAddress19 = GLContext.getFunctionAddress("glVariantfvEXT");
        this.glVariantfvEXT = functionAddress19;
        final boolean b19 = b18 & functionAddress19 != 0L;
        final long functionAddress20 = GLContext.getFunctionAddress("glVariantdvEXT");
        this.glVariantdvEXT = functionAddress20;
        final boolean b20 = b19 & functionAddress20 != 0L;
        final long functionAddress21 = GLContext.getFunctionAddress("glVariantubvEXT");
        this.glVariantubvEXT = functionAddress21;
        final boolean b21 = b20 & functionAddress21 != 0L;
        final long functionAddress22 = GLContext.getFunctionAddress("glVariantusvEXT");
        this.glVariantusvEXT = functionAddress22;
        final boolean b22 = b21 & functionAddress22 != 0L;
        final long functionAddress23 = GLContext.getFunctionAddress("glVariantuivEXT");
        this.glVariantuivEXT = functionAddress23;
        final boolean b23 = b22 & functionAddress23 != 0L;
        final long functionAddress24 = GLContext.getFunctionAddress("glVariantPointerEXT");
        this.glVariantPointerEXT = functionAddress24;
        final boolean b24 = b23 & functionAddress24 != 0L;
        final long functionAddress25 = GLContext.getFunctionAddress("glEnableVariantClientStateEXT");
        this.glEnableVariantClientStateEXT = functionAddress25;
        final boolean b25 = b24 & functionAddress25 != 0L;
        final long functionAddress26 = GLContext.getFunctionAddress("glDisableVariantClientStateEXT");
        this.glDisableVariantClientStateEXT = functionAddress26;
        final boolean b26 = b25 & functionAddress26 != 0L;
        final long functionAddress27 = GLContext.getFunctionAddress("glBindLightParameterEXT");
        this.glBindLightParameterEXT = functionAddress27;
        final boolean b27 = b26 & functionAddress27 != 0L;
        final long functionAddress28 = GLContext.getFunctionAddress("glBindMaterialParameterEXT");
        this.glBindMaterialParameterEXT = functionAddress28;
        final boolean b28 = b27 & functionAddress28 != 0L;
        final long functionAddress29 = GLContext.getFunctionAddress("glBindTexGenParameterEXT");
        this.glBindTexGenParameterEXT = functionAddress29;
        final boolean b29 = b28 & functionAddress29 != 0L;
        final long functionAddress30 = GLContext.getFunctionAddress("glBindTextureUnitParameterEXT");
        this.glBindTextureUnitParameterEXT = functionAddress30;
        final boolean b30 = b29 & functionAddress30 != 0L;
        final long functionAddress31 = GLContext.getFunctionAddress("glBindParameterEXT");
        this.glBindParameterEXT = functionAddress31;
        final boolean b31 = b30 & functionAddress31 != 0L;
        final long functionAddress32 = GLContext.getFunctionAddress("glIsVariantEnabledEXT");
        this.glIsVariantEnabledEXT = functionAddress32;
        final boolean b32 = b31 & functionAddress32 != 0L;
        final long functionAddress33 = GLContext.getFunctionAddress("glGetVariantBooleanvEXT");
        this.glGetVariantBooleanvEXT = functionAddress33;
        final boolean b33 = b32 & functionAddress33 != 0L;
        final long functionAddress34 = GLContext.getFunctionAddress("glGetVariantIntegervEXT");
        this.glGetVariantIntegervEXT = functionAddress34;
        final boolean b34 = b33 & functionAddress34 != 0L;
        final long functionAddress35 = GLContext.getFunctionAddress("glGetVariantFloatvEXT");
        this.glGetVariantFloatvEXT = functionAddress35;
        final boolean b35 = b34 & functionAddress35 != 0L;
        final long functionAddress36 = GLContext.getFunctionAddress("glGetVariantPointervEXT");
        this.glGetVariantPointervEXT = functionAddress36;
        final boolean b36 = b35 & functionAddress36 != 0L;
        final long functionAddress37 = GLContext.getFunctionAddress("glGetInvariantBooleanvEXT");
        this.glGetInvariantBooleanvEXT = functionAddress37;
        final boolean b37 = b36 & functionAddress37 != 0L;
        final long functionAddress38 = GLContext.getFunctionAddress("glGetInvariantIntegervEXT");
        this.glGetInvariantIntegervEXT = functionAddress38;
        final boolean b38 = b37 & functionAddress38 != 0L;
        final long functionAddress39 = GLContext.getFunctionAddress("glGetInvariantFloatvEXT");
        this.glGetInvariantFloatvEXT = functionAddress39;
        final boolean b39 = b38 & functionAddress39 != 0L;
        final long functionAddress40 = GLContext.getFunctionAddress("glGetLocalConstantBooleanvEXT");
        this.glGetLocalConstantBooleanvEXT = functionAddress40;
        final boolean b40 = b39 & functionAddress40 != 0L;
        final long functionAddress41 = GLContext.getFunctionAddress("glGetLocalConstantIntegervEXT");
        this.glGetLocalConstantIntegervEXT = functionAddress41;
        final boolean b41 = b40 & functionAddress41 != 0L;
        final long functionAddress42 = GLContext.getFunctionAddress("glGetLocalConstantFloatvEXT");
        this.glGetLocalConstantFloatvEXT = functionAddress42;
        return b41 & functionAddress42 != 0L;
    }
    
    private boolean EXT_vertex_weighting_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glVertexWeightfEXT");
        this.glVertexWeightfEXT = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glVertexWeightPointerEXT");
        this.glVertexWeightPointerEXT = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean GL11_initNativeFunctionAddresses(final boolean forwardCompatible) {
        boolean b = false;
        Label_0025: {
            if (!forwardCompatible) {
                final long functionAddress = GLContext.getFunctionAddress("glAccum");
                this.glAccum = functionAddress;
                if (functionAddress == 0L) {
                    b = false;
                    break Label_0025;
                }
            }
            b = true;
        }
        boolean b2 = false;
        Label_0050: {
            if (!forwardCompatible) {
                final long functionAddress2 = GLContext.getFunctionAddress("glAlphaFunc");
                this.glAlphaFunc = functionAddress2;
                if (functionAddress2 == 0L) {
                    b2 = false;
                    break Label_0050;
                }
            }
            b2 = true;
        }
        final boolean b3 = b & b2;
        final long functionAddress3 = GLContext.getFunctionAddress("glClearColor");
        this.glClearColor = functionAddress3;
        final boolean b4 = b3 & functionAddress3 != 0L;
        boolean b5 = false;
        Label_0098: {
            if (!forwardCompatible) {
                final long functionAddress4 = GLContext.getFunctionAddress("glClearAccum");
                this.glClearAccum = functionAddress4;
                if (functionAddress4 == 0L) {
                    b5 = false;
                    break Label_0098;
                }
            }
            b5 = true;
        }
        final boolean b6 = b4 & b5;
        final long functionAddress5 = GLContext.getFunctionAddress("glClear");
        this.glClear = functionAddress5;
        final boolean b7 = b6 & functionAddress5 != 0L;
        boolean b8 = false;
        Label_0146: {
            if (!forwardCompatible) {
                final long functionAddress6 = GLContext.getFunctionAddress("glCallLists");
                this.glCallLists = functionAddress6;
                if (functionAddress6 == 0L) {
                    b8 = false;
                    break Label_0146;
                }
            }
            b8 = true;
        }
        final boolean b9 = b7 & b8;
        boolean b10 = false;
        Label_0172: {
            if (!forwardCompatible) {
                final long functionAddress7 = GLContext.getFunctionAddress("glCallList");
                this.glCallList = functionAddress7;
                if (functionAddress7 == 0L) {
                    b10 = false;
                    break Label_0172;
                }
            }
            b10 = true;
        }
        final boolean b11 = b9 & b10;
        final long functionAddress8 = GLContext.getFunctionAddress("glBlendFunc");
        this.glBlendFunc = functionAddress8;
        final boolean b12 = b11 & functionAddress8 != 0L;
        boolean b13 = false;
        Label_0220: {
            if (!forwardCompatible) {
                final long functionAddress9 = GLContext.getFunctionAddress("glBitmap");
                this.glBitmap = functionAddress9;
                if (functionAddress9 == 0L) {
                    b13 = false;
                    break Label_0220;
                }
            }
            b13 = true;
        }
        final boolean b14 = b12 & b13;
        final long functionAddress10 = GLContext.getFunctionAddress("glBindTexture");
        this.glBindTexture = functionAddress10;
        final boolean b15 = b14 & functionAddress10 != 0L;
        boolean b16 = false;
        Label_0268: {
            if (!forwardCompatible) {
                final long functionAddress11 = GLContext.getFunctionAddress("glPrioritizeTextures");
                this.glPrioritizeTextures = functionAddress11;
                if (functionAddress11 == 0L) {
                    b16 = false;
                    break Label_0268;
                }
            }
            b16 = true;
        }
        final boolean b17 = b15 & b16;
        boolean b18 = false;
        Label_0294: {
            if (!forwardCompatible) {
                final long functionAddress12 = GLContext.getFunctionAddress("glAreTexturesResident");
                this.glAreTexturesResident = functionAddress12;
                if (functionAddress12 == 0L) {
                    b18 = false;
                    break Label_0294;
                }
            }
            b18 = true;
        }
        final boolean b19 = b17 & b18;
        boolean b20 = false;
        Label_0320: {
            if (!forwardCompatible) {
                final long functionAddress13 = GLContext.getFunctionAddress("glBegin");
                this.glBegin = functionAddress13;
                if (functionAddress13 == 0L) {
                    b20 = false;
                    break Label_0320;
                }
            }
            b20 = true;
        }
        final boolean b21 = b19 & b20;
        boolean b22 = false;
        Label_0346: {
            if (!forwardCompatible) {
                final long functionAddress14 = GLContext.getFunctionAddress("glEnd");
                this.glEnd = functionAddress14;
                if (functionAddress14 == 0L) {
                    b22 = false;
                    break Label_0346;
                }
            }
            b22 = true;
        }
        final boolean b23 = b21 & b22;
        final long functionAddress15 = GLContext.getFunctionAddress("glArrayElement");
        this.glArrayElement = functionAddress15;
        final boolean b24 = b23 & functionAddress15 != 0L;
        final long functionAddress16 = GLContext.getFunctionAddress("glClearDepth");
        this.glClearDepth = functionAddress16;
        final boolean b25 = b24 & functionAddress16 != 0L;
        boolean b26 = false;
        Label_0416: {
            if (!forwardCompatible) {
                final long functionAddress17 = GLContext.getFunctionAddress("glDeleteLists");
                this.glDeleteLists = functionAddress17;
                if (functionAddress17 == 0L) {
                    b26 = false;
                    break Label_0416;
                }
            }
            b26 = true;
        }
        final boolean b27 = b25 & b26;
        final long functionAddress18 = GLContext.getFunctionAddress("glDeleteTextures");
        this.glDeleteTextures = functionAddress18;
        final boolean b28 = b27 & functionAddress18 != 0L;
        final long functionAddress19 = GLContext.getFunctionAddress("glCullFace");
        this.glCullFace = functionAddress19;
        final boolean b29 = b28 & functionAddress19 != 0L;
        final long functionAddress20 = GLContext.getFunctionAddress("glCopyTexSubImage2D");
        this.glCopyTexSubImage2D = functionAddress20;
        final boolean b30 = b29 & functionAddress20 != 0L;
        final long functionAddress21 = GLContext.getFunctionAddress("glCopyTexSubImage1D");
        this.glCopyTexSubImage1D = functionAddress21;
        final boolean b31 = b30 & functionAddress21 != 0L;
        final long functionAddress22 = GLContext.getFunctionAddress("glCopyTexImage2D");
        this.glCopyTexImage2D = functionAddress22;
        final boolean b32 = b31 & functionAddress22 != 0L;
        final long functionAddress23 = GLContext.getFunctionAddress("glCopyTexImage1D");
        this.glCopyTexImage1D = functionAddress23;
        final boolean b33 = b32 & functionAddress23 != 0L;
        final long functionAddress24 = GLContext.getFunctionAddress("glCopyPixels");
        this.glCopyPixels = functionAddress24;
        final boolean b34 = b33 & functionAddress24 != 0L;
        boolean b35 = false;
        Label_0596: {
            if (!forwardCompatible) {
                final long functionAddress25 = GLContext.getFunctionAddress("glColorPointer");
                this.glColorPointer = functionAddress25;
                if (functionAddress25 == 0L) {
                    b35 = false;
                    break Label_0596;
                }
            }
            b35 = true;
        }
        final boolean b36 = b34 & b35;
        boolean b37 = false;
        Label_0622: {
            if (!forwardCompatible) {
                final long functionAddress26 = GLContext.getFunctionAddress("glColorMaterial");
                this.glColorMaterial = functionAddress26;
                if (functionAddress26 == 0L) {
                    b37 = false;
                    break Label_0622;
                }
            }
            b37 = true;
        }
        final boolean b38 = b36 & b37;
        final long functionAddress27 = GLContext.getFunctionAddress("glColorMask");
        this.glColorMask = functionAddress27;
        final boolean b39 = b38 & functionAddress27 != 0L;
        boolean b40 = false;
        Label_0670: {
            if (!forwardCompatible) {
                final long functionAddress28 = GLContext.getFunctionAddress("glColor3b");
                this.glColor3b = functionAddress28;
                if (functionAddress28 == 0L) {
                    b40 = false;
                    break Label_0670;
                }
            }
            b40 = true;
        }
        final boolean b41 = b39 & b40;
        boolean b42 = false;
        Label_0696: {
            if (!forwardCompatible) {
                final long functionAddress29 = GLContext.getFunctionAddress("glColor3f");
                this.glColor3f = functionAddress29;
                if (functionAddress29 == 0L) {
                    b42 = false;
                    break Label_0696;
                }
            }
            b42 = true;
        }
        final boolean b43 = b41 & b42;
        boolean b44 = false;
        Label_0722: {
            if (!forwardCompatible) {
                final long functionAddress30 = GLContext.getFunctionAddress("glColor3d");
                this.glColor3d = functionAddress30;
                if (functionAddress30 == 0L) {
                    b44 = false;
                    break Label_0722;
                }
            }
            b44 = true;
        }
        final boolean b45 = b43 & b44;
        boolean b46 = false;
        Label_0748: {
            if (!forwardCompatible) {
                final long functionAddress31 = GLContext.getFunctionAddress("glColor3ub");
                this.glColor3ub = functionAddress31;
                if (functionAddress31 == 0L) {
                    b46 = false;
                    break Label_0748;
                }
            }
            b46 = true;
        }
        final boolean b47 = b45 & b46;
        boolean b48 = false;
        Label_0774: {
            if (!forwardCompatible) {
                final long functionAddress32 = GLContext.getFunctionAddress("glColor4b");
                this.glColor4b = functionAddress32;
                if (functionAddress32 == 0L) {
                    b48 = false;
                    break Label_0774;
                }
            }
            b48 = true;
        }
        final boolean b49 = b47 & b48;
        boolean b50 = false;
        Label_0800: {
            if (!forwardCompatible) {
                final long functionAddress33 = GLContext.getFunctionAddress("glColor4f");
                this.glColor4f = functionAddress33;
                if (functionAddress33 == 0L) {
                    b50 = false;
                    break Label_0800;
                }
            }
            b50 = true;
        }
        final boolean b51 = b49 & b50;
        boolean b52 = false;
        Label_0826: {
            if (!forwardCompatible) {
                final long functionAddress34 = GLContext.getFunctionAddress("glColor4d");
                this.glColor4d = functionAddress34;
                if (functionAddress34 == 0L) {
                    b52 = false;
                    break Label_0826;
                }
            }
            b52 = true;
        }
        final boolean b53 = b51 & b52;
        boolean b54 = false;
        Label_0852: {
            if (!forwardCompatible) {
                final long functionAddress35 = GLContext.getFunctionAddress("glColor4ub");
                this.glColor4ub = functionAddress35;
                if (functionAddress35 == 0L) {
                    b54 = false;
                    break Label_0852;
                }
            }
            b54 = true;
        }
        final boolean b55 = b53 & b54;
        final long functionAddress36 = GLContext.getFunctionAddress("glClipPlane");
        this.glClipPlane = functionAddress36;
        final boolean b56 = b55 & functionAddress36 != 0L;
        final long functionAddress37 = GLContext.getFunctionAddress("glClearStencil");
        this.glClearStencil = functionAddress37;
        final boolean b57 = b56 & functionAddress37 != 0L;
        boolean b58 = false;
        Label_0922: {
            if (!forwardCompatible) {
                final long functionAddress38 = GLContext.getFunctionAddress("glEvalPoint1");
                this.glEvalPoint1 = functionAddress38;
                if (functionAddress38 == 0L) {
                    b58 = false;
                    break Label_0922;
                }
            }
            b58 = true;
        }
        final boolean b59 = b57 & b58;
        boolean b60 = false;
        Label_0948: {
            if (!forwardCompatible) {
                final long functionAddress39 = GLContext.getFunctionAddress("glEvalPoint2");
                this.glEvalPoint2 = functionAddress39;
                if (functionAddress39 == 0L) {
                    b60 = false;
                    break Label_0948;
                }
            }
            b60 = true;
        }
        final boolean b61 = b59 & b60;
        boolean b62 = false;
        Label_0974: {
            if (!forwardCompatible) {
                final long functionAddress40 = GLContext.getFunctionAddress("glEvalMesh1");
                this.glEvalMesh1 = functionAddress40;
                if (functionAddress40 == 0L) {
                    b62 = false;
                    break Label_0974;
                }
            }
            b62 = true;
        }
        final boolean b63 = b61 & b62;
        boolean b64 = false;
        Label_1000: {
            if (!forwardCompatible) {
                final long functionAddress41 = GLContext.getFunctionAddress("glEvalMesh2");
                this.glEvalMesh2 = functionAddress41;
                if (functionAddress41 == 0L) {
                    b64 = false;
                    break Label_1000;
                }
            }
            b64 = true;
        }
        final boolean b65 = b63 & b64;
        boolean b66 = false;
        Label_1026: {
            if (!forwardCompatible) {
                final long functionAddress42 = GLContext.getFunctionAddress("glEvalCoord1f");
                this.glEvalCoord1f = functionAddress42;
                if (functionAddress42 == 0L) {
                    b66 = false;
                    break Label_1026;
                }
            }
            b66 = true;
        }
        final boolean b67 = b65 & b66;
        boolean b68 = false;
        Label_1052: {
            if (!forwardCompatible) {
                final long functionAddress43 = GLContext.getFunctionAddress("glEvalCoord1d");
                this.glEvalCoord1d = functionAddress43;
                if (functionAddress43 == 0L) {
                    b68 = false;
                    break Label_1052;
                }
            }
            b68 = true;
        }
        final boolean b69 = b67 & b68;
        boolean b70 = false;
        Label_1078: {
            if (!forwardCompatible) {
                final long functionAddress44 = GLContext.getFunctionAddress("glEvalCoord2f");
                this.glEvalCoord2f = functionAddress44;
                if (functionAddress44 == 0L) {
                    b70 = false;
                    break Label_1078;
                }
            }
            b70 = true;
        }
        final boolean b71 = b69 & b70;
        boolean b72 = false;
        Label_1104: {
            if (!forwardCompatible) {
                final long functionAddress45 = GLContext.getFunctionAddress("glEvalCoord2d");
                this.glEvalCoord2d = functionAddress45;
                if (functionAddress45 == 0L) {
                    b72 = false;
                    break Label_1104;
                }
            }
            b72 = true;
        }
        final boolean b73 = b71 & b72;
        boolean b74 = false;
        Label_1130: {
            if (!forwardCompatible) {
                final long functionAddress46 = GLContext.getFunctionAddress("glEnableClientState");
                this.glEnableClientState = functionAddress46;
                if (functionAddress46 == 0L) {
                    b74 = false;
                    break Label_1130;
                }
            }
            b74 = true;
        }
        final boolean b75 = b73 & b74;
        boolean b76 = false;
        Label_1156: {
            if (!forwardCompatible) {
                final long functionAddress47 = GLContext.getFunctionAddress("glDisableClientState");
                this.glDisableClientState = functionAddress47;
                if (functionAddress47 == 0L) {
                    b76 = false;
                    break Label_1156;
                }
            }
            b76 = true;
        }
        final boolean b77 = b75 & b76;
        final long functionAddress48 = GLContext.getFunctionAddress("glEnable");
        this.glEnable = functionAddress48;
        final boolean b78 = b77 & functionAddress48 != 0L;
        final long functionAddress49 = GLContext.getFunctionAddress("glDisable");
        this.glDisable = functionAddress49;
        final boolean b79 = b78 & functionAddress49 != 0L;
        boolean b80 = false;
        Label_1226: {
            if (!forwardCompatible) {
                final long functionAddress50 = GLContext.getFunctionAddress("glEdgeFlagPointer");
                this.glEdgeFlagPointer = functionAddress50;
                if (functionAddress50 == 0L) {
                    b80 = false;
                    break Label_1226;
                }
            }
            b80 = true;
        }
        final boolean b81 = b79 & b80;
        boolean b82 = false;
        Label_1252: {
            if (!forwardCompatible) {
                final long functionAddress51 = GLContext.getFunctionAddress("glEdgeFlag");
                this.glEdgeFlag = functionAddress51;
                if (functionAddress51 == 0L) {
                    b82 = false;
                    break Label_1252;
                }
            }
            b82 = true;
        }
        final boolean b83 = b81 & b82;
        boolean b84 = false;
        Label_1278: {
            if (!forwardCompatible) {
                final long functionAddress52 = GLContext.getFunctionAddress("glDrawPixels");
                this.glDrawPixels = functionAddress52;
                if (functionAddress52 == 0L) {
                    b84 = false;
                    break Label_1278;
                }
            }
            b84 = true;
        }
        final boolean b85 = b83 & b84;
        final long functionAddress53 = GLContext.getFunctionAddress("glDrawElements");
        this.glDrawElements = functionAddress53;
        final boolean b86 = b85 & functionAddress53 != 0L;
        final long functionAddress54 = GLContext.getFunctionAddress("glDrawBuffer");
        this.glDrawBuffer = functionAddress54;
        final boolean b87 = b86 & functionAddress54 != 0L;
        final long functionAddress55 = GLContext.getFunctionAddress("glDrawArrays");
        this.glDrawArrays = functionAddress55;
        final boolean b88 = b87 & functionAddress55 != 0L;
        final long functionAddress56 = GLContext.getFunctionAddress("glDepthRange");
        this.glDepthRange = functionAddress56;
        final boolean b89 = b88 & functionAddress56 != 0L;
        final long functionAddress57 = GLContext.getFunctionAddress("glDepthMask");
        this.glDepthMask = functionAddress57;
        final boolean b90 = b89 & functionAddress57 != 0L;
        final long functionAddress58 = GLContext.getFunctionAddress("glDepthFunc");
        this.glDepthFunc = functionAddress58;
        final boolean b91 = b90 & functionAddress58 != 0L;
        boolean b92 = false;
        Label_1436: {
            if (!forwardCompatible) {
                final long functionAddress59 = GLContext.getFunctionAddress("glFeedbackBuffer");
                this.glFeedbackBuffer = functionAddress59;
                if (functionAddress59 == 0L) {
                    b92 = false;
                    break Label_1436;
                }
            }
            b92 = true;
        }
        final boolean b93 = b91 & b92;
        boolean b94 = false;
        Label_1462: {
            if (!forwardCompatible) {
                final long functionAddress60 = GLContext.getFunctionAddress("glGetPixelMapfv");
                this.glGetPixelMapfv = functionAddress60;
                if (functionAddress60 == 0L) {
                    b94 = false;
                    break Label_1462;
                }
            }
            b94 = true;
        }
        final boolean b95 = b93 & b94;
        boolean b96 = false;
        Label_1488: {
            if (!forwardCompatible) {
                final long functionAddress61 = GLContext.getFunctionAddress("glGetPixelMapuiv");
                this.glGetPixelMapuiv = functionAddress61;
                if (functionAddress61 == 0L) {
                    b96 = false;
                    break Label_1488;
                }
            }
            b96 = true;
        }
        final boolean b97 = b95 & b96;
        boolean b98 = false;
        Label_1514: {
            if (!forwardCompatible) {
                final long functionAddress62 = GLContext.getFunctionAddress("glGetPixelMapusv");
                this.glGetPixelMapusv = functionAddress62;
                if (functionAddress62 == 0L) {
                    b98 = false;
                    break Label_1514;
                }
            }
            b98 = true;
        }
        final boolean b99 = b97 & b98;
        boolean b100 = false;
        Label_1540: {
            if (!forwardCompatible) {
                final long functionAddress63 = GLContext.getFunctionAddress("glGetMaterialfv");
                this.glGetMaterialfv = functionAddress63;
                if (functionAddress63 == 0L) {
                    b100 = false;
                    break Label_1540;
                }
            }
            b100 = true;
        }
        final boolean b101 = b99 & b100;
        boolean b102 = false;
        Label_1566: {
            if (!forwardCompatible) {
                final long functionAddress64 = GLContext.getFunctionAddress("glGetMaterialiv");
                this.glGetMaterialiv = functionAddress64;
                if (functionAddress64 == 0L) {
                    b102 = false;
                    break Label_1566;
                }
            }
            b102 = true;
        }
        final boolean b103 = b101 & b102;
        boolean b104 = false;
        Label_1592: {
            if (!forwardCompatible) {
                final long functionAddress65 = GLContext.getFunctionAddress("glGetMapfv");
                this.glGetMapfv = functionAddress65;
                if (functionAddress65 == 0L) {
                    b104 = false;
                    break Label_1592;
                }
            }
            b104 = true;
        }
        final boolean b105 = b103 & b104;
        boolean b106 = false;
        Label_1618: {
            if (!forwardCompatible) {
                final long functionAddress66 = GLContext.getFunctionAddress("glGetMapdv");
                this.glGetMapdv = functionAddress66;
                if (functionAddress66 == 0L) {
                    b106 = false;
                    break Label_1618;
                }
            }
            b106 = true;
        }
        final boolean b107 = b105 & b106;
        boolean b108 = false;
        Label_1644: {
            if (!forwardCompatible) {
                final long functionAddress67 = GLContext.getFunctionAddress("glGetMapiv");
                this.glGetMapiv = functionAddress67;
                if (functionAddress67 == 0L) {
                    b108 = false;
                    break Label_1644;
                }
            }
            b108 = true;
        }
        final boolean b109 = b107 & b108;
        boolean b110 = false;
        Label_1670: {
            if (!forwardCompatible) {
                final long functionAddress68 = GLContext.getFunctionAddress("glGetLightfv");
                this.glGetLightfv = functionAddress68;
                if (functionAddress68 == 0L) {
                    b110 = false;
                    break Label_1670;
                }
            }
            b110 = true;
        }
        final boolean b111 = b109 & b110;
        boolean b112 = false;
        Label_1696: {
            if (!forwardCompatible) {
                final long functionAddress69 = GLContext.getFunctionAddress("glGetLightiv");
                this.glGetLightiv = functionAddress69;
                if (functionAddress69 == 0L) {
                    b112 = false;
                    break Label_1696;
                }
            }
            b112 = true;
        }
        final boolean b113 = b111 & b112;
        final long functionAddress70 = GLContext.getFunctionAddress("glGetError");
        this.glGetError = functionAddress70;
        final boolean b114 = b113 & functionAddress70 != 0L;
        final long functionAddress71 = GLContext.getFunctionAddress("glGetClipPlane");
        this.glGetClipPlane = functionAddress71;
        final boolean b115 = b114 & functionAddress71 != 0L;
        final long functionAddress72 = GLContext.getFunctionAddress("glGetBooleanv");
        this.glGetBooleanv = functionAddress72;
        final boolean b116 = b115 & functionAddress72 != 0L;
        final long functionAddress73 = GLContext.getFunctionAddress("glGetDoublev");
        this.glGetDoublev = functionAddress73;
        final boolean b117 = b116 & functionAddress73 != 0L;
        final long functionAddress74 = GLContext.getFunctionAddress("glGetFloatv");
        this.glGetFloatv = functionAddress74;
        final boolean b118 = b117 & functionAddress74 != 0L;
        final long functionAddress75 = GLContext.getFunctionAddress("glGetIntegerv");
        this.glGetIntegerv = functionAddress75;
        final boolean b119 = b118 & functionAddress75 != 0L;
        final long functionAddress76 = GLContext.getFunctionAddress("glGenTextures");
        this.glGenTextures = functionAddress76;
        final boolean b120 = b119 & functionAddress76 != 0L;
        boolean b121 = false;
        Label_1876: {
            if (!forwardCompatible) {
                final long functionAddress77 = GLContext.getFunctionAddress("glGenLists");
                this.glGenLists = functionAddress77;
                if (functionAddress77 == 0L) {
                    b121 = false;
                    break Label_1876;
                }
            }
            b121 = true;
        }
        final boolean b122 = b120 & b121;
        boolean b123 = false;
        Label_1902: {
            if (!forwardCompatible) {
                final long functionAddress78 = GLContext.getFunctionAddress("glFrustum");
                this.glFrustum = functionAddress78;
                if (functionAddress78 == 0L) {
                    b123 = false;
                    break Label_1902;
                }
            }
            b123 = true;
        }
        final boolean b124 = b122 & b123;
        final long functionAddress79 = GLContext.getFunctionAddress("glFrontFace");
        this.glFrontFace = functionAddress79;
        final boolean b125 = b124 & functionAddress79 != 0L;
        boolean b126 = false;
        Label_1950: {
            if (!forwardCompatible) {
                final long functionAddress80 = GLContext.getFunctionAddress("glFogf");
                this.glFogf = functionAddress80;
                if (functionAddress80 == 0L) {
                    b126 = false;
                    break Label_1950;
                }
            }
            b126 = true;
        }
        final boolean b127 = b125 & b126;
        boolean b128 = false;
        Label_1976: {
            if (!forwardCompatible) {
                final long functionAddress81 = GLContext.getFunctionAddress("glFogi");
                this.glFogi = functionAddress81;
                if (functionAddress81 == 0L) {
                    b128 = false;
                    break Label_1976;
                }
            }
            b128 = true;
        }
        final boolean b129 = b127 & b128;
        boolean b130 = false;
        Label_2002: {
            if (!forwardCompatible) {
                final long functionAddress82 = GLContext.getFunctionAddress("glFogfv");
                this.glFogfv = functionAddress82;
                if (functionAddress82 == 0L) {
                    b130 = false;
                    break Label_2002;
                }
            }
            b130 = true;
        }
        final boolean b131 = b129 & b130;
        boolean b132 = false;
        Label_2028: {
            if (!forwardCompatible) {
                final long functionAddress83 = GLContext.getFunctionAddress("glFogiv");
                this.glFogiv = functionAddress83;
                if (functionAddress83 == 0L) {
                    b132 = false;
                    break Label_2028;
                }
            }
            b132 = true;
        }
        final boolean b133 = b131 & b132;
        final long functionAddress84 = GLContext.getFunctionAddress("glFlush");
        this.glFlush = functionAddress84;
        final boolean b134 = b133 & functionAddress84 != 0L;
        final long functionAddress85 = GLContext.getFunctionAddress("glFinish");
        this.glFinish = functionAddress85;
        final boolean b135 = b134 & functionAddress85 != 0L;
        final long functionAddress86 = GLContext.getFunctionAddress("glGetPointerv");
        this.glGetPointerv = functionAddress86;
        final boolean b136 = b135 & functionAddress86 != 0L;
        final long functionAddress87 = GLContext.getFunctionAddress("glIsEnabled");
        this.glIsEnabled = functionAddress87;
        final boolean b137 = b136 & functionAddress87 != 0L;
        final long functionAddress88 = GLContext.getFunctionAddress("glInterleavedArrays");
        this.glInterleavedArrays = functionAddress88;
        final boolean b138 = b137 & functionAddress88 != 0L;
        boolean b139 = false;
        Label_2164: {
            if (!forwardCompatible) {
                final long functionAddress89 = GLContext.getFunctionAddress("glInitNames");
                this.glInitNames = functionAddress89;
                if (functionAddress89 == 0L) {
                    b139 = false;
                    break Label_2164;
                }
            }
            b139 = true;
        }
        final boolean b140 = b138 & b139;
        final long functionAddress90 = GLContext.getFunctionAddress("glHint");
        this.glHint = functionAddress90;
        final boolean b141 = b140 & functionAddress90 != 0L;
        final long functionAddress91 = GLContext.getFunctionAddress("glGetTexParameterfv");
        this.glGetTexParameterfv = functionAddress91;
        final boolean b142 = b141 & functionAddress91 != 0L;
        final long functionAddress92 = GLContext.getFunctionAddress("glGetTexParameteriv");
        this.glGetTexParameteriv = functionAddress92;
        final boolean b143 = b142 & functionAddress92 != 0L;
        final long functionAddress93 = GLContext.getFunctionAddress("glGetTexLevelParameterfv");
        this.glGetTexLevelParameterfv = functionAddress93;
        final boolean b144 = b143 & functionAddress93 != 0L;
        final long functionAddress94 = GLContext.getFunctionAddress("glGetTexLevelParameteriv");
        this.glGetTexLevelParameteriv = functionAddress94;
        final boolean b145 = b144 & functionAddress94 != 0L;
        final long functionAddress95 = GLContext.getFunctionAddress("glGetTexImage");
        this.glGetTexImage = functionAddress95;
        final boolean b146 = b145 & functionAddress95 != 0L;
        boolean b147 = false;
        Label_2322: {
            if (!forwardCompatible) {
                final long functionAddress96 = GLContext.getFunctionAddress("glGetTexGeniv");
                this.glGetTexGeniv = functionAddress96;
                if (functionAddress96 == 0L) {
                    b147 = false;
                    break Label_2322;
                }
            }
            b147 = true;
        }
        final boolean b148 = b146 & b147;
        boolean b149 = false;
        Label_2348: {
            if (!forwardCompatible) {
                final long functionAddress97 = GLContext.getFunctionAddress("glGetTexGenfv");
                this.glGetTexGenfv = functionAddress97;
                if (functionAddress97 == 0L) {
                    b149 = false;
                    break Label_2348;
                }
            }
            b149 = true;
        }
        final boolean b150 = b148 & b149;
        boolean b151 = false;
        Label_2374: {
            if (!forwardCompatible) {
                final long functionAddress98 = GLContext.getFunctionAddress("glGetTexGendv");
                this.glGetTexGendv = functionAddress98;
                if (functionAddress98 == 0L) {
                    b151 = false;
                    break Label_2374;
                }
            }
            b151 = true;
        }
        final boolean b152 = b150 & b151;
        final long functionAddress99 = GLContext.getFunctionAddress("glGetTexEnviv");
        this.glGetTexEnviv = functionAddress99;
        final boolean b153 = b152 & functionAddress99 != 0L;
        final long functionAddress100 = GLContext.getFunctionAddress("glGetTexEnvfv");
        this.glGetTexEnvfv = functionAddress100;
        final boolean b154 = b153 & functionAddress100 != 0L;
        final long functionAddress101 = GLContext.getFunctionAddress("glGetString");
        this.glGetString = functionAddress101;
        final boolean b155 = b154 & functionAddress101 != 0L;
        boolean b156 = false;
        Label_2466: {
            if (!forwardCompatible) {
                final long functionAddress102 = GLContext.getFunctionAddress("glGetPolygonStipple");
                this.glGetPolygonStipple = functionAddress102;
                if (functionAddress102 == 0L) {
                    b156 = false;
                    break Label_2466;
                }
            }
            b156 = true;
        }
        final boolean b157 = b155 & b156;
        boolean b158 = false;
        Label_2492: {
            if (!forwardCompatible) {
                final long functionAddress103 = GLContext.getFunctionAddress("glIsList");
                this.glIsList = functionAddress103;
                if (functionAddress103 == 0L) {
                    b158 = false;
                    break Label_2492;
                }
            }
            b158 = true;
        }
        final boolean b159 = b157 & b158;
        boolean b160 = false;
        Label_2518: {
            if (!forwardCompatible) {
                final long functionAddress104 = GLContext.getFunctionAddress("glMaterialf");
                this.glMaterialf = functionAddress104;
                if (functionAddress104 == 0L) {
                    b160 = false;
                    break Label_2518;
                }
            }
            b160 = true;
        }
        final boolean b161 = b159 & b160;
        boolean b162 = false;
        Label_2544: {
            if (!forwardCompatible) {
                final long functionAddress105 = GLContext.getFunctionAddress("glMateriali");
                this.glMateriali = functionAddress105;
                if (functionAddress105 == 0L) {
                    b162 = false;
                    break Label_2544;
                }
            }
            b162 = true;
        }
        final boolean b163 = b161 & b162;
        boolean b164 = false;
        Label_2570: {
            if (!forwardCompatible) {
                final long functionAddress106 = GLContext.getFunctionAddress("glMaterialfv");
                this.glMaterialfv = functionAddress106;
                if (functionAddress106 == 0L) {
                    b164 = false;
                    break Label_2570;
                }
            }
            b164 = true;
        }
        final boolean b165 = b163 & b164;
        boolean b166 = false;
        Label_2596: {
            if (!forwardCompatible) {
                final long functionAddress107 = GLContext.getFunctionAddress("glMaterialiv");
                this.glMaterialiv = functionAddress107;
                if (functionAddress107 == 0L) {
                    b166 = false;
                    break Label_2596;
                }
            }
            b166 = true;
        }
        final boolean b167 = b165 & b166;
        boolean b168 = false;
        Label_2622: {
            if (!forwardCompatible) {
                final long functionAddress108 = GLContext.getFunctionAddress("glMapGrid1f");
                this.glMapGrid1f = functionAddress108;
                if (functionAddress108 == 0L) {
                    b168 = false;
                    break Label_2622;
                }
            }
            b168 = true;
        }
        final boolean b169 = b167 & b168;
        boolean b170 = false;
        Label_2648: {
            if (!forwardCompatible) {
                final long functionAddress109 = GLContext.getFunctionAddress("glMapGrid1d");
                this.glMapGrid1d = functionAddress109;
                if (functionAddress109 == 0L) {
                    b170 = false;
                    break Label_2648;
                }
            }
            b170 = true;
        }
        final boolean b171 = b169 & b170;
        boolean b172 = false;
        Label_2674: {
            if (!forwardCompatible) {
                final long functionAddress110 = GLContext.getFunctionAddress("glMapGrid2f");
                this.glMapGrid2f = functionAddress110;
                if (functionAddress110 == 0L) {
                    b172 = false;
                    break Label_2674;
                }
            }
            b172 = true;
        }
        final boolean b173 = b171 & b172;
        boolean b174 = false;
        Label_2700: {
            if (!forwardCompatible) {
                final long functionAddress111 = GLContext.getFunctionAddress("glMapGrid2d");
                this.glMapGrid2d = functionAddress111;
                if (functionAddress111 == 0L) {
                    b174 = false;
                    break Label_2700;
                }
            }
            b174 = true;
        }
        final boolean b175 = b173 & b174;
        boolean b176 = false;
        Label_2726: {
            if (!forwardCompatible) {
                final long functionAddress112 = GLContext.getFunctionAddress("glMap2f");
                this.glMap2f = functionAddress112;
                if (functionAddress112 == 0L) {
                    b176 = false;
                    break Label_2726;
                }
            }
            b176 = true;
        }
        final boolean b177 = b175 & b176;
        boolean b178 = false;
        Label_2752: {
            if (!forwardCompatible) {
                final long functionAddress113 = GLContext.getFunctionAddress("glMap2d");
                this.glMap2d = functionAddress113;
                if (functionAddress113 == 0L) {
                    b178 = false;
                    break Label_2752;
                }
            }
            b178 = true;
        }
        final boolean b179 = b177 & b178;
        boolean b180 = false;
        Label_2778: {
            if (!forwardCompatible) {
                final long functionAddress114 = GLContext.getFunctionAddress("glMap1f");
                this.glMap1f = functionAddress114;
                if (functionAddress114 == 0L) {
                    b180 = false;
                    break Label_2778;
                }
            }
            b180 = true;
        }
        final boolean b181 = b179 & b180;
        boolean b182 = false;
        Label_2804: {
            if (!forwardCompatible) {
                final long functionAddress115 = GLContext.getFunctionAddress("glMap1d");
                this.glMap1d = functionAddress115;
                if (functionAddress115 == 0L) {
                    b182 = false;
                    break Label_2804;
                }
            }
            b182 = true;
        }
        final boolean b183 = b181 & b182;
        final long functionAddress116 = GLContext.getFunctionAddress("glLogicOp");
        this.glLogicOp = functionAddress116;
        final boolean b184 = b183 & functionAddress116 != 0L;
        boolean b185 = false;
        Label_2852: {
            if (!forwardCompatible) {
                final long functionAddress117 = GLContext.getFunctionAddress("glLoadName");
                this.glLoadName = functionAddress117;
                if (functionAddress117 == 0L) {
                    b185 = false;
                    break Label_2852;
                }
            }
            b185 = true;
        }
        final boolean b186 = b184 & b185;
        boolean b187 = false;
        Label_2878: {
            if (!forwardCompatible) {
                final long functionAddress118 = GLContext.getFunctionAddress("glLoadMatrixf");
                this.glLoadMatrixf = functionAddress118;
                if (functionAddress118 == 0L) {
                    b187 = false;
                    break Label_2878;
                }
            }
            b187 = true;
        }
        final boolean b188 = b186 & b187;
        boolean b189 = false;
        Label_2904: {
            if (!forwardCompatible) {
                final long functionAddress119 = GLContext.getFunctionAddress("glLoadMatrixd");
                this.glLoadMatrixd = functionAddress119;
                if (functionAddress119 == 0L) {
                    b189 = false;
                    break Label_2904;
                }
            }
            b189 = true;
        }
        final boolean b190 = b188 & b189;
        boolean b191 = false;
        Label_2930: {
            if (!forwardCompatible) {
                final long functionAddress120 = GLContext.getFunctionAddress("glLoadIdentity");
                this.glLoadIdentity = functionAddress120;
                if (functionAddress120 == 0L) {
                    b191 = false;
                    break Label_2930;
                }
            }
            b191 = true;
        }
        final boolean b192 = b190 & b191;
        boolean b193 = false;
        Label_2956: {
            if (!forwardCompatible) {
                final long functionAddress121 = GLContext.getFunctionAddress("glListBase");
                this.glListBase = functionAddress121;
                if (functionAddress121 == 0L) {
                    b193 = false;
                    break Label_2956;
                }
            }
            b193 = true;
        }
        final boolean b194 = b192 & b193;
        final long functionAddress122 = GLContext.getFunctionAddress("glLineWidth");
        this.glLineWidth = functionAddress122;
        final boolean b195 = b194 & functionAddress122 != 0L;
        boolean b196 = false;
        Label_3004: {
            if (!forwardCompatible) {
                final long functionAddress123 = GLContext.getFunctionAddress("glLineStipple");
                this.glLineStipple = functionAddress123;
                if (functionAddress123 == 0L) {
                    b196 = false;
                    break Label_3004;
                }
            }
            b196 = true;
        }
        final boolean b197 = b195 & b196;
        boolean b198 = false;
        Label_3030: {
            if (!forwardCompatible) {
                final long functionAddress124 = GLContext.getFunctionAddress("glLightModelf");
                this.glLightModelf = functionAddress124;
                if (functionAddress124 == 0L) {
                    b198 = false;
                    break Label_3030;
                }
            }
            b198 = true;
        }
        final boolean b199 = b197 & b198;
        boolean b200 = false;
        Label_3056: {
            if (!forwardCompatible) {
                final long functionAddress125 = GLContext.getFunctionAddress("glLightModeli");
                this.glLightModeli = functionAddress125;
                if (functionAddress125 == 0L) {
                    b200 = false;
                    break Label_3056;
                }
            }
            b200 = true;
        }
        final boolean b201 = b199 & b200;
        boolean b202 = false;
        Label_3082: {
            if (!forwardCompatible) {
                final long functionAddress126 = GLContext.getFunctionAddress("glLightModelfv");
                this.glLightModelfv = functionAddress126;
                if (functionAddress126 == 0L) {
                    b202 = false;
                    break Label_3082;
                }
            }
            b202 = true;
        }
        final boolean b203 = b201 & b202;
        boolean b204 = false;
        Label_3108: {
            if (!forwardCompatible) {
                final long functionAddress127 = GLContext.getFunctionAddress("glLightModeliv");
                this.glLightModeliv = functionAddress127;
                if (functionAddress127 == 0L) {
                    b204 = false;
                    break Label_3108;
                }
            }
            b204 = true;
        }
        final boolean b205 = b203 & b204;
        boolean b206 = false;
        Label_3134: {
            if (!forwardCompatible) {
                final long functionAddress128 = GLContext.getFunctionAddress("glLightf");
                this.glLightf = functionAddress128;
                if (functionAddress128 == 0L) {
                    b206 = false;
                    break Label_3134;
                }
            }
            b206 = true;
        }
        final boolean b207 = b205 & b206;
        boolean b208 = false;
        Label_3160: {
            if (!forwardCompatible) {
                final long functionAddress129 = GLContext.getFunctionAddress("glLighti");
                this.glLighti = functionAddress129;
                if (functionAddress129 == 0L) {
                    b208 = false;
                    break Label_3160;
                }
            }
            b208 = true;
        }
        final boolean b209 = b207 & b208;
        boolean b210 = false;
        Label_3186: {
            if (!forwardCompatible) {
                final long functionAddress130 = GLContext.getFunctionAddress("glLightfv");
                this.glLightfv = functionAddress130;
                if (functionAddress130 == 0L) {
                    b210 = false;
                    break Label_3186;
                }
            }
            b210 = true;
        }
        final boolean b211 = b209 & b210;
        boolean b212 = false;
        Label_3212: {
            if (!forwardCompatible) {
                final long functionAddress131 = GLContext.getFunctionAddress("glLightiv");
                this.glLightiv = functionAddress131;
                if (functionAddress131 == 0L) {
                    b212 = false;
                    break Label_3212;
                }
            }
            b212 = true;
        }
        final boolean b213 = b211 & b212;
        final long functionAddress132 = GLContext.getFunctionAddress("glIsTexture");
        this.glIsTexture = functionAddress132;
        final boolean b214 = b213 & functionAddress132 != 0L;
        boolean b215 = false;
        Label_3260: {
            if (!forwardCompatible) {
                final long functionAddress133 = GLContext.getFunctionAddress("glMatrixMode");
                this.glMatrixMode = functionAddress133;
                if (functionAddress133 == 0L) {
                    b215 = false;
                    break Label_3260;
                }
            }
            b215 = true;
        }
        final boolean b216 = b214 & b215;
        boolean b217 = false;
        Label_3286: {
            if (!forwardCompatible) {
                final long functionAddress134 = GLContext.getFunctionAddress("glPolygonStipple");
                this.glPolygonStipple = functionAddress134;
                if (functionAddress134 == 0L) {
                    b217 = false;
                    break Label_3286;
                }
            }
            b217 = true;
        }
        final boolean b218 = b216 & b217;
        final long functionAddress135 = GLContext.getFunctionAddress("glPolygonOffset");
        this.glPolygonOffset = functionAddress135;
        final boolean b219 = b218 & functionAddress135 != 0L;
        final long functionAddress136 = GLContext.getFunctionAddress("glPolygonMode");
        this.glPolygonMode = functionAddress136;
        final boolean b220 = b219 & functionAddress136 != 0L;
        final long functionAddress137 = GLContext.getFunctionAddress("glPointSize");
        this.glPointSize = functionAddress137;
        final boolean b221 = b220 & functionAddress137 != 0L;
        boolean b222 = false;
        Label_3378: {
            if (!forwardCompatible) {
                final long functionAddress138 = GLContext.getFunctionAddress("glPixelZoom");
                this.glPixelZoom = functionAddress138;
                if (functionAddress138 == 0L) {
                    b222 = false;
                    break Label_3378;
                }
            }
            b222 = true;
        }
        final boolean b223 = b221 & b222;
        boolean b224 = false;
        Label_3404: {
            if (!forwardCompatible) {
                final long functionAddress139 = GLContext.getFunctionAddress("glPixelTransferf");
                this.glPixelTransferf = functionAddress139;
                if (functionAddress139 == 0L) {
                    b224 = false;
                    break Label_3404;
                }
            }
            b224 = true;
        }
        final boolean b225 = b223 & b224;
        boolean b226 = false;
        Label_3430: {
            if (!forwardCompatible) {
                final long functionAddress140 = GLContext.getFunctionAddress("glPixelTransferi");
                this.glPixelTransferi = functionAddress140;
                if (functionAddress140 == 0L) {
                    b226 = false;
                    break Label_3430;
                }
            }
            b226 = true;
        }
        final boolean b227 = b225 & b226;
        final long functionAddress141 = GLContext.getFunctionAddress("glPixelStoref");
        this.glPixelStoref = functionAddress141;
        final boolean b228 = b227 & functionAddress141 != 0L;
        final long functionAddress142 = GLContext.getFunctionAddress("glPixelStorei");
        this.glPixelStorei = functionAddress142;
        final boolean b229 = b228 & functionAddress142 != 0L;
        boolean b230 = false;
        Label_3500: {
            if (!forwardCompatible) {
                final long functionAddress143 = GLContext.getFunctionAddress("glPixelMapfv");
                this.glPixelMapfv = functionAddress143;
                if (functionAddress143 == 0L) {
                    b230 = false;
                    break Label_3500;
                }
            }
            b230 = true;
        }
        final boolean b231 = b229 & b230;
        boolean b232 = false;
        Label_3526: {
            if (!forwardCompatible) {
                final long functionAddress144 = GLContext.getFunctionAddress("glPixelMapuiv");
                this.glPixelMapuiv = functionAddress144;
                if (functionAddress144 == 0L) {
                    b232 = false;
                    break Label_3526;
                }
            }
            b232 = true;
        }
        final boolean b233 = b231 & b232;
        boolean b234 = false;
        Label_3552: {
            if (!forwardCompatible) {
                final long functionAddress145 = GLContext.getFunctionAddress("glPixelMapusv");
                this.glPixelMapusv = functionAddress145;
                if (functionAddress145 == 0L) {
                    b234 = false;
                    break Label_3552;
                }
            }
            b234 = true;
        }
        final boolean b235 = b233 & b234;
        boolean b236 = false;
        Label_3578: {
            if (!forwardCompatible) {
                final long functionAddress146 = GLContext.getFunctionAddress("glPassThrough");
                this.glPassThrough = functionAddress146;
                if (functionAddress146 == 0L) {
                    b236 = false;
                    break Label_3578;
                }
            }
            b236 = true;
        }
        final boolean b237 = b235 & b236;
        boolean b238 = false;
        Label_3604: {
            if (!forwardCompatible) {
                final long functionAddress147 = GLContext.getFunctionAddress("glOrtho");
                this.glOrtho = functionAddress147;
                if (functionAddress147 == 0L) {
                    b238 = false;
                    break Label_3604;
                }
            }
            b238 = true;
        }
        final boolean b239 = b237 & b238;
        boolean b240 = false;
        Label_3630: {
            if (!forwardCompatible) {
                final long functionAddress148 = GLContext.getFunctionAddress("glNormalPointer");
                this.glNormalPointer = functionAddress148;
                if (functionAddress148 == 0L) {
                    b240 = false;
                    break Label_3630;
                }
            }
            b240 = true;
        }
        final boolean b241 = b239 & b240;
        boolean b242 = false;
        Label_3656: {
            if (!forwardCompatible) {
                final long functionAddress149 = GLContext.getFunctionAddress("glNormal3b");
                this.glNormal3b = functionAddress149;
                if (functionAddress149 == 0L) {
                    b242 = false;
                    break Label_3656;
                }
            }
            b242 = true;
        }
        final boolean b243 = b241 & b242;
        boolean b244 = false;
        Label_3682: {
            if (!forwardCompatible) {
                final long functionAddress150 = GLContext.getFunctionAddress("glNormal3f");
                this.glNormal3f = functionAddress150;
                if (functionAddress150 == 0L) {
                    b244 = false;
                    break Label_3682;
                }
            }
            b244 = true;
        }
        final boolean b245 = b243 & b244;
        boolean b246 = false;
        Label_3708: {
            if (!forwardCompatible) {
                final long functionAddress151 = GLContext.getFunctionAddress("glNormal3d");
                this.glNormal3d = functionAddress151;
                if (functionAddress151 == 0L) {
                    b246 = false;
                    break Label_3708;
                }
            }
            b246 = true;
        }
        final boolean b247 = b245 & b246;
        boolean b248 = false;
        Label_3734: {
            if (!forwardCompatible) {
                final long functionAddress152 = GLContext.getFunctionAddress("glNormal3i");
                this.glNormal3i = functionAddress152;
                if (functionAddress152 == 0L) {
                    b248 = false;
                    break Label_3734;
                }
            }
            b248 = true;
        }
        final boolean b249 = b247 & b248;
        boolean b250 = false;
        Label_3760: {
            if (!forwardCompatible) {
                final long functionAddress153 = GLContext.getFunctionAddress("glNewList");
                this.glNewList = functionAddress153;
                if (functionAddress153 == 0L) {
                    b250 = false;
                    break Label_3760;
                }
            }
            b250 = true;
        }
        final boolean b251 = b249 & b250;
        boolean b252 = false;
        Label_3786: {
            if (!forwardCompatible) {
                final long functionAddress154 = GLContext.getFunctionAddress("glEndList");
                this.glEndList = functionAddress154;
                if (functionAddress154 == 0L) {
                    b252 = false;
                    break Label_3786;
                }
            }
            b252 = true;
        }
        final boolean b253 = b251 & b252;
        boolean b254 = false;
        Label_3812: {
            if (!forwardCompatible) {
                final long functionAddress155 = GLContext.getFunctionAddress("glMultMatrixf");
                this.glMultMatrixf = functionAddress155;
                if (functionAddress155 == 0L) {
                    b254 = false;
                    break Label_3812;
                }
            }
            b254 = true;
        }
        final boolean b255 = b253 & b254;
        boolean b256 = false;
        Label_3838: {
            if (!forwardCompatible) {
                final long functionAddress156 = GLContext.getFunctionAddress("glMultMatrixd");
                this.glMultMatrixd = functionAddress156;
                if (functionAddress156 == 0L) {
                    b256 = false;
                    break Label_3838;
                }
            }
            b256 = true;
        }
        final boolean b257 = b255 & b256;
        final long functionAddress157 = GLContext.getFunctionAddress("glShadeModel");
        this.glShadeModel = functionAddress157;
        final boolean b258 = b257 & functionAddress157 != 0L;
        boolean b259 = false;
        Label_3886: {
            if (!forwardCompatible) {
                final long functionAddress158 = GLContext.getFunctionAddress("glSelectBuffer");
                this.glSelectBuffer = functionAddress158;
                if (functionAddress158 == 0L) {
                    b259 = false;
                    break Label_3886;
                }
            }
            b259 = true;
        }
        final boolean b260 = b258 & b259;
        final long functionAddress159 = GLContext.getFunctionAddress("glScissor");
        this.glScissor = functionAddress159;
        final boolean b261 = b260 & functionAddress159 != 0L;
        boolean b262 = false;
        Label_3934: {
            if (!forwardCompatible) {
                final long functionAddress160 = GLContext.getFunctionAddress("glScalef");
                this.glScalef = functionAddress160;
                if (functionAddress160 == 0L) {
                    b262 = false;
                    break Label_3934;
                }
            }
            b262 = true;
        }
        final boolean b263 = b261 & b262;
        boolean b264 = false;
        Label_3960: {
            if (!forwardCompatible) {
                final long functionAddress161 = GLContext.getFunctionAddress("glScaled");
                this.glScaled = functionAddress161;
                if (functionAddress161 == 0L) {
                    b264 = false;
                    break Label_3960;
                }
            }
            b264 = true;
        }
        final boolean b265 = b263 & b264;
        boolean b266 = false;
        Label_3986: {
            if (!forwardCompatible) {
                final long functionAddress162 = GLContext.getFunctionAddress("glRotatef");
                this.glRotatef = functionAddress162;
                if (functionAddress162 == 0L) {
                    b266 = false;
                    break Label_3986;
                }
            }
            b266 = true;
        }
        final boolean b267 = b265 & b266;
        boolean b268 = false;
        Label_4012: {
            if (!forwardCompatible) {
                final long functionAddress163 = GLContext.getFunctionAddress("glRotated");
                this.glRotated = functionAddress163;
                if (functionAddress163 == 0L) {
                    b268 = false;
                    break Label_4012;
                }
            }
            b268 = true;
        }
        final boolean b269 = b267 & b268;
        boolean b270 = false;
        Label_4038: {
            if (!forwardCompatible) {
                final long functionAddress164 = GLContext.getFunctionAddress("glRenderMode");
                this.glRenderMode = functionAddress164;
                if (functionAddress164 == 0L) {
                    b270 = false;
                    break Label_4038;
                }
            }
            b270 = true;
        }
        final boolean b271 = b269 & b270;
        boolean b272 = false;
        Label_4064: {
            if (!forwardCompatible) {
                final long functionAddress165 = GLContext.getFunctionAddress("glRectf");
                this.glRectf = functionAddress165;
                if (functionAddress165 == 0L) {
                    b272 = false;
                    break Label_4064;
                }
            }
            b272 = true;
        }
        final boolean b273 = b271 & b272;
        boolean b274 = false;
        Label_4090: {
            if (!forwardCompatible) {
                final long functionAddress166 = GLContext.getFunctionAddress("glRectd");
                this.glRectd = functionAddress166;
                if (functionAddress166 == 0L) {
                    b274 = false;
                    break Label_4090;
                }
            }
            b274 = true;
        }
        final boolean b275 = b273 & b274;
        boolean b276 = false;
        Label_4116: {
            if (!forwardCompatible) {
                final long functionAddress167 = GLContext.getFunctionAddress("glRecti");
                this.glRecti = functionAddress167;
                if (functionAddress167 == 0L) {
                    b276 = false;
                    break Label_4116;
                }
            }
            b276 = true;
        }
        final boolean b277 = b275 & b276;
        final long functionAddress168 = GLContext.getFunctionAddress("glReadPixels");
        this.glReadPixels = functionAddress168;
        final boolean b278 = b277 & functionAddress168 != 0L;
        final long functionAddress169 = GLContext.getFunctionAddress("glReadBuffer");
        this.glReadBuffer = functionAddress169;
        final boolean b279 = b278 & functionAddress169 != 0L;
        boolean b280 = false;
        Label_4186: {
            if (!forwardCompatible) {
                final long functionAddress170 = GLContext.getFunctionAddress("glRasterPos2f");
                this.glRasterPos2f = functionAddress170;
                if (functionAddress170 == 0L) {
                    b280 = false;
                    break Label_4186;
                }
            }
            b280 = true;
        }
        final boolean b281 = b279 & b280;
        boolean b282 = false;
        Label_4212: {
            if (!forwardCompatible) {
                final long functionAddress171 = GLContext.getFunctionAddress("glRasterPos2d");
                this.glRasterPos2d = functionAddress171;
                if (functionAddress171 == 0L) {
                    b282 = false;
                    break Label_4212;
                }
            }
            b282 = true;
        }
        final boolean b283 = b281 & b282;
        boolean b284 = false;
        Label_4238: {
            if (!forwardCompatible) {
                final long functionAddress172 = GLContext.getFunctionAddress("glRasterPos2i");
                this.glRasterPos2i = functionAddress172;
                if (functionAddress172 == 0L) {
                    b284 = false;
                    break Label_4238;
                }
            }
            b284 = true;
        }
        final boolean b285 = b283 & b284;
        boolean b286 = false;
        Label_4264: {
            if (!forwardCompatible) {
                final long functionAddress173 = GLContext.getFunctionAddress("glRasterPos3f");
                this.glRasterPos3f = functionAddress173;
                if (functionAddress173 == 0L) {
                    b286 = false;
                    break Label_4264;
                }
            }
            b286 = true;
        }
        final boolean b287 = b285 & b286;
        boolean b288 = false;
        Label_4290: {
            if (!forwardCompatible) {
                final long functionAddress174 = GLContext.getFunctionAddress("glRasterPos3d");
                this.glRasterPos3d = functionAddress174;
                if (functionAddress174 == 0L) {
                    b288 = false;
                    break Label_4290;
                }
            }
            b288 = true;
        }
        final boolean b289 = b287 & b288;
        boolean b290 = false;
        Label_4316: {
            if (!forwardCompatible) {
                final long functionAddress175 = GLContext.getFunctionAddress("glRasterPos3i");
                this.glRasterPos3i = functionAddress175;
                if (functionAddress175 == 0L) {
                    b290 = false;
                    break Label_4316;
                }
            }
            b290 = true;
        }
        final boolean b291 = b289 & b290;
        boolean b292 = false;
        Label_4342: {
            if (!forwardCompatible) {
                final long functionAddress176 = GLContext.getFunctionAddress("glRasterPos4f");
                this.glRasterPos4f = functionAddress176;
                if (functionAddress176 == 0L) {
                    b292 = false;
                    break Label_4342;
                }
            }
            b292 = true;
        }
        final boolean b293 = b291 & b292;
        boolean b294 = false;
        Label_4368: {
            if (!forwardCompatible) {
                final long functionAddress177 = GLContext.getFunctionAddress("glRasterPos4d");
                this.glRasterPos4d = functionAddress177;
                if (functionAddress177 == 0L) {
                    b294 = false;
                    break Label_4368;
                }
            }
            b294 = true;
        }
        final boolean b295 = b293 & b294;
        boolean b296 = false;
        Label_4394: {
            if (!forwardCompatible) {
                final long functionAddress178 = GLContext.getFunctionAddress("glRasterPos4i");
                this.glRasterPos4i = functionAddress178;
                if (functionAddress178 == 0L) {
                    b296 = false;
                    break Label_4394;
                }
            }
            b296 = true;
        }
        final boolean b297 = b295 & b296;
        boolean b298 = false;
        Label_4420: {
            if (!forwardCompatible) {
                final long functionAddress179 = GLContext.getFunctionAddress("glPushName");
                this.glPushName = functionAddress179;
                if (functionAddress179 == 0L) {
                    b298 = false;
                    break Label_4420;
                }
            }
            b298 = true;
        }
        final boolean b299 = b297 & b298;
        boolean b300 = false;
        Label_4446: {
            if (!forwardCompatible) {
                final long functionAddress180 = GLContext.getFunctionAddress("glPopName");
                this.glPopName = functionAddress180;
                if (functionAddress180 == 0L) {
                    b300 = false;
                    break Label_4446;
                }
            }
            b300 = true;
        }
        final boolean b301 = b299 & b300;
        boolean b302 = false;
        Label_4472: {
            if (!forwardCompatible) {
                final long functionAddress181 = GLContext.getFunctionAddress("glPushMatrix");
                this.glPushMatrix = functionAddress181;
                if (functionAddress181 == 0L) {
                    b302 = false;
                    break Label_4472;
                }
            }
            b302 = true;
        }
        final boolean b303 = b301 & b302;
        boolean b304 = false;
        Label_4498: {
            if (!forwardCompatible) {
                final long functionAddress182 = GLContext.getFunctionAddress("glPopMatrix");
                this.glPopMatrix = functionAddress182;
                if (functionAddress182 == 0L) {
                    b304 = false;
                    break Label_4498;
                }
            }
            b304 = true;
        }
        final boolean b305 = b303 & b304;
        boolean b306 = false;
        Label_4524: {
            if (!forwardCompatible) {
                final long functionAddress183 = GLContext.getFunctionAddress("glPushClientAttrib");
                this.glPushClientAttrib = functionAddress183;
                if (functionAddress183 == 0L) {
                    b306 = false;
                    break Label_4524;
                }
            }
            b306 = true;
        }
        final boolean b307 = b305 & b306;
        boolean b308 = false;
        Label_4550: {
            if (!forwardCompatible) {
                final long functionAddress184 = GLContext.getFunctionAddress("glPopClientAttrib");
                this.glPopClientAttrib = functionAddress184;
                if (functionAddress184 == 0L) {
                    b308 = false;
                    break Label_4550;
                }
            }
            b308 = true;
        }
        final boolean b309 = b307 & b308;
        boolean b310 = false;
        Label_4576: {
            if (!forwardCompatible) {
                final long functionAddress185 = GLContext.getFunctionAddress("glPushAttrib");
                this.glPushAttrib = functionAddress185;
                if (functionAddress185 == 0L) {
                    b310 = false;
                    break Label_4576;
                }
            }
            b310 = true;
        }
        final boolean b311 = b309 & b310;
        boolean b312 = false;
        Label_4602: {
            if (!forwardCompatible) {
                final long functionAddress186 = GLContext.getFunctionAddress("glPopAttrib");
                this.glPopAttrib = functionAddress186;
                if (functionAddress186 == 0L) {
                    b312 = false;
                    break Label_4602;
                }
            }
            b312 = true;
        }
        final boolean b313 = b311 & b312;
        final long functionAddress187 = GLContext.getFunctionAddress("glStencilFunc");
        this.glStencilFunc = functionAddress187;
        final boolean b314 = b313 & functionAddress187 != 0L;
        boolean b315 = false;
        Label_4650: {
            if (!forwardCompatible) {
                final long functionAddress188 = GLContext.getFunctionAddress("glVertexPointer");
                this.glVertexPointer = functionAddress188;
                if (functionAddress188 == 0L) {
                    b315 = false;
                    break Label_4650;
                }
            }
            b315 = true;
        }
        final boolean b316 = b314 & b315;
        boolean b317 = false;
        Label_4676: {
            if (!forwardCompatible) {
                final long functionAddress189 = GLContext.getFunctionAddress("glVertex2f");
                this.glVertex2f = functionAddress189;
                if (functionAddress189 == 0L) {
                    b317 = false;
                    break Label_4676;
                }
            }
            b317 = true;
        }
        final boolean b318 = b316 & b317;
        boolean b319 = false;
        Label_4702: {
            if (!forwardCompatible) {
                final long functionAddress190 = GLContext.getFunctionAddress("glVertex2d");
                this.glVertex2d = functionAddress190;
                if (functionAddress190 == 0L) {
                    b319 = false;
                    break Label_4702;
                }
            }
            b319 = true;
        }
        final boolean b320 = b318 & b319;
        boolean b321 = false;
        Label_4728: {
            if (!forwardCompatible) {
                final long functionAddress191 = GLContext.getFunctionAddress("glVertex2i");
                this.glVertex2i = functionAddress191;
                if (functionAddress191 == 0L) {
                    b321 = false;
                    break Label_4728;
                }
            }
            b321 = true;
        }
        final boolean b322 = b320 & b321;
        boolean b323 = false;
        Label_4754: {
            if (!forwardCompatible) {
                final long functionAddress192 = GLContext.getFunctionAddress("glVertex3f");
                this.glVertex3f = functionAddress192;
                if (functionAddress192 == 0L) {
                    b323 = false;
                    break Label_4754;
                }
            }
            b323 = true;
        }
        final boolean b324 = b322 & b323;
        boolean b325 = false;
        Label_4780: {
            if (!forwardCompatible) {
                final long functionAddress193 = GLContext.getFunctionAddress("glVertex3d");
                this.glVertex3d = functionAddress193;
                if (functionAddress193 == 0L) {
                    b325 = false;
                    break Label_4780;
                }
            }
            b325 = true;
        }
        final boolean b326 = b324 & b325;
        boolean b327 = false;
        Label_4806: {
            if (!forwardCompatible) {
                final long functionAddress194 = GLContext.getFunctionAddress("glVertex3i");
                this.glVertex3i = functionAddress194;
                if (functionAddress194 == 0L) {
                    b327 = false;
                    break Label_4806;
                }
            }
            b327 = true;
        }
        final boolean b328 = b326 & b327;
        boolean b329 = false;
        Label_4832: {
            if (!forwardCompatible) {
                final long functionAddress195 = GLContext.getFunctionAddress("glVertex4f");
                this.glVertex4f = functionAddress195;
                if (functionAddress195 == 0L) {
                    b329 = false;
                    break Label_4832;
                }
            }
            b329 = true;
        }
        final boolean b330 = b328 & b329;
        boolean b331 = false;
        Label_4858: {
            if (!forwardCompatible) {
                final long functionAddress196 = GLContext.getFunctionAddress("glVertex4d");
                this.glVertex4d = functionAddress196;
                if (functionAddress196 == 0L) {
                    b331 = false;
                    break Label_4858;
                }
            }
            b331 = true;
        }
        final boolean b332 = b330 & b331;
        boolean b333 = false;
        Label_4884: {
            if (!forwardCompatible) {
                final long functionAddress197 = GLContext.getFunctionAddress("glVertex4i");
                this.glVertex4i = functionAddress197;
                if (functionAddress197 == 0L) {
                    b333 = false;
                    break Label_4884;
                }
            }
            b333 = true;
        }
        final boolean b334 = b332 & b333;
        boolean b335 = false;
        Label_4910: {
            if (!forwardCompatible) {
                final long functionAddress198 = GLContext.getFunctionAddress("glTranslatef");
                this.glTranslatef = functionAddress198;
                if (functionAddress198 == 0L) {
                    b335 = false;
                    break Label_4910;
                }
            }
            b335 = true;
        }
        final boolean b336 = b334 & b335;
        boolean b337 = false;
        Label_4936: {
            if (!forwardCompatible) {
                final long functionAddress199 = GLContext.getFunctionAddress("glTranslated");
                this.glTranslated = functionAddress199;
                if (functionAddress199 == 0L) {
                    b337 = false;
                    break Label_4936;
                }
            }
            b337 = true;
        }
        final boolean b338 = b336 & b337;
        final long functionAddress200 = GLContext.getFunctionAddress("glTexImage1D");
        this.glTexImage1D = functionAddress200;
        final boolean b339 = b338 & functionAddress200 != 0L;
        final long functionAddress201 = GLContext.getFunctionAddress("glTexImage2D");
        this.glTexImage2D = functionAddress201;
        final boolean b340 = b339 & functionAddress201 != 0L;
        final long functionAddress202 = GLContext.getFunctionAddress("glTexSubImage1D");
        this.glTexSubImage1D = functionAddress202;
        final boolean b341 = b340 & functionAddress202 != 0L;
        final long functionAddress203 = GLContext.getFunctionAddress("glTexSubImage2D");
        this.glTexSubImage2D = functionAddress203;
        final boolean b342 = b341 & functionAddress203 != 0L;
        final long functionAddress204 = GLContext.getFunctionAddress("glTexParameterf");
        this.glTexParameterf = functionAddress204;
        final boolean b343 = b342 & functionAddress204 != 0L;
        final long functionAddress205 = GLContext.getFunctionAddress("glTexParameteri");
        this.glTexParameteri = functionAddress205;
        final boolean b344 = b343 & functionAddress205 != 0L;
        final long functionAddress206 = GLContext.getFunctionAddress("glTexParameterfv");
        this.glTexParameterfv = functionAddress206;
        final boolean b345 = b344 & functionAddress206 != 0L;
        final long functionAddress207 = GLContext.getFunctionAddress("glTexParameteriv");
        this.glTexParameteriv = functionAddress207;
        final boolean b346 = b345 & functionAddress207 != 0L;
        boolean b347 = false;
        Label_5138: {
            if (!forwardCompatible) {
                final long functionAddress208 = GLContext.getFunctionAddress("glTexGenf");
                this.glTexGenf = functionAddress208;
                if (functionAddress208 == 0L) {
                    b347 = false;
                    break Label_5138;
                }
            }
            b347 = true;
        }
        final boolean b348 = b346 & b347;
        boolean b349 = false;
        Label_5164: {
            if (!forwardCompatible) {
                final long functionAddress209 = GLContext.getFunctionAddress("glTexGend");
                this.glTexGend = functionAddress209;
                if (functionAddress209 == 0L) {
                    b349 = false;
                    break Label_5164;
                }
            }
            b349 = true;
        }
        final boolean b350 = b348 & b349;
        boolean b351 = false;
        Label_5190: {
            if (!forwardCompatible) {
                final long functionAddress210 = GLContext.getFunctionAddress("glTexGenfv");
                this.glTexGenfv = functionAddress210;
                if (functionAddress210 == 0L) {
                    b351 = false;
                    break Label_5190;
                }
            }
            b351 = true;
        }
        final boolean b352 = b350 & b351;
        boolean b353 = false;
        Label_5216: {
            if (!forwardCompatible) {
                final long functionAddress211 = GLContext.getFunctionAddress("glTexGendv");
                this.glTexGendv = functionAddress211;
                if (functionAddress211 == 0L) {
                    b353 = false;
                    break Label_5216;
                }
            }
            b353 = true;
        }
        final boolean b354 = b352 & b353;
        boolean b355 = false;
        Label_5242: {
            if (!forwardCompatible) {
                final long functionAddress212 = GLContext.getFunctionAddress("glTexGeni");
                this.glTexGeni = functionAddress212;
                if (functionAddress212 == 0L) {
                    b355 = false;
                    break Label_5242;
                }
            }
            b355 = true;
        }
        final boolean b356 = b354 & b355;
        boolean b357 = false;
        Label_5268: {
            if (!forwardCompatible) {
                final long functionAddress213 = GLContext.getFunctionAddress("glTexGeniv");
                this.glTexGeniv = functionAddress213;
                if (functionAddress213 == 0L) {
                    b357 = false;
                    break Label_5268;
                }
            }
            b357 = true;
        }
        final boolean b358 = b356 & b357;
        final long functionAddress214 = GLContext.getFunctionAddress("glTexEnvf");
        this.glTexEnvf = functionAddress214;
        final boolean b359 = b358 & functionAddress214 != 0L;
        final long functionAddress215 = GLContext.getFunctionAddress("glTexEnvi");
        this.glTexEnvi = functionAddress215;
        final boolean b360 = b359 & functionAddress215 != 0L;
        final long functionAddress216 = GLContext.getFunctionAddress("glTexEnvfv");
        this.glTexEnvfv = functionAddress216;
        final boolean b361 = b360 & functionAddress216 != 0L;
        final long functionAddress217 = GLContext.getFunctionAddress("glTexEnviv");
        this.glTexEnviv = functionAddress217;
        final boolean b362 = b361 & functionAddress217 != 0L;
        boolean b363 = false;
        Label_5382: {
            if (!forwardCompatible) {
                final long functionAddress218 = GLContext.getFunctionAddress("glTexCoordPointer");
                this.glTexCoordPointer = functionAddress218;
                if (functionAddress218 == 0L) {
                    b363 = false;
                    break Label_5382;
                }
            }
            b363 = true;
        }
        final boolean b364 = b362 & b363;
        boolean b365 = false;
        Label_5408: {
            if (!forwardCompatible) {
                final long functionAddress219 = GLContext.getFunctionAddress("glTexCoord1f");
                this.glTexCoord1f = functionAddress219;
                if (functionAddress219 == 0L) {
                    b365 = false;
                    break Label_5408;
                }
            }
            b365 = true;
        }
        final boolean b366 = b364 & b365;
        boolean b367 = false;
        Label_5434: {
            if (!forwardCompatible) {
                final long functionAddress220 = GLContext.getFunctionAddress("glTexCoord1d");
                this.glTexCoord1d = functionAddress220;
                if (functionAddress220 == 0L) {
                    b367 = false;
                    break Label_5434;
                }
            }
            b367 = true;
        }
        final boolean b368 = b366 & b367;
        boolean b369 = false;
        Label_5460: {
            if (!forwardCompatible) {
                final long functionAddress221 = GLContext.getFunctionAddress("glTexCoord2f");
                this.glTexCoord2f = functionAddress221;
                if (functionAddress221 == 0L) {
                    b369 = false;
                    break Label_5460;
                }
            }
            b369 = true;
        }
        final boolean b370 = b368 & b369;
        boolean b371 = false;
        Label_5486: {
            if (!forwardCompatible) {
                final long functionAddress222 = GLContext.getFunctionAddress("glTexCoord2d");
                this.glTexCoord2d = functionAddress222;
                if (functionAddress222 == 0L) {
                    b371 = false;
                    break Label_5486;
                }
            }
            b371 = true;
        }
        final boolean b372 = b370 & b371;
        boolean b373 = false;
        Label_5512: {
            if (!forwardCompatible) {
                final long functionAddress223 = GLContext.getFunctionAddress("glTexCoord3f");
                this.glTexCoord3f = functionAddress223;
                if (functionAddress223 == 0L) {
                    b373 = false;
                    break Label_5512;
                }
            }
            b373 = true;
        }
        final boolean b374 = b372 & b373;
        boolean b375 = false;
        Label_5538: {
            if (!forwardCompatible) {
                final long functionAddress224 = GLContext.getFunctionAddress("glTexCoord3d");
                this.glTexCoord3d = functionAddress224;
                if (functionAddress224 == 0L) {
                    b375 = false;
                    break Label_5538;
                }
            }
            b375 = true;
        }
        final boolean b376 = b374 & b375;
        boolean b377 = false;
        Label_5564: {
            if (!forwardCompatible) {
                final long functionAddress225 = GLContext.getFunctionAddress("glTexCoord4f");
                this.glTexCoord4f = functionAddress225;
                if (functionAddress225 == 0L) {
                    b377 = false;
                    break Label_5564;
                }
            }
            b377 = true;
        }
        final boolean b378 = b376 & b377;
        boolean b379 = false;
        Label_5590: {
            if (!forwardCompatible) {
                final long functionAddress226 = GLContext.getFunctionAddress("glTexCoord4d");
                this.glTexCoord4d = functionAddress226;
                if (functionAddress226 == 0L) {
                    b379 = false;
                    break Label_5590;
                }
            }
            b379 = true;
        }
        final boolean b380 = b378 & b379;
        final long functionAddress227 = GLContext.getFunctionAddress("glStencilOp");
        this.glStencilOp = functionAddress227;
        final boolean b381 = b380 & functionAddress227 != 0L;
        final long functionAddress228 = GLContext.getFunctionAddress("glStencilMask");
        this.glStencilMask = functionAddress228;
        final boolean b382 = b381 & functionAddress228 != 0L;
        final long functionAddress229 = GLContext.getFunctionAddress("glViewport");
        this.glViewport = functionAddress229;
        return b382 & functionAddress229 != 0L;
    }
    
    private boolean GL12_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glDrawRangeElements");
        this.glDrawRangeElements = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glTexImage3D");
        this.glTexImage3D = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glTexSubImage3D");
        this.glTexSubImage3D = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glCopyTexSubImage3D");
        this.glCopyTexSubImage3D = functionAddress4;
        return b3 & functionAddress4 != 0L;
    }
    
    private boolean GL13_initNativeFunctionAddresses(final boolean forwardCompatible) {
        final long functionAddress = GLContext.getFunctionAddress("glActiveTexture");
        this.glActiveTexture = functionAddress;
        final boolean b = functionAddress != 0L;
        boolean b2 = false;
        Label_0046: {
            if (!forwardCompatible) {
                final long functionAddress2 = GLContext.getFunctionAddress("glClientActiveTexture");
                this.glClientActiveTexture = functionAddress2;
                if (functionAddress2 == 0L) {
                    b2 = false;
                    break Label_0046;
                }
            }
            b2 = true;
        }
        final boolean b3 = b & b2;
        final long functionAddress3 = GLContext.getFunctionAddress("glCompressedTexImage1D");
        this.glCompressedTexImage1D = functionAddress3;
        final boolean b4 = b3 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glCompressedTexImage2D");
        this.glCompressedTexImage2D = functionAddress4;
        final boolean b5 = b4 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glCompressedTexImage3D");
        this.glCompressedTexImage3D = functionAddress5;
        final boolean b6 = b5 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glCompressedTexSubImage1D");
        this.glCompressedTexSubImage1D = functionAddress6;
        final boolean b7 = b6 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glCompressedTexSubImage2D");
        this.glCompressedTexSubImage2D = functionAddress7;
        final boolean b8 = b7 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glCompressedTexSubImage3D");
        this.glCompressedTexSubImage3D = functionAddress8;
        final boolean b9 = b8 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glGetCompressedTexImage");
        this.glGetCompressedTexImage = functionAddress9;
        final boolean b10 = b9 & functionAddress9 != 0L;
        boolean b11 = false;
        Label_0226: {
            if (!forwardCompatible) {
                final long functionAddress10 = GLContext.getFunctionAddress("glMultiTexCoord1f");
                this.glMultiTexCoord1f = functionAddress10;
                if (functionAddress10 == 0L) {
                    b11 = false;
                    break Label_0226;
                }
            }
            b11 = true;
        }
        final boolean b12 = b10 & b11;
        boolean b13 = false;
        Label_0252: {
            if (!forwardCompatible) {
                final long functionAddress11 = GLContext.getFunctionAddress("glMultiTexCoord1d");
                this.glMultiTexCoord1d = functionAddress11;
                if (functionAddress11 == 0L) {
                    b13 = false;
                    break Label_0252;
                }
            }
            b13 = true;
        }
        final boolean b14 = b12 & b13;
        boolean b15 = false;
        Label_0278: {
            if (!forwardCompatible) {
                final long functionAddress12 = GLContext.getFunctionAddress("glMultiTexCoord2f");
                this.glMultiTexCoord2f = functionAddress12;
                if (functionAddress12 == 0L) {
                    b15 = false;
                    break Label_0278;
                }
            }
            b15 = true;
        }
        final boolean b16 = b14 & b15;
        boolean b17 = false;
        Label_0304: {
            if (!forwardCompatible) {
                final long functionAddress13 = GLContext.getFunctionAddress("glMultiTexCoord2d");
                this.glMultiTexCoord2d = functionAddress13;
                if (functionAddress13 == 0L) {
                    b17 = false;
                    break Label_0304;
                }
            }
            b17 = true;
        }
        final boolean b18 = b16 & b17;
        boolean b19 = false;
        Label_0330: {
            if (!forwardCompatible) {
                final long functionAddress14 = GLContext.getFunctionAddress("glMultiTexCoord3f");
                this.glMultiTexCoord3f = functionAddress14;
                if (functionAddress14 == 0L) {
                    b19 = false;
                    break Label_0330;
                }
            }
            b19 = true;
        }
        final boolean b20 = b18 & b19;
        boolean b21 = false;
        Label_0356: {
            if (!forwardCompatible) {
                final long functionAddress15 = GLContext.getFunctionAddress("glMultiTexCoord3d");
                this.glMultiTexCoord3d = functionAddress15;
                if (functionAddress15 == 0L) {
                    b21 = false;
                    break Label_0356;
                }
            }
            b21 = true;
        }
        final boolean b22 = b20 & b21;
        boolean b23 = false;
        Label_0382: {
            if (!forwardCompatible) {
                final long functionAddress16 = GLContext.getFunctionAddress("glMultiTexCoord4f");
                this.glMultiTexCoord4f = functionAddress16;
                if (functionAddress16 == 0L) {
                    b23 = false;
                    break Label_0382;
                }
            }
            b23 = true;
        }
        final boolean b24 = b22 & b23;
        boolean b25 = false;
        Label_0408: {
            if (!forwardCompatible) {
                final long functionAddress17 = GLContext.getFunctionAddress("glMultiTexCoord4d");
                this.glMultiTexCoord4d = functionAddress17;
                if (functionAddress17 == 0L) {
                    b25 = false;
                    break Label_0408;
                }
            }
            b25 = true;
        }
        final boolean b26 = b24 & b25;
        boolean b27 = false;
        Label_0434: {
            if (!forwardCompatible) {
                final long functionAddress18 = GLContext.getFunctionAddress("glLoadTransposeMatrixf");
                this.glLoadTransposeMatrixf = functionAddress18;
                if (functionAddress18 == 0L) {
                    b27 = false;
                    break Label_0434;
                }
            }
            b27 = true;
        }
        final boolean b28 = b26 & b27;
        boolean b29 = false;
        Label_0460: {
            if (!forwardCompatible) {
                final long functionAddress19 = GLContext.getFunctionAddress("glLoadTransposeMatrixd");
                this.glLoadTransposeMatrixd = functionAddress19;
                if (functionAddress19 == 0L) {
                    b29 = false;
                    break Label_0460;
                }
            }
            b29 = true;
        }
        final boolean b30 = b28 & b29;
        boolean b31 = false;
        Label_0486: {
            if (!forwardCompatible) {
                final long functionAddress20 = GLContext.getFunctionAddress("glMultTransposeMatrixf");
                this.glMultTransposeMatrixf = functionAddress20;
                if (functionAddress20 == 0L) {
                    b31 = false;
                    break Label_0486;
                }
            }
            b31 = true;
        }
        final boolean b32 = b30 & b31;
        boolean b33 = false;
        Label_0512: {
            if (!forwardCompatible) {
                final long functionAddress21 = GLContext.getFunctionAddress("glMultTransposeMatrixd");
                this.glMultTransposeMatrixd = functionAddress21;
                if (functionAddress21 == 0L) {
                    b33 = false;
                    break Label_0512;
                }
            }
            b33 = true;
        }
        final boolean b34 = b32 & b33;
        final long functionAddress22 = GLContext.getFunctionAddress("glSampleCoverage");
        this.glSampleCoverage = functionAddress22;
        return b34 & functionAddress22 != 0L;
    }
    
    private boolean GL14_initNativeFunctionAddresses(final boolean forwardCompatible) {
        final long functionAddress = GLContext.getFunctionAddress("glBlendEquation");
        this.glBlendEquation = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glBlendColor");
        this.glBlendColor = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        boolean b3 = false;
        Label_0068: {
            if (!forwardCompatible) {
                final long functionAddress3 = GLContext.getFunctionAddress("glFogCoordf");
                this.glFogCoordf = functionAddress3;
                if (functionAddress3 == 0L) {
                    b3 = false;
                    break Label_0068;
                }
            }
            b3 = true;
        }
        final boolean b4 = b2 & b3;
        boolean b5 = false;
        Label_0094: {
            if (!forwardCompatible) {
                final long functionAddress4 = GLContext.getFunctionAddress("glFogCoordd");
                this.glFogCoordd = functionAddress4;
                if (functionAddress4 == 0L) {
                    b5 = false;
                    break Label_0094;
                }
            }
            b5 = true;
        }
        final boolean b6 = b4 & b5;
        boolean b7 = false;
        Label_0120: {
            if (!forwardCompatible) {
                final long functionAddress5 = GLContext.getFunctionAddress("glFogCoordPointer");
                this.glFogCoordPointer = functionAddress5;
                if (functionAddress5 == 0L) {
                    b7 = false;
                    break Label_0120;
                }
            }
            b7 = true;
        }
        final boolean b8 = b6 & b7;
        final long functionAddress6 = GLContext.getFunctionAddress("glMultiDrawArrays");
        this.glMultiDrawArrays = functionAddress6;
        final boolean b9 = b8 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glPointParameteri");
        this.glPointParameteri = functionAddress7;
        final boolean b10 = b9 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glPointParameterf");
        this.glPointParameterf = functionAddress8;
        final boolean b11 = b10 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glPointParameteriv");
        this.glPointParameteriv = functionAddress9;
        final boolean b12 = b11 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glPointParameterfv");
        this.glPointParameterfv = functionAddress10;
        final boolean b13 = b12 & functionAddress10 != 0L;
        boolean b14 = false;
        Label_0256: {
            if (!forwardCompatible) {
                final long functionAddress11 = GLContext.getFunctionAddress("glSecondaryColor3b");
                this.glSecondaryColor3b = functionAddress11;
                if (functionAddress11 == 0L) {
                    b14 = false;
                    break Label_0256;
                }
            }
            b14 = true;
        }
        final boolean b15 = b13 & b14;
        boolean b16 = false;
        Label_0282: {
            if (!forwardCompatible) {
                final long functionAddress12 = GLContext.getFunctionAddress("glSecondaryColor3f");
                this.glSecondaryColor3f = functionAddress12;
                if (functionAddress12 == 0L) {
                    b16 = false;
                    break Label_0282;
                }
            }
            b16 = true;
        }
        final boolean b17 = b15 & b16;
        boolean b18 = false;
        Label_0308: {
            if (!forwardCompatible) {
                final long functionAddress13 = GLContext.getFunctionAddress("glSecondaryColor3d");
                this.glSecondaryColor3d = functionAddress13;
                if (functionAddress13 == 0L) {
                    b18 = false;
                    break Label_0308;
                }
            }
            b18 = true;
        }
        final boolean b19 = b17 & b18;
        boolean b20 = false;
        Label_0334: {
            if (!forwardCompatible) {
                final long functionAddress14 = GLContext.getFunctionAddress("glSecondaryColor3ub");
                this.glSecondaryColor3ub = functionAddress14;
                if (functionAddress14 == 0L) {
                    b20 = false;
                    break Label_0334;
                }
            }
            b20 = true;
        }
        final boolean b21 = b19 & b20;
        boolean b22 = false;
        Label_0360: {
            if (!forwardCompatible) {
                final long functionAddress15 = GLContext.getFunctionAddress("glSecondaryColorPointer");
                this.glSecondaryColorPointer = functionAddress15;
                if (functionAddress15 == 0L) {
                    b22 = false;
                    break Label_0360;
                }
            }
            b22 = true;
        }
        final boolean b23 = b21 & b22;
        final long functionAddress16 = GLContext.getFunctionAddress("glBlendFuncSeparate");
        this.glBlendFuncSeparate = functionAddress16;
        final boolean b24 = b23 & functionAddress16 != 0L;
        boolean b25 = false;
        Label_0408: {
            if (!forwardCompatible) {
                final long functionAddress17 = GLContext.getFunctionAddress("glWindowPos2f");
                this.glWindowPos2f = functionAddress17;
                if (functionAddress17 == 0L) {
                    b25 = false;
                    break Label_0408;
                }
            }
            b25 = true;
        }
        final boolean b26 = b24 & b25;
        boolean b27 = false;
        Label_0434: {
            if (!forwardCompatible) {
                final long functionAddress18 = GLContext.getFunctionAddress("glWindowPos2d");
                this.glWindowPos2d = functionAddress18;
                if (functionAddress18 == 0L) {
                    b27 = false;
                    break Label_0434;
                }
            }
            b27 = true;
        }
        final boolean b28 = b26 & b27;
        boolean b29 = false;
        Label_0460: {
            if (!forwardCompatible) {
                final long functionAddress19 = GLContext.getFunctionAddress("glWindowPos2i");
                this.glWindowPos2i = functionAddress19;
                if (functionAddress19 == 0L) {
                    b29 = false;
                    break Label_0460;
                }
            }
            b29 = true;
        }
        final boolean b30 = b28 & b29;
        boolean b31 = false;
        Label_0486: {
            if (!forwardCompatible) {
                final long functionAddress20 = GLContext.getFunctionAddress("glWindowPos3f");
                this.glWindowPos3f = functionAddress20;
                if (functionAddress20 == 0L) {
                    b31 = false;
                    break Label_0486;
                }
            }
            b31 = true;
        }
        final boolean b32 = b30 & b31;
        boolean b33 = false;
        Label_0512: {
            if (!forwardCompatible) {
                final long functionAddress21 = GLContext.getFunctionAddress("glWindowPos3d");
                this.glWindowPos3d = functionAddress21;
                if (functionAddress21 == 0L) {
                    b33 = false;
                    break Label_0512;
                }
            }
            b33 = true;
        }
        final boolean b34 = b32 & b33;
        if (!forwardCompatible) {
            final long functionAddress22 = GLContext.getFunctionAddress("glWindowPos3i");
            this.glWindowPos3i = functionAddress22;
            if (functionAddress22 == 0L) {
                final boolean b35 = false;
                return b34 & b35;
            }
        }
        final boolean b35 = true;
        return b34 & b35;
    }
    
    private boolean GL15_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glBindBuffer");
        this.glBindBuffer = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glDeleteBuffers");
        this.glDeleteBuffers = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glGenBuffers");
        this.glGenBuffers = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glIsBuffer");
        this.glIsBuffer = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glBufferData");
        this.glBufferData = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glBufferSubData");
        this.glBufferSubData = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glGetBufferSubData");
        this.glGetBufferSubData = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glMapBuffer");
        this.glMapBuffer = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glUnmapBuffer");
        this.glUnmapBuffer = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glGetBufferParameteriv");
        this.glGetBufferParameteriv = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glGetBufferPointerv");
        this.glGetBufferPointerv = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glGenQueries");
        this.glGenQueries = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glDeleteQueries");
        this.glDeleteQueries = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glIsQuery");
        this.glIsQuery = functionAddress14;
        final boolean b14 = b13 & functionAddress14 != 0L;
        final long functionAddress15 = GLContext.getFunctionAddress("glBeginQuery");
        this.glBeginQuery = functionAddress15;
        final boolean b15 = b14 & functionAddress15 != 0L;
        final long functionAddress16 = GLContext.getFunctionAddress("glEndQuery");
        this.glEndQuery = functionAddress16;
        final boolean b16 = b15 & functionAddress16 != 0L;
        final long functionAddress17 = GLContext.getFunctionAddress("glGetQueryiv");
        this.glGetQueryiv = functionAddress17;
        final boolean b17 = b16 & functionAddress17 != 0L;
        final long functionAddress18 = GLContext.getFunctionAddress("glGetQueryObjectiv");
        this.glGetQueryObjectiv = functionAddress18;
        final boolean b18 = b17 & functionAddress18 != 0L;
        final long functionAddress19 = GLContext.getFunctionAddress("glGetQueryObjectuiv");
        this.glGetQueryObjectuiv = functionAddress19;
        return b18 & functionAddress19 != 0L;
    }
    
    private boolean GL20_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glShaderSource");
        this.glShaderSource = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glCreateShader");
        this.glCreateShader = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glIsShader");
        this.glIsShader = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glCompileShader");
        this.glCompileShader = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glDeleteShader");
        this.glDeleteShader = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glCreateProgram");
        this.glCreateProgram = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glIsProgram");
        this.glIsProgram = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glAttachShader");
        this.glAttachShader = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glDetachShader");
        this.glDetachShader = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glLinkProgram");
        this.glLinkProgram = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glUseProgram");
        this.glUseProgram = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glValidateProgram");
        this.glValidateProgram = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glDeleteProgram");
        this.glDeleteProgram = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glUniform1f");
        this.glUniform1f = functionAddress14;
        final boolean b14 = b13 & functionAddress14 != 0L;
        final long functionAddress15 = GLContext.getFunctionAddress("glUniform2f");
        this.glUniform2f = functionAddress15;
        final boolean b15 = b14 & functionAddress15 != 0L;
        final long functionAddress16 = GLContext.getFunctionAddress("glUniform3f");
        this.glUniform3f = functionAddress16;
        final boolean b16 = b15 & functionAddress16 != 0L;
        final long functionAddress17 = GLContext.getFunctionAddress("glUniform4f");
        this.glUniform4f = functionAddress17;
        final boolean b17 = b16 & functionAddress17 != 0L;
        final long functionAddress18 = GLContext.getFunctionAddress("glUniform1i");
        this.glUniform1i = functionAddress18;
        final boolean b18 = b17 & functionAddress18 != 0L;
        final long functionAddress19 = GLContext.getFunctionAddress("glUniform2i");
        this.glUniform2i = functionAddress19;
        final boolean b19 = b18 & functionAddress19 != 0L;
        final long functionAddress20 = GLContext.getFunctionAddress("glUniform3i");
        this.glUniform3i = functionAddress20;
        final boolean b20 = b19 & functionAddress20 != 0L;
        final long functionAddress21 = GLContext.getFunctionAddress("glUniform4i");
        this.glUniform4i = functionAddress21;
        final boolean b21 = b20 & functionAddress21 != 0L;
        final long functionAddress22 = GLContext.getFunctionAddress("glUniform1fv");
        this.glUniform1fv = functionAddress22;
        final boolean b22 = b21 & functionAddress22 != 0L;
        final long functionAddress23 = GLContext.getFunctionAddress("glUniform2fv");
        this.glUniform2fv = functionAddress23;
        final boolean b23 = b22 & functionAddress23 != 0L;
        final long functionAddress24 = GLContext.getFunctionAddress("glUniform3fv");
        this.glUniform3fv = functionAddress24;
        final boolean b24 = b23 & functionAddress24 != 0L;
        final long functionAddress25 = GLContext.getFunctionAddress("glUniform4fv");
        this.glUniform4fv = functionAddress25;
        final boolean b25 = b24 & functionAddress25 != 0L;
        final long functionAddress26 = GLContext.getFunctionAddress("glUniform1iv");
        this.glUniform1iv = functionAddress26;
        final boolean b26 = b25 & functionAddress26 != 0L;
        final long functionAddress27 = GLContext.getFunctionAddress("glUniform2iv");
        this.glUniform2iv = functionAddress27;
        final boolean b27 = b26 & functionAddress27 != 0L;
        final long functionAddress28 = GLContext.getFunctionAddress("glUniform3iv");
        this.glUniform3iv = functionAddress28;
        final boolean b28 = b27 & functionAddress28 != 0L;
        final long functionAddress29 = GLContext.getFunctionAddress("glUniform4iv");
        this.glUniform4iv = functionAddress29;
        final boolean b29 = b28 & functionAddress29 != 0L;
        final long functionAddress30 = GLContext.getFunctionAddress("glUniformMatrix2fv");
        this.glUniformMatrix2fv = functionAddress30;
        final boolean b30 = b29 & functionAddress30 != 0L;
        final long functionAddress31 = GLContext.getFunctionAddress("glUniformMatrix3fv");
        this.glUniformMatrix3fv = functionAddress31;
        final boolean b31 = b30 & functionAddress31 != 0L;
        final long functionAddress32 = GLContext.getFunctionAddress("glUniformMatrix4fv");
        this.glUniformMatrix4fv = functionAddress32;
        final boolean b32 = b31 & functionAddress32 != 0L;
        final long functionAddress33 = GLContext.getFunctionAddress("glGetShaderiv");
        this.glGetShaderiv = functionAddress33;
        final boolean b33 = b32 & functionAddress33 != 0L;
        final long functionAddress34 = GLContext.getFunctionAddress("glGetProgramiv");
        this.glGetProgramiv = functionAddress34;
        final boolean b34 = b33 & functionAddress34 != 0L;
        final long functionAddress35 = GLContext.getFunctionAddress("glGetShaderInfoLog");
        this.glGetShaderInfoLog = functionAddress35;
        final boolean b35 = b34 & functionAddress35 != 0L;
        final long functionAddress36 = GLContext.getFunctionAddress("glGetProgramInfoLog");
        this.glGetProgramInfoLog = functionAddress36;
        final boolean b36 = b35 & functionAddress36 != 0L;
        final long functionAddress37 = GLContext.getFunctionAddress("glGetAttachedShaders");
        this.glGetAttachedShaders = functionAddress37;
        final boolean b37 = b36 & functionAddress37 != 0L;
        final long functionAddress38 = GLContext.getFunctionAddress("glGetUniformLocation");
        this.glGetUniformLocation = functionAddress38;
        final boolean b38 = b37 & functionAddress38 != 0L;
        final long functionAddress39 = GLContext.getFunctionAddress("glGetActiveUniform");
        this.glGetActiveUniform = functionAddress39;
        final boolean b39 = b38 & functionAddress39 != 0L;
        final long functionAddress40 = GLContext.getFunctionAddress("glGetUniformfv");
        this.glGetUniformfv = functionAddress40;
        final boolean b40 = b39 & functionAddress40 != 0L;
        final long functionAddress41 = GLContext.getFunctionAddress("glGetUniformiv");
        this.glGetUniformiv = functionAddress41;
        final boolean b41 = b40 & functionAddress41 != 0L;
        final long functionAddress42 = GLContext.getFunctionAddress("glGetShaderSource");
        this.glGetShaderSource = functionAddress42;
        final boolean b42 = b41 & functionAddress42 != 0L;
        final long functionAddress43 = GLContext.getFunctionAddress("glVertexAttrib1s");
        this.glVertexAttrib1s = functionAddress43;
        final boolean b43 = b42 & functionAddress43 != 0L;
        final long functionAddress44 = GLContext.getFunctionAddress("glVertexAttrib1f");
        this.glVertexAttrib1f = functionAddress44;
        final boolean b44 = b43 & functionAddress44 != 0L;
        final long functionAddress45 = GLContext.getFunctionAddress("glVertexAttrib1d");
        this.glVertexAttrib1d = functionAddress45;
        final boolean b45 = b44 & functionAddress45 != 0L;
        final long functionAddress46 = GLContext.getFunctionAddress("glVertexAttrib2s");
        this.glVertexAttrib2s = functionAddress46;
        final boolean b46 = b45 & functionAddress46 != 0L;
        final long functionAddress47 = GLContext.getFunctionAddress("glVertexAttrib2f");
        this.glVertexAttrib2f = functionAddress47;
        final boolean b47 = b46 & functionAddress47 != 0L;
        final long functionAddress48 = GLContext.getFunctionAddress("glVertexAttrib2d");
        this.glVertexAttrib2d = functionAddress48;
        final boolean b48 = b47 & functionAddress48 != 0L;
        final long functionAddress49 = GLContext.getFunctionAddress("glVertexAttrib3s");
        this.glVertexAttrib3s = functionAddress49;
        final boolean b49 = b48 & functionAddress49 != 0L;
        final long functionAddress50 = GLContext.getFunctionAddress("glVertexAttrib3f");
        this.glVertexAttrib3f = functionAddress50;
        final boolean b50 = b49 & functionAddress50 != 0L;
        final long functionAddress51 = GLContext.getFunctionAddress("glVertexAttrib3d");
        this.glVertexAttrib3d = functionAddress51;
        final boolean b51 = b50 & functionAddress51 != 0L;
        final long functionAddress52 = GLContext.getFunctionAddress("glVertexAttrib4s");
        this.glVertexAttrib4s = functionAddress52;
        final boolean b52 = b51 & functionAddress52 != 0L;
        final long functionAddress53 = GLContext.getFunctionAddress("glVertexAttrib4f");
        this.glVertexAttrib4f = functionAddress53;
        final boolean b53 = b52 & functionAddress53 != 0L;
        final long functionAddress54 = GLContext.getFunctionAddress("glVertexAttrib4d");
        this.glVertexAttrib4d = functionAddress54;
        final boolean b54 = b53 & functionAddress54 != 0L;
        final long functionAddress55 = GLContext.getFunctionAddress("glVertexAttrib4Nub");
        this.glVertexAttrib4Nub = functionAddress55;
        final boolean b55 = b54 & functionAddress55 != 0L;
        final long functionAddress56 = GLContext.getFunctionAddress("glVertexAttribPointer");
        this.glVertexAttribPointer = functionAddress56;
        final boolean b56 = b55 & functionAddress56 != 0L;
        final long functionAddress57 = GLContext.getFunctionAddress("glEnableVertexAttribArray");
        this.glEnableVertexAttribArray = functionAddress57;
        final boolean b57 = b56 & functionAddress57 != 0L;
        final long functionAddress58 = GLContext.getFunctionAddress("glDisableVertexAttribArray");
        this.glDisableVertexAttribArray = functionAddress58;
        final boolean b58 = b57 & functionAddress58 != 0L;
        final long functionAddress59 = GLContext.getFunctionAddress("glGetVertexAttribfv");
        this.glGetVertexAttribfv = functionAddress59;
        final boolean b59 = b58 & functionAddress59 != 0L;
        final long functionAddress60 = GLContext.getFunctionAddress("glGetVertexAttribdv");
        this.glGetVertexAttribdv = functionAddress60;
        final boolean b60 = b59 & functionAddress60 != 0L;
        final long functionAddress61 = GLContext.getFunctionAddress("glGetVertexAttribiv");
        this.glGetVertexAttribiv = functionAddress61;
        final boolean b61 = b60 & functionAddress61 != 0L;
        final long functionAddress62 = GLContext.getFunctionAddress("glGetVertexAttribPointerv");
        this.glGetVertexAttribPointerv = functionAddress62;
        final boolean b62 = b61 & functionAddress62 != 0L;
        final long functionAddress63 = GLContext.getFunctionAddress("glBindAttribLocation");
        this.glBindAttribLocation = functionAddress63;
        final boolean b63 = b62 & functionAddress63 != 0L;
        final long functionAddress64 = GLContext.getFunctionAddress("glGetActiveAttrib");
        this.glGetActiveAttrib = functionAddress64;
        final boolean b64 = b63 & functionAddress64 != 0L;
        final long functionAddress65 = GLContext.getFunctionAddress("glGetAttribLocation");
        this.glGetAttribLocation = functionAddress65;
        final boolean b65 = b64 & functionAddress65 != 0L;
        final long functionAddress66 = GLContext.getFunctionAddress("glDrawBuffers");
        this.glDrawBuffers = functionAddress66;
        final boolean b66 = b65 & functionAddress66 != 0L;
        final long functionAddress67 = GLContext.getFunctionAddress("glStencilOpSeparate");
        this.glStencilOpSeparate = functionAddress67;
        final boolean b67 = b66 & functionAddress67 != 0L;
        final long functionAddress68 = GLContext.getFunctionAddress("glStencilFuncSeparate");
        this.glStencilFuncSeparate = functionAddress68;
        final boolean b68 = b67 & functionAddress68 != 0L;
        final long functionAddress69 = GLContext.getFunctionAddress("glStencilMaskSeparate");
        this.glStencilMaskSeparate = functionAddress69;
        final boolean b69 = b68 & functionAddress69 != 0L;
        final long functionAddress70 = GLContext.getFunctionAddress("glBlendEquationSeparate");
        this.glBlendEquationSeparate = functionAddress70;
        return b69 & functionAddress70 != 0L;
    }
    
    private boolean GL21_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glUniformMatrix2x3fv");
        this.glUniformMatrix2x3fv = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glUniformMatrix3x2fv");
        this.glUniformMatrix3x2fv = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glUniformMatrix2x4fv");
        this.glUniformMatrix2x4fv = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glUniformMatrix4x2fv");
        this.glUniformMatrix4x2fv = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glUniformMatrix3x4fv");
        this.glUniformMatrix3x4fv = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glUniformMatrix4x3fv");
        this.glUniformMatrix4x3fv = functionAddress6;
        return b5 & functionAddress6 != 0L;
    }
    
    private boolean GL30_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glGetStringi");
        this.glGetStringi = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glClearBufferfv");
        this.glClearBufferfv = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glClearBufferiv");
        this.glClearBufferiv = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glClearBufferuiv");
        this.glClearBufferuiv = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glClearBufferfi");
        this.glClearBufferfi = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glVertexAttribI1i");
        this.glVertexAttribI1i = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glVertexAttribI2i");
        this.glVertexAttribI2i = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glVertexAttribI3i");
        this.glVertexAttribI3i = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glVertexAttribI4i");
        this.glVertexAttribI4i = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glVertexAttribI1ui");
        this.glVertexAttribI1ui = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glVertexAttribI2ui");
        this.glVertexAttribI2ui = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glVertexAttribI3ui");
        this.glVertexAttribI3ui = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glVertexAttribI4ui");
        this.glVertexAttribI4ui = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glVertexAttribI1iv");
        this.glVertexAttribI1iv = functionAddress14;
        final boolean b14 = b13 & functionAddress14 != 0L;
        final long functionAddress15 = GLContext.getFunctionAddress("glVertexAttribI2iv");
        this.glVertexAttribI2iv = functionAddress15;
        final boolean b15 = b14 & functionAddress15 != 0L;
        final long functionAddress16 = GLContext.getFunctionAddress("glVertexAttribI3iv");
        this.glVertexAttribI3iv = functionAddress16;
        final boolean b16 = b15 & functionAddress16 != 0L;
        final long functionAddress17 = GLContext.getFunctionAddress("glVertexAttribI4iv");
        this.glVertexAttribI4iv = functionAddress17;
        final boolean b17 = b16 & functionAddress17 != 0L;
        final long functionAddress18 = GLContext.getFunctionAddress("glVertexAttribI1uiv");
        this.glVertexAttribI1uiv = functionAddress18;
        final boolean b18 = b17 & functionAddress18 != 0L;
        final long functionAddress19 = GLContext.getFunctionAddress("glVertexAttribI2uiv");
        this.glVertexAttribI2uiv = functionAddress19;
        final boolean b19 = b18 & functionAddress19 != 0L;
        final long functionAddress20 = GLContext.getFunctionAddress("glVertexAttribI3uiv");
        this.glVertexAttribI3uiv = functionAddress20;
        final boolean b20 = b19 & functionAddress20 != 0L;
        final long functionAddress21 = GLContext.getFunctionAddress("glVertexAttribI4uiv");
        this.glVertexAttribI4uiv = functionAddress21;
        final boolean b21 = b20 & functionAddress21 != 0L;
        final long functionAddress22 = GLContext.getFunctionAddress("glVertexAttribI4bv");
        this.glVertexAttribI4bv = functionAddress22;
        final boolean b22 = b21 & functionAddress22 != 0L;
        final long functionAddress23 = GLContext.getFunctionAddress("glVertexAttribI4sv");
        this.glVertexAttribI4sv = functionAddress23;
        final boolean b23 = b22 & functionAddress23 != 0L;
        final long functionAddress24 = GLContext.getFunctionAddress("glVertexAttribI4ubv");
        this.glVertexAttribI4ubv = functionAddress24;
        final boolean b24 = b23 & functionAddress24 != 0L;
        final long functionAddress25 = GLContext.getFunctionAddress("glVertexAttribI4usv");
        this.glVertexAttribI4usv = functionAddress25;
        final boolean b25 = b24 & functionAddress25 != 0L;
        final long functionAddress26 = GLContext.getFunctionAddress("glVertexAttribIPointer");
        this.glVertexAttribIPointer = functionAddress26;
        final boolean b26 = b25 & functionAddress26 != 0L;
        final long functionAddress27 = GLContext.getFunctionAddress("glGetVertexAttribIiv");
        this.glGetVertexAttribIiv = functionAddress27;
        final boolean b27 = b26 & functionAddress27 != 0L;
        final long functionAddress28 = GLContext.getFunctionAddress("glGetVertexAttribIuiv");
        this.glGetVertexAttribIuiv = functionAddress28;
        final boolean b28 = b27 & functionAddress28 != 0L;
        final long functionAddress29 = GLContext.getFunctionAddress("glUniform1ui");
        this.glUniform1ui = functionAddress29;
        final boolean b29 = b28 & functionAddress29 != 0L;
        final long functionAddress30 = GLContext.getFunctionAddress("glUniform2ui");
        this.glUniform2ui = functionAddress30;
        final boolean b30 = b29 & functionAddress30 != 0L;
        final long functionAddress31 = GLContext.getFunctionAddress("glUniform3ui");
        this.glUniform3ui = functionAddress31;
        final boolean b31 = b30 & functionAddress31 != 0L;
        final long functionAddress32 = GLContext.getFunctionAddress("glUniform4ui");
        this.glUniform4ui = functionAddress32;
        final boolean b32 = b31 & functionAddress32 != 0L;
        final long functionAddress33 = GLContext.getFunctionAddress("glUniform1uiv");
        this.glUniform1uiv = functionAddress33;
        final boolean b33 = b32 & functionAddress33 != 0L;
        final long functionAddress34 = GLContext.getFunctionAddress("glUniform2uiv");
        this.glUniform2uiv = functionAddress34;
        final boolean b34 = b33 & functionAddress34 != 0L;
        final long functionAddress35 = GLContext.getFunctionAddress("glUniform3uiv");
        this.glUniform3uiv = functionAddress35;
        final boolean b35 = b34 & functionAddress35 != 0L;
        final long functionAddress36 = GLContext.getFunctionAddress("glUniform4uiv");
        this.glUniform4uiv = functionAddress36;
        final boolean b36 = b35 & functionAddress36 != 0L;
        final long functionAddress37 = GLContext.getFunctionAddress("glGetUniformuiv");
        this.glGetUniformuiv = functionAddress37;
        final boolean b37 = b36 & functionAddress37 != 0L;
        final long functionAddress38 = GLContext.getFunctionAddress("glBindFragDataLocation");
        this.glBindFragDataLocation = functionAddress38;
        final boolean b38 = b37 & functionAddress38 != 0L;
        final long functionAddress39 = GLContext.getFunctionAddress("glGetFragDataLocation");
        this.glGetFragDataLocation = functionAddress39;
        final boolean b39 = b38 & functionAddress39 != 0L;
        final long functionAddress40 = GLContext.getFunctionAddress("glBeginConditionalRender");
        this.glBeginConditionalRender = functionAddress40;
        final boolean b40 = b39 & functionAddress40 != 0L;
        final long functionAddress41 = GLContext.getFunctionAddress("glEndConditionalRender");
        this.glEndConditionalRender = functionAddress41;
        final boolean b41 = b40 & functionAddress41 != 0L;
        final long functionAddress42 = GLContext.getFunctionAddress("glMapBufferRange");
        this.glMapBufferRange = functionAddress42;
        final boolean b42 = b41 & functionAddress42 != 0L;
        final long functionAddress43 = GLContext.getFunctionAddress("glFlushMappedBufferRange");
        this.glFlushMappedBufferRange = functionAddress43;
        final boolean b43 = b42 & functionAddress43 != 0L;
        final long functionAddress44 = GLContext.getFunctionAddress("glClampColor");
        this.glClampColor = functionAddress44;
        final boolean b44 = b43 & functionAddress44 != 0L;
        final long functionAddress45 = GLContext.getFunctionAddress("glIsRenderbuffer");
        this.glIsRenderbuffer = functionAddress45;
        final boolean b45 = b44 & functionAddress45 != 0L;
        final long functionAddress46 = GLContext.getFunctionAddress("glBindRenderbuffer");
        this.glBindRenderbuffer = functionAddress46;
        final boolean b46 = b45 & functionAddress46 != 0L;
        final long functionAddress47 = GLContext.getFunctionAddress("glDeleteRenderbuffers");
        this.glDeleteRenderbuffers = functionAddress47;
        final boolean b47 = b46 & functionAddress47 != 0L;
        final long functionAddress48 = GLContext.getFunctionAddress("glGenRenderbuffers");
        this.glGenRenderbuffers = functionAddress48;
        final boolean b48 = b47 & functionAddress48 != 0L;
        final long functionAddress49 = GLContext.getFunctionAddress("glRenderbufferStorage");
        this.glRenderbufferStorage = functionAddress49;
        final boolean b49 = b48 & functionAddress49 != 0L;
        final long functionAddress50 = GLContext.getFunctionAddress("glGetRenderbufferParameteriv");
        this.glGetRenderbufferParameteriv = functionAddress50;
        final boolean b50 = b49 & functionAddress50 != 0L;
        final long functionAddress51 = GLContext.getFunctionAddress("glIsFramebuffer");
        this.glIsFramebuffer = functionAddress51;
        final boolean b51 = b50 & functionAddress51 != 0L;
        final long functionAddress52 = GLContext.getFunctionAddress("glBindFramebuffer");
        this.glBindFramebuffer = functionAddress52;
        final boolean b52 = b51 & functionAddress52 != 0L;
        final long functionAddress53 = GLContext.getFunctionAddress("glDeleteFramebuffers");
        this.glDeleteFramebuffers = functionAddress53;
        final boolean b53 = b52 & functionAddress53 != 0L;
        final long functionAddress54 = GLContext.getFunctionAddress("glGenFramebuffers");
        this.glGenFramebuffers = functionAddress54;
        final boolean b54 = b53 & functionAddress54 != 0L;
        final long functionAddress55 = GLContext.getFunctionAddress("glCheckFramebufferStatus");
        this.glCheckFramebufferStatus = functionAddress55;
        final boolean b55 = b54 & functionAddress55 != 0L;
        final long functionAddress56 = GLContext.getFunctionAddress("glFramebufferTexture1D");
        this.glFramebufferTexture1D = functionAddress56;
        final boolean b56 = b55 & functionAddress56 != 0L;
        final long functionAddress57 = GLContext.getFunctionAddress("glFramebufferTexture2D");
        this.glFramebufferTexture2D = functionAddress57;
        final boolean b57 = b56 & functionAddress57 != 0L;
        final long functionAddress58 = GLContext.getFunctionAddress("glFramebufferTexture3D");
        this.glFramebufferTexture3D = functionAddress58;
        final boolean b58 = b57 & functionAddress58 != 0L;
        final long functionAddress59 = GLContext.getFunctionAddress("glFramebufferRenderbuffer");
        this.glFramebufferRenderbuffer = functionAddress59;
        final boolean b59 = b58 & functionAddress59 != 0L;
        final long functionAddress60 = GLContext.getFunctionAddress("glGetFramebufferAttachmentParameteriv");
        this.glGetFramebufferAttachmentParameteriv = functionAddress60;
        final boolean b60 = b59 & functionAddress60 != 0L;
        final long functionAddress61 = GLContext.getFunctionAddress("glGenerateMipmap");
        this.glGenerateMipmap = functionAddress61;
        final boolean b61 = b60 & functionAddress61 != 0L;
        final long functionAddress62 = GLContext.getFunctionAddress("glRenderbufferStorageMultisample");
        this.glRenderbufferStorageMultisample = functionAddress62;
        final boolean b62 = b61 & functionAddress62 != 0L;
        final long functionAddress63 = GLContext.getFunctionAddress("glBlitFramebuffer");
        this.glBlitFramebuffer = functionAddress63;
        final boolean b63 = b62 & functionAddress63 != 0L;
        final long functionAddress64 = GLContext.getFunctionAddress("glTexParameterIiv");
        this.glTexParameterIiv = functionAddress64;
        final boolean b64 = b63 & functionAddress64 != 0L;
        final long functionAddress65 = GLContext.getFunctionAddress("glTexParameterIuiv");
        this.glTexParameterIuiv = functionAddress65;
        final boolean b65 = b64 & functionAddress65 != 0L;
        final long functionAddress66 = GLContext.getFunctionAddress("glGetTexParameterIiv");
        this.glGetTexParameterIiv = functionAddress66;
        final boolean b66 = b65 & functionAddress66 != 0L;
        final long functionAddress67 = GLContext.getFunctionAddress("glGetTexParameterIuiv");
        this.glGetTexParameterIuiv = functionAddress67;
        final boolean b67 = b66 & functionAddress67 != 0L;
        final long functionAddress68 = GLContext.getFunctionAddress("glFramebufferTextureLayer");
        this.glFramebufferTextureLayer = functionAddress68;
        final boolean b68 = b67 & functionAddress68 != 0L;
        final long functionAddress69 = GLContext.getFunctionAddress("glColorMaski");
        this.glColorMaski = functionAddress69;
        final boolean b69 = b68 & functionAddress69 != 0L;
        final long functionAddress70 = GLContext.getFunctionAddress("glGetBooleani_v");
        this.glGetBooleani_v = functionAddress70;
        final boolean b70 = b69 & functionAddress70 != 0L;
        final long functionAddress71 = GLContext.getFunctionAddress("glGetIntegeri_v");
        this.glGetIntegeri_v = functionAddress71;
        final boolean b71 = b70 & functionAddress71 != 0L;
        final long functionAddress72 = GLContext.getFunctionAddress("glEnablei");
        this.glEnablei = functionAddress72;
        final boolean b72 = b71 & functionAddress72 != 0L;
        final long functionAddress73 = GLContext.getFunctionAddress("glDisablei");
        this.glDisablei = functionAddress73;
        final boolean b73 = b72 & functionAddress73 != 0L;
        final long functionAddress74 = GLContext.getFunctionAddress("glIsEnabledi");
        this.glIsEnabledi = functionAddress74;
        final boolean b74 = b73 & functionAddress74 != 0L;
        final long functionAddress75 = GLContext.getFunctionAddress("glBindBufferRange");
        this.glBindBufferRange = functionAddress75;
        final boolean b75 = b74 & functionAddress75 != 0L;
        final long functionAddress76 = GLContext.getFunctionAddress("glBindBufferBase");
        this.glBindBufferBase = functionAddress76;
        final boolean b76 = b75 & functionAddress76 != 0L;
        final long functionAddress77 = GLContext.getFunctionAddress("glBeginTransformFeedback");
        this.glBeginTransformFeedback = functionAddress77;
        final boolean b77 = b76 & functionAddress77 != 0L;
        final long functionAddress78 = GLContext.getFunctionAddress("glEndTransformFeedback");
        this.glEndTransformFeedback = functionAddress78;
        final boolean b78 = b77 & functionAddress78 != 0L;
        final long functionAddress79 = GLContext.getFunctionAddress("glTransformFeedbackVaryings");
        this.glTransformFeedbackVaryings = functionAddress79;
        final boolean b79 = b78 & functionAddress79 != 0L;
        final long functionAddress80 = GLContext.getFunctionAddress("glGetTransformFeedbackVarying");
        this.glGetTransformFeedbackVarying = functionAddress80;
        final boolean b80 = b79 & functionAddress80 != 0L;
        final long functionAddress81 = GLContext.getFunctionAddress("glBindVertexArray");
        this.glBindVertexArray = functionAddress81;
        final boolean b81 = b80 & functionAddress81 != 0L;
        final long functionAddress82 = GLContext.getFunctionAddress("glDeleteVertexArrays");
        this.glDeleteVertexArrays = functionAddress82;
        final boolean b82 = b81 & functionAddress82 != 0L;
        final long functionAddress83 = GLContext.getFunctionAddress("glGenVertexArrays");
        this.glGenVertexArrays = functionAddress83;
        final boolean b83 = b82 & functionAddress83 != 0L;
        final long functionAddress84 = GLContext.getFunctionAddress("glIsVertexArray");
        this.glIsVertexArray = functionAddress84;
        return b83 & functionAddress84 != 0L;
    }
    
    private boolean GL31_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glDrawArraysInstanced");
        this.glDrawArraysInstanced = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glDrawElementsInstanced");
        this.glDrawElementsInstanced = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glCopyBufferSubData");
        this.glCopyBufferSubData = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glPrimitiveRestartIndex");
        this.glPrimitiveRestartIndex = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glTexBuffer");
        this.glTexBuffer = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glGetUniformIndices");
        this.glGetUniformIndices = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glGetActiveUniformsiv");
        this.glGetActiveUniformsiv = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glGetActiveUniformName");
        this.glGetActiveUniformName = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glGetUniformBlockIndex");
        this.glGetUniformBlockIndex = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glGetActiveUniformBlockiv");
        this.glGetActiveUniformBlockiv = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glGetActiveUniformBlockName");
        this.glGetActiveUniformBlockName = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glUniformBlockBinding");
        this.glUniformBlockBinding = functionAddress12;
        return b11 & functionAddress12 != 0L;
    }
    
    private boolean GL32_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glGetBufferParameteri64v");
        this.glGetBufferParameteri64v = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glDrawElementsBaseVertex");
        this.glDrawElementsBaseVertex = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glDrawRangeElementsBaseVertex");
        this.glDrawRangeElementsBaseVertex = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glDrawElementsInstancedBaseVertex");
        this.glDrawElementsInstancedBaseVertex = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glProvokingVertex");
        this.glProvokingVertex = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glTexImage2DMultisample");
        this.glTexImage2DMultisample = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glTexImage3DMultisample");
        this.glTexImage3DMultisample = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glGetMultisamplefv");
        this.glGetMultisamplefv = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glSampleMaski");
        this.glSampleMaski = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glFramebufferTexture");
        this.glFramebufferTexture = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glFenceSync");
        this.glFenceSync = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glIsSync");
        this.glIsSync = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glDeleteSync");
        this.glDeleteSync = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glClientWaitSync");
        this.glClientWaitSync = functionAddress14;
        final boolean b14 = b13 & functionAddress14 != 0L;
        final long functionAddress15 = GLContext.getFunctionAddress("glWaitSync");
        this.glWaitSync = functionAddress15;
        final boolean b15 = b14 & functionAddress15 != 0L;
        final long functionAddress16 = GLContext.getFunctionAddress("glGetInteger64v");
        this.glGetInteger64v = functionAddress16;
        final boolean b16 = b15 & functionAddress16 != 0L;
        final long functionAddress17 = GLContext.getFunctionAddress("glGetInteger64i_v");
        this.glGetInteger64i_v = functionAddress17;
        final boolean b17 = b16 & functionAddress17 != 0L;
        final long functionAddress18 = GLContext.getFunctionAddress("glGetSynciv");
        this.glGetSynciv = functionAddress18;
        return b17 & functionAddress18 != 0L;
    }
    
    private boolean GL33_initNativeFunctionAddresses(final boolean forwardCompatible) {
        final long functionAddress = GLContext.getFunctionAddress("glBindFragDataLocationIndexed");
        this.glBindFragDataLocationIndexed = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glGetFragDataIndex");
        this.glGetFragDataIndex = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glGenSamplers");
        this.glGenSamplers = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glDeleteSamplers");
        this.glDeleteSamplers = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glIsSampler");
        this.glIsSampler = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glBindSampler");
        this.glBindSampler = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glSamplerParameteri");
        this.glSamplerParameteri = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glSamplerParameterf");
        this.glSamplerParameterf = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glSamplerParameteriv");
        this.glSamplerParameteriv = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glSamplerParameterfv");
        this.glSamplerParameterfv = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glSamplerParameterIiv");
        this.glSamplerParameterIiv = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glSamplerParameterIuiv");
        this.glSamplerParameterIuiv = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glGetSamplerParameteriv");
        this.glGetSamplerParameteriv = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glGetSamplerParameterfv");
        this.glGetSamplerParameterfv = functionAddress14;
        final boolean b14 = b13 & functionAddress14 != 0L;
        final long functionAddress15 = GLContext.getFunctionAddress("glGetSamplerParameterIiv");
        this.glGetSamplerParameterIiv = functionAddress15;
        final boolean b15 = b14 & functionAddress15 != 0L;
        final long functionAddress16 = GLContext.getFunctionAddress("glGetSamplerParameterIuiv");
        this.glGetSamplerParameterIuiv = functionAddress16;
        final boolean b16 = b15 & functionAddress16 != 0L;
        final long functionAddress17 = GLContext.getFunctionAddress("glQueryCounter");
        this.glQueryCounter = functionAddress17;
        final boolean b17 = b16 & functionAddress17 != 0L;
        final long functionAddress18 = GLContext.getFunctionAddress("glGetQueryObjecti64v");
        this.glGetQueryObjecti64v = functionAddress18;
        final boolean b18 = b17 & functionAddress18 != 0L;
        final long functionAddress19 = GLContext.getFunctionAddress("glGetQueryObjectui64v");
        this.glGetQueryObjectui64v = functionAddress19;
        final boolean b19 = b18 & functionAddress19 != 0L;
        final long functionAddress20 = GLContext.getFunctionAddress("glVertexAttribDivisor");
        this.glVertexAttribDivisor = functionAddress20;
        final boolean b20 = b19 & functionAddress20 != 0L;
        boolean b21 = false;
        Label_0462: {
            if (!forwardCompatible) {
                final long functionAddress21 = GLContext.getFunctionAddress("glVertexP2ui");
                this.glVertexP2ui = functionAddress21;
                if (functionAddress21 == 0L) {
                    b21 = false;
                    break Label_0462;
                }
            }
            b21 = true;
        }
        final boolean b22 = b20 & b21;
        boolean b23 = false;
        Label_0488: {
            if (!forwardCompatible) {
                final long functionAddress22 = GLContext.getFunctionAddress("glVertexP3ui");
                this.glVertexP3ui = functionAddress22;
                if (functionAddress22 == 0L) {
                    b23 = false;
                    break Label_0488;
                }
            }
            b23 = true;
        }
        final boolean b24 = b22 & b23;
        boolean b25 = false;
        Label_0514: {
            if (!forwardCompatible) {
                final long functionAddress23 = GLContext.getFunctionAddress("glVertexP4ui");
                this.glVertexP4ui = functionAddress23;
                if (functionAddress23 == 0L) {
                    b25 = false;
                    break Label_0514;
                }
            }
            b25 = true;
        }
        final boolean b26 = b24 & b25;
        boolean b27 = false;
        Label_0540: {
            if (!forwardCompatible) {
                final long functionAddress24 = GLContext.getFunctionAddress("glVertexP2uiv");
                this.glVertexP2uiv = functionAddress24;
                if (functionAddress24 == 0L) {
                    b27 = false;
                    break Label_0540;
                }
            }
            b27 = true;
        }
        final boolean b28 = b26 & b27;
        boolean b29 = false;
        Label_0566: {
            if (!forwardCompatible) {
                final long functionAddress25 = GLContext.getFunctionAddress("glVertexP3uiv");
                this.glVertexP3uiv = functionAddress25;
                if (functionAddress25 == 0L) {
                    b29 = false;
                    break Label_0566;
                }
            }
            b29 = true;
        }
        final boolean b30 = b28 & b29;
        boolean b31 = false;
        Label_0592: {
            if (!forwardCompatible) {
                final long functionAddress26 = GLContext.getFunctionAddress("glVertexP4uiv");
                this.glVertexP4uiv = functionAddress26;
                if (functionAddress26 == 0L) {
                    b31 = false;
                    break Label_0592;
                }
            }
            b31 = true;
        }
        final boolean b32 = b30 & b31;
        boolean b33 = false;
        Label_0618: {
            if (!forwardCompatible) {
                final long functionAddress27 = GLContext.getFunctionAddress("glTexCoordP1ui");
                this.glTexCoordP1ui = functionAddress27;
                if (functionAddress27 == 0L) {
                    b33 = false;
                    break Label_0618;
                }
            }
            b33 = true;
        }
        final boolean b34 = b32 & b33;
        boolean b35 = false;
        Label_0644: {
            if (!forwardCompatible) {
                final long functionAddress28 = GLContext.getFunctionAddress("glTexCoordP2ui");
                this.glTexCoordP2ui = functionAddress28;
                if (functionAddress28 == 0L) {
                    b35 = false;
                    break Label_0644;
                }
            }
            b35 = true;
        }
        final boolean b36 = b34 & b35;
        boolean b37 = false;
        Label_0670: {
            if (!forwardCompatible) {
                final long functionAddress29 = GLContext.getFunctionAddress("glTexCoordP3ui");
                this.glTexCoordP3ui = functionAddress29;
                if (functionAddress29 == 0L) {
                    b37 = false;
                    break Label_0670;
                }
            }
            b37 = true;
        }
        final boolean b38 = b36 & b37;
        boolean b39 = false;
        Label_0696: {
            if (!forwardCompatible) {
                final long functionAddress30 = GLContext.getFunctionAddress("glTexCoordP4ui");
                this.glTexCoordP4ui = functionAddress30;
                if (functionAddress30 == 0L) {
                    b39 = false;
                    break Label_0696;
                }
            }
            b39 = true;
        }
        final boolean b40 = b38 & b39;
        boolean b41 = false;
        Label_0722: {
            if (!forwardCompatible) {
                final long functionAddress31 = GLContext.getFunctionAddress("glTexCoordP1uiv");
                this.glTexCoordP1uiv = functionAddress31;
                if (functionAddress31 == 0L) {
                    b41 = false;
                    break Label_0722;
                }
            }
            b41 = true;
        }
        final boolean b42 = b40 & b41;
        boolean b43 = false;
        Label_0748: {
            if (!forwardCompatible) {
                final long functionAddress32 = GLContext.getFunctionAddress("glTexCoordP2uiv");
                this.glTexCoordP2uiv = functionAddress32;
                if (functionAddress32 == 0L) {
                    b43 = false;
                    break Label_0748;
                }
            }
            b43 = true;
        }
        final boolean b44 = b42 & b43;
        boolean b45 = false;
        Label_0774: {
            if (!forwardCompatible) {
                final long functionAddress33 = GLContext.getFunctionAddress("glTexCoordP3uiv");
                this.glTexCoordP3uiv = functionAddress33;
                if (functionAddress33 == 0L) {
                    b45 = false;
                    break Label_0774;
                }
            }
            b45 = true;
        }
        final boolean b46 = b44 & b45;
        boolean b47 = false;
        Label_0800: {
            if (!forwardCompatible) {
                final long functionAddress34 = GLContext.getFunctionAddress("glTexCoordP4uiv");
                this.glTexCoordP4uiv = functionAddress34;
                if (functionAddress34 == 0L) {
                    b47 = false;
                    break Label_0800;
                }
            }
            b47 = true;
        }
        final boolean b48 = b46 & b47;
        boolean b49 = false;
        Label_0826: {
            if (!forwardCompatible) {
                final long functionAddress35 = GLContext.getFunctionAddress("glMultiTexCoordP1ui");
                this.glMultiTexCoordP1ui = functionAddress35;
                if (functionAddress35 == 0L) {
                    b49 = false;
                    break Label_0826;
                }
            }
            b49 = true;
        }
        final boolean b50 = b48 & b49;
        boolean b51 = false;
        Label_0852: {
            if (!forwardCompatible) {
                final long functionAddress36 = GLContext.getFunctionAddress("glMultiTexCoordP2ui");
                this.glMultiTexCoordP2ui = functionAddress36;
                if (functionAddress36 == 0L) {
                    b51 = false;
                    break Label_0852;
                }
            }
            b51 = true;
        }
        final boolean b52 = b50 & b51;
        boolean b53 = false;
        Label_0878: {
            if (!forwardCompatible) {
                final long functionAddress37 = GLContext.getFunctionAddress("glMultiTexCoordP3ui");
                this.glMultiTexCoordP3ui = functionAddress37;
                if (functionAddress37 == 0L) {
                    b53 = false;
                    break Label_0878;
                }
            }
            b53 = true;
        }
        final boolean b54 = b52 & b53;
        boolean b55 = false;
        Label_0904: {
            if (!forwardCompatible) {
                final long functionAddress38 = GLContext.getFunctionAddress("glMultiTexCoordP4ui");
                this.glMultiTexCoordP4ui = functionAddress38;
                if (functionAddress38 == 0L) {
                    b55 = false;
                    break Label_0904;
                }
            }
            b55 = true;
        }
        final boolean b56 = b54 & b55;
        boolean b57 = false;
        Label_0930: {
            if (!forwardCompatible) {
                final long functionAddress39 = GLContext.getFunctionAddress("glMultiTexCoordP1uiv");
                this.glMultiTexCoordP1uiv = functionAddress39;
                if (functionAddress39 == 0L) {
                    b57 = false;
                    break Label_0930;
                }
            }
            b57 = true;
        }
        final boolean b58 = b56 & b57;
        boolean b59 = false;
        Label_0956: {
            if (!forwardCompatible) {
                final long functionAddress40 = GLContext.getFunctionAddress("glMultiTexCoordP2uiv");
                this.glMultiTexCoordP2uiv = functionAddress40;
                if (functionAddress40 == 0L) {
                    b59 = false;
                    break Label_0956;
                }
            }
            b59 = true;
        }
        final boolean b60 = b58 & b59;
        boolean b61 = false;
        Label_0982: {
            if (!forwardCompatible) {
                final long functionAddress41 = GLContext.getFunctionAddress("glMultiTexCoordP3uiv");
                this.glMultiTexCoordP3uiv = functionAddress41;
                if (functionAddress41 == 0L) {
                    b61 = false;
                    break Label_0982;
                }
            }
            b61 = true;
        }
        final boolean b62 = b60 & b61;
        boolean b63 = false;
        Label_1008: {
            if (!forwardCompatible) {
                final long functionAddress42 = GLContext.getFunctionAddress("glMultiTexCoordP4uiv");
                this.glMultiTexCoordP4uiv = functionAddress42;
                if (functionAddress42 == 0L) {
                    b63 = false;
                    break Label_1008;
                }
            }
            b63 = true;
        }
        final boolean b64 = b62 & b63;
        boolean b65 = false;
        Label_1034: {
            if (!forwardCompatible) {
                final long functionAddress43 = GLContext.getFunctionAddress("glNormalP3ui");
                this.glNormalP3ui = functionAddress43;
                if (functionAddress43 == 0L) {
                    b65 = false;
                    break Label_1034;
                }
            }
            b65 = true;
        }
        final boolean b66 = b64 & b65;
        boolean b67 = false;
        Label_1060: {
            if (!forwardCompatible) {
                final long functionAddress44 = GLContext.getFunctionAddress("glNormalP3uiv");
                this.glNormalP3uiv = functionAddress44;
                if (functionAddress44 == 0L) {
                    b67 = false;
                    break Label_1060;
                }
            }
            b67 = true;
        }
        final boolean b68 = b66 & b67;
        boolean b69 = false;
        Label_1086: {
            if (!forwardCompatible) {
                final long functionAddress45 = GLContext.getFunctionAddress("glColorP3ui");
                this.glColorP3ui = functionAddress45;
                if (functionAddress45 == 0L) {
                    b69 = false;
                    break Label_1086;
                }
            }
            b69 = true;
        }
        final boolean b70 = b68 & b69;
        boolean b71 = false;
        Label_1112: {
            if (!forwardCompatible) {
                final long functionAddress46 = GLContext.getFunctionAddress("glColorP4ui");
                this.glColorP4ui = functionAddress46;
                if (functionAddress46 == 0L) {
                    b71 = false;
                    break Label_1112;
                }
            }
            b71 = true;
        }
        final boolean b72 = b70 & b71;
        boolean b73 = false;
        Label_1138: {
            if (!forwardCompatible) {
                final long functionAddress47 = GLContext.getFunctionAddress("glColorP3uiv");
                this.glColorP3uiv = functionAddress47;
                if (functionAddress47 == 0L) {
                    b73 = false;
                    break Label_1138;
                }
            }
            b73 = true;
        }
        final boolean b74 = b72 & b73;
        boolean b75 = false;
        Label_1164: {
            if (!forwardCompatible) {
                final long functionAddress48 = GLContext.getFunctionAddress("glColorP4uiv");
                this.glColorP4uiv = functionAddress48;
                if (functionAddress48 == 0L) {
                    b75 = false;
                    break Label_1164;
                }
            }
            b75 = true;
        }
        final boolean b76 = b74 & b75;
        boolean b77 = false;
        Label_1190: {
            if (!forwardCompatible) {
                final long functionAddress49 = GLContext.getFunctionAddress("glSecondaryColorP3ui");
                this.glSecondaryColorP3ui = functionAddress49;
                if (functionAddress49 == 0L) {
                    b77 = false;
                    break Label_1190;
                }
            }
            b77 = true;
        }
        final boolean b78 = b76 & b77;
        boolean b79 = false;
        Label_1216: {
            if (!forwardCompatible) {
                final long functionAddress50 = GLContext.getFunctionAddress("glSecondaryColorP3uiv");
                this.glSecondaryColorP3uiv = functionAddress50;
                if (functionAddress50 == 0L) {
                    b79 = false;
                    break Label_1216;
                }
            }
            b79 = true;
        }
        final boolean b80 = b78 & b79;
        boolean b81 = false;
        Label_1242: {
            if (!forwardCompatible) {
                final long functionAddress51 = GLContext.getFunctionAddress("glVertexAttribP1ui");
                this.glVertexAttribP1ui = functionAddress51;
                if (functionAddress51 == 0L) {
                    b81 = false;
                    break Label_1242;
                }
            }
            b81 = true;
        }
        final boolean b82 = b80 & b81;
        boolean b83 = false;
        Label_1268: {
            if (!forwardCompatible) {
                final long functionAddress52 = GLContext.getFunctionAddress("glVertexAttribP2ui");
                this.glVertexAttribP2ui = functionAddress52;
                if (functionAddress52 == 0L) {
                    b83 = false;
                    break Label_1268;
                }
            }
            b83 = true;
        }
        final boolean b84 = b82 & b83;
        boolean b85 = false;
        Label_1294: {
            if (!forwardCompatible) {
                final long functionAddress53 = GLContext.getFunctionAddress("glVertexAttribP3ui");
                this.glVertexAttribP3ui = functionAddress53;
                if (functionAddress53 == 0L) {
                    b85 = false;
                    break Label_1294;
                }
            }
            b85 = true;
        }
        final boolean b86 = b84 & b85;
        boolean b87 = false;
        Label_1320: {
            if (!forwardCompatible) {
                final long functionAddress54 = GLContext.getFunctionAddress("glVertexAttribP4ui");
                this.glVertexAttribP4ui = functionAddress54;
                if (functionAddress54 == 0L) {
                    b87 = false;
                    break Label_1320;
                }
            }
            b87 = true;
        }
        final boolean b88 = b86 & b87;
        boolean b89 = false;
        Label_1346: {
            if (!forwardCompatible) {
                final long functionAddress55 = GLContext.getFunctionAddress("glVertexAttribP1uiv");
                this.glVertexAttribP1uiv = functionAddress55;
                if (functionAddress55 == 0L) {
                    b89 = false;
                    break Label_1346;
                }
            }
            b89 = true;
        }
        final boolean b90 = b88 & b89;
        boolean b91 = false;
        Label_1372: {
            if (!forwardCompatible) {
                final long functionAddress56 = GLContext.getFunctionAddress("glVertexAttribP2uiv");
                this.glVertexAttribP2uiv = functionAddress56;
                if (functionAddress56 == 0L) {
                    b91 = false;
                    break Label_1372;
                }
            }
            b91 = true;
        }
        final boolean b92 = b90 & b91;
        boolean b93 = false;
        Label_1398: {
            if (!forwardCompatible) {
                final long functionAddress57 = GLContext.getFunctionAddress("glVertexAttribP3uiv");
                this.glVertexAttribP3uiv = functionAddress57;
                if (functionAddress57 == 0L) {
                    b93 = false;
                    break Label_1398;
                }
            }
            b93 = true;
        }
        final boolean b94 = b92 & b93;
        if (!forwardCompatible) {
            final long functionAddress58 = GLContext.getFunctionAddress("glVertexAttribP4uiv");
            this.glVertexAttribP4uiv = functionAddress58;
            if (functionAddress58 == 0L) {
                final boolean b95 = false;
                return b94 & b95;
            }
        }
        final boolean b95 = true;
        return b94 & b95;
    }
    
    private boolean GL40_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glBlendEquationi");
        this.glBlendEquationi = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glBlendEquationSeparatei");
        this.glBlendEquationSeparatei = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glBlendFunci");
        this.glBlendFunci = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glBlendFuncSeparatei");
        this.glBlendFuncSeparatei = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glDrawArraysIndirect");
        this.glDrawArraysIndirect = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glDrawElementsIndirect");
        this.glDrawElementsIndirect = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glUniform1d");
        this.glUniform1d = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glUniform2d");
        this.glUniform2d = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glUniform3d");
        this.glUniform3d = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glUniform4d");
        this.glUniform4d = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glUniform1dv");
        this.glUniform1dv = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glUniform2dv");
        this.glUniform2dv = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glUniform3dv");
        this.glUniform3dv = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glUniform4dv");
        this.glUniform4dv = functionAddress14;
        final boolean b14 = b13 & functionAddress14 != 0L;
        final long functionAddress15 = GLContext.getFunctionAddress("glUniformMatrix2dv");
        this.glUniformMatrix2dv = functionAddress15;
        final boolean b15 = b14 & functionAddress15 != 0L;
        final long functionAddress16 = GLContext.getFunctionAddress("glUniformMatrix3dv");
        this.glUniformMatrix3dv = functionAddress16;
        final boolean b16 = b15 & functionAddress16 != 0L;
        final long functionAddress17 = GLContext.getFunctionAddress("glUniformMatrix4dv");
        this.glUniformMatrix4dv = functionAddress17;
        final boolean b17 = b16 & functionAddress17 != 0L;
        final long functionAddress18 = GLContext.getFunctionAddress("glUniformMatrix2x3dv");
        this.glUniformMatrix2x3dv = functionAddress18;
        final boolean b18 = b17 & functionAddress18 != 0L;
        final long functionAddress19 = GLContext.getFunctionAddress("glUniformMatrix2x4dv");
        this.glUniformMatrix2x4dv = functionAddress19;
        final boolean b19 = b18 & functionAddress19 != 0L;
        final long functionAddress20 = GLContext.getFunctionAddress("glUniformMatrix3x2dv");
        this.glUniformMatrix3x2dv = functionAddress20;
        final boolean b20 = b19 & functionAddress20 != 0L;
        final long functionAddress21 = GLContext.getFunctionAddress("glUniformMatrix3x4dv");
        this.glUniformMatrix3x4dv = functionAddress21;
        final boolean b21 = b20 & functionAddress21 != 0L;
        final long functionAddress22 = GLContext.getFunctionAddress("glUniformMatrix4x2dv");
        this.glUniformMatrix4x2dv = functionAddress22;
        final boolean b22 = b21 & functionAddress22 != 0L;
        final long functionAddress23 = GLContext.getFunctionAddress("glUniformMatrix4x3dv");
        this.glUniformMatrix4x3dv = functionAddress23;
        final boolean b23 = b22 & functionAddress23 != 0L;
        final long functionAddress24 = GLContext.getFunctionAddress("glGetUniformdv");
        this.glGetUniformdv = functionAddress24;
        final boolean b24 = b23 & functionAddress24 != 0L;
        final long functionAddress25 = GLContext.getFunctionAddress("glMinSampleShading");
        this.glMinSampleShading = functionAddress25;
        final boolean b25 = b24 & functionAddress25 != 0L;
        final long functionAddress26 = GLContext.getFunctionAddress("glGetSubroutineUniformLocation");
        this.glGetSubroutineUniformLocation = functionAddress26;
        final boolean b26 = b25 & functionAddress26 != 0L;
        final long functionAddress27 = GLContext.getFunctionAddress("glGetSubroutineIndex");
        this.glGetSubroutineIndex = functionAddress27;
        final boolean b27 = b26 & functionAddress27 != 0L;
        final long functionAddress28 = GLContext.getFunctionAddress("glGetActiveSubroutineUniformiv");
        this.glGetActiveSubroutineUniformiv = functionAddress28;
        final boolean b28 = b27 & functionAddress28 != 0L;
        final long functionAddress29 = GLContext.getFunctionAddress("glGetActiveSubroutineUniformName");
        this.glGetActiveSubroutineUniformName = functionAddress29;
        final boolean b29 = b28 & functionAddress29 != 0L;
        final long functionAddress30 = GLContext.getFunctionAddress("glGetActiveSubroutineName");
        this.glGetActiveSubroutineName = functionAddress30;
        final boolean b30 = b29 & functionAddress30 != 0L;
        final long functionAddress31 = GLContext.getFunctionAddress("glUniformSubroutinesuiv");
        this.glUniformSubroutinesuiv = functionAddress31;
        final boolean b31 = b30 & functionAddress31 != 0L;
        final long functionAddress32 = GLContext.getFunctionAddress("glGetUniformSubroutineuiv");
        this.glGetUniformSubroutineuiv = functionAddress32;
        final boolean b32 = b31 & functionAddress32 != 0L;
        final long functionAddress33 = GLContext.getFunctionAddress("glGetProgramStageiv");
        this.glGetProgramStageiv = functionAddress33;
        final boolean b33 = b32 & functionAddress33 != 0L;
        final long functionAddress34 = GLContext.getFunctionAddress("glPatchParameteri");
        this.glPatchParameteri = functionAddress34;
        final boolean b34 = b33 & functionAddress34 != 0L;
        final long functionAddress35 = GLContext.getFunctionAddress("glPatchParameterfv");
        this.glPatchParameterfv = functionAddress35;
        final boolean b35 = b34 & functionAddress35 != 0L;
        final long functionAddress36 = GLContext.getFunctionAddress("glBindTransformFeedback");
        this.glBindTransformFeedback = functionAddress36;
        final boolean b36 = b35 & functionAddress36 != 0L;
        final long functionAddress37 = GLContext.getFunctionAddress("glDeleteTransformFeedbacks");
        this.glDeleteTransformFeedbacks = functionAddress37;
        final boolean b37 = b36 & functionAddress37 != 0L;
        final long functionAddress38 = GLContext.getFunctionAddress("glGenTransformFeedbacks");
        this.glGenTransformFeedbacks = functionAddress38;
        final boolean b38 = b37 & functionAddress38 != 0L;
        final long functionAddress39 = GLContext.getFunctionAddress("glIsTransformFeedback");
        this.glIsTransformFeedback = functionAddress39;
        final boolean b39 = b38 & functionAddress39 != 0L;
        final long functionAddress40 = GLContext.getFunctionAddress("glPauseTransformFeedback");
        this.glPauseTransformFeedback = functionAddress40;
        final boolean b40 = b39 & functionAddress40 != 0L;
        final long functionAddress41 = GLContext.getFunctionAddress("glResumeTransformFeedback");
        this.glResumeTransformFeedback = functionAddress41;
        final boolean b41 = b40 & functionAddress41 != 0L;
        final long functionAddress42 = GLContext.getFunctionAddress("glDrawTransformFeedback");
        this.glDrawTransformFeedback = functionAddress42;
        final boolean b42 = b41 & functionAddress42 != 0L;
        final long functionAddress43 = GLContext.getFunctionAddress("glDrawTransformFeedbackStream");
        this.glDrawTransformFeedbackStream = functionAddress43;
        final boolean b43 = b42 & functionAddress43 != 0L;
        final long functionAddress44 = GLContext.getFunctionAddress("glBeginQueryIndexed");
        this.glBeginQueryIndexed = functionAddress44;
        final boolean b44 = b43 & functionAddress44 != 0L;
        final long functionAddress45 = GLContext.getFunctionAddress("glEndQueryIndexed");
        this.glEndQueryIndexed = functionAddress45;
        final boolean b45 = b44 & functionAddress45 != 0L;
        final long functionAddress46 = GLContext.getFunctionAddress("glGetQueryIndexediv");
        this.glGetQueryIndexediv = functionAddress46;
        return b45 & functionAddress46 != 0L;
    }
    
    private boolean GL41_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glReleaseShaderCompiler");
        this.glReleaseShaderCompiler = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glShaderBinary");
        this.glShaderBinary = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glGetShaderPrecisionFormat");
        this.glGetShaderPrecisionFormat = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glDepthRangef");
        this.glDepthRangef = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glClearDepthf");
        this.glClearDepthf = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glGetProgramBinary");
        this.glGetProgramBinary = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glProgramBinary");
        this.glProgramBinary = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glProgramParameteri");
        this.glProgramParameteri = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glUseProgramStages");
        this.glUseProgramStages = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glActiveShaderProgram");
        this.glActiveShaderProgram = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glCreateShaderProgramv");
        this.glCreateShaderProgramv = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glBindProgramPipeline");
        this.glBindProgramPipeline = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glDeleteProgramPipelines");
        this.glDeleteProgramPipelines = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glGenProgramPipelines");
        this.glGenProgramPipelines = functionAddress14;
        final boolean b14 = b13 & functionAddress14 != 0L;
        final long functionAddress15 = GLContext.getFunctionAddress("glIsProgramPipeline");
        this.glIsProgramPipeline = functionAddress15;
        final boolean b15 = b14 & functionAddress15 != 0L;
        final long functionAddress16 = GLContext.getFunctionAddress("glGetProgramPipelineiv");
        this.glGetProgramPipelineiv = functionAddress16;
        final boolean b16 = b15 & functionAddress16 != 0L;
        final long functionAddress17 = GLContext.getFunctionAddress("glProgramUniform1i");
        this.glProgramUniform1i = functionAddress17;
        final boolean b17 = b16 & functionAddress17 != 0L;
        final long functionAddress18 = GLContext.getFunctionAddress("glProgramUniform2i");
        this.glProgramUniform2i = functionAddress18;
        final boolean b18 = b17 & functionAddress18 != 0L;
        final long functionAddress19 = GLContext.getFunctionAddress("glProgramUniform3i");
        this.glProgramUniform3i = functionAddress19;
        final boolean b19 = b18 & functionAddress19 != 0L;
        final long functionAddress20 = GLContext.getFunctionAddress("glProgramUniform4i");
        this.glProgramUniform4i = functionAddress20;
        final boolean b20 = b19 & functionAddress20 != 0L;
        final long functionAddress21 = GLContext.getFunctionAddress("glProgramUniform1f");
        this.glProgramUniform1f = functionAddress21;
        final boolean b21 = b20 & functionAddress21 != 0L;
        final long functionAddress22 = GLContext.getFunctionAddress("glProgramUniform2f");
        this.glProgramUniform2f = functionAddress22;
        final boolean b22 = b21 & functionAddress22 != 0L;
        final long functionAddress23 = GLContext.getFunctionAddress("glProgramUniform3f");
        this.glProgramUniform3f = functionAddress23;
        final boolean b23 = b22 & functionAddress23 != 0L;
        final long functionAddress24 = GLContext.getFunctionAddress("glProgramUniform4f");
        this.glProgramUniform4f = functionAddress24;
        final boolean b24 = b23 & functionAddress24 != 0L;
        final long functionAddress25 = GLContext.getFunctionAddress("glProgramUniform1d");
        this.glProgramUniform1d = functionAddress25;
        final boolean b25 = b24 & functionAddress25 != 0L;
        final long functionAddress26 = GLContext.getFunctionAddress("glProgramUniform2d");
        this.glProgramUniform2d = functionAddress26;
        final boolean b26 = b25 & functionAddress26 != 0L;
        final long functionAddress27 = GLContext.getFunctionAddress("glProgramUniform3d");
        this.glProgramUniform3d = functionAddress27;
        final boolean b27 = b26 & functionAddress27 != 0L;
        final long functionAddress28 = GLContext.getFunctionAddress("glProgramUniform4d");
        this.glProgramUniform4d = functionAddress28;
        final boolean b28 = b27 & functionAddress28 != 0L;
        final long functionAddress29 = GLContext.getFunctionAddress("glProgramUniform1iv");
        this.glProgramUniform1iv = functionAddress29;
        final boolean b29 = b28 & functionAddress29 != 0L;
        final long functionAddress30 = GLContext.getFunctionAddress("glProgramUniform2iv");
        this.glProgramUniform2iv = functionAddress30;
        final boolean b30 = b29 & functionAddress30 != 0L;
        final long functionAddress31 = GLContext.getFunctionAddress("glProgramUniform3iv");
        this.glProgramUniform3iv = functionAddress31;
        final boolean b31 = b30 & functionAddress31 != 0L;
        final long functionAddress32 = GLContext.getFunctionAddress("glProgramUniform4iv");
        this.glProgramUniform4iv = functionAddress32;
        final boolean b32 = b31 & functionAddress32 != 0L;
        final long functionAddress33 = GLContext.getFunctionAddress("glProgramUniform1fv");
        this.glProgramUniform1fv = functionAddress33;
        final boolean b33 = b32 & functionAddress33 != 0L;
        final long functionAddress34 = GLContext.getFunctionAddress("glProgramUniform2fv");
        this.glProgramUniform2fv = functionAddress34;
        final boolean b34 = b33 & functionAddress34 != 0L;
        final long functionAddress35 = GLContext.getFunctionAddress("glProgramUniform3fv");
        this.glProgramUniform3fv = functionAddress35;
        final boolean b35 = b34 & functionAddress35 != 0L;
        final long functionAddress36 = GLContext.getFunctionAddress("glProgramUniform4fv");
        this.glProgramUniform4fv = functionAddress36;
        final boolean b36 = b35 & functionAddress36 != 0L;
        final long functionAddress37 = GLContext.getFunctionAddress("glProgramUniform1dv");
        this.glProgramUniform1dv = functionAddress37;
        final boolean b37 = b36 & functionAddress37 != 0L;
        final long functionAddress38 = GLContext.getFunctionAddress("glProgramUniform2dv");
        this.glProgramUniform2dv = functionAddress38;
        final boolean b38 = b37 & functionAddress38 != 0L;
        final long functionAddress39 = GLContext.getFunctionAddress("glProgramUniform3dv");
        this.glProgramUniform3dv = functionAddress39;
        final boolean b39 = b38 & functionAddress39 != 0L;
        final long functionAddress40 = GLContext.getFunctionAddress("glProgramUniform4dv");
        this.glProgramUniform4dv = functionAddress40;
        final boolean b40 = b39 & functionAddress40 != 0L;
        final long functionAddress41 = GLContext.getFunctionAddress("glProgramUniform1ui");
        this.glProgramUniform1ui = functionAddress41;
        final boolean b41 = b40 & functionAddress41 != 0L;
        final long functionAddress42 = GLContext.getFunctionAddress("glProgramUniform2ui");
        this.glProgramUniform2ui = functionAddress42;
        final boolean b42 = b41 & functionAddress42 != 0L;
        final long functionAddress43 = GLContext.getFunctionAddress("glProgramUniform3ui");
        this.glProgramUniform3ui = functionAddress43;
        final boolean b43 = b42 & functionAddress43 != 0L;
        final long functionAddress44 = GLContext.getFunctionAddress("glProgramUniform4ui");
        this.glProgramUniform4ui = functionAddress44;
        final boolean b44 = b43 & functionAddress44 != 0L;
        final long functionAddress45 = GLContext.getFunctionAddress("glProgramUniform1uiv");
        this.glProgramUniform1uiv = functionAddress45;
        final boolean b45 = b44 & functionAddress45 != 0L;
        final long functionAddress46 = GLContext.getFunctionAddress("glProgramUniform2uiv");
        this.glProgramUniform2uiv = functionAddress46;
        final boolean b46 = b45 & functionAddress46 != 0L;
        final long functionAddress47 = GLContext.getFunctionAddress("glProgramUniform3uiv");
        this.glProgramUniform3uiv = functionAddress47;
        final boolean b47 = b46 & functionAddress47 != 0L;
        final long functionAddress48 = GLContext.getFunctionAddress("glProgramUniform4uiv");
        this.glProgramUniform4uiv = functionAddress48;
        final boolean b48 = b47 & functionAddress48 != 0L;
        final long functionAddress49 = GLContext.getFunctionAddress("glProgramUniformMatrix2fv");
        this.glProgramUniformMatrix2fv = functionAddress49;
        final boolean b49 = b48 & functionAddress49 != 0L;
        final long functionAddress50 = GLContext.getFunctionAddress("glProgramUniformMatrix3fv");
        this.glProgramUniformMatrix3fv = functionAddress50;
        final boolean b50 = b49 & functionAddress50 != 0L;
        final long functionAddress51 = GLContext.getFunctionAddress("glProgramUniformMatrix4fv");
        this.glProgramUniformMatrix4fv = functionAddress51;
        final boolean b51 = b50 & functionAddress51 != 0L;
        final long functionAddress52 = GLContext.getFunctionAddress("glProgramUniformMatrix2dv");
        this.glProgramUniformMatrix2dv = functionAddress52;
        final boolean b52 = b51 & functionAddress52 != 0L;
        final long functionAddress53 = GLContext.getFunctionAddress("glProgramUniformMatrix3dv");
        this.glProgramUniformMatrix3dv = functionAddress53;
        final boolean b53 = b52 & functionAddress53 != 0L;
        final long functionAddress54 = GLContext.getFunctionAddress("glProgramUniformMatrix4dv");
        this.glProgramUniformMatrix4dv = functionAddress54;
        final boolean b54 = b53 & functionAddress54 != 0L;
        final long functionAddress55 = GLContext.getFunctionAddress("glProgramUniformMatrix2x3fv");
        this.glProgramUniformMatrix2x3fv = functionAddress55;
        final boolean b55 = b54 & functionAddress55 != 0L;
        final long functionAddress56 = GLContext.getFunctionAddress("glProgramUniformMatrix3x2fv");
        this.glProgramUniformMatrix3x2fv = functionAddress56;
        final boolean b56 = b55 & functionAddress56 != 0L;
        final long functionAddress57 = GLContext.getFunctionAddress("glProgramUniformMatrix2x4fv");
        this.glProgramUniformMatrix2x4fv = functionAddress57;
        final boolean b57 = b56 & functionAddress57 != 0L;
        final long functionAddress58 = GLContext.getFunctionAddress("glProgramUniformMatrix4x2fv");
        this.glProgramUniformMatrix4x2fv = functionAddress58;
        final boolean b58 = b57 & functionAddress58 != 0L;
        final long functionAddress59 = GLContext.getFunctionAddress("glProgramUniformMatrix3x4fv");
        this.glProgramUniformMatrix3x4fv = functionAddress59;
        final boolean b59 = b58 & functionAddress59 != 0L;
        final long functionAddress60 = GLContext.getFunctionAddress("glProgramUniformMatrix4x3fv");
        this.glProgramUniformMatrix4x3fv = functionAddress60;
        final boolean b60 = b59 & functionAddress60 != 0L;
        final long functionAddress61 = GLContext.getFunctionAddress("glProgramUniformMatrix2x3dv");
        this.glProgramUniformMatrix2x3dv = functionAddress61;
        final boolean b61 = b60 & functionAddress61 != 0L;
        final long functionAddress62 = GLContext.getFunctionAddress("glProgramUniformMatrix3x2dv");
        this.glProgramUniformMatrix3x2dv = functionAddress62;
        final boolean b62 = b61 & functionAddress62 != 0L;
        final long functionAddress63 = GLContext.getFunctionAddress("glProgramUniformMatrix2x4dv");
        this.glProgramUniformMatrix2x4dv = functionAddress63;
        final boolean b63 = b62 & functionAddress63 != 0L;
        final long functionAddress64 = GLContext.getFunctionAddress("glProgramUniformMatrix4x2dv");
        this.glProgramUniformMatrix4x2dv = functionAddress64;
        final boolean b64 = b63 & functionAddress64 != 0L;
        final long functionAddress65 = GLContext.getFunctionAddress("glProgramUniformMatrix3x4dv");
        this.glProgramUniformMatrix3x4dv = functionAddress65;
        final boolean b65 = b64 & functionAddress65 != 0L;
        final long functionAddress66 = GLContext.getFunctionAddress("glProgramUniformMatrix4x3dv");
        this.glProgramUniformMatrix4x3dv = functionAddress66;
        final boolean b66 = b65 & functionAddress66 != 0L;
        final long functionAddress67 = GLContext.getFunctionAddress("glValidateProgramPipeline");
        this.glValidateProgramPipeline = functionAddress67;
        final boolean b67 = b66 & functionAddress67 != 0L;
        final long functionAddress68 = GLContext.getFunctionAddress("glGetProgramPipelineInfoLog");
        this.glGetProgramPipelineInfoLog = functionAddress68;
        final boolean b68 = b67 & functionAddress68 != 0L;
        final long functionAddress69 = GLContext.getFunctionAddress("glVertexAttribL1d");
        this.glVertexAttribL1d = functionAddress69;
        final boolean b69 = b68 & functionAddress69 != 0L;
        final long functionAddress70 = GLContext.getFunctionAddress("glVertexAttribL2d");
        this.glVertexAttribL2d = functionAddress70;
        final boolean b70 = b69 & functionAddress70 != 0L;
        final long functionAddress71 = GLContext.getFunctionAddress("glVertexAttribL3d");
        this.glVertexAttribL3d = functionAddress71;
        final boolean b71 = b70 & functionAddress71 != 0L;
        final long functionAddress72 = GLContext.getFunctionAddress("glVertexAttribL4d");
        this.glVertexAttribL4d = functionAddress72;
        final boolean b72 = b71 & functionAddress72 != 0L;
        final long functionAddress73 = GLContext.getFunctionAddress("glVertexAttribL1dv");
        this.glVertexAttribL1dv = functionAddress73;
        final boolean b73 = b72 & functionAddress73 != 0L;
        final long functionAddress74 = GLContext.getFunctionAddress("glVertexAttribL2dv");
        this.glVertexAttribL2dv = functionAddress74;
        final boolean b74 = b73 & functionAddress74 != 0L;
        final long functionAddress75 = GLContext.getFunctionAddress("glVertexAttribL3dv");
        this.glVertexAttribL3dv = functionAddress75;
        final boolean b75 = b74 & functionAddress75 != 0L;
        final long functionAddress76 = GLContext.getFunctionAddress("glVertexAttribL4dv");
        this.glVertexAttribL4dv = functionAddress76;
        final boolean b76 = b75 & functionAddress76 != 0L;
        final long functionAddress77 = GLContext.getFunctionAddress("glVertexAttribLPointer");
        this.glVertexAttribLPointer = functionAddress77;
        final boolean b77 = b76 & functionAddress77 != 0L;
        final long functionAddress78 = GLContext.getFunctionAddress("glGetVertexAttribLdv");
        this.glGetVertexAttribLdv = functionAddress78;
        final boolean b78 = b77 & functionAddress78 != 0L;
        final long functionAddress79 = GLContext.getFunctionAddress("glViewportArrayv");
        this.glViewportArrayv = functionAddress79;
        final boolean b79 = b78 & functionAddress79 != 0L;
        final long functionAddress80 = GLContext.getFunctionAddress("glViewportIndexedf");
        this.glViewportIndexedf = functionAddress80;
        final boolean b80 = b79 & functionAddress80 != 0L;
        final long functionAddress81 = GLContext.getFunctionAddress("glViewportIndexedfv");
        this.glViewportIndexedfv = functionAddress81;
        final boolean b81 = b80 & functionAddress81 != 0L;
        final long functionAddress82 = GLContext.getFunctionAddress("glScissorArrayv");
        this.glScissorArrayv = functionAddress82;
        final boolean b82 = b81 & functionAddress82 != 0L;
        final long functionAddress83 = GLContext.getFunctionAddress("glScissorIndexed");
        this.glScissorIndexed = functionAddress83;
        final boolean b83 = b82 & functionAddress83 != 0L;
        final long functionAddress84 = GLContext.getFunctionAddress("glScissorIndexedv");
        this.glScissorIndexedv = functionAddress84;
        final boolean b84 = b83 & functionAddress84 != 0L;
        final long functionAddress85 = GLContext.getFunctionAddress("glDepthRangeArrayv");
        this.glDepthRangeArrayv = functionAddress85;
        final boolean b85 = b84 & functionAddress85 != 0L;
        final long functionAddress86 = GLContext.getFunctionAddress("glDepthRangeIndexed");
        this.glDepthRangeIndexed = functionAddress86;
        final boolean b86 = b85 & functionAddress86 != 0L;
        final long functionAddress87 = GLContext.getFunctionAddress("glGetFloati_v");
        this.glGetFloati_v = functionAddress87;
        final boolean b87 = b86 & functionAddress87 != 0L;
        final long functionAddress88 = GLContext.getFunctionAddress("glGetDoublei_v");
        this.glGetDoublei_v = functionAddress88;
        return b87 & functionAddress88 != 0L;
    }
    
    private boolean GL42_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glGetActiveAtomicCounterBufferiv");
        this.glGetActiveAtomicCounterBufferiv = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glTexStorage1D");
        this.glTexStorage1D = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glTexStorage2D");
        this.glTexStorage2D = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glTexStorage3D");
        this.glTexStorage3D = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glDrawTransformFeedbackInstanced");
        this.glDrawTransformFeedbackInstanced = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glDrawTransformFeedbackStreamInstanced");
        this.glDrawTransformFeedbackStreamInstanced = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glDrawArraysInstancedBaseInstance");
        this.glDrawArraysInstancedBaseInstance = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glDrawElementsInstancedBaseInstance");
        this.glDrawElementsInstancedBaseInstance = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glDrawElementsInstancedBaseVertexBaseInstance");
        this.glDrawElementsInstancedBaseVertexBaseInstance = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glBindImageTexture");
        this.glBindImageTexture = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glMemoryBarrier");
        this.glMemoryBarrier = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glGetInternalformativ");
        this.glGetInternalformativ = functionAddress12;
        return b11 & functionAddress12 != 0L;
    }
    
    private boolean GL43_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glClearBufferData");
        this.glClearBufferData = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glClearBufferSubData");
        this.glClearBufferSubData = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glDispatchCompute");
        this.glDispatchCompute = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glDispatchComputeIndirect");
        this.glDispatchComputeIndirect = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glCopyImageSubData");
        this.glCopyImageSubData = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glDebugMessageControl");
        this.glDebugMessageControl = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glDebugMessageInsert");
        this.glDebugMessageInsert = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glDebugMessageCallback");
        this.glDebugMessageCallback = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glGetDebugMessageLog");
        this.glGetDebugMessageLog = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glPushDebugGroup");
        this.glPushDebugGroup = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glPopDebugGroup");
        this.glPopDebugGroup = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glObjectLabel");
        this.glObjectLabel = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glGetObjectLabel");
        this.glGetObjectLabel = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glObjectPtrLabel");
        this.glObjectPtrLabel = functionAddress14;
        final boolean b14 = b13 & functionAddress14 != 0L;
        final long functionAddress15 = GLContext.getFunctionAddress("glGetObjectPtrLabel");
        this.glGetObjectPtrLabel = functionAddress15;
        final boolean b15 = b14 & functionAddress15 != 0L;
        final long functionAddress16 = GLContext.getFunctionAddress("glFramebufferParameteri");
        this.glFramebufferParameteri = functionAddress16;
        final boolean b16 = b15 & functionAddress16 != 0L;
        final long functionAddress17 = GLContext.getFunctionAddress("glGetFramebufferParameteriv");
        this.glGetFramebufferParameteriv = functionAddress17;
        final boolean b17 = b16 & functionAddress17 != 0L;
        final long functionAddress18 = GLContext.getFunctionAddress("glGetInternalformati64v");
        this.glGetInternalformati64v = functionAddress18;
        final boolean b18 = b17 & functionAddress18 != 0L;
        final long functionAddress19 = GLContext.getFunctionAddress("glInvalidateTexSubImage");
        this.glInvalidateTexSubImage = functionAddress19;
        final boolean b19 = b18 & functionAddress19 != 0L;
        final long functionAddress20 = GLContext.getFunctionAddress("glInvalidateTexImage");
        this.glInvalidateTexImage = functionAddress20;
        final boolean b20 = b19 & functionAddress20 != 0L;
        final long functionAddress21 = GLContext.getFunctionAddress("glInvalidateBufferSubData");
        this.glInvalidateBufferSubData = functionAddress21;
        final boolean b21 = b20 & functionAddress21 != 0L;
        final long functionAddress22 = GLContext.getFunctionAddress("glInvalidateBufferData");
        this.glInvalidateBufferData = functionAddress22;
        final boolean b22 = b21 & functionAddress22 != 0L;
        final long functionAddress23 = GLContext.getFunctionAddress("glInvalidateFramebuffer");
        this.glInvalidateFramebuffer = functionAddress23;
        final boolean b23 = b22 & functionAddress23 != 0L;
        final long functionAddress24 = GLContext.getFunctionAddress("glInvalidateSubFramebuffer");
        this.glInvalidateSubFramebuffer = functionAddress24;
        final boolean b24 = b23 & functionAddress24 != 0L;
        final long functionAddress25 = GLContext.getFunctionAddress("glMultiDrawArraysIndirect");
        this.glMultiDrawArraysIndirect = functionAddress25;
        final boolean b25 = b24 & functionAddress25 != 0L;
        final long functionAddress26 = GLContext.getFunctionAddress("glMultiDrawElementsIndirect");
        this.glMultiDrawElementsIndirect = functionAddress26;
        final boolean b26 = b25 & functionAddress26 != 0L;
        final long functionAddress27 = GLContext.getFunctionAddress("glGetProgramInterfaceiv");
        this.glGetProgramInterfaceiv = functionAddress27;
        final boolean b27 = b26 & functionAddress27 != 0L;
        final long functionAddress28 = GLContext.getFunctionAddress("glGetProgramResourceIndex");
        this.glGetProgramResourceIndex = functionAddress28;
        final boolean b28 = b27 & functionAddress28 != 0L;
        final long functionAddress29 = GLContext.getFunctionAddress("glGetProgramResourceName");
        this.glGetProgramResourceName = functionAddress29;
        final boolean b29 = b28 & functionAddress29 != 0L;
        final long functionAddress30 = GLContext.getFunctionAddress("glGetProgramResourceiv");
        this.glGetProgramResourceiv = functionAddress30;
        final boolean b30 = b29 & functionAddress30 != 0L;
        final long functionAddress31 = GLContext.getFunctionAddress("glGetProgramResourceLocation");
        this.glGetProgramResourceLocation = functionAddress31;
        final boolean b31 = b30 & functionAddress31 != 0L;
        final long functionAddress32 = GLContext.getFunctionAddress("glGetProgramResourceLocationIndex");
        this.glGetProgramResourceLocationIndex = functionAddress32;
        final boolean b32 = b31 & functionAddress32 != 0L;
        final long functionAddress33 = GLContext.getFunctionAddress("glShaderStorageBlockBinding");
        this.glShaderStorageBlockBinding = functionAddress33;
        final boolean b33 = b32 & functionAddress33 != 0L;
        final long functionAddress34 = GLContext.getFunctionAddress("glTexBufferRange");
        this.glTexBufferRange = functionAddress34;
        final boolean b34 = b33 & functionAddress34 != 0L;
        final long functionAddress35 = GLContext.getFunctionAddress("glTexStorage2DMultisample");
        this.glTexStorage2DMultisample = functionAddress35;
        final boolean b35 = b34 & functionAddress35 != 0L;
        final long functionAddress36 = GLContext.getFunctionAddress("glTexStorage3DMultisample");
        this.glTexStorage3DMultisample = functionAddress36;
        final boolean b36 = b35 & functionAddress36 != 0L;
        final long functionAddress37 = GLContext.getFunctionAddress("glTextureView");
        this.glTextureView = functionAddress37;
        final boolean b37 = b36 & functionAddress37 != 0L;
        final long functionAddress38 = GLContext.getFunctionAddress("glBindVertexBuffer");
        this.glBindVertexBuffer = functionAddress38;
        final boolean b38 = b37 & functionAddress38 != 0L;
        final long functionAddress39 = GLContext.getFunctionAddress("glVertexAttribFormat");
        this.glVertexAttribFormat = functionAddress39;
        final boolean b39 = b38 & functionAddress39 != 0L;
        final long functionAddress40 = GLContext.getFunctionAddress("glVertexAttribIFormat");
        this.glVertexAttribIFormat = functionAddress40;
        final boolean b40 = b39 & functionAddress40 != 0L;
        final long functionAddress41 = GLContext.getFunctionAddress("glVertexAttribLFormat");
        this.glVertexAttribLFormat = functionAddress41;
        final boolean b41 = b40 & functionAddress41 != 0L;
        final long functionAddress42 = GLContext.getFunctionAddress("glVertexAttribBinding");
        this.glVertexAttribBinding = functionAddress42;
        final boolean b42 = b41 & functionAddress42 != 0L;
        final long functionAddress43 = GLContext.getFunctionAddress("glVertexBindingDivisor");
        this.glVertexBindingDivisor = functionAddress43;
        return b42 & functionAddress43 != 0L;
    }
    
    private boolean GL44_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glBufferStorage");
        this.glBufferStorage = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glClearTexImage");
        this.glClearTexImage = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glClearTexSubImage");
        this.glClearTexSubImage = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glBindBuffersBase");
        this.glBindBuffersBase = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glBindBuffersRange");
        this.glBindBuffersRange = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glBindTextures");
        this.glBindTextures = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glBindSamplers");
        this.glBindSamplers = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glBindImageTextures");
        this.glBindImageTextures = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glBindVertexBuffers");
        this.glBindVertexBuffers = functionAddress9;
        return b8 & functionAddress9 != 0L;
    }
    
    private boolean GL45_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glClipControl");
        this.glClipControl = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glCreateTransformFeedbacks");
        this.glCreateTransformFeedbacks = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glTransformFeedbackBufferBase");
        this.glTransformFeedbackBufferBase = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glTransformFeedbackBufferRange");
        this.glTransformFeedbackBufferRange = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glGetTransformFeedbackiv");
        this.glGetTransformFeedbackiv = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glGetTransformFeedbacki_v");
        this.glGetTransformFeedbacki_v = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glGetTransformFeedbacki64_v");
        this.glGetTransformFeedbacki64_v = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glCreateBuffers");
        this.glCreateBuffers = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glNamedBufferStorage");
        this.glNamedBufferStorage = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glNamedBufferData");
        this.glNamedBufferData = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glNamedBufferSubData");
        this.glNamedBufferSubData = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glCopyNamedBufferSubData");
        this.glCopyNamedBufferSubData = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glClearNamedBufferData");
        this.glClearNamedBufferData = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glClearNamedBufferSubData");
        this.glClearNamedBufferSubData = functionAddress14;
        final boolean b14 = b13 & functionAddress14 != 0L;
        final long functionAddress15 = GLContext.getFunctionAddress("glMapNamedBuffer");
        this.glMapNamedBuffer = functionAddress15;
        final boolean b15 = b14 & functionAddress15 != 0L;
        final long functionAddress16 = GLContext.getFunctionAddress("glMapNamedBufferRange");
        this.glMapNamedBufferRange = functionAddress16;
        final boolean b16 = b15 & functionAddress16 != 0L;
        final long functionAddress17 = GLContext.getFunctionAddress("glUnmapNamedBuffer");
        this.glUnmapNamedBuffer = functionAddress17;
        final boolean b17 = b16 & functionAddress17 != 0L;
        final long functionAddress18 = GLContext.getFunctionAddress("glFlushMappedNamedBufferRange");
        this.glFlushMappedNamedBufferRange = functionAddress18;
        final boolean b18 = b17 & functionAddress18 != 0L;
        final long functionAddress19 = GLContext.getFunctionAddress("glGetNamedBufferParameteriv");
        this.glGetNamedBufferParameteriv = functionAddress19;
        final boolean b19 = b18 & functionAddress19 != 0L;
        final long functionAddress20 = GLContext.getFunctionAddress("glGetNamedBufferParameteri64v");
        this.glGetNamedBufferParameteri64v = functionAddress20;
        final boolean b20 = b19 & functionAddress20 != 0L;
        final long functionAddress21 = GLContext.getFunctionAddress("glGetNamedBufferPointerv");
        this.glGetNamedBufferPointerv = functionAddress21;
        final boolean b21 = b20 & functionAddress21 != 0L;
        final long functionAddress22 = GLContext.getFunctionAddress("glGetNamedBufferSubData");
        this.glGetNamedBufferSubData = functionAddress22;
        final boolean b22 = b21 & functionAddress22 != 0L;
        final long functionAddress23 = GLContext.getFunctionAddress("glCreateFramebuffers");
        this.glCreateFramebuffers = functionAddress23;
        final boolean b23 = b22 & functionAddress23 != 0L;
        final long functionAddress24 = GLContext.getFunctionAddress("glNamedFramebufferRenderbuffer");
        this.glNamedFramebufferRenderbuffer = functionAddress24;
        final boolean b24 = b23 & functionAddress24 != 0L;
        final long functionAddress25 = GLContext.getFunctionAddress("glNamedFramebufferParameteri");
        this.glNamedFramebufferParameteri = functionAddress25;
        final boolean b25 = b24 & functionAddress25 != 0L;
        final long functionAddress26 = GLContext.getFunctionAddress("glNamedFramebufferTexture");
        this.glNamedFramebufferTexture = functionAddress26;
        final boolean b26 = b25 & functionAddress26 != 0L;
        final long functionAddress27 = GLContext.getFunctionAddress("glNamedFramebufferTextureLayer");
        this.glNamedFramebufferTextureLayer = functionAddress27;
        final boolean b27 = b26 & functionAddress27 != 0L;
        final long functionAddress28 = GLContext.getFunctionAddress("glNamedFramebufferDrawBuffer");
        this.glNamedFramebufferDrawBuffer = functionAddress28;
        final boolean b28 = b27 & functionAddress28 != 0L;
        final long functionAddress29 = GLContext.getFunctionAddress("glNamedFramebufferDrawBuffers");
        this.glNamedFramebufferDrawBuffers = functionAddress29;
        final boolean b29 = b28 & functionAddress29 != 0L;
        final long functionAddress30 = GLContext.getFunctionAddress("glNamedFramebufferReadBuffer");
        this.glNamedFramebufferReadBuffer = functionAddress30;
        final boolean b30 = b29 & functionAddress30 != 0L;
        final long functionAddress31 = GLContext.getFunctionAddress("glInvalidateNamedFramebufferData");
        this.glInvalidateNamedFramebufferData = functionAddress31;
        final boolean b31 = b30 & functionAddress31 != 0L;
        final long functionAddress32 = GLContext.getFunctionAddress("glInvalidateNamedFramebufferSubData");
        this.glInvalidateNamedFramebufferSubData = functionAddress32;
        final boolean b32 = b31 & functionAddress32 != 0L;
        final long functionAddress33 = GLContext.getFunctionAddress("glClearNamedFramebufferiv");
        this.glClearNamedFramebufferiv = functionAddress33;
        final boolean b33 = b32 & functionAddress33 != 0L;
        final long functionAddress34 = GLContext.getFunctionAddress("glClearNamedFramebufferuiv");
        this.glClearNamedFramebufferuiv = functionAddress34;
        final boolean b34 = b33 & functionAddress34 != 0L;
        final long functionAddress35 = GLContext.getFunctionAddress("glClearNamedFramebufferfv");
        this.glClearNamedFramebufferfv = functionAddress35;
        final boolean b35 = b34 & functionAddress35 != 0L;
        final long functionAddress36 = GLContext.getFunctionAddress("glClearNamedFramebufferfi");
        this.glClearNamedFramebufferfi = functionAddress36;
        final boolean b36 = b35 & functionAddress36 != 0L;
        final long functionAddress37 = GLContext.getFunctionAddress("glBlitNamedFramebuffer");
        this.glBlitNamedFramebuffer = functionAddress37;
        final boolean b37 = b36 & functionAddress37 != 0L;
        final long functionAddress38 = GLContext.getFunctionAddress("glCheckNamedFramebufferStatus");
        this.glCheckNamedFramebufferStatus = functionAddress38;
        final boolean b38 = b37 & functionAddress38 != 0L;
        final long functionAddress39 = GLContext.getFunctionAddress("glGetNamedFramebufferParameteriv");
        this.glGetNamedFramebufferParameteriv = functionAddress39;
        final boolean b39 = b38 & functionAddress39 != 0L;
        final long functionAddress40 = GLContext.getFunctionAddress("glGetNamedFramebufferAttachmentParameteriv");
        this.glGetNamedFramebufferAttachmentParameteriv = functionAddress40;
        final boolean b40 = b39 & functionAddress40 != 0L;
        final long functionAddress41 = GLContext.getFunctionAddress("glCreateRenderbuffers");
        this.glCreateRenderbuffers = functionAddress41;
        final boolean b41 = b40 & functionAddress41 != 0L;
        final long functionAddress42 = GLContext.getFunctionAddress("glNamedRenderbufferStorage");
        this.glNamedRenderbufferStorage = functionAddress42;
        final boolean b42 = b41 & functionAddress42 != 0L;
        final long functionAddress43 = GLContext.getFunctionAddress("glNamedRenderbufferStorageMultisample");
        this.glNamedRenderbufferStorageMultisample = functionAddress43;
        final boolean b43 = b42 & functionAddress43 != 0L;
        final long functionAddress44 = GLContext.getFunctionAddress("glGetNamedRenderbufferParameteriv");
        this.glGetNamedRenderbufferParameteriv = functionAddress44;
        final boolean b44 = b43 & functionAddress44 != 0L;
        final long functionAddress45 = GLContext.getFunctionAddress("glCreateTextures");
        this.glCreateTextures = functionAddress45;
        final boolean b45 = b44 & functionAddress45 != 0L;
        final long functionAddress46 = GLContext.getFunctionAddress("glTextureBuffer");
        this.glTextureBuffer = functionAddress46;
        final boolean b46 = b45 & functionAddress46 != 0L;
        final long functionAddress47 = GLContext.getFunctionAddress("glTextureBufferRange");
        this.glTextureBufferRange = functionAddress47;
        final boolean b47 = b46 & functionAddress47 != 0L;
        final long functionAddress48 = GLContext.getFunctionAddress("glTextureStorage1D");
        this.glTextureStorage1D = functionAddress48;
        final boolean b48 = b47 & functionAddress48 != 0L;
        final long functionAddress49 = GLContext.getFunctionAddress("glTextureStorage2D");
        this.glTextureStorage2D = functionAddress49;
        final boolean b49 = b48 & functionAddress49 != 0L;
        final long functionAddress50 = GLContext.getFunctionAddress("glTextureStorage3D");
        this.glTextureStorage3D = functionAddress50;
        final boolean b50 = b49 & functionAddress50 != 0L;
        final long functionAddress51 = GLContext.getFunctionAddress("glTextureStorage2DMultisample");
        this.glTextureStorage2DMultisample = functionAddress51;
        final boolean b51 = b50 & functionAddress51 != 0L;
        final long functionAddress52 = GLContext.getFunctionAddress("glTextureStorage3DMultisample");
        this.glTextureStorage3DMultisample = functionAddress52;
        final boolean b52 = b51 & functionAddress52 != 0L;
        final long functionAddress53 = GLContext.getFunctionAddress("glTextureSubImage1D");
        this.glTextureSubImage1D = functionAddress53;
        final boolean b53 = b52 & functionAddress53 != 0L;
        final long functionAddress54 = GLContext.getFunctionAddress("glTextureSubImage2D");
        this.glTextureSubImage2D = functionAddress54;
        final boolean b54 = b53 & functionAddress54 != 0L;
        final long functionAddress55 = GLContext.getFunctionAddress("glTextureSubImage3D");
        this.glTextureSubImage3D = functionAddress55;
        final boolean b55 = b54 & functionAddress55 != 0L;
        final long functionAddress56 = GLContext.getFunctionAddress("glCompressedTextureSubImage1D");
        this.glCompressedTextureSubImage1D = functionAddress56;
        final boolean b56 = b55 & functionAddress56 != 0L;
        final long functionAddress57 = GLContext.getFunctionAddress("glCompressedTextureSubImage2D");
        this.glCompressedTextureSubImage2D = functionAddress57;
        final boolean b57 = b56 & functionAddress57 != 0L;
        final long functionAddress58 = GLContext.getFunctionAddress("glCompressedTextureSubImage3D");
        this.glCompressedTextureSubImage3D = functionAddress58;
        final boolean b58 = b57 & functionAddress58 != 0L;
        final long functionAddress59 = GLContext.getFunctionAddress("glCopyTextureSubImage1D");
        this.glCopyTextureSubImage1D = functionAddress59;
        final boolean b59 = b58 & functionAddress59 != 0L;
        final long functionAddress60 = GLContext.getFunctionAddress("glCopyTextureSubImage2D");
        this.glCopyTextureSubImage2D = functionAddress60;
        final boolean b60 = b59 & functionAddress60 != 0L;
        final long functionAddress61 = GLContext.getFunctionAddress("glCopyTextureSubImage3D");
        this.glCopyTextureSubImage3D = functionAddress61;
        final boolean b61 = b60 & functionAddress61 != 0L;
        final long functionAddress62 = GLContext.getFunctionAddress("glTextureParameterf");
        this.glTextureParameterf = functionAddress62;
        final boolean b62 = b61 & functionAddress62 != 0L;
        final long functionAddress63 = GLContext.getFunctionAddress("glTextureParameterfv");
        this.glTextureParameterfv = functionAddress63;
        final boolean b63 = b62 & functionAddress63 != 0L;
        final long functionAddress64 = GLContext.getFunctionAddress("glTextureParameteri");
        this.glTextureParameteri = functionAddress64;
        final boolean b64 = b63 & functionAddress64 != 0L;
        final long functionAddress65 = GLContext.getFunctionAddress("glTextureParameterIiv");
        this.glTextureParameterIiv = functionAddress65;
        final boolean b65 = b64 & functionAddress65 != 0L;
        final long functionAddress66 = GLContext.getFunctionAddress("glTextureParameterIuiv");
        this.glTextureParameterIuiv = functionAddress66;
        final boolean b66 = b65 & functionAddress66 != 0L;
        final long functionAddress67 = GLContext.getFunctionAddress("glTextureParameteriv");
        this.glTextureParameteriv = functionAddress67;
        final boolean b67 = b66 & functionAddress67 != 0L;
        final long functionAddress68 = GLContext.getFunctionAddress("glGenerateTextureMipmap");
        this.glGenerateTextureMipmap = functionAddress68;
        final boolean b68 = b67 & functionAddress68 != 0L;
        final long functionAddress69 = GLContext.getFunctionAddress("glBindTextureUnit");
        this.glBindTextureUnit = functionAddress69;
        final boolean b69 = b68 & functionAddress69 != 0L;
        final long functionAddress70 = GLContext.getFunctionAddress("glGetTextureImage");
        this.glGetTextureImage = functionAddress70;
        final boolean b70 = b69 & functionAddress70 != 0L;
        final long functionAddress71 = GLContext.getFunctionAddress("glGetCompressedTextureImage");
        this.glGetCompressedTextureImage = functionAddress71;
        final boolean b71 = b70 & functionAddress71 != 0L;
        final long functionAddress72 = GLContext.getFunctionAddress("glGetTextureLevelParameterfv");
        this.glGetTextureLevelParameterfv = functionAddress72;
        final boolean b72 = b71 & functionAddress72 != 0L;
        final long functionAddress73 = GLContext.getFunctionAddress("glGetTextureLevelParameteriv");
        this.glGetTextureLevelParameteriv = functionAddress73;
        final boolean b73 = b72 & functionAddress73 != 0L;
        final long functionAddress74 = GLContext.getFunctionAddress("glGetTextureParameterfv");
        this.glGetTextureParameterfv = functionAddress74;
        final boolean b74 = b73 & functionAddress74 != 0L;
        final long functionAddress75 = GLContext.getFunctionAddress("glGetTextureParameterIiv");
        this.glGetTextureParameterIiv = functionAddress75;
        final boolean b75 = b74 & functionAddress75 != 0L;
        final long functionAddress76 = GLContext.getFunctionAddress("glGetTextureParameterIuiv");
        this.glGetTextureParameterIuiv = functionAddress76;
        final boolean b76 = b75 & functionAddress76 != 0L;
        final long functionAddress77 = GLContext.getFunctionAddress("glGetTextureParameteriv");
        this.glGetTextureParameteriv = functionAddress77;
        final boolean b77 = b76 & functionAddress77 != 0L;
        final long functionAddress78 = GLContext.getFunctionAddress("glCreateVertexArrays");
        this.glCreateVertexArrays = functionAddress78;
        final boolean b78 = b77 & functionAddress78 != 0L;
        final long functionAddress79 = GLContext.getFunctionAddress("glDisableVertexArrayAttrib");
        this.glDisableVertexArrayAttrib = functionAddress79;
        final boolean b79 = b78 & functionAddress79 != 0L;
        final long functionAddress80 = GLContext.getFunctionAddress("glEnableVertexArrayAttrib");
        this.glEnableVertexArrayAttrib = functionAddress80;
        final boolean b80 = b79 & functionAddress80 != 0L;
        final long functionAddress81 = GLContext.getFunctionAddress("glVertexArrayElementBuffer");
        this.glVertexArrayElementBuffer = functionAddress81;
        final boolean b81 = b80 & functionAddress81 != 0L;
        final long functionAddress82 = GLContext.getFunctionAddress("glVertexArrayVertexBuffer");
        this.glVertexArrayVertexBuffer = functionAddress82;
        final boolean b82 = b81 & functionAddress82 != 0L;
        final long functionAddress83 = GLContext.getFunctionAddress("glVertexArrayVertexBuffers");
        this.glVertexArrayVertexBuffers = functionAddress83;
        final boolean b83 = b82 & functionAddress83 != 0L;
        final long functionAddress84 = GLContext.getFunctionAddress("glVertexArrayAttribFormat");
        this.glVertexArrayAttribFormat = functionAddress84;
        final boolean b84 = b83 & functionAddress84 != 0L;
        final long functionAddress85 = GLContext.getFunctionAddress("glVertexArrayAttribIFormat");
        this.glVertexArrayAttribIFormat = functionAddress85;
        final boolean b85 = b84 & functionAddress85 != 0L;
        final long functionAddress86 = GLContext.getFunctionAddress("glVertexArrayAttribLFormat");
        this.glVertexArrayAttribLFormat = functionAddress86;
        final boolean b86 = b85 & functionAddress86 != 0L;
        final long functionAddress87 = GLContext.getFunctionAddress("glVertexArrayAttribBinding");
        this.glVertexArrayAttribBinding = functionAddress87;
        final boolean b87 = b86 & functionAddress87 != 0L;
        final long functionAddress88 = GLContext.getFunctionAddress("glVertexArrayBindingDivisor");
        this.glVertexArrayBindingDivisor = functionAddress88;
        final boolean b88 = b87 & functionAddress88 != 0L;
        final long functionAddress89 = GLContext.getFunctionAddress("glGetVertexArrayiv");
        this.glGetVertexArrayiv = functionAddress89;
        final boolean b89 = b88 & functionAddress89 != 0L;
        final long functionAddress90 = GLContext.getFunctionAddress("glGetVertexArrayIndexediv");
        this.glGetVertexArrayIndexediv = functionAddress90;
        final boolean b90 = b89 & functionAddress90 != 0L;
        final long functionAddress91 = GLContext.getFunctionAddress("glGetVertexArrayIndexed64iv");
        this.glGetVertexArrayIndexed64iv = functionAddress91;
        final boolean b91 = b90 & functionAddress91 != 0L;
        final long functionAddress92 = GLContext.getFunctionAddress("glCreateSamplers");
        this.glCreateSamplers = functionAddress92;
        final boolean b92 = b91 & functionAddress92 != 0L;
        final long functionAddress93 = GLContext.getFunctionAddress("glCreateProgramPipelines");
        this.glCreateProgramPipelines = functionAddress93;
        final boolean b93 = b92 & functionAddress93 != 0L;
        final long functionAddress94 = GLContext.getFunctionAddress("glCreateQueries");
        this.glCreateQueries = functionAddress94;
        final boolean b94 = b93 & functionAddress94 != 0L;
        final long functionAddress95 = GLContext.getFunctionAddress("glMemoryBarrierByRegion");
        this.glMemoryBarrierByRegion = functionAddress95;
        final boolean b95 = b94 & functionAddress95 != 0L;
        final long functionAddress96 = GLContext.getFunctionAddress("glGetTextureSubImage");
        this.glGetTextureSubImage = functionAddress96;
        final boolean b96 = b95 & functionAddress96 != 0L;
        final long functionAddress97 = GLContext.getFunctionAddress("glGetCompressedTextureSubImage");
        this.glGetCompressedTextureSubImage = functionAddress97;
        final boolean b97 = b96 & functionAddress97 != 0L;
        final long functionAddress98 = GLContext.getFunctionAddress("glTextureBarrier");
        this.glTextureBarrier = functionAddress98;
        final boolean b98 = b97 & functionAddress98 != 0L;
        final long functionAddress99 = GLContext.getFunctionAddress("glGetGraphicsResetStatus");
        this.glGetGraphicsResetStatus = functionAddress99;
        final boolean b99 = b98 & functionAddress99 != 0L;
        final long functionAddress100 = GLContext.getFunctionAddress("glReadnPixels");
        this.glReadnPixels = functionAddress100;
        final boolean b100 = b99 & functionAddress100 != 0L;
        final long functionAddress101 = GLContext.getFunctionAddress("glGetnUniformfv");
        this.glGetnUniformfv = functionAddress101;
        final boolean b101 = b100 & functionAddress101 != 0L;
        final long functionAddress102 = GLContext.getFunctionAddress("glGetnUniformiv");
        this.glGetnUniformiv = functionAddress102;
        final boolean b102 = b101 & functionAddress102 != 0L;
        final long functionAddress103 = GLContext.getFunctionAddress("glGetnUniformuiv");
        this.glGetnUniformuiv = functionAddress103;
        return b102 & functionAddress103 != 0L;
    }
    
    private boolean GREMEDY_frame_terminator_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glFrameTerminatorGREMEDY");
        this.glFrameTerminatorGREMEDY = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean GREMEDY_string_marker_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glStringMarkerGREMEDY");
        this.glStringMarkerGREMEDY = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean INTEL_map_texture_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glMapTexture2DINTEL");
        this.glMapTexture2DINTEL = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glUnmapTexture2DINTEL");
        this.glUnmapTexture2DINTEL = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glSyncTextureINTEL");
        this.glSyncTextureINTEL = functionAddress3;
        return b2 & functionAddress3 != 0L;
    }
    
    private boolean KHR_debug_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glDebugMessageControl");
        this.glDebugMessageControl = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glDebugMessageInsert");
        this.glDebugMessageInsert = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glDebugMessageCallback");
        this.glDebugMessageCallback = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glGetDebugMessageLog");
        this.glGetDebugMessageLog = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glPushDebugGroup");
        this.glPushDebugGroup = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glPopDebugGroup");
        this.glPopDebugGroup = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glObjectLabel");
        this.glObjectLabel = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glGetObjectLabel");
        this.glGetObjectLabel = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glObjectPtrLabel");
        this.glObjectPtrLabel = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glGetObjectPtrLabel");
        this.glGetObjectPtrLabel = functionAddress10;
        return b9 & functionAddress10 != 0L;
    }
    
    private boolean KHR_robustness_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glGetGraphicsResetStatus");
        this.glGetGraphicsResetStatus = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glReadnPixels");
        this.glReadnPixels = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glGetnUniformfv");
        this.glGetnUniformfv = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glGetnUniformiv");
        this.glGetnUniformiv = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glGetnUniformuiv");
        this.glGetnUniformuiv = functionAddress5;
        return b4 & functionAddress5 != 0L;
    }
    
    private boolean NV_bindless_multi_draw_indirect_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glMultiDrawArraysIndirectBindlessNV");
        this.glMultiDrawArraysIndirectBindlessNV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glMultiDrawElementsIndirectBindlessNV");
        this.glMultiDrawElementsIndirectBindlessNV = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean NV_bindless_texture_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glGetTextureHandleNV");
        this.glGetTextureHandleNV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glGetTextureSamplerHandleNV");
        this.glGetTextureSamplerHandleNV = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glMakeTextureHandleResidentNV");
        this.glMakeTextureHandleResidentNV = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glMakeTextureHandleNonResidentNV");
        this.glMakeTextureHandleNonResidentNV = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glGetImageHandleNV");
        this.glGetImageHandleNV = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glMakeImageHandleResidentNV");
        this.glMakeImageHandleResidentNV = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glMakeImageHandleNonResidentNV");
        this.glMakeImageHandleNonResidentNV = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glUniformHandleui64NV");
        this.glUniformHandleui64NV = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glUniformHandleui64vNV");
        this.glUniformHandleui64vNV = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glProgramUniformHandleui64NV");
        this.glProgramUniformHandleui64NV = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glProgramUniformHandleui64vNV");
        this.glProgramUniformHandleui64vNV = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glIsTextureHandleResidentNV");
        this.glIsTextureHandleResidentNV = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glIsImageHandleResidentNV");
        this.glIsImageHandleResidentNV = functionAddress13;
        return b12 & functionAddress13 != 0L;
    }
    
    private boolean NV_blend_equation_advanced_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glBlendParameteriNV");
        this.glBlendParameteriNV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glBlendBarrierNV");
        this.glBlendBarrierNV = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean NV_conditional_render_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glBeginConditionalRenderNV");
        this.glBeginConditionalRenderNV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glEndConditionalRenderNV");
        this.glEndConditionalRenderNV = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean NV_copy_image_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glCopyImageSubDataNV");
        this.glCopyImageSubDataNV = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean NV_depth_buffer_float_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glDepthRangedNV");
        this.glDepthRangedNV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glClearDepthdNV");
        this.glClearDepthdNV = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glDepthBoundsdNV");
        this.glDepthBoundsdNV = functionAddress3;
        return b2 & functionAddress3 != 0L;
    }
    
    private boolean NV_draw_texture_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glDrawTextureNV");
        this.glDrawTextureNV = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean NV_evaluators_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glGetMapControlPointsNV");
        this.glGetMapControlPointsNV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glMapControlPointsNV");
        this.glMapControlPointsNV = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glMapParameterfvNV");
        this.glMapParameterfvNV = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glMapParameterivNV");
        this.glMapParameterivNV = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glGetMapParameterfvNV");
        this.glGetMapParameterfvNV = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glGetMapParameterivNV");
        this.glGetMapParameterivNV = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glGetMapAttribParameterfvNV");
        this.glGetMapAttribParameterfvNV = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glGetMapAttribParameterivNV");
        this.glGetMapAttribParameterivNV = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glEvalMapsNV");
        this.glEvalMapsNV = functionAddress9;
        return b8 & functionAddress9 != 0L;
    }
    
    private boolean NV_explicit_multisample_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glGetBooleanIndexedvEXT");
        this.glGetBooleanIndexedvEXT = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glGetIntegerIndexedvEXT");
        this.glGetIntegerIndexedvEXT = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glGetMultisamplefvNV");
        this.glGetMultisamplefvNV = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glSampleMaskIndexedNV");
        this.glSampleMaskIndexedNV = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glTexRenderbufferNV");
        this.glTexRenderbufferNV = functionAddress5;
        return b4 & functionAddress5 != 0L;
    }
    
    private boolean NV_fence_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glGenFencesNV");
        this.glGenFencesNV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glDeleteFencesNV");
        this.glDeleteFencesNV = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glSetFenceNV");
        this.glSetFenceNV = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glTestFenceNV");
        this.glTestFenceNV = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glFinishFenceNV");
        this.glFinishFenceNV = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glIsFenceNV");
        this.glIsFenceNV = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glGetFenceivNV");
        this.glGetFenceivNV = functionAddress7;
        return b6 & functionAddress7 != 0L;
    }
    
    private boolean NV_fragment_program_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glProgramNamedParameter4fNV");
        this.glProgramNamedParameter4fNV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glProgramNamedParameter4dNV");
        this.glProgramNamedParameter4dNV = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glGetProgramNamedParameterfvNV");
        this.glGetProgramNamedParameterfvNV = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glGetProgramNamedParameterdvNV");
        this.glGetProgramNamedParameterdvNV = functionAddress4;
        return b3 & functionAddress4 != 0L;
    }
    
    private boolean NV_framebuffer_multisample_coverage_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glRenderbufferStorageMultisampleCoverageNV");
        this.glRenderbufferStorageMultisampleCoverageNV = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean NV_geometry_program4_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glProgramVertexLimitNV");
        this.glProgramVertexLimitNV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glFramebufferTextureEXT");
        this.glFramebufferTextureEXT = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glFramebufferTextureLayerEXT");
        this.glFramebufferTextureLayerEXT = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glFramebufferTextureFaceEXT");
        this.glFramebufferTextureFaceEXT = functionAddress4;
        return b3 & functionAddress4 != 0L;
    }
    
    private boolean NV_gpu_program4_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glProgramLocalParameterI4iNV");
        this.glProgramLocalParameterI4iNV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glProgramLocalParameterI4ivNV");
        this.glProgramLocalParameterI4ivNV = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glProgramLocalParametersI4ivNV");
        this.glProgramLocalParametersI4ivNV = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glProgramLocalParameterI4uiNV");
        this.glProgramLocalParameterI4uiNV = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glProgramLocalParameterI4uivNV");
        this.glProgramLocalParameterI4uivNV = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glProgramLocalParametersI4uivNV");
        this.glProgramLocalParametersI4uivNV = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glProgramEnvParameterI4iNV");
        this.glProgramEnvParameterI4iNV = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glProgramEnvParameterI4ivNV");
        this.glProgramEnvParameterI4ivNV = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glProgramEnvParametersI4ivNV");
        this.glProgramEnvParametersI4ivNV = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glProgramEnvParameterI4uiNV");
        this.glProgramEnvParameterI4uiNV = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glProgramEnvParameterI4uivNV");
        this.glProgramEnvParameterI4uivNV = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glProgramEnvParametersI4uivNV");
        this.glProgramEnvParametersI4uivNV = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glGetProgramLocalParameterIivNV");
        this.glGetProgramLocalParameterIivNV = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glGetProgramLocalParameterIuivNV");
        this.glGetProgramLocalParameterIuivNV = functionAddress14;
        final boolean b14 = b13 & functionAddress14 != 0L;
        final long functionAddress15 = GLContext.getFunctionAddress("glGetProgramEnvParameterIivNV");
        this.glGetProgramEnvParameterIivNV = functionAddress15;
        final boolean b15 = b14 & functionAddress15 != 0L;
        final long functionAddress16 = GLContext.getFunctionAddress("glGetProgramEnvParameterIuivNV");
        this.glGetProgramEnvParameterIuivNV = functionAddress16;
        return b15 & functionAddress16 != 0L;
    }
    
    private boolean NV_gpu_shader5_initNativeFunctionAddresses(final Set<String> supported_extensions) {
        final long functionAddress = GLContext.getFunctionAddress("glUniform1i64NV");
        this.glUniform1i64NV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glUniform2i64NV");
        this.glUniform2i64NV = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glUniform3i64NV");
        this.glUniform3i64NV = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glUniform4i64NV");
        this.glUniform4i64NV = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glUniform1i64vNV");
        this.glUniform1i64vNV = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glUniform2i64vNV");
        this.glUniform2i64vNV = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glUniform3i64vNV");
        this.glUniform3i64vNV = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glUniform4i64vNV");
        this.glUniform4i64vNV = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glUniform1ui64NV");
        this.glUniform1ui64NV = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glUniform2ui64NV");
        this.glUniform2ui64NV = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glUniform3ui64NV");
        this.glUniform3ui64NV = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glUniform4ui64NV");
        this.glUniform4ui64NV = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glUniform1ui64vNV");
        this.glUniform1ui64vNV = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glUniform2ui64vNV");
        this.glUniform2ui64vNV = functionAddress14;
        final boolean b14 = b13 & functionAddress14 != 0L;
        final long functionAddress15 = GLContext.getFunctionAddress("glUniform3ui64vNV");
        this.glUniform3ui64vNV = functionAddress15;
        final boolean b15 = b14 & functionAddress15 != 0L;
        final long functionAddress16 = GLContext.getFunctionAddress("glUniform4ui64vNV");
        this.glUniform4ui64vNV = functionAddress16;
        final boolean b16 = b15 & functionAddress16 != 0L;
        final long functionAddress17 = GLContext.getFunctionAddress("glGetUniformi64vNV");
        this.glGetUniformi64vNV = functionAddress17;
        final boolean b17 = b16 & functionAddress17 != 0L;
        final long functionAddress18 = GLContext.getFunctionAddress("glGetUniformui64vNV");
        this.glGetUniformui64vNV = functionAddress18;
        final boolean b18 = b17 & functionAddress18 != 0L;
        boolean b19 = false;
        Label_0427: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress19 = GLContext.getFunctionAddress("glProgramUniform1i64NV");
                this.glProgramUniform1i64NV = functionAddress19;
                if (functionAddress19 == 0L) {
                    b19 = false;
                    break Label_0427;
                }
            }
            b19 = true;
        }
        final boolean b20 = b18 & b19;
        boolean b21 = false;
        Label_0460: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress20 = GLContext.getFunctionAddress("glProgramUniform2i64NV");
                this.glProgramUniform2i64NV = functionAddress20;
                if (functionAddress20 == 0L) {
                    b21 = false;
                    break Label_0460;
                }
            }
            b21 = true;
        }
        final boolean b22 = b20 & b21;
        boolean b23 = false;
        Label_0493: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress21 = GLContext.getFunctionAddress("glProgramUniform3i64NV");
                this.glProgramUniform3i64NV = functionAddress21;
                if (functionAddress21 == 0L) {
                    b23 = false;
                    break Label_0493;
                }
            }
            b23 = true;
        }
        final boolean b24 = b22 & b23;
        boolean b25 = false;
        Label_0526: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress22 = GLContext.getFunctionAddress("glProgramUniform4i64NV");
                this.glProgramUniform4i64NV = functionAddress22;
                if (functionAddress22 == 0L) {
                    b25 = false;
                    break Label_0526;
                }
            }
            b25 = true;
        }
        final boolean b26 = b24 & b25;
        boolean b27 = false;
        Label_0559: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress23 = GLContext.getFunctionAddress("glProgramUniform1i64vNV");
                this.glProgramUniform1i64vNV = functionAddress23;
                if (functionAddress23 == 0L) {
                    b27 = false;
                    break Label_0559;
                }
            }
            b27 = true;
        }
        final boolean b28 = b26 & b27;
        boolean b29 = false;
        Label_0592: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress24 = GLContext.getFunctionAddress("glProgramUniform2i64vNV");
                this.glProgramUniform2i64vNV = functionAddress24;
                if (functionAddress24 == 0L) {
                    b29 = false;
                    break Label_0592;
                }
            }
            b29 = true;
        }
        final boolean b30 = b28 & b29;
        boolean b31 = false;
        Label_0625: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress25 = GLContext.getFunctionAddress("glProgramUniform3i64vNV");
                this.glProgramUniform3i64vNV = functionAddress25;
                if (functionAddress25 == 0L) {
                    b31 = false;
                    break Label_0625;
                }
            }
            b31 = true;
        }
        final boolean b32 = b30 & b31;
        boolean b33 = false;
        Label_0658: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress26 = GLContext.getFunctionAddress("glProgramUniform4i64vNV");
                this.glProgramUniform4i64vNV = functionAddress26;
                if (functionAddress26 == 0L) {
                    b33 = false;
                    break Label_0658;
                }
            }
            b33 = true;
        }
        final boolean b34 = b32 & b33;
        boolean b35 = false;
        Label_0691: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress27 = GLContext.getFunctionAddress("glProgramUniform1ui64NV");
                this.glProgramUniform1ui64NV = functionAddress27;
                if (functionAddress27 == 0L) {
                    b35 = false;
                    break Label_0691;
                }
            }
            b35 = true;
        }
        final boolean b36 = b34 & b35;
        boolean b37 = false;
        Label_0724: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress28 = GLContext.getFunctionAddress("glProgramUniform2ui64NV");
                this.glProgramUniform2ui64NV = functionAddress28;
                if (functionAddress28 == 0L) {
                    b37 = false;
                    break Label_0724;
                }
            }
            b37 = true;
        }
        final boolean b38 = b36 & b37;
        boolean b39 = false;
        Label_0757: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress29 = GLContext.getFunctionAddress("glProgramUniform3ui64NV");
                this.glProgramUniform3ui64NV = functionAddress29;
                if (functionAddress29 == 0L) {
                    b39 = false;
                    break Label_0757;
                }
            }
            b39 = true;
        }
        final boolean b40 = b38 & b39;
        boolean b41 = false;
        Label_0790: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress30 = GLContext.getFunctionAddress("glProgramUniform4ui64NV");
                this.glProgramUniform4ui64NV = functionAddress30;
                if (functionAddress30 == 0L) {
                    b41 = false;
                    break Label_0790;
                }
            }
            b41 = true;
        }
        final boolean b42 = b40 & b41;
        boolean b43 = false;
        Label_0823: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress31 = GLContext.getFunctionAddress("glProgramUniform1ui64vNV");
                this.glProgramUniform1ui64vNV = functionAddress31;
                if (functionAddress31 == 0L) {
                    b43 = false;
                    break Label_0823;
                }
            }
            b43 = true;
        }
        final boolean b44 = b42 & b43;
        boolean b45 = false;
        Label_0856: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress32 = GLContext.getFunctionAddress("glProgramUniform2ui64vNV");
                this.glProgramUniform2ui64vNV = functionAddress32;
                if (functionAddress32 == 0L) {
                    b45 = false;
                    break Label_0856;
                }
            }
            b45 = true;
        }
        final boolean b46 = b44 & b45;
        boolean b47 = false;
        Label_0889: {
            if (supported_extensions.contains("GL_EXT_direct_state_access")) {
                final long functionAddress33 = GLContext.getFunctionAddress("glProgramUniform3ui64vNV");
                this.glProgramUniform3ui64vNV = functionAddress33;
                if (functionAddress33 == 0L) {
                    b47 = false;
                    break Label_0889;
                }
            }
            b47 = true;
        }
        final boolean b48 = b46 & b47;
        if (supported_extensions.contains("GL_EXT_direct_state_access")) {
            final long functionAddress34 = GLContext.getFunctionAddress("glProgramUniform4ui64vNV");
            this.glProgramUniform4ui64vNV = functionAddress34;
            if (functionAddress34 == 0L) {
                final boolean b49 = false;
                return b48 & b49;
            }
        }
        final boolean b49 = true;
        return b48 & b49;
    }
    
    private boolean NV_half_float_initNativeFunctionAddresses(final Set<String> supported_extensions) {
        final long functionAddress = GLContext.getFunctionAddress("glVertex2hNV");
        this.glVertex2hNV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glVertex3hNV");
        this.glVertex3hNV = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glVertex4hNV");
        this.glVertex4hNV = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glNormal3hNV");
        this.glNormal3hNV = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glColor3hNV");
        this.glColor3hNV = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glColor4hNV");
        this.glColor4hNV = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glTexCoord1hNV");
        this.glTexCoord1hNV = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glTexCoord2hNV");
        this.glTexCoord2hNV = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glTexCoord3hNV");
        this.glTexCoord3hNV = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glTexCoord4hNV");
        this.glTexCoord4hNV = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glMultiTexCoord1hNV");
        this.glMultiTexCoord1hNV = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glMultiTexCoord2hNV");
        this.glMultiTexCoord2hNV = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glMultiTexCoord3hNV");
        this.glMultiTexCoord3hNV = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glMultiTexCoord4hNV");
        this.glMultiTexCoord4hNV = functionAddress14;
        final boolean b14 = b13 & functionAddress14 != 0L;
        boolean b15 = false;
        Label_0340: {
            if (supported_extensions.contains("GL_EXT_fog_coord")) {
                final long functionAddress15 = GLContext.getFunctionAddress("glFogCoordhNV");
                this.glFogCoordhNV = functionAddress15;
                if (functionAddress15 == 0L) {
                    b15 = false;
                    break Label_0340;
                }
            }
            b15 = true;
        }
        final boolean b16 = b14 & b15;
        boolean b17 = false;
        Label_0374: {
            if (supported_extensions.contains("GL_EXT_secondary_color")) {
                final long functionAddress16 = GLContext.getFunctionAddress("glSecondaryColor3hNV");
                this.glSecondaryColor3hNV = functionAddress16;
                if (functionAddress16 == 0L) {
                    b17 = false;
                    break Label_0374;
                }
            }
            b17 = true;
        }
        final boolean b18 = b16 & b17;
        boolean b19 = false;
        Label_0408: {
            if (supported_extensions.contains("GL_EXT_vertex_weighting")) {
                final long functionAddress17 = GLContext.getFunctionAddress("glVertexWeighthNV");
                this.glVertexWeighthNV = functionAddress17;
                if (functionAddress17 == 0L) {
                    b19 = false;
                    break Label_0408;
                }
            }
            b19 = true;
        }
        final boolean b20 = b18 & b19;
        boolean b21 = false;
        Label_0442: {
            if (supported_extensions.contains("GL_NV_vertex_program")) {
                final long functionAddress18 = GLContext.getFunctionAddress("glVertexAttrib1hNV");
                this.glVertexAttrib1hNV = functionAddress18;
                if (functionAddress18 == 0L) {
                    b21 = false;
                    break Label_0442;
                }
            }
            b21 = true;
        }
        final boolean b22 = b20 & b21;
        boolean b23 = false;
        Label_0476: {
            if (supported_extensions.contains("GL_NV_vertex_program")) {
                final long functionAddress19 = GLContext.getFunctionAddress("glVertexAttrib2hNV");
                this.glVertexAttrib2hNV = functionAddress19;
                if (functionAddress19 == 0L) {
                    b23 = false;
                    break Label_0476;
                }
            }
            b23 = true;
        }
        final boolean b24 = b22 & b23;
        boolean b25 = false;
        Label_0510: {
            if (supported_extensions.contains("GL_NV_vertex_program")) {
                final long functionAddress20 = GLContext.getFunctionAddress("glVertexAttrib3hNV");
                this.glVertexAttrib3hNV = functionAddress20;
                if (functionAddress20 == 0L) {
                    b25 = false;
                    break Label_0510;
                }
            }
            b25 = true;
        }
        final boolean b26 = b24 & b25;
        boolean b27 = false;
        Label_0544: {
            if (supported_extensions.contains("GL_NV_vertex_program")) {
                final long functionAddress21 = GLContext.getFunctionAddress("glVertexAttrib4hNV");
                this.glVertexAttrib4hNV = functionAddress21;
                if (functionAddress21 == 0L) {
                    b27 = false;
                    break Label_0544;
                }
            }
            b27 = true;
        }
        final boolean b28 = b26 & b27;
        boolean b29 = false;
        Label_0578: {
            if (supported_extensions.contains("GL_NV_vertex_program")) {
                final long functionAddress22 = GLContext.getFunctionAddress("glVertexAttribs1hvNV");
                this.glVertexAttribs1hvNV = functionAddress22;
                if (functionAddress22 == 0L) {
                    b29 = false;
                    break Label_0578;
                }
            }
            b29 = true;
        }
        final boolean b30 = b28 & b29;
        boolean b31 = false;
        Label_0612: {
            if (supported_extensions.contains("GL_NV_vertex_program")) {
                final long functionAddress23 = GLContext.getFunctionAddress("glVertexAttribs2hvNV");
                this.glVertexAttribs2hvNV = functionAddress23;
                if (functionAddress23 == 0L) {
                    b31 = false;
                    break Label_0612;
                }
            }
            b31 = true;
        }
        final boolean b32 = b30 & b31;
        boolean b33 = false;
        Label_0646: {
            if (supported_extensions.contains("GL_NV_vertex_program")) {
                final long functionAddress24 = GLContext.getFunctionAddress("glVertexAttribs3hvNV");
                this.glVertexAttribs3hvNV = functionAddress24;
                if (functionAddress24 == 0L) {
                    b33 = false;
                    break Label_0646;
                }
            }
            b33 = true;
        }
        final boolean b34 = b32 & b33;
        if (supported_extensions.contains("GL_NV_vertex_program")) {
            final long functionAddress25 = GLContext.getFunctionAddress("glVertexAttribs4hvNV");
            this.glVertexAttribs4hvNV = functionAddress25;
            if (functionAddress25 == 0L) {
                final boolean b35 = false;
                return b34 & b35;
            }
        }
        final boolean b35 = true;
        return b34 & b35;
    }
    
    private boolean NV_occlusion_query_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glGenOcclusionQueriesNV");
        this.glGenOcclusionQueriesNV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glDeleteOcclusionQueriesNV");
        this.glDeleteOcclusionQueriesNV = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glIsOcclusionQueryNV");
        this.glIsOcclusionQueryNV = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glBeginOcclusionQueryNV");
        this.glBeginOcclusionQueryNV = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glEndOcclusionQueryNV");
        this.glEndOcclusionQueryNV = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glGetOcclusionQueryuivNV");
        this.glGetOcclusionQueryuivNV = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glGetOcclusionQueryivNV");
        this.glGetOcclusionQueryivNV = functionAddress7;
        return b6 & functionAddress7 != 0L;
    }
    
    private boolean NV_parameter_buffer_object_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glProgramBufferParametersfvNV");
        this.glProgramBufferParametersfvNV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glProgramBufferParametersIivNV");
        this.glProgramBufferParametersIivNV = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glProgramBufferParametersIuivNV");
        this.glProgramBufferParametersIuivNV = functionAddress3;
        return b2 & functionAddress3 != 0L;
    }
    
    private boolean NV_path_rendering_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glPathCommandsNV");
        this.glPathCommandsNV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glPathCoordsNV");
        this.glPathCoordsNV = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glPathSubCommandsNV");
        this.glPathSubCommandsNV = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glPathSubCoordsNV");
        this.glPathSubCoordsNV = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glPathStringNV");
        this.glPathStringNV = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glPathGlyphsNV");
        this.glPathGlyphsNV = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glPathGlyphRangeNV");
        this.glPathGlyphRangeNV = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glWeightPathsNV");
        this.glWeightPathsNV = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glCopyPathNV");
        this.glCopyPathNV = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glInterpolatePathsNV");
        this.glInterpolatePathsNV = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glTransformPathNV");
        this.glTransformPathNV = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glPathParameterivNV");
        this.glPathParameterivNV = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glPathParameteriNV");
        this.glPathParameteriNV = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glPathParameterfvNV");
        this.glPathParameterfvNV = functionAddress14;
        final boolean b14 = b13 & functionAddress14 != 0L;
        final long functionAddress15 = GLContext.getFunctionAddress("glPathParameterfNV");
        this.glPathParameterfNV = functionAddress15;
        final boolean b15 = b14 & functionAddress15 != 0L;
        final long functionAddress16 = GLContext.getFunctionAddress("glPathDashArrayNV");
        this.glPathDashArrayNV = functionAddress16;
        final boolean b16 = b15 & functionAddress16 != 0L;
        final long functionAddress17 = GLContext.getFunctionAddress("glGenPathsNV");
        this.glGenPathsNV = functionAddress17;
        final boolean b17 = b16 & functionAddress17 != 0L;
        final long functionAddress18 = GLContext.getFunctionAddress("glDeletePathsNV");
        this.glDeletePathsNV = functionAddress18;
        final boolean b18 = b17 & functionAddress18 != 0L;
        final long functionAddress19 = GLContext.getFunctionAddress("glIsPathNV");
        this.glIsPathNV = functionAddress19;
        final boolean b19 = b18 & functionAddress19 != 0L;
        final long functionAddress20 = GLContext.getFunctionAddress("glPathStencilFuncNV");
        this.glPathStencilFuncNV = functionAddress20;
        final boolean b20 = b19 & functionAddress20 != 0L;
        final long functionAddress21 = GLContext.getFunctionAddress("glPathStencilDepthOffsetNV");
        this.glPathStencilDepthOffsetNV = functionAddress21;
        final boolean b21 = b20 & functionAddress21 != 0L;
        final long functionAddress22 = GLContext.getFunctionAddress("glStencilFillPathNV");
        this.glStencilFillPathNV = functionAddress22;
        final boolean b22 = b21 & functionAddress22 != 0L;
        final long functionAddress23 = GLContext.getFunctionAddress("glStencilStrokePathNV");
        this.glStencilStrokePathNV = functionAddress23;
        final boolean b23 = b22 & functionAddress23 != 0L;
        final long functionAddress24 = GLContext.getFunctionAddress("glStencilFillPathInstancedNV");
        this.glStencilFillPathInstancedNV = functionAddress24;
        final boolean b24 = b23 & functionAddress24 != 0L;
        final long functionAddress25 = GLContext.getFunctionAddress("glStencilStrokePathInstancedNV");
        this.glStencilStrokePathInstancedNV = functionAddress25;
        final boolean b25 = b24 & functionAddress25 != 0L;
        final long functionAddress26 = GLContext.getFunctionAddress("glPathCoverDepthFuncNV");
        this.glPathCoverDepthFuncNV = functionAddress26;
        final boolean b26 = b25 & functionAddress26 != 0L;
        final long functionAddress27 = GLContext.getFunctionAddress("glPathColorGenNV");
        this.glPathColorGenNV = functionAddress27;
        final boolean b27 = b26 & functionAddress27 != 0L;
        final long functionAddress28 = GLContext.getFunctionAddress("glPathTexGenNV");
        this.glPathTexGenNV = functionAddress28;
        final boolean b28 = b27 & functionAddress28 != 0L;
        final long functionAddress29 = GLContext.getFunctionAddress("glPathFogGenNV");
        this.glPathFogGenNV = functionAddress29;
        final boolean b29 = b28 & functionAddress29 != 0L;
        final long functionAddress30 = GLContext.getFunctionAddress("glCoverFillPathNV");
        this.glCoverFillPathNV = functionAddress30;
        final boolean b30 = b29 & functionAddress30 != 0L;
        final long functionAddress31 = GLContext.getFunctionAddress("glCoverStrokePathNV");
        this.glCoverStrokePathNV = functionAddress31;
        final boolean b31 = b30 & functionAddress31 != 0L;
        final long functionAddress32 = GLContext.getFunctionAddress("glCoverFillPathInstancedNV");
        this.glCoverFillPathInstancedNV = functionAddress32;
        final boolean b32 = b31 & functionAddress32 != 0L;
        final long functionAddress33 = GLContext.getFunctionAddress("glCoverStrokePathInstancedNV");
        this.glCoverStrokePathInstancedNV = functionAddress33;
        final boolean b33 = b32 & functionAddress33 != 0L;
        final long functionAddress34 = GLContext.getFunctionAddress("glGetPathParameterivNV");
        this.glGetPathParameterivNV = functionAddress34;
        final boolean b34 = b33 & functionAddress34 != 0L;
        final long functionAddress35 = GLContext.getFunctionAddress("glGetPathParameterfvNV");
        this.glGetPathParameterfvNV = functionAddress35;
        final boolean b35 = b34 & functionAddress35 != 0L;
        final long functionAddress36 = GLContext.getFunctionAddress("glGetPathCommandsNV");
        this.glGetPathCommandsNV = functionAddress36;
        final boolean b36 = b35 & functionAddress36 != 0L;
        final long functionAddress37 = GLContext.getFunctionAddress("glGetPathCoordsNV");
        this.glGetPathCoordsNV = functionAddress37;
        final boolean b37 = b36 & functionAddress37 != 0L;
        final long functionAddress38 = GLContext.getFunctionAddress("glGetPathDashArrayNV");
        this.glGetPathDashArrayNV = functionAddress38;
        final boolean b38 = b37 & functionAddress38 != 0L;
        final long functionAddress39 = GLContext.getFunctionAddress("glGetPathMetricsNV");
        this.glGetPathMetricsNV = functionAddress39;
        final boolean b39 = b38 & functionAddress39 != 0L;
        final long functionAddress40 = GLContext.getFunctionAddress("glGetPathMetricRangeNV");
        this.glGetPathMetricRangeNV = functionAddress40;
        final boolean b40 = b39 & functionAddress40 != 0L;
        final long functionAddress41 = GLContext.getFunctionAddress("glGetPathSpacingNV");
        this.glGetPathSpacingNV = functionAddress41;
        final boolean b41 = b40 & functionAddress41 != 0L;
        final long functionAddress42 = GLContext.getFunctionAddress("glGetPathColorGenivNV");
        this.glGetPathColorGenivNV = functionAddress42;
        final boolean b42 = b41 & functionAddress42 != 0L;
        final long functionAddress43 = GLContext.getFunctionAddress("glGetPathColorGenfvNV");
        this.glGetPathColorGenfvNV = functionAddress43;
        final boolean b43 = b42 & functionAddress43 != 0L;
        final long functionAddress44 = GLContext.getFunctionAddress("glGetPathTexGenivNV");
        this.glGetPathTexGenivNV = functionAddress44;
        final boolean b44 = b43 & functionAddress44 != 0L;
        final long functionAddress45 = GLContext.getFunctionAddress("glGetPathTexGenfvNV");
        this.glGetPathTexGenfvNV = functionAddress45;
        final boolean b45 = b44 & functionAddress45 != 0L;
        final long functionAddress46 = GLContext.getFunctionAddress("glIsPointInFillPathNV");
        this.glIsPointInFillPathNV = functionAddress46;
        final boolean b46 = b45 & functionAddress46 != 0L;
        final long functionAddress47 = GLContext.getFunctionAddress("glIsPointInStrokePathNV");
        this.glIsPointInStrokePathNV = functionAddress47;
        final boolean b47 = b46 & functionAddress47 != 0L;
        final long functionAddress48 = GLContext.getFunctionAddress("glGetPathLengthNV");
        this.glGetPathLengthNV = functionAddress48;
        final boolean b48 = b47 & functionAddress48 != 0L;
        final long functionAddress49 = GLContext.getFunctionAddress("glPointAlongPathNV");
        this.glPointAlongPathNV = functionAddress49;
        return b48 & functionAddress49 != 0L;
    }
    
    private boolean NV_pixel_data_range_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glPixelDataRangeNV");
        this.glPixelDataRangeNV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glFlushPixelDataRangeNV");
        this.glFlushPixelDataRangeNV = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean NV_point_sprite_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glPointParameteriNV");
        this.glPointParameteriNV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glPointParameterivNV");
        this.glPointParameterivNV = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean NV_present_video_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glPresentFrameKeyedNV");
        this.glPresentFrameKeyedNV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glPresentFrameDualFillNV");
        this.glPresentFrameDualFillNV = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glGetVideoivNV");
        this.glGetVideoivNV = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glGetVideouivNV");
        this.glGetVideouivNV = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glGetVideoi64vNV");
        this.glGetVideoi64vNV = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glGetVideoui64vNV");
        this.glGetVideoui64vNV = functionAddress6;
        return b5 & functionAddress6 != 0L;
    }
    
    private boolean NV_primitive_restart_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glPrimitiveRestartNV");
        this.glPrimitiveRestartNV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glPrimitiveRestartIndexNV");
        this.glPrimitiveRestartIndexNV = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean NV_program_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glLoadProgramNV");
        this.glLoadProgramNV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glBindProgramNV");
        this.glBindProgramNV = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glDeleteProgramsNV");
        this.glDeleteProgramsNV = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glGenProgramsNV");
        this.glGenProgramsNV = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glGetProgramivNV");
        this.glGetProgramivNV = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glGetProgramStringNV");
        this.glGetProgramStringNV = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glIsProgramNV");
        this.glIsProgramNV = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glAreProgramsResidentNV");
        this.glAreProgramsResidentNV = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glRequestResidentProgramsNV");
        this.glRequestResidentProgramsNV = functionAddress9;
        return b8 & functionAddress9 != 0L;
    }
    
    private boolean NV_register_combiners_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glCombinerParameterfNV");
        this.glCombinerParameterfNV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glCombinerParameterfvNV");
        this.glCombinerParameterfvNV = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glCombinerParameteriNV");
        this.glCombinerParameteriNV = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glCombinerParameterivNV");
        this.glCombinerParameterivNV = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glCombinerInputNV");
        this.glCombinerInputNV = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glCombinerOutputNV");
        this.glCombinerOutputNV = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glFinalCombinerInputNV");
        this.glFinalCombinerInputNV = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glGetCombinerInputParameterfvNV");
        this.glGetCombinerInputParameterfvNV = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glGetCombinerInputParameterivNV");
        this.glGetCombinerInputParameterivNV = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glGetCombinerOutputParameterfvNV");
        this.glGetCombinerOutputParameterfvNV = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glGetCombinerOutputParameterivNV");
        this.glGetCombinerOutputParameterivNV = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glGetFinalCombinerInputParameterfvNV");
        this.glGetFinalCombinerInputParameterfvNV = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glGetFinalCombinerInputParameterivNV");
        this.glGetFinalCombinerInputParameterivNV = functionAddress13;
        return b12 & functionAddress13 != 0L;
    }
    
    private boolean NV_register_combiners2_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glCombinerStageParameterfvNV");
        this.glCombinerStageParameterfvNV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glGetCombinerStageParameterfvNV");
        this.glGetCombinerStageParameterfvNV = functionAddress2;
        return b & functionAddress2 != 0L;
    }
    
    private boolean NV_shader_buffer_load_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glMakeBufferResidentNV");
        this.glMakeBufferResidentNV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glMakeBufferNonResidentNV");
        this.glMakeBufferNonResidentNV = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glIsBufferResidentNV");
        this.glIsBufferResidentNV = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glMakeNamedBufferResidentNV");
        this.glMakeNamedBufferResidentNV = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glMakeNamedBufferNonResidentNV");
        this.glMakeNamedBufferNonResidentNV = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glIsNamedBufferResidentNV");
        this.glIsNamedBufferResidentNV = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glGetBufferParameterui64vNV");
        this.glGetBufferParameterui64vNV = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glGetNamedBufferParameterui64vNV");
        this.glGetNamedBufferParameterui64vNV = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glGetIntegerui64vNV");
        this.glGetIntegerui64vNV = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glUniformui64NV");
        this.glUniformui64NV = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glUniformui64vNV");
        this.glUniformui64vNV = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glGetUniformui64vNV");
        this.glGetUniformui64vNV = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glProgramUniformui64NV");
        this.glProgramUniformui64NV = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glProgramUniformui64vNV");
        this.glProgramUniformui64vNV = functionAddress14;
        return b13 & functionAddress14 != 0L;
    }
    
    private boolean NV_texture_barrier_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glTextureBarrierNV");
        this.glTextureBarrierNV = functionAddress;
        return functionAddress != 0L;
    }
    
    private boolean NV_texture_multisample_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glTexImage2DMultisampleCoverageNV");
        this.glTexImage2DMultisampleCoverageNV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glTexImage3DMultisampleCoverageNV");
        this.glTexImage3DMultisampleCoverageNV = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glTextureImage2DMultisampleNV");
        this.glTextureImage2DMultisampleNV = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glTextureImage3DMultisampleNV");
        this.glTextureImage3DMultisampleNV = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glTextureImage2DMultisampleCoverageNV");
        this.glTextureImage2DMultisampleCoverageNV = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glTextureImage3DMultisampleCoverageNV");
        this.glTextureImage3DMultisampleCoverageNV = functionAddress6;
        return b5 & functionAddress6 != 0L;
    }
    
    private boolean NV_transform_feedback_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glBindBufferRangeNV");
        this.glBindBufferRangeNV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glBindBufferOffsetNV");
        this.glBindBufferOffsetNV = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glBindBufferBaseNV");
        this.glBindBufferBaseNV = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glTransformFeedbackAttribsNV");
        this.glTransformFeedbackAttribsNV = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glTransformFeedbackVaryingsNV");
        this.glTransformFeedbackVaryingsNV = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glBeginTransformFeedbackNV");
        this.glBeginTransformFeedbackNV = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glEndTransformFeedbackNV");
        this.glEndTransformFeedbackNV = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glGetVaryingLocationNV");
        this.glGetVaryingLocationNV = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glGetActiveVaryingNV");
        this.glGetActiveVaryingNV = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glActiveVaryingNV");
        this.glActiveVaryingNV = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glGetTransformFeedbackVaryingNV");
        this.glGetTransformFeedbackVaryingNV = functionAddress11;
        return b10 & functionAddress11 != 0L;
    }
    
    private boolean NV_transform_feedback2_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glBindTransformFeedbackNV");
        this.glBindTransformFeedbackNV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glDeleteTransformFeedbacksNV");
        this.glDeleteTransformFeedbacksNV = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glGenTransformFeedbacksNV");
        this.glGenTransformFeedbacksNV = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glIsTransformFeedbackNV");
        this.glIsTransformFeedbackNV = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glPauseTransformFeedbackNV");
        this.glPauseTransformFeedbackNV = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glResumeTransformFeedbackNV");
        this.glResumeTransformFeedbackNV = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glDrawTransformFeedbackNV");
        this.glDrawTransformFeedbackNV = functionAddress7;
        return b6 & functionAddress7 != 0L;
    }
    
    private boolean NV_vertex_array_range_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glVertexArrayRangeNV");
        this.glVertexArrayRangeNV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glFlushVertexArrayRangeNV");
        this.glFlushVertexArrayRangeNV = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long platformSpecificFunctionAddress = GLContext.getPlatformSpecificFunctionAddress("gl", new String[] { "Windows", "Linux" }, new String[] { "wgl", "glX" }, "glAllocateMemoryNV");
        this.glAllocateMemoryNV = platformSpecificFunctionAddress;
        final boolean b3 = b2 & platformSpecificFunctionAddress != 0L;
        final long platformSpecificFunctionAddress2 = GLContext.getPlatformSpecificFunctionAddress("gl", new String[] { "Windows", "Linux" }, new String[] { "wgl", "glX" }, "glFreeMemoryNV");
        this.glFreeMemoryNV = platformSpecificFunctionAddress2;
        return b3 & platformSpecificFunctionAddress2 != 0L;
    }
    
    private boolean NV_vertex_attrib_integer_64bit_initNativeFunctionAddresses(final Set<String> supported_extensions) {
        final long functionAddress = GLContext.getFunctionAddress("glVertexAttribL1i64NV");
        this.glVertexAttribL1i64NV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glVertexAttribL2i64NV");
        this.glVertexAttribL2i64NV = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glVertexAttribL3i64NV");
        this.glVertexAttribL3i64NV = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glVertexAttribL4i64NV");
        this.glVertexAttribL4i64NV = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glVertexAttribL1i64vNV");
        this.glVertexAttribL1i64vNV = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glVertexAttribL2i64vNV");
        this.glVertexAttribL2i64vNV = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glVertexAttribL3i64vNV");
        this.glVertexAttribL3i64vNV = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glVertexAttribL4i64vNV");
        this.glVertexAttribL4i64vNV = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glVertexAttribL1ui64NV");
        this.glVertexAttribL1ui64NV = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glVertexAttribL2ui64NV");
        this.glVertexAttribL2ui64NV = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glVertexAttribL3ui64NV");
        this.glVertexAttribL3ui64NV = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glVertexAttribL4ui64NV");
        this.glVertexAttribL4ui64NV = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glVertexAttribL1ui64vNV");
        this.glVertexAttribL1ui64vNV = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glVertexAttribL2ui64vNV");
        this.glVertexAttribL2ui64vNV = functionAddress14;
        final boolean b14 = b13 & functionAddress14 != 0L;
        final long functionAddress15 = GLContext.getFunctionAddress("glVertexAttribL3ui64vNV");
        this.glVertexAttribL3ui64vNV = functionAddress15;
        final boolean b15 = b14 & functionAddress15 != 0L;
        final long functionAddress16 = GLContext.getFunctionAddress("glVertexAttribL4ui64vNV");
        this.glVertexAttribL4ui64vNV = functionAddress16;
        final boolean b16 = b15 & functionAddress16 != 0L;
        final long functionAddress17 = GLContext.getFunctionAddress("glGetVertexAttribLi64vNV");
        this.glGetVertexAttribLi64vNV = functionAddress17;
        final boolean b17 = b16 & functionAddress17 != 0L;
        final long functionAddress18 = GLContext.getFunctionAddress("glGetVertexAttribLui64vNV");
        this.glGetVertexAttribLui64vNV = functionAddress18;
        final boolean b18 = b17 & functionAddress18 != 0L;
        if (supported_extensions.contains("GL_NV_vertex_buffer_unified_memory")) {
            final long functionAddress19 = GLContext.getFunctionAddress("glVertexAttribLFormatNV");
            this.glVertexAttribLFormatNV = functionAddress19;
            if (functionAddress19 == 0L) {
                final boolean b19 = false;
                return b18 & b19;
            }
        }
        final boolean b19 = true;
        return b18 & b19;
    }
    
    private boolean NV_vertex_buffer_unified_memory_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glBufferAddressRangeNV");
        this.glBufferAddressRangeNV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glVertexFormatNV");
        this.glVertexFormatNV = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glNormalFormatNV");
        this.glNormalFormatNV = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glColorFormatNV");
        this.glColorFormatNV = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glIndexFormatNV");
        this.glIndexFormatNV = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glTexCoordFormatNV");
        this.glTexCoordFormatNV = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glEdgeFlagFormatNV");
        this.glEdgeFlagFormatNV = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glSecondaryColorFormatNV");
        this.glSecondaryColorFormatNV = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glFogCoordFormatNV");
        this.glFogCoordFormatNV = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glVertexAttribFormatNV");
        this.glVertexAttribFormatNV = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glVertexAttribIFormatNV");
        this.glVertexAttribIFormatNV = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glGetIntegerui64i_vNV");
        this.glGetIntegerui64i_vNV = functionAddress12;
        return b11 & functionAddress12 != 0L;
    }
    
    private boolean NV_vertex_program_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glExecuteProgramNV");
        this.glExecuteProgramNV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glGetProgramParameterfvNV");
        this.glGetProgramParameterfvNV = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glGetProgramParameterdvNV");
        this.glGetProgramParameterdvNV = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glGetTrackMatrixivNV");
        this.glGetTrackMatrixivNV = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glGetVertexAttribfvNV");
        this.glGetVertexAttribfvNV = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glGetVertexAttribdvNV");
        this.glGetVertexAttribdvNV = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glGetVertexAttribivNV");
        this.glGetVertexAttribivNV = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glGetVertexAttribPointervNV");
        this.glGetVertexAttribPointervNV = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glProgramParameter4fNV");
        this.glProgramParameter4fNV = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glProgramParameter4dNV");
        this.glProgramParameter4dNV = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glProgramParameters4fvNV");
        this.glProgramParameters4fvNV = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glProgramParameters4dvNV");
        this.glProgramParameters4dvNV = functionAddress12;
        final boolean b12 = b11 & functionAddress12 != 0L;
        final long functionAddress13 = GLContext.getFunctionAddress("glTrackMatrixNV");
        this.glTrackMatrixNV = functionAddress13;
        final boolean b13 = b12 & functionAddress13 != 0L;
        final long functionAddress14 = GLContext.getFunctionAddress("glVertexAttribPointerNV");
        this.glVertexAttribPointerNV = functionAddress14;
        final boolean b14 = b13 & functionAddress14 != 0L;
        final long functionAddress15 = GLContext.getFunctionAddress("glVertexAttrib1sNV");
        this.glVertexAttrib1sNV = functionAddress15;
        final boolean b15 = b14 & functionAddress15 != 0L;
        final long functionAddress16 = GLContext.getFunctionAddress("glVertexAttrib1fNV");
        this.glVertexAttrib1fNV = functionAddress16;
        final boolean b16 = b15 & functionAddress16 != 0L;
        final long functionAddress17 = GLContext.getFunctionAddress("glVertexAttrib1dNV");
        this.glVertexAttrib1dNV = functionAddress17;
        final boolean b17 = b16 & functionAddress17 != 0L;
        final long functionAddress18 = GLContext.getFunctionAddress("glVertexAttrib2sNV");
        this.glVertexAttrib2sNV = functionAddress18;
        final boolean b18 = b17 & functionAddress18 != 0L;
        final long functionAddress19 = GLContext.getFunctionAddress("glVertexAttrib2fNV");
        this.glVertexAttrib2fNV = functionAddress19;
        final boolean b19 = b18 & functionAddress19 != 0L;
        final long functionAddress20 = GLContext.getFunctionAddress("glVertexAttrib2dNV");
        this.glVertexAttrib2dNV = functionAddress20;
        final boolean b20 = b19 & functionAddress20 != 0L;
        final long functionAddress21 = GLContext.getFunctionAddress("glVertexAttrib3sNV");
        this.glVertexAttrib3sNV = functionAddress21;
        final boolean b21 = b20 & functionAddress21 != 0L;
        final long functionAddress22 = GLContext.getFunctionAddress("glVertexAttrib3fNV");
        this.glVertexAttrib3fNV = functionAddress22;
        final boolean b22 = b21 & functionAddress22 != 0L;
        final long functionAddress23 = GLContext.getFunctionAddress("glVertexAttrib3dNV");
        this.glVertexAttrib3dNV = functionAddress23;
        final boolean b23 = b22 & functionAddress23 != 0L;
        final long functionAddress24 = GLContext.getFunctionAddress("glVertexAttrib4sNV");
        this.glVertexAttrib4sNV = functionAddress24;
        final boolean b24 = b23 & functionAddress24 != 0L;
        final long functionAddress25 = GLContext.getFunctionAddress("glVertexAttrib4fNV");
        this.glVertexAttrib4fNV = functionAddress25;
        final boolean b25 = b24 & functionAddress25 != 0L;
        final long functionAddress26 = GLContext.getFunctionAddress("glVertexAttrib4dNV");
        this.glVertexAttrib4dNV = functionAddress26;
        final boolean b26 = b25 & functionAddress26 != 0L;
        final long functionAddress27 = GLContext.getFunctionAddress("glVertexAttrib4ubNV");
        this.glVertexAttrib4ubNV = functionAddress27;
        final boolean b27 = b26 & functionAddress27 != 0L;
        final long functionAddress28 = GLContext.getFunctionAddress("glVertexAttribs1svNV");
        this.glVertexAttribs1svNV = functionAddress28;
        final boolean b28 = b27 & functionAddress28 != 0L;
        final long functionAddress29 = GLContext.getFunctionAddress("glVertexAttribs1fvNV");
        this.glVertexAttribs1fvNV = functionAddress29;
        final boolean b29 = b28 & functionAddress29 != 0L;
        final long functionAddress30 = GLContext.getFunctionAddress("glVertexAttribs1dvNV");
        this.glVertexAttribs1dvNV = functionAddress30;
        final boolean b30 = b29 & functionAddress30 != 0L;
        final long functionAddress31 = GLContext.getFunctionAddress("glVertexAttribs2svNV");
        this.glVertexAttribs2svNV = functionAddress31;
        final boolean b31 = b30 & functionAddress31 != 0L;
        final long functionAddress32 = GLContext.getFunctionAddress("glVertexAttribs2fvNV");
        this.glVertexAttribs2fvNV = functionAddress32;
        final boolean b32 = b31 & functionAddress32 != 0L;
        final long functionAddress33 = GLContext.getFunctionAddress("glVertexAttribs2dvNV");
        this.glVertexAttribs2dvNV = functionAddress33;
        final boolean b33 = b32 & functionAddress33 != 0L;
        final long functionAddress34 = GLContext.getFunctionAddress("glVertexAttribs3svNV");
        this.glVertexAttribs3svNV = functionAddress34;
        final boolean b34 = b33 & functionAddress34 != 0L;
        final long functionAddress35 = GLContext.getFunctionAddress("glVertexAttribs3fvNV");
        this.glVertexAttribs3fvNV = functionAddress35;
        final boolean b35 = b34 & functionAddress35 != 0L;
        final long functionAddress36 = GLContext.getFunctionAddress("glVertexAttribs3dvNV");
        this.glVertexAttribs3dvNV = functionAddress36;
        final boolean b36 = b35 & functionAddress36 != 0L;
        final long functionAddress37 = GLContext.getFunctionAddress("glVertexAttribs4svNV");
        this.glVertexAttribs4svNV = functionAddress37;
        final boolean b37 = b36 & functionAddress37 != 0L;
        final long functionAddress38 = GLContext.getFunctionAddress("glVertexAttribs4fvNV");
        this.glVertexAttribs4fvNV = functionAddress38;
        final boolean b38 = b37 & functionAddress38 != 0L;
        final long functionAddress39 = GLContext.getFunctionAddress("glVertexAttribs4dvNV");
        this.glVertexAttribs4dvNV = functionAddress39;
        return b38 & functionAddress39 != 0L;
    }
    
    private boolean NV_video_capture_initNativeFunctionAddresses() {
        final long functionAddress = GLContext.getFunctionAddress("glBeginVideoCaptureNV");
        this.glBeginVideoCaptureNV = functionAddress;
        final boolean b = functionAddress != 0L;
        final long functionAddress2 = GLContext.getFunctionAddress("glBindVideoCaptureStreamBufferNV");
        this.glBindVideoCaptureStreamBufferNV = functionAddress2;
        final boolean b2 = b & functionAddress2 != 0L;
        final long functionAddress3 = GLContext.getFunctionAddress("glBindVideoCaptureStreamTextureNV");
        this.glBindVideoCaptureStreamTextureNV = functionAddress3;
        final boolean b3 = b2 & functionAddress3 != 0L;
        final long functionAddress4 = GLContext.getFunctionAddress("glEndVideoCaptureNV");
        this.glEndVideoCaptureNV = functionAddress4;
        final boolean b4 = b3 & functionAddress4 != 0L;
        final long functionAddress5 = GLContext.getFunctionAddress("glGetVideoCaptureivNV");
        this.glGetVideoCaptureivNV = functionAddress5;
        final boolean b5 = b4 & functionAddress5 != 0L;
        final long functionAddress6 = GLContext.getFunctionAddress("glGetVideoCaptureStreamivNV");
        this.glGetVideoCaptureStreamivNV = functionAddress6;
        final boolean b6 = b5 & functionAddress6 != 0L;
        final long functionAddress7 = GLContext.getFunctionAddress("glGetVideoCaptureStreamfvNV");
        this.glGetVideoCaptureStreamfvNV = functionAddress7;
        final boolean b7 = b6 & functionAddress7 != 0L;
        final long functionAddress8 = GLContext.getFunctionAddress("glGetVideoCaptureStreamdvNV");
        this.glGetVideoCaptureStreamdvNV = functionAddress8;
        final boolean b8 = b7 & functionAddress8 != 0L;
        final long functionAddress9 = GLContext.getFunctionAddress("glVideoCaptureNV");
        this.glVideoCaptureNV = functionAddress9;
        final boolean b9 = b8 & functionAddress9 != 0L;
        final long functionAddress10 = GLContext.getFunctionAddress("glVideoCaptureStreamParameterivNV");
        this.glVideoCaptureStreamParameterivNV = functionAddress10;
        final boolean b10 = b9 & functionAddress10 != 0L;
        final long functionAddress11 = GLContext.getFunctionAddress("glVideoCaptureStreamParameterfvNV");
        this.glVideoCaptureStreamParameterfvNV = functionAddress11;
        final boolean b11 = b10 & functionAddress11 != 0L;
        final long functionAddress12 = GLContext.getFunctionAddress("glVideoCaptureStreamParameterdvNV");
        this.glVideoCaptureStreamParameterdvNV = functionAddress12;
        return b11 & functionAddress12 != 0L;
    }
    
    private static void remove(final Set supported_extensions, final String extension) {
        LWJGLUtil.log(extension + " was reported as available but an entry point is missing");
        supported_extensions.remove(extension);
    }
    
    private Set<String> initAllStubs(boolean forwardCompatible) throws LWJGLException {
        this.glGetError = GLContext.getFunctionAddress("glGetError");
        this.glGetString = GLContext.getFunctionAddress("glGetString");
        this.glGetIntegerv = GLContext.getFunctionAddress("glGetIntegerv");
        this.glGetStringi = GLContext.getFunctionAddress("glGetStringi");
        GLContext.setCapabilities(this);
        final Set<String> supported_extensions = new HashSet<String>(256);
        final int profileMask = GLContext.getSupportedExtensions(supported_extensions);
        if (supported_extensions.contains("OpenGL31") && !supported_extensions.contains("GL_ARB_compatibility") && (profileMask & 0x2) == 0x0) {
            forwardCompatible = true;
        }
        if (!this.GL11_initNativeFunctionAddresses(forwardCompatible)) {
            throw new LWJGLException("GL11 not supported");
        }
        if (supported_extensions.contains("GL_ARB_fragment_program")) {
            supported_extensions.add("GL_ARB_program");
        }
        if (supported_extensions.contains("GL_ARB_pixel_buffer_object")) {
            supported_extensions.add("GL_ARB_buffer_object");
        }
        if (supported_extensions.contains("GL_ARB_vertex_buffer_object")) {
            supported_extensions.add("GL_ARB_buffer_object");
        }
        if (supported_extensions.contains("GL_ARB_vertex_program")) {
            supported_extensions.add("GL_ARB_program");
        }
        if (supported_extensions.contains("GL_EXT_pixel_buffer_object")) {
            supported_extensions.add("GL_ARB_buffer_object");
        }
        if (supported_extensions.contains("GL_NV_fragment_program")) {
            supported_extensions.add("GL_NV_program");
        }
        if (supported_extensions.contains("GL_NV_vertex_program")) {
            supported_extensions.add("GL_NV_program");
        }
        if ((supported_extensions.contains("GL_AMD_debug_output") || supported_extensions.contains("GL_AMDX_debug_output")) && !this.AMD_debug_output_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_AMDX_debug_output");
            remove(supported_extensions, "GL_AMD_debug_output");
        }
        if (supported_extensions.contains("GL_AMD_draw_buffers_blend") && !this.AMD_draw_buffers_blend_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_AMD_draw_buffers_blend");
        }
        if (supported_extensions.contains("GL_AMD_interleaved_elements") && !this.AMD_interleaved_elements_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_AMD_interleaved_elements");
        }
        if (supported_extensions.contains("GL_AMD_multi_draw_indirect") && !this.AMD_multi_draw_indirect_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_AMD_multi_draw_indirect");
        }
        if (supported_extensions.contains("GL_AMD_name_gen_delete") && !this.AMD_name_gen_delete_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_AMD_name_gen_delete");
        }
        if (supported_extensions.contains("GL_AMD_performance_monitor") && !this.AMD_performance_monitor_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_AMD_performance_monitor");
        }
        if (supported_extensions.contains("GL_AMD_sample_positions") && !this.AMD_sample_positions_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_AMD_sample_positions");
        }
        if (supported_extensions.contains("GL_AMD_sparse_texture") && !this.AMD_sparse_texture_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_AMD_sparse_texture");
        }
        if (supported_extensions.contains("GL_AMD_stencil_operation_extended") && !this.AMD_stencil_operation_extended_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_AMD_stencil_operation_extended");
        }
        if (supported_extensions.contains("GL_AMD_vertex_shader_tessellator") && !this.AMD_vertex_shader_tessellator_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_AMD_vertex_shader_tessellator");
        }
        if (supported_extensions.contains("GL_APPLE_element_array") && !this.APPLE_element_array_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_APPLE_element_array");
        }
        if (supported_extensions.contains("GL_APPLE_fence") && !this.APPLE_fence_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_APPLE_fence");
        }
        if (supported_extensions.contains("GL_APPLE_flush_buffer_range") && !this.APPLE_flush_buffer_range_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_APPLE_flush_buffer_range");
        }
        if (supported_extensions.contains("GL_APPLE_object_purgeable") && !this.APPLE_object_purgeable_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_APPLE_object_purgeable");
        }
        if (supported_extensions.contains("GL_APPLE_texture_range") && !this.APPLE_texture_range_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_APPLE_texture_range");
        }
        if (supported_extensions.contains("GL_APPLE_vertex_array_object") && !this.APPLE_vertex_array_object_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_APPLE_vertex_array_object");
        }
        if (supported_extensions.contains("GL_APPLE_vertex_array_range") && !this.APPLE_vertex_array_range_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_APPLE_vertex_array_range");
        }
        if (supported_extensions.contains("GL_APPLE_vertex_program_evaluators") && !this.APPLE_vertex_program_evaluators_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_APPLE_vertex_program_evaluators");
        }
        if (supported_extensions.contains("GL_ARB_ES2_compatibility") && !this.ARB_ES2_compatibility_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_ES2_compatibility");
        }
        if (supported_extensions.contains("GL_ARB_ES3_1_compatibility") && !this.ARB_ES3_1_compatibility_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_ES3_1_compatibility");
        }
        if (supported_extensions.contains("GL_ARB_base_instance") && !this.ARB_base_instance_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_base_instance");
        }
        if (supported_extensions.contains("GL_ARB_bindless_texture") && !this.ARB_bindless_texture_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_bindless_texture");
        }
        if (supported_extensions.contains("GL_ARB_blend_func_extended") && !this.ARB_blend_func_extended_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_blend_func_extended");
        }
        if (supported_extensions.contains("GL_ARB_buffer_object") && !this.ARB_buffer_object_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_buffer_object");
        }
        if (supported_extensions.contains("GL_ARB_buffer_storage") && !this.ARB_buffer_storage_initNativeFunctionAddresses(supported_extensions)) {
            remove(supported_extensions, "GL_ARB_buffer_storage");
        }
        if (supported_extensions.contains("GL_ARB_cl_event") && !this.ARB_cl_event_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_cl_event");
        }
        if (supported_extensions.contains("GL_ARB_clear_buffer_object") && !this.ARB_clear_buffer_object_initNativeFunctionAddresses(supported_extensions)) {
            remove(supported_extensions, "GL_ARB_clear_buffer_object");
        }
        if (supported_extensions.contains("GL_ARB_clear_texture") && !this.ARB_clear_texture_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_clear_texture");
        }
        if (supported_extensions.contains("GL_ARB_clip_control") && !this.ARB_clip_control_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_clip_control");
        }
        if (supported_extensions.contains("GL_ARB_color_buffer_float") && !this.ARB_color_buffer_float_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_color_buffer_float");
        }
        if (supported_extensions.contains("GL_ARB_compute_shader") && !this.ARB_compute_shader_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_compute_shader");
        }
        if (supported_extensions.contains("GL_ARB_compute_variable_group_size") && !this.ARB_compute_variable_group_size_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_compute_variable_group_size");
        }
        if (supported_extensions.contains("GL_ARB_copy_buffer") && !this.ARB_copy_buffer_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_copy_buffer");
        }
        if (supported_extensions.contains("GL_ARB_copy_image") && !this.ARB_copy_image_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_copy_image");
        }
        if (supported_extensions.contains("GL_ARB_debug_output") && !this.ARB_debug_output_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_debug_output");
        }
        if (supported_extensions.contains("GL_ARB_direct_state_access") && !this.ARB_direct_state_access_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_direct_state_access");
        }
        if (supported_extensions.contains("GL_ARB_draw_buffers") && !this.ARB_draw_buffers_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_draw_buffers");
        }
        if (supported_extensions.contains("GL_ARB_draw_buffers_blend") && !this.ARB_draw_buffers_blend_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_draw_buffers_blend");
        }
        if (supported_extensions.contains("GL_ARB_draw_elements_base_vertex") && !this.ARB_draw_elements_base_vertex_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_draw_elements_base_vertex");
        }
        if (supported_extensions.contains("GL_ARB_draw_indirect") && !this.ARB_draw_indirect_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_draw_indirect");
        }
        if (supported_extensions.contains("GL_ARB_draw_instanced") && !this.ARB_draw_instanced_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_draw_instanced");
        }
        if (supported_extensions.contains("GL_ARB_framebuffer_no_attachments") && !this.ARB_framebuffer_no_attachments_initNativeFunctionAddresses(supported_extensions)) {
            remove(supported_extensions, "GL_ARB_framebuffer_no_attachments");
        }
        if (supported_extensions.contains("GL_ARB_framebuffer_object") && !this.ARB_framebuffer_object_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_framebuffer_object");
        }
        if (supported_extensions.contains("GL_ARB_geometry_shader4") && !this.ARB_geometry_shader4_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_geometry_shader4");
        }
        if (supported_extensions.contains("GL_ARB_get_program_binary") && !this.ARB_get_program_binary_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_get_program_binary");
        }
        if (supported_extensions.contains("GL_ARB_get_texture_sub_image") && !this.ARB_get_texture_sub_image_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_get_texture_sub_image");
        }
        if (supported_extensions.contains("GL_ARB_gpu_shader_fp64") && !this.ARB_gpu_shader_fp64_initNativeFunctionAddresses(supported_extensions)) {
            remove(supported_extensions, "GL_ARB_gpu_shader_fp64");
        }
        if (supported_extensions.contains("GL_ARB_imaging") && !this.ARB_imaging_initNativeFunctionAddresses(forwardCompatible)) {
            remove(supported_extensions, "GL_ARB_imaging");
        }
        if (supported_extensions.contains("GL_ARB_indirect_parameters") && !this.ARB_indirect_parameters_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_indirect_parameters");
        }
        if (supported_extensions.contains("GL_ARB_instanced_arrays") && !this.ARB_instanced_arrays_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_instanced_arrays");
        }
        if (supported_extensions.contains("GL_ARB_internalformat_query") && !this.ARB_internalformat_query_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_internalformat_query");
        }
        if (supported_extensions.contains("GL_ARB_internalformat_query2") && !this.ARB_internalformat_query2_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_internalformat_query2");
        }
        if (supported_extensions.contains("GL_ARB_invalidate_subdata") && !this.ARB_invalidate_subdata_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_invalidate_subdata");
        }
        if (supported_extensions.contains("GL_ARB_map_buffer_range") && !this.ARB_map_buffer_range_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_map_buffer_range");
        }
        if (supported_extensions.contains("GL_ARB_matrix_palette") && !this.ARB_matrix_palette_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_matrix_palette");
        }
        if (supported_extensions.contains("GL_ARB_multi_bind") && !this.ARB_multi_bind_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_multi_bind");
        }
        if (supported_extensions.contains("GL_ARB_multi_draw_indirect") && !this.ARB_multi_draw_indirect_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_multi_draw_indirect");
        }
        if (supported_extensions.contains("GL_ARB_multisample") && !this.ARB_multisample_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_multisample");
        }
        if (supported_extensions.contains("GL_ARB_multitexture") && !this.ARB_multitexture_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_multitexture");
        }
        if (supported_extensions.contains("GL_ARB_occlusion_query") && !this.ARB_occlusion_query_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_occlusion_query");
        }
        if (supported_extensions.contains("GL_ARB_point_parameters") && !this.ARB_point_parameters_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_point_parameters");
        }
        if (supported_extensions.contains("GL_ARB_program") && !this.ARB_program_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_program");
        }
        if (supported_extensions.contains("GL_ARB_program_interface_query") && !this.ARB_program_interface_query_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_program_interface_query");
        }
        if (supported_extensions.contains("GL_ARB_provoking_vertex") && !this.ARB_provoking_vertex_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_provoking_vertex");
        }
        if (supported_extensions.contains("GL_ARB_robustness") && !this.ARB_robustness_initNativeFunctionAddresses(forwardCompatible, supported_extensions)) {
            remove(supported_extensions, "GL_ARB_robustness");
        }
        if (supported_extensions.contains("GL_ARB_sample_shading") && !this.ARB_sample_shading_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_sample_shading");
        }
        if (supported_extensions.contains("GL_ARB_sampler_objects") && !this.ARB_sampler_objects_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_sampler_objects");
        }
        if (supported_extensions.contains("GL_ARB_separate_shader_objects") && !this.ARB_separate_shader_objects_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_separate_shader_objects");
        }
        if (supported_extensions.contains("GL_ARB_shader_atomic_counters") && !this.ARB_shader_atomic_counters_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_shader_atomic_counters");
        }
        if (supported_extensions.contains("GL_ARB_shader_image_load_store") && !this.ARB_shader_image_load_store_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_shader_image_load_store");
        }
        if (supported_extensions.contains("GL_ARB_shader_objects") && !this.ARB_shader_objects_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_shader_objects");
        }
        if (supported_extensions.contains("GL_ARB_shader_storage_buffer_object") && !this.ARB_shader_storage_buffer_object_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_shader_storage_buffer_object");
        }
        if (supported_extensions.contains("GL_ARB_shader_subroutine") && !this.ARB_shader_subroutine_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_shader_subroutine");
        }
        if (supported_extensions.contains("GL_ARB_shading_language_include") && !this.ARB_shading_language_include_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_shading_language_include");
        }
        if (supported_extensions.contains("GL_ARB_sparse_buffer") && !this.ARB_sparse_buffer_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_sparse_buffer");
        }
        if (supported_extensions.contains("GL_ARB_sparse_texture") && !this.ARB_sparse_texture_initNativeFunctionAddresses(supported_extensions)) {
            remove(supported_extensions, "GL_ARB_sparse_texture");
        }
        if (supported_extensions.contains("GL_ARB_sync") && !this.ARB_sync_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_sync");
        }
        if (supported_extensions.contains("GL_ARB_tessellation_shader") && !this.ARB_tessellation_shader_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_tessellation_shader");
        }
        if (supported_extensions.contains("GL_ARB_texture_barrier") && !this.ARB_texture_barrier_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_texture_barrier");
        }
        if (supported_extensions.contains("GL_ARB_texture_buffer_object") && !this.ARB_texture_buffer_object_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_texture_buffer_object");
        }
        if (supported_extensions.contains("GL_ARB_texture_buffer_range") && !this.ARB_texture_buffer_range_initNativeFunctionAddresses(supported_extensions)) {
            remove(supported_extensions, "GL_ARB_texture_buffer_range");
        }
        if (supported_extensions.contains("GL_ARB_texture_compression") && !this.ARB_texture_compression_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_texture_compression");
        }
        if (supported_extensions.contains("GL_ARB_texture_multisample") && !this.ARB_texture_multisample_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_texture_multisample");
        }
        if ((supported_extensions.contains("GL_ARB_texture_storage") || supported_extensions.contains("GL_EXT_texture_storage")) && !this.ARB_texture_storage_initNativeFunctionAddresses(supported_extensions)) {
            remove(supported_extensions, "GL_EXT_texture_storage");
            remove(supported_extensions, "GL_ARB_texture_storage");
        }
        if (supported_extensions.contains("GL_ARB_texture_storage_multisample") && !this.ARB_texture_storage_multisample_initNativeFunctionAddresses(supported_extensions)) {
            remove(supported_extensions, "GL_ARB_texture_storage_multisample");
        }
        if (supported_extensions.contains("GL_ARB_texture_view") && !this.ARB_texture_view_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_texture_view");
        }
        if (supported_extensions.contains("GL_ARB_timer_query") && !this.ARB_timer_query_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_timer_query");
        }
        if (supported_extensions.contains("GL_ARB_transform_feedback2") && !this.ARB_transform_feedback2_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_transform_feedback2");
        }
        if (supported_extensions.contains("GL_ARB_transform_feedback3") && !this.ARB_transform_feedback3_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_transform_feedback3");
        }
        if (supported_extensions.contains("GL_ARB_transform_feedback_instanced") && !this.ARB_transform_feedback_instanced_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_transform_feedback_instanced");
        }
        if (supported_extensions.contains("GL_ARB_transpose_matrix") && !this.ARB_transpose_matrix_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_transpose_matrix");
        }
        if (supported_extensions.contains("GL_ARB_uniform_buffer_object") && !this.ARB_uniform_buffer_object_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_uniform_buffer_object");
        }
        if (supported_extensions.contains("GL_ARB_vertex_array_object") && !this.ARB_vertex_array_object_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_vertex_array_object");
        }
        if (supported_extensions.contains("GL_ARB_vertex_attrib_64bit") && !this.ARB_vertex_attrib_64bit_initNativeFunctionAddresses(supported_extensions)) {
            remove(supported_extensions, "GL_ARB_vertex_attrib_64bit");
        }
        if (supported_extensions.contains("GL_ARB_vertex_attrib_binding") && !this.ARB_vertex_attrib_binding_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_vertex_attrib_binding");
        }
        if (supported_extensions.contains("GL_ARB_vertex_blend") && !this.ARB_vertex_blend_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_vertex_blend");
        }
        if (supported_extensions.contains("GL_ARB_vertex_program") && !this.ARB_vertex_program_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_vertex_program");
        }
        if (supported_extensions.contains("GL_ARB_vertex_shader") && !this.ARB_vertex_shader_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_vertex_shader");
        }
        if (supported_extensions.contains("GL_ARB_vertex_type_2_10_10_10_rev") && !this.ARB_vertex_type_2_10_10_10_rev_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_vertex_type_2_10_10_10_rev");
        }
        if (supported_extensions.contains("GL_ARB_viewport_array") && !this.ARB_viewport_array_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ARB_viewport_array");
        }
        if (supported_extensions.contains("GL_ARB_window_pos") && !this.ARB_window_pos_initNativeFunctionAddresses(forwardCompatible)) {
            remove(supported_extensions, "GL_ARB_window_pos");
        }
        if (supported_extensions.contains("GL_ATI_draw_buffers") && !this.ATI_draw_buffers_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ATI_draw_buffers");
        }
        if (supported_extensions.contains("GL_ATI_element_array") && !this.ATI_element_array_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ATI_element_array");
        }
        if (supported_extensions.contains("GL_ATI_envmap_bumpmap") && !this.ATI_envmap_bumpmap_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ATI_envmap_bumpmap");
        }
        if (supported_extensions.contains("GL_ATI_fragment_shader") && !this.ATI_fragment_shader_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ATI_fragment_shader");
        }
        if (supported_extensions.contains("GL_ATI_map_object_buffer") && !this.ATI_map_object_buffer_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ATI_map_object_buffer");
        }
        if (supported_extensions.contains("GL_ATI_pn_triangles") && !this.ATI_pn_triangles_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ATI_pn_triangles");
        }
        if (supported_extensions.contains("GL_ATI_separate_stencil") && !this.ATI_separate_stencil_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ATI_separate_stencil");
        }
        if (supported_extensions.contains("GL_ATI_vertex_array_object") && !this.ATI_vertex_array_object_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ATI_vertex_array_object");
        }
        if (supported_extensions.contains("GL_ATI_vertex_attrib_array_object") && !this.ATI_vertex_attrib_array_object_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ATI_vertex_attrib_array_object");
        }
        if (supported_extensions.contains("GL_ATI_vertex_streams") && !this.ATI_vertex_streams_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_ATI_vertex_streams");
        }
        if (supported_extensions.contains("GL_EXT_bindable_uniform") && !this.EXT_bindable_uniform_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_bindable_uniform");
        }
        if (supported_extensions.contains("GL_EXT_blend_color") && !this.EXT_blend_color_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_blend_color");
        }
        if (supported_extensions.contains("GL_EXT_blend_equation_separate") && !this.EXT_blend_equation_separate_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_blend_equation_separate");
        }
        if (supported_extensions.contains("GL_EXT_blend_func_separate") && !this.EXT_blend_func_separate_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_blend_func_separate");
        }
        if (supported_extensions.contains("GL_EXT_blend_minmax") && !this.EXT_blend_minmax_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_blend_minmax");
        }
        if (supported_extensions.contains("GL_EXT_compiled_vertex_array") && !this.EXT_compiled_vertex_array_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_compiled_vertex_array");
        }
        if (supported_extensions.contains("GL_EXT_depth_bounds_test") && !this.EXT_depth_bounds_test_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_depth_bounds_test");
        }
        supported_extensions.add("GL_EXT_direct_state_access");
        if (supported_extensions.contains("GL_EXT_direct_state_access") && !this.EXT_direct_state_access_initNativeFunctionAddresses(forwardCompatible, supported_extensions)) {
            remove(supported_extensions, "GL_EXT_direct_state_access");
        }
        if (supported_extensions.contains("GL_EXT_draw_buffers2") && !this.EXT_draw_buffers2_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_draw_buffers2");
        }
        if (supported_extensions.contains("GL_EXT_draw_instanced") && !this.EXT_draw_instanced_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_draw_instanced");
        }
        if (supported_extensions.contains("GL_EXT_draw_range_elements") && !this.EXT_draw_range_elements_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_draw_range_elements");
        }
        if (supported_extensions.contains("GL_EXT_fog_coord") && !this.EXT_fog_coord_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_fog_coord");
        }
        if (supported_extensions.contains("GL_EXT_framebuffer_blit") && !this.EXT_framebuffer_blit_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_framebuffer_blit");
        }
        if (supported_extensions.contains("GL_EXT_framebuffer_multisample") && !this.EXT_framebuffer_multisample_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_framebuffer_multisample");
        }
        if (supported_extensions.contains("GL_EXT_framebuffer_object") && !this.EXT_framebuffer_object_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_framebuffer_object");
        }
        if (supported_extensions.contains("GL_EXT_geometry_shader4") && !this.EXT_geometry_shader4_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_geometry_shader4");
        }
        if (supported_extensions.contains("GL_EXT_gpu_program_parameters") && !this.EXT_gpu_program_parameters_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_gpu_program_parameters");
        }
        if (supported_extensions.contains("GL_EXT_gpu_shader4") && !this.EXT_gpu_shader4_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_gpu_shader4");
        }
        if (supported_extensions.contains("GL_EXT_multi_draw_arrays") && !this.EXT_multi_draw_arrays_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_multi_draw_arrays");
        }
        if (supported_extensions.contains("GL_EXT_paletted_texture") && !this.EXT_paletted_texture_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_paletted_texture");
        }
        if (supported_extensions.contains("GL_EXT_point_parameters") && !this.EXT_point_parameters_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_point_parameters");
        }
        if (supported_extensions.contains("GL_EXT_provoking_vertex") && !this.EXT_provoking_vertex_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_provoking_vertex");
        }
        if (supported_extensions.contains("GL_EXT_secondary_color") && !this.EXT_secondary_color_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_secondary_color");
        }
        if (supported_extensions.contains("GL_EXT_separate_shader_objects") && !this.EXT_separate_shader_objects_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_separate_shader_objects");
        }
        if (supported_extensions.contains("GL_EXT_shader_image_load_store") && !this.EXT_shader_image_load_store_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_shader_image_load_store");
        }
        if (supported_extensions.contains("GL_EXT_stencil_clear_tag") && !this.EXT_stencil_clear_tag_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_stencil_clear_tag");
        }
        if (supported_extensions.contains("GL_EXT_stencil_two_side") && !this.EXT_stencil_two_side_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_stencil_two_side");
        }
        if (supported_extensions.contains("GL_EXT_texture_array") && !this.EXT_texture_array_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_texture_array");
        }
        if (supported_extensions.contains("GL_EXT_texture_buffer_object") && !this.EXT_texture_buffer_object_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_texture_buffer_object");
        }
        if (supported_extensions.contains("GL_EXT_texture_integer") && !this.EXT_texture_integer_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_texture_integer");
        }
        if (supported_extensions.contains("GL_EXT_timer_query") && !this.EXT_timer_query_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_timer_query");
        }
        if (supported_extensions.contains("GL_EXT_transform_feedback") && !this.EXT_transform_feedback_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_transform_feedback");
        }
        if (supported_extensions.contains("GL_EXT_vertex_attrib_64bit") && !this.EXT_vertex_attrib_64bit_initNativeFunctionAddresses(supported_extensions)) {
            remove(supported_extensions, "GL_EXT_vertex_attrib_64bit");
        }
        if (supported_extensions.contains("GL_EXT_vertex_shader") && !this.EXT_vertex_shader_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_vertex_shader");
        }
        if (supported_extensions.contains("GL_EXT_vertex_weighting") && !this.EXT_vertex_weighting_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_EXT_vertex_weighting");
        }
        if (supported_extensions.contains("OpenGL12") && !this.GL12_initNativeFunctionAddresses()) {
            remove(supported_extensions, "OpenGL12");
        }
        if (supported_extensions.contains("OpenGL13") && !this.GL13_initNativeFunctionAddresses(forwardCompatible)) {
            remove(supported_extensions, "OpenGL13");
        }
        if (supported_extensions.contains("OpenGL14") && !this.GL14_initNativeFunctionAddresses(forwardCompatible)) {
            remove(supported_extensions, "OpenGL14");
        }
        if (supported_extensions.contains("OpenGL15") && !this.GL15_initNativeFunctionAddresses()) {
            remove(supported_extensions, "OpenGL15");
        }
        if (supported_extensions.contains("OpenGL20") && !this.GL20_initNativeFunctionAddresses()) {
            remove(supported_extensions, "OpenGL20");
        }
        if (supported_extensions.contains("OpenGL21") && !this.GL21_initNativeFunctionAddresses()) {
            remove(supported_extensions, "OpenGL21");
        }
        if (supported_extensions.contains("OpenGL30") && !this.GL30_initNativeFunctionAddresses()) {
            remove(supported_extensions, "OpenGL30");
        }
        if (supported_extensions.contains("OpenGL31") && !this.GL31_initNativeFunctionAddresses()) {
            remove(supported_extensions, "OpenGL31");
        }
        if (supported_extensions.contains("OpenGL32") && !this.GL32_initNativeFunctionAddresses()) {
            remove(supported_extensions, "OpenGL32");
        }
        if (supported_extensions.contains("OpenGL33") && !this.GL33_initNativeFunctionAddresses(forwardCompatible)) {
            remove(supported_extensions, "OpenGL33");
        }
        if (supported_extensions.contains("OpenGL40") && !this.GL40_initNativeFunctionAddresses()) {
            remove(supported_extensions, "OpenGL40");
        }
        if (supported_extensions.contains("OpenGL41") && !this.GL41_initNativeFunctionAddresses()) {
            remove(supported_extensions, "OpenGL41");
        }
        if (supported_extensions.contains("OpenGL42") && !this.GL42_initNativeFunctionAddresses()) {
            remove(supported_extensions, "OpenGL42");
        }
        if (supported_extensions.contains("OpenGL43") && !this.GL43_initNativeFunctionAddresses()) {
            remove(supported_extensions, "OpenGL43");
        }
        if (supported_extensions.contains("OpenGL44") && !this.GL44_initNativeFunctionAddresses()) {
            remove(supported_extensions, "OpenGL44");
        }
        if (supported_extensions.contains("OpenGL45") && !this.GL45_initNativeFunctionAddresses()) {
            remove(supported_extensions, "OpenGL45");
        }
        if (supported_extensions.contains("GL_GREMEDY_frame_terminator") && !this.GREMEDY_frame_terminator_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_GREMEDY_frame_terminator");
        }
        if (supported_extensions.contains("GL_GREMEDY_string_marker") && !this.GREMEDY_string_marker_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_GREMEDY_string_marker");
        }
        if (supported_extensions.contains("GL_INTEL_map_texture") && !this.INTEL_map_texture_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_INTEL_map_texture");
        }
        if (supported_extensions.contains("GL_KHR_debug") && !this.KHR_debug_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_KHR_debug");
        }
        if (supported_extensions.contains("GL_KHR_robustness") && !this.KHR_robustness_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_KHR_robustness");
        }
        if (supported_extensions.contains("GL_NV_bindless_multi_draw_indirect") && !this.NV_bindless_multi_draw_indirect_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_bindless_multi_draw_indirect");
        }
        if (supported_extensions.contains("GL_NV_bindless_texture") && !this.NV_bindless_texture_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_bindless_texture");
        }
        if (supported_extensions.contains("GL_NV_blend_equation_advanced") && !this.NV_blend_equation_advanced_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_blend_equation_advanced");
        }
        if (supported_extensions.contains("GL_NV_conditional_render") && !this.NV_conditional_render_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_conditional_render");
        }
        if (supported_extensions.contains("GL_NV_copy_image") && !this.NV_copy_image_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_copy_image");
        }
        if (supported_extensions.contains("GL_NV_depth_buffer_float") && !this.NV_depth_buffer_float_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_depth_buffer_float");
        }
        if (supported_extensions.contains("GL_NV_draw_texture") && !this.NV_draw_texture_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_draw_texture");
        }
        if (supported_extensions.contains("GL_NV_evaluators") && !this.NV_evaluators_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_evaluators");
        }
        if (supported_extensions.contains("GL_NV_explicit_multisample") && !this.NV_explicit_multisample_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_explicit_multisample");
        }
        if (supported_extensions.contains("GL_NV_fence") && !this.NV_fence_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_fence");
        }
        if (supported_extensions.contains("GL_NV_fragment_program") && !this.NV_fragment_program_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_fragment_program");
        }
        if (supported_extensions.contains("GL_NV_framebuffer_multisample_coverage") && !this.NV_framebuffer_multisample_coverage_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_framebuffer_multisample_coverage");
        }
        if (supported_extensions.contains("GL_NV_geometry_program4") && !this.NV_geometry_program4_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_geometry_program4");
        }
        if (supported_extensions.contains("GL_NV_gpu_program4") && !this.NV_gpu_program4_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_gpu_program4");
        }
        if (supported_extensions.contains("GL_NV_gpu_shader5") && !this.NV_gpu_shader5_initNativeFunctionAddresses(supported_extensions)) {
            remove(supported_extensions, "GL_NV_gpu_shader5");
        }
        if (supported_extensions.contains("GL_NV_half_float") && !this.NV_half_float_initNativeFunctionAddresses(supported_extensions)) {
            remove(supported_extensions, "GL_NV_half_float");
        }
        if (supported_extensions.contains("GL_NV_occlusion_query") && !this.NV_occlusion_query_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_occlusion_query");
        }
        if (supported_extensions.contains("GL_NV_parameter_buffer_object") && !this.NV_parameter_buffer_object_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_parameter_buffer_object");
        }
        if (supported_extensions.contains("GL_NV_path_rendering") && !this.NV_path_rendering_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_path_rendering");
        }
        if (supported_extensions.contains("GL_NV_pixel_data_range") && !this.NV_pixel_data_range_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_pixel_data_range");
        }
        if (supported_extensions.contains("GL_NV_point_sprite") && !this.NV_point_sprite_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_point_sprite");
        }
        if (supported_extensions.contains("GL_NV_present_video") && !this.NV_present_video_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_present_video");
        }
        supported_extensions.add("GL_NV_primitive_restart");
        if (supported_extensions.contains("GL_NV_primitive_restart") && !this.NV_primitive_restart_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_primitive_restart");
        }
        if (supported_extensions.contains("GL_NV_program") && !this.NV_program_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_program");
        }
        if (supported_extensions.contains("GL_NV_register_combiners") && !this.NV_register_combiners_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_register_combiners");
        }
        if (supported_extensions.contains("GL_NV_register_combiners2") && !this.NV_register_combiners2_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_register_combiners2");
        }
        if (supported_extensions.contains("GL_NV_shader_buffer_load") && !this.NV_shader_buffer_load_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_shader_buffer_load");
        }
        if (supported_extensions.contains("GL_NV_texture_barrier") && !this.NV_texture_barrier_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_texture_barrier");
        }
        if (supported_extensions.contains("GL_NV_texture_multisample") && !this.NV_texture_multisample_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_texture_multisample");
        }
        if (supported_extensions.contains("GL_NV_transform_feedback") && !this.NV_transform_feedback_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_transform_feedback");
        }
        if (supported_extensions.contains("GL_NV_transform_feedback2") && !this.NV_transform_feedback2_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_transform_feedback2");
        }
        if (supported_extensions.contains("GL_NV_vertex_array_range") && !this.NV_vertex_array_range_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_vertex_array_range");
        }
        if (supported_extensions.contains("GL_NV_vertex_attrib_integer_64bit") && !this.NV_vertex_attrib_integer_64bit_initNativeFunctionAddresses(supported_extensions)) {
            remove(supported_extensions, "GL_NV_vertex_attrib_integer_64bit");
        }
        if (supported_extensions.contains("GL_NV_vertex_buffer_unified_memory") && !this.NV_vertex_buffer_unified_memory_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_vertex_buffer_unified_memory");
        }
        if (supported_extensions.contains("GL_NV_vertex_program") && !this.NV_vertex_program_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_vertex_program");
        }
        if (supported_extensions.contains("GL_NV_video_capture") && !this.NV_video_capture_initNativeFunctionAddresses()) {
            remove(supported_extensions, "GL_NV_video_capture");
        }
        return supported_extensions;
    }
    
    static void unloadAllStubs() {
    }
    
    ContextCapabilities(final boolean forwardCompatible) throws LWJGLException {
        this.util = new APIUtil();
        this.tracker = new StateTracker();
        final Set<String> supported_extensions = this.initAllStubs(forwardCompatible);
        this.GL_AMD_blend_minmax_factor = supported_extensions.contains("GL_AMD_blend_minmax_factor");
        this.GL_AMD_conservative_depth = supported_extensions.contains("GL_AMD_conservative_depth");
        this.GL_AMD_debug_output = (supported_extensions.contains("GL_AMD_debug_output") || supported_extensions.contains("GL_AMDX_debug_output"));
        this.GL_AMD_depth_clamp_separate = supported_extensions.contains("GL_AMD_depth_clamp_separate");
        this.GL_AMD_draw_buffers_blend = supported_extensions.contains("GL_AMD_draw_buffers_blend");
        this.GL_AMD_interleaved_elements = supported_extensions.contains("GL_AMD_interleaved_elements");
        this.GL_AMD_multi_draw_indirect = supported_extensions.contains("GL_AMD_multi_draw_indirect");
        this.GL_AMD_name_gen_delete = supported_extensions.contains("GL_AMD_name_gen_delete");
        this.GL_AMD_performance_monitor = supported_extensions.contains("GL_AMD_performance_monitor");
        this.GL_AMD_pinned_memory = supported_extensions.contains("GL_AMD_pinned_memory");
        this.GL_AMD_query_buffer_object = supported_extensions.contains("GL_AMD_query_buffer_object");
        this.GL_AMD_sample_positions = supported_extensions.contains("GL_AMD_sample_positions");
        this.GL_AMD_seamless_cubemap_per_texture = supported_extensions.contains("GL_AMD_seamless_cubemap_per_texture");
        this.GL_AMD_shader_atomic_counter_ops = supported_extensions.contains("GL_AMD_shader_atomic_counter_ops");
        this.GL_AMD_shader_stencil_export = supported_extensions.contains("GL_AMD_shader_stencil_export");
        this.GL_AMD_shader_trinary_minmax = supported_extensions.contains("GL_AMD_shader_trinary_minmax");
        this.GL_AMD_sparse_texture = supported_extensions.contains("GL_AMD_sparse_texture");
        this.GL_AMD_stencil_operation_extended = supported_extensions.contains("GL_AMD_stencil_operation_extended");
        this.GL_AMD_texture_texture4 = supported_extensions.contains("GL_AMD_texture_texture4");
        this.GL_AMD_transform_feedback3_lines_triangles = supported_extensions.contains("GL_AMD_transform_feedback3_lines_triangles");
        this.GL_AMD_vertex_shader_layer = supported_extensions.contains("GL_AMD_vertex_shader_layer");
        this.GL_AMD_vertex_shader_tessellator = supported_extensions.contains("GL_AMD_vertex_shader_tessellator");
        this.GL_AMD_vertex_shader_viewport_index = supported_extensions.contains("GL_AMD_vertex_shader_viewport_index");
        this.GL_APPLE_aux_depth_stencil = supported_extensions.contains("GL_APPLE_aux_depth_stencil");
        this.GL_APPLE_client_storage = supported_extensions.contains("GL_APPLE_client_storage");
        this.GL_APPLE_element_array = supported_extensions.contains("GL_APPLE_element_array");
        this.GL_APPLE_fence = supported_extensions.contains("GL_APPLE_fence");
        this.GL_APPLE_float_pixels = supported_extensions.contains("GL_APPLE_float_pixels");
        this.GL_APPLE_flush_buffer_range = supported_extensions.contains("GL_APPLE_flush_buffer_range");
        this.GL_APPLE_object_purgeable = supported_extensions.contains("GL_APPLE_object_purgeable");
        this.GL_APPLE_packed_pixels = supported_extensions.contains("GL_APPLE_packed_pixels");
        this.GL_APPLE_rgb_422 = supported_extensions.contains("GL_APPLE_rgb_422");
        this.GL_APPLE_row_bytes = supported_extensions.contains("GL_APPLE_row_bytes");
        this.GL_APPLE_texture_range = supported_extensions.contains("GL_APPLE_texture_range");
        this.GL_APPLE_vertex_array_object = supported_extensions.contains("GL_APPLE_vertex_array_object");
        this.GL_APPLE_vertex_array_range = supported_extensions.contains("GL_APPLE_vertex_array_range");
        this.GL_APPLE_vertex_program_evaluators = supported_extensions.contains("GL_APPLE_vertex_program_evaluators");
        this.GL_APPLE_ycbcr_422 = supported_extensions.contains("GL_APPLE_ycbcr_422");
        this.GL_ARB_ES2_compatibility = supported_extensions.contains("GL_ARB_ES2_compatibility");
        this.GL_ARB_ES3_1_compatibility = supported_extensions.contains("GL_ARB_ES3_1_compatibility");
        this.GL_ARB_ES3_compatibility = supported_extensions.contains("GL_ARB_ES3_compatibility");
        this.GL_ARB_arrays_of_arrays = supported_extensions.contains("GL_ARB_arrays_of_arrays");
        this.GL_ARB_base_instance = supported_extensions.contains("GL_ARB_base_instance");
        this.GL_ARB_bindless_texture = supported_extensions.contains("GL_ARB_bindless_texture");
        this.GL_ARB_blend_func_extended = supported_extensions.contains("GL_ARB_blend_func_extended");
        this.GL_ARB_buffer_storage = supported_extensions.contains("GL_ARB_buffer_storage");
        this.GL_ARB_cl_event = supported_extensions.contains("GL_ARB_cl_event");
        this.GL_ARB_clear_buffer_object = supported_extensions.contains("GL_ARB_clear_buffer_object");
        this.GL_ARB_clear_texture = supported_extensions.contains("GL_ARB_clear_texture");
        this.GL_ARB_clip_control = supported_extensions.contains("GL_ARB_clip_control");
        this.GL_ARB_color_buffer_float = supported_extensions.contains("GL_ARB_color_buffer_float");
        this.GL_ARB_compatibility = supported_extensions.contains("GL_ARB_compatibility");
        this.GL_ARB_compressed_texture_pixel_storage = supported_extensions.contains("GL_ARB_compressed_texture_pixel_storage");
        this.GL_ARB_compute_shader = supported_extensions.contains("GL_ARB_compute_shader");
        this.GL_ARB_compute_variable_group_size = supported_extensions.contains("GL_ARB_compute_variable_group_size");
        this.GL_ARB_conditional_render_inverted = supported_extensions.contains("GL_ARB_conditional_render_inverted");
        this.GL_ARB_conservative_depth = supported_extensions.contains("GL_ARB_conservative_depth");
        this.GL_ARB_copy_buffer = supported_extensions.contains("GL_ARB_copy_buffer");
        this.GL_ARB_copy_image = supported_extensions.contains("GL_ARB_copy_image");
        this.GL_ARB_cull_distance = supported_extensions.contains("GL_ARB_cull_distance");
        this.GL_ARB_debug_output = supported_extensions.contains("GL_ARB_debug_output");
        this.GL_ARB_depth_buffer_float = supported_extensions.contains("GL_ARB_depth_buffer_float");
        this.GL_ARB_depth_clamp = supported_extensions.contains("GL_ARB_depth_clamp");
        this.GL_ARB_depth_texture = supported_extensions.contains("GL_ARB_depth_texture");
        this.GL_ARB_derivative_control = supported_extensions.contains("GL_ARB_derivative_control");
        this.GL_ARB_direct_state_access = supported_extensions.contains("GL_ARB_direct_state_access");
        this.GL_ARB_draw_buffers = supported_extensions.contains("GL_ARB_draw_buffers");
        this.GL_ARB_draw_buffers_blend = supported_extensions.contains("GL_ARB_draw_buffers_blend");
        this.GL_ARB_draw_elements_base_vertex = supported_extensions.contains("GL_ARB_draw_elements_base_vertex");
        this.GL_ARB_draw_indirect = supported_extensions.contains("GL_ARB_draw_indirect");
        this.GL_ARB_draw_instanced = supported_extensions.contains("GL_ARB_draw_instanced");
        this.GL_ARB_enhanced_layouts = supported_extensions.contains("GL_ARB_enhanced_layouts");
        this.GL_ARB_explicit_attrib_location = supported_extensions.contains("GL_ARB_explicit_attrib_location");
        this.GL_ARB_explicit_uniform_location = supported_extensions.contains("GL_ARB_explicit_uniform_location");
        this.GL_ARB_fragment_coord_conventions = supported_extensions.contains("GL_ARB_fragment_coord_conventions");
        this.GL_ARB_fragment_layer_viewport = supported_extensions.contains("GL_ARB_fragment_layer_viewport");
        this.GL_ARB_fragment_program = (supported_extensions.contains("GL_ARB_fragment_program") && supported_extensions.contains("GL_ARB_program"));
        this.GL_ARB_fragment_program_shadow = supported_extensions.contains("GL_ARB_fragment_program_shadow");
        this.GL_ARB_fragment_shader = supported_extensions.contains("GL_ARB_fragment_shader");
        this.GL_ARB_framebuffer_no_attachments = supported_extensions.contains("GL_ARB_framebuffer_no_attachments");
        this.GL_ARB_framebuffer_object = supported_extensions.contains("GL_ARB_framebuffer_object");
        this.GL_ARB_framebuffer_sRGB = supported_extensions.contains("GL_ARB_framebuffer_sRGB");
        this.GL_ARB_geometry_shader4 = supported_extensions.contains("GL_ARB_geometry_shader4");
        this.GL_ARB_get_program_binary = supported_extensions.contains("GL_ARB_get_program_binary");
        this.GL_ARB_get_texture_sub_image = supported_extensions.contains("GL_ARB_get_texture_sub_image");
        this.GL_ARB_gpu_shader5 = supported_extensions.contains("GL_ARB_gpu_shader5");
        this.GL_ARB_gpu_shader_fp64 = supported_extensions.contains("GL_ARB_gpu_shader_fp64");
        this.GL_ARB_half_float_pixel = supported_extensions.contains("GL_ARB_half_float_pixel");
        this.GL_ARB_half_float_vertex = supported_extensions.contains("GL_ARB_half_float_vertex");
        this.GL_ARB_imaging = supported_extensions.contains("GL_ARB_imaging");
        this.GL_ARB_indirect_parameters = supported_extensions.contains("GL_ARB_indirect_parameters");
        this.GL_ARB_instanced_arrays = supported_extensions.contains("GL_ARB_instanced_arrays");
        this.GL_ARB_internalformat_query = supported_extensions.contains("GL_ARB_internalformat_query");
        this.GL_ARB_internalformat_query2 = supported_extensions.contains("GL_ARB_internalformat_query2");
        this.GL_ARB_invalidate_subdata = supported_extensions.contains("GL_ARB_invalidate_subdata");
        this.GL_ARB_map_buffer_alignment = supported_extensions.contains("GL_ARB_map_buffer_alignment");
        this.GL_ARB_map_buffer_range = supported_extensions.contains("GL_ARB_map_buffer_range");
        this.GL_ARB_matrix_palette = supported_extensions.contains("GL_ARB_matrix_palette");
        this.GL_ARB_multi_bind = supported_extensions.contains("GL_ARB_multi_bind");
        this.GL_ARB_multi_draw_indirect = supported_extensions.contains("GL_ARB_multi_draw_indirect");
        this.GL_ARB_multisample = supported_extensions.contains("GL_ARB_multisample");
        this.GL_ARB_multitexture = supported_extensions.contains("GL_ARB_multitexture");
        this.GL_ARB_occlusion_query = supported_extensions.contains("GL_ARB_occlusion_query");
        this.GL_ARB_occlusion_query2 = supported_extensions.contains("GL_ARB_occlusion_query2");
        this.GL_ARB_pipeline_statistics_query = supported_extensions.contains("GL_ARB_pipeline_statistics_query");
        this.GL_ARB_pixel_buffer_object = (supported_extensions.contains("GL_ARB_pixel_buffer_object") && supported_extensions.contains("GL_ARB_buffer_object"));
        this.GL_ARB_point_parameters = supported_extensions.contains("GL_ARB_point_parameters");
        this.GL_ARB_point_sprite = supported_extensions.contains("GL_ARB_point_sprite");
        this.GL_ARB_program_interface_query = supported_extensions.contains("GL_ARB_program_interface_query");
        this.GL_ARB_provoking_vertex = supported_extensions.contains("GL_ARB_provoking_vertex");
        this.GL_ARB_query_buffer_object = supported_extensions.contains("GL_ARB_query_buffer_object");
        this.GL_ARB_robust_buffer_access_behavior = supported_extensions.contains("GL_ARB_robust_buffer_access_behavior");
        this.GL_ARB_robustness = supported_extensions.contains("GL_ARB_robustness");
        this.GL_ARB_robustness_isolation = supported_extensions.contains("GL_ARB_robustness_isolation");
        this.GL_ARB_sample_shading = supported_extensions.contains("GL_ARB_sample_shading");
        this.GL_ARB_sampler_objects = supported_extensions.contains("GL_ARB_sampler_objects");
        this.GL_ARB_seamless_cube_map = supported_extensions.contains("GL_ARB_seamless_cube_map");
        this.GL_ARB_seamless_cubemap_per_texture = supported_extensions.contains("GL_ARB_seamless_cubemap_per_texture");
        this.GL_ARB_separate_shader_objects = supported_extensions.contains("GL_ARB_separate_shader_objects");
        this.GL_ARB_shader_atomic_counters = supported_extensions.contains("GL_ARB_shader_atomic_counters");
        this.GL_ARB_shader_bit_encoding = supported_extensions.contains("GL_ARB_shader_bit_encoding");
        this.GL_ARB_shader_draw_parameters = supported_extensions.contains("GL_ARB_shader_draw_parameters");
        this.GL_ARB_shader_group_vote = supported_extensions.contains("GL_ARB_shader_group_vote");
        this.GL_ARB_shader_image_load_store = supported_extensions.contains("GL_ARB_shader_image_load_store");
        this.GL_ARB_shader_image_size = supported_extensions.contains("GL_ARB_shader_image_size");
        this.GL_ARB_shader_objects = supported_extensions.contains("GL_ARB_shader_objects");
        this.GL_ARB_shader_precision = supported_extensions.contains("GL_ARB_shader_precision");
        this.GL_ARB_shader_stencil_export = supported_extensions.contains("GL_ARB_shader_stencil_export");
        this.GL_ARB_shader_storage_buffer_object = supported_extensions.contains("GL_ARB_shader_storage_buffer_object");
        this.GL_ARB_shader_subroutine = supported_extensions.contains("GL_ARB_shader_subroutine");
        this.GL_ARB_shader_texture_image_samples = supported_extensions.contains("GL_ARB_shader_texture_image_samples");
        this.GL_ARB_shader_texture_lod = supported_extensions.contains("GL_ARB_shader_texture_lod");
        this.GL_ARB_shading_language_100 = supported_extensions.contains("GL_ARB_shading_language_100");
        this.GL_ARB_shading_language_420pack = supported_extensions.contains("GL_ARB_shading_language_420pack");
        this.GL_ARB_shading_language_include = supported_extensions.contains("GL_ARB_shading_language_include");
        this.GL_ARB_shading_language_packing = supported_extensions.contains("GL_ARB_shading_language_packing");
        this.GL_ARB_shadow = supported_extensions.contains("GL_ARB_shadow");
        this.GL_ARB_shadow_ambient = supported_extensions.contains("GL_ARB_shadow_ambient");
        this.GL_ARB_sparse_buffer = supported_extensions.contains("GL_ARB_sparse_buffer");
        this.GL_ARB_sparse_texture = supported_extensions.contains("GL_ARB_sparse_texture");
        this.GL_ARB_stencil_texturing = supported_extensions.contains("GL_ARB_stencil_texturing");
        this.GL_ARB_sync = supported_extensions.contains("GL_ARB_sync");
        this.GL_ARB_tessellation_shader = supported_extensions.contains("GL_ARB_tessellation_shader");
        this.GL_ARB_texture_barrier = supported_extensions.contains("GL_ARB_texture_barrier");
        this.GL_ARB_texture_border_clamp = supported_extensions.contains("GL_ARB_texture_border_clamp");
        this.GL_ARB_texture_buffer_object = supported_extensions.contains("GL_ARB_texture_buffer_object");
        this.GL_ARB_texture_buffer_object_rgb32 = (supported_extensions.contains("GL_ARB_texture_buffer_object_rgb32") || supported_extensions.contains("GL_EXT_texture_buffer_object_rgb32"));
        this.GL_ARB_texture_buffer_range = supported_extensions.contains("GL_ARB_texture_buffer_range");
        this.GL_ARB_texture_compression = supported_extensions.contains("GL_ARB_texture_compression");
        this.GL_ARB_texture_compression_bptc = (supported_extensions.contains("GL_ARB_texture_compression_bptc") || supported_extensions.contains("GL_EXT_texture_compression_bptc"));
        this.GL_ARB_texture_compression_rgtc = supported_extensions.contains("GL_ARB_texture_compression_rgtc");
        this.GL_ARB_texture_cube_map = supported_extensions.contains("GL_ARB_texture_cube_map");
        this.GL_ARB_texture_cube_map_array = supported_extensions.contains("GL_ARB_texture_cube_map_array");
        this.GL_ARB_texture_env_add = supported_extensions.contains("GL_ARB_texture_env_add");
        this.GL_ARB_texture_env_combine = supported_extensions.contains("GL_ARB_texture_env_combine");
        this.GL_ARB_texture_env_crossbar = supported_extensions.contains("GL_ARB_texture_env_crossbar");
        this.GL_ARB_texture_env_dot3 = supported_extensions.contains("GL_ARB_texture_env_dot3");
        this.GL_ARB_texture_float = supported_extensions.contains("GL_ARB_texture_float");
        this.GL_ARB_texture_gather = supported_extensions.contains("GL_ARB_texture_gather");
        this.GL_ARB_texture_mirror_clamp_to_edge = supported_extensions.contains("GL_ARB_texture_mirror_clamp_to_edge");
        this.GL_ARB_texture_mirrored_repeat = supported_extensions.contains("GL_ARB_texture_mirrored_repeat");
        this.GL_ARB_texture_multisample = supported_extensions.contains("GL_ARB_texture_multisample");
        this.GL_ARB_texture_non_power_of_two = supported_extensions.contains("GL_ARB_texture_non_power_of_two");
        this.GL_ARB_texture_query_levels = supported_extensions.contains("GL_ARB_texture_query_levels");
        this.GL_ARB_texture_query_lod = supported_extensions.contains("GL_ARB_texture_query_lod");
        this.GL_ARB_texture_rectangle = supported_extensions.contains("GL_ARB_texture_rectangle");
        this.GL_ARB_texture_rg = supported_extensions.contains("GL_ARB_texture_rg");
        this.GL_ARB_texture_rgb10_a2ui = supported_extensions.contains("GL_ARB_texture_rgb10_a2ui");
        this.GL_ARB_texture_stencil8 = supported_extensions.contains("GL_ARB_texture_stencil8");
        this.GL_ARB_texture_storage = (supported_extensions.contains("GL_ARB_texture_storage") || supported_extensions.contains("GL_EXT_texture_storage"));
        this.GL_ARB_texture_storage_multisample = supported_extensions.contains("GL_ARB_texture_storage_multisample");
        this.GL_ARB_texture_swizzle = supported_extensions.contains("GL_ARB_texture_swizzle");
        this.GL_ARB_texture_view = supported_extensions.contains("GL_ARB_texture_view");
        this.GL_ARB_timer_query = supported_extensions.contains("GL_ARB_timer_query");
        this.GL_ARB_transform_feedback2 = supported_extensions.contains("GL_ARB_transform_feedback2");
        this.GL_ARB_transform_feedback3 = supported_extensions.contains("GL_ARB_transform_feedback3");
        this.GL_ARB_transform_feedback_instanced = supported_extensions.contains("GL_ARB_transform_feedback_instanced");
        this.GL_ARB_transform_feedback_overflow_query = supported_extensions.contains("GL_ARB_transform_feedback_overflow_query");
        this.GL_ARB_transpose_matrix = supported_extensions.contains("GL_ARB_transpose_matrix");
        this.GL_ARB_uniform_buffer_object = supported_extensions.contains("GL_ARB_uniform_buffer_object");
        this.GL_ARB_vertex_array_bgra = supported_extensions.contains("GL_ARB_vertex_array_bgra");
        this.GL_ARB_vertex_array_object = supported_extensions.contains("GL_ARB_vertex_array_object");
        this.GL_ARB_vertex_attrib_64bit = supported_extensions.contains("GL_ARB_vertex_attrib_64bit");
        this.GL_ARB_vertex_attrib_binding = supported_extensions.contains("GL_ARB_vertex_attrib_binding");
        this.GL_ARB_vertex_blend = supported_extensions.contains("GL_ARB_vertex_blend");
        this.GL_ARB_vertex_buffer_object = (supported_extensions.contains("GL_ARB_vertex_buffer_object") && supported_extensions.contains("GL_ARB_buffer_object"));
        this.GL_ARB_vertex_program = (supported_extensions.contains("GL_ARB_vertex_program") && supported_extensions.contains("GL_ARB_program"));
        this.GL_ARB_vertex_shader = supported_extensions.contains("GL_ARB_vertex_shader");
        this.GL_ARB_vertex_type_10f_11f_11f_rev = supported_extensions.contains("GL_ARB_vertex_type_10f_11f_11f_rev");
        this.GL_ARB_vertex_type_2_10_10_10_rev = supported_extensions.contains("GL_ARB_vertex_type_2_10_10_10_rev");
        this.GL_ARB_viewport_array = supported_extensions.contains("GL_ARB_viewport_array");
        this.GL_ARB_window_pos = supported_extensions.contains("GL_ARB_window_pos");
        this.GL_ATI_draw_buffers = supported_extensions.contains("GL_ATI_draw_buffers");
        this.GL_ATI_element_array = supported_extensions.contains("GL_ATI_element_array");
        this.GL_ATI_envmap_bumpmap = supported_extensions.contains("GL_ATI_envmap_bumpmap");
        this.GL_ATI_fragment_shader = supported_extensions.contains("GL_ATI_fragment_shader");
        this.GL_ATI_map_object_buffer = supported_extensions.contains("GL_ATI_map_object_buffer");
        this.GL_ATI_meminfo = supported_extensions.contains("GL_ATI_meminfo");
        this.GL_ATI_pn_triangles = supported_extensions.contains("GL_ATI_pn_triangles");
        this.GL_ATI_separate_stencil = supported_extensions.contains("GL_ATI_separate_stencil");
        this.GL_ATI_shader_texture_lod = supported_extensions.contains("GL_ATI_shader_texture_lod");
        this.GL_ATI_text_fragment_shader = supported_extensions.contains("GL_ATI_text_fragment_shader");
        this.GL_ATI_texture_compression_3dc = supported_extensions.contains("GL_ATI_texture_compression_3dc");
        this.GL_ATI_texture_env_combine3 = supported_extensions.contains("GL_ATI_texture_env_combine3");
        this.GL_ATI_texture_float = supported_extensions.contains("GL_ATI_texture_float");
        this.GL_ATI_texture_mirror_once = supported_extensions.contains("GL_ATI_texture_mirror_once");
        this.GL_ATI_vertex_array_object = supported_extensions.contains("GL_ATI_vertex_array_object");
        this.GL_ATI_vertex_attrib_array_object = supported_extensions.contains("GL_ATI_vertex_attrib_array_object");
        this.GL_ATI_vertex_streams = supported_extensions.contains("GL_ATI_vertex_streams");
        this.GL_EXT_Cg_shader = supported_extensions.contains("GL_EXT_Cg_shader");
        this.GL_EXT_abgr = supported_extensions.contains("GL_EXT_abgr");
        this.GL_EXT_bgra = supported_extensions.contains("GL_EXT_bgra");
        this.GL_EXT_bindable_uniform = supported_extensions.contains("GL_EXT_bindable_uniform");
        this.GL_EXT_blend_color = supported_extensions.contains("GL_EXT_blend_color");
        this.GL_EXT_blend_equation_separate = supported_extensions.contains("GL_EXT_blend_equation_separate");
        this.GL_EXT_blend_func_separate = supported_extensions.contains("GL_EXT_blend_func_separate");
        this.GL_EXT_blend_minmax = supported_extensions.contains("GL_EXT_blend_minmax");
        this.GL_EXT_blend_subtract = supported_extensions.contains("GL_EXT_blend_subtract");
        this.GL_EXT_compiled_vertex_array = supported_extensions.contains("GL_EXT_compiled_vertex_array");
        this.GL_EXT_depth_bounds_test = supported_extensions.contains("GL_EXT_depth_bounds_test");
        this.GL_EXT_direct_state_access = supported_extensions.contains("GL_EXT_direct_state_access");
        this.GL_EXT_draw_buffers2 = supported_extensions.contains("GL_EXT_draw_buffers2");
        this.GL_EXT_draw_instanced = supported_extensions.contains("GL_EXT_draw_instanced");
        this.GL_EXT_draw_range_elements = supported_extensions.contains("GL_EXT_draw_range_elements");
        this.GL_EXT_fog_coord = supported_extensions.contains("GL_EXT_fog_coord");
        this.GL_EXT_framebuffer_blit = supported_extensions.contains("GL_EXT_framebuffer_blit");
        this.GL_EXT_framebuffer_multisample = supported_extensions.contains("GL_EXT_framebuffer_multisample");
        this.GL_EXT_framebuffer_multisample_blit_scaled = supported_extensions.contains("GL_EXT_framebuffer_multisample_blit_scaled");
        this.GL_EXT_framebuffer_object = supported_extensions.contains("GL_EXT_framebuffer_object");
        this.GL_EXT_framebuffer_sRGB = supported_extensions.contains("GL_EXT_framebuffer_sRGB");
        this.GL_EXT_geometry_shader4 = supported_extensions.contains("GL_EXT_geometry_shader4");
        this.GL_EXT_gpu_program_parameters = supported_extensions.contains("GL_EXT_gpu_program_parameters");
        this.GL_EXT_gpu_shader4 = supported_extensions.contains("GL_EXT_gpu_shader4");
        this.GL_EXT_multi_draw_arrays = supported_extensions.contains("GL_EXT_multi_draw_arrays");
        this.GL_EXT_packed_depth_stencil = supported_extensions.contains("GL_EXT_packed_depth_stencil");
        this.GL_EXT_packed_float = supported_extensions.contains("GL_EXT_packed_float");
        this.GL_EXT_packed_pixels = supported_extensions.contains("GL_EXT_packed_pixels");
        this.GL_EXT_paletted_texture = supported_extensions.contains("GL_EXT_paletted_texture");
        this.GL_EXT_pixel_buffer_object = (supported_extensions.contains("GL_EXT_pixel_buffer_object") && supported_extensions.contains("GL_ARB_buffer_object"));
        this.GL_EXT_point_parameters = supported_extensions.contains("GL_EXT_point_parameters");
        this.GL_EXT_provoking_vertex = supported_extensions.contains("GL_EXT_provoking_vertex");
        this.GL_EXT_rescale_normal = supported_extensions.contains("GL_EXT_rescale_normal");
        this.GL_EXT_secondary_color = supported_extensions.contains("GL_EXT_secondary_color");
        this.GL_EXT_separate_shader_objects = supported_extensions.contains("GL_EXT_separate_shader_objects");
        this.GL_EXT_separate_specular_color = supported_extensions.contains("GL_EXT_separate_specular_color");
        this.GL_EXT_shader_image_load_store = supported_extensions.contains("GL_EXT_shader_image_load_store");
        this.GL_EXT_shadow_funcs = supported_extensions.contains("GL_EXT_shadow_funcs");
        this.GL_EXT_shared_texture_palette = supported_extensions.contains("GL_EXT_shared_texture_palette");
        this.GL_EXT_stencil_clear_tag = supported_extensions.contains("GL_EXT_stencil_clear_tag");
        this.GL_EXT_stencil_two_side = supported_extensions.contains("GL_EXT_stencil_two_side");
        this.GL_EXT_stencil_wrap = supported_extensions.contains("GL_EXT_stencil_wrap");
        this.GL_EXT_texture_3d = supported_extensions.contains("GL_EXT_texture_3d");
        this.GL_EXT_texture_array = supported_extensions.contains("GL_EXT_texture_array");
        this.GL_EXT_texture_buffer_object = supported_extensions.contains("GL_EXT_texture_buffer_object");
        this.GL_EXT_texture_compression_latc = supported_extensions.contains("GL_EXT_texture_compression_latc");
        this.GL_EXT_texture_compression_rgtc = supported_extensions.contains("GL_EXT_texture_compression_rgtc");
        this.GL_EXT_texture_compression_s3tc = supported_extensions.contains("GL_EXT_texture_compression_s3tc");
        this.GL_EXT_texture_env_combine = supported_extensions.contains("GL_EXT_texture_env_combine");
        this.GL_EXT_texture_env_dot3 = supported_extensions.contains("GL_EXT_texture_env_dot3");
        this.GL_EXT_texture_filter_anisotropic = supported_extensions.contains("GL_EXT_texture_filter_anisotropic");
        this.GL_EXT_texture_integer = supported_extensions.contains("GL_EXT_texture_integer");
        this.GL_EXT_texture_lod_bias = supported_extensions.contains("GL_EXT_texture_lod_bias");
        this.GL_EXT_texture_mirror_clamp = supported_extensions.contains("GL_EXT_texture_mirror_clamp");
        this.GL_EXT_texture_rectangle = supported_extensions.contains("GL_EXT_texture_rectangle");
        this.GL_EXT_texture_sRGB = supported_extensions.contains("GL_EXT_texture_sRGB");
        this.GL_EXT_texture_sRGB_decode = supported_extensions.contains("GL_EXT_texture_sRGB_decode");
        this.GL_EXT_texture_shared_exponent = supported_extensions.contains("GL_EXT_texture_shared_exponent");
        this.GL_EXT_texture_snorm = supported_extensions.contains("GL_EXT_texture_snorm");
        this.GL_EXT_texture_swizzle = supported_extensions.contains("GL_EXT_texture_swizzle");
        this.GL_EXT_timer_query = supported_extensions.contains("GL_EXT_timer_query");
        this.GL_EXT_transform_feedback = supported_extensions.contains("GL_EXT_transform_feedback");
        this.GL_EXT_vertex_array_bgra = supported_extensions.contains("GL_EXT_vertex_array_bgra");
        this.GL_EXT_vertex_attrib_64bit = supported_extensions.contains("GL_EXT_vertex_attrib_64bit");
        this.GL_EXT_vertex_shader = supported_extensions.contains("GL_EXT_vertex_shader");
        this.GL_EXT_vertex_weighting = supported_extensions.contains("GL_EXT_vertex_weighting");
        this.OpenGL11 = supported_extensions.contains("OpenGL11");
        this.OpenGL12 = supported_extensions.contains("OpenGL12");
        this.OpenGL13 = supported_extensions.contains("OpenGL13");
        this.OpenGL14 = supported_extensions.contains("OpenGL14");
        this.OpenGL15 = supported_extensions.contains("OpenGL15");
        this.OpenGL20 = supported_extensions.contains("OpenGL20");
        this.OpenGL21 = supported_extensions.contains("OpenGL21");
        this.OpenGL30 = supported_extensions.contains("OpenGL30");
        this.OpenGL31 = supported_extensions.contains("OpenGL31");
        this.OpenGL32 = supported_extensions.contains("OpenGL32");
        this.OpenGL33 = supported_extensions.contains("OpenGL33");
        this.OpenGL40 = supported_extensions.contains("OpenGL40");
        this.OpenGL41 = supported_extensions.contains("OpenGL41");
        this.OpenGL42 = supported_extensions.contains("OpenGL42");
        this.OpenGL43 = supported_extensions.contains("OpenGL43");
        this.OpenGL44 = supported_extensions.contains("OpenGL44");
        this.OpenGL45 = supported_extensions.contains("OpenGL45");
        this.GL_GREMEDY_frame_terminator = supported_extensions.contains("GL_GREMEDY_frame_terminator");
        this.GL_GREMEDY_string_marker = supported_extensions.contains("GL_GREMEDY_string_marker");
        this.GL_HP_occlusion_test = supported_extensions.contains("GL_HP_occlusion_test");
        this.GL_IBM_rasterpos_clip = supported_extensions.contains("GL_IBM_rasterpos_clip");
        this.GL_INTEL_map_texture = supported_extensions.contains("GL_INTEL_map_texture");
        this.GL_KHR_context_flush_control = supported_extensions.contains("GL_KHR_context_flush_control");
        this.GL_KHR_debug = supported_extensions.contains("GL_KHR_debug");
        this.GL_KHR_robust_buffer_access_behavior = supported_extensions.contains("GL_KHR_robust_buffer_access_behavior");
        this.GL_KHR_robustness = supported_extensions.contains("GL_KHR_robustness");
        this.GL_KHR_texture_compression_astc_ldr = supported_extensions.contains("GL_KHR_texture_compression_astc_ldr");
        this.GL_NVX_gpu_memory_info = supported_extensions.contains("GL_NVX_gpu_memory_info");
        this.GL_NV_bindless_multi_draw_indirect = supported_extensions.contains("GL_NV_bindless_multi_draw_indirect");
        this.GL_NV_bindless_texture = supported_extensions.contains("GL_NV_bindless_texture");
        this.GL_NV_blend_equation_advanced = supported_extensions.contains("GL_NV_blend_equation_advanced");
        this.GL_NV_blend_square = supported_extensions.contains("GL_NV_blend_square");
        this.GL_NV_compute_program5 = supported_extensions.contains("GL_NV_compute_program5");
        this.GL_NV_conditional_render = supported_extensions.contains("GL_NV_conditional_render");
        this.GL_NV_copy_depth_to_color = supported_extensions.contains("GL_NV_copy_depth_to_color");
        this.GL_NV_copy_image = supported_extensions.contains("GL_NV_copy_image");
        this.GL_NV_deep_texture3D = supported_extensions.contains("GL_NV_deep_texture3D");
        this.GL_NV_depth_buffer_float = supported_extensions.contains("GL_NV_depth_buffer_float");
        this.GL_NV_depth_clamp = supported_extensions.contains("GL_NV_depth_clamp");
        this.GL_NV_draw_texture = supported_extensions.contains("GL_NV_draw_texture");
        this.GL_NV_evaluators = supported_extensions.contains("GL_NV_evaluators");
        this.GL_NV_explicit_multisample = supported_extensions.contains("GL_NV_explicit_multisample");
        this.GL_NV_fence = supported_extensions.contains("GL_NV_fence");
        this.GL_NV_float_buffer = supported_extensions.contains("GL_NV_float_buffer");
        this.GL_NV_fog_distance = supported_extensions.contains("GL_NV_fog_distance");
        this.GL_NV_fragment_program = (supported_extensions.contains("GL_NV_fragment_program") && supported_extensions.contains("GL_NV_program"));
        this.GL_NV_fragment_program2 = supported_extensions.contains("GL_NV_fragment_program2");
        this.GL_NV_fragment_program4 = supported_extensions.contains("GL_NV_fragment_program4");
        this.GL_NV_fragment_program_option = supported_extensions.contains("GL_NV_fragment_program_option");
        this.GL_NV_framebuffer_multisample_coverage = supported_extensions.contains("GL_NV_framebuffer_multisample_coverage");
        this.GL_NV_geometry_program4 = supported_extensions.contains("GL_NV_geometry_program4");
        this.GL_NV_geometry_shader4 = supported_extensions.contains("GL_NV_geometry_shader4");
        this.GL_NV_gpu_program4 = supported_extensions.contains("GL_NV_gpu_program4");
        this.GL_NV_gpu_program5 = supported_extensions.contains("GL_NV_gpu_program5");
        this.GL_NV_gpu_program5_mem_extended = supported_extensions.contains("GL_NV_gpu_program5_mem_extended");
        this.GL_NV_gpu_shader5 = supported_extensions.contains("GL_NV_gpu_shader5");
        this.GL_NV_half_float = supported_extensions.contains("GL_NV_half_float");
        this.GL_NV_light_max_exponent = supported_extensions.contains("GL_NV_light_max_exponent");
        this.GL_NV_multisample_coverage = supported_extensions.contains("GL_NV_multisample_coverage");
        this.GL_NV_multisample_filter_hint = supported_extensions.contains("GL_NV_multisample_filter_hint");
        this.GL_NV_occlusion_query = supported_extensions.contains("GL_NV_occlusion_query");
        this.GL_NV_packed_depth_stencil = supported_extensions.contains("GL_NV_packed_depth_stencil");
        this.GL_NV_parameter_buffer_object = supported_extensions.contains("GL_NV_parameter_buffer_object");
        this.GL_NV_parameter_buffer_object2 = supported_extensions.contains("GL_NV_parameter_buffer_object2");
        this.GL_NV_path_rendering = supported_extensions.contains("GL_NV_path_rendering");
        this.GL_NV_pixel_data_range = supported_extensions.contains("GL_NV_pixel_data_range");
        this.GL_NV_point_sprite = supported_extensions.contains("GL_NV_point_sprite");
        this.GL_NV_present_video = supported_extensions.contains("GL_NV_present_video");
        this.GL_NV_primitive_restart = supported_extensions.contains("GL_NV_primitive_restart");
        this.GL_NV_register_combiners = supported_extensions.contains("GL_NV_register_combiners");
        this.GL_NV_register_combiners2 = supported_extensions.contains("GL_NV_register_combiners2");
        this.GL_NV_shader_atomic_counters = supported_extensions.contains("GL_NV_shader_atomic_counters");
        this.GL_NV_shader_atomic_float = supported_extensions.contains("GL_NV_shader_atomic_float");
        this.GL_NV_shader_buffer_load = supported_extensions.contains("GL_NV_shader_buffer_load");
        this.GL_NV_shader_buffer_store = supported_extensions.contains("GL_NV_shader_buffer_store");
        this.GL_NV_shader_storage_buffer_object = supported_extensions.contains("GL_NV_shader_storage_buffer_object");
        this.GL_NV_tessellation_program5 = supported_extensions.contains("GL_NV_tessellation_program5");
        this.GL_NV_texgen_reflection = supported_extensions.contains("GL_NV_texgen_reflection");
        this.GL_NV_texture_barrier = supported_extensions.contains("GL_NV_texture_barrier");
        this.GL_NV_texture_compression_vtc = supported_extensions.contains("GL_NV_texture_compression_vtc");
        this.GL_NV_texture_env_combine4 = supported_extensions.contains("GL_NV_texture_env_combine4");
        this.GL_NV_texture_expand_normal = supported_extensions.contains("GL_NV_texture_expand_normal");
        this.GL_NV_texture_multisample = supported_extensions.contains("GL_NV_texture_multisample");
        this.GL_NV_texture_rectangle = supported_extensions.contains("GL_NV_texture_rectangle");
        this.GL_NV_texture_shader = supported_extensions.contains("GL_NV_texture_shader");
        this.GL_NV_texture_shader2 = supported_extensions.contains("GL_NV_texture_shader2");
        this.GL_NV_texture_shader3 = supported_extensions.contains("GL_NV_texture_shader3");
        this.GL_NV_transform_feedback = supported_extensions.contains("GL_NV_transform_feedback");
        this.GL_NV_transform_feedback2 = supported_extensions.contains("GL_NV_transform_feedback2");
        this.GL_NV_vertex_array_range = supported_extensions.contains("GL_NV_vertex_array_range");
        this.GL_NV_vertex_array_range2 = supported_extensions.contains("GL_NV_vertex_array_range2");
        this.GL_NV_vertex_attrib_integer_64bit = supported_extensions.contains("GL_NV_vertex_attrib_integer_64bit");
        this.GL_NV_vertex_buffer_unified_memory = supported_extensions.contains("GL_NV_vertex_buffer_unified_memory");
        this.GL_NV_vertex_program = (supported_extensions.contains("GL_NV_vertex_program") && supported_extensions.contains("GL_NV_program"));
        this.GL_NV_vertex_program1_1 = supported_extensions.contains("GL_NV_vertex_program1_1");
        this.GL_NV_vertex_program2 = supported_extensions.contains("GL_NV_vertex_program2");
        this.GL_NV_vertex_program2_option = supported_extensions.contains("GL_NV_vertex_program2_option");
        this.GL_NV_vertex_program3 = supported_extensions.contains("GL_NV_vertex_program3");
        this.GL_NV_vertex_program4 = supported_extensions.contains("GL_NV_vertex_program4");
        this.GL_NV_video_capture = supported_extensions.contains("GL_NV_video_capture");
        this.GL_SGIS_generate_mipmap = supported_extensions.contains("GL_SGIS_generate_mipmap");
        this.GL_SGIS_texture_lod = supported_extensions.contains("GL_SGIS_texture_lod");
        this.GL_SUN_slice_accum = supported_extensions.contains("GL_SUN_slice_accum");
        this.tracker.init();
    }
}

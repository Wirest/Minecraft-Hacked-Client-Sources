// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.Buffer;
import java.nio.ShortBuffer;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import java.nio.DoubleBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.ByteBuffer;

public final class ARBImaging
{
    public static final int GL_BLEND_COLOR = 32773;
    public static final int GL_FUNC_ADD = 32774;
    public static final int GL_MIN = 32775;
    public static final int GL_MAX = 32776;
    public static final int GL_BLEND_EQUATION = 32777;
    public static final int GL_FUNC_SUBTRACT = 32778;
    public static final int GL_FUNC_REVERSE_SUBTRACT = 32779;
    public static final int GL_COLOR_MATRIX = 32945;
    public static final int GL_COLOR_MATRIX_STACK_DEPTH = 32946;
    public static final int GL_MAX_COLOR_MATRIX_STACK_DEPTH = 32947;
    public static final int GL_POST_COLOR_MATRIX_RED_SCALE = 32948;
    public static final int GL_POST_COLOR_MATRIX_GREEN_SCALE = 32949;
    public static final int GL_POST_COLOR_MATRIX_BLUE_SCALE = 32950;
    public static final int GL_POST_COLOR_MATRIX_ALPHA_SCALE = 32951;
    public static final int GL_POST_COLOR_MATRIX_RED_BIAS = 32952;
    public static final int GL_POST_COLOR_MATRIX_GREEN_BIAS = 32953;
    public static final int GL_POST_COLOR_MATRIX_BLUE_BIAS = 32954;
    public static final int GL_POST_COLOR_MATRIX_ALPHA_BIAS = 32955;
    public static final int GL_COLOR_TABLE = 32976;
    public static final int GL_POST_CONVOLUTION_COLOR_TABLE = 32977;
    public static final int GL_POST_COLOR_MATRIX_COLOR_TABLE = 32978;
    public static final int GL_PROXY_COLOR_TABLE = 32979;
    public static final int GL_PROXY_POST_CONVOLUTION_COLOR_TABLE = 32980;
    public static final int GL_PROXY_POST_COLOR_MATRIX_COLOR_TABLE = 32981;
    public static final int GL_COLOR_TABLE_SCALE = 32982;
    public static final int GL_COLOR_TABLE_BIAS = 32983;
    public static final int GL_COLOR_TABLE_FORMAT = 32984;
    public static final int GL_COLOR_TABLE_WIDTH = 32985;
    public static final int GL_COLOR_TABLE_RED_SIZE = 32986;
    public static final int GL_COLOR_TABLE_GREEN_SIZE = 32987;
    public static final int GL_COLOR_TABLE_BLUE_SIZE = 32988;
    public static final int GL_COLOR_TABLE_ALPHA_SIZE = 32989;
    public static final int GL_COLOR_TABLE_LUMINANCE_SIZE = 32990;
    public static final int GL_COLOR_TABLE_INTENSITY_SIZE = 32991;
    public static final int GL_CONVOLUTION_1D = 32784;
    public static final int GL_CONVOLUTION_2D = 32785;
    public static final int GL_SEPARABLE_2D = 32786;
    public static final int GL_CONVOLUTION_BORDER_MODE = 32787;
    public static final int GL_CONVOLUTION_FILTER_SCALE = 32788;
    public static final int GL_CONVOLUTION_FILTER_BIAS = 32789;
    public static final int GL_REDUCE = 32790;
    public static final int GL_CONVOLUTION_FORMAT = 32791;
    public static final int GL_CONVOLUTION_WIDTH = 32792;
    public static final int GL_CONVOLUTION_HEIGHT = 32793;
    public static final int GL_MAX_CONVOLUTION_WIDTH = 32794;
    public static final int GL_MAX_CONVOLUTION_HEIGHT = 32795;
    public static final int GL_POST_CONVOLUTION_RED_SCALE = 32796;
    public static final int GL_POST_CONVOLUTION_GREEN_SCALE = 32797;
    public static final int GL_POST_CONVOLUTION_BLUE_SCALE = 32798;
    public static final int GL_POST_CONVOLUTION_ALPHA_SCALE = 32799;
    public static final int GL_POST_CONVOLUTION_RED_BIAS = 32800;
    public static final int GL_POST_CONVOLUTION_GREEN_BIAS = 32801;
    public static final int GL_POST_CONVOLUTION_BLUE_BIAS = 32802;
    public static final int GL_POST_CONVOLUTION_ALPHA_BIAS = 32803;
    public static final int GL_IGNORE_BORDER = 33104;
    public static final int GL_CONSTANT_BORDER = 33105;
    public static final int GL_REPLICATE_BORDER = 33107;
    public static final int GL_CONVOLUTION_BORDER_COLOR = 33108;
    public static final int GL_HISTOGRAM = 32804;
    public static final int GL_PROXY_HISTOGRAM = 32805;
    public static final int GL_HISTOGRAM_WIDTH = 32806;
    public static final int GL_HISTOGRAM_FORMAT = 32807;
    public static final int GL_HISTOGRAM_RED_SIZE = 32808;
    public static final int GL_HISTOGRAM_GREEN_SIZE = 32809;
    public static final int GL_HISTOGRAM_BLUE_SIZE = 32810;
    public static final int GL_HISTOGRAM_ALPHA_SIZE = 32811;
    public static final int GL_HISTOGRAM_LUMINANCE_SIZE = 32812;
    public static final int GL_HISTOGRAM_SINK = 32813;
    public static final int GL_MINMAX = 32814;
    public static final int GL_MINMAX_FORMAT = 32815;
    public static final int GL_MINMAX_SINK = 32816;
    public static final int GL_TABLE_TOO_LARGE = 32817;
    
    private ARBImaging() {
    }
    
    public static void glColorTable(final int target, final int internalFormat, final int width, final int format, final int type, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColorTable;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(data, 256);
        nglColorTable(target, internalFormat, width, format, type, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glColorTable(final int target, final int internalFormat, final int width, final int format, final int type, final DoubleBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColorTable;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(data, 256);
        nglColorTable(target, internalFormat, width, format, type, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glColorTable(final int target, final int internalFormat, final int width, final int format, final int type, final FloatBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColorTable;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(data, 256);
        nglColorTable(target, internalFormat, width, format, type, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglColorTable(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5, final long p6);
    
    public static void glColorTable(final int target, final int internalFormat, final int width, final int format, final int type, final long data_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColorTable;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglColorTableBO(target, internalFormat, width, format, type, data_buffer_offset, function_pointer);
    }
    
    static native void nglColorTableBO(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5, final long p6);
    
    public static void glColorSubTable(final int target, final int start, final int count, final int format, final int type, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColorSubTable;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(data, 256);
        nglColorSubTable(target, start, count, format, type, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glColorSubTable(final int target, final int start, final int count, final int format, final int type, final DoubleBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColorSubTable;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(data, 256);
        nglColorSubTable(target, start, count, format, type, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glColorSubTable(final int target, final int start, final int count, final int format, final int type, final FloatBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColorSubTable;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(data, 256);
        nglColorSubTable(target, start, count, format, type, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglColorSubTable(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5, final long p6);
    
    public static void glColorSubTable(final int target, final int start, final int count, final int format, final int type, final long data_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColorSubTable;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglColorSubTableBO(target, start, count, format, type, data_buffer_offset, function_pointer);
    }
    
    static native void nglColorSubTableBO(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5, final long p6);
    
    public static void glColorTableParameter(final int target, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColorTableParameteriv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglColorTableParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglColorTableParameteriv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glColorTableParameter(final int target, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColorTableParameterfv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglColorTableParameterfv(target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglColorTableParameterfv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glCopyColorSubTable(final int target, final int start, final int x, final int y, final int width) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCopyColorSubTable;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglCopyColorSubTable(target, start, x, y, width, function_pointer);
    }
    
    static native void nglCopyColorSubTable(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glCopyColorTable(final int target, final int internalformat, final int x, final int y, final int width) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCopyColorTable;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglCopyColorTable(target, internalformat, x, y, width, function_pointer);
    }
    
    static native void nglCopyColorTable(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glGetColorTable(final int target, final int format, final int type, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetColorTable;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(data, 256);
        nglGetColorTable(target, format, type, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glGetColorTable(final int target, final int format, final int type, final DoubleBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetColorTable;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(data, 256);
        nglGetColorTable(target, format, type, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glGetColorTable(final int target, final int format, final int type, final FloatBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetColorTable;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(data, 256);
        nglGetColorTable(target, format, type, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglGetColorTable(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetColorTableParameter(final int target, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetColorTableParameteriv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetColorTableParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetColorTableParameteriv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetColorTableParameter(final int target, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetColorTableParameterfv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetColorTableParameterfv(target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetColorTableParameterfv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glBlendEquation(final int mode) {
        GL14.glBlendEquation(mode);
    }
    
    public static void glBlendColor(final float red, final float green, final float blue, final float alpha) {
        GL14.glBlendColor(red, green, blue, alpha);
    }
    
    public static void glHistogram(final int target, final int width, final int internalformat, final boolean sink) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glHistogram;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglHistogram(target, width, internalformat, sink, function_pointer);
    }
    
    static native void nglHistogram(final int p0, final int p1, final int p2, final boolean p3, final long p4);
    
    public static void glResetHistogram(final int target) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glResetHistogram;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglResetHistogram(target, function_pointer);
    }
    
    static native void nglResetHistogram(final int p0, final long p1);
    
    public static void glGetHistogram(final int target, final boolean reset, final int format, final int type, final ByteBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetHistogram;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkBuffer(values, 256);
        nglGetHistogram(target, reset, format, type, MemoryUtil.getAddress(values), function_pointer);
    }
    
    public static void glGetHistogram(final int target, final boolean reset, final int format, final int type, final DoubleBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetHistogram;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkBuffer(values, 256);
        nglGetHistogram(target, reset, format, type, MemoryUtil.getAddress(values), function_pointer);
    }
    
    public static void glGetHistogram(final int target, final boolean reset, final int format, final int type, final FloatBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetHistogram;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkBuffer(values, 256);
        nglGetHistogram(target, reset, format, type, MemoryUtil.getAddress(values), function_pointer);
    }
    
    public static void glGetHistogram(final int target, final boolean reset, final int format, final int type, final IntBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetHistogram;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkBuffer(values, 256);
        nglGetHistogram(target, reset, format, type, MemoryUtil.getAddress(values), function_pointer);
    }
    
    public static void glGetHistogram(final int target, final boolean reset, final int format, final int type, final ShortBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetHistogram;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkBuffer(values, 256);
        nglGetHistogram(target, reset, format, type, MemoryUtil.getAddress(values), function_pointer);
    }
    
    static native void nglGetHistogram(final int p0, final boolean p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glGetHistogram(final int target, final boolean reset, final int format, final int type, final long values_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetHistogram;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOenabled(caps);
        nglGetHistogramBO(target, reset, format, type, values_buffer_offset, function_pointer);
    }
    
    static native void nglGetHistogramBO(final int p0, final boolean p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glGetHistogramParameter(final int target, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetHistogramParameterfv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 256);
        nglGetHistogramParameterfv(target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetHistogramParameterfv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetHistogramParameter(final int target, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetHistogramParameteriv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 256);
        nglGetHistogramParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetHistogramParameteriv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glMinmax(final int target, final int internalformat, final boolean sink) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMinmax;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMinmax(target, internalformat, sink, function_pointer);
    }
    
    static native void nglMinmax(final int p0, final int p1, final boolean p2, final long p3);
    
    public static void glResetMinmax(final int target) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glResetMinmax;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglResetMinmax(target, function_pointer);
    }
    
    static native void nglResetMinmax(final int p0, final long p1);
    
    public static void glGetMinmax(final int target, final boolean reset, final int format, final int types, final ByteBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMinmax;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkBuffer(values, 4);
        nglGetMinmax(target, reset, format, types, MemoryUtil.getAddress(values), function_pointer);
    }
    
    public static void glGetMinmax(final int target, final boolean reset, final int format, final int types, final DoubleBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMinmax;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkBuffer(values, 4);
        nglGetMinmax(target, reset, format, types, MemoryUtil.getAddress(values), function_pointer);
    }
    
    public static void glGetMinmax(final int target, final boolean reset, final int format, final int types, final FloatBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMinmax;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkBuffer(values, 4);
        nglGetMinmax(target, reset, format, types, MemoryUtil.getAddress(values), function_pointer);
    }
    
    public static void glGetMinmax(final int target, final boolean reset, final int format, final int types, final IntBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMinmax;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkBuffer(values, 4);
        nglGetMinmax(target, reset, format, types, MemoryUtil.getAddress(values), function_pointer);
    }
    
    public static void glGetMinmax(final int target, final boolean reset, final int format, final int types, final ShortBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMinmax;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkBuffer(values, 4);
        nglGetMinmax(target, reset, format, types, MemoryUtil.getAddress(values), function_pointer);
    }
    
    static native void nglGetMinmax(final int p0, final boolean p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glGetMinmax(final int target, final boolean reset, final int format, final int types, final long values_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMinmax;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOenabled(caps);
        nglGetMinmaxBO(target, reset, format, types, values_buffer_offset, function_pointer);
    }
    
    static native void nglGetMinmaxBO(final int p0, final boolean p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glGetMinmaxParameter(final int target, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMinmaxParameterfv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetMinmaxParameterfv(target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetMinmaxParameterfv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetMinmaxParameter(final int target, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetMinmaxParameteriv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetMinmaxParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetMinmaxParameteriv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glConvolutionFilter1D(final int target, final int internalformat, final int width, final int format, final int type, final ByteBuffer image) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glConvolutionFilter1D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(image, GLChecks.calculateImageStorage(image, format, type, width, 1, 1));
        nglConvolutionFilter1D(target, internalformat, width, format, type, MemoryUtil.getAddress(image), function_pointer);
    }
    
    public static void glConvolutionFilter1D(final int target, final int internalformat, final int width, final int format, final int type, final DoubleBuffer image) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glConvolutionFilter1D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(image, GLChecks.calculateImageStorage(image, format, type, width, 1, 1));
        nglConvolutionFilter1D(target, internalformat, width, format, type, MemoryUtil.getAddress(image), function_pointer);
    }
    
    public static void glConvolutionFilter1D(final int target, final int internalformat, final int width, final int format, final int type, final FloatBuffer image) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glConvolutionFilter1D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(image, GLChecks.calculateImageStorage(image, format, type, width, 1, 1));
        nglConvolutionFilter1D(target, internalformat, width, format, type, MemoryUtil.getAddress(image), function_pointer);
    }
    
    public static void glConvolutionFilter1D(final int target, final int internalformat, final int width, final int format, final int type, final IntBuffer image) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glConvolutionFilter1D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(image, GLChecks.calculateImageStorage(image, format, type, width, 1, 1));
        nglConvolutionFilter1D(target, internalformat, width, format, type, MemoryUtil.getAddress(image), function_pointer);
    }
    
    public static void glConvolutionFilter1D(final int target, final int internalformat, final int width, final int format, final int type, final ShortBuffer image) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glConvolutionFilter1D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(image, GLChecks.calculateImageStorage(image, format, type, width, 1, 1));
        nglConvolutionFilter1D(target, internalformat, width, format, type, MemoryUtil.getAddress(image), function_pointer);
    }
    
    static native void nglConvolutionFilter1D(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5, final long p6);
    
    public static void glConvolutionFilter1D(final int target, final int internalformat, final int width, final int format, final int type, final long image_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glConvolutionFilter1D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglConvolutionFilter1DBO(target, internalformat, width, format, type, image_buffer_offset, function_pointer);
    }
    
    static native void nglConvolutionFilter1DBO(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5, final long p6);
    
    public static void glConvolutionFilter2D(final int target, final int internalformat, final int width, final int height, final int format, final int type, final ByteBuffer image) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glConvolutionFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(image, GLChecks.calculateImageStorage(image, format, type, width, height, 1));
        nglConvolutionFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(image), function_pointer);
    }
    
    public static void glConvolutionFilter2D(final int target, final int internalformat, final int width, final int height, final int format, final int type, final IntBuffer image) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glConvolutionFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(image, GLChecks.calculateImageStorage(image, format, type, width, height, 1));
        nglConvolutionFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(image), function_pointer);
    }
    
    public static void glConvolutionFilter2D(final int target, final int internalformat, final int width, final int height, final int format, final int type, final ShortBuffer image) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glConvolutionFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkBuffer(image, GLChecks.calculateImageStorage(image, format, type, width, height, 1));
        nglConvolutionFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(image), function_pointer);
    }
    
    static native void nglConvolutionFilter2D(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6, final long p7);
    
    public static void glConvolutionFilter2D(final int target, final int internalformat, final int width, final int height, final int format, final int type, final long image_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glConvolutionFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglConvolutionFilter2DBO(target, internalformat, width, height, format, type, image_buffer_offset, function_pointer);
    }
    
    static native void nglConvolutionFilter2DBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6, final long p7);
    
    public static void glConvolutionParameterf(final int target, final int pname, final float params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glConvolutionParameterf;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglConvolutionParameterf(target, pname, params, function_pointer);
    }
    
    static native void nglConvolutionParameterf(final int p0, final int p1, final float p2, final long p3);
    
    public static void glConvolutionParameter(final int target, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glConvolutionParameterfv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglConvolutionParameterfv(target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglConvolutionParameterfv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glConvolutionParameteri(final int target, final int pname, final int params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glConvolutionParameteri;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglConvolutionParameteri(target, pname, params, function_pointer);
    }
    
    static native void nglConvolutionParameteri(final int p0, final int p1, final int p2, final long p3);
    
    public static void glConvolutionParameter(final int target, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glConvolutionParameteriv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglConvolutionParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglConvolutionParameteriv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glCopyConvolutionFilter1D(final int target, final int internalformat, final int x, final int y, final int width) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCopyConvolutionFilter1D;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglCopyConvolutionFilter1D(target, internalformat, x, y, width, function_pointer);
    }
    
    static native void nglCopyConvolutionFilter1D(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glCopyConvolutionFilter2D(final int target, final int internalformat, final int x, final int y, final int width, final int height) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCopyConvolutionFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglCopyConvolutionFilter2D(target, internalformat, x, y, width, height, function_pointer);
    }
    
    static native void nglCopyConvolutionFilter2D(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6);
    
    public static void glGetConvolutionFilter(final int target, final int format, final int type, final ByteBuffer image) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetConvolutionFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(image);
        nglGetConvolutionFilter(target, format, type, MemoryUtil.getAddress(image), function_pointer);
    }
    
    public static void glGetConvolutionFilter(final int target, final int format, final int type, final DoubleBuffer image) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetConvolutionFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(image);
        nglGetConvolutionFilter(target, format, type, MemoryUtil.getAddress(image), function_pointer);
    }
    
    public static void glGetConvolutionFilter(final int target, final int format, final int type, final FloatBuffer image) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetConvolutionFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(image);
        nglGetConvolutionFilter(target, format, type, MemoryUtil.getAddress(image), function_pointer);
    }
    
    public static void glGetConvolutionFilter(final int target, final int format, final int type, final IntBuffer image) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetConvolutionFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(image);
        nglGetConvolutionFilter(target, format, type, MemoryUtil.getAddress(image), function_pointer);
    }
    
    public static void glGetConvolutionFilter(final int target, final int format, final int type, final ShortBuffer image) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetConvolutionFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(image);
        nglGetConvolutionFilter(target, format, type, MemoryUtil.getAddress(image), function_pointer);
    }
    
    static native void nglGetConvolutionFilter(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetConvolutionFilter(final int target, final int format, final int type, final long image_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetConvolutionFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOenabled(caps);
        nglGetConvolutionFilterBO(target, format, type, image_buffer_offset, function_pointer);
    }
    
    static native void nglGetConvolutionFilterBO(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetConvolutionParameter(final int target, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetConvolutionParameterfv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetConvolutionParameterfv(target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetConvolutionParameterfv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetConvolutionParameter(final int target, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetConvolutionParameteriv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetConvolutionParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetConvolutionParameteriv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glSeparableFilter2D(final int target, final int internalformat, final int width, final int height, final int format, final int type, final ByteBuffer row, final ByteBuffer column) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSeparableFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
    }
    
    public static void glSeparableFilter2D(final int target, final int internalformat, final int width, final int height, final int format, final int type, final ByteBuffer row, final DoubleBuffer column) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSeparableFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
    }
    
    public static void glSeparableFilter2D(final int target, final int internalformat, final int width, final int height, final int format, final int type, final ByteBuffer row, final FloatBuffer column) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSeparableFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
    }
    
    public static void glSeparableFilter2D(final int target, final int internalformat, final int width, final int height, final int format, final int type, final ByteBuffer row, final IntBuffer column) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSeparableFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
    }
    
    public static void glSeparableFilter2D(final int target, final int internalformat, final int width, final int height, final int format, final int type, final ByteBuffer row, final ShortBuffer column) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSeparableFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
    }
    
    public static void glSeparableFilter2D(final int target, final int internalformat, final int width, final int height, final int format, final int type, final DoubleBuffer row, final ByteBuffer column) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSeparableFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
    }
    
    public static void glSeparableFilter2D(final int target, final int internalformat, final int width, final int height, final int format, final int type, final DoubleBuffer row, final DoubleBuffer column) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSeparableFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
    }
    
    public static void glSeparableFilter2D(final int target, final int internalformat, final int width, final int height, final int format, final int type, final DoubleBuffer row, final FloatBuffer column) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSeparableFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
    }
    
    public static void glSeparableFilter2D(final int target, final int internalformat, final int width, final int height, final int format, final int type, final DoubleBuffer row, final IntBuffer column) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSeparableFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
    }
    
    public static void glSeparableFilter2D(final int target, final int internalformat, final int width, final int height, final int format, final int type, final DoubleBuffer row, final ShortBuffer column) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSeparableFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
    }
    
    public static void glSeparableFilter2D(final int target, final int internalformat, final int width, final int height, final int format, final int type, final FloatBuffer row, final ByteBuffer column) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSeparableFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
    }
    
    public static void glSeparableFilter2D(final int target, final int internalformat, final int width, final int height, final int format, final int type, final FloatBuffer row, final DoubleBuffer column) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSeparableFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
    }
    
    public static void glSeparableFilter2D(final int target, final int internalformat, final int width, final int height, final int format, final int type, final FloatBuffer row, final FloatBuffer column) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSeparableFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
    }
    
    public static void glSeparableFilter2D(final int target, final int internalformat, final int width, final int height, final int format, final int type, final FloatBuffer row, final IntBuffer column) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSeparableFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
    }
    
    public static void glSeparableFilter2D(final int target, final int internalformat, final int width, final int height, final int format, final int type, final FloatBuffer row, final ShortBuffer column) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSeparableFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
    }
    
    public static void glSeparableFilter2D(final int target, final int internalformat, final int width, final int height, final int format, final int type, final IntBuffer row, final ByteBuffer column) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSeparableFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
    }
    
    public static void glSeparableFilter2D(final int target, final int internalformat, final int width, final int height, final int format, final int type, final IntBuffer row, final DoubleBuffer column) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSeparableFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
    }
    
    public static void glSeparableFilter2D(final int target, final int internalformat, final int width, final int height, final int format, final int type, final IntBuffer row, final FloatBuffer column) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSeparableFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
    }
    
    public static void glSeparableFilter2D(final int target, final int internalformat, final int width, final int height, final int format, final int type, final IntBuffer row, final IntBuffer column) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSeparableFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
    }
    
    public static void glSeparableFilter2D(final int target, final int internalformat, final int width, final int height, final int format, final int type, final IntBuffer row, final ShortBuffer column) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSeparableFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
    }
    
    public static void glSeparableFilter2D(final int target, final int internalformat, final int width, final int height, final int format, final int type, final ShortBuffer row, final ByteBuffer column) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSeparableFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
    }
    
    public static void glSeparableFilter2D(final int target, final int internalformat, final int width, final int height, final int format, final int type, final ShortBuffer row, final DoubleBuffer column) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSeparableFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
    }
    
    public static void glSeparableFilter2D(final int target, final int internalformat, final int width, final int height, final int format, final int type, final ShortBuffer row, final FloatBuffer column) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSeparableFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
    }
    
    public static void glSeparableFilter2D(final int target, final int internalformat, final int width, final int height, final int format, final int type, final ShortBuffer row, final IntBuffer column) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSeparableFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
    }
    
    public static void glSeparableFilter2D(final int target, final int internalformat, final int width, final int height, final int format, final int type, final ShortBuffer row, final ShortBuffer column) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSeparableFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
    }
    
    static native void nglSeparableFilter2D(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6, final long p7, final long p8);
    
    public static void glSeparableFilter2D(final int target, final int internalformat, final int width, final int height, final int format, final int type, final long row_buffer_offset, final long column_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSeparableFilter2D;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureUnpackPBOenabled(caps);
        nglSeparableFilter2DBO(target, internalformat, width, height, format, type, row_buffer_offset, column_buffer_offset, function_pointer);
    }
    
    static native void nglSeparableFilter2DBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6, final long p7, final long p8);
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ByteBuffer row, final ByteBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ByteBuffer row, final ByteBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ByteBuffer row, final ByteBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ByteBuffer row, final ByteBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ByteBuffer row, final DoubleBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ByteBuffer row, final DoubleBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ByteBuffer row, final DoubleBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ByteBuffer row, final DoubleBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ByteBuffer row, final IntBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ByteBuffer row, final IntBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ByteBuffer row, final IntBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ByteBuffer row, final IntBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ByteBuffer row, final ShortBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ByteBuffer row, final ShortBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ByteBuffer row, final ShortBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ByteBuffer row, final ShortBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final DoubleBuffer row, final ByteBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final DoubleBuffer row, final ByteBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final DoubleBuffer row, final ByteBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final DoubleBuffer row, final ByteBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final DoubleBuffer row, final DoubleBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final DoubleBuffer row, final DoubleBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final DoubleBuffer row, final DoubleBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final DoubleBuffer row, final DoubleBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final DoubleBuffer row, final IntBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final DoubleBuffer row, final IntBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final DoubleBuffer row, final IntBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final DoubleBuffer row, final IntBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final DoubleBuffer row, final ShortBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final DoubleBuffer row, final ShortBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final DoubleBuffer row, final ShortBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final DoubleBuffer row, final ShortBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final FloatBuffer row, final ByteBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final FloatBuffer row, final ByteBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final FloatBuffer row, final ByteBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final FloatBuffer row, final ByteBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final FloatBuffer row, final DoubleBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final FloatBuffer row, final DoubleBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final FloatBuffer row, final DoubleBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final FloatBuffer row, final DoubleBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final FloatBuffer row, final IntBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final FloatBuffer row, final IntBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final FloatBuffer row, final IntBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final FloatBuffer row, final IntBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final FloatBuffer row, final ShortBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final FloatBuffer row, final ShortBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final FloatBuffer row, final ShortBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final FloatBuffer row, final ShortBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final IntBuffer row, final ByteBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final IntBuffer row, final ByteBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final IntBuffer row, final ByteBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final IntBuffer row, final ByteBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final IntBuffer row, final DoubleBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final IntBuffer row, final DoubleBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final IntBuffer row, final DoubleBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final IntBuffer row, final DoubleBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final IntBuffer row, final IntBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final IntBuffer row, final IntBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final IntBuffer row, final IntBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final IntBuffer row, final IntBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final IntBuffer row, final ShortBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final IntBuffer row, final ShortBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final IntBuffer row, final ShortBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final IntBuffer row, final ShortBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ShortBuffer row, final ByteBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ShortBuffer row, final ByteBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ShortBuffer row, final ByteBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ShortBuffer row, final ByteBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ShortBuffer row, final DoubleBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ShortBuffer row, final DoubleBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ShortBuffer row, final DoubleBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ShortBuffer row, final DoubleBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ShortBuffer row, final IntBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ShortBuffer row, final IntBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ShortBuffer row, final IntBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ShortBuffer row, final IntBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ShortBuffer row, final ShortBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ShortBuffer row, final ShortBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ShortBuffer row, final ShortBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final ShortBuffer row, final ShortBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    static native void nglGetSeparableFilter(final int p0, final int p1, final int p2, final long p3, final long p4, final long p5, final long p6);
    
    public static void glGetSeparableFilter(final int target, final int format, final int type, final long row_buffer_offset, final long column_buffer_offset, final long span_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetSeparableFilter;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOenabled(caps);
        nglGetSeparableFilterBO(target, format, type, row_buffer_offset, column_buffer_offset, span_buffer_offset, function_pointer);
    }
    
    static native void nglGetSeparableFilterBO(final int p0, final int p1, final int p2, final long p3, final long p4, final long p5, final long p6);
}

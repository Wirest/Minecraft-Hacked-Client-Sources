// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.MemoryUtil;
import java.nio.DoubleBuffer;
import org.lwjgl.BufferChecks;

public final class ARBRobustness
{
    public static final int GL_GUILTY_CONTEXT_RESET_ARB = 33363;
    public static final int GL_INNOCENT_CONTEXT_RESET_ARB = 33364;
    public static final int GL_UNKNOWN_CONTEXT_RESET_ARB = 33365;
    public static final int GL_RESET_NOTIFICATION_STRATEGY_ARB = 33366;
    public static final int GL_LOSE_CONTEXT_ON_RESET_ARB = 33362;
    public static final int GL_NO_RESET_NOTIFICATION_ARB = 33377;
    public static final int GL_CONTEXT_FLAG_ROBUST_ACCESS_BIT_ARB = 4;
    
    private ARBRobustness() {
    }
    
    public static int glGetGraphicsResetStatusARB() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetGraphicsResetStatusARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nglGetGraphicsResetStatusARB(function_pointer);
        return __result;
    }
    
    static native int nglGetGraphicsResetStatusARB(final long p0);
    
    public static void glGetnMapdvARB(final int target, final int query, final DoubleBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnMapdvARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(v);
        nglGetnMapdvARB(target, query, v.remaining() << 3, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglGetnMapdvARB(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetnMapfvARB(final int target, final int query, final FloatBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnMapfvARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(v);
        nglGetnMapfvARB(target, query, v.remaining() << 2, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglGetnMapfvARB(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetnMapivARB(final int target, final int query, final IntBuffer v) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnMapivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(v);
        nglGetnMapivARB(target, query, v.remaining() << 2, MemoryUtil.getAddress(v), function_pointer);
    }
    
    static native void nglGetnMapivARB(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetnPixelMapfvARB(final int map, final FloatBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnPixelMapfvARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(values);
        nglGetnPixelMapfvARB(map, values.remaining() << 2, MemoryUtil.getAddress(values), function_pointer);
    }
    
    static native void nglGetnPixelMapfvARB(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetnPixelMapuivARB(final int map, final IntBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnPixelMapuivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(values);
        nglGetnPixelMapuivARB(map, values.remaining() << 2, MemoryUtil.getAddress(values), function_pointer);
    }
    
    static native void nglGetnPixelMapuivARB(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetnPixelMapusvARB(final int map, final ShortBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnPixelMapusvARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(values);
        nglGetnPixelMapusvARB(map, values.remaining() << 1, MemoryUtil.getAddress(values), function_pointer);
    }
    
    static native void nglGetnPixelMapusvARB(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetnPolygonStippleARB(final ByteBuffer pattern) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnPolygonStippleARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pattern);
        nglGetnPolygonStippleARB(pattern.remaining(), MemoryUtil.getAddress(pattern), function_pointer);
    }
    
    static native void nglGetnPolygonStippleARB(final int p0, final long p1, final long p2);
    
    public static void glGetnTexImageARB(final int target, final int level, final int format, final int type, final ByteBuffer img) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnTexImageARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(img);
        nglGetnTexImageARB(target, level, format, type, img.remaining(), MemoryUtil.getAddress(img), function_pointer);
    }
    
    public static void glGetnTexImageARB(final int target, final int level, final int format, final int type, final DoubleBuffer img) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnTexImageARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(img);
        nglGetnTexImageARB(target, level, format, type, img.remaining() << 3, MemoryUtil.getAddress(img), function_pointer);
    }
    
    public static void glGetnTexImageARB(final int target, final int level, final int format, final int type, final FloatBuffer img) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnTexImageARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(img);
        nglGetnTexImageARB(target, level, format, type, img.remaining() << 2, MemoryUtil.getAddress(img), function_pointer);
    }
    
    public static void glGetnTexImageARB(final int target, final int level, final int format, final int type, final IntBuffer img) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnTexImageARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(img);
        nglGetnTexImageARB(target, level, format, type, img.remaining() << 2, MemoryUtil.getAddress(img), function_pointer);
    }
    
    public static void glGetnTexImageARB(final int target, final int level, final int format, final int type, final ShortBuffer img) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnTexImageARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(img);
        nglGetnTexImageARB(target, level, format, type, img.remaining() << 1, MemoryUtil.getAddress(img), function_pointer);
    }
    
    static native void nglGetnTexImageARB(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5, final long p6);
    
    public static void glGetnTexImageARB(final int target, final int level, final int format, final int type, final int img_bufSize, final long img_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnTexImageARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOenabled(caps);
        nglGetnTexImageARBBO(target, level, format, type, img_bufSize, img_buffer_offset, function_pointer);
    }
    
    static native void nglGetnTexImageARBBO(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5, final long p6);
    
    public static void glReadnPixelsARB(final int x, final int y, final int width, final int height, final int format, final int type, final ByteBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glReadnPixelsARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(data);
        nglReadnPixelsARB(x, y, width, height, format, type, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glReadnPixelsARB(final int x, final int y, final int width, final int height, final int format, final int type, final DoubleBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glReadnPixelsARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(data);
        nglReadnPixelsARB(x, y, width, height, format, type, data.remaining() << 3, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glReadnPixelsARB(final int x, final int y, final int width, final int height, final int format, final int type, final FloatBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glReadnPixelsARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(data);
        nglReadnPixelsARB(x, y, width, height, format, type, data.remaining() << 2, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glReadnPixelsARB(final int x, final int y, final int width, final int height, final int format, final int type, final IntBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glReadnPixelsARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(data);
        nglReadnPixelsARB(x, y, width, height, format, type, data.remaining() << 2, MemoryUtil.getAddress(data), function_pointer);
    }
    
    public static void glReadnPixelsARB(final int x, final int y, final int width, final int height, final int format, final int type, final ShortBuffer data) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glReadnPixelsARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(data);
        nglReadnPixelsARB(x, y, width, height, format, type, data.remaining() << 1, MemoryUtil.getAddress(data), function_pointer);
    }
    
    static native void nglReadnPixelsARB(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final long p7, final long p8);
    
    public static void glReadnPixelsARB(final int x, final int y, final int width, final int height, final int format, final int type, final int data_bufSize, final long data_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glReadnPixelsARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOenabled(caps);
        nglReadnPixelsARBBO(x, y, width, height, format, type, data_bufSize, data_buffer_offset, function_pointer);
    }
    
    static native void nglReadnPixelsARBBO(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final long p7, final long p8);
    
    public static void glGetnColorTableARB(final int target, final int format, final int type, final ByteBuffer table) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnColorTableARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(table);
        nglGetnColorTableARB(target, format, type, table.remaining(), MemoryUtil.getAddress(table), function_pointer);
    }
    
    public static void glGetnColorTableARB(final int target, final int format, final int type, final DoubleBuffer table) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnColorTableARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(table);
        nglGetnColorTableARB(target, format, type, table.remaining() << 3, MemoryUtil.getAddress(table), function_pointer);
    }
    
    public static void glGetnColorTableARB(final int target, final int format, final int type, final FloatBuffer table) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnColorTableARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(table);
        nglGetnColorTableARB(target, format, type, table.remaining() << 2, MemoryUtil.getAddress(table), function_pointer);
    }
    
    static native void nglGetnColorTableARB(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glGetnConvolutionFilterARB(final int target, final int format, final int type, final ByteBuffer image) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnConvolutionFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(image);
        nglGetnConvolutionFilterARB(target, format, type, image.remaining(), MemoryUtil.getAddress(image), function_pointer);
    }
    
    public static void glGetnConvolutionFilterARB(final int target, final int format, final int type, final DoubleBuffer image) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnConvolutionFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(image);
        nglGetnConvolutionFilterARB(target, format, type, image.remaining() << 3, MemoryUtil.getAddress(image), function_pointer);
    }
    
    public static void glGetnConvolutionFilterARB(final int target, final int format, final int type, final FloatBuffer image) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnConvolutionFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(image);
        nglGetnConvolutionFilterARB(target, format, type, image.remaining() << 2, MemoryUtil.getAddress(image), function_pointer);
    }
    
    public static void glGetnConvolutionFilterARB(final int target, final int format, final int type, final IntBuffer image) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnConvolutionFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(image);
        nglGetnConvolutionFilterARB(target, format, type, image.remaining() << 2, MemoryUtil.getAddress(image), function_pointer);
    }
    
    public static void glGetnConvolutionFilterARB(final int target, final int format, final int type, final ShortBuffer image) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnConvolutionFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(image);
        nglGetnConvolutionFilterARB(target, format, type, image.remaining() << 1, MemoryUtil.getAddress(image), function_pointer);
    }
    
    static native void nglGetnConvolutionFilterARB(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glGetnConvolutionFilterARB(final int target, final int format, final int type, final int image_bufSize, final long image_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnConvolutionFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOenabled(caps);
        nglGetnConvolutionFilterARBBO(target, format, type, image_bufSize, image_buffer_offset, function_pointer);
    }
    
    static native void nglGetnConvolutionFilterARBBO(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ByteBuffer row, final ByteBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ByteBuffer row, final ByteBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ByteBuffer row, final ByteBuffer column, final FloatBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ByteBuffer row, final ByteBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ByteBuffer row, final ByteBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ByteBuffer row, final DoubleBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ByteBuffer row, final DoubleBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ByteBuffer row, final DoubleBuffer column, final FloatBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ByteBuffer row, final DoubleBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ByteBuffer row, final DoubleBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ByteBuffer row, final FloatBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ByteBuffer row, final FloatBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ByteBuffer row, final FloatBuffer column, final FloatBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ByteBuffer row, final FloatBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ByteBuffer row, final FloatBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ByteBuffer row, final IntBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ByteBuffer row, final IntBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ByteBuffer row, final IntBuffer column, final FloatBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ByteBuffer row, final IntBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ByteBuffer row, final IntBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ByteBuffer row, final ShortBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ByteBuffer row, final ShortBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ByteBuffer row, final ShortBuffer column, final FloatBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ByteBuffer row, final ShortBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ByteBuffer row, final ShortBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final DoubleBuffer row, final ByteBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final DoubleBuffer row, final ByteBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final DoubleBuffer row, final ByteBuffer column, final FloatBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final DoubleBuffer row, final ByteBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final DoubleBuffer row, final ByteBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final DoubleBuffer row, final DoubleBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final DoubleBuffer row, final DoubleBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final DoubleBuffer row, final DoubleBuffer column, final FloatBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final DoubleBuffer row, final DoubleBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final DoubleBuffer row, final DoubleBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final DoubleBuffer row, final FloatBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final DoubleBuffer row, final FloatBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final DoubleBuffer row, final FloatBuffer column, final FloatBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final DoubleBuffer row, final FloatBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final DoubleBuffer row, final FloatBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final DoubleBuffer row, final IntBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final DoubleBuffer row, final IntBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final DoubleBuffer row, final IntBuffer column, final FloatBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final DoubleBuffer row, final IntBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final DoubleBuffer row, final IntBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final DoubleBuffer row, final ShortBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final DoubleBuffer row, final ShortBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final DoubleBuffer row, final ShortBuffer column, final FloatBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final DoubleBuffer row, final ShortBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final DoubleBuffer row, final ShortBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final FloatBuffer row, final ByteBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final FloatBuffer row, final ByteBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final FloatBuffer row, final ByteBuffer column, final FloatBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final FloatBuffer row, final ByteBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final FloatBuffer row, final ByteBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final FloatBuffer row, final DoubleBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final FloatBuffer row, final DoubleBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final FloatBuffer row, final DoubleBuffer column, final FloatBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final FloatBuffer row, final DoubleBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final FloatBuffer row, final DoubleBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final FloatBuffer row, final FloatBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final FloatBuffer row, final FloatBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final FloatBuffer row, final FloatBuffer column, final FloatBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final FloatBuffer row, final FloatBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final FloatBuffer row, final FloatBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final FloatBuffer row, final IntBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final FloatBuffer row, final IntBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final FloatBuffer row, final IntBuffer column, final FloatBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final FloatBuffer row, final IntBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final FloatBuffer row, final IntBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final FloatBuffer row, final ShortBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final FloatBuffer row, final ShortBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final FloatBuffer row, final ShortBuffer column, final FloatBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final FloatBuffer row, final ShortBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final FloatBuffer row, final ShortBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final IntBuffer row, final ByteBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final IntBuffer row, final ByteBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final IntBuffer row, final ByteBuffer column, final FloatBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final IntBuffer row, final ByteBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final IntBuffer row, final ByteBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final IntBuffer row, final DoubleBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final IntBuffer row, final DoubleBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final IntBuffer row, final DoubleBuffer column, final FloatBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final IntBuffer row, final DoubleBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final IntBuffer row, final DoubleBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final IntBuffer row, final FloatBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final IntBuffer row, final FloatBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final IntBuffer row, final FloatBuffer column, final FloatBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final IntBuffer row, final FloatBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final IntBuffer row, final FloatBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final IntBuffer row, final IntBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final IntBuffer row, final IntBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final IntBuffer row, final IntBuffer column, final FloatBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final IntBuffer row, final IntBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final IntBuffer row, final IntBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final IntBuffer row, final ShortBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final IntBuffer row, final ShortBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final IntBuffer row, final ShortBuffer column, final FloatBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final IntBuffer row, final ShortBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final IntBuffer row, final ShortBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ShortBuffer row, final ByteBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ShortBuffer row, final ByteBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ShortBuffer row, final ByteBuffer column, final FloatBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ShortBuffer row, final ByteBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ShortBuffer row, final ByteBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ShortBuffer row, final DoubleBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ShortBuffer row, final DoubleBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ShortBuffer row, final DoubleBuffer column, final FloatBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ShortBuffer row, final DoubleBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ShortBuffer row, final DoubleBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ShortBuffer row, final FloatBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ShortBuffer row, final FloatBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ShortBuffer row, final FloatBuffer column, final FloatBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ShortBuffer row, final FloatBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ShortBuffer row, final FloatBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ShortBuffer row, final IntBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ShortBuffer row, final IntBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ShortBuffer row, final IntBuffer column, final FloatBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ShortBuffer row, final IntBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ShortBuffer row, final IntBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ShortBuffer row, final ShortBuffer column, final ByteBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ShortBuffer row, final ShortBuffer column, final DoubleBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ShortBuffer row, final ShortBuffer column, final FloatBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ShortBuffer row, final ShortBuffer column, final IntBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final ShortBuffer row, final ShortBuffer column, final ShortBuffer span) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(row);
        BufferChecks.checkDirect(column);
        BufferChecks.checkDirect(span);
        nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
    }
    
    static native void nglGetnSeparableFilterARB(final int p0, final int p1, final int p2, final int p3, final long p4, final int p5, final long p6, final long p7, final long p8);
    
    public static void glGetnSeparableFilterARB(final int target, final int format, final int type, final int row_rowBufSize, final long row_buffer_offset, final int column_columnBufSize, final long column_buffer_offset, final long span_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnSeparableFilterARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOenabled(caps);
        nglGetnSeparableFilterARBBO(target, format, type, row_rowBufSize, row_buffer_offset, column_columnBufSize, column_buffer_offset, span_buffer_offset, function_pointer);
    }
    
    static native void nglGetnSeparableFilterARBBO(final int p0, final int p1, final int p2, final int p3, final long p4, final int p5, final long p6, final long p7, final long p8);
    
    public static void glGetnHistogramARB(final int target, final boolean reset, final int format, final int type, final ByteBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnHistogramARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(values);
        nglGetnHistogramARB(target, reset, format, type, values.remaining(), MemoryUtil.getAddress(values), function_pointer);
    }
    
    public static void glGetnHistogramARB(final int target, final boolean reset, final int format, final int type, final DoubleBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnHistogramARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(values);
        nglGetnHistogramARB(target, reset, format, type, values.remaining() << 3, MemoryUtil.getAddress(values), function_pointer);
    }
    
    public static void glGetnHistogramARB(final int target, final boolean reset, final int format, final int type, final FloatBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnHistogramARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(values);
        nglGetnHistogramARB(target, reset, format, type, values.remaining() << 2, MemoryUtil.getAddress(values), function_pointer);
    }
    
    public static void glGetnHistogramARB(final int target, final boolean reset, final int format, final int type, final IntBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnHistogramARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(values);
        nglGetnHistogramARB(target, reset, format, type, values.remaining() << 2, MemoryUtil.getAddress(values), function_pointer);
    }
    
    public static void glGetnHistogramARB(final int target, final boolean reset, final int format, final int type, final ShortBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnHistogramARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(values);
        nglGetnHistogramARB(target, reset, format, type, values.remaining() << 1, MemoryUtil.getAddress(values), function_pointer);
    }
    
    static native void nglGetnHistogramARB(final int p0, final boolean p1, final int p2, final int p3, final int p4, final long p5, final long p6);
    
    public static void glGetnHistogramARB(final int target, final boolean reset, final int format, final int type, final int values_bufSize, final long values_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnHistogramARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOenabled(caps);
        nglGetnHistogramARBBO(target, reset, format, type, values_bufSize, values_buffer_offset, function_pointer);
    }
    
    static native void nglGetnHistogramARBBO(final int p0, final boolean p1, final int p2, final int p3, final int p4, final long p5, final long p6);
    
    public static void glGetnMinmaxARB(final int target, final boolean reset, final int format, final int type, final ByteBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnMinmaxARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(values);
        nglGetnMinmaxARB(target, reset, format, type, values.remaining(), MemoryUtil.getAddress(values), function_pointer);
    }
    
    public static void glGetnMinmaxARB(final int target, final boolean reset, final int format, final int type, final DoubleBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnMinmaxARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(values);
        nglGetnMinmaxARB(target, reset, format, type, values.remaining() << 3, MemoryUtil.getAddress(values), function_pointer);
    }
    
    public static void glGetnMinmaxARB(final int target, final boolean reset, final int format, final int type, final FloatBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnMinmaxARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(values);
        nglGetnMinmaxARB(target, reset, format, type, values.remaining() << 2, MemoryUtil.getAddress(values), function_pointer);
    }
    
    public static void glGetnMinmaxARB(final int target, final boolean reset, final int format, final int type, final IntBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnMinmaxARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(values);
        nglGetnMinmaxARB(target, reset, format, type, values.remaining() << 2, MemoryUtil.getAddress(values), function_pointer);
    }
    
    public static void glGetnMinmaxARB(final int target, final boolean reset, final int format, final int type, final ShortBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnMinmaxARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(values);
        nglGetnMinmaxARB(target, reset, format, type, values.remaining() << 1, MemoryUtil.getAddress(values), function_pointer);
    }
    
    static native void nglGetnMinmaxARB(final int p0, final boolean p1, final int p2, final int p3, final int p4, final long p5, final long p6);
    
    public static void glGetnMinmaxARB(final int target, final boolean reset, final int format, final int type, final int values_bufSize, final long values_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnMinmaxARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOenabled(caps);
        nglGetnMinmaxARBBO(target, reset, format, type, values_bufSize, values_buffer_offset, function_pointer);
    }
    
    static native void nglGetnMinmaxARBBO(final int p0, final boolean p1, final int p2, final int p3, final int p4, final long p5, final long p6);
    
    public static void glGetnCompressedTexImageARB(final int target, final int lod, final ByteBuffer img) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnCompressedTexImageARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(img);
        nglGetnCompressedTexImageARB(target, lod, img.remaining(), MemoryUtil.getAddress(img), function_pointer);
    }
    
    public static void glGetnCompressedTexImageARB(final int target, final int lod, final IntBuffer img) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnCompressedTexImageARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(img);
        nglGetnCompressedTexImageARB(target, lod, img.remaining() << 2, MemoryUtil.getAddress(img), function_pointer);
    }
    
    public static void glGetnCompressedTexImageARB(final int target, final int lod, final ShortBuffer img) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnCompressedTexImageARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOdisabled(caps);
        BufferChecks.checkDirect(img);
        nglGetnCompressedTexImageARB(target, lod, img.remaining() << 1, MemoryUtil.getAddress(img), function_pointer);
    }
    
    static native void nglGetnCompressedTexImageARB(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetnCompressedTexImageARB(final int target, final int lod, final int img_bufSize, final long img_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnCompressedTexImageARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensurePackPBOenabled(caps);
        nglGetnCompressedTexImageARBBO(target, lod, img_bufSize, img_buffer_offset, function_pointer);
    }
    
    static native void nglGetnCompressedTexImageARBBO(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetnUniformfvARB(final int program, final int location, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnUniformfvARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglGetnUniformfvARB(program, location, params.remaining() << 2, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetnUniformfvARB(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetnUniformivARB(final int program, final int location, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnUniformivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglGetnUniformivARB(program, location, params.remaining() << 2, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetnUniformivARB(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetnUniformuivARB(final int program, final int location, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnUniformuivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglGetnUniformuivARB(program, location, params.remaining() << 2, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetnUniformuivARB(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glGetnUniformdvARB(final int program, final int location, final DoubleBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetnUniformdvARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglGetnUniformdvARB(program, location, params.remaining() << 3, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetnUniformdvARB(final int p0, final int p1, final int p2, final long p3, final long p4);
}

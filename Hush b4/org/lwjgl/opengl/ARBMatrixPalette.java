// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ShortBuffer;
import java.nio.IntBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.LWJGLUtil;
import java.nio.ByteBuffer;
import org.lwjgl.BufferChecks;

public final class ARBMatrixPalette
{
    public static final int GL_MATRIX_PALETTE_ARB = 34880;
    public static final int GL_MAX_MATRIX_PALETTE_STACK_DEPTH_ARB = 34881;
    public static final int GL_MAX_PALETTE_MATRICES_ARB = 34882;
    public static final int GL_CURRENT_PALETTE_MATRIX_ARB = 34883;
    public static final int GL_MATRIX_INDEX_ARRAY_ARB = 34884;
    public static final int GL_CURRENT_MATRIX_INDEX_ARB = 34885;
    public static final int GL_MATRIX_INDEX_ARRAY_SIZE_ARB = 34886;
    public static final int GL_MATRIX_INDEX_ARRAY_TYPE_ARB = 34887;
    public static final int GL_MATRIX_INDEX_ARRAY_STRIDE_ARB = 34888;
    public static final int GL_MATRIX_INDEX_ARRAY_POINTER_ARB = 34889;
    
    private ARBMatrixPalette() {
    }
    
    public static void glCurrentPaletteMatrixARB(final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCurrentPaletteMatrixARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglCurrentPaletteMatrixARB(index, function_pointer);
    }
    
    static native void nglCurrentPaletteMatrixARB(final int p0, final long p1);
    
    public static void glMatrixIndexPointerARB(final int size, final int stride, final ByteBuffer pPointer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMatrixIndexPointerARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(pPointer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).ARB_matrix_palette_glMatrixIndexPointerARB_pPointer = pPointer;
        }
        nglMatrixIndexPointerARB(size, 5121, stride, MemoryUtil.getAddress(pPointer), function_pointer);
    }
    
    public static void glMatrixIndexPointerARB(final int size, final int stride, final IntBuffer pPointer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMatrixIndexPointerARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(pPointer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).ARB_matrix_palette_glMatrixIndexPointerARB_pPointer = pPointer;
        }
        nglMatrixIndexPointerARB(size, 5125, stride, MemoryUtil.getAddress(pPointer), function_pointer);
    }
    
    public static void glMatrixIndexPointerARB(final int size, final int stride, final ShortBuffer pPointer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMatrixIndexPointerARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(pPointer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).ARB_matrix_palette_glMatrixIndexPointerARB_pPointer = pPointer;
        }
        nglMatrixIndexPointerARB(size, 5123, stride, MemoryUtil.getAddress(pPointer), function_pointer);
    }
    
    static native void nglMatrixIndexPointerARB(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glMatrixIndexPointerARB(final int size, final int type, final int stride, final long pPointer_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMatrixIndexPointerARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOenabled(caps);
        nglMatrixIndexPointerARBBO(size, type, stride, pPointer_buffer_offset, function_pointer);
    }
    
    static native void nglMatrixIndexPointerARBBO(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glMatrixIndexuARB(final ByteBuffer pIndices) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMatrixIndexubvARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pIndices);
        nglMatrixIndexubvARB(pIndices.remaining(), MemoryUtil.getAddress(pIndices), function_pointer);
    }
    
    static native void nglMatrixIndexubvARB(final int p0, final long p1, final long p2);
    
    public static void glMatrixIndexuARB(final ShortBuffer pIndices) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMatrixIndexusvARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pIndices);
        nglMatrixIndexusvARB(pIndices.remaining(), MemoryUtil.getAddress(pIndices), function_pointer);
    }
    
    static native void nglMatrixIndexusvARB(final int p0, final long p1, final long p2);
    
    public static void glMatrixIndexuARB(final IntBuffer pIndices) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMatrixIndexuivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pIndices);
        nglMatrixIndexuivARB(pIndices.remaining(), MemoryUtil.getAddress(pIndices), function_pointer);
    }
    
    static native void nglMatrixIndexuivARB(final int p0, final long p1, final long p2);
}

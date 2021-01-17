// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;

public final class NVHalfFloat
{
    public static final int GL_HALF_FLOAT_NV = 5131;
    
    private NVHalfFloat() {
    }
    
    public static void glVertex2hNV(final short x, final short y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertex2hNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertex2hNV(x, y, function_pointer);
    }
    
    static native void nglVertex2hNV(final short p0, final short p1, final long p2);
    
    public static void glVertex3hNV(final short x, final short y, final short z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertex3hNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertex3hNV(x, y, z, function_pointer);
    }
    
    static native void nglVertex3hNV(final short p0, final short p1, final short p2, final long p3);
    
    public static void glVertex4hNV(final short x, final short y, final short z, final short w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertex4hNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertex4hNV(x, y, z, w, function_pointer);
    }
    
    static native void nglVertex4hNV(final short p0, final short p1, final short p2, final short p3, final long p4);
    
    public static void glNormal3hNV(final short nx, final short ny, final short nz) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNormal3hNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNormal3hNV(nx, ny, nz, function_pointer);
    }
    
    static native void nglNormal3hNV(final short p0, final short p1, final short p2, final long p3);
    
    public static void glColor3hNV(final short red, final short green, final short blue) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColor3hNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglColor3hNV(red, green, blue, function_pointer);
    }
    
    static native void nglColor3hNV(final short p0, final short p1, final short p2, final long p3);
    
    public static void glColor4hNV(final short red, final short green, final short blue, final short alpha) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColor4hNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglColor4hNV(red, green, blue, alpha, function_pointer);
    }
    
    static native void nglColor4hNV(final short p0, final short p1, final short p2, final short p3, final long p4);
    
    public static void glTexCoord1hNV(final short s) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTexCoord1hNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTexCoord1hNV(s, function_pointer);
    }
    
    static native void nglTexCoord1hNV(final short p0, final long p1);
    
    public static void glTexCoord2hNV(final short s, final short t) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTexCoord2hNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTexCoord2hNV(s, t, function_pointer);
    }
    
    static native void nglTexCoord2hNV(final short p0, final short p1, final long p2);
    
    public static void glTexCoord3hNV(final short s, final short t, final short r) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTexCoord3hNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTexCoord3hNV(s, t, r, function_pointer);
    }
    
    static native void nglTexCoord3hNV(final short p0, final short p1, final short p2, final long p3);
    
    public static void glTexCoord4hNV(final short s, final short t, final short r, final short q) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTexCoord4hNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTexCoord4hNV(s, t, r, q, function_pointer);
    }
    
    static native void nglTexCoord4hNV(final short p0, final short p1, final short p2, final short p3, final long p4);
    
    public static void glMultiTexCoord1hNV(final int target, final short s) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoord1hNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexCoord1hNV(target, s, function_pointer);
    }
    
    static native void nglMultiTexCoord1hNV(final int p0, final short p1, final long p2);
    
    public static void glMultiTexCoord2hNV(final int target, final short s, final short t) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoord2hNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexCoord2hNV(target, s, t, function_pointer);
    }
    
    static native void nglMultiTexCoord2hNV(final int p0, final short p1, final short p2, final long p3);
    
    public static void glMultiTexCoord3hNV(final int target, final short s, final short t, final short r) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoord3hNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexCoord3hNV(target, s, t, r, function_pointer);
    }
    
    static native void nglMultiTexCoord3hNV(final int p0, final short p1, final short p2, final short p3, final long p4);
    
    public static void glMultiTexCoord4hNV(final int target, final short s, final short t, final short r, final short q) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoord4hNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexCoord4hNV(target, s, t, r, q, function_pointer);
    }
    
    static native void nglMultiTexCoord4hNV(final int p0, final short p1, final short p2, final short p3, final short p4, final long p5);
    
    public static void glFogCoordhNV(final short fog) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFogCoordhNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglFogCoordhNV(fog, function_pointer);
    }
    
    static native void nglFogCoordhNV(final short p0, final long p1);
    
    public static void glSecondaryColor3hNV(final short red, final short green, final short blue) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSecondaryColor3hNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglSecondaryColor3hNV(red, green, blue, function_pointer);
    }
    
    static native void nglSecondaryColor3hNV(final short p0, final short p1, final short p2, final long p3);
    
    public static void glVertexWeighthNV(final short weight) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexWeighthNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexWeighthNV(weight, function_pointer);
    }
    
    static native void nglVertexWeighthNV(final short p0, final long p1);
    
    public static void glVertexAttrib1hNV(final int index, final short x) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib1hNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib1hNV(index, x, function_pointer);
    }
    
    static native void nglVertexAttrib1hNV(final int p0, final short p1, final long p2);
    
    public static void glVertexAttrib2hNV(final int index, final short x, final short y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib2hNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib2hNV(index, x, y, function_pointer);
    }
    
    static native void nglVertexAttrib2hNV(final int p0, final short p1, final short p2, final long p3);
    
    public static void glVertexAttrib3hNV(final int index, final short x, final short y, final short z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib3hNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib3hNV(index, x, y, z, function_pointer);
    }
    
    static native void nglVertexAttrib3hNV(final int p0, final short p1, final short p2, final short p3, final long p4);
    
    public static void glVertexAttrib4hNV(final int index, final short x, final short y, final short z, final short w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib4hNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib4hNV(index, x, y, z, w, function_pointer);
    }
    
    static native void nglVertexAttrib4hNV(final int p0, final short p1, final short p2, final short p3, final short p4, final long p5);
    
    public static void glVertexAttribs1NV(final int index, final ShortBuffer attribs) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribs1hvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(attribs);
        nglVertexAttribs1hvNV(index, attribs.remaining(), MemoryUtil.getAddress(attribs), function_pointer);
    }
    
    static native void nglVertexAttribs1hvNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glVertexAttribs2NV(final int index, final ShortBuffer attribs) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribs2hvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(attribs);
        nglVertexAttribs2hvNV(index, attribs.remaining() >> 1, MemoryUtil.getAddress(attribs), function_pointer);
    }
    
    static native void nglVertexAttribs2hvNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glVertexAttribs3NV(final int index, final ShortBuffer attribs) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribs3hvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(attribs);
        nglVertexAttribs3hvNV(index, attribs.remaining() / 3, MemoryUtil.getAddress(attribs), function_pointer);
    }
    
    static native void nglVertexAttribs3hvNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glVertexAttribs4NV(final int index, final ShortBuffer attribs) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribs4hvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(attribs);
        nglVertexAttribs4hvNV(index, attribs.remaining() >> 2, MemoryUtil.getAddress(attribs), function_pointer);
    }
    
    static native void nglVertexAttribs4hvNV(final int p0, final int p1, final long p2, final long p3);
}

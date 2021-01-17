// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ARBMultitexture
{
    public static final int GL_TEXTURE0_ARB = 33984;
    public static final int GL_TEXTURE1_ARB = 33985;
    public static final int GL_TEXTURE2_ARB = 33986;
    public static final int GL_TEXTURE3_ARB = 33987;
    public static final int GL_TEXTURE4_ARB = 33988;
    public static final int GL_TEXTURE5_ARB = 33989;
    public static final int GL_TEXTURE6_ARB = 33990;
    public static final int GL_TEXTURE7_ARB = 33991;
    public static final int GL_TEXTURE8_ARB = 33992;
    public static final int GL_TEXTURE9_ARB = 33993;
    public static final int GL_TEXTURE10_ARB = 33994;
    public static final int GL_TEXTURE11_ARB = 33995;
    public static final int GL_TEXTURE12_ARB = 33996;
    public static final int GL_TEXTURE13_ARB = 33997;
    public static final int GL_TEXTURE14_ARB = 33998;
    public static final int GL_TEXTURE15_ARB = 33999;
    public static final int GL_TEXTURE16_ARB = 34000;
    public static final int GL_TEXTURE17_ARB = 34001;
    public static final int GL_TEXTURE18_ARB = 34002;
    public static final int GL_TEXTURE19_ARB = 34003;
    public static final int GL_TEXTURE20_ARB = 34004;
    public static final int GL_TEXTURE21_ARB = 34005;
    public static final int GL_TEXTURE22_ARB = 34006;
    public static final int GL_TEXTURE23_ARB = 34007;
    public static final int GL_TEXTURE24_ARB = 34008;
    public static final int GL_TEXTURE25_ARB = 34009;
    public static final int GL_TEXTURE26_ARB = 34010;
    public static final int GL_TEXTURE27_ARB = 34011;
    public static final int GL_TEXTURE28_ARB = 34012;
    public static final int GL_TEXTURE29_ARB = 34013;
    public static final int GL_TEXTURE30_ARB = 34014;
    public static final int GL_TEXTURE31_ARB = 34015;
    public static final int GL_ACTIVE_TEXTURE_ARB = 34016;
    public static final int GL_CLIENT_ACTIVE_TEXTURE_ARB = 34017;
    public static final int GL_MAX_TEXTURE_UNITS_ARB = 34018;
    
    private ARBMultitexture() {
    }
    
    public static void glClientActiveTextureARB(final int texture) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glClientActiveTextureARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglClientActiveTextureARB(texture, function_pointer);
    }
    
    static native void nglClientActiveTextureARB(final int p0, final long p1);
    
    public static void glActiveTextureARB(final int texture) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glActiveTextureARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglActiveTextureARB(texture, function_pointer);
    }
    
    static native void nglActiveTextureARB(final int p0, final long p1);
    
    public static void glMultiTexCoord1fARB(final int target, final float s) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoord1fARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexCoord1fARB(target, s, function_pointer);
    }
    
    static native void nglMultiTexCoord1fARB(final int p0, final float p1, final long p2);
    
    public static void glMultiTexCoord1dARB(final int target, final double s) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoord1dARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexCoord1dARB(target, s, function_pointer);
    }
    
    static native void nglMultiTexCoord1dARB(final int p0, final double p1, final long p2);
    
    public static void glMultiTexCoord1iARB(final int target, final int s) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoord1iARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexCoord1iARB(target, s, function_pointer);
    }
    
    static native void nglMultiTexCoord1iARB(final int p0, final int p1, final long p2);
    
    public static void glMultiTexCoord1sARB(final int target, final short s) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoord1sARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexCoord1sARB(target, s, function_pointer);
    }
    
    static native void nglMultiTexCoord1sARB(final int p0, final short p1, final long p2);
    
    public static void glMultiTexCoord2fARB(final int target, final float s, final float t) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoord2fARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexCoord2fARB(target, s, t, function_pointer);
    }
    
    static native void nglMultiTexCoord2fARB(final int p0, final float p1, final float p2, final long p3);
    
    public static void glMultiTexCoord2dARB(final int target, final double s, final double t) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoord2dARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexCoord2dARB(target, s, t, function_pointer);
    }
    
    static native void nglMultiTexCoord2dARB(final int p0, final double p1, final double p2, final long p3);
    
    public static void glMultiTexCoord2iARB(final int target, final int s, final int t) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoord2iARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexCoord2iARB(target, s, t, function_pointer);
    }
    
    static native void nglMultiTexCoord2iARB(final int p0, final int p1, final int p2, final long p3);
    
    public static void glMultiTexCoord2sARB(final int target, final short s, final short t) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoord2sARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexCoord2sARB(target, s, t, function_pointer);
    }
    
    static native void nglMultiTexCoord2sARB(final int p0, final short p1, final short p2, final long p3);
    
    public static void glMultiTexCoord3fARB(final int target, final float s, final float t, final float r) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoord3fARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexCoord3fARB(target, s, t, r, function_pointer);
    }
    
    static native void nglMultiTexCoord3fARB(final int p0, final float p1, final float p2, final float p3, final long p4);
    
    public static void glMultiTexCoord3dARB(final int target, final double s, final double t, final double r) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoord3dARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexCoord3dARB(target, s, t, r, function_pointer);
    }
    
    static native void nglMultiTexCoord3dARB(final int p0, final double p1, final double p2, final double p3, final long p4);
    
    public static void glMultiTexCoord3iARB(final int target, final int s, final int t, final int r) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoord3iARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexCoord3iARB(target, s, t, r, function_pointer);
    }
    
    static native void nglMultiTexCoord3iARB(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glMultiTexCoord3sARB(final int target, final short s, final short t, final short r) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoord3sARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexCoord3sARB(target, s, t, r, function_pointer);
    }
    
    static native void nglMultiTexCoord3sARB(final int p0, final short p1, final short p2, final short p3, final long p4);
    
    public static void glMultiTexCoord4fARB(final int target, final float s, final float t, final float r, final float q) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoord4fARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexCoord4fARB(target, s, t, r, q, function_pointer);
    }
    
    static native void nglMultiTexCoord4fARB(final int p0, final float p1, final float p2, final float p3, final float p4, final long p5);
    
    public static void glMultiTexCoord4dARB(final int target, final double s, final double t, final double r, final double q) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoord4dARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexCoord4dARB(target, s, t, r, q, function_pointer);
    }
    
    static native void nglMultiTexCoord4dARB(final int p0, final double p1, final double p2, final double p3, final double p4, final long p5);
    
    public static void glMultiTexCoord4iARB(final int target, final int s, final int t, final int r, final int q) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoord4iARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexCoord4iARB(target, s, t, r, q, function_pointer);
    }
    
    static native void nglMultiTexCoord4iARB(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glMultiTexCoord4sARB(final int target, final short s, final short t, final short r, final short q) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glMultiTexCoord4sARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglMultiTexCoord4sARB(target, s, t, r, q, function_pointer);
    }
    
    static native void nglMultiTexCoord4sARB(final int p0, final short p1, final short p2, final short p3, final short p4, final long p5);
}

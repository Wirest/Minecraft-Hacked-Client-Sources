// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ATIVertexStreams
{
    public static final int GL_MAX_VERTEX_STREAMS_ATI = 34667;
    public static final int GL_VERTEX_SOURCE_ATI = 34668;
    public static final int GL_VERTEX_STREAM0_ATI = 34669;
    public static final int GL_VERTEX_STREAM1_ATI = 34670;
    public static final int GL_VERTEX_STREAM2_ATI = 34671;
    public static final int GL_VERTEX_STREAM3_ATI = 34672;
    public static final int GL_VERTEX_STREAM4_ATI = 34673;
    public static final int GL_VERTEX_STREAM5_ATI = 34674;
    public static final int GL_VERTEX_STREAM6_ATI = 34675;
    public static final int GL_VERTEX_STREAM7_ATI = 34676;
    
    private ATIVertexStreams() {
    }
    
    public static void glVertexStream2fATI(final int stream, final float x, final float y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexStream2fATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexStream2fATI(stream, x, y, function_pointer);
    }
    
    static native void nglVertexStream2fATI(final int p0, final float p1, final float p2, final long p3);
    
    public static void glVertexStream2dATI(final int stream, final double x, final double y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexStream2dATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexStream2dATI(stream, x, y, function_pointer);
    }
    
    static native void nglVertexStream2dATI(final int p0, final double p1, final double p2, final long p3);
    
    public static void glVertexStream2iATI(final int stream, final int x, final int y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexStream2iATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexStream2iATI(stream, x, y, function_pointer);
    }
    
    static native void nglVertexStream2iATI(final int p0, final int p1, final int p2, final long p3);
    
    public static void glVertexStream2sATI(final int stream, final short x, final short y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexStream2sATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexStream2sATI(stream, x, y, function_pointer);
    }
    
    static native void nglVertexStream2sATI(final int p0, final short p1, final short p2, final long p3);
    
    public static void glVertexStream3fATI(final int stream, final float x, final float y, final float z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexStream3fATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexStream3fATI(stream, x, y, z, function_pointer);
    }
    
    static native void nglVertexStream3fATI(final int p0, final float p1, final float p2, final float p3, final long p4);
    
    public static void glVertexStream3dATI(final int stream, final double x, final double y, final double z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexStream3dATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexStream3dATI(stream, x, y, z, function_pointer);
    }
    
    static native void nglVertexStream3dATI(final int p0, final double p1, final double p2, final double p3, final long p4);
    
    public static void glVertexStream3iATI(final int stream, final int x, final int y, final int z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexStream3iATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexStream3iATI(stream, x, y, z, function_pointer);
    }
    
    static native void nglVertexStream3iATI(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glVertexStream3sATI(final int stream, final short x, final short y, final short z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexStream3sATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexStream3sATI(stream, x, y, z, function_pointer);
    }
    
    static native void nglVertexStream3sATI(final int p0, final short p1, final short p2, final short p3, final long p4);
    
    public static void glVertexStream4fATI(final int stream, final float x, final float y, final float z, final float w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexStream4fATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexStream4fATI(stream, x, y, z, w, function_pointer);
    }
    
    static native void nglVertexStream4fATI(final int p0, final float p1, final float p2, final float p3, final float p4, final long p5);
    
    public static void glVertexStream4dATI(final int stream, final double x, final double y, final double z, final double w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexStream4dATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexStream4dATI(stream, x, y, z, w, function_pointer);
    }
    
    static native void nglVertexStream4dATI(final int p0, final double p1, final double p2, final double p3, final double p4, final long p5);
    
    public static void glVertexStream4iATI(final int stream, final int x, final int y, final int z, final int w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexStream4iATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexStream4iATI(stream, x, y, z, w, function_pointer);
    }
    
    static native void nglVertexStream4iATI(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glVertexStream4sATI(final int stream, final short x, final short y, final short z, final short w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexStream4sATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexStream4sATI(stream, x, y, z, w, function_pointer);
    }
    
    static native void nglVertexStream4sATI(final int p0, final short p1, final short p2, final short p3, final short p4, final long p5);
    
    public static void glNormalStream3bATI(final int stream, final byte x, final byte y, final byte z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNormalStream3bATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNormalStream3bATI(stream, x, y, z, function_pointer);
    }
    
    static native void nglNormalStream3bATI(final int p0, final byte p1, final byte p2, final byte p3, final long p4);
    
    public static void glNormalStream3fATI(final int stream, final float x, final float y, final float z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNormalStream3fATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNormalStream3fATI(stream, x, y, z, function_pointer);
    }
    
    static native void nglNormalStream3fATI(final int p0, final float p1, final float p2, final float p3, final long p4);
    
    public static void glNormalStream3dATI(final int stream, final double x, final double y, final double z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNormalStream3dATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNormalStream3dATI(stream, x, y, z, function_pointer);
    }
    
    static native void nglNormalStream3dATI(final int p0, final double p1, final double p2, final double p3, final long p4);
    
    public static void glNormalStream3iATI(final int stream, final int x, final int y, final int z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNormalStream3iATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNormalStream3iATI(stream, x, y, z, function_pointer);
    }
    
    static native void nglNormalStream3iATI(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glNormalStream3sATI(final int stream, final short x, final short y, final short z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNormalStream3sATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNormalStream3sATI(stream, x, y, z, function_pointer);
    }
    
    static native void nglNormalStream3sATI(final int p0, final short p1, final short p2, final short p3, final long p4);
    
    public static void glClientActiveVertexStreamATI(final int stream) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glClientActiveVertexStreamATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglClientActiveVertexStreamATI(stream, function_pointer);
    }
    
    static native void nglClientActiveVertexStreamATI(final int p0, final long p1);
    
    public static void glVertexBlendEnvfATI(final int pname, final float param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexBlendEnvfATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexBlendEnvfATI(pname, param, function_pointer);
    }
    
    static native void nglVertexBlendEnvfATI(final int p0, final float p1, final long p2);
    
    public static void glVertexBlendEnviATI(final int pname, final int param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexBlendEnviATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexBlendEnviATI(pname, param, function_pointer);
    }
    
    static native void nglVertexBlendEnviATI(final int p0, final int p1, final long p2);
}

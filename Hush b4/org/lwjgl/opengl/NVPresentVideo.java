// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.LongBuffer;
import org.lwjgl.MemoryUtil;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;

public final class NVPresentVideo
{
    public static final int GL_FRAME_NV = 36390;
    public static final int FIELDS_NV = 36391;
    public static final int GL_CURRENT_TIME_NV = 36392;
    public static final int GL_NUM_FILL_STREAMS_NV = 36393;
    public static final int GL_PRESENT_TIME_NV = 36394;
    public static final int GL_PRESENT_DURATION_NV = 36395;
    public static final int GL_NUM_VIDEO_SLOTS_NV = 8432;
    
    private NVPresentVideo() {
    }
    
    public static void glPresentFrameKeyedNV(final int video_slot, final long minPresentTime, final int beginPresentTimeId, final int presentDurationId, final int type, final int target0, final int fill0, final int key0, final int target1, final int fill1, final int key1) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPresentFrameKeyedNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglPresentFrameKeyedNV(video_slot, minPresentTime, beginPresentTimeId, presentDurationId, type, target0, fill0, key0, target1, fill1, key1, function_pointer);
    }
    
    static native void nglPresentFrameKeyedNV(final int p0, final long p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final int p10, final long p11);
    
    public static void glPresentFrameDualFillNV(final int video_slot, final long minPresentTime, final int beginPresentTimeId, final int presentDurationId, final int type, final int target0, final int fill0, final int target1, final int fill1, final int target2, final int fill2, final int target3, final int fill3) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPresentFrameDualFillNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglPresentFrameDualFillNV(video_slot, minPresentTime, beginPresentTimeId, presentDurationId, type, target0, fill0, target1, fill1, target2, fill2, target3, fill3, function_pointer);
    }
    
    static native void nglPresentFrameDualFillNV(final int p0, final long p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final int p10, final int p11, final int p12, final long p13);
    
    public static void glGetVideoNV(final int video_slot, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVideoivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetVideoivNV(video_slot, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetVideoivNV(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetVideoiNV(final int video_slot, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVideoivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetVideoivNV(video_slot, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetVideouNV(final int video_slot, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVideouivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetVideouivNV(video_slot, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetVideouivNV(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetVideouiNV(final int video_slot, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVideouivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetVideouivNV(video_slot, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetVideoNV(final int video_slot, final int pname, final LongBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVideoi64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetVideoi64vNV(video_slot, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetVideoi64vNV(final int p0, final int p1, final long p2, final long p3);
    
    public static long glGetVideoi64NV(final int video_slot, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVideoi64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final LongBuffer params = APIUtil.getBufferLong(caps);
        nglGetVideoi64vNV(video_slot, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetVideouNV(final int video_slot, final int pname, final LongBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVideoui64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        nglGetVideoui64vNV(video_slot, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetVideoui64vNV(final int p0, final int p1, final long p2, final long p3);
    
    public static long glGetVideoui64NV(final int video_slot, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVideoui64vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final LongBuffer params = APIUtil.getBufferLong(caps);
        nglGetVideoui64vNV(video_slot, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
}

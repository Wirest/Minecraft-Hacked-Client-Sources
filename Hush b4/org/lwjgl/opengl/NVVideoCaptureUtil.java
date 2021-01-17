// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import java.nio.LongBuffer;
import java.nio.ByteBuffer;
import org.lwjgl.LWJGLUtil;

public final class NVVideoCaptureUtil
{
    private NVVideoCaptureUtil() {
    }
    
    private static void checkExtension() {
        if (LWJGLUtil.CHECKS && !GLContext.getCapabilities().GL_NV_video_capture) {
            throw new IllegalStateException("NV_video_capture is not supported");
        }
    }
    
    private static ByteBuffer getPeerInfo() {
        return ContextGL.getCurrentContext().getPeerInfo().getHandle();
    }
    
    public static boolean glBindVideoCaptureDeviceNV(final int video_slot, final long device) {
        checkExtension();
        return nglBindVideoCaptureDeviceNV(getPeerInfo(), video_slot, device);
    }
    
    private static native boolean nglBindVideoCaptureDeviceNV(final ByteBuffer p0, final int p1, final long p2);
    
    public static int glEnumerateVideoCaptureDevicesNV(final LongBuffer devices) {
        checkExtension();
        if (devices != null) {
            BufferChecks.checkBuffer(devices, 1);
        }
        return nglEnumerateVideoCaptureDevicesNV(getPeerInfo(), devices, (devices == null) ? 0 : devices.position());
    }
    
    private static native int nglEnumerateVideoCaptureDevicesNV(final ByteBuffer p0, final LongBuffer p1, final int p2);
    
    public static boolean glLockVideoCaptureDeviceNV(final long device) {
        checkExtension();
        return nglLockVideoCaptureDeviceNV(getPeerInfo(), device);
    }
    
    private static native boolean nglLockVideoCaptureDeviceNV(final ByteBuffer p0, final long p1);
    
    public static boolean glQueryVideoCaptureDeviceNV(final long device, final int attribute, final IntBuffer value) {
        checkExtension();
        BufferChecks.checkBuffer(value, 1);
        return nglQueryVideoCaptureDeviceNV(getPeerInfo(), device, attribute, value, value.position());
    }
    
    private static native boolean nglQueryVideoCaptureDeviceNV(final ByteBuffer p0, final long p1, final int p2, final IntBuffer p3, final int p4);
    
    public static boolean glReleaseVideoCaptureDeviceNV(final long device) {
        checkExtension();
        return nglReleaseVideoCaptureDeviceNV(getPeerInfo(), device);
    }
    
    private static native boolean nglReleaseVideoCaptureDeviceNV(final ByteBuffer p0, final long p1);
}

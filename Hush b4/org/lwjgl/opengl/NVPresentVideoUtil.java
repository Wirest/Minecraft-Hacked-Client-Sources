// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import java.nio.LongBuffer;
import java.nio.ByteBuffer;
import org.lwjgl.LWJGLUtil;

public final class NVPresentVideoUtil
{
    private NVPresentVideoUtil() {
    }
    
    private static void checkExtension() {
        if (LWJGLUtil.CHECKS && !GLContext.getCapabilities().GL_NV_present_video) {
            throw new IllegalStateException("NV_present_video is not supported");
        }
    }
    
    private static ByteBuffer getPeerInfo() {
        return ContextGL.getCurrentContext().getPeerInfo().getHandle();
    }
    
    public static int glEnumerateVideoDevicesNV(final LongBuffer devices) {
        checkExtension();
        if (devices != null) {
            BufferChecks.checkBuffer(devices, 1);
        }
        return nglEnumerateVideoDevicesNV(getPeerInfo(), devices, (devices == null) ? 0 : devices.position());
    }
    
    private static native int nglEnumerateVideoDevicesNV(final ByteBuffer p0, final LongBuffer p1, final int p2);
    
    public static boolean glBindVideoDeviceNV(final int video_slot, final long video_device, final IntBuffer attrib_list) {
        checkExtension();
        if (attrib_list != null) {
            BufferChecks.checkNullTerminated(attrib_list);
        }
        return nglBindVideoDeviceNV(getPeerInfo(), video_slot, video_device, attrib_list, (attrib_list == null) ? 0 : attrib_list.position());
    }
    
    private static native boolean nglBindVideoDeviceNV(final ByteBuffer p0, final int p1, final long p2, final IntBuffer p3, final int p4);
    
    public static boolean glQueryContextNV(final int attrib, final IntBuffer value) {
        checkExtension();
        BufferChecks.checkBuffer(value, 1);
        final ContextGL ctx = ContextGL.getCurrentContext();
        return nglQueryContextNV(ctx.getPeerInfo().getHandle(), ctx.getHandle(), attrib, value, value.position());
    }
    
    private static native boolean nglQueryContextNV(final ByteBuffer p0, final ByteBuffer p1, final int p2, final IntBuffer p3, final int p4);
}

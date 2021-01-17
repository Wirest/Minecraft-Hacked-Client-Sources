// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import java.awt.Canvas;

final class LinuxAWTGLCanvasPeerInfo extends LinuxPeerInfo
{
    private final Canvas component;
    private final AWTSurfaceLock awt_surface;
    private int screen;
    
    LinuxAWTGLCanvasPeerInfo(final Canvas component) {
        this.awt_surface = new AWTSurfaceLock();
        this.screen = -1;
        this.component = component;
    }
    
    @Override
    protected void doLockAndInitHandle() throws LWJGLException {
        final ByteBuffer surface_handle = this.awt_surface.lockAndGetHandle(this.component);
        if (this.screen == -1) {
            try {
                this.screen = getScreenFromSurfaceInfo(surface_handle);
            }
            catch (LWJGLException e) {
                LWJGLUtil.log("Got exception while trying to determine screen: " + e);
                this.screen = 0;
            }
        }
        nInitHandle(this.screen, surface_handle, this.getHandle());
    }
    
    private static native int getScreenFromSurfaceInfo(final ByteBuffer p0) throws LWJGLException;
    
    private static native void nInitHandle(final int p0, final ByteBuffer p1, final ByteBuffer p2) throws LWJGLException;
    
    @Override
    protected void doUnlock() throws LWJGLException {
        this.awt_surface.unlock();
    }
}

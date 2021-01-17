// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.LWJGLException;
import java.nio.IntBuffer;
import java.awt.Canvas;

final class WindowsAWTGLCanvasPeerInfo extends WindowsPeerInfo
{
    private final Canvas component;
    private final AWTSurfaceLock awt_surface;
    private final PixelFormat pixel_format;
    private boolean has_pixel_format;
    
    WindowsAWTGLCanvasPeerInfo(final Canvas component, final PixelFormat pixel_format) {
        this.awt_surface = new AWTSurfaceLock();
        this.component = component;
        this.pixel_format = pixel_format;
    }
    
    @Override
    protected void doLockAndInitHandle() throws LWJGLException {
        nInitHandle(this.awt_surface.lockAndGetHandle(this.component), this.getHandle());
        if (!this.has_pixel_format && this.pixel_format != null) {
            final int format = WindowsPeerInfo.choosePixelFormat(this.getHdc(), this.component.getX(), this.component.getY(), this.pixel_format, null, true, true, false, true);
            WindowsPeerInfo.setPixelFormat(this.getHdc(), format);
            this.has_pixel_format = true;
        }
    }
    
    private static native void nInitHandle(final ByteBuffer p0, final ByteBuffer p1) throws LWJGLException;
    
    @Override
    protected void doUnlock() throws LWJGLException {
        this.awt_surface.unlock();
    }
}

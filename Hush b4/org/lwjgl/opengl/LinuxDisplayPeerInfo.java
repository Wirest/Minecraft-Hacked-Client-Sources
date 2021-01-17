// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengles.GLContext;

final class LinuxDisplayPeerInfo extends LinuxPeerInfo
{
    final boolean egl;
    
    LinuxDisplayPeerInfo() throws LWJGLException {
        this.egl = true;
        GLContext.loadOpenGLLibrary();
    }
    
    LinuxDisplayPeerInfo(final PixelFormat pixel_format) throws LWJGLException {
        this.egl = false;
        LinuxDisplay.lockAWT();
        try {
            org.lwjgl.opengl.GLContext.loadOpenGLLibrary();
            try {
                LinuxDisplay.incDisplay();
                try {
                    initDefaultPeerInfo(LinuxDisplay.getDisplay(), LinuxDisplay.getDefaultScreen(), this.getHandle(), pixel_format);
                }
                catch (LWJGLException e) {
                    LinuxDisplay.decDisplay();
                    throw e;
                }
            }
            catch (LWJGLException e) {
                org.lwjgl.opengl.GLContext.unloadOpenGLLibrary();
                throw e;
            }
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }
    
    private static native void initDefaultPeerInfo(final long p0, final int p1, final ByteBuffer p2, final PixelFormat p3) throws LWJGLException;
    
    @Override
    protected void doLockAndInitHandle() throws LWJGLException {
        LinuxDisplay.lockAWT();
        try {
            initDrawable(LinuxDisplay.getWindow(), this.getHandle());
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }
    
    private static native void initDrawable(final long p0, final ByteBuffer p1);
    
    @Override
    protected void doUnlock() throws LWJGLException {
    }
    
    @Override
    public void destroy() {
        super.destroy();
        if (this.egl) {
            GLContext.unloadOpenGLLibrary();
        }
        else {
            LinuxDisplay.lockAWT();
            LinuxDisplay.decDisplay();
            org.lwjgl.opengl.GLContext.unloadOpenGLLibrary();
            LinuxDisplay.unlockAWT();
        }
    }
}

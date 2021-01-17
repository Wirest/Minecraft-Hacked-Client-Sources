// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.LWJGLException;

final class LinuxPbufferPeerInfo extends LinuxPeerInfo
{
    LinuxPbufferPeerInfo(final int width, final int height, final PixelFormat pixel_format) throws LWJGLException {
        LinuxDisplay.lockAWT();
        try {
            GLContext.loadOpenGLLibrary();
            try {
                LinuxDisplay.incDisplay();
                try {
                    nInitHandle(LinuxDisplay.getDisplay(), LinuxDisplay.getDefaultScreen(), this.getHandle(), width, height, pixel_format);
                }
                catch (LWJGLException e) {
                    LinuxDisplay.decDisplay();
                    throw e;
                }
            }
            catch (LWJGLException e) {
                GLContext.unloadOpenGLLibrary();
                throw e;
            }
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }
    
    private static native void nInitHandle(final long p0, final int p1, final ByteBuffer p2, final int p3, final int p4, final PixelFormat p5) throws LWJGLException;
    
    @Override
    public void destroy() {
        LinuxDisplay.lockAWT();
        nDestroy(this.getHandle());
        LinuxDisplay.decDisplay();
        GLContext.unloadOpenGLLibrary();
        LinuxDisplay.unlockAWT();
    }
    
    private static native void nDestroy(final ByteBuffer p0);
    
    @Override
    protected void doLockAndInitHandle() throws LWJGLException {
    }
    
    @Override
    protected void doUnlock() throws LWJGLException {
    }
}

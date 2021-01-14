package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;

import java.nio.ByteBuffer;

final class WindowsDisplayPeerInfo
        extends WindowsPeerInfo {
    final boolean egl;

    WindowsDisplayPeerInfo(boolean paramBoolean)
            throws LWJGLException {
        this.egl = paramBoolean;
        if (paramBoolean) {
            org.lwjgl.opengles.GLContext.loadOpenGLLibrary();
        } else {
            GLContext.loadOpenGLLibrary();
        }
    }

    private static native void nInitDC(ByteBuffer paramByteBuffer, long paramLong1, long paramLong2);

    void initDC(long paramLong1, long paramLong2)
            throws LWJGLException {
        nInitDC(getHandle(), paramLong1, paramLong2);
    }

    protected void doLockAndInitHandle()
            throws LWJGLException {
    }

    protected void doUnlock()
            throws LWJGLException {
    }

    public void destroy() {
        super.destroy();
        if (this.egl) {
            org.lwjgl.opengles.GLContext.unloadOpenGLLibrary();
        } else {
            GLContext.unloadOpenGLLibrary();
        }
    }
}





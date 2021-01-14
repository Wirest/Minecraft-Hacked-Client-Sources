package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

final class WindowsContextImplementation
        implements ContextImplementation {
    private static native ByteBuffer nCreate(ByteBuffer paramByteBuffer1, IntBuffer paramIntBuffer, ByteBuffer paramByteBuffer2)
            throws LWJGLException;

    private static native void nSwapBuffers(ByteBuffer paramByteBuffer)
            throws LWJGLException;

    private static native void nReleaseCurrentContext()
            throws LWJGLException;

    private static native void nMakeCurrent(ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2)
            throws LWJGLException;

    private static native boolean nIsCurrent(ByteBuffer paramByteBuffer)
            throws LWJGLException;

    private static native boolean nSetSwapInterval(int paramInt);

    private static native void nDestroy(ByteBuffer paramByteBuffer)
            throws LWJGLException;

    public ByteBuffer create(PeerInfo paramPeerInfo, IntBuffer paramIntBuffer, ByteBuffer paramByteBuffer)
            throws LWJGLException {
        ByteBuffer localByteBuffer1 = paramPeerInfo.lockAndGetHandle();
        try {
            ByteBuffer localByteBuffer2 = nCreate(localByteBuffer1, paramIntBuffer, paramByteBuffer);
            return localByteBuffer2;
        } finally {
            paramPeerInfo.unlock();
        }
    }

    native long getHGLRC(ByteBuffer paramByteBuffer);

    native long getHDC(ByteBuffer paramByteBuffer);

    public void swapBuffers()
            throws LWJGLException {
        ContextGL localContextGL = ContextGL.getCurrentContext();
        if (localContextGL == null) {
            throw new IllegalStateException("No context is current");
        }
        synchronized (localContextGL) {
            PeerInfo localPeerInfo = localContextGL.getPeerInfo();
            ByteBuffer localByteBuffer = localPeerInfo.lockAndGetHandle();
            try {
                nSwapBuffers(localByteBuffer);
            } finally {
                localPeerInfo.unlock();
            }
        }
    }

    public void releaseDrawable(ByteBuffer paramByteBuffer)
            throws LWJGLException {
    }

    public void update(ByteBuffer paramByteBuffer) {
    }

    public void releaseCurrentContext()
            throws LWJGLException {
    }

    public void makeCurrent(PeerInfo paramPeerInfo, ByteBuffer paramByteBuffer)
            throws LWJGLException {
        ByteBuffer localByteBuffer = paramPeerInfo.lockAndGetHandle();
        try {
            nMakeCurrent(localByteBuffer, paramByteBuffer);
        } finally {
            paramPeerInfo.unlock();
        }
    }

    public boolean isCurrent(ByteBuffer paramByteBuffer)
            throws LWJGLException {
        boolean bool = nIsCurrent(paramByteBuffer);
        return bool;
    }

    public void setSwapInterval(int paramInt) {
        boolean bool = nSetSwapInterval(paramInt);
        if (!bool) {
            LWJGLUtil.log("Failed to set swap interval");
        }
        Util.checkGLError();
    }

    public void destroy(PeerInfo paramPeerInfo, ByteBuffer paramByteBuffer)
            throws LWJGLException {
        nDestroy(paramByteBuffer);
    }
}





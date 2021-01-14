package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

abstract interface ContextImplementation {
    public abstract ByteBuffer create(PeerInfo paramPeerInfo, IntBuffer paramIntBuffer, ByteBuffer paramByteBuffer)
            throws LWJGLException;

    public abstract void swapBuffers()
            throws LWJGLException;

    public abstract void releaseDrawable(ByteBuffer paramByteBuffer)
            throws LWJGLException;

    public abstract void releaseCurrentContext()
            throws LWJGLException;

    public abstract void update(ByteBuffer paramByteBuffer);

    public abstract void makeCurrent(PeerInfo paramPeerInfo, ByteBuffer paramByteBuffer)
            throws LWJGLException;

    public abstract boolean isCurrent(ByteBuffer paramByteBuffer)
            throws LWJGLException;

    public abstract void setSwapInterval(int paramInt);

    public abstract void destroy(PeerInfo paramPeerInfo, ByteBuffer paramByteBuffer)
            throws LWJGLException;
}





package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

abstract interface DisplayImplementation
        extends InputImplementation {
    public abstract void createWindow(DrawableLWJGL paramDrawableLWJGL, DisplayMode paramDisplayMode, Canvas paramCanvas, int paramInt1, int paramInt2)
            throws LWJGLException;

    public abstract void destroyWindow();

    public abstract void switchDisplayMode(DisplayMode paramDisplayMode)
            throws LWJGLException;

    public abstract void resetDisplayMode();

    public abstract int getGammaRampLength();

    public abstract void setGammaRamp(FloatBuffer paramFloatBuffer)
            throws LWJGLException;

    public abstract String getAdapter();

    public abstract String getVersion();

    public abstract DisplayMode init()
            throws LWJGLException;

    public abstract void setTitle(String paramString);

    public abstract boolean isCloseRequested();

    public abstract boolean isVisible();

    public abstract boolean isActive();

    public abstract boolean isDirty();

    public abstract PeerInfo createPeerInfo(PixelFormat paramPixelFormat, ContextAttribs paramContextAttribs)
            throws LWJGLException;

    public abstract void update();

    public abstract void reshape(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    public abstract DisplayMode[] getAvailableDisplayModes()
            throws LWJGLException;

    public abstract int getPbufferCapabilities();

    public abstract boolean isBufferLost(PeerInfo paramPeerInfo);

    public abstract PeerInfo createPbuffer(int paramInt1, int paramInt2, PixelFormat paramPixelFormat, ContextAttribs paramContextAttribs, IntBuffer paramIntBuffer1, IntBuffer paramIntBuffer2)
            throws LWJGLException;

    public abstract void setPbufferAttrib(PeerInfo paramPeerInfo, int paramInt1, int paramInt2);

    public abstract void bindTexImageToPbuffer(PeerInfo paramPeerInfo, int paramInt);

    public abstract void releaseTexImageFromPbuffer(PeerInfo paramPeerInfo, int paramInt);

    public abstract int setIcon(ByteBuffer[] paramArrayOfByteBuffer);

    public abstract void setResizable(boolean paramBoolean);

    public abstract boolean wasResized();

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract int getX();

    public abstract int getY();

    public abstract float getPixelScaleFactor();
}





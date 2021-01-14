package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public abstract interface InputImplementation {
    public abstract boolean hasWheel();

    public abstract int getButtonCount();

    public abstract void createMouse()
            throws LWJGLException;

    public abstract void destroyMouse();

    public abstract void pollMouse(IntBuffer paramIntBuffer, ByteBuffer paramByteBuffer);

    public abstract void readMouse(ByteBuffer paramByteBuffer);

    public abstract void grabMouse(boolean paramBoolean);

    public abstract int getNativeCursorCapabilities();

    public abstract void setCursorPosition(int paramInt1, int paramInt2);

    public abstract void setNativeCursor(Object paramObject)
            throws LWJGLException;

    public abstract int getMinCursorSize();

    public abstract int getMaxCursorSize();

    public abstract void createKeyboard()
            throws LWJGLException;

    public abstract void destroyKeyboard();

    public abstract void pollKeyboard(ByteBuffer paramByteBuffer);

    public abstract void readKeyboard(ByteBuffer paramByteBuffer);

    public abstract Object createCursor(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, IntBuffer paramIntBuffer1, IntBuffer paramIntBuffer2)
            throws LWJGLException;

    public abstract void destroyCursor(Object paramObject);

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract boolean isInsideWindow();
}





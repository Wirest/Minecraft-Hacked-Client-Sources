package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

abstract class WindowsPeerInfo
        extends PeerInfo {
    protected WindowsPeerInfo() {
        super(createHandle());
    }

    private static native ByteBuffer createHandle();

    protected static int choosePixelFormat(long paramLong, int paramInt1, int paramInt2, PixelFormat paramPixelFormat, IntBuffer paramIntBuffer, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
            throws LWJGLException {
        return nChoosePixelFormat(paramLong, paramInt1, paramInt2, paramPixelFormat, paramIntBuffer, paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4);
    }

    private static native int nChoosePixelFormat(long paramLong, int paramInt1, int paramInt2, PixelFormat paramPixelFormat, IntBuffer paramIntBuffer, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
            throws LWJGLException;

    protected static native void setPixelFormat(long paramLong, int paramInt)
            throws LWJGLException;

    private static native long nGetHdc(ByteBuffer paramByteBuffer);

    private static native long nGetHwnd(ByteBuffer paramByteBuffer);

    public final long getHdc() {
        return nGetHdc(getHandle());
    }

    public final long getHwnd() {
        return nGetHwnd(getHandle());
    }
}





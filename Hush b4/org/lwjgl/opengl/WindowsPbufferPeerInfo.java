// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.LWJGLException;
import java.nio.IntBuffer;

final class WindowsPbufferPeerInfo extends WindowsPeerInfo
{
    WindowsPbufferPeerInfo(final int width, final int height, final PixelFormat pixel_format, final IntBuffer pixelFormatCaps, final IntBuffer pBufferAttribs) throws LWJGLException {
        nCreate(this.getHandle(), width, height, pixel_format, pixelFormatCaps, pBufferAttribs);
    }
    
    private static native void nCreate(final ByteBuffer p0, final int p1, final int p2, final PixelFormat p3, final IntBuffer p4, final IntBuffer p5) throws LWJGLException;
    
    public boolean isBufferLost() {
        return nIsBufferLost(this.getHandle());
    }
    
    private static native boolean nIsBufferLost(final ByteBuffer p0);
    
    public void setPbufferAttrib(final int attrib, final int value) {
        nSetPbufferAttrib(this.getHandle(), attrib, value);
    }
    
    private static native void nSetPbufferAttrib(final ByteBuffer p0, final int p1, final int p2);
    
    public void bindTexImageToPbuffer(final int buffer) {
        nBindTexImageToPbuffer(this.getHandle(), buffer);
    }
    
    private static native void nBindTexImageToPbuffer(final ByteBuffer p0, final int p1);
    
    public void releaseTexImageFromPbuffer(final int buffer) {
        nReleaseTexImageFromPbuffer(this.getHandle(), buffer);
    }
    
    private static native void nReleaseTexImageFromPbuffer(final ByteBuffer p0, final int p1);
    
    @Override
    public void destroy() {
        nDestroy(this.getHandle());
    }
    
    private static native void nDestroy(final ByteBuffer p0);
    
    @Override
    protected void doLockAndInitHandle() throws LWJGLException {
    }
    
    @Override
    protected void doUnlock() throws LWJGLException {
    }
}

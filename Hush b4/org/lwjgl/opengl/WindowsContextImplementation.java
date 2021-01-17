// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.LWJGLUtil;
import org.lwjgl.LWJGLException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

final class WindowsContextImplementation implements ContextImplementation
{
    public ByteBuffer create(final PeerInfo peer_info, final IntBuffer attribs, final ByteBuffer shared_context_handle) throws LWJGLException {
        final ByteBuffer peer_handle = peer_info.lockAndGetHandle();
        try {
            return nCreate(peer_handle, attribs, shared_context_handle);
        }
        finally {
            peer_info.unlock();
        }
    }
    
    private static native ByteBuffer nCreate(final ByteBuffer p0, final IntBuffer p1, final ByteBuffer p2) throws LWJGLException;
    
    native long getHGLRC(final ByteBuffer p0);
    
    native long getHDC(final ByteBuffer p0);
    
    public void swapBuffers() throws LWJGLException {
        final ContextGL current_context = ContextGL.getCurrentContext();
        if (current_context == null) {
            throw new IllegalStateException("No context is current");
        }
        synchronized (current_context) {
            final PeerInfo current_peer_info = current_context.getPeerInfo();
            final ByteBuffer peer_handle = current_peer_info.lockAndGetHandle();
            try {
                nSwapBuffers(peer_handle);
            }
            finally {
                current_peer_info.unlock();
            }
        }
    }
    
    private static native void nSwapBuffers(final ByteBuffer p0) throws LWJGLException;
    
    public void releaseDrawable(final ByteBuffer context_handle) throws LWJGLException {
    }
    
    public void update(final ByteBuffer context_handle) {
    }
    
    public void releaseCurrentContext() throws LWJGLException {
        nReleaseCurrentContext();
    }
    
    private static native void nReleaseCurrentContext() throws LWJGLException;
    
    public void makeCurrent(final PeerInfo peer_info, final ByteBuffer handle) throws LWJGLException {
        final ByteBuffer peer_handle = peer_info.lockAndGetHandle();
        try {
            nMakeCurrent(peer_handle, handle);
        }
        finally {
            peer_info.unlock();
        }
    }
    
    private static native void nMakeCurrent(final ByteBuffer p0, final ByteBuffer p1) throws LWJGLException;
    
    public boolean isCurrent(final ByteBuffer handle) throws LWJGLException {
        final boolean result = nIsCurrent(handle);
        return result;
    }
    
    private static native boolean nIsCurrent(final ByteBuffer p0) throws LWJGLException;
    
    public void setSwapInterval(final int value) {
        final boolean success = nSetSwapInterval(value);
        if (!success) {
            LWJGLUtil.log("Failed to set swap interval");
        }
        Util.checkGLError();
    }
    
    private static native boolean nSetSwapInterval(final int p0);
    
    public void destroy(final PeerInfo peer_info, final ByteBuffer handle) throws LWJGLException {
        nDestroy(handle);
    }
    
    private static native void nDestroy(final ByteBuffer p0) throws LWJGLException;
}

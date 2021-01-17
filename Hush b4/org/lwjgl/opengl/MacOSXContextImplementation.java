// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

final class MacOSXContextImplementation implements ContextImplementation
{
    public ByteBuffer create(final PeerInfo peer_info, final IntBuffer attribs, final ByteBuffer shared_context_handle) throws LWJGLException {
        final ByteBuffer peer_handle = peer_info.lockAndGetHandle();
        try {
            return nCreate(peer_handle, shared_context_handle);
        }
        finally {
            peer_info.unlock();
        }
    }
    
    private static native ByteBuffer nCreate(final ByteBuffer p0, final ByteBuffer p1) throws LWJGLException;
    
    public void swapBuffers() throws LWJGLException {
        final ContextGL current_context = ContextGL.getCurrentContext();
        if (current_context == null) {
            throw new IllegalStateException("No context is current");
        }
        synchronized (current_context) {
            nSwapBuffers(current_context.getHandle());
        }
    }
    
    native long getCGLShareGroup(final ByteBuffer p0);
    
    private static native void nSwapBuffers(final ByteBuffer p0) throws LWJGLException;
    
    public void update(final ByteBuffer context_handle) {
        nUpdate(context_handle);
    }
    
    private static native void nUpdate(final ByteBuffer p0);
    
    public void releaseCurrentContext() throws LWJGLException {
        nReleaseCurrentContext();
    }
    
    private static native void nReleaseCurrentContext() throws LWJGLException;
    
    public void releaseDrawable(final ByteBuffer context_handle) throws LWJGLException {
        clearDrawable(context_handle);
    }
    
    private static native void clearDrawable(final ByteBuffer p0) throws LWJGLException;
    
    static void resetView(final PeerInfo peer_info, final ContextGL context) throws LWJGLException {
        final ByteBuffer peer_handle = peer_info.lockAndGetHandle();
        try {
            synchronized (context) {
                clearDrawable(context.getHandle());
                setView(peer_handle, context.getHandle());
            }
        }
        finally {
            peer_info.unlock();
        }
    }
    
    public void makeCurrent(final PeerInfo peer_info, final ByteBuffer handle) throws LWJGLException {
        final ByteBuffer peer_handle = peer_info.lockAndGetHandle();
        try {
            setView(peer_handle, handle);
            nMakeCurrent(handle);
        }
        finally {
            peer_info.unlock();
        }
    }
    
    private static native void setView(final ByteBuffer p0, final ByteBuffer p1) throws LWJGLException;
    
    private static native void nMakeCurrent(final ByteBuffer p0) throws LWJGLException;
    
    public boolean isCurrent(final ByteBuffer handle) throws LWJGLException {
        final boolean result = nIsCurrent(handle);
        return result;
    }
    
    private static native boolean nIsCurrent(final ByteBuffer p0) throws LWJGLException;
    
    public void setSwapInterval(final int value) {
        final ContextGL current_context = ContextGL.getCurrentContext();
        synchronized (current_context) {
            nSetSwapInterval(current_context.getHandle(), value);
        }
    }
    
    private static native void nSetSwapInterval(final ByteBuffer p0, final int p1);
    
    public void destroy(final PeerInfo peer_info, final ByteBuffer handle) throws LWJGLException {
        nDestroy(handle);
    }
    
    private static native void nDestroy(final ByteBuffer p0) throws LWJGLException;
}

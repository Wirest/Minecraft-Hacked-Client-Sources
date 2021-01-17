// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

final class LinuxContextImplementation implements ContextImplementation
{
    public ByteBuffer create(final PeerInfo peer_info, final IntBuffer attribs, final ByteBuffer shared_context_handle) throws LWJGLException {
        LinuxDisplay.lockAWT();
        try {
            final ByteBuffer peer_handle = peer_info.lockAndGetHandle();
            try {
                return nCreate(peer_handle, attribs, shared_context_handle);
            }
            finally {
                peer_info.unlock();
            }
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }
    
    private static native ByteBuffer nCreate(final ByteBuffer p0, final IntBuffer p1, final ByteBuffer p2) throws LWJGLException;
    
    native long getGLXContext(final ByteBuffer p0);
    
    native long getDisplay(final ByteBuffer p0);
    
    public void releaseDrawable(final ByteBuffer context_handle) throws LWJGLException {
    }
    
    public void swapBuffers() throws LWJGLException {
        final ContextGL current_context = ContextGL.getCurrentContext();
        if (current_context == null) {
            throw new IllegalStateException("No context is current");
        }
        synchronized (current_context) {
            final PeerInfo current_peer_info = current_context.getPeerInfo();
            LinuxDisplay.lockAWT();
            try {
                final ByteBuffer peer_handle = current_peer_info.lockAndGetHandle();
                try {
                    nSwapBuffers(peer_handle);
                }
                finally {
                    current_peer_info.unlock();
                }
            }
            finally {
                LinuxDisplay.unlockAWT();
            }
        }
    }
    
    private static native void nSwapBuffers(final ByteBuffer p0) throws LWJGLException;
    
    public void releaseCurrentContext() throws LWJGLException {
        final ContextGL current_context = ContextGL.getCurrentContext();
        if (current_context == null) {
            throw new IllegalStateException("No context is current");
        }
        synchronized (current_context) {
            final PeerInfo current_peer_info = current_context.getPeerInfo();
            LinuxDisplay.lockAWT();
            try {
                final ByteBuffer peer_handle = current_peer_info.lockAndGetHandle();
                try {
                    nReleaseCurrentContext(peer_handle);
                }
                finally {
                    current_peer_info.unlock();
                }
            }
            finally {
                LinuxDisplay.unlockAWT();
            }
        }
    }
    
    private static native void nReleaseCurrentContext(final ByteBuffer p0) throws LWJGLException;
    
    public void update(final ByteBuffer context_handle) {
    }
    
    public void makeCurrent(final PeerInfo peer_info, final ByteBuffer handle) throws LWJGLException {
        LinuxDisplay.lockAWT();
        try {
            final ByteBuffer peer_handle = peer_info.lockAndGetHandle();
            try {
                nMakeCurrent(peer_handle, handle);
            }
            finally {
                peer_info.unlock();
            }
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }
    
    private static native void nMakeCurrent(final ByteBuffer p0, final ByteBuffer p1) throws LWJGLException;
    
    public boolean isCurrent(final ByteBuffer handle) throws LWJGLException {
        LinuxDisplay.lockAWT();
        try {
            final boolean result = nIsCurrent(handle);
            return result;
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }
    
    private static native boolean nIsCurrent(final ByteBuffer p0) throws LWJGLException;
    
    public void setSwapInterval(final int value) {
        final ContextGL current_context = ContextGL.getCurrentContext();
        final PeerInfo peer_info = current_context.getPeerInfo();
        if (current_context == null) {
            throw new IllegalStateException("No context is current");
        }
        synchronized (current_context) {
            LinuxDisplay.lockAWT();
            try {
                final ByteBuffer peer_handle = peer_info.lockAndGetHandle();
                try {
                    nSetSwapInterval(peer_handle, current_context.getHandle(), value);
                }
                finally {
                    peer_info.unlock();
                }
            }
            catch (LWJGLException e) {
                e.printStackTrace();
            }
            finally {
                LinuxDisplay.unlockAWT();
            }
        }
    }
    
    private static native void nSetSwapInterval(final ByteBuffer p0, final ByteBuffer p1, final int p2);
    
    public void destroy(final PeerInfo peer_info, final ByteBuffer handle) throws LWJGLException {
        LinuxDisplay.lockAWT();
        try {
            final ByteBuffer peer_handle = peer_info.lockAndGetHandle();
            try {
                nDestroy(peer_handle, handle);
            }
            finally {
                peer_info.unlock();
            }
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }
    
    private static native void nDestroy(final ByteBuffer p0, final ByteBuffer p1) throws LWJGLException;
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.LWJGLUtil;
import org.lwjgl.LWJGLException;
import java.nio.ByteBuffer;

abstract class PeerInfo
{
    private final ByteBuffer handle;
    private Thread locking_thread;
    private int lock_count;
    
    protected PeerInfo(final ByteBuffer handle) {
        this.handle = handle;
    }
    
    private void lockAndInitHandle() throws LWJGLException {
        this.doLockAndInitHandle();
    }
    
    public final synchronized void unlock() throws LWJGLException {
        if (this.lock_count <= 0) {
            throw new IllegalStateException("PeerInfo not locked!");
        }
        if (Thread.currentThread() != this.locking_thread) {
            throw new IllegalStateException("PeerInfo already locked by " + this.locking_thread);
        }
        --this.lock_count;
        if (this.lock_count == 0) {
            this.doUnlock();
            this.locking_thread = null;
            this.notify();
        }
    }
    
    protected abstract void doLockAndInitHandle() throws LWJGLException;
    
    protected abstract void doUnlock() throws LWJGLException;
    
    public final synchronized ByteBuffer lockAndGetHandle() throws LWJGLException {
        final Thread this_thread = Thread.currentThread();
        while (this.locking_thread != null && this.locking_thread != this_thread) {
            try {
                this.wait();
            }
            catch (InterruptedException e) {
                LWJGLUtil.log("Interrupted while waiting for PeerInfo lock: " + e);
            }
        }
        if (this.lock_count == 0) {
            this.locking_thread = this_thread;
            this.doLockAndInitHandle();
        }
        ++this.lock_count;
        return this.getHandle();
    }
    
    protected final ByteBuffer getHandle() {
        return this.handle;
    }
    
    public void destroy() {
    }
}

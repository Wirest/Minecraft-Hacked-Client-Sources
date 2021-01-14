package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;

import java.nio.ByteBuffer;

abstract class PeerInfo {
    private final ByteBuffer handle;
    private Thread locking_thread;
    private int lock_count;

    protected PeerInfo(ByteBuffer paramByteBuffer) {
        this.handle = paramByteBuffer;
    }

    private void lockAndInitHandle()
            throws LWJGLException {
        doLockAndInitHandle();
    }

    public final synchronized void unlock()
            throws LWJGLException {
        if (this.lock_count <= 0) {
            throw new IllegalStateException("PeerInfo not locked!");
        }
        if (Thread.currentThread() != this.locking_thread) {
            throw new IllegalStateException("PeerInfo already locked by " + this.locking_thread);
        }
        this.lock_count -= 1;
        if (this.lock_count == 0) {
            doUnlock();
            this.locking_thread = null;
            notify();
        }
    }

    protected abstract void doLockAndInitHandle()
            throws LWJGLException;

    protected abstract void doUnlock()
            throws LWJGLException;

    public final synchronized ByteBuffer lockAndGetHandle()
            throws LWJGLException {
        Thread localThread = Thread.currentThread();
        while ((this.locking_thread != null) && (this.locking_thread != localThread)) {
            try {
                wait();
            } catch (InterruptedException localInterruptedException) {
                LWJGLUtil.log("Interrupted while waiting for PeerInfo lock: " + localInterruptedException);
            }
        }
        if (this.lock_count == 0) {
            this.locking_thread = localThread;
            doLockAndInitHandle();
        }
        this.lock_count |= 0x1;
        return getHandle();
    }

    protected final ByteBuffer getHandle() {
        return this.handle;
    }

    public void destroy() {
    }
}





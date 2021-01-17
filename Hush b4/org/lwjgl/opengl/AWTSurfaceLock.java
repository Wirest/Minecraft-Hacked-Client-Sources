// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.security.PrivilegedActionException;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import java.awt.Canvas;
import java.nio.ByteBuffer;

final class AWTSurfaceLock
{
    private static final int WAIT_DELAY_MILLIS = 100;
    private final ByteBuffer lock_buffer;
    private boolean firstLockSucceeded;
    
    AWTSurfaceLock() {
        this.lock_buffer = createHandle();
    }
    
    private static native ByteBuffer createHandle();
    
    public ByteBuffer lockAndGetHandle(final Canvas component) throws LWJGLException {
        while (!this.privilegedLockAndInitHandle(component)) {
            LWJGLUtil.log("Could not get drawing surface info, retrying...");
            try {
                Thread.sleep(100L);
            }
            catch (InterruptedException e) {
                LWJGLUtil.log("Interrupted while retrying: " + e);
            }
        }
        return this.lock_buffer;
    }
    
    private boolean privilegedLockAndInitHandle(final Canvas component) throws LWJGLException {
        if (this.firstLockSucceeded) {
            return lockAndInitHandle(this.lock_buffer, component);
        }
        try {
            return this.firstLockSucceeded = AccessController.doPrivileged((PrivilegedExceptionAction<Boolean>)new PrivilegedExceptionAction<Boolean>() {
                public Boolean run() throws LWJGLException {
                    return lockAndInitHandle(AWTSurfaceLock.this.lock_buffer, component);
                }
            });
        }
        catch (PrivilegedActionException e) {
            throw (LWJGLException)e.getException();
        }
    }
    
    private static native boolean lockAndInitHandle(final ByteBuffer p0, final Canvas p1) throws LWJGLException;
    
    void unlock() throws LWJGLException {
        nUnlock(this.lock_buffer);
    }
    
    private static native void nUnlock(final ByteBuffer p0) throws LWJGLException;
}

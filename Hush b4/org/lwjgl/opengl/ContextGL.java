// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.Sys;
import org.lwjgl.PointerBuffer;
import java.nio.IntBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import java.nio.ByteBuffer;

final class ContextGL implements Context
{
    private static final ContextImplementation implementation;
    private static final ThreadLocal<ContextGL> current_context_local;
    private final ByteBuffer handle;
    private final PeerInfo peer_info;
    private final ContextAttribs contextAttribs;
    private final boolean forwardCompatible;
    private boolean destroyed;
    private boolean destroy_requested;
    private Thread thread;
    
    private static ContextImplementation createImplementation() {
        switch (LWJGLUtil.getPlatform()) {
            case 1: {
                return new LinuxContextImplementation();
            }
            case 3: {
                return new WindowsContextImplementation();
            }
            case 2: {
                return new MacOSXContextImplementation();
            }
            default: {
                throw new IllegalStateException("Unsupported platform");
            }
        }
    }
    
    PeerInfo getPeerInfo() {
        return this.peer_info;
    }
    
    ContextAttribs getContextAttribs() {
        return this.contextAttribs;
    }
    
    static ContextGL getCurrentContext() {
        return ContextGL.current_context_local.get();
    }
    
    ContextGL(final PeerInfo peer_info, final ContextAttribs attribs, final ContextGL shared_context) throws LWJGLException {
        final ContextGL context_lock = (shared_context != null) ? shared_context : this;
        synchronized (context_lock) {
            if (shared_context != null && shared_context.destroyed) {
                throw new IllegalArgumentException("Shared context is destroyed");
            }
            GLContext.loadOpenGLLibrary();
            try {
                this.peer_info = peer_info;
                this.contextAttribs = attribs;
                IntBuffer attribList;
                if (attribs != null) {
                    attribList = attribs.getAttribList();
                    this.forwardCompatible = attribs.isForwardCompatible();
                }
                else {
                    attribList = null;
                    this.forwardCompatible = false;
                }
                this.handle = ContextGL.implementation.create(peer_info, attribList, (shared_context != null) ? shared_context.handle : null);
            }
            catch (LWJGLException e) {
                GLContext.unloadOpenGLLibrary();
                throw e;
            }
        }
    }
    
    public void releaseCurrent() throws LWJGLException {
        final ContextGL current_context = getCurrentContext();
        if (current_context != null) {
            ContextGL.implementation.releaseCurrentContext();
            GLContext.useContext(null);
            ContextGL.current_context_local.set(null);
            synchronized (current_context) {
                current_context.thread = null;
                current_context.checkDestroy();
            }
        }
    }
    
    public synchronized void releaseDrawable() throws LWJGLException {
        if (this.destroyed) {
            throw new IllegalStateException("Context is destroyed");
        }
        ContextGL.implementation.releaseDrawable(this.getHandle());
    }
    
    public synchronized void update() {
        if (this.destroyed) {
            throw new IllegalStateException("Context is destroyed");
        }
        ContextGL.implementation.update(this.getHandle());
    }
    
    public static void swapBuffers() throws LWJGLException {
        ContextGL.implementation.swapBuffers();
    }
    
    private boolean canAccess() {
        return this.thread == null || Thread.currentThread() == this.thread;
    }
    
    private void checkAccess() {
        if (!this.canAccess()) {
            throw new IllegalStateException("From thread " + Thread.currentThread() + ": " + this.thread + " already has the context current");
        }
    }
    
    public synchronized void makeCurrent() throws LWJGLException {
        this.checkAccess();
        if (this.destroyed) {
            throw new IllegalStateException("Context is destroyed");
        }
        this.thread = Thread.currentThread();
        ContextGL.current_context_local.set(this);
        ContextGL.implementation.makeCurrent(this.peer_info, this.handle);
        GLContext.useContext(this, this.forwardCompatible);
    }
    
    ByteBuffer getHandle() {
        return this.handle;
    }
    
    public synchronized boolean isCurrent() throws LWJGLException {
        if (this.destroyed) {
            throw new IllegalStateException("Context is destroyed");
        }
        return ContextGL.implementation.isCurrent(this.handle);
    }
    
    private void checkDestroy() {
        if (!this.destroyed && this.destroy_requested) {
            try {
                this.releaseDrawable();
                ContextGL.implementation.destroy(this.peer_info, this.handle);
                CallbackUtil.unregisterCallbacks(this);
                this.destroyed = true;
                this.thread = null;
                GLContext.unloadOpenGLLibrary();
            }
            catch (LWJGLException e) {
                LWJGLUtil.log("Exception occurred while destroying context: " + e);
            }
        }
    }
    
    public static void setSwapInterval(final int value) {
        ContextGL.implementation.setSwapInterval(value);
    }
    
    public synchronized void forceDestroy() throws LWJGLException {
        this.checkAccess();
        this.destroy();
    }
    
    public synchronized void destroy() throws LWJGLException {
        if (this.destroyed) {
            return;
        }
        this.destroy_requested = true;
        final boolean was_current = this.isCurrent();
        int error = 0;
        if (was_current) {
            try {
                error = GL11.glGetError();
            }
            catch (Exception ex) {}
            this.releaseCurrent();
        }
        this.checkDestroy();
        if (was_current && error != 0) {
            throw new OpenGLException(error);
        }
    }
    
    public synchronized void setCLSharingProperties(final PointerBuffer properties) throws LWJGLException {
        final ByteBuffer peer_handle = this.peer_info.lockAndGetHandle();
        try {
            switch (LWJGLUtil.getPlatform()) {
                case 3: {
                    final WindowsContextImplementation implWindows = (WindowsContextImplementation)ContextGL.implementation;
                    properties.put(8200L).put(implWindows.getHGLRC(this.handle));
                    properties.put(8203L).put(implWindows.getHDC(peer_handle));
                    return;
                }
                case 1: {
                    final LinuxContextImplementation implLinux = (LinuxContextImplementation)ContextGL.implementation;
                    properties.put(8200L).put(implLinux.getGLXContext(this.handle));
                    properties.put(8202L).put(implLinux.getDisplay(peer_handle));
                    return;
                }
                case 2: {
                    if (LWJGLUtil.isMacOSXEqualsOrBetterThan(10, 6)) {
                        final MacOSXContextImplementation implMacOSX = (MacOSXContextImplementation)ContextGL.implementation;
                        final long CGLShareGroup = implMacOSX.getCGLShareGroup(this.handle);
                        properties.put(268435456L).put(CGLShareGroup);
                        return;
                    }
                    break;
                }
            }
            throw new UnsupportedOperationException("CL/GL context sharing is not supported on this platform.");
        }
        finally {
            this.peer_info.unlock();
        }
    }
    
    static {
        current_context_local = new ThreadLocal<ContextGL>();
        Sys.initialize();
        implementation = createImplementation();
    }
}

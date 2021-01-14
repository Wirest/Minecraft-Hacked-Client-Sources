package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.PointerBuffer;
import org.lwjgl.Sys;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

final class ContextGL
        implements Context {
    private static final ContextImplementation implementation = createImplementation();
    private static final ThreadLocal<ContextGL> current_context_local = new ThreadLocal();

    static {
        Sys.initialize();
    }

    private final ByteBuffer handle;
    private final PeerInfo peer_info;
    private final ContextAttribs contextAttribs;
    private final boolean forwardCompatible;
    private boolean destroyed;
    private boolean destroy_requested;
    private Thread thread;

    ContextGL(PeerInfo paramPeerInfo, ContextAttribs paramContextAttribs, ContextGL paramContextGL)
            throws LWJGLException {
        ContextGL localContextGL = paramContextGL != null ? paramContextGL : this;
        synchronized (localContextGL) {
            if ((paramContextGL != null) && (paramContextGL.destroyed)) {
                throw new IllegalArgumentException("Shared context is destroyed");
            }
            GLContext.loadOpenGLLibrary();
            try {
                this.peer_info = paramPeerInfo;
                this.contextAttribs = paramContextAttribs;
                IntBuffer localIntBuffer;
                if (paramContextAttribs != null) {
                    localIntBuffer = paramContextAttribs.getAttribList();
                    this.forwardCompatible = paramContextAttribs.isForwardCompatible();
                } else {
                    localIntBuffer = null;
                    this.forwardCompatible = false;
                }
                this.handle = implementation.create(paramPeerInfo, localIntBuffer, paramContextGL != null ? paramContextGL.handle : null);
            } catch (LWJGLException localLWJGLException) {
                GLContext.unloadOpenGLLibrary();
                throw localLWJGLException;
            }
        }
    }

    private static ContextImplementation createImplementation() {
        switch () {
            case 1:
                return new LinuxContextImplementation();
            case 3:
                return new WindowsContextImplementation();
            case 2:
                return new MacOSXContextImplementation();
        }
        throw new IllegalStateException("Unsupported platform");
    }

    static ContextGL getCurrentContext() {
        return (ContextGL) current_context_local.get();
    }

    public static void swapBuffers()
            throws LWJGLException {
        implementation.swapBuffers();
    }

    public static void setSwapInterval(int paramInt) {
        implementation.setSwapInterval(paramInt);
    }

    PeerInfo getPeerInfo() {
        return this.peer_info;
    }

    ContextAttribs getContextAttribs() {
        return this.contextAttribs;
    }

    public void releaseCurrent()
            throws LWJGLException {
        ContextGL localContextGL = getCurrentContext();
        if (localContextGL != null) {
            implementation.releaseCurrentContext();
            GLContext.useContext(null);
            current_context_local.set(null);
            synchronized (localContextGL) {
                localContextGL.thread = null;
                localContextGL.checkDestroy();
            }
        }
    }

    public synchronized void releaseDrawable()
            throws LWJGLException {
        if (this.destroyed) {
            throw new IllegalStateException("Context is destroyed");
        }
        implementation.releaseDrawable(getHandle());
    }

    public synchronized void update() {
        if (this.destroyed) {
            throw new IllegalStateException("Context is destroyed");
        }
        implementation.update(getHandle());
    }

    private boolean canAccess() {
        return (this.thread == null) || (Thread.currentThread() == this.thread);
    }

    private void checkAccess() {
        if (!canAccess()) {
            throw new IllegalStateException("From thread " + Thread.currentThread() + ": " + this.thread + " already has the context current");
        }
    }

    public synchronized void makeCurrent()
            throws LWJGLException {
        checkAccess();
        if (this.destroyed) {
            throw new IllegalStateException("Context is destroyed");
        }
        this.thread = Thread.currentThread();
        current_context_local.set(this);
        implementation.makeCurrent(this.peer_info, this.handle);
        GLContext.useContext(this, this.forwardCompatible);
    }

    ByteBuffer getHandle() {
        return this.handle;
    }

    public synchronized boolean isCurrent()
            throws LWJGLException {
        if (this.destroyed) {
            throw new IllegalStateException("Context is destroyed");
        }
        return implementation.isCurrent(this.handle);
    }

    private void checkDestroy() {
        if ((!this.destroyed) && (this.destroy_requested)) {
            try {
                releaseDrawable();
                implementation.destroy(this.peer_info, this.handle);
                CallbackUtil.unregisterCallbacks(this);
                this.destroyed = true;
                this.thread = null;
                GLContext.unloadOpenGLLibrary();
            } catch (LWJGLException localLWJGLException) {
                LWJGLUtil.log("Exception occurred while destroying context: " + localLWJGLException);
            }
        }
    }

    public synchronized void forceDestroy()
            throws LWJGLException {
        checkAccess();
        destroy();
    }

    public synchronized void destroy()
            throws LWJGLException {
        if (this.destroyed) {
            return;
        }
        this.destroy_requested = true;
        boolean bool = isCurrent();
        int i = 0;
        if (bool) {
            try {
                i = GL11.glGetError();
            } catch (Exception localException) {
            }
            releaseCurrent();
        }
        checkDestroy();
        if ((bool) && (i != 0)) {
            throw new OpenGLException(i);
        }
    }

    public synchronized void setCLSharingProperties(PointerBuffer paramPointerBuffer)
            throws LWJGLException {
        ByteBuffer localByteBuffer = this.peer_info.lockAndGetHandle();
        try {
            switch (LWJGLUtil.getPlatform()) {
                case 3:
                    WindowsContextImplementation localWindowsContextImplementation = (WindowsContextImplementation) implementation;
                    paramPointerBuffer.put(8200L).put(localWindowsContextImplementation.getHGLRC(this.handle));
                    paramPointerBuffer.put(8203L).put(localWindowsContextImplementation.getHDC(localByteBuffer));
                    break;
                case 1:
                    LinuxContextImplementation localLinuxContextImplementation = (LinuxContextImplementation) implementation;
                    paramPointerBuffer.put(8200L).put(localLinuxContextImplementation.getGLXContext(this.handle));
                    paramPointerBuffer.put(8202L).put(localLinuxContextImplementation.getDisplay(localByteBuffer));
                    break;
                case 2:
                    if (LWJGLUtil.isMacOSXEqualsOrBetterThan(10, 6)) {
                        MacOSXContextImplementation localMacOSXContextImplementation = (MacOSXContextImplementation) implementation;
                        long l = localMacOSXContextImplementation.getCGLShareGroup(this.handle);
                        paramPointerBuffer.put(268435456L).put(l);
                    }
                    break;
            }
            throw new UnsupportedOperationException("CL/GL context sharing is not supported on this platform.");
        } finally {
            this.peer_info.unlock();
        }
    }
}





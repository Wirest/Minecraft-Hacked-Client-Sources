// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.PointerBuffer;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.LWJGLException;

abstract class DrawableGL implements DrawableLWJGL
{
    protected PixelFormat pixel_format;
    protected PeerInfo peer_info;
    protected ContextGL context;
    
    protected DrawableGL() {
    }
    
    public void setPixelFormat(final PixelFormatLWJGL pf) throws LWJGLException {
        throw new UnsupportedOperationException();
    }
    
    public void setPixelFormat(final PixelFormatLWJGL pf, final ContextAttribs attribs) throws LWJGLException {
        this.pixel_format = (PixelFormat)pf;
        this.peer_info = Display.getImplementation().createPeerInfo(this.pixel_format, attribs);
    }
    
    public PixelFormatLWJGL getPixelFormat() {
        return this.pixel_format;
    }
    
    public ContextGL getContext() {
        synchronized (GlobalLock.lock) {
            return this.context;
        }
    }
    
    public ContextGL createSharedContext() throws LWJGLException {
        synchronized (GlobalLock.lock) {
            this.checkDestroyed();
            return new ContextGL(this.peer_info, this.context.getContextAttribs(), this.context);
        }
    }
    
    public void checkGLError() {
        Util.checkGLError();
    }
    
    public void setSwapInterval(final int swap_interval) {
        ContextGL.setSwapInterval(swap_interval);
    }
    
    public void swapBuffers() throws LWJGLException {
        ContextGL.swapBuffers();
    }
    
    public void initContext(final float r, final float g, final float b) {
        GL11.glClearColor(r, g, b, 0.0f);
        GL11.glClear(16384);
    }
    
    public boolean isCurrent() throws LWJGLException {
        synchronized (GlobalLock.lock) {
            this.checkDestroyed();
            return this.context.isCurrent();
        }
    }
    
    public void makeCurrent() throws LWJGLException {
        synchronized (GlobalLock.lock) {
            this.checkDestroyed();
            this.context.makeCurrent();
        }
    }
    
    public void releaseContext() throws LWJGLException {
        synchronized (GlobalLock.lock) {
            this.checkDestroyed();
            if (this.context.isCurrent()) {
                this.context.releaseCurrent();
            }
        }
    }
    
    public void destroy() {
        synchronized (GlobalLock.lock) {
            if (this.context == null) {
                return;
            }
            try {
                this.releaseContext();
                this.context.forceDestroy();
                this.context = null;
                if (this.peer_info != null) {
                    this.peer_info.destroy();
                    this.peer_info = null;
                }
            }
            catch (LWJGLException e) {
                LWJGLUtil.log("Exception occurred while destroying Drawable: " + e);
            }
        }
    }
    
    public void setCLSharingProperties(final PointerBuffer properties) throws LWJGLException {
        synchronized (GlobalLock.lock) {
            this.checkDestroyed();
            this.context.setCLSharingProperties(properties);
        }
    }
    
    protected final void checkDestroyed() {
        if (this.context == null) {
            throw new IllegalStateException("The Drawable has no context available.");
        }
    }
}

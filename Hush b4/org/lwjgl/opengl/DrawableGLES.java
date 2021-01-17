// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.PointerBuffer;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengles.PowerManagementEventException;
import org.lwjgl.opengles.GLES20;
import org.lwjgl.opengles.Util;
import org.lwjgl.opengles.ContextAttribs;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengles.EGL;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengles.EGLSurface;
import org.lwjgl.opengles.EGLConfig;
import org.lwjgl.opengles.EGLDisplay;
import org.lwjgl.opengles.PixelFormat;

abstract class DrawableGLES implements DrawableLWJGL
{
    protected PixelFormat pixel_format;
    protected EGLDisplay eglDisplay;
    protected EGLConfig eglConfig;
    protected EGLSurface eglSurface;
    protected ContextGLES context;
    protected Drawable shared_drawable;
    
    protected DrawableGLES() {
    }
    
    public void setPixelFormat(final PixelFormatLWJGL pf) throws LWJGLException {
        synchronized (GlobalLock.lock) {
            this.pixel_format = (PixelFormat)pf;
        }
    }
    
    public PixelFormatLWJGL getPixelFormat() {
        synchronized (GlobalLock.lock) {
            return (PixelFormatLWJGL)this.pixel_format;
        }
    }
    
    public void initialize(final long window, final long display_id, final int eglSurfaceType, final PixelFormat pf) throws LWJGLException {
        synchronized (GlobalLock.lock) {
            if (this.eglSurface != null) {
                this.eglSurface.destroy();
                this.eglSurface = null;
            }
            if (this.eglDisplay != null) {
                this.eglDisplay.terminate();
                this.eglDisplay = null;
            }
            final EGLDisplay eglDisplay = EGL.eglGetDisplay((long)(int)display_id);
            final int[] attribs = { 12329, 0, 12352, 4, 12333, 0 };
            final EGLConfig[] configs = eglDisplay.chooseConfig(pf.getAttribBuffer(eglDisplay, eglSurfaceType, attribs), (EGLConfig[])null, BufferUtils.createIntBuffer(1));
            if (configs.length == 0) {
                throw new LWJGLException("No EGLConfigs found for the specified PixelFormat.");
            }
            final EGLConfig eglConfig = pf.getBestMatch(configs);
            final EGLSurface eglSurface = eglDisplay.createWindowSurface(eglConfig, window, (IntBuffer)null);
            pf.setSurfaceAttribs(eglSurface);
            this.eglDisplay = eglDisplay;
            this.eglConfig = eglConfig;
            this.eglSurface = eglSurface;
            if (this.context != null) {
                this.context.getEGLContext().setDisplay(eglDisplay);
            }
        }
    }
    
    public void createContext(final ContextAttribs attribs, final Drawable shared_drawable) throws LWJGLException {
        synchronized (GlobalLock.lock) {
            this.context = new ContextGLES(this, attribs, (shared_drawable != null) ? ((DrawableGLES)shared_drawable).getContext() : null);
            this.shared_drawable = shared_drawable;
        }
    }
    
    Drawable getSharedDrawable() {
        synchronized (GlobalLock.lock) {
            return this.shared_drawable;
        }
    }
    
    public EGLDisplay getEGLDisplay() {
        synchronized (GlobalLock.lock) {
            return this.eglDisplay;
        }
    }
    
    public EGLConfig getEGLConfig() {
        synchronized (GlobalLock.lock) {
            return this.eglConfig;
        }
    }
    
    public EGLSurface getEGLSurface() {
        synchronized (GlobalLock.lock) {
            return this.eglSurface;
        }
    }
    
    public ContextGLES getContext() {
        synchronized (GlobalLock.lock) {
            return this.context;
        }
    }
    
    public Context createSharedContext() throws LWJGLException {
        synchronized (GlobalLock.lock) {
            this.checkDestroyed();
            return new ContextGLES(this, this.context.getContextAttribs(), this.context);
        }
    }
    
    public void checkGLError() {
        Util.checkGLError();
    }
    
    public void setSwapInterval(final int swap_interval) {
        ContextGLES.setSwapInterval(swap_interval);
    }
    
    public void swapBuffers() throws LWJGLException {
        ContextGLES.swapBuffers();
    }
    
    public void initContext(final float r, final float g, final float b) {
        GLES20.glClearColor(r, g, b, 0.0f);
        GLES20.glClear(16384);
    }
    
    public boolean isCurrent() throws LWJGLException {
        synchronized (GlobalLock.lock) {
            this.checkDestroyed();
            return this.context.isCurrent();
        }
    }
    
    public void makeCurrent() throws LWJGLException, PowerManagementEventException {
        synchronized (GlobalLock.lock) {
            this.checkDestroyed();
            this.context.makeCurrent();
        }
    }
    
    public void releaseContext() throws LWJGLException, PowerManagementEventException {
        synchronized (GlobalLock.lock) {
            this.checkDestroyed();
            if (this.context.isCurrent()) {
                this.context.releaseCurrent();
            }
        }
    }
    
    public void destroy() {
        synchronized (GlobalLock.lock) {
            try {
                if (this.context != null) {
                    try {
                        this.releaseContext();
                    }
                    catch (PowerManagementEventException ex) {}
                    this.context.forceDestroy();
                    this.context = null;
                }
                if (this.eglSurface != null) {
                    this.eglSurface.destroy();
                    this.eglSurface = null;
                }
                if (this.eglDisplay != null) {
                    this.eglDisplay.terminate();
                    this.eglDisplay = null;
                }
                this.pixel_format = null;
                this.shared_drawable = null;
            }
            catch (LWJGLException e) {
                LWJGLUtil.log("Exception occurred while destroying Drawable: " + e);
            }
        }
    }
    
    protected void checkDestroyed() {
        if (this.context == null) {
            throw new IllegalStateException("The Drawable has no context available.");
        }
    }
    
    public void setCLSharingProperties(final PointerBuffer properties) throws LWJGLException {
        throw new UnsupportedOperationException();
    }
}

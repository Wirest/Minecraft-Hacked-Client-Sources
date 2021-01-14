package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.PointerBuffer;

abstract class DrawableGL
        implements DrawableLWJGL {
    protected PixelFormat pixel_format;
    protected PeerInfo peer_info;
    protected ContextGL context;

    public void setPixelFormat(PixelFormatLWJGL paramPixelFormatLWJGL, ContextAttribs paramContextAttribs)
            throws LWJGLException {
        this.pixel_format = ((PixelFormat) paramPixelFormatLWJGL);
        this.peer_info = Display.getImplementation().createPeerInfo(this.pixel_format, paramContextAttribs);
    }

    public PixelFormatLWJGL getPixelFormat() {
        return this.pixel_format;
    }

    public void setPixelFormat(PixelFormatLWJGL paramPixelFormatLWJGL)
            throws LWJGLException {
        throw new UnsupportedOperationException();
    }

    public ContextGL getContext() {
        // Byte code:
        //   0: getstatic 52	org/lwjgl/opengl/GlobalLock:lock	Ljava/lang/Object;
        //   3: dup
        //   4: astore_1
        //   5: monitorenter
        //   6: aload_0
        //   7: getfield 54	org/lwjgl/opengl/DrawableGL:context	Lorg/lwjgl/opengl/ContextGL;
        //   10: aload_1
        //   11: monitorexit
        //   12: areturn
        //   13: astore_2
        //   14: aload_1
        //   15: monitorexit
        //   16: aload_2
        //   17: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	18	0	this	DrawableGL
        //   4	11	1	Ljava/lang/Object;	Object
        //   13	4	2	localObject1	Object
        // Exception table:
        //   from	to	target	type
        //   6	12	13	finally
        //   13	16	13	finally
    }

    public ContextGL createSharedContext()
            throws LWJGLException {
        synchronized (GlobalLock.lock) {
            checkDestroyed();
            return new ContextGL(this.peer_info, this.context.getContextAttribs(), this.context);
        }
    }

    public void checkGLError() {
    }

    public void setSwapInterval(int paramInt) {
        ContextGL.setSwapInterval(paramInt);
    }

    public void swapBuffers()
            throws LWJGLException {
    }

    public void initContext(float paramFloat1, float paramFloat2, float paramFloat3) {
        GL11.glClearColor(paramFloat1, paramFloat2, paramFloat3, 0.0F);
        GL11.glClear(16384);
    }

    public boolean isCurrent()
            throws LWJGLException {
        synchronized (GlobalLock.lock) {
            checkDestroyed();
            return this.context.isCurrent();
        }
    }

    public void makeCurrent()
            throws LWJGLException {
        synchronized (GlobalLock.lock) {
            checkDestroyed();
            this.context.makeCurrent();
        }
    }

    public void releaseContext()
            throws LWJGLException {
        synchronized (GlobalLock.lock) {
            checkDestroyed();
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
                releaseContext();
                this.context.forceDestroy();
                this.context = null;
                if (this.peer_info != null) {
                    this.peer_info.destroy();
                    this.peer_info = null;
                }
            } catch (LWJGLException localLWJGLException) {
                LWJGLUtil.log("Exception occurred while destroying Drawable: " + localLWJGLException);
            }
        }
    }

    public void setCLSharingProperties(PointerBuffer paramPointerBuffer)
            throws LWJGLException {
        synchronized (GlobalLock.lock) {
            checkDestroyed();
            this.context.setCLSharingProperties(paramPointerBuffer);
        }
    }

    protected final void checkDestroyed() {
        if (this.context == null) {
            throw new IllegalStateException("The Drawable has no context available.");
        }
    }
}





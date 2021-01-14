package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;

abstract interface Context {
    public abstract boolean isCurrent()
            throws LWJGLException;

    public abstract void makeCurrent()
            throws LWJGLException;

    public abstract void releaseCurrent()
            throws LWJGLException;

    public abstract void releaseDrawable()
            throws LWJGLException;
}





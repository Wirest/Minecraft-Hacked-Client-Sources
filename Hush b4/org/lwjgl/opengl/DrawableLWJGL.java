// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;

interface DrawableLWJGL extends Drawable
{
    void setPixelFormat(final PixelFormatLWJGL p0) throws LWJGLException;
    
    void setPixelFormat(final PixelFormatLWJGL p0, final ContextAttribs p1) throws LWJGLException;
    
    PixelFormatLWJGL getPixelFormat();
    
    Context getContext();
    
    Context createSharedContext() throws LWJGLException;
    
    void checkGLError();
    
    void setSwapInterval(final int p0);
    
    void swapBuffers() throws LWJGLException;
    
    void initContext(final float p0, final float p1, final float p2);
}

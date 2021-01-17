// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;

interface Context
{
    boolean isCurrent() throws LWJGLException;
    
    void makeCurrent() throws LWJGLException;
    
    void releaseCurrent() throws LWJGLException;
    
    void releaseDrawable() throws LWJGLException;
}

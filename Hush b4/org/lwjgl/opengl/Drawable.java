// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.PointerBuffer;
import org.lwjgl.LWJGLException;

public interface Drawable
{
    boolean isCurrent() throws LWJGLException;
    
    void makeCurrent() throws LWJGLException;
    
    void releaseContext() throws LWJGLException;
    
    void destroy();
    
    void setCLSharingProperties(final PointerBuffer p0) throws LWJGLException;
}

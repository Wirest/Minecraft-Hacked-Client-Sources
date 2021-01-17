// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.PointerBuffer;
import org.lwjgl.LWJGLException;

public final class SharedDrawable extends DrawableGL
{
    public SharedDrawable(final Drawable drawable) throws LWJGLException {
        this.context = (ContextGL)((DrawableLWJGL)drawable).createSharedContext();
    }
    
    @Override
    public ContextGL createSharedContext() {
        throw new UnsupportedOperationException();
    }
}

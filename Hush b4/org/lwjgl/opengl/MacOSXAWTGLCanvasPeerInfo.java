// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import java.awt.Canvas;

final class MacOSXAWTGLCanvasPeerInfo extends MacOSXCanvasPeerInfo
{
    private final Canvas component;
    
    MacOSXAWTGLCanvasPeerInfo(final Canvas component, final PixelFormat pixel_format, final ContextAttribs attribs, final boolean support_pbuffer) throws LWJGLException {
        super(pixel_format, attribs, support_pbuffer);
        this.component = component;
    }
    
    @Override
    protected void doLockAndInitHandle() throws LWJGLException {
        this.initHandle(this.component);
    }
}

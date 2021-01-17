// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.awt.Canvas;
import org.lwjgl.LWJGLException;

final class MacOSXDisplayPeerInfo extends MacOSXCanvasPeerInfo
{
    private boolean locked;
    
    MacOSXDisplayPeerInfo(final PixelFormat pixel_format, final ContextAttribs attribs, final boolean support_pbuffer) throws LWJGLException {
        super(pixel_format, attribs, support_pbuffer);
    }
    
    @Override
    protected void doLockAndInitHandle() throws LWJGLException {
        if (this.locked) {
            throw new RuntimeException("Already locked");
        }
        final Canvas canvas = ((MacOSXDisplay)Display.getImplementation()).getCanvas();
        if (canvas != null) {
            this.initHandle(canvas);
            this.locked = true;
        }
    }
    
    @Override
    protected void doUnlock() throws LWJGLException {
        if (this.locked) {
            super.doUnlock();
            this.locked = false;
        }
    }
}

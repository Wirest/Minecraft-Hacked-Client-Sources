// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.LWJGLException;

final class MacOSXPbufferPeerInfo extends MacOSXPeerInfo
{
    MacOSXPbufferPeerInfo(final int width, final int height, final PixelFormat pixel_format, final ContextAttribs attribs) throws LWJGLException {
        super(pixel_format, attribs, false, false, true, false);
        nCreate(this.getHandle(), width, height);
    }
    
    private static native void nCreate(final ByteBuffer p0, final int p1, final int p2) throws LWJGLException;
    
    @Override
    public void destroy() {
        nDestroy(this.getHandle());
    }
    
    private static native void nDestroy(final ByteBuffer p0);
    
    @Override
    protected void doLockAndInitHandle() throws LWJGLException {
    }
    
    @Override
    protected void doUnlock() throws LWJGLException {
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;

abstract class MacOSXPeerInfo extends PeerInfo
{
    MacOSXPeerInfo(final PixelFormat pixel_format, final ContextAttribs attribs, final boolean use_display_bpp, final boolean support_window, final boolean support_pbuffer, final boolean double_buffered) throws LWJGLException {
        super(createHandle());
        final boolean gl32 = attribs != null && (3 < attribs.getMajorVersion() || (attribs.getMajorVersion() == 3 && 2 <= attribs.getMinorVersion())) && attribs.isProfileCore();
        if (gl32 && !LWJGLUtil.isMacOSXEqualsOrBetterThan(10, 7)) {
            throw new LWJGLException("OpenGL 3.2+ requested, but it requires MacOS X 10.7 or newer");
        }
        this.choosePixelFormat(pixel_format, gl32, use_display_bpp, support_window, support_pbuffer, double_buffered);
    }
    
    private static native ByteBuffer createHandle();
    
    private void choosePixelFormat(final PixelFormat pixel_format, final boolean gl32, final boolean use_display_bpp, final boolean support_window, final boolean support_pbuffer, final boolean double_buffered) throws LWJGLException {
        nChoosePixelFormat(this.getHandle(), pixel_format, gl32, use_display_bpp, support_window, support_pbuffer, double_buffered);
    }
    
    private static native void nChoosePixelFormat(final ByteBuffer p0, final PixelFormat p1, final boolean p2, final boolean p3, final boolean p4, final boolean p5, final boolean p6) throws LWJGLException;
    
    @Override
    public void destroy() {
        nDestroy(this.getHandle());
    }
    
    private static native void nDestroy(final ByteBuffer p0);
}

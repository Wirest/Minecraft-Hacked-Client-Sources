// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import java.nio.IntBuffer;
import java.nio.ByteBuffer;

abstract class WindowsPeerInfo extends PeerInfo
{
    protected WindowsPeerInfo() {
        super(createHandle());
    }
    
    private static native ByteBuffer createHandle();
    
    protected static int choosePixelFormat(final long hdc, final int origin_x, final int origin_y, final PixelFormat pixel_format, final IntBuffer pixel_format_caps, final boolean use_hdc_bpp, final boolean support_window, final boolean support_pbuffer, final boolean double_buffered) throws LWJGLException {
        return nChoosePixelFormat(hdc, origin_x, origin_y, pixel_format, pixel_format_caps, use_hdc_bpp, support_window, support_pbuffer, double_buffered);
    }
    
    private static native int nChoosePixelFormat(final long p0, final int p1, final int p2, final PixelFormat p3, final IntBuffer p4, final boolean p5, final boolean p6, final boolean p7, final boolean p8) throws LWJGLException;
    
    protected static native void setPixelFormat(final long p0, final int p1) throws LWJGLException;
    
    public final long getHdc() {
        return nGetHdc(this.getHandle());
    }
    
    private static native long nGetHdc(final ByteBuffer p0);
    
    public final long getHwnd() {
        return nGetHwnd(this.getHandle());
    }
    
    private static native long nGetHwnd(final ByteBuffer p0);
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.security.AccessController;
import org.lwjgl.LWJGLUtil;
import java.security.PrivilegedAction;
import java.awt.Toolkit;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import org.lwjgl.LWJGLException;
import java.awt.Canvas;

final class WindowsCanvasImplementation implements AWTCanvasImplementation
{
    public PeerInfo createPeerInfo(final Canvas component, final PixelFormat pixel_format, final ContextAttribs attribs) throws LWJGLException {
        return new WindowsAWTGLCanvasPeerInfo(component, pixel_format);
    }
    
    public GraphicsConfiguration findConfiguration(final GraphicsDevice device, final PixelFormat pixel_format) throws LWJGLException {
        return null;
    }
    
    static {
        Toolkit.getDefaultToolkit();
        AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Object>() {
            public Object run() {
                try {
                    System.loadLibrary("jawt");
                }
                catch (UnsatisfiedLinkError e) {
                    LWJGLUtil.log("Failed to load jawt: " + e.getMessage());
                }
                return null;
            }
        });
    }
}

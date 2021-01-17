// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import org.lwjgl.LWJGLException;
import java.awt.Canvas;

final class MacOSXCanvasImplementation implements AWTCanvasImplementation
{
    public PeerInfo createPeerInfo(final Canvas component, final PixelFormat pixel_format, final ContextAttribs attribs) throws LWJGLException {
        try {
            return new MacOSXAWTGLCanvasPeerInfo(component, pixel_format, attribs, true);
        }
        catch (LWJGLException e) {
            return new MacOSXAWTGLCanvasPeerInfo(component, pixel_format, attribs, false);
        }
    }
    
    public GraphicsConfiguration findConfiguration(final GraphicsDevice device, final PixelFormat pixel_format) throws LWJGLException {
        return null;
    }
}

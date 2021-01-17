// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import org.lwjgl.LWJGLException;
import java.awt.Canvas;

interface AWTCanvasImplementation
{
    PeerInfo createPeerInfo(final Canvas p0, final PixelFormat p1, final ContextAttribs p2) throws LWJGLException;
    
    GraphicsConfiguration findConfiguration(final GraphicsDevice p0, final PixelFormat p1) throws LWJGLException;
}

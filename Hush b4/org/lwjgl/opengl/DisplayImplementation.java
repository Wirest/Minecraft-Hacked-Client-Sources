// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.LWJGLException;
import java.awt.Canvas;

interface DisplayImplementation extends InputImplementation
{
    void createWindow(final DrawableLWJGL p0, final DisplayMode p1, final Canvas p2, final int p3, final int p4) throws LWJGLException;
    
    void destroyWindow();
    
    void switchDisplayMode(final DisplayMode p0) throws LWJGLException;
    
    void resetDisplayMode();
    
    int getGammaRampLength();
    
    void setGammaRamp(final FloatBuffer p0) throws LWJGLException;
    
    String getAdapter();
    
    String getVersion();
    
    DisplayMode init() throws LWJGLException;
    
    void setTitle(final String p0);
    
    boolean isCloseRequested();
    
    boolean isVisible();
    
    boolean isActive();
    
    boolean isDirty();
    
    PeerInfo createPeerInfo(final PixelFormat p0, final ContextAttribs p1) throws LWJGLException;
    
    void update();
    
    void reshape(final int p0, final int p1, final int p2, final int p3);
    
    DisplayMode[] getAvailableDisplayModes() throws LWJGLException;
    
    int getPbufferCapabilities();
    
    boolean isBufferLost(final PeerInfo p0);
    
    PeerInfo createPbuffer(final int p0, final int p1, final PixelFormat p2, final ContextAttribs p3, final IntBuffer p4, final IntBuffer p5) throws LWJGLException;
    
    void setPbufferAttrib(final PeerInfo p0, final int p1, final int p2);
    
    void bindTexImageToPbuffer(final PeerInfo p0, final int p1);
    
    void releaseTexImageFromPbuffer(final PeerInfo p0, final int p1);
    
    int setIcon(final ByteBuffer[] p0);
    
    void setResizable(final boolean p0);
    
    boolean wasResized();
    
    int getWidth();
    
    int getHeight();
    
    int getX();
    
    int getY();
    
    float getPixelScaleFactor();
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.awt.Container;
import java.awt.Point;
import java.awt.Component;
import javax.swing.SwingUtilities;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.Insets;
import java.awt.Canvas;
import org.lwjgl.LWJGLException;
import java.nio.ByteBuffer;

abstract class MacOSXCanvasPeerInfo extends MacOSXPeerInfo
{
    private final AWTSurfaceLock awt_surface;
    public ByteBuffer window_handle;
    
    protected MacOSXCanvasPeerInfo(final PixelFormat pixel_format, final ContextAttribs attribs, final boolean support_pbuffer) throws LWJGLException {
        super(pixel_format, attribs, true, true, support_pbuffer, true);
        this.awt_surface = new AWTSurfaceLock();
    }
    
    protected void initHandle(final Canvas component) throws LWJGLException {
        boolean forceCALayer = true;
        boolean autoResizable = true;
        final String javaVersion = System.getProperty("java.version");
        if (javaVersion.startsWith("1.5") || javaVersion.startsWith("1.6")) {
            forceCALayer = false;
        }
        else if (javaVersion.startsWith("1.7")) {
            autoResizable = false;
        }
        final Insets insets = getInsets(component);
        final int top = (insets != null) ? insets.top : 0;
        final int left = (insets != null) ? insets.left : 0;
        this.window_handle = nInitHandle(this.awt_surface.lockAndGetHandle(component), this.getHandle(), this.window_handle, forceCALayer, autoResizable, component.getX() - left, component.getY() - top);
        if (javaVersion.startsWith("1.7")) {
            this.addComponentListener(component);
            reSetLayerBounds(component, this.getHandle());
        }
    }
    
    private void addComponentListener(final Canvas component) {
        final ComponentListener[] components = component.getComponentListeners();
        for (int i = 0; i < components.length; ++i) {
            final ComponentListener c = components[i];
            if (c.toString() == "CanvasPeerInfoListener") {
                return;
            }
        }
        final ComponentListener comp = new ComponentListener() {
            public void componentHidden(final ComponentEvent e) {
            }
            
            public void componentMoved(final ComponentEvent e) {
                reSetLayerBounds(component, MacOSXCanvasPeerInfo.this.getHandle());
            }
            
            public void componentResized(final ComponentEvent e) {
                reSetLayerBounds(component, MacOSXCanvasPeerInfo.this.getHandle());
            }
            
            public void componentShown(final ComponentEvent e) {
            }
            
            @Override
            public String toString() {
                return "CanvasPeerInfoListener";
            }
        };
        component.addComponentListener(comp);
    }
    
    private static native ByteBuffer nInitHandle(final ByteBuffer p0, final ByteBuffer p1, final ByteBuffer p2, final boolean p3, final boolean p4, final int p5, final int p6) throws LWJGLException;
    
    private static native void nSetLayerPosition(final ByteBuffer p0, final int p1, final int p2);
    
    private static native void nSetLayerBounds(final ByteBuffer p0, final int p1, final int p2, final int p3, final int p4);
    
    private static void reSetLayerBounds(final Canvas component, final ByteBuffer peer_info_handle) {
        final Component peer = SwingUtilities.getRoot(component);
        final Point rtLoc = SwingUtilities.convertPoint(component.getParent(), component.getLocation(), peer);
        int x = (int)rtLoc.getX();
        int y = (int)rtLoc.getY();
        final Insets insets = getInsets(component);
        x -= ((insets != null) ? insets.left : 0);
        y -= ((insets != null) ? insets.top : 0);
        y = peer.getHeight() - y - component.getHeight();
        nSetLayerBounds(peer_info_handle, x, y, component.getWidth(), component.getHeight());
    }
    
    @Override
    protected void doUnlock() throws LWJGLException {
        this.awt_surface.unlock();
    }
    
    private static Insets getInsets(final Canvas component) {
        final Container c = SwingUtilities.getRootPane(component);
        if (c != null) {
            return c.getInsets();
        }
        return new Insets(0, 0, 0, 0);
    }
}

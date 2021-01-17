// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.awt.event.HierarchyEvent;
import java.awt.event.ComponentEvent;
import java.awt.EventQueue;
import java.awt.Canvas;
import java.awt.event.HierarchyListener;
import java.awt.event.ComponentListener;

final class MacOSXCanvasListener implements ComponentListener, HierarchyListener
{
    private final Canvas canvas;
    private int width;
    private int height;
    private boolean context_update;
    private boolean resized;
    
    MacOSXCanvasListener(final Canvas canvas) {
        (this.canvas = canvas).addComponentListener(this);
        canvas.addHierarchyListener(this);
        this.setUpdate();
    }
    
    public void disableListeners() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                MacOSXCanvasListener.this.canvas.removeComponentListener(MacOSXCanvasListener.this);
                MacOSXCanvasListener.this.canvas.removeHierarchyListener(MacOSXCanvasListener.this);
            }
        });
    }
    
    public boolean syncShouldUpdateContext() {
        final boolean should_update;
        synchronized (this) {
            should_update = this.context_update;
            this.context_update = false;
        }
        return should_update;
    }
    
    private synchronized void setUpdate() {
        synchronized (this) {
            this.width = this.canvas.getWidth();
            this.height = this.canvas.getHeight();
            this.context_update = true;
        }
    }
    
    public int syncGetWidth() {
        synchronized (this) {
            return this.width;
        }
    }
    
    public int syncGetHeight() {
        synchronized (this) {
            return this.height;
        }
    }
    
    public void componentShown(final ComponentEvent e) {
    }
    
    public void componentHidden(final ComponentEvent e) {
    }
    
    public void componentResized(final ComponentEvent e) {
        this.setUpdate();
        this.resized = true;
    }
    
    public void componentMoved(final ComponentEvent e) {
        this.setUpdate();
    }
    
    public void hierarchyChanged(final HierarchyEvent e) {
        this.setUpdate();
    }
    
    public boolean wasResized() {
        if (this.resized) {
            this.resized = false;
            return true;
        }
        return false;
    }
}

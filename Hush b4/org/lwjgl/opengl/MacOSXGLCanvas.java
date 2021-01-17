// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.awt.Graphics;
import java.awt.Canvas;

final class MacOSXGLCanvas extends Canvas
{
    private static final long serialVersionUID = 6916664741667434870L;
    private boolean canvas_painted;
    private boolean dirty;
    
    @Override
    public void update(final Graphics g) {
        this.paint(g);
    }
    
    @Override
    public void paint(final Graphics g) {
        synchronized (this) {
            this.dirty = true;
            this.canvas_painted = true;
        }
    }
    
    public boolean syncCanvasPainted() {
        final boolean result;
        synchronized (this) {
            result = this.canvas_painted;
            this.canvas_painted = false;
        }
        return result;
    }
    
    public boolean syncIsDirty() {
        final boolean result;
        synchronized (this) {
            result = this.dirty;
            this.dirty = false;
        }
        return result;
    }
}

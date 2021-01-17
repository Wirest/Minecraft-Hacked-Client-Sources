// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.opengl.renderer;

public class DefaultLineStripRenderer implements LineStripRenderer
{
    private SGL GL;
    
    public DefaultLineStripRenderer() {
        this.GL = Renderer.get();
    }
    
    @Override
    public void end() {
        this.GL.glEnd();
    }
    
    @Override
    public void setAntiAlias(final boolean antialias) {
        if (antialias) {
            this.GL.glEnable(2848);
        }
        else {
            this.GL.glDisable(2848);
        }
    }
    
    @Override
    public void setWidth(final float width) {
        this.GL.glLineWidth(width);
    }
    
    @Override
    public void start() {
        this.GL.glBegin(3);
    }
    
    @Override
    public void vertex(final float x, final float y) {
        this.GL.glVertex2f(x, y);
    }
    
    @Override
    public void color(final float r, final float g, final float b, final float a) {
        this.GL.glColor4f(r, g, b, a);
    }
    
    @Override
    public void setLineCaps(final boolean caps) {
    }
    
    @Override
    public boolean applyGLLineFixes() {
        return true;
    }
}

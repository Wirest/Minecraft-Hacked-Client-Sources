// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick;

import org.newdawn.slick.opengl.SlickCallable;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;

public class ScalableGame implements Game
{
    private static SGL GL;
    private float normalWidth;
    private float normalHeight;
    private Game held;
    private boolean maintainAspect;
    private int targetWidth;
    private int targetHeight;
    private GameContainer container;
    
    static {
        ScalableGame.GL = Renderer.get();
    }
    
    public ScalableGame(final Game held, final int normalWidth, final int normalHeight) {
        this(held, normalWidth, normalHeight, false);
    }
    
    public ScalableGame(final Game held, final int normalWidth, final int normalHeight, final boolean maintainAspect) {
        this.held = held;
        this.normalWidth = (float)normalWidth;
        this.normalHeight = (float)normalHeight;
        this.maintainAspect = maintainAspect;
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        this.container = container;
        this.recalculateScale();
        this.held.init(container);
    }
    
    public void recalculateScale() throws SlickException {
        this.targetWidth = this.container.getWidth();
        this.targetHeight = this.container.getHeight();
        if (this.maintainAspect) {
            final boolean normalIsWide = this.normalWidth / this.normalHeight > 1.6;
            final boolean containerIsWide = this.targetWidth / (float)this.targetHeight > 1.6;
            final float wScale = this.targetWidth / this.normalWidth;
            final float hScale = this.targetHeight / this.normalHeight;
            if (normalIsWide & containerIsWide) {
                final float scale = (wScale < hScale) ? wScale : hScale;
                this.targetWidth = (int)(this.normalWidth * scale);
                this.targetHeight = (int)(this.normalHeight * scale);
            }
            else if (normalIsWide & !containerIsWide) {
                this.targetWidth = (int)(this.normalWidth * wScale);
                this.targetHeight = (int)(this.normalHeight * wScale);
            }
            else if (!normalIsWide & containerIsWide) {
                this.targetWidth = (int)(this.normalWidth * hScale);
                this.targetHeight = (int)(this.normalHeight * hScale);
            }
            else {
                final float scale = (wScale < hScale) ? wScale : hScale;
                this.targetWidth = (int)(this.normalWidth * scale);
                this.targetHeight = (int)(this.normalHeight * scale);
            }
        }
        if (this.held instanceof InputListener) {
            this.container.getInput().addListener((InputListener)this.held);
        }
        this.container.getInput().setScale(this.normalWidth / this.targetWidth, this.normalHeight / this.targetHeight);
        int yoffset = 0;
        int xoffset = 0;
        if (this.targetHeight < this.container.getHeight()) {
            yoffset = (this.container.getHeight() - this.targetHeight) / 2;
        }
        if (this.targetWidth < this.container.getWidth()) {
            xoffset = (this.container.getWidth() - this.targetWidth) / 2;
        }
        this.container.getInput().setOffset(-xoffset / (this.targetWidth / this.normalWidth), -yoffset / (this.targetHeight / this.normalHeight));
    }
    
    @Override
    public void update(final GameContainer container, final int delta) throws SlickException {
        if (this.targetHeight != container.getHeight() || this.targetWidth != container.getWidth()) {
            this.recalculateScale();
        }
        this.held.update(container, delta);
    }
    
    @Override
    public final void render(final GameContainer container, final Graphics g) throws SlickException {
        int yoffset = 0;
        int xoffset = 0;
        if (this.targetHeight < container.getHeight()) {
            yoffset = (container.getHeight() - this.targetHeight) / 2;
        }
        if (this.targetWidth < container.getWidth()) {
            xoffset = (container.getWidth() - this.targetWidth) / 2;
        }
        SlickCallable.enterSafeBlock();
        g.setClip(xoffset, yoffset, this.targetWidth, this.targetHeight);
        ScalableGame.GL.glTranslatef((float)xoffset, (float)yoffset, 0.0f);
        g.scale(this.targetWidth / this.normalWidth, this.targetHeight / this.normalHeight);
        ScalableGame.GL.glPushMatrix();
        this.held.render(container, g);
        ScalableGame.GL.glPopMatrix();
        g.clearClip();
        SlickCallable.leaveSafeBlock();
        this.renderOverlay(container, g);
    }
    
    protected void renderOverlay(final GameContainer container, final Graphics g) {
    }
    
    @Override
    public boolean closeRequested() {
        return this.held.closeRequested();
    }
    
    @Override
    public String getTitle() {
        return this.held.getTitle();
    }
}

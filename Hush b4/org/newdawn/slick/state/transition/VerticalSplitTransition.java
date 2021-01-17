// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.state.transition;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.Color;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.opengl.renderer.SGL;

public class VerticalSplitTransition implements Transition
{
    protected static SGL GL;
    private GameState prev;
    private float offset;
    private boolean finish;
    private Color background;
    
    static {
        VerticalSplitTransition.GL = Renderer.get();
    }
    
    public VerticalSplitTransition() {
    }
    
    public VerticalSplitTransition(final Color background) {
        this.background = background;
    }
    
    @Override
    public void init(final GameState firstState, final GameState secondState) {
        this.prev = secondState;
    }
    
    @Override
    public boolean isComplete() {
        return this.finish;
    }
    
    @Override
    public void postRender(final StateBasedGame game, final GameContainer container, final Graphics g) throws SlickException {
        g.translate(0.0f, -this.offset);
        g.setClip(0, (int)(-this.offset), container.getWidth(), container.getHeight() / 2);
        if (this.background != null) {
            final Color c = g.getColor();
            g.setColor(this.background);
            g.fillRect(0.0f, 0.0f, (float)container.getWidth(), (float)container.getHeight());
            g.setColor(c);
        }
        VerticalSplitTransition.GL.glPushMatrix();
        this.prev.render(container, game, g);
        VerticalSplitTransition.GL.glPopMatrix();
        g.clearClip();
        g.resetTransform();
        g.translate(0.0f, this.offset);
        g.setClip(0, (int)(container.getHeight() / 2 + this.offset), container.getWidth(), container.getHeight() / 2);
        if (this.background != null) {
            final Color c = g.getColor();
            g.setColor(this.background);
            g.fillRect(0.0f, 0.0f, (float)container.getWidth(), (float)container.getHeight());
            g.setColor(c);
        }
        VerticalSplitTransition.GL.glPushMatrix();
        this.prev.render(container, game, g);
        VerticalSplitTransition.GL.glPopMatrix();
        g.clearClip();
        g.translate(0.0f, -this.offset);
    }
    
    @Override
    public void preRender(final StateBasedGame game, final GameContainer container, final Graphics g) throws SlickException {
    }
    
    @Override
    public void update(final StateBasedGame game, final GameContainer container, final int delta) throws SlickException {
        this.offset += delta * 1.0f;
        if (this.offset > container.getHeight() / 2) {
            this.finish = true;
        }
    }
}

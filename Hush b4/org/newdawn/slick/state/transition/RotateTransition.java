// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.state.transition;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.state.GameState;

public class RotateTransition implements Transition
{
    private GameState prev;
    private float ang;
    private boolean finish;
    private float scale;
    private Color background;
    
    public RotateTransition() {
        this.scale = 1.0f;
    }
    
    public RotateTransition(final Color background) {
        this.scale = 1.0f;
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
        g.translate((float)(container.getWidth() / 2), (float)(container.getHeight() / 2));
        g.scale(this.scale, this.scale);
        g.rotate(0.0f, 0.0f, this.ang);
        g.translate((float)(-container.getWidth() / 2), (float)(-container.getHeight() / 2));
        if (this.background != null) {
            final Color c = g.getColor();
            g.setColor(this.background);
            g.fillRect(0.0f, 0.0f, (float)container.getWidth(), (float)container.getHeight());
            g.setColor(c);
        }
        this.prev.render(container, game, g);
        g.translate((float)(container.getWidth() / 2), (float)(container.getHeight() / 2));
        g.rotate(0.0f, 0.0f, -this.ang);
        g.scale(1.0f / this.scale, 1.0f / this.scale);
        g.translate((float)(-container.getWidth() / 2), (float)(-container.getHeight() / 2));
    }
    
    @Override
    public void preRender(final StateBasedGame game, final GameContainer container, final Graphics g) throws SlickException {
    }
    
    @Override
    public void update(final StateBasedGame game, final GameContainer container, final int delta) throws SlickException {
        this.ang += delta * 0.5f;
        if (this.ang > 500.0f) {
            this.finish = true;
        }
        this.scale -= delta * 0.001f;
        if (this.scale < 0.0f) {
            this.scale = 0.0f;
        }
    }
}

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

public class SelectTransition implements Transition
{
    protected static SGL GL;
    private GameState prev;
    private boolean finish;
    private Color background;
    private float scale1;
    private float xp1;
    private float yp1;
    private float scale2;
    private float xp2;
    private float yp2;
    private boolean init;
    private boolean moveBackDone;
    private int pause;
    
    static {
        SelectTransition.GL = Renderer.get();
    }
    
    public SelectTransition() {
        this.scale1 = 1.0f;
        this.xp1 = 0.0f;
        this.yp1 = 0.0f;
        this.scale2 = 0.4f;
        this.xp2 = 0.0f;
        this.yp2 = 0.0f;
        this.init = false;
        this.moveBackDone = false;
        this.pause = 300;
    }
    
    public SelectTransition(final Color background) {
        this.scale1 = 1.0f;
        this.xp1 = 0.0f;
        this.yp1 = 0.0f;
        this.scale2 = 0.4f;
        this.xp2 = 0.0f;
        this.yp2 = 0.0f;
        this.init = false;
        this.moveBackDone = false;
        this.pause = 300;
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
        g.resetTransform();
        if (!this.moveBackDone) {
            g.translate(this.xp1, this.yp1);
            g.scale(this.scale1, this.scale1);
            g.setClip((int)this.xp1, (int)this.yp1, (int)(this.scale1 * container.getWidth()), (int)(this.scale1 * container.getHeight()));
            this.prev.render(container, game, g);
            g.resetTransform();
            g.clearClip();
        }
    }
    
    @Override
    public void preRender(final StateBasedGame game, final GameContainer container, final Graphics g) throws SlickException {
        if (this.moveBackDone) {
            g.translate(this.xp1, this.yp1);
            g.scale(this.scale1, this.scale1);
            g.setClip((int)this.xp1, (int)this.yp1, (int)(this.scale1 * container.getWidth()), (int)(this.scale1 * container.getHeight()));
            this.prev.render(container, game, g);
            g.resetTransform();
            g.clearClip();
        }
        g.translate(this.xp2, this.yp2);
        g.scale(this.scale2, this.scale2);
        g.setClip((int)this.xp2, (int)this.yp2, (int)(this.scale2 * container.getWidth()), (int)(this.scale2 * container.getHeight()));
    }
    
    @Override
    public void update(final StateBasedGame game, final GameContainer container, final int delta) throws SlickException {
        if (!this.init) {
            this.init = true;
            this.xp2 = (float)(container.getWidth() / 2 + 50);
            this.yp2 = (float)(container.getHeight() / 4);
        }
        if (!this.moveBackDone) {
            if (this.scale1 > 0.4f) {
                this.scale1 -= delta * 0.002f;
                if (this.scale1 <= 0.4f) {
                    this.scale1 = 0.4f;
                }
                this.xp1 += delta * 0.3f;
                if (this.xp1 > 50.0f) {
                    this.xp1 = 50.0f;
                }
                this.yp1 += delta * 0.5f;
                if (this.yp1 > container.getHeight() / 4) {
                    this.yp1 = (float)(container.getHeight() / 4);
                }
            }
            else {
                this.moveBackDone = true;
            }
        }
        else {
            this.pause -= delta;
            if (this.pause > 0) {
                return;
            }
            if (this.scale2 < 1.0f) {
                this.scale2 += delta * 0.002f;
                if (this.scale2 >= 1.0f) {
                    this.scale2 = 1.0f;
                }
                this.xp2 -= delta * 1.5f;
                if (this.xp2 < 0.0f) {
                    this.xp2 = 0.0f;
                }
                this.yp2 -= delta * 0.5f;
                if (this.yp2 < 0.0f) {
                    this.yp2 = 0.0f;
                }
            }
            else {
                this.finish = true;
            }
        }
    }
}

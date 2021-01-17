// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.Game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.CachedRender;
import org.newdawn.slick.BasicGame;

public class CachedRenderTest extends BasicGame
{
    private Runnable operations;
    private CachedRender cached;
    private boolean drawCached;
    
    public CachedRenderTest() {
        super("Cached Render Test");
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        this.operations = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; ++i) {
                    final int c = i + 100;
                    container.getGraphics().setColor(new Color(c, c, c, c));
                    container.getGraphics().drawOval((float)(i * 5 + 50), (float)(i * 3 + 50), 100.0f, 100.0f);
                }
            }
        };
        this.cached = new CachedRender(this.operations);
    }
    
    @Override
    public void update(final GameContainer container, final int delta) throws SlickException {
        if (container.getInput().isKeyPressed(57)) {
            this.drawCached = !this.drawCached;
        }
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) throws SlickException {
        g.setColor(Color.white);
        g.drawString("Press space to toggle caching", 10.0f, 130.0f);
        if (this.drawCached) {
            g.drawString("Drawing from cache", 10.0f, 100.0f);
            this.cached.render();
        }
        else {
            g.drawString("Drawing direct", 10.0f, 100.0f);
            this.operations.run();
        }
    }
    
    public static void main(final String[] argv) {
        try {
            final AppGameContainer container = new AppGameContainer(new CachedRenderTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

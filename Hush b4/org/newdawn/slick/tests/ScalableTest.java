// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Game;
import org.newdawn.slick.ScalableGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.BasicGame;

public class ScalableTest extends BasicGame
{
    public ScalableTest() {
        super("Scalable Test For Widescreen");
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
    }
    
    @Override
    public void update(final GameContainer container, final int delta) throws SlickException {
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) throws SlickException {
        g.setColor(new Color(0.4f, 0.6f, 0.8f));
        g.fillRect(0.0f, 0.0f, 1024.0f, 568.0f);
        g.setColor(Color.white);
        g.drawRect(5.0f, 5.0f, 1014.0f, 558.0f);
        g.setColor(Color.white);
        g.drawString(String.valueOf(container.getInput().getMouseX()) + "," + container.getInput().getMouseY(), 10.0f, 400.0f);
        g.setColor(Color.red);
        g.fillOval((float)(container.getInput().getMouseX() - 10), (float)(container.getInput().getMouseY() - 10), 20.0f, 20.0f);
    }
    
    public static void main(final String[] argv) {
        try {
            final ScalableGame game = new ScalableGame(new ScalableTest(), 1024, 568, true) {
                @Override
                protected void renderOverlay(final GameContainer container, final Graphics g) {
                    g.setColor(Color.white);
                    g.drawString("Outside The Game", 350.0f, 10.0f);
                    g.drawString(String.valueOf(container.getInput().getMouseX()) + "," + container.getInput().getMouseY(), 400.0f, 20.0f);
                }
            };
            final AppGameContainer container = new AppGameContainer(game);
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

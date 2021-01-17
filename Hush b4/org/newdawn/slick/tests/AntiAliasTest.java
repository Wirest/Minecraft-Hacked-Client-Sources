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
import org.newdawn.slick.BasicGame;

public class AntiAliasTest extends BasicGame
{
    public AntiAliasTest() {
        super("AntiAlias Test");
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        container.getGraphics().setBackground(Color.green);
    }
    
    @Override
    public void update(final GameContainer container, final int delta) throws SlickException {
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) throws SlickException {
        g.setAntiAlias(true);
        g.setColor(Color.red);
        g.drawOval(100.0f, 100.0f, 100.0f, 100.0f);
        g.fillOval(300.0f, 100.0f, 100.0f, 100.0f);
        g.setAntiAlias(false);
        g.setColor(Color.red);
        g.drawOval(100.0f, 300.0f, 100.0f, 100.0f);
        g.fillOval(300.0f, 300.0f, 100.0f, 100.0f);
    }
    
    public static void main(final String[] argv) {
        try {
            final AppGameContainer container = new AppGameContainer(new AntiAliasTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

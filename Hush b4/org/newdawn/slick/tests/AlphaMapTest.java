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
import org.newdawn.slick.Image;
import org.newdawn.slick.BasicGame;

public class AlphaMapTest extends BasicGame
{
    private Image alphaMap;
    private Image textureMap;
    
    public AlphaMapTest() {
        super("AlphaMap Test");
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        this.alphaMap = new Image("testdata/alphamap.png");
        this.textureMap = new Image("testdata/grass.png");
        container.getGraphics().setBackground(Color.black);
    }
    
    @Override
    public void update(final GameContainer container, final int delta) throws SlickException {
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) throws SlickException {
        g.clearAlphaMap();
        g.setDrawMode(Graphics.MODE_NORMAL);
        this.textureMap.draw(10.0f, 50.0f);
        g.setColor(Color.red);
        g.fillRect(290.0f, 40.0f, 200.0f, 200.0f);
        g.setColor(Color.white);
        g.setDrawMode(Graphics.MODE_ALPHA_MAP);
        this.alphaMap.draw(300.0f, 50.0f);
        g.setDrawMode(Graphics.MODE_ALPHA_BLEND);
        this.textureMap.draw(300.0f, 50.0f);
        g.setDrawMode(Graphics.MODE_NORMAL);
    }
    
    @Override
    public void keyPressed(final int key, final char c) {
    }
    
    public static void main(final String[] argv) {
        try {
            final AppGameContainer container = new AppGameContainer(new AlphaMapTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

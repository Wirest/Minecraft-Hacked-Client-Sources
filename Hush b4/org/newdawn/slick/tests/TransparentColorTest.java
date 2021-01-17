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

public class TransparentColorTest extends BasicGame
{
    private Image image;
    private Image timage;
    
    public TransparentColorTest() {
        super("Transparent Color Test");
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        this.image = new Image("testdata/transtest.png");
        this.timage = new Image("testdata/transtest.png", new Color(94, 66, 41, 255));
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) {
        g.setBackground(Color.red);
        this.image.draw(10.0f, 10.0f);
        this.timage.draw(10.0f, 310.0f);
    }
    
    @Override
    public void update(final GameContainer container, final int delta) {
    }
    
    public static void main(final String[] argv) {
        try {
            final AppGameContainer container = new AppGameContainer(new TransparentColorTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void keyPressed(final int key, final char c) {
    }
}

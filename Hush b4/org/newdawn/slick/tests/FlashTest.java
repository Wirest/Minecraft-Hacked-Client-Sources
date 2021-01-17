// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.Game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.BasicGame;

public class FlashTest extends BasicGame
{
    private Image image;
    private boolean flash;
    private GameContainer container;
    
    public FlashTest() {
        super("Flash Test");
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        this.container = container;
        this.image = new Image("testdata/logo.tga");
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) {
        g.drawString("Press space to toggle", 10.0f, 50.0f);
        if (this.flash) {
            this.image.draw(100.0f, 100.0f);
        }
        else {
            this.image.drawFlash(100.0f, 100.0f, (float)this.image.getWidth(), (float)this.image.getHeight(), new Color(1.0f, 0.0f, 1.0f, 1.0f));
        }
    }
    
    @Override
    public void update(final GameContainer container, final int delta) {
    }
    
    public static void main(final String[] argv) {
        try {
            final AppGameContainer container = new AppGameContainer(new FlashTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void keyPressed(final int key, final char c) {
        if (key == 57) {
            this.flash = !this.flash;
        }
        if (key == 1) {
            this.container.exit();
        }
    }
}

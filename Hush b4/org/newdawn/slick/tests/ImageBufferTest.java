// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.Game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.BasicGame;

public class ImageBufferTest extends BasicGame
{
    private Image image;
    
    public ImageBufferTest() {
        super("Image Buffer Test");
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        final ImageBuffer buffer = new ImageBuffer(320, 200);
        for (int x = 0; x < 320; ++x) {
            for (int y = 0; y < 200; ++y) {
                if (y == 20) {
                    buffer.setRGBA(x, y, 255, 255, 255, 255);
                }
                else {
                    buffer.setRGBA(x, y, x, y, 0, 255);
                }
            }
        }
        this.image = buffer.getImage();
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) {
        this.image.draw(50.0f, 50.0f);
    }
    
    @Override
    public void update(final GameContainer container, final int delta) {
    }
    
    @Override
    public void keyPressed(final int key, final char c) {
        if (key == 1) {
            System.exit(0);
        }
    }
    
    public static void main(final String[] argv) {
        try {
            final AppGameContainer container = new AppGameContainer(new ImageBufferTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

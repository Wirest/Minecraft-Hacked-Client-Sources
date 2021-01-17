// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.Game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.BasicGame;

public class ImageCornerTest extends BasicGame
{
    private Image image;
    private Image[] images;
    private int width;
    private int height;
    
    public ImageCornerTest() {
        super("Image Corner Test");
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        this.image = new Image("testdata/logo.png");
        this.width = this.image.getWidth() / 3;
        this.height = this.image.getHeight() / 3;
        this.images = new Image[] { this.image.getSubImage(0, 0, this.width, this.height), this.image.getSubImage(this.width, 0, this.width, this.height), this.image.getSubImage(this.width * 2, 0, this.width, this.height), this.image.getSubImage(0, this.height, this.width, this.height), this.image.getSubImage(this.width, this.height, this.width, this.height), this.image.getSubImage(this.width * 2, this.height, this.width, this.height), this.image.getSubImage(0, this.height * 2, this.width, this.height), this.image.getSubImage(this.width, this.height * 2, this.width, this.height), this.image.getSubImage(this.width * 2, this.height * 2, this.width, this.height) };
        this.images[0].setColor(2, 0.0f, 1.0f, 1.0f, 1.0f);
        this.images[1].setColor(3, 0.0f, 1.0f, 1.0f, 1.0f);
        this.images[1].setColor(2, 0.0f, 1.0f, 1.0f, 1.0f);
        this.images[2].setColor(3, 0.0f, 1.0f, 1.0f, 1.0f);
        this.images[3].setColor(1, 0.0f, 1.0f, 1.0f, 1.0f);
        this.images[3].setColor(2, 0.0f, 1.0f, 1.0f, 1.0f);
        this.images[4].setColor(1, 0.0f, 1.0f, 1.0f, 1.0f);
        this.images[4].setColor(0, 0.0f, 1.0f, 1.0f, 1.0f);
        this.images[4].setColor(3, 0.0f, 1.0f, 1.0f, 1.0f);
        this.images[4].setColor(2, 0.0f, 1.0f, 1.0f, 1.0f);
        this.images[5].setColor(0, 0.0f, 1.0f, 1.0f, 1.0f);
        this.images[5].setColor(3, 0.0f, 1.0f, 1.0f, 1.0f);
        this.images[6].setColor(1, 0.0f, 1.0f, 1.0f, 1.0f);
        this.images[7].setColor(1, 0.0f, 1.0f, 1.0f, 1.0f);
        this.images[7].setColor(0, 0.0f, 1.0f, 1.0f, 1.0f);
        this.images[8].setColor(0, 0.0f, 1.0f, 1.0f, 1.0f);
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) {
        for (int x = 0; x < 3; ++x) {
            for (int y = 0; y < 3; ++y) {
                this.images[x + y * 3].draw((float)(100 + x * this.width), (float)(100 + y * this.height));
            }
        }
    }
    
    public static void main(final String[] argv) {
        final boolean sharedContextTest = false;
        try {
            final AppGameContainer container = new AppGameContainer(new ImageCornerTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void update(final GameContainer container, final int delta) throws SlickException {
    }
}

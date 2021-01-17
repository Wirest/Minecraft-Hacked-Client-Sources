// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.Game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.BasicGame;

public class ImageReadTest extends BasicGame
{
    private Image image;
    private Color[] read;
    private Graphics g;
    
    public ImageReadTest() {
        super("Image Read Test");
        this.read = new Color[6];
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        this.image = new Image("testdata/testcard.png");
        this.read[0] = this.image.getColor(0, 0);
        this.read[1] = this.image.getColor(30, 40);
        this.read[2] = this.image.getColor(55, 70);
        this.read[3] = this.image.getColor(80, 90);
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) {
        this.g = g;
        this.image.draw(100.0f, 100.0f);
        g.setColor(Color.white);
        g.drawString("Move mouse over test image", 200.0f, 20.0f);
        g.setColor(this.read[0]);
        g.drawString(this.read[0].toString(), 100.0f, 300.0f);
        g.setColor(this.read[1]);
        g.drawString(this.read[1].toString(), 150.0f, 320.0f);
        g.setColor(this.read[2]);
        g.drawString(this.read[2].toString(), 200.0f, 340.0f);
        g.setColor(this.read[3]);
        g.drawString(this.read[3].toString(), 250.0f, 360.0f);
        if (this.read[4] != null) {
            g.setColor(this.read[4]);
            g.drawString("On image: " + this.read[4].toString(), 100.0f, 250.0f);
        }
        if (this.read[5] != null) {
            g.setColor(Color.white);
            g.drawString("On screen: " + this.read[5].toString(), 100.0f, 270.0f);
        }
    }
    
    @Override
    public void update(final GameContainer container, final int delta) {
        final int mx = container.getInput().getMouseX();
        final int my = container.getInput().getMouseY();
        if (mx >= 100 && my >= 100 && mx < 200 && my < 200) {
            this.read[4] = this.image.getColor(mx - 100, my - 100);
        }
        else {
            this.read[4] = Color.black;
        }
        this.read[5] = this.g.getPixel(mx, my);
    }
    
    public static void main(final String[] argv) {
        try {
            final AppGameContainer container = new AppGameContainer(new ImageReadTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

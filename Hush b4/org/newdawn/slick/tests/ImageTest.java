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

public class ImageTest extends BasicGame
{
    private Image tga;
    private Image scaleMe;
    private Image scaled;
    private Image gif;
    private Image image;
    private Image subImage;
    private Image rotImage;
    private float rot;
    public static boolean exitMe;
    
    static {
        ImageTest.exitMe = true;
    }
    
    public ImageTest() {
        super("Image Test");
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        final Image image = new Image("testdata/logo.png");
        this.tga = image;
        this.image = image;
        this.rotImage = new Image("testdata/logo.png");
        this.rotImage = this.rotImage.getScaledCopy(this.rotImage.getWidth() / 2, this.rotImage.getHeight() / 2);
        this.scaleMe = new Image("testdata/logo.tga", true, 2);
        (this.gif = new Image("testdata/logo.gif")).destroy();
        this.gif = new Image("testdata/logo.gif");
        this.scaled = this.gif.getScaledCopy(120, 120);
        this.subImage = this.image.getSubImage(200, 0, 70, 260);
        this.rot = 0.0f;
        if (ImageTest.exitMe) {
            container.exit();
        }
        final Image test = this.tga.getSubImage(50, 50, 50, 50);
        System.out.println(test.getColor(50, 50));
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) {
        g.drawRect(0.0f, 0.0f, (float)this.image.getWidth(), (float)this.image.getHeight());
        this.image.draw(0.0f, 0.0f);
        this.image.draw(500.0f, 0.0f, 200.0f, 100.0f);
        this.scaleMe.draw(500.0f, 100.0f, 200.0f, 100.0f);
        this.scaled.draw(400.0f, 500.0f);
        final Image flipped = this.scaled.getFlippedCopy(true, false);
        flipped.draw(520.0f, 500.0f);
        final Image flipped2 = flipped.getFlippedCopy(false, true);
        flipped2.draw(520.0f, 380.0f);
        final Image flipped3 = flipped2.getFlippedCopy(true, false);
        flipped3.draw(400.0f, 380.0f);
        for (int i = 0; i < 3; ++i) {
            this.subImage.draw((float)(200 + i * 30), 300.0f);
        }
        g.translate(500.0f, 200.0f);
        g.rotate(50.0f, 50.0f, this.rot);
        g.scale(0.3f, 0.3f);
        this.image.draw();
        g.resetTransform();
        this.rotImage.setRotation(this.rot);
        this.rotImage.draw(100.0f, 200.0f);
    }
    
    @Override
    public void update(final GameContainer container, final int delta) {
        this.rot += delta * 0.1f;
        if (this.rot > 360.0f) {
            this.rot -= 360.0f;
        }
    }
    
    public static void main(final String[] argv) {
        final boolean sharedContextTest = false;
        try {
            ImageTest.exitMe = false;
            if (sharedContextTest) {
                GameContainer.enableSharedContext();
                ImageTest.exitMe = true;
            }
            AppGameContainer container = new AppGameContainer(new ImageTest());
            container.setForceExit(!sharedContextTest);
            container.setDisplayMode(800, 600, false);
            container.start();
            if (sharedContextTest) {
                System.out.println("Exit first instance");
                ImageTest.exitMe = false;
                container = new AppGameContainer(new ImageTest());
                container.setDisplayMode(800, 600, false);
                container.start();
            }
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void keyPressed(final int key, final char c) {
        if (key == 57) {
            if (this.image == this.gif) {
                this.image = this.tga;
            }
            else {
                this.image = this.gif;
            }
        }
    }
}

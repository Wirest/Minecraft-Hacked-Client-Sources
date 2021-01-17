// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.Game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.pbuffer.GraphicsFactory;
import org.newdawn.slick.Color;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.BasicGame;

public class ImageGraphicsTest extends BasicGame
{
    private Image preloaded;
    private Image target;
    private Image cut;
    private Graphics gTarget;
    private Graphics offscreenPreload;
    private Image testImage;
    private Font testFont;
    private float ang;
    private String using;
    
    public ImageGraphicsTest() {
        super("Image Graphics Test");
        this.using = "none";
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        this.testImage = new Image("testdata/logo.png");
        this.preloaded = new Image("testdata/logo.png");
        this.testFont = new AngelCodeFont("testdata/hiero.fnt", "testdata/hiero.png");
        this.target = new Image(400, 300);
        this.cut = new Image(100, 100);
        this.gTarget = this.target.getGraphics();
        (this.offscreenPreload = this.preloaded.getGraphics()).drawString("Drawing over a loaded image", 5.0f, 15.0f);
        this.offscreenPreload.setLineWidth(5.0f);
        this.offscreenPreload.setAntiAlias(true);
        this.offscreenPreload.setColor(Color.blue.brighter());
        this.offscreenPreload.drawOval(200.0f, 30.0f, 50.0f, 50.0f);
        this.offscreenPreload.setColor(Color.white);
        this.offscreenPreload.drawRect(190.0f, 20.0f, 70.0f, 70.0f);
        this.offscreenPreload.flush();
        if (GraphicsFactory.usingFBO()) {
            this.using = "FBO (Frame Buffer Objects)";
        }
        else if (GraphicsFactory.usingPBuffer()) {
            this.using = "Pbuffer (Pixel Buffers)";
        }
        System.out.println(this.preloaded.getColor(50, 50));
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) throws SlickException {
        this.gTarget.setBackground(new Color(0, 0, 0, 0));
        this.gTarget.clear();
        this.gTarget.rotate(200.0f, 160.0f, this.ang);
        this.gTarget.setFont(this.testFont);
        this.gTarget.fillRect(10.0f, 10.0f, 50.0f, 50.0f);
        this.gTarget.drawString("HELLO WORLD", 10.0f, 10.0f);
        this.gTarget.drawImage(this.testImage, 100.0f, 150.0f);
        this.gTarget.drawImage(this.testImage, 100.0f, 50.0f);
        this.gTarget.drawImage(this.testImage, 50.0f, 75.0f);
        this.gTarget.flush();
        g.setColor(Color.red);
        g.fillRect(250.0f, 50.0f, 200.0f, 200.0f);
        this.target.draw(300.0f, 100.0f);
        this.target.draw(300.0f, 410.0f, 200.0f, 150.0f);
        this.target.draw(505.0f, 410.0f, 100.0f, 75.0f);
        g.setColor(Color.white);
        g.drawString("Testing On Offscreen Buffer", 300.0f, 80.0f);
        g.setColor(Color.green);
        g.drawRect(300.0f, 100.0f, (float)this.target.getWidth(), (float)this.target.getHeight());
        g.drawRect(300.0f, 410.0f, (float)(this.target.getWidth() / 2), (float)(this.target.getHeight() / 2));
        g.drawRect(505.0f, 410.0f, (float)(this.target.getWidth() / 4), (float)(this.target.getHeight() / 4));
        g.setColor(Color.white);
        g.drawString("Testing Font On Back Buffer", 10.0f, 100.0f);
        g.drawString("Using: " + this.using, 10.0f, 580.0f);
        g.setColor(Color.red);
        g.fillRect(10.0f, 120.0f, 200.0f, 5.0f);
        final int xp = (int)(60.0 + Math.sin(this.ang / 60.0f) * 50.0);
        g.copyArea(this.cut, xp, 50);
        this.cut.draw(30.0f, 250.0f);
        g.setColor(Color.white);
        g.drawRect(30.0f, 250.0f, (float)this.cut.getWidth(), (float)this.cut.getHeight());
        g.setColor(Color.gray);
        g.drawRect((float)xp, 50.0f, (float)this.cut.getWidth(), (float)this.cut.getHeight());
        this.preloaded.draw(2.0f, 400.0f);
        g.setColor(Color.blue);
        g.drawRect(2.0f, 400.0f, (float)this.preloaded.getWidth(), (float)this.preloaded.getHeight());
    }
    
    @Override
    public void update(final GameContainer container, final int delta) {
        this.ang += delta * 0.1f;
    }
    
    public static void main(final String[] argv) {
        try {
            GraphicsFactory.setUseFBO(false);
            final AppGameContainer container = new AppGameContainer(new ImageGraphicsTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

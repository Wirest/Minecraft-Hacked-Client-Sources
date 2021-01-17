// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.Game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.FastTrig;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.Image;
import org.newdawn.slick.BasicGame;

public class GraphicsTest extends BasicGame
{
    private boolean clip;
    private float ang;
    private Image image;
    private Polygon poly;
    private GameContainer container;
    
    public GraphicsTest() {
        super("Graphics Test");
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        this.container = container;
        this.image = new Image("testdata/logo.tga", true);
        final Image temp = new Image("testdata/palette_tool.png");
        container.setMouseCursor(temp, 0, 0);
        container.setIcons(new String[] { "testdata/icon.tga" });
        container.setTargetFrameRate(100);
        this.poly = new Polygon();
        float len = 100.0f;
        for (int x = 0; x < 360; x += 30) {
            if (len == 100.0f) {
                len = 50.0f;
            }
            else {
                len = 100.0f;
            }
            this.poly.addPoint((float)FastTrig.cos(Math.toRadians(x)) * len, (float)FastTrig.sin(Math.toRadians(x)) * len);
        }
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) throws SlickException {
        g.setColor(Color.white);
        g.setAntiAlias(true);
        for (int x = 0; x < 360; x += 10) {
            g.drawLine(700.0f, 100.0f, (float)(int)(700.0 + Math.cos(Math.toRadians(x)) * 100.0), (float)(int)(100.0 + Math.sin(Math.toRadians(x)) * 100.0));
        }
        g.setAntiAlias(false);
        g.setColor(Color.yellow);
        g.drawString("The Graphics Test!", 300.0f, 50.0f);
        g.setColor(Color.white);
        g.drawString("Space - Toggles clipping", 400.0f, 80.0f);
        g.drawString("Frame rate capped to 100", 400.0f, 120.0f);
        if (this.clip) {
            g.setColor(Color.gray);
            g.drawRect(100.0f, 260.0f, 400.0f, 100.0f);
            g.setClip(100, 260, 400, 100);
        }
        g.setColor(Color.yellow);
        g.translate(100.0f, 120.0f);
        g.fill(this.poly);
        g.setColor(Color.blue);
        g.setLineWidth(3.0f);
        g.draw(this.poly);
        g.setLineWidth(1.0f);
        g.translate(0.0f, 230.0f);
        g.draw(this.poly);
        g.resetTransform();
        g.setColor(Color.magenta);
        g.drawRoundRect(10.0f, 10.0f, 100.0f, 100.0f, 10);
        g.fillRoundRect(10.0f, 210.0f, 100.0f, 100.0f, 10);
        g.rotate(400.0f, 300.0f, this.ang);
        g.setColor(Color.green);
        g.drawRect(200.0f, 200.0f, 200.0f, 200.0f);
        g.setColor(Color.blue);
        g.fillRect(250.0f, 250.0f, 100.0f, 100.0f);
        g.drawImage(this.image, 300.0f, 270.0f);
        g.setColor(Color.red);
        g.drawOval(100.0f, 100.0f, 200.0f, 200.0f);
        g.setColor(Color.red.darker());
        g.fillOval(300.0f, 300.0f, 150.0f, 100.0f);
        g.setAntiAlias(true);
        g.setColor(Color.white);
        g.setLineWidth(5.0f);
        g.drawOval(300.0f, 300.0f, 150.0f, 100.0f);
        g.setAntiAlias(true);
        g.resetTransform();
        if (this.clip) {
            g.clearClip();
        }
    }
    
    @Override
    public void update(final GameContainer container, final int delta) {
        this.ang += delta * 0.1f;
    }
    
    @Override
    public void keyPressed(final int key, final char c) {
        if (key == 1) {
            System.exit(0);
        }
        if (key == 57) {
            this.clip = !this.clip;
        }
    }
    
    public static void main(final String[] argv) {
        try {
            final AppGameContainer container = new AppGameContainer(new GraphicsTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

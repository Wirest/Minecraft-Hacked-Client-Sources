// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.Game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.BasicGame;

public class GradientTest extends BasicGame
{
    private GameContainer container;
    private GradientFill gradient;
    private GradientFill gradient2;
    private GradientFill gradient4;
    private Rectangle rect;
    private Rectangle center;
    private RoundedRectangle round;
    private RoundedRectangle round2;
    private Polygon poly;
    private float ang;
    
    public GradientTest() {
        super("Gradient Test");
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        this.container = container;
        this.rect = new Rectangle(400.0f, 100.0f, 200.0f, 150.0f);
        this.round = new RoundedRectangle(150.0f, 100.0f, 200.0f, 150.0f, 50.0f);
        this.round2 = new RoundedRectangle(150.0f, 300.0f, 200.0f, 150.0f, 50.0f);
        this.center = new Rectangle(350.0f, 250.0f, 100.0f, 100.0f);
        (this.poly = new Polygon()).addPoint(400.0f, 350.0f);
        this.poly.addPoint(550.0f, 320.0f);
        this.poly.addPoint(600.0f, 380.0f);
        this.poly.addPoint(620.0f, 450.0f);
        this.poly.addPoint(500.0f, 450.0f);
        this.gradient = new GradientFill(0.0f, -75.0f, Color.red, 0.0f, 75.0f, Color.yellow, true);
        this.gradient2 = new GradientFill(0.0f, -75.0f, Color.blue, 0.0f, 75.0f, Color.white, true);
        this.gradient4 = new GradientFill(-50.0f, -40.0f, Color.green, 50.0f, 40.0f, Color.cyan, true);
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) {
        g.rotate(400.0f, 300.0f, this.ang);
        g.fill(this.rect, this.gradient);
        g.fill(this.round, this.gradient);
        g.fill(this.poly, this.gradient2);
        g.fill(this.center, this.gradient4);
        g.setAntiAlias(true);
        g.setLineWidth(10.0f);
        g.draw(this.round2, this.gradient2);
        g.setLineWidth(2.0f);
        g.draw(this.poly, this.gradient);
        g.setAntiAlias(false);
        g.fill(this.center, this.gradient4);
        g.setAntiAlias(true);
        g.setColor(Color.black);
        g.draw(this.center);
        g.setAntiAlias(false);
    }
    
    @Override
    public void update(final GameContainer container, final int delta) {
        this.ang += delta * 0.01f;
    }
    
    public static void main(final String[] argv) {
        try {
            Renderer.setRenderer(2);
            final AppGameContainer container = new AppGameContainer(new GradientTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void keyPressed(final int key, final char c) {
        if (key == 1) {
            this.container.exit();
        }
    }
}

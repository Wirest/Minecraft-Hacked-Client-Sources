// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.Game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.geom.Curve;
import org.newdawn.slick.BasicGame;

public class CurveTest extends BasicGame
{
    private Curve curve;
    private Vector2f p1;
    private Vector2f c1;
    private Vector2f c2;
    private Vector2f p2;
    private Polygon poly;
    
    public CurveTest() {
        super("Curve Test");
        this.p1 = new Vector2f(100.0f, 300.0f);
        this.c1 = new Vector2f(100.0f, 100.0f);
        this.c2 = new Vector2f(300.0f, 100.0f);
        this.p2 = new Vector2f(300.0f, 300.0f);
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        container.getGraphics().setBackground(Color.white);
        this.curve = new Curve(this.p2, this.c2, this.c1, this.p1);
        (this.poly = new Polygon()).addPoint(500.0f, 200.0f);
        this.poly.addPoint(600.0f, 200.0f);
        this.poly.addPoint(700.0f, 300.0f);
        this.poly.addPoint(400.0f, 300.0f);
    }
    
    @Override
    public void update(final GameContainer container, final int delta) throws SlickException {
    }
    
    private void drawMarker(final Graphics g, final Vector2f p) {
        g.drawRect(p.x - 5.0f, p.y - 5.0f, 10.0f, 10.0f);
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) throws SlickException {
        g.setColor(Color.gray);
        this.drawMarker(g, this.p1);
        this.drawMarker(g, this.p2);
        g.setColor(Color.red);
        this.drawMarker(g, this.c1);
        this.drawMarker(g, this.c2);
        g.setColor(Color.black);
        g.draw(this.curve);
        g.fill(this.curve);
        g.draw(this.poly);
        g.fill(this.poly);
    }
    
    public static void main(final String[] argv) {
        try {
            final AppGameContainer container = new AppGameContainer(new CurveTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.Game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.geom.ShapeRenderer;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.TexCoordGenerator;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.BasicGame;

public class TexturePaintTest extends BasicGame
{
    private Polygon poly;
    private Image image;
    private Rectangle texRect;
    private TexCoordGenerator texPaint;
    
    public TexturePaintTest() {
        super("Texture Paint Test");
        this.poly = new Polygon();
        this.texRect = new Rectangle(50.0f, 50.0f, 100.0f, 100.0f);
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        this.poly.addPoint(120.0f, 120.0f);
        this.poly.addPoint(420.0f, 100.0f);
        this.poly.addPoint(620.0f, 420.0f);
        this.poly.addPoint(300.0f, 320.0f);
        this.image = new Image("testdata/rocks.png");
        this.texPaint = new TexCoordGenerator() {
            @Override
            public Vector2f getCoordFor(final float x, final float y) {
                final float tx = (TexturePaintTest.this.texRect.getX() - x) / TexturePaintTest.this.texRect.getWidth();
                final float ty = (TexturePaintTest.this.texRect.getY() - y) / TexturePaintTest.this.texRect.getHeight();
                return new Vector2f(tx, ty);
            }
        };
    }
    
    @Override
    public void update(final GameContainer container, final int delta) throws SlickException {
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) throws SlickException {
        g.setColor(Color.white);
        g.texture(this.poly, this.image);
        ShapeRenderer.texture(this.poly, this.image, this.texPaint);
    }
    
    public static void main(final String[] argv) {
        try {
            final AppGameContainer container = new AppGameContainer(new TexturePaintTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

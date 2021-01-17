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
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.BasicGame;

public class LameTest extends BasicGame
{
    private Polygon poly;
    private Image image;
    
    public LameTest() {
        super("Lame Test");
        this.poly = new Polygon();
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        this.poly.addPoint(100.0f, 100.0f);
        this.poly.addPoint(120.0f, 100.0f);
        this.poly.addPoint(120.0f, 120.0f);
        this.poly.addPoint(100.0f, 120.0f);
        this.image = new Image("testdata/rocks.png");
    }
    
    @Override
    public void update(final GameContainer container, final int delta) throws SlickException {
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) throws SlickException {
        g.setColor(Color.white);
        g.texture(this.poly, this.image);
    }
    
    public static void main(final String[] argv) {
        try {
            final AppGameContainer container = new AppGameContainer(new LameTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

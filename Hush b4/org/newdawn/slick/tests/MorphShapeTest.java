// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.Game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.MorphShape;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.BasicGame;

public class MorphShapeTest extends BasicGame
{
    private Shape a;
    private Shape b;
    private Shape c;
    private MorphShape morph;
    private float time;
    
    public MorphShapeTest() {
        super("MorphShapeTest");
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        this.a = new Rectangle(100.0f, 100.0f, 50.0f, 200.0f);
        this.a = this.a.transform(Transform.createRotateTransform(0.1f, 100.0f, 100.0f));
        this.b = new Rectangle(200.0f, 100.0f, 50.0f, 200.0f);
        this.b = this.b.transform(Transform.createRotateTransform(-0.6f, 100.0f, 100.0f));
        this.c = new Rectangle(300.0f, 100.0f, 50.0f, 200.0f);
        this.c = this.c.transform(Transform.createRotateTransform(-0.2f, 100.0f, 100.0f));
        (this.morph = new MorphShape(this.a)).addShape(this.b);
        this.morph.addShape(this.c);
        container.setVSync(true);
    }
    
    @Override
    public void update(final GameContainer container, final int delta) throws SlickException {
        this.time += delta * 0.001f;
        this.morph.setMorphTime(this.time);
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) throws SlickException {
        g.setColor(Color.green);
        g.draw(this.a);
        g.setColor(Color.red);
        g.draw(this.b);
        g.setColor(Color.blue);
        g.draw(this.c);
        g.setColor(Color.white);
        g.draw(this.morph);
    }
    
    public static void main(final String[] argv) {
        try {
            final AppGameContainer container = new AppGameContainer(new MorphShapeTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

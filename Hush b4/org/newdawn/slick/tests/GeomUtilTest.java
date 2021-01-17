// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.Game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.GeomUtil;
import java.util.ArrayList;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.GeomUtilListener;
import org.newdawn.slick.BasicGame;

public class GeomUtilTest extends BasicGame implements GeomUtilListener
{
    private Shape source;
    private Shape cut;
    private Shape[] result;
    private ArrayList points;
    private ArrayList marks;
    private ArrayList exclude;
    private boolean dynamic;
    private GeomUtil util;
    private int xp;
    private int yp;
    private Circle circle;
    private Shape rect;
    private Polygon star;
    private boolean union;
    
    public GeomUtilTest() {
        super("GeomUtilTest");
        this.points = new ArrayList();
        this.marks = new ArrayList();
        this.exclude = new ArrayList();
        this.util = new GeomUtil();
    }
    
    public void init() {
        final Polygon source = new Polygon();
        source.addPoint(100.0f, 100.0f);
        source.addPoint(150.0f, 80.0f);
        source.addPoint(210.0f, 120.0f);
        source.addPoint(340.0f, 150.0f);
        source.addPoint(150.0f, 200.0f);
        source.addPoint(120.0f, 250.0f);
        this.source = source;
        this.circle = new Circle(0.0f, 0.0f, 50.0f);
        this.rect = new Rectangle(-100.0f, -40.0f, 200.0f, 80.0f);
        this.star = new Polygon();
        float dis = 40.0f;
        for (int i = 0; i < 360; i += 30) {
            dis = (float)((dis == 40.0f) ? 60 : 40);
            final double x = Math.cos(Math.toRadians(i)) * dis;
            final double y = Math.sin(Math.toRadians(i)) * dis;
            this.star.addPoint((float)x, (float)y);
        }
        (this.cut = this.circle).setLocation(203.0f, 78.0f);
        this.xp = (int)this.cut.getCenterX();
        this.yp = (int)this.cut.getCenterY();
        this.makeBoolean();
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        this.util.setListener(this);
        this.init();
        container.setVSync(true);
    }
    
    @Override
    public void update(final GameContainer container, final int delta) throws SlickException {
        if (container.getInput().isKeyPressed(57)) {
            this.dynamic = !this.dynamic;
        }
        if (container.getInput().isKeyPressed(28)) {
            this.union = !this.union;
            this.makeBoolean();
        }
        if (container.getInput().isKeyPressed(2)) {
            this.cut = this.circle;
            this.circle.setCenterX((float)this.xp);
            this.circle.setCenterY((float)this.yp);
            this.makeBoolean();
        }
        if (container.getInput().isKeyPressed(3)) {
            this.cut = this.rect;
            this.rect.setCenterX((float)this.xp);
            this.rect.setCenterY((float)this.yp);
            this.makeBoolean();
        }
        if (container.getInput().isKeyPressed(4)) {
            this.cut = this.star;
            this.star.setCenterX((float)this.xp);
            this.star.setCenterY((float)this.yp);
            this.makeBoolean();
        }
        if (this.dynamic) {
            this.xp = container.getInput().getMouseX();
            this.yp = container.getInput().getMouseY();
            this.makeBoolean();
        }
    }
    
    private void makeBoolean() {
        this.marks.clear();
        this.points.clear();
        this.exclude.clear();
        this.cut.setCenterX((float)this.xp);
        this.cut.setCenterY((float)this.yp);
        if (this.union) {
            this.result = this.util.union(this.source, this.cut);
        }
        else {
            this.result = this.util.subtract(this.source, this.cut);
        }
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) throws SlickException {
        g.drawString("Space - toggle movement of cutting shape", 530.0f, 10.0f);
        g.drawString("1,2,3 - select cutting shape", 530.0f, 30.0f);
        g.drawString("Mouse wheel - rotate shape", 530.0f, 50.0f);
        g.drawString("Enter - toggle union/subtract", 530.0f, 70.0f);
        g.drawString("MODE: " + (this.union ? "Union" : "Cut"), 530.0f, 200.0f);
        g.setColor(Color.green);
        g.draw(this.source);
        g.setColor(Color.red);
        g.draw(this.cut);
        g.setColor(Color.white);
        for (int i = 0; i < this.exclude.size(); ++i) {
            final Vector2f pt = this.exclude.get(i);
            g.drawOval(pt.x - 3.0f, pt.y - 3.0f, 7.0f, 7.0f);
        }
        g.setColor(Color.yellow);
        for (int i = 0; i < this.points.size(); ++i) {
            final Vector2f pt = this.points.get(i);
            g.fillOval(pt.x - 1.0f, pt.y - 1.0f, 3.0f, 3.0f);
        }
        g.setColor(Color.white);
        for (int i = 0; i < this.marks.size(); ++i) {
            final Vector2f pt = this.marks.get(i);
            g.fillOval(pt.x - 1.0f, pt.y - 1.0f, 3.0f, 3.0f);
        }
        g.translate(0.0f, 300.0f);
        g.setColor(Color.white);
        if (this.result != null) {
            for (int i = 0; i < this.result.length; ++i) {
                g.draw(this.result[i]);
            }
            g.drawString("Polys:" + this.result.length, 10.0f, 100.0f);
            g.drawString("X:" + this.xp, 10.0f, 120.0f);
            g.drawString("Y:" + this.yp, 10.0f, 130.0f);
        }
    }
    
    public static void main(final String[] argv) {
        try {
            final AppGameContainer container = new AppGameContainer(new GeomUtilTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void pointExcluded(final float x, final float y) {
        this.exclude.add(new Vector2f(x, y));
    }
    
    @Override
    public void pointIntersected(final float x, final float y) {
        this.marks.add(new Vector2f(x, y));
    }
    
    @Override
    public void pointUsed(final float x, final float y) {
        this.points.add(new Vector2f(x, y));
    }
    
    @Override
    public void mouseWheelMoved(final int change) {
        if (this.dynamic) {
            if (change < 0) {
                this.cut = this.cut.transform(Transform.createRotateTransform((float)Math.toRadians(10.0), this.cut.getCenterX(), this.cut.getCenterY()));
            }
            else {
                this.cut = this.cut.transform(Transform.createRotateTransform((float)Math.toRadians(-10.0), this.cut.getCenterX(), this.cut.getCenterY()));
            }
        }
    }
}

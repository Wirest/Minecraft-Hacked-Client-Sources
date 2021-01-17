// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.Game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;
import java.util.ArrayList;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.BasicGame;

public class ShapeTest extends BasicGame
{
    private Rectangle rect;
    private RoundedRectangle roundRect;
    private Ellipse ellipse;
    private Circle circle;
    private Polygon polygon;
    private ArrayList shapes;
    private boolean[] keys;
    private char[] lastChar;
    private Polygon randomShape;
    
    public ShapeTest() {
        super("Geom Test");
        this.randomShape = new Polygon();
    }
    
    public void createPoly(final float x, final float y) {
        final int size = 20;
        final int change = 10;
        (this.randomShape = new Polygon()).addPoint((float)(0 + (int)(Math.random() * change)), (float)(0 + (int)(Math.random() * change)));
        this.randomShape.addPoint((float)(size - (int)(Math.random() * change)), (float)(0 + (int)(Math.random() * change)));
        this.randomShape.addPoint((float)(size - (int)(Math.random() * change)), (float)(size - (int)(Math.random() * change)));
        this.randomShape.addPoint((float)(0 + (int)(Math.random() * change)), (float)(size - (int)(Math.random() * change)));
        this.randomShape.setCenterX(x);
        this.randomShape.setCenterY(y);
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        this.shapes = new ArrayList();
        this.rect = new Rectangle(10.0f, 10.0f, 100.0f, 80.0f);
        this.shapes.add(this.rect);
        this.roundRect = new RoundedRectangle(150.0f, 10.0f, 60.0f, 80.0f, 20.0f);
        this.shapes.add(this.roundRect);
        this.ellipse = new Ellipse(350.0f, 40.0f, 50.0f, 30.0f);
        this.shapes.add(this.ellipse);
        this.circle = new Circle(470.0f, 60.0f, 50.0f);
        this.shapes.add(this.circle);
        this.polygon = new Polygon(new float[] { 550.0f, 10.0f, 600.0f, 40.0f, 620.0f, 100.0f, 570.0f, 130.0f });
        this.shapes.add(this.polygon);
        this.keys = new boolean[256];
        this.lastChar = new char[256];
        this.createPoly(200.0f, 200.0f);
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) {
        g.setColor(Color.green);
        for (int i = 0; i < this.shapes.size(); ++i) {
            g.fill(this.shapes.get(i));
        }
        g.fill(this.randomShape);
        g.setColor(Color.black);
        g.setAntiAlias(true);
        g.draw(this.randomShape);
        g.setAntiAlias(false);
        g.setColor(Color.white);
        g.drawString("keys", 10.0f, 300.0f);
        g.drawString("wasd - move rectangle", 10.0f, 315.0f);
        g.drawString("WASD - resize rectangle", 10.0f, 330.0f);
        g.drawString("tgfh - move rounded rectangle", 10.0f, 345.0f);
        g.drawString("TGFH - resize rounded rectangle", 10.0f, 360.0f);
        g.drawString("ry - resize corner radius on rounded rectangle", 10.0f, 375.0f);
        g.drawString("ikjl - move ellipse", 10.0f, 390.0f);
        g.drawString("IKJL - resize ellipse", 10.0f, 405.0f);
        g.drawString("Arrows - move circle", 10.0f, 420.0f);
        g.drawString("Page Up/Page Down - resize circle", 10.0f, 435.0f);
        g.drawString("numpad 8546 - move polygon", 10.0f, 450.0f);
    }
    
    @Override
    public void update(final GameContainer container, final int delta) {
        this.createPoly(200.0f, 200.0f);
        if (this.keys[1]) {
            System.exit(0);
        }
        if (this.keys[17]) {
            if (this.lastChar[17] == 'w') {
                this.rect.setY(this.rect.getY() - 1.0f);
            }
            else {
                this.rect.setHeight(this.rect.getHeight() - 1.0f);
            }
        }
        if (this.keys[31]) {
            if (this.lastChar[31] == 's') {
                this.rect.setY(this.rect.getY() + 1.0f);
            }
            else {
                this.rect.setHeight(this.rect.getHeight() + 1.0f);
            }
        }
        if (this.keys[30]) {
            if (this.lastChar[30] == 'a') {
                this.rect.setX(this.rect.getX() - 1.0f);
            }
            else {
                this.rect.setWidth(this.rect.getWidth() - 1.0f);
            }
        }
        if (this.keys[32]) {
            if (this.lastChar[32] == 'd') {
                this.rect.setX(this.rect.getX() + 1.0f);
            }
            else {
                this.rect.setWidth(this.rect.getWidth() + 1.0f);
            }
        }
        if (this.keys[20]) {
            if (this.lastChar[20] == 't') {
                this.roundRect.setY(this.roundRect.getY() - 1.0f);
            }
            else {
                this.roundRect.setHeight(this.roundRect.getHeight() - 1.0f);
            }
        }
        if (this.keys[34]) {
            if (this.lastChar[34] == 'g') {
                this.roundRect.setY(this.roundRect.getY() + 1.0f);
            }
            else {
                this.roundRect.setHeight(this.roundRect.getHeight() + 1.0f);
            }
        }
        if (this.keys[33]) {
            if (this.lastChar[33] == 'f') {
                this.roundRect.setX(this.roundRect.getX() - 1.0f);
            }
            else {
                this.roundRect.setWidth(this.roundRect.getWidth() - 1.0f);
            }
        }
        if (this.keys[35]) {
            if (this.lastChar[35] == 'h') {
                this.roundRect.setX(this.roundRect.getX() + 1.0f);
            }
            else {
                this.roundRect.setWidth(this.roundRect.getWidth() + 1.0f);
            }
        }
        if (this.keys[19]) {
            this.roundRect.setCornerRadius(this.roundRect.getCornerRadius() - 1.0f);
        }
        if (this.keys[21]) {
            this.roundRect.setCornerRadius(this.roundRect.getCornerRadius() + 1.0f);
        }
        if (this.keys[23]) {
            if (this.lastChar[23] == 'i') {
                this.ellipse.setCenterY(this.ellipse.getCenterY() - 1.0f);
            }
            else {
                this.ellipse.setRadius2(this.ellipse.getRadius2() - 1.0f);
            }
        }
        if (this.keys[37]) {
            if (this.lastChar[37] == 'k') {
                this.ellipse.setCenterY(this.ellipse.getCenterY() + 1.0f);
            }
            else {
                this.ellipse.setRadius2(this.ellipse.getRadius2() + 1.0f);
            }
        }
        if (this.keys[36]) {
            if (this.lastChar[36] == 'j') {
                this.ellipse.setCenterX(this.ellipse.getCenterX() - 1.0f);
            }
            else {
                this.ellipse.setRadius1(this.ellipse.getRadius1() - 1.0f);
            }
        }
        if (this.keys[38]) {
            if (this.lastChar[38] == 'l') {
                this.ellipse.setCenterX(this.ellipse.getCenterX() + 1.0f);
            }
            else {
                this.ellipse.setRadius1(this.ellipse.getRadius1() + 1.0f);
            }
        }
        if (this.keys[200]) {
            this.circle.setCenterY(this.circle.getCenterY() - 1.0f);
        }
        if (this.keys[208]) {
            this.circle.setCenterY(this.circle.getCenterY() + 1.0f);
        }
        if (this.keys[203]) {
            this.circle.setCenterX(this.circle.getCenterX() - 1.0f);
        }
        if (this.keys[205]) {
            this.circle.setCenterX(this.circle.getCenterX() + 1.0f);
        }
        if (this.keys[201]) {
            this.circle.setRadius(this.circle.getRadius() - 1.0f);
        }
        if (this.keys[209]) {
            this.circle.setRadius(this.circle.getRadius() + 1.0f);
        }
        if (this.keys[72]) {
            this.polygon.setY(this.polygon.getY() - 1.0f);
        }
        if (this.keys[76]) {
            this.polygon.setY(this.polygon.getY() + 1.0f);
        }
        if (this.keys[75]) {
            this.polygon.setX(this.polygon.getX() - 1.0f);
        }
        if (this.keys[77]) {
            this.polygon.setX(this.polygon.getX() + 1.0f);
        }
    }
    
    @Override
    public void keyPressed(final int key, final char c) {
        this.keys[key] = true;
        this.lastChar[key] = c;
    }
    
    @Override
    public void keyReleased(final int key, final char c) {
        this.keys[key] = false;
    }
    
    public static void main(final String[] argv) {
        try {
            Renderer.setRenderer(2);
            final AppGameContainer container = new AppGameContainer(new ShapeTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

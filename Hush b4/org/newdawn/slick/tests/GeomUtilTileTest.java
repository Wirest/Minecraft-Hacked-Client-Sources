// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.Game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;
import java.util.Collection;
import java.util.HashSet;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Polygon;
import java.util.ArrayList;
import org.newdawn.slick.geom.GeomUtil;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.GeomUtilListener;
import org.newdawn.slick.BasicGame;

public class GeomUtilTileTest extends BasicGame implements GeomUtilListener
{
    private Shape source;
    private Shape cut;
    private Shape[] result;
    private GeomUtil util;
    private ArrayList original;
    private ArrayList combined;
    private ArrayList intersections;
    private ArrayList used;
    private ArrayList[][] quadSpace;
    private Shape[][] quadSpaceShapes;
    
    public GeomUtilTileTest() {
        super("GeomUtilTileTest");
        this.util = new GeomUtil();
        this.original = new ArrayList();
        this.combined = new ArrayList();
        this.intersections = new ArrayList();
        this.used = new ArrayList();
    }
    
    private void generateSpace(final ArrayList shapes, final float minx, final float miny, final float maxx, final float maxy, final int segments) {
        this.quadSpace = new ArrayList[segments][segments];
        this.quadSpaceShapes = new Shape[segments][segments];
        final float dx = (maxx - minx) / segments;
        final float dy = (maxy - miny) / segments;
        for (int x = 0; x < segments; ++x) {
            for (int y = 0; y < segments; ++y) {
                this.quadSpace[x][y] = new ArrayList();
                final Polygon segmentPolygon = new Polygon();
                segmentPolygon.addPoint(minx + dx * x, miny + dy * y);
                segmentPolygon.addPoint(minx + dx * x + dx, miny + dy * y);
                segmentPolygon.addPoint(minx + dx * x + dx, miny + dy * y + dy);
                segmentPolygon.addPoint(minx + dx * x, miny + dy * y + dy);
                for (int i = 0; i < shapes.size(); ++i) {
                    final Shape shape = shapes.get(i);
                    if (this.collides(shape, segmentPolygon)) {
                        this.quadSpace[x][y].add(shape);
                    }
                }
                this.quadSpaceShapes[x][y] = segmentPolygon;
            }
        }
    }
    
    private void removeFromQuadSpace(final Shape shape) {
        for (int segments = this.quadSpace.length, x = 0; x < segments; ++x) {
            for (int y = 0; y < segments; ++y) {
                this.quadSpace[x][y].remove(shape);
            }
        }
    }
    
    private void addToQuadSpace(final Shape shape) {
        for (int segments = this.quadSpace.length, x = 0; x < segments; ++x) {
            for (int y = 0; y < segments; ++y) {
                if (this.collides(shape, this.quadSpaceShapes[x][y])) {
                    this.quadSpace[x][y].add(shape);
                }
            }
        }
    }
    
    public void init() {
        final int size = 10;
        final int[][] map = { { 0, 0, 0, 0, 0, 0, 0, 3, 0, 0 }, { 0, 1, 1, 1, 0, 0, 1, 1, 1, 0 }, { 0, 1, 1, 0, 0, 0, 5, 1, 6, 0 }, { 0, 1, 2, 0, 0, 0, 4, 1, 1, 0 }, { 0, 1, 1, 0, 0, 0, 1, 1, 0, 0 }, { 0, 0, 0, 0, 3, 0, 1, 1, 0, 0 }, { 0, 0, 0, 1, 1, 0, 0, 0, 1, 0 }, { 0, 0, 0, 1, 1, 0, 0, 0, 0, 0 }, new int[10], new int[10] };
        for (int x = 0; x < map[0].length; ++x) {
            for (int y = 0; y < map.length; ++y) {
                if (map[y][x] != 0) {
                    switch (map[y][x]) {
                        case 1: {
                            final Polygon p2 = new Polygon();
                            p2.addPoint((float)(x * 32), (float)(y * 32));
                            p2.addPoint((float)(x * 32 + 32), (float)(y * 32));
                            p2.addPoint((float)(x * 32 + 32), (float)(y * 32 + 32));
                            p2.addPoint((float)(x * 32), (float)(y * 32 + 32));
                            this.original.add(p2);
                            break;
                        }
                        case 2: {
                            final Polygon poly = new Polygon();
                            poly.addPoint((float)(x * 32), (float)(y * 32));
                            poly.addPoint((float)(x * 32 + 32), (float)(y * 32));
                            poly.addPoint((float)(x * 32), (float)(y * 32 + 32));
                            this.original.add(poly);
                            break;
                        }
                        case 3: {
                            final Circle ellipse = new Circle((float)(x * 32 + 16), (float)(y * 32 + 32), 16.0f, 16);
                            this.original.add(ellipse);
                            break;
                        }
                        case 4: {
                            final Polygon p3 = new Polygon();
                            p3.addPoint((float)(x * 32 + 32), (float)(y * 32));
                            p3.addPoint((float)(x * 32 + 32), (float)(y * 32 + 32));
                            p3.addPoint((float)(x * 32), (float)(y * 32 + 32));
                            this.original.add(p3);
                            break;
                        }
                        case 5: {
                            final Polygon p4 = new Polygon();
                            p4.addPoint((float)(x * 32), (float)(y * 32));
                            p4.addPoint((float)(x * 32 + 32), (float)(y * 32));
                            p4.addPoint((float)(x * 32 + 32), (float)(y * 32 + 32));
                            this.original.add(p4);
                            break;
                        }
                        case 6: {
                            final Polygon p5 = new Polygon();
                            p5.addPoint((float)(x * 32), (float)(y * 32));
                            p5.addPoint((float)(x * 32 + 32), (float)(y * 32));
                            p5.addPoint((float)(x * 32), (float)(y * 32 + 32));
                            this.original.add(p5);
                            break;
                        }
                    }
                }
            }
        }
        final long before = System.currentTimeMillis();
        this.generateSpace(this.original, 0.0f, 0.0f, (float)((size + 1) * 32), (float)((size + 1) * 32), 8);
        this.combined = this.combineQuadSpace();
        final long after = System.currentTimeMillis();
        System.out.println("Combine took: " + (after - before));
        System.out.println("Combine result: " + this.combined.size());
    }
    
    private ArrayList combineQuadSpace() {
        boolean updated = true;
        while (updated) {
            updated = false;
            for (int x = 0; x < this.quadSpace.length; ++x) {
                for (int y = 0; y < this.quadSpace.length; ++y) {
                    final ArrayList shapes = this.quadSpace[x][y];
                    final int before = shapes.size();
                    this.combine(shapes);
                    final int after = shapes.size();
                    updated |= (before != after);
                }
            }
        }
        final HashSet result = new HashSet();
        for (int x2 = 0; x2 < this.quadSpace.length; ++x2) {
            for (int y2 = 0; y2 < this.quadSpace.length; ++y2) {
                result.addAll(this.quadSpace[x2][y2]);
            }
        }
        return new ArrayList(result);
    }
    
    private ArrayList combine(final ArrayList shapes) {
        ArrayList last = shapes;
        ArrayList current = shapes;
        for (boolean first = true; current.size() != last.size() || first; first = false, last = current, current = this.combineImpl(current)) {}
        final ArrayList pruned = new ArrayList();
        for (int i = 0; i < current.size(); ++i) {
            pruned.add(current.get(i).prune());
        }
        return pruned;
    }
    
    private ArrayList combineImpl(final ArrayList shapes) {
        ArrayList result = new ArrayList(shapes);
        if (this.quadSpace != null) {
            result = shapes;
        }
        for (int i = 0; i < shapes.size(); ++i) {
            final Shape first = (Shape)shapes.get(i);
            for (int j = i + 1; j < shapes.size(); ++j) {
                final Shape second = (Shape)shapes.get(j);
                if (first.intersects(second)) {
                    final Shape[] joined = this.util.union(first, second);
                    if (joined.length == 1) {
                        if (this.quadSpace != null) {
                            this.removeFromQuadSpace(first);
                            this.removeFromQuadSpace(second);
                            this.addToQuadSpace(joined[0]);
                        }
                        else {
                            result.remove(first);
                            result.remove(second);
                            result.add(joined[0]);
                        }
                        return result;
                    }
                }
            }
        }
        return result;
    }
    
    public boolean collides(final Shape shape1, final Shape shape2) {
        if (shape1.intersects(shape2)) {
            return true;
        }
        for (int i = 0; i < shape1.getPointCount(); ++i) {
            final float[] pt = shape1.getPoint(i);
            if (shape2.contains(pt[0], pt[1])) {
                return true;
            }
        }
        for (int i = 0; i < shape2.getPointCount(); ++i) {
            final float[] pt = shape2.getPoint(i);
            if (shape1.contains(pt[0], pt[1])) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        this.util.setListener(this);
        this.init();
    }
    
    @Override
    public void update(final GameContainer container, final int delta) throws SlickException {
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) throws SlickException {
        g.setColor(Color.green);
        for (int i = 0; i < this.original.size(); ++i) {
            final Shape shape = this.original.get(i);
            g.draw(shape);
        }
        g.setColor(Color.white);
        if (this.quadSpaceShapes != null) {
            g.draw(this.quadSpaceShapes[0][0]);
        }
        g.translate(0.0f, 320.0f);
        for (int i = 0; i < this.combined.size(); ++i) {
            g.setColor(Color.white);
            final Shape shape = this.combined.get(i);
            g.draw(shape);
            for (int j = 0; j < shape.getPointCount(); ++j) {
                g.setColor(Color.yellow);
                final float[] pt = shape.getPoint(j);
                g.fillOval(pt[0] - 1.0f, pt[1] - 1.0f, 3.0f, 3.0f);
            }
        }
    }
    
    public static void main(final String[] argv) {
        try {
            final AppGameContainer container = new AppGameContainer(new GeomUtilTileTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void pointExcluded(final float x, final float y) {
    }
    
    @Override
    public void pointIntersected(final float x, final float y) {
        this.intersections.add(new Vector2f(x, y));
    }
    
    @Override
    public void pointUsed(final float x, final float y) {
        this.used.add(new Vector2f(x, y));
    }
}

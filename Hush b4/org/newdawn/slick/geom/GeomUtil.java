// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.geom;

import java.util.ArrayList;

public class GeomUtil
{
    public float EPSILON;
    public float EDGE_SCALE;
    public int MAX_POINTS;
    public GeomUtilListener listener;
    
    public GeomUtil() {
        this.EPSILON = 1.0E-4f;
        this.EDGE_SCALE = 1.0f;
        this.MAX_POINTS = 10000;
    }
    
    public Shape[] subtract(Shape target, Shape missing) {
        target = target.transform(new Transform());
        missing = missing.transform(new Transform());
        int count = 0;
        for (int i = 0; i < target.getPointCount(); ++i) {
            if (missing.contains(target.getPoint(i)[0], target.getPoint(i)[1])) {
                ++count;
            }
        }
        if (count == target.getPointCount()) {
            return new Shape[0];
        }
        if (!target.intersects(missing)) {
            return new Shape[] { target };
        }
        int found = 0;
        for (int j = 0; j < missing.getPointCount(); ++j) {
            if (target.contains(missing.getPoint(j)[0], missing.getPoint(j)[1]) && !this.onPath(target, missing.getPoint(j)[0], missing.getPoint(j)[1])) {
                ++found;
            }
        }
        for (int j = 0; j < target.getPointCount(); ++j) {
            if (missing.contains(target.getPoint(j)[0], target.getPoint(j)[1]) && !this.onPath(missing, target.getPoint(j)[0], target.getPoint(j)[1])) {
                ++found;
            }
        }
        if (found < 1) {
            return new Shape[] { target };
        }
        return this.combine(target, missing, true);
    }
    
    private boolean onPath(final Shape path, final float x, final float y) {
        for (int i = 0; i < path.getPointCount() + 1; ++i) {
            final int n = rationalPoint(path, i + 1);
            final Line line = this.getLine(path, rationalPoint(path, i), n);
            if (line.distance(new Vector2f(x, y)) < this.EPSILON * 100.0f) {
                return true;
            }
        }
        return false;
    }
    
    public void setListener(final GeomUtilListener listener) {
        this.listener = listener;
    }
    
    public Shape[] union(Shape target, Shape other) {
        target = target.transform(new Transform());
        other = other.transform(new Transform());
        if (!target.intersects(other)) {
            return new Shape[] { target, other };
        }
        boolean touches = false;
        int buttCount = 0;
        for (int i = 0; i < target.getPointCount(); ++i) {
            if (other.contains(target.getPoint(i)[0], target.getPoint(i)[1]) && !other.hasVertex(target.getPoint(i)[0], target.getPoint(i)[1])) {
                touches = true;
                break;
            }
            if (other.hasVertex(target.getPoint(i)[0], target.getPoint(i)[1])) {
                ++buttCount;
            }
        }
        for (int i = 0; i < other.getPointCount(); ++i) {
            if (target.contains(other.getPoint(i)[0], other.getPoint(i)[1]) && !target.hasVertex(other.getPoint(i)[0], other.getPoint(i)[1])) {
                touches = true;
                break;
            }
        }
        if (!touches && buttCount < 2) {
            return new Shape[] { target, other };
        }
        return this.combine(target, other, false);
    }
    
    private Shape[] combine(final Shape target, final Shape other, final boolean subtract) {
        if (subtract) {
            final ArrayList shapes = new ArrayList();
            final ArrayList used = new ArrayList();
            for (int i = 0; i < target.getPointCount(); ++i) {
                final float[] point = target.getPoint(i);
                if (other.contains(point[0], point[1])) {
                    used.add(new Vector2f(point[0], point[1]));
                    if (this.listener != null) {
                        this.listener.pointExcluded(point[0], point[1]);
                    }
                }
            }
            for (int i = 0; i < target.getPointCount(); ++i) {
                final float[] point = target.getPoint(i);
                final Vector2f pt = new Vector2f(point[0], point[1]);
                if (!used.contains(pt)) {
                    final Shape result = this.combineSingle(target, other, true, i);
                    shapes.add(result);
                    for (int j = 0; j < result.getPointCount(); ++j) {
                        final float[] kpoint = result.getPoint(j);
                        final Vector2f kpt = new Vector2f(kpoint[0], kpoint[1]);
                        used.add(kpt);
                    }
                }
            }
            return shapes.toArray(new Shape[0]);
        }
        for (int k = 0; k < target.getPointCount(); ++k) {
            if (!other.contains(target.getPoint(k)[0], target.getPoint(k)[1]) && !other.hasVertex(target.getPoint(k)[0], target.getPoint(k)[1])) {
                final Shape shape = this.combineSingle(target, other, false, k);
                return new Shape[] { shape };
            }
        }
        return new Shape[] { other };
    }
    
    private Shape combineSingle(final Shape target, final Shape missing, final boolean subtract, final int start) {
        Shape current = target;
        Shape other = missing;
        int point = start;
        int dir = 1;
        final Polygon poly = new Polygon();
        boolean first = true;
        int loop = 0;
        float px = current.getPoint(point)[0];
        float py = current.getPoint(point)[1];
        while (!poly.hasVertex(px, py) || first || current != target) {
            first = false;
            if (++loop > this.MAX_POINTS) {
                break;
            }
            poly.addPoint(px, py);
            if (this.listener != null) {
                this.listener.pointUsed(px, py);
            }
            final Line line = this.getLine(current, px, py, rationalPoint(current, point + dir));
            final HitResult hit = this.intersect(other, line);
            if (hit != null) {
                final Line hitLine = hit.line;
                final Vector2f pt = hit.pt;
                px = pt.x;
                py = pt.y;
                if (this.listener != null) {
                    this.listener.pointIntersected(px, py);
                }
                if (other.hasVertex(px, py)) {
                    point = other.indexOf(pt.x, pt.y);
                    dir = 1;
                    px = pt.x;
                    py = pt.y;
                    final Shape temp = current;
                    current = other;
                    other = temp;
                }
                else {
                    float dx = hitLine.getDX() / hitLine.length();
                    float dy = hitLine.getDY() / hitLine.length();
                    dx *= this.EDGE_SCALE;
                    dy *= this.EDGE_SCALE;
                    if (current.contains(pt.x + dx, pt.y + dy)) {
                        if (subtract) {
                            if (current == missing) {
                                point = hit.p2;
                                dir = -1;
                            }
                            else {
                                point = hit.p1;
                                dir = 1;
                            }
                        }
                        else if (current == target) {
                            point = hit.p2;
                            dir = -1;
                        }
                        else {
                            point = hit.p2;
                            dir = -1;
                        }
                        final Shape temp2 = current;
                        current = other;
                        other = temp2;
                    }
                    else if (current.contains(pt.x - dx, pt.y - dy)) {
                        if (subtract) {
                            if (current == target) {
                                point = hit.p2;
                                dir = -1;
                            }
                            else {
                                point = hit.p1;
                                dir = 1;
                            }
                        }
                        else if (current == missing) {
                            point = hit.p1;
                            dir = 1;
                        }
                        else {
                            point = hit.p1;
                            dir = 1;
                        }
                        final Shape temp2 = current;
                        current = other;
                        other = temp2;
                    }
                    else {
                        if (subtract) {
                            break;
                        }
                        point = hit.p1;
                        dir = 1;
                        final Shape temp2 = current;
                        current = other;
                        other = temp2;
                        point = rationalPoint(current, point + dir);
                        px = current.getPoint(point)[0];
                        py = current.getPoint(point)[1];
                    }
                }
            }
            else {
                point = rationalPoint(current, point + dir);
                px = current.getPoint(point)[0];
                py = current.getPoint(point)[1];
            }
        }
        poly.addPoint(px, py);
        if (this.listener != null) {
            this.listener.pointUsed(px, py);
        }
        return poly;
    }
    
    public HitResult intersect(final Shape shape, final Line line) {
        float distance = Float.MAX_VALUE;
        HitResult hit = null;
        for (int i = 0; i < shape.getPointCount(); ++i) {
            final int next = rationalPoint(shape, i + 1);
            final Line local = this.getLine(shape, i, next);
            final Vector2f pt = line.intersect(local, true);
            if (pt != null) {
                final float newDis = pt.distance(line.getStart());
                if (newDis < distance && newDis > this.EPSILON) {
                    hit = new HitResult();
                    hit.pt = pt;
                    hit.line = local;
                    hit.p1 = i;
                    hit.p2 = next;
                    distance = newDis;
                }
            }
        }
        return hit;
    }
    
    public static int rationalPoint(final Shape shape, int p) {
        while (p < 0) {
            p += shape.getPointCount();
        }
        while (p >= shape.getPointCount()) {
            p -= shape.getPointCount();
        }
        return p;
    }
    
    public Line getLine(final Shape shape, final int s, final int e) {
        final float[] start = shape.getPoint(s);
        final float[] end = shape.getPoint(e);
        final Line line = new Line(start[0], start[1], end[0], end[1]);
        return line;
    }
    
    public Line getLine(final Shape shape, final float sx, final float sy, final int e) {
        final float[] end = shape.getPoint(e);
        final Line line = new Line(sx, sy, end[0], end[1]);
        return line;
    }
    
    public class HitResult
    {
        public Line line;
        public int p1;
        public int p2;
        public Vector2f pt;
    }
}

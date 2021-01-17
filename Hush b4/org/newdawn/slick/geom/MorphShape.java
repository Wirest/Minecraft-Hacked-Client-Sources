// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.geom;

import java.util.ArrayList;

public class MorphShape extends Shape
{
    private ArrayList shapes;
    private float offset;
    private Shape current;
    private Shape next;
    
    public MorphShape(final Shape base) {
        (this.shapes = new ArrayList()).add(base);
        final float[] copy = base.points;
        this.points = new float[copy.length];
        this.current = base;
        this.next = base;
    }
    
    public void addShape(final Shape shape) {
        if (shape.points.length != this.points.length) {
            throw new RuntimeException("Attempt to morph between two shapes with different vertex counts");
        }
        final Shape prev = this.shapes.get(this.shapes.size() - 1);
        if (this.equalShapes(prev, shape)) {
            this.shapes.add(prev);
        }
        else {
            this.shapes.add(shape);
        }
        if (this.shapes.size() == 2) {
            this.next = this.shapes.get(1);
        }
    }
    
    private boolean equalShapes(final Shape a, final Shape b) {
        a.checkPoints();
        b.checkPoints();
        for (int i = 0; i < a.points.length; ++i) {
            if (a.points[i] != b.points[i]) {
                return false;
            }
        }
        return true;
    }
    
    public void setMorphTime(final float time) {
        int p = (int)time;
        int n = p + 1;
        final float offset = time - p;
        p = this.rational(p);
        n = this.rational(n);
        this.setFrame(p, n, offset);
    }
    
    public void updateMorphTime(final float delta) {
        this.offset += delta;
        if (this.offset < 0.0f) {
            int index = this.shapes.indexOf(this.current);
            if (index < 0) {
                index = this.shapes.size() - 1;
            }
            final int nframe = this.rational(index + 1);
            this.setFrame(index, nframe, this.offset);
            ++this.offset;
        }
        else if (this.offset > 1.0f) {
            int index = this.shapes.indexOf(this.next);
            if (index < 1) {
                index = 0;
            }
            final int nframe = this.rational(index + 1);
            this.setFrame(index, nframe, this.offset);
            --this.offset;
        }
        else {
            this.pointsDirty = true;
        }
    }
    
    public void setExternalFrame(final Shape current) {
        this.current = current;
        this.next = this.shapes.get(0);
        this.offset = 0.0f;
    }
    
    private int rational(int n) {
        while (n >= this.shapes.size()) {
            n -= this.shapes.size();
        }
        while (n < 0) {
            n += this.shapes.size();
        }
        return n;
    }
    
    private void setFrame(final int a, final int b, final float offset) {
        this.current = this.shapes.get(a);
        this.next = this.shapes.get(b);
        this.offset = offset;
        this.pointsDirty = true;
    }
    
    @Override
    protected void createPoints() {
        if (this.current == this.next) {
            System.arraycopy(this.current.points, 0, this.points, 0, this.points.length);
            return;
        }
        final float[] apoints = this.current.points;
        final float[] bpoints = this.next.points;
        for (int i = 0; i < this.points.length; ++i) {
            this.points[i] = apoints[i] * (1.0f - this.offset);
            final float[] points = this.points;
            final int n = i;
            points[n] += bpoints[i] * this.offset;
        }
    }
    
    @Override
    public Shape transform(final Transform transform) {
        this.createPoints();
        final Polygon poly = new Polygon(this.points);
        return poly;
    }
}

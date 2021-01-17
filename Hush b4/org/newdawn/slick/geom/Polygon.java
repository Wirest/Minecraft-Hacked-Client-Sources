// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.geom;

import java.util.ArrayList;

public class Polygon extends Shape
{
    private boolean allowDups;
    private boolean closed;
    
    public Polygon(final float[] points) {
        this.allowDups = false;
        this.closed = true;
        final int length = points.length;
        this.points = new float[length];
        this.maxX = -1.4E-45f;
        this.maxY = -1.4E-45f;
        this.minX = Float.MAX_VALUE;
        this.minY = Float.MAX_VALUE;
        this.x = Float.MAX_VALUE;
        this.y = Float.MAX_VALUE;
        for (int i = 0; i < length; ++i) {
            this.points[i] = points[i];
            if (i % 2 == 0) {
                if (points[i] > this.maxX) {
                    this.maxX = points[i];
                }
                if (points[i] < this.minX) {
                    this.minX = points[i];
                }
                if (points[i] < this.x) {
                    this.x = points[i];
                }
            }
            else {
                if (points[i] > this.maxY) {
                    this.maxY = points[i];
                }
                if (points[i] < this.minY) {
                    this.minY = points[i];
                }
                if (points[i] < this.y) {
                    this.y = points[i];
                }
            }
        }
        this.findCenter();
        this.calculateRadius();
        this.pointsDirty = true;
    }
    
    public Polygon() {
        this.allowDups = false;
        this.closed = true;
        this.points = new float[0];
        this.maxX = -1.4E-45f;
        this.maxY = -1.4E-45f;
        this.minX = Float.MAX_VALUE;
        this.minY = Float.MAX_VALUE;
    }
    
    public void setAllowDuplicatePoints(final boolean allowDups) {
        this.allowDups = allowDups;
    }
    
    public void addPoint(final float x, final float y) {
        if (this.hasVertex(x, y) && !this.allowDups) {
            return;
        }
        final ArrayList tempPoints = new ArrayList();
        for (int i = 0; i < this.points.length; ++i) {
            tempPoints.add(new Float(this.points[i]));
        }
        tempPoints.add(new Float(x));
        tempPoints.add(new Float(y));
        final int length = tempPoints.size();
        this.points = new float[length];
        for (int j = 0; j < length; ++j) {
            this.points[j] = tempPoints.get(j);
        }
        if (x > this.maxX) {
            this.maxX = x;
        }
        if (y > this.maxY) {
            this.maxY = y;
        }
        if (x < this.minX) {
            this.minX = x;
        }
        if (y < this.minY) {
            this.minY = y;
        }
        this.findCenter();
        this.calculateRadius();
        this.pointsDirty = true;
    }
    
    @Override
    public Shape transform(final Transform transform) {
        this.checkPoints();
        final Polygon resultPolygon = new Polygon();
        final float[] result = new float[this.points.length];
        transform.transform(this.points, 0, result, 0, this.points.length / 2);
        resultPolygon.points = result;
        resultPolygon.findCenter();
        resultPolygon.closed = this.closed;
        return resultPolygon;
    }
    
    @Override
    public void setX(final float x) {
        super.setX(x);
        this.pointsDirty = false;
    }
    
    @Override
    public void setY(final float y) {
        super.setY(y);
        this.pointsDirty = false;
    }
    
    @Override
    protected void createPoints() {
    }
    
    @Override
    public boolean closed() {
        return this.closed;
    }
    
    public void setClosed(final boolean closed) {
        this.closed = closed;
    }
    
    public Polygon copy() {
        final float[] copyPoints = new float[this.points.length];
        System.arraycopy(this.points, 0, copyPoints, 0, copyPoints.length);
        return new Polygon(copyPoints);
    }
}

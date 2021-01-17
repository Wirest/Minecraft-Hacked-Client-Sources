// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.geom;

import org.newdawn.slick.util.FastTrig;
import java.util.ArrayList;

public class Ellipse extends Shape
{
    protected static final int DEFAULT_SEGMENT_COUNT = 50;
    private int segmentCount;
    private float radius1;
    private float radius2;
    
    public Ellipse(final float centerPointX, final float centerPointY, final float radius1, final float radius2) {
        this(centerPointX, centerPointY, radius1, radius2, 50);
    }
    
    public Ellipse(final float centerPointX, final float centerPointY, final float radius1, final float radius2, final int segmentCount) {
        this.x = centerPointX - radius1;
        this.y = centerPointY - radius2;
        this.radius1 = radius1;
        this.radius2 = radius2;
        this.segmentCount = segmentCount;
        this.checkPoints();
    }
    
    public void setRadii(final float radius1, final float radius2) {
        this.setRadius1(radius1);
        this.setRadius2(radius2);
    }
    
    public float getRadius1() {
        return this.radius1;
    }
    
    public void setRadius1(final float radius1) {
        if (radius1 != this.radius1) {
            this.radius1 = radius1;
            this.pointsDirty = true;
        }
    }
    
    public float getRadius2() {
        return this.radius2;
    }
    
    public void setRadius2(final float radius2) {
        if (radius2 != this.radius2) {
            this.radius2 = radius2;
            this.pointsDirty = true;
        }
    }
    
    @Override
    protected void createPoints() {
        final ArrayList tempPoints = new ArrayList();
        this.maxX = -1.4E-45f;
        this.maxY = -1.4E-45f;
        this.minX = Float.MAX_VALUE;
        this.minY = Float.MAX_VALUE;
        final float start = 0.0f;
        final float end = 359.0f;
        final float cx = this.x + this.radius1;
        final float cy = this.y + this.radius2;
        final int step = 360 / this.segmentCount;
        for (float a = start; a <= end + step; a += step) {
            float ang = a;
            if (ang > end) {
                ang = end;
            }
            final float newX = (float)(cx + FastTrig.cos(Math.toRadians(ang)) * this.radius1);
            final float newY = (float)(cy + FastTrig.sin(Math.toRadians(ang)) * this.radius2);
            if (newX > this.maxX) {
                this.maxX = newX;
            }
            if (newY > this.maxY) {
                this.maxY = newY;
            }
            if (newX < this.minX) {
                this.minX = newX;
            }
            if (newY < this.minY) {
                this.minY = newY;
            }
            tempPoints.add(new Float(newX));
            tempPoints.add(new Float(newY));
        }
        this.points = new float[tempPoints.size()];
        for (int i = 0; i < this.points.length; ++i) {
            this.points[i] = tempPoints.get(i);
        }
    }
    
    @Override
    public Shape transform(final Transform transform) {
        this.checkPoints();
        final Polygon resultPolygon = new Polygon();
        final float[] result = new float[this.points.length];
        transform.transform(this.points, 0, result, 0, this.points.length / 2);
        resultPolygon.points = result;
        resultPolygon.checkPoints();
        return resultPolygon;
    }
    
    @Override
    protected void findCenter() {
        (this.center = new float[2])[0] = this.x + this.radius1;
        this.center[1] = this.y + this.radius2;
    }
    
    @Override
    protected void calculateRadius() {
        this.boundingCircleRadius = ((this.radius1 > this.radius2) ? this.radius1 : this.radius2);
    }
}

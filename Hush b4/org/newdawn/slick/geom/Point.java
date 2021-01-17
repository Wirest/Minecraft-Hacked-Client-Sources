// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.geom;

public class Point extends Shape
{
    public Point(final float x, final float y) {
        this.x = x;
        this.y = y;
        this.checkPoints();
    }
    
    @Override
    public Shape transform(final Transform transform) {
        final float[] result = new float[this.points.length];
        transform.transform(this.points, 0, result, 0, this.points.length / 2);
        return new Point(this.points[0], this.points[1]);
    }
    
    @Override
    protected void createPoints() {
        (this.points = new float[2])[0] = this.getX();
        this.points[1] = this.getY();
        this.maxX = this.x;
        this.maxY = this.y;
        this.minX = this.x;
        this.minY = this.y;
        this.findCenter();
        this.calculateRadius();
    }
    
    @Override
    protected void findCenter() {
        (this.center = new float[2])[0] = this.points[0];
        this.center[1] = this.points[1];
    }
    
    @Override
    protected void calculateRadius() {
        this.boundingCircleRadius = 0.0f;
    }
}

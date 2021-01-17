// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.geom;

public strictfp class Circle extends Ellipse
{
    public float radius;
    
    public Circle(final float centerPointX, final float centerPointY, final float radius) {
        this(centerPointX, centerPointY, radius, 50);
    }
    
    public Circle(final float centerPointX, final float centerPointY, final float radius, final int segmentCount) {
        super(centerPointX, centerPointY, radius, radius, segmentCount);
        this.x = centerPointX - radius;
        this.y = centerPointY - radius;
        this.radius = radius;
        this.boundingCircleRadius = radius;
    }
    
    @Override
    public strictfp float getCenterX() {
        return this.getX() + this.radius;
    }
    
    @Override
    public strictfp float getCenterY() {
        return this.getY() + this.radius;
    }
    
    @Override
    public strictfp float[] getCenter() {
        return new float[] { this.getCenterX(), this.getCenterY() };
    }
    
    public strictfp void setRadius(final float radius) {
        if (radius != this.radius) {
            this.pointsDirty = true;
            this.setRadii(this.radius = radius, radius);
        }
    }
    
    public strictfp float getRadius() {
        return this.radius;
    }
    
    @Override
    public strictfp boolean intersects(final Shape shape) {
        if (shape instanceof Circle) {
            final Circle other = (Circle)shape;
            float totalRad2 = this.getRadius() + other.getRadius();
            if (Math.abs(other.getCenterX() - this.getCenterX()) > totalRad2) {
                return false;
            }
            if (Math.abs(other.getCenterY() - this.getCenterY()) > totalRad2) {
                return false;
            }
            totalRad2 *= totalRad2;
            final float dx = Math.abs(other.getCenterX() - this.getCenterX());
            final float dy = Math.abs(other.getCenterY() - this.getCenterY());
            return totalRad2 >= dx * dx + dy * dy;
        }
        else {
            if (shape instanceof Rectangle) {
                return this.intersects((Rectangle)shape);
            }
            return super.intersects(shape);
        }
    }
    
    @Override
    public strictfp boolean contains(final float x, final float y) {
        final float xDelta = x - this.getCenterX();
        final float yDelta = y - this.getCenterY();
        return xDelta * xDelta + yDelta * yDelta < this.getRadius() * this.getRadius();
    }
    
    private strictfp boolean contains(final Line line) {
        return this.contains(line.getX1(), line.getY1()) && this.contains(line.getX2(), line.getY2());
    }
    
    @Override
    protected strictfp void findCenter() {
        (this.center = new float[2])[0] = this.x + this.radius;
        this.center[1] = this.y + this.radius;
    }
    
    @Override
    protected strictfp void calculateRadius() {
        this.boundingCircleRadius = this.radius;
    }
    
    private strictfp boolean intersects(final Rectangle other) {
        final Rectangle box = other;
        final Circle circle = this;
        if (box.contains(this.x + this.radius, this.y + this.radius)) {
            return true;
        }
        final float x1 = box.getX();
        final float y1 = box.getY();
        final float x2 = box.getX() + box.getWidth();
        final float y2 = box.getY() + box.getHeight();
        final Line[] lines = { new Line(x1, y1, x2, y1), new Line(x2, y1, x2, y2), new Line(x2, y2, x1, y2), new Line(x1, y2, x1, y1) };
        final float r2 = circle.getRadius() * circle.getRadius();
        final Vector2f pos = new Vector2f(circle.getCenterX(), circle.getCenterY());
        for (int i = 0; i < 4; ++i) {
            final float dis = lines[i].distanceSquared(pos);
            if (dis < r2) {
                return true;
            }
        }
        return false;
    }
    
    private strictfp boolean intersects(final Line other) {
        final Vector2f lineSegmentStart = new Vector2f(other.getX1(), other.getY1());
        final Vector2f lineSegmentEnd = new Vector2f(other.getX2(), other.getY2());
        final Vector2f circleCenter = new Vector2f(this.getCenterX(), this.getCenterY());
        final Vector2f segv = lineSegmentEnd.copy().sub(lineSegmentStart);
        final Vector2f ptv = circleCenter.copy().sub(lineSegmentStart);
        final float segvLength = segv.length();
        final float projvl = ptv.dot(segv) / segvLength;
        Vector2f closest;
        if (projvl < 0.0f) {
            closest = lineSegmentStart;
        }
        else if (projvl > segvLength) {
            closest = lineSegmentEnd;
        }
        else {
            final Vector2f projv = segv.copy().scale(projvl / segvLength);
            closest = lineSegmentStart.copy().add(projv);
        }
        final boolean intersects = circleCenter.copy().sub(closest).lengthSquared() <= this.getRadius() * this.getRadius();
        return intersects;
    }
}

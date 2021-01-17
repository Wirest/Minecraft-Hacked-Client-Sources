// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.geom;

public class Line extends Shape
{
    private Vector2f start;
    private Vector2f end;
    private Vector2f vec;
    private float lenSquared;
    private Vector2f loc;
    private Vector2f v;
    private Vector2f v2;
    private Vector2f proj;
    private Vector2f closest;
    private Vector2f other;
    private boolean outerEdge;
    private boolean innerEdge;
    
    public Line(final float x, final float y, final boolean inner, final boolean outer) {
        this(0.0f, 0.0f, x, y);
    }
    
    public Line(final float x, final float y) {
        this(x, y, true, true);
    }
    
    public Line(final float x1, final float y1, final float x2, final float y2) {
        this(new Vector2f(x1, y1), new Vector2f(x2, y2));
    }
    
    public Line(final float x1, final float y1, final float dx, final float dy, final boolean dummy) {
        this(new Vector2f(x1, y1), new Vector2f(x1 + dx, y1 + dy));
    }
    
    public Line(final float[] start, final float[] end) {
        this.loc = new Vector2f(0.0f, 0.0f);
        this.v = new Vector2f(0.0f, 0.0f);
        this.v2 = new Vector2f(0.0f, 0.0f);
        this.proj = new Vector2f(0.0f, 0.0f);
        this.closest = new Vector2f(0.0f, 0.0f);
        this.other = new Vector2f(0.0f, 0.0f);
        this.outerEdge = true;
        this.innerEdge = true;
        this.set(start, end);
    }
    
    public Line(final Vector2f start, final Vector2f end) {
        this.loc = new Vector2f(0.0f, 0.0f);
        this.v = new Vector2f(0.0f, 0.0f);
        this.v2 = new Vector2f(0.0f, 0.0f);
        this.proj = new Vector2f(0.0f, 0.0f);
        this.closest = new Vector2f(0.0f, 0.0f);
        this.other = new Vector2f(0.0f, 0.0f);
        this.outerEdge = true;
        this.innerEdge = true;
        this.set(start, end);
    }
    
    public void set(final float[] start, final float[] end) {
        this.set(start[0], start[1], end[0], end[1]);
    }
    
    public Vector2f getStart() {
        return this.start;
    }
    
    public Vector2f getEnd() {
        return this.end;
    }
    
    public float length() {
        return this.vec.length();
    }
    
    public float lengthSquared() {
        return this.vec.lengthSquared();
    }
    
    public void set(final Vector2f start, final Vector2f end) {
        super.pointsDirty = true;
        if (this.start == null) {
            this.start = new Vector2f();
        }
        this.start.set(start);
        if (this.end == null) {
            this.end = new Vector2f();
        }
        this.end.set(end);
        (this.vec = new Vector2f(end)).sub(start);
        this.lenSquared = this.vec.lengthSquared();
    }
    
    public void set(final float sx, final float sy, final float ex, final float ey) {
        super.pointsDirty = true;
        this.start.set(sx, sy);
        this.end.set(ex, ey);
        final float dx = ex - sx;
        final float dy = ey - sy;
        this.vec.set(dx, dy);
        this.lenSquared = dx * dx + dy * dy;
    }
    
    public float getDX() {
        return this.end.getX() - this.start.getX();
    }
    
    public float getDY() {
        return this.end.getY() - this.start.getY();
    }
    
    @Override
    public float getX() {
        return this.getX1();
    }
    
    @Override
    public float getY() {
        return this.getY1();
    }
    
    public float getX1() {
        return this.start.getX();
    }
    
    public float getY1() {
        return this.start.getY();
    }
    
    public float getX2() {
        return this.end.getX();
    }
    
    public float getY2() {
        return this.end.getY();
    }
    
    public float distance(final Vector2f point) {
        return (float)Math.sqrt(this.distanceSquared(point));
    }
    
    public boolean on(final Vector2f point) {
        this.getClosestPoint(point, this.closest);
        return point.equals(this.closest);
    }
    
    public float distanceSquared(final Vector2f point) {
        this.getClosestPoint(point, this.closest);
        this.closest.sub(point);
        final float result = this.closest.lengthSquared();
        return result;
    }
    
    public void getClosestPoint(final Vector2f point, final Vector2f result) {
        this.loc.set(point);
        this.loc.sub(this.start);
        float projDistance = this.vec.dot(this.loc);
        projDistance /= this.vec.lengthSquared();
        if (projDistance < 0.0f) {
            result.set(this.start);
            return;
        }
        if (projDistance > 1.0f) {
            result.set(this.end);
            return;
        }
        result.x = this.start.getX() + projDistance * this.vec.getX();
        result.y = this.start.getY() + projDistance * this.vec.getY();
    }
    
    @Override
    public String toString() {
        return "[Line " + this.start + "," + this.end + "]";
    }
    
    public Vector2f intersect(final Line other) {
        return this.intersect(other, false);
    }
    
    public Vector2f intersect(final Line other, final boolean limit) {
        final Vector2f temp = new Vector2f();
        if (!this.intersect(other, limit, temp)) {
            return null;
        }
        return temp;
    }
    
    public boolean intersect(final Line other, final boolean limit, final Vector2f result) {
        final float dx1 = this.end.getX() - this.start.getX();
        final float dx2 = other.end.getX() - other.start.getX();
        final float dy1 = this.end.getY() - this.start.getY();
        final float dy2 = other.end.getY() - other.start.getY();
        final float denom = dy2 * dx1 - dx2 * dy1;
        if (denom == 0.0f) {
            return false;
        }
        float ua = dx2 * (this.start.getY() - other.start.getY()) - dy2 * (this.start.getX() - other.start.getX());
        ua /= denom;
        float ub = dx1 * (this.start.getY() - other.start.getY()) - dy1 * (this.start.getX() - other.start.getX());
        ub /= denom;
        if (limit && (ua < 0.0f || ua > 1.0f || ub < 0.0f || ub > 1.0f)) {
            return false;
        }
        final float u = ua;
        final float ix = this.start.getX() + u * (this.end.getX() - this.start.getX());
        final float iy = this.start.getY() + u * (this.end.getY() - this.start.getY());
        result.set(ix, iy);
        return true;
    }
    
    @Override
    protected void createPoints() {
        (this.points = new float[4])[0] = this.getX1();
        this.points[1] = this.getY1();
        this.points[2] = this.getX2();
        this.points[3] = this.getY2();
    }
    
    @Override
    public Shape transform(final Transform transform) {
        final float[] temp = new float[4];
        this.createPoints();
        transform.transform(this.points, 0, temp, 0, 2);
        return new Line(temp[0], temp[1], temp[2], temp[3]);
    }
    
    @Override
    public boolean closed() {
        return false;
    }
    
    @Override
    public boolean intersects(final Shape shape) {
        if (shape instanceof Circle) {
            return shape.intersects(this);
        }
        return super.intersects(shape);
    }
}

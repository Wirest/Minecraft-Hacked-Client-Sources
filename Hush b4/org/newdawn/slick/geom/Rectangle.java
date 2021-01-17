// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.geom;

public class Rectangle extends Shape
{
    protected float width;
    protected float height;
    
    public Rectangle(final float x, final float y, final float width, final float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maxX = x + width;
        this.maxY = y + height;
        this.checkPoints();
    }
    
    @Override
    public boolean contains(final float xp, final float yp) {
        return xp > this.getX() && yp > this.getY() && xp < this.maxX && yp < this.maxY;
    }
    
    public void setBounds(final Rectangle other) {
        this.setBounds(other.getX(), other.getY(), other.getWidth(), other.getHeight());
    }
    
    public void setBounds(final float x, final float y, final float width, final float height) {
        this.setX(x);
        this.setY(y);
        this.setSize(width, height);
    }
    
    public void setSize(final float width, final float height) {
        this.setWidth(width);
        this.setHeight(height);
    }
    
    @Override
    public float getWidth() {
        return this.width;
    }
    
    @Override
    public float getHeight() {
        return this.height;
    }
    
    public void grow(final float h, final float v) {
        this.setX(this.getX() - h);
        this.setY(this.getY() - v);
        this.setWidth(this.getWidth() + h * 2.0f);
        this.setHeight(this.getHeight() + v * 2.0f);
    }
    
    public void scaleGrow(final float h, final float v) {
        this.grow(this.getWidth() * (h - 1.0f), this.getHeight() * (v - 1.0f));
    }
    
    public void setWidth(final float width) {
        if (width != this.width) {
            this.pointsDirty = true;
            this.width = width;
            this.maxX = this.x + width;
        }
    }
    
    public void setHeight(final float height) {
        if (height != this.height) {
            this.pointsDirty = true;
            this.height = height;
            this.maxY = this.y + height;
        }
    }
    
    @Override
    public boolean intersects(final Shape shape) {
        if (shape instanceof Rectangle) {
            final Rectangle other = (Rectangle)shape;
            return this.x <= other.x + other.width && this.x + this.width >= other.x && this.y <= other.y + other.height && this.y + this.height >= other.y;
        }
        if (shape instanceof Circle) {
            return this.intersects((Circle)shape);
        }
        return super.intersects(shape);
    }
    
    @Override
    protected void createPoints() {
        final float useWidth = this.width;
        final float useHeight = this.height;
        (this.points = new float[8])[0] = this.x;
        this.points[1] = this.y;
        this.points[2] = this.x + useWidth;
        this.points[3] = this.y;
        this.points[4] = this.x + useWidth;
        this.points[5] = this.y + useHeight;
        this.points[6] = this.x;
        this.points[7] = this.y + useHeight;
        this.maxX = this.points[2];
        this.maxY = this.points[5];
        this.minX = this.points[0];
        this.minY = this.points[1];
        this.findCenter();
        this.calculateRadius();
    }
    
    private boolean intersects(final Circle other) {
        return other.intersects((Shape)this);
    }
    
    @Override
    public String toString() {
        return "[Rectangle " + this.width + "x" + this.height + "]";
    }
    
    public static boolean contains(final float xp, final float yp, final float xr, final float yr, final float widthr, final float heightr) {
        return xp >= xr && yp >= yr && xp <= xr + widthr && yp <= yr + heightr;
    }
    
    @Override
    public Shape transform(final Transform transform) {
        this.checkPoints();
        final Polygon resultPolygon = new Polygon();
        final float[] result = new float[this.points.length];
        transform.transform(this.points, 0, result, 0, this.points.length / 2);
        resultPolygon.points = result;
        resultPolygon.findCenter();
        resultPolygon.checkPoints();
        return resultPolygon;
    }
}

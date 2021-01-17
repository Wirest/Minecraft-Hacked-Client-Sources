// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.geom;

public class Curve extends Shape
{
    private Vector2f p1;
    private Vector2f c1;
    private Vector2f c2;
    private Vector2f p2;
    private int segments;
    
    public Curve(final Vector2f p1, final Vector2f c1, final Vector2f c2, final Vector2f p2) {
        this(p1, c1, c2, p2, 20);
    }
    
    public Curve(final Vector2f p1, final Vector2f c1, final Vector2f c2, final Vector2f p2, final int segments) {
        this.p1 = new Vector2f(p1);
        this.c1 = new Vector2f(c1);
        this.c2 = new Vector2f(c2);
        this.p2 = new Vector2f(p2);
        this.segments = segments;
        this.pointsDirty = true;
    }
    
    public Vector2f pointAt(final float t) {
        final float a = 1.0f - t;
        final float b = t;
        final float f1 = a * a * a;
        final float f2 = 3.0f * a * a * b;
        final float f3 = 3.0f * a * b * b;
        final float f4 = b * b * b;
        final float nx = this.p1.x * f1 + this.c1.x * f2 + this.c2.x * f3 + this.p2.x * f4;
        final float ny = this.p1.y * f1 + this.c1.y * f2 + this.c2.y * f3 + this.p2.y * f4;
        return new Vector2f(nx, ny);
    }
    
    @Override
    protected void createPoints() {
        final float step = 1.0f / this.segments;
        this.points = new float[(this.segments + 1) * 2];
        for (int i = 0; i < this.segments + 1; ++i) {
            final float t = i * step;
            final Vector2f p = this.pointAt(t);
            this.points[i * 2] = p.x;
            this.points[i * 2 + 1] = p.y;
        }
    }
    
    @Override
    public Shape transform(final Transform transform) {
        final float[] pts = new float[8];
        final float[] dest = new float[8];
        pts[0] = this.p1.x;
        pts[1] = this.p1.y;
        pts[2] = this.c1.x;
        pts[3] = this.c1.y;
        pts[4] = this.c2.x;
        pts[5] = this.c2.y;
        pts[6] = this.p2.x;
        pts[7] = this.p2.y;
        transform.transform(pts, 0, dest, 0, 4);
        return new Curve(new Vector2f(dest[0], dest[1]), new Vector2f(dest[2], dest[3]), new Vector2f(dest[4], dest[5]), new Vector2f(dest[6], dest[7]));
    }
    
    @Override
    public boolean closed() {
        return false;
    }
}

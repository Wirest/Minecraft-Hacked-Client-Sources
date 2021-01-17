// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.geom;

import java.util.ArrayList;

public class Path extends Shape
{
    private ArrayList localPoints;
    private float cx;
    private float cy;
    private boolean closed;
    private ArrayList holes;
    private ArrayList hole;
    
    public Path(final float sx, final float sy) {
        this.localPoints = new ArrayList();
        this.holes = new ArrayList();
        this.localPoints.add(new float[] { sx, sy });
        this.cx = sx;
        this.cy = sy;
        this.pointsDirty = true;
    }
    
    public void startHole(final float sx, final float sy) {
        this.hole = new ArrayList();
        this.holes.add(this.hole);
    }
    
    public void lineTo(final float x, final float y) {
        if (this.hole != null) {
            this.hole.add(new float[] { x, y });
        }
        else {
            this.localPoints.add(new float[] { x, y });
        }
        this.cx = x;
        this.cy = y;
        this.pointsDirty = true;
    }
    
    public void close() {
        this.closed = true;
    }
    
    public void curveTo(final float x, final float y, final float cx1, final float cy1, final float cx2, final float cy2) {
        this.curveTo(x, y, cx1, cy1, cx2, cy2, 10);
    }
    
    public void curveTo(final float x, final float y, final float cx1, final float cy1, final float cx2, final float cy2, final int segments) {
        if (this.cx == x && this.cy == y) {
            return;
        }
        final Curve curve = new Curve(new Vector2f(this.cx, this.cy), new Vector2f(cx1, cy1), new Vector2f(cx2, cy2), new Vector2f(x, y));
        final float step = 1.0f / segments;
        for (int i = 1; i < segments + 1; ++i) {
            final float t = i * step;
            final Vector2f p = curve.pointAt(t);
            if (this.hole != null) {
                this.hole.add(new float[] { p.x, p.y });
            }
            else {
                this.localPoints.add(new float[] { p.x, p.y });
            }
            this.cx = p.x;
            this.cy = p.y;
        }
        this.pointsDirty = true;
    }
    
    @Override
    protected void createPoints() {
        this.points = new float[this.localPoints.size() * 2];
        for (int i = 0; i < this.localPoints.size(); ++i) {
            final float[] p = this.localPoints.get(i);
            this.points[i * 2] = p[0];
            this.points[i * 2 + 1] = p[1];
        }
    }
    
    @Override
    public Shape transform(final Transform transform) {
        final Path p = new Path(this.cx, this.cy);
        p.localPoints = this.transform(this.localPoints, transform);
        for (int i = 0; i < this.holes.size(); ++i) {
            p.holes.add(this.transform(this.holes.get(i), transform));
        }
        p.closed = this.closed;
        return p;
    }
    
    private ArrayList transform(final ArrayList pts, final Transform t) {
        final float[] in = new float[pts.size() * 2];
        final float[] out = new float[pts.size() * 2];
        for (int i = 0; i < pts.size(); ++i) {
            in[i * 2] = ((float[])pts.get(i))[0];
            in[i * 2 + 1] = ((float[])pts.get(i))[1];
        }
        t.transform(in, 0, out, 0, pts.size());
        final ArrayList outList = new ArrayList();
        for (int j = 0; j < pts.size(); ++j) {
            outList.add(new float[] { out[j * 2], out[j * 2 + 1] });
        }
        return outList;
    }
    
    @Override
    public boolean closed() {
        return this.closed;
    }
}

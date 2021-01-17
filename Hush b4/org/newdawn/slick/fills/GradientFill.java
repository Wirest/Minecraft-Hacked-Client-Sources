// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.fills;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.ShapeFill;

public class GradientFill implements ShapeFill
{
    private Vector2f none;
    private Vector2f start;
    private Vector2f end;
    private Color startCol;
    private Color endCol;
    private boolean local;
    
    public GradientFill(final float sx, final float sy, final Color startCol, final float ex, final float ey, final Color endCol) {
        this(sx, sy, startCol, ex, ey, endCol, false);
    }
    
    public GradientFill(final float sx, final float sy, final Color startCol, final float ex, final float ey, final Color endCol, final boolean local) {
        this(new Vector2f(sx, sy), startCol, new Vector2f(ex, ey), endCol, local);
    }
    
    public GradientFill(final Vector2f start, final Color startCol, final Vector2f end, final Color endCol, final boolean local) {
        this.none = new Vector2f(0.0f, 0.0f);
        this.local = false;
        this.start = new Vector2f(start);
        this.end = new Vector2f(end);
        this.startCol = new Color(startCol);
        this.endCol = new Color(endCol);
        this.local = local;
    }
    
    public GradientFill getInvertedCopy() {
        return new GradientFill(this.start, this.endCol, this.end, this.startCol, this.local);
    }
    
    public void setLocal(final boolean local) {
        this.local = local;
    }
    
    public Vector2f getStart() {
        return this.start;
    }
    
    public Vector2f getEnd() {
        return this.end;
    }
    
    public Color getStartColor() {
        return this.startCol;
    }
    
    public Color getEndColor() {
        return this.endCol;
    }
    
    public void setStart(final float x, final float y) {
        this.setStart(new Vector2f(x, y));
    }
    
    public void setStart(final Vector2f start) {
        this.start = new Vector2f(start);
    }
    
    public void setEnd(final float x, final float y) {
        this.setEnd(new Vector2f(x, y));
    }
    
    public void setEnd(final Vector2f end) {
        this.end = new Vector2f(end);
    }
    
    public void setStartColor(final Color color) {
        this.startCol = new Color(color);
    }
    
    public void setEndColor(final Color color) {
        this.endCol = new Color(color);
    }
    
    @Override
    public Color colorAt(final Shape shape, final float x, final float y) {
        if (this.local) {
            return this.colorAt(x - shape.getCenterX(), y - shape.getCenterY());
        }
        return this.colorAt(x, y);
    }
    
    public Color colorAt(final float x, final float y) {
        final float dx1 = this.end.getX() - this.start.getX();
        final float dy1 = this.end.getY() - this.start.getY();
        final float dx2 = -dy1;
        final float dy2 = dx1;
        final float denom = dy2 * dx1 - dx2 * dy1;
        if (denom == 0.0f) {
            return Color.black;
        }
        float ua = dx2 * (this.start.getY() - y) - dy2 * (this.start.getX() - x);
        ua /= denom;
        float ub = dx1 * (this.start.getY() - y) - dy1 * (this.start.getX() - x);
        ub /= denom;
        float u = ua;
        if (u < 0.0f) {
            u = 0.0f;
        }
        if (u > 1.0f) {
            u = 1.0f;
        }
        final float v = 1.0f - u;
        final Color col = new Color(1, 1, 1, 1);
        col.r = u * this.endCol.r + v * this.startCol.r;
        col.b = u * this.endCol.b + v * this.startCol.b;
        col.g = u * this.endCol.g + v * this.startCol.g;
        col.a = u * this.endCol.a + v * this.startCol.a;
        return col;
    }
    
    @Override
    public Vector2f getOffsetAt(final Shape shape, final float x, final float y) {
        return this.none;
    }
}

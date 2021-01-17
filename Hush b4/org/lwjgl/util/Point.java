// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util;

import java.io.Serializable;

public final class Point implements ReadablePoint, WritablePoint, Serializable
{
    static final long serialVersionUID = 1L;
    private int x;
    private int y;
    
    public Point() {
    }
    
    public Point(final int x, final int y) {
        this.setLocation(x, y);
    }
    
    public Point(final ReadablePoint p) {
        this.setLocation(p);
    }
    
    public void setLocation(final int x, final int y) {
        this.x = x;
        this.y = y;
    }
    
    public void setLocation(final ReadablePoint p) {
        this.x = p.getX();
        this.y = p.getY();
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public void translate(final int dx, final int dy) {
        this.x += dx;
        this.y += dy;
    }
    
    public void translate(final ReadablePoint p) {
        this.x += p.getX();
        this.y += p.getY();
    }
    
    public void untranslate(final ReadablePoint p) {
        this.x -= p.getX();
        this.y -= p.getY();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Point) {
            final Point pt = (Point)obj;
            return this.x == pt.x && this.y == pt.y;
        }
        return super.equals(obj);
    }
    
    @Override
    public String toString() {
        return this.getClass().getName() + "[x=" + this.x + ",y=" + this.y + "]";
    }
    
    @Override
    public int hashCode() {
        final int sum = this.x + this.y;
        return sum * (sum + 1) / 2 + this.x;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void getLocation(final WritablePoint dest) {
        dest.setLocation(this.x, this.y);
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util;

import java.io.Serializable;

public final class Rectangle implements ReadableRectangle, WritableRectangle, Serializable
{
    static final long serialVersionUID = 1L;
    private int x;
    private int y;
    private int width;
    private int height;
    
    public Rectangle() {
    }
    
    public Rectangle(final int x, final int y, final int w, final int h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }
    
    public Rectangle(final ReadablePoint p, final ReadableDimension d) {
        this.x = p.getX();
        this.y = p.getY();
        this.width = d.getWidth();
        this.height = d.getHeight();
    }
    
    public Rectangle(final ReadableRectangle r) {
        this.x = r.getX();
        this.y = r.getY();
        this.width = r.getWidth();
        this.height = r.getHeight();
    }
    
    public void setLocation(final int x, final int y) {
        this.x = x;
        this.y = y;
    }
    
    public void setLocation(final ReadablePoint p) {
        this.x = p.getX();
        this.y = p.getY();
    }
    
    public void setSize(final int w, final int h) {
        this.width = w;
        this.height = h;
    }
    
    public void setSize(final ReadableDimension d) {
        this.width = d.getWidth();
        this.height = d.getHeight();
    }
    
    public void setBounds(final int x, final int y, final int w, final int h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }
    
    public void setBounds(final ReadablePoint p, final ReadableDimension d) {
        this.x = p.getX();
        this.y = p.getY();
        this.width = d.getWidth();
        this.height = d.getHeight();
    }
    
    public void setBounds(final ReadableRectangle r) {
        this.x = r.getX();
        this.y = r.getY();
        this.width = r.getWidth();
        this.height = r.getHeight();
    }
    
    public void getBounds(final WritableRectangle dest) {
        dest.setBounds(this.x, this.y, this.width, this.height);
    }
    
    public void getLocation(final WritablePoint dest) {
        dest.setLocation(this.x, this.y);
    }
    
    public void getSize(final WritableDimension dest) {
        dest.setSize(this.width, this.height);
    }
    
    public void translate(final int x, final int y) {
        this.x += x;
        this.y += y;
    }
    
    public void translate(final ReadablePoint point) {
        this.x += point.getX();
        this.y += point.getY();
    }
    
    public void untranslate(final ReadablePoint point) {
        this.x -= point.getX();
        this.y -= point.getY();
    }
    
    public boolean contains(final ReadablePoint p) {
        return this.contains(p.getX(), p.getY());
    }
    
    public boolean contains(final int X, final int Y) {
        int w = this.width;
        int h = this.height;
        if ((w | h) < 0) {
            return false;
        }
        final int x = this.x;
        final int y = this.y;
        if (X < x || Y < y) {
            return false;
        }
        w += x;
        h += y;
        return (w < x || w > X) && (h < y || h > Y);
    }
    
    public boolean contains(final ReadableRectangle r) {
        return this.contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }
    
    public boolean contains(final int X, final int Y, int W, int H) {
        int w = this.width;
        int h = this.height;
        if ((w | h | W | H) < 0) {
            return false;
        }
        final int x = this.x;
        final int y = this.y;
        if (X < x || Y < y) {
            return false;
        }
        w += x;
        W += X;
        if (W <= X) {
            if (w >= x || W > w) {
                return false;
            }
        }
        else if (w >= x && W > w) {
            return false;
        }
        h += y;
        H += Y;
        if (H <= Y) {
            if (h >= y || H > h) {
                return false;
            }
        }
        else if (h >= y && H > h) {
            return false;
        }
        return true;
    }
    
    public boolean intersects(final ReadableRectangle r) {
        int tw = this.width;
        int th = this.height;
        int rw = r.getWidth();
        int rh = r.getHeight();
        if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
            return false;
        }
        final int tx = this.x;
        final int ty = this.y;
        final int rx = r.getX();
        final int ry = r.getY();
        rw += rx;
        rh += ry;
        tw += tx;
        th += ty;
        return (rw < rx || rw > tx) && (rh < ry || rh > ty) && (tw < tx || tw > rx) && (th < ty || th > ry);
    }
    
    public Rectangle intersection(final ReadableRectangle r, Rectangle dest) {
        int tx1 = this.x;
        int ty1 = this.y;
        final int rx1 = r.getX();
        final int ry1 = r.getY();
        long tx2 = tx1;
        tx2 += this.width;
        long ty2 = ty1;
        ty2 += this.height;
        long rx2 = rx1;
        rx2 += r.getWidth();
        long ry2 = ry1;
        ry2 += r.getHeight();
        if (tx1 < rx1) {
            tx1 = rx1;
        }
        if (ty1 < ry1) {
            ty1 = ry1;
        }
        if (tx2 > rx2) {
            tx2 = rx2;
        }
        if (ty2 > ry2) {
            ty2 = ry2;
        }
        tx2 -= tx1;
        ty2 -= ty1;
        if (tx2 < -2147483648L) {
            tx2 = -2147483648L;
        }
        if (ty2 < -2147483648L) {
            ty2 = -2147483648L;
        }
        if (dest == null) {
            dest = new Rectangle(tx1, ty1, (int)tx2, (int)ty2);
        }
        else {
            dest.setBounds(tx1, ty1, (int)tx2, (int)ty2);
        }
        return dest;
    }
    
    public WritableRectangle union(final ReadableRectangle r, final WritableRectangle dest) {
        final int x1 = Math.min(this.x, r.getX());
        final int x2 = Math.max(this.x + this.width, r.getX() + r.getWidth());
        final int y1 = Math.min(this.y, r.getY());
        final int y2 = Math.max(this.y + this.height, r.getY() + r.getHeight());
        dest.setBounds(x1, y1, x2 - x1, y2 - y1);
        return dest;
    }
    
    public void add(final int newx, final int newy) {
        final int x1 = Math.min(this.x, newx);
        final int x2 = Math.max(this.x + this.width, newx);
        final int y1 = Math.min(this.y, newy);
        final int y2 = Math.max(this.y + this.height, newy);
        this.x = x1;
        this.y = y1;
        this.width = x2 - x1;
        this.height = y2 - y1;
    }
    
    public void add(final ReadablePoint pt) {
        this.add(pt.getX(), pt.getY());
    }
    
    public void add(final ReadableRectangle r) {
        final int x1 = Math.min(this.x, r.getX());
        final int x2 = Math.max(this.x + this.width, r.getX() + r.getWidth());
        final int y1 = Math.min(this.y, r.getY());
        final int y2 = Math.max(this.y + this.height, r.getY() + r.getHeight());
        this.x = x1;
        this.y = y1;
        this.width = x2 - x1;
        this.height = y2 - y1;
    }
    
    public void grow(final int h, final int v) {
        this.x -= h;
        this.y -= v;
        this.width += h * 2;
        this.height += v * 2;
    }
    
    public boolean isEmpty() {
        return this.width <= 0 || this.height <= 0;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Rectangle) {
            final Rectangle r = (Rectangle)obj;
            return this.x == r.x && this.y == r.y && this.width == r.width && this.height == r.height;
        }
        return super.equals(obj);
    }
    
    @Override
    public String toString() {
        return this.getClass().getName() + "[x=" + this.x + ",y=" + this.y + ",width=" + this.width + ",height=" + this.height + "]";
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public void setHeight(final int height) {
        this.height = height;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
    
    public int getX() {
        return this.x;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
}

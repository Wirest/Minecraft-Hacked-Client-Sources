// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.tiny2d;

public class TinyRect
{
    public int xmin;
    public int ymin;
    public int xmax;
    public int ymax;
    
    public TinyRect() {
        final int n = Integer.MIN_VALUE;
        this.ymax = n;
        this.xmax = n;
        this.ymin = n;
        this.xmin = n;
    }
    
    public TinyRect(final TinyRect tinyRect) {
        this.xmin = tinyRect.xmin;
        this.ymin = tinyRect.ymin;
        this.xmax = tinyRect.xmax;
        this.ymax = tinyRect.ymax;
    }
    
    public TinyRect(final int n, final int n2, final int n3, final int n4) {
        if (n < n3) {
            this.xmin = n;
            this.xmax = n3;
        }
        else {
            this.xmin = n3;
            this.xmax = n;
        }
        if (n2 < n4) {
            this.ymin = n2;
            this.ymax = n4;
        }
        else {
            this.ymin = n4;
            this.ymax = n2;
        }
    }
    
    public TinyRect grow(final int n, final int n2) {
        return new TinyRect(this.xmin - n, this.ymin - n2, this.xmax + n, this.ymax + n2);
    }
    
    public void setEmpty() {
        final int n = Integer.MIN_VALUE;
        this.ymax = n;
        this.ymin = n;
        this.xmax = n;
        this.xmin = n;
    }
    
    public void union(final TinyRect tinyRect) {
        if (tinyRect != null && !tinyRect.isEmpty()) {
            if (this.isEmpty()) {
                this.xmin = tinyRect.xmin;
                this.xmax = tinyRect.xmax;
                this.ymin = tinyRect.ymin;
                this.ymax = tinyRect.ymax;
                return;
            }
            this.xmin = TinyUtil.min(this.xmin, tinyRect.xmin);
            this.xmax = TinyUtil.max(this.xmax, tinyRect.xmax);
            this.ymin = TinyUtil.min(this.ymin, tinyRect.ymin);
            this.ymax = TinyUtil.max(this.ymax, tinyRect.ymax);
        }
    }
    
    public TinyRect intersection(final TinyRect tinyRect) {
        return new TinyRect(TinyUtil.max(this.xmin, tinyRect.xmin), TinyUtil.max(this.ymin, tinyRect.ymin), TinyUtil.min(this.xmax, tinyRect.xmax), TinyUtil.min(this.ymax, tinyRect.ymax));
    }
    
    public void add(final TinyPoint tinyPoint) {
        if (this.isEmpty()) {
            final int x = tinyPoint.x;
            this.xmax = x;
            this.xmin = x;
            final int y = tinyPoint.y;
            this.ymax = y;
            this.ymin = y;
            return;
        }
        if (tinyPoint.x < this.xmin) {
            this.xmin = tinyPoint.x;
        }
        else if (tinyPoint.x > this.xmax) {
            this.xmax = tinyPoint.x;
        }
        if (tinyPoint.y < this.ymin) {
            this.ymin = tinyPoint.y;
            return;
        }
        if (tinyPoint.y > this.ymax) {
            this.ymax = tinyPoint.y;
        }
    }
    
    public boolean isEmpty() {
        return this.xmin == Integer.MIN_VALUE;
    }
    
    public void translate(final int n, final int n2) {
        if (!this.isEmpty()) {
            this.xmin += n;
            this.xmax += n;
            this.ymin += n2;
            this.ymax += n2;
        }
    }
    
    public boolean contains(final TinyPoint tinyPoint) {
        return this.xmin <= tinyPoint.x && tinyPoint.x <= this.xmax && this.ymin <= tinyPoint.y && tinyPoint.y <= this.ymax;
    }
    
    public boolean intersects(final TinyRect tinyRect) {
        return this.xmin <= tinyRect.xmax && tinyRect.xmin <= this.xmax && this.ymin <= tinyRect.ymax && tinyRect.ymin <= this.ymax;
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.tiny2d;

public class TinyPoint
{
    public int x;
    public int y;
    
    public TinyPoint() {
        final int n = 0;
        this.y = n;
        this.x = n;
    }
    
    public TinyPoint(final int x, final int y) {
        this.x = x;
        this.y = y;
    }
    
    public int distance(final TinyPoint tinyPoint) {
        return TinyUtil.a(tinyPoint.x - this.x, tinyPoint.y - this.y);
    }
}

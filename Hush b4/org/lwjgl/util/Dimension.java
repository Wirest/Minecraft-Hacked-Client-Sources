// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util;

import java.io.Serializable;

public final class Dimension implements Serializable, ReadableDimension, WritableDimension
{
    static final long serialVersionUID = 1L;
    private int width;
    private int height;
    
    public Dimension() {
    }
    
    public Dimension(final int w, final int h) {
        this.width = w;
        this.height = h;
    }
    
    public Dimension(final ReadableDimension d) {
        this.setSize(d);
    }
    
    public void setSize(final int w, final int h) {
        this.width = w;
        this.height = h;
    }
    
    public void setSize(final ReadableDimension d) {
        this.width = d.getWidth();
        this.height = d.getHeight();
    }
    
    public void getSize(final WritableDimension dest) {
        dest.setSize(this);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ReadableDimension) {
            final ReadableDimension d = (ReadableDimension)obj;
            return this.width == d.getWidth() && this.height == d.getHeight();
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final int sum = this.width + this.height;
        return sum * (sum + 1) / 2 + this.width;
    }
    
    @Override
    public String toString() {
        return this.getClass().getName() + "[width=" + this.width + ",height=" + this.height + "]";
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
}

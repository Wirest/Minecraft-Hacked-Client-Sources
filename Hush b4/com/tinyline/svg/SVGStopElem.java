// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

import com.tinyline.tiny2d.TinyRect;
import com.tinyline.tiny2d.TinyNumber;

public class SVGStopElem extends SVGNode
{
    int v;
    
    SVGStopElem() {
        this.v = 0;
    }
    
    public SVGStopElem(final SVGStopElem svgStopElem) {
        super(svgStopElem);
        this.v = svgStopElem.v;
    }
    
    public SVGNode copyNode() {
        return new SVGStopElem(this);
    }
    
    public int setAttribute(final int n, final Object o) throws Exception {
        super.outlined = false;
        if (super.parent != null) {
            super.parent.outlined = false;
        }
        switch (n) {
            case 55: {
                this.v = ((TinyNumber)o).val;
                return 0;
            }
            default: {
                return super.setAttribute(n, o);
            }
        }
    }
    
    public Object getAttribute(final int n) {
        final Object o = null;
        switch (n) {
            case 55: {
                final int v = this.v;
                if (o != null) {
                    return o;
                }
                return new TinyNumber(v);
            }
            default: {
                return super.getAttribute(n);
            }
        }
    }
    
    public void paint(final SVGRaster svgRaster) {
    }
    
    public int createOutline() {
        return 0;
    }
    
    public TinyRect getDevBounds(final SVGRaster svgRaster) {
        final TinyRect tinyRect = new TinyRect();
        if (super.parent != null) {
            return super.parent.getDevBounds(svgRaster);
        }
        return tinyRect;
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

import com.tinyline.tiny2d.TinyPath;
import com.tinyline.tiny2d.TinyPoint;
import com.tinyline.tiny2d.TinyVector;

public class SVGPolygonElem extends SVGPathElem
{
    public TinyVector points;
    
    SVGPolygonElem() {
        this.points = new TinyVector(4);
    }
    
    public SVGPolygonElem(final SVGPolygonElem svgPolygonElem) {
        super(svgPolygonElem);
        for (int count = svgPolygonElem.points.count, i = 0; i < count; ++i) {
            final TinyPoint tinyPoint = (TinyPoint)svgPolygonElem.points.data[i];
            this.points.addElement(new TinyPoint(tinyPoint.x, tinyPoint.y));
        }
    }
    
    public SVGNode copyNode() {
        return new SVGPolygonElem(this);
    }
    
    public int setAttribute(final int n, final Object o) throws Exception {
        super.outlined = false;
        switch (n) {
            case 63: {
                this.points = (TinyVector)o;
                return 0;
            }
            default: {
                return super.setAttribute(n, o);
            }
        }
    }
    
    public Object getAttribute(final int n) {
        switch (n) {
            case 63: {
                return this.points;
            }
            default: {
                return super.getAttribute(n);
            }
        }
    }
    
    public int createOutline() {
        super.path = TinyPath.pointsToPath(this.points);
        if (super.helem == 24) {
            super.path.closePath();
        }
        return super.createOutline();
    }
}

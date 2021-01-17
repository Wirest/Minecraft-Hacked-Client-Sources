// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

import com.tinyline.tiny2d.TinyColor;
import com.tinyline.tiny2d.i;
import com.tinyline.tiny2d.TinyPoint;
import com.tinyline.tiny2d.TinyMatrix;
import com.tinyline.tiny2d.TinyRect;
import com.tinyline.tiny2d.TinyPath;

public class SVGPathElem extends SVGNode
{
    public TinyPath path;
    public boolean isAntialiased;
    
    SVGPathElem() {
    }
    
    public SVGPathElem(final SVGPathElem svgPathElem) {
        super(svgPathElem);
        if (svgPathElem.path != null) {
            this.path = new TinyPath(svgPathElem.path);
        }
    }
    
    public SVGNode copyNode() {
        return new SVGPathElem(this);
    }
    
    public void paint(final SVGRaster svgRaster) {
        if (this.isVisible() && this.isDisplay()) {
            if (!super.outlined) {
                this.createOutline();
            }
            if (this.path == null) {
                return;
            }
            final i if1 = svgRaster.if;
            final TinyMatrix globalTransform = this.getGlobalTransform();
            if1.a(globalTransform);
            if1.byte((this.getFillRule() == 36) ? 1 : 2);
            final TinyColor fillColor = this.getFillColor();
            final TinyColor strokeColor = this.getStrokeColor();
            if (fillColor.fillType != 0 || strokeColor.fillType != 0) {
                TinyMatrix globalTransform2 = globalTransform;
                if (super.parent.helem == 32) {
                    globalTransform2 = super.parent.getGlobalTransform();
                }
                final TinyRect tinyRect = new TinyRect(super.bounds);
                if (super.parent.helem == 32) {
                    final TinyMatrix tinyMatrix = new TinyMatrix(super.transform.matrix);
                    final TinyPoint tinyPoint = new TinyPoint(tinyRect.xmin, tinyRect.ymin);
                    final TinyPoint tinyPoint2 = new TinyPoint(tinyRect.xmax, tinyRect.ymax);
                    tinyMatrix.transform(tinyPoint);
                    tinyMatrix.transform(tinyPoint2);
                    tinyRect.xmin = tinyPoint.x;
                    tinyRect.ymin = tinyPoint2.y;
                    tinyRect.xmax = tinyPoint2.x;
                    tinyRect.ymax = tinyPoint.y;
                }
                if (fillColor.fillType != 0) {
                    fillColor.setCoords(globalTransform2, tinyRect);
                }
                if (strokeColor.fillType != 0) {
                    strokeColor.setCoords(globalTransform2, tinyRect);
                }
            }
            if1.a(fillColor);
            if1.if(this.getFillOpacity());
            if1.if(strokeColor);
            if1.do(this.getStrokeOpacity());
            final int[] dashArray = this.getDashArray();
            if1.a((int[])((dashArray != SVG.VAL_STROKEDASHARRAYNONE) ? dashArray : null));
            if1.for(this.getDashOffset());
            if1.case(this.getLineThickness());
            final int capStyle = this.getCapStyle();
            if (capStyle == 48) {
                if1.int(2);
            }
            else if (capStyle == 53) {
                if1.int(3);
            }
            else {
                if1.int(1);
            }
            final int joinStyle = this.getJoinStyle();
            if (joinStyle == 48) {
                if1.try(2);
            }
            else if (joinStyle == 12) {
                if1.try(3);
            }
            else {
                if1.try(1);
            }
            if1.new(this.getMiterLimit());
            if1.a(this.getOpacity());
            if1.a(svgRaster.isAntialiased || this.isAntialiased);
            if1.a(svgRaster.clipRect.intersection(svgRaster.viewport));
            if1.do(this.getDevBounds(svgRaster));
            if1.a(this.path);
        }
    }
    
    public int createOutline() {
        if (this.path == null) {
            return 2;
        }
        super.bounds = this.path.getBBox();
        super.outlined = true;
        return 0;
    }
    
    public int setAttribute(final int n, final Object o) throws Exception {
        super.outlined = false;
        switch (n) {
            case 20: {
                this.path = (TinyPath)o;
                return 0;
            }
            default: {
                return super.setAttribute(n, o);
            }
        }
    }
    
    public Object getAttribute(final int n) {
        switch (n) {
            case 20: {
                return this.path;
            }
            default: {
                return super.getAttribute(n);
            }
        }
    }
}

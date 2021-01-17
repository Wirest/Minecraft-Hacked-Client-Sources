// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

import com.tinyline.tiny2d.TinyRect;
import com.tinyline.tiny2d.TinyNumber;
import com.tinyline.tiny2d.TinyMatrix;
import com.tinyline.tiny2d.TinyString;
import com.tinyline.tiny2d.TinyTransform;
import com.tinyline.tiny2d.TinyColor;

public class SVGGradientElem extends SVGNode
{
    public TinyColor gcolor;
    public TinyTransform gradientTransform;
    public int spreadMethod;
    public int gradientUnits;
    public TinyString xlink_href;
    
    SVGGradientElem() {
        this.gcolor = new TinyColor(1, new TinyMatrix());
        this.gradientTransform = new TinyTransform();
    }
    
    public void setDefaults() {
        this.spreadMethod = 41;
        this.gradientUnits = 38;
        if (super.helem == 19) {
            final TinyColor gcolor = this.gcolor;
            final TinyColor gcolor2 = this.gcolor;
            final TinyColor gcolor3 = this.gcolor;
            final int x1 = 0;
            gcolor3.y2 = x1;
            gcolor2.y1 = x1;
            gcolor.x1 = x1;
            this.gcolor.x2 = 256;
        }
        else {
            final TinyColor gcolor4 = this.gcolor;
            final TinyColor gcolor5 = this.gcolor;
            final TinyColor gcolor6 = this.gcolor;
            final int x2 = 128;
            gcolor6.r = x2;
            gcolor5.y1 = x2;
            gcolor4.x1 = x2;
        }
    }
    
    public SVGGradientElem(final SVGGradientElem svgGradientElem) {
        super(svgGradientElem);
        this.gcolor = new TinyColor(svgGradientElem.gcolor);
        this.gradientTransform = new TinyTransform(svgGradientElem.gradientTransform);
        this.spreadMethod = svgGradientElem.spreadMethod;
        this.gradientUnits = svgGradientElem.gradientUnits;
        if (svgGradientElem.xlink_href != null) {
            this.xlink_href = new TinyString(svgGradientElem.xlink_href.data);
        }
    }
    
    public SVGNode copyNode() {
        return new SVGGradientElem(this);
    }
    
    public int setAttribute(final int n, final Object o) throws Exception {
        super.outlined = false;
        switch (n) {
            case 18:
            case 111: {
                this.gcolor.x1 = ((TinyNumber)o).val;
                break;
            }
            case 19:
            case 124: {
                this.gcolor.y1 = ((TinyNumber)o).val;
                break;
            }
            case 112: {
                this.gcolor.x2 = ((TinyNumber)o).val;
                break;
            }
            case 125: {
                this.gcolor.y2 = ((TinyNumber)o).val;
                break;
            }
            case 65: {
                this.gcolor.r = ((TinyNumber)o).val;
                break;
            }
            case 38: {
                this.gradientTransform = (TinyTransform)o;
                break;
            }
            case 75: {
                this.spreadMethod = ((TinyNumber)o).val;
                break;
            }
            case 39: {
                this.gradientUnits = ((TinyNumber)o).val;
                break;
            }
            case 115: {
                this.xlink_href = (TinyString)o;
                break;
            }
            default: {
                return super.setAttribute(n, o);
            }
        }
        return 0;
    }
    
    public Object getAttribute(final int n) {
        int n2 = 0;
        Object o = null;
        switch (n) {
            case 18:
            case 111: {
                n2 = this.gcolor.x1;
                break;
            }
            case 19:
            case 124: {
                n2 = this.gcolor.y1;
                break;
            }
            case 112: {
                n2 = this.gcolor.x2;
                break;
            }
            case 125: {
                n2 = this.gcolor.y2;
                break;
            }
            case 65: {
                n2 = this.gcolor.r;
                break;
            }
            case 38: {
                o = this.gradientTransform;
                break;
            }
            case 75: {
                n2 = this.spreadMethod;
                break;
            }
            case 39: {
                n2 = this.gradientUnits;
                break;
            }
            case 115: {
                o = this.xlink_href;
                break;
            }
            default: {
                return super.getAttribute(n);
            }
        }
        if (o != null) {
            return o;
        }
        return new TinyNumber(n2);
    }
    
    public void paint(final SVGRaster svgRaster) {
    }
    
    public int createOutline() {
        if (super.helem == 19) {
            this.gcolor.fillType = 1;
        }
        else {
            this.gcolor.fillType = 2;
        }
        switch (this.spreadMethod) {
            case 43: {
                this.gcolor.spread = 1;
                break;
            }
            case 45: {
                this.gcolor.spread = 2;
                break;
            }
            default: {
                this.gcolor.spread = 0;
                break;
            }
        }
        if (this.gradientUnits == 38) {
            this.gcolor.units = 1;
        }
        else {
            this.gcolor.units = 0;
        }
        this.gcolor.matrix = this.gradientTransform.matrix;
        final int count = super.children.count;
        this.gcolor.gStops.count = 0;
        for (int i = 0; i < count; ++i) {
            final SVGNode svgNode = (SVGNode)super.children.data[i];
            if (svgNode != null && svgNode.helem == 29) {
                final SVGStopElem svgStopElem = (SVGStopElem)svgNode;
                this.gcolor.addStop((svgStopElem.getStopOpacity() << 24) + (svgStopElem.getStopColor().value & 0xFFFFFF), svgStopElem.v);
            }
        }
        this.gcolor.createColorRamp();
        super.outlined = true;
        return 0;
    }
    
    public TinyRect getDevBounds(final SVGRaster svgRaster) {
        final TinyRect tinyRect = new TinyRect();
        a(super.ownerDocument.root, super.id, svgRaster, tinyRect);
        return tinyRect;
    }
    
    private static void a(final SVGNode svgNode, final TinyString tinyString, final SVGRaster svgRaster, final TinyRect tinyRect) {
        if ((svgNode.fill.fillType == 5 && svgNode.fill.uri != null && svgNode.fill.uri.equals(tinyString)) || (svgNode.stroke.fillType == 5 && svgNode.stroke.uri != null && svgNode.stroke.uri.equals(tinyString))) {
            tinyRect.union(svgNode.getDevBounds(svgRaster));
            return;
        }
        for (int i = 0; i < svgNode.children.count; ++i) {
            final SVGNode svgNode2 = (SVGNode)svgNode.children.data[i];
            if (svgNode2 != null) {
                a(svgNode2, tinyString, svgRaster, tinyRect);
            }
        }
    }
}

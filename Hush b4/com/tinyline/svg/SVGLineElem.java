// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

import com.tinyline.tiny2d.TinyColor;
import com.tinyline.tiny2d.TinyPath;
import com.tinyline.tiny2d.TinyNumber;

public class SVGLineElem extends SVGPathElem
{
    public int x1;
    public int y1;
    public int x2;
    public int y2;
    
    SVGLineElem() {
    }
    
    public SVGLineElem(final SVGLineElem svgLineElem) {
        super(svgLineElem);
        this.x1 = svgLineElem.x1;
        this.y1 = svgLineElem.y1;
        this.x2 = svgLineElem.x2;
        this.y2 = svgLineElem.y2;
    }
    
    public SVGNode copyNode() {
        return new SVGLineElem(this);
    }
    
    public int setAttribute(final int n, final Object o) throws Exception {
        super.outlined = false;
        switch (n) {
            case 111: {
                this.x1 = ((TinyNumber)o).val;
                break;
            }
            case 124: {
                this.y1 = ((TinyNumber)o).val;
                break;
            }
            case 112: {
                this.x2 = ((TinyNumber)o).val;
                break;
            }
            case 125: {
                this.y2 = ((TinyNumber)o).val;
                break;
            }
            default: {
                return super.setAttribute(n, o);
            }
        }
        return 0;
    }
    
    public Object getAttribute(final int n) {
        final Object o = null;
        int n2 = 0;
        switch (n) {
            case 111: {
                n2 = this.x1;
                break;
            }
            case 124: {
                n2 = this.y1;
                break;
            }
            case 112: {
                n2 = this.x2;
                break;
            }
            case 125: {
                n2 = this.y2;
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
    
    public int createOutline() {
        super.path = TinyPath.lineToPath(this.x1, this.y1, this.x2, this.y2);
        super.fill = TinyColor.NONE;
        return super.createOutline();
    }
}

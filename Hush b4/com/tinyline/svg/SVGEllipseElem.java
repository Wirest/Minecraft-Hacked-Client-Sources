// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

import com.tinyline.tiny2d.TinyPath;
import com.tinyline.tiny2d.TinyNumber;

public class SVGEllipseElem extends SVGPathElem
{
    public int cx;
    public int cy;
    public int rx;
    public int ry;
    
    SVGEllipseElem() {
    }
    
    public SVGEllipseElem(final SVGEllipseElem svgEllipseElem) {
        super(svgEllipseElem);
        this.cx = svgEllipseElem.cx;
        this.cy = svgEllipseElem.cy;
        this.rx = svgEllipseElem.rx;
        this.ry = svgEllipseElem.ry;
    }
    
    public SVGNode copyNode() {
        return new SVGEllipseElem(this);
    }
    
    public int setAttribute(final int n, final Object o) throws Exception {
        super.outlined = false;
        switch (n) {
            case 18: {
                this.cx = ((TinyNumber)o).val;
                break;
            }
            case 19: {
                this.cy = ((TinyNumber)o).val;
                break;
            }
            case 65: {
                final int val = ((TinyNumber)o).val;
                this.ry = val;
                this.rx = val;
                break;
            }
            case 72: {
                this.rx = ((TinyNumber)o).val;
                break;
            }
            case 73: {
                this.ry = ((TinyNumber)o).val;
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
            case 18: {
                n2 = this.cx;
                break;
            }
            case 19: {
                n2 = this.cy;
                break;
            }
            case 65: {
                n2 = this.rx;
                break;
            }
            case 72: {
                n2 = this.rx;
                break;
            }
            case 73: {
                n2 = this.ry;
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
        super.path = TinyPath.ovalToPath(this.cx - this.rx, this.cy - this.ry, 2 * this.rx, 2 * this.ry);
        super.fillRule = 2;
        return super.createOutline();
    }
}

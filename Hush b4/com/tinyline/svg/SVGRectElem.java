// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

import com.tinyline.tiny2d.TinyPath;
import com.tinyline.tiny2d.TinyNumber;

public class SVGRectElem extends SVGPathElem
{
    public int rx;
    public int ry;
    public int x;
    public int y;
    public int width;
    public int height;
    
    public SVGRectElem() {
    }
    
    public SVGRectElem(final SVGRectElem svgRectElem) {
        super(svgRectElem);
        this.rx = svgRectElem.rx;
        this.ry = svgRectElem.ry;
        this.x = svgRectElem.x;
        this.y = svgRectElem.y;
        this.width = svgRectElem.width;
        this.height = svgRectElem.height;
    }
    
    public SVGNode copyNode() {
        return new SVGRectElem(this);
    }
    
    public int setAttribute(final int n, final Object o) throws Exception {
        super.outlined = false;
        switch (n) {
            case 109: {
                this.x = ((TinyNumber)o).val;
                break;
            }
            case 123: {
                this.y = ((TinyNumber)o).val;
                break;
            }
            case 107: {
                this.width = ((TinyNumber)o).val;
                break;
            }
            case 41: {
                this.height = ((TinyNumber)o).val;
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
            case 109: {
                n2 = this.x;
                break;
            }
            case 123: {
                n2 = this.y;
                break;
            }
            case 107: {
                n2 = this.width;
                break;
            }
            case 41: {
                n2 = this.height;
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
        if (this.rx == 0) {
            super.path = TinyPath.rectToPath(this.x, this.y, this.x + this.width, this.y + this.height);
        }
        else {
            super.path = TinyPath.roundRectToPath(this.x, this.y, this.width, this.height, this.rx, this.ry);
        }
        super.fillRule = 2;
        return super.createOutline();
    }
}

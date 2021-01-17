// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

import com.tinyline.tiny2d.TinyNumber;
import com.tinyline.tiny2d.TinyString;

public class SVGMPathElem extends SVGNode
{
    public TinyString xlink_href;
    
    public SVGMPathElem() {
    }
    
    public SVGMPathElem(final SVGMPathElem svgmPathElem) {
        super(svgmPathElem);
        if (svgmPathElem.xlink_href != null) {
            this.xlink_href = new TinyString(svgmPathElem.xlink_href.data);
        }
    }
    
    public SVGNode copyNode() {
        return new SVGMPathElem(this);
    }
    
    public int setAttribute(final int n, final Object o) throws Exception {
        super.outlined = false;
        switch (n) {
            case 115: {
                this.xlink_href = (TinyString)o;
                return 0;
            }
            default: {
                return super.setAttribute(n, o);
            }
        }
    }
    
    public Object getAttribute(final int n) {
        final int n2 = 0;
        switch (n) {
            case 115: {
                final TinyString xlink_href = this.xlink_href;
                if (xlink_href != null) {
                    return xlink_href;
                }
                return new TinyNumber(n2);
            }
            default: {
                return super.getAttribute(n);
            }
        }
    }
    
    public void paint(final SVGRaster svgRaster) {
    }
    
    public int createOutline() {
        super.outlined = true;
        return 0;
    }
}

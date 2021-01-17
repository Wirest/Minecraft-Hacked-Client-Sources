// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

public class SVGUnknownElem extends SVGNode
{
    public SVGUnknownElem() {
        super.helem = 35;
    }
    
    public void paint(final SVGRaster svgRaster) {
    }
    
    public SVGNode copyNode() {
        return new SVGUnknownElem();
    }
    
    public int createOutline() {
        super.outlined = true;
        return 0;
    }
}

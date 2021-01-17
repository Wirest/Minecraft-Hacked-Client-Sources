// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

public class SVGRect
{
    public int x;
    public int y;
    public int width;
    public int height;
    
    public SVGRect() {
    }
    
    public SVGRect(final SVGRect svgRect) {
        this.x = svgRect.x;
        this.y = svgRect.y;
        this.width = svgRect.width;
        this.height = svgRect.height;
    }
}

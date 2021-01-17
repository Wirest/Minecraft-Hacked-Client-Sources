// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

import com.tinyline.tiny2d.TinyPath;
import com.tinyline.tiny2d.TinyNumber;
import com.tinyline.tiny2d.TinyGlyph;

public class SVGGlyphElem extends SVGNode
{
    public TinyGlyph tglyph;
    
    SVGGlyphElem() {
        this.tglyph = new TinyGlyph();
    }
    
    public SVGGlyphElem(final SVGGlyphElem svgGlyphElem) {
        super(svgGlyphElem);
        this.tglyph = new TinyGlyph();
        this.tglyph.unicode = svgGlyphElem.tglyph.unicode;
        this.tglyph.horizAdvX = svgGlyphElem.tglyph.horizAdvX;
        this.tglyph.path = svgGlyphElem.tglyph.path;
    }
    
    public int setAttribute(final int n, final Object o) throws Exception {
        super.outlined = false;
        switch (n) {
            case 42: {
                this.tglyph.horizAdvX = ((TinyNumber)o).val;
                break;
            }
            case 100: {
                this.tglyph.unicode = ((TinyNumber)o).val;
                break;
            }
            case 20: {
                this.tglyph.path = (TinyPath)o;
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
        Object path = null;
        switch (n) {
            case 42: {
                n2 = this.tglyph.horizAdvX;
                break;
            }
            case 100: {
                n2 = this.tglyph.unicode;
                break;
            }
            case 20: {
                path = this.tglyph.path;
                break;
            }
            default: {
                return super.getAttribute(n);
            }
        }
        if (path != null) {
            return path;
        }
        return new TinyNumber(n2);
    }
    
    public SVGNode copyNode() {
        return new SVGGlyphElem(this);
    }
    
    public void paint(final SVGRaster svgRaster) {
    }
    
    public int createOutline() {
        return 0;
    }
}

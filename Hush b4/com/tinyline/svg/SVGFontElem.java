// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

import com.tinyline.tiny2d.TinyGlyph;
import com.tinyline.tiny2d.TinyNumber;
import com.tinyline.tiny2d.TinyFont;

public class SVGFontElem extends SVGNode
{
    SVGFontFaceElem else;
    TinyFont char;
    
    SVGFontElem() {
        this.char = new TinyFont();
    }
    
    public SVGFontElem(final SVGFontElem svgFontElem) {
        super(svgFontElem);
        this.else = new SVGFontFaceElem(svgFontElem.else);
        this.char = svgFontElem.char;
    }
    
    public SVGNode copyNode() {
        return new SVGFontElem(this);
    }
    
    public int init() {
        for (int i = 0; i < super.children.count; ++i) {
            final SVGNode svgNode = (SVGNode)super.children.data[i];
            if (svgNode != null) {
                if (svgNode.helem == 10) {
                    this.else = (SVGFontFaceElem)svgNode;
                    this.char.fontFamily = this.else.fontFamily;
                    this.char.unitsPerEm = this.else.K;
                    this.char.ascent = this.else.M;
                    this.char.baseline = this.else.L;
                }
                else if (svgNode.helem == 21) {
                    this.char.missing_glyph = ((SVGGlyphElem)svgNode).tglyph;
                }
                else if (svgNode.helem == 15) {
                    final TinyGlyph tglyph = ((SVGGlyphElem)svgNode).tglyph;
                    this.char.glyphs.put(new TinyNumber(tglyph.unicode), tglyph);
                }
            }
        }
        return 0;
    }
    
    public int setAttribute(final int n, final Object o) throws Exception {
        super.outlined = false;
        switch (n) {
            case 42: {
                this.char.horizAdvX = ((TinyNumber)o).val;
                return 0;
            }
            default: {
                return super.setAttribute(n, o);
            }
        }
    }
    
    public Object getAttribute(final int n) {
        final Object o = null;
        switch (n) {
            case 42: {
                final int horizAdvX = this.char.horizAdvX;
                if (o != null) {
                    return o;
                }
                return new TinyNumber(horizAdvX);
            }
            default: {
                return super.getAttribute(n);
            }
        }
    }
    
    public void paint(final SVGRaster svgRaster) {
    }
    
    public int createOutline() {
        return 0;
    }
}
